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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("persistence")
@Tag("platform")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class Client5AppmanagedTest extends ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client5 {
    static final String VEHICLE_ARCHIVE = "jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle";

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

        /com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_client.xml
        Ejb:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder_vehicles_client.jar");
            // The class files
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            ee.jakarta.tck.persistence.common.schema30.Util.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            Client5.class,
            Client5AppmanagedTest.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The application-client.xml descriptor
            URL resURL = Client5.class.getResource("/com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_client.xml");
            if(resURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client5.class.getResource("//com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client5.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client, Client5.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb.jar");
            // The class files
            jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ee.jakarta.tck.persistence.common.PMClientBase.class,
                ee.jakarta.tck.persistence.core.criteriaapi.CriteriaBuilder.Client5.class,
                ee.jakarta.tck.persistence.common.schema30.Util.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleBean.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            ).addClasses(ee.jakarta.tck.persistence.common.schema30.Util.getSchema30classes());
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client5.class.getResource("//vehicle/appmanaged/appmanaged_vehicle_ejb.xml");
            if(ejbResURL1 != null) {
              jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client5.class.getResource("//vehicle/appmanaged/appmanaged_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb, Client5.class, ejbResURL1);


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

        // Ear
            EnterpriseArchive jpa_core_criteriapia_CriteriaBuilder_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_criteriapia_CriteriaBuilder_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_criteriapia_CriteriaBuilder_vehicles_ear.addAsModule(jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_ejb);
            jpa_core_criteriapia_CriteriaBuilder_vehicles_ear.addAsModule(jpa_core_criteriapia_CriteriaBuilder_appmanaged_vehicle_client);

            jpa_core_criteriapia_CriteriaBuilder_vehicles_ear.addAsLibrary(jpa_core_criteriapia_CriteriaBuilder);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client5.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_criteriapia_CriteriaBuilder_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_criteriapia_CriteriaBuilder_vehicles_ear, Client5.class, earResURL);
        return jpa_core_criteriapia_CriteriaBuilder_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void primaryKeyJoinColumnTest() throws java.lang.Exception {
            super.primaryKeyJoinColumnTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void asc() throws java.lang.Exception {
            super.asc();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void desc() throws java.lang.Exception {
            super.desc();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void sumExpTest() throws java.lang.Exception {
            super.sumExpTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void sumExpNumTest() throws java.lang.Exception {
            super.sumExpNumTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void sumNumExpTest() throws java.lang.Exception {
            super.sumNumExpTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void sumExpExpTest() throws java.lang.Exception {
            super.sumExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void exists() throws java.lang.Exception {
            super.exists();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void subqueryFromEntityTypeTest() throws java.lang.Exception {
            super.subqueryFromEntityTypeTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void all() throws java.lang.Exception {
            super.all();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void sumAsDoubleTest() throws java.lang.Exception {
            super.sumAsDoubleTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void sumAsLongTest() throws java.lang.Exception {
            super.sumAsLongTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void lessThanExpNumTest() throws java.lang.Exception {
            super.lessThanExpNumTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void lessThanExpExpTest() throws java.lang.Exception {
            super.lessThanExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void lessThanOrEqualToExpNumTest() throws java.lang.Exception {
            super.lessThanOrEqualToExpNumTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void lessThanOrEqualToExpExpTest() throws java.lang.Exception {
            super.lessThanOrEqualToExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void between() throws java.lang.Exception {
            super.between();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void ltExpNumTest() throws java.lang.Exception {
            super.ltExpNumTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void ltExpExpTest() throws java.lang.Exception {
            super.ltExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void leExpNumTest() throws java.lang.Exception {
            super.leExpNumTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void leExpExpTest() throws java.lang.Exception {
            super.leExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void neg() throws java.lang.Exception {
            super.neg();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void prodExpNumTest() throws java.lang.Exception {
            super.prodExpNumTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void prodNumExpTest() throws java.lang.Exception {
            super.prodNumExpTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void prodExpExpTest() throws java.lang.Exception {
            super.prodExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void diffExpNumberTest() throws java.lang.Exception {
            super.diffExpNumberTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void diffNumberExpTest() throws java.lang.Exception {
            super.diffNumberExpTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void diffExpExpTest() throws java.lang.Exception {
            super.diffExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void quotExpNumTest() throws java.lang.Exception {
            super.quotExpNumTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void quotNumExpTest() throws java.lang.Exception {
            super.quotNumExpTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void quotExpExpTest() throws java.lang.Exception {
            super.quotExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void modExpIntTest() throws java.lang.Exception {
            super.modExpIntTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void modExpExpTest() throws java.lang.Exception {
            super.modExpExpTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void modIntExpTest() throws java.lang.Exception {
            super.modIntExpTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void sqrt() throws java.lang.Exception {
            super.sqrt();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void toLong() throws java.lang.Exception {
            super.toLong();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void toInteger() throws java.lang.Exception {
            super.toInteger();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void toFloat() throws java.lang.Exception {
            super.toFloat();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void toDouble() throws java.lang.Exception {
            super.toDouble();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void toBigDecimal() throws java.lang.Exception {
            super.toBigDecimal();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void toBigInteger() throws java.lang.Exception {
            super.toBigInteger();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void toStringTest() throws java.lang.Exception {
            super.toStringTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void literal() throws java.lang.Exception {
            super.literal();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void currentDate() throws java.lang.Exception {
            super.currentDate();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void currentTime() throws java.lang.Exception {
            super.currentTime();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void currentTimestamp() throws java.lang.Exception {
            super.currentTimestamp();
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void treatPathClassTest() throws java.lang.Exception {
            super.treatPathClassTest();
        }

}
