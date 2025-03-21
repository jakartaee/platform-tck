package ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor;

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
public class ClientTest extends ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.Client {
    /**
        EE10 Deployment Descriptors:
        jpa_ee_packaging_ejb_descriptor: 
        jpa_ee_packaging_ejb_descriptor_client: META-INF/application-client.xml
        jpa_ee_packaging_ejb_descriptor_ejb: META-INF/persistence.xml,META-INF/orm.xml,myMappingFile.xml,myMappingFile2.xml,META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jpa/ee/packaging/ejb/descriptor/jpa_ee_packaging_ejb_descriptor_client.xml
        Ejb:

        /com/sun/ts/tests/jpa/ee/packaging/ejb/descriptor/jpa_ee_packaging_ejb_descriptor_ejb.jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "jpa_ee_packaging_ejb_descriptor", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jpa_ee_packaging_ejb_descriptor_client = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_ejb_descriptor_client.jar");
            // The class files
            jpa_ee_packaging_ejb_descriptor_client.addClasses(
                    IClient.class,
                    IClientProxy.class,
                    TestAppClient.class
            ).addClasses(AppClient.getAppClasses())
            ;
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/ejb/descriptor/jpa_ee_packaging_ejb_descriptor_client.xml");
            jpa_ee_packaging_ejb_descriptor_client.addAsManifestResource(resURL, "application-client.xml");
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = null;
            jpa_ee_packaging_ejb_descriptor_client.addAsManifestResource(new StringAsset("Main-Class: " + TestAppClient.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jpa_ee_packaging_ejb_descriptor_client, Client.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_ee_packaging_ejb_descriptor_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_ee_packaging_ejb_descriptor_ejb.jar");
            // The class files
            jpa_ee_packaging_ejb_descriptor_ejb.addClasses(
                ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.C.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.A.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.Stateless3IF.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.Stateful3Bean.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.B.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.Stateful3IF.class,
                ee.jakarta.tck.persistence.ee.packaging.ejb.descriptor.Stateless3Bean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/ejb/descriptor/jpa_ee_packaging_ejb_descriptor_ejb.jar.xml");
            jpa_ee_packaging_ejb_descriptor_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            // META-INF/persistence.xml
            ejbResURL1 = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/ejb/descriptor/persistence.xml");
            jpa_ee_packaging_ejb_descriptor_ejb.addAsManifestResource(ejbResURL1, "persistence.xml");
            // META-INF/orm.xml
            ejbResURL1 = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/ejb/descriptor/orm.xml");
            jpa_ee_packaging_ejb_descriptor_ejb.addAsManifestResource(ejbResURL1, "orm.xml");
            // myMappingFile.xml
            ejbResURL1 = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/ejb/descriptor/myMappingFile.xml");
            jpa_ee_packaging_ejb_descriptor_ejb.addAsResource(ejbResURL1, "/myMappingFile.xml");
            // myMappingFile2.xml
            ejbResURL1 = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/ejb/descriptor/myMappingFile2.xml");
            jpa_ee_packaging_ejb_descriptor_ejb.addAsResource(ejbResURL1, "/myMappingFile2.xml");

            // The sun-ejb-jar.xml file
            ejbResURL1 = Client.class.getResource("/ee/jakarta/tck/persistence/ee/packaging/ejb/descriptor/jpa_ee_packaging_ejb_descriptor_ejb.jar.sun-ejb-jar.xml");
            jpa_ee_packaging_ejb_descriptor_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_ee_packaging_ejb_descriptor_ejb, Client.class, ejbResURL1);


            // non-vehicle appclientproxy invoker war
            WebArchive appclientproxy = ShrinkWrap.create(WebArchive.class, "appclientproxy.war");
            appclientproxy.addClasses(Client.class, ClientServletTarget.class, ServletNoVehicle.class);
            appclientproxy.addAsWebInfResource(new StringAsset(""), "beans.xml");

            // Ear
            EnterpriseArchive jpa_ee_packaging_ejb_descriptor_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_packaging_ejb_descriptor.ear");

            // The component jars built by the package target
            jpa_ee_packaging_ejb_descriptor_ear.addAsModule(jpa_ee_packaging_ejb_descriptor_ejb);
            jpa_ee_packaging_ejb_descriptor_ear.addAsModule(jpa_ee_packaging_ejb_descriptor_client);
            jpa_ee_packaging_ejb_descriptor_ear.addAsModule(appclientproxy);

            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_ee_packaging_ejb_descriptor_ear, Client.class, earResURL);
        return jpa_ee_packaging_ejb_descriptor_ear;
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


}