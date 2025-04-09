package com.sun.ts.tests.jms.ee20.cditests.ejbweb;

import com.sun.ts.tests.jms.ee20.cditests.ejbweb.Client;
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
@Tag("jms")
@Tag("platform")
@Tag("jms_web")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.jms.ee20.cditests.ejbweb.Client {
    /**
        EE10 Deployment Descriptors:
        cditestsejbweb: META-INF/application.xml
        cditestsejbweb_client: 
        cditestsejbweb_ejb: META-INF/beans.xml,META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        cditestsejbweb_web: WEB-INF/beans.xml,WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/jms/ee20/cditests/ejbweb/cditestsejbweb_ejb.xml
        /com/sun/ts/tests/jms/ee20/cditests/ejbweb/cditestsejbweb_ejb.jar.sun-ejb-jar.xml
        War:

        /com/sun/ts/tests/jms/ee20/cditests/ejbweb/cditestsejbweb_web.xml
        /com/sun/ts/tests/jms/ee20/cditests/ejbweb/cditestsejbweb_web.war.sun-web.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "cditestsejbweb", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive cditestsejbweb_web = ShrinkWrap.create(WebArchive.class, "cditestsejbweb_web.war");
            // The class files
            cditestsejbweb_web.addClasses(
            com.sun.ts.tests.jms.ee20.cditests.ejbweb.ServletClient.class,
            com.sun.ts.tests.jms.ee20.cditests.ejbweb.MyManagedBean.class
            );

            // The web.xml descriptor
            URL warResURL = Client.class.getResource("cditestsejbweb_web.xml");
            if(warResURL != null) {
              cditestsejbweb_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("cditestsejbweb_web.war.sun-web.xml");
            if(warResURL != null) {
              cditestsejbweb_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            // warResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/cditests/ejbweb/cditestsejbweb_web.xml");
            // if(warResURL != null) {
            //   cditestsejbweb_web.addAsWebResource(warResURL, "/cditestsejbweb_web.xml");
            // }
            warResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/cditests/resources/beans.xml");
            if(warResURL != null) {
              cditestsejbweb_web.addAsWebInfResource(warResURL, "beans.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(cditestsejbweb_web, Client.class, warResURL);

        // Client
            // the jar with the correct archive name
            JavaArchive cditestsejbweb_client = ShrinkWrap.create(JavaArchive.class, "cditestsejbweb_client.jar");
            // The class files
            cditestsejbweb_client.addClasses(
            com.sun.ts.tests.jms.ee20.cditests.ejbweb.EjbClientIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.jms.ee20.cditests.ejbweb.Client.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            // URL resURL = null;
            // URL resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml");
            // if(resURL != null) {
            //   cditestsejbweb_client.addAsManifestResource(resURL, "application-client.xml");
            // }
            // // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            // resURL = Client.class.getResource("jar.sun-application-client.xml");
            // if(resURL != null) {
            //   cditestsejbweb_client.addAsManifestResource(resURL, "application-client.xml");
            // }
            URL resURL = null;
            cditestsejbweb_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");

            // Call the archive processor
            archiveProcessor.processClientArchive(cditestsejbweb_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive cditestsejbweb_ejb = ShrinkWrap.create(JavaArchive.class, "cditestsejbweb_ejb.jar");
            // The class files
            cditestsejbweb_ejb.addClasses(
                com.sun.ts.tests.jms.ee20.cditests.ejbweb.EjbClientIF.class,
                com.sun.ts.tests.jms.ee20.cditests.ejbweb.EjbClient.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("cditestsejbweb_ejb.xml");
            if(ejbResURL != null) {
              cditestsejbweb_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("cditestsejbweb_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              cditestsejbweb_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/cditests/resources/beans.xml");
            if(ejbResURL != null) {
              cditestsejbweb_ejb.addAsManifestResource(ejbResURL, "beans.xml");
            }

            // Call the archive processor
            archiveProcessor.processEjbArchive(cditestsejbweb_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive cditestsejbweb_ear = ShrinkWrap.create(EnterpriseArchive.class, "cditestsejbweb.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            cditestsejbweb_ear.addAsModule(cditestsejbweb_ejb);
            cditestsejbweb_ear.addAsModule(cditestsejbweb_client);
            cditestsejbweb_ear.addAsModule(cditestsejbweb_web);

            // The application.xml descriptor
            URL earResURL = null;
            earResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/cditests/ejbweb/application.xml");
            if(earResURL != null) {
              cditestsejbweb_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            // earResURL = Client.class.getResource("application.ear.sun-application.xml");
            // if(earResURL != null) {
            //   cditestsejbweb_ear.addAsManifestResource(earResURL, "sun-application.xml");
            // }
            // Call the archive processor
            archiveProcessor.processEarArchive(cditestsejbweb_ear, Client.class, earResURL);
        return cditestsejbweb_ear;
        }

        @Test
        @Override
        public void sendRecvQueueTestUsingCDIFromServlet() throws java.lang.Exception {
            super.sendRecvQueueTestUsingCDIFromServlet();
        }

        @Test
        @Override
        public void sendRecvTopicTestUsingCDIFromServlet() throws java.lang.Exception {
            super.sendRecvTopicTestUsingCDIFromServlet();
        }

        @Test
        @Override
        public void sendRecvUsingCDIDefaultFactoryFromServlet() throws java.lang.Exception {
            super.sendRecvUsingCDIDefaultFactoryFromServlet();
        }

        @Test
        @Override
        public void verifySessionModeOnCDIJMSContextFromServlet() throws java.lang.Exception {
            super.verifySessionModeOnCDIJMSContextFromServlet();
        }

        @Test
        @Override
        public void testRestrictionsOnCDIJMSContextFromServlet() throws java.lang.Exception {
            super.testRestrictionsOnCDIJMSContextFromServlet();
        }

        @Test
        @Override
        public void sendRecvQueueTestUsingCDIFromEjb() throws java.lang.Exception {
            super.sendRecvQueueTestUsingCDIFromEjb();
        }

        @Test
        @Override
        public void sendRecvTopicTestUsingCDIFromEjb() throws java.lang.Exception {
            super.sendRecvTopicTestUsingCDIFromEjb();
        }

        @Test
        @Override
        public void sendRecvUsingCDIDefaultFactoryFromEjb() throws java.lang.Exception {
            super.sendRecvUsingCDIDefaultFactoryFromEjb();
        }

        @Test
        @Override
        public void verifySessionModeOnCDIJMSContextFromEjb() throws java.lang.Exception {
            super.verifySessionModeOnCDIJMSContextFromEjb();
        }

        @Test
        @Override
        public void testRestrictionsOnCDIJMSContextFromEjb() throws java.lang.Exception {
            super.testRestrictionsOnCDIJMSContextFromEjb();
        }

        @Test
        @Override
        public void testActiveJTAUsingCDIAcross2MethodsFromEjb() throws java.lang.Exception {
            super.testActiveJTAUsingCDIAcross2MethodsFromEjb();
        }

        @Test
        @Override
        public void sendRecvQueueTestUsingCDIFromManagedBean() throws java.lang.Exception {
            super.sendRecvQueueTestUsingCDIFromManagedBean();
        }

        @Test
        @Override
        public void sendRecvTopicTestUsingCDIFromManagedBean() throws java.lang.Exception {
            super.sendRecvTopicTestUsingCDIFromManagedBean();
        }


}