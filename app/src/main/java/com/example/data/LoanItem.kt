package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loans")
data class LoanItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val type: LoanType, // LENT or BORROWED
    val contactName: String,
    val contactPhone: String? = null,
    val contactEmail: String? = null,
    val valueOrCategory: String? = null, // e.g. "50 €" or "Objet / Outil"
    val photoPath: String? = null,
    val audioPath: String? = null,
    val audioDurationMs: Long = 0,
    val mediaList: List<com.example.media.MediaItem> = emptyList(),
    val dueDate: Long? = null, // Epoch timestamp in ms
    val createdDate: Long = System.currentTimeMillis(),
    val isReturned: Boolean = false,
    val returnedDate: Long? = null,
    val notes: String? = null
) {
    val isOverdue: Boolean
        get() = !isReturned && dueDate != null && dueDate < System.currentTimeMillis()
}
