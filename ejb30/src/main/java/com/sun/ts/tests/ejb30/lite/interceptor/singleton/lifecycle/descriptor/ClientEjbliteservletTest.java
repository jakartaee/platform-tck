package com.sun.ts.tests.ejb30.lite.interceptor.singleton.lifecycle.descriptor;

import com.sun.ts.tests.ejb30.lite.interceptor.singleton.lifecycle.descriptor.Client;
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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("ejb_web")
@Tag("web")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientEjbliteservletTest extends com.sun.ts.tests.ejb30.lite.interceptor.singleton.lifecycle.descriptor.Client {
    static final String VEHICLE_ARCHIVE = "ejblite_interceptor_singleton_lifecycle_descriptor_ejbliteservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_interceptor_singleton_lifecycle_descriptor_ejblitejsf_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_interceptor_singleton_lifecycle_descriptor_ejblitejsp_vehicle_web: WEB-INF/ejb-jar.xml
        ejblite_interceptor_singleton_lifecycle_descriptor_ejbliteservlet_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml
        ejblite_interceptor_singleton_lifecycle_descriptor_ejbliteservlet2_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml

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
            WebArchive ejblite_interceptor_singleton_lifecycle_descriptor_ejbliteservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_interceptor_singleton_lifecycle_descriptor_ejbliteservlet_vehicle_web.war");
            // The class files
            ejblite_interceptor_singleton_lifecycle_descriptor_ejbliteservlet_vehicle_web.addClasses(
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor3.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.InterceptorBeanBase.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor5.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.InterceptorBaseBase.class,
            com.sun.ts.tests.ejb30.lite.interceptor.singleton.lifecycle.descriptor.InterceptorOverride34Bean.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.InterceptorA.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor8.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor1.class,
            com.sun.ts.tests.ejb30.lite.interceptor.singleton.lifecycle.descriptor.InterceptorBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.lite.interceptor.singleton.lifecycle.descriptor.EJBLiteServletVehicle.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.lite.interceptor.singleton.lifecycle.descriptor.Client.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor6.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor4.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.ClientBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.InterceptorBase.class,
            com.sun.ts.tests.ejb30.lite.interceptor.singleton.lifecycle.descriptor.InterceptorOverrideBean.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor9.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.HistorySingletonBean.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor7.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.InterceptorOverrideBase.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.InterceptorIF.class,
            com.sun.ts.tests.ejb30.lite.interceptor.singleton.lifecycle.descriptor.AroundConstructInterceptorBean.class,
            com.sun.ts.tests.ejb30.lite.interceptor.singleton.lifecycle.descriptor.JsfClient.class,
            com.sun.ts.tests.ejb30.lite.interceptor.singleton.lifecycle.descriptor.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor2.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.JsfClientBase.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_interceptor_singleton_lifecycle_descriptor_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/ejbliteservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_interceptor_singleton_lifecycle_descriptor_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/lite/interceptor/singleton/lifecycle/descriptor/ejb-jar.xml");
            if(warResURL != null) {
              ejblite_interceptor_singleton_lifecycle_descriptor_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejb-jar.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet/ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_interceptor_singleton_lifecycle_descriptor_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_interceptor_singleton_lifecycle_descriptor_ejbliteservlet_vehicle_web, Client.class, warResURL);

        return ejblite_interceptor_singleton_lifecycle_descriptor_ejbliteservlet_vehicle_web;
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
        public void overrideBeanInterceptorMethod() {
            super.overrideBeanInterceptorMethod();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void overrideBeanInterceptorMethod3() {
            super.overrideBeanInterceptorMethod3();
        }


}