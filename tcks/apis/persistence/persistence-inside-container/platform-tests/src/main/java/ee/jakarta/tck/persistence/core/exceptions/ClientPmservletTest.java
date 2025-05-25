package ee.jakarta.tck.persistence.core.exceptions;

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
public class ClientPmservletTest extends ee.jakarta.tck.persistence.core.exceptions.Client {
    static final String VEHICLE_ARCHIVE = "jpa_core_exceptions_pmservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_exceptions: META-INF/persistence.xml
        jpa_core_exceptions_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_exceptions_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_exceptions_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_exceptions_vehicles: 

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
            WebArchive jpa_core_exceptions_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_exceptions_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_exceptions_pmservlet_vehicle_web.addClasses(
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
            ee.jakarta.tck.persistence.core.exceptions.Client.class,
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
              jpa_core_exceptions_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_exceptions_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/core/exceptions/jpa_core_exceptions.jar");
            if(warResURL != null) {
              jpa_core_exceptions_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_exceptions.jar");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_exceptions_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/pmservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_exceptions_pmservlet_vehicle_web, Client.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_exceptions = ShrinkWrap.create(JavaArchive.class, "jpa_core_exceptions.jar");
            // The class files
            jpa_core_exceptions.addClasses(
                ee.jakarta.tck.persistence.core.exceptions.Coffee.class
            );
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_exceptions.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_exceptions.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_exceptions.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_exceptions.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_exceptions, Client.class, parURL);
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_exceptions.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_core_exceptions_pmservlet_vehicle_web.addAsLibrary(jpa_core_exceptions);
            return jpa_core_exceptions_pmservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void TransactionRequiredExceptionTest() throws java.lang.Exception {
            super.TransactionRequiredExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void TransactionRequiredException2Test() throws java.lang.Exception {
            super.TransactionRequiredException2Test();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void exceptionTest2() throws java.lang.Exception {
            super.exceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void exceptionTest3() throws java.lang.Exception {
            super.exceptionTest3();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void exceptionTest4() throws java.lang.Exception {
            super.exceptionTest4();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void exceptionTest5() throws java.lang.Exception {
            super.exceptionTest5();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void exceptionTest6() throws java.lang.Exception {
            super.exceptionTest6();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void RollbackExceptionTest() throws java.lang.Exception {
            super.RollbackExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void EntityExistsExceptionTest() throws java.lang.Exception {
            super.EntityExistsExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void EntityNotFoundExceptionTest() throws java.lang.Exception {
            super.EntityNotFoundExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void OptimisticLockExceptionTest() throws java.lang.Exception {
            super.OptimisticLockExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void PersistenceExceptionTest() throws java.lang.Exception {
            super.PersistenceExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void LockTimeoutExceptionTest() throws java.lang.Exception {
            super.LockTimeoutExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void PessimisticLockExceptionTest() throws java.lang.Exception {
            super.PessimisticLockExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void QueryTimeoutExceptionTest() throws java.lang.Exception {
            super.QueryTimeoutExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void NonUniqueResultExceptionTest() throws java.lang.Exception {
            super.NonUniqueResultExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void NoResultExceptionTest() throws java.lang.Exception {
            super.NoResultExceptionTest();
        }


}