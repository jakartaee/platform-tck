package com.sun.ts.tests.ejb32.lite.timer.interceptor.aroundtimeout.singleton.annotated;

import com.sun.ts.tests.ejb32.lite.timer.interceptor.aroundtimeout.singleton.annotated.JsfClient;
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
@Tag("web_optional")
@Tag("ejb_persistent_timer_optional")

public class JsfClientEjbliteservlet2Test extends com.sun.ts.tests.ejb32.lite.timer.interceptor.aroundtimeout.singleton.annotated.JsfClient {
    static final String VEHICLE_ARCHIVE = "ejb32_lite_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet2_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejb32_lite_timer_interceptor_aroundtimeout_singleton_annotated_ejblitejsf_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejb32_lite_timer_interceptor_aroundtimeout_singleton_annotated_ejblitejsp_vehicle_web: WEB-INF/ejb-jar.xml
        ejb32_lite_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml
        ejb32_lite_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet2_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml

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
            WebArchive ejb32_lite_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet2_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejb32_lite_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet2_vehicle_web.war");
            // The class files
            ejb32_lite_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet2_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.Interceptor4.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb32.lite.timer.interceptor.aroundtimeout.singleton.annotated.AroundTimeoutExceptionBean.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.Interceptor6.class,
            com.sun.ts.tests.ejb32.lite.timer.interceptor.aroundtimeout.singleton.annotated.MethodOverrideBean.class,
            com.sun.ts.tests.ejb32.lite.timer.interceptor.aroundtimeout.singleton.annotated.JsfClient.class,
            com.sun.ts.tests.ejb30.timer.common.JsfClientBase.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.Interceptor2.class,
            com.sun.ts.tests.ejb30.timer.common.TimerBeanBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.JsfClientBase.class,
            com.sun.ts.tests.ejb32.lite.timer.interceptor.aroundtimeout.singleton.annotated.AroundTimeoutBean.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.Interceptor1.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.ClientBase.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.ClientBase.class,
            com.sun.ts.tests.ejb32.lite.timer.interceptor.aroundtimeout.singleton.annotated.EJBLiteServlet2Filter.class,
            com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.MethodOverrideBeanBase.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.AroundTimeoutIF.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.timer.common.TimerInfo.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.Interceptor5.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.Interceptor3.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb32.lite.timer.interceptor.aroundtimeout.singleton.annotated.AroundTimeoutOverrideBean.class,
            com.sun.ts.tests.ejb32.lite.timer.interceptor.aroundtimeout.singleton.annotated.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.timer.common.TimeoutStatusBean.class,
            com.sun.ts.tests.ejb32.lite.timer.interceptor.aroundtimeout.singleton.annotated.AroundTimeoutBeanBase.class,
            com.sun.ts.tests.ejb30.timer.common.ScheduleValues.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.InterceptorBase.class,
            com.sun.ts.tests.ejb32.lite.timer.interceptor.aroundtimeout.singleton.annotated.AroundTimeoutComplementBean.class,
            com.sun.ts.tests.ejb32.lite.timer.interceptor.aroundtimeout.singleton.annotated.MethodOverride2Bean.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb32.lite.timer.interceptor.aroundtimeout.singleton.annotated.Client.class,
            com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.AroundTimeoutExceptionBeanBase.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.class,
            com.sun.ts.tests.ejb30.timer.common.TimerUtil.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb32.lite.timer.interceptor.aroundtimeout.singleton.annotated.InvocationContextMethodsBean.class
            );
            // The web.xml descriptor
            URL warResURL = JsfClient.class.getResource("ejbliteservlet2_vehicle_web.xml");
            if(warResURL != null) {
              ejb32_lite_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = JsfClient.class.getResource("/ejbliteservlet2_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejb32_lite_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle.jsp");
            ejb32_lite_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/ejbliteservlet2_vehicle.jsp");

           archiveProcessor.processWebArchive(ejb32_lite_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet2_vehicle_web, JsfClient.class, warResURL);

        return ejb32_lite_timer_interceptor_aroundtimeout_singleton_annotated_ejbliteservlet2_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void aroundTimeoutExceptionAsBusinessMethod() throws java.lang.Exception {
            super.aroundTimeoutExceptionAsBusinessMethod();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void allInterceptors() {
            super.allInterceptors();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void allInterceptorsOverride() {
            super.allInterceptorsOverride();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void allInterceptorsComplement() {
            super.allInterceptorsComplement();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void aroundTimeoutMethod() {
            super.aroundTimeoutMethod();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void aroundTimeoutMethod2() {
            super.aroundTimeoutMethod2();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void aroundTimeoutException() {
            super.aroundTimeoutException();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void invocationContextMethods() {
            super.invocationContextMethods();
        }


}