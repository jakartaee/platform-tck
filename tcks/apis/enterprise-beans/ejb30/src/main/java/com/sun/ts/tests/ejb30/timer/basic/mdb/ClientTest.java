package com.sun.ts.tests.ejb30.timer.basic.mdb;

import com.sun.ts.tests.ejb30.timer.basic.mdb.Client;
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
public class ClientTest extends com.sun.ts.tests.ejb30.timer.basic.mdb.Client {
    /**
        EE10 Deployment Descriptors:
        ejb3_timer_basic_mdb: 
        ejb3_timer_basic_mdb_client: jar.sun-application-client.xml
        ejb3_timer_basic_mdb_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/timer/basic/mdb/ejb3_timer_basic_mdb_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/timer/basic/mdb/ejb3_timer_basic_mdb_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_timer_basic_mdb", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_timer_basic_mdb_client = ShrinkWrap.create(JavaArchive.class, "ejb3_timer_basic_mdb_client.jar");
            // The class files
            ejb3_timer_basic_mdb_client.addClasses(
            com.sun.ts.tests.jms.commonee.Client.class,
            com.sun.ts.tests.ejb30.timer.common.MDBClientBase.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.messaging.ClientBase.class,
            com.sun.ts.tests.ejb30.timer.basic.mdb.Client.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("com/sun/ts/tests/ejb30/timer/basic/mdb/ejb3_timer_basic_mdb_client.xml");
            if(resURL != null) {
              ejb3_timer_basic_mdb_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/timer/basic/mdb/ejb3_timer_basic_mdb_client.jar.sun-application-client.xml");
            if(resURL != null) {
              ejb3_timer_basic_mdb_client.addAsManifestResource(resURL, "application-client.xml");
            }
            ejb3_timer_basic_mdb_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_timer_basic_mdb_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb3_timer_basic_mdb_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_timer_basic_mdb_ejb.jar");
            // The class files
            ejb3_timer_basic_mdb_ejb.addClasses(
                com.sun.ts.tests.ejb30.misc.getresource.common.GetResourceTest.class,
                com.sun.ts.tests.ejb30.timer.common.TimerMessageBeanBase.class,
                com.sun.ts.tests.ejb30.timer.common.TimerInfo.class,
                com.sun.ts.tests.ejb30.timer.common.MethodDispatcher.class,
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.ejb30.common.messaging.StatusReporter.class,
                com.sun.ts.tests.ejb30.timer.basic.mdb.TimerBasicBean.class,
                com.sun.ts.tests.ejb30.timer.basic.mdb.TimerBasicBeanBase.class,
                com.sun.ts.tests.ejb30.timer.basic.mdb.TimerBasicBeanBase2.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/timer/basic/mdb/ejb3_timer_basic_mdb_ejb.xml");
            if(ejbResURL != null) {
              ejb3_timer_basic_mdb_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/timer/basic/mdb/ejb3_timer_basic_mdb_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb3_timer_basic_mdb_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            StringAsset fooTxt = new StringAsset("foo.txt");
            ejb3_timer_basic_mdb_ejb.addAsResource(fooTxt, "com/sun/ts/tests/ejb30/timer/basic/mdb/foo.txt");

            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb3_timer_basic_mdb_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_timer_basic_mdb_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_timer_basic_mdb.ear");

            // Any libraries added to the ear
                URL libURL;
                JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");
                    // The class files
                    shared_lib.addClasses(
                        com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                        com.sun.ts.tests.ejb30.common.helper.Helper.class,
                        com.sun.ts.tests.ejb30.common.messaging.Constants.class,
                        com.sun.ts.tests.ejb30.common.helper.TLogger.class
                    );


                ejb3_timer_basic_mdb_ear.addAsLibrary(shared_lib);


            // The component jars built by the package target
            ejb3_timer_basic_mdb_ear.addAsModule(ejb3_timer_basic_mdb_ejb);
            ejb3_timer_basic_mdb_ear.addAsModule(ejb3_timer_basic_mdb_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/timer/basic/mdb/application.xml");
            if(earResURL != null) {
              ejb3_timer_basic_mdb_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/timer/basic/mdb/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_timer_basic_mdb_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_timer_basic_mdb_ear, Client.class, earResURL);
        return ejb3_timer_basic_mdb_ear;
        }

        @Test
        @Override
        public void test1() throws java.lang.Exception {
            super.test1();
        }

        @Test
        @Override
        public void getResourceInTimeOut() throws java.lang.Exception {
            super.getResourceInTimeOut();
        }


}