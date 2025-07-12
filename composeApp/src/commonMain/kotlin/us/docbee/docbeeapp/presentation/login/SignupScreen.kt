package us.docbee.docbeeapp.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
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
import docbee.composeapp.generated.resources.ic_calendar
import docbee.composeapp.generated.resources.signup_form_birth_date_field
import docbee.composeapp.generated.resources.signup_form_email_field
import docbee.composeapp.generated.resources.signup_form_lastname_field
import docbee.composeapp.generated.resources.signup_form_name_field
import docbee.composeapp.generated.resources.signup_form_password_field
import docbee.composeapp.generated.resources.signup_form_phone_field
import docbee.composeapp.generated.resources.signup_form_register_label
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import us.docbee.docbeeapp.presentation.components.PrimaryButton
import us.docbee.docbeeapp.presentation.components.inputs.InputFieldText
import us.docbee.docbeeapp.presentation.components.inputs.InputPasswordFieldText

@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    isSignupTabbed: Boolean,
    onClick: () -> Unit = { }
) {
    var nameField by remember { mutableStateOf("") }
    var lastNameField by remember { mutableStateOf("") }
    var emailField by remember { mutableStateOf("") }
    var dateOfBirthField by remember { mutableStateOf("") }
    var phoneNumberField by remember { mutableStateOf("") }
    var passwordField by remember { mutableStateOf("") }
    if (!isSignupTabbed) return
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
                value = nameField,
                onValueChange = { nameField = it }
            )
            InputFieldText(
                modifier = Modifier.weight(1f),
                inputLabel = stringResource(Res.string.signup_form_lastname_field),
                keyboardCapitalization = KeyboardCapitalization.Words,
                focusDirection = FocusDirection.Down,
                imeAction = ImeAction.Next,
                value = lastNameField,
                onValueChange = { lastNameField = it }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        InputFieldText(
            inputLabel = stringResource(Res.string.signup_form_email_field),
            focusDirection = FocusDirection.Down,
            imeAction = ImeAction.Next,
            value = emailField,
            onValueChange = { emailField = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputFieldText(
            inputLabel = stringResource(Res.string.signup_form_birth_date_field),
            trailingIcon = vectorResource(Res.drawable.ic_calendar),
            focusDirection = FocusDirection.Down,
            imeAction = ImeAction.Next,
            value = dateOfBirthField,
            onValueChange = { dateOfBirthField = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputFieldText(
            inputLabel = stringResource(Res.string.signup_form_phone_field),
            focusDirection = FocusDirection.Down,
            imeAction = ImeAction.Next,
            value = phoneNumberField,
            onValueChange = { phoneNumberField = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputPasswordFieldText(
            inputLabel = stringResource(Res.string.signup_form_password_field),
            value = passwordField,
            onValueChange = { passwordField = it },
            imeAction = ImeAction.Done
        )
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            text = stringResource(Res.string.signup_form_register_label),
            onClick = onClick
        )
    }
}