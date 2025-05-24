package ee.jakarta.tck.persistence.core.types.generator;

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
public class Client1PmservletTest extends ee.jakarta.tck.persistence.core.types.generator.Client1 {
    static final String VEHICLE_ARCHIVE = "jpa_core_types_generator_pmservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_types_generator: META-INF/persistence.xml
        jpa_core_types_generator_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_types_generator_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_types_generator_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_types_generator_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_types_generator_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_types_generator_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_types_generator_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_types_generator_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_types_generator_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_types_generator_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_types_generator_vehicles: 

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
            WebArchive jpa_core_types_generator_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_types_generator_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_types_generator_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            Fault.class,
            ee.jakarta.tck.persistence.core.types.generator.Client1.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            ee.jakarta.tck.persistence.core.types.generator.Client.class
            );
            // The web.xml descriptor
            URL warResURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_types_generator_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }

            // Any libraries added to the war

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_types_generator_pmservlet_vehicle_web, Client1.class, warResURL);

        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_types_generator = ShrinkWrap.create(JavaArchive.class, "jpa_core_types_generator.jar");
            // The class files
            jpa_core_types_generator.addClasses(
                ee.jakarta.tck.persistence.core.types.generator.DataTypes2.class,
                ee.jakarta.tck.persistence.core.types.generator.DataTypes.class,
                ee.jakarta.tck.persistence.core.types.generator.DataTypes3.class,
                ee.jakarta.tck.persistence.core.types.generator.DataTypes4.class
            );
            // The persistence.xml descriptor
            URL parURL = Client1.class.getResource("/ee/jakarta/tck/persistence/common/template/persistence.xml");
            if(parURL != null) {
              jpa_core_types_generator.addAsManifestResource(parURL, "persistence.xml");
            }


            jpa_core_types_generator_pmservlet_vehicle_web.addAsLibrary(jpa_core_types_generator);
            return jpa_core_types_generator_pmservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void generatorTypeTest1() throws java.lang.Exception {
            super.generatorTypeTest1();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void generatorTypeTest2() throws java.lang.Exception {
            super.generatorTypeTest2();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void generatorTypeTest3() throws java.lang.Exception {
            super.generatorTypeTest3();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void generatorTypeTest4() throws java.lang.Exception {
            super.generatorTypeTest4();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void generatorTypeTest5() throws java.lang.Exception {
            super.generatorTypeTest5();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void generatorTypeTest6() throws java.lang.Exception {
            super.generatorTypeTest6();
        }


}