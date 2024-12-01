package org.example.Client.MainFunctions.RegisterDevice;

import org.example.Client.Enums.DeviceType;

public class RegisterDeviceTest {
    public static void main(String[] args) {
        RegisterDevice rPowerSensor1 = new RegisterDevice("Smart_Meter", DeviceType.PowerSensor, "This");
        RegisterDevice rPowerSensor2 = new RegisterDevice("PS01", DeviceType.PowerSensor, "Is");

        RegisterDevice rAcSensor1 = new RegisterDevice("AC118", DeviceType.AcSensor, "The");
        RegisterDevice rAcSensor2 = new RegisterDevice("AS01", DeviceType.AcSensor, "Username");
        try {
            rPowerSensor1.execute();
            rPowerSensor2.execute();
            rAcSensor1.execute();
            rAcSensor2.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
