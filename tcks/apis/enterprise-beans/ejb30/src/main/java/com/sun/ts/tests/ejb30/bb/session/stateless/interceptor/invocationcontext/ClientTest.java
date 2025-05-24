package com.sun.ts.tests.ejb30.bb.session.stateless.interceptor.invocationcontext;

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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateless.interceptor.invocationcontext.Client {
    /**
        EE10 Deployment Descriptors:
        ejb3_bb_stateless_interceptor_invocationcontext: 
        ejb3_bb_stateless_interceptor_invocationcontext_client: 
        ejb3_bb_stateless_interceptor_invocationcontext_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateless/interceptor/invocationcontext/ejb3_bb_stateless_interceptor_invocationcontext_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_bb_stateless_interceptor_invocationcontext", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateless_interceptor_invocationcontext_client = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateless_interceptor_invocationcontext_client.jar");
            // The class files
            ejb3_bb_stateless_interceptor_invocationcontext_client.addClasses(
            com.sun.ts.tests.ejb30.common.invocationcontext.ClientBase.class,
            Fault.class,
            EETest.class,
            SetupException.class,
            com.sun.ts.tests.ejb30.bb.session.stateless.interceptor.invocationcontext.Client.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/interceptor/invocationcontext/ejb3_bb_stateless_interceptor_invocationcontext_client.xml");
            if(resURL != null) {
              ejb3_bb_stateless_interceptor_invocationcontext_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/interceptor/invocationcontext/ejb3_bb_stateless_interceptor_invocationcontext_client.jar.sun-application-client.xml");
            if(resURL != null) {
              ejb3_bb_stateless_interceptor_invocationcontext_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            ejb3_bb_stateless_interceptor_invocationcontext_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_bb_stateless_interceptor_invocationcontext_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateless_interceptor_invocationcontext_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateless_interceptor_invocationcontext_ejb.jar");
            // The class files
            ejb3_bb_stateless_interceptor_invocationcontext_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.invocationcontext.InvocationContextBase.class,
                com.sun.ts.tests.ejb30.common.invocationcontext.InterceptorForAll.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.interceptor.invocationcontext.InvocationContextBean.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.interceptor.invocationcontext.InvocationContextInterceptorBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/interceptor/invocationcontext/ejb3_bb_stateless_interceptor_invocationcontext_ejb.xml");
            if(ejbResURL != null) {
              ejb3_bb_stateless_interceptor_invocationcontext_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/interceptor/invocationcontext/ejb3_bb_stateless_interceptor_invocationcontext_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb3_bb_stateless_interceptor_invocationcontext_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb3_bb_stateless_interceptor_invocationcontext_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_bb_stateless_interceptor_invocationcontext_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_bb_stateless_interceptor_invocationcontext.ear");

            // Any libraries added to the ear
                URL libURL;
                JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");
                    // The class files
                    shared_lib.addClasses(
                        com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                        com.sun.ts.tests.ejb30.common.invocationcontext.InvocationContextIF.class,
                        com.sun.ts.tests.ejb30.common.helper.Helper.class,
                        com.sun.ts.tests.ejb30.common.invocationcontext.InvocationContextTestImpl.class,
                        com.sun.ts.tests.ejb30.common.helper.TLogger.class
                    );


                ejb3_bb_stateless_interceptor_invocationcontext_ear.addAsLibrary(shared_lib);


            // The component jars built by the package target
            ejb3_bb_stateless_interceptor_invocationcontext_ear.addAsModule(ejb3_bb_stateless_interceptor_invocationcontext_ejb);
            ejb3_bb_stateless_interceptor_invocationcontext_ear.addAsModule(ejb3_bb_stateless_interceptor_invocationcontext_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/interceptor/invocationcontext/application.xml");
            if(earResURL != null) {
              ejb3_bb_stateless_interceptor_invocationcontext_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/interceptor/invocationcontext/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_bb_stateless_interceptor_invocationcontext_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_bb_stateless_interceptor_invocationcontext_ear, Client.class, earResURL);
        return ejb3_bb_stateless_interceptor_invocationcontext_ear;
        }

        @Test
        @Override
        public void setParametersIllegalArgumentException() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.setParametersIllegalArgumentException();
        }

        @Test
        @Override
        public void getTarget() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.getTarget();
        }

        @Test
        @Override
        public void getContextData() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.getContextData();
        }

        @Test
        @Override
        public void getSetParameters() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.getSetParameters();
        }

        @Test
        @Override
        public void proceedAgain() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.proceedAgain();
        }


}