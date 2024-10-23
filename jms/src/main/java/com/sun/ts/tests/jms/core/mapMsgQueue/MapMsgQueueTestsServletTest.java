package com.sun.ts.tests.jms.core.mapMsgQueue;

import com.sun.ts.tests.jms.core.mapMsgQueue.MapMsgQueueTests;
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
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class MapMsgQueueTestsServletTest extends com.sun.ts.tests.jms.core.mapMsgQueue.MapMsgQueueTests {
    static final String VEHICLE_ARCHIVE = "mapMsgQueue_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        mapMsgQueue_appclient_vehicle: 
        mapMsgQueue_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        mapMsgQueue_ejb_vehicle: 
        mapMsgQueue_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        mapMsgQueue_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        mapMsgQueue_jsp_vehicle: 
        mapMsgQueue_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        mapMsgQueue_servlet_vehicle: 
        mapMsgQueue_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/mapMsgQueue/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive mapMsgQueue_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "mapMsgQueue_servlet_vehicle_web.war");
            // The class files
            mapMsgQueue_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.jms.core.mapMsgQueue.MapMsgQueueTests.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
                                             com.sun.ts.tests.jms.core.mapMsgQueue.MapMsgQueueTests.class
            );
            // The web.xml descriptor
            URL warResURL = MapMsgQueueTests.class.getResource("/com/sun/ts/tests/jms/core/mapMsgQueue/servlet_vehicle_web.xml");
            if(warResURL != null) {
              mapMsgQueue_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = MapMsgQueueTests.class.getResource("/com/sun/ts/tests/jms/core/mapMsgQueue/mapMsgQueue_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              mapMsgQueue_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }


           // Call the archive processor
           archiveProcessor.processWebArchive(mapMsgQueue_servlet_vehicle_web, MapMsgQueueTests.class, warResURL);

        // Ear
            EnterpriseArchive mapMsgQueue_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "mapMsgQueue_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mapMsgQueue_servlet_vehicle_ear.addAsModule(mapMsgQueue_servlet_vehicle_web);



        return mapMsgQueue_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void mapMessageFullMsgQTest() throws java.lang.Exception {
            super.mapMessageFullMsgQTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionQTestsBoolean() throws java.lang.Exception {
            super.MapMessageConversionQTestsBoolean();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionQTestsByte() throws java.lang.Exception {
            super.MapMessageConversionQTestsByte();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionQTestsShort() throws java.lang.Exception {
            super.MapMessageConversionQTestsShort();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionQTestsChar() throws java.lang.Exception {
            super.MapMessageConversionQTestsChar();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionQTestsInt() throws java.lang.Exception {
            super.MapMessageConversionQTestsInt();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionQTestsLong() throws java.lang.Exception {
            super.MapMessageConversionQTestsLong();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionQTestsFloat() throws java.lang.Exception {
            super.MapMessageConversionQTestsFloat();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionQTestsDouble() throws java.lang.Exception {
            super.MapMessageConversionQTestsDouble();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionQTestsString() throws java.lang.Exception {
            super.MapMessageConversionQTestsString();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionQTestsBytes() throws java.lang.Exception {
            super.MapMessageConversionQTestsBytes();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void MapMessageConversionQTestsInvFormatString() throws java.lang.Exception {
            super.MapMessageConversionQTestsInvFormatString();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void mapMessageQNotWritable() throws java.lang.Exception {
            super.mapMessageQNotWritable();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void mapMessageQIllegalarg() throws java.lang.Exception {
            super.mapMessageQIllegalarg();
        }


}