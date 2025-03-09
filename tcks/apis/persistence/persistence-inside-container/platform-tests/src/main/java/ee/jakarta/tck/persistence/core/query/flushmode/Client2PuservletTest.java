package ee.jakarta.tck.persistence.core.query.flushmode;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

import java.net.URL;



@ExtendWith(ArquillianExtension.class)
@Tag("persistence")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class Client2PuservletTest extends ee.jakarta.tck.persistence.core.query.flushmode.Client2 {
    static final String VEHICLE_ARCHIVE = "jpa_core_query_flushmode_puservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_query_flushmode: META-INF/persistence.xml
        jpa_core_query_flushmode_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_query_flushmode_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_query_flushmode_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_query_flushmode_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_query_flushmode_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_query_flushmode_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_query_flushmode_vehicles: 

        Found Descriptors:
        War:

        /com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jpa_core_query_flushmode_puservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_query_flushmode_puservlet_vehicle_web.war");
            // The class files
            jpa_core_query_flushmode_puservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            ee.jakarta.tck.persistence.core.query.flushmode.Client2.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            ee.jakarta.tck.persistence.common.schema30.Util.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The web.xml descriptor
            URL warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_query_flushmode_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_query_flushmode_puservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client2.class.getResource("/com/sun/ts/tests/jpa/core/query/flushmode/jpa_core_query_flushmode.jar");
            if(warResURL != null) {
              jpa_core_query_flushmode_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_query_flushmode.jar");
            }
            warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_query_flushmode_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/puservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_query_flushmode_puservlet_vehicle_web, Client2.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_query_flushmode = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_flushmode.jar");
            // The class files
            jpa_core_query_flushmode.addClasses(
                ee.jakarta.tck.persistence.common.schema30.Department.class,
                ee.jakarta.tck.persistence.common.schema30.Trim.class,
                ee.jakarta.tck.persistence.common.schema30.CreditCard.class,
                ee.jakarta.tck.persistence.common.schema30.Info.class,
                ee.jakarta.tck.persistence.common.schema30.SoftwareProduct.class,
                ee.jakarta.tck.persistence.common.schema30.Product.class,
                ee.jakarta.tck.persistence.common.schema30.Phone.class,
                ee.jakarta.tck.persistence.common.schema30.Spouse.class,
                ee.jakarta.tck.persistence.common.schema30.LineItem.class,
                ee.jakarta.tck.persistence.common.schema30.Employee.class,
                ee.jakarta.tck.persistence.common.schema30.ShelfLife.class,
                ee.jakarta.tck.persistence.common.schema30.Customer.class,
                ee.jakarta.tck.persistence.common.schema30.Address.class,
                ee.jakarta.tck.persistence.common.schema30.Alias.class,
                ee.jakarta.tck.persistence.common.schema30.Order.class,
                ee.jakarta.tck.persistence.common.schema30.HardwareProduct.class,
                ee.jakarta.tck.persistence.common.schema30.LineItemException.class,
                ee.jakarta.tck.persistence.common.schema30.Country.class
            );
            // The persistence.xml descriptor
            URL parURL = Client2.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_query_flushmode.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client2.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_query_flushmode.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client2.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_query_flushmode.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client2.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_query_flushmode.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_query_flushmode, Client2.class, parURL);
            parURL = Client2.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_query_flushmode.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_core_query_flushmode_puservlet_vehicle_web.addAsLibrary(jpa_core_query_flushmode);
            return jpa_core_query_flushmode_puservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void flushModeTest2() throws java.lang.Exception {
            super.flushModeTest2();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void flushModeTest3() throws java.lang.Exception {
            super.flushModeTest3();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void flushModeTest4() throws java.lang.Exception {
            super.flushModeTest4();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void flushModeTest5() throws java.lang.Exception {
            super.flushModeTest5();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void flushModeTest6() throws java.lang.Exception {
            super.flushModeTest6();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void flushModeTest7() throws java.lang.Exception {
            super.flushModeTest7();
        }


}