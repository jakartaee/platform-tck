package com.sun.ts.tests.ejb30.bb.mdb.listenerintf.implementing.externalizable;

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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.mdb.listenerintf.implementing.externalizable.Client {
    /**
        EE10 Deployment Descriptors:
        mdb_listenerintf_externalizable: 
        mdb_listenerintf_externalizable_client: jar.sun-application-client.xml
        mdb_listenerintf_externalizable_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/bb/mdb/listenerintf/implementing/externalizable/mdb_listenerintf_externalizable_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/bb/mdb/listenerintf/implementing/externalizable/mdb_listenerintf_externalizable_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_listenerintf_externalizable", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_listenerintf_externalizable_client = ShrinkWrap.create(JavaArchive.class, "mdb_listenerintf_externalizable_client.jar");
            // The class files
            mdb_listenerintf_externalizable_client.addClasses(
            com.sun.ts.tests.jms.commonee.Client.class,
            Fault.class,
            com.sun.ts.tests.ejb30.common.messaging.Constants.class,
            EETest.class,
            com.sun.ts.tests.ejb30.bb.mdb.listenerintf.implementing.externalizable.Client.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.common.messaging.ClientBase.class,
            SetupException.class,
            com.sun.ts.tests.ejb30.bb.mdb.dest.common.ClientBase.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/listenerintf/implementing/externalizable/mdb_listenerintf_externalizable_client.xml");
            if(resURL != null) {
              mdb_listenerintf_externalizable_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/listenerintf/implementing/externalizable/mdb_listenerintf_externalizable_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_listenerintf_externalizable_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdb_listenerintf_externalizable_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_listenerintf_externalizable_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_listenerintf_externalizable_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_listenerintf_externalizable_ejb.jar");
            // The class files
            mdb_listenerintf_externalizable_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.ejb30.common.messaging.StatusReporter.class,
                com.sun.ts.tests.ejb30.common.messaging.Constants.class,
                com.sun.ts.tests.ejb30.bb.mdb.dest.common.DestBeanBase.class,
                com.sun.ts.tests.ejb30.bb.mdb.listenerintf.implementing.externalizable.DestBean.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/listenerintf/implementing/externalizable/mdb_listenerintf_externalizable_ejb.xml");
            if(ejbResURL != null) {
              mdb_listenerintf_externalizable_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/listenerintf/implementing/externalizable/mdb_listenerintf_externalizable_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_listenerintf_externalizable_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_listenerintf_externalizable_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_listenerintf_externalizable_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_listenerintf_externalizable.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_listenerintf_externalizable_ear.addAsModule(mdb_listenerintf_externalizable_ejb);
            mdb_listenerintf_externalizable_ear.addAsModule(mdb_listenerintf_externalizable_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/listenerintf/implementing/externalizable/application.xml");
            if(earResURL != null) {
              mdb_listenerintf_externalizable_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/listenerintf/implementing/externalizable/application.ear.sun-application.xml");
            if(earResURL != null) {
              mdb_listenerintf_externalizable_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(mdb_listenerintf_externalizable_ear, Client.class, earResURL);
        return mdb_listenerintf_externalizable_ear;
        }

        @Test
        @Override
        public void test1() throws java.lang.Exception {
            super.test1();
        }


}