package com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ1;

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
public class ClientTest extends com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ1.MDBClient {
    /**
        EE10 Deployment Descriptors:
        mdb_msgTypesQ1: 
        mdb_msgTypesQ1_client: jar.sun-application-client.xml
        mdb_msgTypesQ1_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/ee/mdb/mdb_msgTypesQ1/mdb_msgTypesQ1_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/jms/ee/mdb/mdb_msgTypesQ1/mdb_msgTypesQ1_ejb.xml
        /com/sun/ts/tests/jms/ee/mdb/mdb_msgTypesQ1/mdb_msgTypesQ1_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_msgTypesQ1", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_msgTypesQ1_client = ShrinkWrap.create(JavaArchive.class, "mdb_msgTypesQ1_client.jar");
            // The class files
            mdb_msgTypesQ1_client.addClasses(
            Fault.class,
            com.sun.ts.tests.jms.common.JmsUtil.class,
            com.sun.ts.tests.jms.commonee.MDB_Q_Test.class,
            EETest.class,
            com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ1.MDBClient.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = MDBClient.class.getResource("mdb_msgTypesQ1_client.xml");
            if(resURL != null) {
              mdb_msgTypesQ1_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MDBClient.class.getResource("mdb_msgTypesQ1_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_msgTypesQ1_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdb_msgTypesQ1_client.addAsManifestResource(new StringAsset("Main-Class: " + MDBClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_msgTypesQ1_client, MDBClient.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_msgTypesQ1_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_msgTypesQ1_ejb.jar");
            // The class files
            mdb_msgTypesQ1_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.jms.commonee.MDB_Q_TestEJB.class,
                com.sun.ts.tests.jms.commonee.MDB_Q_Test.class,
                com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ1.MsgBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = MDBClient.class.getResource("mdb_msgTypesQ1_ejb.xml");
            if(ejbResURL != null) {
              mdb_msgTypesQ1_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = MDBClient.class.getResource("mdb_msgTypesQ1_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_msgTypesQ1_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_msgTypesQ1_ejb, MDBClient.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_msgTypesQ1_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_msgTypesQ1.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_msgTypesQ1_ear.addAsModule(mdb_msgTypesQ1_ejb);
            mdb_msgTypesQ1_ear.addAsModule(mdb_msgTypesQ1_client);




        return mdb_msgTypesQ1_ear;
        }

        @Test
        @Override
        public void mdbBytesMsgNullStreamQTest() throws java.lang.Exception {
            super.mdbBytesMsgNullStreamQTest();
        }

        @Test
        @Override
        public void mdbBytesMessageQTestsFullMsg() throws java.lang.Exception {
            super.mdbBytesMessageQTestsFullMsg();
        }

        @Test
        @Override
        public void mdbMapMessageFullMsgQTest() throws java.lang.Exception {
            super.mdbMapMessageFullMsgQTest();
        }

        @Test
        @Override
        public void mdbMapMessageConversionQTestsBoolean() throws java.lang.Exception {
            super.mdbMapMessageConversionQTestsBoolean();
        }

        @Test
        @Override
        public void mdbMapMessageConversionQTestsByte() throws java.lang.Exception {
            super.mdbMapMessageConversionQTestsByte();
        }

        @Test
        @Override
        public void mdbMapMessageConversionQTestsShort() throws java.lang.Exception {
            super.mdbMapMessageConversionQTestsShort();
        }

        @Test
        @Override
        public void mdbMapMessageConversionQTestsChar() throws java.lang.Exception {
            super.mdbMapMessageConversionQTestsChar();
        }

        @Test
        @Override
        public void mdbMapMessageConversionQTestsInt() throws java.lang.Exception {
            super.mdbMapMessageConversionQTestsInt();
        }

        @Test
        @Override
        public void mdbMapMessageConversionQTestsLong() throws java.lang.Exception {
            super.mdbMapMessageConversionQTestsLong();
        }

        @Test
        @Override
        public void mdbMapMessageConversionQTestsFloat() throws java.lang.Exception {
            super.mdbMapMessageConversionQTestsFloat();
        }

        @Test
        @Override
        public void mdbMapMessageConversionQTestsDouble() throws java.lang.Exception {
            super.mdbMapMessageConversionQTestsDouble();
        }

        @Test
        @Override
        public void mdbMapMessageConversionQTestsString() throws java.lang.Exception {
            super.mdbMapMessageConversionQTestsString();
        }

        @Test
        @Override
        public void mdbMapMessageConversionQTestsBytes() throws java.lang.Exception {
            super.mdbMapMessageConversionQTestsBytes();
        }

        @Test
        @Override
        public void mdbMapMessageConversionQTestsInvFormatString() throws java.lang.Exception {
            super.mdbMapMessageConversionQTestsInvFormatString();
        }


}