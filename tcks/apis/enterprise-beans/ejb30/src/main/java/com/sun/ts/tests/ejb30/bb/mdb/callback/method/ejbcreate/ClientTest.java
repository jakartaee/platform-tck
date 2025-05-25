package com.sun.ts.tests.ejb30.bb.mdb.callback.method.ejbcreate;

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
@Tag("ejb_mdb_optional")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.bb.mdb.callback.method.ejbcreate.Client {
    /**
        EE10 Deployment Descriptors:
        mdb_callback_method_ejbcreate: 
        mdb_callback_method_ejbcreate_client: jar.sun-application-client.xml
        mdb_callback_method_ejbcreate_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/bb/mdb/callback/method/ejbcreate/mdb_callback_method_ejbcreate_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/bb/mdb/callback/method/ejbcreate/mdb_callback_method_ejbcreate_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_callback_method_ejbcreate", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_callback_method_ejbcreate_client = ShrinkWrap.create(JavaArchive.class, "mdb_callback_method_ejbcreate_client.jar");
            // The class files
            mdb_callback_method_ejbcreate_client.addClasses(
            com.sun.ts.tests.jms.commonee.Client.class,
            com.sun.ts.tests.ejb30.common.callback.MDBClientBase.class,
            Fault.class,
            com.sun.ts.tests.ejb30.bb.mdb.callback.method.ejbcreate.Client.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.messaging.Constants.class,
            EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.common.messaging.ClientBase.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/callback/method/ejbcreate/mdb_callback_method_ejbcreate_client.xml");
            if(resURL != null) {
              mdb_callback_method_ejbcreate_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/callback/method/ejbcreate/mdb_callback_method_ejbcreate_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_callback_method_ejbcreate_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdb_callback_method_ejbcreate_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_callback_method_ejbcreate_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_callback_method_ejbcreate_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_callback_method_ejbcreate_ejb.jar");
            // The class files
            mdb_callback_method_ejbcreate_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.callback.CallbackIF.class,
                com.sun.ts.tests.ejb30.common.callback.MDBCallbackBeanBase.class,
                com.sun.ts.tests.ejb30.bb.mdb.callback.method.ejbcreate.CallbackBean0.class,
                com.sun.ts.tests.ejb30.common.callback.SharedCallbackBeanBase.class,
                com.sun.ts.tests.ejb30.common.callback.CallbackBeanBase.class,
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.ejb30.common.messaging.StatusReporter.class,
                com.sun.ts.tests.ejb30.common.messaging.Constants.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.bb.mdb.callback.method.ejbcreate.CallbackBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/callback/method/ejbcreate/mdb_callback_method_ejbcreate_ejb.xml");
            if(ejbResURL != null) {
              mdb_callback_method_ejbcreate_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/callback/method/ejbcreate/mdb_callback_method_ejbcreate_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_callback_method_ejbcreate_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_callback_method_ejbcreate_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_callback_method_ejbcreate_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_callback_method_ejbcreate.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_callback_method_ejbcreate_ear.addAsModule(mdb_callback_method_ejbcreate_ejb);
            mdb_callback_method_ejbcreate_ear.addAsModule(mdb_callback_method_ejbcreate_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/callback/method/ejbcreate/application.xml");
            if(earResURL != null) {
              mdb_callback_method_ejbcreate_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/callback/method/ejbcreate/application.ear.sun-application.xml");
            if(earResURL != null) {
              mdb_callback_method_ejbcreate_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(mdb_callback_method_ejbcreate_ear, Client.class, earResURL);
        return mdb_callback_method_ejbcreate_ear;
        }

        @Test
        @Override
        public void isPostConstructCalledTest() throws java.lang.Exception {
            super.isPostConstructCalledTest();
        }

        @Test
        @Override
        public void isInjectionDoneTest() throws java.lang.Exception {
            super.isInjectionDoneTest();
        }


}