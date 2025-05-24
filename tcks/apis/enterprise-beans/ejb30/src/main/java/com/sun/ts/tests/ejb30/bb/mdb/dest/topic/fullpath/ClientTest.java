package com.sun.ts.tests.ejb30.bb.mdb.dest.topic.fullpath;

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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.mdb.dest.topic.fullpath.Client {
    /**
        EE10 Deployment Descriptors:
        mdb_dest_topic_fullpath: 
        mdb_dest_topic_fullpath_client: META-INF/application-client.xml,jar.sun-application-client.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/bb/mdb/dest/topic/fullpath/mdb_dest_topic_fullpath_client.xml
        /com/sun/ts/tests/ejb30/bb/mdb/dest/topic/fullpath/mdb_dest_topic_fullpath_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/bb/mdb/dest/topic/fullpath/one_ejb.xml
        /com/sun/ts/tests/ejb30/bb/mdb/dest/topic/fullpath/one_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "mdb_dest_topic_fullpath", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mdb_dest_topic_fullpath_client = ShrinkWrap.create(JavaArchive.class, "mdb_dest_topic_fullpath_client.jar");
            // The class files
            mdb_dest_topic_fullpath_client.addClasses(
            com.sun.ts.tests.ejb30.common.messaging.TopicClientBase.class,
            com.sun.ts.tests.jms.commonee.Client.class,
            com.sun.ts.tests.ejb30.bb.mdb.dest.common.TopicClientBase.class,
            com.sun.ts.tests.ejb30.bb.mdb.dest.topic.fullpath.Client.class,
            Fault.class,
            com.sun.ts.tests.ejb30.common.messaging.Constants.class,
            EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.tests.ejb30.common.messaging.ClientBase.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/topic/fullpath/mdb_dest_topic_fullpath_client.xml");
            if(resURL != null) {
              mdb_dest_topic_fullpath_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/topic/fullpath/mdb_dest_topic_fullpath_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdb_dest_topic_fullpath_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdb_dest_topic_fullpath_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdb_dest_topic_fullpath_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive one_ejb = ShrinkWrap.create(JavaArchive.class, "one_ejb.jar");
            // The class files
            one_ejb.addClasses(
                com.sun.ts.tests.ejb30.bb.mdb.dest.common.TopicDestBean.class,
                com.sun.ts.tests.jms.common.JmsUtil.class,
                com.sun.ts.tests.ejb30.common.messaging.StatusReporter.class,
                com.sun.ts.tests.ejb30.common.messaging.Constants.class,
                com.sun.ts.tests.ejb30.bb.mdb.dest.common.DestBeanBase.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/topic/fullpath/one_ejb.xml");
            if(ejbResURL != null) {
              one_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/topic/fullpath/one_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              one_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(one_ejb, Client.class, ejbResURL);
            // two_ejb
            JavaArchive two_ejb = ShrinkWrap.create(JavaArchive.class, "two_ejb.jar");
            two_ejb.addClasses(
                    com.sun.ts.tests.ejb30.common.calc.BaseRemoteCalculator.class,
                    com.sun.ts.tests.ejb30.common.calc.CalculatorException.class,
                    com.sun.ts.tests.ejb30.common.calc.NoInterfaceRemoteCalculator.class,
                    com.sun.ts.tests.ejb30.common.calc.RemoteCalculator.class,
                    com.sun.ts.tests.ejb30.bb.session.stateless.basic.RemoteCalculatorBean.class
            );
            // The ejb-jar.xml descriptor
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/topic/fullpath/two_ejb.xml");
            if(ejbResURL != null) {
                two_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/topic/fullpath/two_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
                two_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(two_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive mdb_dest_topic_fullpath_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdb_dest_topic_fullpath.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdb_dest_topic_fullpath_ear.addAsModule(two_ejb);
            mdb_dest_topic_fullpath_ear.addAsModule(one_ejb);
            mdb_dest_topic_fullpath_ear.addAsModule(mdb_dest_topic_fullpath_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/topic/fullpath/application.xml");
            if(earResURL != null) {
              mdb_dest_topic_fullpath_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/mdb/dest/topic/fullpath/application.ear.sun-application.xml");
            if(earResURL != null) {
              mdb_dest_topic_fullpath_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(mdb_dest_topic_fullpath_ear, Client.class, earResURL);
        return mdb_dest_topic_fullpath_ear;
        }

        @Test
        @Override
        public void test1() throws java.lang.Exception {
            super.test1();
        }


}