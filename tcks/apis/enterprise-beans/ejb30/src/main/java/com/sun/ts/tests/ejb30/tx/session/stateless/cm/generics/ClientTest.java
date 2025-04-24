package com.sun.ts.tests.ejb30.tx.session.stateless.cm.generics;

import com.sun.ts.tests.ejb30.tx.session.stateless.cm.generics.Client;
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
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.tx.session.stateless.cm.generics.Client {
    /**
        EE10 Deployment Descriptors:
        tx_stateless_generics: 
        tx_stateless_generics_ejb: jar.sun-ejb-jar.xml
        tx_stateless_generics_web: WEB-INF/web.xml

        Found Descriptors:
        Ejb:

        /com/sun/ts/tests/ejb30/tx/session/stateless/cm/generics/tx_stateless_generics_ejb.jar.sun-ejb-jar.xml
        War:

        /com/sun/ts/tests/ejb30/tx/session/stateless/cm/generics/tx_stateless_generics_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = "tx_stateless_generics", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive tx_stateless_generics_web = ShrinkWrap.create(WebArchive.class, "tx_stateless_generics_web.war");
            // The class files
            tx_stateless_generics_web.addClasses(
            com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet.class,
            com.sun.ts.tests.ejb30.tx.session.stateless.cm.generics.TestServlet.class,
            com.sun.ts.tests.servlet.common.util.Data.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateless/cm/generics/tx_stateless_generics_web.xml");
            tx_stateless_generics_web.addAsWebInfResource(warResURL, "web.xml");
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateless/cm/generics/tx_stateless_generics_web.war.sun-web.xml");
            if(warResURL != null) {
              tx_stateless_generics_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateless/cm/generics/tx_stateless_generics_web.xml");
            if(warResURL != null) {
              tx_stateless_generics_web.addAsWebResource(warResURL, "/tx_stateless_generics_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(tx_stateless_generics_web, Client.class, warResURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive tx_stateless_generics_ejb = ShrinkWrap.create(JavaArchive.class, "tx_stateless_generics_ejb.jar");
            // The class files
            tx_stateless_generics_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.generics.GreetingBeanBase.class,
                com.sun.ts.tests.ejb30.common.generics.DateGreetingBean.class,
                com.sun.ts.tests.ejb30.common.generics.GreetingBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateless/cm/generics/tx_stateless_generics_ejb.xml");
            if(ejbResURL != null) {
              tx_stateless_generics_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateless/cm/generics/tx_stateless_generics_ejb.jar.sun-ejb-jar.xml");
            tx_stateless_generics_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            // Call the archive processor
            archiveProcessor.processEjbArchive(tx_stateless_generics_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive tx_stateless_generics_ear = ShrinkWrap.create(EnterpriseArchive.class, "tx_stateless_generics.ear");

            // Any libraries added to the ear
                URL libURL;
                JavaArchive greeting_ejb_client_view_lib = ShrinkWrap.create(JavaArchive.class, "greeting-ejb-client-view.jar");
                    // The class files
                    greeting_ejb_client_view_lib.addClasses(
                        com.sun.ts.tests.ejb30.common.generics.LocalParameterizedIF.class,
                        com.sun.ts.tests.ejb30.common.generics.ParameterizedIF.class,
                        com.sun.ts.tests.ejb30.common.generics.RemoteParameterizedIF.class,
                        com.sun.ts.tests.ejb30.common.helper.Helper.class,
                        com.sun.ts.tests.ejb30.common.generics.RemoteIntGreetingIF.class,
                        com.sun.ts.tests.ejb30.common.generics.GenericGreetingIF.class,
                        com.sun.ts.tests.ejb30.common.generics.IntGreetingIF.class,
                        com.sun.ts.tests.ejb30.common.generics.LocalIntGreetingIF.class
                    );


                tx_stateless_generics_ear.addAsLibrary(greeting_ejb_client_view_lib);


            // The component jars built by the package target
            tx_stateless_generics_ear.addAsModule(tx_stateless_generics_ejb);
            tx_stateless_generics_ear.addAsModule(tx_stateless_generics_web);

            // Call the archive processor
            archiveProcessor.processEarArchive(tx_stateless_generics_ear, Client.class, null);
        return tx_stateless_generics_ear;
        }

        @Test
        @Override
        public void genericsTxMandatoryRemote() throws java.lang.Exception {
            super.genericsTxMandatoryRemote();
        }

        @Test
        @Override
        public void genericsTxMandatoryLocal() throws java.lang.Exception {
            super.genericsTxMandatoryLocal();
        }

        @Test
        @Override
        public void genericLocalBusinessInterfaceTxMandatory() throws java.lang.Exception {
            super.genericLocalBusinessInterfaceTxMandatory();
        }

        @Test
        @Override
        public void parameterizedParamLocalTxMandatory() throws java.lang.Exception {
            super.parameterizedParamLocalTxMandatory();
        }

        @Test
        @Override
        public void parameterizedReturnLocalTxMandatory() throws java.lang.Exception {
            super.parameterizedReturnLocalTxMandatory();
        }

        @Test
        @Override
        public void parameterizedParamRemoteTxMandatory() throws java.lang.Exception {
            super.parameterizedParamRemoteTxMandatory();
        }

        @Test
        @Override
        public void parameterizedReturnRemoteTxMandatory() throws java.lang.Exception {
            super.parameterizedReturnRemoteTxMandatory();
        }

        @Test
        @Override
        public void rolesAllowedLocalDateGreeting() throws java.lang.Exception {
            super.rolesAllowedLocalDateGreeting();
        }

        @Test
        @Override
        public void rolesAllowedRemoteIntGreet() throws java.lang.Exception {
            super.rolesAllowedRemoteIntGreet();
        }

        @Test
        @Override
        public void rolesAllowedLocalIntGreet() throws java.lang.Exception {
            super.rolesAllowedLocalIntGreet();
        }

        @Test
        @Override
        public void rolesAllowedNoArgLocalDateGreeting() throws java.lang.Exception {
            super.rolesAllowedNoArgLocalDateGreeting();
        }

        @Test
        @Override
        public void rolesAllowedNoArgRemoteIntGreet() throws java.lang.Exception {
            super.rolesAllowedNoArgRemoteIntGreet();
        }

        @Test
        @Override
        public void rolesAllowedNoArgLocalIntGreet() throws java.lang.Exception {
            super.rolesAllowedNoArgLocalIntGreet();
        }


}