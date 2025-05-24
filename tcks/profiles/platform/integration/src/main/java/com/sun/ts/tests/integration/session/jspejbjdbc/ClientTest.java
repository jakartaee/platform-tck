package com.sun.ts.tests.integration.session.jspejbjdbc;

import com.sun.ts.lib.harness.Fault;

import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;


@ExtendWith(ArquillianExtension.class)
@Tag("integration")
@Tag("platform")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.integration.session.jspejbjdbc.URLClient {
    /**
        EE10 Deployment Descriptors:
        int_S_jspejbjdbc: 
        int_S_jspejbjdbc_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        int_S_jspejbjdbc_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Ejb:

        /com/sun/ts/tests/integration/session/jspejbjdbc/int_S_jspejbjdbc_ejb.xml
        /com/sun/ts/tests/integration/session/jspejbjdbc/int_S_jspejbjdbc_ejb.jar.sun-ejb-jar.xml
        War:

        /com/sun/ts/tests/integration/session/jspejbjdbc/int_S_jspejbjdbc_web.xml
        /com/sun/ts/tests/integration/session/jspejbjdbc/int_S_jspejbjdbc_web.war.sun-web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = "int_S_jspejbjdbc", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive int_S_jspejbjdbc_web = ShrinkWrap.create(WebArchive.class, "int_S_jspejbjdbc_web.war");
            // The class files
            int_S_jspejbjdbc_web.addClasses(
            com.sun.ts.tests.integration.session.jspejbjdbc.Teller.class,
            com.sun.ts.tests.integration.session.jspejbjdbc.AccessJSPBean.class
            );
            // The web.xml descriptor
            URL warResURL = URLClient.class.getResource("int_S_jspejbjdbc_web.xml");
            int_S_jspejbjdbc_web.addAsWebInfResource(warResURL, "web.xml");
            // The sun-web.xml descriptor
            warResURL = URLClient.class.getResource("int_S_jspejbjdbc_web.war.sun-web.xml");
            int_S_jspejbjdbc_web.addAsWebInfResource(warResURL, "sun-web.xml");

            // Any libraries added to the war

            // Web content
            warResURL = URLClient.class.getResource("/com/sun/ts/tests/integration/session/jspejbjdbc/contentRoot/client.html");
            int_S_jspejbjdbc_web.addAsWebResource(warResURL, "/client.html");
            warResURL = URLClient.class.getResource("/com/sun/ts/tests/integration/session/jspejbjdbc/contentRoot/jsp2ejb.jsp");
            int_S_jspejbjdbc_web.addAsWebResource(warResURL, "/jsp2ejb.jsp");
            warResURL = URLClient.class.getResource("/com/sun/ts/tests/integration/session/jspejbjdbc/contentRoot/jsp2ejbother.jsp");
            int_S_jspejbjdbc_web.addAsWebResource(warResURL, "/jsp2ejbother.jsp");
            warResURL = URLClient.class.getResource("/com/sun/ts/tests/integration/session/jspejbjdbc/contentRoot/jspbean2ejb.jsp");
            int_S_jspejbjdbc_web.addAsWebResource(warResURL, "/jspbean2ejb.jsp");

           // Call the archive processor
           archiveProcessor.processWebArchive(int_S_jspejbjdbc_web, URLClient.class, warResURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive int_S_jspejbjdbc_ejb = ShrinkWrap.create(JavaArchive.class, "int_S_jspejbjdbc_ejb.jar");
            // The class files
            int_S_jspejbjdbc_ejb.addClasses(
                com.sun.ts.tests.integration.util.DBSupport.class,
                com.sun.ts.tests.integration.util.Account.class,
                com.sun.ts.tests.integration.session.jspejbjdbc.Teller.class,
                com.sun.ts.tests.integration.session.jspejbjdbc.TellerBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = URLClient.class.getResource("int_S_jspejbjdbc_ejb.xml");
            int_S_jspejbjdbc_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            // The sun-ejb-jar.xml file
            ejbResURL1 = URLClient.class.getResource("int_S_jspejbjdbc_ejb.jar.sun-ejb-jar.xml");
            int_S_jspejbjdbc_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            // Call the archive processor
            archiveProcessor.processEjbArchive(int_S_jspejbjdbc_ejb, URLClient.class, ejbResURL1);


        // Ear
            EnterpriseArchive int_S_jspejbjdbc_ear = ShrinkWrap.create(EnterpriseArchive.class, "int_S_jspejbjdbc.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            int_S_jspejbjdbc_ear.addAsModule(int_S_jspejbjdbc_ejb);
            int_S_jspejbjdbc_ear.addAsModule(int_S_jspejbjdbc_web);



            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(int_S_jspejbjdbc_ear, URLClient.class, earResURL);
        return int_S_jspejbjdbc_ear;
        }

        @Test
        @Override
        public void test1() throws Fault {
            super.test1();
        }

        @Test
        @Override
        public void test2() throws Fault {
            super.test2();
        }


}