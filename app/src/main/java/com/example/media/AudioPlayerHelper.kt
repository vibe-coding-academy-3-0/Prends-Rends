package com.example.media

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

sealed class AudioPlayerState {
    object Idle : AudioPlayerState()
    object Loading : AudioPlayerState()
    object Playing : AudioPlayerState()
    object Paused : AudioPlayerState()
    data class Error(val message: String) : AudioPlayerState()
}

class AudioPlayerHelper(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var progressJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _playerState = MutableStateFlow<AudioPlayerState>(AudioPlayerState.Idle)
    val playerState: StateFlow<AudioPlayerState> = _playerState.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0)
    val currentPosition: StateFlow<Int> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0)
    val duration: StateFlow<Int> = _duration.asStateFlow()

    private var currentFilePath: String? = null

    fun playAudio(filePath: String) {
        if (currentFilePath == filePath && mediaPlayer != null) {
            when (_playerState.value) {
                is AudioPlayerState.Playing -> pauseAudio()
                is AudioPlayerState.Paused -> {
                    try {
                        mediaPlayer?.start()
                        _playerState.value = AudioPlayerState.Playing
                        _isPlaying.value = true
                        startProgressTracker()
                    } catch (e: Exception) {
                        _playerState.value = AudioPlayerState.Error("Erreur de lecture audio")
                        _isPlaying.value = false
                    }
                }
                else -> playFromBeginning(filePath)
            }
            return
        }

        playFromBeginning(filePath)
    }

    private fun playFromBeginning(filePath: String) {
        stopAudio()
        currentFilePath = filePath
        _playerState.value = AudioPlayerState.Loading

        try {
            val file = File(filePath)
            if (!file.exists()) {
                Log.e("AudioPlayerHelper", "File does not exist: $filePath")
                _playerState.value = AudioPlayerState.Error("Fichier audio introuvable")
                _isPlaying.value = false
                return
            }

            mediaPlayer = MediaPlayer().apply {
                setDataSource(context, Uri.fromFile(file))
                setOnPreparedListener { mp ->
                    _duration.value = mp.duration
                    mp.start()
                    _playerState.value = AudioPlayerState.Playing
                    _isPlaying.value = true
                    startProgressTracker()
                }
                setOnCompletionListener {
                    _playerState.value = AudioPlayerState.Idle
                    _isPlaying.value = false
                    _currentPosition.value = 0
                    stopProgressTracker()
                }
                setOnErrorListener { _, what, extra ->
                    Log.e("AudioPlayerHelper", "MediaPlayer error: what=$what extra=$extra")
                    _playerState.value = AudioPlayerState.Error("Erreur lors de la lecture")
                    _isPlaying.value = false
                    stopProgressTracker()
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            Log.e("AudioPlayerHelper", "Error playing audio: ${e.message}")
            _playerState.value = AudioPlayerState.Error(e.message ?: "Erreur audio")
            _isPlaying.value = false
            stopAudio()
        }
    }

    fun pauseAudio() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _playerState.value = AudioPlayerState.Paused
                _isPlaying.value = false
                stopProgressTracker()
            }
        }
    }

    fun seekTo(positionMs: Int) {
        mediaPlayer?.let {
            it.seekTo(positionMs)
            _currentPosition.value = positionMs
        }
    }

    fun stopAudio() {
        stopProgressTracker()
        try {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
            }
        } catch (ignored: Exception) {}
        mediaPlayer = null
        _playerState.value = AudioPlayerState.Idle
        _isPlaying.value = false
        _currentPosition.value = 0
        _duration.value = 0
        currentFilePath = null
    }

    private fun startProgressTracker() {
        stopProgressTracker()
        progressJob = scope.launch {
            while (_isPlaying.value) {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        _currentPosition.value = it.currentPosition
                    }
                }
                delay(200)
            }
        }
    }

    private fun stopProgressTracker() {
        progressJob?.cancel()
        progressJob = null
    }
}
