package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder;

import ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client2;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("persistence")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")

public class Client2PuservletTest extends ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client2 {
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
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
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
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            ee.jakarta.tck.persistence.common.schema30.Util.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client2.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client2.class.getResource("//com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
           archiveProcessor.processWebArchive(jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web, Client2.class, warResURL);

        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriapia_CriteriaBuilder = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder.jar");
            // The class files
            jpa_core_criteriapia_CriteriaBuilder.addClasses(
                ee.jakarta.tck.persistence.common.schema30.Department.class,
                ee.jakarta.tck.persistence.common.schema30.Address_.class,
                ee.jakarta.tck.persistence.common.schema30.Department_.class,
                ee.jakarta.tck.persistence.common.schema30.CreditCard.class,
                ee.jakarta.tck.persistence.common.schema30.Info.class,
                ee.jakarta.tck.persistence.common.schema30.LineItem_.class,
                ee.jakarta.tck.persistence.common.schema30.Phone.class,
                ee.jakarta.tck.persistence.common.schema30.Customer_.class,
                ee.jakarta.tck.persistence.common.schema30.Employee_.class,
                ee.jakarta.tck.persistence.common.schema30.Trim_.class,
                ee.jakarta.tck.persistence.common.schema30.Order_.class,
                ee.jakarta.tck.persistence.common.schema30.ShelfLife_.class,
                ee.jakarta.tck.persistence.common.schema30.ShelfLife.class,
                ee.jakarta.tck.persistence.common.schema30.Phone_.class,
                ee.jakarta.tck.persistence.common.schema30.Address.class,
                ee.jakarta.tck.persistence.common.schema30.Info_.class,
                ee.jakarta.tck.persistence.common.schema30.HardwareProduct.class,
                ee.jakarta.tck.persistence.common.schema30.Country_.class,
                ee.jakarta.tck.persistence.common.schema30.Alias_.class,
                ee.jakarta.tck.persistence.common.schema30.Trim.class,
                ee.jakarta.tck.persistence.common.schema30.HardwareProduct_.class,
                ee.jakarta.tck.persistence.common.schema30.CreditCard_.class,
                ee.jakarta.tck.persistence.common.schema30.SoftwareProduct.class,
                ee.jakarta.tck.persistence.common.schema30.Product.class,
                ee.jakarta.tck.persistence.common.schema30.Spouse.class,
                ee.jakarta.tck.persistence.common.schema30.SoftwareProduct_.class,
                ee.jakarta.tck.persistence.common.schema30.Spouse_.class,
                ee.jakarta.tck.persistence.common.schema30.LineItem.class,
                ee.jakarta.tck.persistence.common.schema30.Employee.class,
                ee.jakarta.tck.persistence.common.schema30.Product_.class,
                ee.jakarta.tck.persistence.common.schema30.Customer.class,
                ee.jakarta.tck.persistence.common.schema30.Alias.class,
                ee.jakarta.tck.persistence.common.schema30.Order.class,
                ee.jakarta.tck.persistence.common.schema30.LineItemException.class,
                ee.jakarta.tck.persistence.common.schema30.Country.class
            );
            // The persistence.xml descriptor
            URL parURL = Client2.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsManifestResource(parURL, "persistence.xml");
            }
            archiveProcessor.processParArchive(jpa_core_criteriapia_CriteriaBuilder, Client2.class, parURL);
            // The orm.xml file
            parURL = Client2.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_criteriapia_CriteriaBuilder_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_criteriapia_CriteriaBuilder_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_criteriapia_CriteriaBuilder_vehicles_ear.addAsModule(jpa_core_criteriapia_CriteriaBuilder_puservlet_vehicle_web);

            jpa_core_criteriapia_CriteriaBuilder_vehicles_ear.addAsLibrary(jpa_core_criteriapia_CriteriaBuilder);



