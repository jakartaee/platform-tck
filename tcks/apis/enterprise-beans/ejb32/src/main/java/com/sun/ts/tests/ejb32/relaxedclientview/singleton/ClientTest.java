package com.sun.ts.tests.ejb32.relaxedclientview.singleton;

import com.sun.ts.tests.ejb32.relaxedclientview.singleton.Client;
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

public class ClientTest extends com.sun.ts.tests.ejb32.relaxedclientview.singleton.Client {
    /**
        EE10 Deployment Descriptors:
        singleton_relaxed_client_view: 
        singleton_relaxed_client_view_client: 
        singleton_relaxed_client_view_ejb: META-INF/ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb32/relaxedclientview/singleton/singleton_relaxed_client_view_ejb.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "singleton_relaxed_client_view", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive singleton_relaxed_client_view_client = ShrinkWrap.create(JavaArchive.class, "singleton_relaxed_client_view_client.jar");
            // The class files
            singleton_relaxed_client_view_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb32.relaxedclientview.common.NormalInterface2.class,
            com.sun.ts.tests.ejb32.relaxedclientview.common.TestConstants.class,
            com.sun.ts.tests.ejb32.relaxedclientview.common.RemoteAnnotationInterface1.class,
            com.sun.ts.tests.ejb32.relaxedclientview.common.HelperSingleton.class,
            com.sun.ts.tests.ejb32.relaxedclientview.singleton.Client.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb32.relaxedclientview.common.ClientBase.class,
            com.sun.ts.tests.ejb32.relaxedclientview.common.NormalInterface1.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb32/relaxedclientview/singleton/singleton_relaxed_client_view_client.jar.sun-application-client.xml");
            singleton_relaxed_client_view_client.addAsManifestResource(resURL, "sun-application-client.xml");
            singleton_relaxed_client_view_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(singleton_relaxed_client_view_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive singleton_relaxed_client_view_ejb = ShrinkWrap.create(JavaArchive.class, "singleton_relaxed_client_view_ejb.jar");
            // The class files
            singleton_relaxed_client_view_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb32.relaxedclientview.singleton.OneRemoteAnnotationOnInterfaceBean.class,
                com.sun.ts.tests.ejb32.relaxedclientview.singleton.OneRemoteAnnotationOnEjbBean.class,
                com.sun.ts.tests.ejb32.relaxedclientview.common.BaseBean.class,
                com.sun.ts.tests.ejb32.relaxedclientview.common.TestConstants.class,
                com.sun.ts.tests.ejb32.relaxedclientview.singleton.LocalDDBean.class,
                com.sun.ts.tests.ejb32.relaxedclientview.common.RemoteAnnotationInterface1.class,
                com.sun.ts.tests.ejb32.relaxedclientview.singleton.HelperSingletonBean.class,
                com.sun.ts.tests.ejb32.relaxedclientview.singleton.NoAnnotationBean.class,
                com.sun.ts.tests.ejb32.relaxedclientview.singleton.NoInterfaceViewBean.class,
                com.sun.ts.tests.ejb32.relaxedclientview.singleton.LocalAnnotationBean.class,
                com.sun.ts.tests.ejb32.relaxedclientview.singleton.RemoteAnnotationBean.class,
                com.sun.ts.tests.ejb32.relaxedclientview.common.NormalInterface2.class,
                com.sun.ts.tests.ejb32.relaxedclientview.common.HelperSingleton.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb32.relaxedclientview.common.AbstractHelperSingleton.class,
                com.sun.ts.tests.ejb32.relaxedclientview.common.NormalInterface1.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb32/relaxedclientview/singleton/singleton_relaxed_client_view_ejb.xml");
            singleton_relaxed_client_view_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            // Call the archive processor
            archiveProcessor.processEjbArchive(singleton_relaxed_client_view_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive singleton_relaxed_client_view_ear = ShrinkWrap.create(EnterpriseArchive.class, "singleton_relaxed_client_view.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            singleton_relaxed_client_view_ear.addAsModule(singleton_relaxed_client_view_ejb);
            singleton_relaxed_client_view_ear.addAsModule(singleton_relaxed_client_view_client);



            // The application.xml descriptor
            // Call the archive processor
            archiveProcessor.processEarArchive(singleton_relaxed_client_view_ear, Client.class, null);
        return singleton_relaxed_client_view_ear;
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