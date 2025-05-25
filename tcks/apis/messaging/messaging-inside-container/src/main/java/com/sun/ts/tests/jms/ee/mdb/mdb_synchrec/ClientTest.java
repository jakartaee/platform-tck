package com.sun.ts.tests.jms.ee.mdb.mdb_synchrec;

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
public class ClientTest extends com.sun.ts.tests.jms.ee.mdb.mdb_synchrec.MDBClient {
    /**
        EE10 Deployment Descriptors:
        mdb_synchrec: 
        mdb_synchrec_client: META-INF/application-client.xml,jar.sun-application-client.xml
        mdb_synchrec_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/ee/mdb/mdb_synchrec/mdb_synchrec_client.xml
        /com/sun/ts/tests/jms/ee/mdb/mdb_synchrec/mdb_synchrec_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/jms/ee/mdb/mdb_synchrec/mdb_synchrec_ejb.xml
        /com/sun/ts/tests/jms/ee/mdb/mdb_synchrec/mdb_synchrec_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_synchrec", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_synchrec_client = ShrinkWrap.create(JavaArchive.class, "mdb_synchrec_client.jar");
            // The class files
            mdb_synchrec_client.addClasses(
            Fault.class,
            com.sun.ts.tests.jms.common.JmsUtil.class,
            com.sun.ts.tests.jms.ee.mdb.mdb_synchrec.MDBClient.class,
            EETest.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = MDBClient.class.getResource("mdb_synchrec_client.xml");
            if(resURL != null) {
              mdb_synchrec_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MDBClient.class.getResource("mdb_synchrec_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_synchrec_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdb_synchrec_client.addAsManifestResource(new StringAsset("Main-Class: " + MDBClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_synchrec_client, MDBClient.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_synchrec_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_synchrec_ejb.jar");
            // The class files
            mdb_synchrec_ejb.addClasses(
                com.sun.ts.tests.jms.ee.mdb.mdb_synchrec.MsgBean.class,
                com.sun.ts.tests.jms.common.JmsUtil.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = MDBClient.class.getResource("mdb_synchrec_ejb.xml");
            if(ejbResURL != null) {
              mdb_synchrec_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = MDBClient.class.getResource("mdb_synchrec_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_synchrec_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_synchrec_ejb, MDBClient.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_synchrec_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_synchrec.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_synchrec_ear.addAsModule(mdb_synchrec_ejb);
            mdb_synchrec_ear.addAsModule(mdb_synchrec_client);




        return mdb_synchrec_ear;
        }

        @Test
        @Override
        public void test1() throws java.lang.Exception {
            super.test1();
        }


}