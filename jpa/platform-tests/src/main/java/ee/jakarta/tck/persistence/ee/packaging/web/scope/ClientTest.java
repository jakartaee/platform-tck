package ee.jakarta.tck.persistence.ee.packaging.web.scope;

import ee.jakarta.tck.persistence.ee.packaging.web.scope.Client;
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

public class ClientTest extends ee.jakarta.tck.persistence.ee.packaging.web.scope.Client {
    /**
        EE10 Deployment Descriptors:
        jpa_ee_packaging_web_scope: META-INF/application.xml
        jpa_ee_packaging_web_scope_web: WEB-INF/classes/META-INF/persistence.xml,WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jpa/ee/packaging/web/scope/jpa_ee_packaging_web_scope_web.xml
        /com/sun/ts/tests/jpa/ee/packaging/web/scope/jpa_ee_packaging_web_scope_web.war.sun-web.xml
        Ear:

        /com/sun/ts/tests/jpa/ee/packaging/web/scope/application.xml
        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = "jpa_ee_packaging_web_scope", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jpa_ee_packaging_web_scope_web = ShrinkWrap.create(WebArchive.class, "jpa_ee_packaging_web_scope_web.war");
            // The class files
            jpa_ee_packaging_web_scope_web.addClasses(
            ee.jakarta.tck.persistence.ee.util.Data.class,
            ee.jakarta.tck.persistence.ee.util.HttpTCKServlet.class,
            ee.jakarta.tck.persistence.ee.packaging.web.scope.ServletTest.class,
            ee.jakarta.tck.persistence.ee.common.Account.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("com/sun/ts/tests/jpa/ee/packaging/web/scope/jpa_ee_packaging_web_scope_web.xml");
            if(warResURL != null) {
              jpa_ee_packaging_web_scope_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/packaging/web/scope/jpa_ee_packaging_web_scope_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_ee_packaging_web_scope_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/packaging/web/scope/jpa_ee_packaging_web_scope_web.xml");
            jpa_ee_packaging_web_scope_web.addAsWebResource(warResURL, "/jpa_ee_packaging_web_scope_web.xml");

           archiveProcessor.processWebArchive(jpa_ee_packaging_web_scope_web, Client.class, warResURL);

        // Ear
            EnterpriseArchive jpa_ee_packaging_web_scope_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_packaging_web_scope.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_ee_packaging_web_scope_ear.addAsModule(jpa_ee_packaging_web_scope_web);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/packaging/web/scope/application.xml");
            if(earResURL != null) {
              jpa_ee_packaging_web_scope_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/packaging/web/scope/application.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_ee_packaging_web_scope_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            archiveProcessor.processEarArchive(jpa_ee_packaging_web_scope_ear, Client.class, earResURL);
        return jpa_ee_packaging_web_scope_ear;
        }

        @Test
        @Override
        public void test1() throws java.lang.Exception {
            super.test1();
        }


}