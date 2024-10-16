package ee.jakarta.tck.persistence.ee.packaging.ejb.exclude;

import ee.jakarta.tck.persistence.ee.packaging.ejb.exclude.Client;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends ee.jakarta.tck.persistence.ee.packaging.ejb.exclude.Client {
    /**
        EE10 Deployment Descriptors:
        jpa_ee_packaging_ejb_exclude: 
        jpa_ee_packaging_ejb_exclude_client: 
        jpa_ee_packaging_ejb_exclude_ejb: META-INF/persistence.xml

        Found Descriptors:
        Client:

        Ejb:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "jpa_ee_packaging_ejb_exclude", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_ee_packaging_ejb_exclude_client = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_ejb_exclude_client.jar");
            // The class files
            jpa_ee_packaging_ejb_exclude_client.addClasses(
            ee.jakarta.tck.persistence.ee.packaging.ejb.exclude.Client.class,
            ee.jakarta.tck.persistence.ee.packaging.ejb.exclude.Stateful3IF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("");
            if(resURL != null) {
              jpa_ee_packaging_ejb_exclude_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_ee_packaging_ejb_exclude_client.addAsManifestResource(resURL, "application-client.xml");
            }
            jpa_ee_packaging_ejb_exclude_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_ee_packaging_ejb_exclude_client, Client.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_ee_packaging_ejb_exclude_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_ejb_exclude_ejb.jar");
            // The class files
            jpa_ee_packaging_ejb_exclude_ejb.addClasses(
                ee.jakarta.tck.persistence.ee.packaging.ejb.exclude.Stateful3IF.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.exclude.A.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.exclude.Stateful3Bean.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.exclude.B.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client.class.getResource("/");
            if(ejbResURL1 != null) {
              jpa_ee_packaging_ejb_exclude_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client.class.getResource("/.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_ee_packaging_ejb_exclude_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_ee_packaging_ejb_exclude_ejb, Client.class, ejbResURL1);


        // Ear
            EnterpriseArchive jpa_ee_packaging_ejb_exclude_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_packaging_ejb_exclude.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_ee_packaging_ejb_exclude_ear.addAsModule(jpa_ee_packaging_ejb_exclude_ejb);
            jpa_ee_packaging_ejb_exclude_ear.addAsModule(jpa_ee_packaging_ejb_exclude_client);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_ee_packaging_ejb_exclude_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_ee_packaging_ejb_exclude_ear, Client.class, earResURL);
        return jpa_ee_packaging_ejb_exclude_ear;
        }

        @Test
        @Override
        public void test1() throws java.lang.Exception {
            super.test1();
        }

        @Test
        @Override
        public void test2() throws java.lang.Exception {
            super.test2();
        }


}