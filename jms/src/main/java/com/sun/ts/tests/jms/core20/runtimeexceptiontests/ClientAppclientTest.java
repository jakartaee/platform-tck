package com.sun.ts.tests.jms.core20.runtimeexceptiontests;

import com.sun.ts.tests.jms.core20.runtimeexceptiontests.Client;
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
@Tag("jms")
@Tag("platform")
@Tag("jms_web")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientAppclientTest extends com.sun.ts.tests.jms.core20.runtimeexceptiontests.Client {
    static final String VEHICLE_ARCHIVE = "runtimeexceptiontests_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        runtimeexceptiontests_appclient_vehicle: 
        runtimeexceptiontests_appclient_vehicle_client: META-INF/application-client.xml
        runtimeexceptiontests_ejb_vehicle: 
        runtimeexceptiontests_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        runtimeexceptiontests_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        runtimeexceptiontests_jsp_vehicle: 
        runtimeexceptiontests_jsp_vehicle_web: WEB-INF/web.xml
        runtimeexceptiontests_servlet_vehicle: 
        runtimeexceptiontests_servlet_vehicle_web: WEB-INF/web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive runtimeexceptiontests_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "runtimeexceptiontests_appclient_vehicle_client.jar");
            // The class files
            runtimeexceptiontests_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.core20.runtimeexceptiontests.Client.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml");
            if(resURL != null) {
              runtimeexceptiontests_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            // resURL = Client.class.getResource("//com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.jar.sun-application-client.xml");
            // if(resURL != null) {
            //   runtimeexceptiontests_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            // }
            runtimeexceptiontests_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(runtimeexceptiontests_appclient_vehicle_client, Client.class, resURL);

        // Ear
            EnterpriseArchive runtimeexceptiontests_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "runtimeexceptiontests_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            runtimeexceptiontests_appclient_vehicle_ear.addAsModule(runtimeexceptiontests_appclient_vehicle_client);



        return runtimeexceptiontests_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void transactionRolledBackRuntimeExceptionTest1() throws java.lang.Exception {
            super.transactionRolledBackRuntimeExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void transactionRolledBackRuntimeExceptionTest2() throws java.lang.Exception {
            super.transactionRolledBackRuntimeExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void transactionRolledBackRuntimeExceptionTest3() throws java.lang.Exception {
            super.transactionRolledBackRuntimeExceptionTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void transactionInProgressRuntimeExceptionTest1() throws java.lang.Exception {
            super.transactionInProgressRuntimeExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void transactionInProgressRuntimeExceptionTest2() throws java.lang.Exception {
            super.transactionInProgressRuntimeExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void transactionInProgressRuntimeExceptionTest3() throws java.lang.Exception {
            super.transactionInProgressRuntimeExceptionTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void resourceAllocationRuntimeExceptionTest1() throws java.lang.Exception {
            super.resourceAllocationRuntimeExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void resourceAllocationRuntimeExceptionTest2() throws java.lang.Exception {
            super.resourceAllocationRuntimeExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void resourceAllocationRuntimeExceptionTest3() throws java.lang.Exception {
            super.resourceAllocationRuntimeExceptionTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void messageNotWriteableRuntimeExceptionTest1() throws java.lang.Exception {
            super.messageNotWriteableRuntimeExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void messageNotWriteableRuntimeExceptionTest2() throws java.lang.Exception {
            super.messageNotWriteableRuntimeExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void messageNotWriteableRuntimeExceptionTest3() throws java.lang.Exception {
            super.messageNotWriteableRuntimeExceptionTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void messageFormatRuntimeExceptionTest1() throws java.lang.Exception {
            super.messageFormatRuntimeExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void messageFormatRuntimeExceptionTest2() throws java.lang.Exception {
            super.messageFormatRuntimeExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void messageFormatRuntimeExceptionTest3() throws java.lang.Exception {
            super.messageFormatRuntimeExceptionTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void jmsSecurityRuntimeExceptionTest1() throws java.lang.Exception {
            super.jmsSecurityRuntimeExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void jmsSecurityRuntimeExceptionTest2() throws java.lang.Exception {
            super.jmsSecurityRuntimeExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void jmsSecurityRuntimeExceptionTest3() throws java.lang.Exception {
            super.jmsSecurityRuntimeExceptionTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void jmsRuntimeExceptionTest1() throws java.lang.Exception {
            super.jmsRuntimeExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void jmsRuntimeExceptionTest2() throws java.lang.Exception {
            super.jmsRuntimeExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void jmsRuntimeExceptionTest3() throws java.lang.Exception {
            super.jmsRuntimeExceptionTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void invalidSelectorRuntimeExceptionTest1() throws java.lang.Exception {
            super.invalidSelectorRuntimeExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void invalidSelectorRuntimeExceptionTest2() throws java.lang.Exception {
            super.invalidSelectorRuntimeExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void invalidSelectorRuntimeExceptionTest3() throws java.lang.Exception {
            super.invalidSelectorRuntimeExceptionTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void invalidDestinationRuntimeExceptionTest1() throws java.lang.Exception {
            super.invalidDestinationRuntimeExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void invalidDestinationRuntimeExceptionTest2() throws java.lang.Exception {
            super.invalidDestinationRuntimeExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void invalidDestinationRuntimeExceptionTest3() throws java.lang.Exception {
            super.invalidDestinationRuntimeExceptionTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void invalidClientIDRuntimeExceptionTest1() throws java.lang.Exception {
            super.invalidClientIDRuntimeExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void invalidClientIDRuntimeExceptionTest2() throws java.lang.Exception {
            super.invalidClientIDRuntimeExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void invalidClientIDRuntimeExceptionTest3() throws java.lang.Exception {
            super.invalidClientIDRuntimeExceptionTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void illegalStateRuntimeExceptionTest1() throws java.lang.Exception {
            super.illegalStateRuntimeExceptionTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void illegalStateRuntimeExceptionTest2() throws java.lang.Exception {
            super.illegalStateRuntimeExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void illegalStateRuntimeExceptionTest3() throws java.lang.Exception {
            super.illegalStateRuntimeExceptionTest3();
        }


}