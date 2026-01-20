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
    private var callback: ((PermissionResult) -> Unit)? = null

    private val delegate = LocationDelegate { status -> handleAuthorizationStatus(status) }

    init {
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.delegate = delegate
    }

    override fun isPermissionGranted(): Boolean {
        return when (locationManager.authorizationStatus()) {
            kCLAuthorizationStatusAuthorizedWhenInUse, kCLAuthorizationStatusAuthorizedAlways -> true
            else -> false
        }
    }

    override fun requestPermission(callback: (PermissionResult) -> Unit) {
        this.callback = callback
        when (locationManager.authorizationStatus()) {
            kCLAuthorizationStatusNotDetermined -> {
                locationManager.requestWhenInUseAuthorization()
            }

            kCLAuthorizationStatusAuthorizedWhenInUse,
            kCLAuthorizationStatusAuthorizedAlways -> {
                dispatchResult(PermissionResult.Granted)
            }

            kCLAuthorizationStatusDenied,
            kCLAuthorizationStatusRestricted -> {
                dispatchResult(PermissionResult.Denied)
            }

            else -> {
                dispatchResult(PermissionResult.Denied)
            }
        }
    }

    private fun handleAuthorizationStatus(status: CLAuthorizationStatus) {
        when (status) {
            kCLAuthorizationStatusAuthorizedWhenInUse,
            kCLAuthorizationStatusAuthorizedAlways -> {
                dispatchResult(PermissionResult.Granted)
            }

            kCLAuthorizationStatusDenied,
            kCLAuthorizationStatusRestricted -> {
                dispatchResult(PermissionResult.Denied)
            }

            else -> Unit
        }
    }

    private fun dispatchResult(result: PermissionResult) {
        callback?.invoke(result)
        callback = null
    }

    private class LocationDelegate(
        private val onStatusChanged: (CLAuthorizationStatus) -> Unit
    ) : NSObject(), CLLocationManagerDelegateProtocol {

        override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
            onStatusChanged(manager.authorizationStatus())
        }

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