package us.docbee.docbeeapp.domain.repositories

import us.docbee.docbeeapp.domain.models.location.LocationResult

interface LocationRepository {
    suspend fun fetchCurrentLocation(): LocationResult
}