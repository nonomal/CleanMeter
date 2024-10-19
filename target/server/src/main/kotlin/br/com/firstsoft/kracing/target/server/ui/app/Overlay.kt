package ui.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import mahm.MahmReader
import ui.OverlayUi

@Composable
fun Overlay(
    mahmReader: MahmReader = MahmReader(),
    overlaySettings: OverlaySettings,
) = MaterialTheme {
    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
        OverlayUi(mahmReader, overlaySettings)
    }
}
