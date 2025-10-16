package us.docbee.docbeeapp.domain.usecases.alerts

import us.docbee.docbeeapp.domain.mappers.toAlertModel
import us.docbee.docbeeapp.domain.models.alerts.AddAlertResult
import us.docbee.docbeeapp.domain.models.alerts.AlertParams
import us.docbee.docbeeapp.domain.models.directory.UserUidResult
import us.docbee.docbeeapp.domain.repositories.AlertsRepository
import us.docbee.docbeeapp.domain.repositories.UserRepository

class SaveAlertsUseCase(
    private val alertsRepository: AlertsRepository,
    private val userRepository: UserRepository
) {

    suspend fun saveAlert(alert: AlertParams): AddAlertResult {
        val userResponse = userRepository.fetchUserId()
        if (userResponse !is UserUidResult.Success) {
            return AddAlertResult.Error
        }

        alertsRepository.saveAlerts(userResponse.uid, alert.toAlertModel())
        return AddAlertResult.Success
    }
}