package org.example.AAS;

import org.eclipse.basyx.components.aas.AASServerComponent;
import org.eclipse.basyx.components.aas.configuration.AASServerBackend;
import org.eclipse.basyx.components.aas.configuration.BaSyxAASServerConfiguration;
import org.eclipse.basyx.components.configuration.BaSyxContextConfiguration;
import org.eclipse.basyx.components.registry.RegistryComponent;
import org.eclipse.basyx.components.registry.configuration.BaSyxRegistryConfiguration;
import org.eclipse.basyx.components.registry.configuration.RegistryBackend;
import org.eclipse.paho.client.mqttv3.*;
import org.example.AAS.MQTT.ConfigureTopicSubscribes;
import org.example.AAS.MQTT.MqttSubscriberManager;

public class AASServers {
    public static final String REGISTRYPATH = "http://0.0.0.0:4000/registry";
    //public static final String AASSERVERPATH = "http://0.0.0.0:4001/aasServer";

    //MQTT
    private static final String BROKER_URL = "tcp://localhost:1883";
    private Thread mqttHandlerThread;
    private MqttSubscriberManager mqttSubscriberManager;
    private String clientId = "AASServersTopicHandler";
    private String topic = "DevicesTopicManager";

    //AAS Server and Registry threads
    Thread aasServerThread;
    Thread aasRegistryThread;

    public AASServers() {
        this.aasRegistryThread = new Thread(AASServers::startRegistry);
        this.aasServerThread = new Thread(AASServers::startAASServer);

        try {
            this.mqttSubscriberManager = new MqttSubscriberManager(BROKER_URL, 10000);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

        // TODO: Implement the registration and delete handler's thread!!!
        // Lambda segítségével egy külön szálban futtatjuk az MQTT subscriber logikát
        mqttHandlerThread = new Thread(() -> {
            try {
                // MQTT kliens inicializálása
                MqttClient client = new MqttClient(BROKER_URL, clientId);

                // Callback beállítása az események kezeléséhez
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        System.out.println("Kapcsolat megszakadt: " + cause.getMessage());
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) {
                        System.out.println("Üzenet érkezett a topicról (" + topic + "): " + new String(message.getPayload()));

                        // TODO: Have to implement the publisher side as well (client), not only the server side (server)!!!!!
                        String eventTypeAndTopicMerged = new String(message.getPayload());
                        System.out.println(eventTypeAndTopicMerged);

                        String eventTypeAndTopic[] = eventTypeAndTopicMerged.split("\\.");

                        System.out.println("Eddig jo...\n");
                        System.out.println(eventTypeAndTopic[0] + " és " + eventTypeAndTopic[1]);
                        mqttSubscriberManager.handleEvent(eventTypeAndTopic[0], eventTypeAndTopic[1]);

                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        // Ez csak publisher esetén érdekes, de kötelező megvalósítani
                    }
                });

                // Kapcsolódás a brokerhez
                client.connect();
                System.out.println("Kapcsolódás sikeres a brokerhez: " + BROKER_URL);

                // Feliratkozás a topicra
                client.subscribe(topic);
                System.out.println("Feliratkozás a topicra: " + topic);

                // A szál futása addig tartson, amíg nem szakítjuk meg manuálisan
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(1000); // Várakozás, hogy ne terheljük a szálat
                }

                // Leiratkozás és kapcsolat bontása tisztán
                client.unsubscribe(topic);
                client.disconnect();
                System.out.println("Kapcsolat lezárva és leiratkozás megtörtént.");

            } catch (Exception e) {
                System.err.println("Hiba történt az MQTT szál futása közben: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void start() {
        this.aasRegistryThread.start();
        this.aasServerThread.start();
        this.mqttHandlerThread.start();

        try {
            Thread.sleep(5000);
            new ConfigureTopicSubscribes().executeTopicConfiguration();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        this.aasRegistryThread.interrupt();
        this.aasServerThread.interrupt();
        this.mqttHandlerThread.interrupt();
    }

    /**
     * Starts an empty registry at "http://localhost:4000"
     */
    private static void startRegistry() {
        BaSyxContextConfiguration contextConfig = new BaSyxContextConfiguration(4000, "/registry");
        BaSyxRegistryConfiguration registryConfig = new BaSyxRegistryConfiguration(RegistryBackend.MONGODB);
        RegistryComponent registry = new RegistryComponent(contextConfig, registryConfig);
        // Start the created server
        registry.startComponent();
    }

    /**
     * Startup an empty server at "http://localhost:4001/"
     */
    private static void startAASServer() {
        BaSyxContextConfiguration contextConfig = new BaSyxContextConfiguration(4001, "/aasServer");
        BaSyxAASServerConfiguration aasServerConfig = new BaSyxAASServerConfiguration(AASServerBackend.MONGODB, "", REGISTRYPATH);
        AASServerComponent aasServer = new AASServerComponent(contextConfig, aasServerConfig);
        // Start the created server
        aasServer.startComponent();
    }
}
