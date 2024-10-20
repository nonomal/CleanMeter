package br.com.firstsoft.target.server.ui

import Label
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.firstsoft.target.server.ui.components.CheckboxWithLabel
import br.com.firstsoft.target.server.ui.components.DropdownMenu
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ui.app.OVERLAY_SETTINGS_PREFERENCE_KEY
import ui.app.OverlaySettings
import ui.app.positionsLabels
import java.awt.GraphicsEnvironment

@Composable
fun OverlaySettingsUi(
    onOverlaySettings: (OverlaySettings) -> Unit,
) = Column(
    modifier = Modifier
        .padding(bottom = 8.dp)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    val screenDevices = remember { GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices }

    var overlaySettings by remember {
        val json = PreferencesRepository.getPreferenceString(OVERLAY_SETTINGS_PREFERENCE_KEY)
        val settings = if (json != null) {
            try {
                Json.decodeFromString<OverlaySettings>(json)
            } catch (e: Exception) {
                OverlaySettings()
            }
        } else {
            OverlaySettings()
        }
        mutableStateOf(settings)
    }

    LaunchedEffect(overlaySettings) {
        PreferencesRepository.setPreference(OVERLAY_SETTINGS_PREFERENCE_KEY, Json.encodeToString(overlaySettings))
        onOverlaySettings(overlaySettings)
    }

    DropdownMenu(
        title = "Overlay Position",
        options = positionsLabels,
        selectedIndex = overlaySettings.positionIndex,
        onValueChanged = { overlaySettings = overlaySettings.copy(positionIndex = it) }
    )
    DropdownMenu(
        title = "Selected Display",
        options = screenDevices.map { it.defaultConfiguration.device.iDstring },
        selectedIndex = overlaySettings.selectedDisplayIndex,
        onValueChanged = { overlaySettings = overlaySettings.copy(selectedDisplayIndex = it) }
    )
    Divider()

    DropdownMenu(
        title = "Graph Type",
        options = OverlaySettings.ProgressType.entries.map { it.name },
        selectedIndex = overlaySettings.progressType.ordinal,
        onValueChanged = {
            overlaySettings = overlaySettings.copy(progressType = OverlaySettings.ProgressType.entries[it])
        }
    )
    Divider()

    Label("Orientation")
    CheckboxWithLabel(
        label = "Use Horizontal?",
        onCheckedChange = { overlaySettings = overlaySettings.copy(isHorizontal = it) },
        checked = overlaySettings.isHorizontal,
    )
    Divider()

    Label("FPS Settings")
    CheckboxWithLabel(
        label = "Frame Count",
        onCheckedChange = { overlaySettings = overlaySettings.copy(fps = it) },
        checked = overlaySettings.fps
    )
    CheckboxWithLabel(
        label = "Frame Time",
        onCheckedChange = { overlaySettings = overlaySettings.copy(frametime = it) },
        checked = overlaySettings.frametime
    )
    Divider()

    Label("GPU Settings")
    CheckboxWithLabel(
        label = "GPU Temperature",
        onCheckedChange = { overlaySettings = overlaySettings.copy(gpuTemp = it) },
        checked = overlaySettings.gpuTemp
    )
    CheckboxWithLabel(
        label = "GPU Usage",
        onCheckedChange = { overlaySettings = overlaySettings.copy(gpuUsage = it) },
        checked = overlaySettings.gpuUsage
    )
    CheckboxWithLabel(
        label = "VRAM Usage",
        onCheckedChange = { overlaySettings = overlaySettings.copy(vramUsage = it) },
        checked = overlaySettings.vramUsage
    )
    Divider()

    Label("CPU Settings")
    CheckboxWithLabel(
        label = "CPU Temperature",
        onCheckedChange = { overlaySettings = overlaySettings.copy(cpuTemp = it) },
        checked = overlaySettings.cpuTemp
    )
    CheckboxWithLabel(
        label = "CPU Usage",
        onCheckedChange = { overlaySettings = overlaySettings.copy(cpuUsage = it) },
        checked = overlaySettings.cpuUsage
    )
    Divider()

    Label("RAM Settings")
    CheckboxWithLabel(
        label = "RAM Usage",
        onCheckedChange = { overlaySettings = overlaySettings.copy(ramUsage = it) },
        checked = overlaySettings.ramUsage
    )
}