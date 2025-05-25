package ee.jakarta.tck.persistence.jpa22.repeatable.namednativequery;

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
public class ClientPmservletTest extends ee.jakarta.tck.persistence.jpa22.repeatable.namednativequery.Client {
    static final String VEHICLE_ARCHIVE = "jpa_jpa22_repeatable_namednativequery_pmservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_jpa22_repeatable_namednativequery: META-INF/persistence.xml
        jpa_jpa22_repeatable_namednativequery_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_jpa22_repeatable_namednativequery_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_jpa22_repeatable_namednativequery_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_jpa22_repeatable_namednativequery_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_jpa22_repeatable_namednativequery_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_jpa22_repeatable_namednativequery_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_jpa22_repeatable_namednativequery_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_jpa22_repeatable_namednativequery_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_jpa22_repeatable_namednativequery_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_jpa22_repeatable_namednativequery_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_jpa22_repeatable_namednativequery_vehicles: 

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
            WebArchive jpa_jpa22_repeatable_namednativequery_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_jpa22_repeatable_namednativequery_pmservlet_vehicle_web.war");
            // The class files
            jpa_jpa22_repeatable_namednativequery_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            ee.jakarta.tck.persistence.jpa22.repeatable.namednativequery.Client.class,
            Fault.class,
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
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_jpa22_repeatable_namednativequery_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_jpa22_repeatable_namednativequery_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/jpa22/repeatable/namednativequery/jpa_jpa22_repeatable_namednativequery.jar");
            if(warResURL != null) {
              jpa_jpa22_repeatable_namednativequery_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_jpa22_repeatable_namednativequery.jar");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_jpa22_repeatable_namednativequery_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/pmservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_jpa22_repeatable_namednativequery_pmservlet_vehicle_web, Client.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_jpa22_repeatable_namednativequery = ShrinkWrap.create(JavaArchive.class, "jpa_jpa22_repeatable_namednativequery.jar");
            // The class files
            jpa_jpa22_repeatable_namednativequery.addClasses(
                ee.jakarta.tck.persistence.jpa22.repeatable.namednativequery.Coffee.class
            );
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_jpa22_repeatable_namednativequery.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_jpa22_repeatable_namednativequery.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_jpa22_repeatable_namednativequery.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_jpa22_repeatable_namednativequery.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_jpa22_repeatable_namednativequery, Client.class, parURL);
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_jpa22_repeatable_namednativequery.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_jpa22_repeatable_namednativequery_pmservlet_vehicle_web.addAsLibrary(jpa_jpa22_repeatable_namednativequery);
            return jpa_jpa22_repeatable_namednativequery_pmservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void findTest() throws java.lang.Exception {
            super.findTest();
        }


}