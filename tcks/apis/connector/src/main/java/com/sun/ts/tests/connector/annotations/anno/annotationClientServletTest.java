package com.sun.ts.tests.connector.annotations.anno;

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
public class annotationClientServletTest extends com.sun.ts.tests.connector.annotations.anno.annotationClient {
    static final String VEHICLE_ARCHIVE = "annotations_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        annotations_ejb_vehicle: META-INF/jboss-deployment-structure.xml
        annotations_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        annotations_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        annotations_jsp_vehicle: META-INF/jboss-deployment-structure.xml
        annotations_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        annotations_servlet_vehicle: META-INF/jboss-deployment-structure.xml
        annotations_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/connector/annotations/anno/servlet_vehicle_web.xml
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
            WebArchive annotations_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "annotations_servlet_vehicle_web.war");

            // The class files
            annotations_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.connector.annotations.anno.annotationClient.class,
            com.sun.ts.tests.connector.util.DBSupport.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = annotationClient.class.getResource("annotations_servlet_vehicle_servlet.xml");
            if(warResURL != null) {
              annotations_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = annotationClient.class.getResource("annotations_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              annotations_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(annotations_servlet_vehicle_web, annotationClient.class, warResURL);



        // Ear
            EnterpriseArchive annotations_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "annotations_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            annotations_servlet_vehicle_ear.addAsModule(annotations_servlet_vehicle_web);


            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(annotations_servlet_vehicle_ear, annotationClient.class, earResURL);
        return annotations_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testConnectorAnnotation() throws java.lang.Exception {
            super.testConnectorAnnotation();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testSetterMethodConfigPropAnno() throws java.lang.Exception {
            super.testSetterMethodConfigPropAnno();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testConfigPropertyAnnotation() throws java.lang.Exception {
            super.testConfigPropertyAnnotation();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testRAAccessibility() throws java.lang.Exception {
            super.testRAAccessibility();
        }


}