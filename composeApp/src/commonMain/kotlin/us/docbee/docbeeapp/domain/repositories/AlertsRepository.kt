package us.docbee.docbeeapp.domain.repositories

import us.docbee.docbeeapp.domain.models.alerts.AddAlertResult
import us.docbee.docbeeapp.domain.models.alerts.AlertModel
import us.docbee.docbeeapp.domain.models.alerts.DeleteAlertResult
import us.docbee.docbeeapp.domain.models.alerts.FetchAlertsResult

interface AlertsRepository {
    suspend fun fetchAlerts(uid: String): FetchAlertsResult
    suspend fun saveAlerts(uid: String, alert: AlertModel): AddAlertResult
    suspend fun deleteAlert(uid: String, alertUid: String): DeleteAlertResult
    suspend fun modifyAlert(uid: String, alert: AlertModel): AddAlertResult
}