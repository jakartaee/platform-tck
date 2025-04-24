package com.sun.ts.tests.ejb30.bb.mdb.dest.topic.jarwar;

import com.sun.ts.tests.ejb30.bb.mdb.dest.topic.jarwar.Client;
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
@Tag("ejb_mdb_optional")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.bb.mdb.dest.topic.jarwar.Client {
    /**
        EE10 Deployment Descriptors:
        mdb_dest_topic_jarwar: META-INF/application.xml
        mdb_dest_topic_jarwar_client: META-INF/application-client.xml,jar.sun-application-client.xml
        mdb_dest_topic_jarwar_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        mdb_dest_topic_jarwar_web: WEB-INF/web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/bb/mdb/dest/topic/jarwar/mdb_dest_topic_jarwar_client.xml
        /com/sun/ts/tests/ejb30/bb/mdb/dest/topic/jarwar/mdb_dest_topic_jarwar_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/bb/mdb/dest/topic/jarwar/mdb_dest_topic_jarwar_ejb.xml
        /com/sun/ts/tests/ejb30/bb/mdb/dest/topic/jarwar/mdb_dest_topic_jarwar_ejb.jar.sun-ejb-jar.xml
        War:

        /com/sun/ts/tests/ejb30/bb/mdb/dest/topic/jarwar/mdb_dest_topic_jarwar_web.xml
        Ear:

        /com/sun/ts/tests/ejb30/bb/mdb/dest/topic/jarwar/application.xml
        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_dest_topic_jarwar", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive mdb_dest_topic_jarwar_web = ShrinkWrap.create(WebArchive.class, "mdb_dest_topic_jarwar_web.war");
            // The class files
            mdb_dest_topic_jarwar_web.addClasses(
            com.sun.ts.tests.ejb30.bb.mdb.dest.topic.jarwar.TestServlet.class,
            com.sun.ts.tests.ejb30.common.messaging.Constants.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/topic/jarwar/mdb_dest_topic_jarwar_web.xml");
            if(warResURL != null) {
              mdb_dest_topic_jarwar_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/topic/jarwar/mdb_dest_topic_jarwar_web.war.sun-web.xml");
            if(warResURL != null) {
              mdb_dest_topic_jarwar_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/topic/jarwar/mdb_dest_topic_jarwar_web.xml");
            if(warResURL != null) {
              mdb_dest_topic_jarwar_web.addAsWebResource(warResURL, "//mdb_dest_topic_jarwar_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(mdb_dest_topic_jarwar_web, Client.class, warResURL);

        // Client
            // the jar with the correct archive name
            JavaArchive mdb_dest_topic_jarwar_client = ShrinkWrap.create(JavaArchive.class, "mdb_dest_topic_jarwar_client.jar");
            // The class files
            mdb_dest_topic_jarwar_client.addClasses(
            com.sun.ts.tests.ejb30.common.messaging.TopicClientBase.class,
            com.sun.ts.tests.jms.commonee.Client.class,
            com.sun.ts.tests.ejb30.bb.mdb.dest.common.TopicClientBase.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.bb.mdb.dest.topic.jarwar.Client.class,
            com.sun.ts.tests.ejb30.common.messaging.Constants.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.common.messaging.ClientBase.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/topic/jarwar/mdb_dest_topic_jarwar_client.xml");
            if(resURL != null) {
              mdb_dest_topic_jarwar_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/topic/jarwar/mdb_dest_topic_jarwar_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_dest_topic_jarwar_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdb_dest_topic_jarwar_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_dest_topic_jarwar_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_dest_topic_jarwar_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_dest_topic_jarwar_ejb.jar");
            // The class files
            mdb_dest_topic_jarwar_ejb.addClasses(
                com.sun.ts.tests.ejb30.bb.mdb.dest.common.TopicDestBean.class,
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.ejb30.common.messaging.StatusReporter.class,
                com.sun.ts.tests.ejb30.common.messaging.Constants.class,
                com.sun.ts.tests.ejb30.bb.mdb.dest.common.DestBeanBase.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/topic/jarwar/mdb_dest_topic_jarwar_ejb.xml");
            if(ejbResURL != null) {
              mdb_dest_topic_jarwar_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/topic/jarwar/mdb_dest_topic_jarwar_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_dest_topic_jarwar_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_dest_topic_jarwar_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_dest_topic_jarwar_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_dest_topic_jarwar.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_dest_topic_jarwar_ear.addAsModule(mdb_dest_topic_jarwar_ejb);
            mdb_dest_topic_jarwar_ear.addAsModule(mdb_dest_topic_jarwar_client);
            mdb_dest_topic_jarwar_ear.addAsModule(mdb_dest_topic_jarwar_web);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/topic/jarwar/application.xml");
            if(earResURL != null) {
              mdb_dest_topic_jarwar_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/topic/jarwar/application.ear.sun-application.xml");
            if(earResURL != null) {
              mdb_dest_topic_jarwar_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(mdb_dest_topic_jarwar_ear, Client.class, earResURL);
        return mdb_dest_topic_jarwar_ear;
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