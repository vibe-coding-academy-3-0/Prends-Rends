package com.example.media

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import java.io.File
import java.io.IOException

class AudioRecorderHelper(private val context: Context) {

    private var mediaRecorder: MediaRecorder? = null
    var isRecording = false
        private set
    var currentOutputFile: File? = null
        private set

    fun startRecording(): File? {
        if (isRecording) stopRecording()

        val audioDir = File(context.filesDir, "audio_notes").apply {
            if (!exists()) mkdirs()
        }
        val outputFile = File(audioDir, "note_${System.currentTimeMillis()}.m4a")
        currentOutputFile = outputFile

        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }.apply {
            try {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)
                setOutputFile(outputFile.absolutePath)
                prepare()
                start()
                isRecording = true
                Log.d("AudioRecorderHelper", "Started recording to ${outputFile.absolutePath}")
            } catch (e: IOException) {
                Log.e("AudioRecorderHelper", "Recording prepare/start failed: ${e.message}")
                releaseRecorder()
                return null
            } catch (e: Exception) {
                Log.e("AudioRecorderHelper", "Recording failed: ${e.message}")
                releaseRecorder()
                return null
            }
        }
        return outputFile
    }

    fun stopRecording(): String? {
        if (!isRecording) return currentOutputFile?.absolutePath

        return try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            isRecording = false
            Log.d("AudioRecorderHelper", "Stopped recording: ${currentOutputFile?.absolutePath}")
            currentOutputFile?.absolutePath
        } catch (e: Exception) {
            Log.e("AudioRecorderHelper", "Error stopping recorder: ${e.message}")
            currentOutputFile?.delete()
            null
        } finally {
            mediaRecorder = null
            isRecording = false
        }
    }

    fun cancelRecording() {
        if (isRecording) {
            try {
                mediaRecorder?.stop()
            } catch (ignored: Exception) {}
            releaseRecorder()
        }
        currentOutputFile?.delete()
        currentOutputFile = null
    }

    private fun releaseRecorder() {
        try {
            mediaRecorder?.release()
        } catch (ignored: Exception) {}
        mediaRecorder = null
        isRecording = false
    }
}
