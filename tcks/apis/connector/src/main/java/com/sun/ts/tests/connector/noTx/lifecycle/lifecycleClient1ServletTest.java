package com.sun.ts.tests.connector.noTx.lifecycle;

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
public class lifecycleClient1ServletTest extends com.sun.ts.tests.connector.noTx.lifecycle.lifecycleClient1 {
    static final String VEHICLE_ARCHIVE = "lifecycle_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        lifecycle_ejb_vehicle: 
        lifecycle_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        lifecycle_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        lifecycle_jsp_vehicle: 
        lifecycle_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        lifecycle_servlet_vehicle: 
        lifecycle_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/connector/noTx/lifecycle/servlet_vehicle_web.xml
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
            WebArchive lifecycle_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "lifecycle_servlet_vehicle_web.war");
            // The class files
            lifecycle_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.connector.noTx.lifecycle.lifecycleClient1.class,
            Fault.class,
            com.sun.ts.tests.connector.util.DBSupport.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = lifecycleClient1.class.getResource("lifecycle_servlet_vehicle_servlet.xml");
            lifecycle_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            // The sun-web.xml descriptor
            warResURL = lifecycleClient1.class.getResource("lifecycle_servlet_vehicle_web.war.sun-web.xml");
            lifecycle_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");

            // Any libraries added to the war

           // Call the archive processor
           archiveProcessor.processWebArchive(lifecycle_servlet_vehicle_web, lifecycleClient1.class, warResURL);

        // Ear
            EnterpriseArchive lifecycle_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "lifecycle_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            lifecycle_servlet_vehicle_ear.addAsModule(lifecycle_servlet_vehicle_web);

            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(lifecycle_servlet_vehicle_ear, lifecycleClient1.class, earResURL);
        return lifecycle_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAssociationMCFandRA() throws java.lang.Exception {
            super.testAssociationMCFandRA();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAssociationMCFandRA2() throws java.lang.Exception {
            super.testAssociationMCFandRA2();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testBootstrapforNull() throws java.lang.Exception {
            super.testBootstrapforNull();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testInstantiationOfRA() throws java.lang.Exception {
            super.testInstantiationOfRA();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testRASharability() throws java.lang.Exception {
            super.testRASharability();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testMCFcalledOnce() throws java.lang.Exception {
            super.testMCFcalledOnce();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testRAforJavaBean() throws java.lang.Exception {
            super.testRAforJavaBean();
        }


}