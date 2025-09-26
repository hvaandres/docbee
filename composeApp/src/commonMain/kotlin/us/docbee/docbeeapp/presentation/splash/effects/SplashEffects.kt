package us.docbee.docbeeapp.presentation.splash.effects

sealed class SplashEffects {
    data object NavigateToAuthenticate: SplashEffects()
    data object NavigateToDashboard: SplashEffects()
}