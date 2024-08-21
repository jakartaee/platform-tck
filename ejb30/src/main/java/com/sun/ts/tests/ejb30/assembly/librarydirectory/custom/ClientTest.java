package com.sun.ts.tests.ejb30.assembly.librarydirectory.custom;

import com.sun.ts.tests.ejb30.assembly.librarydirectory.custom.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.assembly.librarydirectory.custom.Client {
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
        ejb3_assembly_librarydirectory_custom: META-INF/application.xml
        ejb3_assembly_librarydirectory_custom_client: jar.sun-application-client.xml
        ejb3_assembly_librarydirectory_custom_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/assembly/librarydirectory/custom/ejb3_assembly_librarydirectory_custom_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/assembly/librarydirectory/custom/ejb3_assembly_librarydirectory_custom_ejb.jar.sun-ejb-jar.xml
        Ear:

        /com/sun/ts/tests/ejb30/assembly/librarydirectory/custom/application.xml
        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_assembly_librarydirectory_custom", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_assembly_librarydirectory_custom_client = ShrinkWrap.create(JavaArchive.class, "ejb3_assembly_librarydirectory_custom_client.jar");
            // The class files
            ejb3_assembly_librarydirectory_custom_client.addClasses(
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.assembly.common.ClientBase.class,
            com.sun.ts.tests.ejb30.assembly.librarydirectory.custom.Client.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("com/sun/ts/tests/ejb30/assembly/librarydirectory/custom/ejb3_assembly_librarydirectory_custom_client.xml");
            if(resURL != null) {
              ejb3_assembly_librarydirectory_custom_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/librarydirectory/custom/ejb3_assembly_librarydirectory_custom_client.jar.sun-application-client.xml");
            if(resURL != null) {
              ejb3_assembly_librarydirectory_custom_client.addAsManifestResource(resURL, "application-client.xml");
            }
            ejb3_assembly_librarydirectory_custom_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_assembly_librarydirectory_custom_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb3_assembly_librarydirectory_custom_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_assembly_librarydirectory_custom_ejb.jar");
            // The class files
            ejb3_assembly_librarydirectory_custom_ejb.addClasses(
                com.sun.ts.tests.ejb30.assembly.librarydirectory.custom.AssemblyBean.class,
                com.sun.ts.tests.ejb30.assembly.common.AssemblyBeanBase.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/librarydirectory/custom/ejb3_assembly_librarydirectory_custom_ejb.xml");
            if(ejbResURL != null) {
              ejb3_assembly_librarydirectory_custom_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/librarydirectory/custom/ejb3_assembly_librarydirectory_custom_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb3_assembly_librarydirectory_custom_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb3_assembly_librarydirectory_custom_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_assembly_librarydirectory_custom_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_assembly_librarydirectory_custom.ear");

            // Any libraries added to the ear
                URL libURL;
                JavaArchive lib_shared_lib = ShrinkWrap.create(JavaArchive.class, "lib-shared.jar");

                // The resources
                        libURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/common/foo.txt");
                        lib_shared_lib.addAsResource(libURL, "/com/sun/ts/tests/ejb30/assembly/common/foo.txt");
                        libURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/librarydirectory/custom/foo.txt");
                        lib_shared_lib.addAsResource(libURL, "/com/sun/ts/tests/ejb30/assembly/librarydirectory/custom/foo.txt");
                        libURL = Client.class.getResource("/foo.txt");
                        lib_shared_lib.addAsResource(libURL, "/foo.txt");

                ejb3_assembly_librarydirectory_custom_ear.addAsLibrary(lib_shared_lib);
                JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");
                    // The class files
                    shared_lib.addClasses(
                        com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                        com.sun.ts.tests.ejb30.misc.getresource.common.GetResourceTest.class,
                        com.sun.ts.tests.ejb30.assembly.common.AssemblyRemoteIF.class,
                        com.sun.ts.tests.ejb30.assembly.common.AssemblyLocalIF.class,
                        com.sun.ts.tests.ejb30.assembly.common.AssemblyInterceptor.class,
                        com.sun.ts.tests.ejb30.assembly.common.Util.class,
                        com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                        com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                        com.sun.ts.tests.ejb30.assembly.common.AssemblyCommonIF.class
                    );


                ejb3_assembly_librarydirectory_custom_ear.addAsLibrary(shared_lib);
                JavaArchive hello_client_view_lib = ShrinkWrap.create(JavaArchive.class, "hello-client-view.jar");
                    // The class files
                    hello_client_view_lib.addClasses(
                        com.sun.ts.tests.ejb30.common.helloejbjar.HelloCommonIF.class,
                        com.sun.ts.tests.ejb30.common.helloejbjar.HelloLocalIF.class,
                        com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF.class
                    );


                ejb3_assembly_librarydirectory_custom_ear.addAsLibrary(hello_client_view_lib);
                JavaArchive second_level_jar_lib = ShrinkWrap.create(JavaArchive.class, "second-level-jar.jar");

                // The resources
                        libURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/librarydirectory/custom/second-level-jar.txt");
                        second_level_jar_lib.addAsResource(libURL, "/com/sun/ts/tests/ejb30/assembly/librarydirectory/custom/second-level-jar.txt");

                ejb3_assembly_librarydirectory_custom_ear.addAsLibrary(second_level_jar_lib);


            // The component jars built by the package target
            ejb3_assembly_librarydirectory_custom_ear.addAsModule(ejb3_assembly_librarydirectory_custom_ejb);
            ejb3_assembly_librarydirectory_custom_ear.addAsModule(ejb3_assembly_librarydirectory_custom_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/librarydirectory/custom/application.xml");
            if(earResURL != null) {
              ejb3_assembly_librarydirectory_custom_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/librarydirectory/custom/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_assembly_librarydirectory_custom_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_assembly_librarydirectory_custom_ear, Client.class, earResURL);
        return ejb3_assembly_librarydirectory_custom_ear;
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_assembly_librarydirectory_custom")
        public void libDirNotUsed() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.libDirNotUsed();
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_assembly_librarydirectory_custom")
        public void libDirNotUsedEJB() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.libDirNotUsedEJB();
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_assembly_librarydirectory_custom")
        public void secondLevelJar() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.secondLevelJar();
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_assembly_librarydirectory_custom")
        public void secondLevelJarEJB() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.secondLevelJarEJB();
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_assembly_librarydirectory_custom")
        public void secondLevelDir() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.secondLevelDir();
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_assembly_librarydirectory_custom")
        public void secondLevelDirEJB() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.secondLevelDirEJB();
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_assembly_librarydirectory_custom")
        public void postConstructInvokedInSuperElseWhere() throws java.lang.Exception {
            super.postConstructInvokedInSuperElseWhere();
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_assembly_librarydirectory_custom")
        public void remoteAdd() throws java.lang.Exception {
            super.remoteAdd();
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_assembly_librarydirectory_custom")
        public void remoteAddByHelloEJB() throws java.lang.Exception {
            super.remoteAddByHelloEJB();
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_assembly_librarydirectory_custom")
        public void remoteAddByHelloEJBFromAssemblyBean() throws java.lang.Exception {
            super.remoteAddByHelloEJBFromAssemblyBean();
        }


}