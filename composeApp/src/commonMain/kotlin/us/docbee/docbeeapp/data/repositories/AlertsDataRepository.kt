package us.docbee.docbeeapp.data.repositories

import us.docbee.docbeeapp.domain.managers.AlertsStringsManager
import us.docbee.docbeeapp.domain.models.alerts.AlertModel
import us.docbee.docbeeapp.domain.models.alerts.FetchAlertsResult
import us.docbee.docbeeapp.domain.repositories.AlertsRepository

class AlertsDataRepository(
    private val stringsManager: AlertsStringsManager
) : AlertsRepository {

    override suspend fun fetchAlerts(uid: String): FetchAlertsResult {
        return FetchAlertsResult.Success(defaultList())
    }

    private suspend fun defaultList(): List<AlertModel> {
        return listOf(
            AlertModel(
                uid = stringsManager.getLostUid(),
                title = stringsManager.getLostTitle(),
                description = stringsManager.getLostDescription(),
                icon = stringsManager.getLostIcon()
            ),
            AlertModel(
                uid = stringsManager.getFallingUid(),
                title = stringsManager.getFallingTitle(),
                description = stringsManager.getFallingDescription(),
                icon = stringsManager.getFallingIcon()
            ),
            AlertModel(
                uid = stringsManager.getDizzyUid(),
                title = stringsManager.getDizzyTitle(),
                description = stringsManager.getDizzyDescription(),
                icon = stringsManager.getDizzyIcon()
            ),
            AlertModel(
                uid = stringsManager.getAccidentUid(),
                title = stringsManager.getAccidentTitle(),
                description = stringsManager.getAccidentDescription(),
                icon = stringsManager.getAccidentIcon()
            )
        )
    }
}