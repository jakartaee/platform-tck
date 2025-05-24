package ee.jakarta.tck.persistence.core.query.apitests;

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
public class Client1PmservletTest extends ee.jakarta.tck.persistence.core.query.apitests.Client1 {
    static final String VEHICLE_ARCHIVE = "jpa_core_query_apitests_pmservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_query_apitests: META-INF/persistence.xml
        jpa_core_query_apitests_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_query_apitests_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_query_apitests_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_query_apitests_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_query_apitests_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_query_apitests_vehicles: 

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
            WebArchive jpa_core_query_apitests_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_query_apitests_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_query_apitests_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
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
            ee.jakarta.tck.persistence.core.query.apitests.Client1.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_query_apitests_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_query_apitests_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client1.class.getResource("/com/sun/ts/tests/jpa/core/query/apitests/jpa_core_query_apitests.jar");
            if(warResURL != null) {
              jpa_core_query_apitests_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_query_apitests.jar");
            }
            warResURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_query_apitests_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/pmservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_query_apitests_pmservlet_vehicle_web, Client1.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_query_apitests = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_apitests.jar");
            // The class files
            jpa_core_query_apitests.addClasses(
                ee.jakarta.tck.persistence.core.query.apitests.Employee.class,
                ee.jakarta.tck.persistence.core.query.apitests.Department.class,
                ee.jakarta.tck.persistence.core.query.apitests.DataTypes2.class,
                ee.jakarta.tck.persistence.core.query.apitests.Insurance.class
            );
            // The persistence.xml descriptor
            URL parURL = Client1.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_query_apitests.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client1.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_query_apitests.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client1.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_query_apitests.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client1.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_query_apitests.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_query_apitests, Client1.class, parURL);
            parURL = Client1.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_query_apitests.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_core_query_apitests_pmservlet_vehicle_web.addAsLibrary(jpa_core_query_apitests);
            return jpa_core_query_apitests_pmservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setFirstResultTest() throws java.lang.Exception {
            super.setFirstResultTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setFirstResultIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setFirstResultIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getParameterTest() throws java.lang.Exception {
            super.getParameterTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getParameterIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getParameterIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getParameterIllegalArgumentException2Test() throws java.lang.Exception {
            super.getParameterIllegalArgumentException2Test();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getParameterIntClassTest() throws java.lang.Exception {
            super.getParameterIntClassTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getParameterIntIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getParameterIntIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getParameterValueParameterTest() throws java.lang.Exception {
            super.getParameterValueParameterTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getParameterValueParameterIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getParameterValueParameterIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getParameterValueParameterIllegalStateExceptionTest() throws java.lang.Exception {
            super.getParameterValueParameterIllegalStateExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterParameterObjectIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterParameterObjectIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterParameterDateTemporalTypeIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterParameterDateTemporalTypeIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getParameterValueStringTest() throws java.lang.Exception {
            super.getParameterValueStringTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getParameterValueStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getParameterValueStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getParameterValueStringIllegalStateExceptionTest() throws java.lang.Exception {
            super.getParameterValueStringIllegalStateExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getParameterValueIntTest() throws java.lang.Exception {
            super.getParameterValueIntTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getParameterValueIntIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getParameterValueIntIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getParameterValueIntIllegalStateExceptionTest() throws java.lang.Exception {
            super.getParameterValueIntIllegalStateExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameter1Test() throws java.lang.Exception {
            super.setParameter1Test();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameter2Test() throws java.lang.Exception {
            super.setParameter2Test();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterStringObject1IllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterStringObject1IllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterStringObject2IllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterStringObject2IllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterStringDateTemporalTypeTest() throws java.lang.Exception {
            super.setParameterStringDateTemporalTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterStringDateTemporalTypeIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterStringDateTemporalTypeIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterStringCalendarTemporalTypeTest() throws java.lang.Exception {
            super.setParameterStringCalendarTemporalTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterStringCalendarTemporalTypeTestIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterStringCalendarTemporalTypeTestIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterIntObjectTest() throws java.lang.Exception {
            super.setParameterIntObjectTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterIntObjectIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterIntObjectIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterIntDateTemporalTypeTest() throws java.lang.Exception {
            super.setParameterIntDateTemporalTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterIntDateTemporalTypeIllegalArgumentException1Test() throws java.lang.Exception {
            super.setParameterIntDateTemporalTypeIllegalArgumentException1Test();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterIntCalendarTemporalTypeTest() throws java.lang.Exception {
            super.setParameterIntCalendarTemporalTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameter7Test() throws java.lang.Exception {
            super.setParameter7Test();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterParameterCalendarTemporalTypeTest() throws java.lang.Exception {
            super.setParameterParameterCalendarTemporalTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameterParameterDateTemporalTypeTest() throws java.lang.Exception {
            super.setParameterParameterDateTemporalTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setParameter8Test() throws java.lang.Exception {
            super.setParameter8Test();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getSingleResultNoResultExceptionTest() throws java.lang.Exception {
            super.getSingleResultNoResultExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getSingleResultTransactionRequiredException() throws java.lang.Exception {
            super.getSingleResultTransactionRequiredException();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getSingleResultNonUniqueResultExceptionTest() throws java.lang.Exception {
            super.getSingleResultNonUniqueResultExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void isBoundTest() throws java.lang.Exception {
            super.isBoundTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setFirstResult() throws java.lang.Exception {
            super.setFirstResult();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryAPITest11() throws java.lang.Exception {
            super.queryAPITest11();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryAPITest12() throws java.lang.Exception {
            super.queryAPITest12();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setFirstResultIllegalArgumentException() throws java.lang.Exception {
            super.setFirstResultIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setGetMaxResultsTest() throws java.lang.Exception {
            super.setGetMaxResultsTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setMaxResultsIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setMaxResultsIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getResultListTransactionRequiredExceptionTest() throws java.lang.Exception {
            super.getResultListTransactionRequiredExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void setMaxResults() throws java.lang.Exception {
            super.setMaxResults();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryAPITest16() throws java.lang.Exception {
            super.queryAPITest16();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryAPITest17() throws java.lang.Exception {
            super.queryAPITest17();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getSingleResultTest() throws java.lang.Exception {
            super.getSingleResultTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getSingleResultIllegalStateException() throws java.lang.Exception {
            super.getSingleResultIllegalStateException();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void executeUpdateIllegalStateException() throws java.lang.Exception {
            super.executeUpdateIllegalStateException();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void executeUpdateTransactionRequiredExceptionTest() throws java.lang.Exception {
            super.executeUpdateTransactionRequiredExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryAPITest21() throws java.lang.Exception {
            super.queryAPITest21();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryAPITest22() throws java.lang.Exception {
            super.queryAPITest22();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryAPITest23() throws java.lang.Exception {
            super.queryAPITest23();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryAPITest24() throws java.lang.Exception {
            super.queryAPITest24();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryAPITest25() throws java.lang.Exception {
            super.queryAPITest25();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void queryAPITest27() throws java.lang.Exception {
            super.queryAPITest27();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getResultListIllegalStateException() throws java.lang.Exception {
            super.getResultListIllegalStateException();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void noTransactionLockModeTypeNoneTest() throws java.lang.Exception {
            super.noTransactionLockModeTypeNoneTest();
        }


}