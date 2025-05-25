package com.sun.ts.tests.ejb30.bb.session.stateful.cm.allowed;

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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateful.cm.allowed.Client {
    /**
        EE10 Deployment Descriptors:
        ejb3_bb_stateful_cm_allowed: 
        ejb3_bb_stateful_cm_allowed_client: 
        ejb3_bb_stateful_cm_allowed_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateful/cm/allowed/ejb3_bb_stateful_cm_allowed_ejb.xml
        /com/sun/ts/tests/ejb30/bb/session/stateful/cm/allowed/ejb3_bb_stateful_cm_allowed_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_bb_stateful_cm_allowed", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateful_cm_allowed_client = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateful_cm_allowed_client.jar");
            // The class files
            ejb3_bb_stateful_cm_allowed_client.addClasses(
            com.sun.ts.tests.ejb30.common.allowed.NoTxAllowedIF.class,
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.tests.ejb30.common.allowed.CallbackAllowedIF.class,
            com.sun.ts.tests.ejb30.common.allowed.AllowedIF.class,
            Fault.class,
            com.sun.ts.tests.ejb30.common.allowed.ClientBase.class,
            com.sun.ts.tests.ejb30.common.allowed.SessionContextAllowedIF.class,
            com.sun.ts.tests.ejb30.common.allowed.Constants.class,
            EETest.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.cm.allowed.Client.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.cm.allowed.SetRollbackOnlyIF.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            // The sun-application-client.xml
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/cm/allowed/ejb3_bb_stateful_cm_allowed_client.sun-application-client.xml");
            ejb3_bb_stateful_cm_allowed_client.addAsManifestResource(resURL, "sun-application-client.xml");
            ejb3_bb_stateful_cm_allowed_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_bb_stateful_cm_allowed_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateful_cm_allowed_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateful_cm_allowed_ejb.jar");
            // The class files
            ejb3_bb_stateful_cm_allowed_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.allowed.NoTxAllowedIF.class,
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.common.allowed.CallbackAllowedIF.class,
                com.sun.ts.tests.ejb30.common.allowed.stateful.TimerLocalIF.class,
                com.sun.ts.tests.ejb30.common.allowed.AllowedIF.class,
                com.sun.ts.tests.ejb30.common.allowed.SessionContextAllowedBeanBase.class,
                com.sun.ts.tests.ejb30.common.allowed.Operations.class,
                com.sun.ts.tests.ejb30.common.allowed.stateful.TimerEJB.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.cm.allowed.InjectionAllowedBean.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.cm.allowed.NoTxAllowedBean.class,
                com.sun.ts.tests.ejb30.common.allowed.Constants.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.cm.allowed.AllowedBean.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.cm.allowed.CallbackAllowedBean.class,
                com.sun.ts.tests.ejb30.common.allowed.InjectiontAllowedBeanBase.class,
                com.sun.ts.tests.ejb30.common.allowed.CallbackAllowedBeanBase.class,
                com.sun.ts.tests.ejb30.common.allowed.CallbackAllowedLocalIF.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.cm.allowed.SetRollbackOnlyIF.class,
                com.sun.ts.tests.ejb30.common.allowed.stateful.StatefulOperations.class,
                com.sun.ts.tests.ejb30.common.allowed.MySessionSynchronization.class,
                com.sun.ts.tests.ejb30.common.allowed.SessionContextAllowedLocalIF.class,
                com.sun.ts.tests.ejb30.common.allowed.SessionContextAllowedIF.class,
                com.sun.ts.tests.ejb30.common.allowed.stateful.StatefulAllowedBeanBase.class,
                com.sun.ts.tests.ejb30.common.allowed.AllowedLocalIF.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.cm.allowed.SessionContextAllowedBean.class,
                com.sun.ts.tests.ejb30.common.allowed.stateful.StatefulCancelInterceptor.class,
                com.sun.ts.tests.ejb30.common.allowed.CancelInterceptor.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.cm.allowed.AllowedBeanSessionSynchronizationBase.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.cm.allowed.SetRollbackOnlyBean.class,
                com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                com.sun.ts.tests.ejb30.common.allowed.AllowedBeanBase.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/cm/allowed/ejb3_bb_stateful_cm_allowed_ejb.xml");
            if(ejbResURL != null) {
              ejb3_bb_stateful_cm_allowed_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/cm/allowed/ejb3_bb_stateful_cm_allowed_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb3_bb_stateful_cm_allowed_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb3_bb_stateful_cm_allowed_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_bb_stateful_cm_allowed_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_bb_stateful_cm_allowed.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            ejb3_bb_stateful_cm_allowed_ear.addAsModule(ejb3_bb_stateful_cm_allowed_ejb);
            ejb3_bb_stateful_cm_allowed_ear.addAsModule(ejb3_bb_stateful_cm_allowed_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/cm/allowed/application.xml");
            if(earResURL != null) {
              ejb3_bb_stateful_cm_allowed_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/cm/allowed/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_bb_stateful_cm_allowed_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_bb_stateful_cm_allowed_ear, Client.class, earResURL);
        return ejb3_bb_stateful_cm_allowed_ear;
        }

        @Test
        @Override
        public void postConstructTest() throws java.lang.Exception {
            super.postConstructTest();
        }

        @Test
        @Override
        public void setSessionContextTest() throws java.lang.Exception {
            super.setSessionContextTest();
        }

        @Test
        @Override
        public void businessTest() throws java.lang.Exception {
            super.businessTest();
        }

        @Test
        @Override
        public void preInvokeTest() throws java.lang.Exception {
            super.preInvokeTest();
        }

        @Test
        @Override
        public void postInvokeTest() throws java.lang.Exception {
            super.postInvokeTest();
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

        @Test
        @Override
        public void afterCompletionTest() throws java.lang.Exception {
            super.afterCompletionTest();
        }

        @Test
        @Override
        public void txNotSupportedTest() throws java.lang.Exception {
            super.txNotSupportedTest();
        }

        @Test
        @Override
        public void txSupportsTest() throws java.lang.Exception {
            super.txSupportsTest();
        }

        @Test
        @Override
        public void txNeverTest() throws java.lang.Exception {
            super.txNeverTest();
        }

        @Test
        @Override
        public void businessSetRollbackOnlyTest() throws java.lang.Exception {
            super.businessSetRollbackOnlyTest();
        }

        @Test
        @Override
        public void afterBeginSetRollbackOnlyTest() throws java.lang.Exception {
            super.afterBeginSetRollbackOnlyTest();
        }

        @Test
        @Override
        public void beforeCompletionSetRollbackOnlyTest() throws java.lang.Exception {
            super.beforeCompletionSetRollbackOnlyTest();
        }

        @Test
        @Override
        public void afterCompletionSetRollbackOnlyTest() throws java.lang.Exception {
            super.afterCompletionSetRollbackOnlyTest();
        }

        @Test
        @Override
        public void injectionMethod() throws java.lang.Exception {
            super.injectionMethod();
        }


}