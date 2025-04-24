package com.sun.ts.tests.ejb30.bb.session.stateless.annotation.enventry;

import com.sun.ts.tests.ejb30.bb.session.stateless.annotation.enventry.Client;
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
@Tag("platform")
@Tag("ejb_3x_remote_optional")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateless.annotation.enventry.Client {
    /**
        EE10 Deployment Descriptors:
        ejb3_bb_stateless_enventry: 
        ejb3_bb_stateless_enventry_client: 
        ejb3_bb_stateless_enventry_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        ejb3_bb_stateless_enventrydefault: 
        ejb3_bb_stateless_enventrydefault_client: 
        ejb3_bb_stateless_enventrydefault_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        ejb3_bb_stateless_enventrynoat: 
        ejb3_bb_stateless_enventrynoat_client: 
        ejb3_bb_stateless_enventrynoat_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateless/annotation/enventry/ejb3_bb_stateless_enventry_ejb.xml
        /com/sun/ts/tests/ejb30/bb/session/stateless/annotation/enventry/ejb3_bb_stateless_enventry_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_bb_stateless_enventry", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateless_enventry_client = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateless_enventry_client.jar");
            // The class files
            ejb3_bb_stateless_enventry_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb30.bb.session.stateless.annotation.enventry.Client.class,
            com.sun.ts.tests.ejb30.common.annotation.enventry.ClientBase.class,
            com.sun.ts.tests.ejb30.common.annotation.enventry.EnvEntryIF.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/enventry/ejb3_bb_stateless_enventry_client.xml");
            if(resURL != null) {
              ejb3_bb_stateless_enventry_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/enventry/ejb3_bb_stateless_enventry_client.jar.sun-application-client.xml");
            if(resURL != null) {
              ejb3_bb_stateless_enventry_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            ejb3_bb_stateless_enventry_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_bb_stateless_enventry_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateless_enventry_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateless_enventry_ejb.jar");
            // The class files
            ejb3_bb_stateless_enventry_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.annotation.enventry.EnvEntrySetterBean.class,
                com.sun.ts.tests.ejb30.common.annotation.enventry.Constants.class,
                com.sun.ts.tests.ejb30.common.annotation.enventry.EnvEntryBeanBase.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.annotation.enventry.EnvEntryTypeBean.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                com.sun.ts.tests.ejb30.common.annotation.enventry.EnvEntryIF.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.annotation.enventry.EnvEntryFieldBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/enventry/ejb3_bb_stateless_enventry_ejb.xml");
            if(ejbResURL != null) {
              ejb3_bb_stateless_enventry_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/enventry/ejb3_bb_stateless_enventry_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb3_bb_stateless_enventry_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb3_bb_stateless_enventry_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_bb_stateless_enventry_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_bb_stateless_enventry.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            ejb3_bb_stateless_enventry_ear.addAsModule(ejb3_bb_stateless_enventry_ejb);
            ejb3_bb_stateless_enventry_ear.addAsModule(ejb3_bb_stateless_enventry_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/enventry/application.xml");
            if(earResURL != null) {
              ejb3_bb_stateless_enventry_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/enventry/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_bb_stateless_enventry_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_bb_stateless_enventry_ear, Client.class, earResURL);
        return ejb3_bb_stateless_enventry_ear;
        }

        @Test
        @Override
        public void stringTest() throws java.lang.Exception {
            super.stringTest();
        }

        @Test
        @Override
        public void charTest() throws java.lang.Exception {
            super.charTest();
        }

        @Test
        @Override
        public void intTest() throws java.lang.Exception {
            super.intTest();
        }

        @Test
        @Override
        public void booleanTest() throws java.lang.Exception {
            super.booleanTest();
        }

        @Test
        @Override
        public void doubleTest() throws java.lang.Exception {
            super.doubleTest();
        }

        @Test
        @Override
        public void byteTest() throws java.lang.Exception {
            super.byteTest();
        }

        @Test
        @Override
        public void shortTest() throws java.lang.Exception {
            super.shortTest();
        }

        @Test
        @Override
        public void longTest() throws java.lang.Exception {
            super.longTest();
        }

        @Test
        @Override
        public void floatTest() throws java.lang.Exception {
            super.floatTest();
        }

        @Test
        @Override
        public void stringDeepTest() throws java.lang.Exception {
            super.stringDeepTest();
        }

        @Test
        @Override
        public void charDeepTest() throws java.lang.Exception {
            super.charDeepTest();
        }

        @Test
        @Override
        public void intDeepTest() throws java.lang.Exception {
            super.intDeepTest();
        }

        @Test
        @Override
        public void booleanDeepTest() throws java.lang.Exception {
            super.booleanDeepTest();
        }

        @Test
        @Override
        public void doubleDeepTest() throws java.lang.Exception {
            super.doubleDeepTest();
        }

        @Test
        @Override
        public void byteDeepTest() throws java.lang.Exception {
            super.byteDeepTest();
        }

        @Test
        @Override
        public void shortDeepTest() throws java.lang.Exception {
            super.shortDeepTest();
        }

        @Test
        @Override
        public void longDeepTest() throws java.lang.Exception {
            super.longDeepTest();
        }

        @Test
        @Override
        public void floatDeepTest() throws java.lang.Exception {
            super.floatDeepTest();
        }


}