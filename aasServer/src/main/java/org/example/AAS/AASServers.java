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

        // Set the ThreadPool with 10000 maxThreads
        try {
            this.mqttSubscriberManager = new MqttSubscriberManager(BROKER_URL, 10000);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

        // Using a lambda to run the MQTT subscriber logic in a separate thread
        mqttHandlerThread = new Thread(() -> {
            try {
                // Initializing the MQTT client
                MqttClient client = new MqttClient(BROKER_URL, clientId);

                // Setting up a callback for handling events
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        System.out.println("Connection lost: " + cause.getMessage());
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) {
                        System.out.println("Message received from the topic (" + topic + "): " + new String(message.getPayload()));

                        String eventTypeAndTopicMerged = new String(message.getPayload());
                        System.out.println(eventTypeAndTopicMerged);

                        String eventTypeAndTopic[] = eventTypeAndTopicMerged.split("\\.");

                        System.out.println(eventTypeAndTopic[0] + " és " + eventTypeAndTopic[1]);
                        mqttSubscriberManager.handleEvent(eventTypeAndTopic[0], eventTypeAndTopic[1]);

                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        // This is a must-have in the case of publishing
                    }
                });

                // Connecting to the Broker
                client.connect();
                System.out.println("Connection to the broker successful: " + BROKER_URL);

                // Subscribing to the topic
                client.subscribe(topic);
                System.out.println("Subscribing to the topic: " + topic);

                // The thread should run until it is manually interrupted
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(1000); // Waiting to avoid overloading the thread
                }

                // Unsubscribing and disconnecting cleanly
                client.unsubscribe(topic);
                client.disconnect();
                System.out.println("Connection closed and unsubscribed successfully.");

            } catch (Exception e) {
                System.err.println("An error occurred during the MQTT thread execution: " + e.getMessage());
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
