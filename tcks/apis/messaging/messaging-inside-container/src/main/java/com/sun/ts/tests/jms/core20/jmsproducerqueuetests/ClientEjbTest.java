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
public class ClientEjbTest extends com.sun.ts.tests.jms.core20.jmsproducerqueuetests.Client {
    static final String VEHICLE_ARCHIVE = "jmsproducerqueuetests_ejb_vehicle";

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

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core20/jmsproducerqueuetests/ejb_vehicle_ejb.xml
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
            JavaArchive jmsproducerqueuetests_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jmsproducerqueuetests_ejb_vehicle_client.jar");
            // The class files
            jmsproducerqueuetests_ejb_vehicle_client.addClasses(
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
            com.sun.ts.tests.jms.core20.jmsproducerqueuetests.Client.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              jmsproducerqueuetests_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("jmsproducerqueuetests_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jmsproducerqueuetests_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            // jmsproducerqueuetests_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            jmsproducerqueuetests_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jmsproducerqueuetests_ejb_vehicle_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive jmsproducerqueuetests_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jmsproducerqueuetests_ejb_vehicle_ejb.jar");
            // The class files
            jmsproducerqueuetests_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.jms.core20.jmsproducerqueuetests.Client.class,
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
            URL ejbResURL = Client.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              jmsproducerqueuetests_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("jmsproducerqueuetests_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              jmsproducerqueuetests_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jmsproducerqueuetests_ejb_vehicle_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive jmsproducerqueuetests_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "jmsproducerqueuetests_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jmsproducerqueuetests_ejb_vehicle_ear.addAsModule(jmsproducerqueuetests_ejb_vehicle_ejb);
            jmsproducerqueuetests_ejb_vehicle_ear.addAsModule(jmsproducerqueuetests_ejb_vehicle_client);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jmsproducerqueuetests_ejb_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jmsproducerqueuetests_ejb_vehicle_ear, Client.class, earResURL);
        return jmsproducerqueuetests_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void sendAndRecvTest1() throws java.lang.Exception {
            super.sendAndRecvTest1();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void sendAndRecvTest2() throws java.lang.Exception {
            super.sendAndRecvTest2();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void sendAndRecvTest3() throws java.lang.Exception {
            super.sendAndRecvTest3();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void sendAndRecvTest4() throws java.lang.Exception {
            super.sendAndRecvTest4();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void sendAndRecvTest5() throws java.lang.Exception {
            super.sendAndRecvTest5();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void sendAndRecvMsgsOfEachMsgTypeTest() throws java.lang.Exception {
            super.sendAndRecvMsgsOfEachMsgTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void setGetDeliveryModeTest() throws java.lang.Exception {
            super.setGetDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void setGetDeliveryDelayTest() throws java.lang.Exception {
            super.setGetDeliveryDelayTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void setGetDisableMessageIDTest() throws java.lang.Exception {
            super.setGetDisableMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void setGetDisableMessageTimestampTest() throws java.lang.Exception {
            super.setGetDisableMessageTimestampTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void setGetPriorityTest() throws java.lang.Exception {
            super.setGetPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void setGetTimeToLiveTest() throws java.lang.Exception {
            super.setGetTimeToLiveTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void deliveryDelayTest() throws java.lang.Exception {
            super.deliveryDelayTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgHdrMessageIDTest() throws java.lang.Exception {
            super.msgHdrMessageIDTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgHdrTimeStampTest() throws java.lang.Exception {
            super.msgHdrTimeStampTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgHdrJMSPriorityTest() throws java.lang.Exception {
            super.msgHdrJMSPriorityTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgHdrJMSExpirationTest() throws java.lang.Exception {
            super.msgHdrJMSExpirationTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgHdrJMSDeliveryModeTest() throws java.lang.Exception {
            super.msgHdrJMSDeliveryModeTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgHdrJMSDeliveryTimeTest() throws java.lang.Exception {
            super.msgHdrJMSDeliveryTimeTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void setGetAllPropertyTypesTest() throws java.lang.Exception {
            super.setGetAllPropertyTypesTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void setGetAllHeaderTypesTest() throws java.lang.Exception {
            super.setGetAllHeaderTypesTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgPropertiesTest() throws java.lang.Exception {
            super.msgPropertiesTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgPropertiesConversionTests() throws java.lang.Exception {
            super.msgPropertiesConversionTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgPropertyExistTest() throws java.lang.Exception {
            super.msgPropertyExistTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgJMSXPropertiesTest() throws java.lang.Exception {
            super.msgJMSXPropertiesTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void setPropertyExceptionTests() throws java.lang.Exception {
            super.setPropertyExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void sendExceptionTests() throws java.lang.Exception {
            super.sendExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void getBodyTests() throws java.lang.Exception {
            super.getBodyTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void getBodyExceptionTests() throws java.lang.Exception {
            super.getBodyExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void isBodyAssignableToTest() throws java.lang.Exception {
            super.isBodyAssignableToTest();
        }


}