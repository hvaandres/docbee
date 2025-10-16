package us.docbee.docbeeapp.data.entities.alerts

data class AlertFetchResponse(
    val data: List<MutableMap<String, Any>?>? = null,
    val errorCode: String? = null,
    val errorMessage: String? = null
)