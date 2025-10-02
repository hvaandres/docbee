package us.docbee.docbeeapp.presentation.emergency.states

import us.docbee.docbeeapp.domain.models.directory.ContactModel

data class UiState(
    val isSharingLocation: Boolean = false,
    val contacts: List<ContactModel> = emptyList()
)