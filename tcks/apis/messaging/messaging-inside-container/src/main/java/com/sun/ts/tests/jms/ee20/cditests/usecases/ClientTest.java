package com.sun.ts.tests.jms.ee20.cditests.usecases;

import com.sun.ts.tests.jms.ee20.cditests.usecases.Client;
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
@Tag("jms")
@Tag("platform")
@Tag("jms_web")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.jms.ee20.cditests.usecases.Client {
    /**
        EE10 Deployment Descriptors:
        cditestsusecases: META-INF/application.xml
        cditestsusecases_client: 
        cditestsusecases_ejb: META-INF/beans.xml,META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/jms/ee20/cditests/usecases/cditestsusecases_ejb.xml
        /com/sun/ts/tests/jms/ee20/cditests/usecases/cditestsusecases_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "cditestsusecases", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive cditestsusecases_client = ShrinkWrap.create(JavaArchive.class, "cditestsusecases_client.jar");
            // The class files
            cditestsusecases_client.addClasses(
            com.sun.ts.tests.jms.ee20.cditests.usecases.CMBean1IF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.jms.ee20.cditests.usecases.Client.class,
            com.sun.ts.tests.jms.ee20.cditests.usecases.BMBean1IF.class,
            com.sun.ts.tests.jms.ee20.cditests.usecases.BMBean2IF.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.jms.ee20.cditests.usecases.CMBean2IF.class
            );
            // The application-client.xml descriptor
            // URL resURL = Client.class.getResource("");
            // if(resURL != null) {
            //   cditestsusecases_client.addAsManifestResource(resURL, "application-client.xml");
            // }
            // // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            // resURL = Client.class.getResource("/.jar.sun-application-client.xml");
            // if(resURL != null) {
            //   cditestsusecases_client.addAsManifestResource(resURL, "application-client.xml");
            // }
            URL resURL = null;
            cditestsusecases_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(cditestsusecases_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive cditestsusecases_ejb = ShrinkWrap.create(JavaArchive.class, "cditestsusecases_ejb.jar");
            // The class files
            cditestsusecases_ejb.addClasses(
                com.sun.ts.tests.jms.ee20.cditests.usecases.BMBean1.class,
                com.sun.ts.tests.jms.ee20.cditests.usecases.CMBean1IF.class,
                com.sun.ts.tests.jms.ee20.cditests.usecases.CMBean2.class,
                com.sun.ts.tests.jms.ee20.cditests.usecases.BMBean1IF.class,
                com.sun.ts.tests.jms.ee20.cditests.usecases.BMBean2.class,
                com.sun.ts.tests.jms.ee20.cditests.usecases.BMBean2IF.class,
                com.sun.ts.tests.jms.ee20.cditests.usecases.CMBean1.class,
                com.sun.ts.tests.jms.ee20.cditests.usecases.CMBean2IF.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("cditestsusecases_ejb.xml");
            if(ejbResURL != null) {
              cditestsusecases_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("cditestsusecases_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              cditestsusecases_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(cditestsusecases_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive cditestsusecases_ear = ShrinkWrap.create(EnterpriseArchive.class, "cditestsusecases.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            cditestsusecases_ear.addAsModule(cditestsusecases_ejb);
            cditestsusecases_ear.addAsModule(cditestsusecases_client);



            // The application.xml descriptor
            URL earResURL = null;
            earResURL = Client.class.getResource("/com/sun/ts/tests/jms/ee20/cditests/usecases/application.xml");
            if(earResURL != null) {
              cditestsusecases_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // // The sun-application.xml descriptor
            // earResURL = Client.class.getResource("application.ear.sun-application.xml");
            // if(earResURL != null) {
            //   cditestsusecases_ear.addAsManifestResource(earResURL, "sun-application.xml");
            // }
            // Call the archive processor
            archiveProcessor.processEarArchive(cditestsusecases_ear, Client.class, earResURL);
        return cditestsusecases_ear;
        }

        @Test
        @Override
        public void beanUseCaseA() throws java.lang.Exception {
            super.beanUseCaseA();
        }

        @Test
        @Override
        public void beanUseCaseB() throws java.lang.Exception {
            super.beanUseCaseB();
        }

        @Test
        @Override
        public void beanUseCaseC() throws java.lang.Exception {
            super.beanUseCaseC();
        }

        @Test
        @Override
        public void beanUseCaseD() throws java.lang.Exception {
            super.beanUseCaseD();
        }

        @Test
        @Override
        public void beanUseCaseE() throws java.lang.Exception {
            super.beanUseCaseE();
        }

        @Test
        @Override
        public void beanUseCaseF() throws java.lang.Exception {
            super.beanUseCaseF();
        }

        @Test
        @Override
        public void beanUseCaseG() throws java.lang.Exception {
            super.beanUseCaseG();
        }

        @Test
        @Override
        public void beanUseCaseH() throws java.lang.Exception {
            super.beanUseCaseH();
        }

        @Test
        @Override
        public void beanUseCaseJ() throws java.lang.Exception {
            super.beanUseCaseJ();
        }

        @Test
        @Override
        public void beanUseCaseK() throws java.lang.Exception {
            super.beanUseCaseK();
        }


}