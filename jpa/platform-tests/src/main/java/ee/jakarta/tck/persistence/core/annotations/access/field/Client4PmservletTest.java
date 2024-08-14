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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("jpa")
@Tag("javaee")
@Tag("javaee_web_profile")

public class Client4PmservletTest extends ee.jakarta.tck.persistence.core.annotations.access.field.Client4 {
    static final String VEHICLE_ARCHIVE = "jpa_core_annotations_access_field_pmservlet_vehicle";

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
        War:

        /com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jpa_core_annotations_access_field_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_annotations_access_field_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_annotations_access_field_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            ee.jakarta.tck.persistence.core.annotations.access.field.Client4.class
            );
            // The web.xml descriptor
            URL warResURL = Client4.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_annotations_access_field_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client4.class.getResource("//com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_annotations_access_field_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
           archiveProcessor.processWebArchive(jpa_core_annotations_access_field_pmservlet_vehicle_web, Client4.class, warResURL);

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
            archiveProcessor.processParArchive(jpa_core_annotations_access_field, Client4.class, parURL);
            // The orm.xml file
            parURL = Client4.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_annotations_access_field.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_annotations_access_field_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_annotations_access_field_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_annotations_access_field_vehicles_ear.addAsModule(jpa_core_annotations_access_field_pmservlet_vehicle_web);

            jpa_core_annotations_access_field_vehicles_ear.addAsLibrary(jpa_core_annotations_access_field);



            // The application.xml descriptor
            URL earResURL = Client4.class.getResource("/com/sun/ts/tests/jpa/core/annotations/access/field/");
            if(earResURL != null) {
              jpa_core_annotations_access_field_vehicles_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client4.class.getResource("/com/sun/ts/tests/jpa/core/annotations/access/field/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_annotations_access_field_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            archiveProcessor.processEarArchive(jpa_core_annotations_access_field_vehicles_ear, Client4.class, earResURL);
        return jpa_core_annotations_access_field_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void testExtractDateYear() throws java.lang.Exception {
            super.testExtractDateYear();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void testExtractDateQuarter() throws java.lang.Exception {
            super.testExtractDateQuarter();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void testExtractDateMonth() throws java.lang.Exception {
            super.testExtractDateMonth();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void testExtractDateDay() throws java.lang.Exception {
            super.testExtractDateDay();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void testExtractTimeHour() throws java.lang.Exception {
            super.testExtractTimeHour();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void testExtractTimeMinute() throws java.lang.Exception {
            super.testExtractTimeMinute();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void testExtractTimeSecond() throws java.lang.Exception {
            super.testExtractTimeSecond();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void testExtractDateTimeYear() throws java.lang.Exception {
            super.testExtractDateTimeYear();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void testExtractDateTimeQuarter() throws java.lang.Exception {
            super.testExtractDateTimeQuarter();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void testExtractDateTimeMonth() throws java.lang.Exception {
            super.testExtractDateTimeMonth();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void testExtractDateTimeDay() throws java.lang.Exception {
            super.testExtractDateTimeDay();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void testExtractDateTimeHour() throws java.lang.Exception {
            super.testExtractDateTimeHour();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void testExtractDateTimeMinute() throws java.lang.Exception {
            super.testExtractDateTimeMinute();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void testExtractDateTimeSecond() throws java.lang.Exception {
            super.testExtractDateTimeSecond();
        }


}