package br.com.firstsoft.core.os.hardwaremonitor

import br.com.firstsoft.core.common.hardwaremonitor.HardwareMonitorData
import br.com.firstsoft.core.os.util.getByteBuffer
import br.com.firstsoft.core.os.util.readString
import br.com.firstsoft.core.os.win32.WindowsService
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.WinNT
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.coroutines.cancellation.CancellationException

private const val MEMORY_MAP_FILE_NAME = "CleanMeterHardwareMonitor"

object HardwareMonitorReader {
    private val windowsService = WindowsService()
    private var memoryMapFile: WinNT.HANDLE? = null
    private var pointer: Pointer? = null

    val currentData = flow {
        while (pointer == null) {
            tryOpenMemoryFile()
            delay(2000L)
        }
        pointer?.let { pointer ->
            while (true) {
                try {
                    val jsonLength = readJsonLength(pointer)
                    val readJsonString = readJsonString(jsonLength, pointer)
                    val data = Json.decodeFromString<HardwareMonitorData>(readJsonString)
                    emit(data)
                    delay(500L)
                } catch (e: CancellationException) {
                    break
                }
            }
        }
    }

    private fun tryOpenMemoryFile() {
        if (memoryMapFile == null) {
            windowsService.openMemoryMapFile(MEMORY_MAP_FILE_NAME)?.let { handle ->
                memoryMapFile = handle
                pointer = windowsService.mapViewOfFile(handle)
            }
        }
    }

    private fun readJsonLength(pointer: Pointer): Int {
        val buffer = getByteBuffer(pointer, 4)
        return buffer.int
    }

    private fun readJsonString(length: Int, pointer: Pointer): String {
        val buffer = pointer.getByteArray(4, length).let { ByteBuffer.wrap(it).order(ByteOrder.LITTLE_ENDIAN) }

        return buffer.readString(length)
    }
}