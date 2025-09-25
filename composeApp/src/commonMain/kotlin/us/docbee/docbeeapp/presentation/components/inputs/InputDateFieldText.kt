package us.docbee.docbeeapp.presentation.components.inputs

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.general_label_cancel
import docbee.composeapp.generated.resources.general_label_ok
import docbee.composeapp.generated.resources.ic_calendar
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import us.docbee.docbeeapp.presentation.theme.Black100
import us.docbee.docbeeapp.presentation.theme.Blue100
import us.docbee.docbeeapp.presentation.theme.Gray
import us.docbee.docbeeapp.presentation.theme.Gray300
import us.docbee.docbeeapp.presentation.theme.Gray400
import us.docbee.docbeeapp.presentation.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputDateFieldText(
    modifier: Modifier = Modifier,
    value: String,
    inputLabel: String,
    datePickerState: DatePickerState,
    isError: Boolean = false,
    errorLabel: String = "",
    onAcceptFocusDirection: FocusDirection = FocusDirection.Down,
    onCancelFocusDirection: FocusDirection = FocusDirection.Up,
    onSelectedDate: (Long) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }
    var hasFocus by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Long?>(null) }
    var showModal by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Release) {
                showModal = true
            }
        }
    }

    Box(modifier = Modifier) {
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
                            showModal = true
                            hasFocus = true
                        }
                        if (!focusState.isFocused) {
                            hasFocus = false
                        }
                    },
                value = value,
                isError = isError,
                onValueChange = { },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Gray300,
                    focusedBorderColor = Blue100,
                    focusedTextColor = Black100,
                    unfocusedTextColor = Black100,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                ),
                textStyle = MaterialTheme.typography.bodyMedium,
                shape = RoundedCornerShape(10.dp),
                interactionSource = interactionSource,
                readOnly = true,
                trailingIcon = {
                    Image(
                        modifier = Modifier.size(16.dp),
                        imageVector = vectorResource(Res.drawable.ic_calendar),
                        colorFilter = ColorFilter.tint(color = Gray400),
                        contentDescription = null
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

        if (showModal) {
            DatePickerDialog(
                onDismissRequest = { showModal = false },
                confirmButton = {
                    DatePickerButtons(
                        text = stringResource(Res.string.general_label_ok),
                        onClick = {
                            selectedDate = datePickerState.selectedDateMillis
                            selectedDate?.let { onSelectedDate(it) }
                            showModal = false
                            focusManager.moveFocus(onAcceptFocusDirection)
                        }
                    )
                },
                dismissButton = {
                    DatePickerButtons(
                        text = stringResource(Res.string.general_label_cancel),
                        onClick = {
                            showModal = false
                            focusManager.moveFocus(onCancelFocusDirection)
                        }
                    )
                }
            ) {
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.sizeIn(maxWidth = 350.dp)
                )
            }
        }
    }
}

@Composable
fun DatePickerButtons(
    text: String,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = Gray
        )
    }
}