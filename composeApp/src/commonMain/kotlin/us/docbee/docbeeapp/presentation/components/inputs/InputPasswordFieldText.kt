package us.docbee.docbeeapp.presentation.components.inputs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.ic_eye_off
import docbee.composeapp.generated.resources.ic_eye_on
import org.jetbrains.compose.resources.vectorResource
import us.docbee.docbeeapp.presentation.components.inputs.transformations.AsteriskPasswordVisualTransformation

@Composable
fun InputPasswordFieldText(
    modifier: Modifier = Modifier,
    inputLabel: String,
    value: String,
    onValueChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Next,
    focusDirection: FocusDirection = FocusDirection.Down
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    InputFieldText(
        modifier = modifier,
        inputLabel = inputLabel,
        value = value,
        onValueChange = onValueChange,
        trailingIcon = if (!isPasswordVisible) {
            vectorResource(Res.drawable.ic_eye_off)
        } else {
            vectorResource(Res.drawable.ic_eye_on)
        },
        visualTransformation = if (!isPasswordVisible) {
            AsteriskPasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        onTrailingIconClick = { isPasswordVisible = !isPasswordVisible },
        focusDirection = focusDirection,
        imeAction = imeAction,
        keyboardCapitalization = KeyboardCapitalization.None,
        keyboardType = KeyboardType.Password
    )
}