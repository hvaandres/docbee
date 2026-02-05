package us.docbee.docbeeapp.presentation.login.providers

sealed class GoogleAuthResult {
    data class Success(val idToken: String): GoogleAuthResult()
    data object Cancelled: GoogleAuthResult()
    data object Error: GoogleAuthResult()
}