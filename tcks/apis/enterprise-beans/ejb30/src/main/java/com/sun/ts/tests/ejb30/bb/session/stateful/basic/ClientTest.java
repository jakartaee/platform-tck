package com.sun.ts.tests.ejb30.bb.session.stateful.basic;

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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateful.basic.Client {
    /**
        EE10 Deployment Descriptors:
        ejb3_bb_stateful_basic: 
        ejb3_bb_stateful_basic_client: META-INF/application-client.xml
        ejb3_bb_stateful_basic_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/bb/session/stateful/basic/ejb3_bb_stateful_basic_client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateful/basic/ejb3_bb_stateful_basic_ejb.xml
        /com/sun/ts/tests/ejb30/bb/session/stateful/basic/ejb3_bb_stateful_basic_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_bb_stateful_basic", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateful_basic_client = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateful_basic_client.jar");
            // The class files
            ejb3_bb_stateful_basic_client.addClasses(
            com.sun.ts.tests.ejb30.bb.session.stateful.basic.StatefulRemoteCalculator.class,
            Fault.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.basic.Client.class,
            com.sun.ts.tests.ejb30.common.calc.RemoteCalculator.class,
            EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.common.calc.CalculatorException.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/basic/ejb3_bb_stateful_basic_client.xml");
            ejb3_bb_stateful_basic_client.addAsManifestResource(resURL, "application-client.xml");
            // The sun-application-client.xml file
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/basic/ejb3_bb_stateful_basic_client.sun-application-client.xml");
            ejb3_bb_stateful_basic_client.addAsManifestResource(resURL, "sun-application-client.xml");
            ejb3_bb_stateful_basic_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_bb_stateful_basic_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateful_basic_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateful_basic_ejb.jar");
            // The class files
            ejb3_bb_stateful_basic_ejb.addClasses(
                com.sun.ts.tests.ejb30.bb.session.stateful.basic.StatefulRemoteCalculator.class,
                com.sun.ts.tests.ejb30.common.calc.NoInterfaceRemoteCalculator.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.basic.RemoteCalculatorBean.class,
                com.sun.ts.tests.ejb30.common.calc.BaseRemoteCalculator.class,
                com.sun.ts.tests.ejb30.common.calc.RemoteCalculator.class,
                com.sun.ts.tests.ejb30.common.calc.CalculatorException.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/basic/ejb3_bb_stateful_basic_ejb.xml");
            if(ejbResURL != null) {
              ejb3_bb_stateful_basic_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/basic/ejb3_bb_stateful_basic_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb3_bb_stateful_basic_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb3_bb_stateful_basic_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_bb_stateful_basic_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_bb_stateful_basic.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            ejb3_bb_stateful_basic_ear.addAsModule(ejb3_bb_stateful_basic_ejb);
            ejb3_bb_stateful_basic_ear.addAsModule(ejb3_bb_stateful_basic_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/basic/application.xml");
            if(earResURL != null) {
              ejb3_bb_stateful_basic_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/basic/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_bb_stateful_basic_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_bb_stateful_basic_ear, Client.class, earResURL);
        return ejb3_bb_stateful_basic_ear;
        }

        @Test
        @Override
        public void postConstructInvokedEvenNoResourceInjection() throws java.lang.Exception {
            super.postConstructInvokedEvenNoResourceInjection();
        }

        @Test
        @Override
        public void testOperations() throws java.lang.Exception {
            super.testOperations();
        }


}