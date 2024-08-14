package ee.jakarta.tck.persistence.core.StoredProcedureQuery;

import ee.jakarta.tck.persistence.core.StoredProcedureQuery.Client1;
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
@Tag("jpa")
@Tag("javaee")
@Tag("javaee_web_profile")

public class Client1PuservletTest extends ee.jakarta.tck.persistence.core.StoredProcedureQuery.Client1 {
    static final String VEHICLE_ARCHIVE = "jpa_core_StoredProcedureQuery_puservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_StoredProcedureQuery: myMappingFile.xml,META-INF/persistence.xml
        jpa_core_StoredProcedureQuery_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_StoredProcedureQuery_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_StoredProcedureQuery_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_StoredProcedureQuery_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_StoredProcedureQuery_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_StoredProcedureQuery_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_StoredProcedureQuery_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_StoredProcedureQuery_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_StoredProcedureQuery_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_StoredProcedureQuery_vehicles: 

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
            WebArchive jpa_core_StoredProcedureQuery_puservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_StoredProcedureQuery_puservlet_vehicle_web.war");
            // The class files
            jpa_core_StoredProcedureQuery_puservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            ee.jakarta.tck.persistence.core.StoredProcedureQuery.Client1.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_StoredProcedureQuery_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client1.class.getResource("//com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_StoredProcedureQuery_puservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
           archiveProcessor.processWebArchive(jpa_core_StoredProcedureQuery_puservlet_vehicle_web, Client1.class, warResURL);

        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_StoredProcedureQuery = ShrinkWrap.create(JavaArchive.class, "jpa_core_StoredProcedureQuery.jar");
            // The class files
            jpa_core_StoredProcedureQuery.addClasses(
                ee.jakarta.tck.persistence.core.StoredProcedureQuery.Employee2.class,
                ee.jakarta.tck.persistence.core.StoredProcedureQuery.Employee.class,
                ee.jakarta.tck.persistence.core.StoredProcedureQuery.EmployeeMappedSC.class
            );
            // The persistence.xml descriptor
            URL parURL = Client1.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_StoredProcedureQuery.addAsManifestResource(parURL, "persistence.xml");
            }
            archiveProcessor.processParArchive(jpa_core_StoredProcedureQuery, Client1.class, parURL);
            // The orm.xml file
            parURL = Client1.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_StoredProcedureQuery.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_StoredProcedureQuery_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_StoredProcedureQuery_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_StoredProcedureQuery_vehicles_ear.addAsModule(jpa_core_StoredProcedureQuery_puservlet_vehicle_web);

            jpa_core_StoredProcedureQuery_vehicles_ear.addAsLibrary(jpa_core_StoredProcedureQuery);



            // The application.xml descriptor
            URL earResURL = Client1.class.getResource("/com/sun/ts/tests/jpa/core/StoredProcedureQuery/");
            if(earResURL != null) {
              jpa_core_StoredProcedureQuery_vehicles_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client1.class.getResource("/com/sun/ts/tests/jpa/core/StoredProcedureQuery/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_StoredProcedureQuery_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            archiveProcessor.processEarArchive(jpa_core_StoredProcedureQuery_vehicles_ear, Client1.class, earResURL);
        return jpa_core_StoredProcedureQuery_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void executeTest() throws java.lang.Exception {
            super.executeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getOutputParameterValueIntTest() throws java.lang.Exception {
            super.getOutputParameterValueIntTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getOutputParameterValueIntIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getOutputParameterValueIntIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getFirstResultTest() throws java.lang.Exception {
            super.getFirstResultTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getMaxResultsTest() throws java.lang.Exception {
            super.getMaxResultsTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getSingleResultTest() throws java.lang.Exception {
            super.getSingleResultTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getSingleResultOrNullWithValueTest() throws java.lang.Exception {
            super.getSingleResultOrNullWithValueTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getSingleResultOrNullWithNullTest() throws java.lang.Exception {
            super.getSingleResultOrNullWithNullTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getSingleResultNoResultExceptionTest() throws java.lang.Exception {
            super.getSingleResultNoResultExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getSingleResultNonUniqueResultExceptionTest() throws java.lang.Exception {
            super.getSingleResultNonUniqueResultExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void setgetFlushModeTest() throws java.lang.Exception {
            super.setgetFlushModeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void setLockModeIllegalStateExceptionTest() throws java.lang.Exception {
            super.setLockModeIllegalStateExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getLockModeIllegalStateExceptionTest() throws java.lang.Exception {
            super.getLockModeIllegalStateExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void setGetParameterIntTest() throws java.lang.Exception {
            super.setGetParameterIntTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getParameterStringExceptionTest() throws java.lang.Exception {
            super.getParameterStringExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getParameterIntIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getParameterIntIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void setParameterParameterObjectTest() throws java.lang.Exception {
            super.setParameterParameterObjectTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void setParameterParameterObjectIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterParameterObjectIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void setParameterIntObjectIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterIntObjectIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getParametersTest() throws java.lang.Exception {
            super.getParametersTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void setParameterIntDateTemporalTypeTest() throws java.lang.Exception {
            super.setParameterIntDateTemporalTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void setParameterIntDateTemporalTypeIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterIntDateTemporalTypeIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void setParameterParameterDateTemporalTypeTest() throws java.lang.Exception {
            super.setParameterParameterDateTemporalTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void setParameterParameterDateTemporalTypeIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterParameterDateTemporalTypeIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void executeUpdateOfAnUpdateTest() throws java.lang.Exception {
            super.executeUpdateOfAnUpdateTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void executeUpdateOfADeleteTest() throws java.lang.Exception {
            super.executeUpdateOfADeleteTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void executeUpdateTransactionRequiredExceptionTest() throws java.lang.Exception {
            super.executeUpdateTransactionRequiredExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getParameterValueParameterTest() throws java.lang.Exception {
            super.getParameterValueParameterTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getParameterValueParameterIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getParameterValueParameterIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getParameterValueParameterIllegalStateExceptionTest() throws java.lang.Exception {
            super.getParameterValueParameterIllegalStateExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getParameterValueIntTest() throws java.lang.Exception {
            super.getParameterValueIntTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getParameterValueIntIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getParameterValueIntIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getParameterValueIntIllegalStateExceptionTest() throws java.lang.Exception {
            super.getParameterValueIntIllegalStateExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void setHintStringObjectTest() throws java.lang.Exception {
            super.setHintStringObjectTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void xmlOverridesNamedStoredProcedureQueryTest() throws java.lang.Exception {
            super.xmlOverridesNamedStoredProcedureQueryTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void xmlOverridesSqlResultSetMappingAnnotationTest() throws java.lang.Exception {
            super.xmlOverridesSqlResultSetMappingAnnotationTest();
        }


}
