using System.Text.Json.Serialization;
using LibreHardwareMonitor.Hardware;

namespace HardwareMonitor.SharedMemory;

public static class SharedMemoryConsts
{
    public const string SharedMemoryName = "CleanMeterHardwareMonitor";
}

public class SharedMemoryHardware
{
    public string Name { get; set; }
    public string Identifier { get; set; }
}

public class SharedMemorySensor
{
    public ISensor Sensor;
    public string Name { get; set; }
    public string Identifier { get; set; }
    public int SensorType { get; set; }
    public float Value { get; set; }

    public bool ShouldSerializeSensor()
    {
        return false;
    }
};

public class SharedMemoryData
{
    public long LastPollTime { get; set; }
    public List<SharedMemoryHardware> Hardwares { get; set; }
    public List<SharedMemorySensor> Sensors { get; set; }
}

[JsonSourceGenerationOptions(WriteIndented = false, GenerationMode = JsonSourceGenerationMode.Serialization)]
[JsonSerializable(typeof(SharedMemoryData))]
[JsonSerializable(typeof(SharedMemorySensor))]
[JsonSerializable(typeof(SharedMemoryHardware))]
internal partial class SerializeContext : JsonSerializerContext
{
}