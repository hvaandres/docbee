package com.docbee.tealapp.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.docbee.tealapp.data.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AndroidLocationService(private val context: Context) : LocationService {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    override suspend fun requestPermissions(): Boolean {
        // Permissions are handled by the Activity/Composable
        // This just checks if they're granted
        return hasPermissions()
    }

    override fun hasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    override suspend fun getCurrentLocation(): Result<Location> {
        if (!hasPermissions()) {
            return Result.failure(SecurityException("Location permissions not granted"))
        }

        return try {
            val cancellationTokenSource = CancellationTokenSource()
            
            val locationResult = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            ).await()

            if (locationResult != null) {
                Result.success(
                    Location(
                        latitude = locationResult.latitude,
                        longitude = locationResult.longitude
                    )
                )
            } else {
                // Fallback to last known location
                val lastLocation = fusedLocationClient.lastLocation.await()
                if (lastLocation != null) {
                    Result.success(
                        Location(
                            latitude = lastLocation.latitude,
                            longitude = lastLocation.longitude
                        )
                    )
                } else {
                    Result.failure(Exception("Unable to get location"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

actual fun createLocationService(): LocationService {
    // This will be provided by MainActivity context
    throw IllegalStateException("LocationService must be created with Android Context")
}

// Factory function to create from Android context
fun createAndroidLocationService(context: Context): LocationService {
    return AndroidLocationService(context)
}
