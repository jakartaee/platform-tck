package ee.jakarta.tck.persistence.ee.packaging.ejb.resource_local;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.tests.common.base.EETest;
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


/**
 * In EE10 this was an appclient that uses a remote stateless EJB as the test container. It did not use the
 * persistence test vehicles.
 *
 * TBD for EE11
 */
@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("tck-appclient")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends ee.jakarta.tck.persistence.ee.packaging.ejb.resource_local.Client {
    /**
        EE10 Deployment Descriptors:
        jpa_ee_packaging_ejb_resource_local: 
        jpa_ee_packaging_ejb_resource_local_client: META-INF/application-client.xml
        jpa_ee_packaging_ejb_resource_local_ejb: META-INF/persistence.xml,META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jpa/ee/packaging/ejb/resource_local/jpa_ee_packaging_ejb_resource_local_client.xml
        Ejb:

        /com/sun/ts/tests/jpa/ee/packaging/ejb/resource_local/jpa_ee_packaging_ejb_resource_local_ejb.jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "jpa_ee_packaging_ejb_resource_local", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_ee_packaging_ejb_resource_local_client = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_ejb_resource_local_client.jar");
            // The class files
            jpa_ee_packaging_ejb_resource_local_client.addClasses(
            Fault.class,
            ee.jakarta.tck.persistence.ee.packaging.ejb.resource_local.Stateless3IF.class,
            EETest.class,
            SetupException.class,
            Stateless3IFProxy.class,
            Client.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/ejb/resource_local/jpa_ee_packaging_ejb_resource_local_client.xml");
            jpa_ee_packaging_ejb_resource_local_client.addAsManifestResource(resURL, "application-client.xml");
            // The sun-application-client.xml is not used
            resURL = null;
            jpa_ee_packaging_ejb_resource_local_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_ee_packaging_ejb_resource_local_client, Client.class, resURL);

            // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_ee_packaging_ejb_resource_local_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_ejb_resource_local_ejb.jar");
            // The class files
            jpa_ee_packaging_ejb_resource_local_ejb.addClasses(
                ee.jakarta.tck.persistence.ee.packaging.ejb.resource_local.Stateless3Bean.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.resource_local.Stateless3IF.class,
                ee.jakarta.tck.persistence.ee.common.A.class,
                ee.jakarta.tck.persistence.ee.common.B.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/ejb/resource_local/jpa_ee_packaging_ejb_resource_local_ejb.jar.xml");
            jpa_ee_packaging_ejb_resource_local_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/ejb/resource_local/jpa_ee_packaging_ejb_resource_local_ejb.jar.sun-ejb-jar.xml");
            jpa_ee_packaging_ejb_resource_local_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_ee_packaging_ejb_resource_local_ejb, Client.class, ejbResURL1);
            ejbResURL1 = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/ejb/resource_local/persistence.xml");
            jpa_ee_packaging_ejb_resource_local_ejb.addAsManifestResource(ejbResURL1, "persistence.xml");


            // non-vehicle appclientproxy invoker war
            WebArchive appclientproxy = ShrinkWrap.create(WebArchive.class, "appclientproxy.war");
            appclientproxy.addClasses(Client.class, StatelessServletTarget.class, ServletNoVehicle.class);
            appclientproxy.addAsWebInfResource(new StringAsset(""), "beans.xml");

        // Ear
            EnterpriseArchive jpa_ee_packaging_ejb_resource_local_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_packaging_ejb_resource_local.ear");

            jpa_ee_packaging_ejb_resource_local_ear.addAsModule(jpa_ee_packaging_ejb_resource_local_ejb);
            jpa_ee_packaging_ejb_resource_local_ear.addAsModule(jpa_ee_packaging_ejb_resource_local_client);
            jpa_ee_packaging_ejb_resource_local_ear.addAsModule(appclientproxy);

            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_ee_packaging_ejb_resource_local_ear, Client.class, earResURL);
        return jpa_ee_packaging_ejb_resource_local_ear;
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

        @Test
        @Override
        public void test3() throws java.lang.Exception {
            super.test3();
        }

        @Test
        @Override
        public void test4() throws java.lang.Exception {
            super.test4();
        }

        @Test
        @Override
        public void test5() throws java.lang.Exception {
            super.test5();
        }

        @Test
        @Override
        public void test6() throws java.lang.Exception {
            super.test6();
        }

        @Test
        @Override
        public void test7() throws java.lang.Exception {
            super.test7();
        }

        @Test
        @Override
        public void test8() throws java.lang.Exception {
            super.test8();
        }

        @Test
        @Override
        public void test9() throws java.lang.Exception {
            super.test9();
        }

        @Test
        @Override
        public void test10() throws java.lang.Exception {
            super.test10();
        }

        @Test
        @Override
        public void test11() throws java.lang.Exception {
            super.test11();
        }

        @Test
        @Override
        public void test12() throws java.lang.Exception {
            super.test12();
        }

        @Test
        @Override
        public void test13() throws java.lang.Exception {
            super.test13();
        }

        @Test
        @Override
        public void test14() throws java.lang.Exception {
            super.test14();
        }

        @Test
        @Override
        public void test15() throws java.lang.Exception {
            super.test15();
        }

}