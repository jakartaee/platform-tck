package com.sun.ts.tests.ejb30.misc.getresource.appclient;

import com.sun.ts.tests.ejb30.misc.getresource.appclient.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.misc.getresource.appclient.Client {
    /**
        EE10 Deployment Descriptors:
        misc_getresource_appclient: 
        misc_getresource_appclient_client: 

        Found Descriptors:
        Client:

        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "misc_getresource_appclient", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive misc_getresource_appclient_client = ShrinkWrap.create(JavaArchive.class, "misc_getresource_appclient_client.jar");
            // The class files
            misc_getresource_appclient_client.addClasses(
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.misc.getresource.appclient.Client.class,
            com.sun.ts.tests.ejb30.misc.getresource.common.GetResourceBeanBase.class,
            com.sun.ts.tests.ejb30.misc.getresource.appclient.GetResourceDelegate.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            misc_getresource_appclient_client.add(new StringAsset("cts-ejb3-test-5.1.1-beta.txt"), "/com/sun/ts/tests/ejb30/misc/getresource/appclient/cts-ejb3-test-5.1.1-beta.txt");
            misc_getresource_appclient_client.add(new StringAsset("/"), "/cts-ejb3-test-5.1.1-beta.txt");

            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/getresource/appclient/misc_getresource_appclient_client.xml");
            if(resURL != null) {
              misc_getresource_appclient_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/getresource/appclient/misc_getresource_appclient_client.jar.sun-application-client.xml");
            if(resURL != null) {
              misc_getresource_appclient_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            misc_getresource_appclient_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(misc_getresource_appclient_client, Client.class, resURL);

        // Ear
            EnterpriseArchive misc_getresource_appclient_ear = ShrinkWrap.create(EnterpriseArchive.class, "misc_getresource_appclient.ear");

            // Any libraries added to the ear
                JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");
                    // The class files
                    shared_lib.addClasses(
                        com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                        com.sun.ts.tests.ejb30.misc.getresource.common.GetResourceTest.class,
                        com.sun.ts.tests.ejb30.misc.getresource.common.GetResourceIF.class,
                        com.sun.ts.tests.ejb30.common.helper.TLogger.class
                    );


                // The resources
                shared_lib.add(new StringAsset("cts-ejb3-test-5.1.1-beta-ear-lib.txt"), "/com/sun/ts/tests/ejb30/misc/getresource/appclient/cts-ejb3-test-5.1.1-beta-ear-lib.txt");
                shared_lib.add(new StringAsset("/lib"), "/cts-ejb3-test-5.1.1-beta-ear-lib.txt");

                misc_getresource_appclient_ear.addAsLibrary(shared_lib);


            // The component jars built by the package target
            misc_getresource_appclient_ear.addAsModule(misc_getresource_appclient_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/getresource/appclient/application.xml");
            if(earResURL != null) {
              misc_getresource_appclient_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/getresource/appclient/application.ear.sun-application.xml");
            if(earResURL != null) {
              misc_getresource_appclient_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(misc_getresource_appclient_ear, Client.class, earResURL);
        return misc_getresource_appclient_ear;
        }

        @Test
        @Override
        public void getResourceNullParam() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.getResourceNullParam();
        }

        @Test
        @Override
        public void getResourceAsStreamNullParam() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.getResourceAsStreamNullParam();
        }

        @Test
        @Override
        public void getResourceNonexisting() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.getResourceNonexisting();
        }

        @Test
        @Override
        public void getResourceAsStreamNonexisting() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.getResourceAsStreamNonexisting();
        }

        @Test
        @Override
        public void getResourceSamePackage() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.getResourceSamePackage();
        }

        @Test
        @Override
        public void getResourceAsStreamSamePackage() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.getResourceAsStreamSamePackage();
        }

        @Test
        @Override
        public void getResourceResolve() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.getResourceResolve();
        }

        @Test
        @Override
        public void getResourceAsStreamResolve() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.getResourceAsStreamResolve();
        }

        @Test
        @Override
        public void getResourceResolveEarLib() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.getResourceResolveEarLib();
        }

        @Test
        @Override
        public void getResourceAsStreamResolveEarLib() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.getResourceAsStreamResolveEarLib();
        }


}