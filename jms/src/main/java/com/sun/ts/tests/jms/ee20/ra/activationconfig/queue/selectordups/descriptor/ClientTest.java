package com.sun.ts.tests.jms.ee20.ra.activationconfig.queue.selectordups.descriptor;

import com.sun.ts.tests.jms.ee20.ra.activationconfig.queue.selectordups.descriptor.Client;
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
@Tag("jms")
@Tag("platform")
@Tag("jms_web")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.jms.ee20.ra.activationconfig.queue.selectordups.descriptor.Client {
    /**
        EE10 Deployment Descriptors:
        queue_selectordups_descriptor: 
        queue_selectordups_descriptor_client: jar.sun-application-client.xml,jar.sun-application-client.xml
        queue_selectordups_descriptor_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml,META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/ee20/ra/activationconfig/queue/selectordups/descriptor/queue_selectordups_descriptor_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/jms/ee20/ra/activationconfig/queue/selectordups/descriptor/queue_selectordups_descriptor_ejb.xml
        /com/sun/ts/tests/jms/ee20/ra/activationconfig/queue/selectordups/descriptor/queue_selectordups_descriptor_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "queue_selectordups_descriptor", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive queue_selectordups_descriptor_client = ShrinkWrap.create(JavaArchive.class, "queue_selectordups_descriptor_client.jar");
            // The class files
            queue_selectordups_descriptor_client.addClasses(
            com.sun.ts.tests.jms.commonee.Client.class,
            com.sun.ts.tests.jms.ee20.ra.activationconfig.common.QueueClientBase.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.jms.ee20.ra.common.messaging.QueueClientBase.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.jms.ee20.ra.activationconfig.queue.selectordups.descriptor.Client.class,
            com.sun.ts.tests.jms.ee20.ra.common.messaging.TLogger.class,
            com.sun.ts.tests.jms.ee20.ra.common.messaging.Constants.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("com/sun/ts/tests/jms/ee20/ra/activationconfig/queue/selectordups/descriptor/queue_selectordups_descriptor_client.xml");
            if(resURL != null) {
              queue_selectordups_descriptor_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/ra/activationconfig/queue/selectordups/descriptor/queue_selectordups_descriptor_client.jar.sun-application-client.xml");
            if(resURL != null) {
              queue_selectordups_descriptor_client.addAsManifestResource(resURL, "application-client.xml");
            }
            queue_selectordups_descriptor_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(queue_selectordups_descriptor_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive queue_selectordups_descriptor_ejb = ShrinkWrap.create(JavaArchive.class, "queue_selectordups_descriptor_ejb.jar");
            // The class files
            queue_selectordups_descriptor_ejb.addClasses(
                com.sun.ts.tests.jms.ee20.ra.activationconfig.common.ActivationConfigBeanBase.class,
                com.sun.ts.tests.jms.ee20.ra.common.messaging.StatusReporter.class,
                com.sun.ts.tests.jms.ee20.ra.activationconfig.queue.selectorauto.descriptor.ActivationConfigBean.class,
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.jms.ee20.ra.common.messaging.TLogger.class,
                com.sun.ts.tests.jms.ee20.ra.common.messaging.Constants.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/ra/activationconfig/queue/selectordups/descriptor/queue_selectordups_descriptor_ejb.xml");
            if(ejbResURL != null) {
              queue_selectordups_descriptor_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/ra/activationconfig/queue/selectordups/descriptor/queue_selectordups_descriptor_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              queue_selectordups_descriptor_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(queue_selectordups_descriptor_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive queue_selectordups_descriptor_ear = ShrinkWrap.create(EnterpriseArchive.class, "queue_selectordups_descriptor.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            queue_selectordups_descriptor_ear.addAsModule(queue_selectordups_descriptor_ejb);
            queue_selectordups_descriptor_ear.addAsModule(queue_selectordups_descriptor_client);



            // The application.xml descriptor
            URL earResURL = null;
            earResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/ra/activationconfig/queue/selectordups/descriptor/application.xml");
            if(earResURL != null) {
              queue_selectordups_descriptor_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/ra/activationconfig/queue/selectordups/descriptor/application.ear.sun-application.xml");
            if(earResURL != null) {
              queue_selectordups_descriptor_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(queue_selectordups_descriptor_ear, Client.class, earResURL);
        return queue_selectordups_descriptor_ear;
        }

        @Test
        @Override
        public void test1() throws java.lang.Exception {
            super.test1();
        }

        @Test
        @Override
        public void negativeTest1() throws java.lang.Exception {
            super.negativeTest1();
        }

        @Test
        @Override
        public void negativeTest2() throws java.lang.Exception {
            super.negativeTest2();
        }


}