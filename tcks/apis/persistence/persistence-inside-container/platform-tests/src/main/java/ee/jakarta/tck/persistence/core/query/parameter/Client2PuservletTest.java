package ee.jakarta.tck.persistence.core.query.parameter;

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
public class Client2PuservletTest extends ee.jakarta.tck.persistence.core.query.parameter.Client2 {
    static final String VEHICLE_ARCHIVE = "jpa_core_query_parameter_puservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_query_parameter: META-INF/persistence.xml
        jpa_core_query_parameter_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_query_parameter_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_query_parameter_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_query_parameter_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_query_parameter_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_query_parameter_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_query_parameter_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_query_parameter_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_query_parameter_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_query_parameter_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_query_parameter_vehicles: 

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
            WebArchive jpa_core_query_parameter_puservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_query_parameter_puservlet_vehicle_web.war");
            // The class files
            jpa_core_query_parameter_puservlet_vehicle_web.addClasses(
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
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            ee.jakarta.tck.persistence.core.query.parameter.Client2.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            jpa_core_query_parameter_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_query_parameter_puservlet_vehicle_web, Client2.class, warResURL);

        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_query_parameter = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_parameter.jar");
            // The class files
            jpa_core_query_parameter.addClasses(
                ee.jakarta.tck.persistence.core.query.parameter.Employee.class
            );
            // The persistence.xml descriptor
            URL parURL = Client2.class.getResource("persistence.xml");
            jpa_core_query_parameter.addAsManifestResource(parURL, "persistence.xml");
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_query_parameter, Client2.class, parURL);
            jpa_core_query_parameter_puservlet_vehicle_web.addAsLibrary(jpa_core_query_parameter);

        return jpa_core_query_parameter_puservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void parameterPositionalTest() throws java.lang.Exception {
            super.parameterPositionalTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void parameterUpdateTest() throws java.lang.Exception {
            super.parameterUpdateTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void parameterCaseTest() throws java.lang.Exception {
            super.parameterCaseTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void parameterNamedParameterTwiceTest() throws java.lang.Exception {
            super.parameterNamedParameterTwiceTest();
        }


}