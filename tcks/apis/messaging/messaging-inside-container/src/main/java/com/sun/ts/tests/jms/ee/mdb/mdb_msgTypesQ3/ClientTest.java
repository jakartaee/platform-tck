package com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ3;

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
public class ClientTest extends com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ3.MDBClient {
    /**
        EE10 Deployment Descriptors:
        mdb_msgTypesQ3: 
        mdb_msgTypesQ3_client: jar.sun-application-client.xml
        mdb_msgTypesQ3_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/ee/mdb/mdb_msgTypesQ3/mdb_msgTypesQ3_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/jms/ee/mdb/mdb_msgTypesQ3/mdb_msgTypesQ3_ejb.xml
        /com/sun/ts/tests/jms/ee/mdb/mdb_msgTypesQ3/mdb_msgTypesQ3_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_msgTypesQ3", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_msgTypesQ3_client = ShrinkWrap.create(JavaArchive.class, "mdb_msgTypesQ3_client.jar");
            // The class files
            mdb_msgTypesQ3_client.addClasses(
            Fault.class,
            com.sun.ts.tests.jms.common.JmsUtil.class,
            com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ3.MDBClient.class,
            com.sun.ts.tests.jms.commonee.MDB_Q_Test.class,
            EETest.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = MDBClient.class.getResource("mdb_msgTypesQ3_client.xml");
            if(resURL != null) {
              mdb_msgTypesQ3_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MDBClient.class.getResource("mdb_msgTypesQ3_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_msgTypesQ3_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdb_msgTypesQ3_client.addAsManifestResource(new StringAsset("Main-Class: " + MDBClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_msgTypesQ3_client, MDBClient.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_msgTypesQ3_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_msgTypesQ3_ejb.jar");
            // The class files
            mdb_msgTypesQ3_ejb.addClasses(
                com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ3.MsgBeanMsgTestQ3.class,
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.jms.commonee.MDB_Q_TestEJB.class,
                com.sun.ts.tests.jms.commonee.MDB_Q_Test.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = MDBClient.class.getResource("mdb_msgTypesQ3_ejb.xml");
            if(ejbResURL != null) {
              mdb_msgTypesQ3_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = MDBClient.class.getResource("mdb_msgTypesQ3_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_msgTypesQ3_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_msgTypesQ3_ejb, MDBClient.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_msgTypesQ3_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_msgTypesQ3.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_msgTypesQ3_ear.addAsModule(mdb_msgTypesQ3_ejb);
            mdb_msgTypesQ3_ear.addAsModule(mdb_msgTypesQ3_client);




        return mdb_msgTypesQ3_ear;
        }

        @Test
        @Override
        public void mdbMsgClearBodyQueueTextTest() throws java.lang.Exception {
            super.mdbMsgClearBodyQueueTextTest();
        }

        @Test
        @Override
        public void mdbMsgClearBodyQueueObjectTest() throws java.lang.Exception {
            super.mdbMsgClearBodyQueueObjectTest();
        }

        @Test
        @Override
        public void mdbMsgClearBodyQueueMapTest() throws java.lang.Exception {
            super.mdbMsgClearBodyQueueMapTest();
        }

        @Test
        @Override
        public void mdbMsgClearBodyQueueBytesTest() throws java.lang.Exception {
            super.mdbMsgClearBodyQueueBytesTest();
        }

        @Test
        @Override
        public void mdbMsgClearBodyQueueStreamTest() throws java.lang.Exception {
            super.mdbMsgClearBodyQueueStreamTest();
        }

        @Test
        @Override
        public void mdbMsgResetQueueTest() throws java.lang.Exception {
            super.mdbMsgResetQueueTest();
        }

        @Test
        @Override
        public void mdbReadNullCharNotValidQueueMapTest() throws java.lang.Exception {
            super.mdbReadNullCharNotValidQueueMapTest();
        }

        @Test
        @Override
        public void mdbReadNullCharNotValidQueueStreamTest() throws java.lang.Exception {
            super.mdbReadNullCharNotValidQueueStreamTest();
        }


}