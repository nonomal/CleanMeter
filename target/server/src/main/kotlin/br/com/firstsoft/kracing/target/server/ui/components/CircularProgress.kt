package br.com.firstsoft.kracing.target.server.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.ColorTokens.ClearGray
import ui.ColorTokens.Green
import ui.ColorTokens.Red
import ui.ColorTokens.Yellow

@Composable
fun CircularProgress(value: Float, label: String, unit: String) =
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
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = label,
                fontSize = 16.sp,
                color = Color.White,
                lineHeight = 0.sp,
                fontWeight = FontWeight.Normal,
            )
            Text(
                text = unit,
                fontSize = 10.sp,
                color = Color.White,
                lineHeight = 0.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }