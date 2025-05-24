package com.sun.ts.tests.ejb30.bb.session.stateful.equals.annotated;

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
@Tag("ejb_3x_remote_optional")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateful.equals.annotated.Client {
    /**
        EE10 Deployment Descriptors:
        stateful_equals_annotated: 
        stateful_equals_annotated_client: 
        stateful_equals_annotated_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateful/equals/annotated/stateful_equals_annotated_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "stateful_equals_annotated", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive stateful_equals_annotated_client = ShrinkWrap.create(JavaArchive.class, "stateful_equals_annotated_client.jar");
            // The class files
            stateful_equals_annotated_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.tests.ejb30.common.equals.TestIF.class,
            com.sun.ts.tests.ejb30.common.equals.ClientBase.class,
            Fault.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.equals.annotated.Client.class,
            com.sun.ts.tests.ejb30.common.equals.ShoppingCartIF.class,
            com.sun.ts.tests.ejb30.common.equals.CartIF.class,
            EETest.class,
            com.sun.ts.tests.ejb30.common.equals.Comparator.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/equals/annotated/stateful_equals_annotated_client.jar.sun-application-client.xml");
            stateful_equals_annotated_client.addAsManifestResource(resURL, "sun-application-client.xml");
            stateful_equals_annotated_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(stateful_equals_annotated_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive stateful_equals_annotated_ejb = ShrinkWrap.create(JavaArchive.class, "stateful_equals_annotated_ejb.jar");
            // The class files
            stateful_equals_annotated_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.common.equals.TestBeanForStateful.class,
                com.sun.ts.tests.ejb30.common.equals.Comparator.class,
                com.sun.ts.tests.ejb30.common.equals.TestIF.class,
                com.sun.ts.tests.ejb30.common.equals.TestBeanBase.class,
                com.sun.ts.tests.ejb30.common.equals.LocalCartIF.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.equals.annotated.CartBean.class,
                com.sun.ts.tests.ejb30.common.equals.ShoppingCartIF.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.equals.annotated.ShoppingCartBean.class,
                com.sun.ts.tests.ejb30.common.equals.CartIF.class,
                com.sun.ts.tests.ejb30.common.equals.LocalShoppingCartIF.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class
            );
            // The ejb-jar.xml descriptor
            // The sun-ejb-jar.xml file
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/equals/annotated/stateful_equals_annotated_ejb.jar.sun-ejb-jar.xml");
            stateful_equals_annotated_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            // Call the archive processor
            archiveProcessor.processEjbArchive(stateful_equals_annotated_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive stateful_equals_annotated_ear = ShrinkWrap.create(EnterpriseArchive.class, "stateful_equals_annotated.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            stateful_equals_annotated_ear.addAsModule(stateful_equals_annotated_ejb);
            stateful_equals_annotated_ear.addAsModule(stateful_equals_annotated_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/equals/annotated/application.xml");
            if(earResURL != null) {
              stateful_equals_annotated_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/equals/annotated/application.ear.sun-application.xml");
            if(earResURL != null) {
              stateful_equals_annotated_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(stateful_equals_annotated_ear, Client.class, earResURL);
        return stateful_equals_annotated_ear;
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
        public void otherNotEquals() throws java.lang.Exception {
            super.otherNotEquals();
        }

        @Test
        @Override
        public void otherNotEqualsLookup() throws java.lang.Exception {
            super.otherNotEqualsLookup();
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
        public void testBeanotherNotEquals() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.testBeanotherNotEquals();
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
        public void testBeanotherNotEqualsLookup() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.testBeanotherNotEqualsLookup();
        }

        @Test
        @Override
        public void testBeandifferentInterfaceNotEqualLookup() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.testBeandifferentInterfaceNotEqualLookup();
        }


}