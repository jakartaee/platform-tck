package com.sun.ts.tests.jms.ee20.cditests.mdb;

import com.sun.ts.tests.jms.ee20.cditests.mdb.Client;
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

import org.jboss.shrinkwrap.api.spec.WebArchive;
import com.sun.ts.tests.common.vehicle.none.proxy.AppClient;
import com.sun.ts.tests.common.vehicle.none.proxy.ServletNoVehicle;


@ExtendWith(ArquillianExtension.class)
@Tag("jms")
@Tag("platform")
@Tag("jms_web")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.jms.ee20.cditests.mdb.Client {
    /**
        EE10 Deployment Descriptors:
        cditestsmdb: META-INF/application.xml
        cditestsmdb_client: 
        cditestsmdb_ejb: META-INF/beans.xml,META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/jms/ee20/cditests/mdb/cditestsmdb_ejb.xml
        /com/sun/ts/tests/jms/ee20/cditests/mdb/cditestsmdb_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "cditestsmdb", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive cditestsmdb_client = ShrinkWrap.create(JavaArchive.class, "cditestsmdb_client.jar");
            // The class files
            cditestsmdb_client.addClasses(
            com.sun.ts.tests.jms.ee20.cditests.mdb.EjbClientIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.jms.ee20.cditests.mdb.Client.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The sun-application-client.xml descriptor
            URL resURL = Client.class.getResource("cditestsmdb_client.jar.sun-application-client.xml");
            if(resURL != null) {
              cditestsmdb_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            // resURL = Client.class.getResource("");
            // if(resURL != null) {
            //   cditestsmdb_client.addAsManifestResource(resURL, "sun-application-client.xml");
            // }
            cditestsmdb_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(cditestsmdb_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive cditestsmdb_ejb = ShrinkWrap.create(JavaArchive.class, "cditestsmdb_ejb.jar");
            // The class files
            cditestsmdb_ejb.addClasses(
                com.sun.ts.tests.jms.ee20.cditests.mdb.EjbClientIF.class,
                com.sun.ts.tests.jms.ee20.cditests.mdb.MsgBeanQ.class,
                com.sun.ts.tests.jms.ee20.cditests.mdb.EjbClient.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("cditestsmdb_ejb.xml");
            if(ejbResURL != null) {
              cditestsmdb_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("cditestsmdb_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              cditestsmdb_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/cditests/resources/beans.xml");
            if(ejbResURL != null) {
              cditestsmdb_ejb.addAsManifestResource(ejbResURL, "beans.xml");
            }

            // Call the archive processor
            archiveProcessor.processEjbArchive(cditestsmdb_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive cditestsmdb_ear = ShrinkWrap.create(EnterpriseArchive.class, "cditestsmdb.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            cditestsmdb_ear.addAsModule(cditestsmdb_ejb);
            cditestsmdb_ear.addAsModule(cditestsmdb_client);

            // The application.xml descriptor
            URL earResURL = null;
            earResURL = Client.class.getResource("application.xml");
            if(earResURL != null) {
              cditestsmdb_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // // The sun-application.xml descriptor
            // earResURL = Client.class.getResource("/./application.ear.sun-application.xml");
            // if(earResURL != null) {
            //   cditestsmdb_ear.addAsManifestResource(earResURL, "sun-application.xml");
            // }
            // Call the archive processor
            archiveProcessor.processEarArchive(cditestsmdb_ear, Client.class, earResURL);
        return cditestsmdb_ear;
        }

        @Test
        @Override
        public void testCDIInjectionOfMDBWithQueueReplyFromEjb() throws java.lang.Exception {
            super.testCDIInjectionOfMDBWithQueueReplyFromEjb();
        }

        @Test
        @Override
        public void testCDIInjectionOfMDBWithTopicReplyFromEjb() throws java.lang.Exception {
            super.testCDIInjectionOfMDBWithTopicReplyFromEjb();
        }


}