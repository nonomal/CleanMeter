package br.com.firstsoft.target.server.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ProgressUnit(unit: String) {
    Text(
        text = unit,
        fontSize = 10.sp,
        color = Color.White,
        lineHeight = 0.sp,
        fontWeight = FontWeight.Normal,
    )
}

@Composable
fun ProgressLabel(label: String) {
    Text(
        text = label,
        fontSize = 16.sp,
        color = Color.White,
        lineHeight = 0.sp,
        fontWeight = FontWeight.Normal,
    )
}