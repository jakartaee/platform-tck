package ee.jakarta.tck.persistence.ee.pluggability.contracts.jta;

import ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Client;
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
@Tag("tck-appclient")

public class ClientStateful3Test extends ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Client {
    static final String VEHICLE_ARCHIVE = "jpa_ee_pluggability_contracts_jta_stateful3_vehicle";

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
        Client:

        /com/sun/ts/tests/common/vehicle/stateful3/stateful3_vehicle_client.xml
        Ejb:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_ee_pluggability_contracts_jta_vehicles_client.jar");
            // The class files
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleRunner.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            Client.class,
            ClientStateful3Test.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/stateful3/stateful3_vehicle_client.xml");
            if(resURL != null) {
              jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/stateful3/stateful3_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb.jar");
            // The class files
            jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ee.jakarta.tck.persistence.common.PMClientBase.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleBean.class,
                com.sun.ts.tests.common.vehicle.stateful3.Stateful3VehicleIF.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                ee.jakarta.tck.persistence.ee.pluggability.contracts.jta.Client.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/stateful3/stateful3_vehicle_client.xml");
            if(ejbResURL != null) {
              jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/vehicle/stateful3/stateful3_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb, Client.class, ejbResURL);

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
            // The orm.xml file
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_ee_pluggability_contracts_jta.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_ee_pluggability_contracts_jta_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_pluggability_contracts_jta_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_ee_pluggability_contracts_jta_vehicles_ear.addAsModule(jpa_ee_pluggability_contracts_jta_stateful3_vehicle_ejb);
            jpa_ee_pluggability_contracts_jta_vehicles_ear.addAsModule(jpa_ee_pluggability_contracts_jta_stateful3_vehicle_client);

            jpa_ee_pluggability_contracts_jta_vehicles_ear.addAsLibrary(jpa_ee_pluggability_contracts_jta);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/pluggability/contracts/jta/");
            if(earResURL != null) {
              jpa_ee_pluggability_contracts_jta_vehicles_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/pluggability/contracts/jta/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_ee_pluggability_contracts_jta_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_ee_pluggability_contracts_jta_vehicles_ear, Client.class, earResURL);
        return jpa_ee_pluggability_contracts_jta_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("stateful3")
        public void createEMF() throws java.lang.Exception {
            super.createEMF();
        }

        @Test
        @Override
        @TargetVehicle("stateful3")
        public void getPersistenceProviderClassName() throws java.lang.Exception {
            super.getPersistenceProviderClassName();
        }

        @Test
        @Override
        @TargetVehicle("stateful3")
        public void getPersistenceUnitNameTest() throws java.lang.Exception {
            super.getPersistenceUnitNameTest();
        }

        @Test
        @Override
        @TargetVehicle("stateful3")
        public void getTransactionType() throws java.lang.Exception {
            super.getTransactionType();
        }

        @Test
        @Override
        @TargetVehicle("stateful3")
        public void getManagedClassNames() throws java.lang.Exception {
            super.getManagedClassNames();
        }

        @Test
        @Override
        @TargetVehicle("stateful3")
        public void getJarFileUrls() throws java.lang.Exception {
            super.getJarFileUrls();
        }

        @Test
        @Override
        @TargetVehicle("stateful3")
        public void getPersistenceUnitRootUrl() throws java.lang.Exception {
            super.getPersistenceUnitRootUrl();
        }

        @Test
        @Override
        @TargetVehicle("stateful3")
        public void getPersistenceXMLSchemaVersion() throws java.lang.Exception {
            super.getPersistenceXMLSchemaVersion();
        }

        @Test
        @Override
        @TargetVehicle("stateful3")
        public void getProperties() throws java.lang.Exception {
            super.getProperties();
        }

        @Test
        @Override
        @TargetVehicle("stateful3")
        public void getSharedCacheMode() throws java.lang.Exception {
            super.getSharedCacheMode();
        }

        @Test
        @Override
        @TargetVehicle("stateful3")
        public void getValidationMode() throws java.lang.Exception {
            super.getValidationMode();
        }

        @Test
        @Override
        @TargetVehicle("stateful3")
        public void getClassLoader() throws java.lang.Exception {
            super.getClassLoader();
        }

        @Test
        @Override
        @TargetVehicle("stateful3")
        public void getNewTempClassLoader() throws java.lang.Exception {
            super.getNewTempClassLoader();
        }

        @Test
        @Override
        @TargetVehicle("stateful3")
        public void getMappingFileNames() throws java.lang.Exception {
            super.getMappingFileNames();
        }

        @Test
        @Override
        @TargetVehicle("stateful3")
        public void getJtaDataSource() throws java.lang.Exception {
            super.getJtaDataSource();
        }

        @Test
        @Override
        @TargetVehicle("stateful3")
        public void excludeUnlistedClasses() throws java.lang.Exception {
            super.excludeUnlistedClasses();
        }

        @Test
        @Override
        @TargetVehicle("stateful3")
        public void getProviderUtil() throws java.lang.Exception {
            super.getProviderUtil();
        }

        @Test
        @Override
        @TargetVehicle("stateful3")
        public void isLoaded() throws java.lang.Exception {
            super.isLoaded();
        }

}
