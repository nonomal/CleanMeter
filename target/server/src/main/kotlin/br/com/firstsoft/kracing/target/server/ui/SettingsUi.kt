package ui

import Label
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import PreferencesRepository
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import br.com.firstsoft.kracing.target.server.ui.components.CheckboxWithLabel
import win32.WinRegistry

const val PREFERENCE_START_MINIMIZED = "PREFERENCE_START_MINIMIZED"

@Composable
fun AppSettings() = Column {
    Column(
        modifier = Modifier.weight(1f).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        startWithWindowsCheckbox()
        startMinimizedCheckbox()
    }
    Box(modifier = Modifier.weight(0.1f)) {
        FooterUi()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun startWithWindowsCheckbox() {
    var state by remember { mutableStateOf(WinRegistry.isAppRegisteredToStartWithWindows()) }

    CheckboxWithLabel(
        label = "Start with Windows",
        checked = state,
        onCheckedChange = { value ->
            state = value
            if (value) {
                WinRegistry.registerAppToStartWithWindows()
            } else {
                WinRegistry.removeAppFromStartWithWindows()
            }
        }
    ) {
        TooltipArea({
            Label(text = "Admin rights needed")
        }) {
            Icon(imageVector = Icons.Filled.AdminPanelSettings, null)
        }
    }
}

@Composable
private fun startMinimizedCheckbox() {
    var state by remember { mutableStateOf(PreferencesRepository.getPreferenceBoolean(PREFERENCE_START_MINIMIZED)) }
    CheckboxWithLabel(
        label = "Start Minimized",
        checked = state,
        onCheckedChange = { value ->
            state = value
            PreferencesRepository.setPreferenceBoolean(PREFERENCE_START_MINIMIZED, value)
        },
    )
}
