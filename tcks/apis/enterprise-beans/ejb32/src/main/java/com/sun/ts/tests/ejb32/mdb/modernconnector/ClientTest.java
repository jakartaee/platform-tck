package com.sun.ts.tests.ejb32.mdb.modernconnector;

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
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.path.BasicPath;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;


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
            Fault.class,
            com.sun.ts.tests.ejb32.mdb.modernconnector.ejb.EventLoggerRemote.class,
            EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb32.mdb.modernconnector.Client.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = null;
            ejb32_mdb_modernconnector_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
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
            URL ejbResURL = null;
            // Call the archive processor
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
            JavaArchive ejb32_mdb_modernconnector_ra = ShrinkWrap.create(JavaArchive.class, "ejb32_mdb_modernconnector_ra.rar");
            ejb32_mdb_modernconnector_ra.add(ejb32_mdb_modernconnector_ra_jar, new BasicPath("/"), ZipExporter.class);
            // The ra-jar.xml descriptor
            URL raResURL = Client.class.getResource("/com/sun/ts/tests/ejb32/mdb/modernconnector/connector/META-INF/ra.xml");
            ejb32_mdb_modernconnector_ra.addAsManifestResource(raResURL, "ra.xml");
            // The sun-ra.xml file
            raResURL = null;
            // Call the archive processor
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
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb32_mdb_modernconnector_ear, Client.class, earResURL);
        return ejb32_mdb_modernconnector_ear;
        }

        @Test
        @Override
        public void testModernConnector() throws Fault {
            super.testModernConnector();
        }


}