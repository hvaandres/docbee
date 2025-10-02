package us.docbee.docbeeapp.data.repositories

import us.docbee.docbeeapp.data.datasources.LocationDataSource
import us.docbee.docbeeapp.domain.models.location.LocationData
import us.docbee.docbeeapp.domain.models.location.LocationResult
import us.docbee.docbeeapp.domain.repositories.LocationRepository

class LocationDataRepository(
    private val locationDataSource: LocationDataSource
) : LocationRepository {
    override suspend fun fetchCurrentLocation(): LocationResult {
        val response = locationDataSource.fetchCurrentLocation()
        return if (response.location != null) {
            LocationResult.Success(
                LocationData(
                    response.location.latitude,
                    response.location.longitude
                )
            )
        } else {
            LocationResult.Error
        }
    }
}