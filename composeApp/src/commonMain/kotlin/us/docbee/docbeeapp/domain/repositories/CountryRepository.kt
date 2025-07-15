package us.docbee.docbeeapp.domain.repositories

import us.docbee.docbeeapp.domain.models.Country

interface CountryRepository {
    suspend fun getCountries(): List<Country>
}