package com.sun.ts.tests.connector.noTx.lifecycle;

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
public class lifecycleClient1EjbTest extends com.sun.ts.tests.connector.noTx.lifecycle.lifecycleClient1 {
    static final String VEHICLE_ARCHIVE = "lifecycle_ejb_vehicle";

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
        Client:

        /com/sun/ts/tests/connector/noTx/lifecycle/lifecycle_ejb_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/connector/noTx/lifecycle/lifecycle_ejb_vehicle_ejb.xml
        /com/sun/ts/tests/connector/noTx/lifecycle/ejb_vehicle_ejb.xml
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
            JavaArchive lifecycle_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "lifecycle_ejb_vehicle_client.jar");
            // The class files
            lifecycle_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.connector.noTx.lifecycle.lifecycleClient1.class,
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
            URL resURL = lifecycleClient1.class.getResource("lifecycle_ejb_vehicle_client.xml");
            lifecycle_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = lifecycleClient1.class.getResource("lifecycle_ejb_vehicle_client.jar.sun-application-client.xml");
            lifecycle_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            lifecycle_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(lifecycle_ejb_vehicle_client, lifecycleClient1.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive lifecycle_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "lifecycle_ejb_vehicle_ejb.jar");
            // The class files
            lifecycle_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.tests.connector.noTx.lifecycle.lifecycleClient1.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.connector.util.DBSupport.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = lifecycleClient1.class.getResource("lifecycle_ejb_vehicle_ejb.xml");
            lifecycle_ejb_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            // The sun-ejb-jar.xml file
            ejbResURL1 = lifecycleClient1.class.getResource("lifecycle_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            lifecycle_ejb_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            // Call the archive processor
            archiveProcessor.processEjbArchive(lifecycle_ejb_vehicle_ejb, lifecycleClient1.class, ejbResURL1);


        // Ear
            EnterpriseArchive lifecycle_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "lifecycle_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            lifecycle_ejb_vehicle_ear.addAsModule(lifecycle_ejb_vehicle_ejb);
            lifecycle_ejb_vehicle_ear.addAsModule(lifecycle_ejb_vehicle_client);


            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(lifecycle_ejb_vehicle_ear, lifecycleClient1.class, earResURL);
        return lifecycle_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAssociationMCFandRA() throws java.lang.Exception {
            super.testAssociationMCFandRA();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAssociationMCFandRA2() throws java.lang.Exception {
            super.testAssociationMCFandRA2();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testBootstrapforNull() throws java.lang.Exception {
            super.testBootstrapforNull();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testInstantiationOfRA() throws java.lang.Exception {
            super.testInstantiationOfRA();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testRASharability() throws java.lang.Exception {
            super.testRASharability();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testMCFcalledOnce() throws java.lang.Exception {
            super.testMCFcalledOnce();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testRAforJavaBean() throws java.lang.Exception {
            super.testRAforJavaBean();
        }


}