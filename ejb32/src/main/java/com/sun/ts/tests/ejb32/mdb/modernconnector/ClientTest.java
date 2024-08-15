package com.sun.ts.tests.ejb32.mdb.modernconnector;

import com.sun.ts.tests.ejb32.mdb.modernconnector.Client;
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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("ejb_mdb_optional")
@Tag("tck-appclient")

public class ClientTest extends com.sun.ts.tests.ejb32.mdb.modernconnector.Client {
    /**
        EE10 Deployment Descriptors:
        ejb32_mdb_modernconnector: 
        ejb32_mdb_modernconnector_client: 
        ejb32_mdb_modernconnector_ejb: 
        ejb32_mdb_modernconnector_ra: META-INF/ra.xml

        Found Descriptors:
        Client:

        Ejb:

        Rar:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb32_mdb_modernconnector", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb32_mdb_modernconnector_client = ShrinkWrap.create(JavaArchive.class, "ejb32_mdb_modernconnector_client.jar");
            // The class files
            ejb32_mdb_modernconnector_client.addClasses(
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb32.mdb.modernconnector.ejb.EventLoggerRemote.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb32.mdb.modernconnector.Client.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("com/sun/ts/tests/ejb32/mdb/modernconnector/ejb32_mdb_modernconnector_client.xml");
            if(resURL != null) {
              ejb32_mdb_modernconnector_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb32/mdb/modernconnector/ejb32_mdb_modernconnector_client.jar.sun-application-client.xml");
            if(resURL != null) {
              ejb32_mdb_modernconnector_client.addAsManifestResource(resURL, "application-client.xml");
            }
            ejb32_mdb_modernconnector_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            archiveProcessor.processClientArchive(ejb32_mdb_modernconnector_client, Client.class, resURL);


        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb32_mdb_modernconnector_ejb = ShrinkWrap.create(JavaArchive.class, "ejb32_mdb_modernconnector_ejb.jar");
            // The class files
            ejb32_mdb_modernconnector_ejb.addClasses(
                com.sun.ts.tests.ejb32.mdb.modernconnector.ejb.BatchEventMonitorBean.class,
                com.sun.ts.tests.ejb32.mdb.modernconnector.ejb.LoggerInterceptor.class,
                com.sun.ts.tests.ejb32.mdb.modernconnector.ejb.EventLoggerRemote.class,
                com.sun.ts.tests.ejb32.mdb.modernconnector.ejb.OnlineEventMonitorBean.class,
                com.sun.ts.tests.ejb32.mdb.modernconnector.ejb.EventLoggerBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb32/mdb/modernconnector/ejb32_mdb_modernconnector_ejb.xml");
            if(ejbResURL != null) {
              ejb32_mdb_modernconnector_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb32/mdb/modernconnector/ejb32_mdb_modernconnector_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb32_mdb_modernconnector_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            archiveProcessor.processEjbArchive(ejb32_mdb_modernconnector_ejb, Client.class, ejbResURL);

        // Rar
            // the jar with the correct archive name
            JavaArchive ejb32_mdb_modernconnector_ra_jar = ShrinkWrap.create(JavaArchive.class, "ejb32_mdb_modernconnector_ra.jar");
            // The class files
            ejb32_mdb_modernconnector_ra_jar.addClasses(
                 com.sun.ts.tests.ejb32.mdb.modernconnector.connector.EventMonitorConfig.class,
                 com.sun.ts.tests.ejb32.mdb.modernconnector.connector.EventMonitorAdapter.class,
                 com.sun.ts.tests.ejb32.mdb.modernconnector.connector.NoUseListener.class,
                 com.sun.ts.tests.ejb32.mdb.modernconnector.connector.EventMonitor.class
            );
            JavaArchive ejb32_mdb_modernconnector_ra = ShrinkWrap.create(JavaArchive.class, "ejb32_mdb_modernconnector.rar");
            ejb32_mdb_modernconnector_ra.add(ejb32_mdb_modernconnector_ra_jar, ejb32_mdb_modernconnector_ra_jar.getName(), ZipExporter.class);
            // The ra-jar.xml descriptor
            URL raResURL = Client.class.getResource("/com/sun/ts/tests/ejb32/mdb/modernconnector/connector/META-INF/ra.xml");
            if(raResURL != null) {
              ejb32_mdb_modernconnector_ra.addAsManifestResource(raResURL, "ra.xml");
            }
            // The sun-ra.xml file
            raResURL = Client.class.getResource("/com/sun/ts/tests/ejb32/mdb/modernconnector/connector/META-INF/ra.jar.sun-ra.xml");
            if(raResURL != null) {
              ejb32_mdb_modernconnector_ra.addAsManifestResource(raResURL, "sun-ra.xml");
            }
            archiveProcessor.processRarArchive(ejb32_mdb_modernconnector_ra, Client.class, raResURL);

        // Ear
            EnterpriseArchive ejb32_mdb_modernconnector_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb32_mdb_modernconnector.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            ejb32_mdb_modernconnector_ear.addAsModule(ejb32_mdb_modernconnector_ejb);
            ejb32_mdb_modernconnector_ear.addAsModule(ejb32_mdb_modernconnector_client);
            ejb32_mdb_modernconnector_ear.addAsModule(ejb32_mdb_modernconnector_ra);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb32/mdb/modernconnector/application.xml");
            if(earResURL != null) {
              ejb32_mdb_modernconnector_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb32/mdb/modernconnector/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb32_mdb_modernconnector_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            archiveProcessor.processEarArchive(ejb32_mdb_modernconnector_ear, Client.class, earResURL);
        return ejb32_mdb_modernconnector_ear;
        }

        @Test
        @Override
        public void testModernConnector() throws Fault {
            super.testModernConnector();
        }


}