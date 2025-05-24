package com.sun.ts.tests.connector.noTx.workmgt;

import java.net.URL;

import com.sun.ts.lib.harness.Fault;
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
@Tag("connector")
@Tag("platform")
@Tag("connector_standalone")
@Tag("connector_web")
@Tag("web_optional")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class workmgtClient1ServletTest extends com.sun.ts.tests.connector.noTx.workmgt.workmgtClient1 {
    static final String VEHICLE_ARCHIVE = "workmgt_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        workmgt_ejb_vehicle: 
        workmgt_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        workmgt_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        workmgt_jsp_vehicle: 
        workmgt_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        workmgt_servlet_vehicle: 
        workmgt_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/connector/noTx/workmgt/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Rar:

        /com/sun/ts/tests/common/connector/whitebox/mdcomplete/ra-md-complete.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive workmgt_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "workmgt_servlet_vehicle_web.war");
            // The class files
            workmgt_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.connector.noTx.workmgt.workmgtClient1.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.connector.util.DBSupport.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = workmgtClient1.class.getResource("workmgt_servlet_vehicle_servlet.xml");
            if(warResURL != null) {
              workmgt_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = workmgtClient1.class.getResource("workmgt_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              workmgt_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

           // Call the archive processor
           archiveProcessor.processWebArchive(workmgt_servlet_vehicle_web, workmgtClient1.class, warResURL);

        // Ear
            EnterpriseArchive workmgt_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "workmgt_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            workmgt_servlet_vehicle_ear.addAsModule(workmgt_servlet_vehicle_web);

            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(workmgt_servlet_vehicle_ear, workmgtClient1.class, earResURL);
        return workmgt_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testWorkManagerImplementaion() throws java.lang.Exception {
            super.testWorkManagerImplementaion();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testWorkListenerImplementation() throws java.lang.Exception {
            super.testWorkListenerImplementation();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testScheduleWork() throws java.lang.Exception {
            super.testScheduleWork();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testScheduleWorkListener() throws java.lang.Exception {
            super.testScheduleWorkListener();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testWorkCompletedException() throws java.lang.Exception {
            super.testWorkCompletedException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testForUnsharedTimer() throws java.lang.Exception {
            super.testForUnsharedTimer();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testNestedWork() throws java.lang.Exception {
            super.testNestedWork();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testUnknownWorkDuration() throws java.lang.Exception {
            super.testUnknownWorkDuration();
        }


}