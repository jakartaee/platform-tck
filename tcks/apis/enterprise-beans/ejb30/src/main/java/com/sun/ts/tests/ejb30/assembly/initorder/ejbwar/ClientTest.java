package com.sun.ts.tests.ejb30.assembly.initorder.ejbwar;

import com.sun.ts.tests.ejb30.assembly.initorder.ejbwar.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.assembly.initorder.ejbwar.Client {
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
        ejb3_assembly_initorder_ejbwar: META-INF/application.xml
        ejb3_assembly_initorder_ejbwar_ejb: jar.sun-ejb-jar.xml
        ejb3_assembly_initorder_ejbwar_web: 

        Found Descriptors:
        Ejb:

        /com/sun/ts/tests/ejb30/assembly/initorder/ejbwar/ejb3_assembly_initorder_ejbwar_ejb.jar.sun-ejb-jar.xml
        War:

        Ear:

        /com/sun/ts/tests/ejb30/assembly/initorder/ejbwar/application.xml
        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = "ejb3_assembly_initorder_ejbwar", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive ejb3_assembly_initorder_ejbwar_web = ShrinkWrap.create(WebArchive.class, "ejb3_assembly_initorder_ejbwar_web.war");
            // The class files
            ejb3_assembly_initorder_ejbwar_web.addClasses(
            com.sun.ts.tests.ejb30.assembly.initorder.ejbwar.TestServlet.class,
            com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet.class,
            com.sun.ts.tests.servlet.common.util.Data.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/initorder/ejbwar/ejb3_assembly_initorder_ejbwar_web.xml");
            if(warResURL != null) {
              ejb3_assembly_initorder_ejbwar_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/initorder/ejbwar/ejb3_assembly_initorder_ejbwar_web.war.sun-web.xml");
            if(warResURL != null) {
              ejb3_assembly_initorder_ejbwar_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
           // Call the archive processor
           archiveProcessor.processWebArchive(ejb3_assembly_initorder_ejbwar_web, Client.class, warResURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb3_assembly_initorder_ejbwar_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_assembly_initorder_ejbwar_ejb.jar");
            // The class files
            ejb3_assembly_initorder_ejbwar_ejb.addClasses(
                com.sun.ts.tests.ejb30.assembly.initorder.common.InitOrderBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/initorder/ejbwar/ejb3_assembly_initorder_ejbwar_ejb.xml");
            if(ejbResURL != null) {
              ejb3_assembly_initorder_ejbwar_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/initorder/ejbwar/ejb3_assembly_initorder_ejbwar_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb3_assembly_initorder_ejbwar_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb3_assembly_initorder_ejbwar_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_assembly_initorder_ejbwar_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_assembly_initorder_ejbwar.ear");

            // Any libraries added to the ear
                URL libURL;
                JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");
                    // The class files
                    shared_lib.addClasses(
                        com.sun.ts.tests.ejb30.common.helper.Helper.class,
                        com.sun.ts.tests.ejb30.common.helloejbjar.HelloCommonIF.class,
                        com.sun.ts.tests.ejb30.common.helloejbjar.HelloLocalIF.class,
                        com.sun.ts.tests.ejb30.assembly.initorder.common.InitOrderRemoteIF.class,
                        com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                        com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF.class
                    );


                ejb3_assembly_initorder_ejbwar_ear.addAsLibrary(shared_lib);


            // The component jars built by the package target
            ejb3_assembly_initorder_ejbwar_ear.addAsModule(ejb3_assembly_initorder_ejbwar_ejb);
            ejb3_assembly_initorder_ejbwar_ear.addAsModule(ejb3_assembly_initorder_ejbwar_web);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/initorder/ejbwar/application.xml");
            if(earResURL != null) {
              ejb3_assembly_initorder_ejbwar_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/initorder/ejbwar/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_assembly_initorder_ejbwar_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_assembly_initorder_ejbwar_ear, Client.class, earResURL);
        return ejb3_assembly_initorder_ejbwar_ear;
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_assembly_initorder_ejbwar")
        public void initOrder() throws java.lang.Exception {
            super.initOrder();
        }


}