package com.sun.ts.tests.ejb30.bb.session.stateful.migration.threetwo.annotated;

import java.net.URL;
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
@Tag("platform_optional")
@Tag("ejb_2x_optional")
@Tag("tck-appclient")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateful.migration.threetwo.annotated.Client {
    /**
        EE10 Deployment Descriptors:
        stateful_migration_threetwo_annotated: 
        stateful_migration_threetwo_annotated_client: 

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/annotated/one_ejb.xml
        /com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/annotated/one_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "stateful_migration_threetwo_annotated", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive stateful_migration_threetwo_annotated_client = ShrinkWrap.create(JavaArchive.class, "stateful_migration_threetwo_annotated_client.jar");
            // The class files
            stateful_migration_threetwo_annotated_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.migration.threetwo.annotated.Client.class,
            com.sun.ts.tests.ejb30.common.migration.threetwo.ClientBase.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.common.migration.threetwo.ThreeTestIF.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/annotated/stateful_migration_threetwo_annotated_client.xml");
            if(resURL != null) {
              stateful_migration_threetwo_annotated_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/annotated/stateful_migration_threetwo_annotated_client.jar.sun-application-client.xml");
            if(resURL != null) {
              stateful_migration_threetwo_annotated_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            stateful_migration_threetwo_annotated_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(stateful_migration_threetwo_annotated_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive one_ejb = ShrinkWrap.create(JavaArchive.class, "one_ejb.jar");
            // The class files
            one_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.common.migration.threetwo.MigrationBeanBase.class,
                com.sun.ts.tests.ejb30.common.migration.threetwo.MigrationBean.class,
                    com.sun.ts.tests.ejb30.common.migration.threetwo.TwoLocalHome.class,
                    com.sun.ts.tests.ejb30.common.migration.threetwo.TwoLocalIF.class,
                    com.sun.ts.tests.ejb30.common.migration.threetwo.TwoRemoteIF.class,
                    com.sun.ts.tests.ejb30.common.migration.threetwo.TwoRemoteHome.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/annotated/one_ejb.xml");
            if(ejbResURL != null) {
              one_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/annotated/one_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              one_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(one_ejb, Client.class, ejbResURL);
            // two_ejb
            JavaArchive two_ejb = ShrinkWrap.create(JavaArchive.class, "/com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/annotated/two_ejb.jar");
            two_ejb.addClasses(
                    com.sun.ts.tests.ejb30.common.migration.threetwo.StatefulThreeTestBean.class,
                    com.sun.ts.tests.ejb30.common.migration.threetwo.ThreeTestBeanBase.class,
                    com.sun.ts.tests.ejb30.common.migration.threetwo.ThreeTestIF.class,
                    com.sun.ts.tests.ejb30.common.migration.threetwo.TwoLocalHome.class,
                    com.sun.ts.tests.ejb30.common.migration.threetwo.TwoLocalIF.class,
                    com.sun.ts.tests.ejb30.common.migration.threetwo.TwoRemoteHome.class,
                    com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                    com.sun.ts.tests.ejb30.common.migration.threetwo.TwoRemoteIF.class
            );
            // The ejb-jar.xml descriptor
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/annotated/two_ejb.xml");
            if(ejbResURL != null) {
                two_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/annotated/two_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
                two_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(two_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive stateful_migration_threetwo_annotated_ear = ShrinkWrap.create(EnterpriseArchive.class, "stateful_migration_threetwo_annotated.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            stateful_migration_threetwo_annotated_ear.addAsModule(two_ejb);
            stateful_migration_threetwo_annotated_ear.addAsModule(one_ejb);
            stateful_migration_threetwo_annotated_ear.addAsModule(stateful_migration_threetwo_annotated_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/annotated/application.xml");
            if(earResURL != null) {
              stateful_migration_threetwo_annotated_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/annotated/application.ear.sun-application.xml");
            if(earResURL != null) {
              stateful_migration_threetwo_annotated_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(stateful_migration_threetwo_annotated_ear, Client.class, earResURL);
        return stateful_migration_threetwo_annotated_ear;
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


}