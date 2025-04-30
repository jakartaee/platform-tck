package com.sun.ts.tests.ejb30.bb.session.stateless.annotation.envsharing;

import com.sun.ts.tests.ejb30.bb.session.stateless.annotation.envsharing.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateless.annotation.envsharing.Client {
    /**
        EE10 Deployment Descriptors:
        ejb3_bb_stateless_envsharing: 
        ejb3_bb_stateless_envsharing_client: 
        ejb3_bb_stateless_envsharing_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateless/annotation/envsharing/ejb3_bb_stateless_envsharing_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_bb_stateless_envsharing", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateless_envsharing_client = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateless_envsharing_client.jar");
            // The class files
            ejb3_bb_stateless_envsharing_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.common.annotation.resource.ResourceIF.class,
            com.sun.ts.tests.ejb30.bb.session.stateless.annotation.envsharing.Client.class,
            com.sun.ts.tests.ejb30.common.annotation.resource.ClientBase.class,
            com.sun.ts.tests.ejb30.common.annotation.resource.UserTransactionNegativeIF.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/envsharing/ejb3_bb_stateless_envsharing_client.xml");
            if(resURL != null) {
              ejb3_bb_stateless_envsharing_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/envsharing/ejb3_bb_stateless_envsharing_client.jar.sun-application-client.xml");
            if(resURL != null) {
              ejb3_bb_stateless_envsharing_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            ejb3_bb_stateless_envsharing_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_bb_stateless_envsharing_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateless_envsharing_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateless_envsharing_ejb.jar");
            // The class files
            ejb3_bb_stateless_envsharing_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.annotation.envsharing.ResourceSetterBean.class,
                com.sun.ts.tests.ejb30.common.annotation.resource.ResourceIF.class,
                com.sun.ts.tests.ejb30.common.annotation.resource.EnvSharingBeanBase.class,
                com.sun.ts.tests.ejb30.common.annotation.resource.Constants.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.annotation.envsharing.ResourceTypeBean.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.annotation.envsharing.ResourceFieldBean.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.common.annotation.resource.ResourceBeanBase.class,
                com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/envsharing/ejb3_bb_stateless_envsharing_ejb.xml");
            if(ejbResURL != null) {
              ejb3_bb_stateless_envsharing_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/envsharing/ejb3_bb_stateless_envsharing_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb3_bb_stateless_envsharing_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb3_bb_stateless_envsharing_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_bb_stateless_envsharing_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_bb_stateless_envsharing.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            ejb3_bb_stateless_envsharing_ear.addAsModule(ejb3_bb_stateless_envsharing_ejb);
            ejb3_bb_stateless_envsharing_ear.addAsModule(ejb3_bb_stateless_envsharing_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/envsharing/application.xml");
            if(earResURL != null) {
              ejb3_bb_stateless_envsharing_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/envsharing/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_bb_stateless_envsharing_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_bb_stateless_envsharing_ear, Client.class, earResURL);
        return ejb3_bb_stateless_envsharing_ear;
        }

        @Test
        @Override
        public void urlTest() throws java.lang.Exception {
            super.urlTest();
        }

        @Test
        @Override
        public void mailSessionTest() throws java.lang.Exception {
            super.mailSessionTest();
        }


}