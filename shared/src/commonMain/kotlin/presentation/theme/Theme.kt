package presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ShowtimeLightColorScheme: ColorScheme = lightColorScheme(
    primary = ShowtimePrimary,
    onPrimary = ShowtimeOnPrimary,
    primaryContainer = ShowtimePrimaryContainer,
    onPrimaryContainer = ShowtimeOnPrimaryContainer,

    secondary = ShowtimeSecondary,
    onSecondary = ShowtimeOnSecondary,
    secondaryContainer = ShowtimeSecondaryContainer,
    onSecondaryContainer = ShowtimeOnSecondaryContainer,

    background = ShowtimeBackground,
    onBackground = ShowtimeOnBackground,

    surface = ShowtimeSurface,
    onSurface = ShowtimeOnSurface,

    surfaceVariant = ShowtimeSurfaceVariant,
    onSurfaceVariant = ShowtimeOnSurfaceVariant,

    outline = ShowtimeOutline,
    outlineVariant = ShowtimeOutlineVariant,

    error = ShowtimeError,
    onError = ShowtimeOnError,
    errorContainer = ShowtimeErrorContainer,
    onErrorContainer = ShowtimeOnErrorContainer
)

@Composable
fun ShowtimeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ShowtimeLightColorScheme,
        typography = ShowtimeTypography,
        shapes = ShowtimeShapes,
        content = content
    )
}