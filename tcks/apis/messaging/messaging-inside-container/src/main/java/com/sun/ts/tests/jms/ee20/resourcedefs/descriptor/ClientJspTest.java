package com.sun.ts.tests.jms.ee20.resourcedefs.descriptor;

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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@Disabled("https://github.com/jakartaee/platform-tck/issues/2231")
@ExtendWith(ArquillianExtension.class)
@Tag("jms")
@Tag("jms_optional")
@Tag("platform_optional")
@Tag("web_optional")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientJspTest extends com.sun.ts.tests.jms.ee20.resourcedefs.descriptor.Client {
    static final String VEHICLE_ARCHIVE = "resourcedefs_descriptor_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        resourcedefs_descriptor_appclient_vehicle: 
        resourcedefs_descriptor_appclient_vehicle_client: META-INF/application-client.xml
        resourcedefs_descriptor_ejb_vehicle: 
        resourcedefs_descriptor_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        resourcedefs_descriptor_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        resourcedefs_descriptor_jsp_vehicle: 
        resourcedefs_descriptor_jsp_vehicle_web: WEB-INF/web.xml
        resourcedefs_descriptor_servlet_vehicle: 
        resourcedefs_descriptor_servlet_vehicle_web: WEB-INF/web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/ee20/resourcedefs/descriptor/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive resourcedefs_descriptor_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "resourcedefs_descriptor_jsp_vehicle_web.war");
            // The class files
            resourcedefs_descriptor_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.ee20.resourcedefs.descriptor.Client.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("xml/jsp_vehicle_web.xml");
            resourcedefs_descriptor_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            // The sun-web.xml descriptor
            // warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.war.sun-web.xml");
            // if(warResURL != null) {
            //   resourcedefs_descriptor_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            // }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              resourcedefs_descriptor_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              resourcedefs_descriptor_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(resourcedefs_descriptor_jsp_vehicle_web, Client.class, warResURL);

        // Ear
            EnterpriseArchive resourcedefs_descriptor_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "resourcedefs_descriptor_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            resourcedefs_descriptor_jsp_vehicle_ear.addAsModule(resourcedefs_descriptor_jsp_vehicle_web);



        return resourcedefs_descriptor_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendAndRecvQueueTest() throws java.lang.Exception {
            super.sendAndRecvQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void sendAndRecvTopicTest() throws java.lang.Exception {
            super.sendAndRecvTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void checkClientIDOnDurableConnFactoryTest() throws java.lang.Exception {
            super.checkClientIDOnDurableConnFactoryTest();
        }


}