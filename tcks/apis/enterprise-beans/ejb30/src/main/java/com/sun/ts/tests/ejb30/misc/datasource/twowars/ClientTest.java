package com.sun.ts.tests.ejb30.misc.datasource.twowars;

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
@Tag("platform")
@Tag("ejb_3x_remote_optional")
@Tag("web_optional")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.misc.datasource.twowars.Client {

    // Doesn't seem to work, but is needed for test:
    // org.jboss.arquillian.container.test.impl.execution.ClientTestExecuter#deployment will always return null
    // deployment with this deployment present.
    @Deployment(name = "two_standalone_component_web", order = 1, testable = true)
    public static WebArchive createDeploymentWar(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War 2
        // the war with the correct archive name
        WebArchive ejb3_misc_datasource_twowars_web_2 = ShrinkWrap.create(WebArchive.class, "two_standalone_component_web.war");

        // The class files
        ejb3_misc_datasource_twowars_web_2.addClasses(
            com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet.class,
            com.sun.ts.tests.ejb30.assembly.appres.common.TestServletBase.class,
            com.sun.ts.tests.ejb30.misc.datasource.twowars.TestServlet2.class,
            com.sun.ts.tests.servlet.common.util.Data.class);

        // Call the archive processor
        URL warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/datasource/twowars/ejb3_misc_datasource_twowars_web.war.sun-web.xml");
        archiveProcessor.processWebArchive(ejb3_misc_datasource_twowars_web_2, Client.class, warResURL);

        return ejb3_misc_datasource_twowars_web_2;
    }


    /**
     * EE10 Deployment Descriptors: ejb3_misc_datasource_twowars: ejb3_misc_datasource_twowars_ejb: jar.sun-ejb-jar.xml
     * ejb3_misc_datasource_twowars_web:
     *
     * Found Descriptors: Ejb:
     *
     * /com/sun/ts/tests/ejb30/misc/datasource/twowars/ejb3_misc_datasource_twowars_ejb.jar.sun-ejb-jar.xml War:
     *
     * Ear:
     *
     */
    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = "ejb3_misc_datasource_twowars", order = 2)
    public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War 1
        // the war with the correct archive name
        WebArchive ejb3_misc_datasource_twowars_web = ShrinkWrap.create(WebArchive.class, "ejb3_misc_datasource_twowars_web.war");

        // The class files
        ejb3_misc_datasource_twowars_web.addClasses(
            com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet.class,
            com.sun.ts.tests.ejb30.assembly.appres.common.TestServletBase.class,
            com.sun.ts.tests.ejb30.misc.datasource.twowars.TestServlet.class,
            com.sun.ts.tests.servlet.common.util.Data.class);

        // The web.xml descriptor
        URL warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/datasource/twowars/ejb3_misc_datasource_twowars_web.xml");
        if (warResURL != null) {
            ejb3_misc_datasource_twowars_web.addAsWebInfResource(warResURL, "web.xml");
        }
        // The sun-web.xml descriptor
        warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/datasource/twowars/ejb3_misc_datasource_twowars_web.war.sun-web.xml");
        if (warResURL != null) {
            ejb3_misc_datasource_twowars_web.addAsWebInfResource(warResURL, "sun-web.xml");
        }

        // Any libraries added to the war

        // Web content

        // Call the archive processor
        archiveProcessor.processWebArchive(ejb3_misc_datasource_twowars_web, Client.class, warResURL);


        // Ejb
        // the jar with the correct archive name
        JavaArchive ejb3_misc_datasource_twowars_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_misc_datasource_twowars_ejb.jar");

        // The class files
        ejb3_misc_datasource_twowars_ejb.addClasses(com.sun.ts.tests.ejb30.misc.datasource.twowars.DataSourceBean.class);

        // The ejb-jar.xml descriptor
        URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/datasource/twowars/ejb3_misc_datasource_twowars_ejb.xml");
        if (ejbResURL != null) {
            ejb3_misc_datasource_twowars_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
        }

        // The sun-ejb-jar.xml file
        ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/datasource/twowars/ejb3_misc_datasource_twowars_ejb.jar.sun-ejb-jar.xml");
        if (ejbResURL != null) {
            ejb3_misc_datasource_twowars_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
        }
        // Call the archive processor
        archiveProcessor.processEjbArchive(ejb3_misc_datasource_twowars_ejb, Client.class, ejbResURL);


        // Ear
        EnterpriseArchive ejb3_misc_datasource_twowars_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_misc_datasource_twowars.ear");

        // Any libraries added to the ear
        JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");

        // The class files
        shared_lib.addClasses(com.sun.ts.tests.ejb30.assembly.appres.common.AppResLocalIF.class,
                com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceTest.class,
                com.sun.ts.tests.ejb30.assembly.appres.common.AppResBeanBase.class,
                com.sun.ts.tests.ejb30.common.helper.Helper.class,
                com.sun.ts.tests.ejb30.assembly.appres.common.AppResCommonIF.class,
                com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF.class,
                com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class);

        ejb3_misc_datasource_twowars_ear.addAsLibrary(shared_lib);

        // The component jars built by the package target
        ejb3_misc_datasource_twowars_ear.addAsModule(ejb3_misc_datasource_twowars_ejb);
        ejb3_misc_datasource_twowars_ear.addAsModule(ejb3_misc_datasource_twowars_web);

        // The application.xml descriptor
        URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/datasource/twowars/application.xml");
        if (earResURL != null) {
            ejb3_misc_datasource_twowars_ear.addAsManifestResource(earResURL, "application.xml");
        }

        // The sun-application.xml descriptor
        earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/datasource/twowars/application.ear.sun-application.xml");
        if (earResURL != null) {
            ejb3_misc_datasource_twowars_ear.addAsManifestResource(earResURL, "sun-application.xml");
        }

        // Call the archive processor
        archiveProcessor.processEarArchive(ejb3_misc_datasource_twowars_ear, Client.class, earResURL);

        return ejb3_misc_datasource_twowars_ear;
    }

    @Test
    @Override
    public void servletPostConstruct() throws java.lang.Exception {
        super.servletPostConstruct();
    }

    @Test
    @Override
    public void servletPostConstruct2() throws java.lang.Exception {
        super.servletPostConstruct2();
    }

    @Test
    @Override
    public void ejbPostConstruct() throws java.lang.Exception {
        super.ejbPostConstruct();
    }

}