package ee.jakarta.tck.persistence.ee.packaging.web.standalone;

import ee.jakarta.tck.persistence.ee.packaging.web.standalone.Client;
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
public class ClientTest extends ee.jakarta.tck.persistence.ee.packaging.web.standalone.Client {
    /**
        EE10 Deployment Descriptors:
        jpa_ee_packaging_web_standalone_component_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jpa/ee/packaging/web/standalone/jpa_ee_packaging_web_standalone_component_web.xml
        /com/sun/ts/tests/jpa/ee/packaging/web/standalone/jpa_ee_packaging_web_standalone_component_web.war.sun-web.xml
        /com/sun/ts/tests/jpa/ee/packaging/web/standalone/jpa_ee_packaging_web_standalone_component_web.war.sun-web.xml
        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = "jpa_ee_packaging_web_standalone_component", order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
            // the war with the correct archive name
            WebArchive jpa_ee_packaging_web_standalone_component_web = ShrinkWrap.create(WebArchive.class, "jpa_ee_packaging_web_standalone_component_web.war");
            // The class files
            jpa_ee_packaging_web_standalone_component_web.addClasses(
            ee.jakarta.tck.persistence.ee.util.Data.class,
            ee.jakarta.tck.persistence.ee.util.HttpTCKServlet.class,
            ee.jakarta.tck.persistence.ee.packaging.web.standalone.ServletTest.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("com/sun/ts/tests/jpa/ee/packaging/web/standalone/jpa_ee_packaging_web_standalone_component_web.xml");
            if(warResURL != null) {
              jpa_ee_packaging_web_standalone_component_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/packaging/web/standalone/jpa_ee_packaging_web_standalone_component_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_ee_packaging_web_standalone_component_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/packaging/web/standalone/jpa_ee_packaging_web_standalone_component_web.xml");
            if(warResURL != null) {
              jpa_ee_packaging_web_standalone_component_web.addAsWebResource(warResURL, "//jpa_ee_packaging_web_standalone_component_web.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/packaging/web/standalone/jpa_ee_packaging_web_standalone.jar");
            if(warResURL != null) {
              jpa_ee_packaging_web_standalone_component_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_ee_packaging_web_standalone.jar");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_ee_packaging_web_standalone_component_web, Client.class, warResURL);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_ee_packaging_web_standalone = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_web_standalone.jar");
            // The class files
            jpa_ee_packaging_web_standalone.addClasses(
                ee.jakarta.tck.persistence.ee.common.Account.class
            );
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_ee_packaging_web_standalone.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_ee_packaging_web_standalone.addAsResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_ee_packaging_web_standalone.addAsResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_ee_packaging_web_standalone.addAsResource(mappingURL, "myMappingFile2.xml");
            }
            // Call the archive processor
            archiveProcessor.processParArchive(jpa_ee_packaging_web_standalone, Client.class, parURL);
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_ee_packaging_web_standalone.addAsManifestResource(parURL, "orm.xml");
            }

        return jpa_ee_packaging_web_standalone_component_web;
        }

        @Test
        @Override
        public void test1() throws java.lang.Exception {
            super.test1();
        }


}