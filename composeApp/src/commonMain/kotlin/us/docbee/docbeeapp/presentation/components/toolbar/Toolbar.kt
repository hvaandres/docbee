package us.docbee.docbeeapp.presentation.components.toolbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import us.docbee.docbeeapp.presentation.theme.White

@Composable
fun Toolbar(
    title: StringResource,
    subtitle: StringResource,
    isBackVisible: Boolean,
    onBackClicked: () -> Unit
) {
    Box(
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
            .padding(top = 32.dp, bottom = 24.dp)
            .fillMaxWidth()
    ) {
        if (isBackVisible) {
            Icon(
                modifier = Modifier
                    .padding(start = 42.dp)
                    .size(24.dp)
                    .clickable { onBackClicked() },
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = White
            )
        }
        Column(
            modifier = Modifier.align(Alignment.Center).padding(horizontal = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = White
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(subtitle),
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                color = White
            )
        }
    }
}