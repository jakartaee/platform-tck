package ee.jakarta.tck.persistence.core.criteriaapi.metamodelquery;

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
public class Client2PuservletTest extends ee.jakarta.tck.persistence.core.criteriaapi.metamodelquery.Client2 {
    static final String VEHICLE_ARCHIVE = "jpa_core_criteriaapi_metamodelquery_puservlet_vehicle";

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

        /com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jpa_core_criteriaapi_metamodelquery_puservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_criteriaapi_metamodelquery_puservlet_vehicle_web.war");
            // The class files
            jpa_core_criteriaapi_metamodelquery_puservlet_vehicle_web.addClasses(
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
            com.sun.ts.lib.harness.Status.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            ee.jakarta.tck.persistence.core.criteriaapi.metamodelquery.Client2.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The web.xml descriptor
            URL warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_metamodelquery_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_metamodelquery_puservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client2.class.getResource("/com/sun/ts/tests/jpa/core/criteriaapi/metamodelquery/jpa_core_criteriaapi_metamodelquery.jar");
            if(warResURL != null) {
              jpa_core_criteriaapi_metamodelquery_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_criteriaapi_metamodelquery.jar");
            }
            warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_metamodelquery_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/puservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_criteriaapi_metamodelquery_puservlet_vehicle_web, Client2.class, warResURL);


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
            URL parURL = Client2.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_criteriaapi_metamodelquery.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client2.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_metamodelquery.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client2.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_metamodelquery.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client2.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_metamodelquery.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_criteriaapi_metamodelquery, Client2.class, parURL);
            parURL = Client2.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_criteriaapi_metamodelquery.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_core_criteriaapi_metamodelquery_puservlet_vehicle_web.addAsLibrary(jpa_core_criteriaapi_metamodelquery);
            return jpa_core_criteriaapi_metamodelquery_puservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest1() throws java.lang.Exception {
            super.queryTest1();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest5() throws java.lang.Exception {
            super.queryTest5();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest8() throws java.lang.Exception {
            super.queryTest8();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest9() throws java.lang.Exception {
            super.queryTest9();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest10() throws java.lang.Exception {
            super.queryTest10();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest11() throws java.lang.Exception {
            super.queryTest11();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest12() throws java.lang.Exception {
            super.queryTest12();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest13() throws java.lang.Exception {
            super.queryTest13();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest14() throws java.lang.Exception {
            super.queryTest14();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest27() throws java.lang.Exception {
            super.queryTest27();
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
        public void queryTest32() throws java.lang.Exception {
            super.queryTest32();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest33() throws java.lang.Exception {
            super.queryTest33();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest34() throws java.lang.Exception {
            super.queryTest34();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest35() throws java.lang.Exception {
            super.queryTest35();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest39() throws java.lang.Exception {
            super.queryTest39();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest42() throws java.lang.Exception {
            super.queryTest42();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest48() throws java.lang.Exception {
            super.queryTest48();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest60() throws java.lang.Exception {
            super.queryTest60();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest62() throws java.lang.Exception {
            super.queryTest62();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest63() throws java.lang.Exception {
            super.queryTest63();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest65() throws java.lang.Exception {
            super.queryTest65();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest66() throws java.lang.Exception {
            super.queryTest66();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryTest67() throws java.lang.Exception {
            super.queryTest67();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_leftouterjoin_Mx1() throws java.lang.Exception {
            super.test_leftouterjoin_Mx1();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_groupBy_1() throws java.lang.Exception {
            super.test_groupBy_1();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_innerjoin_1xM() throws java.lang.Exception {
            super.test_innerjoin_1xM();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_innerjoin_Mx1() throws java.lang.Exception {
            super.test_innerjoin_Mx1();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void fetchFetchPluralAttributeTest() throws java.lang.Exception {
            super.fetchFetchPluralAttributeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void fetchFetchPluralAttributeJoinTypeTest() throws java.lang.Exception {
            super.fetchFetchPluralAttributeJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void fetchFetchStringTest() throws java.lang.Exception {
            super.fetchFetchStringTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void fetchFetchStringJoinTypeTest() throws java.lang.Exception {
            super.fetchFetchStringJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void fetchFetchStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.fetchFetchStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void fetchFetchStringJoinTypeIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.fetchFetchStringJoinTypeIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void fetchPluralAttribute1xMTest() throws java.lang.Exception {
            super.fetchPluralAttribute1xMTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void fetchPluralAttributeJoinType1xMTest() throws java.lang.Exception {
            super.fetchPluralAttributeJoinType1xMTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_fetchjoin_Mx1() throws java.lang.Exception {
            super.test_fetchjoin_Mx1();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_fetchjoin_Mx1_1() throws java.lang.Exception {
            super.test_fetchjoin_Mx1_1();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_notBetweenArithmetic() throws java.lang.Exception {
            super.test_notBetweenArithmetic();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_ANDconditionTT() throws java.lang.Exception {
            super.test_ANDconditionTT();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_ANDconditionTF() throws java.lang.Exception {
            super.test_ANDconditionTF();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_ANDconditionFT() throws java.lang.Exception {
            super.test_ANDconditionFT();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_ANDconditionFF() throws java.lang.Exception {
            super.test_ANDconditionFF();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_ORconditionTT() throws java.lang.Exception {
            super.test_ORconditionTT();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_ORconditionTF() throws java.lang.Exception {
            super.test_ORconditionTF();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_ORconditionFT() throws java.lang.Exception {
            super.test_ORconditionFT();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_ORconditionFF() throws java.lang.Exception {
            super.test_ORconditionFF();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_groupByWhereClause() throws java.lang.Exception {
            super.test_groupByWhereClause();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_ABSHavingClause() throws java.lang.Exception {
            super.test_ABSHavingClause();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_SQRTWhereClause() throws java.lang.Exception {
            super.test_SQRTWhereClause();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void subQueryGetSelectionGetParentTest() throws java.lang.Exception {
            super.subQueryGetSelectionGetParentTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void subQueryHavingExpressionTest() throws java.lang.Exception {
            super.subQueryHavingExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void subQueryHavingPredicateArrayTest() throws java.lang.Exception {
            super.subQueryHavingPredicateArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void subQuerySelectExpressionTest() throws java.lang.Exception {
            super.subQuerySelectExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void subQueryWhereExpressionTest() throws java.lang.Exception {
            super.subQueryWhereExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void subQueryWherePredicateArrayTest() throws java.lang.Exception {
            super.subQueryWherePredicateArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void subQueryDistinctTest() throws java.lang.Exception {
            super.subQueryDistinctTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_subquery_exists_02() throws java.lang.Exception {
            super.test_subquery_exists_02();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_subquery_like() throws java.lang.Exception {
            super.test_subquery_like();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_subquery_between() throws java.lang.Exception {
            super.test_subquery_between();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_subquery_join() throws java.lang.Exception {
            super.test_subquery_join();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getCorrelationParentIllegalStateExceptionTest() throws java.lang.Exception {
            super.getCorrelationParentIllegalStateExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void subquerySetJoinTest() throws java.lang.Exception {
            super.subquerySetJoinTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void subqueryListJoinTest() throws java.lang.Exception {
            super.subqueryListJoinTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void subqueryCollectionJoinTest() throws java.lang.Exception {
            super.subqueryCollectionJoinTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_subquery_ALL_GT() throws java.lang.Exception {
            super.test_subquery_ALL_GT();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_subquery_ALL_LT() throws java.lang.Exception {
            super.test_subquery_ALL_LT();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_subquery_ALL_EQ() throws java.lang.Exception {
            super.test_subquery_ALL_EQ();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_subquery_ALL_LTEQ() throws java.lang.Exception {
            super.test_subquery_ALL_LTEQ();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_subquery_ALL_GTEQ() throws java.lang.Exception {
            super.test_subquery_ALL_GTEQ();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_subquery_ALL_NOTEQ() throws java.lang.Exception {
            super.test_subquery_ALL_NOTEQ();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_subquery_ANY_GT() throws java.lang.Exception {
            super.test_subquery_ANY_GT();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_subquery_ANY_LT() throws java.lang.Exception {
            super.test_subquery_ANY_LT();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_subquery_ANY_EQ() throws java.lang.Exception {
            super.test_subquery_ANY_EQ();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_subquery_SOME_LTEQ() throws java.lang.Exception {
            super.test_subquery_SOME_LTEQ();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void test_subquery_SOME_GTEQ() throws java.lang.Exception {
            super.test_subquery_SOME_GTEQ();
        }


}