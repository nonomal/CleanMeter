package br.com.firstsoft.core.os.win32

import br.com.firstsoft.core.os.hardwaremonitor.HardwareMonitorProcessManager
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Path

object WinRegistry {
    const val STARTUP_ITEMS_LOCATION = "HKEY_LOCAL_MACHINE\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run"
    const val REGISTRY_APP_NAME = "cleanmeter"

    fun read(location: String, key: String): List<String> {
        val proc = ProcessBuilder("reg", "query", location, "/v", key)
            .redirectErrorStream(true)
            .start()
        val input = BufferedReader(InputStreamReader(proc.inputStream))

        return input.lineSequence().toList()
    }

    fun write(location: String, key: String, value: String, type: String = "REG_SZ") {
        val proc = ProcessBuilder("reg", "add", location, "/v", key, "/t", type, "/d", value)
            .redirectErrorStream(true)
            .start()

        val input = BufferedReader(InputStreamReader(proc.inputStream))

        println(input.lineSequence().toList())
    }

    fun delete(location: String, key: String) {
        val proc = ProcessBuilder("reg", "delete", location, "/v", key, "/f")
            .redirectErrorStream(true)
            .start()

        val input = BufferedReader(InputStreamReader(proc.inputStream))

        println(input.lineSequence().toList())
    }

    fun isAppRegisteredToStartWithWindows(): Boolean {
        val queryOutput = read(STARTUP_ITEMS_LOCATION, REGISTRY_APP_NAME)

        return queryOutput.map { it.indexOf(REGISTRY_APP_NAME) >= 0 }.any { it }
    }

    fun registerAppToStartWithWindows() {
        if (WindowsService.isProcessElevated()) {
            write(STARTUP_ITEMS_LOCATION, REGISTRY_APP_NAME, "\\\"${Path.of("").toAbsolutePath()}\\$REGISTRY_APP_NAME.exe\\\" --autostart")
            HardwareMonitorProcessManager.createService()
        }
    }

    fun removeAppFromStartWithWindows() {
        if (WindowsService.isProcessElevated()) {
            delete(STARTUP_ITEMS_LOCATION, REGISTRY_APP_NAME)
            HardwareMonitorProcessManager.stopService()
            HardwareMonitorProcessManager.deleteService()
        }
    }
}
