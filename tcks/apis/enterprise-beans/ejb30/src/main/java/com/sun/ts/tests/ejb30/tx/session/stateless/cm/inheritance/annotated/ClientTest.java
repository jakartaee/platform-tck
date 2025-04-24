package com.sun.ts.tests.ejb30.tx.session.stateless.cm.inheritance.annotated;

import com.sun.ts.tests.ejb30.tx.session.stateless.cm.inheritance.annotated.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.tx.session.stateless.cm.inheritance.annotated.Client {
    /**
        EE10 Deployment Descriptors:
        tx_stateless_inheritance_annotated: 
        tx_stateless_inheritance_annotated_ejb: jar.sun-ejb-jar.xml
        tx_stateless_inheritance_annotated_web: WEB-INF/web.xml

        Found Descriptors:
        Ejb:

        /com/sun/ts/tests/ejb30/tx/session/stateless/cm/inheritance/annotated/tx_stateless_inheritance_annotated_ejb.jar.sun-ejb-jar.xml
        War:

        /com/sun/ts/tests/ejb30/tx/session/stateless/cm/inheritance/annotated/tx_stateless_inheritance_annotated_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = "tx_stateless_inheritance_annotated", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive tx_stateless_inheritance_annotated_web = ShrinkWrap.create(WebArchive.class, "tx_stateless_inheritance_annotated_web.war");
            // The class files
            tx_stateless_inheritance_annotated_web.addClasses(
            com.sun.ts.tests.ejb30.tx.session.stateless.cm.inheritance.annotated.TestServlet.class,
            com.sun.ts.tests.ejb30.tx.common.session.inheritance.TestLogic.class,
            com.sun.ts.tests.ejb30.tx.common.session.inheritance.TestServletBase.class,
            com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet.class,
            com.sun.ts.tests.servlet.common.util.Data.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateless/cm/inheritance/annotated/tx_stateless_inheritance_annotated_web.xml");
            if(warResURL != null) {
              tx_stateless_inheritance_annotated_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateless/cm/inheritance/annotated/tx_stateless_inheritance_annotated_web.war.sun-web.xml");
            if(warResURL != null) {
              tx_stateless_inheritance_annotated_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

           // Call the archive processor
           archiveProcessor.processWebArchive(tx_stateless_inheritance_annotated_web, Client.class, warResURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive tx_stateless_inheritance_annotated_ejb = ShrinkWrap.create(JavaArchive.class, "tx_stateless_inheritance_annotated_ejb.jar");
            // The class files
            tx_stateless_inheritance_annotated_ejb.addClasses(
                com.sun.ts.tests.ejb30.tx.common.session.inheritance.TxBeanBase.class,
                com.sun.ts.tests.ejb30.tx.common.session.inheritance.DBeanBase.class,
                com.sun.ts.tests.ejb30.tx.session.stateless.cm.inheritance.annotated.EBean.class,
                com.sun.ts.tests.ejb30.tx.common.session.inheritance.CBeanBase.class,
                com.sun.ts.tests.ejb30.tx.session.stateless.cm.inheritance.annotated.DBean.class,
                com.sun.ts.tests.ejb30.tx.common.session.inheritance.EBeanBase.class,
                com.sun.ts.tests.ejb30.tx.session.stateless.cm.inheritance.annotated.FBean.class,
                com.sun.ts.tests.ejb30.tx.common.session.inheritance.ABeanBase.class,
                com.sun.ts.tests.ejb30.tx.common.session.inheritance.FBeanBase.class,
                com.sun.ts.tests.ejb30.tx.common.session.inheritance.BBeanBase.class,
                com.sun.ts.tests.ejb30.tx.session.stateless.cm.inheritance.annotated.ABean.class,
                com.sun.ts.tests.ejb30.tx.session.stateless.cm.inheritance.annotated.BBean.class,
                com.sun.ts.tests.ejb30.tx.session.stateless.cm.inheritance.annotated.CBean.class
            );

            // The sun-ejb-jar.xml file
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateless/cm/inheritance/annotated/tx_stateless_inheritance_annotated_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              tx_stateless_inheritance_annotated_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(tx_stateless_inheritance_annotated_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive tx_stateless_inheritance_annotated_ear = ShrinkWrap.create(EnterpriseArchive.class, "tx_stateless_inheritance_annotated.ear");

            // Any libraries added to the ear
                URL libURL;
                JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");
                    // The class files
                    shared_lib.addClasses(
                        com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                        com.sun.ts.tests.ejb30.common.helper.Helper.class,
                        com.sun.ts.tests.ejb30.tx.common.session.inheritance.TxRemoteIF.class,
                        com.sun.ts.tests.ejb30.tx.common.session.inheritance.TxLocalIF.class,
                        com.sun.ts.tests.ejb30.tx.common.session.inheritance.TxCommonIF.class,
                        com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class
                    );


                tx_stateless_inheritance_annotated_ear.addAsLibrary(shared_lib);


            // The component jars built by the package target
            tx_stateless_inheritance_annotated_ear.addAsModule(tx_stateless_inheritance_annotated_ejb);
            tx_stateless_inheritance_annotated_ear.addAsModule(tx_stateless_inheritance_annotated_web);


            // The sun-application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateless/cm/inheritance/annotated/tx_stateless_inheritance_annotated.ear.sun-application.xml");
            if(earResURL != null) {
              tx_stateless_inheritance_annotated_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(tx_stateless_inheritance_annotated_ear, Client.class, earResURL);
        return tx_stateless_inheritance_annotated_ear;
        }

        @Test
        @Override
        public void aBeanRemote() throws java.lang.Exception {
            super.aBeanRemote();
        }

        @Test
        @Override
        public void bBeanRemote() throws java.lang.Exception {
            super.bBeanRemote();
        }

        @Test
        @Override
        public void cBeanRemote() throws java.lang.Exception {
            super.cBeanRemote();
        }

        @Test
        @Override
        public void dBeanRemote() throws java.lang.Exception {
            super.dBeanRemote();
        }

        @Test
        @Override
        public void eBeanRemote() throws java.lang.Exception {
            super.eBeanRemote();
        }

        @Test
        @Override
        public void aBeanLocal() throws java.lang.Exception {
            super.aBeanLocal();
        }

        @Test
        @Override
        public void bBeanLocal() throws java.lang.Exception {
            super.bBeanLocal();
        }

        @Test
        @Override
        public void cBeanLocal() throws java.lang.Exception {
            super.cBeanLocal();
        }

        @Test
        @Override
        public void dBeanLocal() throws java.lang.Exception {
            super.dBeanLocal();
        }

        @Test
        @Override
        public void eBeanLocal() throws java.lang.Exception {
            super.eBeanLocal();
        }

        @Test
        @Override
        public void overloadedMethodsTxLocal() throws java.lang.Exception {
            super.overloadedMethodsTxLocal();
        }

        @Test
        @Override
        public void overloadedMethodsTxRemote() throws java.lang.Exception {
            super.overloadedMethodsTxRemote();
        }


}