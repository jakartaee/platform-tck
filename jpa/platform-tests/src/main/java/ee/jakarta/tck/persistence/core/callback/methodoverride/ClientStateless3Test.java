package ee.jakarta.tck.persistence.core.callback.methodoverride;

import ee.jakarta.tck.persistence.core.callback.methodoverride.Client;
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
import com.sun.ts.lib.harness.Status;
import java.util.Properties;



@ExtendWith(ArquillianExtension.class)
@Tag("persistence")
@Tag("platform")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientStateless3Test extends ee.jakarta.tck.persistence.core.callback.methodoverride.Client {
    static final String VEHICLE_ARCHIVE = "jpa_core_callback_methodoverride_stateless3_vehicle";

    public static void main(String[] args) {
      ClientStateless3Test theTests = new ClientStateless3Test();
      Status s = theTests.run(args, System.out, System.err);
      s.exit();
    }

        /**
        EE10 Deployment Descriptors:
        jpa_core_callback_methodoverride: META-INF/orm.xml,META-INF/persistence.xml
        jpa_core_callback_methodoverride_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_callback_methodoverride_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_callback_methodoverride_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_callback_methodoverride_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_callback_methodoverride_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_callback_methodoverride_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_callback_methodoverride_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_callback_methodoverride_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_callback_methodoverride_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_callback_methodoverride_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_callback_methodoverride_vehicles: 

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/stateless3/stateless3_vehicle_client.xml
        Ejb:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_core_callback_methodoverride_stateless3_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_core_callback_methodoverride_vehicles_client.jar");
            // The class files
            jpa_core_callback_methodoverride_stateless3_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            ee.jakarta.tck.persistence.core.callback.common.EntityCallbackClientBase.class,
            ee.jakarta.tck.persistence.core.callback.methodoverride.Client.class,
            ClientStateless3Test.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/stateless3/stateless3_vehicle_client.xml");
            if(resURL != null) {
              jpa_core_callback_methodoverride_stateless3_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/stateless3/stateless3_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_core_callback_methodoverride_stateless3_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jpa_core_callback_methodoverride_stateless3_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_core_callback_methodoverride_stateless3_vehicle_client, Client.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_core_callback_methodoverride_stateless3_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_core_callback_methodoverride_stateless3_vehicle_ejb.jar");
            // The class files
            jpa_core_callback_methodoverride_stateless3_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                ee.jakarta.tck.persistence.core.callback.common.Constants.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ee.jakarta.tck.persistence.common.PMClientBase.class,
                ee.jakarta.tck.persistence.core.callback.common.EntityCallbackClientBase.class,
                ee.jakarta.tck.persistence.core.callback.methodoverride.Client.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class,
                com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client.class.getResource("/com/sun/ts/tests/common/vehicle/stateless3/stateless3_vehicle_client.xml");
            if(ejbResURL1 != null) {
//              jpa_core_callback_methodoverride_stateless3_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client.class.getResource("/com/sun/ts/tests/common/vehicle/stateless3/stateless3_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_core_callback_methodoverride_stateless3_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_core_callback_methodoverride_stateless3_vehicle_ejb, Client.class, ejbResURL1);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_callback_methodoverride = ShrinkWrap.create(JavaArchive.class, "jpa_core_callback_methodoverride.jar");
            // The class files
            jpa_core_callback_methodoverride.addClasses(
                ee.jakarta.tck.persistence.core.callback.common.ListenerC.class,
                ee.jakarta.tck.persistence.core.callback.common.Constants.class,
                ee.jakarta.tck.persistence.core.callback.common.GenerictListener.class,
                ee.jakarta.tck.persistence.core.callback.common.ListenerCC.class,
                ee.jakarta.tck.persistence.core.callback.common.GenerictListenerImpl.class,
                ee.jakarta.tck.persistence.core.callback.methodoverride.Product.class,
                ee.jakarta.tck.persistence.core.callback.common.CallbackStatusImpl.class,
                ee.jakarta.tck.persistence.core.callback.common.ListenerBB.class,
                ee.jakarta.tck.persistence.core.callback.common.ListenerB.class,
                ee.jakarta.tck.persistence.core.callback.common.ListenerBase.class,
                ee.jakarta.tck.persistence.core.callback.common.CallbackStatusIF.class,
                ee.jakarta.tck.persistence.core.callback.common.ListenerA.class,
                ee.jakarta.tck.persistence.core.callback.common.ListenerAA.class,
                ee.jakarta.tck.persistence.core.callback.methodoverride.Order.class,
                ee.jakarta.tck.persistence.core.callback.methodoverride.LineItem.class
            );
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_callback_methodoverride.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_callback_methodoverride.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_callback_methodoverride.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_callback_methodoverride.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_callback_methodoverride, Client.class, parURL);
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_callback_methodoverride.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_callback_methodoverride_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_callback_methodoverride_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_callback_methodoverride_vehicles_ear.addAsModule(jpa_core_callback_methodoverride_stateless3_vehicle_ejb);
            jpa_core_callback_methodoverride_vehicles_ear.addAsModule(jpa_core_callback_methodoverride_stateless3_vehicle_client);

            jpa_core_callback_methodoverride_vehicles_ear.addAsLibrary(jpa_core_callback_methodoverride);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_callback_methodoverride_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_callback_methodoverride_vehicles_ear, Client.class, earResURL);
        return jpa_core_callback_methodoverride_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void prePersistTest() throws java.lang.Exception {
            super.prePersistTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void prePersistMultiTest() throws java.lang.Exception {
            super.prePersistMultiTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void prePersistCascadeTest() throws java.lang.Exception {
            super.prePersistCascadeTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void prePersistMultiCascadeTest() throws java.lang.Exception {
            super.prePersistMultiCascadeTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void preRemoveTest() throws java.lang.Exception {
            super.preRemoveTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void preRemoveMultiTest() throws java.lang.Exception {
            super.preRemoveMultiTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void preRemoveCascadeTest() throws java.lang.Exception {
            super.preRemoveCascadeTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void preRemoveMultiCascadeTest() throws java.lang.Exception {
            super.preRemoveMultiCascadeTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void preUpdateTest() throws java.lang.Exception {
            super.preUpdateTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void postLoadTest() throws java.lang.Exception {
            super.postLoadTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void postLoadMultiTest() throws java.lang.Exception {
            super.postLoadMultiTest();
        }

}
