package ee.jakarta.tck.persistence.core.entitytest.persist.oneXmany;

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


@ExtendWith(ArquillianExtension.class)
@Tag("persistence")
@Tag("platform")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientAppmanagednotxTest extends ee.jakarta.tck.persistence.core.entitytest.persist.oneXmany.Client {
    static final String VEHICLE_ARCHIVE = "jpa_core_et_persist_oneXmany_appmanagedNoTx_vehicle";

    public static void main(String[] args) {
      ClientAppmanagednotxTest theTests = new ClientAppmanagednotxTest();
      Status s = theTests.run(args, System.out, System.err);
      s.exit();
    }

        /**
        EE10 Deployment Descriptors:
        jpa_core_et_persist_oneXmany: META-INF/persistence.xml
        jpa_core_et_persist_oneXmany_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_et_persist_oneXmany_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_et_persist_oneXmany_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_et_persist_oneXmany_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_et_persist_oneXmany_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_et_persist_oneXmany_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_et_persist_oneXmany_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_et_persist_oneXmany_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_et_persist_oneXmany_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_et_persist_oneXmany_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_et_persist_oneXmany_vehicles: 
        jpa_core_et_persist_oneXmanyFetchEager: META-INF/persistence.xml
        jpa_core_et_persist_oneXmanyFetchEager_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_et_persist_oneXmanyFetchEager_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_et_persist_oneXmanyFetchEager_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_et_persist_oneXmanyFetchEager_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_et_persist_oneXmanyFetchEager_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_et_persist_oneXmanyFetchEager_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_et_persist_oneXmanyFetchEager_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_et_persist_oneXmanyFetchEager_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_et_persist_oneXmanyFetchEager_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_et_persist_oneXmanyFetchEager_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_et_persist_oneXmanyFetchEager_vehicles: 

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
            JavaArchive jpa_core_et_persist_oneXmany_appmanagedNoTx_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_persist_oneXmany_vehicles_client.jar");
            // The class files
            jpa_core_et_persist_oneXmany_appmanagedNoTx_vehicle_client.addClasses(
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
            ee.jakarta.tck.persistence.core.entitytest.persist.oneXmany.Client.class,
            ClientAppmanagednotxTest.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml");
            if(resURL != null) {
              jpa_core_et_persist_oneXmany_appmanagedNoTx_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_core_et_persist_oneXmany_appmanagedNoTx_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jpa_core_et_persist_oneXmany_appmanagedNoTx_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + com.sun.ts.tests.common.vehicle.VehicleClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_core_et_persist_oneXmany_appmanagedNoTx_vehicle_client, Client.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_core_et_persist_oneXmany_appmanagedNoTx_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_persist_oneXmany_appmanagedNoTx_vehicle_ejb.jar");
            // The class files
            jpa_core_et_persist_oneXmany_appmanagedNoTx_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                Fault.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ee.jakarta.tck.persistence.common.PMClientBase.class,
                ee.jakarta.tck.persistence.core.entitytest.persist.oneXmany.Client.class,
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
            URL ejbResURL1 = Client.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml");
            if(ejbResURL1 != null) {
//              jpa_core_et_persist_oneXmany_appmanagedNoTx_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_core_et_persist_oneXmany_appmanagedNoTx_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_core_et_persist_oneXmany_appmanagedNoTx_vehicle_ejb, Client.class, ejbResURL1);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_et_persist_oneXmany = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_persist_oneXmany.jar");
            // The class files
            jpa_core_et_persist_oneXmany.addClasses(
                ee.jakarta.tck.persistence.core.entitytest.persist.oneXmany.B.class,
                ee.jakarta.tck.persistence.core.entitytest.persist.oneXmany.A.class
            );
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_et_persist_oneXmany.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_et_persist_oneXmany.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_et_persist_oneXmany.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_et_persist_oneXmany.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_et_persist_oneXmany, Client.class, parURL);
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_et_persist_oneXmany.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_et_persist_oneXmany_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_et_persist_oneXmany_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_et_persist_oneXmany_vehicles_ear.addAsModule(jpa_core_et_persist_oneXmany_appmanagedNoTx_vehicle_ejb);
            jpa_core_et_persist_oneXmany_vehicles_ear.addAsModule(jpa_core_et_persist_oneXmany_appmanagedNoTx_vehicle_client);

            jpa_core_et_persist_oneXmany_vehicles_ear.addAsLibrary(jpa_core_et_persist_oneXmany);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_et_persist_oneXmany_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_et_persist_oneXmany_vehicles_ear, Client.class, earResURL);
        return jpa_core_et_persist_oneXmany_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void persist1XMTest1() throws java.lang.Exception {
            super.persist1XMTest1();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void persist1XMTest3() throws java.lang.Exception {
            super.persist1XMTest3();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void persist1XMTest4() throws java.lang.Exception {
            super.persist1XMTest4();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void persist1XMTest5() throws java.lang.Exception {
            super.persist1XMTest5();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void persist1XMTest6() throws java.lang.Exception {
            super.persist1XMTest6();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void persist1XMTest7() throws java.lang.Exception {
            super.persist1XMTest7();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void persist1XMTest7IllegalStateException() throws java.lang.Exception {
            super.persist1XMTest7IllegalStateException();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void persist1XMTest8() throws java.lang.Exception {
            super.persist1XMTest8();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void persist1XMTest9() throws java.lang.Exception {
            super.persist1XMTest9();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void persist1XMTest10() throws java.lang.Exception {
            super.persist1XMTest10();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void persist1XMTest11() throws java.lang.Exception {
            super.persist1XMTest11();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void persist1XMTest12() throws java.lang.Exception {
            super.persist1XMTest12();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void persist1XMTest13() throws java.lang.Exception {
            super.persist1XMTest13();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void persist1XMTest14() throws java.lang.Exception {
            super.persist1XMTest14();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void persist1XMTest15() throws java.lang.Exception {
            super.persist1XMTest15();
        }

}
