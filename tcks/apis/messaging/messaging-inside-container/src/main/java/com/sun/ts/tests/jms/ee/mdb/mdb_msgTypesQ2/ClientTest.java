package com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ2;

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
public class ClientTest extends com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ2.MDBClient {
    /**
        EE10 Deployment Descriptors:
        mdb_msgTypesQ2: 
        mdb_msgTypesQ2_client: jar.sun-application-client.xml
        mdb_msgTypesQ2_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/ee/mdb/mdb_msgTypesQ2/mdb_msgTypesQ2_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/jms/ee/mdb/mdb_msgTypesQ2/mdb_msgTypesQ2_ejb.xml
        /com/sun/ts/tests/jms/ee/mdb/mdb_msgTypesQ2/mdb_msgTypesQ2_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_msgTypesQ2", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_msgTypesQ2_client = ShrinkWrap.create(JavaArchive.class, "mdb_msgTypesQ2_client.jar");
            // The class files
            mdb_msgTypesQ2_client.addClasses(
            Fault.class,
            com.sun.ts.tests.jms.common.JmsUtil.class,
            com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ2.MDBClient.class,
            com.sun.ts.tests.jms.commonee.MDB_Q_Test.class,
            EETest.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = MDBClient.class.getResource("mdb_msgTypesQ2_client.xml");
            if(resURL != null) {
              mdb_msgTypesQ2_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MDBClient.class.getResource("mdb_msgTypesQ2_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_msgTypesQ2_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdb_msgTypesQ2_client.addAsManifestResource(new StringAsset("Main-Class: " + MDBClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_msgTypesQ2_client, MDBClient.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_msgTypesQ2_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_msgTypesQ2_ejb.jar");
            // The class files
            mdb_msgTypesQ2_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.jms.commonee.MDB_Q_TestEJB.class,
                com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ2.MsgBeanMsgTestQ2.class,
                com.sun.ts.tests.jms.commonee.MDB_Q_Test.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = MDBClient.class.getResource("mdb_msgTypesQ2_ejb.xml");
            if(ejbResURL != null) {
              mdb_msgTypesQ2_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = MDBClient.class.getResource("mdb_msgTypesQ2_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_msgTypesQ2_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_msgTypesQ2_ejb, MDBClient.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_msgTypesQ2_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_msgTypesQ2.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_msgTypesQ2_ear.addAsModule(mdb_msgTypesQ2_ejb);
            mdb_msgTypesQ2_ear.addAsModule(mdb_msgTypesQ2_client);




        return mdb_msgTypesQ2_ear;
        }

        @Test
        @Override
        public void mdbMessageObjectCopyQTest() throws java.lang.Exception {
            super.mdbMessageObjectCopyQTest();
        }

        @Test
        @Override
        public void mdbStreamMessageConversionQTestsBoolean() throws java.lang.Exception {
            super.mdbStreamMessageConversionQTestsBoolean();
        }

        @Test
        @Override
        public void mdbStreamMessageConversionQTestsByte() throws java.lang.Exception {
            super.mdbStreamMessageConversionQTestsByte();
        }

        @Test
        @Override
        public void mdbStreamMessageConversionQTestsShort() throws java.lang.Exception {
            super.mdbStreamMessageConversionQTestsShort();
        }

        @Test
        @Override
        public void mdbStreamMessageConversionQTestsInt() throws java.lang.Exception {
            super.mdbStreamMessageConversionQTestsInt();
        }

        @Test
        @Override
        public void mdbStreamMessageConversionQTestsLong() throws java.lang.Exception {
            super.mdbStreamMessageConversionQTestsLong();
        }

        @Test
        @Override
        public void mdbStreamMessageConversionQTestsFloat() throws java.lang.Exception {
            super.mdbStreamMessageConversionQTestsFloat();
        }

        @Test
        @Override
        public void mdbStreamMessageConversionQTestsDouble() throws java.lang.Exception {
            super.mdbStreamMessageConversionQTestsDouble();
        }

        @Test
        @Override
        public void mdbStreamMessageConversionQTestsString() throws java.lang.Exception {
            super.mdbStreamMessageConversionQTestsString();
        }

        @Test
        @Override
        public void mdbStreamMessageConversionQTestsChar() throws java.lang.Exception {
            super.mdbStreamMessageConversionQTestsChar();
        }

        @Test
        @Override
        public void mdbStreamMessageConversionQTestsBytes() throws java.lang.Exception {
            super.mdbStreamMessageConversionQTestsBytes();
        }

        @Test
        @Override
        public void mdbStreamMessageConversionQTestsInvFormatString() throws java.lang.Exception {
            super.mdbStreamMessageConversionQTestsInvFormatString();
        }

        @Test
        @Override
        public void mdbStreamMessageQTestsFullMsg() throws java.lang.Exception {
            super.mdbStreamMessageQTestsFullMsg();
        }

        @Test
        @Override
        public void mdbStreamMessageQTestNull() throws java.lang.Exception {
            super.mdbStreamMessageQTestNull();
        }


}