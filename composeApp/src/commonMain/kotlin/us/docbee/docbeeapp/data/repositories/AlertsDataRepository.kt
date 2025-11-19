package us.docbee.docbeeapp.data.repositories

import us.docbee.docbeeapp.data.datasources.AlertsRemoteDataSource
import us.docbee.docbeeapp.domain.managers.AlertsStringsManager
import us.docbee.docbeeapp.domain.models.alerts.AddAlertResult
import us.docbee.docbeeapp.domain.models.alerts.AlertModel
import us.docbee.docbeeapp.domain.models.alerts.DeleteAlertResult
import us.docbee.docbeeapp.domain.models.alerts.FetchAlertsResult
import us.docbee.docbeeapp.domain.repositories.AlertsRepository

class AlertsDataRepository(
    private val stringsManager: AlertsStringsManager,
    private val alertDataSource: AlertsRemoteDataSource
) : AlertsRepository {

    override suspend fun fetchAlerts(uid: String): FetchAlertsResult {
        var alertList = mutableListOf<AlertModel>().apply { addAll(defaultList()) }

        val response = alertDataSource.fetchAlert(uid)
        response.data?.mapNotNull { it }?.let {
            alertList.addAll(it)
        }

        return FetchAlertsResult.Success(alertList)
    }

    override suspend fun saveAlerts(uid: String, alert: AlertModel): AddAlertResult {
        val response = alertDataSource.saveAlert(uid, alert)
        return when {
            response.alert != null -> AddAlertResult.Success(response.alert)
            else -> AddAlertResult.Error
        }
    }

    override suspend fun deleteAlert(
        uid: String,
        alertUid: String
    ): DeleteAlertResult {
        val response = alertDataSource.deleteAlert(uid, alertUid)
        return when {
            response.errorCode != null -> DeleteAlertResult.Error
            else -> DeleteAlertResult.Success
        }
    }

    private suspend fun defaultList(): List<AlertModel> {
        return listOf(
            AlertModel(
                uid = stringsManager.getLostUid(),
                name = stringsManager.getLostTitle(),
                message = stringsManager.getLostDescription(),
                icon = stringsManager.getLostIcon()
            ),
            AlertModel(
                uid = stringsManager.getFallingUid(),
                name = stringsManager.getFallingTitle(),
                message = stringsManager.getFallingDescription(),
                icon = stringsManager.getFallingIcon()
            ),
            AlertModel(
                uid = stringsManager.getDizzyUid(),
                name = stringsManager.getDizzyTitle(),
                message = stringsManager.getDizzyDescription(),
                icon = stringsManager.getDizzyIcon()
            ),
            AlertModel(
                uid = stringsManager.getAccidentUid(),
                name = stringsManager.getAccidentTitle(),
                message = stringsManager.getAccidentDescription(),
                icon = stringsManager.getAccidentIcon()
            )
        )
    }
}