package us.docbee.docbeeapp.data.datasources

import us.docbee.docbeeapp.data.entities.alerts.AlertFetchResponse
import us.docbee.docbeeapp.data.entities.alerts.AlertsDeleteResponse
import us.docbee.docbeeapp.domain.models.alerts.AlertModel

interface AlertsRemoteDataSource {
    suspend fun saveAlert(uid: String, alert: AlertModel)
    suspend fun fetchAlert(uid: String): AlertFetchResponse
    suspend fun deleteAlert(uid: String, alertUid: String): AlertsDeleteResponse
}

expect fun getAlertsDataSource(): AlertsRemoteDataSource