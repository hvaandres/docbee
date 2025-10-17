package us.docbee.docbeeapp.domain.usecases.alerts

import us.docbee.docbeeapp.domain.models.alerts.DeleteAlertResult
import us.docbee.docbeeapp.domain.models.directory.UserUidResult
import us.docbee.docbeeapp.domain.repositories.AlertsRepository
import us.docbee.docbeeapp.domain.repositories.UserRepository

class DeleteAlertsUseCase(
    private val alertsRepository: AlertsRepository,
    private val userRepository: UserRepository
) {
    suspend fun deleteAlert(uid: String): DeleteAlertResult {
        val userResponse = userRepository.fetchUserId()
        if (userResponse !is UserUidResult.Success) {
            return DeleteAlertResult.Unauthorized
        }

        return alertsRepository.deleteAlert(userResponse.uid, uid)
    }
}