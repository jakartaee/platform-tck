package com.sun.ts.tests.ejb30.tx.session.stateless.cm.covariant;

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
public class ClientTest extends com.sun.ts.tests.ejb30.tx.session.stateless.cm.covariant.Client {
    /**
     * EE10 Deployment Descriptors: tx_stateless_covariant: tx_stateless_covariant_ejb: jar.sun-ejb-jar.xml
     * tx_stateless_covariant_web: WEB-INF/web.xml
     *
     * Found Descriptors: Ejb:
     *
     * /com/sun/ts/tests/ejb30/tx/session/stateless/cm/covariant/tx_stateless_covariant_ejb.jar.sun-ejb-jar.xml War:
     *
     * /com/sun/ts/tests/ejb30/tx/session/stateless/cm/covariant/tx_stateless_covariant_web.xml Ear:
     *
     */
    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = "tx_stateless_covariant", order = 2)
    public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
        // the war with the correct archive name
        WebArchive tx_stateless_covariant_web = ShrinkWrap.create(WebArchive.class, "tx_stateless_covariant_web.war");

        // The class files
        tx_stateless_covariant_web.addClasses(
            com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet.class,
            com.sun.ts.tests.ejb30.common.covariant.TestServletBase.class,
            com.sun.ts.tests.ejb30.tx.session.stateless.cm.covariant.TestServlet.class,
            com.sun.ts.tests.servlet.common.util.Data.class);

        // The web.xml descriptor
        URL warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateless/cm/covariant/tx_stateless_covariant_web.xml");
        if (warResURL != null) {
            tx_stateless_covariant_web.addAsWebInfResource(warResURL, "web.xml");
        }

        // Call the archive processor
        archiveProcessor.processWebArchive(tx_stateless_covariant_web, Client.class, warResURL);


        // Ejb
        // the jar with the correct archive name
        JavaArchive tx_stateless_covariant_ejb = ShrinkWrap.create(JavaArchive.class, "tx_stateless_covariant_ejb.jar");

        // The class files
        tx_stateless_covariant_ejb.addClasses(
            com.sun.ts.tests.ejb30.tx.session.stateless.cm.covariant.FuzzyBean.class,
            com.sun.ts.tests.ejb30.common.covariant.FuzzyBeanBase.class);

        // The sun-ejb-jar.xml file
        URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateless/cm/covariant/tx_stateless_covariant_ejb.jar.sun-ejb-jar.xml");
        if (ejbResURL != null) {
            tx_stateless_covariant_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
        }
        // Call the archive processor
        archiveProcessor.processEjbArchive(tx_stateless_covariant_ejb, Client.class, ejbResURL);


        // Ear
        EnterpriseArchive tx_stateless_covariant_ear = ShrinkWrap.create(EnterpriseArchive.class, "tx_stateless_covariant.ear");

        // Any libraries added to the ear
        JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");

        // The class files
        shared_lib.addClasses(
            com.sun.ts.tests.ejb30.common.covariant.FuzzyCommonIF.class,
            com.sun.ts.tests.ejb30.common.covariant.FuzzyLocalIF.class,
            com.sun.ts.tests.ejb30.common.covariant.FuzzyRemoteIF.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class);

        tx_stateless_covariant_ear.addAsLibrary(shared_lib);

        // The component jars built by the package target
        tx_stateless_covariant_ear.addAsModule(tx_stateless_covariant_ejb);
        tx_stateless_covariant_ear.addAsModule(tx_stateless_covariant_web);

        // The application.xml descriptor
        URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateless/cm/covariant/application.xml");
        if (earResURL != null) {
            tx_stateless_covariant_ear.addAsManifestResource(earResURL, "application.xml");
        }

        // Call the archive processor
        archiveProcessor.processEarArchive(tx_stateless_covariant_ear, Client.class, earResURL);

        return tx_stateless_covariant_ear;
    }

    @Test
    @Override
    public void getMessage() throws java.lang.Exception {
        super.getMessage();
    }

    @Test
    @Override
    public void getMessages() throws java.lang.Exception {
        super.getMessages();
    }

    @Test
    @Override
    public void getNumbers() throws java.lang.Exception {
        super.getNumbers();
    }

}