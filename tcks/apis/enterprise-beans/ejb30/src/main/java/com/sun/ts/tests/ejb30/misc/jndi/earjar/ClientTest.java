package com.sun.ts.tests.ejb30.misc.jndi.earjar;

import com.sun.ts.tests.ejb30.misc.jndi.earjar.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.misc.jndi.earjar.Client {
        @Deployment(name = "ejb3_common_helloejbjar_standalone_component", order = 1, testable = false)
        public static JavaArchive createCommonDeployment() {
            JavaArchive ejb3_common_helloejbjar_standalone_component_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_common_helloejbjar_standalone_component_ejb.jar");
            // The class files
            ejb3_common_helloejbjar_standalone_component_ejb.addClasses(
                    com.sun.ts.tests.ejb30.common.helloejbjar.HelloCommonIF.class,
                    com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF.class,
                    com.sun.ts.tests.ejb30.common.helloejbjar.HelloLocalIF.class,
                    com.sun.ts.tests.ejb30.common.helloejbjar.HelloBeanBase.class,
                    com.sun.ts.tests.ejb30.common.helloejbjar.HelloBean.class,
                    com.sun.ts.tests.ejb30.common.helper.TLogger.class
            );
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/common/helloejbjar/ejb3_common_helloejbjar_standalone_component_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
                ejb3_common_helloejbjar_standalone_component_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }

            return ejb3_common_helloejbjar_standalone_component_ejb;
        }

    /**
        EE10 Deployment Descriptors:
        misc_jndi_earjar: 
        misc_jndi_earjar_client: META-INF/application-client.xml,jar.sun-application-client.xml
        misc_jndi_earjar_ejb: 

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/misc/jndi/earjar/misc_jndi_earjar_client.xml
        /com/sun/ts/tests/ejb30/misc/jndi/earjar/misc_jndi_earjar_client.jar.sun-application-client.xml
        Ejb:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "misc_jndi_earjar", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive misc_jndi_earjar_client = ShrinkWrap.create(JavaArchive.class, "misc_jndi_earjar_client.jar");
            // The class files
            misc_jndi_earjar_client.addClasses(
            com.sun.ts.tests.ejb30.misc.jndi.earjar.Client.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/jndi/earjar/misc_jndi_earjar_client.xml");
            if(resURL != null) {
              misc_jndi_earjar_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/jndi/earjar/misc_jndi_earjar_client.jar.sun-application-client.xml");
            if(resURL != null) {
              misc_jndi_earjar_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            misc_jndi_earjar_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(misc_jndi_earjar_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive misc_jndi_earjar_ejb = ShrinkWrap.create(JavaArchive.class, "misc_jndi_earjar_ejb.jar");
            // The class files
            misc_jndi_earjar_ejb.addClasses(
                com.sun.ts.tests.ejb30.lite.basic.stateless.TwoInterfacesBasicBean.class,
                com.sun.ts.tests.ejb30.misc.jndi.earjar.TestBean.class,
                com.sun.ts.tests.ejb30.lite.basic.common.BasicBeanBase.class,
                com.sun.ts.tests.ejb30.lite.basic.stateless.BasicBean.class,
                com.sun.ts.tests.ejb30.lite.basic.stateless.OneInterfaceBasicBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/jndi/earjar/misc_jndi_earjar_ejb.xml");
            if(ejbResURL != null) {
              misc_jndi_earjar_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/jndi/earjar/misc_jndi_earjar_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              misc_jndi_earjar_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(misc_jndi_earjar_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive misc_jndi_earjar_ear = ShrinkWrap.create(EnterpriseArchive.class, "misc_jndi_earjar.ear");

            // Any libraries added to the ear
                URL libURL;
                JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");
                    // The class files
                    shared_lib.addClasses(
                        com.sun.ts.tests.ejb30.lite.basic.common.Basic1IF.class,
                        com.sun.ts.tests.ejb30.misc.jndi.earjar.TestIF.class,
                        com.sun.ts.tests.ejb30.common.helper.Helper.class,
                        com.sun.ts.tests.ejb30.lite.basic.common.GlobalJNDITest.class,
                        com.sun.ts.tests.ejb30.common.helloejbjar.HelloCommonIF.class,
                        com.sun.ts.tests.ejb30.common.helloejbjar.HelloLocalIF.class,
                        com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                        com.sun.ts.tests.ejb30.lite.basic.common.BasicBeanHelper.class,
                        com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF.class,
                        com.sun.ts.tests.ejb30.lite.basic.common.Basic2IF.class
                    );


                misc_jndi_earjar_ear.addAsLibrary(shared_lib);


            // The component jars built by the package target
            misc_jndi_earjar_ear.addAsModule(misc_jndi_earjar_ejb);
            misc_jndi_earjar_ear.addAsModule(misc_jndi_earjar_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/jndi/earjar/application.xml");
            if(earResURL != null) {
              misc_jndi_earjar_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/jndi/earjar/application.ear.sun-application.xml");
            if(earResURL != null) {
              misc_jndi_earjar_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(misc_jndi_earjar_ear, Client.class, earResURL);
        return misc_jndi_earjar_ear;
        }

        @Test
        @Override
        @OperateOnDeployment("misc_jndi_earjar")
        public void beanValidator() {
            super.beanValidator();
        }

        @Test
        @Override
        @OperateOnDeployment("misc_jndi_earjar")
        public void appNameModuleName() {
            super.appNameModuleName();
        }

        @Test
        @Override
        @OperateOnDeployment("misc_jndi_earjar")
        public void appNameModuleNameFromEJB() {
            super.appNameModuleNameFromEJB();
        }

        @Test
        @Override
        @OperateOnDeployment("misc_jndi_earjar")
        public void globalJNDIHelloEJB() {
            super.globalJNDIHelloEJB();
        }

        @Test
        @Override
        @OperateOnDeployment("misc_jndi_earjar")
        public void ejbRefHello() {
            super.ejbRefHello();
        }

        @Test
        @Override
        @OperateOnDeployment("misc_jndi_earjar")
        public void globalJNDIHelloEJB2() {
            super.globalJNDIHelloEJB2();
        }

        @Test
        @Override
        @OperateOnDeployment("misc_jndi_earjar")
        public void globalJNDI() {
            super.globalJNDI();
        }

        @Test
        @Override
        @OperateOnDeployment("misc_jndi_earjar")
        public void appJNDI() {
            super.appJNDI();
        }

        @Test
        @Override
        @OperateOnDeployment("misc_jndi_earjar")
        public void globalJNDI2() {
            super.globalJNDI2();
        }

        @Test
        @Override
        @OperateOnDeployment("misc_jndi_earjar")
        public void appJNDI2() {
            super.appJNDI2();
        }

        @Test
        @Override
        @OperateOnDeployment("misc_jndi_earjar")
        public void moduleJNDI2() {
            super.moduleJNDI2();
        }


}