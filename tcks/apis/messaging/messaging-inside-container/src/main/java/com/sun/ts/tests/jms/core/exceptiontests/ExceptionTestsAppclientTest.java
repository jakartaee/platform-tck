package com.sun.ts.tests.jms.core.exceptiontests;

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
public class ExceptionTestsAppclientTest extends com.sun.ts.tests.jms.core.exceptiontests.ExceptionTests {
    static final String VEHICLE_ARCHIVE = "exception_appclient_vehicle";

        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive exceptiontests_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "exceptiontests_appclient_vehicle_client.jar");
            // The class files
            exceptiontests_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.core.exceptiontests.ExceptionTests.class,
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
            URL resURL = ExceptionTests.class.getResource("/com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml");
            if(resURL != null) {
                exceptiontests_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            // resURL = ExceptionTests.class.getResource("exceptiontests_appclient_vehicle_client.jar.sun-application-client.xml");
            // if(resURL != null) {
            //     exceptiontests_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            // }
            exceptiontests_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ExceptionTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(exceptiontests_appclient_vehicle_client, ExceptionTests.class, resURL);

        // Ear
            EnterpriseArchive exceptiontests_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "exceptiontests_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            exceptiontests_appclient_vehicle_ear.addAsModule(exceptiontests_appclient_vehicle_client);



        return exceptiontests_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void transactionRolledBackExceptionTest1() throws java.lang.Exception {
            super.transactionRolledBackExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void transactionRolledBackExceptionTest2() throws java.lang.Exception {
            super.transactionRolledBackExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void testTransactionInProgressException1() throws java.lang.Exception {
            super.testTransactionInProgressException1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void testTransactionInProgressException2() throws java.lang.Exception {
            super.testTransactionInProgressException2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void testResourceAllocationException1() throws java.lang.Exception {
            super.testResourceAllocationException1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void resourceAllocationExceptionTest2() throws java.lang.Exception {
            super.resourceAllocationExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void testMessageNotWriteableException1() throws java.lang.Exception {
            super.testMessageNotWriteableException1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void testMessageNotWriteableException2() throws java.lang.Exception {
            super.testMessageNotWriteableException2();
        }


        @Test
        @Override
        @TargetVehicle("appclient")
        public void testMessageNotReadableException1() throws java.lang.Exception {
            super.testMessageNotReadableException1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void testMessageNotReadableException2() throws java.lang.Exception {
            super.testMessageNotReadableException2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void testMessageFormatException1() throws java.lang.Exception {
            super.testMessageFormatException1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void testMessageFormatException2() throws java.lang.Exception {
            super.testMessageFormatException2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void testMessageEOFException1() throws java.lang.Exception {
            super.testMessageEOFException1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void testMessageEOFException2() throws java.lang.Exception {
            super.testMessageEOFException2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void testJMSSecurityException1() throws java.lang.Exception {
            super.testMessageEOFException1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void testJMSSecurityException2() throws java.lang.Exception {
            super.testJMSSecurityException2();
        }


        @Test
        @Override
        @TargetVehicle("appclient")
        public void testJMSException1() throws java.lang.Exception {
            super.testJMSException1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void testJMSException2() throws java.lang.Exception {
            super.testJMSException2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void invalidSelectorExceptionTest1() throws java.lang.Exception {
            super.invalidSelectorExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void invalidSelectorExceptionTest2() throws java.lang.Exception {
            super.invalidSelectorExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void invalidDestinationExceptionTest1() throws java.lang.Exception {
            super.invalidDestinationExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void invalidDestinationExceptionTest2() throws java.lang.Exception {
            super.invalidDestinationExceptionTest2();
        }


        @Test
        @Override
        @TargetVehicle("appclient")
        public void invalidClientIDExceptionTest1() throws java.lang.Exception {
            super.invalidClientIDExceptionTest1();
        }


        @Test
        @Override
        @TargetVehicle("appclient")
        public void invalidClientIDExceptionTest2() throws java.lang.Exception {
            super.invalidClientIDExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void illegalStateExceptionTest1() throws java.lang.Exception {
            super.illegalStateExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void illegalStateExceptionTest2() throws java.lang.Exception {
            super.illegalStateExceptionTest2();
        }


}