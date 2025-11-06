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
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import docbee.composeapp.generated.resources.alerts_item_delete_alert
import docbee.composeapp.generated.resources.alerts_item_edit_alert
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import us.docbee.docbeeapp.presentation.theme.Black
import us.docbee.docbeeapp.presentation.theme.White
import kotlin.math.roundToInt

@Composable
fun CardDescriptionItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    icon: DrawableResource,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit = { },
    onEditClick: () -> Unit = { },
    isSwipeable: Boolean = false,
    swipeState: HorizontalSwipeState = HorizontalSwipeState.Closed,
    onSwipeChanged: (HorizontalSwipeState) -> Unit = { }
) {

    if (!isSwipeable) {
        StaticCardDescriptionItem(
            modifier = modifier,
            title = title,
            description = description,
            icon = icon,
            onClick = onClick
        )
    } else {
        SwipeableCardDescriptionItem(
            modifier = modifier,
            title = title,
            description = description,
            icon = icon,
            onClick = onClick,
            onDeleteClick = onDeleteClick,
            onEditClick = onEditClick,
            swipeState = swipeState,
            onSwipeChanged = onSwipeChanged
        )
    }
}

@Composable
fun SwipeableCardDescriptionItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    icon: DrawableResource,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    swipeState: HorizontalSwipeState,
    onSwipeChanged: (HorizontalSwipeState) -> Unit = { }
){
    val density = LocalDensity.current
    val actionsWidthPx = with(density) { 124.dp.toPx() }
    val state = remember {
        AnchoredDraggableState(
            initialValue = HorizontalSwipeState.Closed,
            anchors = DraggableAnchors {
                HorizontalSwipeState.Closed at 0f
                HorizontalSwipeState.OpenLeft at actionsWidthPx
                HorizontalSwipeState.OpenRight at -actionsWidthPx
            }
        )
    }

    LaunchedEffect(swipeState) {
        state.animateTo(swipeState)
    }

    LaunchedEffect(state.currentValue) {
        onSwipeChanged(state.currentValue)
    }

    Box(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BackgroundAlertButton(
                modifier = Modifier.fillMaxHeight(),
                text = stringResource(Res.string.alerts_item_delete_alert),
                onClick = onDeleteClick,
                color = White
            )
            BackgroundAlertButton(
                modifier = Modifier.fillMaxHeight(),
                text = stringResource(Res.string.alerts_item_edit_alert),
                onClick = onEditClick,
                color = White
            )
        }
        Box(
            modifier = Modifier.fillMaxWidth()
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
            StaticCardDescriptionItem(
                modifier = Modifier,
                title = title,
                description = description,
                icon = icon,
                onClick = onClick
            )
        }
    }
}

@Composable
fun StaticCardDescriptionItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    icon: DrawableResource,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = White, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = Black
            )
            Text(
                text = description,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Light,
                color = Black
            )
        }
        Image(
            modifier = Modifier.size(16.dp),
            imageVector = vectorResource(icon),
            contentDescription = null
        )
    }
}

@Composable
fun BackgroundAlertButton(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier.width(120.dp).fillMaxHeight()
            .background(color = color, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}

enum class HorizontalSwipeState { Closed, OpenLeft, OpenRight }