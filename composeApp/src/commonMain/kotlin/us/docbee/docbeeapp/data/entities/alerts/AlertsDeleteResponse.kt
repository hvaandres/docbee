package us.docbee.docbeeapp.data.entities.alerts

data class AlertsDeleteResponse(
    val successDeleted: Boolean? = null,
    val errorCode: String? = null,
    val errorMessage: String? = null
)