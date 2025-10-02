package us.docbee.docbeeapp.data.datasources

import us.docbee.docbeeapp.data.entities.LocationResponse

interface LocationDataSource {
    suspend fun fetchCurrentLocation(): LocationResponse
}

expect fun provideLocationDataSource(): LocationDataSource
