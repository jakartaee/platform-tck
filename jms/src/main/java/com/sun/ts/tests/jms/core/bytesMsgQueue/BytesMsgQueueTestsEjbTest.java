package com.sun.ts.tests.jms.core.bytesMsgQueue;

import com.sun.ts.tests.jms.core.bytesMsgQueue.BytesMsgQueueTests;
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
public class BytesMsgQueueTestsEjbTest extends com.sun.ts.tests.jms.core.bytesMsgQueue.BytesMsgQueueTests {
    static final String VEHICLE_ARCHIVE = "bytesMsgQueue_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        bytesMsgQueue_appclient_vehicle: 
        bytesMsgQueue_appclient_vehicle_client: jar.sun-application-client.xml,META-INF/application-client.xml
        bytesMsgQueue_ejb_vehicle: 
        bytesMsgQueue_ejb_vehicle_client: jar.sun-application-client.xml,META-INF/application-client.xml
        bytesMsgQueue_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        bytesMsgQueue_jsp_vehicle: 
        bytesMsgQueue_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        bytesMsgQueue_servlet_vehicle: 
        bytesMsgQueue_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core/bytesMsgQueue/ejb_vehicle_ejb.xml
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
            JavaArchive bytesMsgQueue_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "bytesMsgQueue_ejb_vehicle_client.jar");
            // The class files
            bytesMsgQueue_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.core.bytesMsgQueue.BytesMsgQueueTests.class
            );
            // The application-client.xml descriptor
            URL resURL = BytesMsgQueueTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              bytesMsgQueue_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = BytesMsgQueueTests.class.getResource("bytesMsgQueue_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              bytesMsgQueue_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            // bytesMsgQueue_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + BytesMsgQueueTests.class.getName() + "\n"), "MANIFEST.MF");
            bytesMsgQueue_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");

            // Call the archive processor
            archiveProcessor.processClientArchive(bytesMsgQueue_ejb_vehicle_client, BytesMsgQueueTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive bytesMsgQueue_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "bytesMsgQueue_ejb_vehicle_ejb.jar");
            // The class files
            bytesMsgQueue_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.jms.core.bytesMsgQueue.BytesMsgQueueTests.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = BytesMsgQueueTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              bytesMsgQueue_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = BytesMsgQueueTests.class.getResource("bytesMsgQueue_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              bytesMsgQueue_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(bytesMsgQueue_ejb_vehicle_ejb, BytesMsgQueueTests.class, ejbResURL);

        // Ear
            EnterpriseArchive bytesMsgQueue_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "bytesMsgQueue_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            bytesMsgQueue_ejb_vehicle_ear.addAsModule(bytesMsgQueue_ejb_vehicle_ejb);
            bytesMsgQueue_ejb_vehicle_ear.addAsModule(bytesMsgQueue_ejb_vehicle_client);



        return bytesMsgQueue_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void bytesMsgNullStreamQTest() throws java.lang.Exception {
            super.bytesMsgNullStreamQTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void bytesMessageQTestsFullMsg() throws java.lang.Exception {
            super.bytesMessageQTestsFullMsg();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void bytesMessageQNotWriteable() throws java.lang.Exception {
            super.bytesMessageQNotWriteable();
        }


}