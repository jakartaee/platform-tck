package com.sun.ts.tests.ejb30.bb.session.stateless.migration.twothree.descriptor;

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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;


@ExtendWith(ArquillianExtension.class)
@Tag("platform_optional")
@Tag("ejb_2x_optional")
@Tag("tck-appclient")

@Disabled("https://github.com/jakartaee/platform-tck/issues/2273")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateless.migration.twothree.descriptor.Client {
    /**
        EE10 Deployment Descriptors:
        stateless_migration_twothree_descriptor: 
        stateless_migration_twothree_descriptor_client: META-INF/application-client.xml
        stateless_migration_twothree_descriptor_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/bb/session/stateless/migration/twothree/descriptor/stateless_migration_twothree_descriptor_client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateless/migration/twothree/descriptor/stateless_migration_twothree_descriptor_ejb.xml
        /com/sun/ts/tests/ejb30/bb/session/stateless/migration/twothree/descriptor/stateless_migration_twothree_descriptor_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "stateless_migration_twothree_descriptor", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive stateless_migration_twothree_descriptor_client = ShrinkWrap.create(JavaArchive.class, "stateless_migration_twothree_descriptor_client.jar");
            // The class files
            stateless_migration_twothree_descriptor_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.tests.ejb30.common.migration.twothree.ThreeTestIF.class,
            Fault.class,
            com.sun.ts.tests.ejb30.common.migration.twothree.TwoTestRemoteIF.class,
            com.sun.ts.tests.ejb30.bb.session.stateless.migration.twothree.descriptor.Client.class,
            com.sun.ts.tests.ejb30.common.migration.twothree.ClientBase.class,
            EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/migration/twothree/descriptor/stateless_migration_twothree_descriptor_client.xml");
            if(resURL != null) {
              stateless_migration_twothree_descriptor_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/migration/twothree/descriptor/stateless_migration_twothree_descriptor_client.jar.sun-application-client.xml");
            if(resURL != null) {
              stateless_migration_twothree_descriptor_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            stateless_migration_twothree_descriptor_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(stateless_migration_twothree_descriptor_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive stateless_migration_twothree_descriptor_ejb = ShrinkWrap.create(JavaArchive.class, "stateless_migration_twothree_descriptor_ejb.jar");
            // The class files
            stateless_migration_twothree_descriptor_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.common.migration.twothree.ThreeTestIF.class,
                com.sun.ts.tests.ejb30.common.migration.twothree.TwoLocalIF.class,
                com.sun.ts.tests.ejb30.common.migration.twothree.TwoTestBean.class,
                com.sun.ts.tests.ejb30.common.migration.twothree.MigrationBeanBase.class,
                com.sun.ts.tests.ejb30.common.migration.twothree.TwoTestRemoteIF.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.migration.twothree.descriptor.MigrationBean.class,
                com.sun.ts.tests.ejb30.common.migration.twothree.ThreeIF.class,
                com.sun.ts.tests.ejb30.common.migration.twothree.ThreeLocalIF.class,
                com.sun.ts.tests.ejb30.common.migration.twothree.TwoRemoteIF.class,
                com.sun.ts.tests.ejb30.common.migration.twothree.ThreeTestBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/migration/twothree/descriptor/stateless_migration_twothree_descriptor_ejb.xml");
            if(ejbResURL != null) {
              stateless_migration_twothree_descriptor_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/migration/twothree/descriptor/stateless_migration_twothree_descriptor_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              stateless_migration_twothree_descriptor_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(stateless_migration_twothree_descriptor_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive stateless_migration_twothree_descriptor_ear = ShrinkWrap.create(EnterpriseArchive.class, "stateless_migration_twothree_descriptor.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            stateless_migration_twothree_descriptor_ear.addAsModule(stateless_migration_twothree_descriptor_ejb);
            stateless_migration_twothree_descriptor_ear.addAsModule(stateless_migration_twothree_descriptor_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/migration/twothree/descriptor/application.xml");
            if(earResURL != null) {
              stateless_migration_twothree_descriptor_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/migration/twothree/descriptor/application.ear.sun-application.xml");
            if(earResURL != null) {
              stateless_migration_twothree_descriptor_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(stateless_migration_twothree_descriptor_ear, Client.class, earResURL);
        return stateless_migration_twothree_descriptor_ear;
        }

        @Test
        @Override
        public void callRemoteTest() throws java.lang.Exception {
            super.callRemoteTest();
        }

        @Test
        @Override
        public void callLocalTest() throws java.lang.Exception {
            super.callLocalTest();
        }

        @Test
        @Override
        public void callRemoteSameTxContextTest() throws java.lang.Exception {
            super.callRemoteSameTxContextTest();
        }

        @Test
        @Override
        public void callLocalSameTxContextTest() throws java.lang.Exception {
            super.callLocalSameTxContextTest();
        }

        @Test
        @Override
        public void callThreeRemoteTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.callThreeRemoteTest();
        }

        @Test
        @Override
        public void callThreeLocalTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.callThreeLocalTest();
        }


}