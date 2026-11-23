package com.harshal.androidapp

data class SmsMessage(
    val id: Long,
    val sender: String,
    val body: String,
    val timestamp: Long,
)
