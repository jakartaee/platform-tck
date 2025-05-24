package com.sun.ts.tests.ejb30.bb.session.stateless.callback.inheritance.descriptor;

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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateless.callback.inheritance.descriptor.Client {
    /**
        EE10 Deployment Descriptors:
        stateless_callback_inheritance_descriptor: 
        stateless_callback_inheritance_descriptor_client: 
        stateless_callback_inheritance_descriptor_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateless/callback/inheritance/descriptor/stateless_callback_inheritance_descriptor_ejb.xml
        /com/sun/ts/tests/ejb30/bb/session/stateless/callback/inheritance/descriptor/stateless_callback_inheritance_descriptor_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "stateless_callback_inheritance_descriptor", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive stateless_callback_inheritance_descriptor_client = ShrinkWrap.create(JavaArchive.class, "stateless_callback_inheritance_descriptor_client.jar");
            // The class files
            stateless_callback_inheritance_descriptor_client.addClasses(
            com.sun.ts.tests.ejb30.common.callback.CallbackIF.class,
            com.sun.ts.tests.ejb30.common.callback.ClientBase.class,
            Fault.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.bb.session.stateless.callback.inheritance.descriptor.Client.class,
            com.sun.ts.tests.ejb30.common.callback.Callback2IF.class,
            EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/callback/inheritance/descriptor/stateless_callback_inheritance_descriptor_client.xml");
            if(resURL != null) {
              stateless_callback_inheritance_descriptor_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/callback/inheritance/descriptor/stateless_callback_inheritance_descriptor_client.jar.sun-application-client.xml");
            if(resURL != null) {
              stateless_callback_inheritance_descriptor_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            stateless_callback_inheritance_descriptor_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(stateless_callback_inheritance_descriptor_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive stateless_callback_inheritance_descriptor_ejb = ShrinkWrap.create(JavaArchive.class, "stateless_callback_inheritance_descriptor_ejb.jar");
            // The class files
            stateless_callback_inheritance_descriptor_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.callback.InterceptorA.class,
                com.sun.ts.tests.ejb30.common.callback.InterceptorBaseBase.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.callback.inheritance.descriptor.Callback2BeanSuper.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.callback.inheritance.descriptor.CallbackBeanSuperSuper.class,
                com.sun.ts.tests.ejb30.common.callback.InterceptorH.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.callback.inheritance.descriptor.Callback2BeanSuperSuper.class,
                com.sun.ts.tests.ejb30.common.callback.CallbackIF.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.callback.inheritance.descriptor.CallbackBean.class,
                com.sun.ts.tests.ejb30.common.callback.InterceptorC.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.callback.inheritance.descriptor.Callback3BeanSuperSuper.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.callback.inheritance.descriptor.Callback2Bean.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.common.callback.InterceptorBase.class,
                com.sun.ts.tests.ejb30.common.callback.InterceptorF.class,
                com.sun.ts.tests.ejb30.common.callback.InterceptorG.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.callback.inheritance.descriptor.Callback4BeanSuper.class,
                com.sun.ts.tests.ejb30.common.callback.SharedCallbackBeanBase.class,
                com.sun.ts.tests.ejb30.common.callback.InterceptorI.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.callback.inheritance.descriptor.Callback4Bean.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.callback.inheritance.descriptor.Callback3BeanSuper.class,
                com.sun.ts.tests.ejb30.common.callback.Callback2BeanBase.class,
                com.sun.ts.tests.ejb30.common.callback.InterceptorJ.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.callback.inheritance.descriptor.Callback3Bean.class,
                com.sun.ts.tests.ejb30.common.callback.CallbackBeanBase.class,
                com.sun.ts.tests.ejb30.common.callback.InterceptorE.class,
                com.sun.ts.tests.ejb30.common.callback.Callback2IF.class,
                com.sun.ts.tests.ejb30.common.callback.InterceptorB.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.callback.inheritance.descriptor.CallbackBeanSuper.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/callback/inheritance/descriptor/stateless_callback_inheritance_descriptor_ejb.xml");
            if(ejbResURL != null) {
              stateless_callback_inheritance_descriptor_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/callback/inheritance/descriptor/stateless_callback_inheritance_descriptor_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              stateless_callback_inheritance_descriptor_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(stateless_callback_inheritance_descriptor_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive stateless_callback_inheritance_descriptor_ear = ShrinkWrap.create(EnterpriseArchive.class, "stateless_callback_inheritance_descriptor.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            stateless_callback_inheritance_descriptor_ear.addAsModule(stateless_callback_inheritance_descriptor_ejb);
            stateless_callback_inheritance_descriptor_ear.addAsModule(stateless_callback_inheritance_descriptor_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/callback/inheritance/descriptor/application.xml");
            if(earResURL != null) {
              stateless_callback_inheritance_descriptor_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/callback/inheritance/descriptor/application.ear.sun-application.xml");
            if(earResURL != null) {
              stateless_callback_inheritance_descriptor_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(stateless_callback_inheritance_descriptor_ear, Client.class, earResURL);
        return stateless_callback_inheritance_descriptor_ear;
        }

        @Test
        @Override
        public void inheritanceInterceptorsForCallbackBean3() throws java.lang.Exception {
            super.inheritanceInterceptorsForCallbackBean3();
        }

        @Test
        @Override
        public void inheritanceInterceptorsForCallbackBean1() throws java.lang.Exception {
            super.inheritanceInterceptorsForCallbackBean1();
        }

        @Test
        @Override
        public void inheritanceInterceptorsForCallbackBean2() throws java.lang.Exception {
            super.inheritanceInterceptorsForCallbackBean2();
        }

        @Test
        @Override
        public void inheritanceInterceptorsForCallbackBean4() throws java.lang.Exception {
            super.inheritanceInterceptorsForCallbackBean4();
        }


}