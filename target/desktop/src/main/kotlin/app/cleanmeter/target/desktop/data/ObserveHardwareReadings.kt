package app.cleanmeter.target.desktop.data

import app.cleanmeter.core.os.hardwaremonitor.HardwareMonitorReader

object ObserveHardwareReadings {
    val data = HardwareMonitorReader.currentData
}