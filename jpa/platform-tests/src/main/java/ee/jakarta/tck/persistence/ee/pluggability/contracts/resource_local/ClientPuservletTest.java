package ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local;

import ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Client;
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
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
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
            com.sun.ts.lib.harness.EETest.Fault.class,
            ee.jakarta.tck.persistence.ee.pluggability.contracts.resource_local.Client.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              pluggability_contracts_resource_local_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("//com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              pluggability_contracts_resource_local_puservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
           archiveProcessor.processWebArchive(pluggability_contracts_resource_local_puservlet_vehicle_web, Client.class, warResURL);

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
            if(parURL != null) {
              pluggability_contracts_resource_local.addAsManifestResource(parURL, "persistence.xml");
            }
            archiveProcessor.processParArchive(pluggability_contracts_resource_local, Client.class, parURL);
            // The orm.xml file
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              pluggability_contracts_resource_local.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive pluggability_contracts_resource_local_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "pluggability_contracts_resource_local_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            pluggability_contracts_resource_local_vehicles_ear.addAsModule(pluggability_contracts_resource_local_puservlet_vehicle_web);

            pluggability_contracts_resource_local_vehicles_ear.addAsLibrary(pluggability_contracts_resource_local);



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
            archiveProcessor.processEarArchive(pluggability_contracts_resource_local_vehicles_ear, Client.class, earResURL);
        return pluggability_contracts_resource_local_vehicles_ear;
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