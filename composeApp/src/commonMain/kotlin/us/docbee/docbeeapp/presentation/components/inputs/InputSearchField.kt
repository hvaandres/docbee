package us.docbee.docbeeapp.presentation.components.inputs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.ic_close
import docbee.composeapp.generated.resources.ic_search
import org.jetbrains.compose.resources.vectorResource
import us.docbee.docbeeapp.presentation.theme.Gray100
import us.docbee.docbeeapp.presentation.theme.Gray600

@Composable
fun InputSearchField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Gray100,
            focusedBorderColor = Gray100,
            focusedTextColor = Gray600,
            unfocusedTextColor = Gray600,
            focusedContainerColor = Gray100,
            unfocusedContainerColor = Gray100
        ),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.titleMedium,
                color = Gray600
            )
        },
        leadingIcon = {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_search),
                tint = Gray600,
                contentDescription = null
            )
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.ic_close),
                        tint = Gray600,
                        contentDescription = null
                    )
                }
            }
        }
    )
}
