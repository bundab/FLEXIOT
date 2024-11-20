package org.example.Client.MainFunctions.RegisterDevice;

import org.example.Client.Enums.DeviceType;

public class RegisterDeviceTest {
    public static void main(String[] args) {
        RegisterDevice rPowerSensor1 = new RegisterDevice("Smart_Meter", DeviceType.PowerSensor, "Anyad");
        RegisterDevice rPowerSensor2 = new RegisterDevice("PS01", DeviceType.PowerSensor, "Hogy");

        RegisterDevice rAcSensor1 = new RegisterDevice("AC118", DeviceType.AcSensor, "Van");
        RegisterDevice rAcSensor2 = new RegisterDevice("AS01", DeviceType.AcSensor, "Ma");
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
