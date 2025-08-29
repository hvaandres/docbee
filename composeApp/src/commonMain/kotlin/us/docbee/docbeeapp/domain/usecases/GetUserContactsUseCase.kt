package us.docbee.docbeeapp.domain.usecases

import us.docbee.docbeeapp.domain.models.directory.ContactResult
import us.docbee.docbeeapp.domain.models.directory.UserUidResult
import us.docbee.docbeeapp.domain.repositories.ContactsRepository
import us.docbee.docbeeapp.domain.repositories.UserRepository

class GetUserContactsUseCase(
    val repository: ContactsRepository,
    val userRepository: UserRepository
) {
    suspend fun fetchUserContacts(): ContactResult {
        val userId = userRepository.fetchUserId()

        if (userId !is UserUidResult.Success) {
            return ContactResult.Unauthorized
        }

        return repository.fetchUserContact(userId.uid)
    }
}