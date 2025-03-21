package ee.jakarta.tck.persistence.ee.packaging.ejb.standalone;

import com.sun.ts.tests.common.vehicle.none.proxy.AppClient;
import com.sun.ts.tests.common.vehicle.none.proxy.ServletNoVehicle;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;

import java.net.URL;



@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends ee.jakarta.tck.persistence.ee.packaging.ejb.standalone.Client {
    /**
        EE10 Deployment Descriptors:
        jpa_ee_packaging_ejb_standalone_ejb: 

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/jpa/ee/packaging/ejb/standalone/jpa_ee_packaging_ejb_standalone_component_ejb.jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "jpa_ee_packaging_ejb_standalone_ejb", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_ee_packaging_ejb_standalone_client = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_ejb_standalone_client.jar");
            // The class files
            jpa_ee_packaging_ejb_standalone_client.addClasses(
                    IClient.class,
                    IClientProxy.class,
                    TestAppClient.class
            ).addClasses(AppClient.getAppClasses())
            ;
            // The application-client.xml descriptor
            URL resURL = null;
            jpa_ee_packaging_ejb_standalone_client.addAsManifestResource(new StringAsset("Main-Class: " + TestAppClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_ee_packaging_ejb_standalone_client, Client.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_ee_packaging_ejb_standalone_component_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_ejb_standalone_component_ejb.jar");
            // The class files
            jpa_ee_packaging_ejb_standalone_component_ejb.addClasses(
                ee.jakarta.tck.persistence.ee.packaging.ejb.standalone.Stateful3Bean.class,
                ee.jakarta.tck.persistence.ee.common.A.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.standalone.Stateful3IF.class,
                ee.jakarta.tck.persistence.ee.common.B.class
            );
            URL ejbResURL1 = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/ejb/standalone/persistence.xml");
            jpa_ee_packaging_ejb_standalone_component_ejb.addAsManifestResource(ejbResURL1, "persistence.xml");
            // The ejb-jar.xml descriptor
            ejbResURL1 = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/ejb/standalone/jpa_ee_packaging_ejb_standalone_component_ejb.jar.xml");
            jpa_ee_packaging_ejb_standalone_component_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/ejb/standalone/jpa_ee_packaging_ejb_standalone_component_ejb.jar.sun-ejb-jar.xml");
            jpa_ee_packaging_ejb_standalone_component_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_ee_packaging_ejb_standalone_component_ejb, Client.class, ejbResURL1);

            // non-vehicle appclientproxy invoker war
            WebArchive appclientproxy = ShrinkWrap.create(WebArchive.class, "appclientproxy.war");
            appclientproxy.addClasses(Client.class, ClientServletTarget.class, ServletNoVehicle.class);
            appclientproxy.addAsWebInfResource(new StringAsset(""), "beans.xml");

        // Ear
            EnterpriseArchive jpa_ee_packaging_ejb_standalone_ejb_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_packaging_ejb_standalone_ejb.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_ee_packaging_ejb_standalone_ejb_ear.addAsModule(jpa_ee_packaging_ejb_standalone_component_ejb);
            jpa_ee_packaging_ejb_standalone_ejb_ear.addAsModule(jpa_ee_packaging_ejb_standalone_client);
            jpa_ee_packaging_ejb_standalone_ejb_ear.addAsModule(appclientproxy);

            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_ee_packaging_ejb_standalone_ejb_ear, Client.class, earResURL);
        return jpa_ee_packaging_ejb_standalone_ejb_ear;
        }

        @Test
        @Override
        public void test1() throws java.lang.Exception {
            super.test1();
        }


}