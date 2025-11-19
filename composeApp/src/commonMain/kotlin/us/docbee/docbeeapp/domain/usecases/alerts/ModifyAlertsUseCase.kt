package us.docbee.docbeeapp.domain.usecases.alerts

import us.docbee.docbeeapp.domain.mappers.toAlertModel
import us.docbee.docbeeapp.domain.models.alerts.AddAlertResult
import us.docbee.docbeeapp.domain.models.alerts.AlertModifyParams
import us.docbee.docbeeapp.domain.models.directory.UserUidResult
import us.docbee.docbeeapp.domain.repositories.AlertsRepository
import us.docbee.docbeeapp.domain.repositories.UserRepository

class ModifyAlertsUseCase(
    private val alertsRepository: AlertsRepository,
    private val userRepository: UserRepository
) {
    suspend fun modifyAlert(alert: AlertModifyParams): AddAlertResult {
        val userResponse = userRepository.fetchUserId()
        if (userResponse !is UserUidResult.Success) {
            return AddAlertResult.Unauthorized
        }

        return alertsRepository.modifyAlert(userResponse.uid, alert.toAlertModel())
    }
}