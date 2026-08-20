package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.LoanItem
import com.example.data.LoanRepository
import com.example.data.LoanType
import com.example.i18n.AppLanguage
import com.example.media.AudioPlayerHelper
import com.example.media.AudioRecorderHelper
import com.example.media.MediaFileManager
import com.example.media.MediaItem
import com.example.notification.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

enum class StatusFilter {
    ALL,      // Tous
    ACTIVE,   // En cours
    OVERDUE,  // En retard
    RETURNED  // Rendu
}

enum class TabType {
    ALL,      // Tous
    LENT,     // J'ai prêté
    BORROWED  // J'ai emprunté
}

data class DashboardSummary(
    val totalLentCount: Int = 0,
    val totalBorrowedCount: Int = 0,
    val totalOverdueCount: Int = 0,
    val totalReturnedCount: Int = 0
)

class LoanViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LoanRepository
    val recorderHelper: AudioRecorderHelper
    val playerHelper: AudioPlayerHelper
    val mediaFileManager: MediaFileManager

    private val _currentLanguage = MutableStateFlow(AppLanguage.FR)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _currentThemeMode = MutableStateFlow(com.example.ui.theme.ThemeMode.LIGHT)
    val currentThemeMode: StateFlow<com.example.ui.theme.ThemeMode> = _currentThemeMode.asStateFlow()

    private val _selectedTab = MutableStateFlow(TabType.ALL)
    val selectedTab: StateFlow<TabType> = _selectedTab.asStateFlow()

    private val _selectedStatusFilter = MutableStateFlow(StatusFilter.ALL)
    val selectedStatusFilter: StateFlow<StatusFilter> = _selectedStatusFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _allLoans = MutableStateFlow<List<LoanItem>>(emptyList())

    // Audio recording state for form
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _recordedAudioPath = MutableStateFlow<String?>(null)
    val recordedAudioPath: StateFlow<String?> = _recordedAudioPath.asStateFlow()

    private val _recordingDurationSeconds = MutableStateFlow(0)
    val recordingDurationSeconds: StateFlow<Int> = _recordingDurationSeconds.asStateFlow()

    private var recordingTimerJob: kotlinx.coroutines.Job? = null

    init {
        val database = AppDatabase.getDatabase(application)
        repository = LoanRepository(database.loanDao())
        recorderHelper = AudioRecorderHelper(application)
        playerHelper = AudioPlayerHelper(application)
        mediaFileManager = MediaFileManager(application)

        viewModelScope.launch {
            repository.allLoans.collect { list ->
                _allLoans.value = list
            }
        }
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    fun setThemeMode(mode: com.example.ui.theme.ThemeMode) {
        _currentThemeMode.value = mode
    }

    val filteredLoans: StateFlow<List<LoanItem>> = combine(
        _allLoans,
        _selectedTab,
        _selectedStatusFilter,
        _searchQuery
    ) { loans, tab, statusFilter, query ->
        loans.filter { loan ->
            // Tab filter
            val matchesTab = when (tab) {
                TabType.ALL -> true
                TabType.LENT -> loan.type == LoanType.LENT
                TabType.BORROWED -> loan.type == LoanType.BORROWED
            }

            // Status filter
            val matchesStatus = when (statusFilter) {
                StatusFilter.ALL -> true
                StatusFilter.ACTIVE -> !loan.isReturned && !loan.isOverdue
                StatusFilter.OVERDUE -> loan.isOverdue
                StatusFilter.RETURNED -> loan.isReturned
            }

            // Search query
            val matchesSearch = query.isBlank() ||
                    loan.title.contains(query, ignoreCase = true) ||
                    loan.contactName.contains(query, ignoreCase = true) ||
                    (loan.notes?.contains(query, ignoreCase = true) == true) ||
                    (loan.valueOrCategory?.contains(query, ignoreCase = true) == true)

            matchesTab && matchesStatus && matchesSearch
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val dashboardSummary: StateFlow<DashboardSummary> = _allLoans.combine(_allLoans) { loans, _ ->
        val lent = loans.count { it.type == LoanType.LENT && !it.isReturned }
        val borrowed = loans.count { it.type == LoanType.BORROWED && !it.isReturned }
        val overdue = loans.count { it.isOverdue }
        val returned = loans.count { it.isReturned }
        DashboardSummary(
            totalLentCount = lent,
            totalBorrowedCount = borrowed,
            totalOverdueCount = overdue,
            totalReturnedCount = returned
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardSummary()
    )

    fun setSelectedTab(tab: TabType) {
        _selectedTab.value = tab
    }

    fun setSelectedStatusFilter(filter: StatusFilter) {
        _selectedStatusFilter.value = filter
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun getLoanByIdFlow(id: Long): StateFlow<LoanItem?> {
        val result = MutableStateFlow<LoanItem?>(null)
        viewModelScope.launch {
            repository.getLoanByIdFlow(id).collect {
                result.value = it
            }
        }
        return result.asStateFlow()
    }

    // Audio Recording Logic
    fun startRecordingAudio() {
        val file = recorderHelper.startRecording()
        if (file != null) {
            _isRecording.value = true
            _recordingDurationSeconds.value = 0
            startRecordingTimer()
        }
    }

    fun stopRecordingAudio() {
        val path = recorderHelper.stopRecording()
        stopRecordingTimer()
        _isRecording.value = false
        if (path != null) {
            _recordedAudioPath.value = path
        }
    }

    fun cancelRecordingAudio() {
        recorderHelper.cancelRecording()
        stopRecordingTimer()
        _isRecording.value = false
        _recordedAudioPath.value = null
        _recordingDurationSeconds.value = 0
    }

    fun setRecordedAudioPath(path: String?) {
        _recordedAudioPath.value = path
    }

    private fun startRecordingTimer() {
        stopRecordingTimer()
        recordingTimerJob = viewModelScope.launch {
            while (_isRecording.value) {
                kotlinx.coroutines.delay(1000)
                _recordingDurationSeconds.value += 1
            }
        }
    }

    private fun stopRecordingTimer() {
        recordingTimerJob?.cancel()
        recordingTimerJob = null
    }

    // CRUD Logic
    fun saveLoan(
        id: Long = 0,
        title: String,
        type: LoanType,
        contactName: String,
        contactPhone: String?,
        contactEmail: String?,
        valueOrCategory: String?,
        photoPath: String?,
        audioPath: String?,
        audioDurationMs: Long,
        mediaList: List<MediaItem> = emptyList(),
        dueDate: Long?,
        notes: String?,
        onSuccess: (Long) -> Unit = {}
    ) {
        viewModelScope.launch {
            val loan = LoanItem(
                id = id,
                title = title.trim(),
                type = type,
                contactName = contactName.trim(),
                contactPhone = contactPhone?.trim(),
                contactEmail = contactEmail?.trim(),
                valueOrCategory = valueOrCategory?.trim(),
                photoPath = photoPath ?: mediaList.firstOrNull { it.type == com.example.media.MediaType.PHOTO }?.filePath,
                audioPath = audioPath,
                audioDurationMs = audioDurationMs,
                mediaList = mediaList,
                dueDate = dueDate,
                notes = notes?.trim()
            )

            val savedId = if (id == 0L) {
                repository.insertLoan(loan)
            } else {
                repository.updateLoan(loan)
                id
            }

            // Schedule notification reminder if dueDate set
            if (dueDate != null && dueDate > System.currentTimeMillis()) {
                NotificationHelper.scheduleReminder(
                    context = getApplication(),
                    loanId = savedId,
                    title = title,
                    contactName = contactName,
                    isLent = type == LoanType.LENT,
                    triggerAtMillis = dueDate
                )
            } else if (dueDate == null) {
                NotificationHelper.cancelReminder(getApplication(), savedId)
            }

            // Reset recording state
            _recordedAudioPath.value = null
            _recordingDurationSeconds.value = 0

            onSuccess(savedId)
        }
    }

    fun toggleReturnedStatus(loan: LoanItem) {
        viewModelScope.launch {
            val newStatus = !loan.isReturned
            repository.setReturnedStatus(loan.id, newStatus)
            if (newStatus) {
                NotificationHelper.cancelReminder(getApplication(), loan.id)
            } else if (loan.dueDate != null && loan.dueDate > System.currentTimeMillis()) {
                NotificationHelper.scheduleReminder(
                    context = getApplication(),
                    loanId = loan.id,
                    title = loan.title,
                    contactName = loan.contactName,
                    isLent = loan.type == LoanType.LENT,
                    triggerAtMillis = loan.dueDate
                )
            }
        }
    }

    fun deleteLoan(loan: LoanItem, onDeleted: () -> Unit = {}) {
        viewModelScope.launch {
            NotificationHelper.cancelReminder(getApplication(), loan.id)
            // Delete audio file if exists
            loan.audioPath?.let { path ->
                try { File(path).delete() } catch (ignored: Exception) {}
            }
            // Delete photo file if exists
            loan.photoPath?.let { path ->
                try { File(path).delete() } catch (ignored: Exception) {}
            }
            // Delete media attachments
            loan.mediaList.forEach { item ->
                try { File(item.filePath).delete() } catch (ignored: Exception) {}
            }
            repository.deleteLoan(loan)
            onDeleted()
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerHelper.stopAudio()
        recorderHelper.cancelRecording()
    }
}
