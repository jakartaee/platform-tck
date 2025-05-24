package com.sun.ts.tests.jms.core20.jmsconsumertests;

import com.sun.ts.lib.harness.Fault;

import java.net.URL;

import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;
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
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("jms")
@Tag("platform")
@Tag("jms_web")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientEjbTest extends com.sun.ts.tests.jms.core20.jmsconsumertests.Client {
    static final String VEHICLE_ARCHIVE = "jmsconsumertests_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        jmsconsumertests_appclient_vehicle: 
        jmsconsumertests_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml,META-INF/application-client.xml,jar.sun-application-client.xml
        jmsconsumertests_ejb_vehicle: 
        jmsconsumertests_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        jmsconsumertests_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        jmsconsumertests_jsp_vehicle: 
        jmsconsumertests_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        jmsconsumertests_servlet_vehicle: 
        jmsconsumertests_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core20/jmsconsumertests/ejb_vehicle_ejb.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.jar.sun-ejb-jar.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive jmsconsumertests_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "jmsconsumertests_ejb_vehicle_client.jar");
            // The class files
            jmsconsumertests_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.core20.jmsconsumertests.Client.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              jmsconsumertests_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("jmsconsumertests_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              jmsconsumertests_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            // jmsconsumertests_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            jmsconsumertests_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(jmsconsumertests_ejb_vehicle_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive jmsconsumertests_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "jmsconsumertests_ejb_vehicle_ejb.jar");
            // The class files
            jmsconsumertests_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                Fault.class,
                EETest.class,
                ServiceEETest.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
                com.sun.ts.tests.jms.core20.jmsconsumertests.Client.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              jmsconsumertests_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("jmsconsumertests_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              jmsconsumertests_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(jmsconsumertests_ejb_vehicle_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive jmsconsumertests_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "jmsconsumertests_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            jmsconsumertests_ejb_vehicle_ear.addAsModule(jmsconsumertests_ejb_vehicle_ejb);
            jmsconsumertests_ejb_vehicle_ear.addAsModule(jmsconsumertests_ejb_vehicle_client);



            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              jmsconsumertests_ejb_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(jmsconsumertests_ejb_vehicle_ear, Client.class, earResURL);
        return jmsconsumertests_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void queueReceiveTests() throws java.lang.Exception {
            super.queueReceiveTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void queueReceiveBodyTests() throws java.lang.Exception {
            super.queueReceiveBodyTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void queueReceiveBodyExceptionTests() throws java.lang.Exception {
            super.queueReceiveBodyExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void queueGetMessageSelectorTest() throws java.lang.Exception {
            super.queueGetMessageSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void queueCloseTest() throws java.lang.Exception {
            super.queueCloseTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void topicReceiveTests() throws java.lang.Exception {
            super.topicReceiveTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void topicReceiveBodyTests() throws java.lang.Exception {
            super.topicReceiveBodyTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void topicReceiveBodyExceptionTests() throws java.lang.Exception {
            super.topicReceiveBodyExceptionTests();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void topicGetMessageSelectorTest() throws java.lang.Exception {
            super.topicGetMessageSelectorTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void topicCloseTest() throws java.lang.Exception {
            super.topicCloseTest();
        }


}