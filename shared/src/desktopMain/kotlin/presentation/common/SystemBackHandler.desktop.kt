package presentation.common

import androidx.compose.runtime.Composable

@Composable
actual fun SystemBackHandler(
    enabled: Boolean,
    onBack: () -> Unit
) {
    // Desktop nema Android sistemski Back.
    // Za sada ne radimo ništa.
}