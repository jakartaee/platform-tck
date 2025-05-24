package com.sun.ts.tests.jms.ee20.resourcedefs.annotations;

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
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;


@Disabled("https://github.com/jakartaee/platform-tck/issues/2231")
@ExtendWith(ArquillianExtension.class)
@Tag("jms")
@Tag("jms_optional")
@Tag("platform_optional")
@Tag("web_optional")
@Tag("tck-appclient")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.jms.ee20.resourcedefs.annotations.Client {
    /**
        EE10 Deployment Descriptors:
        resourcedefs_annotations: META-INF/application.xml
        resourcedefs_annotations_client: 
        resourcedefs_annotations_ejb: 
        resourcedefs_annotations_web: 

        Found Descriptors:
        Client:

        Ejb:

        War:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "resourcedefs_annotations", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive resourcedefs_annotations_web = ShrinkWrap.create(WebArchive.class, "resourcedefs_annotations_web.war");
            // The class files
            resourcedefs_annotations_web.addClasses(
            com.sun.ts.tests.jms.ee20.resourcedefs.annotations.MyBean.class,
            com.sun.ts.tests.jms.ee20.resourcedefs.annotations.ServletClient.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("com/sun/ts/tests/jms/ee20/resourcedefs/annotations/resourcedefs_annotations_web.xml");
            if(warResURL != null) {
              resourcedefs_annotations_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/resourcedefs/annotations/resourcedefs_annotations_web.war.sun-web.xml");
            if(warResURL != null) {
              resourcedefs_annotations_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/resourcedefs/annotations/JspClient.jsp");
            if(warResURL != null) {
              resourcedefs_annotations_web.addAsWebResource(warResURL, "/JspClient.jsp");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/resourcedefs/annotations/index.html");
            if(warResURL != null) {
              resourcedefs_annotations_web.addAsWebResource(warResURL, "/index.html");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(resourcedefs_annotations_web, Client.class, warResURL);

        // Client
            // the jar with the correct archive name
            JavaArchive resourcedefs_annotations_client = ShrinkWrap.create(JavaArchive.class, "resourcedefs_annotations_client.jar");
            // The class files
            resourcedefs_annotations_client.addClasses(
            Fault.class,
            com.sun.ts.tests.jms.ee20.resourcedefs.annotations.Client.class,
            com.sun.ts.tests.jms.ee20.resourcedefs.annotations.EjbClientIF.class,
            EETest.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("application-client.xml");
            if(resURL != null) {
              resourcedefs_annotations_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("sun-application-client.xml");
            if(resURL != null) {
              resourcedefs_annotations_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            resourcedefs_annotations_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(resourcedefs_annotations_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive resourcedefs_annotations_ejb = ShrinkWrap.create(JavaArchive.class, "resourcedefs_annotations_ejb.jar");
            // The class files
            resourcedefs_annotations_ejb.addClasses(
                com.sun.ts.tests.jms.ee20.resourcedefs.annotations.EjbClient.class,
                com.sun.ts.tests.jms.ee20.resourcedefs.annotations.EjbClientIF.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("resourcedefs_annotations_ejb.xml");
            if(ejbResURL != null) {
              resourcedefs_annotations_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("resourcedefs_annotations_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              resourcedefs_annotations_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(resourcedefs_annotations_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive resourcedefs_annotations_ear = ShrinkWrap.create(EnterpriseArchive.class, "resourcedefs_annotations.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            resourcedefs_annotations_ear.addAsModule(resourcedefs_annotations_ejb);
            resourcedefs_annotations_ear.addAsModule(resourcedefs_annotations_client);
            resourcedefs_annotations_ear.addAsModule(resourcedefs_annotations_web);



            // The application.xml descriptor
            URL earResURL = null;
            earResURL = Client.class.getResource("application.xml.clientear");
            if(earResURL != null) {
              resourcedefs_annotations_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("application.ear.sun-application.xml");
            if(earResURL != null) {
              resourcedefs_annotations_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(resourcedefs_annotations_ear, Client.class, earResURL);
        return resourcedefs_annotations_ear;
        }

        @Test
        @Override
        public void sendAndRecvQueueTestFromAppClient() throws java.lang.Exception {
            super.sendAndRecvQueueTestFromAppClient();
        }

        @Test
        @Override
        public void sendAndRecvTopicTestFromAppClient() throws java.lang.Exception {
            super.sendAndRecvTopicTestFromAppClient();
        }

        @Test
        @Override
        public void sendAndRecvQueueTestFromServletClient() throws java.lang.Exception {
            super.sendAndRecvQueueTestFromServletClient();
        }

        @Test
        @Override
        public void sendAndRecvTopicTestFromServletClient() throws java.lang.Exception {
            super.sendAndRecvTopicTestFromServletClient();
        }

        @Test
        @Override
        public void sendAndRecvQueueTestFromJspClient() throws java.lang.Exception {
            super.sendAndRecvQueueTestFromJspClient();
        }

        @Test
        @Override
        public void sendAndRecvTopicTestFromJspClient() throws java.lang.Exception {
            super.sendAndRecvTopicTestFromJspClient();
        }

        @Test
        @Override
        public void sendAndRecvQueueTestFromEjbClient() throws java.lang.Exception {
            super.sendAndRecvQueueTestFromEjbClient();
        }

        @Test
        @Override
        public void sendAndRecvTopicTestFromEjbClient() throws java.lang.Exception {
            super.sendAndRecvTopicTestFromEjbClient();
        }

        @Test
        @Override
        public void checkClientIDOnDurableConnFactoryTest() throws java.lang.Exception {
            super.checkClientIDOnDurableConnFactoryTest();
        }


}