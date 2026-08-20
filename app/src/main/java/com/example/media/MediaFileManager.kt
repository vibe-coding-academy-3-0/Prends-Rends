package com.example.media

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class MediaFileManager(private val context: Context) {

    val mediaDir: File
        get() = File(context.filesDir, "loan_media").apply {
            if (!exists()) mkdirs()
        }

    val audioDir: File
        get() = File(context.filesDir, "audio_notes").apply {
            if (!exists()) mkdirs()
        }

    fun createCameraImageFile(): Pair<Uri, File> {
        val file = File(mediaDir, "IMG_${System.currentTimeMillis()}.jpg")
        val authority = "${context.packageName}.fileprovider"
        val uri = FileProvider.getUriForFile(context, authority, file)
        return Pair(uri, file)
    }

    fun createCameraVideoFile(): Pair<Uri, File> {
        val file = File(mediaDir, "VID_${System.currentTimeMillis()}.mp4")
        val authority = "${context.packageName}.fileprovider"
        val uri = FileProvider.getUriForFile(context, authority, file)
        return Pair(uri, file)
    }

    fun copyUriToInternalStorage(uri: Uri, isVideo: Boolean = false): MediaItem? {
        return try {
            val extension = if (isVideo) "mp4" else "jpg"
            val type = if (isVideo) MediaType.VIDEO else MediaType.PHOTO
            val fileName = "${if (isVideo) "VID" else "IMG"}_${System.currentTimeMillis()}.$extension"
            val destFile = File(mediaDir, fileName)

            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(destFile)

            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            MediaItem(
                filePath = destFile.absolutePath,
                type = type,
                fileName = fileName
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
