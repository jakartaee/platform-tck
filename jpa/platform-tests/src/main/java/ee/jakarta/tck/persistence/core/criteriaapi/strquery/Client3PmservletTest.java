package ee.jakarta.tck.persistence.core.criteriaapi.strquery;

import ee.jakarta.tck.persistence.core.criteriaapi.strquery.Client3;
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

public class Client3PmservletTest extends ee.jakarta.tck.persistence.core.criteriaapi.strquery.Client3 {
    static final String VEHICLE_ARCHIVE = "jpa_core_criteriaapi_strquery_pmservlet_vehicle";

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

        /com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jpa_core_criteriaapi_strquery_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_criteriaapi_strquery_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            ee.jakarta.tck.persistence.core.criteriaapi.strquery.Client3.class,
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
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client3.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_strquery_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client3.class.getResource("//com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_strquery_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
           archiveProcessor.processWebArchive(jpa_core_criteriaapi_strquery_pmservlet_vehicle_web, Client3.class, warResURL);

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
            URL parURL = Client3.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_criteriaapi_strquery.addAsManifestResource(parURL, "persistence.xml");
            }
            archiveProcessor.processParArchive(jpa_core_criteriaapi_strquery, Client3.class, parURL);
            // The orm.xml file
            parURL = Client3.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_criteriaapi_strquery.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_criteriaapi_strquery_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_criteriaapi_strquery_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_criteriaapi_strquery_vehicles_ear.addAsModule(jpa_core_criteriaapi_strquery_pmservlet_vehicle_web);

            jpa_core_criteriaapi_strquery_vehicles_ear.addAsLibrary(jpa_core_criteriaapi_strquery);



            // The application.xml descriptor
            URL earResURL = Client3.class.getResource("/com/sun/ts/tests/jpa/core/criteriaapi/strquery/");
            if(earResURL != null) {
              jpa_core_criteriaapi_strquery_vehicles_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client3.class.getResource("/com/sun/ts/tests/jpa/core/criteriaapi/strquery/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_criteriaapi_strquery_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            archiveProcessor.processEarArchive(jpa_core_criteriaapi_strquery_vehicles_ear, Client3.class, earResURL);
        return jpa_core_criteriaapi_strquery_vehicles_ear;
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
        public void test_fetchjoin_1x1() throws java.lang.Exception {
            super.test_fetchjoin_1x1();
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

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fromIsCorrelatedTest() throws java.lang.Exception {
            super.fromIsCorrelatedTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fetchStringTest() throws java.lang.Exception {
            super.fetchStringTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fetchStringJoinTypeTest() throws java.lang.Exception {
            super.fetchStringJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void isNullOneToOneTest() throws java.lang.Exception {
            super.isNullOneToOneTest();
        }


}