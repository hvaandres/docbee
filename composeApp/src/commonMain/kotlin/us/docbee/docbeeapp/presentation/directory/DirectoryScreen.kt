package us.docbee.docbeeapp.presentation.directory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.general_label_search
import docbee.composeapp.generated.resources.home_directory_contacts_empty_title
import docbee.composeapp.generated.resources.home_directory_contacts_error_title
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import us.docbee.docbeeapp.presentation.components.AnimatedVector
import us.docbee.docbeeapp.presentation.components.ContactCard
import us.docbee.docbeeapp.presentation.components.FloatingButton
import us.docbee.docbeeapp.presentation.components.SwipeState
import us.docbee.docbeeapp.presentation.components.inputs.InputSearchField
import us.docbee.docbeeapp.presentation.directory.effects.DirectoryEffects
import us.docbee.docbeeapp.presentation.directory.events.DirectoryEvents
import us.docbee.docbeeapp.presentation.directory.states.ContactState
import us.docbee.docbeeapp.presentation.navigation.AddContactRoute
import us.docbee.docbeeapp.presentation.theme.White
import us.docbee.docbeeapp.utils.EMPTY_STATE_ANIMATED_VECTOR
import us.docbee.docbeeapp.utils.GENERAL_ERROR_ANIMATED_VECTOR

@Composable
fun DirectoryScreen(
    parentNavController: NavHostController,
    navController: NavHostController,
    viewModel: DirectoryViewModel
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(DirectoryEvents.OnInitEvent)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is DirectoryEffects.NavigateToAddContact -> parentNavController.navigate(
                    AddContactRoute
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 48.dp)
        ) {
            when {
                state.isError -> {
                    ContactListMessage(
                        animation = GENERAL_ERROR_ANIMATED_VECTOR,
                        message = stringResource(Res.string.home_directory_contacts_error_title)
                    )
                }
                state.contacts.isEmpty() -> {
                    ContactListMessage(
                        animation = EMPTY_STATE_ANIMATED_VECTOR,
                        message = stringResource(Res.string.home_directory_contacts_empty_title)
                    )
                }
                else -> {
                    Column(modifier = Modifier.padding(vertical = 24.dp)) {
                        InputSearchField(
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = stringResource(Res.string.general_label_search),
                            value = state.contactSearch,
                            onValueChange = { search ->
                                viewModel.onEvent(
                                    DirectoryEvents.OnSearchEvent(
                                        search
                                    )
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        ContactList(
                            contacts = state.contacts,
                            onArchive = { uid ->
                                viewModel.onEvent(
                                    DirectoryEvents.OnArchiveContactEvent(
                                        uid
                                    )
                                )
                            },
                            onDelete = { uid ->
                                viewModel.onEvent(
                                    DirectoryEvents.OnDeleteContactEvent(
                                        uid
                                    )
                                )
                            },
                            onClick = { uid ->
                                viewModel.onEvent(
                                    DirectoryEvents.OnClickContactEvent(
                                        uid
                                    )
                                )
                            },
                            onSwipeChange = { uid, swipe ->
                                viewModel.onEvent(
                                    DirectoryEvents.OnSwipeContactEvent(
                                        uid,
                                        swipe
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
        if (!state.isMaxContactsReached && !state.isError && !state.isLoading) {
            FloatingButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = { viewModel.onEvent(DirectoryEvents.OnAddContactEvent) }
            )
        }
    }
}

@Composable
fun ContactListMessage(
    animation: String,
    message: String
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 24.dp)
            .offset(y = -(48.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVector(location = animation)
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Normal,
            color = White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ContactList(
    contacts: List<ContactState>,
    onArchive: (uid: String) -> Unit,
    onDelete: (uid: String) -> Unit,
    onClick: (uid: String) -> Unit,
    onSwipeChange: (uid: String, event: SwipeState) -> Unit
) {
    LazyColumn {
        items(items = contacts, key = { contact -> contact.uid + contact.position }) { item ->
            ContactCard(
                gender = item.contact.gender,
                fullName = "${item.contact.firstName} ${item.contact.lastName}",
                address = item.contact.address,
                position = item.position,
                swipeState = item.swipeState,
                onArchive = { onArchive(item.uid) },
                onDelete = { onDelete(item.uid) },
                onClick = { onClick(item.uid) },
                onSwipeChanged = { swipe -> onSwipeChange(item.uid, swipe) }
            )
        }
    }
}