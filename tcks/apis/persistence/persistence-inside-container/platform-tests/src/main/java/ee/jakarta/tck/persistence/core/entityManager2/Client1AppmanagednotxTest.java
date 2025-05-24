package ee.jakarta.tck.persistence.core.entityManager2;

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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

import java.net.URL;
import java.util.Properties;


@ExtendWith(ArquillianExtension.class)
@Tag("persistence")
@Tag("platform")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class Client1AppmanagednotxTest extends ee.jakarta.tck.persistence.core.entityManager2.Client1 {
    static final String VEHICLE_ARCHIVE = "jpa_core_entityManager2_appmanagedNoTx_vehicle";

    public static void main(String[] args) {
      Client1AppmanagednotxTest theTests = new Client1AppmanagednotxTest();
      Status s = theTests.run(args, System.out, System.err);
      s.exit();
    }

    public void setup(String[] args, Properties p) throws Exception {
        super.setup(args, p);
    }

        /**
        EE10 Deployment Descriptors:
        jpa_core_entityManager2: META-INF/persistence.xml
        jpa_core_entityManager2_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_entityManager2_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_entityManager2_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_entityManager2_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_entityManager2_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_entityManager2_vehicles: 

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
            JavaArchive jpa_core_entityManager2_appmanagedNoTx_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_core_entityManager2_vehicles_client.jar");
            // The class files
            jpa_core_entityManager2_appmanagedNoTx_vehicle_client.addClasses(
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
            ee.jakarta.tck.persistence.core.entityManager2.Employee.class,
            ee.jakarta.tck.persistence.core.entityManager2.Order.class,
            Client1.class,
            Client1AppmanagednotxTest.class
            );
            // The application-client.xml descriptor
            URL resURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml");
            if(resURL != null) {
              jpa_core_entityManager2_appmanagedNoTx_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_core_entityManager2_appmanagedNoTx_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jpa_core_entityManager2_appmanagedNoTx_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + com.sun.ts.tests.common.vehicle.VehicleClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_core_entityManager2_appmanagedNoTx_vehicle_client, Client1.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb.jar");
            // The class files
            jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb.addClasses(
                ee.jakarta.tck.persistence.core.entityManager2.Client1.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                Fault.class,
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
            URL ejbResURL1 = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml");
            if(ejbResURL1 != null) {
//              jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb, Client1.class, ejbResURL1);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_entityManager2 = ShrinkWrap.create(JavaArchive.class, "jpa_core_entityManager2.jar");
            // The class files
            jpa_core_entityManager2.addClasses(
                ee.jakarta.tck.persistence.core.entityManager2.Employee.class,
                ee.jakarta.tck.persistence.core.entityManager2.Order.class,
                ee.jakarta.tck.persistence.core.entityManager2.DoesNotExist.class
            );
            // The persistence.xml descriptor
            URL parURL = Client1.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_entityManager2.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client1.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_entityManager2.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client1.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_entityManager2.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client1.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_entityManager2.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_entityManager2, Client1.class, parURL);
            parURL = Client1.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_entityManager2.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_entityManager2_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_entityManager2_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_entityManager2_vehicles_ear.addAsModule(jpa_core_entityManager2_appmanagedNoTx_vehicle_ejb);
            jpa_core_entityManager2_vehicles_ear.addAsModule(jpa_core_entityManager2_appmanagedNoTx_vehicle_client);

            jpa_core_entityManager2_vehicles_ear.addAsLibrary(jpa_core_entityManager2);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client1.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_entityManager2_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_entityManager2_vehicles_ear, Client1.class, earResURL);
        return jpa_core_entityManager2_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void findExceptionsTest() throws java.lang.Exception {
            super.findExceptionsTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void flushExceptionsTest() throws java.lang.Exception {
            super.flushExceptionsTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback1Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback1Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback2Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback2Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback3Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback3Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback4Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback4Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback5Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback5Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback6Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback6Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback7Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback7Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback8Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback8Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback9Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback9Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback10Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback10Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback11Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback11Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback12Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback12Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback13Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback13Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback14Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback14Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback15Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback15Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback16Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback16Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback17Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback17Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback21Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback21Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback23Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback23Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback24Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback24Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback25Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback25Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback26Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback26Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback27Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback27Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback28Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback28Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback29Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback29Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback30Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback30Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback31Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback31Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void entityManagerMethodsRuntimeExceptionsCauseRollback32Test() throws java.lang.Exception {
            super.entityManagerMethodsRuntimeExceptionsCauseRollback32Test();
        }

}
