package us.docbee.docbeeapp.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.no_internet_connection_paragraph
import docbee.composeapp.generated.resources.no_internet_connection_title
import org.jetbrains.compose.resources.stringResource
import us.docbee.docbeeapp.presentation.theme.Black
import us.docbee.docbeeapp.presentation.theme.White

@Composable
fun NoInternetConnectionScreen() {
    Column(
        modifier = Modifier.fillMaxSize().background(Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement =  Arrangement.Center,
    ) {
        Text(
            stringResource(Res.string.no_internet_connection_title),
            style = TextStyle(color = White),
        )
        Text(
            stringResource(Res.string.no_internet_connection_paragraph),
            style = TextStyle(color = White),
        )
    }
}