package com.sun.ts.tests.ejb30.bb.session.stateless.callback.threelevels.descriptor;

import com.sun.ts.tests.ejb30.bb.session.stateless.callback.threelevels.descriptor.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateless.callback.threelevels.descriptor.Client {
    /**
        EE10 Deployment Descriptors:
        ejb3_bb_stateless_callback_threelevels_descriptor: 
        ejb3_bb_stateless_callback_threelevels_descriptor_client: 

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateless/callback/threelevels/descriptor/one_ejb.xml
        /com/sun/ts/tests/ejb30/bb/session/stateless/callback/threelevels/descriptor/one_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_bb_stateless_callback_threelevels_descriptor", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateless_callback_threelevels_descriptor_client = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateless_callback_threelevels_descriptor_client.jar");
            // The class files
            ejb3_bb_stateless_callback_threelevels_descriptor_client.addClasses(
            com.sun.ts.tests.ejb30.common.callback.CallbackIF.class,
            com.sun.ts.tests.ejb30.bb.session.stateless.callback.threelevels.descriptor.Client.class,
            com.sun.ts.tests.ejb30.common.callback.ClientBase.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.callback.Callback2IF.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            // The sun-application-client.xml
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/callback/threelevels/descriptor/client.sun-application-client.xml");
            ejb3_bb_stateless_callback_threelevels_descriptor_client.addAsManifestResource(resURL, "sun-application-client.xml");
            ejb3_bb_stateless_callback_threelevels_descriptor_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_bb_stateless_callback_threelevels_descriptor_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive one_ejb = ShrinkWrap.create(JavaArchive.class, "one_ejb.jar");
            // The class files
            one_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.callback.InterceptorA.class,
                com.sun.ts.tests.ejb30.common.callback.InterceptorBaseBase.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.callback.listener.descriptor.CallbackBean.class,
                com.sun.ts.tests.ejb30.common.callback.SharedCallbackBeanBase.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.callback.listener.descriptor.NoAnnotationCallback2Bean.class,
                com.sun.ts.tests.ejb30.common.callback.CallbackIF.class,
                com.sun.ts.tests.ejb30.common.callback.InterceptorD.class,
                com.sun.ts.tests.ejb30.common.callback.Callback2BeanBase.class,
                com.sun.ts.tests.ejb30.common.callback.CallbackBeanBase.class,
                com.sun.ts.tests.ejb30.common.callback.InterceptorC.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.callback.threelevels.descriptor.Callback3Bean.class,
                com.sun.ts.tests.ejb30.common.callback.Callback2IF.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.callback.listener.descriptor.Callback2Bean.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.common.callback.InterceptorB.class,
                com.sun.ts.tests.ejb30.common.callback.InterceptorBase.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/callback/threelevels/descriptor/one_ejb.xml");
            if(ejbResURL != null) {
              one_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/callback/threelevels/descriptor/one_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              one_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(one_ejb, Client.class, ejbResURL);
            // two_ejb
            JavaArchive two_ejb = ShrinkWrap.create(JavaArchive.class, "two_ejb.jar");
            two_ejb.addClasses(
                    com.sun.ts.tests.ejb30.bb.session.stateless.callback.listener.descriptor.NoAnnotationCallback2Bean.class,
                    com.sun.ts.tests.ejb30.common.callback.Callback2BeanBase.class,
                    com.sun.ts.tests.ejb30.common.callback.Callback2IF.class,
                    com.sun.ts.tests.ejb30.common.callback.CallbackBeanBase.class,
                    com.sun.ts.tests.ejb30.common.callback.SharedCallbackBeanBase.class,
                    com.sun.ts.tests.ejb30.common.callback.CallbackIF.class,
                    com.sun.ts.tests.ejb30.common.callback.InterceptorBase.class,
                    com.sun.ts.tests.ejb30.common.callback.InterceptorBaseBase.class,
                    com.sun.ts.tests.ejb30.common.callback.InterceptorA.class,
                    com.sun.ts.tests.ejb30.common.callback.InterceptorB.class,
                    com.sun.ts.tests.ejb30.common.callback.InterceptorC.class,
                    com.sun.ts.tests.ejb30.common.callback.InterceptorD.class,
                    com.sun.ts.tests.ejb30.common.helper.TLogger.class
            );
            // The ejb-jar.xml descriptor
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/callback/threelevels/descriptor/two_ejb.xml");
            if(ejbResURL != null) {
                two_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/callback/threelevels/descriptor/two_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
                two_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(two_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_bb_stateless_callback_threelevels_descriptor_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_bb_stateless_callback_threelevels_descriptor.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            ejb3_bb_stateless_callback_threelevels_descriptor_ear.addAsModule(two_ejb);
            ejb3_bb_stateless_callback_threelevels_descriptor_ear.addAsModule(one_ejb);
            ejb3_bb_stateless_callback_threelevels_descriptor_ear.addAsModule(ejb3_bb_stateless_callback_threelevels_descriptor_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/callback/threelevels/descriptor/application.xml");
            if(earResURL != null) {
              ejb3_bb_stateless_callback_threelevels_descriptor_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/callback/threelevels/descriptor/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_bb_stateless_callback_threelevels_descriptor_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_bb_stateless_callback_threelevels_descriptor_ear, Client.class, earResURL);
        return ejb3_bb_stateless_callback_threelevels_descriptor_ear;
        }

        @Test
        @Override
        public void threeLevelInterceptorssForCallbackBean1() throws java.lang.Exception {
            super.threeLevelInterceptorssForCallbackBean1();
        }

        @Test
        @Override
        public void threeLevelInterceptorssForCallbackBean2() throws java.lang.Exception {
            super.threeLevelInterceptorssForCallbackBean2();
        }

        @Test
        @Override
        public void threeLevelInterceptorssForCallbackBean3() throws java.lang.Exception {
            super.threeLevelInterceptorssForCallbackBean3();
        }

        @Test
        @Override
        public void threeLevelInterceptorssForCallbackBean4() throws java.lang.Exception {
            super.threeLevelInterceptorssForCallbackBean4();
        }

        @Test
        @Override
        public void threeLevelSingleDefaultInterceptorsJar() throws java.lang.Exception {
            super.threeLevelSingleDefaultInterceptorsJar();
        }

        @Test
        @Override
        public void threeLevelInvocationContextDataForCallbackBean1() throws java.lang.Exception {
            super.threeLevelInvocationContextDataForCallbackBean1();
        }

        @Test
        @Override
        public void threeLevelInvocationContextDataForCallbackBean2() throws java.lang.Exception {
            super.threeLevelInvocationContextDataForCallbackBean2();
        }

        @Test
        @Override
        public void threeLevelInvocationContextDataForCallbackBean3() throws java.lang.Exception {
            super.threeLevelInvocationContextDataForCallbackBean3();
        }

        @Test
        @Override
        public void threeLevelInvocationContextDataForCallbackBean4() throws java.lang.Exception {
            super.threeLevelInvocationContextDataForCallbackBean4();
        }

        @Test
        @Override
        public void threeLevelSingleDefaultInvocationContextJar() throws java.lang.Exception {
            super.threeLevelSingleDefaultInvocationContextJar();
        }


}