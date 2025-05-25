package com.sun.ts.tests.jms.core20.jmsproducerqueuetests;

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
public class ClientServletTest extends com.sun.ts.tests.jms.core20.jmsproducerqueuetests.Client {
    static final String VEHICLE_ARCHIVE = "jmsproducerqueuetests_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jmsproducerqueuetests_appclient_vehicle: 
        jmsproducerqueuetests_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        jmsproducerqueuetests_ejb_vehicle: 
        jmsproducerqueuetests_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        jmsproducerqueuetests_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        jmsproducerqueuetests_jsp_vehicle: 
        jmsproducerqueuetests_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        jmsproducerqueuetests_servlet_vehicle: 
        jmsproducerqueuetests_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core20/jmsproducerqueuetests/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jmsproducerqueuetests_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jmsproducerqueuetests_servlet_vehicle_web.war");
            // The class files
            jmsproducerqueuetests_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.jms.core20.jmsproducerqueuetests.Client.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/jms/core20/jmsproducerqueuetests/servlet_vehicle_web.xml");
            if(warResURL != null) {
              jmsproducerqueuetests_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/jms/core20/jmsproducerqueuetests/jmsproducerqueuetests_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jmsproducerqueuetests_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }


           // Call the archive processor
           archiveProcessor.processWebArchive(jmsproducerqueuetests_servlet_vehicle_web, Client.class, warResURL);

        // Ear
            EnterpriseArchive jmsproducerqueuetests_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "jmsproducerqueuetests_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jmsproducerqueuetests_servlet_vehicle_ear.addAsModule(jmsproducerqueuetests_servlet_vehicle_web);



        return jmsproducerqueuetests_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void sendAndRecvTest1() throws java.lang.Exception {
            super.sendAndRecvTest1();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void sendAndRecvTest2() throws java.lang.Exception {
            super.sendAndRecvTest2();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void sendAndRecvTest3() throws java.lang.Exception {
            super.sendAndRecvTest3();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void sendAndRecvTest4() throws java.lang.Exception {
            super.sendAndRecvTest4();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void sendAndRecvTest5() throws java.lang.Exception {
            super.sendAndRecvTest5();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void sendAndRecvMsgsOfEachMsgTypeTest() throws java.lang.Exception {
            super.sendAndRecvMsgsOfEachMsgTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void setGetDeliveryModeTest() throws java.lang.Exception {
            super.setGetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void setGetDeliveryDelayTest() throws java.lang.Exception {
            super.setGetDeliveryDelayTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void setGetDisableMessageIDTest() throws java.lang.Exception {
            super.setGetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void setGetDisableMessageTimestampTest() throws java.lang.Exception {
            super.setGetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void setGetPriorityTest() throws java.lang.Exception {
            super.setGetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void setGetTimeToLiveTest() throws java.lang.Exception {
            super.setGetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void deliveryDelayTest() throws java.lang.Exception {
            super.deliveryDelayTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrMessageIDTest() throws java.lang.Exception {
            super.msgHdrMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrTimeStampTest() throws java.lang.Exception {
            super.msgHdrTimeStampTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrJMSPriorityTest() throws java.lang.Exception {
            super.msgHdrJMSPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrJMSExpirationTest() throws java.lang.Exception {
            super.msgHdrJMSExpirationTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrJMSDeliveryModeTest() throws java.lang.Exception {
            super.msgHdrJMSDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgHdrJMSDeliveryTimeTest() throws java.lang.Exception {
            super.msgHdrJMSDeliveryTimeTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void setGetAllPropertyTypesTest() throws java.lang.Exception {
            super.setGetAllPropertyTypesTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void setGetAllHeaderTypesTest() throws java.lang.Exception {
            super.setGetAllHeaderTypesTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgPropertiesTest() throws java.lang.Exception {
            super.msgPropertiesTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgPropertiesConversionTests() throws java.lang.Exception {
            super.msgPropertiesConversionTests();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgPropertyExistTest() throws java.lang.Exception {
            super.msgPropertyExistTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgJMSXPropertiesTest() throws java.lang.Exception {
            super.msgJMSXPropertiesTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void setPropertyExceptionTests() throws java.lang.Exception {
            super.setPropertyExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void sendExceptionTests() throws java.lang.Exception {
            super.sendExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void getBodyTests() throws java.lang.Exception {
            super.getBodyTests();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void getBodyExceptionTests() throws java.lang.Exception {
            super.getBodyExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void isBodyAssignableToTest() throws java.lang.Exception {
            super.isBodyAssignableToTest();
        }


}