package ee.jakarta.tck.persistence.core.query.apitests;

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
public class Client1AppmanagednotxTest extends ee.jakarta.tck.persistence.core.query.apitests.Client1 {
    static final String VEHICLE_ARCHIVE = "jpa_core_query_apitests_appmanagedNoTx_vehicle";

    public static void main(String[] args) {
      Client1AppmanagednotxTest theTests = new Client1AppmanagednotxTest();
      Status s = theTests.run(args, System.out, System.err);
      s.exit();
    }

    public void setup(String[] args, Properties p) throws Exception {
        super.setup(args, p);
    }

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
        Client:

        /com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml
        Ejb:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_core_query_apitests_appmanagedNoTx_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_apitests_vehicles_client.jar");
            // The class files
            jpa_core_query_apitests_appmanagedNoTx_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.web.AltWebVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            Client1.class,
            Client1AppmanagednotxTest.class
            );
            // The application-client.xml descriptor
            URL resURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml");
            if(resURL != null) {
              jpa_core_query_apitests_appmanagedNoTx_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_core_query_apitests_appmanagedNoTx_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jpa_core_query_apitests_appmanagedNoTx_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + com.sun.ts.tests.common.vehicle.VehicleClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_core_query_apitests_appmanagedNoTx_vehicle_client, Client1.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb.jar");
            // The class files
            jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                Fault.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ee.jakarta.tck.persistence.common.PMClientBase.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                EETest.class,
                ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                ee.jakarta.tck.persistence.core.query.apitests.Client1.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
                com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml");
            if(ejbResURL1 != null) {
//              jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client1.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb, Client1.class, ejbResURL1);


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

        // Ear
            EnterpriseArchive jpa_core_query_apitests_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_query_apitests_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_query_apitests_vehicles_ear.addAsModule(jpa_core_query_apitests_appmanagedNoTx_vehicle_ejb);
            jpa_core_query_apitests_vehicles_ear.addAsModule(jpa_core_query_apitests_appmanagedNoTx_vehicle_client);

            jpa_core_query_apitests_vehicles_ear.addAsLibrary(jpa_core_query_apitests);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client1.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_query_apitests_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_query_apitests_vehicles_ear, Client1.class, earResURL);
        return jpa_core_query_apitests_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setFirstResultTest() throws java.lang.Exception {
            super.setFirstResultTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setFirstResultIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setFirstResultIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getParameterTest() throws java.lang.Exception {
            super.getParameterTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getParameterIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getParameterIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getParameterIllegalArgumentException2Test() throws java.lang.Exception {
            super.getParameterIllegalArgumentException2Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getParameterIntClassTest() throws java.lang.Exception {
            super.getParameterIntClassTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getParameterIntIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getParameterIntIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getParameterValueParameterTest() throws java.lang.Exception {
            super.getParameterValueParameterTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getParameterValueParameterIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getParameterValueParameterIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getParameterValueParameterIllegalStateExceptionTest() throws java.lang.Exception {
            super.getParameterValueParameterIllegalStateExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameterParameterObjectIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterParameterObjectIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterParameterCalendarTemporalTypeIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameterParameterDateTemporalTypeIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterParameterDateTemporalTypeIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getParameterValueStringTest() throws java.lang.Exception {
            super.getParameterValueStringTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getParameterValueStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getParameterValueStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getParameterValueStringIllegalStateExceptionTest() throws java.lang.Exception {
            super.getParameterValueStringIllegalStateExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getParameterValueIntTest() throws java.lang.Exception {
            super.getParameterValueIntTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getParameterValueIntIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getParameterValueIntIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getParameterValueIntIllegalStateExceptionTest() throws java.lang.Exception {
            super.getParameterValueIntIllegalStateExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameter1Test() throws java.lang.Exception {
            super.setParameter1Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameter2Test() throws java.lang.Exception {
            super.setParameter2Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameterStringObject1IllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterStringObject1IllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameterStringObject2IllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterStringObject2IllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameterStringDateTemporalTypeTest() throws java.lang.Exception {
            super.setParameterStringDateTemporalTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameterStringDateTemporalTypeIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterStringDateTemporalTypeIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameterStringCalendarTemporalTypeTest() throws java.lang.Exception {
            super.setParameterStringCalendarTemporalTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameterStringCalendarTemporalTypeTestIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterStringCalendarTemporalTypeTestIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameterIntObjectTest() throws java.lang.Exception {
            super.setParameterIntObjectTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameterIntObjectIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterIntObjectIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameterIntDateTemporalTypeTest() throws java.lang.Exception {
            super.setParameterIntDateTemporalTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameterIntDateTemporalTypeIllegalArgumentException1Test() throws java.lang.Exception {
            super.setParameterIntDateTemporalTypeIllegalArgumentException1Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameterIntCalendarTemporalTypeTest() throws java.lang.Exception {
            super.setParameterIntCalendarTemporalTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setParameterIntCalendarTemporalTypeIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameter7Test() throws java.lang.Exception {
            super.setParameter7Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameterParameterCalendarTemporalTypeTest() throws java.lang.Exception {
            super.setParameterParameterCalendarTemporalTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameterParameterDateTemporalTypeTest() throws java.lang.Exception {
            super.setParameterParameterDateTemporalTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setParameter8Test() throws java.lang.Exception {
            super.setParameter8Test();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getSingleResultNoResultExceptionTest() throws java.lang.Exception {
            super.getSingleResultNoResultExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getSingleResultTransactionRequiredException() throws java.lang.Exception {
            super.getSingleResultTransactionRequiredException();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getSingleResultNonUniqueResultExceptionTest() throws java.lang.Exception {
            super.getSingleResultNonUniqueResultExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void isBoundTest() throws java.lang.Exception {
            super.isBoundTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setFirstResult() throws java.lang.Exception {
            super.setFirstResult();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryAPITest11() throws java.lang.Exception {
            super.queryAPITest11();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryAPITest12() throws java.lang.Exception {
            super.queryAPITest12();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setFirstResultIllegalArgumentException() throws java.lang.Exception {
            super.setFirstResultIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setGetMaxResultsTest() throws java.lang.Exception {
            super.setGetMaxResultsTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setMaxResultsIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.setMaxResultsIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getResultListTransactionRequiredExceptionTest() throws java.lang.Exception {
            super.getResultListTransactionRequiredExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void setMaxResults() throws java.lang.Exception {
            super.setMaxResults();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryAPITest16() throws java.lang.Exception {
            super.queryAPITest16();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryAPITest17() throws java.lang.Exception {
            super.queryAPITest17();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getSingleResultTest() throws java.lang.Exception {
            super.getSingleResultTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getSingleResultIllegalStateException() throws java.lang.Exception {
            super.getSingleResultIllegalStateException();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void executeUpdateIllegalStateException() throws java.lang.Exception {
            super.executeUpdateIllegalStateException();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void executeUpdateTransactionRequiredExceptionTest() throws java.lang.Exception {
            super.executeUpdateTransactionRequiredExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryAPITest21() throws java.lang.Exception {
            super.queryAPITest21();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryAPITest22() throws java.lang.Exception {
            super.queryAPITest22();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryAPITest23() throws java.lang.Exception {
            super.queryAPITest23();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryAPITest24() throws java.lang.Exception {
            super.queryAPITest24();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryAPITest25() throws java.lang.Exception {
            super.queryAPITest25();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void queryAPITest27() throws java.lang.Exception {
            super.queryAPITest27();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getResultListIllegalStateException() throws java.lang.Exception {
            super.getResultListIllegalStateException();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void noTransactionLockModeTypeNoneTest() throws java.lang.Exception {
            super.noTransactionLockModeTypeNoneTest();
        }

}
