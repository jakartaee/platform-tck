package ee.jakarta.tck.persistence.core.metamodelapi.pluralattribute;

import ee.jakarta.tck.persistence.core.metamodelapi.pluralattribute.Client;
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
@Tag("web")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientPuservletTest extends ee.jakarta.tck.persistence.core.metamodelapi.pluralattribute.Client {
    static final String VEHICLE_ARCHIVE = "jpa_core_metamodelapi_pluralattribute_puservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_metamodelapi_pluralattribute: META-INF/persistence.xml
        jpa_core_metamodelapi_pluralattribute_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_metamodelapi_pluralattribute_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_metamodelapi_pluralattribute_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_metamodelapi_pluralattribute_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_metamodelapi_pluralattribute_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_metamodelapi_pluralattribute_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_metamodelapi_pluralattribute_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_metamodelapi_pluralattribute_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_metamodelapi_pluralattribute_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_metamodelapi_pluralattribute_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_metamodelapi_pluralattribute_vehicles: 

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
            WebArchive jpa_core_metamodelapi_pluralattribute_puservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_metamodelapi_pluralattribute_puservlet_vehicle_web.war");
            // The class files
            jpa_core_metamodelapi_pluralattribute_puservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            ee.jakarta.tck.persistence.core.metamodelapi.pluralattribute.Client.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_metamodelapi_pluralattribute_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_metamodelapi_pluralattribute_puservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/core/metamodelapi/pluralattribute/jpa_core_metamodelapi_pluralattribute.jar");
            if(warResURL != null) {
              jpa_core_metamodelapi_pluralattribute_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_metamodelapi_pluralattribute.jar");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_metamodelapi_pluralattribute_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/puservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_metamodelapi_pluralattribute_puservlet_vehicle_web, Client.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_metamodelapi_pluralattribute = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_pluralattribute.jar");
            // The class files
            jpa_core_metamodelapi_pluralattribute.addClasses(
                ee.jakarta.tck.persistence.core.metamodelapi.pluralattribute.Uni1XMProject.class,
                ee.jakarta.tck.persistence.core.metamodelapi.pluralattribute.Uni1XMPerson.class
            );
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_metamodelapi_pluralattribute.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_metamodelapi_pluralattribute.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_metamodelapi_pluralattribute.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_metamodelapi_pluralattribute.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_metamodelapi_pluralattribute, Client.class, parURL);
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_metamodelapi_pluralattribute.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_metamodelapi_pluralattribute_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_metamodelapi_pluralattribute_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_metamodelapi_pluralattribute_vehicles_ear.addAsModule(jpa_core_metamodelapi_pluralattribute_puservlet_vehicle_web);

            jpa_core_metamodelapi_pluralattribute_vehicles_ear.addAsLibrary(jpa_core_metamodelapi_pluralattribute);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_metamodelapi_pluralattribute_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_metamodelapi_pluralattribute_vehicles_ear, Client.class, earResURL);
        return jpa_core_metamodelapi_pluralattribute_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getCollectionType() throws java.lang.Exception {
            super.getCollectionType();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getElementType() throws java.lang.Exception {
            super.getElementType();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void isCollection() throws java.lang.Exception {
            super.isCollection();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void isAssociation() throws java.lang.Exception {
            super.isAssociation();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getPersistentAttributeType() throws java.lang.Exception {
            super.getPersistentAttributeType();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getName() throws java.lang.Exception {
            super.getName();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getJavaType() throws java.lang.Exception {
            super.getJavaType();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getJavaMember() throws java.lang.Exception {
            super.getJavaMember();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getBindableType() throws java.lang.Exception {
            super.getBindableType();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getBindableJavaType() throws java.lang.Exception {
            super.getBindableJavaType();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaringType() throws java.lang.Exception {
            super.getDeclaringType();
        }


}