package com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.singleton.annotated;

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
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("ejb")
@Tag("ejb30")
@Tag("platform")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientEjbliteservletTest extends com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.singleton.annotated.Client {
    static final String VEHICLE_ARCHIVE = "ejb30_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejb30_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml

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
            WebArchive ejb30_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejb30_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet_vehicle_web.war");
            // The class files
            ejb30_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.Interceptor4.class,
            Fault.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.singleton.annotated.AroundTimeoutOverrideBean.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.singleton.annotated.AroundTimeoutExceptionBean.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.Interceptor6.class,
            com.sun.ts.tests.ejb30.timer.common.JsfClientBase.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.Interceptor2.class,
            com.sun.ts.tests.ejb30.timer.common.TimerBeanBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.singleton.annotated.AroundTimeoutBean.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.Interceptor1.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.ClientBase.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.ClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.MethodOverrideBeanBase.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.AroundTimeoutIF.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.AroundTimeoutBeanBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.timer.common.TimerInfo.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.Interceptor5.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.singleton.annotated.MethodOverrideBean.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.Interceptor3.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.singleton.annotated.MethodOverride2Bean.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.singleton.annotated.EJBLiteServletVehicle.class,
            com.sun.ts.tests.ejb30.timer.common.TimeoutStatusBean.class,
            com.sun.ts.tests.ejb30.timer.common.ScheduleValues.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.InterceptorBase.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.singleton.annotated.AroundTimeoutComplementBean.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.singleton.annotated.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.AroundTimeoutExceptionBeanBase.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.singleton.annotated.InvocationContextMethodsBean.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.class,
            com.sun.ts.tests.ejb30.timer.common.TimerUtil.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.singleton.annotated.Client.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            SetupException.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejb30_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/ejbliteservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejb30_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/timer/interceptor/aroundtimeout/singleton/annotated/ejb-jar.xml");
            if(warResURL != null) {
              ejb30_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejb-jar.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet/ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejb30_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejb30_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet_vehicle_web, Client.class, warResURL);

        return ejb30_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void aroundTimeoutExceptionAsBusinessMethod() throws java.lang.Exception {
            super.aroundTimeoutExceptionAsBusinessMethod();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void allInterceptors() {
            super.allInterceptors();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void allInterceptorsOverride() {
            super.allInterceptorsOverride();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void allInterceptorsComplement() {
            super.allInterceptorsComplement();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void aroundTimeoutMethod() {
            super.aroundTimeoutMethod();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void aroundTimeoutMethod2() {
            super.aroundTimeoutMethod2();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void aroundTimeoutException() {
            super.aroundTimeoutException();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void invocationContextMethods() {
            super.invocationContextMethods();
        }


}