package us.docbee.docbeeapp.utils.ui

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import docbee.composeapp.generated.resources.Res
import docbee.composeapp.generated.resources.allDrawableResources
import docbee.composeapp.generated.resources.ic_contact
import org.jetbrains.compose.resources.DrawableResource

fun String.getDrawable(): DrawableResource {
    return Res.allDrawableResources[toLowerCase(Locale.current)] ?: Res.drawable.ic_contact
}