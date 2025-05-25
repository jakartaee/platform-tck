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
public class ClientEjbTest extends com.sun.ts.tests.jms.core20.jmscontexttopictests.Client {
    static final String VEHICLE_ARCHIVE = "jmscontexttopictests_ejb_vehicle";

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
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core20/jmscontexttopictests/ejb_vehicle_ejb.xml
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
            JavaArchive jmscontexttopictests_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jmscontexttopictests_ejb_vehicle_client.jar");
            // The class files
            jmscontexttopictests_ejb_vehicle_client.addClasses(
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
            com.sun.ts.tests.jms.core20.jmscontexttopictests.Client.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              jmscontexttopictests_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("jmscontexttopictests_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jmscontexttopictests_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            // jmscontexttopictests_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            jmscontexttopictests_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jmscontexttopictests_ejb_vehicle_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive jmscontexttopictests_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jmscontexttopictests_ejb_vehicle_ejb.jar");
            // The class files
            jmscontexttopictests_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                Fault.class,
                EETest.class,
                com.sun.ts.tests.jms.core20.jmscontexttopictests.Client.class,
                ServiceEETest.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              jmscontexttopictests_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("jmscontexttopictests_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              jmscontexttopictests_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jmscontexttopictests_ejb_vehicle_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive jmscontexttopictests_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "jmscontexttopictests_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jmscontexttopictests_ejb_vehicle_ear.addAsModule(jmscontexttopictests_ejb_vehicle_ejb);
            jmscontexttopictests_ejb_vehicle_ear.addAsModule(jmscontexttopictests_ejb_vehicle_client);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jmscontexttopictests_ejb_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jmscontexttopictests_ejb_vehicle_ear, Client.class, earResURL);
        return jmscontexttopictests_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void createTemporayTopicTest() throws java.lang.Exception {
            super.createTemporayTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void getMetaDataTest() throws java.lang.Exception {
            super.getMetaDataTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void getSessionModeTest() throws java.lang.Exception {
            super.getSessionModeTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void getTransactedTest() throws java.lang.Exception {
            super.getTransactedTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void setGetAutoStartTest() throws java.lang.Exception {
            super.setGetAutoStartTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void createConsumerTest() throws java.lang.Exception {
            super.createConsumerTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void createDurableConsumerTest1() throws java.lang.Exception {
            super.createDurableConsumerTest1();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void createDurableConsumerTest2() throws java.lang.Exception {
            super.createDurableConsumerTest2();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void createSharedDurableConsumerTest1() throws java.lang.Exception {
            super.createSharedDurableConsumerTest1();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void createSharedDurableConsumerTest2() throws java.lang.Exception {
            super.createSharedDurableConsumerTest2();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void createSharedDurableConsumerTest3() throws java.lang.Exception {
            super.createSharedDurableConsumerTest3();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void createSharedConsumerTest1() throws java.lang.Exception {
            super.createSharedConsumerTest1();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void createSharedConsumerTest2() throws java.lang.Exception {
            super.createSharedConsumerTest2();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void multipleCloseContextTest() throws java.lang.Exception {
            super.multipleCloseContextTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void simpleDurableConsumerTest() throws java.lang.Exception {
            super.simpleDurableConsumerTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void inactiveDurableConsumerTopicRecTest() throws java.lang.Exception {
            super.inactiveDurableConsumerTopicRecTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void durableConsumerTopicNoLocalTest() throws java.lang.Exception {
            super.durableConsumerTopicNoLocalTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void durableConsumerChangeSelectorTest() throws java.lang.Exception {
            super.durableConsumerChangeSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void durableConsumerChangeSelectorTest2() throws java.lang.Exception {
            super.durableConsumerChangeSelectorTest2();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void verifyClientIDOnAdminConfiguredIDTest() throws java.lang.Exception {
            super.verifyClientIDOnAdminConfiguredIDTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void invalidDestinationRuntimeExceptionTests() throws java.lang.Exception {
            super.invalidDestinationRuntimeExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void invalidSelectorRuntimeExceptionTests() throws java.lang.Exception {
            super.invalidSelectorRuntimeExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void jMSRuntimeExceptionTests() throws java.lang.Exception {
            super.jMSRuntimeExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void illegalStateRuntimeExceptionTest() throws java.lang.Exception {
            super.illegalStateRuntimeExceptionTest();
        }


}