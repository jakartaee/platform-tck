package com.sun.ts.tests.connector.annotations.anno;

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
public class annotationClientJspTest extends com.sun.ts.tests.connector.annotations.anno.annotationClient {
    static final String VEHICLE_ARCHIVE = "annotations_jsp_vehicle";

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

        /com/sun/ts/tests/connector/annotations/anno/annotations_jsp_vehicle_web.xml
        /com/sun/ts/tests/connector/annotations/anno/jsp_vehicle_web.xml
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
            WebArchive annotations_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "annotations_jsp_vehicle_web.war");
            // The class files
            annotations_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.connector.annotations.anno.annotationClient.class,
            com.sun.ts.tests.connector.util.DBSupport.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = annotationClient.class.getResource("annotations_jsp_vehicle_web.xml");
            if(warResURL != null) {
              annotations_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = annotationClient.class.getResource("annotations_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              annotations_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            warResURL = annotationClient.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              annotations_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = annotationClient.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              annotations_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(annotations_jsp_vehicle_web, annotationClient.class, warResURL);



        // Ear
            EnterpriseArchive annotations_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "annotations_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            annotations_jsp_vehicle_ear.addAsModule(annotations_jsp_vehicle_web);


            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = annotationClient.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              annotations_jsp_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(annotations_jsp_vehicle_ear, annotationClient.class, earResURL);
        return annotations_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testConnectorAnnotation() throws java.lang.Exception {
            super.testConnectorAnnotation();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testSetterMethodConfigPropAnno() throws java.lang.Exception {
            super.testSetterMethodConfigPropAnno();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testConfigPropertyAnnotation() throws java.lang.Exception {
            super.testConfigPropertyAnnotation();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testRAAccessibility() throws java.lang.Exception {
            super.testRAAccessibility();
        }


}