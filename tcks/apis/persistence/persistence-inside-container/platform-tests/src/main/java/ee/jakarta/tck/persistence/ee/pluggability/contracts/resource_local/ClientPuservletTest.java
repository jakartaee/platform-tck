package ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local;

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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

import java.net.URL;



@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")

public class ClientPuservletTest extends ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Client {
    static final String VEHICLE_ARCHIVE = "pluggability_contracts_resource_local_puservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        pluggability_contracts_resource_local: META-INF/myMappingFile1.xml,META-INF/myMappingFile2.xml,META-INF/orm.xml,META-INF/persistence.xml
        pluggability_contracts_resource_local_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        pluggability_contracts_resource_local_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        pluggability_contracts_resource_local_puservlet_vehicle_web: WEB-INF/web.xml
        pluggability_contracts_resource_local_vehicles: 

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
            WebArchive pluggability_contracts_resource_local_puservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "pluggability_contracts_resource_local_puservlet_vehicle_web.war");
            // The class files
            pluggability_contracts_resource_local_puservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class,
            Fault.class,
            ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Client.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            pluggability_contracts_resource_local_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            // The sun-web.xml descriptor
            warResURL = null;
           // Call the archive processor
           archiveProcessor.processWebArchive(pluggability_contracts_resource_local_puservlet_vehicle_web, Client.class, warResURL);

        // Par
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
        pluggability_contracts_resource_local_puservlet_vehicle_web.addAsLibrary(pluggability_contracts_resource_local);

        // jpa_alternate_provider.jar
        JavaArchive jpa_alternate_provider = ShrinkWrap.create(JavaArchive.class, "jpa_alternate_provider.jar");
        jpa_alternate_provider.addPackage("ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation");
        parURL = Client.class.getResource("/ee/jakarta/tck/persistence/common/pluggability/altprovider/METAINF/services/jakarta.persistence.spi.PersistenceProvider");
        jpa_alternate_provider.addAsServiceProvider(jakarta.persistence.spi.PersistenceProvider.class, ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.PersistenceProvider.class);
        pluggability_contracts_resource_local_puservlet_vehicle_web.addAsLibrary(jpa_alternate_provider);

        return pluggability_contracts_resource_local_puservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void createEMF() throws java.lang.Exception {
            super.createEMF();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getPersistenceProviderClassName() throws java.lang.Exception {
            super.getPersistenceProviderClassName();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getPersistenceUnitNameTest() throws java.lang.Exception {
            super.getPersistenceUnitNameTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getTransactionType() throws java.lang.Exception {
            super.getTransactionType();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getManagedClassNames() throws java.lang.Exception {
            super.getManagedClassNames();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getJarFileUrls() throws java.lang.Exception {
            super.getJarFileUrls();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getPersistenceUnitRootUrl() throws java.lang.Exception {
            super.getPersistenceUnitRootUrl();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getPersistenceXMLSchemaVersion() throws java.lang.Exception {
            super.getPersistenceXMLSchemaVersion();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getProperties() throws java.lang.Exception {
            super.getProperties();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getSharedCacheMode() throws java.lang.Exception {
            super.getSharedCacheMode();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getValidationMode() throws java.lang.Exception {
            super.getValidationMode();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getClassLoader() throws java.lang.Exception {
            super.getClassLoader();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getNewTempClassLoader() throws java.lang.Exception {
            super.getNewTempClassLoader();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getMappingFileNames() throws java.lang.Exception {
            super.getMappingFileNames();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getNonJtaDataSource() throws java.lang.Exception {
            super.getNonJtaDataSource();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void excludeUnlistedClasses() throws java.lang.Exception {
            super.excludeUnlistedClasses();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getProviderUtil() throws java.lang.Exception {
            super.getProviderUtil();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void isLoaded() throws java.lang.Exception {
            super.isLoaded();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void isLoadedWithoutReference() throws java.lang.Exception {
            super.isLoadedWithoutReference();
        }


}