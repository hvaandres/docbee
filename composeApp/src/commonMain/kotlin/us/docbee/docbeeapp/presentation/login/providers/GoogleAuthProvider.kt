package us.docbee.docbeeapp.presentation.login.providers

interface GoogleAuthProvider {
    suspend fun getGoogleIdToken(): String
}

expect fun getGoogleAuthProvider(): GoogleAuthProvider