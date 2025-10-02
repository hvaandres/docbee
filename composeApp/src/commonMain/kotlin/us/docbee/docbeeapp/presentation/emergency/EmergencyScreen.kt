package us.docbee.docbeeapp.presentation.emergency

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.background_map
import docbee.composeapp.generated.resources.emergency_bottom_sheet_button_safe
import docbee.composeapp.generated.resources.emergency_bottom_sheet_description
import docbee.composeapp.generated.resources.emergency_bottom_sheet_title
import docbee.composeapp.generated.resources.emergency_toolbar_title
import docbee.composeapp.generated.resources.ic_male
import docbee.composeapp.generated.resources.ic_wireless
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import us.docbee.docbeeapp.domain.models.directory.ContactModel
import us.docbee.docbeeapp.presentation.components.CircularLayout
import us.docbee.docbeeapp.presentation.components.PrimaryButton
import us.docbee.docbeeapp.presentation.components.toolbar.ToolbarSecondary
import us.docbee.docbeeapp.presentation.emergency.effects.EmergencyEffects
import us.docbee.docbeeapp.presentation.emergency.events.EmergencyEvents
import us.docbee.docbeeapp.presentation.theme.Black
import us.docbee.docbeeapp.presentation.theme.Green100
import us.docbee.docbeeapp.presentation.theme.Transparent
import us.docbee.docbeeapp.presentation.theme.White
import us.docbee.docbeeapp.presentation.theme.white100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyScreen(
    navController: NavController,
    viewModel: EmergencyViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is EmergencyEffects.NavigateBack -> navController.navigateUp()
                is EmergencyEffects.NotifyImSafe -> navController.navigateUp()
            }
        }
    }

    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            confirmValueChange = { state -> state != SheetValue.Hidden },
            skipHiddenState = true
        )
    )

    Box(modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)) {
        BottomSheetScaffold(
            modifier = Modifier,
            containerColor = Transparent,
            sheetContainerColor = Black,
            scaffoldState = sheetState,
            sheetDragHandle = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .width(150.dp)
                        .height(4.dp)
                        .background(color = White, shape = RoundedCornerShape(8.dp))
                )
            },
            sheetPeekHeight = 450.dp,
            sheetContent = {
                EmergencyBottomSheet(
                    isExpanded = sheetState.bottomSheetState.currentValue == SheetValue.Expanded,
                    contacts = uiState.contacts,
                    onSafeClick = { viewModel.onEvent(EmergencyEvents.OnClickImSafe) }
                )
            }
        ) { contentPadding ->
            Box {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(Res.drawable.background_map),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                ToolbarSecondary(
                    title = Res.string.emergency_toolbar_title,
                    onBackClicked = { viewModel.onEvent(EmergencyEvents.OnBackPressed) }
                )
            }
        }
    }
}

@Composable
fun EmergencyBottomSheet(
    isExpanded: Boolean,
    contacts: List<ContactModel>,
    onSafeClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 48.dp)
            .verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Box(
                modifier = Modifier
                    .padding(32.dp)
                    .size(107.dp)
                    .clip(CircleShape)
                    .clickable { },
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .align(Alignment.Center)
                        .background(color = Green100, shape = CircleShape)
                )
                Box(
                    modifier = Modifier.padding(5.dp)
                        .fillMaxSize()
                        .align(Alignment.Center)
                        .background(color = white100, shape = CircleShape)
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "3",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Black
                )
            }
        }
        Text(
            text = stringResource(Res.string.emergency_bottom_sheet_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = White
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = stringResource(Res.string.emergency_bottom_sheet_description),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Normal,
            color = White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(42.dp))
        if (contacts.isNotEmpty()) {
            ContactNotified(contacts)
            Spacer(modifier = Modifier.height(42.dp))
            PrimaryButton(
                modifier = Modifier.width(120.dp),
                text = stringResource(Res.string.emergency_bottom_sheet_button_safe),
                onClick = onSafeClick,
                backgroundColor = Green100
            )
        }
    }
}

@Composable
fun ContactNotified(contacts: List<ContactModel>) {
    Box(contentAlignment = Alignment.Center) {
        CircularLayout(modifier = Modifier, radius = 100.dp) {
            repeat(contacts.size) { index ->
                Box(
                    modifier = Modifier.size(58.dp)
                        .background(color = White, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.size(56.dp),
                        imageVector = vectorResource(Res.drawable.ic_male),
                        contentDescription = null
                    )
                }
            }
        }
        Box(
            modifier = Modifier.size(58.dp).background(color = White, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.size(24.dp),
                imageVector = vectorResource(Res.drawable.ic_wireless),
                contentDescription = null
            )
        }
    }
}