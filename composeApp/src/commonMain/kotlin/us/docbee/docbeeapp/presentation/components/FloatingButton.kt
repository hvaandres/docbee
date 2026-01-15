package us.docbee.docbeeapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import us.docbee.docbeeapp.presentation.theme.White

@Composable
fun FloatingButton(
    modifier: Modifier = Modifier,
    background: Color = Color.Transparent,
    tintColor: Color = Color.White,
    onClick: () -> Unit
) {
    Image(
        modifier = modifier
            .padding(end = 28.dp, bottom = 28.dp)
            .size(42.dp)
            .background(color = background, CircleShape)
            .clickable { onClick() },
        imageVector = Icons.Outlined.AddCircleOutline,
        colorFilter = ColorFilter.tint(color = tintColor),
        contentDescription = null
    )
}