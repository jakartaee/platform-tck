package com.sun.ts.tests.ejb30.lite.stateful.timeout.annotated;

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
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

import java.net.URL;



@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("ejb_web")
@Tag("web")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientEjblitejspTest extends com.sun.ts.tests.ejb30.lite.stateful.timeout.annotated.Client {
    static final String VEHICLE_ARCHIVE = "ejblite_stateful_timeout_annotated_ejblitejsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_stateful_timeout_annotated_ejblitejsp_vehicle_web: 

        Found Descriptors:
        War:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
            // the war with the correct archive name
            WebArchive ejblite_stateful_timeout_annotated_ejblitejsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_stateful_timeout_annotated_ejblitejsp_vehicle_web.war");
            // The class files
            ejblite_stateful_timeout_annotated_ejblitejsp_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.lite.stateful.timeout.common.ClientBase.class,
            com.sun.ts.tests.ejb30.lite.stateful.timeout.common.StatefulTimeoutBeanBase.class,
            Fault.class,
            com.sun.ts.tests.ejb30.lite.stateful.timeout.annotated.HourUnitBean.class,
            com.sun.ts.tests.ejb30.lite.stateful.timeout.annotated.DayUnitBean.class,
            com.sun.ts.tests.ejb30.lite.stateful.timeout.annotated.SecondUnitBean.class,
            com.sun.ts.tests.ejb30.lite.stateful.timeout.annotated.MicrosecondUnitBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.lite.stateful.timeout.annotated.ZeroTimeoutBean.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.lite.stateful.timeout.common.StatefulTimeoutIF.class,
            com.sun.ts.tests.ejb30.lite.stateful.timeout.annotated.EJBLiteJSPTag.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.lite.stateful.timeout.annotated.NanosecondUnitBean.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.stateful.timeout.annotated.DefaultUnitBean.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.lite.stateful.timeout.common.StatefulTimeoutRemoteIF.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.lite.stateful.timeout.annotated.Minus1TimeoutBean.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            com.sun.ts.tests.ejb30.lite.stateful.timeout.annotated.MillisecondUnitBean.class,
            SetupException.class,
            com.sun.ts.tests.ejb30.lite.stateful.timeout.annotated.Client.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/vehicle/ejblitejsp/ejblitejsp_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_stateful_timeout_annotated_ejblitejsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("//vehicle/ejblitejsp/ejblitejsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_stateful_timeout_annotated_ejblitejsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/lite/stateful/timeout/annotated/ejblitejsp.tld");
            if(warResURL != null) {
              ejblite_stateful_timeout_annotated_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/tlds/ejblitejsp.tld");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsp/ejblitejsp.tld");
            if(warResURL != null) {
              ejblite_stateful_timeout_annotated_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/ejblitejsp.tld");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsp/ejblitejsp_vehicle.jsp");
            if(warResURL != null) {
              ejblite_stateful_timeout_annotated_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/ejblitejsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_stateful_timeout_annotated_ejblitejsp_vehicle_web, Client.class, warResURL);

        return ejblite_stateful_timeout_annotated_ejblitejsp_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void defaultUnitLocal() {
            super.defaultUnitLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void defaultUnitNoInterface() {
            super.defaultUnitNoInterface();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void secondUnitLocal() {
            super.secondUnitLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void secondUnitNoInterface() {
            super.secondUnitNoInterface();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void dayUnitLocal() {
            super.dayUnitLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void minus1Timeout() {
            super.minus1Timeout();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void hourUnitLocal() {
            super.hourUnitLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void millisecondUnitLocal() {
            super.millisecondUnitLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void microsecondUnitLocal() {
            super.microsecondUnitLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void nanosecondUnitLocal() {
            super.nanosecondUnitLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void zeroTimeout() {
            super.zeroTimeout();
        }


}