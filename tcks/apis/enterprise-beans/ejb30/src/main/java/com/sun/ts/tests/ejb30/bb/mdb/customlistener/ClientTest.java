package com.sun.ts.tests.ejb30.bb.mdb.customlistener;

import com.sun.ts.tests.ejb30.bb.mdb.customlistener.Client;
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
@Tag("ejb_mdb_optional")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.bb.mdb.customlistener.Client {
    /**
        EE10 Deployment Descriptors:
        mdb_custom_listener: 
        mdb_custom_listener_client: jar.sun-application-client.xml
        mdb_custom_listener_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/bb/mdb/customlistener/mdb_custom_listener_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/bb/mdb/customlistener/mdb_custom_listener_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_custom_listener", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_custom_listener_client = ShrinkWrap.create(JavaArchive.class, "mdb_custom_listener_client.jar");
            // The class files
            mdb_custom_listener_client.addClasses(
            com.sun.ts.tests.jms.commonee.Client.class,
            com.sun.ts.tests.ejb30.common.callback.MDBClientBase.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.messaging.Constants.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.common.calc.CalculatorException.class,
            com.sun.ts.tests.ejb30.common.messaging.ClientBase.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb30.bb.mdb.customlistener.Client.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/customlistener/mdb_custom_listener_client.xml");
            if(resURL != null) {
              mdb_custom_listener_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/customlistener/mdb_custom_listener_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_custom_listener_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdb_custom_listener_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_custom_listener_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_custom_listener_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_custom_listener_ejb.jar");
            // The class files
            mdb_custom_listener_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.callback.CallbackIF.class,
                com.sun.ts.tests.ejb30.common.callback.SharedCallbackBeanBase.class,
                com.sun.ts.tests.ejb30.common.callback.CallbackBeanBase.class,
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.ejb30.common.messaging.StatusReporter.class,
                com.sun.ts.tests.ejb30.common.messaging.Constants.class,
                com.sun.ts.tests.ejb30.bb.mdb.customlistener.MDBean.class,
                com.sun.ts.tests.common.connector.util.TSMessageListenerInterface.class,
                com.sun.ts.tests.common.connector.util.AppException.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/customlistener/mdb_custom_listener_ejb.xml");
            if(ejbResURL != null) {
              mdb_custom_listener_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/customlistener/mdb_custom_listener_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_custom_listener_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_custom_listener_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_custom_listener_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_custom_listener.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_custom_listener_ear.addAsModule(mdb_custom_listener_ejb);
            mdb_custom_listener_ear.addAsModule(mdb_custom_listener_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/customlistener/application.xml");
            if(earResURL != null) {
              mdb_custom_listener_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/customlistener/application.ear.sun-application.xml");
            if(earResURL != null) {
              mdb_custom_listener_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(mdb_custom_listener_ear, Client.class, earResURL);
        return mdb_custom_listener_ear;
        }

        @Test
        @Override
        public void isPostConstructCalledTest() throws java.lang.Exception {
            super.isPostConstructCalledTest();
        }


}