package com.sun.ts.tests.ejb32.lite.timer.schedule.lifecycle;

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
@Tag("web_optional")
@Tag("ejb_persistent_timer_optional")
@Tag("tck-javatest")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientEjbliteservlet2Test extends com.sun.ts.tests.ejb32.lite.timer.schedule.lifecycle.Client {
    static final String VEHICLE_ARCHIVE = "ejb32_lite_timer_schedule_lifecycle_ejbliteservlet2_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejb32_lite_timer_schedule_lifecycle_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejb32_lite_timer_schedule_lifecycle_ejblitejsp_vehicle_web: 
        ejb32_lite_timer_schedule_lifecycle_ejbliteservlet_vehicle_web: WEB-INF/web.xml
        ejb32_lite_timer_schedule_lifecycle_ejbliteservlet2_vehicle_web: WEB-INF/web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/common/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle_web.xml
        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
            // the war with the correct archive name
            WebArchive ejb32_lite_timer_schedule_lifecycle_ejbliteservlet2_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejb32_lite_timer_schedule_lifecycle_ejbliteservlet2_vehicle_web.war");
            // The class files
            ejb32_lite_timer_schedule_lifecycle_ejbliteservlet2_vehicle_web.addClasses(
            Fault.class,
            com.sun.ts.tests.ejb32.lite.timer.schedule.lifecycle.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.timer.common.JsfClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.TimerBeanBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.ClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.ejb32.lite.timer.schedule.lifecycle.EJBLiteServlet2Filter.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.timer.common.TimerInfo.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb32.lite.timer.schedule.lifecycle.ScheduleBean.class,
            com.sun.ts.tests.ejb30.timer.common.TimeoutStatusBean.class,
            com.sun.ts.tests.ejb30.timer.common.ScheduleValues.class,
            com.sun.ts.tests.ejb32.lite.timer.schedule.lifecycle.Client.class,
            com.sun.ts.tests.ejb32.lite.timer.schedule.lifecycle.JsfClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.class,
            com.sun.ts.tests.ejb30.timer.common.TimerUtil.class,
            SetupException.class
            );
            // common lang3 classes
            ejb32_lite_timer_schedule_lifecycle_ejbliteservlet2_vehicle_web.addClasses(
                    org.apache.commons.lang3.StringUtils.class,
                    org.apache.commons.lang3.time.DateUtils.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("ejbliteservlet2_vehicle_web.xml");
            if(warResURL != null) {
              ejb32_lite_timer_schedule_lifecycle_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/ejbliteservlet2_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejb32_lite_timer_schedule_lifecycle_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet2/EJBLiteServlet2Filter.java.txt");
            if(warResURL != null) {
              ejb32_lite_timer_schedule_lifecycle_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/EJBLiteServlet2Filter.java.txt");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle_web.xml");
            if(warResURL != null) {
              ejb32_lite_timer_schedule_lifecycle_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet2_vehicle_web.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle.jsp");
            if(warResURL != null) {
              ejb32_lite_timer_schedule_lifecycle_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/ejbliteservlet2_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejb32_lite_timer_schedule_lifecycle_ejbliteservlet2_vehicle_web, Client.class, warResURL);

        return ejb32_lite_timer_schedule_lifecycle_ejbliteservlet2_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void timerHandle() throws java.io.IOException, java.lang.ClassNotFoundException {
            super.timerHandle();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void timerHandleIllegalStateException() {
            super.timerHandleIllegalStateException();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void isCalendarTimerAndGetSchedule() {
            super.isCalendarTimerAndGetSchedule();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void timerEquals() {
            super.timerEquals();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void createAndComplete() {
            super.createAndComplete();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void completeAndNoSuchObjectLocalException() {
            super.completeAndNoSuchObjectLocalException();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void cancelAndNoSuchObjectLocalException() {
            super.cancelAndNoSuchObjectLocalException();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void cancelWithTxAndNoSuchObjectLocalException() {
            super.cancelWithTxAndNoSuchObjectLocalException();
        }


}