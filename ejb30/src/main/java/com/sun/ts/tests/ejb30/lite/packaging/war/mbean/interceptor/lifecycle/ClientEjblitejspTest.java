package com.sun.ts.tests.ejb30.lite.packaging.war.mbean.interceptor.lifecycle;

import com.sun.ts.tests.ejb30.lite.packaging.war.mbean.interceptor.lifecycle.Client;
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
public class ClientEjblitejspTest extends com.sun.ts.tests.ejb30.lite.packaging.war.mbean.interceptor.lifecycle.Client {
    static final String VEHICLE_ARCHIVE = "ejblite_packaging_war_mbean_interceptor_lifecycle_ejblitejsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_packaging_war_mbean_interceptor_lifecycle_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_packaging_war_mbean_interceptor_lifecycle_ejblitejsp_vehicle_web: 
        ejblite_packaging_war_mbean_interceptor_lifecycle_ejbliteservlet_vehicle_web: WEB-INF/web.xml
        ejblite_packaging_war_mbean_interceptor_lifecycle_ejbliteservlet2_vehicle_web: WEB-INF/web.xml
        ejblite_packaging_war_mbean_interceptor_lifecyclecdi_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_packaging_war_mbean_interceptor_lifecyclecdi_ejblitejsp_vehicle_web: WEB-INF/beans.xml
        ejblite_packaging_war_mbean_interceptor_lifecyclecdi_ejbliteservlet_vehicle_web: WEB-INF/beans.xml,WEB-INF/web.xml
        ejblite_packaging_war_mbean_interceptor_lifecyclecdi_ejbliteservlet2_vehicle_web: WEB-INF/beans.xml,WEB-INF/web.xml
        ejblite_packaging_war_mbean_interceptor_lifecycleejbcdi_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_packaging_war_mbean_interceptor_lifecycleejbcdi_ejblitejsp_vehicle_web: WEB-INF/beans.xml
        ejblite_packaging_war_mbean_interceptor_lifecycleejbcdi_ejbliteservlet_vehicle_web: WEB-INF/beans.xml,WEB-INF/web.xml
        ejblite_packaging_war_mbean_interceptor_lifecycleejbcdi_ejbliteservlet2_vehicle_web: WEB-INF/beans.xml,WEB-INF/web.xml

        Found Descriptors:
        War:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
            // the war with the correct archive name
            WebArchive ejblite_packaging_war_mbean_interceptor_lifecycle_ejblitejsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_packaging_war_mbean_interceptor_lifecycle_ejblitejsp_vehicle_web.war");
            // The class files
            ejblite_packaging_war_mbean_interceptor_lifecycle_ejblitejsp_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.lite.packaging.war.mbean.interceptor.lifecycle.InterceptorOverride34Bean.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor3.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.InterceptorBeanBase.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor5.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.mbean.interceptor.lifecycle.JsfClient.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.InterceptorBaseBase.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor8.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.mbean.interceptor.lifecycle.InterceptorBean.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor1.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.mbean.interceptor.lifecycle.HttpServletDelegate.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.mbean.interceptor.lifecycle.InterceptorOverrideBean.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor6.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor4.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.ClientBase.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.mbean.interceptor.lifecycle.Client.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.InterceptorBase.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor9.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.HistorySingletonBean.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor7.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.mbean.interceptor.lifecycle.AManagedBean.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.InterceptorOverrideBase.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.mbean.interceptor.lifecycle.EJBLiteJSPTag.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.InterceptorIF.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor2.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.JsfClientBase.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/vehicle/ejblitejsp/ejblitejsp_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_packaging_war_mbean_interceptor_lifecycle_ejblitejsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("//vehicle/ejblitejsp/ejblitejsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_packaging_war_mbean_interceptor_lifecycle_ejblitejsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/lite/packaging/war/mbean/interceptor/lifecycle/ejblitejsp.tld");
            if(warResURL != null) {
              ejblite_packaging_war_mbean_interceptor_lifecycle_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/tlds/ejblitejsp.tld");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsp/ejblitejsp.tld");
            if(warResURL != null) {
              ejblite_packaging_war_mbean_interceptor_lifecycle_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/ejblitejsp.tld");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsp/ejblitejsp_vehicle.jsp");
            if(warResURL != null) {
              ejblite_packaging_war_mbean_interceptor_lifecycle_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/ejblitejsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_packaging_war_mbean_interceptor_lifecycle_ejblitejsp_vehicle_web, Client.class, warResURL);

        return ejblite_packaging_war_mbean_interceptor_lifecycle_ejblitejsp_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void allInterceptors() {
            super.allInterceptors();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void overrideBeanInterceptorMethod() {
            super.overrideBeanInterceptorMethod();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void overrideBeanInterceptorMethod3() {
            super.overrideBeanInterceptorMethod3();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void identityHashCode() {
            super.identityHashCode();
        }


}