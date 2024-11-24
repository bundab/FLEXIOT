package org.example.WebServer.Interaction;

import org.example.Client.Enums.DeviceType;

public class DeviceTypeMapping {

    public static String convertDeviceTypeToString(DeviceType deviceType) {
        return switch (deviceType) {
            case AcSensor -> "AcSensor";
            case PowerSensor -> "PowerSensor";
            case HumiditySensor -> "HumiditySensor";
            case SoundSensor -> "SoundSensor";
            case AdvancedWeatherSensor -> "AdvancedWeatherSensor";
            default -> "Unknown Device Type";  // Default case for safety
        };
    }

    // Convert String to DeviceType
    public static DeviceType convertStringToDeviceType(String deviceTypeString) {
        // Handle invalid input
        return switch (deviceTypeString) {
            case "AcSensor" -> DeviceType.AcSensor;
            case "PowerSensor" -> DeviceType.PowerSensor;
            case "HumiditySensor" -> DeviceType.HumiditySensor;
            case "SoundSensor" -> DeviceType.SoundSensor;
            case "AdvancedWeatherSensor" -> DeviceType.AdvancedWeatherSensor;
            default ->
                    throw new IllegalArgumentException("Unknown Device Type: " + deviceTypeString);  // Handle invalid input
        };
    }
}