            // The application.xml descriptor
            URL earResURL = Client2.class.getResource("/com/sun/ts/tests/jpa/core/criteriaapi/CriteriaBuilder/");
            if(earResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_vehicles_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client2.class.getResource("/com/sun/ts/tests/jpa/core/criteriaapi/CriteriaBuilder/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            archiveProcessor.processEarArchive(jpa_core_criteriapia_CriteriaBuilder_vehicles_ear, Client2.class, earResURL);
        return jpa_core_criteriapia_CriteriaBuilder_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void construct() throws java.lang.Exception {
            super.construct();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void tupleIntTest() throws java.lang.Exception {
            super.tupleIntTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void tupleToArrayTest() throws java.lang.Exception {
            super.tupleToArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void tupleIntClassTest() throws java.lang.Exception {
            super.tupleIntClassTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void tupleGetIntClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.tupleGetIntClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void tupleElementGetJavaTypeTest() throws java.lang.Exception {
            super.tupleElementGetJavaTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void tupleSelectionArrayTest() throws java.lang.Exception {
            super.tupleSelectionArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void array() throws java.lang.Exception {
            super.array();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void arrayIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.arrayIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void constructIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.constructIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void countDistinct() throws java.lang.Exception {
            super.countDistinct();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void andPredicates() throws java.lang.Exception {
            super.andPredicates();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void orPredicates() throws java.lang.Exception {
            super.orPredicates();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void isNull() throws java.lang.Exception {
            super.isNull();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void isNotNull() throws java.lang.Exception {
            super.isNotNull();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void parameter() throws java.lang.Exception {
            super.parameter();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void parameterCaseSensitiveTest() throws java.lang.Exception {
            super.parameterCaseSensitiveTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void criteriaBuilderValuesTest() throws java.lang.Exception {
            super.criteriaBuilderValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void criteriaBuilderIn1Test() throws java.lang.Exception {
            super.criteriaBuilderIn1Test();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void criteriaBuilderIn2Test() throws java.lang.Exception {
            super.criteriaBuilderIn2Test();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void criteriaBuilderInValueTest() throws java.lang.Exception {
            super.criteriaBuilderInValueTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void expressionInObjectArrayTest() throws java.lang.Exception {
            super.expressionInObjectArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void expressionInExpressionArrayTest() throws java.lang.Exception {
            super.expressionInExpressionArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void expressionInExpressionTest() throws java.lang.Exception {
            super.expressionInExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void expressionInCollectionTest() throws java.lang.Exception {
            super.expressionInCollectionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void parameterExpressionIsNullTest() throws java.lang.Exception {
            super.parameterExpressionIsNullTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void parameterExpressionIsNotNullTest() throws java.lang.Exception {
            super.parameterExpressionIsNotNullTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void likeExpStringTest() throws java.lang.Exception {
            super.likeExpStringTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void likeExpExpTest() throws java.lang.Exception {
            super.likeExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void notLikeExpStringTest() throws java.lang.Exception {
            super.notLikeExpStringTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void notLikeExpExpTest() throws java.lang.Exception {
            super.notLikeExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void notLikeExpExpExpTest() throws java.lang.Exception {
            super.notLikeExpExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void notLikeExpExpCharTest() throws java.lang.Exception {
            super.notLikeExpExpCharTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void notLikeExpStringExpTest() throws java.lang.Exception {
            super.notLikeExpStringExpTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void notLikeExpStringCharTest() throws java.lang.Exception {
            super.notLikeExpStringCharTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void concatExpStringTest() throws java.lang.Exception {
            super.concatExpStringTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void concatStringExpTest() throws java.lang.Exception {
            super.concatStringExpTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void concatExpExpTest() throws java.lang.Exception {
            super.concatExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void trimBothExpTest() throws java.lang.Exception {
            super.trimBothExpTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void lower() throws java.lang.Exception {
            super.lower();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void nullifExpressionExpressionTest() throws java.lang.Exception {
            super.nullifExpressionExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void nullifExpressionObjectTest() throws java.lang.Exception {
            super.nullifExpressionObjectTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void selectMultiSelectTest() throws java.lang.Exception {
            super.selectMultiSelectTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void multiRootTest() throws java.lang.Exception {
            super.multiRootTest();
        }


}