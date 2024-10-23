package com.sun.ts.tests.jms.ee.mdb.mdb_msgPropsQ;

import com.sun.ts.tests.jms.ee.mdb.mdb_msgPropsQ.MDBClient;
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
public class ClientTest extends com.sun.ts.tests.jms.ee.mdb.mdb_msgPropsQ.MDBClient {
    /**
        EE10 Deployment Descriptors:
        mdb_msgPropsQ: 
        mdb_msgPropsQ_client: jar.sun-application-client.xml
        mdb_msgPropsQ_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/ee/mdb/mdb_msgPropsQ/mdb_msgPropsQ_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/jms/ee/mdb/mdb_msgPropsQ/mdb_msgPropsQ_ejb.xml
        /com/sun/ts/tests/jms/ee/mdb/mdb_msgPropsQ/mdb_msgPropsQ_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_msgPropsQ", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_msgPropsQ_client = ShrinkWrap.create(JavaArchive.class, "mdb_msgPropsQ_client.jar");
            // The class files
            mdb_msgPropsQ_client.addClasses(
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.jms.ee.mdb.mdb_msgPropsQ.MDBClient.class,
            com.sun.ts.tests.jms.common.JmsUtil.class,
            com.sun.ts.tests.jms.commonee.MDB_Q_Test.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = MDBClient.class.getResource("mdb_msgPropsQ_client.xml");
            if(resURL != null) {
              mdb_msgPropsQ_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MDBClient.class.getResource("mdb_msgPropsQ_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_msgPropsQ_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdb_msgPropsQ_client.addAsManifestResource(new StringAsset("Main-Class: " + MDBClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_msgPropsQ_client, MDBClient.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_msgPropsQ_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_msgPropsQ_ejb.jar");
            // The class files
            mdb_msgPropsQ_ejb.addClasses(
                com.sun.ts.tests.jms.ee.mdb.mdb_msgPropsQ.MsgBeanMsgTestPropsQ.class,
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.jms.commonee.MDB_Q_TestEJB.class,
                com.sun.ts.tests.jms.commonee.MDB_Q_Test.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = MDBClient.class.getResource("mdb_msgPropsQ_ejb.xml");
            if(ejbResURL != null) {
              mdb_msgPropsQ_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = MDBClient.class.getResource("mdb_msgPropsQ_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_msgPropsQ_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_msgPropsQ_ejb, MDBClient.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_msgPropsQ_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_msgPropsQ.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_msgPropsQ_ear.addAsModule(mdb_msgPropsQ_ejb);
            mdb_msgPropsQ_ear.addAsModule(mdb_msgPropsQ_client);




        return mdb_msgPropsQ_ear;
        }

        @Test
        @Override
        public void mdbMsgPropertiesQTest() throws java.lang.Exception {
            super.mdbMsgPropertiesQTest();
        }

        @Test
        @Override
        public void mdbMsgPropertiesConversionQTest() throws java.lang.Exception {
            super.mdbMsgPropertiesConversionQTest();
        }


}