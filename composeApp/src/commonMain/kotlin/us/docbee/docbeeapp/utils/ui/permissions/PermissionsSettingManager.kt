package us.docbee.docbeeapp.utils.ui.permissions

interface PermissionSettingsManager {
    fun openAppSettings()
}

expect fun providePermissionSettingsManager(): PermissionSettingsManager
