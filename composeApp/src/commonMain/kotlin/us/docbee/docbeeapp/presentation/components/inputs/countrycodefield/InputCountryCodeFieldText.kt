package us.docbee.docbeeapp.presentation.components.inputs.countrycodefield

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.ic_chevron_down
import org.jetbrains.compose.resources.vectorResource
import us.docbee.docbeeapp.presentation.theme.Black100
import us.docbee.docbeeapp.presentation.theme.Blue100
import us.docbee.docbeeapp.presentation.theme.Gray
import us.docbee.docbeeapp.presentation.theme.Gray300
import us.docbee.docbeeapp.utils.COUNTRY_DEFAULT_ICON

@Composable
fun InputCountryCodeFieldText(
    modifier: Modifier = Modifier,
    state: PhoneInputState,
    inputLabel: String,
    onChangePhoneNumber: (String) -> Unit,
    onCountryCodeClicked: () -> Unit = { },
    isError: Boolean = false,
    errorLabel: String = "",
    keyboardType: KeyboardType = KeyboardType.Phone,
    keyboardCapitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    focusDirection: FocusDirection = FocusDirection.Down,
    imeAction: ImeAction = ImeAction.Next,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = inputLabel,
            style = MaterialTheme.typography.labelMedium,
            color = if (isError) MaterialTheme.colorScheme.error else Gray
        )
        OutlinedTextField(
            modifier = modifier.fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused && !hasFocus) {
                        hasFocus = true
                    }
                    if (!focusState.isFocused) {
                        hasFocus = false
                    }
                },
            value = state.phoneNumber,
            onValueChange = onChangePhoneNumber,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Gray300,
                focusedBorderColor = Blue100,
                focusedTextColor = Black100,
                unfocusedTextColor = Black100
            ),
            textStyle = MaterialTheme.typography.bodyMedium,
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(
                capitalization = keyboardCapitalization,
                autoCorrectEnabled = false,
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(focusDirection) }
            ),
            leadingIcon = {
                CountryCodeIcon(
                    modifier = Modifier.width(80.dp)
                        .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                        .padding(end = 12.dp)
                        .clickable { onCountryCodeClicked() }
                        .fillMaxHeight()
                        .padding(start = 12.dp),
                    hasFocus = hasFocus,
                    icon = state.selectedCountry?.flagAssetPath ?: COUNTRY_DEFAULT_ICON
                )
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

@Composable
fun CountryCodeIcon(
    modifier: Modifier = Modifier,
    icon: String,
    hasFocus: Boolean
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier.size(18.dp).clip(CircleShape),
                model = Res.getUri(icon),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                modifier = Modifier.size(12.dp),
                imageVector = vectorResource(Res.drawable.ic_chevron_down),
                colorFilter = ColorFilter.tint(color = Gray),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        VerticalDivider(
            modifier = Modifier.height(56.dp).align(Alignment.CenterEnd),
            thickness = if (!hasFocus) 1.dp else 2.dp,
            color = if (!hasFocus) Gray300 else Blue100
        )
    }
}