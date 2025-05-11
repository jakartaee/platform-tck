package com.sun.ts.tests.ejb30.bb.localaccess.statelessclient;

import com.sun.ts.tests.ejb30.bb.localaccess.statelessclient.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.localaccess.statelessclient.Client {
    /**
        EE10 Deployment Descriptors:
        localaccess_statelessclient: 
        localaccess_statelessclient_client: 
        localaccess_statelessclient_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb30/bb/localaccess/statelessclient/localaccess_statelessclient_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "localaccess_statelessclient", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive localaccess_statelessclient_client = ShrinkWrap.create(JavaArchive.class, "localaccess_statelessclient_client.jar");
            // The class files
            localaccess_statelessclient_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.bb.localaccess.common.ClientBase.class,
            com.sun.ts.tests.ejb30.bb.localaccess.common.StatefulDefaultLocalIF.class,
            com.sun.ts.tests.ejb30.bb.localaccess.common.DefaultLocalIF.class,
            com.sun.ts.tests.ejb30.bb.localaccess.common.StatefulLocalIF.class,
            com.sun.ts.tests.ejb30.bb.localaccess.statelessclient.Client.class,
            com.sun.ts.tests.ejb30.bb.localaccess.common.CommonIF.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.bb.localaccess.common.TestBeanIF.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.common.calc.CalculatorException.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb30.bb.localaccess.common.Constants.class,
            com.sun.ts.tests.ejb30.bb.localaccess.common.LocalIF.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("client.sun-application-client.xml");
            localaccess_statelessclient_client.addAsManifestResource(resURL, "sun-application-client.xml");
            localaccess_statelessclient_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(localaccess_statelessclient_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive localaccess_statelessclient_ejb = ShrinkWrap.create(JavaArchive.class, "localaccess_statelessclient_ejb.jar");
            // The class files
            localaccess_statelessclient_ejb.addClasses(
                com.sun.ts.tests.ejb30.bb.localaccess.statelessclient.StatelessTestBean.class,
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.TestBeanBase.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.StatelessDefaultLocalBean.class,
                com.sun.ts.tests.ejb30.common.appexception.UncheckedAppException.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.StatefulDefaultLocalIF.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.DefaultLocalIF.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.CommonBase.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.StatefulDefaultLocalBean.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.StatefulLocalBean.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.StatefulLocalIF.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.StatelessLocal2Bean.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.StatelessLocalBean.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.CommonIF.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.TestBeanIF.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.common.calc.CalculatorException.class,
                com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.Constants.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.LocalIF.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/localaccess/statelessclient/localaccess_statelessclient_ejb.xml");
            if(ejbResURL != null) {
              localaccess_statelessclient_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/localaccess/statelessclient/localaccess_statelessclient_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              localaccess_statelessclient_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(localaccess_statelessclient_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive localaccess_statelessclient_ear = ShrinkWrap.create(EnterpriseArchive.class, "localaccess_statelessclient.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            localaccess_statelessclient_ear.addAsModule(localaccess_statelessclient_ejb);
            localaccess_statelessclient_ear.addAsModule(localaccess_statelessclient_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/localaccess/statelessclient/application.xml");
            if(earResURL != null) {
              localaccess_statelessclient_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/localaccess/statelessclient/application.ear.sun-application.xml");
            if(earResURL != null) {
              localaccess_statelessclient_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(localaccess_statelessclient_ear, Client.class, earResURL);
        return localaccess_statelessclient_ear;
        }

        @Test
        @Override
        public void passByValueTest() throws java.lang.Exception {
            super.passByValueTest();
        }

        @Test
        @Override
        public void passByReferenceTest1() throws java.lang.Exception {
            super.passByReferenceTest1();
        }

        @Test
        @Override
        public void exceptionTest1() throws java.lang.Exception {
            super.exceptionTest1();
        }

        @Test
        @Override
        public void runtimeExceptionTest1() throws java.lang.Exception {
            super.runtimeExceptionTest1();
        }

        @Test
        @Override
        public void passByReferenceTest2() throws java.lang.Exception {
            super.passByReferenceTest2();
        }

        @Test
        @Override
        public void exceptionTest2() throws java.lang.Exception {
            super.exceptionTest2();
        }

        @Test
        @Override
        public void runtimeExceptionTest2() throws java.lang.Exception {
            super.runtimeExceptionTest2();
        }

        @Test
        @Override
        public void passByReferenceTest3() throws java.lang.Exception {
            super.passByReferenceTest3();
        }

        @Test
        @Override
        public void exceptionTest3() throws java.lang.Exception {
            super.exceptionTest3();
        }

        @Test
        @Override
        public void runtimeExceptionTest3() throws java.lang.Exception {
            super.runtimeExceptionTest3();
        }

        @Test
        @Override
        public void passByReferenceTest4() throws java.lang.Exception {
            super.passByReferenceTest4();
        }

        @Test
        @Override
        public void exceptionTest4() throws java.lang.Exception {
            super.exceptionTest4();
        }

        @Test
        @Override
        public void runtimeExceptionTest4() throws java.lang.Exception {
            super.runtimeExceptionTest4();
        }

        @Test
        @Override
        public void passByReferenceTest5() throws java.lang.Exception {
            super.passByReferenceTest5();
        }

        @Test
        @Override
        public void exceptionTest5() throws java.lang.Exception {
            super.exceptionTest5();
        }

        @Test
        @Override
        public void runtimeExceptionTest5() throws java.lang.Exception {
            super.runtimeExceptionTest5();
        }


}