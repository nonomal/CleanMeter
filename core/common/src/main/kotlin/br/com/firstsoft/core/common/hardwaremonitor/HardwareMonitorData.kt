package br.com.firstsoft.core.common.hardwaremonitor

import kotlinx.serialization.Serializable

@Serializable
data class HardwareMonitorData(
    val LastPollTime: Long,
    val Hardwares: List<Hardware>,
    val Sensors: List<Sensor>
) {
    @Serializable
    data class Hardware(
        val Name: String,
        val Identifier: String
    )

    @Serializable
    data class Sensor(
        val Name: String,
        val Identifier: String,
        val SensorType: Int,
        val Value: Float
    )
}
