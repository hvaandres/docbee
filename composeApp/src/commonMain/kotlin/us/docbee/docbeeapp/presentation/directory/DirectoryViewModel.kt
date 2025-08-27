package us.docbee.docbeeapp.presentation.directory

import us.docbee.docbeeapp.domain.models.directory.ContactModel
import us.docbee.docbeeapp.presentation.components.SwipeState
import us.docbee.docbeeapp.presentation.core.BaseViewModel
import us.docbee.docbeeapp.presentation.directory.effects.DirectoryEffects
import us.docbee.docbeeapp.presentation.directory.events.DirectoryEvents
import us.docbee.docbeeapp.presentation.directory.states.ContactState
import us.docbee.docbeeapp.presentation.directory.states.DirectoryState

class DirectoryViewModel :
    BaseViewModel<DirectoryState, DirectoryEvents, DirectoryEffects>(DirectoryState()) {

    init {
        onEvent(DirectoryEvents.OnInitEvent)
    }

    override fun onEvent(event: DirectoryEvents) {
        when (event) {
            is DirectoryEvents.OnInitEvent -> initDirectory()
            is DirectoryEvents.OnSwipeContactEvent -> onSwipeContactEvent(event.uid, event.swipeState)
            is DirectoryEvents.OnSearchEvent -> searchContact(event.search)
            is DirectoryEvents.OnArchiveContactEvent -> archiveContact(event.uid)
            is DirectoryEvents.OnDeleteContactEvent -> deleteContact(event.uid)
            is DirectoryEvents.OnClickContactEvent -> clickContact(event.uid)
        }
    }

    private fun onSwipeContactEvent(uid: String, state: SwipeState) {
        updateState {
            copy(
                contacts = contacts.map { item ->
                    when {
                        item.uid == uid -> item.copy(swipeState = state)
                        state == SwipeState.Open -> item.copy(swipeState = SwipeState.Closed)
                        else -> item
                    }
                }
            )
        }
    }

    private fun initDirectory() {
        updateState {
            copy(contacts = dummyContacts())
        }
    }

    private fun searchContact(text: String) {
        updateState { copy(contactSearch = text) }
    }

    private fun archiveContact(uid: String) {
        updateState {
            copy(
                contacts = contacts.map { item ->
                    if (item.uid == uid) {
                        item.copy(swipeState = SwipeState.Closed)
                    } else {
                        item
                    }
                }
            )
        }
    }

    private fun deleteContact(uid: String) {
        updateState {
            copy(
                contacts = contacts.map { item ->
                    if (item.uid == uid) {
                        item.copy(swipeState = SwipeState.Closed)
                    } else {
                        item
                    }
                }
            )
        }
    }

    private fun clickContact(uid: String) {

    }

    private fun dummyContacts(): List<ContactState> {
        return listOf(
            ContactState(
                uid = "1",
                contact = ContactModel(
                    uid = "1",
                    firstName = "Alice",
                    lastName = "Johnson",
                    email = "alice.johnson@example.com",
                    phoneNumber = "+1 555-1234",
                    address = "123 Maple Street, Springfield",
                    gender = "F"
                )
            ),
            ContactState(
                uid = "2",
                contact = ContactModel(
                    uid = "2",
                    firstName = "Bob",
                    lastName = "Smith",
                    email = "bob.smith@example.com",
                    phoneNumber = "+1 555-5678",
                    address = "456 Oak Avenue, Shelbyville",
                    gender = "M"
                )
            ),
            ContactState(
                uid = "3",
                contact = ContactModel(
                    uid = "3",
                    firstName = "Carla",
                    lastName = "Mendoza",
                    email = "carla.mendoza@example.com",
                    phoneNumber = "+1 555-2468",
                    address = "789 Pine Road, Ogdenville",
                    gender = "F"
                )
            ),
            ContactState(
                uid = "4",
                contact = ContactModel(
                    uid = "4",
                    firstName = "David",
                    lastName = "Lee",
                    email = "david.lee@example.com",
                    phoneNumber = "+1 555-1357",
                    address = "321 Cedar Blvd, North Haverbrook",
                    gender = "M"
                )
            ),
            ContactState(
                uid = "5",
                contact = ContactModel(
                    uid = "5",
                    firstName = "Elena",
                    lastName = "Garcia",
                    email = "elena.garcia@example.com",
                    phoneNumber = "+1 555-9876",
                    address = "654 Birch Lane, Springfield",
                    gender = "F"
                )
            )
        )
    }
}