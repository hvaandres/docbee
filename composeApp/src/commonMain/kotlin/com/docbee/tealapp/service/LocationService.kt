package com.docbee.tealapp.service

import com.docbee.tealapp.data.Location

/**
 * Platform-specific location service interface
 */
interface LocationService {
    /**
     * Request location permissions (if needed)
     * Returns true if permissions are granted
     */
    suspend fun requestPermissions(): Boolean

    /**
     * Check if location permissions are granted
     */
    fun hasPermissions(): Boolean

    /**
     * Get current device location
     */
    suspend fun getCurrentLocation(): Result<Location>
}

expect fun createLocationService(): LocationService
