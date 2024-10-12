package ee.jakarta.tck.persistence.core.entityManager;

import ee.jakarta.tck.persistence.core.entityManager.Client1;
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
public class Client1AppmanagedTest extends ee.jakarta.tck.persistence.core.entityManager.Client1 {
    static final String VEHICLE_ARCHIVE = "jpa_core_entityManager_appmanaged_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_entityManager: META-INF/persistence.xml
        jpa_core_entityManager_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_entityManager_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_entityManager_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_entityManager_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_entityManager_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_entityManager_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_entityManager_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_entityManager_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_entityManager_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_entityManager_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_entityManager_vehicles: 
        jpa_core_entityManager2: META-INF/persistence.xml
        jpa_core_entityManager2_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_entityManager2_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_entityManager2_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_entityManager2_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_entityManager2_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_entityManager2_vehicles: 
        jpa_core_entityManagerFactory: META-INF/persistence.xml
        jpa_core_entityManagerFactory_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_entityManagerFactory_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_entityManagerFactory_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_entityManagerFactory_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_entityManagerFactory_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_entityManagerFactory_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_entityManagerFactory_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_entityManagerFactory_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_entityManagerFactory_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_entityManagerFactory_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_entityManagerFactory_vehicles: 
        jpa_core_entityManagerFactoryCloseException: META-INF/persistence.xml
        jpa_core_entityManagerFactoryCloseException_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_entityManagerFactoryCloseException_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_entityManagerFactoryCloseException_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_entityManagerFactoryCloseException_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_entityManagerFactoryCloseException_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_entityManagerFactoryCloseException_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_entityManagerFactoryCloseException_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_entityManagerFactoryCloseException_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_entityManagerFactoryCloseException_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_entityManagerFactoryCloseException_vehicles: 

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
            JavaArchive jpa_core_entityManager_appmanaged_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_core_entityManager_vehicles_client.jar");
            // The class files
            jpa_core_entityManager_appmanaged_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            ee.jakarta.tck.persistence.core.entityManager.Order.class,
            ee.jakarta.tck.persistence.core.entityManager.Employee.class,
            Client1.class,
            Client1AppmanagedTest.class
            );
            // The application-client.xml descriptor
            URL resURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_client.xml");
            if(resURL != null) {
              jpa_core_entityManager_appmanaged_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client1.class.getResource("//com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_core_entityManager_appmanaged_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jpa_core_entityManager_appmanaged_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client1.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_core_entityManager_appmanaged_vehicle_client, Client1.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_core_entityManager_appmanaged_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_core_entityManager_appmanaged_vehicle_ejb.jar");
            // The class files
            jpa_core_entityManager_appmanaged_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class,
                ee.jakarta.tck.persistence.core.entityManager.Client1.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ee.jakarta.tck.persistence.common.PMClientBase.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleBean.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client1.class.getResource("//vehicle/appmanaged/appmanaged_vehicle_ejb.xml");
            if(ejbResURL1 != null) {
              jpa_core_entityManager_appmanaged_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client1.class.getResource("//vehicle/appmanaged/appmanaged_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_core_entityManager_appmanaged_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_core_entityManager_appmanaged_vehicle_ejb, Client1.class, ejbResURL1);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_entityManager = ShrinkWrap.create(JavaArchive.class, "jpa_core_entityManager.jar");
            // The class files
            jpa_core_entityManager.addClasses(
                ee.jakarta.tck.persistence.core.entityManager.Employee.class,
                ee.jakarta.tck.persistence.core.entityManager.Order.class
            );
            // The persistence.xml descriptor
            URL parURL = Client1.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_entityManager.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client1.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_entityManager.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client1.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_entityManager.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client1.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_entityManager.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_entityManager, Client1.class, parURL);
            parURL = Client1.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_entityManager.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_entityManager_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_entityManager_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_entityManager_vehicles_ear.addAsModule(jpa_core_entityManager_appmanaged_vehicle_ejb);
            jpa_core_entityManager_vehicles_ear.addAsModule(jpa_core_entityManager_appmanaged_vehicle_client);

            jpa_core_entityManager_vehicles_ear.addAsLibrary(jpa_core_entityManager);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client1.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_entityManager_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_entityManager_vehicles_ear, Client1.class, earResURL);
        return jpa_core_entityManager_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void autoCloseableTest() throws java.lang.Exception {
            super.autoCloseableTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void mergeTest() throws java.lang.Exception {
            super.mergeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void mergeExceptionsTest() throws java.lang.Exception {
            super.mergeExceptionsTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void removeExceptionsTest() throws java.lang.Exception {
            super.removeExceptionsTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void lockIllegalStateExceptionTest() throws java.lang.Exception {
            super.lockIllegalStateExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void refreshInvalidObjectIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.refreshInvalidObjectIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void refreshNonManagedObjectIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.refreshNonManagedObjectIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void refreshInvalidObjectMapIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.refreshInvalidObjectMapIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void refreshNonManagedObjectMapIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.refreshNonManagedObjectMapIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void refreshInvalidObjectLockModeTypeIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.refreshInvalidObjectLockModeTypeIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void refreshNonManagedObjectLockModeTypeIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.refreshNonManagedObjectLockModeTypeIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void refreshInvalidObjectLockModeTypeMapIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.refreshInvalidObjectLockModeTypeMapIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void refreshNonManagedObjectLockModeTypeMapIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.refreshNonManagedObjectLockModeTypeMapIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void containsIllegalArgumentException() throws java.lang.Exception {
            super.containsIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void createNamedQueryIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.createNamedQueryIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void createQueryIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.createQueryIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void detachIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.detachIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void getEntityManagerFactoryTest() throws java.lang.Exception {
            super.getEntityManagerFactoryTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void emGetMetamodelTest() throws java.lang.Exception {
            super.emGetMetamodelTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void setPropertyTest() throws java.lang.Exception {
            super.setPropertyTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void getCriteriaBuilderTest() throws java.lang.Exception {
            super.getCriteriaBuilderTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void isJoinedToTransactionTest() throws java.lang.Exception {
            super.isJoinedToTransactionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void createStoredProcedureQueryStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.createStoredProcedureQueryStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void createStoredProcedureQueryStringClassArrayIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.createStoredProcedureQueryStringClassArrayIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void createStoredProcedureQueryStringStringArrayIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.createStoredProcedureQueryStringStringArrayIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void createNamedStoredProcedureQueryStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.createNamedStoredProcedureQueryStringIllegalArgumentExceptionTest();
        }

}
