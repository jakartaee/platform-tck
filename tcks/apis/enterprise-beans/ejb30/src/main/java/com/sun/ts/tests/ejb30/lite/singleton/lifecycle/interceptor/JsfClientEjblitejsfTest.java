package com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor;

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
public class JsfClientEjblitejsfTest extends com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor.JsfClient {
    static final String VEHICLE_ARCHIVE = "ejblite_singleton_lifecycle_interceptor_ejblitejsf_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_singleton_lifecycle_interceptor_ejblitejsf_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_singleton_lifecycle_interceptor_ejblitejsp_vehicle_web: WEB-INF/ejb-jar.xml
        ejblite_singleton_lifecycle_interceptor_ejbliteservlet_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml
        ejblite_singleton_lifecycle_interceptor_ejbliteservlet2_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml

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
            WebArchive ejblite_singleton_lifecycle_interceptor_ejblitejsf_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_singleton_lifecycle_interceptor_ejblitejsf_vehicle_web.war");
            // The class files
            ejblite_singleton_lifecycle_interceptor_ejblitejsf_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor.Interceptor0.class,
            Fault.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor.CSingletonIF.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor.ASingletonBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.lite.singleton.common.SingletonInterceptorBase.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor.CSingletonBean.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor.Interceptor2.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor.CommonSingletonIF.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor.C2SingletonIF.class,
            EETest.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor.BeanBase.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor.Client.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor.Interceptor1.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor.JsfClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor.InterceptorBase.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            SetupException.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.interceptor.Interceptor3.class
            );

            // The web.xml descriptor
            URL warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_singleton_lifecycle_interceptor_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = JsfClient.class.getResource("//com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_singleton_lifecycle_interceptor_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb30/lite/singleton/lifecycle/interceptor/beans.xml");
            if(warResURL != null) {
              ejblite_singleton_lifecycle_interceptor_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/beans.xml");
            if(warResURL != null) {
              ejblite_singleton_lifecycle_interceptor_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb30/lite/singleton/lifecycle/interceptor/ejb-jar.xml");
            if(warResURL != null) {
              ejblite_singleton_lifecycle_interceptor_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejb-jar.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_singleton_lifecycle_interceptor_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejblitejsf_vehicle_web.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb30/lite/singleton/lifecycle/interceptor/faces-config.xml");
            if(warResURL != null) {
              ejblite_singleton_lifecycle_interceptor_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/faces-config.xml");
            if(warResURL != null) {
              ejblite_singleton_lifecycle_interceptor_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle.xhtml");
            if(warResURL != null) {
              ejblite_singleton_lifecycle_interceptor_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/ejblitejsf_vehicle.xhtml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_singleton_lifecycle_interceptor_ejblitejsf_vehicle_web, JsfClient.class, warResURL);

        return ejblite_singleton_lifecycle_interceptor_ejblitejsf_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void sameInterceptorInstanceA() {
            super.sameInterceptorInstanceA();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void sameInterceptorInstanceC() {
            super.sameInterceptorInstanceC();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void noDestructionAfterSystemExceptionA() {
            super.noDestructionAfterSystemExceptionA();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void noDestructionAfterSystemExceptionC() {
            super.noDestructionAfterSystemExceptionC();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void differentInterceptorInstance() {
            super.differentInterceptorInstance();
        }


}