package org.example.Client.MainFunctions.GetDevice;

import org.example.Client.Enums.DeviceType;

public class GetDeviceTest {
    public static void main(String[] args) {
        GetDevice gAcSensor1 = new GetDevice("AC118", DeviceType.AcSensor);
        GetDevice gAcSensor2 = new GetDevice("AS01", DeviceType.AcSensor);

        GetDevice gPowerSensor1 = new GetDevice("Smart_Meter", DeviceType.PowerSensor);
        GetDevice gPowerSensor2 = new GetDevice("PS01", DeviceType.PowerSensor);
        try {
            gAcSensor1.execute();
            gAcSensor2.execute();
            gPowerSensor1.execute();
            gPowerSensor2.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
