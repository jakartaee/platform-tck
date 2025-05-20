package com.sun.ts.tests.ejb32.relaxedclientview.stateful;

import com.sun.ts.tests.ejb32.relaxedclientview.stateful.Client;
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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("ejb")
@Tag("ejb32")
@Tag("platform")
@Tag("tck-appclient")

public class ClientTest extends com.sun.ts.tests.ejb32.relaxedclientview.stateful.Client {
    /**
        EE10 Deployment Descriptors:
        stateful_relaxed_client_view: 
        stateful_relaxed_client_view_client: 
        stateful_relaxed_client_view_ejb: META-INF/ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb32/relaxedclientview/stateful/stateful_relaxed_client_view_ejb.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "stateful_relaxed_client_view", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive stateful_relaxed_client_view_client = ShrinkWrap.create(JavaArchive.class, "stateful_relaxed_client_view_client.jar");
            // The class files
            stateful_relaxed_client_view_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb32.relaxedclientview.common.NormalInterface2.class,
            com.sun.ts.tests.ejb32.relaxedclientview.common.TestConstants.class,
            com.sun.ts.tests.ejb32.relaxedclientview.common.RemoteAnnotationInterface1.class,
            com.sun.ts.tests.ejb32.relaxedclientview.common.HelperSingleton.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb32.relaxedclientview.common.ClientBase.class,
            com.sun.ts.tests.ejb32.relaxedclientview.common.NormalInterface1.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb32.relaxedclientview.stateful.Client.class
            );
            // The application-client.xml descriptor
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb32/relaxedclientview/stateful/stateful_relaxed_client_view_client.jar.sun-application-client.xml");
            stateful_relaxed_client_view_client.addAsManifestResource(resURL, "sun-application-client.xml");
            stateful_relaxed_client_view_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(stateful_relaxed_client_view_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive stateful_relaxed_client_view_ejb = ShrinkWrap.create(JavaArchive.class, "stateful_relaxed_client_view_ejb.jar");
            // The class files
            stateful_relaxed_client_view_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb32.relaxedclientview.common.BaseBean.class,
                com.sun.ts.tests.ejb32.relaxedclientview.common.TestConstants.class,
                com.sun.ts.tests.ejb32.relaxedclientview.stateful.LocalDDBean.class,
                com.sun.ts.tests.ejb32.relaxedclientview.common.RemoteAnnotationInterface1.class,
                com.sun.ts.tests.ejb32.relaxedclientview.stateful.HelperSingletonBean.class,
                com.sun.ts.tests.ejb32.relaxedclientview.stateful.LocalAnnotationBean.class,
                com.sun.ts.tests.ejb32.relaxedclientview.stateful.OneRemoteAnnotationOnInterfaceBean.class,
                com.sun.ts.tests.ejb32.relaxedclientview.stateful.OneRemoteAnnotationOnEjbBean.class,
                com.sun.ts.tests.ejb32.relaxedclientview.stateful.RemoteAnnotationBean.class,
                com.sun.ts.tests.ejb32.relaxedclientview.stateful.NoAnnotationBean.class,
                com.sun.ts.tests.ejb32.relaxedclientview.common.NormalInterface2.class,
                com.sun.ts.tests.ejb32.relaxedclientview.common.HelperSingleton.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb32.relaxedclientview.common.AbstractHelperSingleton.class,
                com.sun.ts.tests.ejb32.relaxedclientview.common.NormalInterface1.class,
                com.sun.ts.tests.ejb32.relaxedclientview.stateful.NoInterfaceViewBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb32/relaxedclientview/stateful/stateful_relaxed_client_view_ejb.xml");
            if(ejbResURL != null) {
              stateful_relaxed_client_view_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb32/relaxedclientview/stateful/stateful_relaxed_client_view_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              stateful_relaxed_client_view_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(stateful_relaxed_client_view_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive stateful_relaxed_client_view_ear = ShrinkWrap.create(EnterpriseArchive.class, "stateful_relaxed_client_view.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            stateful_relaxed_client_view_ear.addAsModule(stateful_relaxed_client_view_ejb);
            stateful_relaxed_client_view_ear.addAsModule(stateful_relaxed_client_view_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb32/relaxedclientview/stateful/application.xml");
            if(earResURL != null) {
              stateful_relaxed_client_view_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb32/relaxedclientview/stateful/application.ear.sun-application.xml");
            if(earResURL != null) {
              stateful_relaxed_client_view_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(stateful_relaxed_client_view_ear, Client.class, earResURL);
        return stateful_relaxed_client_view_ear;
        }

        @Test
        @Override
        public void noAnnotationTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.noAnnotationTest();
        }

        @Test
        @Override
        public void localAnnotationTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.localAnnotationTest();
        }

        @Test
        @Override
        public void remoteAnnotationTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.remoteAnnotationTest();
        }

        @Test
        @Override
        public void oneRemoteAnnotationOnInterfaceTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.oneRemoteAnnotationOnInterfaceTest();
        }

        @Test
        @Override
        public void oneRemoteAnnotationOnEjbTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.oneRemoteAnnotationOnEjbTest();
        }

        @Test
        @Override
        public void noInterfaceViewTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.noInterfaceViewTest();
        }

        @Test
        @Override
        public void localDDTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.localDDTest();
        }


}