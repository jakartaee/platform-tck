package com.sun.ts.tests.integration.sec.propagation;

import com.sun.ts.tests.integration.sec.propagation.Client;
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
@Tag("integration")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.integration.sec.propagation.Client {
    /**
        EE10 Deployment Descriptors:
        integration_sec_propagation: ear.sun-application.xml
        integration_sec_propagation_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        integration_sec_propagation_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Ejb:

        /com/sun/ts/tests/integration/sec/propagation/integration_sec_propagation_ejb.xml
        /com/sun/ts/tests/integration/sec/propagation/integration_sec_propagation_ejb.jar.sun-ejb-jar.xml
        War:

        /com/sun/ts/tests/integration/sec/propagation/integration_sec_propagation_web.xml
        /com/sun/ts/tests/integration/sec/propagation/integration_sec_propagation_web.war.sun-web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = "integration_sec_propagation", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive integration_sec_propagation_web = ShrinkWrap.create(WebArchive.class, "integration_sec_propagation_web.war");
            // The class files
            integration_sec_propagation_web.addClasses(
            com.sun.ts.tests.integration.sec.propagation.Bean1.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("integration_sec_propagation_web.xml");
            integration_sec_propagation_web.addAsWebInfResource(warResURL, "web.xml");
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("integration_sec_propagation_web.war.sun-web.xml");
            integration_sec_propagation_web.addAsWebInfResource(warResURL, "sun-web.xml");

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("ejb_to_ejb.jsp");
            integration_sec_propagation_web.addAsWebResource(warResURL, "/ejb_to_ejb.jsp");
            warResURL = Client.class.getResource("web_to_ejb_auth.jsp");
            integration_sec_propagation_web.addAsWebResource(warResURL, "/web_to_ejb_auth.jsp");
            warResURL = Client.class.getResource("web_to_ejb_noauth.jsp");
            integration_sec_propagation_web.addAsWebResource(warResURL, "/web_to_ejb_noauth.jsp");

           // Call the archive processor
           archiveProcessor.processWebArchive(integration_sec_propagation_web, Client.class, warResURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive integration_sec_propagation_ejb = ShrinkWrap.create(JavaArchive.class, "integration_sec_propagation_ejb.jar");
            // The class files
            integration_sec_propagation_ejb.addClasses(
                com.sun.ts.tests.integration.sec.propagation.Bean1.class,
                com.sun.ts.tests.integration.sec.propagation.Bean1EJB.class,
                com.sun.ts.tests.integration.sec.propagation.Bean2.class,
                com.sun.ts.tests.integration.sec.propagation.Bean2EJB.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client.class.getResource("integration_sec_propagation_ejb.xml");
            if(ejbResURL1 != null) {
              integration_sec_propagation_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client.class.getResource("integration_sec_propagation_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              integration_sec_propagation_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(integration_sec_propagation_ejb, Client.class, ejbResURL1);


        // Ear
            EnterpriseArchive integration_sec_propagation_ear = ShrinkWrap.create(EnterpriseArchive.class, "integration_sec_propagation.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            integration_sec_propagation_ear.addAsModule(integration_sec_propagation_ejb);
            integration_sec_propagation_ear.addAsModule(integration_sec_propagation_web);

            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(integration_sec_propagation_ear, Client.class, earResURL);
        return integration_sec_propagation_ear;
        }

        @Test
        @Override
        public void test_web_to_ejb_auth() throws java.lang.Exception {
            super.test_web_to_ejb_auth();
        }

        @Test
        @Override
        public void test_web_to_ejb_noauth() throws java.lang.Exception {
            super.test_web_to_ejb_noauth();
        }

        @Test
        @Override
        public void test_ejb_to_ejb() throws java.lang.Exception {
            super.test_ejb_to_ejb();
        }


}