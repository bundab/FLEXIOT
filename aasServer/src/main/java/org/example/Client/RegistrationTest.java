package org.example.Client;

import org.example.Client.Enums.DeviceType;

public class RegistrationTest {
    public static void main(String[] args) {
        Registration r = new Registration("Smart_Meter", DeviceType.AcSensor);
        try {
            r.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
