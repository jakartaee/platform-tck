package com.sun.ts.tests.ejb30.bb.mdb.listenerintf.implementing.serializable;

import com.sun.ts.tests.ejb30.bb.mdb.listenerintf.implementing.serializable.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.mdb.listenerintf.implementing.serializable.Client {
    /**
        EE10 Deployment Descriptors:
        mdb_listenerintf_serializable: 
        mdb_listenerintf_serializable_client: jar.sun-application-client.xml
        mdb_listenerintf_serializable_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/bb/mdb/listenerintf/implementing/serializable/mdb_listenerintf_serializable_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/bb/mdb/listenerintf/implementing/serializable/mdb_listenerintf_serializable_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_listenerintf_serializable", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_listenerintf_serializable_client = ShrinkWrap.create(JavaArchive.class, "mdb_listenerintf_serializable_client.jar");
            // The class files
            mdb_listenerintf_serializable_client.addClasses(
            com.sun.ts.tests.jms.commonee.Client.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.bb.mdb.listenerintf.implementing.serializable.Client.class,
            com.sun.ts.tests.ejb30.common.messaging.Constants.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.common.messaging.ClientBase.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb30.bb.mdb.dest.common.ClientBase.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/listenerintf/implementing/serializable/mdb_listenerintf_serializable_client.xml");
            if(resURL != null) {
              mdb_listenerintf_serializable_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/listenerintf/implementing/serializable/mdb_listenerintf_serializable_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_listenerintf_serializable_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdb_listenerintf_serializable_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_listenerintf_serializable_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_listenerintf_serializable_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_listenerintf_serializable_ejb.jar");
            // The class files
            mdb_listenerintf_serializable_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.ejb30.bb.mdb.listenerintf.implementing.serializable.DestBean.class,
                com.sun.ts.tests.ejb30.common.messaging.StatusReporter.class,
                com.sun.ts.tests.ejb30.common.messaging.Constants.class,
                com.sun.ts.tests.ejb30.bb.mdb.dest.common.DestBeanBase.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/listenerintf/implementing/serializable/mdb_listenerintf_serializable_ejb.xml");
            if(ejbResURL != null) {
              mdb_listenerintf_serializable_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/listenerintf/implementing/serializable/mdb_listenerintf_serializable_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_listenerintf_serializable_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_listenerintf_serializable_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_listenerintf_serializable_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_listenerintf_serializable.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_listenerintf_serializable_ear.addAsModule(mdb_listenerintf_serializable_ejb);
            mdb_listenerintf_serializable_ear.addAsModule(mdb_listenerintf_serializable_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/listenerintf/implementing/serializable/application.xml");
            if(earResURL != null) {
              mdb_listenerintf_serializable_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/listenerintf/implementing/serializable/application.ear.sun-application.xml");
            if(earResURL != null) {
              mdb_listenerintf_serializable_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(mdb_listenerintf_serializable_ear, Client.class, earResURL);
        return mdb_listenerintf_serializable_ear;
        }

        @Test
        @Override
        public void test1() throws java.lang.Exception {
            super.test1();
        }


}