package us.docbee.docbeeapp.domain.mappers

import us.docbee.docbeeapp.domain.models.alerts.AlertModel
import us.docbee.docbeeapp.domain.models.alerts.AlertModifyParams
import us.docbee.docbeeapp.domain.models.alerts.AlertParams

fun AlertModel.toMap(): Map<Any?, *> = mapOf(
    "name" to name,
    "message" to message,
    "icon" to icon,
    "uid" to uid
)

fun AlertParams.toAlertModel(): AlertModel = AlertModel(
    name = name,
    message = message,
    icon = icon
)

fun AlertModifyParams.toAlertModel(): AlertModel = AlertModel(
    uid = uid,
    name = name,
    message = message,
    icon = icon
)