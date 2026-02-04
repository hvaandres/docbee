package us.docbee.docbeeapp.presentation.login.events

sealed class AuthEvents {
    data object OnGoogleLogin: AuthEvents()
    data object OnAppleLogin: AuthEvents()
    data object OnSuccessNavigation: AuthEvents()
}