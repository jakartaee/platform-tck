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
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
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
@Tag("web")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientPuservletTest extends ee.jakarta.tck.persistence.core.enums.Client {
    static final String VEHICLE_ARCHIVE = "jpa_core_enums_puservlet_vehicle";

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
            WebArchive jpa_core_enums_puservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_enums_puservlet_vehicle_web.war");
            // The class files
            jpa_core_enums_puservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            ee.jakarta.tck.persistence.core.enums.Client.class,
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
            if(warResURL != null) {
              jpa_core_enums_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_enums_puservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/core/enums/jpa_core_enums.jar");
            if(warResURL != null) {
              jpa_core_enums_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_enums.jar");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_enums_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/puservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_enums_puservlet_vehicle_web, Client.class, warResURL);


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

            jpa_core_enums_puservlet_vehicle_web.addAsLibrary(jpa_core_enums);
            return jpa_core_enums_puservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void accessTypeValueOfTest() throws java.lang.Exception {
            super.accessTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void accessTypeValuesTest() throws java.lang.Exception {
            super.accessTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void cacheRetrieveModeValueOfTest() throws java.lang.Exception {
            super.cacheRetrieveModeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void cacheRetrieveModeValuesTest() throws java.lang.Exception {
            super.cacheRetrieveModeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void cacheStoreModeValueOfTest() throws java.lang.Exception {
            super.cacheStoreModeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void cacheStoreModeValuesTest() throws java.lang.Exception {
            super.cacheStoreModeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void cascadeTypeValueOfTest() throws java.lang.Exception {
            super.cascadeTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void cascadeTypeValuesTest() throws java.lang.Exception {
            super.cascadeTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void discriminatorTypeValueOfTest() throws java.lang.Exception {
            super.discriminatorTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void discriminatorTypeValuesTest() throws java.lang.Exception {
            super.discriminatorTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void enumTypeValueOfTest() throws java.lang.Exception {
            super.enumTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void enumTypeValuesTest() throws java.lang.Exception {
            super.enumTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void fetchTypeValueOfTest() throws java.lang.Exception {
            super.fetchTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void fetchTypeValuesTest() throws java.lang.Exception {
            super.fetchTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void flushModeTypeValueOfTest() throws java.lang.Exception {
            super.flushModeTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void flushModeTypeValuesTest() throws java.lang.Exception {
            super.flushModeTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void setgetFlushModeEntityManagerTest() throws java.lang.Exception {
            super.setgetFlushModeEntityManagerTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void setgetFlushModeTest() throws java.lang.Exception {
            super.setgetFlushModeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void setgetFlushModeTQTest() throws java.lang.Exception {
            super.setgetFlushModeTQTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void generationTypeValueOfTest() throws java.lang.Exception {
            super.generationTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void generationTypeValuesTest() throws java.lang.Exception {
            super.generationTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void inheritanceTypeValueOfTest() throws java.lang.Exception {
            super.inheritanceTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void inheritanceTypeValuesTest() throws java.lang.Exception {
            super.inheritanceTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void lockModeTypeValueOfTest() throws java.lang.Exception {
            super.lockModeTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void lockModeTypeValuesTest() throws java.lang.Exception {
            super.lockModeTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void persistenceContextTypeValueOfTest() throws java.lang.Exception {
            super.persistenceContextTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void persistenceContextTypeValuesTest() throws java.lang.Exception {
            super.persistenceContextTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void pessimisticLockScopeValueOfTest() throws java.lang.Exception {
            super.pessimisticLockScopeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void pessimisticLockScopeValuesTest() throws java.lang.Exception {
            super.pessimisticLockScopeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void sharedCacheModeValueOfTest() throws java.lang.Exception {
            super.sharedCacheModeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void sharedCacheModeValuesTest() throws java.lang.Exception {
            super.sharedCacheModeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void validationModeValueOfTest() throws java.lang.Exception {
            super.validationModeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void validationModeValuesTest() throws java.lang.Exception {
            super.validationModeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void temporalTypeValuesTest() throws java.lang.Exception {
            super.temporalTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void temporalTypeValueOfTest() throws java.lang.Exception {
            super.temporalTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinTypeValuesTest() throws java.lang.Exception {
            super.joinTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinTypeValueOfTest() throws java.lang.Exception {
            super.joinTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void persistentAttributeTypeValuesTest() throws java.lang.Exception {
            super.persistentAttributeTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void persistentAttributeTypeValueOfTest() throws java.lang.Exception {
            super.persistentAttributeTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void bindableTypeValuesTest() throws java.lang.Exception {
            super.bindableTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void bindableTypeValueOfTest() throws java.lang.Exception {
            super.bindableTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void collectionTypeValuesTest() throws java.lang.Exception {
            super.collectionTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void collectionTypeValueOfTest() throws java.lang.Exception {
            super.collectionTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void persistenceTypeValuesTest() throws java.lang.Exception {
            super.persistenceTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void persistenceTypeValueOfTest() throws java.lang.Exception {
            super.persistenceTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void loadStateValuesTest() throws java.lang.Exception {
            super.loadStateValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void loadStateValueOfTest() throws java.lang.Exception {
            super.loadStateValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void persistenceUnitTransactionTypeValuesTest() throws java.lang.Exception {
            super.persistenceUnitTransactionTypeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void persistenceUnitTransactionTypeValueOfTest() throws java.lang.Exception {
            super.persistenceUnitTransactionTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void parameterModeValuesTest() throws java.lang.Exception {
            super.parameterModeValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void parameterModeValueOfTest() throws java.lang.Exception {
            super.parameterModeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void synchronizationTypeValueOfTest() throws java.lang.Exception {
            super.synchronizationTypeValueOfTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void synchronizationTypeValuesTest() throws java.lang.Exception {
            super.synchronizationTypeValuesTest();
        }


}