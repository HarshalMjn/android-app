package com.harshal.androidapp

import android.content.ContentResolver
import android.provider.Telephony

/** Reads text messages from the phone's SMS inbox. Requires the READ_SMS permission. */
class SmsRepository(private val resolver: ContentResolver) {

    fun loadInbox(limit: Int = 500): List<SmsMessage> {
        val projection = arrayOf(
            Telephony.Sms._ID,
            Telephony.Sms.ADDRESS,
            Telephony.Sms.BODY,
            Telephony.Sms.DATE,
        )
        val messages = mutableListOf<SmsMessage>()
        resolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            projection,
            null,
            null,
            "${Telephony.Sms.DATE} DESC",
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(Telephony.Sms._ID)
            val addressCol = cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)
            val bodyCol = cursor.getColumnIndexOrThrow(Telephony.Sms.BODY)
            val dateCol = cursor.getColumnIndexOrThrow(Telephony.Sms.DATE)
            while (cursor.moveToNext() && messages.size < limit) {
                messages += SmsMessage(
                    id = cursor.getLong(idCol),
                    sender = cursor.getString(addressCol).orEmpty(),
                    body = cursor.getString(bodyCol).orEmpty(),
                    timestamp = cursor.getLong(dateCol),
                )
            }
        }
        return messages
    }
}
