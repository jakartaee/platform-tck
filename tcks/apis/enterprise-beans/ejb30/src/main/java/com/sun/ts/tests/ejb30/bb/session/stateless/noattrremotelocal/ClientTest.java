package com.sun.ts.tests.ejb30.bb.session.stateless.noattrremotelocal;

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
@Tag("ejb_3x_remote_optional")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateless.noattrremotelocal.Client {
    /**
        EE10 Deployment Descriptors:
        ejb3_bb_stateless_noattrremotelocal: 
        ejb3_bb_stateless_noattrremotelocal_client: 
        ejb3_bb_stateless_noattrremotelocal_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateless/noattrremotelocal/ejb3_bb_stateless_noattrremotelocal_ejb.xml
        /com/sun/ts/tests/ejb30/bb/session/stateless/noattrremotelocal/ejb3_bb_stateless_noattrremotelocal_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_bb_stateless_noattrremotelocal", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateless_noattrremotelocal_client = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateless_noattrremotelocal_client.jar");
            // The class files
            ejb3_bb_stateless_noattrremotelocal_client.addClasses(
            com.sun.ts.tests.ejb30.common.busiface.TestIF.class,
            com.sun.ts.tests.ejb30.common.busiface.ClientBase.class,
            Fault.class,
            com.sun.ts.tests.ejb30.common.busiface.BusinessIF2.class,
            com.sun.ts.tests.ejb30.common.busiface.BusinessLocalIF2.class,
            com.sun.ts.tests.ejb30.common.busiface.AnnotatedBusinessInterface1.class,
            com.sun.ts.tests.ejb30.common.busiface.BusinessIF1.class,
            com.sun.ts.tests.ejb30.common.busiface.BusinessLocalIF1.class,
            com.sun.ts.tests.ejb30.bb.session.stateless.noattrremotelocal.Client.class,
            com.sun.ts.tests.ejb30.common.busiface.Constants.class,
            com.sun.ts.tests.ejb30.common.busiface.AnnotatedBusinessInterface2.class,
            EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            SetupException.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/noattrremotelocal/ejb3_bb_stateless_noattrremotelocal_client.xml");
            if(resURL != null) {
              ejb3_bb_stateless_noattrremotelocal_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/noattrremotelocal/ejb3_bb_stateless_noattrremotelocal_client.jar.sun-application-client.xml");
            if(resURL != null) {
              ejb3_bb_stateless_noattrremotelocal_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            ejb3_bb_stateless_noattrremotelocal_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_bb_stateless_noattrremotelocal_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb3_bb_stateless_noattrremotelocal_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_bb_stateless_noattrremotelocal_ejb.jar");
            // The class files
            ejb3_bb_stateless_noattrremotelocal_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.busiface.TestIF.class,
                com.sun.ts.tests.ejb30.common.busiface.TestBeanBase.class,
                com.sun.ts.tests.ejb30.common.busiface.AnnotatedLocalBusinessInterface2.class,
                com.sun.ts.tests.ejb30.common.busiface.BusinessIF2.class,
                com.sun.ts.tests.ejb30.common.busiface.BusinessLocalIF2.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.noattrremotelocal.BusinessBean.class,
                com.sun.ts.tests.ejb30.common.busiface.AnnotatedBusinessInterface1.class,
                com.sun.ts.tests.ejb30.common.busiface.SessionBeanLocalBeanBase.class,
                com.sun.ts.tests.ejb30.common.busiface.AnnotatedLocalBusinessInterface1.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.noattrremotelocal.SessionBeanLocalBean.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.noattrremotelocal.ExternalizableLocalBean.class,
                com.sun.ts.tests.ejb30.common.busiface.BusinessBeanBase.class,
                com.sun.ts.tests.ejb30.common.busiface.BusinessIF1.class,
                com.sun.ts.tests.ejb30.common.busiface.BusinessLocalIF1.class,
                com.sun.ts.tests.ejb30.common.busiface.BusinessLocal1Base.class,
                com.sun.ts.tests.ejb30.common.busiface.Constants.class,
                com.sun.ts.tests.ejb30.common.busiface.AnnotatedBusinessInterface2.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.noattrremotelocal.SerializableLocalBean.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.noattrremotelocal.TestBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/noattrremotelocal/ejb3_bb_stateless_noattrremotelocal_ejb.xml");
            if(ejbResURL != null) {
              ejb3_bb_stateless_noattrremotelocal_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/noattrremotelocal/ejb3_bb_stateless_noattrremotelocal_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb3_bb_stateless_noattrremotelocal_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb3_bb_stateless_noattrremotelocal_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_bb_stateless_noattrremotelocal_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_bb_stateless_noattrremotelocal.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            ejb3_bb_stateless_noattrremotelocal_ear.addAsModule(ejb3_bb_stateless_noattrremotelocal_ejb);
            ejb3_bb_stateless_noattrremotelocal_ear.addAsModule(ejb3_bb_stateless_noattrremotelocal_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/noattrremotelocal/application.xml");
            if(earResURL != null) {
              ejb3_bb_stateless_noattrremotelocal_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/noattrremotelocal/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_bb_stateless_noattrremotelocal_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_bb_stateless_noattrremotelocal_ear, Client.class, earResURL);
        return ejb3_bb_stateless_noattrremotelocal_ear;
        }

        @Test
        @Override
        public void multipleInterfacesTest1() throws java.lang.Exception {
            super.multipleInterfacesTest1();
        }

        @Test
        @Override
        public void multipleInterfacesTest2() throws java.lang.Exception {
            super.multipleInterfacesTest2();
        }

        @Test
        @Override
        public void multipleInterfacesLocalTest1() throws java.lang.Exception {
            super.multipleInterfacesLocalTest1();
        }

        @Test
        @Override
        public void singleInterfaceLocalSerializableTest() throws java.lang.Exception {
            super.singleInterfaceLocalSerializableTest();
        }

        @Test
        @Override
        public void singleInterfaceLocalExternalizableTest() throws java.lang.Exception {
            super.singleInterfaceLocalExternalizableTest();
        }

        @Test
        @Override
        public void singleInterfaceLocalSessionBeanTest() throws java.lang.Exception {
            super.singleInterfaceLocalSessionBeanTest();
        }


}