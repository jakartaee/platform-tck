package com.sun.ts.tests.jms.core.exceptionTopic;

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
public class ExceptionTopicTestsEjbTest extends com.sun.ts.tests.jms.core.exceptionTopic.ExceptionTopicTests {
    static final String VEHICLE_ARCHIVE = "exceptionTopic_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        exceptionTopic_appclient_vehicle: 
        exceptionTopic_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        exceptionTopic_ejb_vehicle: 
        exceptionTopic_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        exceptionTopic_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        exceptionTopic_jsp_vehicle: 
        exceptionTopic_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        exceptionTopic_servlet_vehicle: 
        exceptionTopic_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core/exceptionTopic/ejb_vehicle_ejb.xml
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
            JavaArchive exceptionTopic_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "exceptionTopic_ejb_vehicle_client.jar");
            // The class files
            exceptionTopic_ejb_vehicle_client.addClasses(
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
            com.sun.ts.tests.jms.core.exceptionTopic.ExceptionTopicTests.class
            );
            // The application-client.xml descriptor
            URL resURL = ExceptionTopicTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              exceptionTopic_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = ExceptionTopicTests.class.getResource("exceptionTopic_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              exceptionTopic_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            //exceptionTopic_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + ExceptionTopicTests.class.getName() + "\n"), "MANIFEST.MF");
            exceptionTopic_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(exceptionTopic_ejb_vehicle_client, ExceptionTopicTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive exceptionTopic_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "exceptionTopic_ejb_vehicle_ejb.jar");
            // The class files
            exceptionTopic_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.jms.core.exceptionTopic.ExceptionTopicTests.class,
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
            URL ejbResURL = ExceptionTopicTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              exceptionTopic_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = ExceptionTopicTests.class.getResource("exceptionTopic_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              exceptionTopic_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(exceptionTopic_ejb_vehicle_ejb, ExceptionTopicTests.class, ejbResURL);

        // Ear
            EnterpriseArchive exceptionTopic_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "exceptionTopic_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            exceptionTopic_ejb_vehicle_ear.addAsModule(exceptionTopic_ejb_vehicle_ejb);
            exceptionTopic_ejb_vehicle_ear.addAsModule(exceptionTopic_ejb_vehicle_client);



        return exceptionTopic_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xInvalidDestinationExceptionTopicTest() throws java.lang.Exception {
            super.xInvalidDestinationExceptionTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageNotReadableExceptionTopicTest() throws java.lang.Exception {
            super.xMessageNotReadableExceptionTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageNotWriteableExceptionTTestforTextMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionTTestforTextMessage();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageNotWriteableExceptionTestforBytesMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionTestforBytesMessage();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageNotWriteableExceptionTTestforStreamMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionTTestforStreamMessage();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageNotWriteableExceptionTTestforMapMessage() throws java.lang.Exception {
            super.xMessageNotWriteableExceptionTTestforMapMessage();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xNullPointerExceptionTopicTest() throws java.lang.Exception {
            super.xNullPointerExceptionTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageEOFExceptionTTestforBytesMessage() throws java.lang.Exception {
            super.xMessageEOFExceptionTTestforBytesMessage();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageEOFExceptionTTestforStreamMessage() throws java.lang.Exception {
            super.xMessageEOFExceptionTTestforStreamMessage();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageFormatExceptionTTestforBytesMessage() throws java.lang.Exception {
            super.xMessageFormatExceptionTTestforBytesMessage();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageFormatExceptionTTestforStreamMessage() throws java.lang.Exception {
            super.xMessageFormatExceptionTTestforStreamMessage();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xInvalidSelectorExceptionTopicTest() throws java.lang.Exception {
            super.xInvalidSelectorExceptionTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xIllegalStateExceptionTopicTest() throws java.lang.Exception {
            super.xIllegalStateExceptionTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xUnsupportedOperationExceptionTTest1() throws java.lang.Exception {
            super.xUnsupportedOperationExceptionTTest1();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xInvalidDestinationExceptionTTests() throws java.lang.Exception {
            super.xInvalidDestinationExceptionTTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageNotReadableExceptionTBytesMsgTest() throws java.lang.Exception {
            super.xMessageNotReadableExceptionTBytesMsgTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xMessageNotReadableExceptionTStreamMsgTest() throws java.lang.Exception {
            super.xMessageNotReadableExceptionTStreamMsgTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void xIllegalStateExceptionTestQueueMethodsT() throws java.lang.Exception {
            super.xIllegalStateExceptionTestQueueMethodsT();
        }


}