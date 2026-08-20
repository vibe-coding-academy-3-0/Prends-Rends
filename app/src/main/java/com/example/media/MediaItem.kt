package com.example.media

import java.util.UUID

enum class MediaType {
    PHOTO,
    VIDEO,
    AUDIO
}

data class MediaItem(
    val id: String = UUID.randomUUID().toString(),
    val filePath: String,
    val type: MediaType,
    val durationMs: Long = 0L,
    val fileName: String? = null
)
