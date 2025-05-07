package com.sun.ts.tests.connector.annotations.partialanno;

import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
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
@Tag("connector")
@Tag("platform")
@Tag("connector_standalone")
@Tag("connector_web")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class paClientEjbTest extends com.sun.ts.tests.connector.annotations.partialanno.paClient {
    static final String VEHICLE_ARCHIVE = "partialanno_ejb_vehicle";

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
        Client:

        /com/sun/ts/tests/connector/annotations/partialanno/partialanno_ejb_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/connector/annotations/partialanno/partialanno_ejb_vehicle_ejb.xml
        /com/sun/ts/tests/connector/annotations/partialanno/ejb_vehicle_ejb.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.jar.sun-ejb-jar.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.xml
        Rar:

        /com/sun/ts/tests/common/connector/whitebox/partialanno/ra-md-complete.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive partialanno_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "partialanno_ejb_vehicle_client.jar");
            // The class files
            partialanno_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
            com.sun.ts.tests.connector.util.DBSupport.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.connector.annotations.partialanno.paClient.class
            );
            // The application-client.xml descriptor
            URL resURL = paClient.class.getResource("partialanno_ejb_vehicle_client.xml");
            partialanno_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = paClient.class.getResource("partialanno_ejb_vehicle_client.jar.sun-application-client.xml");
            partialanno_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            partialanno_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(partialanno_ejb_vehicle_client, paClient.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive partialanno_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "partialanno_ejb_vehicle_ejb.jar");
            // The class files
            partialanno_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.connector.annotations.partialanno.paClient.class,
                com.sun.ts.tests.connector.util.DBSupport.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = paClient.class.getResource("partialanno_ejb_vehicle_ejb.xml");
            partialanno_ejb_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            // The sun-ejb-jar.xml file
            ejbResURL1 = paClient.class.getResource("partialanno_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            partialanno_ejb_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            // Call the archive processor
            archiveProcessor.processEjbArchive(partialanno_ejb_vehicle_ejb, paClient.class, ejbResURL1);


        // Ear
            EnterpriseArchive partialanno_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "partialanno_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            partialanno_ejb_vehicle_ear.addAsModule(partialanno_ejb_vehicle_ejb);
            partialanno_ejb_vehicle_ear.addAsModule(partialanno_ejb_vehicle_client);

            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(partialanno_ejb_vehicle_ear, paClient.class, earResURL);
        return partialanno_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testMixedModeConfigPropertyMCF() throws java.lang.Exception {
            super.testMixedModeConfigPropertyMCF();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testNoDefaultVallAnnoElement() throws java.lang.Exception {
            super.testNoDefaultVallAnnoElement();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testMixedModeConfigPropertyRA() throws java.lang.Exception {
            super.testMixedModeConfigPropertyRA();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testMixedModeConfigPropertyOverride() throws java.lang.Exception {
            super.testMixedModeConfigPropertyOverride();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testMixedModeConfigPropertyNoOverride() throws java.lang.Exception {
            super.testMixedModeConfigPropertyNoOverride();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testDDOverridesAnno() throws java.lang.Exception {
            super.testDDOverridesAnno();
        }


}