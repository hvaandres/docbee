package us.docbee.docbeeapp.utils.ui.permissions

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class AndroidLocationPermissionManager : LocationPermissionManager {

    private lateinit var _activity: ComponentActivity
    private lateinit var _callback: (PermissionResult) -> Unit
    private lateinit var requestContactPermissionLauncher: ActivityResultLauncher<String>

    private val permission = Manifest.permission.ACCESS_FINE_LOCATION

    override fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            _activity,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun requestPermission(callback: (PermissionResult) -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                _activity,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                callback.invoke(PermissionResult.Granted)
            }

            ActivityCompat.shouldShowRequestPermissionRationale(_activity, permission) -> {
                callback.invoke(PermissionResult.Rational)
            }

            else -> {
                _callback = callback
                requestContactPermissionLauncher.launch(permission)
            }
        }
    }

    fun setActivity(activity: ComponentActivity) {
        _activity = activity
        requestContactPermissionLauncher =
            _activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    _callback.invoke(PermissionResult.Granted)
                } else {
                    _callback.invoke(PermissionResult.Denied)
                }
            }
    }
}

actual fun provideLocationPermissionManager(): LocationPermissionManager =
    AndroidLocationPermissionManager()