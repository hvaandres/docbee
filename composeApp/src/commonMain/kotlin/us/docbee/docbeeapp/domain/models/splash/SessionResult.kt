package us.docbee.docbeeapp.domain.models.splash

sealed class SessionResult {
    data object UserLogged: SessionResult()
    data object UserNoLogged: SessionResult()
}