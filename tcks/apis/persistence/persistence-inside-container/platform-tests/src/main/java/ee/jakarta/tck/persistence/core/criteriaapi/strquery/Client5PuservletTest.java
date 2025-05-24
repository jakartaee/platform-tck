package ee.jakarta.tck.persistence.core.criteriaapi.strquery;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;
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
public class Client5PuservletTest extends ee.jakarta.tck.persistence.core.criteriaapi.strquery.Client5 {
    static final String VEHICLE_ARCHIVE = "jpa_core_criteriaapi_strquery_puservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_criteriaapi_strquery: META-INF/persistence.xml
        jpa_core_criteriaapi_strquery_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_strquery_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_strquery_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_strquery_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_criteriaapi_strquery_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_criteriaapi_strquery_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_strquery_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_strquery_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_strquery_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_strquery_vehicles: 

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
            WebArchive jpa_core_criteriaapi_strquery_puservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_criteriaapi_strquery_puservlet_vehicle_web.war");
            // The class files
            jpa_core_criteriaapi_strquery_puservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            ee.jakarta.tck.persistence.common.schema30.Util.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            ee.jakarta.tck.persistence.core.criteriaapi.strquery.Client5.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The web.xml descriptor
            URL warResURL = Client5.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_strquery_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client5.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_strquery_puservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client5.class.getResource("/com/sun/ts/tests/jpa/core/criteriaapi/strquery/jpa_core_criteriaapi_strquery.jar");
            if(warResURL != null) {
              jpa_core_criteriaapi_strquery_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_criteriaapi_strquery.jar");
            }
            warResURL = Client5.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_strquery_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/puservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_criteriaapi_strquery_puservlet_vehicle_web, Client5.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriaapi_strquery = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_strquery.jar");
            // The class files
            jpa_core_criteriaapi_strquery.addClasses(
                ee.jakarta.tck.persistence.common.schema30.Department.class,
                ee.jakarta.tck.persistence.common.schema30.Address_.class,
                ee.jakarta.tck.persistence.common.schema30.Department_.class,
                ee.jakarta.tck.persistence.common.schema30.CreditCard.class,
                ee.jakarta.tck.persistence.common.schema30.Info.class,
                ee.jakarta.tck.persistence.common.schema30.LineItem_.class,
                ee.jakarta.tck.persistence.common.schema30.Phone.class,
                ee.jakarta.tck.persistence.common.schema30.Customer_.class,
                ee.jakarta.tck.persistence.common.schema30.Employee_.class,
                ee.jakarta.tck.persistence.common.schema30.Trim_.class,
                ee.jakarta.tck.persistence.common.schema30.Order_.class,
                ee.jakarta.tck.persistence.common.schema30.ShelfLife_.class,
                ee.jakarta.tck.persistence.common.schema30.ShelfLife.class,
                ee.jakarta.tck.persistence.common.schema30.Phone_.class,
                ee.jakarta.tck.persistence.common.schema30.Address.class,
                ee.jakarta.tck.persistence.common.schema30.Info_.class,
                ee.jakarta.tck.persistence.common.schema30.HardwareProduct.class,
                ee.jakarta.tck.persistence.common.schema30.Country_.class,
                ee.jakarta.tck.persistence.common.schema30.Alias_.class,
                ee.jakarta.tck.persistence.common.schema30.Trim.class,
                ee.jakarta.tck.persistence.common.schema30.HardwareProduct_.class,
                ee.jakarta.tck.persistence.common.schema30.CreditCard_.class,
                ee.jakarta.tck.persistence.common.schema30.SoftwareProduct.class,
                ee.jakarta.tck.persistence.common.schema30.Product.class,
                ee.jakarta.tck.persistence.common.schema30.Spouse.class,
                ee.jakarta.tck.persistence.common.schema30.SoftwareProduct_.class,
                ee.jakarta.tck.persistence.common.schema30.Spouse_.class,
                ee.jakarta.tck.persistence.common.schema30.LineItem.class,
                ee.jakarta.tck.persistence.common.schema30.Employee.class,
                ee.jakarta.tck.persistence.common.schema30.Product_.class,
                ee.jakarta.tck.persistence.common.schema30.Customer.class,
                ee.jakarta.tck.persistence.common.schema30.Alias.class,
                ee.jakarta.tck.persistence.common.schema30.Order.class,
                ee.jakarta.tck.persistence.common.schema30.LineItemException.class,
                ee.jakarta.tck.persistence.common.schema30.Country.class
            );
            // The persistence.xml descriptor
            URL parURL = Client5.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_criteriaapi_strquery.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client5.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_strquery.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client5.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_strquery.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client5.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_strquery.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_criteriaapi_strquery, Client5.class, parURL);
            parURL = Client5.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_criteriaapi_strquery.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_core_criteriaapi_strquery_puservlet_vehicle_web.addAsLibrary(jpa_core_criteriaapi_strquery);
            return jpa_core_criteriaapi_strquery_puservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest3() throws java.lang.Exception {
            super.queryTest3();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest20() throws java.lang.Exception {
            super.queryTest20();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest21() throws java.lang.Exception {
            super.queryTest21();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest24() throws java.lang.Exception {
            super.queryTest24();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest25() throws java.lang.Exception {
            super.queryTest25();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest26() throws java.lang.Exception {
            super.queryTest26();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest28() throws java.lang.Exception {
            super.queryTest28();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest29() throws java.lang.Exception {
            super.queryTest29();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest30() throws java.lang.Exception {
            super.queryTest30();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest31() throws java.lang.Exception {
            super.queryTest31();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest45() throws java.lang.Exception {
            super.queryTest45();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest49() throws java.lang.Exception {
            super.queryTest49();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest50() throws java.lang.Exception {
            super.queryTest50();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest51() throws java.lang.Exception {
            super.queryTest51();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest53() throws java.lang.Exception {
            super.queryTest53();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest54() throws java.lang.Exception {
            super.queryTest54();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_leftouterjoin_MxM() throws java.lang.Exception {
            super.test_leftouterjoin_MxM();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_upperStringExpression() throws java.lang.Exception {
            super.test_upperStringExpression();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_lowerStringExpression() throws java.lang.Exception {
            super.test_lowerStringExpression();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_innerjoin_MxM() throws java.lang.Exception {
            super.test_innerjoin_MxM();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_fetchjoin_MxM() throws java.lang.Exception {
            super.test_fetchjoin_MxM();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_substringHavingClause() throws java.lang.Exception {
            super.test_substringHavingClause();
        }


}