package com.sun.ts.tests.ejb30.bb.session.stateful.interceptor.listener.override;

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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateful.interceptor.listener.override.Client {
    /**
        EE10 Deployment Descriptors:
        ejb3_bb_stateful_interceptor_listener_override: 
        ejb3_bb_stateful_interceptor_listener_override_client: 
        ejb3_bb_stateful_interceptor_listener_override_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateful/interceptor/listener/override/ejb3_bb_stateful_interceptor_listener_override_ejb.xml
        /com/sun/ts/tests/ejb30/bb/session/stateful/interceptor/listener/override/ejb3_bb_stateful_interceptor_listener_override_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_bb_stateful_interceptor_listener_override", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateful_interceptor_listener_override_client = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateful_interceptor_listener_override_client.jar");
            // The class files
            ejb3_bb_stateful_interceptor_listener_override_client.addClasses(
            com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeIF.class,
            Fault.class,
            com.sun.ts.tests.ejb30.common.interceptor.Constants.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.interceptor.listener.override.Client.class,
            EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.common.calc.CalculatorException.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            com.sun.ts.tests.ejb30.common.interceptor.ClientBase.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/interceptor/listener/override/ejb3_bb_stateful_interceptor_listener_override_client.xml");
            if(resURL != null) {
              ejb3_bb_stateful_interceptor_listener_override_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/interceptor/listener/override/ejb3_bb_stateful_interceptor_listener_override_client.jar.sun-application-client.xml");
            if(resURL != null) {
              ejb3_bb_stateful_interceptor_listener_override_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            ejb3_bb_stateful_interceptor_listener_override_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_bb_stateful_interceptor_listener_override_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateful_interceptor_listener_override_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateful_interceptor_listener_override_ejb.jar");
            // The class files
            ejb3_bb_stateful_interceptor_listener_override_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.interceptor.Interceptor2.class,
                com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeIF.class,
                com.sun.ts.tests.ejb30.common.interceptor.Constants.class,
                com.sun.ts.tests.ejb30.common.messaging.Constants.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.interceptor.listener.override.AroundInvokeBean.class,
                com.sun.ts.tests.ejb30.common.calc.CalculatorException.class,
                com.sun.ts.tests.ejb30.common.interceptor.Interceptor1.class,
                com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeBase.class,
                com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeTestImpl.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/interceptor/listener/override/ejb3_bb_stateful_interceptor_listener_override_ejb.xml");
            if(ejbResURL != null) {
              ejb3_bb_stateful_interceptor_listener_override_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/interceptor/listener/override/ejb3_bb_stateful_interceptor_listener_override_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb3_bb_stateful_interceptor_listener_override_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb3_bb_stateful_interceptor_listener_override_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_bb_stateful_interceptor_listener_override_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_bb_stateful_interceptor_listener_override.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            ejb3_bb_stateful_interceptor_listener_override_ear.addAsModule(ejb3_bb_stateful_interceptor_listener_override_ejb);
            ejb3_bb_stateful_interceptor_listener_override_ear.addAsModule(ejb3_bb_stateful_interceptor_listener_override_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/interceptor/listener/override/application.xml");
            if(earResURL != null) {
              ejb3_bb_stateful_interceptor_listener_override_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/interceptor/listener/override/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_bb_stateful_interceptor_listener_override_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_bb_stateful_interceptor_listener_override_ear, Client.class, earResURL);
        return ejb3_bb_stateful_interceptor_listener_override_ear;
        }

        @Test
        @Override
        public void getBeanTest() throws java.lang.Exception {
            super.getBeanTest();
        }

        @Test
        @Override
        public void getParametersTest() throws java.lang.Exception {
            super.getParametersTest();
        }

        @Test
        @Override
        public void getParametersEmptyTest() throws java.lang.Exception {
            super.getParametersEmptyTest();
        }

        @Test
        @Override
        public void txRollbackOnlyTest() throws java.lang.Exception {
            super.txRollbackOnlyTest();
        }

        @Test
        @Override
        public void txRollbackOnlyAfterTest() throws java.lang.Exception {
            super.txRollbackOnlyAfterTest();
        }

        @Test
        @Override
        public void runtimeExceptionTest() throws java.lang.Exception {
            super.runtimeExceptionTest();
        }

        @Test
        @Override
        public void runtimeExceptionAfterTest() throws java.lang.Exception {
            super.runtimeExceptionAfterTest();
        }

        @Test
        @Override
        public void setParametersTest() throws java.lang.Exception {
            super.setParametersTest();
        }

        @Test
        @Override
        public void getContextDataTest() throws java.lang.Exception {
            super.getContextDataTest();
        }

        @Test
        @Override
        public void getMethodTest() throws java.lang.Exception {
            super.getMethodTest();
        }

        @Test
        @Override
        public void exceptionTest() throws java.lang.Exception {
            super.exceptionTest();
        }

        @Test
        @Override
        public void suppressExceptionTest() throws java.lang.Exception {
            super.suppressExceptionTest();
        }

        @Test
        @Override
        public void orderTest() throws java.lang.Exception {
            super.orderTest();
        }

        @Test
        @Override
        public void sameInvocationContextTest() throws java.lang.Exception {
            super.sameInvocationContextTest();
        }

        @Test
        @Override
        public void afterBeginTest() throws java.lang.Exception {
            super.afterBeginTest();
        }

        @Test
        @Override
        public void beforeCompletionTest() throws java.lang.Exception {
            super.beforeCompletionTest();
        }


}