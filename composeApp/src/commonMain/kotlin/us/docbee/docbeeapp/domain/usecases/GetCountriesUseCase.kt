package us.docbee.docbeeapp.domain.usecases

import us.docbee.docbeeapp.domain.models.Country
import us.docbee.docbeeapp.domain.repositories.CountryRepository

class GetCountriesUseCase(
    private val repository: CountryRepository
) {
    suspend fun fetchCountries(): List<Country> {
        return repository.getCountries()
            .sortedBy { it.name }
    }
}