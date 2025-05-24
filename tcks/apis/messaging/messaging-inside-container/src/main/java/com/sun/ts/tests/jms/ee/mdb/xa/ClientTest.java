package com.sun.ts.tests.jms.ee.mdb.xa;

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
public class ClientTest extends com.sun.ts.tests.jms.ee.mdb.xa.MDBClient {
    /**
        EE10 Deployment Descriptors:
        mdb_msg_xa: 
        mdb_msg_xa_client: META-INF/application-client.xml,jar.sun-application-client.xml
        mdb_msg_xa_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/ee/mdb/xa/mdb_msg_xa_client.xml
        /com/sun/ts/tests/jms/ee/mdb/xa/mdb_msg_xa_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/jms/ee/mdb/xa/mdb_msg_xa_ejb.xml
        /com/sun/ts/tests/jms/ee/mdb/xa/mdb_msg_xa_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_msg_xa", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_msg_xa_client = ShrinkWrap.create(JavaArchive.class, "mdb_msg_xa_client.jar");
            // The class files
            mdb_msg_xa_client.addClasses(
            com.sun.ts.tests.jms.commonee.Client.class,
            Fault.class,
            com.sun.ts.tests.jms.ee.mdb.xa.MDBClient.class,
            com.sun.ts.tests.jms.common.JmsUtil.class,
            EETest.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = MDBClient.class.getResource("mdb_msg_xa_client.xml");
            if(resURL != null) {
              mdb_msg_xa_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MDBClient.class.getResource("mdb_msg_xa_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_msg_xa_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdb_msg_xa_client.addAsManifestResource(new StringAsset("Main-Class: " + MDBClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_msg_xa_client, MDBClient.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_msg_xa_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_msg_xa_ejb.jar");
            // The class files
            mdb_msg_xa_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.jms.ee.mdb.xa.MsgBeanxa.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = MDBClient.class.getResource("mdb_msg_xa_ejb.xml");
            if(ejbResURL != null) {
              mdb_msg_xa_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = MDBClient.class.getResource("mdb_msg_xa_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_msg_xa_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_msg_xa_ejb, MDBClient.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_msg_xa_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_msg_xa.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_msg_xa_ear.addAsModule(mdb_msg_xa_ejb);
            mdb_msg_xa_ear.addAsModule(mdb_msg_xa_client);




        return mdb_msg_xa_ear;
        }

        @Test
        @Override
        public void Test1() throws java.lang.Exception {
            super.Test1();
        }

        @Test
        @Override
        public void Test2() throws java.lang.Exception {
            super.Test2();
        }

        @Test
        @Override
        public void Test3() throws java.lang.Exception {
            super.Test3();
        }

        @Test
        @Override
        public void Test4() throws java.lang.Exception {
            super.Test4();
        }

        @Test
        @Override
        public void Test5() throws java.lang.Exception {
            super.Test5();
        }

        @Test
        @Override
        public void Test6() throws java.lang.Exception {
            super.Test6();
        }

        @Test
        @Override
        public void Test7() throws java.lang.Exception {
            super.Test7();
        }

        @Test
        @Override
        public void Test8() throws java.lang.Exception {
            super.Test8();
        }

        @Test
        @Override
        public void Test9() throws java.lang.Exception {
            super.Test9();
        }

        @Test
        @Override
        public void Test10() throws java.lang.Exception {
            super.Test10();
        }


}