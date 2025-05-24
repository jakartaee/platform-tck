package com.sun.ts.tests.ejb30.bb.session.stateless.annotation.resource;

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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateless.annotation.resource.Client {
    /**
        EE10 Deployment Descriptors:
        ejb3_bb_stateless_resource: 
        ejb3_bb_stateless_resource_client: META-INF/application-client.xml,jar.sun-application-client.xml
        ejb3_bb_stateless_resource_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        ejb3_bb_stateless_resourcenoat: 
        ejb3_bb_stateless_resourcenoat_client: 
        ejb3_bb_stateless_resourcenoat_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        ejb3_bb_stateless_resourceoverride: 
        ejb3_bb_stateless_resourceoverride_client: 
        ejb3_bb_stateless_resourceoverride_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/bb/session/stateless/annotation/resource/ejb3_bb_stateless_resource_client.xml
        /com/sun/ts/tests/ejb30/bb/session/stateless/annotation/resource/ejb3_bb_stateless_resource_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateless/annotation/resource/ejb3_bb_stateless_resource_ejb.xml
        /com/sun/ts/tests/ejb30/bb/session/stateless/annotation/resource/ejb3_bb_stateless_resource_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_bb_stateless_resource", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateless_resource_client = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateless_resource_client.jar");
            // The class files
            ejb3_bb_stateless_resource_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            Fault.class,
            com.sun.ts.tests.ejb30.common.annotation.resource.ResourceIF.class,
            com.sun.ts.tests.ejb30.common.annotation.resource.ClientBase.class,
            com.sun.ts.tests.ejb30.common.annotation.resource.UserTransactionNegativeIF.class,
            EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            SetupException.class,
            com.sun.ts.tests.ejb30.bb.session.stateless.annotation.resource.Client.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/resource/ejb3_bb_stateless_resource_client.xml");
            if(resURL != null) {
              ejb3_bb_stateless_resource_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/resource/ejb3_bb_stateless_resource_client.jar.sun-application-client.xml");
            if(resURL != null) {
              ejb3_bb_stateless_resource_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            ejb3_bb_stateless_resource_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_bb_stateless_resource_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateless_resource_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateless_resource_ejb.jar");
            // The class files
            ejb3_bb_stateless_resource_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.annotation.resource.UserTransactionNegativeBean.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.annotation.resource.ResourceSetterBean.class,
                com.sun.ts.tests.ejb30.common.annotation.resource.ResourceIF.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.annotation.resource.ResourceTypeBean.class,
                com.sun.ts.tests.ejb30.common.annotation.resource.Constants.class,
                com.sun.ts.tests.ejb30.common.annotation.resource.UserTransactionNegativeBeanBase.class,
                com.sun.ts.tests.ejb30.common.annotation.resource.UserTransactionNegativeIF.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.common.annotation.resource.ResourceBeanBase.class,
                com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.annotation.resource.ResourceFieldBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/resource/ejb3_bb_stateless_resource_ejb.xml");
            if(ejbResURL != null) {
              ejb3_bb_stateless_resource_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/resource/ejb3_bb_stateless_resource_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb3_bb_stateless_resource_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb3_bb_stateless_resource_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_bb_stateless_resource_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_bb_stateless_resource.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            ejb3_bb_stateless_resource_ear.addAsModule(ejb3_bb_stateless_resource_ejb);
            ejb3_bb_stateless_resource_ear.addAsModule(ejb3_bb_stateless_resource_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/resource/application.xml");
            if(earResURL != null) {
              ejb3_bb_stateless_resource_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/resource/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_bb_stateless_resource_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_bb_stateless_resource_ear, Client.class, earResURL);
        return ejb3_bb_stateless_resource_ear;
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
        public void userTransactionLookupNegativeTest() throws java.lang.Exception {
            super.userTransactionLookupNegativeTest();
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

        @Test
        @Override
        public void customResourceInjectionInAppclient() throws java.lang.Exception {
            super.customResourceInjectionInAppclient();
        }

        @Test
        @Override
        public void customResourceLookupInAppclient() throws java.lang.Exception {
            super.customResourceLookupInAppclient();
        }

        @Test
        @Override
        public void customResourceInjectionInEjb() throws java.lang.Exception {
            super.customResourceInjectionInEjb();
        }

        @Test
        @Override
        public void customResourceLookupInEjb() throws java.lang.Exception {
            super.customResourceLookupInEjb();
        }

        @Test
        @Override
        public void TransactionSynchronizationRegistryInjection() throws java.lang.Exception {
            super.TransactionSynchronizationRegistryInjection();
        }

        @Test
        @Override
        public void TransactionSynchronizationRegistryLookup() throws java.lang.Exception {
            super.TransactionSynchronizationRegistryLookup();
        }

        @Test
        @Override
        public void TimerServiceInjection() throws java.lang.Exception {
            super.TimerServiceInjection();
        }

        @Test
        @Override
        public void TimerServiceLookup() throws java.lang.Exception {
            super.TimerServiceLookup();
        }

        @Test
        @Override
        public void destinationQTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.destinationQTest();
        }

        @Test
        @Override
        public void destinationTTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.destinationTTest();
        }


}