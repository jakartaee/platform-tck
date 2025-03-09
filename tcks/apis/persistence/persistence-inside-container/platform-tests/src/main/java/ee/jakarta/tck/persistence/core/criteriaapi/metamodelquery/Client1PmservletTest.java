package ee.jakarta.tck.persistence.core.criteriaapi.metamodelquery;

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
public class Client1PmservletTest extends ee.jakarta.tck.persistence.core.criteriaapi.metamodelquery.Client1 {
    static final String VEHICLE_ARCHIVE = "jpa_core_criteriaapi_metamodelquery_pmservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_criteriaapi_metamodelquery: META-INF/persistence.xml
        jpa_core_criteriaapi_metamodelquery_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_metamodelquery_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_metamodelquery_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_metamodelquery_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_metamodelquery_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_criteriaapi_metamodelquery_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_criteriaapi_metamodelquery_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_metamodelquery_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_metamodelquery_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_metamodelquery_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_metamodelquery_vehicles: 

        Found Descriptors:
        War:

        /com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jpa_core_criteriaapi_metamodelquery_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_criteriaapi_metamodelquery_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_criteriaapi_metamodelquery_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            ee.jakarta.tck.persistence.common.schema30.Util.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            ee.jakarta.tck.persistence.core.criteriaapi.metamodelquery.Client1.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The web.xml descriptor
            URL warResURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_metamodelquery_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_metamodelquery_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client1.class.getResource("/com/sun/ts/tests/jpa/core/criteriaapi/metamodelquery/jpa_core_criteriaapi_metamodelquery.jar");
            if(warResURL != null) {
              jpa_core_criteriaapi_metamodelquery_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_criteriaapi_metamodelquery.jar");
            }
            warResURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_metamodelquery_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/pmservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_criteriaapi_metamodelquery_pmservlet_vehicle_web, Client1.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriaapi_metamodelquery = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_metamodelquery.jar");
            // The class files
            jpa_core_criteriaapi_metamodelquery.addClasses(
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
            URL parURL = Client1.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_criteriaapi_metamodelquery.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client1.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_metamodelquery.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client1.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_metamodelquery.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client1.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_metamodelquery.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_criteriaapi_metamodelquery, Client1.class, parURL);
            parURL = Client1.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_criteriaapi_metamodelquery.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_core_criteriaapi_metamodelquery_pmservlet_vehicle_web.addAsLibrary(jpa_core_criteriaapi_metamodelquery);
            return jpa_core_criteriaapi_metamodelquery_pmservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest2() throws java.lang.Exception {
            super.queryTest2();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest4() throws java.lang.Exception {
            super.queryTest4();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest6() throws java.lang.Exception {
            super.queryTest6();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest15() throws java.lang.Exception {
            super.queryTest15();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest16() throws java.lang.Exception {
            super.queryTest16();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest17() throws java.lang.Exception {
            super.queryTest17();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest18() throws java.lang.Exception {
            super.queryTest18();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest19() throws java.lang.Exception {
            super.queryTest19();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest22() throws java.lang.Exception {
            super.queryTest22();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest23() throws java.lang.Exception {
            super.queryTest23();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest36() throws java.lang.Exception {
            super.queryTest36();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest37() throws java.lang.Exception {
            super.queryTest37();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest47() throws java.lang.Exception {
            super.queryTest47();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest52() throws java.lang.Exception {
            super.queryTest52();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest56() throws java.lang.Exception {
            super.queryTest56();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest58() throws java.lang.Exception {
            super.queryTest58();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest59() throws java.lang.Exception {
            super.queryTest59();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest61() throws java.lang.Exception {
            super.queryTest61();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest64() throws java.lang.Exception {
            super.queryTest64();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest69() throws java.lang.Exception {
            super.queryTest69();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest71() throws java.lang.Exception {
            super.queryTest71();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_leftouterjoin_1xM() throws java.lang.Exception {
            super.test_leftouterjoin_1xM();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_groupBy() throws java.lang.Exception {
            super.test_groupBy();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_innerjoin_1x1() throws java.lang.Exception {
            super.test_innerjoin_1x1();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fetchFetchSingularAttributeTest() throws java.lang.Exception {
            super.fetchFetchSingularAttributeTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fetchFetchSingularAttributeJoinTypeTest() throws java.lang.Exception {
            super.fetchFetchSingularAttributeJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fetchTest() throws java.lang.Exception {
            super.fetchTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fetchGetParentTest() throws java.lang.Exception {
            super.fetchGetParentTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_fetchjoin_1x1() throws java.lang.Exception {
            super.test_fetchjoin_1x1();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fetchSingularAttributeJoinType1X1Test() throws java.lang.Exception {
            super.fetchSingularAttributeJoinType1X1Test();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_fetchjoin_1xM() throws java.lang.Exception {
            super.test_fetchjoin_1xM();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_groupByHaving() throws java.lang.Exception {
            super.test_groupByHaving();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_concatHavingClause() throws java.lang.Exception {
            super.test_concatHavingClause();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_lowerHavingClause() throws java.lang.Exception {
            super.test_lowerHavingClause();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_upperHavingClause() throws java.lang.Exception {
            super.test_upperHavingClause();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_lengthHavingClause() throws java.lang.Exception {
            super.test_lengthHavingClause();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_locateHavingClause() throws java.lang.Exception {
            super.test_locateHavingClause();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_subquery_in() throws java.lang.Exception {
            super.test_subquery_in();
        }


}