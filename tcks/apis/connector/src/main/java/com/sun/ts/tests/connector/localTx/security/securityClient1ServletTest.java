package com.sun.ts.tests.connector.localTx.security;

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
public class securityClient1ServletTest extends com.sun.ts.tests.connector.localTx.security.securityClient1 {
    static final String VEHICLE_ARCHIVE = "localTx_security_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        localTx_security_ejb_vehicle: 
        localTx_security_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        localTx_security_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        localTx_security_jsp_vehicle: 
        localTx_security_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        localTx_security_servlet_vehicle: 
        localTx_security_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/connector/localTx/security/servlet_vehicle_web.xml
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
            WebArchive localTx_security_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "localTx_security_servlet_vehicle_web.war");
            // The class files
            localTx_security_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.connector.localTx.security.securityClient1.class,
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
            URL warResURL = securityClient1.class.getResource("localTx_security_servlet_vehicle_servlet.xml");
            if(warResURL != null) {
              localTx_security_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = securityClient1.class.getResource("localTx_security_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              localTx_security_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

           // Call the archive processor
           archiveProcessor.processWebArchive(localTx_security_servlet_vehicle_web, securityClient1.class, warResURL);

        // Ear
            EnterpriseArchive localTx_security_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "localTx_security_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            localTx_security_servlet_vehicle_ear.addAsModule(localTx_security_servlet_vehicle_web);



            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(localTx_security_servlet_vehicle_ear, securityClient1.class, earResURL);
        return localTx_security_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testContainerManaged() throws java.lang.Exception {
            super.testContainerManaged();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testComponentManaged() throws java.lang.Exception {
            super.testComponentManaged();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAppEISSignon() throws java.lang.Exception {
            super.testAppEISSignon();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testCBTestCPCandNullPrin() throws java.lang.Exception {
            super.testCBTestCPCandNullPrin();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testCBTestCPCandGPC() throws java.lang.Exception {
            super.testCBTestCPCandGPC();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testCBTestAllCallbacksAndPrin() throws java.lang.Exception {
            super.testCBTestAllCallbacksAndPrin();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testCBTestCPCandPrin() throws java.lang.Exception {
            super.testCBTestCPCandPrin();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testCBTestAllCallbacksNullPrin() throws java.lang.Exception {
            super.testCBTestAllCallbacksNullPrin();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testCBTestCPCandPVC() throws java.lang.Exception {
            super.testCBTestCPCandPVC();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testCBTestGPCandCPCFail() throws java.lang.Exception {
            super.testCBTestGPCandCPCFail();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testCBTestEISCPCandPrin() throws java.lang.Exception {
            super.testCBTestEISCPCandPrin();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testConnManagerAllocateConnection() throws java.lang.Exception {
            super.testConnManagerAllocateConnection();
        }


}