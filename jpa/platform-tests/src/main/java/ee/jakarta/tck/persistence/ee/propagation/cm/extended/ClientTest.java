package ee.jakarta.tck.persistence.ee.propagation.cm.extended;

import ee.jakarta.tck.persistence.ee.propagation.cm.extended.Client;
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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("tck-appclient")

public class ClientTest extends ee.jakarta.tck.persistence.ee.propagation.cm.extended.Client {
    /**
        EE10 Deployment Descriptors:
        jpa_ee_propagation_cm_ext: META-INF/persistence.xml
        jpa_ee_propagation_cm_ext_client: jar.sun-application-client.xml
        jpa_ee_propagation_cm_ext_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "jpa_ee_propagation_cm_ext", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_ee_propagation_cm_ext_client = ShrinkWrap.create(JavaArchive.class, "jpa_ee_propagation_cm_ext_client.jar");
            // The class files
            jpa_ee_propagation_cm_ext_client.addClasses(
            com.sun.ts.lib.harness.EETest.Fault.class,
            ee.jakarta.tck.persistence.ee.propagation.cm.extended.Stateful3IF.class,
            com.sun.ts.lib.harness.EETest.class,
            ee.jakarta.tck.persistence.ee.propagation.cm.extended.Client.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("com/sun/ts/tests/jpa/ee/propagation/cm/extended/");
            if(resURL != null) {
              jpa_ee_propagation_cm_ext_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/propagation/cm/extended/.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_ee_propagation_cm_ext_client.addAsManifestResource(resURL, "application-client.xml");
            }
            jpa_ee_propagation_cm_ext_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_ee_propagation_cm_ext_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive jpa_ee_propagation_cm_ext_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_ee_propagation_cm_ext_ejb.jar");
            // The class files
            jpa_ee_propagation_cm_ext_ejb.addClasses(
                ee.jakarta.tck.persistence.ee.propagation.cm.extended.Stateful3IF.class,
                ee.jakarta.tck.persistence.ee.propagation.cm.extended.TellerBean.class,
                ee.jakarta.tck.persistence.ee.propagation.cm.extended.Teller.class,
                ee.jakarta.tck.persistence.ee.propagation.cm.extended.Stateful3Bean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/propagation/cm/extended/");
            if(ejbResURL != null) {
              jpa_ee_propagation_cm_ext_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/propagation/cm/extended/.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              jpa_ee_propagation_cm_ext_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_ee_propagation_cm_ext_ejb, Client.class, ejbResURL);

        // Par
            // the jar with the correct archive name
            JavaArchive jpa_ee_propagation_cm_ext = ShrinkWrap.create(JavaArchive.class, "jpa_ee_propagation_cm_ext.jar");
            // The class files
            jpa_ee_propagation_cm_ext.addClasses(
                ee.jakarta.tck.persistence.ee.common.A.class,
                ee.jakarta.tck.persistence.ee.common.Account.class,
                ee.jakarta.tck.persistence.ee.common.B.class
            );
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_ee_propagation_cm_ext.addAsManifestResource(parURL, "persistence.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_ee_propagation_cm_ext, Client.class, parURL);
            // The orm.xml file
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_ee_propagation_cm_ext.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_ee_propagation_cm_ext_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_propagation_cm_ext.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_ee_propagation_cm_ext_ear.addAsModule(jpa_ee_propagation_cm_ext_ejb);
            jpa_ee_propagation_cm_ext_ear.addAsModule(jpa_ee_propagation_cm_ext_client);

            jpa_ee_propagation_cm_ext_ear.addAsLibrary(jpa_ee_propagation_cm_ext);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/propagation/cm/extended/");
            if(earResURL != null) {
              jpa_ee_propagation_cm_ext_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/propagation/cm/extended/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_ee_propagation_cm_ext_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_ee_propagation_cm_ext_ear, Client.class, earResURL);
        return jpa_ee_propagation_cm_ext_ear;
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

        @Test
        @Override
        public void test3() throws java.lang.Exception {
            super.test3();
        }

        @Test
        @Override
        public void test4() throws java.lang.Exception {
            super.test4();
        }

        @Test
        @Override
        public void test5() throws java.lang.Exception {
            super.test5();
        }

        @Test
        @Override
        public void test6() throws java.lang.Exception {
            super.test6();
        }

        @Test
        @Override
        public void test7() throws java.lang.Exception {
            super.test7();
        }


}