package com.sun.ts.tests.connector.noTx.security;

import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
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
@Tag("connector")
@Tag("platform")
@Tag("connector_standalone")
@Tag("connector_web")
@Tag("web_optional")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class securityClient1JspTest extends com.sun.ts.tests.connector.noTx.security.securityClient1 {
    static final String VEHICLE_ARCHIVE = "security_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        security_ejb_vehicle: 
        security_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        security_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        security_jsp_vehicle: 
        security_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        security_servlet_vehicle: 
        security_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/connector/noTx/security/security_jsp_vehicle_web.xml
        /com/sun/ts/tests/connector/noTx/security/jsp_vehicle_web.xml
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
            WebArchive security_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "security_jsp_vehicle_web.war");
            // The class files
            security_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.connector.noTx.security.securityClient1.class,
            com.sun.ts.tests.connector.util.DBSupport.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = securityClient1.class.getResource("security_jsp_vehicle_web.xml");
            if(warResURL != null) {
              security_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = securityClient1.class.getResource("security_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              security_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = securityClient1.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              security_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = securityClient1.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              security_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(security_jsp_vehicle_web, securityClient1.class, warResURL);



        // Ear
            EnterpriseArchive security_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "security_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            security_jsp_vehicle_ear.addAsModule(security_jsp_vehicle_web);



            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(security_jsp_vehicle_ear, securityClient1.class, earResURL);
        return security_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testContainerManaged() throws java.lang.Exception {
            super.testContainerManaged();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testComponentManaged() throws java.lang.Exception {
            super.testComponentManaged();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testAppEISSignon() throws java.lang.Exception {
            super.testAppEISSignon();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testConnManagerAllocateConnection() throws java.lang.Exception {
            super.testConnManagerAllocateConnection();
        }


}