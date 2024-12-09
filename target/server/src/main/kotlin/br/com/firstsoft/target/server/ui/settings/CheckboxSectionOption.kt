package br.com.firstsoft.target.server.ui.settings

import br.com.firstsoft.core.common.hardwaremonitor.HardwareMonitorData

data class CheckboxSectionOption(
    val isSelected: Boolean,
    val name: String,
    val type: SensorType,
    val dataType: HardwareMonitorData.SensorType,
    val optionReadingId: String = "",
    val useCustomSensor: Boolean = false,
    val useCheckbox: Boolean = true,
)

enum class SensorType {
    Framerate, Frametime, CpuTemp, CpuUsage, GpuTemp, GpuUsage, VramUsage, TotalVramUsed, RamUsage, UpRate, DownRate, NetGraph
}

enum class SectionType {
    Fps, Gpu, Cpu, Ram, Network,
}