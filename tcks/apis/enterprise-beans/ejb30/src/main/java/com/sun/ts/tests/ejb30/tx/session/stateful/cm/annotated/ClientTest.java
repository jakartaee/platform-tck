package com.sun.ts.tests.ejb30.tx.session.stateful.cm.annotated;

import com.sun.ts.lib.harness.Fault;

import java.net.URL;

import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.tests.common.base.EETest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.tx.session.stateful.cm.annotated.Client {
    /**
        EE10 Deployment Descriptors:
        ejb3_tx_stateful_cm_annotated: 
        ejb3_tx_stateful_cm_annotated_client: 
        ejb3_tx_stateful_cm_annotated_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb30/tx/session/stateful/cm/annotated/ejb3_tx_stateful_cm_annotated_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_tx_stateful_cm_annotated", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_tx_stateful_cm_annotated_client = ShrinkWrap.create(JavaArchive.class, "ejb3_tx_stateful_cm_annotated_client.jar");
            // The class files
            ejb3_tx_stateful_cm_annotated_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            Fault.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.tx.session.stateful.cm.annotated.Client.class,
            com.sun.ts.tests.ejb30.tx.common.session.cm.TestIF.class,
            EETest.class,
            SetupException.class,
            com.sun.ts.tests.ejb30.tx.common.session.cm.ClientBase.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateful/cm/annotated/ejb3_tx_stateful_cm_annotated_client.xml");
            if(resURL != null) {
              ejb3_tx_stateful_cm_annotated_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateful/cm/annotated/ejb3_tx_stateful_cm_annotated_client.jar.sun-application-client.xml");
            if(resURL != null) {
              ejb3_tx_stateful_cm_annotated_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            ejb3_tx_stateful_cm_annotated_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_tx_stateful_cm_annotated_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb3_tx_stateful_cm_annotated_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_tx_stateful_cm_annotated_ejb.jar");
            // The class files
            ejb3_tx_stateful_cm_annotated_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.tx.common.session.cm.LocalTxBeanBase.class,
                com.sun.ts.tests.ejb30.tx.common.session.cm.StatefulLocalTxBean.class,
                com.sun.ts.tests.ejb30.tx.common.session.cm.TxBeanBase0.class,
                com.sun.ts.tests.ejb30.tx.common.session.cm.LocalTxIF.class,
                com.sun.ts.tests.ejb30.tx.session.stateful.cm.annotated.TestBean.class,
                com.sun.ts.tests.ejb30.tx.common.session.cm.TestBeanBase.class,
                com.sun.ts.tests.ejb30.tx.common.session.cm.TxBeanBase.class,
                com.sun.ts.tests.ejb30.common.helper.Helper.class,
                com.sun.ts.tests.ejb30.tx.common.session.cm.TxIF.class,
                com.sun.ts.tests.ejb30.tx.common.session.cm.TestIF.class,
                com.sun.ts.tests.ejb30.tx.common.session.cm.LocalTestBeanBase.class,
                com.sun.ts.tests.ejb30.tx.common.session.cm.StatefulTxBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateful/cm/annotated/ejb3_tx_stateful_cm_annotated_ejb.xml");
            if(ejbResURL != null) {
              ejb3_tx_stateful_cm_annotated_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateful/cm/annotated/ejb3_tx_stateful_cm_annotated_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb3_tx_stateful_cm_annotated_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb3_tx_stateful_cm_annotated_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_tx_stateful_cm_annotated_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_tx_stateful_cm_annotated.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            ejb3_tx_stateful_cm_annotated_ear.addAsModule(ejb3_tx_stateful_cm_annotated_ejb);
            ejb3_tx_stateful_cm_annotated_ear.addAsModule(ejb3_tx_stateful_cm_annotated_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateful/cm/annotated/application.xml");
            if(earResURL != null) {
              ejb3_tx_stateful_cm_annotated_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/tx/session/stateful/cm/annotated/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_tx_stateful_cm_annotated_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_tx_stateful_cm_annotated_ear, Client.class, earResURL);
        return ejb3_tx_stateful_cm_annotated_ear;
        }

        @Test
        @Override
        public void mandatoryTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.mandatoryTest();
        }

        @Test
        @Override
        public void localMandatoryTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.localMandatoryTest();
        }

        @Test
        @Override
        public void neverTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.neverTest();
        }

        @Test
        @Override
        public void localNeverTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.localNeverTest();
        }

        @Test
        @Override
        public void localRequiresNewRemoveTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.localRequiresNewRemoveTest();
        }

        @Test
        @Override
        public void supportsTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.supportsTest();
        }

        @Test
        @Override
        public void localSupportsTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.localSupportsTest();
        }

        @Test
        @Override
        public void illegalGetSetRollbackOnlyNeverTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.illegalGetSetRollbackOnlyNeverTest();
        }

        @Test
        @Override
        public void localIllegalGetSetRollbackOnlyNeverTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.localIllegalGetSetRollbackOnlyNeverTest();
        }

        @Test
        @Override
        public void illegalGetSetRollbackOnlyNotSupportedTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.illegalGetSetRollbackOnlyNotSupportedTest();
        }

        @Test
        @Override
        public void localIllegalGetSetRollbackOnlyNotSupportedTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.localIllegalGetSetRollbackOnlyNotSupportedTest();
        }

        @Test
        @Override
        public void systemExceptionTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.systemExceptionTest();
        }


}