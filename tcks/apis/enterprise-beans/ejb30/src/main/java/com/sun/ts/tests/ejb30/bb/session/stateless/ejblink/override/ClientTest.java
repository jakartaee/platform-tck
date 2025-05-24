package com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.override;

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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.override.Client {
    /**
        EE10 Deployment Descriptors:
        ejb3_stateless_ejblink_override: 
        ejb3_stateless_ejblink_override_client: META-INF/application-client.xml,jar.sun-application-client.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/override/ejb3_stateless_ejblink_override_client.xml
        /com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/override/ejb3_stateless_ejblink_override_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/override/one_ejb.xml
        /com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/override/one_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_stateless_ejblink_override", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_stateless_ejblink_override_client = ShrinkWrap.create(JavaArchive.class, "ejb3_stateless_ejblink_override_client.jar");
            // The class files
            ejb3_stateless_ejblink_override_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            Fault.class,
            com.sun.ts.tests.ejb30.common.ejblink.EjbLinkIF.class,
            com.sun.ts.tests.ejb30.common.ejblink.Constants.class,
            com.sun.ts.tests.ejb30.common.ejblink.ClientBase.class,
            com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.override.Client.class,
            EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            SetupException.class,
            com.sun.ts.tests.ejb30.common.ejblink.CommonIF.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/override/ejb3_stateless_ejblink_override_client.xml");
            if(resURL != null) {
              ejb3_stateless_ejblink_override_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/override/ejb3_stateless_ejblink_override_client.jar.sun-application-client.xml");
            if(resURL != null) {
              ejb3_stateless_ejblink_override_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            ejb3_stateless_ejblink_override_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_stateless_ejblink_override_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive one_ejb = ShrinkWrap.create(JavaArchive.class, "one_ejb.jar");
            // The class files
            one_ejb.addClasses(
                    com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.override.EjbLink1Bean.class,
                    com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.override.UnusedEjbLink3Bean.class,
                    com.sun.ts.tests.ejb30.common.ejblink.Constants.class,
                    com.sun.ts.tests.ejb30.common.ejblink.EjbLinkBeanBase.class,
                    com.sun.ts.tests.ejb30.common.ejblink.EjbLinkIF.class,
                    com.sun.ts.tests.ejb30.common.ejblink.CommonIF.class,
                    com.sun.ts.tests.ejb30.common.ejblink.EjbLinkLocalIF.class,
                    com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                    com.sun.ts.tests.ejb30.common.helper.TestFailedException.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/override/one_ejb.xml");
            if(ejbResURL != null) {
              one_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/override/one_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              one_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(one_ejb, Client.class, ejbResURL);
            // two_ejb
            JavaArchive two_ejb = ShrinkWrap.create(JavaArchive.class, "two_ejb.jar");
            two_ejb.addClasses(
                    com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                    com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                    com.sun.ts.tests.ejb30.common.calc.BaseRemoteCalculator.class,
                    com.sun.ts.tests.ejb30.common.calc.CalculatorException.class,
                    com.sun.ts.tests.ejb30.common.calc.NoInterfaceRemoteCalculator.class,
                    com.sun.ts.tests.ejb30.common.calc.RemoteCalculator.class,
                    com.sun.ts.tests.ejb30.common.ejblink.CommonIF.class,
                    com.sun.ts.tests.ejb30.common.ejblink.Constants.class,
                    com.sun.ts.tests.ejb30.common.ejblink.EjbLinkBeanBase.class,
                    com.sun.ts.tests.ejb30.common.ejblink.EjbLinkIF.class,
                    com.sun.ts.tests.ejb30.common.ejblink.EjbLinkLocalIF.class,
                    com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.override.EjbLink2Bean.class,
                    com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.override.EjbLink2BeanBase.class,
                    com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.override.EjbLink3Bean.class
            );
            // The ejb-jar.xml descriptor
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/override/two_ejb.xml");
            if(ejbResURL != null) {
                two_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/override/two_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
                two_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(two_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_stateless_ejblink_override_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_stateless_ejblink_override.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            ejb3_stateless_ejblink_override_ear.addAsModule(two_ejb);
            ejb3_stateless_ejblink_override_ear.addAsModule(one_ejb);
            ejb3_stateless_ejblink_override_ear.addAsModule(ejb3_stateless_ejblink_override_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/override/application.xml");
            if(earResURL != null) {
              ejb3_stateless_ejblink_override_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/override/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_stateless_ejblink_override_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_stateless_ejblink_override_ear, Client.class, earResURL);
        return ejb3_stateless_ejblink_override_ear;
        }

        @Test
        @Override
        public void callBean1() throws java.lang.Exception {
            super.callBean1();
        }

        @Test
        @Override
        public void callBean2() throws java.lang.Exception {
            super.callBean2();
        }

        @Test
        @Override
        public void callBean1Bean2() throws java.lang.Exception {
            super.callBean1Bean2();
        }

        @Test
        @Override
        public void callBean2Bean1() throws java.lang.Exception {
            super.callBean2Bean1();
        }

        @Test
        @Override
        public void callBean2Bean3() throws java.lang.Exception {
            super.callBean2Bean3();
        }

        @Test
        @Override
        public void callBean2Bean1Local() throws java.lang.Exception {
            super.callBean2Bean1Local();
        }

        @Test
        @Override
        public void callInjectedBean1() throws java.lang.Exception {
            super.callInjectedBean1();
        }


}