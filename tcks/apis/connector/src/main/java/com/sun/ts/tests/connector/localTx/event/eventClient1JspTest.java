package com.sun.ts.tests.connector.localTx.event;

import java.net.URL;

import com.sun.ts.lib.harness.Fault;
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
@Tag("connector")
@Tag("platform")
@Tag("connector_standalone")
@Tag("connector_web")
@Tag("web_optional")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class eventClient1JspTest extends com.sun.ts.tests.connector.localTx.event.eventClient1 {
    static final String VEHICLE_ARCHIVE = "localTx_event_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        localTx_event_ejb_vehicle: 
        localTx_event_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        localTx_event_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        localTx_event_jsp_vehicle: 
        localTx_event_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        localTx_event_servlet_vehicle: 
        localTx_event_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/connector/localTx/event/localTx_event_jsp_vehicle_web.xml
        /com/sun/ts/tests/connector/localTx/event/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Rar:

        /com/sun/ts/tests/common/connector/whitebox/mdcomplete/ra-md-complete.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive localTx_event_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "localTx_event_jsp_vehicle_web.war");
            // The class files
            localTx_event_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.connector.localTx.event.eventClient1.class,
            com.sun.ts.tests.connector.util.DBSupport.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = eventClient1.class.getResource("localTx_event_jsp_vehicle_web.xml");
            if(warResURL != null) {
              localTx_event_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = eventClient1.class.getResource("localTx_event_jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              localTx_event_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = eventClient1.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            if(warResURL != null) {
              localTx_event_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            }
            warResURL = eventClient1.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            if(warResURL != null) {
              localTx_event_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(localTx_event_jsp_vehicle_web, eventClient1.class, warResURL);

        // Ear
            EnterpriseArchive localTx_event_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "localTx_event_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            localTx_event_jsp_vehicle_ear.addAsModule(localTx_event_jsp_vehicle_web);

            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(localTx_event_jsp_vehicle_ear, eventClient1.class, earResURL);
        return localTx_event_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testConnectionEventListener() throws java.lang.Exception {
            super.testConnectionEventListener();
        }


}