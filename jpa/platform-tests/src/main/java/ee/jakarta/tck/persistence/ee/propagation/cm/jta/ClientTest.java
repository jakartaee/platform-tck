package ee.jakarta.tck.persistence.ee.propagation.cm.jta;

import ee.jakarta.tck.persistence.ee.propagation.cm.jta.Client;
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
public class ClientTest extends ee.jakarta.tck.persistence.ee.propagation.cm.jta.Client {
    /**
        EE10 Deployment Descriptors:
        jpa_ee_propagation_cm_jta: META-INF/application.xml,META-INF/persistence.xml
        jpa_ee_propagation_cm_jta_ejb: jar.sun-ejb-jar.xml
        jpa_ee_propagation_cm_jta_web: WEB-INF/web.xml

        Found Descriptors:
        Ejb:

        War:

        /com/sun/ts/tests/jpa/ee/propagation/cm/jta/jpa_ee_propagation_cm_jta_web.xml
        Ear:

        /com/sun/ts/tests/jpa/ee/propagation/cm/jta/application.xml
        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = "jpa_ee_propagation_cm_jta", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive jpa_ee_propagation_cm_jta_web = ShrinkWrap.create(WebArchive.class, "jpa_ee_propagation_cm_jta_web.war");
            // The class files
            jpa_ee_propagation_cm_jta_web.addClasses(
            ee.jakarta.tck.persistence.ee.util.Data.class,
            ee.jakarta.tck.persistence.ee.util.HttpTCKServlet.class,
            ee.jakarta.tck.persistence.ee.propagation.cm.jta.Teller.class,
            ee.jakarta.tck.persistence.ee.propagation.cm.jta.ServletTest.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("com/sun/ts/tests/jpa/ee/propagation/cm/jta/jpa_ee_propagation_cm_jta_web.xml");
            if(warResURL != null) {
              jpa_ee_propagation_cm_jta_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/propagation/cm/jta/jpa_ee_propagation_cm_jta_web.war.sun-web.xml");
            if(warResURL != null) {
              jpa_ee_propagation_cm_jta_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/propagation/cm/jta/jpa_ee_propagation_cm_jta_web.xml");
            if(warResURL != null) {
              jpa_ee_propagation_cm_jta_web.addAsWebResource(warResURL, "//jpa_ee_propagation_cm_jta_web.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/propagation/cm/jta/jpa_ee_propagation_cm_jta.jar");
            if(warResURL != null) {
              jpa_ee_propagation_cm_jta_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_ee_propagation_cm_jta.jar");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/propagation/cm/jta/jpa_ee_propagation_cm_jta_ejb.jar");
            if(warResURL != null) {
              jpa_ee_propagation_cm_jta_web.addAsWebResource(warResURL, "/WEB-INF/lib/jpa_ee_propagation_cm_jta_ejb.jar");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(jpa_ee_propagation_cm_jta_web, Client.class, warResURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive jpa_ee_propagation_cm_jta_ejb = ShrinkWrap.create(JavaArchive.class, "jpa_ee_propagation_cm_jta_ejb.jar");
            // The class files
            jpa_ee_propagation_cm_jta_ejb.addClasses(
                ee.jakarta.tck.persistence.ee.propagation.cm.jta.TellerBean2.class,
                ee.jakarta.tck.persistence.ee.propagation.cm.jta.Teller.class,
                ee.jakarta.tck.persistence.ee.propagation.cm.jta.TellerBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client.class.getResource("/");
            if(ejbResURL1 != null) {
              jpa_ee_propagation_cm_jta_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client.class.getResource("/.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              jpa_ee_propagation_cm_jta_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jpa_ee_propagation_cm_jta_ejb, Client.class, ejbResURL1);


        // Par
            // the jar with the correct archive name
            JavaArchive jpa_ee_propagation_cm_jta = ShrinkWrap.create(JavaArchive.class, "jpa_ee_propagation_cm_jta.jar");
            // The class files
            jpa_ee_propagation_cm_jta.addClasses(
                ee.jakarta.tck.persistence.ee.common.A.class,
                ee.jakarta.tck.persistence.ee.common.Account.class,
                ee.jakarta.tck.persistence.ee.common.B.class
            );
            // The persistence.xml descriptor
            URL parURL = Client.class.getResource("persistence.xml");
            if(parURL != null) {
              jpa_ee_propagation_cm_jta.addAsManifestResource(parURL, "persistence.xml");
            }
            // Add the Persistence mapping-file
            URL mappingURL = Client.class.getResource("myMappingFile.xml");
            if(mappingURL != null) {
              jpa_ee_propagation_cm_jta.addAsManifestResource(mappingURL, "myMappingFile.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile1.xml");
            if(mappingURL != null) {
              jpa_ee_propagation_cm_jta.addAsManifestResource(mappingURL, "myMappingFile1.xml");
            }
            mappingURL = Client.class.getResource("myMappingFile2.xml");
            if(mappingURL != null) {
              jpa_ee_propagation_cm_jta.addAsManifestResource(mappingURL, "myMappingFile2.xml");
            }

            // Call the archive processor
            archiveProcessor.processParArchive(jpa_ee_propagation_cm_jta, Client.class, parURL);
            // The orm.xml mapping file
            parURL = Client.class.getResource("orm.xml");
            if(parURL != null) {
              jpa_ee_propagation_cm_jta.addAsManifestResource(parURL, "orm.xml");
            }
            // The mapping-file may be anywhere on the classpath

        // Ear
            EnterpriseArchive jpa_ee_propagation_cm_jta_ear = ShrinkWrap.create(EnterpriseArchive.class, "jpa_ee_propagation_cm_jta.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jpa_ee_propagation_cm_jta_ear.addAsModule(jpa_ee_propagation_cm_jta_ejb);
            jpa_ee_propagation_cm_jta_ear.addAsModule(jpa_ee_propagation_cm_jta_web);
            jpa_ee_propagation_cm_jta_ear.addAsModule(jpa_ee_propagation_cm_jta_web);

            jpa_ee_propagation_cm_jta_ear.addAsLibrary(jpa_ee_propagation_cm_jta);



            // The application.xml descriptor
            URL earResURL = null;
            earResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/propagation/cm/jta/application.xml");
            if(earResURL != null) {
              jpa_ee_propagation_cm_jta_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/jpa/ee/propagation/cm/jta/application.ear.sun-application.xml");
            if(earResURL != null) {
              jpa_ee_propagation_cm_jta_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jpa_ee_propagation_cm_jta_ear, Client.class, earResURL);
        return jpa_ee_propagation_cm_jta_ear;
        }

        @Test
        @Override
        public void test1() throws java.lang.Exception {
            super.test1();
        }

        @Test
        @Override
        public void test1a() throws java.lang.Exception {
            super.test1a();
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
        public void getTransactionIllegalStateException() throws java.lang.Exception {
            super.getTransactionIllegalStateException();
        }

        @Test
        @Override
        public void closeObjectTransactionRequiredExceptionTest() throws java.lang.Exception {
            super.closeObjectTransactionRequiredExceptionTest();
        }

        @Test
        @Override
        public void mergeObjectTransactionRequiredExceptionTest() throws java.lang.Exception {
            super.mergeObjectTransactionRequiredExceptionTest();
        }

        @Test
        @Override
        public void persistObjectTransactionRequiredExceptionTest() throws java.lang.Exception {
            super.persistObjectTransactionRequiredExceptionTest();
        }

        @Test
        @Override
        public void refreshObjectTransactionRequiredExceptionTest() throws java.lang.Exception {
            super.refreshObjectTransactionRequiredExceptionTest();
        }

        @Test
        @Override
        public void refreshObjectMapTransactionRequiredExceptionTest() throws java.lang.Exception {
            super.refreshObjectMapTransactionRequiredExceptionTest();
        }

        @Test
        @Override
        public void refreshObjectLockModeTypeTransactionRequiredExceptionTest() throws java.lang.Exception {
            super.refreshObjectLockModeTypeTransactionRequiredExceptionTest();
        }

        @Test
        @Override
        public void refreshObjectLockModeTypeMapTransactionRequiredExceptionTest() throws java.lang.Exception {
            super.refreshObjectLockModeTypeMapTransactionRequiredExceptionTest();
        }

        @Test
        @Override
        public void removeObjectTransactionRequiredExceptionTest() throws java.lang.Exception {
            super.removeObjectTransactionRequiredExceptionTest();
        }


}