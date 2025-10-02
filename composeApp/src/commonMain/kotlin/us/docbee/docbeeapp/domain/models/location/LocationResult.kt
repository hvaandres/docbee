package us.docbee.docbeeapp.domain.models.location

sealed class LocationResult {
    data class Success(val location: LocationData): LocationResult()
    data object Error: LocationResult()
    data object MissingPermission: LocationResult()
}

data class LocationData(
    val latitude: Double,
    val longitude: Double
)