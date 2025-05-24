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
public class Client2PmservletTest extends ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client2 {
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
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            ee.jakarta.tck.persistence.common.schema30.Util.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client2.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The web.xml descriptor
            URL warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client2.class.getResource("/com/sun/ts/tests/jpa/core/criteriaapi/CriteriaBuilder/jpa_core_criteriapia_CriteriaBuilder.jar");
            if(warResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_criteriapia_CriteriaBuilder.jar");
            }
            warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/pmservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web, Client2.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriapia_CriteriaBuilder = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder.jar");
            // The class files
            jpa_core_criteriapia_CriteriaBuilder.addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The persistence.xml descriptor
            URL parURL = Client2.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client2.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client2.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client2.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_criteriapia_CriteriaBuilder, Client2.class, parURL);
            parURL = Client2.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web.addAsLibrary(jpa_core_criteriapia_CriteriaBuilder);
            return jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void construct() throws java.lang.Exception {
            super.construct();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void tupleIntTest() throws java.lang.Exception {
            super.tupleIntTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void tupleToArrayTest() throws java.lang.Exception {
            super.tupleToArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void tupleIntClassTest() throws java.lang.Exception {
            super.tupleIntClassTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void tupleGetIntClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.tupleGetIntClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void tupleElementGetJavaTypeTest() throws java.lang.Exception {
            super.tupleElementGetJavaTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void tupleSelectionArrayTest() throws java.lang.Exception {
            super.tupleSelectionArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void array() throws java.lang.Exception {
            super.array();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void arrayIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.arrayIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void constructIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.constructIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void countDistinct() throws java.lang.Exception {
            super.countDistinct();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void andPredicates() throws java.lang.Exception {
            super.andPredicates();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void orPredicates() throws java.lang.Exception {
            super.orPredicates();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void isNull() throws java.lang.Exception {
            super.isNull();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void isNotNull() throws java.lang.Exception {
            super.isNotNull();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void parameter() throws java.lang.Exception {
            super.parameter();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void parameterCaseSensitiveTest() throws java.lang.Exception {
            super.parameterCaseSensitiveTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void criteriaBuilderValuesTest() throws java.lang.Exception {
            super.criteriaBuilderValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void criteriaBuilderIn1Test() throws java.lang.Exception {
            super.criteriaBuilderIn1Test();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void criteriaBuilderIn2Test() throws java.lang.Exception {
            super.criteriaBuilderIn2Test();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void criteriaBuilderInValueTest() throws java.lang.Exception {
            super.criteriaBuilderInValueTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void expressionInObjectArrayTest() throws java.lang.Exception {
            super.expressionInObjectArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void expressionInExpressionArrayTest() throws java.lang.Exception {
            super.expressionInExpressionArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void expressionInExpressionTest() throws java.lang.Exception {
            super.expressionInExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void expressionInCollectionTest() throws java.lang.Exception {
            super.expressionInCollectionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void parameterExpressionIsNullTest() throws java.lang.Exception {
            super.parameterExpressionIsNullTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void parameterExpressionIsNotNullTest() throws java.lang.Exception {
            super.parameterExpressionIsNotNullTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void likeExpStringTest() throws java.lang.Exception {
            super.likeExpStringTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void likeExpExpTest() throws java.lang.Exception {
            super.likeExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void notLikeExpStringTest() throws java.lang.Exception {
            super.notLikeExpStringTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void notLikeExpExpTest() throws java.lang.Exception {
            super.notLikeExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void notLikeExpExpExpTest() throws java.lang.Exception {
            super.notLikeExpExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void notLikeExpExpCharTest() throws java.lang.Exception {
            super.notLikeExpExpCharTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void notLikeExpStringExpTest() throws java.lang.Exception {
            super.notLikeExpStringExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void notLikeExpStringCharTest() throws java.lang.Exception {
            super.notLikeExpStringCharTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void concatExpStringTest() throws java.lang.Exception {
            super.concatExpStringTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void concatStringExpTest() throws java.lang.Exception {
            super.concatStringExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void concatExpExpTest() throws java.lang.Exception {
            super.concatExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void trimBothExpTest() throws java.lang.Exception {
            super.trimBothExpTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void lower() throws java.lang.Exception {
            super.lower();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void nullifExpressionExpressionTest() throws java.lang.Exception {
            super.nullifExpressionExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void nullifExpressionObjectTest() throws java.lang.Exception {
            super.nullifExpressionObjectTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void selectMultiSelectTest() throws java.lang.Exception {
            super.selectMultiSelectTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void multiRootTest() throws java.lang.Exception {
            super.multiRootTest();
        }


}