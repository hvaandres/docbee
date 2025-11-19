package us.docbee.docbeeapp.data.entities.alerts

import us.docbee.docbeeapp.domain.models.alerts.AlertModel

data class AlertFetchResponse(
    val data: List<AlertModel>? = null,
    val errorCode: String? = null,
    val errorMessage: String? = null
)