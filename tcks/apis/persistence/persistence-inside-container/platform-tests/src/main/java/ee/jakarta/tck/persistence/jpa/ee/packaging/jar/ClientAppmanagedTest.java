package ee.jakarta.tck.persistence.jpa.ee.packaging.jar;

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



@ExtendWith(ArquillianExtension.class)
@Tag("persistence")
@Tag("platform")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientAppmanagedTest extends ee.jakarta.tck.persistence.jpa.ee.packaging.jar.Client {
    static final String VEHICLE_ARCHIVE = "jpa_ee_packaging_jar_appmanaged_vehicle";

        /**
        EE10 Deployment Descriptors:
        jpa_ee_packaging_jar: META-INF/persistence.xml,META-INF/orm.xml,myMappingFile.xml,myMappingFile2.xml
        jpa_ee_packaging_jar_appmanaged_vehicle_client: META-INF/application-client.xml
        jpa_ee_packaging_jar_appmanaged_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_ee_packaging_jar_pmservlet_vehicle_web: WEB-INF/web.xml
        jpa_ee_packaging_jar_stateful3_vehicle_client: META-INF/application-client.xml
        jpa_ee_packaging_jar_stateful3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_ee_packaging_jar_stateless3_vehicle_client: META-INF/application-client.xml
        jpa_ee_packaging_jar_stateless3_vehicle_ejb: jar.sun-ejb-jar.xml
        jpa_ee_packaging_jar_vehicles: 
        jpa_ee_packaging_jar1: 
        jpa_ee_packaging_jar2: 

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
            JavaArchive jpa_ee_packaging_jar_appmanaged_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_jar_vehicles_client.jar");
            // The class files
            jpa_ee_packaging_jar_appmanaged_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            ee.jakarta.tck.persistence.common.PMClientBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.web.AltWebVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class,
            Client.class,
            ClientAppmanagedTest.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_client.xml");
            if(resURL != null) {
              jpa_ee_packaging_jar_appmanaged_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jpa_ee_packaging_jar_appmanaged_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            jpa_ee_packaging_jar_appmanaged_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + com.sun.ts.tests.common.vehicle.VehicleClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_ee_packaging_jar_appmanaged_vehicle_client, Client.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_ee_packaging_jar_appmanaged_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_jar_appmanaged_vehicle_ejb.jar");
            // The class files
            jpa_ee_packaging_jar_appmanaged_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
                ee.jakarta.tck.persistence.jpa.ee.packaging.jar.Client.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManager.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareIF.class,
                Fault.class,
                com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleIF.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UseEntityManagerFactory.class,
                ee.jakarta.tck.persistence.common.PMClientBase.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.appmanaged.AppManagedVehicleBean.class,
                com.sun.ts.tests.common.vehicle.ejb3share.UserTransactionWrapper.class,
                EETest.class,
                ServiceEETest.class,
                com.sun.ts.tests.common.vehicle.ejb3share.EntityTransactionWrapper.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The ejb-jar.xml descriptor
            // The sun-ejb-jar.xml file
            URL ejbResURL1 = Client.class.getResource("/com/sun/ts/tests/common/vehicle/appmanaged/appmanaged_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_ee_packaging_jar_appmanaged_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_ee_packaging_jar_appmanaged_vehicle_ejb, Client.class, ejbResURL1);

            URL libURL;
            JavaArchive jpa_ee_packaging_jar2_lib = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_jar2.jar");

            // The class files
            jpa_ee_packaging_jar2_lib.addClasses(C.class);

            JavaArchive jpa_ee_packaging_jar1_lib = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_jar1.jar");

            // The class files
            jpa_ee_packaging_jar1_lib.addClasses(B.class);

            JavaArchive jpa_ee_packaging_jar_lib = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_jar.jar");

            // The class files
            jpa_ee_packaging_jar_lib.addClasses(A.class);

            // The resources
            libURL = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/jar/persistence.xml");
            jpa_ee_packaging_jar_lib.addAsManifestResource(libURL, "persistence.xml");

            libURL = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/jar/orm.xml");
            jpa_ee_packaging_jar_lib.addAsManifestResource(libURL, "orm.xml");

            libURL = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/jar/myMappingFile.xml");
            jpa_ee_packaging_jar_lib.addAsResource(libURL, "myMappingFile.xml");

            libURL = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/jar/myMappingFile2.xml");
            jpa_ee_packaging_jar_lib.addAsResource(libURL, "myMappingFile2.xml");

        // Ear
            EnterpriseArchive jpa_ee_packaging_jar_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_packaging_jar_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_ee_packaging_jar_vehicles_ear.addAsModule(jpa_ee_packaging_jar_appmanaged_vehicle_ejb);
            jpa_ee_packaging_jar_vehicles_ear.addAsModule(jpa_ee_packaging_jar_appmanaged_vehicle_client);

            jpa_ee_packaging_jar_vehicles_ear.addAsLibrary(jpa_ee_packaging_jar2_lib);
            jpa_ee_packaging_jar_vehicles_ear.addAsLibrary(jpa_ee_packaging_jar1_lib);
            jpa_ee_packaging_jar_vehicles_ear.addAsLibrary(jpa_ee_packaging_jar_lib);


            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_ee_packaging_jar_vehicles_ear, Client.class, earResURL);
        return jpa_ee_packaging_jar_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("appmanaged")
        public void JarFileElementsTest() throws java.lang.Exception {
            super.JarFileElementsTest();
        }

}
