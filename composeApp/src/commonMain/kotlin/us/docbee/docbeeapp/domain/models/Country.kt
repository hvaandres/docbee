package us.docbee.docbeeapp.domain.models

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class Country(
    @SerialName("country_name")
    val name: String,
    @SerialName("country_code")
    val isoCode: String,
    @SerialName("phone_code")
    val callingCode: String,
    val flagAssetPath: String = ""
)