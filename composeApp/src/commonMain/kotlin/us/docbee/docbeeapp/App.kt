package us.docbee.docbeeapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import us.docbee.docbeeapp.data.getEmailAuth
import us.docbee.docbeeapp.presentation.login.LoginScreen
import us.docbee.docbeeapp.presentation.login.LoginViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LoginScreen(koinViewModel())
        }
    }
}