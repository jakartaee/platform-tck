package com.sun.ts.tests.jms.ee.mdb.mdb_sndQ;

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
public class ClientTest extends com.sun.ts.tests.jms.ee.mdb.mdb_sndQ.MDBClient {
    /**
        EE10 Deployment Descriptors:
        mdb_sndQ: 
        mdb_sndQ_client: jar.sun-application-client.xml
        mdb_sndQ_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/ee/mdb/mdb_sndQ/mdb_sndQ_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/jms/ee/mdb/mdb_sndQ/mdb_sndQ_ejb.xml
        /com/sun/ts/tests/jms/ee/mdb/mdb_sndQ/mdb_sndQ_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_sndQ", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_sndQ_client = ShrinkWrap.create(JavaArchive.class, "mdb_sndQ_client.jar");
            // The class files
            mdb_sndQ_client.addClasses(
            Fault.class,
            com.sun.ts.tests.jms.common.JmsUtil.class,
            com.sun.ts.tests.jms.ee.mdb.mdb_sndQ.MDB_SNDQ_Test.class,
            com.sun.ts.tests.jms.ee.mdb.mdb_sndQ.MDBClient.class,
            EETest.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = MDBClient.class.getResource("mdb_sndQ_client.xml");
            if(resURL != null) {
              mdb_sndQ_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MDBClient.class.getResource("mdb_sndQ_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_sndQ_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdb_sndQ_client.addAsManifestResource(new StringAsset("Main-Class: " + MDBClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_sndQ_client, MDBClient.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_sndQ_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_sndQ_ejb.jar");
            // The class files
            mdb_sndQ_ejb.addClasses(
                com.sun.ts.tests.jms.ee.mdb.mdb_sndQ.MsgBean.class,
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.jms.ee.mdb.mdb_sndQ.MDB_SNDQ_Test.class,
                com.sun.ts.tests.jms.ee.mdb.mdb_sndQ.MDB_SNDQ_TestEJB.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = MDBClient.class.getResource("mdb_sndQ_ejb.xml");
            if(ejbResURL != null) {
              mdb_sndQ_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = MDBClient.class.getResource("mdb_sndQ_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_sndQ_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_sndQ_ejb, MDBClient.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_sndQ_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_sndQ.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_sndQ_ear.addAsModule(mdb_sndQ_ejb);
            mdb_sndQ_ear.addAsModule(mdb_sndQ_client);




        return mdb_sndQ_ear;
        }

        @Test
        @Override
        public void mdbSendTextMsgTest() throws java.lang.Exception {
            super.mdbSendTextMsgTest();
        }

        @Test
        @Override
        public void mdbSendBytesMsgTest() throws java.lang.Exception {
            super.mdbSendBytesMsgTest();
        }

        @Test
        @Override
        public void mdbSendMapMsgTest() throws java.lang.Exception {
            super.mdbSendMapMsgTest();
        }

        @Test
        @Override
        public void mdbSendStreamMsgTest() throws java.lang.Exception {
            super.mdbSendStreamMsgTest();
        }

        @Test
        @Override
        public void mdbSendObjectMsgTest() throws java.lang.Exception {
            super.mdbSendObjectMsgTest();
        }


}