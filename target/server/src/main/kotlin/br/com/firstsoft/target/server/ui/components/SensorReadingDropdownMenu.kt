package br.com.firstsoft.target.server.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import br.com.firstsoft.core.common.hardwaremonitor.HardwareMonitorData
import br.com.firstsoft.target.server.ui.ColorTokens.AlmostVisibleGray
import br.com.firstsoft.target.server.ui.ColorTokens.BarelyVisibleGray
import br.com.firstsoft.target.server.ui.ColorTokens.DarkGray
import br.com.firstsoft.target.server.ui.ColorTokens.Gray200
import br.com.firstsoft.target.server.ui.ColorTokens.LabelGray
import br.com.firstsoft.target.server.ui.ColorTokens.MutedGray
import br.com.firstsoft.target.server.ui.overlay.conditional

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SensorReadingDropdownMenu(
    options: List<HardwareMonitorData.Sensor>,
    selectedIndex: Int,
    onValueChanged: (HardwareMonitorData.Sensor) -> Unit,
    label: String? = null,
    sensorName: String,
    dropdownLabel: (HardwareMonitorData.Sensor) -> String = { "${it.Name} (${it.Value} - ${it.SensorType})" },
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[selectedIndex]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(start = 12.dp, top = 16.dp)
                .fillMaxWidth()
                .background(BarelyVisibleGray, RoundedCornerShape(8.dp))
                .padding(16.dp)
                .border(1.dp, AlmostVisibleGray, RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (label != null) {
                    Text(
                        text = label,
                        color = LabelGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 0.sp,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Text(
                    text = "${selectedOption.Name} - ${selectedOption.SensorType}",
                    color = DarkGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 0.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            IconButton(onClick = { }, modifier = Modifier.size(20.dp).clearAndSetSemantics { }) {
                Icon(
                    Icons.Rounded.ChevronRight,
                    "Trailing icon for exposed dropdown menu",
                    Modifier.rotate(
                        if (expanded)
                            270f
                        else
                            90f
                    )
                )
            }
        }

        if (expanded) {
            Popup(
                onDismissRequest = { expanded = false },
                popupPositionProvider = object : PopupPositionProvider {
                    override fun calculatePosition(
                        anchorBounds: IntRect,
                        windowSize: IntSize,
                        layoutDirection: LayoutDirection,
                        popupContentSize: IntSize
                    ): IntOffset {
                        return IntOffset(0, 56)
                    }

                },
                properties = PopupProperties(
                    clippingEnabled = true,
                    dismissOnBackPress = true,
                    focusable = true,
                )
            ) {
                var filteredItems by remember { mutableStateOf(options) }
                Surface(color = Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp)) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .shadow(8.dp, RoundedCornerShape(12.dp))
                                .background(Color.White, RoundedCornerShape(12.dp))
                                .size(650.dp, 400.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Header(
                                label = sensorName,
                                onCloseRequest = {
                                    expanded = false
                                },
                                onFilterChange = { filter ->
                                    if (filter.isNotEmpty()) {
                                        filteredItems =
                                            options.filter {
                                                it.SensorType.name.contains(filter, true) ||
                                                        it.Name.contains(filter, true)
                                            }
                                    } else {
                                        filteredItems = options
                                    }
                                }
                            )

                            LazyColumn(
                                modifier = Modifier.size(650.dp, 400.dp).padding(horizontal = 24.dp),
                                contentPadding = PaddingValues(bottom = 24.dp)
                            ) {
                                filteredItems.forEachIndexed { index, item ->
                                    item {
                                        Row(
                                            modifier = Modifier
                                                .height(40.dp)
                                                .fillMaxWidth()
                                                .clickable {
                                                    expanded = false
                                                    selectedOption = item
                                                    onValueChanged(item)
                                                }
                                                .conditional(
                                                    predicate = index == selectedIndex,
                                                    ifTrue = { background(Gray200, RoundedCornerShape(8.dp)) }
                                                )
                                                .padding(horizontal = 12.dp, vertical = 10.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = dropdownLabel(item),
                                                color = DarkGray,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight(550),
                                                lineHeight = 0.sp,
                                                modifier = Modifier
                                            )

                                            if (index == selectedIndex) {
                                                Icon(
                                                    Icons.Rounded.Check,
                                                    "Trailing icon for exposed dropdown menu",
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(
    label: String,
    onCloseRequest: () -> Unit,
    onFilterChange: (String) -> Unit
) {
    var filter by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth()
            .drawBehind {
                val y = size.height - 4
                drawLine(
                    color = BarelyVisibleGray,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 2f
                )
            }
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Select $label sensor",
                fontSize = 13.sp,
                color = MutedGray,
                lineHeight = 0.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )

            IconButton(
                onClick = onCloseRequest,
                modifier = Modifier.size(20.dp).clearAndSetSemantics { }
            ) {
                Icon(
                    Icons.Rounded.Close,
                    "Trailing icon for exposed dropdown menu",
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .border(1.dp, AlmostVisibleGray, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier.size(20.dp).clearAndSetSemantics { }
            ) {
                Icon(
                    Icons.Rounded.Search,
                    "Leading icon for search box",
                    tint = MutedGray
                )
            }

            BasicTextField(
                value = filter,
                textStyle = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 2.sp,
                    textAlign = TextAlign.Start,
                ),
                singleLine = true,
                onValueChange = {
                    filter = it
                    onFilterChange(it)
                },
                modifier = Modifier.weight(1f),
            )
        }
    }
}