package ee.jakarta.tck.persistence.core.query.language;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
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
import java.util.Properties;


@ExtendWith(ArquillianExtension.class)
@Tag("persistence")
@Tag("platform")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class Client1AppmanagednotxTest extends ee.jakarta.tck.persistence.core.query.language.Client1 {
    static final String VEHICLE_ARCHIVE = "jpa_core_query_language_appmanagedNoTx_vehicle";

    public static void main(String[] args) {
      Client1AppmanagednotxTest theTests = new Client1AppmanagednotxTest();
      Status s = theTests.run(args, System.out, System.err);
      s.exit();
    }

    public void setup(String[] args, Properties p) throws Exception {
        super.setup(args, p);
    }

        /**
        EE10 Deployment Descriptors:
        jpa_core_query_language: META-INF/persistence.xml
        jpa_core_query_language_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_query_language_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_query_language_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_query_language_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_query_language_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_query_language_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_query_language_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_query_language_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_query_language_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_query_language_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_query_language_vehicles: 

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
            JavaArchive jpa_core_query_language_appmanagedNoTx_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_language_vehicles_client.jar");
            // The class files
            jpa_core_query_language_appmanagedNoTx_vehicle_client.addClasses(
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
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            Client1.class,
            Client1AppmanagednotxTest.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The application-client.xml descriptor
            URL resURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml");
            if(resURL != null) {
              jpa_core_query_language_appmanagedNoTx_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_core_query_language_appmanagedNoTx_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jpa_core_query_language_appmanagedNoTx_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + com.sun.ts.tests.common.vehicle.VehicleClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_core_query_language_appmanagedNoTx_vehicle_client, Client1.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_core_query_language_appmanagedNoTx_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_language_appmanagedNoTx_vehicle_ejb.jar");
            // The class files
            jpa_core_query_language_appmanagedNoTx_vehicle_ejb.addClasses(
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
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                ee.jakarta.tck.persistence.core.query.language.Client1.class,
                EETest.class,
                ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
                com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class,
                Client1.class,
                Client1AppmanagednotxTest.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml");
            if(ejbResURL1 != null) {
//              jpa_core_query_language_appmanagedNoTx_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_core_query_language_appmanagedNoTx_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_core_query_language_appmanagedNoTx_vehicle_ejb, Client1.class, ejbResURL1);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_query_language = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_language.jar");
            // The class files
            jpa_core_query_language.addClasses(
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
            URL parURL = Client1.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_query_language.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client1.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_query_language.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client1.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_query_language.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client1.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_query_language.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_query_language, Client1.class, parURL);
            parURL = Client1.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_query_language.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_query_language_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_query_language_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_query_language_vehicles_ear.addAsModule(jpa_core_query_language_appmanagedNoTx_vehicle_ejb);
            jpa_core_query_language_vehicles_ear.addAsModule(jpa_core_query_language_appmanagedNoTx_vehicle_client);

            jpa_core_query_language_vehicles_ear.addAsLibrary(jpa_core_query_language);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client1.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_query_language_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_query_language_vehicles_ear, Client1.class, earResURL);
        return jpa_core_query_language_vehicles_ear;
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
        public void test_fetchjoin_1xM() throws java.lang.Exception {
            super.test_fetchjoin_1xM();
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
        public void test_subquery_exists_01() throws java.lang.Exception {
            super.test_subquery_exists_01();
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

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void treatJoinClassTest() throws java.lang.Exception {
            super.treatJoinClassTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void treatInWhereClauseTest() throws java.lang.Exception {
            super.treatInWhereClauseTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void appropriateSuffixesTest() throws java.lang.Exception {
            super.appropriateSuffixesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void sqlApproximateNumericLiteralTest() throws java.lang.Exception {
            super.sqlApproximateNumericLiteralTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void joinOnExpressionTest() throws java.lang.Exception {
            super.joinOnExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void subqueryVariableOverridesQueryVariableTest() throws java.lang.Exception {
            super.subqueryVariableOverridesQueryVariableTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void longIdentifierNameTest() throws java.lang.Exception {
            super.longIdentifierNameTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void underscoreIdentifierNameTest() throws java.lang.Exception {
            super.underscoreIdentifierNameTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void dollarsignIdentifierNameTest() throws java.lang.Exception {
            super.dollarsignIdentifierNameTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void distinctNotSpecifiedTest() throws java.lang.Exception {
            super.distinctNotSpecifiedTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void resultVariableTest() throws java.lang.Exception {
            super.resultVariableTest();
        }

}
