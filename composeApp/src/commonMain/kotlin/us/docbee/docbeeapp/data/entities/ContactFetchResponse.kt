package us.docbee.docbeeapp.data.entities

import us.docbee.docbeeapp.domain.models.directory.ContactModel

data class ContactFetchResponse(
    val data: List<ContactModel>? = null,
    val errorCode: String? = null,
    val errorMessage: String? = null
)