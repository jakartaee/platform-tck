package com.sun.ts.tests.jms.core.appclient.txqueuetests;

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
public class TxQueueTestsAppclientTest extends com.sun.ts.tests.jms.core.appclient.txqueuetests.TxQueueTests {
    static final String VEHICLE_ARCHIVE = "txqueuetests_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        txqueuetests_appclient_vehicle: 
        txqueuetests_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/core/appclient/txqueuetests/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive txqueuetests_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "txqueuetests_appclient_vehicle_client.jar");
            // The class files
            txqueuetests_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.jms.core.appclient.txqueuetests.TxQueueTests.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = TxQueueTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              txqueuetests_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = TxQueueTests.class.getResource("txqueuetests_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              txqueuetests_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            txqueuetests_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + TxQueueTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(txqueuetests_appclient_vehicle_client, TxQueueTests.class, resURL);

        // Ear
            EnterpriseArchive txqueuetests_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "txqueuetests_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            txqueuetests_appclient_vehicle_ear.addAsModule(txqueuetests_appclient_vehicle_client);



        return txqueuetests_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void simpleSendReceiveTxQueueTest() throws java.lang.Exception {
            super.simpleSendReceiveTxQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void commitAckMsgQueueTest() throws java.lang.Exception {
            super.commitAckMsgQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void rollbackRecoverQueueTest() throws java.lang.Exception {
            super.rollbackRecoverQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void redeliveredFlagTxQueueTest() throws java.lang.Exception {
            super.redeliveredFlagTxQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void transactionRollbackOnSessionCloseRecQTest() throws java.lang.Exception {
            super.transactionRollbackOnSessionCloseRecQTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void transactionRollbackOnSendQTest() throws java.lang.Exception {
            super.transactionRollbackOnSendQTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void transactionRollbackOnRecQTest() throws java.lang.Exception {
            super.transactionRollbackOnRecQTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void txRollbackOnConnectionCloseRecQTest() throws java.lang.Exception {
            super.txRollbackOnConnectionCloseRecQTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void txCloseRolledBackSessionRecQTest() throws java.lang.Exception {
            super.txCloseRolledBackSessionRecQTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void txMultiQTest() throws java.lang.Exception {
            super.txMultiQTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void commitRollbackMultiMsgsTest() throws java.lang.Exception {
            super.commitRollbackMultiMsgsTest();
        }


}