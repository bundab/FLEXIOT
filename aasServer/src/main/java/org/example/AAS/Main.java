package org.example.AAS;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.example.AAS.MQTT.MqttSubscriberManager;

public class Main {
    public static void main(String[] args) {
        AASServers aasServers = new AASServers();
        aasServers.start();

        /*try {
            // MqttSubscriberManager példányosítása egy 5 szálas szálmedencével
            MqttSubscriberManager manager = new MqttSubscriberManager("tcp://localhost:1883", 1000);

            // Feliratkozási esemény kezelése
            System.out.println("Feliratkozás az első topic-ra...");
            manager.handleEvent("ADD", "ac/temperature");

            // További topic hozzáadása
            System.out.println("Feliratkozás egy második topic-ra...");
            manager.handleEvent("ADD", "TSensor123/temperature");

            // Néhány másodpercig várakozunk, hogy érkezzenek üzenetek
            Thread.sleep(30000);  // 10 másodpercig fut a program, hogy lássuk a beérkező üzeneteket

            // Leiratkozás az első topic-ról
            System.out.println("Leiratkozás az első topic-ról...");
            manager.handleEvent("REMOVE", "TSensor123/temperature");

            // További várakozás, hogy figyeljük a fennmaradó topic üzeneteit
            Thread.sleep(15000);

            // Program vége: leiratkozás és szálmedence leállítása
            System.out.println("Program leállítása és kapcsolatok bezárása...");
            manager.shutdown();

        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}