package com.sun.ts.tests.jms.core.appclient.txtopictests;

import com.sun.ts.lib.harness.Fault;

import java.net.URL;

import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;
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
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("jms")
@Tag("platform")
@Tag("jms_web")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TxTopicTestsAppclientTest extends com.sun.ts.tests.jms.core.appclient.txtopictests.TxTopicTests {
    static final String VEHICLE_ARCHIVE = "txTopicTests_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        txTopicTests_appclient_vehicle: 
        txTopicTests_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/core/appclient/txtopictests/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive txTopicTests_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "txTopicTests_appclient_vehicle_client.jar");
            // The class files
            txTopicTests_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.core.appclient.txtopictests.TxTopicTests.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = TxTopicTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              txTopicTests_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = TxTopicTests.class.getResource("txTopicTests_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              txTopicTests_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            txTopicTests_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + TxTopicTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(txTopicTests_appclient_vehicle_client, TxTopicTests.class, resURL);

        // Ear
            EnterpriseArchive txTopicTests_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "txTopicTests_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            txTopicTests_appclient_vehicle_ear.addAsModule(txTopicTests_appclient_vehicle_client);



        return txTopicTests_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void commitAckMsgTopicTest() throws java.lang.Exception {
            super.commitAckMsgTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void rollbackRecoverTopicTest() throws java.lang.Exception {
            super.rollbackRecoverTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void redeliveredFlagTxTopicTest() throws java.lang.Exception {
            super.redeliveredFlagTxTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void transactionRollbackOnSessionCloseReceiveTopicTest() throws java.lang.Exception {
            super.transactionRollbackOnSessionCloseReceiveTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void transactionRollbackOnPublishTopicTest() throws java.lang.Exception {
            super.transactionRollbackOnPublishTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void transactionRollbackOnRecTopicTest() throws java.lang.Exception {
            super.transactionRollbackOnRecTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void txRollbackOnConnectionCloseReceiveTopicTest() throws java.lang.Exception {
            super.txRollbackOnConnectionCloseReceiveTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void commitRollbackMultiMsgsTest() throws java.lang.Exception {
            super.commitRollbackMultiMsgsTest();
        }


}