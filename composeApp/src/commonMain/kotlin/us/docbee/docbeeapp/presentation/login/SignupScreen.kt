package us.docbee.docbeeapp.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.modal_country_code_search_title
import docbee.composeapp.generated.resources.modal_country_code_title
import docbee.composeapp.generated.resources.signup_form_birth_date_field
import docbee.composeapp.generated.resources.signup_form_email_field
import docbee.composeapp.generated.resources.signup_form_lastname_field
import docbee.composeapp.generated.resources.signup_form_name_field
import docbee.composeapp.generated.resources.signup_form_password_field
import docbee.composeapp.generated.resources.signup_form_phone_field
import docbee.composeapp.generated.resources.signup_form_register_label
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import us.docbee.docbeeapp.presentation.components.PrimaryButton
import us.docbee.docbeeapp.presentation.components.inputs.InputDateFieldText
import us.docbee.docbeeapp.presentation.components.inputs.InputFieldText
import us.docbee.docbeeapp.presentation.components.inputs.InputPasswordFieldText
import us.docbee.docbeeapp.presentation.components.inputs.countrycodefield.CountrySelectorPicker
import us.docbee.docbeeapp.presentation.components.inputs.countrycodefield.InputCountryCodeFieldText
import us.docbee.docbeeapp.presentation.login.effects.SignupEffect
import us.docbee.docbeeapp.presentation.login.events.SignupEvents
import us.docbee.docbeeapp.presentation.login.states.SignupState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    snackbarState: SnackbarHostState,
    isSignupTabbed: Boolean,
    viewModel: SignupViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState()

    var isCountrySelectorVisible by remember { mutableStateOf(false) }

    LaunchedEffect(!isSignupTabbed) {
        viewModel.onEvent(SignupEvents.OnResetEvent)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is SignupEffect.ShowErrorMessage -> {
                    snackbarState.showSnackbar(
                        message = effect.error,
                        duration = SnackbarDuration.Short
                    )
                }

                is SignupEffect.OpenCountrySelector -> isCountrySelectorVisible = true
                is SignupEffect.CloseCountrySelector -> isCountrySelectorVisible = false
                is SignupEffect.NavigateToDashboard -> Unit
            }
        }
    }
    if (isSignupTabbed) {
        SignupContainer(
            modifier = modifier,
            uiState = state.value,
            onChangeName = { name ->
                viewModel.onEvent(SignupEvents.OnChangeNameField(name))
            },
            onChangeLastName = { lastname ->
                viewModel.onEvent(SignupEvents.OnChangeLastNameField(lastname))
            },
            onChangeEmail = { email ->
                viewModel.onEvent(SignupEvents.OnChangeEmailField(email))
            },
            onChangeDateOfBirth = { dateOfBirth ->
                viewModel.onEvent(SignupEvents.OnChangeDateOfBirthField(dateOfBirth))
            },
            onChangePhoneNumber = { phoneNumber ->
                viewModel.onEvent(SignupEvents.OnChangePhoneNumberField(phoneNumber))
            },
            onChangePassword = { password ->
                viewModel.onEvent(SignupEvents.OnChangePasswordField(password))
            },
            onSignupClick = {
                viewModel.onEvent(SignupEvents.OnSignupClickButton)
            },
            onCountryCodeClicked = {
                viewModel.onEvent(SignupEvents.CountryCodeEvent)
            }
        )
        CountrySelectorPicker(
            isVisible = isCountrySelectorVisible,
            searchValue = state.value.phoneState.searchCountryValue,
            title = stringResource(Res.string.modal_country_code_title),
            placeholder = stringResource(Res.string.modal_country_code_search_title),
            countries = state.value.phoneState.countries,
            onSearchValueChange = { search ->
                viewModel.onEvent(SignupEvents.OnChangeSearchField(search))
            },
            onDismiss = { viewModel.onEvent(SignupEvents.CloseCountryCodeEvent) },
            onCountrySelect = { country -> viewModel.onEvent(SignupEvents.OnCountryCodeField(country)) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupContainer(
    modifier: Modifier = Modifier,
    uiState: SignupState,
    onChangeName: (String) -> Unit,
    onChangeLastName: (String) -> Unit,
    onChangeEmail: (String) -> Unit,
    onChangeDateOfBirth: (Long) -> Unit,
    onChangePhoneNumber: (String) -> Unit,
    onChangePassword: (String) -> Unit,
    onSignupClick: () -> Unit,
    onCountryCodeClicked: () -> Unit
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InputFieldText(
                modifier = Modifier.weight(1f),
                inputLabel = stringResource(Res.string.signup_form_name_field),
                keyboardCapitalization = KeyboardCapitalization.Words,
                focusDirection = FocusDirection.Right,
                imeAction = ImeAction.Next,
                value = uiState.name,
                onValueChange = onChangeName
            )
            InputFieldText(
                modifier = Modifier.weight(1f),
                inputLabel = stringResource(Res.string.signup_form_lastname_field),
                keyboardCapitalization = KeyboardCapitalization.Words,
                focusDirection = FocusDirection.Down,
                imeAction = ImeAction.Next,
                value = uiState.lastName,
                onValueChange = onChangeLastName
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        InputFieldText(
            inputLabel = stringResource(Res.string.signup_form_email_field),
            focusDirection = FocusDirection.Down,
            imeAction = ImeAction.Next,
            value = uiState.email,
            onValueChange = onChangeEmail
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputDateFieldText(
            value = uiState.dateOfBirth,
            inputLabel = stringResource(Res.string.signup_form_birth_date_field),
            datePickerState = rememberDatePickerState(),
            onSelectedDate = onChangeDateOfBirth
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputCountryCodeFieldText(
            state = uiState.phoneState,
            inputLabel = stringResource(Res.string.signup_form_phone_field),
            focusDirection = FocusDirection.Down,
            imeAction = ImeAction.Next,
            onChangePhoneNumber = onChangePhoneNumber,
            onCountryCodeClicked = onCountryCodeClicked
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputPasswordFieldText(
            inputLabel = stringResource(Res.string.signup_form_password_field),
            value = uiState.password,
            onValueChange = onChangePassword,
            imeAction = ImeAction.Done
        )
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            text = stringResource(Res.string.signup_form_register_label),
            onClick = onSignupClick
        )
    }
}