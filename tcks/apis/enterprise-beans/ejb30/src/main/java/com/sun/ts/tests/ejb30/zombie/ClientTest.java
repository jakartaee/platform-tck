package com.sun.ts.tests.ejb30.zombie;

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
@Tag("platform")
@Tag("ejb_mdb_optional")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.zombie.Client {
    /**
        EE10 Deployment Descriptors:
        mdb_zombie: 
        mdb_zombie_client: 
        mdb_zombie_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb30/zombie/mdb_zombie_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_zombie", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_zombie_client = ShrinkWrap.create(JavaArchive.class, "mdb_zombie_client.jar");
            // The class files
            mdb_zombie_client.addClasses(
            Fault.class,
            com.sun.ts.tests.ejb30.common.messaging.Constants.class,
            com.sun.ts.tests.ejb30.zombie.Client.class,
            EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/zombie/mdb_zombie_client.xml");
            if(resURL != null) {
              mdb_zombie_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/zombie/mdb_zombie_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_zombie_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdb_zombie_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_zombie_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_zombie_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_zombie_ejb.jar");
            // The class files
            mdb_zombie_ejb.addClasses(
                com.sun.ts.tests.ejb30.zombie.MessageBean.class,
                com.sun.ts.tests.ejb30.common.messaging.Constants.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/zombie/mdb_zombie_ejb.xml");
            if(ejbResURL != null) {
              mdb_zombie_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/zombie/mdb_zombie_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_zombie_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_zombie_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_zombie_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_zombie.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_zombie_ear.addAsModule(mdb_zombie_ejb);
            mdb_zombie_ear.addAsModule(mdb_zombie_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/zombie/application.xml");
            if(earResURL != null) {
              mdb_zombie_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/zombie/application.ear.sun-application.xml");
            if(earResURL != null) {
              mdb_zombie_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(mdb_zombie_ear, Client.class, earResURL);
        return mdb_zombie_ear;
        }

        @Test
        @Override
        public void test1() throws java.lang.Exception {
            super.test1();
        }


}