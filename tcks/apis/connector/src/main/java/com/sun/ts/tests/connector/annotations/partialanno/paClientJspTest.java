package com.sun.ts.tests.connector.annotations.partialanno;

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
public class paClientJspTest extends com.sun.ts.tests.connector.annotations.partialanno.paClient {
    static final String VEHICLE_ARCHIVE = "partialanno_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        partialanno_ejb_vehicle: META-INF/jboss-deployment-structure.xml
        partialanno_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        partialanno_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        partialanno_jsp_vehicle: META-INF/jboss-deployment-structure.xml
        partialanno_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        partialanno_servlet_vehicle: META-INF/jboss-deployment-structure.xml
        partialanno_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/connector/annotations/partialanno/partialanno_jsp_vehicle_web.xml
        /com/sun/ts/tests/connector/annotations/partialanno/jsp_vehicle_web.xml
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
            WebArchive partialanno_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "partialanno_jsp_vehicle_web.war");
            // The class files
            partialanno_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.connector.annotations.partialanno.paClient.class,
            com.sun.ts.tests.connector.util.DBSupport.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = paClient.class.getResource("jsp_vehicle_web.xml");
            if(warResURL != null) {
              partialanno_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = paClient.class.getResource("partialanno_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              partialanno_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = paClient.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              partialanno_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = paClient.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              partialanno_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(partialanno_jsp_vehicle_web, paClient.class, warResURL);


        // Ear
            EnterpriseArchive partialanno_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "partialanno_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            partialanno_jsp_vehicle_ear.addAsModule(partialanno_jsp_vehicle_web);

            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = paClient.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              partialanno_jsp_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(partialanno_jsp_vehicle_ear, paClient.class, earResURL);
        return partialanno_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testMixedModeConfigPropertyMCF() throws java.lang.Exception {
            super.testMixedModeConfigPropertyMCF();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testNoDefaultVallAnnoElement() throws java.lang.Exception {
            super.testNoDefaultVallAnnoElement();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testMixedModeConfigPropertyRA() throws java.lang.Exception {
            super.testMixedModeConfigPropertyRA();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testMixedModeConfigPropertyOverride() throws java.lang.Exception {
            super.testMixedModeConfigPropertyOverride();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testMixedModeConfigPropertyNoOverride() throws java.lang.Exception {
            super.testMixedModeConfigPropertyNoOverride();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testDDOverridesAnno() throws java.lang.Exception {
            super.testDDOverridesAnno();
        }


}