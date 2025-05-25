package ee.jakarta.tck.persistence.ee.pluggability.contracts.jta;

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

public class ClientPmservletTest extends ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Client {
    static final String VEHICLE_ARCHIVE = "jpa_ee_pluggability_contracts_jta_pmservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_ee_pluggability_contracts_jta: META-INF/persistence.xml
        jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_ee_pluggability_contracts_jta_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_ee_pluggability_contracts_jta_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_ee_pluggability_contracts_jta_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_ee_pluggability_contracts_jta_vehicles: 

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
            WebArchive jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web.war");
            // The class files
            jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class,
            ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Client.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // Par
                // the jar with the correct archive name
                JavaArchive jpa_ee_pluggability_contracts_jta = ShrinkWrap.create(JavaArchive.class, "jpa_ee_pluggability_contracts_jta.jar");
                // The class files
                jpa_ee_pluggability_contracts_jta.addClasses(
                    ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Order3.class,
                    ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Order2.class,
                    ee.jakarta.tck.persistence.common.pluggability.util.LogRecordEntry.class,
                    ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Order.class,
                    ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Order4.class,
                    ee.jakarta.tck.persistence.common.pluggability.util.LogFileProcessor.class
                );
                // The persistence.xml descriptor
                URL parURL = Client.class.getResource("persistence.xml");
                if(parURL != null) {
                  jpa_ee_pluggability_contracts_jta.addAsManifestResource(parURL, "persistence.xml");
                }
                // Call the archive processor
                archiveProcessor.processParArchive(jpa_ee_pluggability_contracts_jta, Client.class, parURL);
                // The orm.xml and mapping files
                parURL = Client.class.getResource("orm.xml");
                jpa_ee_pluggability_contracts_jta.addAsManifestResource(parURL, "orm.xml");
                parURL = Client.class.getResource("myMappingFile1.xml");
                jpa_ee_pluggability_contracts_jta.addAsManifestResource(parURL, "myMappingFile1.xml");
                parURL = Client.class.getResource("myMappingFile2.xml");
                jpa_ee_pluggability_contracts_jta.addAsManifestResource(parURL, "myMappingFile2.xml");

            JavaArchive jpa_alternate_provider = ShrinkWrap.create(JavaArchive.class,"jpa_alternate_provider.jar");
            jpa_alternate_provider.addClasses(
                    ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.CacheImpl.class,
                    ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.ClassTransformerImpl.class,
                    ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.EntityManagerFactoryImpl.class,
                    ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.EntityManagerImpl.class,
                    ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.EntityTransactionImpl.class,
                    ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.PersistenceProvider.class,
                    ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.PersistenceUnitInfoImpl.class,
                    ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.QueryImpl.class,
                    ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.TSLogger.class,
                    ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.TSLogRecord.class,
                    ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.TSXMLFormatter.class
            ).addAsResource(
                    new StringAsset("ee.jakarta.tck.persistence.common.pluggability.altprovider.implementation.PersistenceProvider"),
                    "META-INF/services/jakarta.persistence.spi.PersistenceProvider");
            // Add the WAR libraries (alternative Persistence Provider and Persistence archive)
            jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web.addAsLibraries(jpa_alternate_provider, jpa_ee_pluggability_contracts_jta);

            // The web.xml descriptor
            // The sun-web.xml descriptor

            URL warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web, Client.class, warResURL);


            jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web.addAsLibrary(jpa_alternate_provider);
            return jpa_ee_pluggability_contracts_jta_pmservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void createEMF() throws java.lang.Exception {
            super.createEMF();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getPersistenceProviderClassName() throws java.lang.Exception {
            super.getPersistenceProviderClassName();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getPersistenceUnitNameTest() throws java.lang.Exception {
            super.getPersistenceUnitNameTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getTransactionType() throws java.lang.Exception {
            super.getTransactionType();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getManagedClassNames() throws java.lang.Exception {
            super.getManagedClassNames();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getJarFileUrls() throws java.lang.Exception {
            super.getJarFileUrls();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getPersistenceUnitRootUrl() throws java.lang.Exception {
            super.getPersistenceUnitRootUrl();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getPersistenceXMLSchemaVersion() throws java.lang.Exception {
            super.getPersistenceXMLSchemaVersion();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getProperties() throws java.lang.Exception {
            super.getProperties();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getSharedCacheMode() throws java.lang.Exception {
            super.getSharedCacheMode();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getValidationMode() throws java.lang.Exception {
            super.getValidationMode();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getClassLoader() throws java.lang.Exception {
            super.getClassLoader();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getNewTempClassLoader() throws java.lang.Exception {
            super.getNewTempClassLoader();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getMappingFileNames() throws java.lang.Exception {
            super.getMappingFileNames();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getJtaDataSource() throws java.lang.Exception {
            super.getJtaDataSource();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void excludeUnlistedClasses() throws java.lang.Exception {
            super.excludeUnlistedClasses();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getProviderUtil() throws java.lang.Exception {
            super.getProviderUtil();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void isLoaded() throws java.lang.Exception {
            super.isLoaded();
        }


}