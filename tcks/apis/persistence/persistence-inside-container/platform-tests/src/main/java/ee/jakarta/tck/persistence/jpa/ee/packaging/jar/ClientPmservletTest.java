package ee.jakarta.tck.persistence.jpa.ee.packaging.jar;

import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.lib.harness.Fault;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.harness.SetupException;
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
import ee.jakarta.tck.persistence.common.PMClientBase;
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
public class ClientPmservletTest extends ee.jakarta.tck.persistence.jpa.ee.packaging.jar.Client {

    private static final long serialVersionUID = 1L;
    static final String VEHICLE_ARCHIVE = "jpa_ee_packaging_jar_pmservlet_vehicle";

    /**
     * EE10 Deployment Descriptors: jpa_ee_packaging_jar:
     * META-INF/persistence.xml,META-INF/orm.xml,myMappingFile.xml,myMappingFile2.xml
     * jpa_ee_packaging_jar_appmanaged_vehicle_client: META-INF/application-client.xml
     * jpa_ee_packaging_jar_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml jpa_ee_packaging_jar_pmservlet_vehicle_web:
     * WEB-INF/web.xml jpa_ee_packaging_jar_stateful3_vehicle_client: META-INF/application-client.xml
     * jpa_ee_packaging_jar_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml jpa_ee_packaging_jar_stateless3_vehicle_client:
     * META-INF/application-client.xml jpa_ee_packaging_jar_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
     * jpa_ee_packaging_jar_vehicles: jpa_ee_packaging_jar1: jpa_ee_packaging_jar2:
     *
     * Found Descriptors: War:
     *
     * /com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml Ear:
     *
     */
    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = VEHICLE_ARCHIVE, order = 2)
    public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
        // the war with the correct archive name
        WebArchive jpa_ee_packaging_jar_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class,
                "jpa_ee_packaging_jar_pmservlet_vehicle_web.war");

        // The class files
        jpa_ee_packaging_jar_pmservlet_vehicle_web.addClasses(
                EJB3ShareBaseBean.class,
                Client.class,
                VehicleRunnerFactory.class,
                UseEntityManager.class,
                EJB3ShareIF.class, Fault.class,
                UseEntityManagerFactory.class,
                PMClientBase.class,
                ServletVehicle.class,
                VehicleRunnable.class,
                UserTransactionWrapper.class,
                EETest.class,
                ServiceEETest.class,
                EntityTransactionWrapper.class,
                PMServletVehicle.class,
                SetupException.class,
                VehicleClient.class,
                NoopTransactionWrapper.class);

        // The web.xml descriptor
        URL warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
        if (warResURL != null) {
            jpa_ee_packaging_jar_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
        }

        // The sun-web.xml descriptor
        warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
        if (warResURL != null) {
            jpa_ee_packaging_jar_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
        }

        // Any libraries added to the war
        URL libURL;
        JavaArchive jpa_ee_packaging_jar2_lib = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_jar2.jar");

        // The class files
        jpa_ee_packaging_jar2_lib.addClasses(C.class);

        jpa_ee_packaging_jar_pmservlet_vehicle_web.addAsLibrary(jpa_ee_packaging_jar2_lib);
        JavaArchive jpa_ee_packaging_jar1_lib = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_jar1.jar");

        // The class files
        jpa_ee_packaging_jar1_lib.addClasses(B.class);

        jpa_ee_packaging_jar_pmservlet_vehicle_web.addAsLibrary(jpa_ee_packaging_jar1_lib);
        JavaArchive jpa_ee_packaging_jar_lib = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_jar.jar");

        // The class files
        jpa_ee_packaging_jar_lib.addClasses(A.class);

        // The resources
        libURL = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/jar/persistence.xml");
        jpa_ee_packaging_jar_lib.addAsManifestResource(libURL, "persistence.xml");

        libURL = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/jar/orm.xml");
        jpa_ee_packaging_jar_lib.addAsManifestResource(libURL, "orm.xml");

        libURL = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/jar/myMappingFile.xml");
        jpa_ee_packaging_jar_lib.addAsResource(libURL, "myMappingFile.xml");

        libURL = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/jar/myMappingFile2.xml");
        jpa_ee_packaging_jar_lib.addAsResource(libURL, "myMappingFile2.xml");

        jpa_ee_packaging_jar_pmservlet_vehicle_web.addAsLibrary(jpa_ee_packaging_jar_lib);


        // Call the archive processor
        archiveProcessor.processWebArchive(jpa_ee_packaging_jar_pmservlet_vehicle_web, Client.class, warResURL);

        return jpa_ee_packaging_jar_pmservlet_vehicle_web;
    }

    @Test
    @Override
    @TargetVehicle("pmservlet")
    public void JarFileElementsTest() throws java.lang.Exception {
        super.JarFileElementsTest();
    }

}
