package us.docbee.docbeeapp.presentation.directory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.TestOnly
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.general_label_search
import org.jetbrains.compose.resources.stringResource
import us.docbee.docbeeapp.presentation.components.ContactCard
import us.docbee.docbeeapp.presentation.components.SwipeState
import us.docbee.docbeeapp.presentation.components.inputs.InputSearchField
import us.docbee.docbeeapp.presentation.directory.events.DirectoryEvents
import us.docbee.docbeeapp.presentation.directory.states.ContactState
import us.docbee.docbeeapp.presentation.theme.White

@Composable
fun DirectoryScreen(navController: NavHostController, viewModel: DirectoryViewModel) {
    val state by viewModel.uiState.collectAsState()
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
        itemsIndexed(items = contacts, key = {_, contact -> contact.uid}) { index, item ->
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