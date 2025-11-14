package ee.jakarta.tck.persistence.core.metamodelapi.identifiabletype;

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
import org.junit.jupiter.api.Disabled;
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
public class ClientPuservletTest extends ee.jakarta.tck.persistence.core.metamodelapi.identifiabletype.Client {
    static final String VEHICLE_ARCHIVE = "jpa_core_metamodelapi_identifiabletype_puservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_core_metamodelapi_identifiabletype: META-INF/persistence.xml
        jpa_core_metamodelapi_identifiabletype_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_core_metamodelapi_identifiabletype_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_metamodelapi_identifiabletype_appmanagedNoTx_vehicle_client: META-INF/application-client.xml
        jpa_core_metamodelapi_identifiabletype_appmanagedNoTx_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_metamodelapi_identifiabletype_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_metamodelapi_identifiabletype_puservlet_vehicle_web: WEB-INF/web.xml
        jpa_core_metamodelapi_identifiabletype_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_core_metamodelapi_identifiabletype_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_metamodelapi_identifiabletype_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_core_metamodelapi_identifiabletype_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_core_metamodelapi_identifiabletype_vehicles: 

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
            WebArchive jpa_core_metamodelapi_identifiabletype_puservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_core_metamodelapi_identifiabletype_puservlet_vehicle_web.war");
            // The class files
            jpa_core_metamodelapi_identifiabletype_puservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            com.sun.ts.tests.common.vehicle.puservlet.PUServletVehicle.class,
            Fault.class,
            ee.jakarta.tck.persistence.core.metamodelapi.identifiabletype.Client.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_metamodelapi_identifiabletype_puservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_core_metamodelapi_identifiabletype_puservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/core/metamodelapi/identifiabletype/jpa_core_metamodelapi_identifiabletype.jar");
            if(warResURL != null) {
              jpa_core_metamodelapi_identifiabletype_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_core_metamodelapi_identifiabletype.jar");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/puservlet/puservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_core_metamodelapi_identifiabletype_puservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/puservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_core_metamodelapi_identifiabletype_puservlet_vehicle_web, Client.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_core_metamodelapi_identifiabletype = ShrinkWrap.create(JavaArchive.class, "jpa_core_metamodelapi_identifiabletype.jar");
            // The class files
            jpa_core_metamodelapi_identifiabletype.addClasses(
                ee.jakarta.tck.persistence.core.metamodelapi.identifiabletype.B.class,
                ee.jakarta.tck.persistence.core.metamodelapi.identifiabletype.DID2EmployeeId.class,
                ee.jakarta.tck.persistence.core.metamodelapi.identifiabletype.DID2Employee.class,
                ee.jakarta.tck.persistence.core.metamodelapi.identifiabletype.Address.class,
                ee.jakarta.tck.persistence.core.metamodelapi.identifiabletype.A.class,
                ee.jakarta.tck.persistence.core.metamodelapi.identifiabletype.ZipCode.class
            );
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_core_metamodelapi_identifiabletype.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_core_metamodelapi_identifiabletype.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_core_metamodelapi_identifiabletype.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_core_metamodelapi_identifiabletype.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_core_metamodelapi_identifiabletype, Client.class, parURL);
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_core_metamodelapi_identifiabletype.addAsManifestResource(parURL, "orm.xml");
            }

            jpa_core_metamodelapi_identifiabletype_puservlet_vehicle_web.addAsLibrary(jpa_core_metamodelapi_identifiabletype);
            return jpa_core_metamodelapi_identifiabletype_puservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getId() throws java.lang.Exception {
            super.getId();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getIdIllegalArgumentException() throws java.lang.Exception {
            super.getIdIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getVersion() throws java.lang.Exception {
            super.getVersion();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getVersionIllegalArgumentException() throws java.lang.Exception {
            super.getVersionIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredId() throws java.lang.Exception {
            super.getDeclaredId();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredIdIllegalArgumentException() throws java.lang.Exception {
            super.getDeclaredIdIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredVersion() throws java.lang.Exception {
            super.getDeclaredVersion();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredVersionIllegalArgumentException() throws java.lang.Exception {
            super.getDeclaredVersionIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getSupertype() throws java.lang.Exception {
            super.getSupertype();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void hasSingleIdAttribute() throws java.lang.Exception {
            super.hasSingleIdAttribute();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void hasVersionAttribute() throws java.lang.Exception {
            super.hasVersionAttribute();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getIdClassAttributes() throws java.lang.Exception {
            super.getIdClassAttributes();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getIdClassAttributesIllegalArgumentException() throws java.lang.Exception {
            super.getIdClassAttributesIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getIdType() throws java.lang.Exception {
            super.getIdType();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getAttribute() throws java.lang.Exception {
            super.getAttribute();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getAttributeIllegalArgumentException() throws java.lang.Exception {
            super.getAttributeIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getAttributes() throws java.lang.Exception {
            super.getAttributes();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getCollectionStringClass() throws java.lang.Exception {
            super.getCollectionStringClass();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getCollectionStringClassIllegalArgumentException() throws java.lang.Exception {
            super.getCollectionStringClassIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getCollectionString() throws java.lang.Exception {
            super.getCollectionString();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getCollectionStringIllegalArgumentException() throws java.lang.Exception {
            super.getCollectionStringIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredAttribute() throws java.lang.Exception {
            super.getDeclaredAttribute();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredAttributeIllegalArgumentException() throws java.lang.Exception {
            super.getDeclaredAttributeIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredAttributes() throws java.lang.Exception {
            super.getDeclaredAttributes();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredCollectionStringClass() throws java.lang.Exception {
            super.getDeclaredCollectionStringClass();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredCollectionStringClassIllegalArgumentException() throws java.lang.Exception {
            super.getDeclaredCollectionStringClassIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredCollectionString() throws java.lang.Exception {
            super.getDeclaredCollectionString();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredCollectionStringIllegalArgumentException() throws java.lang.Exception {
            super.getDeclaredCollectionStringIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredListStringClass() throws java.lang.Exception {
            super.getDeclaredListStringClass();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredListStringClassIllegalArgumentException() throws java.lang.Exception {
            super.getDeclaredListStringClassIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredListString() throws java.lang.Exception {
            super.getDeclaredListString();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredListStringIllegalArgumentException() throws java.lang.Exception {
            super.getDeclaredListStringIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredMapStringClassClass() throws java.lang.Exception {
            super.getDeclaredMapStringClassClass();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredMapStringClassClassIllegalArgumentException() throws java.lang.Exception {
            super.getDeclaredMapStringClassClassIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredMapString() throws java.lang.Exception {
            super.getDeclaredMapString();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredMapStringIllegalArgumentException() throws java.lang.Exception {
            super.getDeclaredMapStringIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredSetStringClass() throws java.lang.Exception {
            super.getDeclaredSetStringClass();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredSetStringClassIllegalArgumentException() throws java.lang.Exception {
            super.getDeclaredSetStringClassIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredSetString() throws java.lang.Exception {
            super.getDeclaredSetString();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredSetStringIllegalArgumentException() throws java.lang.Exception {
            super.getDeclaredSetStringIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredSingularAttributeStringClass() throws java.lang.Exception {
            super.getDeclaredSingularAttributeStringClass();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredSingularAttributeStringClassIllegalArgumentException() throws java.lang.Exception {
            super.getDeclaredSingularAttributeStringClassIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredSingularAttributeString() throws java.lang.Exception {
            super.getDeclaredSingularAttributeString();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredSingularAttributeStringIllegalArgumentException() throws java.lang.Exception {
            super.getDeclaredSingularAttributeStringIllegalArgumentException();
        }

        @Disabled("https://github.com/jakartaee/platform-tck/issues/2497")
        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredSingularAttributes() throws java.lang.Exception {
            super.getDeclaredSingularAttributes();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getListStringClass() throws java.lang.Exception {
            super.getListStringClass();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getListStringClassIllegalArgumentException() throws java.lang.Exception {
            super.getListStringClassIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getListString() throws java.lang.Exception {
            super.getListString();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getListStringIllegalArgumentException() throws java.lang.Exception {
            super.getListStringIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getMapStringClassClass() throws java.lang.Exception {
            super.getMapStringClassClass();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getMapStringClassClassIllegalArgumentException() throws java.lang.Exception {
            super.getMapStringClassClassIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getMapString() throws java.lang.Exception {
            super.getMapString();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getMapStringIllegalArgumentException() throws java.lang.Exception {
            super.getMapStringIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getPluralAttributes() throws java.lang.Exception {
            super.getPluralAttributes();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getDeclaredPluralAttributes() throws java.lang.Exception {
            super.getDeclaredPluralAttributes();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getSetStringClass() throws java.lang.Exception {
            super.getSetStringClass();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getSetStringClassIllegalArgumentException() throws java.lang.Exception {
            super.getSetStringClassIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getSetString() throws java.lang.Exception {
            super.getSetString();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getSetStringIllegalArgumentException() throws java.lang.Exception {
            super.getSetStringIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getSingularAttributeStringClass() throws java.lang.Exception {
            super.getSingularAttributeStringClass();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getSingularAttributeStringClassIllegalArgumentException() throws java.lang.Exception {
            super.getSingularAttributeStringClassIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getSingularAttributeString() throws java.lang.Exception {
            super.getSingularAttributeString();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getSingularAttributeStringIllegalArgumentException() throws java.lang.Exception {
            super.getSingularAttributeStringIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("puservlet")
        public void getSingularAttributes() throws java.lang.Exception {
            super.getSingularAttributes();
        }


}