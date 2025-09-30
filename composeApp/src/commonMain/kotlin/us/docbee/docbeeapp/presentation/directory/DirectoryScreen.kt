package us.docbee.docbeeapp.presentation.directory

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.general_label_search
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import us.docbee.docbeeapp.presentation.components.ContactCard
import us.docbee.docbeeapp.presentation.components.SwipeState
import us.docbee.docbeeapp.presentation.components.inputs.InputSearchField
import us.docbee.docbeeapp.presentation.directory.effects.DirectoryEffects
import us.docbee.docbeeapp.presentation.directory.events.DirectoryEvents
import us.docbee.docbeeapp.presentation.directory.states.ContactState
import us.docbee.docbeeapp.presentation.navigation.AddContactRoute
import us.docbee.docbeeapp.presentation.theme.White

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
                is DirectoryEffects.NavigateToAddContact -> parentNavController.navigate(AddContactRoute)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(vertical = 24.dp, horizontal = 48.dp)
        ) {
            InputSearchField(
                modifier = Modifier.fillMaxWidth(),
                placeholder = stringResource(Res.string.general_label_search),
                value = state.contactSearch,
                onValueChange = { search -> viewModel.onEvent(DirectoryEvents.OnSearchEvent(search)) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            when {
                state.isError -> Text("Error", color = White) // TODO -> It will change in further PRs
                state.contacts.isEmpty() -> Text("Empty State", color = White) // TODO -> It will change in further PRs
                else -> {
                    ContactList(
                        contacts = state.contacts,
                        onArchive = { uid -> viewModel.onEvent(DirectoryEvents.OnArchiveContactEvent(uid)) },
                        onDelete = { uid -> viewModel.onEvent(DirectoryEvents.OnDeleteContactEvent(uid)) },
                        onClick = { uid -> viewModel.onEvent(DirectoryEvents.OnClickContactEvent(uid)) },
                        onSwipeChange = { uid, swipe -> viewModel.onEvent(DirectoryEvents.OnSwipeContactEvent(uid, swipe)) }
                    )
                }
            }
        }
        if (!state.isMaxContactsReached && !state.isError) {
            Image(
                modifier = Modifier.align(Alignment.BottomEnd)
                    .padding(end = 28.dp, bottom = 28.dp)
                    .size(42.dp)
                    .clickable { viewModel.onEvent(DirectoryEvents.OnAddContactEvent) },
                imageVector = Icons.Outlined.AddCircleOutline,
                colorFilter = ColorFilter.tint(color = White),
                contentDescription = null
            )
        }
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
        itemsIndexed(items = contacts, key = { _, contact -> contact.uid }) { index, item ->
            ContactCard(
                gender = item.contact.gender,
                fullName = "${item.contact.firstName} ${item.contact.lastName}",
                address = item.contact.address,
                position = index + 1,
                swipeState = item.swipeState,
                onArchive = { onArchive(item.uid) },
                onDelete = { onDelete(item.uid) },
                onClick = { onClick(item.uid) },
                onSwipeChanged = { swipe -> onSwipeChange(item.uid, swipe) }
            )
        }
    }
}