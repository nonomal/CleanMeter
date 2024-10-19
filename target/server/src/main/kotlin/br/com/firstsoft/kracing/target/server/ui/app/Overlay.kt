package ui.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font
import mahm.MahmReader
import ui.OverlayUi

val fontFamily = FontFamily(
    Font(
        resource = "font/inter.ttf"
    ),
)

@Composable
fun Overlay(
    mahmReader: MahmReader = MahmReader(),
    overlaySettings: OverlaySettings,
) = MaterialTheme(
    typography = Typography(defaultFontFamily = fontFamily),
) {
    val arrangement = when (overlaySettings.positionIndex) {
        0 -> Alignment.TopStart
        1 -> Alignment.TopCenter
        2 -> Alignment.TopEnd
        3 -> Alignment.BottomStart
        4 -> Alignment.BottomCenter
        5 -> Alignment.BottomEnd
        else -> Alignment.Center
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = arrangement) {
        OverlayUi(mahmReader, overlaySettings)
    }
}
