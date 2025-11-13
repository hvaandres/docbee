package us.docbee.docbeeapp.presentation.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import kotlinx.cinterop.ExperimentalForeignApi
import us.docbee.docbeeapp.components.AnimatedVectorUIViewControllerFactory

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun AnimatedVectorNative(
    modifier: Modifier,
    path: String
) {
    UIKitViewController(
        modifier = modifier.aspectRatio(1f),
        factory = { AnimatedVectorUIViewControllerFactory.createWithContent(path) },
        update = { controller ->
            AnimatedVectorUIViewControllerFactory.updateWithController(controller, path)
        }
    )
}
