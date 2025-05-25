package com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesT3;

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
public class ClientTest extends com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesT3.MDBClient {
    /**
        EE10 Deployment Descriptors:
        mdb_msgTypesT3: 
        mdb_msgTypesT3_client: jar.sun-application-client.xml
        mdb_msgTypesT3_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/ee/mdb/mdb_msgTypesT3/mdb_msgTypesT3_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/jms/ee/mdb/mdb_msgTypesT3/mdb_msgTypesT3_ejb.xml
        /com/sun/ts/tests/jms/ee/mdb/mdb_msgTypesT3/mdb_msgTypesT3_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_msgTypesT3", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_msgTypesT3_client = ShrinkWrap.create(JavaArchive.class, "mdb_msgTypesT3_client.jar");
            // The class files
            mdb_msgTypesT3_client.addClasses(
            com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesT3.MDBClient.class,
            com.sun.ts.tests.jms.commonee.MDB_T_Test.class,
            Fault.class,
            com.sun.ts.tests.jms.common.JmsUtil.class,
            EETest.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = MDBClient.class.getResource("mdb_msgTypesT3_client.xml");
            if(resURL != null) {
              mdb_msgTypesT3_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MDBClient.class.getResource("mdb_msgTypesT3_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_msgTypesT3_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdb_msgTypesT3_client.addAsManifestResource(new StringAsset("Main-Class: " + MDBClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_msgTypesT3_client, MDBClient.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_msgTypesT3_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_msgTypesT3_ejb.jar");
            // The class files
            mdb_msgTypesT3_ejb.addClasses(
                com.sun.ts.tests.jms.commonee.MDB_T_TestEJB.class,
                com.sun.ts.tests.jms.commonee.MDB_T_Test.class,
                com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesT3.MsgBeanMsgTestT3.class,
                com.sun.ts.tests.jms.common.JmsUtil.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = MDBClient.class.getResource("mdb_msgTypesT3_ejb.xml");
            if(ejbResURL != null) {
              mdb_msgTypesT3_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = MDBClient.class.getResource("mdb_msgTypesT3_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_msgTypesT3_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_msgTypesT3_ejb, MDBClient.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_msgTypesT3_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_msgTypesT3.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_msgTypesT3_ear.addAsModule(mdb_msgTypesT3_ejb);
            mdb_msgTypesT3_ear.addAsModule(mdb_msgTypesT3_client);




        return mdb_msgTypesT3_ear;
        }

        @Test
        @Override
        public void mdbMsgClearBodyTopicTextTest() throws java.lang.Exception {
            super.mdbMsgClearBodyTopicTextTest();
        }

        @Test
        @Override
        public void mdbMsgClearBodyTopicObjectTest() throws java.lang.Exception {
            super.mdbMsgClearBodyTopicObjectTest();
        }

        @Test
        @Override
        public void mdbMsgClearBodyTopicMapTest() throws java.lang.Exception {
            super.mdbMsgClearBodyTopicMapTest();
        }

        @Test
        @Override
        public void mdbMsgClearBodyTopicBytesTest() throws java.lang.Exception {
            super.mdbMsgClearBodyTopicBytesTest();
        }

        @Test
        @Override
        public void mdbMsgClearBodyTopicStreamTest() throws java.lang.Exception {
            super.mdbMsgClearBodyTopicStreamTest();
        }

        @Test
        @Override
        public void mdbMsgResetTopicTest() throws java.lang.Exception {
            super.mdbMsgResetTopicTest();
        }

        @Test
        @Override
        public void mdbReadNullCharNotValidTopicMapTest() throws java.lang.Exception {
            super.mdbReadNullCharNotValidTopicMapTest();
        }

        @Test
        @Override
        public void mdbReadNullCharNotValidTopicStreamTest() throws java.lang.Exception {
            super.mdbReadNullCharNotValidTopicStreamTest();
        }


}