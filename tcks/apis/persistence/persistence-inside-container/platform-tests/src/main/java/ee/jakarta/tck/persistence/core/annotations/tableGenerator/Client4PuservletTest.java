package ee.jakarta.tck.persistence.core.annotations.tableGenerator;

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
public class Client4PuservletTest extends ee.jakarta.tck.persistence.core.annotations.tableGenerator.Client4 {
    static final String VEHICLE_ARCHIVE = "jpa_core_annotations_tableGenerator_puservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_annotations_tableGenerator: META-INF/persistence.xml
        jpa_core_annotations_tableGenerator_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_tableGenerator_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_tableGenerator_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_tableGenerator_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_tableGenerator_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_tableGenerator_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_tableGenerator_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_tableGenerator_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_tableGenerator_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_tableGenerator_vehicles: 

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
            WebArchive jpa_core_annotations_tableGenerator_puservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_annotations_tableGenerator_puservlet_vehicle_web.war");
            // The class files
            jpa_core_annotations_tableGenerator_puservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            ee.jakarta.tck.persistence.core.annotations.tableGenerator.Client4.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            ee.jakarta.tck.persistence.core.annotations.tableGenerator.Client.class
            );
            // The web.xml descriptor
            URL warResURL = Client4.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_annotations_tableGenerator_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client4.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_annotations_tableGenerator_puservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client4.class.getResource("/com/sun/ts/tests/jpa/core/annotations/tableGenerator/jpa_core_annotations_tableGenerator.jar");
            if(warResURL != null) {
              jpa_core_annotations_tableGenerator_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_annotations_tableGenerator.jar");
            }
            warResURL = Client4.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_annotations_tableGenerator_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/puservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_annotations_tableGenerator_puservlet_vehicle_web, Client4.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_annotations_tableGenerator = ShrinkWrap.create(JavaArchive.class, "jpa_core_annotations_tableGenerator.jar");
            // The class files
            jpa_core_annotations_tableGenerator.addClasses(
                ee.jakarta.tck.persistence.core.annotations.tableGenerator.DataTypes2.class,
                ee.jakarta.tck.persistence.core.annotations.tableGenerator.DataTypes.class,
                ee.jakarta.tck.persistence.core.annotations.tableGenerator.DataTypes3.class,
                ee.jakarta.tck.persistence.core.annotations.tableGenerator.DataTypes4.class
            );
            // The persistence.xml descriptor
            URL parURL = Client4.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_annotations_tableGenerator.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client4.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_annotations_tableGenerator.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client4.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_annotations_tableGenerator.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client4.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_annotations_tableGenerator.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_annotations_tableGenerator, Client4.class, parURL);
            parURL = Client4.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_annotations_tableGenerator.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_core_annotations_tableGenerator_puservlet_vehicle_web.addAsLibrary(jpa_core_annotations_tableGenerator);
            return jpa_core_annotations_tableGenerator_puservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void generatorGlobalTest() throws java.lang.Exception {
            super.generatorGlobalTest();
        }


}