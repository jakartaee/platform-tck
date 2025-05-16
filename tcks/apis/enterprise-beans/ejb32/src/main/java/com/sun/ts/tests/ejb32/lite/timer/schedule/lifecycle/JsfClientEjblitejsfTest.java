package com.sun.ts.tests.ejb32.lite.timer.schedule.lifecycle;

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
public class JsfClientEjblitejsfTest extends com.sun.ts.tests.ejb32.lite.timer.schedule.lifecycle.JsfClient {
    static final String VEHICLE_ARCHIVE = "ejb32_lite_timer_schedule_lifecycle_ejblitejsf_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejb32_lite_timer_schedule_lifecycle_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejb32_lite_timer_schedule_lifecycle_ejblitejsp_vehicle_web: 
        ejb32_lite_timer_schedule_lifecycle_ejbliteservlet_vehicle_web: WEB-INF/web.xml
        ejb32_lite_timer_schedule_lifecycle_ejbliteservlet2_vehicle_web: WEB-INF/web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml
        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
            // the war with the correct archive name
            WebArchive ejb32_lite_timer_schedule_lifecycle_ejblitejsf_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejb32_lite_timer_schedule_lifecycle_ejblitejsf_vehicle_web.war");
            // The class files
            ejb32_lite_timer_schedule_lifecycle_ejblitejsf_vehicle_web.addClasses(
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb32.lite.timer.schedule.lifecycle.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.timer.common.JsfClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.TimerBeanBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.ClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
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
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            String[] activeMavenProfiles = {"staging"};
            ejb32_lite_timer_schedule_lifecycle_ejblitejsf_vehicle_web.addClasses(
                    org.apache.commons.lang3.StringUtils.class,
                    org.apache.commons.lang3.time.DateUtils.class
            );
            // The web.xml descriptor
            URL warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejb32_lite_timer_schedule_lifecycle_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = JsfClient.class.getResource("//com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejb32_lite_timer_schedule_lifecycle_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb32/lite/timer/schedule/lifecycle/beans.xml");
            if(warResURL != null) {
              ejb32_lite_timer_schedule_lifecycle_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/beans.xml");
            if(warResURL != null) {
              ejb32_lite_timer_schedule_lifecycle_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejb32_lite_timer_schedule_lifecycle_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejblitejsf_vehicle_web.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb32/lite/timer/schedule/lifecycle/faces-config.xml");
            if(warResURL != null) {
              ejb32_lite_timer_schedule_lifecycle_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/faces-config.xml");
            if(warResURL != null) {
              ejb32_lite_timer_schedule_lifecycle_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle.xhtml");
            if(warResURL != null) {
              ejb32_lite_timer_schedule_lifecycle_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/ejblitejsf_vehicle.xhtml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejb32_lite_timer_schedule_lifecycle_ejblitejsf_vehicle_web, JsfClient.class, warResURL);

        return ejb32_lite_timer_schedule_lifecycle_ejblitejsf_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void timerHandle() throws java.io.IOException, java.lang.ClassNotFoundException {
            super.timerHandle();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void timerHandleIllegalStateException() {
            super.timerHandleIllegalStateException();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void isCalendarTimerAndGetSchedule() {
            super.isCalendarTimerAndGetSchedule();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void timerEquals() {
            super.timerEquals();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void createAndComplete() {
            super.createAndComplete();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void completeAndNoSuchObjectLocalException() {
            super.completeAndNoSuchObjectLocalException();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void cancelAndNoSuchObjectLocalException() {
            super.cancelAndNoSuchObjectLocalException();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void cancelWithTxAndNoSuchObjectLocalException() {
            super.cancelWithTxAndNoSuchObjectLocalException();
        }


}