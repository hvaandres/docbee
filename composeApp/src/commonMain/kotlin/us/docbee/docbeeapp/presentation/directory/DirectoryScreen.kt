package us.docbee.docbeeapp.presentation.directory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.general_label_search
import org.jetbrains.compose.resources.stringResource
import us.docbee.docbeeapp.presentation.components.ContactCard
import us.docbee.docbeeapp.presentation.components.inputs.InputSearchField
import us.docbee.docbeeapp.presentation.directory.events.DirectoryEvents

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
        LazyColumn {
            itemsIndexed(items = state.contacts) { index, item ->
                ContactCard(
                    gender = item.contact.gender,
                    fullName = "${item.contact.firstName} ${item.contact.lastName}",
                    address = item.contact.address,
                    position = index + 1,
                    swipeState = item.swipeState,
                    onArchive = { viewModel.onEvent(DirectoryEvents.OnArchiveContactEvent(item.contact.uid)) },
                    onDelete = { viewModel.onEvent(DirectoryEvents.OnDeleteContactEvent(item.contact.uid)) },
                    onClick = { viewModel.onEvent(DirectoryEvents.OnClickContactEvent(item.contact.uid)) },
                    onSwipeChanged = { swipe -> viewModel.onEvent(DirectoryEvents.OnSwipeContactEvent(item.contact.uid, swipe)) }
                )
            }
        }
    }
}