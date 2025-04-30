package com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.one;

import com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.one.Client;
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
@Tag("platform")
@Tag("ejb_3x_remote_optional")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.one.Client {
    /**
        EE10 Deployment Descriptors:
        ejb3_stateless_ejblink_one: 
        ejb3_stateless_ejblink_one_client: META-INF/application-client.xml,jar.sun-application-client.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/one/ejb3_stateless_ejblink_one_client.xml
        /com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/one/ejb3_stateless_ejblink_one_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/one/one_ejb.xml
        /com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/one/one_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_stateless_ejblink_one", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_stateless_ejblink_one_client = ShrinkWrap.create(JavaArchive.class, "ejb3_stateless_ejblink_one_client.jar");
            // The class files
            ejb3_stateless_ejblink_one_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.common.ejblink.EjbLinkIF.class,
            com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.one.Client.class,
            com.sun.ts.tests.ejb30.common.ejblink.Constants.class,
            com.sun.ts.tests.ejb30.common.ejblink.ClientBase.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb30.common.ejblink.CommonIF.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/one/ejb3_stateless_ejblink_one_client.xml");
            if(resURL != null) {
              ejb3_stateless_ejblink_one_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/one/ejb3_stateless_ejblink_one_client.jar.sun-application-client.xml");
            if(resURL != null) {
              ejb3_stateless_ejblink_one_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            ejb3_stateless_ejblink_one_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_stateless_ejblink_one_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive one_ejb = ShrinkWrap.create(JavaArchive.class, "one_ejb.jar");
            // The class files
            one_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.common.ejblink.EjbLinkLocalIF.class,
                com.sun.ts.tests.ejb30.common.ejblink.EjbLinkIF.class,
                com.sun.ts.tests.ejb30.common.ejblink.Constants.class,
                com.sun.ts.tests.ejb30.common.ejblink.EjbLinkBeanBase.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.one.EjbLink1Bean.class,
                com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                com.sun.ts.tests.ejb30.common.ejblink.CommonIF.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/one/one_ejb.xml");
            if(ejbResURL != null) {
              one_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/one/one_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              one_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(one_ejb, Client.class, ejbResURL);
            // two_ejb
            JavaArchive two_ejb = ShrinkWrap.create(JavaArchive.class, "two_ejb.jar");
            two_ejb.addClasses(
                    com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.one.EjbLink2Bean.class,
                    com.sun.ts.tests.ejb30.bb.session.stateless.ejblink.one.EjbLink3Bean.class,
                    com.sun.ts.tests.ejb30.common.ejblink.Constants.class,
                    com.sun.ts.tests.ejb30.common.ejblink.EjbLinkBeanBase.class,
                    com.sun.ts.tests.ejb30.common.ejblink.EjbLinkIF.class,
                    com.sun.ts.tests.ejb30.common.ejblink.CommonIF.class,
                    com.sun.ts.tests.ejb30.common.ejblink.EjbLinkLocalIF.class,
                    com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                    com.sun.ts.tests.ejb30.common.helper.TestFailedException.class
            );
            // The ejb-jar.xml descriptor
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/one/two_ejb.xml");
            if(ejbResURL != null) {
                two_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/one/two_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
                two_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(two_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_stateless_ejblink_one_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_stateless_ejblink_one.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            ejb3_stateless_ejblink_one_ear.addAsModule(two_ejb);
            ejb3_stateless_ejblink_one_ear.addAsModule(one_ejb);
            ejb3_stateless_ejblink_one_ear.addAsModule(ejb3_stateless_ejblink_one_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/one/application.xml");
            if(earResURL != null) {
              ejb3_stateless_ejblink_one_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/ejblink/one/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_stateless_ejblink_one_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_stateless_ejblink_one_ear, Client.class, earResURL);
        return ejb3_stateless_ejblink_one_ear;
        }

        @Test
        @Override
        public void callBean1() throws java.lang.Exception {
            super.callBean1();
        }

        @Test
        @Override
        public void callBean2() throws java.lang.Exception {
            super.callBean2();
        }

        @Test
        @Override
        public void callBean1Bean2() throws java.lang.Exception {
            super.callBean1Bean2();
        }

        @Test
        @Override
        public void callBean2Bean1() throws java.lang.Exception {
            super.callBean2Bean1();
        }

        @Test
        @Override
        public void callBean2Bean3() throws java.lang.Exception {
            super.callBean2Bean3();
        }

        @Test
        @Override
        public void callBean2Bean1Local() throws java.lang.Exception {
            super.callBean2Bean1Local();
        }


}