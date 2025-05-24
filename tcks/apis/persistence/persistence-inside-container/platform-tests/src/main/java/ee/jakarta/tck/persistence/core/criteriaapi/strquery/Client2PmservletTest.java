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
public class Client2PmservletTest extends ee.jakarta.tck.persistence.core.criteriaapi.strquery.Client2 {
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
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jpa_core_criteriaapi_strquery_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_criteriaapi_strquery_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            ee.jakarta.tck.persistence.core.criteriaapi.strquery.Client2.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            ee.jakarta.tck.persistence.common.schema30.Util.class,
            com.sun.ts.lib.harness.Status.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The web.xml descriptor
            URL warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_strquery_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_strquery_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client2.class.getResource("/com/sun/ts/tests/jpa/core/criteriaapi/strquery/jpa_core_criteriaapi_strquery.jar");
            if(warResURL != null) {
              jpa_core_criteriaapi_strquery_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_criteriaapi_strquery.jar");
            }
            warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_strquery_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/pmservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_criteriaapi_strquery_pmservlet_vehicle_web, Client2.class, warResURL);


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
            URL parURL = Client2.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_criteriaapi_strquery.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client2.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_strquery.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client2.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_strquery.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client2.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_strquery.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_criteriaapi_strquery, Client2.class, parURL);
            parURL = Client2.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_criteriaapi_strquery.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_core_criteriaapi_strquery_pmservlet_vehicle_web.addAsLibrary(jpa_core_criteriaapi_strquery);
            return jpa_core_criteriaapi_strquery_pmservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest1() throws java.lang.Exception {
            super.queryTest1();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest5() throws java.lang.Exception {
            super.queryTest5();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest8() throws java.lang.Exception {
            super.queryTest8();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest9() throws java.lang.Exception {
            super.queryTest9();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest10() throws java.lang.Exception {
            super.queryTest10();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest11() throws java.lang.Exception {
            super.queryTest11();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest12() throws java.lang.Exception {
            super.queryTest12();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest13() throws java.lang.Exception {
            super.queryTest13();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest14() throws java.lang.Exception {
            super.queryTest14();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest27() throws java.lang.Exception {
            super.queryTest27();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest32() throws java.lang.Exception {
            super.queryTest32();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest33() throws java.lang.Exception {
            super.queryTest33();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest34() throws java.lang.Exception {
            super.queryTest34();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest35() throws java.lang.Exception {
            super.queryTest35();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest39() throws java.lang.Exception {
            super.queryTest39();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest42() throws java.lang.Exception {
            super.queryTest42();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest48() throws java.lang.Exception {
            super.queryTest48();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest60() throws java.lang.Exception {
            super.queryTest60();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest62() throws java.lang.Exception {
            super.queryTest62();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest63() throws java.lang.Exception {
            super.queryTest63();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest65() throws java.lang.Exception {
            super.queryTest65();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest66() throws java.lang.Exception {
            super.queryTest66();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryTest67() throws java.lang.Exception {
            super.queryTest67();
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
        public void test_leftouterjoin_Mx1() throws java.lang.Exception {
            super.test_leftouterjoin_Mx1();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_groupBy_1() throws java.lang.Exception {
            super.test_groupBy_1();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_innerjoin_1xM() throws java.lang.Exception {
            super.test_innerjoin_1xM();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_innerjoin_Mx1() throws java.lang.Exception {
            super.test_innerjoin_Mx1();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_fetchjoin_Mx1() throws java.lang.Exception {
            super.test_fetchjoin_Mx1();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_fetchjoin_Mx1_1() throws java.lang.Exception {
            super.test_fetchjoin_Mx1_1();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_notBetweenArithmetic() throws java.lang.Exception {
            super.test_notBetweenArithmetic();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_ANDconditionTT() throws java.lang.Exception {
            super.test_ANDconditionTT();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_ANDconditionTF() throws java.lang.Exception {
            super.test_ANDconditionTF();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_ANDconditionFT() throws java.lang.Exception {
            super.test_ANDconditionFT();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_ANDconditionFF() throws java.lang.Exception {
            super.test_ANDconditionFF();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_ORconditionTT() throws java.lang.Exception {
            super.test_ORconditionTT();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_ORconditionTF() throws java.lang.Exception {
            super.test_ORconditionTF();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_ORconditionFT() throws java.lang.Exception {
            super.test_ORconditionFT();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_ORconditionFF() throws java.lang.Exception {
            super.test_ORconditionFF();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_groupByWhereClause() throws java.lang.Exception {
            super.test_groupByWhereClause();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_ABSHavingClause() throws java.lang.Exception {
            super.test_ABSHavingClause();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_SQRTWhereClause() throws java.lang.Exception {
            super.test_SQRTWhereClause();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_subquery_exists_01() throws java.lang.Exception {
            super.test_subquery_exists_01();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_subquery_exists_02() throws java.lang.Exception {
            super.test_subquery_exists_02();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_subquery_like() throws java.lang.Exception {
            super.test_subquery_like();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_subquery_between() throws java.lang.Exception {
            super.test_subquery_between();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_subquery_join() throws java.lang.Exception {
            super.test_subquery_join();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_subquery_ALL_GT() throws java.lang.Exception {
            super.test_subquery_ALL_GT();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_subquery_ALL_LT() throws java.lang.Exception {
            super.test_subquery_ALL_LT();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_subquery_ALL_EQ() throws java.lang.Exception {
            super.test_subquery_ALL_EQ();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_subquery_ALL_LTEQ() throws java.lang.Exception {
            super.test_subquery_ALL_LTEQ();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_subquery_ALL_GTEQ() throws java.lang.Exception {
            super.test_subquery_ALL_GTEQ();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_subquery_ALL_NOTEQ() throws java.lang.Exception {
            super.test_subquery_ALL_NOTEQ();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_subquery_ANY_GT() throws java.lang.Exception {
            super.test_subquery_ANY_GT();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_subquery_ANY_LT() throws java.lang.Exception {
            super.test_subquery_ANY_LT();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_subquery_ANY_EQ() throws java.lang.Exception {
            super.test_subquery_ANY_EQ();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_subquery_SOME_LTEQ() throws java.lang.Exception {
            super.test_subquery_SOME_LTEQ();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test_subquery_SOME_GTEQ() throws java.lang.Exception {
            super.test_subquery_SOME_GTEQ();
        }


}