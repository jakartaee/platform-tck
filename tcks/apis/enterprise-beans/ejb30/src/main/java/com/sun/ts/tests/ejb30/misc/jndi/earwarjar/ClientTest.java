package com.sun.ts.tests.ejb30.misc.jndi.earwarjar;

import com.sun.ts.tests.ejb30.misc.jndi.earwarjar.Client;
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
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.misc.jndi.earwarjar.Client {
    /**
        EE10 Deployment Descriptors:
        misc_jndi_earwarjar: 
        misc_jndi_earwarjar_ejb: 
        misc_jndi_earwarjar_web: 

        Found Descriptors:
        Ejb:

        War:

        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = "misc_jndi_earwarjar", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive misc_jndi_earwarjar_web = ShrinkWrap.create(WebArchive.class, "misc_jndi_earwarjar_web.war");
            // The class files
            misc_jndi_earwarjar_web.addClasses(
            com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet.class,
            com.sun.ts.tests.servlet.common.util.Data.class,
            com.sun.ts.tests.ejb30.misc.jndi.earwar.TestServlet.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/jndi/earwarjar/misc_jndi_earwarjar_web.xml");
            if(warResURL != null) {
              misc_jndi_earwarjar_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/jndi/earwarjar/misc_jndi_earwarjar_web.war.sun-web.xml");
            if(warResURL != null) {
              misc_jndi_earwarjar_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
           // Call the archive processor
           archiveProcessor.processWebArchive(misc_jndi_earwarjar_web, Client.class, warResURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive misc_jndi_earwarjar_ejb = ShrinkWrap.create(JavaArchive.class, "misc_jndi_earwarjar_ejb.jar");
            // The class files
            misc_jndi_earwarjar_ejb.addClasses(
                com.sun.ts.tests.ejb30.lite.basic.stateless.TwoInterfacesBasicBean.class,
                com.sun.ts.tests.ejb30.lite.basic.common.BasicBeanBase.class,
                com.sun.ts.tests.ejb30.lite.basic.stateless.BasicBean.class,
                com.sun.ts.tests.ejb30.lite.basic.stateless.OneInterfaceBasicBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/jndi/earwarjar/misc_jndi_earwarjar_ejb.xml");
            if(ejbResURL != null) {
              misc_jndi_earwarjar_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/jndi/earwarjar/misc_jndi_earwarjar_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              misc_jndi_earwarjar_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(misc_jndi_earwarjar_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive misc_jndi_earwarjar_ear = ShrinkWrap.create(EnterpriseArchive.class, "misc_jndi_earwarjar.ear");

            // Any libraries added to the ear
                URL libURL;
                JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");
                    // The class files
                    shared_lib.addClasses(
                        com.sun.ts.tests.ejb30.lite.basic.common.Basic1IF.class,
                        com.sun.ts.tests.ejb30.common.helper.Helper.class,
                        com.sun.ts.tests.ejb30.lite.basic.common.GlobalJNDITest.class,
                        com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                        com.sun.ts.tests.ejb30.lite.basic.common.BasicBeanHelper.class,
                        com.sun.ts.tests.ejb30.lite.basic.common.Basic2IF.class
                    );


                misc_jndi_earwarjar_ear.addAsLibrary(shared_lib);


            // The component jars built by the package target
            misc_jndi_earwarjar_ear.addAsModule(misc_jndi_earwarjar_ejb);
            misc_jndi_earwarjar_ear.addAsModule(misc_jndi_earwarjar_web);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/jndi/earwarjar/application.xml");
            if(earResURL != null) {
              misc_jndi_earwarjar_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/jndi/earwarjar/application.ear.sun-application.xml");
            if(earResURL != null) {
              misc_jndi_earwarjar_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(misc_jndi_earwarjar_ear, Client.class, earResURL);
        return misc_jndi_earwarjar_ear;
        }

        @Test
        @Override
        public void globalJNDI() throws java.lang.Exception {
            super.globalJNDI();
        }

        @Test
        @Override
        public void globalJNDI2() throws java.lang.Exception {
            super.globalJNDI2();
        }

        @Test
        @Override
        public void appJNDI() throws java.lang.Exception {
            super.appJNDI();
        }

        @Test
        @Override
        public void appJNDI2() throws java.lang.Exception {
            super.appJNDI2();
        }

        @Test
        @Override
        public void moduleJNDI2() throws java.lang.Exception {
            super.moduleJNDI2();
        }


}