package com.sun.ts.tests.jms.core.exceptionQueue;

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
public class ExceptionQueueTestsEjbTest extends com.sun.ts.tests.jms.core.exceptionQueue.ExceptionQueueTests {
    static final String VEHICLE_ARCHIVE = "exceptionQueue_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        exceptionQueue_appclient_vehicle: 
        exceptionQueue_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        exceptionQueue_ejb_vehicle: 
        exceptionQueue_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        exceptionQueue_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        exceptionQueue_jsp_vehicle: 
        exceptionQueue_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        exceptionQueue_servlet_vehicle: 
        exceptionQueue_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core/exceptionQueue/ejb_vehicle_ejb.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.jar.sun-ejb-jar.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive exceptionQueue_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "exceptionQueue_ejb_vehicle_client.jar");
            // The class files
            exceptionQueue_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.core.exceptionQueue.ExceptionQueueTests.class
            );
            // The application-client.xml descriptor
            URL resURL = ExceptionQueueTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              exceptionQueue_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = ExceptionQueueTests.class.getResource("exceptionQueue_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              exceptionQueue_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            //exceptionQueue_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ExceptionQueueTests.class.getName() + "\n"), "MANIFEST.MF");
            exceptionQueue_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(exceptionQueue_ejb_vehicle_client, ExceptionQueueTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive exceptionQueue_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "exceptionQueue_ejb_vehicle_ejb.jar");
            // The class files
            exceptionQueue_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.jms.core.exceptionQueue.ExceptionQueueTests.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                Fault.class,
                EETest.class,
                ServiceEETest.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = ExceptionQueueTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              exceptionQueue_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = ExceptionQueueTests.class.getResource("exceptionQueue_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              exceptionQueue_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(exceptionQueue_ejb_vehicle_ejb, ExceptionQueueTests.class, ejbResURL);

        // Ear
            EnterpriseArchive exceptionQueue_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "exceptionQueue_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            exceptionQueue_ejb_vehicle_ear.addAsModule(exceptionQueue_ejb_vehicle_ejb);
            exceptionQueue_ejb_vehicle_ear.addAsModule(exceptionQueue_ejb_vehicle_client);



        return exceptionQueue_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xInvalidDestinationExceptionQTest() throws java.lang.Exception {
            super.xInvalidDestinationExceptionQTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageNotReadableExceptionQueueTest() throws java.lang.Exception {
            super.xMessageNotReadableExceptionQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageNotWriteableExceptionQTestforTextMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionQTestforTextMessage();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageNotWriteableExceptionQTestforBytesMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionQTestforBytesMessage();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageNotWriteableExceptionQTestforStreamMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionQTestforStreamMessage();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageNotWriteableExceptionQTestforMapMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionQTestforMapMessage();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xNullPointerExceptionQueueTest() throws java.lang.Exception {
            super.xNullPointerExceptionQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageEOFExceptionQTestforBytesMessage() throws java.lang.Exception {
            super.xMessageEOFExceptionQTestforBytesMessage();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageEOFExceptionQTestforStreamMessage() throws java.lang.Exception {
            super.xMessageEOFExceptionQTestforStreamMessage();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageFormatExceptionQTestforBytesMessage() throws java.lang.Exception {
            super.xMessageFormatExceptionQTestforBytesMessage();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageFormatExceptionQTestforStreamMessage() throws java.lang.Exception {
            super.xMessageFormatExceptionQTestforStreamMessage();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xInvalidSelectorExceptionQueueTest() throws java.lang.Exception {
            super.xInvalidSelectorExceptionQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xIllegalStateExceptionQueueTest() throws java.lang.Exception {
            super.xIllegalStateExceptionQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xUnsupportedOperationExceptionQTest1() throws java.lang.Exception {
            super.xUnsupportedOperationExceptionQTest1();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xUnsupportedOperationExceptionQTest2() throws java.lang.Exception {
            super.xUnsupportedOperationExceptionQTest2();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xUnsupportedOperationExceptionQTest3() throws java.lang.Exception {
            super.xUnsupportedOperationExceptionQTest3();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xInvalidDestinationExceptionQTests() throws java.lang.Exception {
            super.xInvalidDestinationExceptionQTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageNotReadableExceptionQBytesMsgTest() throws java.lang.Exception {
            super.xMessageNotReadableExceptionQBytesMsgTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageNotReadableExceptionQStreamMsgTest() throws java.lang.Exception {
            super.xMessageNotReadableExceptionQStreamMsgTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xIllegalStateExceptionTestTopicMethodsQ() throws java.lang.Exception {
            super.xIllegalStateExceptionTestTopicMethodsQ();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xIllegalStateExceptionTestRollbackQ() throws java.lang.Exception {
            super.xIllegalStateExceptionTestRollbackQ();
        }


}