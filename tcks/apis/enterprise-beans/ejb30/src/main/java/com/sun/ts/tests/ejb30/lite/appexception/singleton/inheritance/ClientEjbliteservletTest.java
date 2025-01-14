package com.sun.ts.tests.ejb30.lite.appexception.singleton.inheritance;

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
public class ClientEjbliteservletTest extends com.sun.ts.tests.ejb30.lite.appexception.singleton.inheritance.Client {
    static final String VEHICLE_ARCHIVE = "ejblite_appexception_singleton_inherited_ejbliteservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_appexception_singleton_inherited_ejblitejsf_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_appexception_singleton_inherited_ejblitejsp_vehicle_web: WEB-INF/ejb-jar.xml
        ejblite_appexception_singleton_inherited_ejbliteservlet_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml
        ejblite_appexception_singleton_inherited_ejbliteservlet2_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml

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
            WebArchive ejblite_appexception_singleton_inherited_ejbliteservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_appexception_singleton_inherited_ejbliteservlet_vehicle_web.war");
            // The class files
            ejblite_appexception_singleton_inherited_ejbliteservlet_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.lite.appexception.common.inheritance.Exception2.class,
            com.sun.ts.tests.ejb30.lite.appexception.common.inheritance.JsfClientBase.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.lite.appexception.common.inheritance.ClientBase.class,
            com.sun.ts.tests.ejb30.lite.appexception.common.inheritance.InheritanceBeanBase.class,
            com.sun.ts.tests.ejb30.lite.appexception.common.inheritance.Exception4.class,
            com.sun.ts.tests.ejb30.lite.appexception.common.inheritance.Exception7.class,
            com.sun.ts.tests.ejb30.lite.appexception.singleton.inheritance.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.common.appexception.AtUncheckedAppException.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.lite.appexception.singleton.inheritance.Client.class,
            com.sun.ts.tests.ejb30.lite.appexception.common.inheritance.Exception5.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.lite.appexception.common.inheritance.InheritanceIF.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.lite.appexception.singleton.inheritance.InheritanceBean.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.lite.appexception.singleton.inheritance.EJBLiteServletVehicle.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.appexception.common.inheritance.Exception3.class,
            com.sun.ts.tests.ejb30.lite.appexception.singleton.inheritance.JsfClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.lite.appexception.common.inheritance.Exception6.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb30.lite.appexception.common.inheritance.Exception1.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_appexception_singleton_inherited_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/ejbliteservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_appexception_singleton_inherited_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/lite/appexception/singleton/inheritance/ejb-jar.xml");
            if(warResURL != null) {
              ejblite_appexception_singleton_inherited_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejb-jar.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet/ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_appexception_singleton_inherited_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_appexception_singleton_inherited_ejbliteservlet_vehicle_web, Client.class, warResURL);

        return ejblite_appexception_singleton_inherited_ejbliteservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void uncheckedAppException1() {
            super.uncheckedAppException1();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void uncheckedAppException2() {
            super.uncheckedAppException2();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void uncheckedAppException3() {
            super.uncheckedAppException3();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void uncheckedSystemException4() {
            super.uncheckedSystemException4();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void uncheckedSystemException5() {
            super.uncheckedSystemException5();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void uncheckedAppException6() {
            super.uncheckedAppException6();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void uncheckedAppException7() {
            super.uncheckedAppException7();
        }


}