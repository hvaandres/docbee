package us.docbee.docbeeapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.ic_check_mark
import org.jetbrains.compose.resources.vectorResource
import us.docbee.docbeeapp.presentation.theme.Blue100
import us.docbee.docbeeapp.presentation.theme.Gray
import us.docbee.docbeeapp.presentation.theme.Transparent
import us.docbee.docbeeapp.presentation.theme.White

@Composable
fun CheckboxWithText(
    modifier: Modifier = Modifier,
    text: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = modifier
                .size(20.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(color = if (isChecked) Blue100 else Transparent)
                .border(width = 1.dp, color = Gray, shape = RoundedCornerShape(6.dp))
                .clickable(
                    indication = null,
                    interactionSource = null,
                    onClick = { onCheckedChange(!isChecked) }
                )
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_check_mark),
                tint = White,
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier.padding(start = 6.dp),
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = Gray
        )
    }
}