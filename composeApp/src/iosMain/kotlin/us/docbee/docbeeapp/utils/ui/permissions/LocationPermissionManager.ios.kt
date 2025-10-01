package us.docbee.docbeeapp.utils.ui.permissions

import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.darwin.NSObject

class IosLocationPermissionManager : LocationPermissionManager {

    private val locationManager = CLLocationManager()
    private var _callback: ((PermissionResult) -> Unit)? = null

    init {
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.delegate = LocationDelegate { status ->
            handleAuthorizationStatus(status)
        }
    }

    override fun isPermissionGranted(): Boolean {
        return CLLocationManager.authorizationStatus() == kCLAuthorizationStatusAuthorizedWhenInUse
                || CLLocationManager.authorizationStatus() == kCLAuthorizationStatusAuthorizedAlways
    }

    override fun requestPermission(callback: (PermissionResult) -> Unit) {
        when (CLLocationManager.authorizationStatus()) {
            kCLAuthorizationStatusNotDetermined -> {
                _callback = callback
                locationManager.requestWhenInUseAuthorization()
            }

            kCLAuthorizationStatusRestricted, kCLAuthorizationStatusDenied -> {
                callback.invoke(PermissionResult.Denied)
            }

            kCLAuthorizationStatusAuthorizedWhenInUse, kCLAuthorizationStatusAuthorizedAlways -> {
                callback.invoke(PermissionResult.Granted)
            }

            else -> {
                callback.invoke(PermissionResult.Denied)
            }
        }
    }

    private fun handleAuthorizationStatus(status: CLAuthorizationStatus) {
        when (status) {
            kCLAuthorizationStatusAuthorizedWhenInUse,
            kCLAuthorizationStatusAuthorizedAlways -> _callback?.invoke(PermissionResult.Granted)

            kCLAuthorizationStatusDenied,
            kCLAuthorizationStatusRestricted -> _callback?.invoke(PermissionResult.Denied)
        }
    }

    private class LocationDelegate(
        private val onStatusChanged: (CLAuthorizationStatus) -> Unit
    ) : NSObject(), CLLocationManagerDelegateProtocol {

        override fun locationManager(
            manager: CLLocationManager,
            didChangeAuthorizationStatus: CLAuthorizationStatus
        ) {
            onStatusChanged(didChangeAuthorizationStatus)
        }
    }
}

actual fun provideLocationPermissionManager(): LocationPermissionManager =
    IosLocationPermissionManager()