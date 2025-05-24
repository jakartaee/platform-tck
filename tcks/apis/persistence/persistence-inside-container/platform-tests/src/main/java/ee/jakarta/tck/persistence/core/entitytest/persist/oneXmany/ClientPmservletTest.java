package ee.jakarta.tck.persistence.core.entitytest.persist.oneXmany;

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
public class ClientPmservletTest extends ee.jakarta.tck.persistence.core.entitytest.persist.oneXmany.Client {
    static final String VEHICLE_ARCHIVE = "jpa_core_et_persist_oneXmany_pmservlet_vehicle";

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
            WebArchive jpa_core_et_persist_oneXmany_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_et_persist_oneXmany_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_et_persist_oneXmany_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            ee.jakarta.tck.persistence.core.entitytest.persist.oneXmany.Client.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_et_persist_oneXmany_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_et_persist_oneXmany_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/core/entitytest/persist/oneXmany/jpa_core_et_persist_oneXmany.jar");
            if(warResURL != null) {
              jpa_core_et_persist_oneXmany_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_et_persist_oneXmany.jar");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_et_persist_oneXmany_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/pmservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_et_persist_oneXmany_pmservlet_vehicle_web, Client.class, warResURL);


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

            jpa_core_et_persist_oneXmany_pmservlet_vehicle_web.addAsLibrary(jpa_core_et_persist_oneXmany);
            return jpa_core_et_persist_oneXmany_pmservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void persist1XMTest1() throws java.lang.Exception {
            super.persist1XMTest1();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void persist1XMTest3() throws java.lang.Exception {
            super.persist1XMTest3();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void persist1XMTest4() throws java.lang.Exception {
            super.persist1XMTest4();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void persist1XMTest5() throws java.lang.Exception {
            super.persist1XMTest5();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void persist1XMTest6() throws java.lang.Exception {
            super.persist1XMTest6();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void persist1XMTest7() throws java.lang.Exception {
            super.persist1XMTest7();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void persist1XMTest7IllegalStateException() throws java.lang.Exception {
            super.persist1XMTest7IllegalStateException();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void persist1XMTest8() throws java.lang.Exception {
            super.persist1XMTest8();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void persist1XMTest9() throws java.lang.Exception {
            super.persist1XMTest9();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void persist1XMTest10() throws java.lang.Exception {
            super.persist1XMTest10();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void persist1XMTest11() throws java.lang.Exception {
            super.persist1XMTest11();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void persist1XMTest12() throws java.lang.Exception {
            super.persist1XMTest12();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void persist1XMTest13() throws java.lang.Exception {
            super.persist1XMTest13();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void persist1XMTest14() throws java.lang.Exception {
            super.persist1XMTest14();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void persist1XMTest15() throws java.lang.Exception {
            super.persist1XMTest15();
        }


}