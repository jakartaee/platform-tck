package ee.jakarta.tck.persistence.core.annotations.tableGenerator;

import ee.jakarta.tck.persistence.core.annotations.tableGenerator.Client2;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("jpa")
@Tag("javaee")
@Tag("javaee_web_profile")

public class Client2AppmanagednotxTest extends ee.jakarta.tck.persistence.core.annotations.tableGenerator.Client2 {
    static final String VEHICLE_ARCHIVE = "jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_annotations_tableGenerator: META-INF/persistence.xml
        jpa_core_annotations_tableGenerator_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_tableGenerator_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_tableGenerator_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_tableGenerator_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_tableGenerator_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_tableGenerator_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_tableGenerator_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_tableGenerator_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_tableGenerator_vehicles: 

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml
        Ejb:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_client.jar");
            // The class files
            jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The application-client.xml descriptor
            URL resURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml");
            if(resURL != null) {
              jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client2.class.getResource("//com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client2.class.getName() + "\n"), "MANIFEST.MF");
            archiveProcessor.processClientArchive(jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_client, Client2.class, resURL);


        // Ejb
            // the jar with the correct archive name
            JavaArchive jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_ejb.jar");
            // The class files
            jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ee.jakarta.tck.persistence.common.PMClientBase.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
                com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class,
                ee.jakarta.tck.persistence.core.annotations.tableGenerator.Client2.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client2.class.getResource("//vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_ejb.xml");
            if(ejbResURL != null) {
              jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client2.class.getResource("//vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            archiveProcessor.processEjbArchive(jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_ejb, Client2.class, ejbResURL);

        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_annotations_tableGenerator = ShrinkWrap.create(JavaArchive.class, "jpa_core_annotations_tableGenerator.jar");
            // The class files
            jpa_core_annotations_tableGenerator.addClasses(
                ee.jakarta.tck.persistence.core.annotations.tableGenerator.DataTypes2.class,
                ee.jakarta.tck.persistence.core.annotations.tableGenerator.DataTypes.class,
                ee.jakarta.tck.persistence.core.annotations.tableGenerator.DataTypes3.class,
                ee.jakarta.tck.persistence.core.annotations.tableGenerator.DataTypes4.class
            );
            // The persistence.xml descriptor
            URL parURL = Client2.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_annotations_tableGenerator.addAsManifestResource(parURL, "persistence.xml");
            }
            archiveProcessor.processParArchive(jpa_core_annotations_tableGenerator, Client2.class, parURL);
            // The orm.xml file
            parURL = Client2.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_annotations_tableGenerator.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_annotations_tableGenerator_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_annotations_tableGenerator_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_annotations_tableGenerator_vehicles_ear.addAsModule(jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_ejb);
            jpa_core_annotations_tableGenerator_vehicles_ear.addAsModule(jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_client);

            jpa_core_annotations_tableGenerator_vehicles_ear.addAsLibrary(jpa_core_annotations_tableGenerator);



            // The application.xml descriptor
            URL earResURL = Client2.class.getResource("/com/sun/ts/tests/jpa/core/annotations/tableGenerator/");
            if(earResURL != null) {
              jpa_core_annotations_tableGenerator_vehicles_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client2.class.getResource("/com/sun/ts/tests/jpa/core/annotations/tableGenerator/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_annotations_tableGenerator_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            archiveProcessor.processEarArchive(jpa_core_annotations_tableGenerator_vehicles_ear, Client2.class, earResURL);
        return jpa_core_annotations_tableGenerator_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void generatorOnPropertyTest() throws java.lang.Exception {
            super.generatorOnPropertyTest();
        }


}