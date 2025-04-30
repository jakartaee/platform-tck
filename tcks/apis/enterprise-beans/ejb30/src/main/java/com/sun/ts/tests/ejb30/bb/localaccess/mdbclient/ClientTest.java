package com.sun.ts.tests.ejb30.bb.localaccess.mdbclient;

import com.sun.ts.tests.ejb30.bb.localaccess.mdbclient.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.localaccess.mdbclient.Client {
    /**
        EE10 Deployment Descriptors:
        localaccess_mdbclient: 
        localaccess_mdbclient_client: jar.sun-application-client.xml
        localaccess_mdbclient_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/bb/localaccess/mdbclient/localaccess_mdbclient_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/bb/localaccess/mdbclient/localaccess_mdbclient_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "localaccess_mdbclient", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive localaccess_mdbclient_client = ShrinkWrap.create(JavaArchive.class, "localaccess_mdbclient_client.jar");
            // The class files
            localaccess_mdbclient_client.addClasses(
            com.sun.ts.tests.jms.commonee.Client.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.common.messaging.Constants.class,
            com.sun.ts.tests.ejb30.bb.localaccess.mdbclient.Client.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.common.messaging.ClientBase.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/localaccess/mdbclient/localaccess_mdbclient_client.xml");
            if(resURL != null) {
              localaccess_mdbclient_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/localaccess/mdbclient/localaccess_mdbclient_client.jar.sun-application-client.xml");
            if(resURL != null) {
              localaccess_mdbclient_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            localaccess_mdbclient_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(localaccess_mdbclient_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive localaccess_mdbclient_ejb = ShrinkWrap.create(JavaArchive.class, "localaccess_mdbclient_ejb.jar");
            // The class files
            localaccess_mdbclient_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.TestBeanBase.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.StatelessDefaultLocalBean.class,
                com.sun.ts.tests.ejb30.bb.localaccess.mdbclient.MdbTestBean.class,
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.ejb30.common.messaging.StatusReporter.class,
                com.sun.ts.tests.ejb30.common.appexception.UncheckedAppException.class,
                com.sun.ts.tests.ejb30.common.messaging.Constants.class,
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
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/localaccess/mdbclient/localaccess_mdbclient_ejb.xml");
            if(ejbResURL != null) {
              localaccess_mdbclient_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/localaccess/mdbclient/localaccess_mdbclient_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              localaccess_mdbclient_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(localaccess_mdbclient_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive localaccess_mdbclient_ear = ShrinkWrap.create(EnterpriseArchive.class, "localaccess_mdbclient.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            localaccess_mdbclient_ear.addAsModule(localaccess_mdbclient_ejb);
            localaccess_mdbclient_ear.addAsModule(localaccess_mdbclient_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/localaccess/mdbclient/application.xml");
            if(earResURL != null) {
              localaccess_mdbclient_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/localaccess/mdbclient/application.ear.sun-application.xml");
            if(earResURL != null) {
              localaccess_mdbclient_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(localaccess_mdbclient_ear, Client.class, earResURL);
        return localaccess_mdbclient_ear;
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