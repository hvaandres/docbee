package us.docbee.docbeeapp.utils.ui.permissions

import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString

class IosPermissionSettingsManager : PermissionSettingsManager {
    override fun openAppSettings() {
        val url = NSURL(string = UIApplicationOpenSettingsURLString)
        if (UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(url, emptyMap<Any?, Any>(), null)
        }
    }
}

actual fun providePermissionSettingsManager(): PermissionSettingsManager =
    IosPermissionSettingsManager()