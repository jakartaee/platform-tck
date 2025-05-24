package com.sun.ts.tests.ejb30.bb.session.stateful.remove.annotated;

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
@Tag("platform_optional")
@Tag("ejb_2x_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateful.remove.annotated.Client {
    /**
        EE10 Deployment Descriptors:
        stateful_remove_annotated: 
        stateful_remove_annotated_client: 
        stateful_remove_annotated_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateful/remove/annotated/stateful_remove_annotated_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "stateful_remove_annotated", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive stateful_remove_annotated_client = ShrinkWrap.create(JavaArchive.class, "stateful_remove_annotated_client.jar");
            // The class files
            stateful_remove_annotated_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveIF.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveNotRetainIF.class,
            Fault.class,
            com.sun.ts.tests.ejb30.common.appexception.UncheckedAppException.class,
            com.sun.ts.tests.ejb30.common.migration.twothree.TwoRemoteIF.class,
            com.sun.ts.tests.ejb30.common.appexception.AtUncheckedAppException.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.Remove2IF.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.remove.annotated.Client.class,
            EETest.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.ClientBase.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            SetupException.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.TestIF.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/remove/annotated/stateful_remove_annotated_client.xml");
            if(resURL != null) {
              stateful_remove_annotated_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/remove/annotated/stateful_remove_annotated_client.jar.sun-application-client.xml");
            if(resURL != null) {
              stateful_remove_annotated_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            stateful_remove_annotated_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(stateful_remove_annotated_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive stateful_remove_annotated_ejb = ShrinkWrap.create(JavaArchive.class, "stateful_remove_annotated_ejb.jar");
            // The class files
            stateful_remove_annotated_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveIF.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveNotRetainIF.class,
                com.sun.ts.tests.ejb30.common.migration.twothree.TwoLocalIF.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveLocalIF.class,
                com.sun.ts.tests.ejb30.common.appexception.UncheckedAppException.class,
                com.sun.ts.tests.ejb30.common.migration.twothree.TwoRemoteIF.class,
                com.sun.ts.tests.ejb30.common.appexception.AtUncheckedAppException.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.annotated.RemoveBean.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.Remove2IF.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.TestBeanBase.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.annotated.StatelessTestBean.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.annotated.RemoveNotRetainBean.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveLocal2IF.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.TestIF.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/remove/annotated/stateful_remove_annotated_ejb.xml");
            if(ejbResURL != null) {
              stateful_remove_annotated_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/remove/annotated/stateful_remove_annotated_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              stateful_remove_annotated_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(stateful_remove_annotated_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive stateful_remove_annotated_ear = ShrinkWrap.create(EnterpriseArchive.class, "stateful_remove_annotated.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            stateful_remove_annotated_ear.addAsModule(stateful_remove_annotated_ejb);
            stateful_remove_annotated_ear.addAsModule(stateful_remove_annotated_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/remove/annotated/application.xml");
            if(earResURL != null) {
              stateful_remove_annotated_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/remove/annotated/application.ear.sun-application.xml");
            if(earResURL != null) {
              stateful_remove_annotated_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(stateful_remove_annotated_ear, Client.class, earResURL);
        return stateful_remove_annotated_ear;
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
        public void testBeanremoveBean() throws java.lang.Exception {
            super.testBeanremoveBean();
        }

        @Test
        @Override
        public void testBeanremoveBeanRemote() throws java.lang.Exception {
            super.testBeanremoveBeanRemote();
        }

        @Test
        @Override
        public void testBeanremoveBean2() throws java.lang.Exception {
            super.testBeanremoveBean2();
        }

        @Test
        @Override
        public void testBeanremoveBean2Remote() throws java.lang.Exception {
            super.testBeanremoveBean2Remote();
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
        public void testBeanretainBean() throws java.lang.Exception {
            super.testBeanretainBean();
        }

        @Test
        @Override
        public void testBeanretainBeanRemote() throws java.lang.Exception {
            super.testBeanretainBeanRemote();
        }

        @Test
        @Override
        public void testBeanretainBean2() throws java.lang.Exception {
            super.testBeanretainBean2();
        }

        @Test
        @Override
        public void testBeanretainBean2Remote() throws java.lang.Exception {
            super.testBeanretainBean2Remote();
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

        @Test
        @Override
        public void alwaysRemoveAfterSystemException() throws java.lang.Exception {
            super.alwaysRemoveAfterSystemException();
        }

        @Test
        @Override
        public void testBeanretainBeanOverloaded() throws java.lang.Exception {
            super.testBeanretainBeanOverloaded();
        }

        @Test
        @Override
        public void retainBeanOverloaded() throws java.lang.Exception {
            super.retainBeanOverloaded();
        }


}