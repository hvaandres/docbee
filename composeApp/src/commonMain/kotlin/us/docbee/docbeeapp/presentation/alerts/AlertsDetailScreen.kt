package us.docbee.docbeeapp.presentation.alerts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.alerts_sending_alerts_appbar_subtitle
import docbee.composeapp.generated.resources.alerts_sending_alerts_appbar_title
import docbee.composeapp.generated.resources.alerts_sending_alerts_description
import org.jetbrains.compose.resources.stringResource
import us.docbee.docbeeapp.presentation.components.AnimatedVector
import us.docbee.docbeeapp.presentation.components.toolbar.Toolbar
import us.docbee.docbeeapp.presentation.theme.Black
import us.docbee.docbeeapp.presentation.theme.White
import us.docbee.docbeeapp.utils.ALERT_ANIMATED_VECTOR
import us.docbee.docbeeapp.utils.ui.SetStatusBar

@Composable
fun AlertsDetailScreen(navController: NavHostController) {

    val animatedVector = remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        animatedVector.value = Res.readBytes(ALERT_ANIMATED_VECTOR).decodeToString()
    }

    SetStatusBar(isDarkMode = true)
    Scaffold(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = Black,
        topBar = {
            Toolbar(
                title = Res.string.alerts_sending_alerts_appbar_title,
                subtitle = Res.string.alerts_sending_alerts_appbar_subtitle,
                isBackVisible = true,
                onBackClicked = { navController.navigateUp() }
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 54.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                animatedVector.value?.let {
                    AnimatedVector(
                        modifier = Modifier.fillMaxWidth(),
                        path = it
                    )
                }
                Text(
                    text = stringResource(Res.string.alerts_sending_alerts_description),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Normal,
                    color = White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}