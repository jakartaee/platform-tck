package com.sun.ts.tests.ejb30.misc.getresource.warejb;

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
public class ClientTest extends com.sun.ts.tests.ejb30.misc.getresource.warejb.Client {

    /**
     * EE10 Deployment Descriptors: misc_getresource_warejb: misc_getresource_warejb_ejb: jar.sun-ejb-jar.xml
     * misc_getresource_warejb_web:
     *
     * Found Descriptors: Ejb:
     *
     * /com/sun/ts/tests/ejb30/misc/getresource/warejb/misc_getresource_warejb_ejb.jar.sun-ejb-jar.xml War:
     *
     * Ear:
     *
     */
    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = "misc_getresource_warejb", order = 2)
    public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
        // the war with the correct archive name
        WebArchive misc_getresource_warejb_web = ShrinkWrap.create(WebArchive.class, "misc_getresource_warejb_web.war");

        // The class files
        misc_getresource_warejb_web.addClasses(
            com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet.class,
            com.sun.ts.tests.ejb30.misc.getresource.warejb.TestServlet.class, com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            com.sun.ts.tests.servlet.common.util.Data.class,
            com.sun.ts.tests.ejb30.misc.getresource.warejb.GetResourceDelegateForWeb.class);

        // The web.xml descriptor
        URL warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/getresource/warejb/misc_getresource_warejb_web.xml");
        if (warResURL != null) {
            misc_getresource_warejb_web.addAsWebInfResource(warResURL, "web.xml");
        }
        // The sun-web.xml descriptor
        warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/getresource/warejb/misc_getresource_warejb_web.war.sun-web.xml");
        if (warResURL != null) {
            misc_getresource_warejb_web.addAsWebInfResource(warResURL, "sun-web.xml");
        }

        // Any libraries added to the war

        // Web content                        /com/sun/ts/tests/ejb30/misc/getresource/warejb/cts-ejb3-test-5.1.1-beta.txt
        warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/getresource/warejb/cts-ejb3-test-5.1.1-beta.txt");
        misc_getresource_warejb_web.addAsWebResource(warResURL, "/WEB-INF/classes/com/sun/ts/tests/ejb30/misc/getresource/warejb/cts-ejb3-test-5.1.1-beta.txt");

        warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/getresource/warejb/root-cts-ejb3-test-5.1.1-beta.txt");
        misc_getresource_warejb_web.addAsWebResource(warResURL, "/WEB-INF/classes/cts-ejb3-test-5.1.1-beta.txt");



        // Call the archive processor
        archiveProcessor.processWebArchive(misc_getresource_warejb_web, Client.class, warResURL);


        // Ejb
        // the jar with the correct archive name
        JavaArchive misc_getresource_warejb_ejb = ShrinkWrap.create(JavaArchive.class, "misc_getresource_warejb_ejb.jar");

        // The class files
        misc_getresource_warejb_ejb.addClasses(
            com.sun.ts.tests.ejb30.misc.getresource.warejb.GetResourceBean.class,
            com.sun.ts.tests.ejb30.misc.getresource.warejb.GetResourceDelegateForEJB.class);

        // The ejb-jar.xml descriptor
        URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/getresource/warejb/misc_getresource_warejb_ejb.xml");
        if (ejbResURL != null) {
            misc_getresource_warejb_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
        }

        // The sun-ejb-jar.xml file
        ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/getresource/warejb/misc_getresource_warejb_ejb.jar.sun-ejb-jar.xml");
        if (ejbResURL != null) {
            misc_getresource_warejb_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
        }

        ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/getresource/warejb/cts-ejb3-test-5.1.1-beta.txt");
        misc_getresource_warejb_ejb.addAsResource(ejbResURL, "/com/sun/ts/tests/ejb30/misc/getresource/warejb/cts-ejb3-test-5.1.1-beta.txt");

        ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/getresource/warejb/root-cts-ejb3-test-5.1.1-beta.txt");
        misc_getresource_warejb_ejb.addAsResource(ejbResURL, "/cts-ejb3-test-5.1.1-beta.txt");

        // Call the archive processor
        archiveProcessor.processEjbArchive(misc_getresource_warejb_ejb, Client.class, ejbResURL);


        // Ear
        EnterpriseArchive misc_getresource_warejb_ear = ShrinkWrap.create(EnterpriseArchive.class, "misc_getresource_warejb.ear");

        // Any libraries added to the ear
        URL libURL;
        JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");

        // The class files
        shared_lib.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.tests.ejb30.misc.getresource.common.GetResourceTest.class,
            com.sun.ts.tests.ejb30.misc.getresource.common.GetResourceIF.class,
            com.sun.ts.tests.ejb30.misc.getresource.common.GetResourceBeanBase.class,
            com.sun.ts.tests.ejb30.misc.getresource.warejb.GetResourceDelegate.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class);

        // The resources                   /com/sun/ts/tests/ejb30/misc/getresource/warejb/cts-ejb3-test-5.1.1-beta-ear-lib.txt
        libURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/getresource/warejb/cts-ejb3-test-5.1.1-beta-ear-lib.txt");
        shared_lib.addAsResource(libURL, "/com/sun/ts/tests/ejb30/misc/getresource/warejb/cts-ejb3-test-5.1.1-beta-ear-lib.txt");

        libURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/getresource/warejb/root-cts-ejb3-test-5.1.1-beta-ear-lib.txt");
        shared_lib.addAsResource(libURL, "/cts-ejb3-test-5.1.1-beta-ear-lib.txt");

        misc_getresource_warejb_ear.addAsLibrary(shared_lib);

        // The component jars built by the package target
        misc_getresource_warejb_ear.addAsModule(misc_getresource_warejb_ejb);
        misc_getresource_warejb_ear.addAsModule(misc_getresource_warejb_web);

        // The application.xml descriptor
        URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/getresource/warejb/application.xml");
        if (earResURL != null) {
            misc_getresource_warejb_ear.addAsManifestResource(earResURL, "application.xml");
        }

        // The sun-application.xml descriptor
        earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/getresource/warejb/application.ear.sun-application.xml");
        if (earResURL != null) {
            misc_getresource_warejb_ear.addAsManifestResource(earResURL, "sun-application.xml");
        }

        // Call the archive processor
        archiveProcessor.processEarArchive(misc_getresource_warejb_ear, Client.class, earResURL);

        return misc_getresource_warejb_ear;
    }

    @Test
    @Override
    public void getResourceNullParam() throws java.lang.Exception {
        super.getResourceNullParam();
    }

    @Test
    @Override
    public void getResourceAsStreamNullParam() throws java.lang.Exception {
        super.getResourceAsStreamNullParam();
    }

    @Test
    @Override
    public void getResourceNonexisting() throws java.lang.Exception {
        super.getResourceNonexisting();
    }

    @Test
    @Override
    public void getResourceAsStreamNonexisting() throws java.lang.Exception {
        super.getResourceAsStreamNonexisting();
    }

    @Test
    @Override
    public void getResourceSamePackage() throws java.lang.Exception {
        super.getResourceSamePackage();
    }

    @Test
    @Override
    public void getResourceAsStreamSamePackage() throws java.lang.Exception {
        super.getResourceAsStreamSamePackage();
    }

    @Test
    @Override
    public void getResourceResolve() throws java.lang.Exception {
        super.getResourceResolve();
    }

    @Test
    @Override
    public void getResourceAsStreamResolve() throws java.lang.Exception {
        super.getResourceAsStreamResolve();
    }

    @Test
    @Override
    public void getResourceResolveEarLib() throws java.lang.Exception {
        super.getResourceResolveEarLib();
    }

    @Test
    @Override
    public void getResourceAsStreamResolveEarLib() throws java.lang.Exception {
        super.getResourceAsStreamResolveEarLib();
    }

    @Test
    @Override
    public void getResourceNullParamEJB() throws java.lang.Exception {
        super.getResourceNullParamEJB();
    }

    @Test
    @Override
    public void getResourceAsStreamNullParamEJB() throws java.lang.Exception {
        super.getResourceAsStreamNullParamEJB();
    }

    @Test
    @Override
    public void getResourceNonexistingEJB() throws java.lang.Exception {
        super.getResourceNonexistingEJB();
    }

    @Test
    @Override
    public void getResourceAsStreamNonexistingEJB() throws java.lang.Exception {
        super.getResourceAsStreamNonexistingEJB();
    }

    @Test
    @Override
    public void getResourceSamePackageEJB() throws java.lang.Exception {
        super.getResourceSamePackageEJB();
    }

    @Test
    @Override
    public void getResourceAsStreamSamePackageEJB() throws java.lang.Exception {
        super.getResourceAsStreamSamePackageEJB();
    }

    @Test
    @Override
    public void getResourceResolveEJB() throws java.lang.Exception {
        super.getResourceResolveEJB();
    }

    @Test
    @Override
    public void getResourceAsStreamResolveEJB() throws java.lang.Exception {
        super.getResourceAsStreamResolveEJB();
    }

    @Test
    @Override
    public void getResourceResolveEarLibEJB() throws java.lang.Exception {
        super.getResourceResolveEarLibEJB();
    }

    @Test
    @Override
    public void getResourceAsStreamResolveEarLibEJB() throws java.lang.Exception {
        super.getResourceAsStreamResolveEarLibEJB();
    }

}