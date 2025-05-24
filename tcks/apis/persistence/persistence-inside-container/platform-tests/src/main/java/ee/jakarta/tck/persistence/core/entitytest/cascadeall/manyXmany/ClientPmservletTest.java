package ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany;

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
import org.junit.jupiter.api.Order;
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

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientPmservletTest extends ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany.Client {
    static final String VEHICLE_ARCHIVE = "jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_et_cascadeall_manyXmany: META-INF/persistence.xml
        jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_et_cascadeall_manyXmany_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_et_cascadeall_manyXmany_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_et_cascadeall_manyXmany_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_et_cascadeall_manyXmany_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_et_cascadeall_manyXmany_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_et_cascadeall_manyXmany_vehicles: 

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
            WebArchive jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany.Client.class,
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
              jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web, Client.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_et_cascadeall_manyXmany = ShrinkWrap.create(JavaArchive.class, "jpa_core_et_cascadeall_manyXmany.jar");
            // The class files
            jpa_core_et_cascadeall_manyXmany.addClasses(
                ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany.B.class,
                ee.jakarta.tck.persistence.core.entitytest.cascadeall.manyXmany.A.class
            );
            jpa_core_et_cascadeall_manyXmany.addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            jpa_core_et_cascadeall_manyXmany.addAsManifestResource(parURL, "persistence.xml");
            archiveProcessor.processParArchive(jpa_core_et_cascadeall_manyXmany, Client.class, parURL);
            jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web.addAsLibrary(jpa_core_et_cascadeall_manyXmany);

        return jpa_core_et_cascadeall_manyXmany_pmservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        @Order(3)
        public void cascadeAllMXMTest1() throws java.lang.Exception {
            super.cascadeAllMXMTest1();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        @Order(2)
        public void cascadeAllMXMTest2() throws java.lang.Exception {
            super.cascadeAllMXMTest2();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        @Order(4)
        public void cascadeAllMXMTest3() throws java.lang.Exception {
            super.cascadeAllMXMTest3();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        @Order(1)
        public void cascadeAllMXMTest4() throws java.lang.Exception {
            super.cascadeAllMXMTest4();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void cascadeAllMXMTest5() throws java.lang.Exception {
            super.cascadeAllMXMTest5();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void cascadeAllMXMTest6() throws java.lang.Exception {
            super.cascadeAllMXMTest6();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void cascadeAllMXMTest7() throws java.lang.Exception {
            super.cascadeAllMXMTest7();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void cascadeAllMXMTest8() throws java.lang.Exception {
            super.cascadeAllMXMTest8();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void cascadeAllMXMTest9() throws java.lang.Exception {
            super.cascadeAllMXMTest9();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void cascadeAllMXMTest10() throws java.lang.Exception {
            super.cascadeAllMXMTest10();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void cascadeAllMXMTest11() throws java.lang.Exception {
            super.cascadeAllMXMTest11();
        }


}