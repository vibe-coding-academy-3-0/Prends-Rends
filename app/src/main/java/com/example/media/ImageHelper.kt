package com.example.media

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object ImageHelper {

    fun createImageUri(context: Context): Pair<Uri, File> {
        val picturesDir = File(context.filesDir, "loan_photos").apply {
            if (!exists()) mkdirs()
        }
        val photoFile = File(picturesDir, "photo_${System.currentTimeMillis()}.jpg")
        val authority = "${context.packageName}.fileprovider"
        val photoUri = FileProvider.getUriForFile(context, authority, photoFile)
        return Pair(photoUri, photoFile)
    }

    fun saveBitmapToFile(context: Context, bitmap: android.graphics.Bitmap): String? {
        return try {
            val picturesDir = File(context.filesDir, "loan_photos").apply {
                if (!exists()) mkdirs()
            }
            val photoFile = File(picturesDir, "photo_${System.currentTimeMillis()}.jpg")
            java.io.FileOutputStream(photoFile).use { out ->
                bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, out)
            }
            photoFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
