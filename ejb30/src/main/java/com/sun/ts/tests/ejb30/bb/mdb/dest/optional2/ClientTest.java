package com.sun.ts.tests.ejb30.bb.mdb.dest.optional2;

import com.sun.ts.tests.ejb30.bb.mdb.dest.optional2.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.mdb.dest.optional2.Client {
    /**
        EE10 Deployment Descriptors:
        mdb_dest_optional2: 
        mdb_dest_optional2_client: jar.sun-application-client.xml
        mdb_dest_optional2_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/bb/mdb/dest/optional2/mdb_dest_optional2_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/bb/mdb/dest/optional2/mdb_dest_optional2_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_dest_optional2", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_dest_optional2_client = ShrinkWrap.create(JavaArchive.class, "mdb_dest_optional2_client.jar");
            // The class files
            mdb_dest_optional2_client.addClasses(
            com.sun.ts.tests.jms.commonee.Client.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.common.messaging.Constants.class,
            com.sun.ts.tests.ejb30.bb.mdb.dest.optional2.Client.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.common.messaging.ClientBase.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb30.bb.mdb.dest.common.ClientBase.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("com/sun/ts/tests/ejb30/bb/mdb/dest/optional2/mdb_dest_optional2_client.xml");
            if(resURL != null) {
              mdb_dest_optional2_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/optional2/mdb_dest_optional2_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_dest_optional2_client.addAsManifestResource(resURL, "application-client.xml");
            }
            mdb_dest_optional2_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_dest_optional2_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mdb_dest_optional2_ejb = ShrinkWrap.create(JavaArchive.class, "mdb_dest_optional2_ejb.jar");
            // The class files
            mdb_dest_optional2_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.ejb30.common.messaging.StatusReporter.class,
                com.sun.ts.tests.ejb30.common.messaging.Constants.class,
                com.sun.ts.tests.ejb30.bb.mdb.dest.common.DestBeanBase.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.bb.mdb.dest.common.DestBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/optional2/mdb_dest_optional2_ejb.xml");
            if(ejbResURL != null) {
              mdb_dest_optional2_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/optional2/mdb_dest_optional2_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mdb_dest_optional2_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdb_dest_optional2_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_dest_optional2_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_dest_optional2.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_dest_optional2_ear.addAsModule(mdb_dest_optional2_ejb);
            mdb_dest_optional2_ear.addAsModule(mdb_dest_optional2_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/optional2/application.xml");
            if(earResURL != null) {
              mdb_dest_optional2_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/optional2/application.ear.sun-application.xml");
            if(earResURL != null) {
              mdb_dest_optional2_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(mdb_dest_optional2_ear, Client.class, earResURL);
        return mdb_dest_optional2_ear;
        }

        @Test
        @Override
        public void test1() throws java.lang.Exception {
            super.test1();
        }


}