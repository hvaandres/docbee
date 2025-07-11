package us.docbee.docbeeapp.presentation.components.inputs.transformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class AsteriskPasswordVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val transformed = "*".repeat(text.text.length)
        return TransformedText(
            AnnotatedString(transformed),
            OffsetMapping.Identity
        )
    }
}