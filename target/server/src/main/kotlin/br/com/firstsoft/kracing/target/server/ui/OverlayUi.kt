package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.firstsoft.kracing.target.server.ui.components.CircularProgress
import br.com.firstsoft.kracing.target.server.ui.components.Pill
import mahm.CpuTemp
import mahm.CpuUsage
import mahm.Data
import mahm.FPS
import mahm.Frametime
import mahm.GpuTemp
import mahm.GpuUsage
import mahm.MahmReader
import mahm.RamUsage
import mahm.RamUsagePercent
import mahm.VramUsage
import mahm.VramUsagePercent
import ui.app.OverlaySettings

object ColorTokens {
    val Green = Color(0xff1cad69)
    val Yellow = Color(0xfffcc748)
    val Red = Color(0xffed4335)
    val ClearGray = Color(0x11d3d3d3)
    val OffWhite = Color(0xffc0c0c0)
}

inline fun Modifier.conditional(
    predicate: Boolean,
    ifTrue: Modifier.() -> Modifier,
    ifFalse: Modifier.() -> Modifier = { this },
): Modifier = if (predicate) ifTrue(this) else ifFalse(this)

@Composable
fun OverlayUi(
    reader: MahmReader,
    overlaySettings: OverlaySettings,
) {

    val data by reader.currentData.collectAsState(null)

    if (data == null) {
        Text("Unable to read data...")
        return
    }

    if (overlaySettings.isHorizontal) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight()
                .background(Color.Black.copy(alpha = 0.36f), CircleShape)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Content(
                data!!,
                overlaySettings = overlaySettings,
            )
        }
    } else {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.36f), RoundedCornerShape(12.dp))
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            Content(
                data!!,
                overlaySettings = overlaySettings,
            )
        }
    }
}

@Composable
fun Content(data: Data, overlaySettings: OverlaySettings) {
    if (overlaySettings.isHorizontal) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            fps(overlaySettings, data)
            gpu(overlaySettings, data)
            cpu(overlaySettings, data)
            ram(overlaySettings, data)
        }
    } else {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Layout(
                content = {
                    fps(overlaySettings, data)
                    gpu(overlaySettings, data)
                    cpu(overlaySettings, data)
                    ram(overlaySettings, data)
                },
                measurePolicy = object : MeasurePolicy {
                    override fun MeasureScope.measure(
                        measurables: List<Measurable>,
                        constraints: Constraints
                    ): MeasureResult {
                        val maxWidth = maxIntrinsicWidth(measurables, constraints.maxHeight)
                        val placeables = measurables.map { it.measure(Constraints.fixedWidth(maxWidth)) }
                        val height = placeables.sumOf { it.height } + 4.dp.roundToPx() * (placeables.size - 1)

                        return layout(maxWidth, height) {
                            var yPosition = 0
                            placeables.forEach { placeable ->
                                placeable.placeRelative(x = 0, y = yPosition)
                                yPosition += placeable.height + 4.dp.roundToPx()
                            }
                        }
                    }

                    override fun IntrinsicMeasureScope.maxIntrinsicWidth(
                        measurables: List<IntrinsicMeasurable>,
                        height: Int
                    ): Int {
                        return measurables.map { it.maxIntrinsicWidth(height) }.maxOf { it }
                    }
                })
        }
    }
}

@Composable
private fun ram(overlaySettings: OverlaySettings, data: Data) {
    if (overlaySettings.ramUsage) {
        Pill(
            title = "RAM",
            isHorizontal = overlaySettings.isHorizontal,
        ) {
            CircularProgress(
                value = data.RamUsagePercent / 100f,
                label = String.format("%02.1f", data.RamUsage / 1000),
                unit = "GB"
            )
        }
    }
}

@Composable
private fun cpu(overlaySettings: OverlaySettings, data: Data) {
    if (overlaySettings.cpuTemp || overlaySettings.cpuUsage) {
        Pill(
            title = "CPU",
            isHorizontal = overlaySettings.isHorizontal,
        ) {
            if (overlaySettings.cpuTemp) {
                CircularProgress(value = data.CpuTemp / 100f, label = "${data.CpuTemp}", unit = "c")
            }
            if (overlaySettings.cpuUsage) {
                CircularProgress(value = data.CpuUsage / 100f, label = String.format("%02d", data.CpuUsage), unit = "%")
            }
        }
    }
}

@Composable
private fun gpu(overlaySettings: OverlaySettings, data: Data) {
    if (overlaySettings.gpuTemp || overlaySettings.gpuUsage || overlaySettings.vramUsage) {
        Pill(
            title = "GPU",
            isHorizontal = overlaySettings.isHorizontal,
        ) {
            if (overlaySettings.gpuTemp) {
                CircularProgress(value = data.GpuTemp / 100f, label = "${data.GpuTemp}", unit = "c")
            }
            if (overlaySettings.gpuUsage) {
                CircularProgress(value = data.GpuUsage / 100f, label = String.format("%02d", data.GpuUsage), unit = "%")
            }
            if (overlaySettings.vramUsage) {
                CircularProgress(
                    value = data.VramUsagePercent / 100f,
                    label = String.format("%02.1f", data.VramUsage / 1000),
                    unit = "GB"
                )
            }
        }
    }
}

@Composable
private fun fps(overlaySettings: OverlaySettings, data: Data) {
    if (overlaySettings.fps || overlaySettings.frametime) {
        Pill(
            title = "FPS",
            isHorizontal = overlaySettings.isHorizontal,
        ) {
            if (overlaySettings.fps) {
                Text(
                    text = "${data.FPS}",
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 0.sp,
                    fontWeight = FontWeight.Normal,
                )
            }

            if (overlaySettings.frametime) {
                Text(
                    text = "${String.format("%02.01f", data.Frametime)}ms",
                    color = Color.White,
                    fontSize = 12.sp,
                    lineHeight = 0.sp,
                    fontWeight = FontWeight.Normal,
                )
            }
        }
    }
}
