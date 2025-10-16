package us.docbee.docbeeapp.domain.mappers

import us.docbee.docbeeapp.domain.models.alerts.AlertModel
import us.docbee.docbeeapp.domain.models.alerts.AlertParams

fun AlertModel.toMap(): Map<Any?, *> = mapOf(
    "name" to name,
    "message" to message,
    "icon" to icon,
    "uid" to uid
)

fun MutableMap<String, Any>.toAlertDomain() = AlertModel(
    uid = this["uid"] as String,
    name = this["name"] as String,
    message = this["message"] as String,
    icon = this["icon"] as String
)

fun AlertParams.toAlertModel(): AlertModel = AlertModel(
    name = name,
    message = message,
    icon = icon
)