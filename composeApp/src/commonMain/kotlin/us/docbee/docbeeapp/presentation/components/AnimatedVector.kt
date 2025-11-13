package us.docbee.docbeeapp.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import docbee.composeapp.generated.resources.Res

@Composable
expect fun AnimatedVectorNative(modifier: Modifier = Modifier, path: String)

@Composable
fun AnimatedVector(modifier: Modifier = Modifier, location: String) {
    val animatedVector = remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        animatedVector.value = Res.readBytes(location).decodeToString()
    }
    animatedVector.value?.let { vector ->
        AnimatedVectorNative(modifier = modifier, path = vector)
    }
}