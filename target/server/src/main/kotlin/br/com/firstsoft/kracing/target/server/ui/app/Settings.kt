package ui.app

import Label
import PreferencesRepository
import Title
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.firstsoft.kracing.target.server.ui.components.CheckboxWithLabel
import br.com.firstsoft.kracing.target.server.ui.components.DropdownMenu
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ui.AppSettings

const val OVERLAY_SETTINGS_PREFERENCE_KEY = "OVERLAY_SETTINGS_PREFERENCE_KEY"

val positionsLabels = listOf(
    "Top Start",
    "Top Center",
    "Top End",
    "Bottom Start",
    "Bottom Center",
    "Bottom End",
)

@Composable
fun Settings(
    onOverlaySettings: (OverlaySettings) -> Unit,
) = MaterialTheme {
    Column(modifier = Modifier.fillMaxSize()) {

        var selectedTabIndex by remember { mutableStateOf(0) }

        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(selected = selectedTabIndex == 0, onClick = { selectedTabIndex = 0 }) {
                Title("Overlay Settings")
            }
            Tab(selected = selectedTabIndex == 1, onClick = { selectedTabIndex = 1 }) {
                Title("App Settings")
            }
        }

        when (selectedTabIndex) {
            0 -> OverlaySettingsUi(onOverlaySettings)
            1 -> AppSettings()
            else -> Unit
        }
    }
}

@Serializable
data class OverlaySettings(
    val isHorizontal: Boolean = true,
    val positionIndex: Int = 0,
    val fps: Boolean = true,
    val frametime: Boolean = false,
    val cpuTemp: Boolean = true,
    val cpuUsage: Boolean = true,
    val gpuTemp: Boolean = true,
    val gpuUsage: Boolean = true,
    val vramUsage: Boolean = false,
    val ramUsage: Boolean = false,
)

@Composable
private fun OverlaySettingsUi(
    onOverlaySettings: (OverlaySettings) -> Unit,
) = Column(
    modifier = Modifier
        .padding(bottom = 8.dp)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    var overlaySettings by remember {
        val json = PreferencesRepository.getPreferenceString(OVERLAY_SETTINGS_PREFERENCE_KEY)
        val settings = if (json != null) {
            Json.decodeFromString<OverlaySettings>(json)
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
    Divider()

    Label("Orientation")
    CheckboxWithLabel(
        label = "Use Horizontal?",
        onCheckedChange = { overlaySettings = overlaySettings.copy(isHorizontal = it) },
        checked = overlaySettings.isHorizontal,
    )

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
