package org.example.AAS;

import org.eclipse.basyx.components.aas.AASServerComponent;
import org.eclipse.basyx.components.aas.configuration.AASServerBackend;
import org.eclipse.basyx.components.aas.configuration.BaSyxAASServerConfiguration;
import org.eclipse.basyx.components.configuration.BaSyxContextConfiguration;
import org.eclipse.basyx.components.registry.RegistryComponent;
import org.eclipse.basyx.components.registry.configuration.BaSyxRegistryConfiguration;
import org.eclipse.basyx.components.registry.configuration.RegistryBackend;

public class AASServers {
    public static final String REGISTRYPATH = "http://0.0.0.0:4000/registry";
    public static final String AASSERVERPATH = "http://0.0.0.0:4001/aasServer";

    //MQTT
    private static final String BROKER_URL = "tcp://localhost:1883";

    //AAS Server and Registry threads
    Thread aasServerThread;
    Thread aasRegistryThread;

    public AASServers() {
        this.aasRegistryThread = new Thread(AASServers::startRegistry);
        this.aasServerThread = new Thread(AASServers::startAASServer);
    }

    public void start() {
        this.aasRegistryThread.start();
        this.aasServerThread.start();
    }

    public void stop() {
        this.aasRegistryThread.interrupt();
        this.aasServerThread.interrupt();
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
