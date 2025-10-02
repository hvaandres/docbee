package us.docbee.docbeeapp.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularLayout(
    modifier: Modifier = Modifier,
    radius: Dp = 100.dp,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        val radiusPx = with(density) { radius.toPx() }

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }

        val maxChildWidth = placeables.maxOf { it.width }
        val maxChildHeight = placeables.maxOf { it.height }

        val desiredWidth = (2 * radiusPx + maxChildWidth).toInt()
        val desiredHeight = (2 * radiusPx + maxChildHeight).toInt()

        val layoutWidth = constraints.constrainWidth(desiredWidth)
        val layoutHeight = constraints.constrainHeight(desiredHeight)

        val centerX = layoutWidth / 2
        val centerY = layoutHeight / 2

        val angleIncrement = 360f / placeables.size

        layout(width = layoutWidth, height = layoutHeight) {
            placeables.forEachIndexed { index, placeable ->
                val angle = index * angleIncrement - 90f
                val radians = angle * (PI / 180.0)
                val x = centerX + (radiusPx * cos(radians)).toFloat() - placeable.width / 2
                val y = centerY + (radiusPx * sin(radians)).toFloat() - placeable.height / 2

                placeable.placeRelative(x = x.toInt(), y = y.toInt())
            }
        }

    }
}