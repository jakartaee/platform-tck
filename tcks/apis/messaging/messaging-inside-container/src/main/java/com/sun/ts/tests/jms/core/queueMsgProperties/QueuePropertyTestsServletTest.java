package com.sun.ts.tests.jms.core.queueMsgProperties;

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
public class QueuePropertyTestsServletTest extends com.sun.ts.tests.jms.core.queueMsgProperties.QueuePropertyTests {
    static final String VEHICLE_ARCHIVE = "queueMsgProperties_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        queueMsgProperties_appclient_vehicle: 
        queueMsgProperties_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        queueMsgProperties_ejb_vehicle: 
        queueMsgProperties_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        queueMsgProperties_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        queueMsgProperties_jsp_vehicle: 
        queueMsgProperties_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        queueMsgProperties_servlet_vehicle: 
        queueMsgProperties_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/queueMsgProperties/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive queueMsgProperties_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "queueMsgProperties_servlet_vehicle_web.war");
            // The class files
            queueMsgProperties_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.core.queueMsgProperties.QueuePropertyTests.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = QueuePropertyTests.class.getResource("/com/sun/ts/tests/jms/core/queueMsgProperties/servlet_vehicle_web.xml");
            if(warResURL != null) {
              queueMsgProperties_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = QueuePropertyTests.class.getResource("/com/sun/ts/tests/jms/core/queueMsgProperties/queueMsgProperties_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              queueMsgProperties_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }


           // Call the archive processor
           archiveProcessor.processWebArchive(queueMsgProperties_servlet_vehicle_web, QueuePropertyTests.class, warResURL);

        // Ear
            EnterpriseArchive queueMsgProperties_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "queueMsgProperties_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            queueMsgProperties_servlet_vehicle_ear.addAsModule(queueMsgProperties_servlet_vehicle_web);



        return queueMsgProperties_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgPropertiesQTest() throws java.lang.Exception {
            super.msgPropertiesQTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgPropertiesConversionQTest() throws java.lang.Exception {
            super.msgPropertiesConversionQTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void msgPropertyExistTest() throws java.lang.Exception {
            super.msgPropertyExistTest();
        }


}