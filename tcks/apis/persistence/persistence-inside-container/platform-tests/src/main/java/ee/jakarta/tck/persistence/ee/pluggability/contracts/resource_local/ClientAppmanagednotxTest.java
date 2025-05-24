package ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local;

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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

import java.net.URL;


@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("tck-appclient")

public class ClientAppmanagednotxTest extends ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Client {
    static final String VEHICLE_ARCHIVE = "pluggability_contracts_resource_local_appmanagedNoTx_vehicle";

    public static void main(String[] args) {
      ClientAppmanagednotxTest theTests = new ClientAppmanagednotxTest();
      Status s = theTests.run(args, System.out, System.err);
      s.exit();
    }

        /**
        EE10 Deployment Descriptors:
        pluggability_contracts_resource_local: META-INF/myMappingFile1.xml,META-INF/myMappingFile2.xml,META-INF/orm.xml,META-INF/persistence.xml
        pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        pluggability_contracts_resource_local_puservlet_vehicle_web: WEB-INF/web.xml
        pluggability_contracts_resource_local_vehicles: 

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
            JavaArchive pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client = ShrinkWrap.create(JavaArchive.class, "pluggability_contracts_resource_local_vehicles_client.jar");
            // The class files
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client.addClasses(
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
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Client.class,
            ClientAppmanagednotxTest.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml");
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = null;
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + com.sun.ts.tests.common.vehicle.VehicleClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb.jar");
            // The class files
            pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                Fault.class,
                ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Client.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ee.jakarta.tck.persistence.common.PMClientBase.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                EETest.class,
                ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
                com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = null;
            // The sun-ejb-jar.xml file
            // Call the archive processor
            archiveProcessor.processEjbArchive(pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb, Client.class, ejbResURL);

        // Par
            // the jar with the correct archive name
            JavaArchive pluggability_contracts_resource_local = ShrinkWrap.create(JavaArchive.class, "pluggability_contracts_resource_local.jar");
            // The class files
            pluggability_contracts_resource_local.addClasses(
                ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Order4.class,
                ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Order.class,
                ee.jakarta.tck.persistence.common.pluggability.util.LogRecordEntry.class,
                ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Order3.class,
                ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Order2.class,
                ee.jakarta.tck.persistence.common.pluggability.util.LogFileProcessor.class
            );
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            pluggability_contracts_resource_local.addAsManifestResource(parURL, "persistence.xml");
            // Call the archive processor
            archiveProcessor.processParArchive(pluggability_contracts_resource_local, Client.class, parURL);
            // The orm.xml file
            parURL = Client.class.getResource("orm.xml");
            pluggability_contracts_resource_local.addAsManifestResource(parURL, "orm.xml");
            // Mapping files
            parURL = Client.class.getResource("myMappingFile1.xml");
            pluggability_contracts_resource_local.addAsManifestResource(parURL, "myMappingFile1.xml");
            parURL = Client.class.getResource("myMappingFile2.xml");
            pluggability_contracts_resource_local.addAsManifestResource(parURL, "myMappingFile2.xml");

            // jpa_alternate_provider.jar
            JavaArchive jpa_alternate_provider = ShrinkWrap.create(JavaArchive.class, "jpa_alternate_provider.jar");
            jpa_alternate_provider.addPackage("ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation");
            parURL = Client.class.getResource("/ee/jakarta/tck/persistence/common/pluggability/altprovider/METAINF/services/jakarta.persistence.spi.PersistenceProvider");
            jpa_alternate_provider.addAsServiceProvider(jakarta.persistence.spi.PersistenceProvider.class, ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.PersistenceProvider.class);

        // Ear
            EnterpriseArchive pluggability_contracts_resource_local_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "pluggability_contracts_resource_local_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            pluggability_contracts_resource_local_vehicles_ear.addAsModule(pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb);
            pluggability_contracts_resource_local_vehicles_ear.addAsModule(pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client);

            pluggability_contracts_resource_local_vehicles_ear.addAsLibrary(pluggability_contracts_resource_local);
            pluggability_contracts_resource_local_vehicles_ear.addAsLibrary(jpa_alternate_provider);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/pluggability/contracts/resource_local/");
            if(earResURL != null) {
              pluggability_contracts_resource_local_vehicles_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/pluggability/contracts/resource_local/.ear.sun-application.xml");
            if(earResURL != null) {
              pluggability_contracts_resource_local_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(pluggability_contracts_resource_local_vehicles_ear, Client.class, earResURL);
        return pluggability_contracts_resource_local_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void createEMF() throws java.lang.Exception {
            super.createEMF();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getPersistenceProviderClassName() throws java.lang.Exception {
            super.getPersistenceProviderClassName();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getPersistenceUnitNameTest() throws java.lang.Exception {
            super.getPersistenceUnitNameTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getTransactionType() throws java.lang.Exception {
            super.getTransactionType();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getManagedClassNames() throws java.lang.Exception {
            super.getManagedClassNames();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getJarFileUrls() throws java.lang.Exception {
            super.getJarFileUrls();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getPersistenceUnitRootUrl() throws java.lang.Exception {
            super.getPersistenceUnitRootUrl();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getPersistenceXMLSchemaVersion() throws java.lang.Exception {
            super.getPersistenceXMLSchemaVersion();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getProperties() throws java.lang.Exception {
            super.getProperties();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getSharedCacheMode() throws java.lang.Exception {
            super.getSharedCacheMode();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getValidationMode() throws java.lang.Exception {
            super.getValidationMode();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getClassLoader() throws java.lang.Exception {
            super.getClassLoader();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getNewTempClassLoader() throws java.lang.Exception {
            super.getNewTempClassLoader();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getMappingFileNames() throws java.lang.Exception {
            super.getMappingFileNames();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getNonJtaDataSource() throws java.lang.Exception {
            super.getNonJtaDataSource();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void excludeUnlistedClasses() throws java.lang.Exception {
            super.excludeUnlistedClasses();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getProviderUtil() throws java.lang.Exception {
            super.getProviderUtil();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void isLoaded() throws java.lang.Exception {
            super.isLoaded();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void isLoadedWithoutReference() throws java.lang.Exception {
            super.isLoadedWithoutReference();
        }

}
