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
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class Client2AppmanagednotxTest extends ee.jakarta.tck.persistence.core.criteriaapi.metamodelquery.Client2 {
    static final String VEHICLE_ARCHIVE = "jpa_core_criteriaapi_metamodelquery_appmanagedNoTx_vehicle";

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
        Client:

        /com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml
        Ejb:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriaapi_metamodelquery_appmanagedNoTx_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_metamodelquery_vehicles_client.jar");
            // The class files
            jpa_core_criteriaapi_metamodelquery_appmanagedNoTx_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.web.AltWebVehicleRunner.class,
            ee.jakarta.tck.persistence.common.schema30.Util.class,
            com.sun.ts.lib.harness.Status.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            Client2.class,
            Client2AppmanagednotxTest.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The application-client.xml descriptor
            URL resURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml");
            if(resURL != null) {
              jpa_core_criteriaapi_metamodelquery_appmanagedNoTx_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_core_criteriaapi_metamodelquery_appmanagedNoTx_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jpa_core_criteriaapi_metamodelquery_appmanagedNoTx_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + com.sun.ts.tests.common.vehicle.VehicleClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_core_criteriaapi_metamodelquery_appmanagedNoTx_vehicle_client, Client2.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriaapi_metamodelquery_appmanagedNoTx_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_metamodelquery_appmanagedNoTx_vehicle_ejb.jar");
            // The class files
            jpa_core_criteriaapi_metamodelquery_appmanagedNoTx_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                Fault.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ee.jakarta.tck.persistence.common.PMClientBase.class,
                ee.jakarta.tck.persistence.common.schema30.Util.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                ee.jakarta.tck.persistence.core.criteriaapi.metamodelquery.Client2.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                EETest.class,
                ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
                com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml");
            if(ejbResURL1 != null) {
//              jpa_core_criteriaapi_metamodelquery_appmanagedNoTx_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_core_criteriaapi_metamodelquery_appmanagedNoTx_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_core_criteriaapi_metamodelquery_appmanagedNoTx_vehicle_ejb, Client2.class, ejbResURL1);


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

        // Ear
            EnterpriseArchive jpa_core_criteriaapi_metamodelquery_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_criteriaapi_metamodelquery_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_criteriaapi_metamodelquery_vehicles_ear.addAsModule(jpa_core_criteriaapi_metamodelquery_appmanagedNoTx_vehicle_ejb);
            jpa_core_criteriaapi_metamodelquery_vehicles_ear.addAsModule(jpa_core_criteriaapi_metamodelquery_appmanagedNoTx_vehicle_client);

            jpa_core_criteriaapi_metamodelquery_vehicles_ear.addAsLibrary(jpa_core_criteriaapi_metamodelquery);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client2.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_criteriaapi_metamodelquery_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_criteriaapi_metamodelquery_vehicles_ear, Client2.class, earResURL);
        return jpa_core_criteriaapi_metamodelquery_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest1() throws java.lang.Exception {
            super.queryTest1();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest5() throws java.lang.Exception {
            super.queryTest5();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest8() throws java.lang.Exception {
            super.queryTest8();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest9() throws java.lang.Exception {
            super.queryTest9();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest10() throws java.lang.Exception {
            super.queryTest10();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest11() throws java.lang.Exception {
            super.queryTest11();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest12() throws java.lang.Exception {
            super.queryTest12();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest13() throws java.lang.Exception {
            super.queryTest13();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest14() throws java.lang.Exception {
            super.queryTest14();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest27() throws java.lang.Exception {
            super.queryTest27();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest29() throws java.lang.Exception {
            super.queryTest29();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest30() throws java.lang.Exception {
            super.queryTest30();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest32() throws java.lang.Exception {
            super.queryTest32();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest33() throws java.lang.Exception {
            super.queryTest33();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest34() throws java.lang.Exception {
            super.queryTest34();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest35() throws java.lang.Exception {
            super.queryTest35();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest39() throws java.lang.Exception {
            super.queryTest39();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest42() throws java.lang.Exception {
            super.queryTest42();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest48() throws java.lang.Exception {
            super.queryTest48();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest60() throws java.lang.Exception {
            super.queryTest60();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest62() throws java.lang.Exception {
            super.queryTest62();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest63() throws java.lang.Exception {
            super.queryTest63();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest65() throws java.lang.Exception {
            super.queryTest65();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest66() throws java.lang.Exception {
            super.queryTest66();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryTest67() throws java.lang.Exception {
            super.queryTest67();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_leftouterjoin_Mx1() throws java.lang.Exception {
            super.test_leftouterjoin_Mx1();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_groupBy_1() throws java.lang.Exception {
            super.test_groupBy_1();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_innerjoin_1xM() throws java.lang.Exception {
            super.test_innerjoin_1xM();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_innerjoin_Mx1() throws java.lang.Exception {
            super.test_innerjoin_Mx1();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void fetchFetchPluralAttributeTest() throws java.lang.Exception {
            super.fetchFetchPluralAttributeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void fetchFetchPluralAttributeJoinTypeTest() throws java.lang.Exception {
            super.fetchFetchPluralAttributeJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void fetchFetchStringTest() throws java.lang.Exception {
            super.fetchFetchStringTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void fetchFetchStringJoinTypeTest() throws java.lang.Exception {
            super.fetchFetchStringJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void fetchFetchStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.fetchFetchStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void fetchFetchStringJoinTypeIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.fetchFetchStringJoinTypeIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void fetchPluralAttribute1xMTest() throws java.lang.Exception {
            super.fetchPluralAttribute1xMTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void fetchPluralAttributeJoinType1xMTest() throws java.lang.Exception {
            super.fetchPluralAttributeJoinType1xMTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_fetchjoin_Mx1() throws java.lang.Exception {
            super.test_fetchjoin_Mx1();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_fetchjoin_Mx1_1() throws java.lang.Exception {
            super.test_fetchjoin_Mx1_1();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_notBetweenArithmetic() throws java.lang.Exception {
            super.test_notBetweenArithmetic();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_ANDconditionTT() throws java.lang.Exception {
            super.test_ANDconditionTT();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_ANDconditionTF() throws java.lang.Exception {
            super.test_ANDconditionTF();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_ANDconditionFT() throws java.lang.Exception {
            super.test_ANDconditionFT();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_ANDconditionFF() throws java.lang.Exception {
            super.test_ANDconditionFF();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_ORconditionTT() throws java.lang.Exception {
            super.test_ORconditionTT();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_ORconditionTF() throws java.lang.Exception {
            super.test_ORconditionTF();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_ORconditionFT() throws java.lang.Exception {
            super.test_ORconditionFT();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_ORconditionFF() throws java.lang.Exception {
            super.test_ORconditionFF();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_groupByWhereClause() throws java.lang.Exception {
            super.test_groupByWhereClause();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_ABSHavingClause() throws java.lang.Exception {
            super.test_ABSHavingClause();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_SQRTWhereClause() throws java.lang.Exception {
            super.test_SQRTWhereClause();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void subQueryGetSelectionGetParentTest() throws java.lang.Exception {
            super.subQueryGetSelectionGetParentTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void subQueryHavingExpressionTest() throws java.lang.Exception {
            super.subQueryHavingExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void subQueryHavingPredicateArrayTest() throws java.lang.Exception {
            super.subQueryHavingPredicateArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void subQuerySelectExpressionTest() throws java.lang.Exception {
            super.subQuerySelectExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void subQueryWhereExpressionTest() throws java.lang.Exception {
            super.subQueryWhereExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void subQueryWherePredicateArrayTest() throws java.lang.Exception {
            super.subQueryWherePredicateArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void subQueryDistinctTest() throws java.lang.Exception {
            super.subQueryDistinctTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_subquery_exists_02() throws java.lang.Exception {
            super.test_subquery_exists_02();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_subquery_like() throws java.lang.Exception {
            super.test_subquery_like();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_subquery_between() throws java.lang.Exception {
            super.test_subquery_between();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_subquery_join() throws java.lang.Exception {
            super.test_subquery_join();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getCorrelationParentIllegalStateExceptionTest() throws java.lang.Exception {
            super.getCorrelationParentIllegalStateExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void subquerySetJoinTest() throws java.lang.Exception {
            super.subquerySetJoinTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void subqueryListJoinTest() throws java.lang.Exception {
            super.subqueryListJoinTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void subqueryCollectionJoinTest() throws java.lang.Exception {
            super.subqueryCollectionJoinTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_subquery_ALL_GT() throws java.lang.Exception {
            super.test_subquery_ALL_GT();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_subquery_ALL_LT() throws java.lang.Exception {
            super.test_subquery_ALL_LT();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_subquery_ALL_EQ() throws java.lang.Exception {
            super.test_subquery_ALL_EQ();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_subquery_ALL_LTEQ() throws java.lang.Exception {
            super.test_subquery_ALL_LTEQ();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_subquery_ALL_GTEQ() throws java.lang.Exception {
            super.test_subquery_ALL_GTEQ();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_subquery_ALL_NOTEQ() throws java.lang.Exception {
            super.test_subquery_ALL_NOTEQ();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_subquery_ANY_GT() throws java.lang.Exception {
            super.test_subquery_ANY_GT();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_subquery_ANY_LT() throws java.lang.Exception {
            super.test_subquery_ANY_LT();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_subquery_ANY_EQ() throws java.lang.Exception {
            super.test_subquery_ANY_EQ();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_subquery_SOME_LTEQ() throws java.lang.Exception {
            super.test_subquery_SOME_LTEQ();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void test_subquery_SOME_GTEQ() throws java.lang.Exception {
            super.test_subquery_SOME_GTEQ();
        }

}
