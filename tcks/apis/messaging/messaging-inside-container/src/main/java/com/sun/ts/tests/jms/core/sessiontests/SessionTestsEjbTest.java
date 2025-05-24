package com.sun.ts.tests.jms.core.sessiontests;

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
public class SessionTestsEjbTest extends com.sun.ts.tests.jms.core.sessiontests.SessionTests {
    static final String VEHICLE_ARCHIVE = "sessiontests_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        sessiontests_appclient_vehicle: 
        sessiontests_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        sessiontests_ejb_vehicle: 
        sessiontests_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        sessiontests_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml,META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        sessiontests_jsp_vehicle: 
        sessiontests_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml,WEB-INF/web.xml,war.sun-web.xml
        sessiontests_servlet_vehicle: 
        sessiontests_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml,WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core/sessiontests/ejb_vehicle_ejb.xml
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
            JavaArchive sessiontests_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "sessiontests_ejb_vehicle_client.jar");
            // The class files
            sessiontests_ejb_vehicle_client.addClasses(
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
            com.sun.ts.tests.jms.core.sessiontests.SessionTests.class
            );
            // The application-client.xml descriptor
            URL resURL = SessionTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              sessiontests_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = SessionTests.class.getResource("sessiontests_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              sessiontests_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            //sessiontests_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + SessionTests.class.getName() + "\n"), "MANIFEST.MF");
            sessiontests_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(sessiontests_ejb_vehicle_client, SessionTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive sessiontests_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "sessiontests_ejb_vehicle_ejb.jar");
            // The class files
            sessiontests_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                Fault.class,
                EETest.class,
                ServiceEETest.class,
                com.sun.ts.tests.jms.core.sessiontests.SessionTests.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = SessionTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              sessiontests_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = SessionTests.class.getResource("sessiontests_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              sessiontests_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(sessiontests_ejb_vehicle_ejb, SessionTests.class, ejbResURL);

        // Ear
            EnterpriseArchive sessiontests_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "sessiontests_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            sessiontests_ejb_vehicle_ear.addAsModule(sessiontests_ejb_vehicle_ejb);
            sessiontests_ejb_vehicle_ear.addAsModule(sessiontests_ejb_vehicle_client);



        return sessiontests_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void SimpleSendAndReceiveQ() throws java.lang.Exception {
            super.SimpleSendAndReceiveQ();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void SimpleSendAndReceiveT() throws java.lang.Exception {
            super.SimpleSendAndReceiveT();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void selectorAndBrowserTests() throws java.lang.Exception {
            super.selectorAndBrowserTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void SubscriberTests() throws java.lang.Exception {
            super.SubscriberTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void IllegalStateTestQ() throws java.lang.Exception {
            super.IllegalStateTestQ();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void ackTests() throws java.lang.Exception {
            super.ackTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void InvalidDestinationTests() throws java.lang.Exception {
            super.InvalidDestinationTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void InvalidSelectorTests() throws java.lang.Exception {
            super.InvalidSelectorTests();
        }


}