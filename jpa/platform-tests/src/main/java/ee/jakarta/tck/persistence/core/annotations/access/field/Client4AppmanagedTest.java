package ee.jakarta.tck.persistence.core.annotations.access.field;

import ee.jakarta.tck.persistence.core.annotations.access.field.Client4;
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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;
import com.sun.ts.lib.harness.Status;
import java.util.Properties;


@ExtendWith(ArquillianExtension.class)
@Tag("persistence")
@Tag("platform")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class Client4AppmanagedTest extends ee.jakarta.tck.persistence.core.annotations.access.field.Client4 {
    static final String VEHICLE_ARCHIVE = "jpa_core_annotations_access_field_appmanaged_vehicle";

    public static void main(String[] args) {
        Client4AppmanagedTest theTests = new Client4AppmanagedTest();
        Status s = theTests.run(args, System.out, System.err);
        s.exit();
      }
  
      public void setup(String[] args, Properties p) throws Exception {
          super.setup(args, p);
      }
  
        /**
        EE10 Deployment Descriptors:
        jpa_core_annotations_access_field: META-INF/persistence.xml
        jpa_core_annotations_access_field_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_access_field_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_access_field_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_access_field_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_access_field_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_access_field_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_access_field_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_access_field_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_access_field_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_access_field_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_access_field_vehicles: 

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_client.xml
        Ejb:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_core_annotations_access_field_appmanaged_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_core_annotations_access_field_vehicles_client.jar");
            // The class files
            jpa_core_annotations_access_field_appmanaged_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            ee.jakarta.tck.persistence.core.annotations.access.field.DataTypes.class,
            ee.jakarta.tck.persistence.core.annotations.access.field.DataTypes2.class,
            ee.jakarta.tck.persistence.core.annotations.access.field.Client.class,
            ee.jakarta.tck.persistence.core.annotations.access.field.Client4.class,
            Client4AppmanagedTest.class
            );
            // The application-client.xml descriptor
            URL resURL = Client4.class.getResource("/com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_client.xml");
            if(resURL != null) {
              jpa_core_annotations_access_field_appmanaged_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client4.class.getResource("//com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_core_annotations_access_field_appmanaged_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jpa_core_annotations_access_field_appmanaged_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client4.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_core_annotations_access_field_appmanaged_vehicle_client, Client4.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_core_annotations_access_field_appmanaged_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_core_annotations_access_field_appmanaged_vehicle_ejb.jar");
            // The class files
            jpa_core_annotations_access_field_appmanaged_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ee.jakarta.tck.persistence.common.PMClientBase.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleBean.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
                ee.jakarta.tck.persistence.core.annotations.access.field.Client4.class,
                ee.jakarta.tck.persistence.core.annotations.access.field.Client.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client4.class.getResource("//com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_client.xml");
            if(ejbResURL1 != null) {
              jpa_core_annotations_access_field_appmanaged_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client4.class.getResource("//com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_core_annotations_access_field_appmanaged_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_core_annotations_access_field_appmanaged_vehicle_ejb, Client4.class, ejbResURL1);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_annotations_access_field = ShrinkWrap.create(JavaArchive.class, "jpa_core_annotations_access_field.jar");
            // The class files
            jpa_core_annotations_access_field.addClasses(
                ee.jakarta.tck.persistence.core.annotations.access.field.DataTypes.class,
                ee.jakarta.tck.persistence.core.annotations.access.field.DataTypes2.class,
                ee.jakarta.tck.persistence.core.types.common.Grade.class
            );
            // The persistence.xml descriptor
            URL parURL = Client4.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_annotations_access_field.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client4.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_annotations_access_field.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client4.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_annotations_access_field.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client4.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_annotations_access_field.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_annotations_access_field, Client4.class, parURL);
            parURL = Client4.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_annotations_access_field.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_annotations_access_field_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_annotations_access_field_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_annotations_access_field_vehicles_ear.addAsModule(jpa_core_annotations_access_field_appmanaged_vehicle_ejb);
            jpa_core_annotations_access_field_vehicles_ear.addAsModule(jpa_core_annotations_access_field_appmanaged_vehicle_client);

            jpa_core_annotations_access_field_vehicles_ear.addAsLibrary(jpa_core_annotations_access_field);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client4.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_annotations_access_field_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_annotations_access_field_vehicles_ear, Client4.class, earResURL);
        return jpa_core_annotations_access_field_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void testExtractDateYear() throws java.lang.Exception {
            super.testExtractDateYear();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void testExtractDateQuarter() throws java.lang.Exception {
            super.testExtractDateQuarter();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void testExtractDateMonth() throws java.lang.Exception {
            super.testExtractDateMonth();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void testExtractDateDay() throws java.lang.Exception {
            super.testExtractDateDay();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void testExtractTimeHour() throws java.lang.Exception {
            super.testExtractTimeHour();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void testExtractTimeMinute() throws java.lang.Exception {
            super.testExtractTimeMinute();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void testExtractTimeSecond() throws java.lang.Exception {
            super.testExtractTimeSecond();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void testExtractDateTimeYear() throws java.lang.Exception {
            super.testExtractDateTimeYear();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void testExtractDateTimeQuarter() throws java.lang.Exception {
            super.testExtractDateTimeQuarter();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void testExtractDateTimeMonth() throws java.lang.Exception {
            super.testExtractDateTimeMonth();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void testExtractDateTimeDay() throws java.lang.Exception {
            super.testExtractDateTimeDay();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void testExtractDateTimeHour() throws java.lang.Exception {
            super.testExtractDateTimeHour();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void testExtractDateTimeMinute() throws java.lang.Exception {
            super.testExtractDateTimeMinute();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void testExtractDateTimeSecond() throws java.lang.Exception {
            super.testExtractDateTimeSecond();
        }

}
