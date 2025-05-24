package com.sun.ts.tests.connector.localTx.workcontext;

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
public class WorkContextClientJspTest extends com.sun.ts.tests.connector.localTx.workcontext.WorkContextClient {
    static final String VEHICLE_ARCHIVE = "workcontext_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        workcontext_ejb_vehicle: 
        workcontext_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        workcontext_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        workcontext_jsp_vehicle: 
        workcontext_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        workcontext_servlet_vehicle: 
        workcontext_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/connector/localTx/workcontext/workcontext_jsp_vehicle_web.xml
        /com/sun/ts/tests/connector/localTx/workcontext/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
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
            WebArchive workcontext_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "workcontext_jsp_vehicle_web.war");
            // The class files
            workcontext_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.connector.localTx.workcontext.WorkContextClient.class,
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
            URL warResURL = WorkContextClient.class.getResource("workcontext_jsp_vehicle_web.xml");
            if(warResURL != null) {
              workcontext_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = WorkContextClient.class.getResource("workcontext_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              workcontext_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = WorkContextClient.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              workcontext_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = WorkContextClient.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              workcontext_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(workcontext_jsp_vehicle_web, WorkContextClient.class, warResURL);
        // Ear
            EnterpriseArchive workcontext_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "workcontext_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            workcontext_jsp_vehicle_ear.addAsModule(workcontext_jsp_vehicle_web);


            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(workcontext_jsp_vehicle_ear, WorkContextClient.class, earResURL);
        return workcontext_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testIsContextSupported() throws java.lang.Exception {
            super.testIsContextSupported();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testSecurityContextExecSubject() throws java.lang.Exception {
            super.testSecurityContextExecSubject();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testSecurityContextServiceSubject() throws java.lang.Exception {
            super.testSecurityContextServiceSubject();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testSecurityContextCBH() throws java.lang.Exception {
            super.testSecurityContextCBH();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testWorkContextErrorCode() throws java.lang.Exception {
            super.testWorkContextErrorCode();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testWorkContextErrorCode2() throws java.lang.Exception {
            super.testWorkContextErrorCode2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testNestedWorkContexts() throws java.lang.Exception {
            super.testNestedWorkContexts();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testNestedWorkContexts2() throws java.lang.Exception {
            super.testNestedWorkContexts2();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testNestedWorkContexts3() throws java.lang.Exception {
            super.testNestedWorkContexts3();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testWorkContextProvider() throws java.lang.Exception {
            super.testWorkContextProvider();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testWorkContextNotifications() throws java.lang.Exception {
            super.testWorkContextNotifications();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testHICNotifications() throws java.lang.Exception {
            super.testHICNotifications();
        }


}