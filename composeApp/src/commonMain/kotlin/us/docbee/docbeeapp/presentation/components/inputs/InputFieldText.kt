package us.docbee.docbeeapp.presentation.components.inputs

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import us.docbee.docbeeapp.presentation.theme.Black100
import us.docbee.docbeeapp.presentation.theme.Blue100
import us.docbee.docbeeapp.presentation.theme.Gray
import us.docbee.docbeeapp.presentation.theme.Gray300
import us.docbee.docbeeapp.presentation.theme.Gray400

@Composable
fun InputFieldText(
    modifier: Modifier = Modifier,
    inputLabel: String,
    value: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    focusDirection: FocusDirection = FocusDirection.Down,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    trailingIcon: ImageVector? = null,
    isError: Boolean = false,
    errorLabel: String = "",
    onValueChange: (String) -> Unit,
    onTrailingIconClick: () -> Unit = { }
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = inputLabel,
            style = MaterialTheme.typography.labelMedium,
            color = if (isError) MaterialTheme.colorScheme.error else Gray
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            isError = isError,
            visualTransformation = visualTransformation,
            textStyle = MaterialTheme.typography.bodyMedium,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Gray300,
                focusedBorderColor = Blue100,
                focusedTextColor = Black100,
                unfocusedTextColor = Black100
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = keyboardCapitalization,
                autoCorrectEnabled = false,
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(focusDirection) }
            ),
            trailingIcon = {
                if (trailingIcon != null) {
                    Image(
                        modifier = Modifier.size(16.dp).clickable { onTrailingIconClick() },
                        imageVector = trailingIcon,
                        colorFilter = ColorFilter.tint(color = Gray400),
                        contentDescription = null
                    )
                }
            },
            supportingText = {
                if (isError && errorLabel.isNotEmpty()) {
                    Text(
                        text = errorLabel,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        )
    }
}