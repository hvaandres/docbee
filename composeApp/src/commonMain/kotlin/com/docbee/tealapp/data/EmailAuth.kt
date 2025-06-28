package com.docbee.tealapp.data

interface EmailAuth {
    suspend fun authenticate(email: String, password: String): Boolean
}

expect fun getEmailAuth(): EmailAuth