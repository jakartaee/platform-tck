package com.sun.ts.tests.jms.core.streamMsgTopic;

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
public class StreamMsgTopicTestsAppclientTest extends com.sun.ts.tests.jms.core.streamMsgTopic.StreamMsgTopicTests {
    static final String VEHICLE_ARCHIVE = "streamMsgTopic_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        streamMsgTopic_appclient_vehicle: 
        streamMsgTopic_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        streamMsgTopic_ejb_vehicle: 
        streamMsgTopic_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        streamMsgTopic_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        streamMsgTopic_jsp_vehicle: 
        streamMsgTopic_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        streamMsgTopic_servlet_vehicle: 
        streamMsgTopic_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/core/streamMsgTopic/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive streamMsgTopic_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "streamMsgTopic_appclient_vehicle_client.jar");
            // The class files
            streamMsgTopic_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.jms.core.streamMsgTopic.StreamMsgTopicTests.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = StreamMsgTopicTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              streamMsgTopic_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = StreamMsgTopicTests.class.getResource("streamMsgTopic_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              streamMsgTopic_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            streamMsgTopic_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + StreamMsgTopicTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(streamMsgTopic_appclient_vehicle_client, StreamMsgTopicTests.class, resURL);

        // Ear
            EnterpriseArchive streamMsgTopic_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "streamMsgTopic_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            streamMsgTopic_appclient_vehicle_ear.addAsModule(streamMsgTopic_appclient_vehicle_client);



        return streamMsgTopic_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionTopicTestsBoolean() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsBoolean();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionTopicTestsByte() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsByte();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionTopicTestsShort() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsShort();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionTopicTestsInt() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsInt();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionTopicTestsLong() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsLong();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionTopicTestsFloat() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsFloat();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionTopicTestsDouble() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsDouble();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionTopicTestsString() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsString();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionTopicTestsChar() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsChar();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionTopicTestsBytes() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsBytes();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageConversionTopicTestsInvFormatString() throws java.lang.Exception {
            super.streamMessageConversionTopicTestsInvFormatString();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageTopicTestsFullMsg() throws java.lang.Exception {
            super.streamMessageTopicTestsFullMsg();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageTopicTestNull() throws java.lang.Exception {
            super.streamMessageTopicTestNull();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void streamMessageQNotWritable() throws java.lang.Exception {
            super.streamMessageQNotWritable();
        }


}