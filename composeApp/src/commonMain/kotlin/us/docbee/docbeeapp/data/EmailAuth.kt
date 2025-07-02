package us.docbee.docbeeapp.data

interface EmailAuth {
    suspend fun authenticate(email: String, password: String): Boolean
}

expect fun getEmailAuth(): EmailAuth