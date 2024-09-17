package com.sun.ts.tests.ejb30.assembly.mbean.appclient;

import com.sun.ts.tests.ejb30.assembly.mbean.appclient.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.assembly.mbean.appclient.Client {
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
        ejb3_assembly_mbean_appclient: 
        ejb3_assembly_mbean_appclient_client: 

        Found Descriptors:
        Client:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_assembly_mbean_appclient", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_assembly_mbean_appclient_client = ShrinkWrap.create(JavaArchive.class, "ejb3_assembly_mbean_appclient_client.jar");
            // The class files
            ejb3_assembly_mbean_appclient_client.addClasses(
            com.sun.ts.tests.ejb30.assembly.mbean.appclient.OneManagedBean.class,
            com.sun.ts.tests.ejb30.assembly.mbean.appclient.TwoManagedBean.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.assembly.mbean.appclient.Client.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("com/sun/ts/tests/ejb30/assembly/mbean/appclient/ejb3_assembly_mbean_appclient_client.xml");
            if(resURL != null) {
              ejb3_assembly_mbean_appclient_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/mbean/appclient/ejb3_assembly_mbean_appclient_client.jar.sun-application-client.xml");
            if(resURL != null) {
              ejb3_assembly_mbean_appclient_client.addAsManifestResource(resURL, "application-client.xml");
            }
            ejb3_assembly_mbean_appclient_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_assembly_mbean_appclient_client, Client.class, resURL);

        // Ear
            EnterpriseArchive ejb3_assembly_mbean_appclient_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_assembly_mbean_appclient.ear");

            // Any libraries added to the ear
                URL libURL;
                JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");
                    // The class files
                    shared_lib.addClasses(
                        com.sun.ts.tests.ejb30.assembly.appres.common.AppResBeanBase.class,
                        com.sun.ts.tests.ejb30.common.helper.Helper.class,
                        com.sun.ts.tests.ejb30.assembly.appres.common.AppResCommonIF.class,
                        com.sun.ts.tests.ejb30.assembly.appres.common.AppResTest.class,
                        com.sun.ts.tests.ejb30.common.helloejbjar.HelloCommonIF.class,
                        com.sun.ts.tests.ejb30.common.helloejbjar.HelloLocalIF.class,
                        com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                        com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF.class
                    );


                ejb3_assembly_mbean_appclient_ear.addAsLibrary(shared_lib);


            // The component jars built by the package target
            ejb3_assembly_mbean_appclient_ear.addAsModule(ejb3_assembly_mbean_appclient_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/mbean/appclient/application.xml");
            if(earResURL != null) {
              ejb3_assembly_mbean_appclient_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/assembly/mbean/appclient/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_assembly_mbean_appclient_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_assembly_mbean_appclient_ear, Client.class, earResURL);
        return ejb3_assembly_mbean_appclient_ear;
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_assembly_mbean_appclient")
        public void clientPostConstruct() {
            super.clientPostConstruct();
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_assembly_mbean_appclient")
        public void mbeanPostConstruct() {
            super.mbeanPostConstruct();
        }


}