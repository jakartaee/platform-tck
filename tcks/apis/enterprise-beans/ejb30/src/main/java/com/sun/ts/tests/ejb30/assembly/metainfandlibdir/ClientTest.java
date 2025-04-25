package com.sun.ts.tests.ejb30.assembly.metainfandlibdir;

import com.sun.ts.tests.ejb30.assembly.metainfandlibdir.Client;
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
import org.jboss.shrinkwrap.impl.base.path.BasicPath;
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
public class ClientTest extends com.sun.ts.tests.ejb30.assembly.metainfandlibdir.Client {
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
        ejb3_assembly_metainfandlibdir: META-INF/application.xml
        ejb3_assembly_metainfandlibdir_ejb: jar.sun-ejb-jar.xml
        ejb3_assembly_metainfandlibdir_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Ejb:

        /com/sun/ts/tests/ejb30/assembly/metainfandlibdir/ejb3_assembly_metainfandlibdir_ejb.jar.sun-ejb-jar.xml
        War:

        /com/sun/ts/tests/ejb30/assembly/metainfandlibdir/ejb3_assembly_metainfandlibdir_web.xml
        /com/sun/ts/tests/ejb30/assembly/metainfandlibdir/ejb3_assembly_metainfandlibdir_web.war.sun-web.xml
        Ear:

