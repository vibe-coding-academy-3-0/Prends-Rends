package com.example.data

import androidx.room.TypeConverter
import com.example.media.MediaItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val mediaListType = Types.newParameterizedType(List::class.java, MediaItem::class.java)
    private val mediaListAdapter = moshi.adapter<List<MediaItem>>(mediaListType)

    @TypeConverter
    fun fromLoanType(type: LoanType): String = type.name

    @TypeConverter
    fun toLoanType(value: String): LoanType = try {
        LoanType.valueOf(value)
    } catch (e: Exception) {
        LoanType.LENT
    }

    @TypeConverter
    fun fromMediaList(list: List<MediaItem>?): String {
        return if (list == null) "[]" else mediaListAdapter.toJson(list)
    }

    @TypeConverter
    fun toMediaList(value: String?): List<MediaItem> {
        if (value.isNullOrBlank()) return emptyList()
        return try {
            mediaListAdapter.fromJson(value) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}

