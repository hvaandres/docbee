package us.docbee.docbeeapp.presentation.components

import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DefaultAlert(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    confirmText: String,
    cancelText: String,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = { Text(text = title) },
        text = { Text(text = description) },
        confirmButton = {
            PrimaryButton(
                modifier = Modifier.wrapContentWidth(),
                text = confirmText,
                onClick = onConfirmClick
            )
        },
        dismissButton = {
            PrimaryButton(
                text = cancelText,
                onClick = onCancelClick
            )
        }
    )
}