package ee.jakarta.tck.persistence.core.enums;

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
public class ClientAppmanagedTest extends ee.jakarta.tck.persistence.core.enums.Client {
    static final String VEHICLE_ARCHIVE = "jpa_core_enums_appmanaged_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_enums: META-INF/persistence.xml
        jpa_core_enums_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_enums_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_enums_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_enums_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_enums_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_enums_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_enums_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_enums_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_enums_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_enums_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_enums_vehicles: 

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
            JavaArchive jpa_core_enums_appmanaged_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_core_enums_vehicles_client.jar");
            // The class files
            jpa_core_enums_appmanaged_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.web.AltWebVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            Client.class,
            ClientAppmanagedTest.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_client.xml");
            if(resURL != null) {
              jpa_core_enums_appmanaged_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_core_enums_appmanaged_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jpa_core_enums_appmanaged_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + com.sun.ts.tests.common.vehicle.VehicleClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_core_enums_appmanaged_vehicle_client, Client.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_core_enums_appmanaged_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_core_enums_appmanaged_vehicle_ejb.jar");
            // The class files
            jpa_core_enums_appmanaged_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                Fault.class,
                com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ee.jakarta.tck.persistence.common.PMClientBase.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleBean.class,
                ee.jakarta.tck.persistence.core.enums.Client.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                EETest.class,
                ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client.class.getResource("/com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_client.xml");
            if(ejbResURL1 != null) {
//              jpa_core_enums_appmanaged_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client.class.getResource("/com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_core_enums_appmanaged_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_core_enums_appmanaged_vehicle_ejb, Client.class, ejbResURL1);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_enums = ShrinkWrap.create(JavaArchive.class, "jpa_core_enums.jar");
            // The class files
            jpa_core_enums.addClasses(
                ee.jakarta.tck.persistence.core.enums.Order.class
            );
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_enums.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_enums.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_enums.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_enums.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_enums, Client.class, parURL);
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_enums.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_enums_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_enums_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_enums_vehicles_ear.addAsModule(jpa_core_enums_appmanaged_vehicle_ejb);
            jpa_core_enums_vehicles_ear.addAsModule(jpa_core_enums_appmanaged_vehicle_client);

            jpa_core_enums_vehicles_ear.addAsLibrary(jpa_core_enums);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_enums_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_enums_vehicles_ear, Client.class, earResURL);
        return jpa_core_enums_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void accessTypeValueOfTest() throws java.lang.Exception {
            super.accessTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void accessTypeValuesTest() throws java.lang.Exception {
            super.accessTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void cacheRetrieveModeValueOfTest() throws java.lang.Exception {
            super.cacheRetrieveModeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void cacheRetrieveModeValuesTest() throws java.lang.Exception {
            super.cacheRetrieveModeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void cacheStoreModeValueOfTest() throws java.lang.Exception {
            super.cacheStoreModeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void cacheStoreModeValuesTest() throws java.lang.Exception {
            super.cacheStoreModeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void cascadeTypeValueOfTest() throws java.lang.Exception {
            super.cascadeTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void cascadeTypeValuesTest() throws java.lang.Exception {
            super.cascadeTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void discriminatorTypeValueOfTest() throws java.lang.Exception {
            super.discriminatorTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void discriminatorTypeValuesTest() throws java.lang.Exception {
            super.discriminatorTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void enumTypeValueOfTest() throws java.lang.Exception {
            super.enumTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void enumTypeValuesTest() throws java.lang.Exception {
            super.enumTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void fetchTypeValueOfTest() throws java.lang.Exception {
            super.fetchTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void fetchTypeValuesTest() throws java.lang.Exception {
            super.fetchTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void flushModeTypeValueOfTest() throws java.lang.Exception {
            super.flushModeTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void flushModeTypeValuesTest() throws java.lang.Exception {
            super.flushModeTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void setgetFlushModeEntityManagerTest() throws java.lang.Exception {
            super.setgetFlushModeEntityManagerTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void setgetFlushModeTest() throws java.lang.Exception {
            super.setgetFlushModeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void setgetFlushModeTQTest() throws java.lang.Exception {
            super.setgetFlushModeTQTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void generationTypeValueOfTest() throws java.lang.Exception {
            super.generationTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void generationTypeValuesTest() throws java.lang.Exception {
            super.generationTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void inheritanceTypeValueOfTest() throws java.lang.Exception {
            super.inheritanceTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void inheritanceTypeValuesTest() throws java.lang.Exception {
            super.inheritanceTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void lockModeTypeValueOfTest() throws java.lang.Exception {
            super.lockModeTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void lockModeTypeValuesTest() throws java.lang.Exception {
            super.lockModeTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void persistenceContextTypeValueOfTest() throws java.lang.Exception {
            super.persistenceContextTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void persistenceContextTypeValuesTest() throws java.lang.Exception {
            super.persistenceContextTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void pessimisticLockScopeValueOfTest() throws java.lang.Exception {
            super.pessimisticLockScopeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void pessimisticLockScopeValuesTest() throws java.lang.Exception {
            super.pessimisticLockScopeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void sharedCacheModeValueOfTest() throws java.lang.Exception {
            super.sharedCacheModeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void sharedCacheModeValuesTest() throws java.lang.Exception {
            super.sharedCacheModeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void validationModeValueOfTest() throws java.lang.Exception {
            super.validationModeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void validationModeValuesTest() throws java.lang.Exception {
            super.validationModeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void temporalTypeValuesTest() throws java.lang.Exception {
            super.temporalTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void temporalTypeValueOfTest() throws java.lang.Exception {
            super.temporalTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void joinTypeValuesTest() throws java.lang.Exception {
            super.joinTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void joinTypeValueOfTest() throws java.lang.Exception {
            super.joinTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void persistentAttributeTypeValuesTest() throws java.lang.Exception {
            super.persistentAttributeTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void persistentAttributeTypeValueOfTest() throws java.lang.Exception {
            super.persistentAttributeTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void bindableTypeValuesTest() throws java.lang.Exception {
            super.bindableTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void bindableTypeValueOfTest() throws java.lang.Exception {
            super.bindableTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void collectionTypeValuesTest() throws java.lang.Exception {
            super.collectionTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void collectionTypeValueOfTest() throws java.lang.Exception {
            super.collectionTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void persistenceTypeValuesTest() throws java.lang.Exception {
            super.persistenceTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void persistenceTypeValueOfTest() throws java.lang.Exception {
            super.persistenceTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void loadStateValuesTest() throws java.lang.Exception {
            super.loadStateValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void loadStateValueOfTest() throws java.lang.Exception {
            super.loadStateValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void persistenceUnitTransactionTypeValuesTest() throws java.lang.Exception {
            super.persistenceUnitTransactionTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void persistenceUnitTransactionTypeValueOfTest() throws java.lang.Exception {
            super.persistenceUnitTransactionTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void parameterModeValuesTest() throws java.lang.Exception {
            super.parameterModeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void parameterModeValueOfTest() throws java.lang.Exception {
            super.parameterModeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void synchronizationTypeValueOfTest() throws java.lang.Exception {
            super.synchronizationTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void synchronizationTypeValuesTest() throws java.lang.Exception {
            super.synchronizationTypeValuesTest();
        }

}
