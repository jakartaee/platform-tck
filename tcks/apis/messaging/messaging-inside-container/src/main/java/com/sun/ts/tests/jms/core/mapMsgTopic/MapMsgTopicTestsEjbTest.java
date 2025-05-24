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
public class MapMsgTopicTestsEjbTest extends com.sun.ts.tests.jms.core.mapMsgTopic.MapMsgTopicTests {
    static final String VEHICLE_ARCHIVE = "mapMsgTopic_ejb_vehicle";

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
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core/mapMsgTopic/ejb_vehicle_ejb.xml
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
            JavaArchive mapMsgTopic_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "mapMsgTopic_ejb_vehicle_client.jar");
            // The class files
            mapMsgTopic_ejb_vehicle_client.addClasses(
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
            com.sun.ts.tests.jms.core.mapMsgTopic.MapMsgTopicTests.class
            );
            // The application-client.xml descriptor
            URL resURL = MapMsgTopicTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              mapMsgTopic_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MapMsgTopicTests.class.getResource("mapMsgTopic_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mapMsgTopic_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            //mapMsgTopic_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + MapMsgTopicTests.class.getName() + "\n"), "MANIFEST.MF");
            mapMsgTopic_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mapMsgTopic_ejb_vehicle_client, MapMsgTopicTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive mapMsgTopic_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "mapMsgTopic_ejb_vehicle_ejb.jar");
            // The class files
            mapMsgTopic_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                Fault.class,
                com.sun.ts.tests.jms.core.mapMsgTopic.MapMsgTopicTests.class,
                EETest.class,
                ServiceEETest.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = MapMsgTopicTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              mapMsgTopic_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = MapMsgTopicTests.class.getResource("mapMsgTopic_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              mapMsgTopic_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mapMsgTopic_ejb_vehicle_ejb, MapMsgTopicTests.class, ejbResURL);

        // Ear
            EnterpriseArchive mapMsgTopic_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "mapMsgTopic_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mapMsgTopic_ejb_vehicle_ear.addAsModule(mapMsgTopic_ejb_vehicle_ejb);
            mapMsgTopic_ejb_vehicle_ear.addAsModule(mapMsgTopic_ejb_vehicle_client);



        return mapMsgTopic_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void mapMessageFullMsgTopicTest() throws java.lang.Exception {
            super.mapMessageFullMsgTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void MapMessageConversionTopicTestsBoolean() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsBoolean();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void MapMessageConversionTopicTestsByte() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsByte();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void MapMessageConversionTopicTestsShort() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsShort();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void MapMessageConversionTopicTestsChar() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsChar();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void MapMessageConversionTopicTestsInt() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsInt();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void MapMessageConversionTopicTestsLong() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsLong();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void MapMessageConversionTopicTestsFloat() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsFloat();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void MapMessageConversionTopicTestsDouble() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsDouble();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void MapMessageConversionTopicTestsString() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsString();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void MapMessageConversionTopicTestsBytes() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsBytes();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void MapMessageConversionTopicTestsInvFormatString() throws java.lang.Exception {
            super.MapMessageConversionTopicTestsInvFormatString();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void mapMessageTNotWritable() throws java.lang.Exception {
            super.mapMessageTNotWritable();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void mapMessageTIllegalarg() throws java.lang.Exception {
            super.mapMessageTIllegalarg();
        }


}