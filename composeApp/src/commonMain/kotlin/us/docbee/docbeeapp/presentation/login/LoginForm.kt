package us.docbee.docbeeapp.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.login_form_email_field
import docbee.composeapp.generated.resources.login_form_forgot_password
import docbee.composeapp.generated.resources.login_form_login_label
import docbee.composeapp.generated.resources.login_form_password_field
import docbee.composeapp.generated.resources.login_form_remember_check
import org.jetbrains.compose.resources.stringResource
import us.docbee.docbeeapp.presentation.components.CheckboxWithText
import us.docbee.docbeeapp.presentation.components.PrimaryButton
import us.docbee.docbeeapp.presentation.components.inputs.InputFieldText
import us.docbee.docbeeapp.presentation.components.inputs.InputPasswordFieldText
import us.docbee.docbeeapp.presentation.theme.Blue200

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {
    var rememberCheck by remember { mutableStateOf(false) }
    var emailField by remember { mutableStateOf("") }
    var passwordField by remember { mutableStateOf("") }
    Column(modifier = modifier) {
        InputFieldText(
            inputLabel = stringResource(Res.string.login_form_email_field),
            value = emailField,
            onValueChange = { emailField = it }
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputPasswordFieldText(
            inputLabel = stringResource(Res.string.login_form_password_field),
            value = passwordField,
            onValueChange = { passwordField = it },
            imeAction = ImeAction.Done
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CheckboxWithText(
                text = stringResource(Res.string.login_form_remember_check),
                isChecked = rememberCheck,
                onCheckedChange = { rememberCheck = it }
            )
            Text(
                text = stringResource(Res.string.login_form_forgot_password),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = Blue200
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        PrimaryButton(
            text = stringResource(Res.string.login_form_login_label),
            onClick = onClick
        )
    }
}