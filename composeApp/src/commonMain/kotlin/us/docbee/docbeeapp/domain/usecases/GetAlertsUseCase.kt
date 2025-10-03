package us.docbee.docbeeapp.domain.usecases

import us.docbee.docbeeapp.domain.models.alerts.FetchAlertsResult
import us.docbee.docbeeapp.domain.models.directory.UserUidResult
import us.docbee.docbeeapp.domain.repositories.AlertsRepository
import us.docbee.docbeeapp.domain.repositories.UserRepository

class GetAlertsUseCase(
    private val alertsRepository: AlertsRepository,
    private val userRepository: UserRepository
) {
    suspend fun fetchAlerts(): FetchAlertsResult {
        val userResponse = userRepository.fetchUserId()
        if (userResponse !is UserUidResult.Success) {
            return FetchAlertsResult.Error
        }

        return alertsRepository.fetchAlerts(userResponse.uid)
    }
}