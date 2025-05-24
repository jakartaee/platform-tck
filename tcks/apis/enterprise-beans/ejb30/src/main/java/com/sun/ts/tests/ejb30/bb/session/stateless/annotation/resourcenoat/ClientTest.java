package com.sun.ts.tests.ejb30.bb.session.stateless.annotation.resourcenoat;

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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateless.annotation.resourcenoat.Client {
    /**
        EE10 Deployment Descriptors:
        ejb3_bb_stateless_resourcenoat: 
        ejb3_bb_stateless_resourcenoat_client: 
        ejb3_bb_stateless_resourcenoat_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateless/annotation/resourcenoat/ejb3_bb_stateless_resourcenoat_ejb.xml
        /com/sun/ts/tests/ejb30/bb/session/stateless/annotation/resourcenoat/ejb3_bb_stateless_resourcenoat_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_bb_stateless_resourcenoat", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateless_resourcenoat_client = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateless_resourcenoat_client.jar");
            // The class files
            ejb3_bb_stateless_resourcenoat_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.tests.ejb30.bb.session.stateless.annotation.resourcenoat.Client.class,
            Fault.class,
            com.sun.ts.tests.ejb30.common.annotation.resource.ResourceIF.class,
            com.sun.ts.tests.ejb30.common.annotation.resource.ResourceNoop.class,
            com.sun.ts.tests.ejb30.common.annotation.resource.ClientBase.class,
            com.sun.ts.tests.ejb30.common.annotation.resource.UserTransactionNegativeIF.class,
            EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            // The sun-application-client.xml
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/resourcenoat/ejb3_bb_stateless_resourcenoat_client.jar.sun-application-client.xml");
            ejb3_bb_stateless_resourcenoat_client.addAsManifestResource(resURL, "sun-application-client.xml");
            ejb3_bb_stateless_resourcenoat_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_bb_stateless_resourcenoat_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateless_resourcenoat_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateless_resourcenoat_ejb.jar");
            // The class files
            ejb3_bb_stateless_resourcenoat_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.common.annotation.resource.ResourceIF.class,
                com.sun.ts.tests.ejb30.common.annotation.resource.Constants.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.annotation.resourcenoat.ResourceFieldBean.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.common.annotation.resource.ResourceBeanBase.class,
                com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.annotation.resourcenoat.ResourceSetterBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/resourcenoat/ejb3_bb_stateless_resourcenoat_ejb.xml");
            if(ejbResURL != null) {
              ejb3_bb_stateless_resourcenoat_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/resourcenoat/ejb3_bb_stateless_resourcenoat_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb3_bb_stateless_resourcenoat_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb3_bb_stateless_resourcenoat_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_bb_stateless_resourcenoat_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_bb_stateless_resourcenoat.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            ejb3_bb_stateless_resourcenoat_ear.addAsModule(ejb3_bb_stateless_resourcenoat_ejb);
            ejb3_bb_stateless_resourcenoat_ear.addAsModule(ejb3_bb_stateless_resourcenoat_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/resourcenoat/application.xml");
            if(earResURL != null) {
              ejb3_bb_stateless_resourcenoat_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/resourcenoat/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_bb_stateless_resourcenoat_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_bb_stateless_resourcenoat_ear, Client.class, earResURL);
        return ejb3_bb_stateless_resourcenoat_ear;
        }

        @Test
        @Override
        public void ejbContextTest() throws java.lang.Exception {
            super.ejbContextTest();
        }

        @Test
        @Override
        public void dataSourceTest() throws java.lang.Exception {
            super.dataSourceTest();
        }

        @Test
        @Override
        public void dataSource2Test() throws java.lang.Exception {
            super.dataSource2Test();
        }

        @Test
        @Override
        public void urlTest() throws java.lang.Exception {
            super.urlTest();
        }

        @Test
        @Override
        public void queueConnectionFactoryTest() throws java.lang.Exception {
            super.queueConnectionFactoryTest();
        }

        @Test
        @Override
        public void topicConnectionFactoryTest() throws java.lang.Exception {
            super.topicConnectionFactoryTest();
        }

        @Test
        @Override
        public void connectionFactoryQTest() throws java.lang.Exception {
            super.connectionFactoryQTest();
        }

        @Test
        @Override
        public void connectionFactoryTTest() throws java.lang.Exception {
            super.connectionFactoryTTest();
        }

        @Test
        @Override
        public void userTransactionTest() throws java.lang.Exception {
            super.userTransactionTest();
        }

        @Test
        @Override
        public void topicTest() throws java.lang.Exception {
            super.topicTest();
        }

        @Test
        @Override
        public void queueTest() throws java.lang.Exception {
            super.queueTest();
        }


}