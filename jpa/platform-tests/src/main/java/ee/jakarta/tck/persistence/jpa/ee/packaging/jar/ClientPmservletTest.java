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
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
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
            if(warResURL != null) {
              jpa_ee_packaging_jar_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_ee_packaging_jar_pmservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war
                URL libURL;
                JavaArchive jpa_ee_packaging_jar2_lib = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_jar2.jar");
                    // The class files
                    jpa_ee_packaging_jar2_lib.addClasses(
                        ee.jakarta.tck.persistence.jpa.ee.packaging.jar.C.class
                    );


                // The resources
                        libURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/packaging/jar/C.java");
                        jpa_ee_packaging_jar2_lib.addAsResource(libURL, "/com/sun/ts/tests/jpa/ee/packaging/jar/C.java");

                jpa_ee_packaging_jar_pmservlet_vehicle_web.addAsLibrary(jpa_ee_packaging_jar2_lib);
                JavaArchive jpa_ee_packaging_jar1_lib = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_jar1.jar");
                    // The class files
                    jpa_ee_packaging_jar1_lib.addClasses(
                        ee.jakarta.tck.persistence.jpa.ee.packaging.jar.B.class
                    );


                // The resources
                        libURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/packaging/jar/B.java");
                        jpa_ee_packaging_jar1_lib.addAsResource(libURL, "/com/sun/ts/tests/jpa/ee/packaging/jar/B.java");

                jpa_ee_packaging_jar_pmservlet_vehicle_web.addAsLibrary(jpa_ee_packaging_jar1_lib);
                JavaArchive jpa_ee_packaging_jar_lib = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_jar.jar");
                    // The class files
                    jpa_ee_packaging_jar_lib.addClasses(
                        ee.jakarta.tck.persistence.jpa.ee.packaging.jar.A.class
                    );


                // The resources
                        libURL = Client.class.getResource("/persistence.xml");
                        jpa_ee_packaging_jar_lib.addAsResource(libURL, "/persistence.xml");
                        libURL = Client.class.getResource("/orm.xml");
                        jpa_ee_packaging_jar_lib.addAsResource(libURL, "/orm.xml");
                        libURL = Client.class.getResource("/myMappingFile.xml");
                        jpa_ee_packaging_jar_lib.addAsResource(libURL, "/myMappingFile.xml");
                        libURL = Client.class.getResource("/myMappingFile2.xml");
                        jpa_ee_packaging_jar_lib.addAsResource(libURL, "/myMappingFile2.xml");
                        libURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/packaging/jar/A.java");
                        jpa_ee_packaging_jar_lib.addAsResource(libURL, "/com/sun/ts/tests/jpa/ee/packaging/jar/A.java");

                jpa_ee_packaging_jar_pmservlet_vehicle_web.addAsLibrary(jpa_ee_packaging_jar_lib);


            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/pmservlet/pmservlet_vehicle_web.xml");
            if(warResURL != null) {
              jpa_ee_packaging_jar_pmservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/pmservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_ee_packaging_jar_pmservlet_vehicle_web, Client.class, warResURL);


        // Ear
            EnterpriseArchive jpa_ee_packaging_jar_vehicles_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_packaging_jar_vehicles.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_ee_packaging_jar_vehicles_ear.addAsModule(jpa_ee_packaging_jar_pmservlet_vehicle_web);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_ee_packaging_jar_vehicles_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_ee_packaging_jar_vehicles_ear, Client.class, earResURL);
        return jpa_ee_packaging_jar_vehicles_ear;
        }

        @Test
        @Override
        @TargetVehicle("pmservlet")
        public void JarFileElementsTest() throws java.lang.Exception {
            super.JarFileElementsTest();
        }


}