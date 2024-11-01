package client;

import ejb.VehicleIF;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ArchiveAsset;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.protocol.appclient.AppClientCmd;
import tck.arquillian.protocol.appclient.AppClientProtocolConfiguration;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Simple test to run an application client against a remote EJB
 *
 * Run with:
 * mvn -Darquillian.xml=test-arquillian.xml test
 */
@ExtendWith(ArquillianExtension.class)
public class AppClientTest {
    @Deployment(name = "jpa_core_EntityGraph_stateless_vehicle", testable = false)
    public static EnterpriseArchive createDeployment() {
        JavaArchive jpa_core_EntityGraph_stateless_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_core_EntityGraph_vehicles_client.jar");
        // The class files
        jpa_core_EntityGraph_stateless_vehicle_client.addClasses(RemoteEjbClient.class, VehicleIF.class);
        // The application-client.xml descriptor
        URL resURL = AppClientTest.class.getResource("/application-client.xml");
        jpa_core_EntityGraph_stateless_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
        jpa_core_EntityGraph_stateless_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + RemoteEjbClient.class.getName() + "\n"), "MANIFEST.MF");

        JavaArchive jpa_core_EntityGraph_stateless_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_core_EntityGraph_stateless_vehicle_ejb.jar");
        // The class files
        jpa_core_EntityGraph_stateless_vehicle_ejb.addClasses(
                VehicleIF.class,
                ejb.StatelessVehicleBean.class
        );
        // The sun-ejb-jar.xml file
        URL ejbResURL1 = AppClientTest.class.getResource("/sun-ejb-jar.xml");
        jpa_core_EntityGraph_stateless_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");

        EnterpriseArchive jpa_core_EntityGraph_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_EntityGraph_vehicles.ear");

        // The component jars built by the package target
        jpa_core_EntityGraph_vehicles_ear.addAsModule(jpa_core_EntityGraph_stateless_vehicle_ejb);
        jpa_core_EntityGraph_vehicles_ear.addAsModule(jpa_core_EntityGraph_stateless_vehicle_client);

        extractEar(jpa_core_EntityGraph_vehicles_ear);

        return jpa_core_EntityGraph_vehicles_ear;
    }

    // From the AppClientDeploymentPackager
    private static void extractEar(EnterpriseArchive ear) {
        // Write out the ear with the test dependencies for use by the appclient launcher
        String extractDir = "target/appclient";
        File appclient = new File(extractDir);
        if(!appclient.exists()) {
            if(appclient.mkdirs()) {
                System.out.println("Created appclient directory: " + appclient.getAbsolutePath());
            } else {
                throw new RuntimeException("Failed to create appclient directory: " + appclient.getAbsolutePath());
            }
        }
        File archiveOnDisk = new File(appclient, ear.getName());
        final ZipExporter exporter = ear.as(ZipExporter.class);
        exporter.exportTo(archiveOnDisk, true);
        System.out.println("Exported test ear to: " + archiveOnDisk.getAbsolutePath());

        for (ArchivePath path : ear.getContent().keySet()) {
            Node node = ear.get(path);
            if (node.getAsset() instanceof ArchiveAsset) {
                ArchiveAsset asset = (ArchiveAsset) node.getAsset();
                File archiveFile = new File(appclient, path.get());
                if(!archiveFile.getParentFile().exists()) {
                    archiveFile.getParentFile().mkdirs();
                }
                final ZipExporter zipExporter = asset.getArchive().as(ZipExporter.class);
                zipExporter.exportTo(archiveFile, true);
                System.out.println("Exported test ear content to: " + archiveFile.getAbsolutePath());
            } else if(node.getAsset() instanceof FileAsset) {
                FileAsset asset = (FileAsset) node.getAsset();
                File file = new File(appclient, path.get());
                if(!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                try {
                    Files.copy(asset.openStream(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Exported test ear content to: " + file.getAbsolutePath());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to export test ear content to: " + file.getAbsolutePath(), e);
                }
            }
        }
    }
    @Test
    public void runAppClientWithConfig() throws Exception {
        String clientEnvp = "AS_JAVA="+System.getenv("JAVA_HOME")+";AS_DEBUG=true";
        String clientCmdLineString = "target/glassfish8/glassfish/bin/appclient\\" +
                "-Djava.util.logging.config.file=jakartaeetck/logging.properties\\" +
                "-jar\\" +
                "${clientEarDir}/${clientAppArchive}"
        ;

        AppClientProtocolConfiguration config = new AppClientProtocolConfiguration();
        config.setClientCmdLineString(clientCmdLineString);
        config.setClientEnvString(clientEnvp);
        config.setCmdLineArgSeparator("\\\\");
        config.setClientDir(".");
        config.setClientEarDir("target/appclient");
        config.setUnpackClientEar(true);

        AppClientCmd appClientCmd = new AppClientCmd(config);
        appClientCmd.run("jpa_core_EntityGraph_vehicles.ear", "jpa_core_EntityGraph_vehicles_client.jar");
        String[] output = appClientCmd.readAll(15000);
        for (String line : output) {
            System.out.println(line);
        }
    }
}
