package us.docbee.docbeeapp.domain.usecases

import us.docbee.docbeeapp.domain.mappers.toContactModel
import us.docbee.docbeeapp.domain.models.directory.AddContactResult
import us.docbee.docbeeapp.domain.models.directory.UserUidResult
import us.docbee.docbeeapp.domain.models.signup.ContactParams
import us.docbee.docbeeapp.domain.repositories.ContactsRepository
import us.docbee.docbeeapp.domain.repositories.UserRepository

class SaveUserContactUseCase(
    val repository: ContactsRepository,
    val userRepository: UserRepository
) {
    suspend fun saveUserContact(contact: ContactParams): AddContactResult {
        val userId = userRepository.fetchUserId()

        if (userId !is UserUidResult.Success) {
            return AddContactResult.Unauthorized
        }
        repository.saveUserContact(userId.uid, contact.toContactModel())
        return AddContactResult.Success
    }
}