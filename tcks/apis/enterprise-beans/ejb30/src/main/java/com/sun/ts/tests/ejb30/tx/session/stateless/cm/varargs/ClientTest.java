package com.sun.ts.tests.ejb30.tx.session.stateless.cm.varargs;

import com.sun.ts.tests.ejb30.tx.session.stateless.cm.varargs.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.tx.session.stateless.cm.varargs.Client {
    /**
        EE10 Deployment Descriptors:
        tx_stateless_varargs: 
        tx_stateless_varargs_ejb: jar.sun-ejb-jar.xml
        tx_stateless_varargs_web: WEB-INF/web.xml

        Found Descriptors:
        Ejb:

        /com/sun/ts/tests/ejb30/tx/session/stateless/cm/varargs/tx_stateless_varargs_ejb.jar.sun-ejb-jar.xml
        War:

        /com/sun/ts/tests/ejb30/tx/session/stateless/cm/varargs/tx_stateless_varargs_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = "tx_stateless_varargs", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive tx_stateless_varargs_web = ShrinkWrap.create(WebArchive.class, "tx_stateless_varargs_web.war");
            // The class files
            tx_stateless_varargs_web.addClasses(
            com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet.class,
            com.sun.ts.tests.ejb30.tx.session.stateless.cm.varargs.TestServlet.class,
            com.sun.ts.tests.servlet.common.util.Data.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateless/cm/varargs/tx_stateless_varargs_web.xml");
            if(warResURL != null) {
              tx_stateless_varargs_web.addAsWebInfResource(warResURL, "web.xml");
            }

            // Any libraries added to the war

            // Web content

           // Call the archive processor
           archiveProcessor.processWebArchive(tx_stateless_varargs_web, Client.class, warResURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive tx_stateless_varargs_ejb = ShrinkWrap.create(JavaArchive.class, "tx_stateless_varargs_ejb.jar");
            // The class files
            tx_stateless_varargs_ejb.addClasses(
                com.sun.ts.tests.ejb30.tx.session.stateless.cm.varargs.VarargsBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateless/cm/varargs/tx_stateless_varargs_ejb.xml");
            if(ejbResURL != null) {
              tx_stateless_varargs_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(tx_stateless_varargs_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive tx_stateless_varargs_ear = ShrinkWrap.create(EnterpriseArchive.class, "tx_stateless_varargs.ear");

            // Any libraries added to the ear
                URL libURL;
                JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");
                    // The class files
                    shared_lib.addClasses(
                        com.sun.ts.tests.ejb30.tx.session.stateless.cm.varargs.VarargsRemoteIF.class,
                        com.sun.ts.tests.ejb30.tx.session.stateless.cm.varargs.VarargsCommonIF.class,
                        com.sun.ts.tests.ejb30.tx.session.stateless.cm.varargs.VarargsLocalIF.class
                    );


                tx_stateless_varargs_ear.addAsLibrary(shared_lib);


            // The component jars built by the package target
            tx_stateless_varargs_ear.addAsModule(tx_stateless_varargs_ejb);
            tx_stateless_varargs_ear.addAsModule(tx_stateless_varargs_web);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateless/cm/varargs/application.xml");
            if(earResURL != null) {
              tx_stateless_varargs_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateless/cm/varargs/application.ear.sun-application.xml");
            if(earResURL != null) {
              tx_stateless_varargs_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(tx_stateless_varargs_ear, Client.class, earResURL);
        return tx_stateless_varargs_ear;
        }

        @Test
        @Override
        public void formatLocal() throws java.lang.Exception {
            super.formatLocal();
        }

        @Test
        @Override
        public void formatRemote() throws java.lang.Exception {
            super.formatRemote();
        }

        @Test
        @Override
        public void addLocal() throws java.lang.Exception {
            super.addLocal();
        }

        @Test
        @Override
        public void addRemote() throws java.lang.Exception {
            super.addRemote();
        }

        @Test
        @Override
        public void listDogsLocal() throws java.lang.Exception {
            super.listDogsLocal();
        }

        @Test
        @Override
        public void listDogsRemote() throws java.lang.Exception {
            super.listDogsRemote();
        }


}