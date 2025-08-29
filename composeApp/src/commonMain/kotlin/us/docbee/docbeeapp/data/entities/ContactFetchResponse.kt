package us.docbee.docbeeapp.data.entities

data class ContactFetchResponse(
    val data: List<MutableMap<String, Any>?>? = null,
    val errorCode: String? = null,
    val errorMessage: String? = null
)