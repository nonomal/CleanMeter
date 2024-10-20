package ui.app

import Title
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import br.com.firstsoft.target.server.ui.AppTheme
import br.com.firstsoft.target.server.ui.OverlaySettingsUi
import kotlinx.serialization.Serializable
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
) = AppTheme {
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
    val progressType: ProgressType = ProgressType.Circular
) {
    @Serializable
    enum class ProgressType {
        Circular, Bar, None
    }
}
