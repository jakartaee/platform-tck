package ee.jakarta.tck.persistence.core.annotations.access.field;

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
public class Client1PmservletTest extends ee.jakarta.tck.persistence.core.annotations.access.field.Client1 {
    static final String VEHICLE_ARCHIVE = "jpa_core_annotations_access_field_pmservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_annotations_access_field: META-INF/persistence.xml
        jpa_core_annotations_access_field_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_access_field_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_access_field_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_access_field_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_access_field_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_access_field_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_annotations_access_field_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_access_field_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_access_field_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_annotations_access_field_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_annotations_access_field_vehicles: 

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
            WebArchive jpa_core_annotations_access_field_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_annotations_access_field_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_annotations_access_field_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            ee.jakarta.tck.persistence.core.annotations.access.field.Client1.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            ee.jakarta.tck.persistence.core.annotations.access.field.Client.class
            );
            // The web.xml descriptor
            URL warResURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_annotations_access_field_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_annotations_access_field_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client1.class.getResource("/com/sun/ts/tests/jpa/core/annotations/access/field/jpa_core_annotations_access_field.jar");
            if(warResURL != null) {
              jpa_core_annotations_access_field_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_annotations_access_field.jar");
            }
            warResURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_annotations_access_field_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/pmservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_annotations_access_field_pmservlet_vehicle_web, Client1.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_annotations_access_field = ShrinkWrap.create(JavaArchive.class, "jpa_core_annotations_access_field.jar");
            // The class files
            jpa_core_annotations_access_field.addClasses(
                ee.jakarta.tck.persistence.core.annotations.access.field.DataTypes.class,
                ee.jakarta.tck.persistence.core.annotations.access.field.DataTypes2.class,
                ee.jakarta.tck.persistence.core.types.common.Grade.class
            );
            // The persistence.xml descriptor
            URL parURL = Client1.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_annotations_access_field.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client1.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_annotations_access_field.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client1.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_annotations_access_field.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client1.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_annotations_access_field.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_annotations_access_field, Client1.class, parURL);
            parURL = Client1.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_annotations_access_field.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_core_annotations_access_field_pmservlet_vehicle_web.addAsLibrary(jpa_core_annotations_access_field);
            return jpa_core_annotations_access_field_pmservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fieldTypeTest1() throws java.lang.Exception {
            super.fieldTypeTest1();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fieldTypeTest2() throws java.lang.Exception {
            super.fieldTypeTest2();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fieldTypeTest3() throws java.lang.Exception {
            super.fieldTypeTest3();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fieldTypeTest4() throws java.lang.Exception {
            super.fieldTypeTest4();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fieldTypeTest5() throws java.lang.Exception {
            super.fieldTypeTest5();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fieldTypeTest6() throws java.lang.Exception {
            super.fieldTypeTest6();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fieldTypeTest7() throws java.lang.Exception {
            super.fieldTypeTest7();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fieldTypeTest8() throws java.lang.Exception {
            super.fieldTypeTest8();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fieldTypeTest9() throws java.lang.Exception {
            super.fieldTypeTest9();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fieldTypeTest10() throws java.lang.Exception {
            super.fieldTypeTest10();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fieldTypeTest11() throws java.lang.Exception {
            super.fieldTypeTest11();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fieldTypeTest12() throws java.lang.Exception {
            super.fieldTypeTest12();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fieldTypeTest13() throws java.lang.Exception {
            super.fieldTypeTest13();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fieldTypeTest14() throws java.lang.Exception {
            super.fieldTypeTest14();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fieldTypeTest15() throws java.lang.Exception {
            super.fieldTypeTest15();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fieldTypeTest16() throws java.lang.Exception {
            super.fieldTypeTest16();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void fieldTypeTest17() throws java.lang.Exception {
            super.fieldTypeTest17();
        }


}