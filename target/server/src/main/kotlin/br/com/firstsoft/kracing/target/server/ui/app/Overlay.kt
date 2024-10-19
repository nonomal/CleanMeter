package ui.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
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
    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
        OverlayUi(mahmReader, overlaySettings)
    }
}
