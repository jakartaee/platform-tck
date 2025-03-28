package com.sun.ts.tests.integration.sec.secbasicssl;

import com.sun.ts.tests.integration.sec.secbasicssl.Client;
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
@Tag("integration")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.integration.sec.secbasicssl.Client {
    /**
        EE10 Deployment Descriptors:
        integration_sec_secbasicssl: ear.sun-application.xml
        integration_sec_secbasicssl_web: WEB-INF/web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/integration/sec/secbasicssl/integration_sec_secbasicssl_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = "integration_sec_secbasicssl", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive integration_sec_secbasicssl_web = ShrinkWrap.create(WebArchive.class, "integration_sec_secbasicssl_web.war");
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("integration_sec_secbasicssl_web.xml");
            integration_sec_secbasicssl_web.addAsWebInfResource(warResURL, "web.xml");
            // The sun-web.xml descriptor
            // warResURL = Client.class.getResource("integration_sec_secbasicssl_web.war.sun-web.xml");
            // integration_sec_secbasicssl_web.addAsWebInfResource(warResURL, "sun-web.xml");

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("authorized.jsp");
            integration_sec_secbasicssl_web.addAsWebResource(warResURL, "/authorized.jsp");
            warResURL = Client.class.getResource("basicSSL.jsp");
            integration_sec_secbasicssl_web.addAsWebResource(warResURL, "/basicSSL.jsp");
            warResURL = Client.class.getResource("requestAttributes.jsp");
            integration_sec_secbasicssl_web.addAsWebResource(warResURL, "/requestAttributes.jsp");
            warResURL = Client.class.getResource("rolereverse.jsp");
            integration_sec_secbasicssl_web.addAsWebResource(warResURL, "/rolereverse.jsp");
            warResURL = Client.class.getResource("webApiRemoteUser1.jsp");
            integration_sec_secbasicssl_web.addAsWebResource(warResURL, "/webApiRemoteUser1.jsp");
            warResURL = Client.class.getResource("webNoAuthz.jsp");
            integration_sec_secbasicssl_web.addAsWebResource(warResURL, "/webNoAuthz.jsp");
            warResURL = Client.class.getResource("webNotInRole.jsp");
            integration_sec_secbasicssl_web.addAsWebResource(warResURL, "/webNotInRole.jsp");
            warResURL = Client.class.getResource("webRoleRefScope1.jsp");
            integration_sec_secbasicssl_web.addAsWebResource(warResURL, "/webRoleRefScope1.jsp");

           // Call the archive processor
           archiveProcessor.processWebArchive(integration_sec_secbasicssl_web, Client.class, warResURL);


        // Ear
            EnterpriseArchive integration_sec_secbasicssl_ear = ShrinkWrap.create(EnterpriseArchive.class, "integration_sec_secbasicssl.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            integration_sec_secbasicssl_ear.addAsModule(integration_sec_secbasicssl_web);
            URL earResURL = Client.class.getResource("integration_sec_secbasicssl.ear.sun-application.xml");
            integration_sec_secbasicssl_ear.addAsManifestResource(earResURL, "sun-application.xml");




            // The application.xml descriptor
            earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(integration_sec_secbasicssl_ear, Client.class, earResURL);
        return integration_sec_secbasicssl_ear;
        }

        @Test
        @Override
        public void test_login_basic_over_ssl() throws java.lang.Exception {
            super.test_login_basic_over_ssl();
        }

        @Test
        @Override
        public void test_web_no_authz() throws java.lang.Exception {
            super.test_web_no_authz();
        }

        @Test
        @Override
        public void test_web_not_in_role() throws java.lang.Exception {
            super.test_web_not_in_role();
        }

        @Test
        @Override
        public void test_web_api_remoteuser_1() throws java.lang.Exception {
            super.test_web_api_remoteuser_1();
        }

        @Test
        @Override
        public void test_web_api_remoteuser_2() throws java.lang.Exception {
            super.test_web_api_remoteuser_2();
        }

        @Test
        @Override
        public void test_web_roleref_scope_1() throws java.lang.Exception {
            super.test_web_roleref_scope_1();
        }

        @Test
        @Override
        public void test_web_roleref_scope_2() throws java.lang.Exception {
            super.test_web_roleref_scope_2();
        }

        @Test
        @Override
        public void test_web_is_authz() throws java.lang.Exception {
            super.test_web_is_authz();
        }

        @Test
        @Override
        public void test_request_attributes() throws java.lang.Exception {
            super.test_request_attributes();
        }


}