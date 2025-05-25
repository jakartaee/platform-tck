package com.sun.ts.tests.ejb32.lite.timer.timerconfig;

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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

import java.net.URL;



@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("ejb_web_profile")
@Tag("web")
@Tag("tck-javatest")

public class ClientEjbliteservletTest extends com.sun.ts.tests.ejb32.lite.timer.timerconfig.Client {
    static final String VEHICLE_ARCHIVE = "ejb32_lite_timer_timerconfig_ejbliteservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejb32_lite_timer_timerconfig_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejb32_lite_timer_timerconfig_ejblitejsp_vehicle_web: 
        ejb32_lite_timer_timerconfig_ejbliteservlet_vehicle_web: WEB-INF/web.xml
        ejb32_lite_timer_timerconfig_ejbliteservlet2_vehicle_web: WEB-INF/web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/common/vehicle/ejbliteservlet/ejbliteservlet_vehicle_web.xml
        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
            // the war with the correct archive name
            WebArchive ejb32_lite_timer_timerconfig_ejbliteservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejb32_lite_timer_timerconfig_ejbliteservlet_vehicle_web.war");
            // The class files
            ejb32_lite_timer_timerconfig_ejbliteservlet_vehicle_web.addClasses(
            com.sun.ts.tests.ejb32.lite.timer.timerconfig.Client.class,
            Fault.class,
            com.sun.ts.tests.ejb32.lite.timer.timerconfig.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.timer.common.JsfClientBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.ClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod.class,
            com.sun.ts.tests.ejb32.lite.timer.timerconfig.TimerConfigBean.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.ejb32.lite.timer.timerconfig.JsfClient.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.timer.common.TimerInfo.class,
            com.sun.ts.tests.ejb32.lite.timer.timerconfig.TimerConfigIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.timer.common.TimeoutStatusBean.class,
            com.sun.ts.tests.ejb32.lite.timer.timerconfig.EJBLiteServletVehicle.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.TimerUtil.class,
            SetupException.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejb32_lite_timer_timerconfig_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/ejbliteservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejb32_lite_timer_timerconfig_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet/EJBLiteServletVehicle.java.txt");
            if(warResURL != null) {
              ejb32_lite_timer_timerconfig_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/EJBLiteServletVehicle.java.txt");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet/HttpServletDelegate.java.txt");
            if(warResURL != null) {
              ejb32_lite_timer_timerconfig_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/HttpServletDelegate.java.txt");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet/ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejb32_lite_timer_timerconfig_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejb32_lite_timer_timerconfig_ejbliteservlet_vehicle_web, Client.class, warResURL);

        return ejb32_lite_timer_timerconfig_ejbliteservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void isPersistent() {
            super.isPersistent();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void gettersSetters() {
            super.gettersSetters();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void illegalArgumentException() {
            super.illegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void resetTimerConfig() {
            super.resetTimerConfig();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void createTimerWithLong() {
            super.createTimerWithLong();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void createTimerWithLongRecurring() {
            super.createTimerWithLongRecurring();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void createTimerWithDate() {
            super.createTimerWithDate();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void createTimerWithDateRecurring() {
            super.createTimerWithDateRecurring();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void createTimerWithSchedule() {
            super.createTimerWithSchedule();
        }


}