package com.sun.ts.tests.ejb30.bb.session.stateful.remove.descriptor;

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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateful.remove.descriptor.Client {
    /**
        EE10 Deployment Descriptors:
        stateful_remove_descriptor: 
        stateful_remove_descriptor_client: META-INF/application-client.xml,jar.sun-application-client.xml
        stateful_remove_descriptor_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/bb/session/stateful/remove/descriptor/stateful_remove_descriptor_client.xml
        /com/sun/ts/tests/ejb30/bb/session/stateful/remove/descriptor/stateful_remove_descriptor_client.jar.sun-application-client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateful/remove/descriptor/stateful_remove_descriptor_ejb.xml
        /com/sun/ts/tests/ejb30/bb/session/stateful/remove/descriptor/stateful_remove_descriptor_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "stateful_remove_descriptor", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive stateful_remove_descriptor_client = ShrinkWrap.create(JavaArchive.class, "stateful_remove_descriptor_client.jar");
            // The class files
            stateful_remove_descriptor_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveIF.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveNotRetainIF.class,
            Fault.class,
            com.sun.ts.tests.ejb30.common.appexception.UncheckedAppException.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.remove.descriptor.Client.class,
            com.sun.ts.tests.ejb30.common.migration.twothree.TwoRemoteIF.class,
            com.sun.ts.tests.ejb30.common.appexception.AtUncheckedAppException.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.Remove2IF.class,
            EETest.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.ClientBase.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            SetupException.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.TestIF.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/remove/descriptor/stateful_remove_descriptor_client.xml");
            if(resURL != null) {
              stateful_remove_descriptor_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/remove/descriptor/stateful_remove_descriptor_client.jar.sun-application-client.xml");
            if(resURL != null) {
              stateful_remove_descriptor_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            stateful_remove_descriptor_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(stateful_remove_descriptor_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive stateful_remove_descriptor_ejb = ShrinkWrap.create(JavaArchive.class, "stateful_remove_descriptor_ejb.jar");
            // The class files
            stateful_remove_descriptor_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.descriptor.StatelessTestBean.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveIF.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveNotRetainIF.class,
                com.sun.ts.tests.ejb30.common.migration.twothree.TwoLocalIF.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveLocalIF.class,
                com.sun.ts.tests.ejb30.common.appexception.UncheckedAppException.class,
                com.sun.ts.tests.ejb30.common.migration.twothree.TwoRemoteIF.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.descriptor.RemoveBean.class,
                com.sun.ts.tests.ejb30.common.appexception.AtUncheckedAppException.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.Remove2IF.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.TestBeanBase.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.RemoveLocal2IF.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.descriptor.RemoveNotRetainBean.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.remove.common.TestIF.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/remove/descriptor/stateful_remove_descriptor_ejb.xml");
            if(ejbResURL != null) {
              stateful_remove_descriptor_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/remove/descriptor/stateful_remove_descriptor_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              stateful_remove_descriptor_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(stateful_remove_descriptor_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive stateful_remove_descriptor_ear = ShrinkWrap.create(EnterpriseArchive.class, "stateful_remove_descriptor.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            stateful_remove_descriptor_ear.addAsModule(stateful_remove_descriptor_ejb);
            stateful_remove_descriptor_ear.addAsModule(stateful_remove_descriptor_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/remove/descriptor/application.xml");
            if(earResURL != null) {
              stateful_remove_descriptor_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/remove/descriptor/application.ear.sun-application.xml");
            if(earResURL != null) {
              stateful_remove_descriptor_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(stateful_remove_descriptor_ear, Client.class, earResURL);
        return stateful_remove_descriptor_ear;
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
        public void testBeanremoveBean2() throws java.lang.Exception {
            super.testBeanremoveBean2();
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
        public void testBeanretainBean2() throws java.lang.Exception {
            super.testBeanretainBean2();
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
        public void testBeanremoveBeanRemote() throws java.lang.Exception {
            super.testBeanremoveBeanRemote();
        }

        @Test
        @Override
        public void testBeanremoveBean2Remote() throws java.lang.Exception {
            super.testBeanremoveBean2Remote();
        }

        @Test
        @Override
        public void testBeanretainBeanRemote() throws java.lang.Exception {
            super.testBeanretainBeanRemote();
        }

        @Test
        @Override
        public void testBeanretainBean2Remote() throws java.lang.Exception {
            super.testBeanretainBean2Remote();
        }

        @Test
        @Override
        public void removeNotRetainBeanOverloadedRemoveMethod() throws java.lang.Exception {
            super.removeNotRetainBeanOverloadedRemoveMethod();
        }

        @Test
        @Override
        public void removeNotRetainBeanOverloadedRemove2Method() throws java.lang.Exception {
            super.removeNotRetainBeanOverloadedRemove2Method();
        }

        @Test
        @Override
        public void retainBeanOverloaded() throws java.lang.Exception {
            super.retainBeanOverloaded();
        }

        @Test
        @Override
        public void testBeanretainBeanOverloaded() throws java.lang.Exception {
            super.testBeanretainBeanOverloaded();
        }


}