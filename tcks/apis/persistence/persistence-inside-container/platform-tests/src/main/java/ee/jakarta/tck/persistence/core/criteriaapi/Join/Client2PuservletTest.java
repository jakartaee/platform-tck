package ee.jakarta.tck.persistence.core.criteriaapi.Join;

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
public class Client2PuservletTest extends ee.jakarta.tck.persistence.core.criteriaapi.Join.Client2 {
    static final String VEHICLE_ARCHIVE = "jpa_core_criteriaapi_Join_puservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_criteriaapi_Join: META-INF/persistence.xml
        jpa_core_criteriaapi_Join_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_Join_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_Join_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_Join_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_criteriaapi_Join_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_criteriaapi_Join_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_Join_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_Join_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_criteriaapi_Join_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_criteriaapi_Join_vehicles: 

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
            WebArchive jpa_core_criteriaapi_Join_puservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_criteriaapi_Join_puservlet_vehicle_web.war");
            // The class files
            jpa_core_criteriaapi_Join_puservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.core.criteriaapi.Join.Client2.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            ee.jakarta.tck.persistence.common.schema30.Util.class,
            com.sun.ts.lib.harness.Status.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The web.xml descriptor
            URL warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_Join_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_Join_puservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client2.class.getResource("/com/sun/ts/tests/jpa/core/criteriaapi/Join/jpa_core_criteriaapi_Join.jar");
            if(warResURL != null) {
              jpa_core_criteriaapi_Join_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_criteriaapi_Join.jar");
            }
            warResURL = Client2.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriaapi_Join_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/puservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_criteriaapi_Join_puservlet_vehicle_web, Client2.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriaapi_Join = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriaapi_Join.jar");
            // The class files
            jpa_core_criteriaapi_Join.addClasses(
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
              jpa_core_criteriaapi_Join.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client2.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_Join.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client2.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_Join.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client2.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_criteriaapi_Join.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_criteriaapi_Join, Client2.class, parURL);
            parURL = Client2.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_criteriaapi_Join.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_core_criteriaapi_Join_puservlet_vehicle_web.addAsLibrary(jpa_core_criteriaapi_Join);
            return jpa_core_criteriaapi_Join_puservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinStringTest() throws java.lang.Exception {
            super.joinStringTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinStringJoinTypeTest() throws java.lang.Exception {
            super.joinStringJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinSingularAttributeTest() throws java.lang.Exception {
            super.joinSingularAttributeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinSingularAttributeJoinTypeTest() throws java.lang.Exception {
            super.joinSingularAttributeJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinCollectionAttributeTest() throws java.lang.Exception {
            super.joinCollectionAttributeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinCollectionAttributeJoinTypeTest() throws java.lang.Exception {
            super.joinCollectionAttributeJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinCollectionStringTest() throws java.lang.Exception {
            super.joinCollectionStringTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinCollectionStringJoinTypeTest() throws java.lang.Exception {
            super.joinCollectionStringJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinSetAttributeTest() throws java.lang.Exception {
            super.joinSetAttributeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinSetAttributeJoinTypeTest() throws java.lang.Exception {
            super.joinSetAttributeJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinSetStringTest() throws java.lang.Exception {
            super.joinSetStringTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinSetStringJoinTypeTest() throws java.lang.Exception {
            super.joinSetStringJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinListAttributeTest() throws java.lang.Exception {
            super.joinListAttributeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinListAttributeJoinTypeTest() throws java.lang.Exception {
            super.joinListAttributeJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinListStringTest() throws java.lang.Exception {
            super.joinListStringTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void joinListStringJoinTypeTest() throws java.lang.Exception {
            super.joinListStringJoinTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void pluralJoinTest() throws java.lang.Exception {
            super.pluralJoinTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void pluralJoinOnExpressionTest() throws java.lang.Exception {
            super.pluralJoinOnExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void pluralJoinOnPredicateArrayTest() throws java.lang.Exception {
            super.pluralJoinOnPredicateArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void collectionJoinOnExpressionTest() throws java.lang.Exception {
            super.collectionJoinOnExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void collectionJoinOnPredicateArrayTest() throws java.lang.Exception {
            super.collectionJoinOnPredicateArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void listJoinOnExpressionTest() throws java.lang.Exception {
            super.listJoinOnExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void listJoinOnPredicateArrayTest() throws java.lang.Exception {
            super.listJoinOnPredicateArrayTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void setJoinOnExpressionTest() throws java.lang.Exception {
            super.setJoinOnExpressionTest();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void setJoinOnPredicateArrayTest() throws java.lang.Exception {
            super.setJoinOnPredicateArrayTest();
        }


}