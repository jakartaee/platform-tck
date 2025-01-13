package ee.jakarta.tck.persistence.core.entityManagerFactoryCloseExceptions;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.common.vehicle.VehicleClient;
import com.sun.ts.tests.common.vehicle.VehicleRunnable;
import com.sun.ts.tests.common.vehicle.VehicleRunnerFactory;
import com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean;
import com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF;
import com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper;
import com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper;
import com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager;
import com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory;
import com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper;
import com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle;
import com.sun.ts.tests.common.vehicle.servlet.ServletVehicle;

import java.net.URL;

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

import ee.jakarta.tck.persistence.common.PMClientBase;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@ExtendWith(ArquillianExtension.class)
@Tag("persistence")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientPmservletTest extends Client {

    private static final long serialVersionUID = 1L;
    static final String VEHICLE_ARCHIVE = "jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
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
            WebArchive jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class,
            ee.jakarta.tck.persistence.core.entityManagerFactoryCloseExceptions.Client.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }

            // Par
            JavaArchive jpa_core_entityManagerFactoryCloseException_lib = ShrinkWrap.create(JavaArchive.class, "jpa_core_entityManagerFactoryCloseException.jar");
            URL parURL = Client.class.getResource("/ee/jakarta/tck/persistence/common/template/persistence.xml");
            jpa_core_entityManagerFactoryCloseException_lib.addAsManifestResource(parURL, "persistence.xml");
            jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle_web.addAsLibrary(jpa_core_entityManagerFactoryCloseException_lib);

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle_web, Client.class, warResURL);

        return jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle_web;
        }

    @Test
    @Override
    @TargetVehicle("pmservlet")
    public void exceptionsTest() throws java.lang.Exception {
        super.exceptionsTest();
    }

}
