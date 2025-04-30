package com.sun.ts.tests.ejb30.bb.session.stateful.sessioncontext.descriptor;

import com.sun.ts.tests.ejb30.bb.session.stateful.sessioncontext.descriptor.Client;
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
@Tag("ejb_3x_remote_optional")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.bb.session.stateful.sessioncontext.descriptor.Client {
    /**
        EE10 Deployment Descriptors:
        stateful_sessioncontext_descriptor: 
        stateful_sessioncontext_descriptor_client: META-INF/application-client.xml
        stateful_sessioncontext_descriptor_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/bb/session/stateful/sessioncontext/descriptor/stateful_sessioncontext_descriptor_client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/bb/session/stateful/sessioncontext/descriptor/stateful_sessioncontext_descriptor_ejb.xml
        /com/sun/ts/tests/ejb30/bb/session/stateful/sessioncontext/descriptor/stateful_sessioncontext_descriptor_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "stateful_sessioncontext_descriptor", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive stateful_sessioncontext_descriptor_client = ShrinkWrap.create(JavaArchive.class, "stateful_sessioncontext_descriptor_client.jar");
            // The class files
            stateful_sessioncontext_descriptor_client.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.common.sessioncontext.Constants.class,
            com.sun.ts.tests.ejb30.common.sessioncontext.ClientBase.class,
            com.sun.ts.tests.ejb30.common.sessioncontext.CommonIF.class,
            com.sun.ts.tests.ejb30.common.sessioncontext.ThreeLocal1IF.class,
            com.sun.ts.tests.ejb30.common.sessioncontext.Three2IF.class,
            com.sun.ts.tests.ejb30.common.sessioncontext.ThreeLocal2IF.class,
            com.sun.ts.tests.ejb30.common.sessioncontext.AcceptIF.class,
            com.sun.ts.tests.ejb30.common.sessioncontext.TestIF.class,
            com.sun.ts.tests.ejb30.common.sessioncontext.Three1IF.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.helper.TLogger.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.sessioncontext.descriptor.Client.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/sessioncontext/descriptor/stateful_sessioncontext_descriptor_client.xml");
            if(resURL != null) {
              stateful_sessioncontext_descriptor_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/sessioncontext/descriptor/stateful_sessioncontext_descriptor_client.jar.sun-application-client.xml");
            if(resURL != null) {
              stateful_sessioncontext_descriptor_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            stateful_sessioncontext_descriptor_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(stateful_sessioncontext_descriptor_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive stateful_sessioncontext_descriptor_ejb = ShrinkWrap.create(JavaArchive.class, "stateful_sessioncontext_descriptor_ejb.jar");
            // The class files
            stateful_sessioncontext_descriptor_ejb.addClasses(
                com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.common.sessioncontext.AcceptBeanBase.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.sessioncontext.descriptor.SessionContextBean.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.sessioncontext.descriptor.TestBean.class,
                com.sun.ts.tests.ejb30.common.sessioncontext.TestBeanBase.class,
                com.sun.ts.tests.ejb30.common.sessioncontext.Constants.class,
                com.sun.ts.tests.ejb30.common.sessioncontext.SessionContextBeanBase.class,
                com.sun.ts.tests.ejb30.common.sessioncontext.AcceptLocalIF.class,
                com.sun.ts.tests.ejb30.common.sessioncontext.CommonIF.class,
                com.sun.ts.tests.ejb30.common.sessioncontext.ThreeLocal1IF.class,
                com.sun.ts.tests.ejb30.common.sessioncontext.Three2IF.class,
                com.sun.ts.tests.ejb30.common.sessioncontext.ThreeLocal2IF.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.sessioncontext.descriptor.AcceptBean.class,
                com.sun.ts.tests.ejb30.common.sessioncontext.AcceptIF.class,
                com.sun.ts.tests.ejb30.common.sessioncontext.TestIF.class,
                com.sun.ts.tests.ejb30.common.sessioncontext.Three1IF.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/sessioncontext/descriptor/stateful_sessioncontext_descriptor_ejb.xml");
            if(ejbResURL != null) {
              stateful_sessioncontext_descriptor_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/sessioncontext/descriptor/stateful_sessioncontext_descriptor_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              stateful_sessioncontext_descriptor_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(stateful_sessioncontext_descriptor_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive stateful_sessioncontext_descriptor_ear = ShrinkWrap.create(EnterpriseArchive.class, "stateful_sessioncontext_descriptor.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            stateful_sessioncontext_descriptor_ear.addAsModule(stateful_sessioncontext_descriptor_ejb);
            stateful_sessioncontext_descriptor_ear.addAsModule(stateful_sessioncontext_descriptor_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/sessioncontext/descriptor/application.xml");
            if(earResURL != null) {
              stateful_sessioncontext_descriptor_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/sessioncontext/descriptor/application.ear.sun-application.xml");
            if(earResURL != null) {
              stateful_sessioncontext_descriptor_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(stateful_sessioncontext_descriptor_ear, Client.class, earResURL);
        return stateful_sessioncontext_descriptor_ear;
        }

        @Test
        @Override
        public void getBusinessObjectRemote1() throws java.lang.Exception {
            super.getBusinessObjectRemote1();
        }

        @Test
        @Override
        public void getBusinessObjectRemote2() throws java.lang.Exception {
            super.getBusinessObjectRemote2();
        }

        @Test
        @Override
        public void getBusinessObjectLocal1() throws java.lang.Exception {
            super.getBusinessObjectLocal1();
        }

        @Test
        @Override
        public void getBusinessObjectLocal2() throws java.lang.Exception {
            super.getBusinessObjectLocal2();
        }

        @Test
        @Override
        public void passBusinessObjectRemote1() throws java.lang.Exception {
            super.passBusinessObjectRemote1();
        }

        @Test
        @Override
        public void passBusinessObjectRemote2() throws java.lang.Exception {
            super.passBusinessObjectRemote2();
        }

        @Test
        @Override
        public void passBusinessObjectLocal1() throws java.lang.Exception {
            super.passBusinessObjectLocal1();
        }

        @Test
        @Override
        public void passBusinessObjectLocal2() throws java.lang.Exception {
            super.passBusinessObjectLocal2();
        }

        @Test
        @Override
        public void lookupIllegalArgumentException() throws java.lang.Exception {
            super.lookupIllegalArgumentException();
        }

        @Test
        @Override
        public void getBusinessObjectRemote1Illegal() throws java.lang.Exception {
            super.getBusinessObjectRemote1Illegal();
        }

        @Test
        @Override
        public void getBusinessObjectLocal1Illegal() throws java.lang.Exception {
            super.getBusinessObjectLocal1Illegal();
        }

        @Test
        @Override
        public void getInvokedBusinessInterfaceRemote1() throws java.lang.Exception {
            super.getInvokedBusinessInterfaceRemote1();
        }

        @Test
        @Override
        public void getInvokedBusinessInterfaceRemote2() throws java.lang.Exception {
            super.getInvokedBusinessInterfaceRemote2();
        }

        @Test
        @Override
        public void getInvokedBusinessInterfaceLocal1() throws java.lang.Exception {
            super.getInvokedBusinessInterfaceLocal1();
        }

        @Test
        @Override
        public void getInvokedBusinessInterfaceLocal2() throws java.lang.Exception {
            super.getInvokedBusinessInterfaceLocal2();
        }


}