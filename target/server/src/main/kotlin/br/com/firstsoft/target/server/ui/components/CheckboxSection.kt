package br.com.firstsoft.target.server.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.firstsoft.target.server.ui.ColorTokens.MutedGray
import br.com.firstsoft.target.server.ui.settings.CheckboxSectionOption

@Composable
fun CheckboxSection(
    title: String,
    options: List<CheckboxSectionOption>,
    onOptionToggle: (CheckboxSectionOption) -> Unit,
    onSwitchToggle: (Boolean) -> Unit,
) = Column(
    modifier = Modifier.animateContentSize().background(Color.White, RoundedCornerShape(12.dp)).padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
) {
    val isAnySelected by remember(options) { derivedStateOf { options.any { it.isSelected } } }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            color = MutedGray,
            lineHeight = 0.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        )
        Toggle(
            checked = isAnySelected,
            onCheckedChange = onSwitchToggle
        )
    }

    if (isAnySelected) {
        Column(modifier = Modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            options.forEach { option ->
                CheckboxWithLabel(
                    label = option.name,
                    onCheckedChange = { onOptionToggle(option.copy(isSelected = !option.isSelected)) },
                    checked = option.isSelected,
                )
            }
        }
    }
}

@Composable
fun CustomBodyCheckboxSection(
    title: String,
    options: List<CheckboxSectionOption>,
    onSwitchToggle: (Boolean) -> Unit,
    body: @Composable (List<CheckboxSectionOption>) -> Unit,
) = Column(
    modifier = Modifier.animateContentSize().background(Color.White, RoundedCornerShape(12.dp)).padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
) {
    val isAnySelected by remember(options) { derivedStateOf { options.any { it.isSelected } } }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            color = MutedGray,
            lineHeight = 0.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        )
        Toggle(
            checked = isAnySelected,
            onCheckedChange = onSwitchToggle
        )
    }

    if (isAnySelected) {
        body(options)
    }
}