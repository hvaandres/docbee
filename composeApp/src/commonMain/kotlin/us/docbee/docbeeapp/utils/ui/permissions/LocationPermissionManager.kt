package us.docbee.docbeeapp.utils.ui.permissions

sealed class PermissionResult {
    data object Init: PermissionResult()
    data object Granted: PermissionResult()
    data object Denied: PermissionResult()
    data object Rational: PermissionResult()
}

interface LocationPermissionManager {
    fun isPermissionGranted() : Boolean
    fun requestPermission(callback: (PermissionResult) -> Unit)
}

expect fun provideLocationPermissionManager(): LocationPermissionManager