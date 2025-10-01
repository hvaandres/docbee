package us.docbee.docbeeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.koin.android.ext.android.inject
import us.docbee.docbeeapp.utils.ui.permissions.AndroidLocationPermissionManager
import us.docbee.docbeeapp.utils.ui.permissions.LocationPermissionManager

class MainActivity : ComponentActivity() {

    private val locationManager: LocationPermissionManager by inject<LocationPermissionManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        (locationManager as AndroidLocationPermissionManager).setActivity(this)

        setContent {
            App()
        }
    }
}