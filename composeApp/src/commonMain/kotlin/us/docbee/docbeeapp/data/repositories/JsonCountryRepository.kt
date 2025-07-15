package us.docbee.docbeeapp.data.repositories

import kotlinx.serialization.builtins.ListSerializer
import us.docbee.docbeeapp.data.datasources.interfaces.ResourcesLoader
import us.docbee.docbeeapp.domain.models.Country
import us.docbee.docbeeapp.domain.repositories.CountryRepository
import us.docbee.docbeeapp.utils.COUNTRY_BASE_LOCATION_ICON
import us.docbee.docbeeapp.utils.COUNTRY_DATA_LOCATION

class JsonCountryRepository(
    private val resourceLoader: ResourcesLoader
) : CountryRepository {
    override suspend fun getCountries(): List<Country> {
        return resourceLoader.loadResource<Country>(
            path = COUNTRY_DATA_LOCATION,
            deserializer = ListSerializer(Country.serializer())
        ).map {
            it.copy(
                flagAssetPath = COUNTRY_BASE_LOCATION_ICON.replace("ISO", it.isoCode.lowercase())
            )
        }
    }
}