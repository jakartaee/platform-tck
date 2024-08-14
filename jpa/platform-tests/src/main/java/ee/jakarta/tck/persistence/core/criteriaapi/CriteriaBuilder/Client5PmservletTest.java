package ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder;

import ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client5;
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
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client5.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            ee.jakarta.tck.persistence.common.schema30.Util.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client5.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client5.class.getResource("//com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
           archiveProcessor.processWebArchive(jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web, Client5.class, warResURL);

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
            URL parURL = Client5.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsManifestResource(parURL, "persistence.xml");
            }
            archiveProcessor.processParArchive(jpa_core_criteriapia_CriteriaBuilder, Client5.class, parURL);
            // The orm.xml file
            parURL = Client5.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_criteriapia_CriteriaBuilder.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_criteriapia_CriteriaBuilder_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_criteriapia_CriteriaBuilder_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_criteriapia_CriteriaBuilder_vehicles_ear.addAsModule(jpa_core_criteriapia_CriteriaBuilder_pmservlet_vehicle_web);

            jpa_core_criteriapia_CriteriaBuilder_vehicles_ear.addAsLibrary(jpa_core_criteriapia_CriteriaBuilder);



            // The application.xml descriptor
            URL earResURL = Client5.class.getResource("/com/sun/ts/tests/jpa/core/criteriaapi/CriteriaBuilder/");
            if(earResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_vehicles_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client5.class.getResource("/com/sun/ts/tests/jpa/core/criteriaapi/CriteriaBuilder/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            archiveProcessor.processEarArchive(jpa_core_criteriapia_CriteriaBuilder_vehicles_ear, Client5.class, earResURL);
        return jpa_core_criteriapia_CriteriaBuilder_vehicles_ear;
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