        /com/sun/ts/tests/ejb30/assembly/metainfandlibdir/application.xml
        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = "ejb3_assembly_metainfandlibdir", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive ejb3_assembly_metainfandlibdir_web = ShrinkWrap.create(WebArchive.class, "ejb3_assembly_metainfandlibdir_web.war");
            // The class files
            ejb3_assembly_metainfandlibdir_web.addClasses(
            com.sun.ts.tests.ejb30.assembly.metainfandlibdir.TestServlet.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/metainfandlibdir/ejb3_assembly_metainfandlibdir_web.xml");
            if(warResURL != null) {
              ejb3_assembly_metainfandlibdir_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/metainfandlibdir/ejb3_assembly_metainfandlibdir_web.war.sun-web.xml");
            if(warResURL != null) {
              ejb3_assembly_metainfandlibdir_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/metainfandlibdir/war.mf");
            ejb3_assembly_metainfandlibdir_web.setManifest(warResURL);

            // Any libraries added to the war

           // Call the archive processor
           archiveProcessor.processWebArchive(ejb3_assembly_metainfandlibdir_web, Client.class, warResURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb3_assembly_metainfandlibdir_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_assembly_metainfandlibdir_ejb.jar");
            // The class files
            ejb3_assembly_metainfandlibdir_ejb.addClasses(
                com.sun.ts.tests.ejb30.assembly.common.AssemblyInterceptor.class,
                com.sun.ts.tests.ejb30.assembly.common.AssemblyBeanBase.class,
                com.sun.ts.tests.ejb30.assembly.common.AssemblyBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/metainfandlibdir/ejb3_assembly_metainfandlibdir_ejb.xml");
            if(ejbResURL != null) {
              ejb3_assembly_metainfandlibdir_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/metainfandlibdir/ejb3_assembly_metainfandlibdir_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb3_assembly_metainfandlibdir_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/metainfandlibdir/ejb-jar.mf");
            ejb3_assembly_metainfandlibdir_ejb.setManifest(ejbResURL);
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb3_assembly_metainfandlibdir_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_assembly_metainfandlibdir_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_assembly_metainfandlibdir.ear");

            // Any libraries added to the ear
                URL libURL;
                JavaArchive sub_shared_lib = ShrinkWrap.create(JavaArchive.class, "sub-shared.jar");

                // The resources
            StringAsset fooTxt = new StringAsset("foo.txt");
           sub_shared_lib.add(fooTxt, "/com/sun/ts/tests/ejb30/assembly/metainfandlibdir/foo.txt");
            sub_shared_lib.add(fooTxt, "/com/sun/ts/tests/ejb30/assembly/metainfandlibdir/foo.txt");

                ejb3_assembly_metainfandlibdir_ear.add(sub_shared_lib, new BasicPath("my-lib/lib"), ZipExporter.class);
                JavaArchive x4war_1_0_1_lib = ShrinkWrap.create(JavaArchive.class, "4war-1.0.1.jar");
                    // The class files
                    x4war_1_0_1_lib.addClasses(
                        com.sun.ts.tests.ejb30.assembly.metainfandlibdir.EJBInjectionFilterBase.class,
                        com.sun.ts.tests.ejb30.assembly.metainfandlibdir.EJBInjectionFilter.class,
                        com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet.class,
                        com.sun.ts.tests.servlet.common.util.Data.class,
                        com.sun.ts.tests.ejb30.assembly.metainfandlibdir.TestServletBase.class
                    );


                ejb3_assembly_metainfandlibdir_ear.add(x4war_1_0_1_lib, new BasicPath("/"), ZipExporter.class);
                JavaArchive hello_client_view_lib = ShrinkWrap.create(JavaArchive.class, "hello-client-view.jar");
                    // The class files
                    hello_client_view_lib.addClasses(
                        com.sun.ts.tests.ejb30.common.helloejbjar.HelloCommonIF.class,
                        com.sun.ts.tests.ejb30.common.helloejbjar.HelloLocalIF.class,
                        com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF.class
                    );
                ejb3_assembly_metainfandlibdir_ear.add(hello_client_view_lib, new BasicPath("/"), ZipExporter.class);

                JavaArchive x4common_1_0_1_lib = ShrinkWrap.create(JavaArchive.class, "4common-1.0.1.jar");
                    // The class files
                    x4common_1_0_1_lib.addClasses(
                        com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                        com.sun.ts.tests.ejb30.assembly.common.ConcurrentLookup.LookupThread.class,
                        com.sun.ts.tests.ejb30.assembly.common.AssemblyRemoteIF.class,
                        com.sun.ts.tests.ejb30.assembly.common.AssemblyLocalIF.class,
                        com.sun.ts.tests.ejb30.assembly.common.Util.class,
                        com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                        com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                        com.sun.ts.tests.ejb30.assembly.common.AssemblyCommonIF.class,
                        com.sun.ts.tests.ejb30.assembly.common.ConcurrentLookup.class
                    );


                ejb3_assembly_metainfandlibdir_ear.add(x4common_1_0_1_lib, new BasicPath("my-lib"), ZipExporter.class);


            // The component jars built by the package target
            ejb3_assembly_metainfandlibdir_ear.addAsModule(ejb3_assembly_metainfandlibdir_ejb);
            ejb3_assembly_metainfandlibdir_ear.addAsModule(ejb3_assembly_metainfandlibdir_web);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/metainfandlibdir/application.xml");
            if(earResURL != null) {
              ejb3_assembly_metainfandlibdir_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/metainfandlibdir/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_assembly_metainfandlibdir_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_assembly_metainfandlibdir_ear, Client.class, earResURL);
        return ejb3_assembly_metainfandlibdir_ear;
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_assembly_metainfandlibdir")
        public void remoteAdd() throws java.lang.Exception {
            super.remoteAdd();
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_assembly_metainfandlibdir")
        public void remoteAddByHelloEJB() throws java.lang.Exception {
            super.remoteAddByHelloEJB();
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_assembly_metainfandlibdir")
        public void remoteAddByHelloEJBFromAssemblyBean() throws java.lang.Exception {
            super.remoteAddByHelloEJBFromAssemblyBean();
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_assembly_metainfandlibdir")
        public void ejbInjectionInFilterTest() throws java.lang.Exception {
            super.ejbInjectionInFilterTest();
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_assembly_metainfandlibdir")
        public void libSubdirNotScanned() throws java.lang.Exception {
            super.libSubdirNotScanned();
        }


}