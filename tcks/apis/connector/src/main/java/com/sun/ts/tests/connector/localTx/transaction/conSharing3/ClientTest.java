package com.sun.ts.tests.connector.localTx.transaction.conSharing3;

import com.sun.ts.tests.connector.localTx.transaction.conSharing3.Client;
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
@Tag("connector")
@Tag("connector_jta_optional")
@Tag("platform")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.connector.localTx.transaction.conSharing3.Client {
    /**
        EE10 Deployment Descriptors:
        ejb_txTran_conSharing3: 
        ejb_txTran_conSharing3_client: META-INF/application-client.xml,jar.sun-application-client.xml
        ejb_txTran_conSharing3_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/connector/localTx/transaction/conSharing3/ejb_txTran_conSharing3_client.xml
        /com/sun/ts/tests/connector/localTx/transaction/conSharing3/ejb_txTran_conSharing3_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/connector/localTx/transaction/conSharing3/ejb_txTran_conSharing3_ejb.xml
        /com/sun/ts/tests/connector/localTx/transaction/conSharing3/ejb_txTran_conSharing3_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb_txTran_conSharing3", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb_txTran_conSharing3_client = ShrinkWrap.create(JavaArchive.class, "ejb_txTran_conSharing3_client.jar");
            // The class files
            ejb_txTran_conSharing3_client.addClasses(
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.connector.localTx.transaction.conSharing3.Client.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.connector.localTx.transaction.conSharing3.TestBean.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/connector/localTx/transaction/conSharing3/ejb_txTran_conSharing3_client.xml");
            ejb_txTran_conSharing3_client.addAsManifestResource(resURL, "application-client.xml");
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/connector/localTx/transaction/conSharing3/ejb_txTran_conSharing3_client.jar.sun-application-client.xml");
            ejb_txTran_conSharing3_client.addAsManifestResource(resURL, "sun-application-client.xml");
            ejb_txTran_conSharing3_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.connector.localTx.transaction.conSharing3.Client\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb_txTran_conSharing3_client, Client.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive ejb_txTran_conSharing3_ejb = ShrinkWrap.create(JavaArchive.class, "ejb_txTran_conSharing3_ejb.jar");
            // The class files
            ejb_txTran_conSharing3_ejb.addClasses(
                com.sun.ts.tests.connector.localTx.transaction.conSharing3.BeanA.class,
                com.sun.ts.tests.connector.localTx.transaction.conSharing3.TestBeanEJB.class,
                com.sun.ts.tests.connector.localTx.transaction.conSharing3.BeanBEJB.class,
                com.sun.ts.tests.connector.localTx.transaction.conSharing3.BeanAEJB.class,
                com.sun.ts.tests.connector.localTx.transaction.conSharing3.BeanB.class,
                com.sun.ts.tests.connector.util.DBSupport.class,
                com.sun.ts.tests.connector.localTx.transaction.conSharing3.TestBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client.class.getResource("/com/sun/ts/tests/connector/localTx/transaction/conSharing3/ejb_txTran_conSharing3_ejb.xml");
            ejb_txTran_conSharing3_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client.class.getResource("/com/sun/ts/tests/connector/localTx/transaction/conSharing3/ejb_txTran_conSharing3_ejb.jar.sun-ejb-jar.xml");
            ejb_txTran_conSharing3_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb_txTran_conSharing3_ejb, Client.class, ejbResURL1);

        // Ear
            EnterpriseArchive ejb_txTran_conSharing3_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb_txTran_conSharing3.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            ejb_txTran_conSharing3_ear.addAsModule(ejb_txTran_conSharing3_ejb);
            ejb_txTran_conSharing3_ear.addAsModule(ejb_txTran_conSharing3_client);


            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb_txTran_conSharing3_ear, Client.class, earResURL);
        return ejb_txTran_conSharing3_ear;
        }

        @Test
        @Override
        public void test1() throws java.lang.Exception {
            super.test1();
        }


}