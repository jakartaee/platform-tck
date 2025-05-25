package ee.jakarta.tck.persistence.core.annotations.mapkey;

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
public class Client2PuservletTest extends ee.jakarta.tck.persistence.core.annotations.mapkey.Client2 {
    static final String VEHICLE_ARCHIVE = "jpa_core_annotations_mapkey_puservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_annotations_mapkey: META-INF/persistence.xml
        jpa_core_annotations_mapkey_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkey_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkey_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkey_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkey_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_mapkey_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_mapkey_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkey_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkey_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkey_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkey_vehicles: 
        jpa_core_annotations_mapkeyclass: META-INF/persistence.xml
        jpa_core_annotations_mapkeyclass_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkeyclass_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkeyclass_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkeyclass_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkeyclass_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_mapkeyclass_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_mapkeyclass_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkeyclass_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkeyclass_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkeyclass_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkeyclass_vehicles: 
        jpa_core_annotations_mapkeycolumn: META-INF/persistence.xml
        jpa_core_annotations_mapkeycolumn_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkeycolumn_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkeycolumn_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkeycolumn_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkeycolumn_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_mapkeycolumn_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_mapkeycolumn_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkeycolumn_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkeycolumn_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkeycolumn_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkeycolumn_vehicles: 
        jpa_core_annotations_mapkeyenumerated: META-INF/persistence.xml
        jpa_core_annotations_mapkeyenumerated_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkeyenumerated_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkeyenumerated_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkeyenumerated_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkeyenumerated_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_mapkeyenumerated_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_mapkeyenumerated_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkeyenumerated_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkeyenumerated_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkeyenumerated_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkeyenumerated_vehicles: 
        jpa_core_annotations_mapkeyjoincolumn: META-INF/persistence.xml
        jpa_core_annotations_mapkeyjoincolumn_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkeyjoincolumn_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkeyjoincolumn_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkeyjoincolumn_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkeyjoincolumn_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_mapkeyjoincolumn_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_mapkeyjoincolumn_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkeyjoincolumn_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkeyjoincolumn_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_mapkeyjoincolumn_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_mapkeyjoincolumn_vehicles: 

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
            WebArchive jpa_core_annotations_mapkey_puservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_annotations_mapkey_puservlet_vehicle_web.war");
            // The class files
            jpa_core_annotations_mapkey_puservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            ee.jakarta.tck.persistence.core.annotations.mapkey.Client2.class,
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
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            ee.jakarta.tck.persistence.core.annotations.mapkey.Client.class
            );
            // The web.xml descriptor
            URL warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_annotations_mapkey_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_annotations_mapkey_puservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client2.class.getResource("/com/sun/ts/tests/jpa/core/annotations/mapkey/jpa_core_annotations_mapkey.jar");
            if(warResURL != null) {
              jpa_core_annotations_mapkey_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_annotations_mapkey.jar");
            }
            warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_annotations_mapkey_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/puservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_annotations_mapkey_puservlet_vehicle_web, Client2.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_annotations_mapkey = ShrinkWrap.create(JavaArchive.class, "jpa_core_annotations_mapkey.jar");
            // The class files
            jpa_core_annotations_mapkey.addClasses(
                ee.jakarta.tck.persistence.core.annotations.mapkey.Department.class,
                ee.jakarta.tck.persistence.core.annotations.mapkey.Employee.class,
                ee.jakarta.tck.persistence.core.annotations.mapkey.Employee4.class,
                ee.jakarta.tck.persistence.core.annotations.mapkey.Employee2.class,
                ee.jakarta.tck.persistence.core.annotations.mapkey.Employee3.class
            );
            // The persistence.xml descriptor
            URL parURL = Client2.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_annotations_mapkey.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client2.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_annotations_mapkey.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client2.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_annotations_mapkey.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client2.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_annotations_mapkey.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_annotations_mapkey, Client2.class, parURL);
            parURL = Client2.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_annotations_mapkey.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_core_annotations_mapkey_puservlet_vehicle_web.addAsLibrary(jpa_core_annotations_mapkey);
            return jpa_core_annotations_mapkey_puservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinColumnInsertable() throws java.lang.Exception {
            super.joinColumnInsertable();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinColumnUpdatable() throws java.lang.Exception {
            super.joinColumnUpdatable();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void columnInsertable() throws java.lang.Exception {
            super.columnInsertable();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void columnUpdatable() throws java.lang.Exception {
            super.columnUpdatable();
        }


}