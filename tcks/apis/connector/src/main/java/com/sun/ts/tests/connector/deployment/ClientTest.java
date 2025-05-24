package com.sun.ts.tests.connector.deployment;

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
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.connector.deployment.DeploymentClient {
    /**
        EE10 Deployment Descriptors:
        ejb_Deployment: 
        ejb_Deployment_client: META-INF/application-client.xml,jar.sun-application-client.xml
        ejb_Deployment_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/connector/deployment/ejb_Deployment_client.xml
        /com/sun/ts/tests/connector/deployment/ejb_Deployment_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/connector/deployment/ejb_Deployment_ejb.xml
        /com/sun/ts/tests/connector/deployment/ejb_Deployment_ejb.jar.sun-ejb-jar.xml
        Rar:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb_Deployment", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb_Deployment_client = ShrinkWrap.create(JavaArchive.class, "ejb_Deployment_client.jar");
            // The class files
            ejb_Deployment_client.addClasses(
            com.sun.ts.tests.connector.deployment.Deployment.class,
            com.sun.ts.tests.connector.deployment.DeploymentClient.class,
            Fault.class,
            EETest.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = DeploymentClient.class.getResource("ejb_Deployment_client.xml");
            if(resURL != null) {
              ejb_Deployment_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = DeploymentClient.class.getResource("ejb_Deployment_client.jar.sun-application-client.xml");
            if(resURL != null) {
              ejb_Deployment_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            ejb_Deployment_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.connector.deployment.DeploymentClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb_Deployment_client, DeploymentClient.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive ejb_Deployment_ejb = ShrinkWrap.create(JavaArchive.class, "ejb_Deployment_ejb.jar");
            // The class files
            ejb_Deployment_ejb.addClasses(
                com.sun.ts.tests.connector.deployment.Deployment.class,
                com.sun.ts.tests.connector.deployment.DeploymentEJB.class,
                com.sun.ts.tests.connector.util.DBSupport.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = DeploymentClient.class.getResource("ejb_Deployment_ejb.xml");
            if(ejbResURL1 != null) {
              ejb_Deployment_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = DeploymentClient.class.getResource("ejb_Deployment_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              ejb_Deployment_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb_Deployment_ejb, DeploymentClient.class, ejbResURL1);

            // Rar
            JavaArchive ejb_Deployment_rar = ShrinkWrap.create(JavaArchive.class, "whitebox-tx.rar");
            URL rarURL = ClientTest.class.getResource("ra.xml");
            ejb_Deployment_rar.addAsManifestResource(rarURL, "ra.xml");;
            rarURL = ClientTest.class.getResource("sun-ra.xml");
            ejb_Deployment_rar.addAsManifestResource(rarURL, "sun-ra.xml");;
            archiveProcessor.processRarArchive(ejb_Deployment_rar, DeploymentClient.class, rarURL);

        // Ear
            EnterpriseArchive ejb_Deployment_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb_Deployment.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            ejb_Deployment_ear.addAsModule(ejb_Deployment_ejb);
            ejb_Deployment_ear.addAsModule(ejb_Deployment_client);
            ejb_Deployment_ear.addAsModule(ejb_Deployment_rar);


            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb_Deployment_ear, DeploymentClient.class, earResURL);
        return ejb_Deployment_ear;
        }

        @Test
        @Override
        public void testRarInEar() throws java.lang.Exception {
            super.testRarInEar();
        }


}