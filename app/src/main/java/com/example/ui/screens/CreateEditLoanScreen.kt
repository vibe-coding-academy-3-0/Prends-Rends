package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.LoanType
import com.example.i18n.AppStrings
import com.example.media.ContactHelper
import com.example.media.MediaItem
import com.example.media.MediaType
import com.example.ui.components.AudioPlayerCard
import com.example.ui.components.LanguageSelector
import com.example.ui.components.ThemeSelector
import com.example.ui.components.MediaGalleryCarousel
import com.example.viewmodel.LoanViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditLoanScreen(
    loanId: Long?,
    viewModel: LoanViewModel,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val context = LocalContext.current
    val currentLang by viewModel.currentLanguage.collectAsState()
    val currentThemeMode by viewModel.currentThemeMode.collectAsState()

    var title by remember { mutableStateOf("") }
    var loanType by remember { mutableStateOf(LoanType.LENT) }
    var contactName by remember { mutableStateOf("") }
    var contactPhone by remember { mutableStateOf("") }
    var valueOrCategory by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val mediaAttachments = remember { mutableStateListOf<MediaItem>() }

    var currentCameraUri by remember { mutableStateOf<Uri?>(null) }
    var currentCameraFile by remember { mutableStateOf<File?>(null) }

    val isRecording by viewModel.isRecording.collectAsState()
    val recordedAudioPath by viewModel.recordedAudioPath.collectAsState()
    val recordingSeconds by viewModel.recordingDurationSeconds.collectAsState()

    var dueDateMillis by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Load existing loan if editing
    LaunchedEffect(loanId) {
        if (loanId != null && loanId != 0L) {
            viewModel.getLoanByIdFlow(loanId).collect { existing ->
                if (existing != null) {
                    title = existing.title
                    loanType = existing.type
                    contactName = existing.contactName
                    contactPhone = existing.contactPhone ?: ""
                    valueOrCategory = existing.valueOrCategory ?: ""
                    notes = existing.notes ?: ""
                    mediaAttachments.clear()
                    mediaAttachments.addAll(existing.mediaList)
                    if (existing.photoPath != null && mediaAttachments.none { it.filePath == existing.photoPath }) {
                        mediaAttachments.add(0, MediaItem(filePath = existing.photoPath, type = MediaType.PHOTO))
                    }
                    viewModel.setRecordedAudioPath(existing.audioPath)
                    dueDateMillis = existing.dueDate
                }
            }
        }
    }

    // Launchers
    // 1. Contact Picker
    val contactPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { uri ->
        if (uri != null) {
            val info = ContactHelper.getContactFromUri(context, uri)
            if (info != null) {
                contactName = info.name
                if (info.phone != null) {
                    contactPhone = info.phone
                }
                Toast.makeText(context, "Contact : ${info.name}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val contactPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                contactPickerLauncher.launch(null)
            } catch (e: Exception) {
                Toast.makeText(context, "Répertoire non disponible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 2. Camera Photo
    val cameraPhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && currentCameraFile != null) {
            val item = MediaItem(
                filePath = currentCameraFile!!.absolutePath,
                type = MediaType.PHOTO,
                fileName = currentCameraFile!!.name
            )
            mediaAttachments.add(item)
            Toast.makeText(context, "Photo capturée !", Toast.LENGTH_SHORT).show()
        }
    }

    // 3. Camera Video
    val cameraVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        if (success && currentCameraFile != null) {
            val item = MediaItem(
                filePath = currentCameraFile!!.absolutePath,
                type = MediaType.VIDEO,
                fileName = currentCameraFile!!.name
            )
            mediaAttachments.add(item)
            Toast.makeText(context, "Vidéo capturée !", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted && currentCameraUri != null) {
            cameraPhotoLauncher.launch(currentCameraUri!!)
        }
    }

    // 4. System Gallery Multi Picker
    val galleryPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        uris.forEach { uri ->
            val mimeType = context.contentResolver.getType(uri)
            val isVideo = mimeType?.startsWith("video") == true
            val copiedItem = viewModel.mediaFileManager.copyUriToInternalStorage(uri, isVideo = isVideo)
            if (copiedItem != null) {
                mediaAttachments.add(copiedItem)
            }
        }
        if (uris.isNotEmpty()) {
            Toast.makeText(context, "${uris.size} média(s) ajouté(s)", Toast.LENGTH_SHORT).show()
        }
    }

    // 5. Audio Recorder Permission
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startRecordingAudio()
        } else {
            Toast.makeText(context, "Permission microphone refusée", Toast.LENGTH_SHORT).show()
        }
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (loanId != null && loanId != 0L) AppStrings.editTitle(currentLang) else AppStrings.createTitle(currentLang),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        ThemeSelector(
                            currentThemeMode = currentThemeMode,
                            onThemeModeSelected = { viewModel.setThemeMode(it) }
                        )
                        LanguageSelector(
                            currentLanguage = currentLang,
                            onLanguageSelected = { viewModel.setLanguage(it) }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Type Selector: J'ai prêté vs J'ai emprunté
            Text(
                text = AppStrings.typeHeader(currentLang),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TypeChoiceCard(
                    text = AppStrings.typeLent(currentLang),
                    subtitle = AppStrings.typeLentSub(currentLang),
                    icon = Icons.Default.NorthEast,
                    isSelected = loanType == LoanType.LENT,
                    activeColor = Color(0xFF10B981),
                    onClick = { loanType = LoanType.LENT },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("type_lent_button")
                )

                TypeChoiceCard(
                    text = AppStrings.typeBorrowed(currentLang),
                    subtitle = AppStrings.typeBorrowedSub(currentLang),
                    icon = Icons.Default.SouthWest,
                    isSelected = loanType == LoanType.BORROWED,
                    activeColor = Color(0xFF0284C7),
                    onClick = { loanType = LoanType.BORROWED },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("type_borrowed_button")
                )
            }

            // Title Input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(AppStrings.titleLabel(currentLang)) },
                placeholder = { Text(AppStrings.titlePlaceholder(currentLang)) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = textFieldColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("loan_title_input")
            )

            // Value / Amount Input
            OutlinedTextField(
                value = valueOrCategory,
                onValueChange = { valueOrCategory = it },
                label = { Text(AppStrings.valueLabel(currentLang)) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = textFieldColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("loan_value_input")
            )

            // Contact Section
            Text(
                text = AppStrings.personHeader(currentLang),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = contactName,
                    onValueChange = { contactName = it },
                    label = { Text(AppStrings.contactNameLabel(currentLang)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = textFieldColors,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("contact_name_input")
                )

                OutlinedButton(
                    onClick = {
                        val permission = Manifest.permission.READ_CONTACTS
                        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                            try {
                                contactPickerLauncher.launch(null)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Repertoire non disponible", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            contactPermissionLauncher.launch(permission)
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    modifier = Modifier
                        .height(56.dp)
                        .testTag("pick_contact_button")
                ) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Pick Contact")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(AppStrings.pickContactBtn(currentLang), fontSize = 12.sp)
                }
            }

            // Phone Number Input
            OutlinedTextField(
                value = contactPhone,
                onValueChange = { contactPhone = it },
                label = { Text(AppStrings.contactPhoneLabel(currentLang)) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = textFieldColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("contact_phone_input")
            )

            // Media Attachments Section (Camera Photo, Camera Video, Gallery)
            Text(
                text = AppStrings.mediaHeader(currentLang),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Take Photo Button
                        OutlinedButton(
                            onClick = {
                                val (uri, file) = viewModel.mediaFileManager.createCameraImageFile()
                                currentCameraUri = uri
                                currentCameraFile = file
                                val permission = Manifest.permission.CAMERA
                                if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                                    cameraPhotoLauncher.launch(uri)
                                } else {
                                    cameraPermissionLauncher.launch(permission)
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("take_photo_btn")
                        ) {
                            Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(AppStrings.takePhotoBtn(currentLang), fontSize = 11.sp, maxLines = 1)
                        }

                        // Take Video Button
                        OutlinedButton(
                            onClick = {
                                val (uri, file) = viewModel.mediaFileManager.createCameraVideoFile()
                                currentCameraUri = uri
                                currentCameraFile = file
                                cameraVideoLauncher.launch(uri)
                            },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("take_video_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(AppStrings.recordVideoBtn(currentLang), fontSize = 11.sp, maxLines = 1)
                        }

                        // Gallery Button
                        OutlinedButton(
                            onClick = {
                                galleryPickerLauncher.launch("*/*")
                            },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("gallery_picker_btn")
                        ) {
                            Icon(imageVector = Icons.Default.PermMedia, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(AppStrings.galleryBtn(currentLang), fontSize = 11.sp, maxLines = 1)
                        }
                    }

                    if (mediaAttachments.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        MediaGalleryCarousel(
                            mediaList = mediaAttachments,
                            isEditable = true,
                            onRemoveMedia = { item -> mediaAttachments.remove(item) }
                        )
                    }
                }
            }

            // Voice Note Recorder Card
            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(AppStrings.voiceNoteHeader(currentLang), fontWeight = FontWeight.SemiBold)
                        }

                        if (!isRecording) {
                            Button(
                                onClick = {
                                    val permission = Manifest.permission.RECORD_AUDIO
                                    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                                        viewModel.startRecordingAudio()
                                    } else {
                                        audioPermissionLauncher.launch(permission)
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (recordedAudioPath == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                                ),
                                modifier = Modifier.testTag("record_audio_button")
                            ) {
                                Icon(imageVector = Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (recordedAudioPath == null) AppStrings.recordBtn(currentLang) else "Ré-enregistrer", fontSize = 12.sp)
                            }
                        } else {
                            Button(
                                onClick = { viewModel.stopRecordingAudio() },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                modifier = Modifier.testTag("stop_record_button")
                            ) {
                                Icon(imageVector = Icons.Default.Stop, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("${AppStrings.stopBtn(currentLang)} (${recordingSeconds}s)", fontSize = 12.sp)
                            }
                        }
                    }

                    if (recordedAudioPath != null && File(recordedAudioPath!!).exists()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AudioPlayerCard(
                                audioPath = recordedAudioPath!!,
                                playerHelper = viewModel.playerHelper,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { viewModel.setRecordedAudioPath(null) }
                            ) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Supprimer note vocale", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }

            // Return Date & Time Section
            Text(
                text = AppStrings.reminderHeader(currentLang),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (dueDateMillis != null) formatDateAndTime(dueDateMillis!!) else "Pas de rappel défini",
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            if (dueDateMillis != null) {
                                Text(
                                    text = "Rappel automatique activé",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Row {
                        if (dueDateMillis != null) {
                            IconButton(onClick = { dueDateMillis = null }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Effacer date")
                            }
                        }
                        Button(
                            onClick = { showDatePicker = true },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("set_return_date_button")
                        ) {
                            Text(if (dueDateMillis == null) AppStrings.setDateBtn(currentLang) else AppStrings.changeDateBtn(currentLang), fontSize = 12.sp)
                        }
                    }
                }
            }

            // Notes Section
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text(AppStrings.notesLabel(currentLang)) },
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(14.dp),
                colors = textFieldColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("loan_notes_input")
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Save Submit Button
            Button(
                onClick = {
                    if (title.isBlank()) {
                        Toast.makeText(context, "Veuillez entrer le nom de l'objet ou le montant", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (contactName.isBlank()) {
                        Toast.makeText(context, "Veuillez indiquer le nom du contact", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    viewModel.saveLoan(
                        id = loanId ?: 0L,
                        title = title,
                        type = loanType,
                        contactName = contactName,
                        contactPhone = contactPhone.ifBlank { null },
                        contactEmail = null,
                        valueOrCategory = valueOrCategory.ifBlank { null },
                        photoPath = mediaAttachments.firstOrNull { it.type == MediaType.PHOTO }?.filePath,
                        audioPath = recordedAudioPath,
                        audioDurationMs = 0L,
                        mediaList = mediaAttachments.toList(),
                        dueDate = dueDateMillis,
                        notes = notes.ifBlank { null },
                        onSuccess = {
                            Toast.makeText(context, "Prêt enregistré avec succès !", Toast.LENGTH_SHORT).show()
                            onSaveSuccess()
                        }
                    )
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_loan_button")
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(AppStrings.saveBtn(currentLang), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // DatePicker Dialog
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = dueDateMillis ?: (System.currentTimeMillis() + 86400000)
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis
                    if (selectedDate != null) {
                        dueDateMillis = selectedDate
                        showDatePicker = false
                        showTimePicker = true
                    }
                }) {
                    Text("Suivant")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(AppStrings.cancelBtn(currentLang))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // TimePicker Dialog
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(initialHour = 10, initialMinute = 0)
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    if (dueDateMillis != null) {
                        val calendar = Calendar.getInstance().apply {
                            timeInMillis = dueDateMillis!!
                            set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                            set(Calendar.MINUTE, timePickerState.minute)
                        }
                        dueDateMillis = calendar.timeInMillis
                    }
                    showTimePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text(AppStrings.cancelBtn(currentLang))
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Sélectionner l'heure du rappel", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
                    TimePicker(state = timePickerState)
                }
            }
        )
    }
}

@Composable
private fun TypeChoiceCard(
    text: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) activeColor else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) activeColor.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) activeColor else activeColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = if (isSelected) Color.White else activeColor,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) activeColor else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = if (isSelected) activeColor.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatDateAndTime(timestampMillis: Long): String {
    val sdf = SimpleDateFormat("EEEE dd MMMM yyyy à HH:mm", Locale.FRENCH)
    return sdf.format(Date(timestampMillis))
}
