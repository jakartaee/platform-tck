package ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor;

import ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.Client;
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
public class ClientTest extends ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.Client {
    /**
        EE10 Deployment Descriptors:
        jpa_ee_packaging_ejb_descriptor: 
        jpa_ee_packaging_ejb_descriptor_client: META-INF/application-client.xml
        jpa_ee_packaging_ejb_descriptor_ejb: META-INF/persistence.xml,META-INF/orm.xml,myMappingFile.xml,myMappingFile2.xml,META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jpa/ee/packaging/ejb/descriptor/jpa_ee_packaging_ejb_descriptor_client.xml
        Ejb:

        /com/sun/ts/tests/jpa/ee/packaging/ejb/descriptor/jpa_ee_packaging_ejb_descriptor_ejb.jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "jpa_ee_packaging_ejb_descriptor", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_ee_packaging_ejb_descriptor_client = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_ejb_descriptor_client.jar");
            // The class files
            jpa_ee_packaging_ejb_descriptor_client.addClasses(
            ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.C.class,
            ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.Client.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.Stateless3IF.class,
            com.sun.ts.lib.harness.EETest.class,
            ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.Stateful3IF.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("com/sun/ts/tests/jpa/ee/packaging/ejb/descriptor/jpa_ee_packaging_ejb_descriptor_client.xml");
            if(resURL != null) {
              jpa_ee_packaging_ejb_descriptor_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/packaging/ejb/descriptor/jpa_ee_packaging_ejb_descriptor_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_ee_packaging_ejb_descriptor_client.addAsManifestResource(resURL, "application-client.xml");
            }
            jpa_ee_packaging_ejb_descriptor_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_ee_packaging_ejb_descriptor_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive jpa_ee_packaging_ejb_descriptor_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_ejb_descriptor_ejb.jar");
            // The class files
            jpa_ee_packaging_ejb_descriptor_ejb.addClasses(
                ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.C.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.A.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.Stateless3IF.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.Stateful3Bean.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.B.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.Stateful3IF.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.Stateless3Bean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/packaging/ejb/descriptor/jpa_ee_packaging_ejb_descriptor_ejb.jar.xml");
            if(ejbResURL != null) {
              jpa_ee_packaging_ejb_descriptor_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/packaging/ejb/descriptor/jpa_ee_packaging_ejb_descriptor_ejb.jar.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              jpa_ee_packaging_ejb_descriptor_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_ee_packaging_ejb_descriptor_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive jpa_ee_packaging_ejb_descriptor_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_packaging_ejb_descriptor.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_ee_packaging_ejb_descriptor_ear.addAsModule(jpa_ee_packaging_ejb_descriptor_ejb);
            jpa_ee_packaging_ejb_descriptor_ear.addAsModule(jpa_ee_packaging_ejb_descriptor_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/packaging/ejb/descriptor/");
            if(earResURL != null) {
              jpa_ee_packaging_ejb_descriptor_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/packaging/ejb/descriptor/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_ee_packaging_ejb_descriptor_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_ee_packaging_ejb_descriptor_ear, Client.class, earResURL);
        return jpa_ee_packaging_ejb_descriptor_ear;
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


}