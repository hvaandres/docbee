package us.docbee.docbeeapp.data.datasources

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.datetime.Clock
import org.koin.core.context.GlobalContext
import us.docbee.docbeeapp.data.entities.Location
import us.docbee.docbeeapp.data.entities.LocationResponse
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


class AndroidLocationDataSource(private val context: Context) : LocationDataSource {

    val maxAccuracyMeters: Float = 20f
    val timeout: Duration = 10000.milliseconds

    @SuppressLint("MissingPermission")
    override suspend fun fetchCurrentLocation(): LocationResponse {
        return suspendCancellableCoroutine { continuation ->
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            val startTime = Clock.System.now()

            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
                .setWaitForAccurateLocation(true)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val location = result.lastLocation ?: return

                    val elapsed = Clock.System.now() - startTime
                    val isAccurateEnough = location.accuracy <= maxAccuracyMeters
                    val isTimeout = elapsed >= timeout

                    if (isAccurateEnough || isTimeout) {
                        fusedLocationClient.removeLocationUpdates(this)
                        val lastLocation = result.lastLocation
                        if (lastLocation != null) {
                            continuation.resume(
                                LocationResponse(
                                    location = Location(
                                        latitude = lastLocation.latitude,
                                        longitude = lastLocation.longitude,
                                        accuracy = lastLocation.accuracy
                                    )
                                ), null
                            )
                        } else {
                            continuation.resume(
                                LocationResponse(errorCode = "UNKNOWN_ERROR"),
                                null
                            )
                        }
                    }
                }
            }

            fusedLocationClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            continuation.invokeOnCancellation {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }
}

actual fun provideLocationDataSource(): LocationDataSource =
    AndroidLocationDataSource(GlobalContext.get().get())
