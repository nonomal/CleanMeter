// See https://aka.ms/new-console-template for more information

using HardwareMonitor.Monitor;
using LibreHardwareMonitor.Hardware;

var poller = new MonitorPoller();
await poller.Start();