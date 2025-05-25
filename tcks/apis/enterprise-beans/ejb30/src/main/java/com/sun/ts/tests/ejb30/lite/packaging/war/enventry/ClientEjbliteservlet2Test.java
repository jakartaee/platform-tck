package com.sun.ts.tests.ejb30.lite.packaging.war.enventry;

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
public class ClientEjbliteservlet2Test extends com.sun.ts.tests.ejb30.lite.packaging.war.enventry.Client {
    static final String VEHICLE_ARCHIVE = "ejblite_packaging_war_enventry_ejbliteservlet2_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_packaging_war_enventry_ejblitejsp_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml
        ejblite_packaging_war_enventry_ejbliteservlet_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml
        ejblite_packaging_war_enventry_ejbliteservlet2_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/ejb30/lite/packaging/war/enventry/ejbliteservlet2_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle_web.xml
        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
            // the war with the correct archive name
            WebArchive ejblite_packaging_war_enventry_ejbliteservlet2_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_packaging_war_enventry_ejbliteservlet2_vehicle_web.war");
            // The class files
            ejblite_packaging_war_enventry_ejbliteservlet2_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.lite.packaging.war.enventry.Interceptor2.class,
            Fault.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.enventry.Interceptor0.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.enventry.TwoBean.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.enventry.EJBLiteServlet2Filter.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.enventry.HttpServletDelegate.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.enventry.Interceptor1.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.enventry.ComponentBase.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.enventry.Interceptor3.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.enventry.ThreeBean.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.enventry.InterceptorBase.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.enventry.Client.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.enventry.BeanBase.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.enventry.OneBean.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            SetupException.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("ejbliteservlet2_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_packaging_war_enventry_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/ejbliteservlet2_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_packaging_war_enventry_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/lite/packaging/war/enventry/ejb-jar.xml");
            if(warResURL != null) {
              ejblite_packaging_war_enventry_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejb-jar.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_packaging_war_enventry_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet2_vehicle_web.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/lite/packaging/war/enventry/ejbliteservlet2_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_packaging_war_enventry_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet2_vehicle_web.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle.jsp");
            if(warResURL != null) {
              ejblite_packaging_war_enventry_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/ejbliteservlet2_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_packaging_war_enventry_ejbliteservlet2_vehicle_web, Client.class, warResURL);

        return ejblite_packaging_war_enventry_ejbliteservlet2_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void injectedIntoClient() {
            super.injectedIntoClient();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void injectedIntoOneBean() {
            super.injectedIntoOneBean();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void injectedIntoTwoBean() {
            super.injectedIntoTwoBean();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void injectedIntoThreeBean() {
            super.injectedIntoThreeBean();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void injectedIntoOneBeanInterceptors() {
            super.injectedIntoOneBeanInterceptors();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void injectedIntoTwoBeanInterceptors() {
            super.injectedIntoTwoBeanInterceptors();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void injectedIntoThreeBeanInterceptors() {
            super.injectedIntoThreeBeanInterceptors();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void appNameModuleName() {
            super.appNameModuleName();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void appNameModuleNameFromEJB() {
            super.appNameModuleNameFromEJB();
        }


}