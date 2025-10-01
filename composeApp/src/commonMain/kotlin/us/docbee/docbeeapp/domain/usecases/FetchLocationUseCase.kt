package us.docbee.docbeeapp.domain.usecases

import us.docbee.docbeeapp.domain.models.location.LocationResult
import us.docbee.docbeeapp.domain.repositories.LocationRepository
import us.docbee.docbeeapp.utils.ui.permissions.LocationPermissionManager

class FetchLocationUseCase(
    private val locationRepository: LocationRepository,
    private val permissionManager: LocationPermissionManager
) {
    suspend fun fetchCurrentLocation(): LocationResult {
        return if (!permissionManager.isPermissionGranted()) {
            LocationResult.MissingPermission
        } else {
            locationRepository.fetchCurrentLocation()
        }
    }
}