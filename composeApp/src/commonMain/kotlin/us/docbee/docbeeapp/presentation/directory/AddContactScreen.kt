package us.docbee.docbeeapp.presentation.directory

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.add_contact_app_bar_subtitle
import docbee.composeapp.generated.resources.add_contact_app_bar_title
import docbee.composeapp.generated.resources.add_contact_form_address_field
import docbee.composeapp.generated.resources.add_contact_form_birth_date_field
import docbee.composeapp.generated.resources.add_contact_form_email_field
import docbee.composeapp.generated.resources.add_contact_form_lastname_field
import docbee.composeapp.generated.resources.add_contact_form_name_field
import docbee.composeapp.generated.resources.add_contact_form_phone_field
import docbee.composeapp.generated.resources.add_contact_form_register_label
import docbee.composeapp.generated.resources.ic_male
import docbee.composeapp.generated.resources.modal_country_code_search_title
import docbee.composeapp.generated.resources.modal_country_code_title
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import us.docbee.docbeeapp.presentation.components.PrimaryButton
import us.docbee.docbeeapp.presentation.components.inputs.InputDateFieldText
import us.docbee.docbeeapp.presentation.components.inputs.InputFieldText
import us.docbee.docbeeapp.presentation.components.inputs.countrycodefield.CountrySelectorPicker
import us.docbee.docbeeapp.presentation.components.inputs.countrycodefield.InputCountryCodeFieldText
import us.docbee.docbeeapp.presentation.components.toolbar.Toolbar
import us.docbee.docbeeapp.presentation.directory.effects.AddContactEffects
import us.docbee.docbeeapp.presentation.directory.events.AddContactEvents
import us.docbee.docbeeapp.presentation.directory.states.AddContactState
import us.docbee.docbeeapp.presentation.theme.Black
import us.docbee.docbeeapp.presentation.theme.Green100
import us.docbee.docbeeapp.presentation.theme.White
import us.docbee.docbeeapp.utils.ui.SetStatusBar

@Composable
fun AddContactScreen(navController: NavHostController, viewModel: AddContactViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var isCountrySelectorVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is AddContactEffects.NavigateBack -> navController.navigateUp()
                is AddContactEffects.NavigateToDirectory -> navController.navigateUp()
                is AddContactEffects.OpenCountrySelector -> isCountrySelectorVisible = true
                is AddContactEffects.CloseCountrySelector -> isCountrySelectorVisible = false
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = Black,
        topBar = {
            Toolbar(
                title = Res.string.add_contact_app_bar_title,
                subtitle = Res.string.add_contact_app_bar_subtitle,
                isBackVisible = true,
                onBackClicked = { viewModel.onEvent(AddContactEvents.OnBackPressed) }
            )
        }
    ) { contentPadding ->
        SetStatusBar(isDarkMode = true)
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = 54.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.padding(vertical = 24.dp)
                    .size(92.dp)
                    .border(width = 2.dp, shape = CircleShape, color = White),
                painter = painterResource(Res.drawable.ic_male),
                colorFilter = ColorFilter.tint(color = White),
                contentDescription = null
            )
            AddContactForm(
                modifier = Modifier,
                uiState = uiState,
                onChangeName = { name -> viewModel.onEvent(AddContactEvents.OnChangeNameField(name))},
                onChangeLastName = { lastname -> viewModel.onEvent(AddContactEvents.OnChangeLastNameField(lastname)) },
                onChangeEmail = { email -> viewModel.onEvent(AddContactEvents.OnChangeEmailField(email)) },
                onChangeDateOfBirth = { birth -> viewModel.onEvent(AddContactEvents.OnChangeDateOfBirthField(birth)) },
                onChangePhoneNumber = { phone -> viewModel.onEvent(AddContactEvents.OnChangePhoneNumberField(phone)) },
                onChangeAddress = { address -> viewModel.onEvent(AddContactEvents.OnChangeAddressField(address)) },
                onCountryCodeClicked = { viewModel.onEvent(AddContactEvents.CountryCodeEvent) },
                onSaveClick = { viewModel.onEvent(AddContactEvents.OnSaveClick) }
            )
            CountrySelectorPicker(
                isVisible = isCountrySelectorVisible,
                searchValue = uiState.phoneState.searchCountryValue,
                title = stringResource(Res.string.modal_country_code_title),
                placeholder = stringResource(Res.string.modal_country_code_search_title),
                countries = uiState.phoneState.countries,
                onSearchValueChange = { search ->
                    viewModel.onEvent(AddContactEvents.OnChangeSearchField(search))
                },
                onDismiss = { viewModel.onEvent(AddContactEvents.CloseCountryCodeEvent) },
                onCountrySelect = { country -> viewModel.onEvent(AddContactEvents.OnCountryCodeField(country)) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactForm(
    modifier: Modifier = Modifier,
    uiState: AddContactState,
    onChangeName: (String) -> Unit,
    onChangeLastName: (String) -> Unit,
    onChangeEmail: (String) -> Unit,
    onChangeDateOfBirth: (Long) -> Unit,
    onChangePhoneNumber: (String) -> Unit,
    onChangeAddress: (String) -> Unit,
    onSaveClick: () -> Unit,
    onCountryCodeClicked: () -> Unit
) {
    Column(modifier = modifier) {
        InputFieldText(
            inputLabel = stringResource(Res.string.add_contact_form_name_field),
            keyboardCapitalization = KeyboardCapitalization.Words,
            focusDirection = FocusDirection.Right,
            imeAction = ImeAction.Next,
            value = uiState.name,
            isError = uiState.nameError.isNotEmpty(),
            errorLabel = uiState.nameError,
            onValueChange = onChangeName
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputFieldText(
            inputLabel = stringResource(Res.string.add_contact_form_lastname_field),
            keyboardCapitalization = KeyboardCapitalization.Words,
            focusDirection = FocusDirection.Down,
            imeAction = ImeAction.Next,
            value = uiState.lastName,
            isError = uiState.lastNameError.isNotEmpty(),
            errorLabel = uiState.lastNameError,
            onValueChange = onChangeLastName
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputFieldText(
            inputLabel = stringResource(Res.string.add_contact_form_email_field),
            focusDirection = FocusDirection.Down,
            imeAction = ImeAction.Next,
            value = uiState.email,
            isError = uiState.emailError.isNotEmpty(),
            errorLabel = uiState.emailError,
            onValueChange = onChangeEmail
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputDateFieldText(
            value = uiState.dateOfBirth,
            inputLabel = stringResource(Res.string.add_contact_form_birth_date_field),
            isError = uiState.dateOfBirthError.isNotEmpty(),
            errorLabel = uiState.dateOfBirthError,
            datePickerState = rememberDatePickerState(),
            onSelectedDate = onChangeDateOfBirth
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputCountryCodeFieldText(
            state = uiState.phoneState,
            inputLabel = stringResource(Res.string.add_contact_form_phone_field),
            focusDirection = FocusDirection.Down,
            imeAction = ImeAction.Next,
            isError = uiState.phoneState.error.isNotEmpty(),
            errorLabel = uiState.phoneState.error,
            onChangePhoneNumber = onChangePhoneNumber,
            onCountryCodeClicked = onCountryCodeClicked
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputFieldText(
            inputLabel = stringResource(Res.string.add_contact_form_address_field),
            value = uiState.address,
            isError = uiState.addressError.isNotEmpty(),
            errorLabel = uiState.addressError,
            onValueChange = onChangeAddress,
            imeAction = ImeAction.Done
        )
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            text = stringResource(Res.string.add_contact_form_register_label),
            onClick = onSaveClick,
            backgroundColor = Green100
        )
    }
}