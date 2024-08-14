package com.sun.ts.tests.ejb32.lite.timer.schedule.expression.annotated;

import com.sun.ts.tests.ejb32.lite.timer.schedule.expression.annotated.JsfClient;
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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("ejb_web_profile")
@Tag("web")

public class JsfClientEjbliteservletTest extends com.sun.ts.tests.ejb32.lite.timer.schedule.expression.annotated.JsfClient {
    static final String VEHICLE_ARCHIVE = "ejb32_lite_timer_schedule_expression_annotated_ejbliteservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejb32_lite_timer_schedule_expression_annotated_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejb32_lite_timer_schedule_expression_annotated_ejblitejsp_vehicle_web: 
        ejb32_lite_timer_schedule_expression_annotated_ejbliteservlet_vehicle_web: WEB-INF/web.xml
        ejb32_lite_timer_schedule_expression_annotated_ejbliteservlet2_vehicle_web: WEB-INF/web.xml

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
            WebArchive ejb32_lite_timer_schedule_expression_annotated_ejbliteservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejb32_lite_timer_schedule_expression_annotated_ejbliteservlet_vehicle_web.war");
            // The class files
            ejb32_lite_timer_schedule_expression_annotated_ejbliteservlet_vehicle_web.addClasses(
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb32.lite.timer.schedule.expression.annotated.JsfClient.class,
            com.sun.ts.tests.ejb30.timer.common.JsfClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.TimerBeanBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb32.lite.timer.schedule.expression.annotated.Client.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.ClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb32.lite.timer.schedule.expression.annotated.EJBLiteServletVehicle.class,
            com.sun.ts.tests.ejb32.lite.timer.schedule.expression.annotated.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.timer.common.TimerInfo.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.timer.common.TimeoutStatusBean.class,
            com.sun.ts.tests.ejb30.timer.common.ScheduleValues.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.class,
            com.sun.ts.tests.ejb30.timer.common.TimerUtil.class,
            com.sun.ts.tests.ejb32.lite.timer.schedule.expression.annotated.ScheduleBean.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The web.xml descriptor
            URL warResURL = JsfClient.class.getResource("ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejb32_lite_timer_schedule_expression_annotated_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = JsfClient.class.getResource("/ejbliteservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejb32_lite_timer_schedule_expression_annotated_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
           archiveProcessor.processWebArchive(ejb32_lite_timer_schedule_expression_annotated_ejbliteservlet_vehicle_web, JsfClient.class, warResURL);

        return ejb32_lite_timer_schedule_expression_annotated_ejbliteservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void validSecondValuesInt() {
            super.validSecondValuesInt();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void validSecondValuesString() {
            super.validSecondValuesString();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void validMinuteValuesInt() {
            super.validMinuteValuesInt();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void validMinuteValuesString() {
            super.validMinuteValuesString();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void validHourValuesInt() {
            super.validHourValuesInt();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void validHourValuesString() {
            super.validHourValuesString();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void validMonthValuesInt() {
            super.validMonthValuesInt();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void validMonthValuesString() {
            super.validMonthValuesString();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void validYearValuesInt() {
            super.validYearValuesInt();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void validYearValuesString() {
            super.validYearValuesString();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void validDayOfMonthValuesInt() {
            super.validDayOfMonthValuesInt();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void validDayOfMonthValuesString() {
            super.validDayOfMonthValuesString();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void validDayOfWeekValuesInt() {
            super.validDayOfWeekValuesInt();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void validDayOfWeekValuesString() {
            super.validDayOfWeekValuesString();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void invalidSecondValuesInt() {
            super.invalidSecondValuesInt();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void invalidSecondValuesString() {
            super.invalidSecondValuesString();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void invalidMinuteValuesInt() {
            super.invalidMinuteValuesInt();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void invalidMinuteValuesString() {
            super.invalidMinuteValuesString();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void invalidHourValuesInt() {
            super.invalidHourValuesInt();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void invalidHourValuesString() {
            super.invalidHourValuesString();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void invalidMonthValuesInt() {
            super.invalidMonthValuesInt();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void invalidMonthValuesString() {
            super.invalidMonthValuesString();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void invalidYearValuesInt() {
            super.invalidYearValuesInt();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void invalidYearValuesString() {
            super.invalidYearValuesString();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void invalidDayOfMonthValuesInt() {
            super.invalidDayOfMonthValuesInt();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void invalidDayOfMonthValuesString() {
            super.invalidDayOfMonthValuesString();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void invalidDayOfWeekValuesInt() {
            super.invalidDayOfWeekValuesInt();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void invalidDayOfWeekValuesString() {
            super.invalidDayOfWeekValuesString();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void leapYear() {
            super.leapYear();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void attributeDefaults() {
            super.attributeDefaults();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void dayOfMonthOverDayOfWeek() {
            super.dayOfMonthOverDayOfWeek();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void dayOfWeekOverDayOfMonth() {
            super.dayOfWeekOverDayOfMonth();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void dayOfMonthAndDayOfWeek() {
            super.dayOfMonthAndDayOfWeek();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void validStart() {
            super.validStart();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void validEnd() {
            super.validEnd();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void validStartEnd() {
            super.validStartEnd();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void validTimeZone() {
            super.validTimeZone();
        }


}