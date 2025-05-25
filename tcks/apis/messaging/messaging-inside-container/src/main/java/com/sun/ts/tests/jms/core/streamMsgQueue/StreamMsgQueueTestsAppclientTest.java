package com.sun.ts.tests.jms.core.streamMsgQueue;

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
public class StreamMsgQueueTestsAppclientTest extends com.sun.ts.tests.jms.core.streamMsgQueue.StreamMsgQueueTests {
    static final String VEHICLE_ARCHIVE = "streamMsgQueue_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        streamMsgQueue_appclient_vehicle: 
        streamMsgQueue_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        streamMsgQueue_ejb_vehicle: 
        streamMsgQueue_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        streamMsgQueue_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        streamMsgQueue_jsp_vehicle: 
        streamMsgQueue_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        streamMsgQueue_servlet_vehicle: 
        streamMsgQueue_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/core/streamMsgQueue/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive streamMsgQueue_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "streamMsgQueue_appclient_vehicle_client.jar");
            // The class files
            streamMsgQueue_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.jms.core.streamMsgQueue.StreamMsgQueueTests.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = StreamMsgQueueTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              streamMsgQueue_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = StreamMsgQueueTests.class.getResource("streamMsgQueue_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              streamMsgQueue_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            streamMsgQueue_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + StreamMsgQueueTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(streamMsgQueue_appclient_vehicle_client, StreamMsgQueueTests.class, resURL);

        // Ear
            EnterpriseArchive streamMsgQueue_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "streamMsgQueue_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            streamMsgQueue_appclient_vehicle_ear.addAsModule(streamMsgQueue_appclient_vehicle_client);



        return streamMsgQueue_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionQTestsBoolean() throws java.lang.Exception {
            super.streamMessageConversionQTestsBoolean();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionQTestsByte() throws java.lang.Exception {
            super.streamMessageConversionQTestsByte();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionQTestsShort() throws java.lang.Exception {
            super.streamMessageConversionQTestsShort();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionQTestsInt() throws java.lang.Exception {
            super.streamMessageConversionQTestsInt();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionQTestsLong() throws java.lang.Exception {
            super.streamMessageConversionQTestsLong();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionQTestsFloat() throws java.lang.Exception {
            super.streamMessageConversionQTestsFloat();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionQTestsDouble() throws java.lang.Exception {
            super.streamMessageConversionQTestsDouble();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionQTestsString() throws java.lang.Exception {
            super.streamMessageConversionQTestsString();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionQTestsChar() throws java.lang.Exception {
            super.streamMessageConversionQTestsChar();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionQTestsBytes() throws java.lang.Exception {
            super.streamMessageConversionQTestsBytes();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionQTestsInvFormatString() throws java.lang.Exception {
            super.streamMessageConversionQTestsInvFormatString();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageQTestsFullMsg() throws java.lang.Exception {
            super.streamMessageQTestsFullMsg();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageQTestNull() throws java.lang.Exception {
            super.streamMessageQTestNull();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageQNotWritable() throws java.lang.Exception {
            super.streamMessageQNotWritable();
        }


}