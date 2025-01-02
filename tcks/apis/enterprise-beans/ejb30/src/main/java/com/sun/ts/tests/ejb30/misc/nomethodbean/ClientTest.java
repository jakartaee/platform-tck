package com.sun.ts.tests.ejb30.misc.nomethodbean;

import com.sun.ts.tests.ejb30.misc.nomethodbean.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.misc.nomethodbean.Client {
    /**
     * EE10 Deployment Descriptors: ejb3_misc_nomethodbean: ejb3_misc_nomethodbean_ejb: jar.sun-ejb-jar.xml
     * ejb3_misc_nomethodbean_web:
     *
     * Found Descriptors: Ejb:
     *
     * /com/sun/ts/tests/ejb30/misc/nomethodbean/ejb3_misc_nomethodbean_ejb.jar.sun-ejb-jar.xml War:
     *
     * Ear:
     *
     */
    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = "ejb3_misc_nomethodbean", order = 2)
    public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
        // the war with the correct archive name
        WebArchive ejb3_misc_nomethodbean_web = ShrinkWrap.create(WebArchive.class, "ejb3_misc_nomethodbean_web.war");

        // The class files
        ejb3_misc_nomethodbean_web.addClasses(
            com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet.class,
            com.sun.ts.tests.ejb30.misc.nomethodbean.TestServlet.class, com.sun.ts.tests.servlet.common.util.Data.class);

        // Call the archive processor
        archiveProcessor.processWebArchive(ejb3_misc_nomethodbean_web, Client.class, null);


        // Ejb
        // the jar with the correct archive name
        JavaArchive ejb3_misc_nomethodbean_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_misc_nomethodbean_ejb.jar");

        // The class files
        ejb3_misc_nomethodbean_ejb.addClasses(
            com.sun.ts.tests.ejb30.misc.nomethodbean.NoMethodStatelessBean.class,
            com.sun.ts.tests.ejb30.misc.nomethodbean.NoMethodStatefulBean.class);

        // The sun-ejb-jar.xml file
        URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/nomethodbean/ejb3_misc_nomethodbean_ejb.jar.sun-ejb-jar.xml");
        if (ejbResURL != null) {
            ejb3_misc_nomethodbean_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
        }

        // Call the archive processor
        archiveProcessor.processEjbArchive(ejb3_misc_nomethodbean_ejb, Client.class, ejbResURL);


        // Ear
        EnterpriseArchive ejb3_misc_nomethodbean_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_misc_nomethodbean.ear");

        // Any libraries added to the ear
        JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");

        // The class files
        shared_lib.addClasses(
            com.sun.ts.tests.ejb30.misc.nomethodbean.NoMethodLocalIF.class,
            com.sun.ts.tests.ejb30.misc.nomethodbean.NoMethodRemoteIF.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class);

        ejb3_misc_nomethodbean_ear.addAsLibrary(shared_lib);

        // The component jars built by the package target
        ejb3_misc_nomethodbean_ear.addAsModule(ejb3_misc_nomethodbean_ejb);
        ejb3_misc_nomethodbean_ear.addAsModule(ejb3_misc_nomethodbean_web);

        // The application.xml descriptor
        URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/nomethodbean/application.xml");
        if (earResURL != null) {
            ejb3_misc_nomethodbean_ear.addAsManifestResource(earResURL, "application.xml");
        }

        // Call the archive processor
        archiveProcessor.processEarArchive(ejb3_misc_nomethodbean_ear, Client.class, earResURL);

        return ejb3_misc_nomethodbean_ear;
    }

    @Test
    @Override
    public void noMethodStateless() throws java.lang.Exception {
        super.noMethodStateless();
    }

    @Test
    @Override
    public void noMethodStateful() throws java.lang.Exception {
        super.noMethodStateful();
    }

}