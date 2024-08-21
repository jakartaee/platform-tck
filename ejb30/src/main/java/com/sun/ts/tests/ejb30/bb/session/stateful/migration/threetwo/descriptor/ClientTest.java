package com.sun.ts.tests.ejb30.bb.session.stateful.migration.threetwo.descriptor;

import com.sun.ts.tests.ejb30.bb.session.stateful.migration.threetwo.descriptor.Client;
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
@Tag("platform_optional")
@Tag("ejb_2x_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateful.migration.threetwo.descriptor.Client {
    /**
        EE10 Deployment Descriptors:
        stateful_migration_threetwo_descriptor: 
        stateful_migration_threetwo_descriptor_client: META-INF/application-client.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/descriptor/stateful_migration_threetwo_descriptor_client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/descriptor/one_ejb.xml
        /com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/descriptor/one_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "stateful_migration_threetwo_descriptor", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive stateful_migration_threetwo_descriptor_client = ShrinkWrap.create(JavaArchive.class, "stateful_migration_threetwo_descriptor_client.jar");
            // The class files
            stateful_migration_threetwo_descriptor_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.tests.ejb30.common.migration.threetwo.ClientBase.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.migration.threetwo.descriptor.Client.class,
            com.sun.ts.tests.ejb30.common.migration.threetwo.ThreeTestIF.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/descriptor/stateful_migration_threetwo_descriptor_client.xml");
            if(resURL != null) {
              stateful_migration_threetwo_descriptor_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/descriptor/stateful_migration_threetwo_descriptor_client.jar.sun-application-client.xml");
            if(resURL != null) {
              stateful_migration_threetwo_descriptor_client.addAsManifestResource(resURL, "application-client.xml");
            }
            stateful_migration_threetwo_descriptor_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(stateful_migration_threetwo_descriptor_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive one_ejb = ShrinkWrap.create(JavaArchive.class, "one_ejb.jar");
            // The class files
            one_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.common.migration.threetwo.MigrationBeanBase.class,
                com.sun.ts.tests.ejb30.common.migration.threetwo.MigrationBean.class,
                com.sun.ts.tests.ejb30.common.migration.threetwo.TwoLocalIF.class,
                com.sun.ts.tests.ejb30.common.migration.threetwo.TwoRemoteIF.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/descriptor/one_ejb.xml");
            if(ejbResURL != null) {
              one_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/descriptor/one_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              one_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(one_ejb, Client.class, ejbResURL);
            // two_ejb
            JavaArchive two_ejb = ShrinkWrap.create(JavaArchive.class, "two_ejb.jar");
            two_ejb.addClasses(
                    com.sun.ts.tests.ejb30.common.migration.threetwo.ThreeTestBeanBase.class,
                    com.sun.ts.tests.ejb30.common.migration.threetwo.DescriptorThreeTestBean.class,
                    com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                    com.sun.ts.tests.ejb30.common.migration.threetwo.ThreeTestIF.class,
                    com.sun.ts.tests.ejb30.common.migration.threetwo.TwoLocalHome.class,
                    com.sun.ts.tests.ejb30.common.migration.threetwo.TwoLocalIF.class,
                    com.sun.ts.tests.ejb30.common.migration.threetwo.TwoRemoteHome.class,
                    com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                    com.sun.ts.tests.ejb30.common.migration.threetwo.TwoRemoteIF.class
            );
            // The ejb-jar.xml descriptor
            ejbResURL = Client.class.getResource("two_ejb.xml");
            if(ejbResURL != null) {
                one_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("two_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
                one_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }

        // Ear
            EnterpriseArchive stateful_migration_threetwo_descriptor_ear = ShrinkWrap.create(EnterpriseArchive.class, "stateful_migration_threetwo_descriptor.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            stateful_migration_threetwo_descriptor_ear.addAsModule(two_ejb);
            stateful_migration_threetwo_descriptor_ear.addAsModule(one_ejb);
            stateful_migration_threetwo_descriptor_ear.addAsModule(stateful_migration_threetwo_descriptor_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/descriptor/application.xml");
            if(earResURL != null) {
              stateful_migration_threetwo_descriptor_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/migration/threetwo/descriptor/application.ear.sun-application.xml");
            if(earResURL != null) {
              stateful_migration_threetwo_descriptor_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(stateful_migration_threetwo_descriptor_ear, Client.class, earResURL);
        return stateful_migration_threetwo_descriptor_ear;
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