package us.docbee.docbeeapp.presentation.login.providers

interface GoogleAuthProvider {
    suspend fun getGoogleAuthData(): GoogleAuthResult
}

expect fun getGoogleAuthProvider(): GoogleAuthProvider