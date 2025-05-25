package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder;

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
public class Client4PuservletTest extends ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client4 {
    static final String VEHICLE_ARCHIVE = "jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_criteriapia_CriteriaBuilder: META-INF/persistence.xml
        jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriapia_CriteriaBuilder_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriapia_CriteriaBuilder_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriapia_CriteriaBuilder_vehicles: 

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
            WebArchive jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web.war");
            // The class files
            jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            ee.jakarta.tck.persistence.common.schema30.Util.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client4.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The web.xml descriptor
            URL warResURL = Client4.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client4.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client4.class.getResource("/com/sun/ts/tests/jpa/core/criteriaapi/CriteriaBuilder/jpa_core_criteriapia_CriteriaBuilder.jar");
            if(warResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_criteriapia_CriteriaBuilder.jar");
            }
            warResURL = Client4.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/puservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web, Client4.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriapia_CriteriaBuilder = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder.jar");
            // The class files
            jpa_core_criteriapia_CriteriaBuilder.addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The persistence.xml descriptor
            URL parURL = Client4.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client4.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client4.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client4.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_criteriapia_CriteriaBuilder, Client4.class, parURL);
            parURL = Client4.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web.addAsLibrary(jpa_core_criteriapia_CriteriaBuilder);
            return jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void greaterThanExpNumTest() throws java.lang.Exception {
            super.greaterThanExpNumTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void greaterThanExpExpTest() throws java.lang.Exception {
            super.greaterThanExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void greaterThanOrEqualToExpNumTest() throws java.lang.Exception {
            super.greaterThanOrEqualToExpNumTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void greaterThanOrEqualToExpExpTest() throws java.lang.Exception {
            super.greaterThanOrEqualToExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void gtExpNumTest() throws java.lang.Exception {
            super.gtExpNumTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void gtExpExpTest() throws java.lang.Exception {
            super.gtExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void geExpNumTest() throws java.lang.Exception {
            super.geExpNumTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void geExpExpTest() throws java.lang.Exception {
            super.geExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void substringExpIntTest() throws java.lang.Exception {
            super.substringExpIntTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void substringExpExpTest() throws java.lang.Exception {
            super.substringExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void substringExpIntIntTest() throws java.lang.Exception {
            super.substringExpIntIntTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void substringExpExpExpTest() throws java.lang.Exception {
            super.substringExpExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void upper() throws java.lang.Exception {
            super.upper();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void length() throws java.lang.Exception {
            super.length();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void locateExpStringTest() throws java.lang.Exception {
            super.locateExpStringTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void locateExpExpTest() throws java.lang.Exception {
            super.locateExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void locateExpressionExpressionExpressionTest() throws java.lang.Exception {
            super.locateExpressionExpressionExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void locateExpressionStringIntTest() throws java.lang.Exception {
            super.locateExpressionStringIntTest();
        }


}