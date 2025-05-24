package com.sun.ts.tests.jms.core.mapMsgTopic;

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
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
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
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class MapMsgTopicTestsServletTest extends com.sun.ts.tests.jms.core.mapMsgTopic.MapMsgTopicTests {
    static final String VEHICLE_ARCHIVE = "mapMsgTopic_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        mapMsgTopic_appclient_vehicle: 
        mapMsgTopic_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        mapMsgTopic_ejb_vehicle: 
        mapMsgTopic_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        mapMsgTopic_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        mapMsgTopic_jsp_vehicle: 
        mapMsgTopic_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        mapMsgTopic_servlet_vehicle: 
        mapMsgTopic_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/mapMsgTopic/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive mapMsgTopic_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "mapMsgTopic_servlet_vehicle_web.war");
            // The class files
            mapMsgTopic_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.core.mapMsgTopic.MapMsgTopicTests.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = MapMsgTopicTests.class.getResource("/com/sun/ts/tests/jms/core/mapMsgTopic/servlet_vehicle_web.xml");
            if(warResURL != null) {
              mapMsgTopic_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = MapMsgTopicTests.class.getResource("/com/sun/ts/tests/jms/core/mapMsgTopic/mapMsgTopic_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              mapMsgTopic_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }


           // Call the archive processor
           archiveProcessor.processWebArchive(mapMsgTopic_servlet_vehicle_web, MapMsgTopicTests.class, warResURL);

        // Ear
            EnterpriseArchive mapMsgTopic_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "mapMsgTopic_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mapMsgTopic_servlet_vehicle_ear.addAsModule(mapMsgTopic_servlet_vehicle_web);



        return mapMsgTopic_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void mapMessageFullMsgTopicTest() throws java.lang.Exception {
            super.mapMessageFullMsgTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionTopicTestsBoolean() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsBoolean();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionTopicTestsByte() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsByte();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionTopicTestsShort() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsShort();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionTopicTestsChar() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsChar();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionTopicTestsInt() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsInt();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionTopicTestsLong() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsLong();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionTopicTestsFloat() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsFloat();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionTopicTestsDouble() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsDouble();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionTopicTestsString() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsString();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionTopicTestsBytes() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsBytes();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionTopicTestsInvFormatString() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsInvFormatString();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void mapMessageTNotWritable() throws java.lang.Exception {
            super.mapMessageTNotWritable();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void mapMessageTIllegalarg() throws java.lang.Exception {
            super.mapMessageTIllegalarg();
        }


}