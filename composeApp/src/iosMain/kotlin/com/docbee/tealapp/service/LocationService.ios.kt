package com.docbee.tealapp.service

import com.docbee.tealapp.data.Location
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.*
import platform.Foundation.NSError
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
class IOSLocationService : LocationService, CLLocationManagerDelegateProtocol {
    private val locationManager = CLLocationManager()
    private var locationContinuation: ((Result<Location>) -> Unit)? = null

    init {
        locationManager.delegate = this
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
    }

    override suspend fun requestPermissions(): Boolean {
        return suspendCancellableCoroutine { continuation ->
            val currentStatus = CLLocationManager.authorizationStatus()
            
            when (currentStatus) {
                kCLAuthorizationStatusAuthorizedWhenInUse,
                kCLAuthorizationStatusAuthorizedAlways -> {
                    continuation.resume(true)
                }
                kCLAuthorizationStatusNotDetermined -> {
                    locationManager.requestWhenInUseAuthorization()
                    // Authorization callback will be handled by delegate
                    // For now, assume it will be granted
                    continuation.resume(true)
                }
                else -> {
                    continuation.resume(false)
                }
            }
        }
    }

    override fun hasPermissions(): Boolean {
        val status = CLLocationManager.authorizationStatus()
        return status == kCLAuthorizationStatusAuthorizedWhenInUse ||
                status == kCLAuthorizationStatusAuthorizedAlways
    }

    override suspend fun getCurrentLocation(): Result<Location> {
        if (!hasPermissions()) {
            return Result.failure(SecurityException("Location permissions not granted"))
        }

        return suspendCancellableCoroutine { continuation ->
            locationContinuation = { result ->
                continuation.resume(result)
            }
            
            locationManager.requestLocation()
            
            continuation.invokeOnCancellation {
                locationContinuation = null
            }
        }
    }

    // CLLocationManagerDelegate methods
    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        val location = didUpdateLocations.lastOrNull() as? CLLocation
        location?.let {
            it.coordinate.useContents {
                val result = Location(
                    latitude = latitude,
                    longitude = longitude
                )
                locationContinuation?.invoke(Result.success(result))
                locationContinuation = null
            }
        }
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
        locationContinuation?.invoke(
            Result.failure(Exception(didFailWithError.localizedDescription))
        )
        locationContinuation = null
    }
}

actual fun createLocationService(): LocationService {
    return IOSLocationService()
}
