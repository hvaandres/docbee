package us.docbee.docbeeapp.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.flow.collectLatest
import us.docbee.docbeeapp.presentation.login.effects.LoginEffect
import us.docbee.docbeeapp.presentation.login.events.LoginEvents
import us.docbee.docbeeapp.presentation.login.states.LoginState

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val state = viewModel.state.collectAsState()
    var showModalError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is LoginEffect.ShowErrorMessage -> {
                    errorMessage = effect.error
                    showModalError = true
                }

                is LoginEffect.HideErrorMessage -> showModalError = false
                is LoginEffect.NavigateToDashboard -> println("Login Success") /* navController.navigate(DashboardScreenRoute) */
            }
        }
    }

    LoginScreenContent(
        state = state.value,
        errorMessage = errorMessage,
        showModalError = showModalError,
        onChangeEmail = { value -> viewModel.onEvent(LoginEvents.OnChangeEmailField(value)) },
        onLoginClick = { viewModel.onEvent(LoginEvents.OnLoginClickButton) },
        onHideModal = { viewModel.onEvent(LoginEvents.OnDismissModalError) }
    )
}

@Composable
fun LoginScreenContent(
    state: LoginState,
    errorMessage: String,
    showModalError: Boolean,
    onChangeEmail: (String) -> Unit,
    onLoginClick: () -> Unit,
    onHideModal: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        OutlinedTextField(
            value = state.email,
            onValueChange = onChangeEmail,
            isError = state.isEmailInvalid
        )
        Button(onClick = onLoginClick) {
            Text(text = "Login")
        }
        if (showModalError) {
            Dialog(onDismissRequest = onHideModal) {
                Column(modifier = Modifier.fillMaxWidth().background(color = Color.White)) {
                    Text(text = errorMessage)
                    Button(onClick = onHideModal) {
                        Text(text = "Done")
                    }
                }
            }
        }
    }
}