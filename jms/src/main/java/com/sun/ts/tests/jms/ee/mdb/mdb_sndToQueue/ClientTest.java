package com.sun.ts.tests.jms.ee.mdb.mdb_sndToQueue;

import com.sun.ts.tests.jms.ee.mdb.mdb_sndToQueue.MDBClient;
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
public class ClientTest extends com.sun.ts.tests.jms.ee.mdb.mdb_sndToQueue.MDBClient {
    /**
        EE10 Deployment Descriptors:
        mdb_sndToQueue: 
        mdb_sndToQueue_client: jar.sun-application-client.xml
        mdb_sndToQueue_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/ee/mdb/mdb_sndToQueue/mdb_sndToQueue_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/jms/ee/mdb/mdb_sndToQueue/mdb_sndToQueue_ejb.xml
        /com/sun/ts/tests/jms/ee/mdb/mdb_sndToQueue/mdb_sndToQueue_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_sndToQueue", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_sndToQueue_client = ShrinkWrap.create(JavaArchive.class, "mdb_sndToQueue_client.jar");
            // The class files
            mdb_sndToQueue_client.addClasses(
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.jms.ee.mdb.mdb_sndToQueue.MDBClient.class,
            com.sun.ts.tests.jms.common.JmsUtil.class,
            com.sun.ts.tests.jms.commonee.MDB_Q_Test.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = MDBClient.class.getResource("com/sun/ts/tests/jms/ee/mdb/mdb_sndToQueue/mdb_sndToQueue_client.xml");
            if(resURL != null) {
              mdb_sndToQueue_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MDBClient.class.getResource("/com/sun/ts/tests/jms/ee/mdb/mdb_sndToQueue/mdb_sndToQueue_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_sndToQueue_client.addAsManifestResource(resURL, "application-client.xml");
            }
            mdb_sndToQueue_client.addAsManifestResource(new StringAsset("Main-Class: " + MDBClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_sndToQueue_client, MDBClient.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_sndToQueue_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_sndToQueue_ejb.jar");
            // The class files
            mdb_sndToQueue_ejb.addClasses(
                com.sun.ts.tests.jms.ee.mdb.mdb_sndToQueue.MsgBean.class,
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.jms.commonee.MDB_Q_TestEJB.class,
                com.sun.ts.tests.jms.commonee.MDB_Q_Test.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = MDBClient.class.getResource("/com/sun/ts/tests/jms/ee/mdb/mdb_sndToQueue/mdb_sndToQueue_ejb.xml");
            if(ejbResURL != null) {
              mdb_sndToQueue_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = MDBClient.class.getResource("/com/sun/ts/tests/jms/ee/mdb/mdb_sndToQueue/mdb_sndToQueue_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_sndToQueue_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_sndToQueue_ejb, MDBClient.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_sndToQueue_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_sndToQueue.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_sndToQueue_ear.addAsModule(mdb_sndToQueue_ejb);
            mdb_sndToQueue_ear.addAsModule(mdb_sndToQueue_client);



            // The application.xml descriptor
            URL earResURL = MDBClient.class.getResource("/com/sun/ts/tests/jms/ee/mdb/mdb_sndToQueue/");
            if(earResURL != null) {
              mdb_sndToQueue_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = MDBClient.class.getResource("/com/sun/ts/tests/jms/ee/mdb/mdb_sndToQueue/.ear.sun-application.xml");
            if(earResURL != null) {
              mdb_sndToQueue_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(mdb_sndToQueue_ear, MDBClient.class, earResURL);
        return mdb_sndToQueue_ear;
        }

        @Test
        @Override
        public void mdbSendTextMsgToQueueTest() throws java.lang.Exception {
            super.mdbSendTextMsgToQueueTest();
        }

        @Test
        @Override
        public void mdbSendBytesMsgToQueueTest() throws java.lang.Exception {
            super.mdbSendBytesMsgToQueueTest();
        }

        @Test
        @Override
        public void mdbSendMapMsgToQueueTest() throws java.lang.Exception {
            super.mdbSendMapMsgToQueueTest();
        }

        @Test
        @Override
        public void mdbSendStreamMsgToQueueTest() throws java.lang.Exception {
            super.mdbSendStreamMsgToQueueTest();
        }

        @Test
        @Override
        public void mdbSendObjectMsgToQueueTest() throws java.lang.Exception {
            super.mdbSendObjectMsgToQueueTest();
        }


}