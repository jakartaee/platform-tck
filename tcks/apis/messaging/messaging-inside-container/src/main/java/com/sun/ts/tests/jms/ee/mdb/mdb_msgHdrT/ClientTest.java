package com.sun.ts.tests.jms.ee.mdb.mdb_msgHdrT;

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
public class ClientTest extends com.sun.ts.tests.jms.ee.mdb.mdb_msgHdrT.MDBClient {
    /**
        EE10 Deployment Descriptors:
        mdb_msgHdrT: 
        mdb_msgHdrT_client: jar.sun-application-client.xml
        mdb_msgHdrT_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/ee/mdb/mdb_msgHdrT/mdb_msgHdrT_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/jms/ee/mdb/mdb_msgHdrT/mdb_msgHdrT_ejb.xml
        /com/sun/ts/tests/jms/ee/mdb/mdb_msgHdrT/mdb_msgHdrT_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_msgHdrT", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_msgHdrT_client = ShrinkWrap.create(JavaArchive.class, "mdb_msgHdrT_client.jar");
            // The class files
            mdb_msgHdrT_client.addClasses(
            com.sun.ts.tests.jms.commonee.MDB_T_Test.class,
            Fault.class,
            com.sun.ts.tests.jms.common.JmsUtil.class,
            EETest.class,
            com.sun.ts.tests.jms.ee.mdb.mdb_msgHdrT.MDBClient.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = MDBClient.class.getResource("mdb_msgHdrT_client.xml");
            if(resURL != null) {
              mdb_msgHdrT_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MDBClient.class.getResource("mdb_msgHdrT_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_msgHdrT_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdb_msgHdrT_client.addAsManifestResource(new StringAsset("Main-Class: " + MDBClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_msgHdrT_client, MDBClient.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_msgHdrT_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_msgHdrT_ejb.jar");
            // The class files
            mdb_msgHdrT_ejb.addClasses(
                com.sun.ts.tests.jms.commonee.MDB_T_TestEJB.class,
                com.sun.ts.tests.jms.commonee.MDB_T_Test.class,
                com.sun.ts.tests.jms.ee.mdb.mdb_msgHdrT.MsgBeanMsgTestHdrT.class,
                com.sun.ts.tests.jms.common.JmsUtil.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = MDBClient.class.getResource("mdb_msgHdrT_ejb.xml");
            if(ejbResURL != null) {
              mdb_msgHdrT_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = MDBClient.class.getResource("mdb_msgHdrT_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_msgHdrT_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_msgHdrT_ejb, MDBClient.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_msgHdrT_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_msgHdrT.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_msgHdrT_ear.addAsModule(mdb_msgHdrT_ejb);
            mdb_msgHdrT_ear.addAsModule(mdb_msgHdrT_client);




        return mdb_msgHdrT_ear;
        }

        @Test
        @Override
        public void mdbMsgHdrTimeStampTTest() throws java.lang.Exception {
            super.mdbMsgHdrTimeStampTTest();
        }

        @Test
        @Override
        public void mdbMsgHdrCorlIdTTextTest() throws java.lang.Exception {
            super.mdbMsgHdrCorlIdTTextTest();
        }

        @Test
        @Override
        public void mdbMsgHdrCorlIdTBytesTest() throws java.lang.Exception {
            super.mdbMsgHdrCorlIdTBytesTest();
        }

        @Test
        @Override
        public void mdbMsgHdrCorlIdTMapTest() throws java.lang.Exception {
            super.mdbMsgHdrCorlIdTMapTest();
        }

        @Test
        @Override
        public void mdbMsgHdrCorlIdTStreamTest() throws java.lang.Exception {
            super.mdbMsgHdrCorlIdTStreamTest();
        }

        @Test
        @Override
        public void mdbMsgHdrCorlIdTObjectTest() throws java.lang.Exception {
            super.mdbMsgHdrCorlIdTObjectTest();
        }

        @Test
        @Override
        public void mdbMsgHdrReplyToTTest() throws java.lang.Exception {
            super.mdbMsgHdrReplyToTTest();
        }

        @Test
        @Override
        public void mdbMsgHdrJMSTypeTTest() throws java.lang.Exception {
            super.mdbMsgHdrJMSTypeTTest();
        }

        @Test
        @Override
        public void mdbMsgHdrJMSPriorityTTest() throws java.lang.Exception {
            super.mdbMsgHdrJMSPriorityTTest();
        }

        @Test
        @Override
        public void mdbMsgHdrJMSExpirationTopicTest() throws java.lang.Exception {
            super.mdbMsgHdrJMSExpirationTopicTest();
        }

        @Test
        @Override
        public void mdbMsgHdrJMSDestinationTTest() throws java.lang.Exception {
            super.mdbMsgHdrJMSDestinationTTest();
        }

        @Test
        @Override
        public void mdbMsgHdrJMSDeliveryModeTTest() throws java.lang.Exception {
            super.mdbMsgHdrJMSDeliveryModeTTest();
        }

        @Test
        @Override
        public void mdbMsgHdrIDTTest() throws java.lang.Exception {
            super.mdbMsgHdrIDTTest();
        }


}