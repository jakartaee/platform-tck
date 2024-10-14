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
import com.sun.ts.lib.harness.Status;


@ExtendWith(ArquillianExtension.class)
@Tag("persistence")
@Tag("platform")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientAppmanagednotxTest extends ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Client {
    static final String VEHICLE_ARCHIVE = "jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle";

    public static void main(String[] args) {
      ClientAppmanagednotxTest theTests = new ClientAppmanagednotxTest();
      Status s = theTests.run(args, System.out, System.err);
      s.exit();
    }

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
            JavaArchive jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_managedtype_vehicles_client.jar");
            // The class files
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Client.class,
            ClientAppmanagednotxTest.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml");
            if(resURL != null) {
              jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client, Client.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb.jar");
            // The class files
            jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleIF.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ee.jakarta.tck.persistence.common.PMClientBase.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                ee.jakarta.tck.persistence.core.metamodelapi.managedtype.Client.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
                com.sun.ts.tests.common.vehicle.appmanagedNoTx.AppManagedNoTxVehicleBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_client.xml");
            if(ejbResURL1 != null) {
              jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client.class.getResource("/com/sun/ts/tests/common/vehicle/appmanagedNoTx/appmanagedNoTx_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb, Client.class, ejbResURL1);


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
            jpa_core_metamodelapi_managedtype_vehicles_ear.addAsModule(jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_ejb);
            jpa_core_metamodelapi_managedtype_vehicles_ear.addAsModule(jpa_core_metamodelapi_managedtype_appmanagedNoTx_vehicle_client);

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
        @TargetVehicle("appmanagedNoTx")
        public void managedtype() throws java.lang.Exception {
            super.managedtype();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getAttributes() throws java.lang.Exception {
            super.getAttributes();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredAttributes() throws java.lang.Exception {
            super.getDeclaredAttributes();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getSingularAttributeStringClassTest() throws java.lang.Exception {
            super.getSingularAttributeStringClassTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getSingularAttributeStringClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getSingularAttributeStringClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getSingularAttributeStringTest() throws java.lang.Exception {
            super.getSingularAttributeStringTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getSingularAttributeStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getSingularAttributeStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredSingularAttributeStringClassTest() throws java.lang.Exception {
            super.getDeclaredSingularAttributeStringClassTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredSingularAttributeStringClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredSingularAttributeStringClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredSingularAttributeStringTest() throws java.lang.Exception {
            super.getDeclaredSingularAttributeStringTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredSingularAttributeStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredSingularAttributeStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredSingularAttributes() throws java.lang.Exception {
            super.getDeclaredSingularAttributes();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getSingularAttributes() throws java.lang.Exception {
            super.getSingularAttributes();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getCollectionStringTest() throws java.lang.Exception {
            super.getCollectionStringTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getCollectionStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getCollectionStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getCollectionStringClassTest() throws java.lang.Exception {
            super.getCollectionStringClassTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getCollectionStringClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getCollectionStringClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getSetStringClassTest() throws java.lang.Exception {
            super.getSetStringClassTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getSetStringClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getSetStringClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getSetStringTest() throws java.lang.Exception {
            super.getSetStringTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getSetStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getSetStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getListStringClassTest() throws java.lang.Exception {
            super.getListStringClassTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getListStringClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getListStringClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getListStringTest() throws java.lang.Exception {
            super.getListStringTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getListStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getListStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getMapStringClassTest() throws java.lang.Exception {
            super.getMapStringClassTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getMapStringClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getMapStringClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getMapStringTest() throws java.lang.Exception {
            super.getMapStringTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getMapStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getMapStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredCollectionStringClassTest() throws java.lang.Exception {
            super.getDeclaredCollectionStringClassTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredCollectionStringClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredCollectionStringClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredCollectionStringTest() throws java.lang.Exception {
            super.getDeclaredCollectionStringTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredCollectionStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredCollectionStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredSetStringClassTest() throws java.lang.Exception {
            super.getDeclaredSetStringClassTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredSetStringClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredSetStringClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredSetStringTest() throws java.lang.Exception {
            super.getDeclaredSetStringTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredSetStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredSetStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredListStringClassTest() throws java.lang.Exception {
            super.getDeclaredListStringClassTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredListStringClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredListStringClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredListStringTest() throws java.lang.Exception {
            super.getDeclaredListStringTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredListStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredListStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredMapStringClassClassTest() throws java.lang.Exception {
            super.getDeclaredMapStringClassClassTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredMapStringClassClassIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredMapStringClassClassIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredMapStringTest() throws java.lang.Exception {
            super.getDeclaredMapStringTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredMapStringIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredMapStringIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getPluralAttributes() throws java.lang.Exception {
            super.getPluralAttributes();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredPluralAttributes() throws java.lang.Exception {
            super.getDeclaredPluralAttributes();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getAttribute() throws java.lang.Exception {
            super.getAttribute();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getAttributeIllegalArgumentException() throws java.lang.Exception {
            super.getAttributeIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredAttribute() throws java.lang.Exception {
            super.getDeclaredAttribute();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getDeclaredAttributeIllegalArgumentExceptionTest() throws java.lang.Exception {
            super.getDeclaredAttributeIllegalArgumentExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("appmanagedNoTx")
        public void getPersistenceType() throws java.lang.Exception {
            super.getPersistenceType();
        }

}
