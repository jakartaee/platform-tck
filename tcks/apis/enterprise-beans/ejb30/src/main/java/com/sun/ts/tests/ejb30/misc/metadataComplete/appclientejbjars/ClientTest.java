package com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars;

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
public class ClientTest extends com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars.Client {
    /**
        EE10 Deployment Descriptors:
        misc_metadataComplete_appclientejbjars: 
        misc_metadataComplete_appclientejbjars_client: META-INF/application-client.xml
        misc_metadataComplete_appclientejbjars_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/misc/metadataComplete/appclientejbjars/misc_metadataComplete_appclientejbjars_client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/misc/metadataComplete/appclientejbjars/misc_metadataComplete_appclientejbjars_ejb.xml
        /com/sun/ts/tests/ejb30/misc/metadataComplete/appclientejbjars/misc_metadataComplete_appclientejbjars_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "misc_metadataComplete_appclientejbjars", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive misc_metadataComplete_appclientejbjars_client = ShrinkWrap.create(JavaArchive.class, "misc_metadataComplete_appclientejbjars_client.jar");
            // The class files
            misc_metadataComplete_appclientejbjars_client.addClasses(
            com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars.Client.class,
            Fault.class,
            com.sun.ts.tests.ejb30.common.calc.RemoteCalculator.class,
            EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.common.calc.CalculatorException.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/metadataComplete/appclientejbjars/misc_metadataComplete_appclientejbjars_client.xml");
            if(resURL != null) {
              misc_metadataComplete_appclientejbjars_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/metadataComplete/appclientejbjars/misc_metadataComplete_appclientejbjars_client.jar.sun-application-client.xml");
            if(resURL != null) {
              misc_metadataComplete_appclientejbjars_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            misc_metadataComplete_appclientejbjars_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(misc_metadataComplete_appclientejbjars_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive misc_metadataComplete_appclientejbjars_ejb = ShrinkWrap.create(JavaArchive.class, "misc_metadataComplete_appclientejbjars_ejb.jar");
            // The class files
            misc_metadataComplete_appclientejbjars_ejb.addClasses(
                com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars.InterceptorNotUsed.class,
                com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars.InterceptorUsed.class,
                com.sun.ts.tests.ejb30.common.calc.NoInterfaceRemoteCalculator.class,
                com.sun.ts.tests.ejb30.common.calc.BaseRemoteCalculator.class,
                com.sun.ts.tests.ejb30.common.calc.RemoteCalculator.class,
                com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars.RemoteCalculatorBean0.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars.StatefulRemoteCalculatorBean.class,
                com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars.StatelessRemoteCalculatorBean.class,
                com.sun.ts.tests.ejb30.common.calc.CalculatorException.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/metadataComplete/appclientejbjars/misc_metadataComplete_appclientejbjars_ejb.xml");
            if(ejbResURL != null) {
              misc_metadataComplete_appclientejbjars_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/metadataComplete/appclientejbjars/misc_metadataComplete_appclientejbjars_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              misc_metadataComplete_appclientejbjars_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(misc_metadataComplete_appclientejbjars_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive misc_metadataComplete_appclientejbjars_ear = ShrinkWrap.create(EnterpriseArchive.class, "misc_metadataComplete_appclientejbjars.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            misc_metadataComplete_appclientejbjars_ear.addAsModule(misc_metadataComplete_appclientejbjars_ejb);
            misc_metadataComplete_appclientejbjars_ear.addAsModule(misc_metadataComplete_appclientejbjars_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/metadataComplete/appclientejbjars/application.xml");
            if(earResURL != null) {
              misc_metadataComplete_appclientejbjars_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/metadataComplete/appclientejbjars/application.ear.sun-application.xml");
            if(earResURL != null) {
              misc_metadataComplete_appclientejbjars_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(misc_metadataComplete_appclientejbjars_ear, Client.class, earResURL);
        return misc_metadataComplete_appclientejbjars_ear;
        }

        @Test
        @Override
        public void annotationNotProcessedForStateless() throws java.lang.Exception {
            super.annotationNotProcessedForStateless();
        }

        @Test
        @Override
        public void annotationNotProcessedForStateful() throws java.lang.Exception {
            super.annotationNotProcessedForStateful();
        }

        @Test
        @Override
        public void annotationNotProcessedForAppclient() throws java.lang.Exception {
            super.annotationNotProcessedForAppclient();
        }


}