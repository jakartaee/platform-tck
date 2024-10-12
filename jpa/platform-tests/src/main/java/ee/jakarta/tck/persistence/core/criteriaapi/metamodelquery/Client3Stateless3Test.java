package ee.jakarta.tck.persistence.core.criteriaapi.metamodelquery;

import ee.jakarta.tck.persistence.core.criteriaapi.metamodelquery.Client3;
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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("persistence")
@Tag("platform")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class Client3Stateless3Test extends ee.jakarta.tck.persistence.core.criteriaapi.metamodelquery.Client3 {
    static final String VEHICLE_ARCHIVE = "jpa_core_criteriaapi_metamodelquery_stateless3_vehicle";

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

        /com/sun/ts/tests/common/vehicle/stateless3/stateless3_vehicle_client.xml
        Ejb:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriaapi_metamodelquery_stateless3_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_metamodelquery_vehicles_client.jar");
            // The class files
            jpa_core_criteriaapi_metamodelquery_stateless3_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            ee.jakarta.tck.persistence.common.schema30.Util.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            Client3.class,
            Client3Stateless3Test.class)
            .addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The application-client.xml descriptor
            URL resURL = Client3.class.getResource("/com/sun/ts/tests/common/vehicle/stateless3/stateless3_vehicle_client.xml");
            if(resURL != null) {
              jpa_core_criteriaapi_metamodelquery_stateless3_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client3.class.getResource("//com/sun/ts/tests/common/vehicle/stateless3/stateless3_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_core_criteriaapi_metamodelquery_stateless3_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jpa_core_criteriaapi_metamodelquery_stateless3_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client3.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_core_criteriaapi_metamodelquery_stateless3_vehicle_client, Client3.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriaapi_metamodelquery_stateless3_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_metamodelquery_stateless3_vehicle_ejb.jar");
            // The class files
            jpa_core_criteriaapi_metamodelquery_stateless3_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ee.jakarta.tck.persistence.common.PMClientBase.class,
                ee.jakarta.tck.persistence.core.criteriaapi.metamodelquery.Client3.class,
                ee.jakarta.tck.persistence.common.schema30.Util.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class,
                com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client3.class.getResource("//vehicle/stateless3/stateless3_vehicle_ejb.xml");
            if(ejbResURL1 != null) {
              jpa_core_criteriaapi_metamodelquery_stateless3_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client3.class.getResource("//vehicle/stateless3/stateless3_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_core_criteriaapi_metamodelquery_stateless3_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_core_criteriaapi_metamodelquery_stateless3_vehicle_ejb, Client3.class, ejbResURL1);


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
            URL parURL = Client3.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_criteriaapi_metamodelquery.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client3.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_metamodelquery.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client3.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_metamodelquery.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client3.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_metamodelquery.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_criteriaapi_metamodelquery, Client3.class, parURL);
            parURL = Client3.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_criteriaapi_metamodelquery.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_criteriaapi_metamodelquery_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_criteriaapi_metamodelquery_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_criteriaapi_metamodelquery_vehicles_ear.addAsModule(jpa_core_criteriaapi_metamodelquery_stateless3_vehicle_ejb);
            jpa_core_criteriaapi_metamodelquery_vehicles_ear.addAsModule(jpa_core_criteriaapi_metamodelquery_stateless3_vehicle_client);

            jpa_core_criteriaapi_metamodelquery_vehicles_ear.addAsLibrary(jpa_core_criteriaapi_metamodelquery);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client3.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_criteriaapi_metamodelquery_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_criteriaapi_metamodelquery_vehicles_ear, Client3.class, earResURL);
        return jpa_core_criteriaapi_metamodelquery_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void queryTest3() throws java.lang.Exception {
            super.queryTest3();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void queryTest20() throws java.lang.Exception {
            super.queryTest20();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void queryTest21() throws java.lang.Exception {
            super.queryTest21();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void queryTest31() throws java.lang.Exception {
            super.queryTest31();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void likeExpExpCharTest() throws java.lang.Exception {
            super.likeExpExpCharTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void likeExpExpExpTest() throws java.lang.Exception {
            super.likeExpExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void likeExpStringExpTest() throws java.lang.Exception {
            super.likeExpStringExpTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void queryTest45() throws java.lang.Exception {
            super.queryTest45();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void queryTest49() throws java.lang.Exception {
            super.queryTest49();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void queryTest50() throws java.lang.Exception {
            super.queryTest50();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void queryTest51() throws java.lang.Exception {
            super.queryTest51();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void queryTest53() throws java.lang.Exception {
            super.queryTest53();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void queryTest54() throws java.lang.Exception {
            super.queryTest54();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void test_leftouterjoin_MxM() throws java.lang.Exception {
            super.test_leftouterjoin_MxM();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void test_upperStringExpression() throws java.lang.Exception {
            super.test_upperStringExpression();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void test_lowerStringExpression() throws java.lang.Exception {
            super.test_lowerStringExpression();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void test_innerjoin_MxM() throws java.lang.Exception {
            super.test_innerjoin_MxM();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void test_fetchjoin_MxM() throws java.lang.Exception {
            super.test_fetchjoin_MxM();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void substringHavingExpressionTest() throws java.lang.Exception {
            super.substringHavingExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void substringHavingExpressionPredicateArrayTest() throws java.lang.Exception {
            super.substringHavingExpressionPredicateArrayTest();
        }

}
