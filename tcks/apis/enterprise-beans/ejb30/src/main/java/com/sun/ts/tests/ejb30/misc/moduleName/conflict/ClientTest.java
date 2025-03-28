package com.sun.ts.tests.ejb30.misc.moduleName.conflict;

import com.sun.ts.tests.ejb30.misc.moduleName.conflict.Client;
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
@Tag("ejb_3x_remote_optional")
@Tag("web_optional")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.misc.moduleName.conflict.Client {
    /**
        EE10 Deployment Descriptors:
        ejb3_misc_moduleName_conflict: 
        ejb3_misc_moduleName_conflict_web: 

        Found Descriptors:
        War:

        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = "ejb3_misc_moduleName_conflict", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive ejb3_misc_moduleName_conflict_web = ShrinkWrap.create(WebArchive.class, "ejb3_misc_moduleName_conflict_web.war");
            // The class files
            ejb3_misc_moduleName_conflict_web.addClasses(
            com.sun.ts.tests.ejb30.misc.moduleName.conflict.TestServlet.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("com/sun/ts/tests/ejb30/misc/moduleName/conflict/ejb3_misc_moduleName_conflict_web.xml");
            if(warResURL != null) {
              ejb3_misc_moduleName_conflict_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/moduleName/conflict/ejb3_misc_moduleName_conflict_web.war.sun-web.xml");
            if(warResURL != null) {
              ejb3_misc_moduleName_conflict_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
           // Call the archive processor
           archiveProcessor.processWebArchive(ejb3_misc_moduleName_conflict_web, Client.class, warResURL);

        // Ear
            EnterpriseArchive ejb3_misc_moduleName_conflict_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_misc_moduleName_conflict.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            ejb3_misc_moduleName_conflict_ear.addAsModule(ejb3_misc_moduleName_conflict_web);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/moduleName/conflict/");
            if(earResURL != null) {
              ejb3_misc_moduleName_conflict_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/moduleName/conflict/.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_misc_moduleName_conflict_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_misc_moduleName_conflict_ear, Client.class, earResURL);
        return ejb3_misc_moduleName_conflict_ear;
        }

        @Test
        @Override
        public void servletPostConstruct() throws java.lang.Exception {
            super.servletPostConstruct();
        }


}