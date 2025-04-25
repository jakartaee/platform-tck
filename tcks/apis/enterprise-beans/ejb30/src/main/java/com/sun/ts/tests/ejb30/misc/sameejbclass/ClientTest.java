package com.sun.ts.tests.ejb30.misc.sameejbclass;

import com.sun.ts.tests.ejb30.misc.sameejbclass.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.misc.sameejbclass.Client {
    /**
        EE10 Deployment Descriptors:
        misc_sameejbclass: 
        misc_sameejbclass_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        misc_sameejbclass_web: 

        Found Descriptors:
        Ejb:

        /com/sun/ts/tests/ejb30/misc/sameejbclass/misc_sameejbclass_ejb.xml
        /com/sun/ts/tests/ejb30/misc/sameejbclass/misc_sameejbclass_ejb.jar.sun-ejb-jar.xml
        War:

        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = "misc_sameejbclass", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive misc_sameejbclass_web = ShrinkWrap.create(WebArchive.class, "misc_sameejbclass_web.war");
            // The class files
            misc_sameejbclass_web.addClasses(
            com.sun.ts.tests.ejb30.misc.sameejbclass.TestServlet.class,
            com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet.class,
            com.sun.ts.tests.servlet.common.util.Data.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/sameejbclass/misc_sameejbclass_web.xml");
            if(warResURL != null) {
              misc_sameejbclass_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/sameejbclass/misc_sameejbclass_web.war.sun-web.xml");
            if(warResURL != null) {
              misc_sameejbclass_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
           // Call the archive processor
           archiveProcessor.processWebArchive(misc_sameejbclass_web, Client.class, warResURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive misc_sameejbclass_ejb = ShrinkWrap.create(JavaArchive.class, "misc_sameejbclass_ejb.jar");
            // The class files
            misc_sameejbclass_ejb.addClasses(
                com.sun.ts.tests.ejb30.misc.sameejbclass.SameEJBClassBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/sameejbclass/misc_sameejbclass_ejb.xml");
            if(ejbResURL != null) {
              misc_sameejbclass_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/sameejbclass/misc_sameejbclass_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              misc_sameejbclass_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(misc_sameejbclass_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive misc_sameejbclass_ear = ShrinkWrap.create(EnterpriseArchive.class, "misc_sameejbclass.ear");

            // Any libraries added to the ear
                URL libURL;
                JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");
                    // The class files
                    shared_lib.addClasses(
                        com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                        com.sun.ts.tests.ejb30.misc.sameejbclass.SameEJBClassRemoteIF.class,
                        com.sun.ts.tests.ejb30.common.helper.Helper.class,
                        com.sun.ts.tests.ejb30.misc.sameejbclass.SameEJBClassIF.class,
                        com.sun.ts.tests.ejb30.common.helper.TLogger.class
                    );


                misc_sameejbclass_ear.addAsLibrary(shared_lib);


            // The component jars built by the package target
            misc_sameejbclass_ear.addAsModule(misc_sameejbclass_ejb);
            misc_sameejbclass_ear.addAsModule(misc_sameejbclass_web);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/sameejbclass/application.xml");
            if(earResURL != null) {
              misc_sameejbclass_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/sameejbclass/application.ear.sun-application.xml");
            if(earResURL != null) {
              misc_sameejbclass_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(misc_sameejbclass_ear, Client.class, earResURL);
        return misc_sameejbclass_ear;
        }

        @Test
        @Override
        public void checkEnvEntry() throws java.lang.Exception {
            super.checkEnvEntry();
        }

        @Test
        @Override
        public void testDTO() throws java.lang.Exception {
            super.testDTO();
        }


}