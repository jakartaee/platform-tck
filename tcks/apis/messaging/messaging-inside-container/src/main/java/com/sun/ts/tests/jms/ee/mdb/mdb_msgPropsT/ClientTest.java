package com.sun.ts.tests.jms.ee.mdb.mdb_msgPropsT;

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
public class ClientTest extends com.sun.ts.tests.jms.ee.mdb.mdb_msgPropsT.MDBClient {
    /**
        EE10 Deployment Descriptors:
        mdb_msgPropsT: 
        mdb_msgPropsT_client: jar.sun-application-client.xml
        mdb_msgPropsT_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/ee/mdb/mdb_msgPropsT/mdb_msgPropsT_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/jms/ee/mdb/mdb_msgPropsT/mdb_msgPropsT_ejb.xml
        /com/sun/ts/tests/jms/ee/mdb/mdb_msgPropsT/mdb_msgPropsT_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_msgPropsT", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_msgPropsT_client = ShrinkWrap.create(JavaArchive.class, "mdb_msgPropsT_client.jar");
            // The class files
            mdb_msgPropsT_client.addClasses(
            com.sun.ts.tests.jms.ee.mdb.mdb_msgPropsT.MDBClient.class,
            com.sun.ts.tests.jms.commonee.MDB_T_Test.class,
            Fault.class,
            com.sun.ts.tests.jms.common.JmsUtil.class,
            EETest.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = MDBClient.class.getResource("mdb_msgPropsT_client.xml");
            if(resURL != null) {
              mdb_msgPropsT_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MDBClient.class.getResource("mdb_msgPropsT_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_msgPropsT_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdb_msgPropsT_client.addAsManifestResource(new StringAsset("Main-Class: " + MDBClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_msgPropsT_client, MDBClient.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_msgPropsT_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_msgPropsT_ejb.jar");
            // The class files
            mdb_msgPropsT_ejb.addClasses(
                com.sun.ts.tests.jms.commonee.MDB_T_TestEJB.class,
                com.sun.ts.tests.jms.commonee.MDB_T_Test.class,
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.jms.ee.mdb.mdb_msgPropsT.MsgBeanMsgTestPropsT.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = MDBClient.class.getResource("mdb_msgPropsT_ejb.xml");
            if(ejbResURL != null) {
              mdb_msgPropsT_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = MDBClient.class.getResource("mdb_msgPropsT_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_msgPropsT_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_msgPropsT_ejb, MDBClient.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_msgPropsT_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_msgPropsT.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_msgPropsT_ear.addAsModule(mdb_msgPropsT_ejb);
            mdb_msgPropsT_ear.addAsModule(mdb_msgPropsT_client);




        return mdb_msgPropsT_ear;
        }

        @Test
        @Override
        public void mdbMsgPropertiesTTest() throws java.lang.Exception {
            super.mdbMsgPropertiesTTest();
        }

        @Test
        @Override
        public void mdbMsgPropertiesConversionTTest() throws java.lang.Exception {
            super.mdbMsgPropertiesConversionTTest();
        }


}