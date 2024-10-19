package br.com.firstsoft.target.server

import PreferencesRepository
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ui.PREFERENCE_START_MINIMIZED
import ui.app.Overlay
import ui.app.OverlaySettings
import ui.app.Settings
import win32.WindowsService

val positions = listOf(
    Alignment.TopStart,
    Alignment.TopCenter,
    Alignment.TopEnd,
    Alignment.BottomStart,
    Alignment.BottomCenter,
    Alignment.BottomEnd,
)

fun main() = application {
    var overlaySettings by remember { mutableStateOf(OverlaySettings()) }

    OverlayWindow(overlaySettings)

    SettingsWindow {
        overlaySettings = it
    }
}

@Composable
private fun ApplicationScope.OverlayWindow(overlaySettings: OverlaySettings) {
    val alignment = remember(overlaySettings.positionIndex) { positions[overlaySettings.positionIndex] }
    val overlayState = rememberWindowState().apply {
        size = if (overlaySettings.isHorizontal) DpSize(1024.dp, 80.dp) else DpSize(350.dp, 1024.dp)
        placement = WindowPlacement.Floating
        position = WindowPosition.Aligned(alignment)
    }

    Window(
        state = overlayState,
        onCloseRequest = { exitApplication() },
        visible = true,
        title = "Clean Meter",
        resizable = false,
        alwaysOnTop = true,
        transparent = true,
        undecorated = true,
        focusable = false,
        enabled = false
    ) {
        WindowsService.makeComponentTransparent(window)
        Overlay(overlaySettings = overlaySettings)
    }
}

@Composable
private fun ApplicationScope.SettingsWindow(onOverlaySettings: (OverlaySettings) -> Unit) {
    val icon = painterResource("imgs/logo.png")
    val state = rememberWindowState().apply {
        size = DpSize(500.dp, 500.dp)
    }
    var isVisible by remember {
        mutableStateOf(
            PreferencesRepository.getPreferenceBooleanNullable(
                PREFERENCE_START_MINIMIZED
            )?.not() ?: true
        )
    }

    Window(
        state = state,
        onCloseRequest = { isVisible = false },
        icon = icon,
        visible = isVisible,
        title = "Clean Meter",
        resizable = false
    ) {
        Settings(onOverlaySettings = onOverlaySettings)
    }

    if (!isVisible) {
        Tray(
            icon = icon,
            onAction = { isVisible = true },
            menu = {
                Item("Quit", onClick = ::exitApplication)
            }
        )
    }
}
