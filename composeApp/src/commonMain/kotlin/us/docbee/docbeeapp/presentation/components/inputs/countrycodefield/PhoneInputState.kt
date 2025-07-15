package us.docbee.docbeeapp.presentation.components.inputs.countrycodefield

import us.docbee.docbeeapp.domain.models.Country

data class PhoneInputState(
    val countries: List<Country> = emptyList(),
    val selectedCountry: Country? = null,
    val phoneNumber: String = ""
)