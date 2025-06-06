package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

import java.net.URL;
import java.util.Properties;


@ExtendWith(ArquillianExtension.class)
@Tag("persistence")
@Tag("platform")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class Client2Stateless3Test extends ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client2 {
    static final String VEHICLE_ARCHIVE = "jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle";

    public static void main(String[] args) {
      Client2Stateless3Test theTests = new Client2Stateless3Test();
      Status s = theTests.run(args, System.out, System.err);
      s.exit();
    }

    public void setup(String[] args, Properties p) throws Exception {
        super.setup(args, p);
    }

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
        Client:

        /com/sun/ts/tests/common/vehicle/stateless3/stateless3_vehicle_client.xml
        Ejb:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder_vehicles_client.jar");
            // The class files
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            ee.jakarta.tck.persistence.common.schema30.Util.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class,
            EETest.class,
            com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleRunner.class,
            com.sun.ts.tests.common.vehicle.web.AltWebVehicleRunner.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            Client2.class,
            Client2Stateless3Test.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The application-client.xml descriptor
            URL resURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/stateless3/stateless3_vehicle_client.xml");
            if(resURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/stateless3/stateless3_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + com.sun.ts.tests.common.vehicle.VehicleClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client, Client2.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb.jar");
            // The class files
            jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                Fault.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ee.jakarta.tck.persistence.common.PMClientBase.class,
                ee.jakarta.tck.persistence.common.schema30.Util.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleBean.class,
                ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client2.class,
                com.sun.ts.tests.common.vehicle.stateless3.Stateless3VehicleIF.class,
                EETest.class,
                ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/stateless3/stateless3_vehicle_client.xml");
            if(ejbResURL1 != null) {
//              jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/stateless3/stateless3_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb, Client2.class, ejbResURL1);


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

        // Ear
            EnterpriseArchive jpa_core_criteriapia_CriteriaBuilder_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_criteriapia_CriteriaBuilder_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_criteriapia_CriteriaBuilder_vehicles_ear.addAsModule(jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_ejb);
            jpa_core_criteriapia_CriteriaBuilder_vehicles_ear.addAsModule(jpa_core_criteriapia_CriteriaBuilder_stateless3_vehicle_client);

            jpa_core_criteriapia_CriteriaBuilder_vehicles_ear.addAsLibrary(jpa_core_criteriapia_CriteriaBuilder);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client2.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_criteriapia_CriteriaBuilder_vehicles_ear, Client2.class, earResURL);
        return jpa_core_criteriapia_CriteriaBuilder_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void construct() throws java.lang.Exception {
            super.construct();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void tupleIntTest() throws java.lang.Exception {
            super.tupleIntTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void tupleToArrayTest() throws java.lang.Exception {
            super.tupleToArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void tupleIntClassTest() throws java.lang.Exception {
            super.tupleIntClassTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void tupleGetIntClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.tupleGetIntClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void tupleElementGetJavaTypeTest() throws java.lang.Exception {
            super.tupleElementGetJavaTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void tupleSelectionArrayTest() throws java.lang.Exception {
            super.tupleSelectionArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void array() throws java.lang.Exception {
            super.array();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void arrayIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.arrayIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void constructIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.constructIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void countDistinct() throws java.lang.Exception {
            super.countDistinct();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void andPredicates() throws java.lang.Exception {
            super.andPredicates();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void orPredicates() throws java.lang.Exception {
            super.orPredicates();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void isNull() throws java.lang.Exception {
            super.isNull();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void isNotNull() throws java.lang.Exception {
            super.isNotNull();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void parameter() throws java.lang.Exception {
            super.parameter();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void parameterCaseSensitiveTest() throws java.lang.Exception {
            super.parameterCaseSensitiveTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void criteriaBuilderValuesTest() throws java.lang.Exception {
            super.criteriaBuilderValuesTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void criteriaBuilderIn1Test() throws java.lang.Exception {
            super.criteriaBuilderIn1Test();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void criteriaBuilderIn2Test() throws java.lang.Exception {
            super.criteriaBuilderIn2Test();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void criteriaBuilderInValueTest() throws java.lang.Exception {
            super.criteriaBuilderInValueTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void expressionInObjectArrayTest() throws java.lang.Exception {
            super.expressionInObjectArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void expressionInExpressionArrayTest() throws java.lang.Exception {
            super.expressionInExpressionArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void expressionInExpressionTest() throws java.lang.Exception {
            super.expressionInExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void expressionInCollectionTest() throws java.lang.Exception {
            super.expressionInCollectionTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void parameterExpressionIsNullTest() throws java.lang.Exception {
            super.parameterExpressionIsNullTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void parameterExpressionIsNotNullTest() throws java.lang.Exception {
            super.parameterExpressionIsNotNullTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void likeExpStringTest() throws java.lang.Exception {
            super.likeExpStringTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void likeExpExpTest() throws java.lang.Exception {
            super.likeExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void notLikeExpStringTest() throws java.lang.Exception {
            super.notLikeExpStringTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void notLikeExpExpTest() throws java.lang.Exception {
            super.notLikeExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void notLikeExpExpExpTest() throws java.lang.Exception {
            super.notLikeExpExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void notLikeExpExpCharTest() throws java.lang.Exception {
            super.notLikeExpExpCharTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void notLikeExpStringExpTest() throws java.lang.Exception {
            super.notLikeExpStringExpTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void notLikeExpStringCharTest() throws java.lang.Exception {
            super.notLikeExpStringCharTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void concatExpStringTest() throws java.lang.Exception {
            super.concatExpStringTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void concatStringExpTest() throws java.lang.Exception {
            super.concatStringExpTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void concatExpExpTest() throws java.lang.Exception {
            super.concatExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void trimBothExpTest() throws java.lang.Exception {
            super.trimBothExpTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void lower() throws java.lang.Exception {
            super.lower();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void nullifExpressionExpressionTest() throws java.lang.Exception {
            super.nullifExpressionExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void nullifExpressionObjectTest() throws java.lang.Exception {
            super.nullifExpressionObjectTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void selectMultiSelectTest() throws java.lang.Exception {
            super.selectMultiSelectTest();
        }

        @Test
        @Override
        @TargetVehicle("stateless3")
        public void multiRootTest() throws java.lang.Exception {
            super.multiRootTest();
        }

}
