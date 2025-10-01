package us.docbee.docbeeapp.utils.ui.permissions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import org.koin.core.context.GlobalContext

class AndroidPermissionSettingsManager(private val context: Context) : PermissionSettingsManager {
    override fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}

actual fun providePermissionSettingsManager(): PermissionSettingsManager =
    AndroidPermissionSettingsManager(GlobalContext.get().get())