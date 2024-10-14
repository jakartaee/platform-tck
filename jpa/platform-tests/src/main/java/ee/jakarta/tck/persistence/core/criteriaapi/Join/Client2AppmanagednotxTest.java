package ee.jakarta.tck.persistence.core.criteriaapi.Join;

import ee.jakarta.tck.persistence.core.criteriaapi.Join.Client2;
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
public class Client2AppmanagednotxTest extends ee.jakarta.tck.persistence.core.criteriaapi.Join.Client2 {
    static final String VEHICLE_ARCHIVE = "jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_criteriaapi_Join: META-INF/persistence.xml
        jpa_core_criteriaapi_Join_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_Join_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_Join_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_criteriaapi_Join_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_criteriaapi_Join_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_Join_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_Join_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_Join_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_Join_vehicles: 

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
            JavaArchive jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_Join_vehicles_client.jar");
            // The class files
            jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class,
            ee.jakarta.tck.persistence.common.schema30.Util.class,
            com.sun.ts.lib.harness.Status.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            Client2.class,
            Client2AppmanagednotxTest.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The application-client.xml descriptor
            URL resURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml");
            if(resURL != null) {
              jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client2.class.getResource("//com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client2.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle_client, Client2.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle_ejb.jar");
            // The class files
            jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ee.jakarta.tck.persistence.core.criteriaapi.Join.Client2.class,
                ee.jakarta.tck.persistence.common.PMClientBase.class,
                ee.jakarta.tck.persistence.common.schema30.Util.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
                com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client2.class.getResource("//com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml");
            if(ejbResURL1 != null) {
              jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client2.class.getResource("//vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle_ejb, Client2.class, ejbResURL1);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriaapi_Join = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_Join.jar");
            // The class files
            jpa_core_criteriaapi_Join.addClasses(
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
              jpa_core_criteriaapi_Join.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client2.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_Join.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client2.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_Join.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client2.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_Join.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_criteriaapi_Join, Client2.class, parURL);
            parURL = Client2.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_criteriaapi_Join.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_criteriaapi_Join_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_criteriaapi_Join_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_criteriaapi_Join_vehicles_ear.addAsModule(jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle_ejb);
            jpa_core_criteriaapi_Join_vehicles_ear.addAsModule(jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle_client);

            jpa_core_criteriaapi_Join_vehicles_ear.addAsLibrary(jpa_core_criteriaapi_Join);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client2.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_criteriaapi_Join_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_criteriaapi_Join_vehicles_ear, Client2.class, earResURL);
        return jpa_core_criteriaapi_Join_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void joinStringTest() throws java.lang.Exception {
            super.joinStringTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void joinStringJoinTypeTest() throws java.lang.Exception {
            super.joinStringJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void joinSingularAttributeTest() throws java.lang.Exception {
            super.joinSingularAttributeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void joinSingularAttributeJoinTypeTest() throws java.lang.Exception {
            super.joinSingularAttributeJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void joinCollectionAttributeTest() throws java.lang.Exception {
            super.joinCollectionAttributeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void joinCollectionAttributeJoinTypeTest() throws java.lang.Exception {
            super.joinCollectionAttributeJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void joinCollectionStringTest() throws java.lang.Exception {
            super.joinCollectionStringTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void joinCollectionStringJoinTypeTest() throws java.lang.Exception {
            super.joinCollectionStringJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void joinSetAttributeTest() throws java.lang.Exception {
            super.joinSetAttributeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void joinSetAttributeJoinTypeTest() throws java.lang.Exception {
            super.joinSetAttributeJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void joinSetStringTest() throws java.lang.Exception {
            super.joinSetStringTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void joinSetStringJoinTypeTest() throws java.lang.Exception {
            super.joinSetStringJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void joinListAttributeTest() throws java.lang.Exception {
            super.joinListAttributeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void joinListAttributeJoinTypeTest() throws java.lang.Exception {
            super.joinListAttributeJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void joinListStringTest() throws java.lang.Exception {
            super.joinListStringTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void joinListStringJoinTypeTest() throws java.lang.Exception {
            super.joinListStringJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void pluralJoinTest() throws java.lang.Exception {
            super.pluralJoinTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void pluralJoinOnExpressionTest() throws java.lang.Exception {
            super.pluralJoinOnExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void pluralJoinOnPredicateArrayTest() throws java.lang.Exception {
            super.pluralJoinOnPredicateArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void collectionJoinOnExpressionTest() throws java.lang.Exception {
            super.collectionJoinOnExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void collectionJoinOnPredicateArrayTest() throws java.lang.Exception {
            super.collectionJoinOnPredicateArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void listJoinOnExpressionTest() throws java.lang.Exception {
            super.listJoinOnExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void listJoinOnPredicateArrayTest() throws java.lang.Exception {
            super.listJoinOnPredicateArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setJoinOnExpressionTest() throws java.lang.Exception {
            super.setJoinOnExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setJoinOnPredicateArrayTest() throws java.lang.Exception {
            super.setJoinOnPredicateArrayTest();
        }

}
