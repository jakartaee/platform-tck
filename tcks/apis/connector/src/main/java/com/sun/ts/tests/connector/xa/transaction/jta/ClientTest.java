package com.sun.ts.tests.connector.xa.transaction.jta;

import com.sun.ts.tests.connector.xa.transaction.jta.JTATestClient;
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
@Tag("connector_jta_optional")
@Tag("platform")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.connector.xa.transaction.jta.JTATestClient {
    /**
        EE10 Deployment Descriptors:
        ejb_JTATest: 
        ejb_JTATest_client: META-INF/application-client.xml,jar.sun-application-client.xml
        ejb_JTATest_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/connector/xa/transaction/jta/ejb_JTATest_client.xml
        /com/sun/ts/tests/connector/xa/transaction/jta/ejb_JTATest_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/connector/xa/transaction/jta/ejb_JTATest_ejb.xml
        /com/sun/ts/tests/connector/xa/transaction/jta/ejb_JTATest_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb_JTATest", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb_JTATest_client = ShrinkWrap.create(JavaArchive.class, "ejb_JTATest_client.jar");
            // The class files
            ejb_JTATest_client.addClasses(
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.connector.xa.transaction.jta.JTATestClient.class,
            com.sun.ts.tests.connector.xa.transaction.jta.JTATest.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = JTATestClient.class.getResource("/com/sun/ts/tests/connector/xa/transaction/jta/ejb_JTATest_client.xml");
            ejb_JTATest_client.addAsManifestResource(resURL, "application-client.xml");
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = JTATestClient.class.getResource("/com/sun/ts/tests/connector/xa/transaction/jta/ejb_JTATest_client.jar.sun-application-client.xml");
            ejb_JTATest_client.addAsManifestResource(resURL, "sun-application-client.xml");
            ejb_JTATest_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.connector.xa.transaction.jta.JTATestClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb_JTATest_client, JTATestClient.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive ejb_JTATest_ejb = ShrinkWrap.create(JavaArchive.class, "ejb_JTATest_ejb.jar");
            // The class files
            ejb_JTATest_ejb.addClasses(
                com.sun.ts.tests.connector.xa.transaction.jta.JTATestEJB.class,
                com.sun.ts.tests.connector.xa.transaction.jta.JTATest.class,
                com.sun.ts.tests.connector.util.DBSupport.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = JTATestClient.class.getResource("/com/sun/ts/tests/connector/xa/transaction/jta/ejb_JTATest_ejb.xml");
            if(ejbResURL1 != null) {
              ejb_JTATest_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = JTATestClient.class.getResource("/com/sun/ts/tests/connector/xa/transaction/jta/ejb_JTATest_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              ejb_JTATest_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb_JTATest_ejb, JTATestClient.class, ejbResURL1);


        // Ear
            EnterpriseArchive ejb_JTATest_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb_JTATest.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            ejb_JTATest_ear.addAsModule(ejb_JTATest_ejb);
            ejb_JTATest_ear.addAsModule(ejb_JTATest_client);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb_JTATest_ear, JTATestClient.class, earResURL);
        return ejb_JTATest_ear;
        }

        @Test
        @Override
        public void testXAResource1() throws java.lang.Exception {
            super.testXAResource1();
        }

        @Test
        @Override
        public void testXAResource2() throws java.lang.Exception {
            super.testXAResource2();
        }


}