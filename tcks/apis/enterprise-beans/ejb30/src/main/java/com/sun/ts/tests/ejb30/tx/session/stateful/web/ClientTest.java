package com.sun.ts.tests.ejb30.tx.session.stateful.web;

import com.sun.ts.tests.ejb30.tx.session.stateful.web.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.tx.session.stateful.web.Client {
    /**
        EE10 Deployment Descriptors:
        tx_stateful_web: 
        tx_stateful_web_ejb: jar.sun-ejb-jar.xml
        tx_stateful_web_web: WEB-INF/web.xml

        Found Descriptors:
        Ejb:

        /com/sun/ts/tests/ejb30/tx/session/stateful/web/tx_stateful_web_ejb.jar.sun-ejb-jar.xml
        War:

        /com/sun/ts/tests/ejb30/tx/session/stateful/web/tx_stateful_web_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = "tx_stateful_web", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive tx_stateful_web_web = ShrinkWrap.create(WebArchive.class, "tx_stateful_web_web.war");
            // The class files
            tx_stateful_web_web.addClasses(
            com.sun.ts.tests.ejb30.tx.common.web.RemoteIF.class,
            com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet.class,
            com.sun.ts.tests.ejb30.tx.common.web.FooServlet.class,
            com.sun.ts.tests.ejb30.tx.common.web.LocalIF.class,
            com.sun.ts.tests.servlet.common.util.Data.class,
            com.sun.ts.tests.ejb30.tx.common.web.Constants.class,
            com.sun.ts.tests.ejb30.tx.common.web.TxServlet.class,
            com.sun.ts.tests.ejb30.tx.common.web.TestServlet.class
            );

            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateful/web/tx_stateful_web_web.xml");
            if(warResURL != null) {
              tx_stateful_web_web.addAsWebInfResource(warResURL, "web.xml");
            }

            // Any libraries added to the war


           // Call the archive processor
           archiveProcessor.processWebArchive(tx_stateful_web_web, Client.class, warResURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive tx_stateful_web_ejb = ShrinkWrap.create(JavaArchive.class, "tx_stateful_web_ejb.jar");
            // The class files
            tx_stateful_web_ejb.addClasses(
                com.sun.ts.tests.ejb30.tx.common.web.RemoteIF.class,
                com.sun.ts.tests.ejb30.tx.common.web.LocalIF.class,
                com.sun.ts.tests.ejb30.tx.session.stateful.web.StatefulTestBean.class,
                com.sun.ts.tests.ejb30.tx.common.web.Constants.class
            );
            // The sun-ejb-jar.xml file
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateful/web/tx_stateful_web_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              tx_stateful_web_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(tx_stateful_web_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive tx_stateful_web_ear = ShrinkWrap.create(EnterpriseArchive.class, "tx_stateful_web.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            tx_stateful_web_ear.addAsModule(tx_stateful_web_ejb);
            tx_stateful_web_ear.addAsModule(tx_stateful_web_web);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateful/web/application.xml");
            if(earResURL != null) {
              tx_stateful_web_ear.addAsManifestResource(earResURL, "application.xml");
            }

            // Call the archive processor
            archiveProcessor.processEarArchive(tx_stateful_web_ear, Client.class, earResURL);
        return tx_stateful_web_ear;
        }

        @Test
        @Override
        public void servletRemoteCmt() throws java.lang.Exception {
            super.servletRemoteCmt();
        }

        @Test
        @Override
        public void servletLocalCmt() throws java.lang.Exception {
            super.servletLocalCmt();
        }

        @Test
        @Override
        public void servletRemoteCmtRequiresNew() throws java.lang.Exception {
            super.servletRemoteCmtRequiresNew();
        }

        @Test
        @Override
        public void servletLocalCmtRequiresNew() throws java.lang.Exception {
            super.servletLocalCmtRequiresNew();
        }

        @Test
        @Override
        public void servletRemoteCmtMandatory() throws java.lang.Exception {
            super.servletRemoteCmtMandatory();
        }

        @Test
        @Override
        public void servletLocalCmtMandatory() throws java.lang.Exception {
            super.servletLocalCmtMandatory();
        }

        @Test
        @Override
        public void servletRemoteCmtNever() throws java.lang.Exception {
            super.servletRemoteCmtNever();
        }

        @Test
        @Override
        public void servletLocalCmtNever() throws java.lang.Exception {
            super.servletLocalCmtNever();
        }


}