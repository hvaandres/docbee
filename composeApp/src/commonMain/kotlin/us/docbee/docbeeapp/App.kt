package us.docbee.docbeeapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import us.docbee.docbeeapp.data.getEmailAuth

@Composable
@Preview
fun App() {
    MaterialTheme {
        val coroutineScope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        val state = getEmailAuth().signup("test1@testing.com", "abcd1234")
                        println(state)
                    }
                }
            ) {
                Text("Create User")
            }

            Button(
                onClick = {
                    coroutineScope.launch {
                        val state = getEmailAuth().authenticate("test1@testing.com", "abcd1234")
                        println(state)
                    }
                }
            ) {
                Text("Login User")
            }
        }
    }
}