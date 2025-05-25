package com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout;

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
public class ClientEjbliteservlet2Test extends com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.Client {
    static final String VEHICLE_ARCHIVE = "ejblite_singleton_concurrency_container_accesstimeout_ejbliteservlet2_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_singleton_concurrency_container_accesstimeout_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_singleton_concurrency_container_accesstimeout_ejblitejsp_vehicle_web: 
        ejblite_singleton_concurrency_container_accesstimeout_ejbliteservlet_vehicle_web: WEB-INF/web.xml
        ejblite_singleton_concurrency_container_accesstimeout_ejbliteservlet2_vehicle_web: WEB-INF/web.xml

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
            WebArchive ejblite_singleton_concurrency_container_accesstimeout_ejbliteservlet2_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_singleton_concurrency_container_accesstimeout_ejbliteservlet2_vehicle_web.war");
            // The class files
            ejblite_singleton_concurrency_container_accesstimeout_ejbliteservlet2_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.PlainAccessTimeoutBeanBase.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.TimeUnitBean.class,
            Fault.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.Client.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.MethodLevelCallbackAccessTimeoutBean.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.JsfClient.Read2Task.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.JsfClient.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.Client.Read2Task.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.JsfClient.ReadTask.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.ClassLevelCallbackAccessTimeoutBean.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.JsfClient.WriteTask.class,
            EETest.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.Client.WriteTask.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.Interceptor1.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.ClassLevelAccessTimeoutBean.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.KeepWaitOrNotAllowedBean.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.InheritAccessTimeoutBean.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.MethodLevelAccessTimeoutBean.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.EJBLiteServlet2Filter.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.CallbackAccessTimeoutBeanBase.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.AnnotatedAccessTimeoutBeanBase.class,
            SetupException.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.AccessTimeoutIF.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout.Client.ReadTask.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("ejbliteservlet2_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_singleton_concurrency_container_accesstimeout_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/ejbliteservlet2_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_singleton_concurrency_container_accesstimeout_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_singleton_concurrency_container_accesstimeout_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet2_vehicle_web.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle.jsp");
            if(warResURL != null) {
              ejblite_singleton_concurrency_container_accesstimeout_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/ejbliteservlet2_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_singleton_concurrency_container_accesstimeout_ejbliteservlet2_vehicle_web, Client.class, warResURL);

        return ejblite_singleton_concurrency_container_accesstimeout_ejbliteservlet2_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void longReadsClassLevel() {
            super.longReadsClassLevel();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void longReadsInherit() {
            super.longReadsInherit();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void longReadsMethodLevel() {
            super.longReadsMethodLevel();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void longReadsLongRead2sClassLevel() {
            super.longReadsLongRead2sClassLevel();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void longReadsLongRead2sInherit() {
            super.longReadsLongRead2sInherit();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void longReadsLongRead2sMethodLevel() {
            super.longReadsLongRead2sMethodLevel();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void longWritesClassLevel() {
            super.longWritesClassLevel();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void concurrencyNotAllowed() {
            super.concurrencyNotAllowed();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void keepWaiting() {
            super.keepWaiting();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void longWritesInherit() {
            super.longWritesInherit();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void longWritesMethodLevel() {
            super.longWritesMethodLevel();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void timeoutInterceptor() {
            super.timeoutInterceptor();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void timeUnits() {
            super.timeUnits();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void classLevelCallbackAccessTimeout() throws java.lang.Exception {
            super.classLevelCallbackAccessTimeout();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void methodLevelCallbackAccessTimeout() throws java.lang.Exception {
            super.methodLevelCallbackAccessTimeout();
        }


}