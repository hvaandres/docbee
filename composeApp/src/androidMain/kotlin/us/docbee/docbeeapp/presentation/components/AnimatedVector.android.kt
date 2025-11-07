package us.docbee.docbeeapp.presentation.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource

@Composable
actual fun AnimatedVector(
    modifier: Modifier,
    path: String
) {
    DotLottieAnimation(
        modifier = modifier.aspectRatio(1f),
        source = DotLottieSource.Json(path),
        autoplay = true,
        loop = true
    )
}