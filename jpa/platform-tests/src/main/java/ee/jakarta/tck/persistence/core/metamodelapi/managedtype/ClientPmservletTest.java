package ee.jakarta.tck.persistence.core.metamodelapi.managedtype;

import ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Client;
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
public class ClientPmservletTest extends ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Client {
    static final String VEHICLE_ARCHIVE = "jpa_core_metamodelapi_managedtype_pmservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_metamodelapi_managedtype: META-INF/persistence.xml
        jpa_core_metamodelapi_managedtype_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_metamodelapi_managedtype_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_metamodelapi_managedtype_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_metamodelapi_managedtype_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_metamodelapi_managedtype_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_metamodelapi_managedtype_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_metamodelapi_managedtype_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_metamodelapi_managedtype_vehicles: 

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
            WebArchive jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web.war");
            // The class files
            jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.tests.common.vehicle.pmservlet.PMServletVehicle.class,
            ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Client.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/core/metamodelapi/managedtype/jpa_core_metamodelapi_managedtype.jar");
            if(warResURL != null) {
              jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_metamodelapi_managedtype.jar");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/pmservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web, Client.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_metamodelapi_managedtype = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_managedtype.jar");
            // The class files
            jpa_core_metamodelapi_managedtype.addClasses(
                ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Department.class,
                ee.jakarta.tck.persistence.core.metamodelapi.managedtype.A.class,
                ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Project.class,
                ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Order.class,
                ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Address.class,
                ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Employee.class,
                ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMProject.class,
                ee.jakarta.tck.persistence.core.metamodelapi.managedtype.B.class,
                ee.jakarta.tck.persistence.core.metamodelapi.managedtype.ZipCode.class,
                ee.jakarta.tck.persistence.core.metamodelapi.managedtype.BiDirMX1Person.class,
                ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Uni1XMPerson.class
            );
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_metamodelapi_managedtype.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_metamodelapi_managedtype.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_metamodelapi_managedtype.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_metamodelapi_managedtype.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_metamodelapi_managedtype, Client.class, parURL);
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_metamodelapi_managedtype.addAsManifestResource(parURL, "orm.xml");
            }

        // Ear
            EnterpriseArchive jpa_core_metamodelapi_managedtype_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_core_metamodelapi_managedtype_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_core_metamodelapi_managedtype_vehicles_ear.addAsModule(jpa_core_metamodelapi_managedtype_pmservlet_vehicle_web);

            jpa_core_metamodelapi_managedtype_vehicles_ear.addAsLibrary(jpa_core_metamodelapi_managedtype);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_core_metamodelapi_managedtype_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_core_metamodelapi_managedtype_vehicles_ear, Client.class, earResURL);
        return jpa_core_metamodelapi_managedtype_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void managedtype() throws java.lang.Exception {
            super.managedtype();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getAttributes() throws java.lang.Exception {
            super.getAttributes();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredAttributes() throws java.lang.Exception {
            super.getDeclaredAttributes();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getSingularAttributeStringClassTest() throws java.lang.Exception {
            super.getSingularAttributeStringClassTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getSingularAttributeStringClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getSingularAttributeStringClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getSingularAttributeStringTest() throws java.lang.Exception {
            super.getSingularAttributeStringTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getSingularAttributeStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getSingularAttributeStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredSingularAttributeStringClassTest() throws java.lang.Exception {
            super.getDeclaredSingularAttributeStringClassTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredSingularAttributeStringClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredSingularAttributeStringClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredSingularAttributeStringTest() throws java.lang.Exception {
            super.getDeclaredSingularAttributeStringTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredSingularAttributeStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredSingularAttributeStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredSingularAttributes() throws java.lang.Exception {
            super.getDeclaredSingularAttributes();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getSingularAttributes() throws java.lang.Exception {
            super.getSingularAttributes();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getCollectionStringTest() throws java.lang.Exception {
            super.getCollectionStringTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getCollectionStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getCollectionStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getCollectionStringClassTest() throws java.lang.Exception {
            super.getCollectionStringClassTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getCollectionStringClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getCollectionStringClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getSetStringClassTest() throws java.lang.Exception {
            super.getSetStringClassTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getSetStringClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getSetStringClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getSetStringTest() throws java.lang.Exception {
            super.getSetStringTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getSetStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getSetStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getListStringClassTest() throws java.lang.Exception {
            super.getListStringClassTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getListStringClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getListStringClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getListStringTest() throws java.lang.Exception {
            super.getListStringTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getListStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getListStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getMapStringClassTest() throws java.lang.Exception {
            super.getMapStringClassTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getMapStringClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getMapStringClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getMapStringTest() throws java.lang.Exception {
            super.getMapStringTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getMapStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getMapStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredCollectionStringClassTest() throws java.lang.Exception {
            super.getDeclaredCollectionStringClassTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredCollectionStringClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredCollectionStringClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredCollectionStringTest() throws java.lang.Exception {
            super.getDeclaredCollectionStringTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredCollectionStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredCollectionStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredSetStringClassTest() throws java.lang.Exception {
            super.getDeclaredSetStringClassTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredSetStringClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredSetStringClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredSetStringTest() throws java.lang.Exception {
            super.getDeclaredSetStringTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredSetStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredSetStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredListStringClassTest() throws java.lang.Exception {
            super.getDeclaredListStringClassTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredListStringClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredListStringClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredListStringTest() throws java.lang.Exception {
            super.getDeclaredListStringTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredListStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredListStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredMapStringClassClassTest() throws java.lang.Exception {
            super.getDeclaredMapStringClassClassTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredMapStringClassClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredMapStringClassClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredMapStringTest() throws java.lang.Exception {
            super.getDeclaredMapStringTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredMapStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredMapStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getPluralAttributes() throws java.lang.Exception {
            super.getPluralAttributes();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredPluralAttributes() throws java.lang.Exception {
            super.getDeclaredPluralAttributes();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getAttribute() throws java.lang.Exception {
            super.getAttribute();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getAttributeIllegalArgumentException() throws java.lang.Exception {
            super.getAttributeIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredAttribute() throws java.lang.Exception {
            super.getDeclaredAttribute();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getDeclaredAttributeIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredAttributeIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void getPersistenceType() throws java.lang.Exception {
            super.getPersistenceType();
        }


}