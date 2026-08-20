package com.example.media

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log

data class ContactInfo(
    val name: String,
    val phone: String? = null,
    val email: String? = null
)

object ContactHelper {

    fun getContactFromUri(context: Context, contactUri: Uri): ContactInfo? {
        val resolver = context.contentResolver
        var name: String? = null
        var phone: String? = null
        var contactId: String? = null

        try {
            // First query contact details
            val cursor: Cursor? = resolver.query(
                contactUri,
                null, null, null, null
            )

            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                    if (nameIndex >= 0) {
                        name = it.getString(nameIndex)
                    }

                    val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
                    if (idIndex >= 0) {
                        contactId = it.getString(idIndex)
                    }

                    val hasPhoneIndex = it.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)
                    val hasPhone = if (hasPhoneIndex >= 0) it.getInt(hasPhoneIndex) else 0

                    if (hasPhone > 0 && contactId != null) {
                        phone = getPhoneNumber(context, contactId)
                    }
                }
            }

            // Fallback for direct phone content uri
            if (name == null && contactUri.toString().contains("phone")) {
                resolver.query(
                    contactUri,
                    arrayOf(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                    ),
                    null, null, null
                )?.use { pCursor ->
                    if (pCursor.moveToFirst()) {
                        name = pCursor.getString(0)
                        phone = pCursor.getString(1)
                    }
                }
            }

            if (name.isNull_or_blank()) {
                return null
            }

            return ContactInfo(
                name = name!!,
                phone = phone?.replace(" ", "")?.replace("-", "")
            )
        } catch (e: Exception) {
            Log.e("ContactHelper", "Error reading contact details: ${e.message}")
            return null
        }
    }

    private fun String?.isNull_or_blank(): Boolean {
        return this == null || this.trim().isEmpty()
    }

    private fun getPhoneNumber(context: Context, contactId: String?): String? {
        if (contactId == null) return null
        var phone: String? = null
        val resolver = context.contentResolver

        val pCursor = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId),
            null
        )

        pCursor?.use {
            if (it.moveToFirst()) {
                phone = it.getString(0)
            }
        }
        return phone
    }
}
