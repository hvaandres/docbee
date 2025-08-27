package us.docbee.docbeeapp.presentation.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.general_label_archive
import docbee.composeapp.generated.resources.general_label_delete
import docbee.composeapp.generated.resources.ic_female
import docbee.composeapp.generated.resources.ic_male
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import us.docbee.docbeeapp.presentation.theme.Black
import us.docbee.docbeeapp.presentation.theme.Blue300
import us.docbee.docbeeapp.presentation.theme.Green100
import us.docbee.docbeeapp.presentation.theme.Red100
import us.docbee.docbeeapp.presentation.theme.White
import kotlin.math.roundToInt

@Composable
fun ContactCard(
    modifier: Modifier = Modifier,
    gender: String,
    fullName: String,
    address: String,
    position: Int,
    swipeState: SwipeState,
    onArchive: () -> Unit = { },
    onDelete: () -> Unit = { },
    onClick: () -> Unit = { },
    onSwipeChanged: (SwipeState) -> Unit = { }
) {
    val isOdd = remember { position % 2 != 0 }
    val icon = remember { if (gender == "M") Res.drawable.ic_male else Res.drawable.ic_female }

    val density = LocalDensity.current
    val actionsWidthPx = with(density) { 160.dp.toPx() }
    val state = remember {
        AnchoredDraggableState(
            initialValue = SwipeState.Closed,
            anchors = DraggableAnchors {
                SwipeState.Closed at 0f
                SwipeState.Open at -actionsWidthPx
            }
        )
    }

    LaunchedEffect(swipeState) {
        state.animateTo(swipeState)
    }

    LaunchedEffect(state.currentValue) {
        onSwipeChanged(state.currentValue)
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        BackgroundContactItem(
            modifier = modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            onArchive = onArchive,
            onDelete = onDelete
        )
        Box(
            modifier = modifier.fillMaxWidth()
                .offset { IntOffset(x = state.requireOffset().roundToInt(), y = 0) }
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Horizontal,
                    flingBehavior = AnchoredDraggableDefaults.flingBehavior(
                        state = state,
                        animationSpec = tween(200)
                    )
                )
        ) {
            ForegroundContactItem(
                fullName = fullName,
                address = address,
                isOdd = isOdd,
                icon = icon,
                onClick = onClick
            )
        }
    }
}

@Composable
fun BackgroundContactItem(
    modifier: Modifier = Modifier,
    onArchive: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = modifier.width(160.dp).height(88.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        BackgroundContactButton(
            text = stringResource(Res.string.general_label_archive),
            onClick = onArchive,
            color = Blue300
        )
        BackgroundContactButton(
            text = stringResource(Res.string.general_label_delete),
            onClick = onDelete,
            color = Red100
        )
    }
}

@Composable
fun BackgroundContactButton(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.width(80.dp).fillMaxHeight()
            .background(color = color, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun ForegroundContactItem(
    modifier: Modifier = Modifier,
    fullName: String,
    address: String,
    isOdd: Boolean,
    icon: DrawableResource,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth()
            .clickable { onClick() }
            .background(color = if (isOdd) Black else Green100, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(64.dp)
                .background(color = White, shape = CircleShape)
                .padding(12.dp),
            painter = painterResource(icon),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = fullName,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = if (isOdd) White else Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = address,
                maxLines = 2,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Light,
                color = if (isOdd) White else Black
            )
        }
    }
}

enum class SwipeState { Closed, Open }