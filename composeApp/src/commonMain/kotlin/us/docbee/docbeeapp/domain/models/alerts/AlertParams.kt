package us.docbee.docbeeapp.domain.models.alerts

data class AlertParams(
    val name: String,
    val message: String,
    val icon: String
)

data class AlertModifyParams(
    val uid: String,
    val name: String,
    val message: String,
    val icon: String
)