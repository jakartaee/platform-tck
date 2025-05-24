package com.sun.ts.tests.ejb30.bb.mdb.activationconfig.topic.selectordupsnondurable.complement;

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
@Tag("platform")
@Tag("ejb_mdb_optional")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.bb.mdb.activationconfig.topic.selectordupsnondurable.complement.Client {
    /**
        EE10 Deployment Descriptors:
        topic_selectordupsnondurable_complement: 
        topic_selectordupsnondurable_complement_client: jar.sun-application-client.xml
        topic_selectordupsnondurable_complement_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/bb/mdb/activationconfig/topic/selectordupsnondurable/complement/topic_selectordupsnondurable_complement_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/bb/mdb/activationconfig/topic/selectordupsnondurable/complement/topic_selectordupsnondurable_complement_ejb.xml
        /com/sun/ts/tests/ejb30/bb/mdb/activationconfig/topic/selectordupsnondurable/complement/topic_selectordupsnondurable_complement_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "topic_selectordupsnondurable_complement", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive topic_selectordupsnondurable_complement_client = ShrinkWrap.create(JavaArchive.class, "topic_selectordupsnondurable_complement_client.jar");
            // The class files
            topic_selectordupsnondurable_complement_client.addClasses(
            com.sun.ts.tests.ejb30.common.messaging.TopicClientBase.class,
            com.sun.ts.tests.jms.commonee.Client.class,
            Fault.class,
            com.sun.ts.tests.ejb30.bb.mdb.activationconfig.topic.selectordupsnondurable.complement.Client.class,
            com.sun.ts.tests.ejb30.common.messaging.Constants.class,
            EETest.class,
            com.sun.ts.tests.ejb30.bb.mdb.activationconfig.common.TopicClientBase.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.common.messaging.ClientBase.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/activationconfig/topic/selectordupsnondurable/complement/topic_selectordupsnondurable_complement_client.xml");
            if(resURL != null) {
              topic_selectordupsnondurable_complement_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/activationconfig/topic/selectordupsnondurable/complement/topic_selectordupsnondurable_complement_client.jar.sun-application-client.xml");
            if(resURL != null) {
              topic_selectordupsnondurable_complement_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            topic_selectordupsnondurable_complement_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(topic_selectordupsnondurable_complement_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive topic_selectordupsnondurable_complement_ejb = ShrinkWrap.create(JavaArchive.class, "topic_selectordupsnondurable_complement_ejb.jar");
            // The class files
            topic_selectordupsnondurable_complement_ejb.addClasses(
                com.sun.ts.tests.ejb30.bb.mdb.activationconfig.topic.selectordupsnondurable.complement.ActivationConfigBean.class,
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.ejb30.common.messaging.StatusReporter.class,
                com.sun.ts.tests.ejb30.common.messaging.Constants.class,
                com.sun.ts.tests.ejb30.bb.mdb.activationconfig.common.ActivationConfigBeanBase.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/activationconfig/topic/selectordupsnondurable/complement/topic_selectordupsnondurable_complement_ejb.xml");
            if(ejbResURL != null) {
              topic_selectordupsnondurable_complement_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/activationconfig/topic/selectordupsnondurable/complement/topic_selectordupsnondurable_complement_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              topic_selectordupsnondurable_complement_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(topic_selectordupsnondurable_complement_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive topic_selectordupsnondurable_complement_ear = ShrinkWrap.create(EnterpriseArchive.class, "topic_selectordupsnondurable_complement.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            topic_selectordupsnondurable_complement_ear.addAsModule(topic_selectordupsnondurable_complement_ejb);
            topic_selectordupsnondurable_complement_ear.addAsModule(topic_selectordupsnondurable_complement_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/activationconfig/topic/selectordupsnondurable/complement/application.xml");
            if(earResURL != null) {
              topic_selectordupsnondurable_complement_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/activationconfig/topic/selectordupsnondurable/complement/application.ear.sun-application.xml");
            if(earResURL != null) {
              topic_selectordupsnondurable_complement_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(topic_selectordupsnondurable_complement_ear, Client.class, earResURL);
        return topic_selectordupsnondurable_complement_ear;
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