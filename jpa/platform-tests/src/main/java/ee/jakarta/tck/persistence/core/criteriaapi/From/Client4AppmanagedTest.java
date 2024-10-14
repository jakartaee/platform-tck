package ee.jakarta.tck.persistence.core.criteriaapi.From;

import ee.jakarta.tck.persistence.core.criteriaapi.From.Client4;
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
public class Client4AppmanagedTest extends ee.jakarta.tck.persistence.core.criteriaapi.From.Client4 {
    static final String VEHICLE_ARCHIVE = "jpa_core_criteriaapi_From_appmanaged_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_criteriaapi_From: META-INF/persistence.xml
        jpa_core_criteriaapi_From_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_From_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_From_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_From_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_From_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_criteriaapi_From_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_criteriaapi_From_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_From_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_From_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_From_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_From_vehicles: 

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_client.xml
        Ejb:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriaapi_From_appmanaged_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_From_vehicles_client.jar");
            // The class files
            jpa_core_criteriaapi_From_appmanaged_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            ee.jakarta.tck.persistence.common.schema30.Util.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            Client4.class,
            Client4AppmanagedTest.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The application-client.xml descriptor
            URL resURL = Client4.class.getResource("/com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_client.xml");
            if(resURL != null) {
              jpa_core_criteriaapi_From_appmanaged_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client4.class.getResource("//com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_core_criteriaapi_From_appmanaged_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jpa_core_criteriaapi_From_appmanaged_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client4.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_core_criteriaapi_From_appmanaged_vehicle_client, Client4.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriaapi_From_appmanaged_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_From_appmanaged_vehicle_ejb.jar");
            // The class files
            jpa_core_criteriaapi_From_appmanaged_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ee.jakarta.tck.persistence.common.PMClientBase.class,
                ee.jakarta.tck.persistence.common.schema30.Util.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                ee.jakarta.tck.persistence.core.criteriaapi.From.Client4.class,
                com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleBean.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client4.class.getResource("//com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_client.xml");
            if(ejbResURL1 != null) {
              jpa_core_criteriaapi_From_appmanaged_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client4.class.getResource("//vehicle/appmanaged/appmanaged_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_core_criteriaapi_From_appmanaged_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_core_criteriaapi_From_appmanaged_vehicle_ejb, Client4.class, ejbResURL1);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriaapi_From = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_From.jar");
            // The class files
            jpa_core_criteriaapi_From.addClasses(
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
            URL parURL = Client4.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_criteriaapi_From.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client4.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_From.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client4.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_From.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client4.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_From.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_criteriaapi_From, Client4.class, parURL);
            parURL = Client4.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_criteriaapi_From.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_criteriaapi_From_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_criteriaapi_From_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_criteriaapi_From_vehicles_ear.addAsModule(jpa_core_criteriaapi_From_appmanaged_vehicle_ejb);
            jpa_core_criteriaapi_From_vehicles_ear.addAsModule(jpa_core_criteriaapi_From_appmanaged_vehicle_client);

            jpa_core_criteriaapi_From_vehicles_ear.addAsLibrary(jpa_core_criteriaapi_From);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client4.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_criteriaapi_From_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_criteriaapi_From_vehicles_ear, Client4.class, earResURL);
        return jpa_core_criteriaapi_From_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void joinMapAttributeTest() throws java.lang.Exception {
            super.joinMapAttributeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void joinMapAttributeJoinTypeTest() throws java.lang.Exception {
            super.joinMapAttributeJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void joinMapStringTest() throws java.lang.Exception {
            super.joinMapStringTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void joinMapStringJoinTypeTest() throws java.lang.Exception {
            super.joinMapStringJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void fromGetMapAttributeTest() throws java.lang.Exception {
            super.fromGetMapAttributeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void pathGetMapAttributeTest() throws java.lang.Exception {
            super.pathGetMapAttributeTest();
        }

}
