package com.sun.ts.tests.jms.core.bytesMsgQueue;

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
public class BytesMsgQueueTestsServletTest extends com.sun.ts.tests.jms.core.bytesMsgQueue.BytesMsgQueueTests {
    static final String VEHICLE_ARCHIVE = "bytesMsgQueue_servlet_vehicle";

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
        War:

        /com/sun/ts/tests/jms/core/bytesMsgQueue/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive bytesMsgQueue_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "bytesMsgQueue_servlet_vehicle_web.war");
            // The class files
            bytesMsgQueue_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.core.bytesMsgQueue.BytesMsgQueueTests.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = BytesMsgQueueTests.class.getResource("/com/sun/ts/tests/jms/core/bytesMsgQueue/servlet_vehicle_web.xml");
            if(warResURL != null) {
              bytesMsgQueue_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = BytesMsgQueueTests.class.getResource("/com/sun/ts/tests/jms/core/bytesMsgQueue/bytesMsgQueue_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              bytesMsgQueue_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }


           // Call the archive processor
           archiveProcessor.processWebArchive(bytesMsgQueue_servlet_vehicle_web, BytesMsgQueueTests.class, warResURL);

        // Ear
            EnterpriseArchive bytesMsgQueue_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "bytesMsgQueue_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            bytesMsgQueue_servlet_vehicle_ear.addAsModule(bytesMsgQueue_servlet_vehicle_web);



        return bytesMsgQueue_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void bytesMsgNullStreamQTest() throws java.lang.Exception {
            super.bytesMsgNullStreamQTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void bytesMessageQTestsFullMsg() throws java.lang.Exception {
            super.bytesMessageQTestsFullMsg();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void bytesMessageQNotWriteable() throws java.lang.Exception {
            super.bytesMessageQNotWriteable();
        }


}