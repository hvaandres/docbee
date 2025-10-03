package us.docbee.docbeeapp.domain.models.alerts

sealed class FetchAlertsResult {
    data class Success(val alerts: List<AlertModel>): FetchAlertsResult()
    data object Error: FetchAlertsResult()
}