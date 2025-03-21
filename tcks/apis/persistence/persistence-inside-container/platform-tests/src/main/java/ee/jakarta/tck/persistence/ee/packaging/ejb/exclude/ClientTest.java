package ee.jakarta.tck.persistence.ee.packaging.ejb.exclude;

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
public class ClientTest extends ee.jakarta.tck.persistence.ee.packaging.ejb.exclude.Client {
    /**
        EE10 Deployment Descriptors:
        jpa_ee_packaging_ejb_exclude: 
        jpa_ee_packaging_ejb_exclude_client: 
        jpa_ee_packaging_ejb_exclude_ejb: META-INF/persistence.xml
        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "jpa_ee_packaging_ejb_exclude", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_ee_packaging_ejb_exclude_client = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_ejb_exclude_client.jar");
            // The class files
            jpa_ee_packaging_ejb_exclude_client.addClasses(
                    IClient.class,
                    IClientProxy.class,
                    TestAppClient.class
            ).addClasses(AppClient.getAppClasses())
            ;
            // The application-client.xml descriptor
            URL resURL = null;
            jpa_ee_packaging_ejb_exclude_client.addAsManifestResource(new StringAsset("Main-Class: " + TestAppClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_ee_packaging_ejb_exclude_client, Client.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_ee_packaging_ejb_exclude_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_ejb_exclude_ejb.jar");
            // The class files
            jpa_ee_packaging_ejb_exclude_ejb.addClasses(
                ee.jakarta.tck.persistence.ee.packaging.ejb.exclude.Stateful3IF.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.exclude.A.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.exclude.Stateful3Bean.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.exclude.B.class
            );
            // The ejb-jar.xml descriptor
            // The sun-ejb-jar.xml file
            URL ejbResURL1 = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/ejb/exclude/jpa_ee_packaging_ejb.jar.sun-ejb-jar.xml");
            jpa_ee_packaging_ejb_exclude_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            ejbResURL1 = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/ejb/exclude/persistence.xml");
            jpa_ee_packaging_ejb_exclude_ejb.addAsManifestResource(ejbResURL1, "persistence.xml");
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_ee_packaging_ejb_exclude_ejb, Client.class, ejbResURL1);

            // non-vehicle appclientproxy invoker war
            WebArchive appclientproxy = ShrinkWrap.create(WebArchive.class, "appclientproxy.war");
            appclientproxy.addClasses(Client.class, ClientServletTarget.class, ServletNoVehicle.class, Stateful3IF.class);
            appclientproxy.addAsWebInfResource(new StringAsset(""), "beans.xml");

        // Ear
            EnterpriseArchive jpa_ee_packaging_ejb_exclude_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_packaging_ejb_exclude.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_ee_packaging_ejb_exclude_ear.addAsModule(jpa_ee_packaging_ejb_exclude_ejb);
            jpa_ee_packaging_ejb_exclude_ear.addAsModule(jpa_ee_packaging_ejb_exclude_client);
            jpa_ee_packaging_ejb_exclude_ear.addAsModule(appclientproxy);

            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_ee_packaging_ejb_exclude_ear, Client.class, earResURL);
        return jpa_ee_packaging_ejb_exclude_ear;
        }

        @Test
        @Override
        public void test1() throws java.lang.Exception {
            super.test1();
        }

        @Test
        @Override
        public void test2() throws java.lang.Exception {
            super.test2();
        }


}