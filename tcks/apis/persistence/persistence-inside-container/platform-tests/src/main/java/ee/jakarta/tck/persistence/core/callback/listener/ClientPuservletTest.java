package ee.jakarta.tck.persistence.core.callback.listener;

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
public class ClientPuservletTest extends ee.jakarta.tck.persistence.core.callback.listener.Client {
    static final String VEHICLE_ARCHIVE = "jpa_core_callback_listener_puservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_callback_listener: META-INF/orm.xml,META-INF/persistence.xml
        jpa_core_callback_listener_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_callback_listener_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_callback_listener_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_callback_listener_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_callback_listener_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_callback_listener_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_callback_listener_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_callback_listener_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_callback_listener_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_callback_listener_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_callback_listener_vehicles: 
        jpa_core_callback_listeneroverride: META-INF/orm.xml,META-INF/persistence.xml
        jpa_core_callback_listeneroverride_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_callback_listeneroverride_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_callback_listeneroverride_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_callback_listeneroverride_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_callback_listeneroverride_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_callback_listeneroverride_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_callback_listeneroverride_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_callback_listeneroverride_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_callback_listeneroverride_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_callback_listeneroverride_vehicles: 

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
            WebArchive jpa_core_callback_listener_puservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_callback_listener_puservlet_vehicle_web.war");
            // The class files
            jpa_core_callback_listener_puservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class,
            ee.jakarta.tck.persistence.core.callback.common.Constants.class,
            Fault.class,
            ee.jakarta.tck.persistence.core.callback.listener.Client.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            ee.jakarta.tck.persistence.core.callback.common.EntityCallbackClientBase.class,
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
            if(warResURL != null) {
              jpa_core_callback_listener_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_callback_listener_puservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/core/callback/listener/jpa_core_callback_listener.jar");
            if(warResURL != null) {
              jpa_core_callback_listener_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_callback_listener.jar");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_callback_listener_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/puservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_callback_listener_puservlet_vehicle_web, Client.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_callback_listener = ShrinkWrap.create(JavaArchive.class, "jpa_core_callback_listener.jar");
            // The class files
            jpa_core_callback_listener.addClasses(
                ee.jakarta.tck.persistence.core.callback.common.ListenerC.class,
                ee.jakarta.tck.persistence.core.callback.common.Constants.class,
                ee.jakarta.tck.persistence.core.callback.common.GenerictListener.class,
                ee.jakarta.tck.persistence.core.callback.common.ListenerCC.class,
                ee.jakarta.tck.persistence.core.callback.common.GenerictListenerImpl.class,
                ee.jakarta.tck.persistence.core.callback.listener.Product.class,
                ee.jakarta.tck.persistence.core.callback.common.CallbackStatusImpl.class,
                ee.jakarta.tck.persistence.core.callback.listener.Order.class,
                ee.jakarta.tck.persistence.core.callback.common.ListenerBB.class,
                ee.jakarta.tck.persistence.core.callback.common.ListenerB.class,
                ee.jakarta.tck.persistence.core.callback.common.ListenerBase.class,
                ee.jakarta.tck.persistence.core.callback.common.CallbackStatusIF.class,
                ee.jakarta.tck.persistence.core.callback.common.ListenerA.class,
                ee.jakarta.tck.persistence.core.callback.common.ListenerAA.class,
                ee.jakarta.tck.persistence.core.callback.listener.LineItem.class,
                ee.jakarta.tck.persistence.core.callback.listener.LineItemSuper.class
            );
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_callback_listener.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_callback_listener.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_callback_listener.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_callback_listener.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_callback_listener, Client.class, parURL);
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_callback_listener.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_core_callback_listener_puservlet_vehicle_web.addAsLibrary(jpa_core_callback_listener);
            return jpa_core_callback_listener_puservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void prePersistTest() throws java.lang.Exception {
            super.prePersistTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void prePersistMultiTest() throws java.lang.Exception {
            super.prePersistMultiTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void prePersistCascadeTest() throws java.lang.Exception {
            super.prePersistCascadeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void prePersistMultiCascadeTest() throws java.lang.Exception {
            super.prePersistMultiCascadeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void preRemoveTest() throws java.lang.Exception {
            super.preRemoveTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void preRemoveMultiTest() throws java.lang.Exception {
            super.preRemoveMultiTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void preRemoveCascadeTest() throws java.lang.Exception {
            super.preRemoveCascadeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void preRemoveMultiCascadeTest() throws java.lang.Exception {
            super.preRemoveMultiCascadeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void preUpdateTest() throws java.lang.Exception {
            super.preUpdateTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void postLoadTest() throws java.lang.Exception {
            super.postLoadTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void postLoadMultiTest() throws java.lang.Exception {
            super.postLoadMultiTest();
        }


}