package com.sun.ts.tests.jms.ee.mdb.mdb_msgHdrQ;

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
public class ClientTest extends com.sun.ts.tests.jms.ee.mdb.mdb_msgHdrQ.MDBClient {
    /**
        EE10 Deployment Descriptors:
        mdb_msgHdrQ: 
        mdb_msgHdrQ_client: jar.sun-application-client.xml
        mdb_msgHdrQ_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/ee/mdb/mdb_msgHdrQ/mdb_msgHdrQ_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/jms/ee/mdb/mdb_msgHdrQ/mdb_msgHdrQ_ejb.xml
        /com/sun/ts/tests/jms/ee/mdb/mdb_msgHdrQ/mdb_msgHdrQ_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_msgHdrQ", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_msgHdrQ_client = ShrinkWrap.create(JavaArchive.class, "mdb_msgHdrQ_client.jar");
            // The class files
            mdb_msgHdrQ_client.addClasses(
            Fault.class,
            com.sun.ts.tests.jms.common.JmsUtil.class,
            com.sun.ts.tests.jms.ee.mdb.mdb_msgHdrQ.MDBClient.class,
            com.sun.ts.tests.jms.commonee.MDB_Q_Test.class,
            EETest.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = MDBClient.class.getResource("mdb_msgHdrQ_client.xml");
            if(resURL != null) {
              mdb_msgHdrQ_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MDBClient.class.getResource("mdb_msgHdrQ_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_msgHdrQ_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdb_msgHdrQ_client.addAsManifestResource(new StringAsset("Main-Class: " + MDBClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_msgHdrQ_client, MDBClient.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_msgHdrQ_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_msgHdrQ_ejb.jar");
            // The class files
            mdb_msgHdrQ_ejb.addClasses(
                com.sun.ts.tests.jms.ee.mdb.mdb_msgHdrQ.MsgBeanMsgTestHdrQ.class,
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.jms.commonee.MDB_Q_TestEJB.class,
                com.sun.ts.tests.jms.commonee.MDB_Q_Test.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = MDBClient.class.getResource("mdb_msgHdrQ_ejb.xml");
            if(ejbResURL != null) {
              mdb_msgHdrQ_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = MDBClient.class.getResource("mdb_msgHdrQ_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_msgHdrQ_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_msgHdrQ_ejb, MDBClient.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_msgHdrQ_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_msgHdrQ.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_msgHdrQ_ear.addAsModule(mdb_msgHdrQ_ejb);
            mdb_msgHdrQ_ear.addAsModule(mdb_msgHdrQ_client);




        return mdb_msgHdrQ_ear;
        }

        @Test
        @Override
        public void mdbMsgHdrTimeStampQTest() throws java.lang.Exception {
            super.mdbMsgHdrTimeStampQTest();
        }

        @Test
        @Override
        public void mdbMsgHdrCorlIdQTextTest() throws java.lang.Exception {
            super.mdbMsgHdrCorlIdQTextTest();
        }

        @Test
        @Override
        public void mdbMsgHdrCorlIdQBytesTest() throws java.lang.Exception {
            super.mdbMsgHdrCorlIdQBytesTest();
        }

        @Test
        @Override
        public void mdbMsgHdrCorlIdQMapTest() throws java.lang.Exception {
            super.mdbMsgHdrCorlIdQMapTest();
        }

        @Test
        @Override
        public void mdbMsgHdrCorlIdQStreamTest() throws java.lang.Exception {
            super.mdbMsgHdrCorlIdQStreamTest();
        }

        @Test
        @Override
        public void mdbMsgHdrCorlIdQObjectTest() throws java.lang.Exception {
            super.mdbMsgHdrCorlIdQObjectTest();
        }

        @Test
        @Override
        public void mdbMsgHdrReplyToQTest() throws java.lang.Exception {
            super.mdbMsgHdrReplyToQTest();
        }

        @Test
        @Override
        public void mdbMsgHdrJMSTypeQTest() throws java.lang.Exception {
            super.mdbMsgHdrJMSTypeQTest();
        }

        @Test
        @Override
        public void mdbMsgHdrJMSPriorityQTest() throws java.lang.Exception {
            super.mdbMsgHdrJMSPriorityQTest();
        }

        @Test
        @Override
        public void mdbMsgHdrJMSExpirationQueueTest() throws java.lang.Exception {
            super.mdbMsgHdrJMSExpirationQueueTest();
        }

        @Test
        @Override
        public void mdbMsgHdrJMSDestinationQTest() throws java.lang.Exception {
            super.mdbMsgHdrJMSDestinationQTest();
        }

        @Test
        @Override
        public void mdbMsgHdrJMSDeliveryModeQTest() throws java.lang.Exception {
            super.mdbMsgHdrJMSDeliveryModeQTest();
        }

        @Test
        @Override
        public void mdbMsgHdrIDQTest() throws java.lang.Exception {
            super.mdbMsgHdrIDQTest();
        }


}