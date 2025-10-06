package us.docbee.docbeeapp.domain.repositories

import us.docbee.docbeeapp.domain.models.alerts.FetchAlertsResult

interface AlertsRepository {
    suspend fun fetchAlerts(uid: String): FetchAlertsResult
}