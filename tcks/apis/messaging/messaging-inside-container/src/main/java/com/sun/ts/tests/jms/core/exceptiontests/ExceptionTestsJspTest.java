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
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
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
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ExceptionTestsJspTest extends com.sun.ts.tests.jms.core.exceptiontests.ExceptionTests {
    static final String VEHICLE_ARCHIVE = "exceptiontests_jsp_vehicle";

        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive exceptiontests_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "exceptiontests_jsp_vehicle_web.war");
            // The class files
            exceptiontests_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.core.exceptiontests.ExceptionTests.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = ExceptionTests.class.getResource("jsp_vehicle_web.xml");
            if(warResURL != null) {
              exceptiontests_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // // The sun-web.xml descriptor
            // warResURL = ExceptionTests.class.getResource("/com/sun/ts/tests/jms/core/exceptiontests/exceptiontests_jsp_vehicle_web.war.sun-web.xml");
            // if(warResURL != null) {
            //   exceptiontests_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            // }

            warResURL = ExceptionTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              exceptiontests_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = ExceptionTests.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              exceptiontests_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(exceptiontests_jsp_vehicle_web, ExceptionTests.class, warResURL);

        // Ear
            EnterpriseArchive exceptiontests_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "exceptiontests_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            exceptiontests_jsp_vehicle_ear.addAsModule(exceptiontests_jsp_vehicle_web);



        return exceptiontests_jsp_vehicle_ear;
        }


        @Test
        @Override
        @TargetVehicle("jsp")
        public void transactionRolledBackExceptionTest1() throws java.lang.Exception {
            super.transactionRolledBackExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void transactionRolledBackExceptionTest2() throws java.lang.Exception {
            super.transactionRolledBackExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testTransactionInProgressException1() throws java.lang.Exception {
            super.testTransactionInProgressException1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testTransactionInProgressException2() throws java.lang.Exception {
            super.testTransactionInProgressException2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testResourceAllocationException1() throws java.lang.Exception {
            super.testResourceAllocationException1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void resourceAllocationExceptionTest2() throws java.lang.Exception {
            super.resourceAllocationExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testMessageNotWriteableException1() throws java.lang.Exception {
            super.testMessageNotWriteableException1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testMessageNotWriteableException2() throws java.lang.Exception {
            super.testMessageNotWriteableException2();
        }


        @Test
        @Override
        @TargetVehicle("jsp")
        public void testMessageNotReadableException1() throws java.lang.Exception {
            super.testMessageNotReadableException1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testMessageNotReadableException2() throws java.lang.Exception {
            super.testMessageNotReadableException2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testMessageFormatException1() throws java.lang.Exception {
            super.testMessageFormatException1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testMessageFormatException2() throws java.lang.Exception {
            super.testMessageFormatException2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testMessageEOFException1() throws java.lang.Exception {
            super.testMessageEOFException1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testMessageEOFException2() throws java.lang.Exception {
            super.testMessageEOFException2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testJMSSecurityException1() throws java.lang.Exception {
            super.testMessageEOFException1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testJMSSecurityException2() throws java.lang.Exception {
            super.testJMSSecurityException2();
        }


        @Test
        @Override
        @TargetVehicle("jsp")
        public void testJMSException1() throws java.lang.Exception {
            super.testJMSException1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testJMSException2() throws java.lang.Exception {
            super.testJMSException2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void invalidSelectorExceptionTest1() throws java.lang.Exception {
            super.invalidSelectorExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void invalidSelectorExceptionTest2() throws java.lang.Exception {
            super.invalidSelectorExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void invalidDestinationExceptionTest1() throws java.lang.Exception {
            super.invalidDestinationExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void invalidDestinationExceptionTest2() throws java.lang.Exception {
            super.invalidDestinationExceptionTest2();
        }


        @Test
        @Override
        @TargetVehicle("jsp")
        public void invalidClientIDExceptionTest1() throws java.lang.Exception {
            super.invalidClientIDExceptionTest1();
        }


        @Test
        @Override
        @TargetVehicle("jsp")
        public void invalidClientIDExceptionTest2() throws java.lang.Exception {
            super.invalidClientIDExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void illegalStateExceptionTest1() throws java.lang.Exception {
            super.illegalStateExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void illegalStateExceptionTest2() throws java.lang.Exception {
            super.illegalStateExceptionTest2();
        }



}