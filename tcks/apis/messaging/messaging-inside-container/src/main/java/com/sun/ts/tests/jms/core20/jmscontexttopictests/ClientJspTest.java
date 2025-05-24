package com.sun.ts.tests.jms.core20.jmscontexttopictests;

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
public class ClientJspTest extends com.sun.ts.tests.jms.core20.jmscontexttopictests.Client {
    static final String VEHICLE_ARCHIVE = "jmscontexttopictests_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        jmscontexttopictests_appclient_vehicle: 
        jmscontexttopictests_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        jmscontexttopictests_ejb_vehicle: 
        jmscontexttopictests_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        jmscontexttopictests_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        jmscontexttopictests_jsp_vehicle: 
        jmscontexttopictests_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        jmscontexttopictests_servlet_vehicle: 
        jmscontexttopictests_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core20/jmscontexttopictests/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jmscontexttopictests_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "jmscontexttopictests_jsp_vehicle_web.war");
            // The class files
            jmscontexttopictests_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            EETest.class,
            com.sun.ts.tests.jms.core20.jmscontexttopictests.Client.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/jms/core20/jmscontexttopictests/jsp_vehicle_web.xml");
            if(warResURL != null) {
              jmscontexttopictests_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/jms/core20/jmscontexttopictests/jmscontexttopictests_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jmscontexttopictests_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              jmscontexttopictests_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              jmscontexttopictests_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jmscontexttopictests_jsp_vehicle_web, Client.class, warResURL);

        // Ear
            EnterpriseArchive jmscontexttopictests_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "jmscontexttopictests_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jmscontexttopictests_jsp_vehicle_ear.addAsModule(jmscontexttopictests_jsp_vehicle_web);



        return jmscontexttopictests_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createTemporayTopicTest() throws java.lang.Exception {
            super.createTemporayTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void getMetaDataTest() throws java.lang.Exception {
            super.getMetaDataTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void getSessionModeTest() throws java.lang.Exception {
            super.getSessionModeTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void getTransactedTest() throws java.lang.Exception {
            super.getTransactedTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void setGetAutoStartTest() throws java.lang.Exception {
            super.setGetAutoStartTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createConsumerTest() throws java.lang.Exception {
            super.createConsumerTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createDurableConsumerTest1() throws java.lang.Exception {
            super.createDurableConsumerTest1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createDurableConsumerTest2() throws java.lang.Exception {
            super.createDurableConsumerTest2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createSharedDurableConsumerTest1() throws java.lang.Exception {
            super.createSharedDurableConsumerTest1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createSharedDurableConsumerTest2() throws java.lang.Exception {
            super.createSharedDurableConsumerTest2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createSharedDurableConsumerTest3() throws java.lang.Exception {
            super.createSharedDurableConsumerTest3();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createSharedConsumerTest1() throws java.lang.Exception {
            super.createSharedConsumerTest1();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void createSharedConsumerTest2() throws java.lang.Exception {
            super.createSharedConsumerTest2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void multipleCloseContextTest() throws java.lang.Exception {
            super.multipleCloseContextTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void simpleDurableConsumerTest() throws java.lang.Exception {
            super.simpleDurableConsumerTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void inactiveDurableConsumerTopicRecTest() throws java.lang.Exception {
            super.inactiveDurableConsumerTopicRecTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void durableConsumerTopicNoLocalTest() throws java.lang.Exception {
            super.durableConsumerTopicNoLocalTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void durableConsumerChangeSelectorTest() throws java.lang.Exception {
            super.durableConsumerChangeSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void durableConsumerChangeSelectorTest2() throws java.lang.Exception {
            super.durableConsumerChangeSelectorTest2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void verifyClientIDOnAdminConfiguredIDTest() throws java.lang.Exception {
            super.verifyClientIDOnAdminConfiguredIDTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void invalidDestinationRuntimeExceptionTests() throws java.lang.Exception {
            super.invalidDestinationRuntimeExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void invalidSelectorRuntimeExceptionTests() throws java.lang.Exception {
            super.invalidSelectorRuntimeExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void jMSRuntimeExceptionTests() throws java.lang.Exception {
            super.jMSRuntimeExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void illegalStateRuntimeExceptionTest() throws java.lang.Exception {
            super.illegalStateRuntimeExceptionTest();
        }


}