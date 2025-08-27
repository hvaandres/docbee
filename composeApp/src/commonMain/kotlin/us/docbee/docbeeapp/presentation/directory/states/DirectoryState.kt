package us.docbee.docbeeapp.presentation.directory.states

import us.docbee.docbeeapp.domain.models.directory.ContactModel
import us.docbee.docbeeapp.presentation.components.SwipeState

data class DirectoryState(
    val contacts: List<ContactState> = emptyList<ContactState>(),
    val contactSearch: String = ""
)

data class ContactState(
    val uid: String,
    val contact: ContactModel,
    val swipeState: SwipeState = SwipeState.Closed
)