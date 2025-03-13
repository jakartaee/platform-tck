package ee.jakarta.tck.persistence.ee.packaging.appclient.annotation;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;

import java.net.URL;



@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends ee.jakarta.tck.persistence.ee.packaging.appclient.annotation.Client {
    /**
        EE10 Deployment Descriptors:
        jpa_ee_packaging_appclient_annotation: 
        jpa_ee_packaging_appclient_annotation_client: META-INF/persistence.xml

        Found Descriptors:
        Client:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "jpa_ee_packaging_appclient_annotation", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_ee_packaging_appclient_annotation_client = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_appclient_annotation_client.jar");
            // The class files
            jpa_ee_packaging_appclient_annotation_client.addClasses(
            com.sun.ts.lib.harness.EETest.Fault.class,
            ee.jakarta.tck.persistence.ee.packaging.appclient.annotation.Coffee.class,
            ee.jakarta.tck.persistence.ee.packaging.appclient.annotation.Client.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("");
            if(resURL != null) {
              jpa_ee_packaging_appclient_annotation_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_ee_packaging_appclient_annotation_client.addAsManifestResource(resURL, "application-client.xml");
            }
            jpa_ee_packaging_appclient_annotation_client.addAsManifestResource(new StringAsset("Main-Class: " + com.sun.ts.tests.common.vehicle.VehicleClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_ee_packaging_appclient_annotation_client, Client.class, resURL);


        // Ear
            EnterpriseArchive jpa_ee_packaging_appclient_annotation_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_packaging_appclient_annotation.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_ee_packaging_appclient_annotation_ear.addAsModule(jpa_ee_packaging_appclient_annotation_client);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_ee_packaging_appclient_annotation_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_ee_packaging_appclient_annotation_ear, Client.class, earResURL);
        return jpa_ee_packaging_appclient_annotation_ear;
        }

        @Test
        @Override
        public void test1() throws java.lang.Exception {
            super.test1();
        }


}