package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import ui.ColorTokens.ClearGray
import ui.ColorTokens.Green
import ui.ColorTokens.OffWhite
import ui.ColorTokens.Red
import ui.ColorTokens.Yellow
import ui.app.OverlaySettings

object ColorTokens {
    val Green = Color(0xff1cad69)
    val Yellow = Color(0xfffcc748)
    val Red = Color(0xffed4335)
    val ClearGray = Color(0x11d3d3d3)
    val OffWhite = Color(0xffc0c0c0)
}

@Composable
fun OverlayUi(
    reader: MahmReader,
    overlaySettings: OverlaySettings,
) = Row(
    modifier = Modifier
        .padding(16.dp)
        .fillMaxHeight()
        .background(Color.Black.copy(alpha = 0.36f), CircleShape)
        .padding(4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center,
) {

    val data by reader.currentData.collectAsState(null)

    if (data == null) {
        Text("Unable to read data...")
        return@Row
    }

    Content(
        data!!,
        overlaySettings = overlaySettings,
    )
}

@Composable
fun Pill(
    title: String,
    minWidth: Dp = 80.dp,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxHeight()
            .widthIn(min = minWidth)
            .background(Color.Black.copy(alpha = 0.3f), CircleShape)
            .padding(vertical = 4.dp, horizontal = 12.dp)
    ) {
        Text(
            text = title,
            fontSize = 10.sp,
            color = OffWhite,
            lineHeight = 0.sp,
            fontWeight = FontWeight.Normal,
            letterSpacing = 1.sp
        )

        content()
    }
}

@Composable
fun CircularProgress(value: Float, label: String) =
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        val color = when {
            value > 0.7f && value < 0.9f -> Red
            value > 0.5f && value < 0.7f -> Yellow
            else -> Green
        }
        CircularProgressIndicator(
            progress = value,
            modifier = Modifier.size(24.dp),
            color = color,
            backgroundColor = ClearGray,
            strokeCap = StrokeCap.Round,
            strokeWidth = 3.dp
        )
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.White,
            lineHeight = 0.sp,
            fontWeight = FontWeight.Normal,
        )
    }

@Composable
fun Content(
    data: Data,
    overlaySettings: OverlaySettings,
) = Row(
    modifier = Modifier
        .fillMaxHeight(),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {
    if (overlaySettings.fps || overlaySettings.frametime) {
        Pill(title = "FPS") {
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

    if (overlaySettings.gpuTemp || overlaySettings.gpuUsage || overlaySettings.vramUsage) {
        Pill(title = "GPU") {
            if (overlaySettings.gpuTemp) {
                CircularProgress(value = data.GpuTemp / 100f, label = "${data.GpuTemp}c")
            }
            if (overlaySettings.gpuUsage) {
                CircularProgress(value = data.GpuUsage / 100f, label = "${String.format("%02d", data.GpuUsage)}%")
            }
            if (overlaySettings.vramUsage) {
                CircularProgress(
                    value = data.VramUsagePercent / 100f,
                    label = "${String.format("%02.1f", data.VramUsage / 1000)}GB"
                )
            }
        }
    }

    if (overlaySettings.cpuTemp || overlaySettings.cpuUsage) {
        Pill(title = "CPU") {
            if (overlaySettings.cpuTemp) {
                CircularProgress(value = data.CpuTemp / 100f, label = "${data.CpuTemp}c")
            }
            if (overlaySettings.cpuUsage) {
                CircularProgress(value = data.CpuUsage / 100f, label = "${String.format("%02d", data.CpuUsage)}%")
            }
        }
    }

    if (overlaySettings.ramUsage) {
        Pill(title = "RAM") {
            CircularProgress(
                value = data.RamUsagePercent / 100f,
                label = "${String.format("%02.1f", data.RamUsage / 1000)}GB"
            )
        }
    }

}

