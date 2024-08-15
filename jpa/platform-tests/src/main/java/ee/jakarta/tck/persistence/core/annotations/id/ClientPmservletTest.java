package ee.jakarta.tck.persistence.core.annotations.id;

import ee.jakarta.tck.persistence.core.annotations.id.Client;
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
@Tag("persistence")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")

public class ClientPmservletTest extends ee.jakarta.tck.persistence.core.annotations.id.Client {
    static final String VEHICLE_ARCHIVE = "jpa_core_annotations_basic_pmservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_annotations_basic: META-INF/persistence.xml,META-INF/persistence.xml
        jpa_core_annotations_basic_appmanaged_vehicle_client: META-INF/application-client.xml,META-INF/application-client.xml
        jpa_core_annotations_basic_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml,jar.sun-ejb-jar.xml
        jpa_core_annotations_basic_appmanagedNoTx_vehicle_client: META-INF/application-client.xml,META-INF/application-client.xml
        jpa_core_annotations_basic_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml,jar.sun-ejb-jar.xml
        jpa_core_annotations_basic_pmservlet_vehicle_web: WEB-INF/web.xml,WEB-INF/web.xml
        jpa_core_annotations_basic_puservlet_vehicle_web: WEB-INF/web.xml,WEB-INF/web.xml
        jpa_core_annotations_basic_stateful3_vehicle_client: META-INF/application-client.xml,META-INF/application-client.xml
        jpa_core_annotations_basic_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml,jar.sun-ejb-jar.xml
        jpa_core_annotations_basic_stateless3_vehicle_client: META-INF/application-client.xml,META-INF/application-client.xml
        jpa_core_annotations_basic_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml,jar.sun-ejb-jar.xml
        jpa_core_annotations_basic_vehicles: 

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
            WebArchive jpa_core_annotations_basic_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_annotations_basic_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_annotations_basic_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            ee.jakarta.tck.persistence.core.annotations.id.Client.class,
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
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_annotations_basic_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("//com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_annotations_basic_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
           archiveProcessor.processWebArchive(jpa_core_annotations_basic_pmservlet_vehicle_web, Client.class, warResURL);

        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_annotations_basic = ShrinkWrap.create(JavaArchive.class, "jpa_core_annotations_basic.jar");
            // The class files
            jpa_core_annotations_basic.addClasses(
                ee.jakarta.tck.persistence.core.annotations.id.PropertyStringId.class,
                ee.jakarta.tck.persistence.core.annotations.id.PropertyUtilDateId.class,
                ee.jakarta.tck.persistence.core.annotations.id.FieldSQLDateId.class,
                ee.jakarta.tck.persistence.core.annotations.id.FieldStringId.class,
                ee.jakarta.tck.persistence.core.annotations.id.PropertyBigIntegerId.class,
                ee.jakarta.tck.persistence.core.annotations.id.PropertySQLDateId.class,
                ee.jakarta.tck.persistence.core.annotations.id.FieldIntegerId.class,
                ee.jakarta.tck.persistence.core.annotations.id.FieldBigDecimalId.class,
                ee.jakarta.tck.persistence.core.annotations.id.PropertyIntId.class,
                ee.jakarta.tck.persistence.core.annotations.id.PropertyIntegerId.class,
                ee.jakarta.tck.persistence.core.annotations.id.FieldIntId.class,
                ee.jakarta.tck.persistence.core.annotations.id.FieldUtilDateId.class,
                ee.jakarta.tck.persistence.core.annotations.id.PropertyBigDecimalId.class,
                ee.jakarta.tck.persistence.core.annotations.id.FieldBigIntegerId.class
            );
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_annotations_basic.addAsManifestResource(parURL, "persistence.xml");
            }
            archiveProcessor.processParArchive(jpa_core_annotations_basic, Client.class, parURL);
            // The orm.xml file
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_annotations_basic.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_annotations_basic_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_annotations_basic_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_annotations_basic_vehicles_ear.addAsModule(jpa_core_annotations_basic_pmservlet_vehicle_web);

            jpa_core_annotations_basic_vehicles_ear.addAsLibrary(jpa_core_annotations_basic);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/jpa/core/annotations/id/");
            if(earResURL != null) {
              jpa_core_annotations_basic_vehicles_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/jpa/core/annotations/id/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_annotations_basic_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            archiveProcessor.processEarArchive(jpa_core_annotations_basic_vehicles_ear, Client.class, earResURL);
        return jpa_core_annotations_basic_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void FieldIntegerIdTest() throws java.lang.Exception {
            super.FieldIntegerIdTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void FieldIntIdTest() throws java.lang.Exception {
            super.FieldIntIdTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void FieldBigIntegerIdTest() throws java.lang.Exception {
            super.FieldBigIntegerIdTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void FieldBigDecimalIdTest() throws java.lang.Exception {
            super.FieldBigDecimalIdTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void FieldStringIdTest() throws java.lang.Exception {
            super.FieldStringIdTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void FieldSQLDateIdTest() throws java.lang.Exception {
            super.FieldSQLDateIdTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void FieldUtilDateIdTest() throws java.lang.Exception {
            super.FieldUtilDateIdTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void PropertyIntegerIdTest() throws java.lang.Exception {
            super.PropertyIntegerIdTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void PropertyIntIdTest() throws java.lang.Exception {
            super.PropertyIntIdTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void PropertyBigIntegerIdTest() throws java.lang.Exception {
            super.PropertyBigIntegerIdTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void PropertyBigDecimalIdTest() throws java.lang.Exception {
            super.PropertyBigDecimalIdTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void PropertyStringIdTest() throws java.lang.Exception {
            super.PropertyStringIdTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void PropertySQLDateIdTest() throws java.lang.Exception {
            super.PropertySQLDateIdTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void PropertyUtilDateIdTest() throws java.lang.Exception {
            super.PropertyUtilDateIdTest();
        }


}