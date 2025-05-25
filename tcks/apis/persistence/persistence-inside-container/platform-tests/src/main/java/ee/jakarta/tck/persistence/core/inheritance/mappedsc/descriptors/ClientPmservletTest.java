package ee.jakarta.tck.persistence.core.inheritance.mappedsc.descriptors;

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
public class ClientPmservletTest extends ee.jakarta.tck.persistence.core.inheritance.mappedsc.descriptors.Client {
    static final String VEHICLE_ARCHIVE = "jpa_core_inherit_msc_descriptors_pmservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_inherit_msc_descriptors: META-INF/orm.xml,META-INF/persistence.xml
        jpa_core_inherit_msc_descriptors_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_inherit_msc_descriptors_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_inherit_msc_descriptors_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_inherit_msc_descriptors_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_inherit_msc_descriptors_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_inherit_msc_descriptors_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_inherit_msc_descriptors_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_inherit_msc_descriptors_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_inherit_msc_descriptors_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_inherit_msc_descriptors_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_inherit_msc_descriptors_vehicles: 

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
            WebArchive jpa_core_inherit_msc_descriptors_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_inherit_msc_descriptors_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_inherit_msc_descriptors_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            ee.jakarta.tck.persistence.core.inheritance.mappedsc.descriptors.Client.class,
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
              jpa_core_inherit_msc_descriptors_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_inherit_msc_descriptors_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/core/inheritance/mappedsc/descriptors/jpa_core_inherit_msc_descriptors.jar");
            if(warResURL != null) {
              jpa_core_inherit_msc_descriptors_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_inherit_msc_descriptors.jar");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_inherit_msc_descriptors_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/pmservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_inherit_msc_descriptors_pmservlet_vehicle_web, Client.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_inherit_msc_descriptors = ShrinkWrap.create(JavaArchive.class, "jpa_core_inherit_msc_descriptors.jar");
            // The class files
            jpa_core_inherit_msc_descriptors.addClasses(
                ee.jakarta.tck.persistence.core.inheritance.mappedsc.descriptors.Employee.class,
                ee.jakarta.tck.persistence.core.inheritance.mappedsc.descriptors.Department.class,
                ee.jakarta.tck.persistence.core.inheritance.mappedsc.descriptors.FullTimeEmployee.class,
                ee.jakarta.tck.persistence.core.inheritance.mappedsc.descriptors.AbstractPersonnel.class,
                ee.jakarta.tck.persistence.core.inheritance.mappedsc.descriptors.Project.class,
                ee.jakarta.tck.persistence.core.inheritance.mappedsc.descriptors.PartTimeEmployee.class
            );
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_inherit_msc_descriptors.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_inherit_msc_descriptors.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_inherit_msc_descriptors.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_inherit_msc_descriptors.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_inherit_msc_descriptors, Client.class, parURL);
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_inherit_msc_descriptors.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_core_inherit_msc_descriptors_pmservlet_vehicle_web.addAsLibrary(jpa_core_inherit_msc_descriptors);
            return jpa_core_inherit_msc_descriptors_pmservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test1() throws java.lang.Exception {
            super.test1();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void test2() throws java.lang.Exception {
            super.test2();
        }


}