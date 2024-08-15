package ee.jakarta.tck.persistence.core.annotations.nativequery;

import ee.jakarta.tck.persistence.core.annotations.nativequery.Client;
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

public class ClientPuservletTest extends ee.jakarta.tck.persistence.core.annotations.nativequery.Client {
    static final String VEHICLE_ARCHIVE = "jpa_core_annotations_nativequery_puservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_annotations_nativequery: META-INF/orm.xml,META-INF/persistence.xml
        jpa_core_annotations_nativequery_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_nativequery_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_nativequery_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_nativequery_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_nativequery_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_nativequery_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_nativequery_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_nativequery_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_nativequery_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_nativequery_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_nativequery_vehicles: 

        Found Descriptors:
        War:

        /com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jpa_core_annotations_nativequery_puservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_annotations_nativequery_puservlet_vehicle_web.war");
            // The class files
            jpa_core_annotations_nativequery_puservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            ee.jakarta.tck.persistence.core.annotations.nativequery.Client.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_annotations_nativequery_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("//com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_annotations_nativequery_puservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
           archiveProcessor.processWebArchive(jpa_core_annotations_nativequery_puservlet_vehicle_web, Client.class, warResURL);

        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_annotations_nativequery = ShrinkWrap.create(JavaArchive.class, "jpa_core_annotations_nativequery.jar");
            // The class files
            jpa_core_annotations_nativequery.addClasses(
                ee.jakarta.tck.persistence.core.annotations.nativequery.PurchaseOrder.class,
                ee.jakarta.tck.persistence.core.annotations.nativequery.Order1.class,
                ee.jakarta.tck.persistence.core.annotations.nativequery.Order2.class,
                ee.jakarta.tck.persistence.core.annotations.nativequery.Item.class
            );
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_annotations_nativequery.addAsManifestResource(parURL, "persistence.xml");
            }
            archiveProcessor.processParArchive(jpa_core_annotations_nativequery, Client.class, parURL);
            // The orm.xml file
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_annotations_nativequery.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_annotations_nativequery_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_annotations_nativequery_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_annotations_nativequery_vehicles_ear.addAsModule(jpa_core_annotations_nativequery_puservlet_vehicle_web);

            jpa_core_annotations_nativequery_vehicles_ear.addAsLibrary(jpa_core_annotations_nativequery);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/jpa/core/annotations/nativequery/");
            if(earResURL != null) {
              jpa_core_annotations_nativequery_vehicles_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/jpa/core/annotations/nativequery/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_annotations_nativequery_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            archiveProcessor.processEarArchive(jpa_core_annotations_nativequery_vehicles_ear, Client.class, earResURL);
        return jpa_core_annotations_nativequery_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void nativeQueryTest2() throws java.lang.Exception {
            super.nativeQueryTest2();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void nativeQueryTest3() throws java.lang.Exception {
            super.nativeQueryTest3();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void nativeQueryColumnResultTypeTest() throws java.lang.Exception {
            super.nativeQueryColumnResultTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void nativeQueryTestConstructorResult() throws java.lang.Exception {
            super.nativeQueryTestConstructorResult();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void nativeQueryTestConstructorResultWithId() throws java.lang.Exception {
            super.nativeQueryTestConstructorResultWithId();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void nativeQueryTestConstructorResultNoId() throws java.lang.Exception {
            super.nativeQueryTestConstructorResultNoId();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void createNativeQueryStringTest() throws java.lang.Exception {
            super.createNativeQueryStringTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void createNativeQueryResultClassTest() throws java.lang.Exception {
            super.createNativeQueryResultClassTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void createNativeQueryResultClassTQTest() throws java.lang.Exception {
            super.createNativeQueryResultClassTQTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void executeUpdateTransactionRequiredExceptionTest() throws java.lang.Exception {
            super.executeUpdateTransactionRequiredExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void setParameterTest() throws java.lang.Exception {
            super.setParameterTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getSingleResultTest() throws java.lang.Exception {
            super.getSingleResultTest();
        }


}