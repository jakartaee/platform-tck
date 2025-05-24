package com.sun.ts.tests.jms.ee20.ra.activationconfig.topic.selectorautociddurable.annotated;

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
public class ClientTest extends com.sun.ts.tests.jms.ee20.ra.activationconfig.topic.selectorautociddurable.annotated.Client {
    /**
        EE10 Deployment Descriptors:
        topic_selectorautociddurable_annotated: 
        topic_selectorautociddurable_annotated_client: jar.sun-application-client.xml
        topic_selectorautociddurable_annotated_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/ee20/ra/activationconfig/topic/selectorautociddurable/annotated/topic_selectorautociddurable_annotated_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/jms/ee20/ra/activationconfig/topic/selectorautociddurable/annotated/topic_selectorautociddurable_annotated_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "topic_selectorautociddurable_annotated", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive topic_selectorautociddurable_annotated_client = ShrinkWrap.create(JavaArchive.class, "topic_selectorautociddurable_annotated_client.jar");
            // The class files
            topic_selectorautociddurable_annotated_client.addClasses(
            com.sun.ts.tests.jms.commonee.Client.class,
            Fault.class,
            com.sun.ts.tests.jms.ee20.ra.activationconfig.common.TopicClientBase.class,
            com.sun.ts.tests.jms.ee20.ra.common.messaging.QueueClientBase.class,
            com.sun.ts.tests.jms.ee20.ra.common.messaging.TopicClientBase.class,
            com.sun.ts.tests.jms.ee20.ra.activationconfig.topic.selectorautociddurable.annotated.Client.class,
            EETest.class,
            com.sun.ts.tests.jms.ee20.ra.common.messaging.TLogger.class,
            com.sun.ts.tests.jms.ee20.ra.common.messaging.Constants.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("topic_selectorautociddurable_annotated_client.xml");
            if(resURL != null) {
              topic_selectorautociddurable_annotated_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/ra/activationconfig/topic/selectorautociddurable/annotated/topic_selectorautociddurable_annotated_client.jar.sun-application-client.xml");
            if(resURL != null) {
              topic_selectorautociddurable_annotated_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            topic_selectorautociddurable_annotated_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(topic_selectorautociddurable_annotated_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive topic_selectorautociddurable_annotated_ejb = ShrinkWrap.create(JavaArchive.class, "topic_selectorautociddurable_annotated_ejb.jar");
            // The class files
            topic_selectorautociddurable_annotated_ejb.addClasses(
                com.sun.ts.tests.jms.ee20.ra.activationconfig.common.ActivationConfigBeanBase.class,
                com.sun.ts.tests.jms.ee20.ra.common.messaging.StatusReporter.class,
                com.sun.ts.tests.jms.ee20.ra.activationconfig.topic.selectorautociddurable.annotated.ActivationConfigBean.class,
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.jms.ee20.ra.common.messaging.TLogger.class,
                com.sun.ts.tests.jms.ee20.ra.common.messaging.Constants.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/ra/activationconfig/topic/selectorautociddurable/annotated/topic_selectorautociddurable_annotated_ejb.xml");
            if(ejbResURL != null) {
              topic_selectorautociddurable_annotated_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/ra/activationconfig/topic/selectorautociddurable/annotated/topic_selectorautociddurable_annotated_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              topic_selectorautociddurable_annotated_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(topic_selectorautociddurable_annotated_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive topic_selectorautociddurable_annotated_ear = ShrinkWrap.create(EnterpriseArchive.class, "topic_selectorautociddurable_annotated.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            topic_selectorautociddurable_annotated_ear.addAsModule(topic_selectorautociddurable_annotated_ejb);
            topic_selectorautociddurable_annotated_ear.addAsModule(topic_selectorautociddurable_annotated_client);



            // The application.xml descriptor
            URL earResURL = null;
            earResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/ra/activationconfig/topic/selectorautociddurable/annotated/application.xml");
            if(earResURL != null) {
              topic_selectorautociddurable_annotated_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/ra/activationconfig/topic/selectorautociddurable/annotated/application.ear.sun-application.xml");
            if(earResURL != null) {
              topic_selectorautociddurable_annotated_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(topic_selectorautociddurable_annotated_ear, Client.class, earResURL);
        return topic_selectorautociddurable_annotated_ear;
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