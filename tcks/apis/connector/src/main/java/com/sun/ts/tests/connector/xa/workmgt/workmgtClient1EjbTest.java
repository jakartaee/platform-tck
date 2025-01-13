package com.sun.ts.tests.connector.xa.workmgt;

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
public class workmgtClient1EjbTest extends com.sun.ts.tests.connector.xa.workmgt.workmgtClient1 {
    static final String VEHICLE_ARCHIVE = "xa_workmgt_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        xa_workmgt_ejb_vehicle: 
        xa_workmgt_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        xa_workmgt_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        xa_workmgt_jsp_vehicle: 
        xa_workmgt_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        xa_workmgt_servlet_vehicle: 
        xa_workmgt_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/connector/xa/workmgt/xa_workmgt_ejb_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/connector/xa/workmgt/xa_workmgt_ejb_vehicle_ejb.xml
        /com/sun/ts/tests/connector/xa/workmgt/ejb_vehicle_ejb.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.jar.sun-ejb-jar.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.xml
        Rar:

        /com/sun/ts/tests/common/connector/whitebox/mdcomplete/ra-md-complete.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive xa_workmgt_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "xa_workmgt_ejb_vehicle_client.jar");
            // The class files
            xa_workmgt_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.connector.xa.workmgt.workmgtClient1.class,
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
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = workmgtClient1.class.getResource("xa_workmgt_ejb_vehicle_client.xml");
            if(resURL != null) {
              xa_workmgt_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = workmgtClient1.class.getResource("xa_workmgt_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              xa_workmgt_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            xa_workmgt_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(xa_workmgt_ejb_vehicle_client, workmgtClient1.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive xa_workmgt_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "xa_workmgt_ejb_vehicle_ejb.jar");
            // The class files
            xa_workmgt_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.connector.xa.workmgt.workmgtClient1.class,
                com.sun.ts.tests.connector.util.DBSupport.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = workmgtClient1.class.getResource("xa_workmgt_ejb_vehicle_ejb.xml");
            if(ejbResURL1 != null) {
              xa_workmgt_ejb_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = workmgtClient1.class.getResource("xa_workmgt_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              xa_workmgt_ejb_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(xa_workmgt_ejb_vehicle_ejb, workmgtClient1.class, ejbResURL1);


        // Ear
            EnterpriseArchive xa_workmgt_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "xa_workmgt_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            xa_workmgt_ejb_vehicle_ear.addAsModule(xa_workmgt_ejb_vehicle_ejb);
            xa_workmgt_ejb_vehicle_ear.addAsModule(xa_workmgt_ejb_vehicle_client);


            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(xa_workmgt_ejb_vehicle_ear, workmgtClient1.class, earResURL);
        return xa_workmgt_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testWorkManagerImplementaion() throws java.lang.Exception {
            super.testWorkManagerImplementaion();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testWorkListenerImplementation() throws java.lang.Exception {
            super.testWorkListenerImplementation();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testScheduleWork() throws java.lang.Exception {
            super.testScheduleWork();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testScheduleWorkListener() throws java.lang.Exception {
            super.testScheduleWorkListener();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testWorkCompletedException() throws java.lang.Exception {
            super.testWorkCompletedException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testForUnsharedTimer() throws java.lang.Exception {
            super.testForUnsharedTimer();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testNestedWork() throws java.lang.Exception {
            super.testNestedWork();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testUnknownWorkDuration() throws java.lang.Exception {
            super.testUnknownWorkDuration();
        }


}