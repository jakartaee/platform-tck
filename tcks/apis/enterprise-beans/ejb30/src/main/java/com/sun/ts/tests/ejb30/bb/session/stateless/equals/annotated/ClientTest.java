package com.sun.ts.tests.ejb30.bb.session.stateless.equals.annotated;

import com.sun.ts.tests.ejb30.bb.session.stateless.equals.annotated.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateless.equals.annotated.Client {
    /**
        EE10 Deployment Descriptors:
        stateless_equals_annotated: 
        stateless_equals_annotated_client: 
        stateless_equals_annotated_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateless/equals/annotated/stateless_equals_annotated_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "stateless_equals_annotated", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive stateless_equals_annotated_client = ShrinkWrap.create(JavaArchive.class, "stateless_equals_annotated_client.jar");
            // The class files
            stateless_equals_annotated_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.tests.ejb30.common.equals.TestIF.class,
            com.sun.ts.tests.ejb30.common.equals.ClientBase.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.common.equals.ShoppingCartIF.class,
            com.sun.ts.tests.ejb30.common.equals.CartIF.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.equals.Comparator.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb30.bb.session.stateless.equals.annotated.Client.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/equals/annotated/stateless_equals_annotated_client.xml");
            if(resURL != null) {
              stateless_equals_annotated_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/equals/annotated/stateless_equals_annotated_client.jar.sun-application-client.xml");
            if(resURL != null) {
              stateless_equals_annotated_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            stateless_equals_annotated_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(stateless_equals_annotated_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive stateless_equals_annotated_ejb = ShrinkWrap.create(JavaArchive.class, "stateless_equals_annotated_ejb.jar");
            // The class files
            stateless_equals_annotated_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.equals.annotated.CartBean.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.equals.annotated.ShoppingCartBean.class,
                com.sun.ts.tests.ejb30.common.equals.TestBean.class,
                com.sun.ts.tests.ejb30.common.equals.Comparator.class,
                com.sun.ts.tests.ejb30.common.equals.TestIF.class,
                com.sun.ts.tests.ejb30.common.equals.TestBeanBase.class,
                com.sun.ts.tests.ejb30.common.equals.LocalCartIF.class,
                com.sun.ts.tests.ejb30.common.equals.ShoppingCartIF.class,
                com.sun.ts.tests.ejb30.common.equals.CartIF.class,
                com.sun.ts.tests.ejb30.common.equals.LocalShoppingCartIF.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/equals/annotated/stateless_equals_annotated_ejb.xml");
            if(ejbResURL != null) {
              stateless_equals_annotated_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/equals/annotated/stateless_equals_annotated_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              stateless_equals_annotated_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(stateless_equals_annotated_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive stateless_equals_annotated_ear = ShrinkWrap.create(EnterpriseArchive.class, "stateless_equals_annotated.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            stateless_equals_annotated_ear.addAsModule(stateless_equals_annotated_ejb);
            stateless_equals_annotated_ear.addAsModule(stateless_equals_annotated_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/equals/annotated/application.xml");
            if(earResURL != null) {
              stateless_equals_annotated_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/equals/annotated/application.ear.sun-application.xml");
            if(earResURL != null) {
              stateless_equals_annotated_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(stateless_equals_annotated_ear, Client.class, earResURL);
        return stateless_equals_annotated_ear;
        }

        @Test
        @Override
        public void equalsNull() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.equalsNull();
        }

        @Test
        @Override
        public void selfEquals() throws java.lang.Exception {
            super.selfEquals();
        }

        @Test
        @Override
        public void selfEqualsLookup() throws java.lang.Exception {
            super.selfEqualsLookup();
        }

        @Test
        @Override
        public void otherEquals() throws java.lang.Exception {
            super.otherEquals();
        }

        @Test
        @Override
        public void otherEqualsLookup() throws java.lang.Exception {
            super.otherEqualsLookup();
        }

        @Test
        @Override
        public void differentInterfaceNotEqual() throws java.lang.Exception {
            super.differentInterfaceNotEqual();
        }

        @Test
        @Override
        public void differentInterfaceNotEqualLookup() throws java.lang.Exception {
            super.differentInterfaceNotEqualLookup();
        }

        @Test
        @Override
        public void testBeanselfEquals() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.testBeanselfEquals();
        }

        @Test
        @Override
        public void testBeanotherEquals() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.testBeanotherEquals();
        }

        @Test
        @Override
        public void testBeandifferentInterfaceNotEqual() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.testBeandifferentInterfaceNotEqual();
        }

        @Test
        @Override
        public void testBeanselfEqualsLookup() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.testBeanselfEqualsLookup();
        }

        @Test
        @Override
        public void testBeanotherEqualsLookup() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.testBeanotherEqualsLookup();
        }

        @Test
        @Override
        public void testBeandifferentInterfaceNotEqualLookup() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.testBeandifferentInterfaceNotEqualLookup();
        }


}