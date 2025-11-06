package us.docbee.docbeeapp.data.entities.alerts

import us.docbee.docbeeapp.domain.models.alerts.AlertModel

data class AlertSaveResponse(
    val alert: AlertModel? = null,
    val errorCode: String? = null,
    val errorMessage: String? = null
)