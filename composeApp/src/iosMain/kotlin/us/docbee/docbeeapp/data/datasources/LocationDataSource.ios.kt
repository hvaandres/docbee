package us.docbee.docbeeapp.data.datasources

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.darwin.NSObject
import us.docbee.docbeeapp.data.entities.Location
import us.docbee.docbeeapp.data.entities.LocationResponse
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime

class IosLocationDataSource : LocationDataSource {

    val maxAccuracyMeters: Float = 20f
    val timeout: Duration = 10000.milliseconds

    @OptIn(ExperimentalForeignApi::class, ExperimentalTime::class)
    override suspend fun fetchCurrentLocation(): LocationResponse {
        return suspendCancellableCoroutine { continuation ->
            val manager = CLLocationManager()
            val startTime = Clock.System.now()
            var isCompleted = false

            val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
                override fun locationManager(
                    manager: CLLocationManager,
                    didUpdateLocations: List<*>
                ) {
                    if (isCompleted) return
                    val elapsed = Clock.System.now() - startTime
                    val location = didUpdateLocations.lastOrNull() as? CLLocation ?: return
                    val isAccurateEnough = location.horizontalAccuracy <= maxAccuracyMeters
                    val isTimeout = elapsed >= timeout

                    if (isAccurateEnough || isTimeout) {
                        isCompleted = true
                        location.coordinate.useContents {
                            continuation.resume(
                                LocationResponse(
                                    location = Location(
                                        latitude = latitude,
                                        longitude = longitude,
                                        accuracy = location.horizontalAccuracy.toFloat()
                                    )
                                ), null)
                        }
                        manager.stopUpdatingLocation()
                    }
                }

                override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
                    if (isCompleted) return
                    isCompleted = true

                    val errorMessage = didFailWithError.userInfo["NSLocalizedDescription"] as? String
                    continuation.resume(
                        LocationResponse(errorCode = "UNKNOWN_ERROR", errorMessage = errorMessage),
                        null
                    )
                    manager.stopUpdatingLocation()
                }
            }

            manager.desiredAccuracy = kCLLocationAccuracyBest
            manager.delegate = delegate
            manager.requestWhenInUseAuthorization()
            manager.startUpdatingLocation()

            continuation.invokeOnCancellation {
                isCompleted = true
                manager.stopUpdatingLocation()
            }
        }
    }
}

actual fun provideLocationDataSource(): LocationDataSource = IosLocationDataSource()