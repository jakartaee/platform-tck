package com.sun.ts.tests.connector.resourceDefs.servlet;

import com.sun.ts.tests.connector.resourceDefs.servlet.Client;
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
@Tag("connector_resourcedef_servlet_optional")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.connector.resourceDefs.servlet.Client {
    /**
        EE10 Deployment Descriptors:
        servlet_resourcedefs: ear.sun-application.xml
        servlet_resourcedefs_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/connector/resourceDefs/servlet/servlet_resourcedefs_web.xml
        /com/sun/ts/tests/connector/resourceDefs/servlet/servlet_resourcedefs_web.war.sun-web.xml
        Rar:

        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = "servlet_resourcedefs", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive servlet_resourcedefs_web = ShrinkWrap.create(WebArchive.class, "servlet_resourcedefs_web.war");
            // The class files
            servlet_resourcedefs_web.addClasses(
            com.sun.ts.tests.connector.resourceDefs.servlet.CRDTestServlet.class,
            com.sun.ts.tests.connector.resourceDefs.servlet.Client.class,
            com.sun.ts.tests.connector.resourceDefs.servlet.AODTestServlet.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("servlet_resourcedefs_web.xml");
            if(warResURL != null) {
              servlet_resourcedefs_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("servlet_resourcedefs_web.war.sun-web.xml");
            if(warResURL != null) {
              servlet_resourcedefs_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/connector/resourceDefs/servlet/servlet_resourcedefs_web.xml");
            if(warResURL != null) {
              servlet_resourcedefs_web.addAsWebResource(warResURL, "//servlet_resourcedefs_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(servlet_resourcedefs_web, Client.class, warResURL);

        // Ear
            EnterpriseArchive servlet_resourcedefs_ear = ShrinkWrap.create(EnterpriseArchive.class, "servlet_resourcedefs.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            servlet_resourcedefs_ear.addAsModule(servlet_resourcedefs_web);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("servlet_resourcedefs.ear.sun-application.xml");
            if(earResURL != null) {
              servlet_resourcedefs_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(servlet_resourcedefs_ear, Client.class, earResURL);
        return servlet_resourcedefs_ear;
        }

        @Test
        @Override
        public void ValidateGlobalResourceDef() throws java.lang.Exception {
            super.ValidateGlobalResourceDef();
        }

        @Test
        @Override
        public void ValidateAppResourceDef() throws java.lang.Exception {
            super.ValidateAppResourceDef();
        }

        @Test
        @Override
        public void ValidateModuleResourceDef() throws java.lang.Exception {
            super.ValidateModuleResourceDef();
        }

        @Test
        @Override
        public void ValidateCompResourceDef() throws java.lang.Exception {
            super.ValidateCompResourceDef();
        }

        @Test
        @Override
        public void ValidateGlobalAdminObj() throws java.lang.Exception {
            super.ValidateGlobalAdminObj();
        }

        @Test
        @Override
        public void ValidateAppAdminObj() throws java.lang.Exception {
            super.ValidateAppAdminObj();
        }

        @Test
        @Override
        public void ValidateModuleAdminObj() throws java.lang.Exception {
            super.ValidateModuleAdminObj();
        }

        @Test
        @Override
        public void ValidateCompAdminObj() throws java.lang.Exception {
            super.ValidateCompAdminObj();
        }


}