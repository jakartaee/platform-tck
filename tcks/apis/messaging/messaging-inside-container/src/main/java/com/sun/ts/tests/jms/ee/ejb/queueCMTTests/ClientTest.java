package com.sun.ts.tests.jms.ee.ejb.queueCMTTests;

import com.sun.ts.lib.harness.Fault;

import java.net.URL;

import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.tests.common.base.EETest;
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


@ExtendWith(ArquillianExtension.class)
@Tag("jms")
@Tag("platform")
@Tag("jms_web")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.jms.ee.ejb.queueCMTTests.Client {
    /**
        EE10 Deployment Descriptors:
        jms_ejb_queueCMTTests: 
        jms_ejb_queueCMTTests_client: META-INF/application-client.xml,jar.sun-application-client.xml
        jms_ejb_queueCMTTests_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/ee/ejb/queueCMTTests/jms_ejb_queueCMTTests_client.xml
        /com/sun/ts/tests/jms/ee/ejb/queueCMTTests/jms_ejb_queueCMTTests_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/jms/ee/ejb/queueCMTTests/jms_ejb_queueCMTTests_ejb.xml
        /com/sun/ts/tests/jms/ee/ejb/queueCMTTests/jms_ejb_queueCMTTests_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "jms_ejb_queueCMTTests", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jms_ejb_queueCMTTests_client = ShrinkWrap.create(JavaArchive.class, "jms_ejb_queueCMTTests_client.jar");
            // The class files
            jms_ejb_queueCMTTests_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            Fault.class,
            com.sun.ts.tests.jms.ee.ejb.queueCMTTests.Client.class,
            EETest.class,
            SetupException.class,
            com.sun.ts.tests.jms.commonee.Tests.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("jms_ejb_queueCMTTests_client.xml");
            if(resURL != null) {
              jms_ejb_queueCMTTests_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("jms_ejb_queueCMTTests_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jms_ejb_queueCMTTests_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jms_ejb_queueCMTTests_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jms_ejb_queueCMTTests_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive jms_ejb_queueCMTTests_ejb = ShrinkWrap.create(JavaArchive.class, "jms_ejb_queueCMTTests_ejb.jar");
            // The class files
            jms_ejb_queueCMTTests_ejb.addClasses(
                com.sun.ts.tests.jms.commonee.TestsEJB.class,
                com.sun.ts.tests.jms.commonee.Tests.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee/ejb/queueCMTTests/jms_ejb_queueCMTTests_ejb.xml");
            if(ejbResURL != null) {
              jms_ejb_queueCMTTests_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee/ejb/queueCMTTests/jms_ejb_queueCMTTests_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              jms_ejb_queueCMTTests_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jms_ejb_queueCMTTests_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive jms_ejb_queueCMTTests_ear = ShrinkWrap.create(EnterpriseArchive.class, "jms_ejb_queueCMTTests.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jms_ejb_queueCMTTests_ear.addAsModule(jms_ejb_queueCMTTests_ejb);
            jms_ejb_queueCMTTests_ear.addAsModule(jms_ejb_queueCMTTests_client);



        return jms_ejb_queueCMTTests_ear;
        }

        @Test
        @Override
        public void bytesMessageFullMsgTests() throws java.lang.Exception {
            super.bytesMessageFullMsgTests();
        }

        @Test
        @Override
        public void mapMessageFullMsgTest() throws java.lang.Exception {
            super.mapMessageFullMsgTest();
        }

        @Test
        @Override
        public void streamMessageFullMsgTest() throws java.lang.Exception {
            super.streamMessageFullMsgTest();
        }

        @Test
        @Override
        public void msgHdrIDTest() throws java.lang.Exception {
            super.msgHdrIDTest();
        }

        @Test
        @Override
        public void msgHdrTimeStampTest() throws java.lang.Exception {
            super.msgHdrTimeStampTest();
        }

        @Test
        @Override
        public void msgHdrCorlIdTest() throws java.lang.Exception {
            super.msgHdrCorlIdTest();
        }

        @Test
        @Override
        public void msgHdrReplyToTest() throws java.lang.Exception {
            super.msgHdrReplyToTest();
        }

        @Test
        @Override
        public void msgHdrJMSTypeTest() throws java.lang.Exception {
            super.msgHdrJMSTypeTest();
        }

        @Test
        @Override
        public void msgHdrJMSPriorityTest() throws java.lang.Exception {
            super.msgHdrJMSPriorityTest();
        }

        @Test
        @Override
        public void msgHdrJMSExpirationTest() throws java.lang.Exception {
            super.msgHdrJMSExpirationTest();
        }

        @Test
        @Override
        public void msgHdrJMSDestinationTest() throws java.lang.Exception {
            super.msgHdrJMSDestinationTest();
        }

        @Test
        @Override
        public void msgHdrJMSDeliveryModeTest() throws java.lang.Exception {
            super.msgHdrJMSDeliveryModeTest();
        }

        @Test
        @Override
        public void messageOrderTest() throws java.lang.Exception {
            super.messageOrderTest();
        }

        @Test
        @Override
        public void nullDestinationTest() throws java.lang.Exception {
            super.nullDestinationTest();
        }


}