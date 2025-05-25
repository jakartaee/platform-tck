package com.sun.ts.tests.ejb30.bb.session.stateless.annotation.appexception.annotated;

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
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateless.annotation.appexception.annotated.Client {
    /**
        EE10 Deployment Descriptors:
        ejb3_stateless_appexception_annotated: 
        ejb3_stateless_appexception_annotated_client: 
        ejb3_stateless_appexception_annotated_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateless/annotation/appexception/annotated/ejb3_stateless_appexception_annotated_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_stateless_appexception_annotated", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_stateless_appexception_annotated_client = ShrinkWrap.create(JavaArchive.class, "ejb3_stateless_appexception_annotated_client.jar");
            // The class files
            ejb3_stateless_appexception_annotated_client.addClasses(
            com.sun.ts.tests.ejb30.bb.session.stateless.annotation.appexception.annotated.Client.class,
            Fault.class,
            com.sun.ts.tests.ejb30.common.appexception.RollbackIF.class,
            com.sun.ts.tests.ejb30.common.appexception.ClientBase.class,
            com.sun.ts.tests.ejb30.common.appexception.CommonIF.class,
            EETest.class,
            SetupException.class,
            com.sun.ts.tests.ejb30.common.appexception.AppExceptionIF.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/appexception/annotated/ejb3_stateless_appexception_annotated_client.xml");
            if(resURL != null) {
              ejb3_stateless_appexception_annotated_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/appexception/annotated/ejb3_stateless_appexception_annotated_client.jar.sun-application-client.xml");
            if(resURL != null) {
              ejb3_stateless_appexception_annotated_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            ejb3_stateless_appexception_annotated_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_stateless_appexception_annotated_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb3_stateless_appexception_annotated_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_stateless_appexception_annotated_ejb.jar");
            // The class files
            ejb3_stateless_appexception_annotated_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.appexception.AppExceptionBeanBase.class,
                com.sun.ts.tests.ejb30.common.appexception.AppExceptionLocalIF.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.annotation.appexception.annotated.RollbackBean.class,
                com.sun.ts.tests.ejb30.common.appexception.RollbackBeanBase.class,
                com.sun.ts.tests.ejb30.common.appexception.RollbackIF.class,
                com.sun.ts.tests.ejb30.common.appexception.CommonIF.class,
                com.sun.ts.tests.ejb30.bb.session.stateless.annotation.appexception.annotated.AppExceptionBean.class,
                com.sun.ts.tests.ejb30.common.appexception.AppExceptionIF.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/appexception/annotated/ejb3_stateless_appexception_annotated_ejb.xml");
            if(ejbResURL != null) {
              ejb3_stateless_appexception_annotated_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/appexception/annotated/ejb3_stateless_appexception_annotated_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb3_stateless_appexception_annotated_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb3_stateless_appexception_annotated_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_stateless_appexception_annotated_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_stateless_appexception_annotated.ear");

            // Any libraries added to the ear
                URL libURL;
                JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");
                    // The class files
                    shared_lib.addClasses(
                        com.sun.ts.tests.ejb30.common.appexception.AtUncheckedAppException.class,
                        com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                        com.sun.ts.tests.ejb30.common.appexception.UncheckedRollbackAppException.class,
                        com.sun.ts.tests.ejb30.common.appexception.AtCheckedAppException.class,
                        com.sun.ts.tests.ejb30.common.appexception.AtCheckedRollbackAppException.class,
                        com.sun.ts.tests.ejb30.common.appexception.UncheckedAppException.class,
                        com.sun.ts.tests.ejb30.common.appexception.CheckedAppException.class,
                        com.sun.ts.tests.ejb30.common.appexception.AtUncheckedRollbackAppException.class,
                        com.sun.ts.tests.ejb30.common.appexception.CheckedRollbackAppException.class,
                        com.sun.ts.tests.ejb30.common.helper.TLogger.class
                    );


                ejb3_stateless_appexception_annotated_ear.addAsLibrary(shared_lib);


            // The component jars built by the package target
            ejb3_stateless_appexception_annotated_ear.addAsModule(ejb3_stateless_appexception_annotated_ejb);
            ejb3_stateless_appexception_annotated_ear.addAsModule(ejb3_stateless_appexception_annotated_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/appexception/annotated/application.xml");
            if(earResURL != null) {
              ejb3_stateless_appexception_annotated_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateless/annotation/appexception/annotated/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_stateless_appexception_annotated_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_stateless_appexception_annotated_ear, Client.class, earResURL);
        return ejb3_stateless_appexception_annotated_ear;
        }

        @Test
        @Override
        public void atCheckedAppExceptionTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atCheckedAppExceptionTest();
        }

        @Test
        @Override
        public void atCheckedAppExceptionTest2() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atCheckedAppExceptionTest2();
        }

        @Test
        @Override
        public void atCheckedAppExceptionTestLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atCheckedAppExceptionTestLocal();
        }

        @Test
        @Override
        public void atUncheckedAppExceptionTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atUncheckedAppExceptionTest();
        }

        @Test
        @Override
        public void atUncheckedAppExceptionTest2() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atUncheckedAppExceptionTest2();
        }

        @Test
        @Override
        public void atUncheckedAppExceptionTestLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atUncheckedAppExceptionTestLocal();
        }

        @Test
        @Override
        public void atCheckedRollbackAppExceptionTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atCheckedRollbackAppExceptionTest();
        }

        @Test
        @Override
        public void atCheckedRollbackAppExceptionTestLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atCheckedRollbackAppExceptionTestLocal();
        }

        @Test
        @Override
        public void atUncheckedRollbackAppExceptionTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atUncheckedRollbackAppExceptionTest();
        }

        @Test
        @Override
        public void atUncheckedRollbackAppExceptionTestLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atUncheckedRollbackAppExceptionTestLocal();
        }


}