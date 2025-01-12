package ee.jakarta.tck.persistence.jpa.ee.packaging.jar;

import ee.jakarta.tck.persistence.jpa.ee.packaging.jar.Client;
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
public class ClientPmservletTest extends ee.jakarta.tck.persistence.jpa.ee.packaging.jar.Client {
    static final String VEHICLE_ARCHIVE = "jpa_ee_packaging_jar_pmservlet_vehicle";

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
        War:

        /com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml

         starksm@Scotts-Mac-Studio jar % jar -tf jpa_ee_packaging_jar_pmservlet_vehicle_web.war
         WEB-INF/classes/com/sun/ts/lib/harness/EETest$Fault.class
         WEB-INF/classes/com/sun/ts/lib/harness/EETest$SetupException.class
         WEB-INF/classes/com/sun/ts/lib/harness/EETest.class
         WEB-INF/classes/com/sun/ts/lib/harness/ServiceEETest.class
         WEB-INF/classes/com/sun/ts/tests/common/vehicle/VehicleClient.class
         WEB-INF/classes/com/sun/ts/tests/common/vehicle/VehicleRunnable.class
         WEB-INF/classes/com/sun/ts/tests/common/vehicle/VehicleRunnerFactory.class
         WEB-INF/classes/com/sun/ts/tests/common/vehicle/ejb3share/EJB3ShareBaseBean.class
         WEB-INF/classes/com/sun/ts/tests/common/vehicle/ejb3share/EJB3ShareIF.class
         WEB-INF/classes/com/sun/ts/tests/common/vehicle/ejb3share/EntityTransactionWrapper.class
         WEB-INF/classes/com/sun/ts/tests/common/vehicle/ejb3share/NoopTransactionWrapper.class
         WEB-INF/classes/com/sun/ts/tests/common/vehicle/ejb3share/UseEntityManager.class
         WEB-INF/classes/com/sun/ts/tests/common/vehicle/ejb3share/UseEntityManagerFactory.class
         WEB-INF/classes/com/sun/ts/tests/common/vehicle/ejb3share/UserTransactionWrapper.class
         WEB-INF/classes/com/sun/ts/tests/common/vehicle/pmservlet/PMServletVehicle.class
         WEB-INF/classes/com/sun/ts/tests/common/vehicle/servlet/ServletVehicle.class
         WEB-INF/classes/com/sun/ts/tests/jpa/common/PMClientBase.class
         WEB-INF/classes/com/sun/ts/tests/jpa/ee/packaging/
         WEB-INF/classes/com/sun/ts/tests/jpa/ee/packaging/jar/
         WEB-INF/classes/com/sun/ts/tests/jpa/ee/packaging/jar/Client.class
         WEB-INF/lib/jpa_ee_packaging_jar.jar
         WEB-INF/lib/jpa_ee_packaging_jar1.jar
         WEB-INF/lib/jpa_ee_packaging_jar2.jar
         WEB-INF/web.xml

         */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jpa_ee_packaging_jar_pmservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "jpa_ee_packaging_jar_pmservlet_vehicle_web.war");
            // The class files
            jpa_ee_packaging_jar_pmservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejb3share.EJB3ShareBaseBean.class,
            ee.jakarta.tck.persistence.jpa.ee.packaging.jar.Client.class,
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
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.common.vehicle.ejb3share.NoopTransactionWrapper.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            jpa_ee_packaging_jar_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");

            // Any libraries added to the war
            URL libURL;
            JavaArchive jpa_ee_packaging_jar2_lib = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_jar2.jar");
            // The class files
            jpa_ee_packaging_jar2_lib.addClasses(
                ee.jakarta.tck.persistence.jpa.ee.packaging.jar.C.class
            );
            jpa_ee_packaging_jar_pmservlet_vehicle_web.addAsLibrary(jpa_ee_packaging_jar2_lib);

            JavaArchive jpa_ee_packaging_jar1_lib = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_jar1.jar");
            // The class files
            jpa_ee_packaging_jar1_lib.addClasses(
                ee.jakarta.tck.persistence.jpa.ee.packaging.jar.B.class
            );
            jpa_ee_packaging_jar_pmservlet_vehicle_web.addAsLibrary(jpa_ee_packaging_jar1_lib);

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_ee_packaging_jar_pmservlet_vehicle_web, Client.class, warResURL);

           // PAR
          JavaArchive jpa_ee_packaging_jar = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_jar.jar");
          jpa_ee_packaging_jar.addClass(A.class);
          System.out.println("Client.cs: "+Client.class.getProtectionDomain().getCodeSource());
          URL parURL = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/jar/persistence.xml");
          System.out.println("persistence.xml: "+parURL);
          jpa_ee_packaging_jar.addAsManifestResource(parURL,"persistence.xml");
          parURL = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/jar/orm.xml");
          System.out.println("orm.xml: "+parURL);
          jpa_ee_packaging_jar.addAsManifestResource(parURL, "orm.xml");
          parURL = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/jar/myMappingFile.xml");
          jpa_ee_packaging_jar.addAsResource(parURL, "myMappingFile.xml");
          parURL = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/jar/myMappingFile2.xml");
          jpa_ee_packaging_jar.addAsResource(parURL, "myMappingFile2.xml");
          jpa_ee_packaging_jar_pmservlet_vehicle_web.addAsLibrary(jpa_ee_packaging_jar);

        return jpa_ee_packaging_jar_pmservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void JarFileElementsTest() throws java.lang.Exception {
            super.JarFileElementsTest();
        }


}
