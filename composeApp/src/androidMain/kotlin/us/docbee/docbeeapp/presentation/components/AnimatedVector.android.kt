package us.docbee.docbeeapp.presentation.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
actual fun AnimatedVectorNative(
    modifier: Modifier,
    path: String
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.JsonString(path))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
    LottieAnimation(
        modifier = modifier.aspectRatio(1f),
        composition = composition,
        progress = { progress }
    )
}