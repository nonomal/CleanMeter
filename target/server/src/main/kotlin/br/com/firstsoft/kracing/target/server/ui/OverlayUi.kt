package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import br.com.firstsoft.kracing.target.server.ui.components.CircularProgress
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
            ContentWrapper(
                data!!,
                overlaySettings = overlaySettings,
            )
        }
    }
}

@Composable
fun Pill(
    title: String,
    isHorizontal: Boolean,
    minWidth: Dp = 80.dp,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .conditional(
                predicate = isHorizontal,
                ifTrue = { fillMaxHeight().widthIn(min = minWidth).background(Color.Black.copy(alpha = 0.3f), CircleShape) },
                ifFalse = { fillMaxWidth().background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(8.dp)) },
            )
            .padding(vertical = if (isHorizontal) 4.dp else 8.dp, horizontal = 12.dp)
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
fun ContentWrapper(data: Data, overlaySettings: OverlaySettings) {
    if (overlaySettings.isHorizontal) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Content(data, overlaySettings)
        }
    } else {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Content(data, overlaySettings)
        }
    }
}

@Composable
fun Content(
    data: Data,
    overlaySettings: OverlaySettings,
) {
    if (overlaySettings.fps || overlaySettings.frametime) {
        Pill(title = "FPS", isHorizontal = overlaySettings.isHorizontal) {
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
        Pill(title = "GPU", isHorizontal = overlaySettings.isHorizontal) {
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

    if (overlaySettings.cpuTemp || overlaySettings.cpuUsage) {
        Pill(title = "CPU", isHorizontal = overlaySettings.isHorizontal) {
            if (overlaySettings.cpuTemp) {
                CircularProgress(value = data.CpuTemp / 100f, label = "${data.CpuTemp}", unit = "c")
            }
            if (overlaySettings.cpuUsage) {
                CircularProgress(value = data.CpuUsage / 100f, label = String.format("%02d", data.CpuUsage), unit = "%")
            }
        }
    }

    if (overlaySettings.ramUsage) {
        Pill(title = "RAM", isHorizontal = overlaySettings.isHorizontal) {
            CircularProgress(
                value = data.RamUsagePercent / 100f,
                label = String.format("%02.1f", data.RamUsage / 1000),
                unit = "GB"
            )
        }
    }
}
