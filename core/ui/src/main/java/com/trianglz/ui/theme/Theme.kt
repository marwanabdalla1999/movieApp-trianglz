package com.core_ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf



enum class ThemeMode {
    LIGHT, DARK
}
val LocalThemeMode = compositionLocalOf { ThemeMode.LIGHT }

val LocalToggleTheme = staticCompositionLocalOf<() -> Unit> {
    error("No Theme toggle provided")
}

private val DarkColorScheme = darkColorScheme(
    primary = Purple80, secondary = PurpleGrey80, tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40, secondary = PurpleGrey40, tertiary = Pink40

)

@Composable
fun AspireThemeProvider(
    initialMode: ThemeMode = ThemeMode.LIGHT, content: @Composable () -> Unit
) {
    var themeMode by rememberSaveable { mutableStateOf(initialMode) }
    val toggleTheme = {
        themeMode = if (themeMode == ThemeMode.LIGHT) ThemeMode.DARK else ThemeMode.LIGHT
    }

    CompositionLocalProvider(
        LocalThemeMode provides themeMode, LocalToggleTheme provides toggleTheme
    ) {
        ThemeContent { content() }
    }
}

@Composable
private fun ThemeContent(content: @Composable () -> Unit) {
    val darkTheme = LocalThemeMode.current == ThemeMode.DARK

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme, typography = Typography, content = content
    )
}