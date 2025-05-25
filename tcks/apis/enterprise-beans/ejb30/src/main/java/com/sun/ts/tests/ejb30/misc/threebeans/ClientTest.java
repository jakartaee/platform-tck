package com.sun.ts.tests.ejb30.misc.threebeans;

import com.sun.ts.lib.harness.Fault;

import java.net.URL;

import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.tests.common.base.EETest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;


@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("ejb_3x_remote_optional")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.misc.threebeans.Client {
    /**
        EE10 Deployment Descriptors:
        ejb3_misc_threebeans: 
        ejb3_misc_threebeans_client: 
        ejb3_misc_threebeans_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb30/misc/threebeans/ejb3_misc_threebeans_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_misc_threebeans", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_misc_threebeans_client = ShrinkWrap.create(JavaArchive.class, "ejb3_misc_threebeans_client.jar");
            // The class files
            ejb3_misc_threebeans_client.addClasses(
            com.sun.ts.tests.ejb30.misc.threebeans.Client.class,
            Fault.class,
            EETest.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/threebeans/ejb3_misc_threebeans_client.xml");
            if(resURL != null) {
              ejb3_misc_threebeans_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/threebeans/ejb3_misc_threebeans_client.jar.sun-application-client.xml");
            if(resURL != null) {
              ejb3_misc_threebeans_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            ejb3_misc_threebeans_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_misc_threebeans_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb3_misc_threebeans_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_misc_threebeans_ejb.jar");
            // The class files
            ejb3_misc_threebeans_ejb.addClasses(
                com.sun.ts.tests.ejb30.misc.threebeans.ThreeBean.class,
                com.sun.ts.tests.ejb30.misc.threebeans.CommonBeanBase.class,
                com.sun.ts.tests.ejb30.misc.threebeans.FourBean.class,
                com.sun.ts.tests.ejb30.misc.threebeans.OneBean.class,
                com.sun.ts.tests.ejb30.misc.threebeans.TwoBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/threebeans/ejb3_misc_threebeans_ejb.xml");
            if(ejbResURL != null) {
              ejb3_misc_threebeans_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/threebeans/ejb3_misc_threebeans_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb3_misc_threebeans_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb3_misc_threebeans_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_misc_threebeans_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_misc_threebeans.ear");

            // Any libraries added to the ear
                URL libURL;
                JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");
                    // The class files
                    shared_lib.addClasses(
                        com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                        com.sun.ts.tests.ejb30.misc.threebeans.CommonIF.class,
                        com.sun.ts.tests.ejb30.common.helper.Helper.class,
                        com.sun.ts.tests.ejb30.misc.threebeans.TwoRemoteIF.class,
                        com.sun.ts.tests.ejb30.misc.threebeans.OneRemoteIF.class,
                        com.sun.ts.tests.ejb30.misc.threebeans.ThreeRemoteIF.class,
                        com.sun.ts.tests.ejb30.misc.threebeans.TwoLocalIF.class,
                        com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                        com.sun.ts.tests.ejb30.misc.threebeans.ThreeLocalIF.class,
                        com.sun.ts.tests.ejb30.misc.threebeans.OneLocalIF.class
                    );


                ejb3_misc_threebeans_ear.addAsLibrary(shared_lib);


            // The component jars built by the package target
            ejb3_misc_threebeans_ear.addAsModule(ejb3_misc_threebeans_ejb);
            ejb3_misc_threebeans_ear.addAsModule(ejb3_misc_threebeans_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/threebeans/application.xml");
            if(earResURL != null) {
              ejb3_misc_threebeans_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/threebeans/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_misc_threebeans_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_misc_threebeans_ear, Client.class, earResURL);
        return ejb3_misc_threebeans_ear;
        }

        @Test
        @Override
        public void testOne() throws java.lang.Exception {
            super.testOne();
        }

        @Test
        @Override
        public void testException() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.testException();
        }

        @Test
        @Override
        public void testNumber() throws java.lang.Exception {
            super.testNumber();
        }

        @Test
        @Override
        public void testTwo() throws java.lang.Exception {
            super.testTwo();
        }

        @Test
        @Override
        public void testThree() throws java.lang.Exception {
            super.testThree();
        }

        @Test
        @Override
        public void testFour() throws java.lang.Exception {
            super.testFour();
        }


}