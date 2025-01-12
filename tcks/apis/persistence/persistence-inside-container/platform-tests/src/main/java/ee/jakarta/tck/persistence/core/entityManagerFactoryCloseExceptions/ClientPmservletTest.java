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
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
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
     * EE10 Deployment Descriptors: jpa_core_entityManagerFactoryCloseException: META-INF/persistence.xml
     * jpa_core_entityManagerFactoryCloseException_appmanaged_vehicle_client: META-INF/application-client.xml
     * jpa_core_entityManagerFactoryCloseException_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
     * jpa_core_entityManagerFactoryCloseException_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
     * jpa_core_entityManagerFactoryCloseException_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
     * jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle_web: WEB-INF/web.xml
     * jpa_core_entityManagerFactoryCloseException_puservlet_vehicle_web: WEB-INF/web.xml
     * jpa_core_entityManagerFactoryCloseException_stateful3_vehicle_client: META-INF/application-client.xml
     * jpa_core_entityManagerFactoryCloseException_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
     * jpa_core_entityManagerFactoryCloseException_stateless3_vehicle_client: META-INF/application-client.xml
     * jpa_core_entityManagerFactoryCloseException_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
     * jpa_core_entityManagerFactoryCloseException_vehicles:
     *
     * Found Descriptors: War:
     *
     * /com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml Ear:
     *
     */
    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = VEHICLE_ARCHIVE, order = 2)
    public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
        // the war with the correct archive name
        WebArchive jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle_web =
            ShrinkWrap.create(WebArchive.class, "jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle_web.war");

        // The class files
        jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle_web.addClasses(
                EJB3ShareBaseBean.class,
                VehicleRunnerFactory.class,
                UseEntityManager.class,
                EJB3ShareIF.class,
                Fault.class,
                UseEntityManagerFactory.class,
                PMClientBase.class,
                ServletVehicle.class,
                VehicleRunnable.class,
                UserTransactionWrapper.class,
                EETest.class,
                ServiceEETest.class,
                EntityTransactionWrapper.class,
                PMServletVehicle.class,
                Client.class,
                SetupException.class, VehicleClient.class,
                NoopTransactionWrapper.class);

        // The web.xml descriptor
        URL warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
        if (warResURL != null) {
            jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
        }

        // The sun-web.xml descriptor
        warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
        if (warResURL != null) {
            jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
        }

        // Any libraries added to the war
        JavaArchive jpa_core_entityManagerFactoryCloseException_lib =
            ShrinkWrap.create(JavaArchive.class, "jpa_core_entityManagerFactoryCloseException.jar");

        // The resources
        URL libURL = Client.class.getResource("persistence.xml");
        jpa_core_entityManagerFactoryCloseException_lib.addAsManifestResource(libURL, "persistence.xml");

        jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle_web.addAsLibrary(jpa_core_entityManagerFactoryCloseException_lib);

        // Web content
        warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
        if (warResURL != null) {
            jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle_web.addAsWebResource(warResURL,
                    "/WEB-INF/pmservlet_vehicle_web.xml");
        }

        // Call the archive processor
        archiveProcessor.processWebArchive(jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle_web, Client.class, warResURL);

        // Ear
        EnterpriseArchive jpa_core_entityManagerFactoryCloseException_vehicles_ear =
            ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_entityManagerFactoryCloseException_vehicles.ear");

        // Any libraries added to the ear

        // The component jars built by the package target
        jpa_core_entityManagerFactoryCloseException_vehicles_ear
                .addAsModule(jpa_core_entityManagerFactoryCloseException_pmservlet_vehicle_web);

        // The sun-application.xml descriptor
        URL earResURL = Client.class.getResource("/.ear.sun-application.xml");
        if (earResURL != null) {
            jpa_core_entityManagerFactoryCloseException_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
        }

        // Call the archive processor
        archiveProcessor.processEarArchive(jpa_core_entityManagerFactoryCloseException_vehicles_ear, Client.class, earResURL);

        return jpa_core_entityManagerFactoryCloseException_vehicles_ear;
    }

    @Test
    @Override
    @TargetVehicle("pmservlet")
    public void exceptionsTest() throws java.lang.Exception {
        super.exceptionsTest();
    }

}
