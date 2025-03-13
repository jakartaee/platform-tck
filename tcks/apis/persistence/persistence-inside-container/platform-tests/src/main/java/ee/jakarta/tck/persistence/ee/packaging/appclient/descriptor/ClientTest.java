package ee.jakarta.tck.persistence.ee.packaging.appclient.descriptor;

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
public class ClientTest extends ee.jakarta.tck.persistence.ee.packaging.appclient.descriptor.Client {
    /**
        EE10 Deployment Descriptors:
        jpa_ee_packaging_appclient_descriptor: 
        jpa_ee_packaging_appclient_descriptor_client: META-INF/application-client.xml,META-INF/orm.xml,META-INF/persistence.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jpa/ee/packaging/appclient/descriptor/jpa_ee_packaging_appclient_descriptor_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "jpa_ee_packaging_appclient_descriptor", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_ee_packaging_appclient_descriptor_client = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_appclient_descriptor_client.jar");
            // The class files
            jpa_ee_packaging_appclient_descriptor_client.addClasses(
            ee.jakarta.tck.persistence.ee.packaging.appclient.descriptor.Coffee.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.lib.harness.EETest.class,
            ee.jakarta.tck.persistence.ee.packaging.appclient.descriptor.Client.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("com/sun/ts/tests/jpa/ee/packaging/appclient/descriptor/jpa_ee_packaging_appclient_descriptor_client.xml");
            if(resURL != null) {
              jpa_ee_packaging_appclient_descriptor_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/packaging/appclient/descriptor/jpa_ee_packaging_appclient_descriptor_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_ee_packaging_appclient_descriptor_client.addAsManifestResource(resURL, "application-client.xml");
            }
            jpa_ee_packaging_appclient_descriptor_client.addAsManifestResource(new StringAsset("Main-Class: " + com.sun.ts.tests.common.vehicle.VehicleClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_ee_packaging_appclient_descriptor_client, Client.class, resURL);


        // Ear
            EnterpriseArchive jpa_ee_packaging_appclient_descriptor_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_packaging_appclient_descriptor.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_ee_packaging_appclient_descriptor_ear.addAsModule(jpa_ee_packaging_appclient_descriptor_client);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_ee_packaging_appclient_descriptor_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_ee_packaging_appclient_descriptor_ear, Client.class, earResURL);
        return jpa_ee_packaging_appclient_descriptor_ear;
        }

        @Test
        @Override
        public void test1() throws java.lang.Exception {
            super.test1();
        }


}