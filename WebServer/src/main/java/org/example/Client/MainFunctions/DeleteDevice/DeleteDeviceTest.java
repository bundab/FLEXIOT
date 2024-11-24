package org.example.Client.MainFunctions.DeleteDevice;

import org.example.Client.Enums.DeviceType;

public class DeleteDeviceTest {
    public static void main(String[] args) {
        DeleteDevice d = new DeleteDevice("Smart_Meter", DeviceType.PowerSensor);
        try {
            d.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
