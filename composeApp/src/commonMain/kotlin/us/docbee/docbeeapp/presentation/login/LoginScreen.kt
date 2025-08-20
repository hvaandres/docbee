package us.docbee.docbeeapp.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.login_form_email_field
import docbee.composeapp.generated.resources.login_form_email_field_error
import docbee.composeapp.generated.resources.login_form_forgot_password
import docbee.composeapp.generated.resources.login_form_login_label
import docbee.composeapp.generated.resources.login_form_password_field
import docbee.composeapp.generated.resources.login_form_password_field_error
import docbee.composeapp.generated.resources.login_form_remember_check
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import us.docbee.docbeeapp.presentation.components.CheckboxWithText
import us.docbee.docbeeapp.presentation.components.PrimaryButton
import us.docbee.docbeeapp.presentation.components.inputs.InputFieldText
import us.docbee.docbeeapp.presentation.components.inputs.InputPasswordFieldText
import us.docbee.docbeeapp.presentation.login.effects.LoginEffect
import us.docbee.docbeeapp.presentation.login.events.LoginEvents
import us.docbee.docbeeapp.presentation.theme.Blue200

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    snackbarState: SnackbarHostState,
    isLoginTabbed: Boolean,
    onAuthenticationSuccess: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(!isLoginTabbed) {
        viewModel.onEvent(LoginEvents.OnResetEvent)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is LoginEffect.ShowErrorMessage -> {
                    snackbarState.showSnackbar(
                        message = effect.error,
                        duration = SnackbarDuration.Short
                    )
                }

                is LoginEffect.NavigateToForgotPassword -> Unit
                is LoginEffect.NavigateToDashboard -> onAuthenticationSuccess()
            }
        }
    }

    if (isLoginTabbed) {
        LoginContainer(
            modifier = modifier,
            email = state.email,
            password = state.password,
            isRememberCheck = state.rememberMe,
            isEmailError = state.isEmailInvalid,
            isPasswordError = state.isPasswordInvalid,
            onChangeEmail = { email -> viewModel.onEvent(LoginEvents.OnChangeEmailField(email)) },
            onChangePassword = { password ->
                viewModel.onEvent(LoginEvents.OnChangePasswordField(password))
            },
            onChangeRememberCheck = { isChecked ->
                viewModel.onEvent(LoginEvents.OnRememberCheckBox(isChecked))
            },
            onLoginClick = { viewModel.onEvent(LoginEvents.OnLoginClickButton) },
            onForgotPassword = { viewModel.onEvent(LoginEvents.OnForgotPasswordClick) }
        )
    }
}

@Composable
fun LoginContainer(
    modifier: Modifier = Modifier,
    email: String,
    password: String,
    isRememberCheck: Boolean,
    isEmailError: Boolean,
    isPasswordError: Boolean,
    onChangeEmail: (String) -> Unit,
    onChangePassword: (String) -> Unit,
    onChangeRememberCheck: (Boolean) -> Unit,
    onLoginClick: () -> Unit,
    onForgotPassword: () -> Unit
) {
    Column(modifier = modifier) {
        InputFieldText(
            inputLabel = stringResource(Res.string.login_form_email_field),
            value = email,
            onValueChange = onChangeEmail,
            isError = isEmailError,
            errorLabel = stringResource(Res.string.login_form_email_field_error)
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputPasswordFieldText(
            inputLabel = stringResource(Res.string.login_form_password_field),
            value = password,
            onValueChange = onChangePassword,
            imeAction = ImeAction.Done,
            isError = isPasswordError,
            errorLabel = stringResource(Res.string.login_form_password_field_error)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CheckboxWithText(
                text = stringResource(Res.string.login_form_remember_check),
                isChecked = isRememberCheck,
                onCheckedChange = onChangeRememberCheck
            )
            Text(
                modifier = Modifier.clickable { onForgotPassword() }.padding(vertical = 4.dp),
                text = stringResource(Res.string.login_form_forgot_password),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = Blue200
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            text = stringResource(Res.string.login_form_login_label),
            onClick = onLoginClick
        )
    }
}