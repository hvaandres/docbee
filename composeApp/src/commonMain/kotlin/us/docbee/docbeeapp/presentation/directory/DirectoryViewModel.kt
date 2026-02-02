package us.docbee.docbeeapp.presentation.directory

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import us.docbee.docbeeapp.domain.models.directory.ContactResult
import us.docbee.docbeeapp.domain.models.directory.DeleteContactResult
import us.docbee.docbeeapp.domain.usecases.GetUserContactsUseCase
import us.docbee.docbeeapp.domain.usecases.directory.DeleteUserContactUseCase
import us.docbee.docbeeapp.presentation.components.SwipeState
import us.docbee.docbeeapp.presentation.core.BaseViewModel
import us.docbee.docbeeapp.presentation.directory.effects.DirectoryEffects
import us.docbee.docbeeapp.presentation.directory.events.DirectoryEvents
import us.docbee.docbeeapp.presentation.directory.states.ContactState
import us.docbee.docbeeapp.presentation.directory.states.DirectoryState
import us.docbee.docbeeapp.utils.MAX_CONTACTS_ALLOWED

class DirectoryViewModel(
    private val userContacts: GetUserContactsUseCase,
    private val deleteContact: DeleteUserContactUseCase
) : BaseViewModel<DirectoryState, DirectoryEvents, DirectoryEffects>(DirectoryState()) {

    override fun onEvent(event: DirectoryEvents) {
        when (event) {
            is DirectoryEvents.OnInitEvent -> initDirectory()
            is DirectoryEvents.OnSwipeContactEvent -> onSwipeContactEvent(event.uid, event.swipeState)
            is DirectoryEvents.OnSearchEvent -> searchContact(event.search)
            is DirectoryEvents.OnArchiveContactEvent -> archiveContact(event.uid)
            is DirectoryEvents.OnDeleteContactEvent -> deleteContact(event.uid)
            is DirectoryEvents.OnClickContactEvent -> clickContact(event.uid)
            is DirectoryEvents.OnAddContactEvent -> emitEffect(DirectoryEffects.NavigateToAddContact)
        }
    }

    private fun onSwipeContactEvent(uid: String, state: SwipeState) {
        updateState {
            copy(
                contacts = contacts.mapIndexed { index, item ->
                    when {
                        item.uid == uid -> item.copy(swipeState = state, position = index)
                        state == SwipeState.Open -> item.copy(swipeState = SwipeState.Closed, position = index)
                        else -> item.copy(position = index)
                    }
                }
            )
        }
    }

    private fun initDirectory() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            when (val response = userContacts.fetchUserContacts()) {
                is ContactResult.Success -> {
                    updateState {
                        copy(
                            contacts = response.list.mapIndexed { index, contact ->
                                ContactState(uid = contact.uid, contact = contact, position = index)
                            },
                            isMaxContactsReached = response.list.size >= MAX_CONTACTS_ALLOWED,
                            isError = false
                        )
                    }
                }
                is ContactResult.Empty -> {
                    updateState { copy(contacts = emptyList(), isError = false) }
                }
                is ContactResult.Error, ContactResult.Unauthorized -> {
                    updateState { copy(isError = true) }
                }
                is ContactResult.NoInternetConnection -> {
                    // WIP add hasNotInternet parameter
                    updateState { copy(isError = true) }
                }
            }
            updateState { copy(isLoading = false) }
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
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            when (deleteContact.deleteUserContact(uid)) {
                is DeleteContactResult.Success -> updateState {
                    val updatedList = contacts.filter { it.uid != uid }
                        .mapIndexed { index, item -> item.copy(position = index) }
                    copy(
                        contacts = updatedList,
                        isMaxContactsReached = updatedList.size >= MAX_CONTACTS_ALLOWED
                    )
                }
                is DeleteContactResult.Error, DeleteContactResult.Unauthorized -> updateState {
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
            updateState { copy(isLoading = false) }
        }
    }

    private fun clickContact(uid: String) {
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
}