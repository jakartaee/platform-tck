package com.sun.ts.tests.jms.core20.jmsproducerqueuetests;

import com.sun.ts.tests.jms.core20.jmsproducerqueuetests.Client;
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
@Tag("jms")
@Tag("platform")
@Tag("jms_web")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientAppclientTest extends com.sun.ts.tests.jms.core20.jmsproducerqueuetests.Client {
    static final String VEHICLE_ARCHIVE = "jmsproducerqueuetests_appclient_vehicle";

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
        Client:

        /com/sun/ts/tests/jms/core20/jmsproducerqueuetests/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jmsproducerqueuetests_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jmsproducerqueuetests_appclient_vehicle_client.jar");
            // The class files
            jmsproducerqueuetests_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.jms.core20.jmsproducerqueuetests.Client.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              jmsproducerqueuetests_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("jmsproducerqueuetests_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jmsproducerqueuetests_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jmsproducerqueuetests_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jmsproducerqueuetests_appclient_vehicle_client, Client.class, resURL);

        // Ear
            EnterpriseArchive jmsproducerqueuetests_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "jmsproducerqueuetests_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jmsproducerqueuetests_appclient_vehicle_ear.addAsModule(jmsproducerqueuetests_appclient_vehicle_client);



        return jmsproducerqueuetests_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendAndRecvTest1() throws java.lang.Exception {
            super.sendAndRecvTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendAndRecvTest2() throws java.lang.Exception {
            super.sendAndRecvTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendAndRecvTest3() throws java.lang.Exception {
            super.sendAndRecvTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendAndRecvTest4() throws java.lang.Exception {
            super.sendAndRecvTest4();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendAndRecvTest5() throws java.lang.Exception {
            super.sendAndRecvTest5();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendAndRecvMsgsOfEachMsgTypeTest() throws java.lang.Exception {
            super.sendAndRecvMsgsOfEachMsgTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void setGetDeliveryModeTest() throws java.lang.Exception {
            super.setGetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void setGetDeliveryDelayTest() throws java.lang.Exception {
            super.setGetDeliveryDelayTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void setGetDisableMessageIDTest() throws java.lang.Exception {
            super.setGetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void setGetDisableMessageTimestampTest() throws java.lang.Exception {
            super.setGetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void setGetPriorityTest() throws java.lang.Exception {
            super.setGetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void setGetTimeToLiveTest() throws java.lang.Exception {
            super.setGetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void deliveryDelayTest() throws java.lang.Exception {
            super.deliveryDelayTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrMessageIDTest() throws java.lang.Exception {
            super.msgHdrMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrTimeStampTest() throws java.lang.Exception {
            super.msgHdrTimeStampTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrJMSPriorityTest() throws java.lang.Exception {
            super.msgHdrJMSPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrJMSExpirationTest() throws java.lang.Exception {
            super.msgHdrJMSExpirationTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrJMSDeliveryModeTest() throws java.lang.Exception {
            super.msgHdrJMSDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgHdrJMSDeliveryTimeTest() throws java.lang.Exception {
            super.msgHdrJMSDeliveryTimeTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void setGetAllPropertyTypesTest() throws java.lang.Exception {
            super.setGetAllPropertyTypesTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void setGetAllHeaderTypesTest() throws java.lang.Exception {
            super.setGetAllHeaderTypesTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgPropertiesTest() throws java.lang.Exception {
            super.msgPropertiesTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgPropertiesConversionTests() throws java.lang.Exception {
            super.msgPropertiesConversionTests();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgPropertyExistTest() throws java.lang.Exception {
            super.msgPropertyExistTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void msgJMSXPropertiesTest() throws java.lang.Exception {
            super.msgJMSXPropertiesTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void setPropertyExceptionTests() throws java.lang.Exception {
            super.setPropertyExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void sendExceptionTests() throws java.lang.Exception {
            super.sendExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void getBodyTests() throws java.lang.Exception {
            super.getBodyTests();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void getBodyExceptionTests() throws java.lang.Exception {
            super.getBodyExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void isBodyAssignableToTest() throws java.lang.Exception {
            super.isBodyAssignableToTest();
        }


}