package us.docbee.docbeeapp.data.entities

data class LocationResponse(
    val location: Location? = null,
    val errorCode: String? = null,
    val errorMessage: String? = null
)

data class Location(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float? = null
)