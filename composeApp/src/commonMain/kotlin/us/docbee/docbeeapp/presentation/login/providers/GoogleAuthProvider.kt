package us.docbee.docbeeapp.presentation.login.providers

interface GoogleAuthProvider {
    suspend fun getGoogleIdToken(): GoogleAuthResult
}

expect fun getGoogleAuthProvider(): GoogleAuthProvider