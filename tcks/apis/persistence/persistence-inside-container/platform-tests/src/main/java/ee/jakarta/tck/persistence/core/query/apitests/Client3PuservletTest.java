package ee.jakarta.tck.persistence.core.query.apitests;

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
public class Client3PuservletTest extends ee.jakarta.tck.persistence.core.query.apitests.Client3 {
    static final String VEHICLE_ARCHIVE = "jpa_core_query_apitests_puservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_query_apitests: META-INF/persistence.xml
        jpa_core_query_apitests_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_query_apitests_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_query_apitests_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_query_apitests_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_query_apitests_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_query_apitests_vehicles: 

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
            WebArchive jpa_core_query_apitests_puservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_query_apitests_puservlet_vehicle_web.war");
            // The class files
            jpa_core_query_apitests_puservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            ee.jakarta.tck.persistence.core.query.apitests.Client3.class,
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
            URL warResURL = Client3.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_query_apitests_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client3.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_query_apitests_puservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client3.class.getResource("/com/sun/ts/tests/jpa/core/query/apitests/jpa_core_query_apitests.jar");
            if(warResURL != null) {
              jpa_core_query_apitests_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_query_apitests.jar");
            }
            warResURL = Client3.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_query_apitests_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/puservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_query_apitests_puservlet_vehicle_web, Client3.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_query_apitests = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_apitests.jar");
            // The class files
            jpa_core_query_apitests.addClasses(
                ee.jakarta.tck.persistence.core.query.apitests.Employee.class,
                ee.jakarta.tck.persistence.core.query.apitests.Department.class,
                ee.jakarta.tck.persistence.core.query.apitests.DataTypes2.class,
                ee.jakarta.tck.persistence.core.query.apitests.Insurance.class
            );
            // The persistence.xml descriptor
            URL parURL = Client3.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_query_apitests.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client3.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_query_apitests.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client3.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_query_apitests.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client3.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_query_apitests.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_query_apitests, Client3.class, parURL);
            parURL = Client3.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_query_apitests.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_core_query_apitests_puservlet_vehicle_web.addAsLibrary(jpa_core_query_apitests);
            return jpa_core_query_apitests_puservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryAPITest28() throws java.lang.Exception {
            super.queryAPITest28();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void queryAPITest29() throws java.lang.Exception {
            super.queryAPITest29();
        }


}