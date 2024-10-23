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
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class MapMsgQueueTestsAppclientTest extends com.sun.ts.tests.jms.core.mapMsgQueue.MapMsgQueueTests {
    static final String VEHICLE_ARCHIVE = "mapMsgQueue_appclient_vehicle";

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
        Client:

        /com/sun/ts/tests/jms/core/mapMsgQueue/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive mapMsgQueue_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "mapMsgQueue_appclient_vehicle_client.jar");
            // The class files
            mapMsgQueue_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.jms.core.mapMsgQueue.MapMsgQueueTests.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = MapMsgQueueTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              mapMsgQueue_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MapMsgQueueTests.class.getResource("mapMsgQueue_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mapMsgQueue_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mapMsgQueue_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + MapMsgQueueTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mapMsgQueue_appclient_vehicle_client, MapMsgQueueTests.class, resURL);

        // Ear
            EnterpriseArchive mapMsgQueue_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "mapMsgQueue_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mapMsgQueue_appclient_vehicle_ear.addAsModule(mapMsgQueue_appclient_vehicle_client);



        return mapMsgQueue_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void mapMessageFullMsgQTest() throws java.lang.Exception {
            super.mapMessageFullMsgQTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void MapMessageConversionQTestsBoolean() throws java.lang.Exception {
            super.MapMessageConversionQTestsBoolean();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void MapMessageConversionQTestsByte() throws java.lang.Exception {
            super.MapMessageConversionQTestsByte();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void MapMessageConversionQTestsShort() throws java.lang.Exception {
            super.MapMessageConversionQTestsShort();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void MapMessageConversionQTestsChar() throws java.lang.Exception {
            super.MapMessageConversionQTestsChar();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void MapMessageConversionQTestsInt() throws java.lang.Exception {
            super.MapMessageConversionQTestsInt();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void MapMessageConversionQTestsLong() throws java.lang.Exception {
            super.MapMessageConversionQTestsLong();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void MapMessageConversionQTestsFloat() throws java.lang.Exception {
            super.MapMessageConversionQTestsFloat();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void MapMessageConversionQTestsDouble() throws java.lang.Exception {
            super.MapMessageConversionQTestsDouble();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void MapMessageConversionQTestsString() throws java.lang.Exception {
            super.MapMessageConversionQTestsString();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void MapMessageConversionQTestsBytes() throws java.lang.Exception {
            super.MapMessageConversionQTestsBytes();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void MapMessageConversionQTestsInvFormatString() throws java.lang.Exception {
            super.MapMessageConversionQTestsInvFormatString();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void mapMessageQNotWritable() throws java.lang.Exception {
            super.mapMessageQNotWritable();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void mapMessageQIllegalarg() throws java.lang.Exception {
            super.mapMessageQIllegalarg();
        }


}