package com.sun.ts.tests.ejb30.bb.session.stateful.remove.complement;

import com.sun.ts.tests.ejb30.bb.session.stateful.remove.complement.Client;
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
@Tag("platform_optional")
@Tag("ejb_2x_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateful.remove.complement.Client {
    /**
        EE10 Deployment Descriptors:
        stateful_remove_complement: 
        stateful_remove_complement_client: META-INF/application-client.xml
        stateful_remove_complement_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/bb/session/stateful/remove/complement/stateful_remove_complement_client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateful/remove/complement/stateful_remove_complement_ejb.xml
        /com/sun/ts/tests/ejb30/bb/session/stateful/remove/complement/stateful_remove_complement_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "stateful_remove_complement", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive stateful_remove_complement_client = ShrinkWrap.create(JavaArchive.class, "stateful_remove_complement_client.jar");
            // The class files
            stateful_remove_complement_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveIF.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveNotRetainIF.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.common.appexception.UncheckedAppException.class,
            com.sun.ts.tests.ejb30.common.migration.twothree.TwoRemoteIF.class,
            com.sun.ts.tests.ejb30.common.appexception.AtUncheckedAppException.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.Remove2IF.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.remove.complement.Client.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.ClientBase.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.TestIF.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/remove/complement/stateful_remove_complement_client.xml");
            if(resURL != null) {
              stateful_remove_complement_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/remove/complement/stateful_remove_complement_client.jar.sun-application-client.xml");
            if(resURL != null) {
              stateful_remove_complement_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            stateful_remove_complement_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(stateful_remove_complement_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive stateful_remove_complement_ejb = ShrinkWrap.create(JavaArchive.class, "stateful_remove_complement_ejb.jar");
            // The class files
            stateful_remove_complement_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.descriptor.StatelessTestBean.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveIF.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveNotRetainIF.class,
                com.sun.ts.tests.ejb30.common.migration.twothree.TwoLocalIF.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveLocalIF.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.complement.RemoveBean.class,
                com.sun.ts.tests.ejb30.common.appexception.UncheckedAppException.class,
                com.sun.ts.tests.ejb30.common.migration.twothree.TwoRemoteIF.class,
                com.sun.ts.tests.ejb30.common.appexception.AtUncheckedAppException.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.Remove2IF.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.TestBeanBase.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.complement.RemoveNotRetainBean.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveLocal2IF.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.TestIF.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/remove/complement/stateful_remove_complement_ejb.xml");
            if(ejbResURL != null) {
              stateful_remove_complement_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/remove/complement/stateful_remove_complement_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              stateful_remove_complement_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(stateful_remove_complement_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive stateful_remove_complement_ear = ShrinkWrap.create(EnterpriseArchive.class, "stateful_remove_complement.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            stateful_remove_complement_ear.addAsModule(stateful_remove_complement_ejb);
            stateful_remove_complement_ear.addAsModule(stateful_remove_complement_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/remove/complement/application.xml");
            if(earResURL != null) {
              stateful_remove_complement_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/remove/complement/application.ear.sun-application.xml");
            if(earResURL != null) {
              stateful_remove_complement_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(stateful_remove_complement_ear, Client.class, earResURL);
        return stateful_remove_complement_ear;
        }

        @Test
        @Override
        public void removeBean() throws java.lang.Exception {
            super.removeBean();
        }

        @Test
        @Override
        public void removeBean2() throws java.lang.Exception {
            super.removeBean2();
        }

        @Test
        @Override
        public void retainBean() throws java.lang.Exception {
            super.retainBean();
        }

        @Test
        @Override
        public void retainBean2() throws java.lang.Exception {
            super.retainBean2();
        }

        @Test
        @Override
        public void removeNotRetainBean() throws java.lang.Exception {
            super.removeNotRetainBean();
        }

        @Test
        @Override
        public void removeNotRetainBean2() throws java.lang.Exception {
            super.removeNotRetainBean2();
        }


}