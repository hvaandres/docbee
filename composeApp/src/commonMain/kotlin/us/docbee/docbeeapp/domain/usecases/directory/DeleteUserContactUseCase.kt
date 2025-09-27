package us.docbee.docbeeapp.domain.usecases.directory

import us.docbee.docbeeapp.domain.models.directory.DeleteContactResult
import us.docbee.docbeeapp.domain.models.directory.UserUidResult
import us.docbee.docbeeapp.domain.repositories.ContactsRepository
import us.docbee.docbeeapp.domain.repositories.UserRepository

class DeleteUserContactUseCase(
    private val contactsRepository: ContactsRepository,
    private val userRepository: UserRepository
) {

    suspend fun deleteUserContact(contactUid: String): DeleteContactResult {
        val userId = userRepository.fetchUserId()

        if (userId !is UserUidResult.Success) {
            return DeleteContactResult.Unauthorized
        }

        contactsRepository.deleteUserContact(userId.uid, contactUid)
        return DeleteContactResult.Success
    }
}