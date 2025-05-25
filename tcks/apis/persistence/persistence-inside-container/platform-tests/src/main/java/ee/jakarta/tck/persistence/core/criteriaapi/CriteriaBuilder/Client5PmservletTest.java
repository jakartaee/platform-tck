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
public class Client5PmservletTest extends ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client5 {
    static final String VEHICLE_ARCHIVE = "jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle";

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

        /com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client5.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            ee.jakarta.tck.persistence.common.schema30.Util.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The web.xml descriptor
            URL warResURL = Client5.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client5.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client5.class.getResource("/com/sun/ts/tests/jpa/core/criteriaapi/CriteriaBuilder/jpa_core_criteriapia_CriteriaBuilder.jar");
            if(warResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_criteriapia_CriteriaBuilder.jar");
            }
            warResURL = Client5.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/pmservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web, Client5.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriapia_CriteriaBuilder = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder.jar");
            // The class files
            jpa_core_criteriapia_CriteriaBuilder.addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The persistence.xml descriptor
            URL parURL = Client5.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client5.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client5.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client5.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_criteriapia_CriteriaBuilder, Client5.class, parURL);
            parURL = Client5.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web.addAsLibrary(jpa_core_criteriapia_CriteriaBuilder);
            return jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void primaryKeyJoinColumnTest() throws java.lang.Exception {
            super.primaryKeyJoinColumnTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void asc() throws java.lang.Exception {
            super.asc();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void desc() throws java.lang.Exception {
            super.desc();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void sumExpTest() throws java.lang.Exception {
            super.sumExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void sumExpNumTest() throws java.lang.Exception {
            super.sumExpNumTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void sumNumExpTest() throws java.lang.Exception {
            super.sumNumExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void sumExpExpTest() throws java.lang.Exception {
            super.sumExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void exists() throws java.lang.Exception {
            super.exists();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void subqueryFromEntityTypeTest() throws java.lang.Exception {
            super.subqueryFromEntityTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void all() throws java.lang.Exception {
            super.all();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void sumAsDoubleTest() throws java.lang.Exception {
            super.sumAsDoubleTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void sumAsLongTest() throws java.lang.Exception {
            super.sumAsLongTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void lessThanExpNumTest() throws java.lang.Exception {
            super.lessThanExpNumTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void lessThanExpExpTest() throws java.lang.Exception {
            super.lessThanExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void lessThanOrEqualToExpNumTest() throws java.lang.Exception {
            super.lessThanOrEqualToExpNumTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void lessThanOrEqualToExpExpTest() throws java.lang.Exception {
            super.lessThanOrEqualToExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void between() throws java.lang.Exception {
            super.between();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void ltExpNumTest() throws java.lang.Exception {
            super.ltExpNumTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void ltExpExpTest() throws java.lang.Exception {
            super.ltExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void leExpNumTest() throws java.lang.Exception {
            super.leExpNumTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void leExpExpTest() throws java.lang.Exception {
            super.leExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void neg() throws java.lang.Exception {
            super.neg();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void prodExpNumTest() throws java.lang.Exception {
            super.prodExpNumTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void prodNumExpTest() throws java.lang.Exception {
            super.prodNumExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void prodExpExpTest() throws java.lang.Exception {
            super.prodExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void diffExpNumberTest() throws java.lang.Exception {
            super.diffExpNumberTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void diffNumberExpTest() throws java.lang.Exception {
            super.diffNumberExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void diffExpExpTest() throws java.lang.Exception {
            super.diffExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void quotExpNumTest() throws java.lang.Exception {
            super.quotExpNumTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void quotNumExpTest() throws java.lang.Exception {
            super.quotNumExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void quotExpExpTest() throws java.lang.Exception {
            super.quotExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void modExpIntTest() throws java.lang.Exception {
            super.modExpIntTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void modExpExpTest() throws java.lang.Exception {
            super.modExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void modIntExpTest() throws java.lang.Exception {
            super.modIntExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void sqrt() throws java.lang.Exception {
            super.sqrt();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void toLong() throws java.lang.Exception {
            super.toLong();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void toInteger() throws java.lang.Exception {
            super.toInteger();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void toFloat() throws java.lang.Exception {
            super.toFloat();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void toDouble() throws java.lang.Exception {
            super.toDouble();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void toBigDecimal() throws java.lang.Exception {
            super.toBigDecimal();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void toBigInteger() throws java.lang.Exception {
            super.toBigInteger();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void toStringTest() throws java.lang.Exception {
            super.toStringTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void literal() throws java.lang.Exception {
            super.literal();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void currentDate() throws java.lang.Exception {
            super.currentDate();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void currentTime() throws java.lang.Exception {
            super.currentTime();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void currentTimestamp() throws java.lang.Exception {
            super.currentTimestamp();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void treatPathClassTest() throws java.lang.Exception {
            super.treatPathClassTest();
        }


}