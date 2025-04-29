package com.sun.ts.tests.ejb30.misc.moduleName.twowars;

import com.sun.ts.tests.ejb30.misc.moduleName.twowars.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.misc.moduleName.twowars.Client {

    @Deployment(name = "two_standalone_component_web", order = 2, testable = false)
    public static WebArchive createCommonDeployment() {
        WebArchive two_standalone_component_web = ShrinkWrap.create(WebArchive.class, "two_standalone_component_web.war");
        // The class files
        two_standalone_component_web.addClasses(
                com.sun.ts.tests.ejb30.assembly.appres.common.AppResBeanBase.class,
                com.sun.ts.tests.ejb30.assembly.appres.common.AppResCommonIF.class,
                com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF.class,
                com.sun.ts.tests.ejb30.assembly.appres.common.TestServletBase.class,
                com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet.class,
                com.sun.ts.tests.servlet.common.util.Data.class,
                com.sun.ts.tests.ejb30.assembly.appres.common.TestServletBase.class,
                com.sun.ts.tests.ejb30.common.helper.Helper.class,
                com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                com.sun.ts.tests.ejb30.misc.moduleName.twowars.TestServlet2.class,
                com.sun.ts.tests.ejb30.misc.moduleName.twowars.Module2Bean.class
        );
        URL warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/moduleName/twowars/two_standalone_component_web.xml");
        two_standalone_component_web.addAsWebInfResource(warResURL, "web.xml");
        warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/moduleName/twowars/two_standalone_component_web.war.sun-web.xml");
        two_standalone_component_web.addAsWebInfResource(warResURL, "sun-web.xml");

        return two_standalone_component_web;
    }

    /**
        EE10 Deployment Descriptors:
        ejb3_misc_moduleName_twowars: 
        ejb3_misc_moduleName_twowars_web: WEB-INF/web.xml,war.sun-ejb-jar.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/ejb30/misc/moduleName/twowars/ejb3_misc_moduleName_twowars_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = "ejb3_misc_moduleName_twowars", order = 1)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive ejb3_misc_moduleName_twowars_web = ShrinkWrap.create(WebArchive.class, "ejb3_misc_moduleName_twowars_web.war");
            // The class files
            ejb3_misc_moduleName_twowars_web.addClasses(
            com.sun.ts.tests.ejb30.misc.moduleName.twowars.ModuleBean.class,
            com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet.class,
            com.sun.ts.tests.ejb30.misc.moduleName.twowars.TestServlet.class,
            com.sun.ts.tests.ejb30.assembly.appres.common.TestServletBase.class,
            com.sun.ts.tests.servlet.common.util.Data.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/moduleName/twowars/ejb3_misc_moduleName_twowars_web.xml");
            if(warResURL != null) {
              ejb3_misc_moduleName_twowars_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/moduleName/twowars/ejb3_misc_moduleName_twowars_web.war.sun-web.xml");
            if(warResURL != null) {
              ejb3_misc_moduleName_twowars_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war


           // Call the archive processor
           archiveProcessor.processWebArchive(ejb3_misc_moduleName_twowars_web, Client.class, warResURL);

        // Ear
            EnterpriseArchive ejb3_misc_moduleName_twowars_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_misc_moduleName_twowars.ear");

            // Any libraries added to the ear
                URL libURL;
                JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");
                    // The class files
                    shared_lib.addClasses(
                        com.sun.ts.tests.ejb30.assembly.appres.common.AppResBeanBase.class,
                        com.sun.ts.tests.ejb30.common.helper.Helper.class,
                        com.sun.ts.tests.ejb30.assembly.appres.common.AppResCommonIF.class,
                        com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF.class,
                        com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class
                    );


                ejb3_misc_moduleName_twowars_ear.addAsLibrary(shared_lib);


            // The component jars built by the package target
            ejb3_misc_moduleName_twowars_ear.addAsModule(ejb3_misc_moduleName_twowars_web);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/moduleName/twowars/application.xml");
            if(earResURL != null) {
              ejb3_misc_moduleName_twowars_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/moduleName/twowars/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_misc_moduleName_twowars_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_misc_moduleName_twowars_ear, Client.class, earResURL);
        return ejb3_misc_moduleName_twowars_ear;
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_misc_moduleName_twowars")
        public void servletPostConstruct() throws java.lang.Exception {
            super.servletPostConstruct();
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_misc_moduleName_twowars")
        public void servletPostConstruct2() throws java.lang.Exception {
            super.servletPostConstruct2();
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_misc_moduleName_twowars")
        public void ejbPostConstruct() throws java.lang.Exception {
            super.ejbPostConstruct();
        }


}