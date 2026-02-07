package us.docbee.docbeeapp.presentation.login.providers

import us.docbee.docbeeapp.domain.models.login.AuthenticationModel

sealed class GoogleAuthResult {
    data class Success(val user: AuthenticationModel): GoogleAuthResult()
    data object Cancelled: GoogleAuthResult()
    data object Error: GoogleAuthResult()
}