package com.sun.ts.tests.ejb30.lite.nointerface.annotated;

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
public class ClientEjbliteservletTest extends com.sun.ts.tests.ejb30.lite.nointerface.annotated.Client {
    static final String VEHICLE_ARCHIVE = "ejblite_nointerface_annotated_ejbliteservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_nointerface_annotated_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_nointerface_annotated_ejblitejsp_vehicle_web: 
        ejblite_nointerface_annotated_ejbliteservlet_vehicle_web: WEB-INF/web.xml
        ejblite_nointerface_annotated_ejbliteservlet2_vehicle_web: WEB-INF/web.xml

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
            WebArchive ejblite_nointerface_annotated_ejbliteservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_nointerface_annotated_ejbliteservlet_vehicle_web.war");
            // The class files
            ejblite_nointerface_annotated_ejbliteservlet_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.lite.nointerface.annotated.BeanBase.class,
            com.sun.ts.tests.ejb30.lite.nointerface.annotated.HasInterface.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.lite.nointerface.annotated.HasInterfaceSingletonBean.class,
            com.sun.ts.tests.ejb30.lite.nointerface.annotated.ClientBase.class,
            com.sun.ts.tests.ejb30.lite.nointerface.annotated.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.lite.nointerface.annotated.EJBLiteServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.lite.nointerface.annotated.JsfClientBase.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.ejb30.lite.nointerface.annotated.JsfClient.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.lite.nointerface.annotated.NoInterfaceStatefulBean.class,
            com.sun.ts.tests.ejb30.lite.nointerface.annotated.NoInterfaceSingletonBean.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.nointerface.annotated.NoInterfaceStatelessBean.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.lite.nointerface.annotated.Client.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_nointerface_annotated_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/ejbliteservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_nointerface_annotated_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet/ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_nointerface_annotated_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_nointerface_annotated_ejbliteservlet_vehicle_web, Client.class, warResURL);

        return ejblite_nointerface_annotated_ejbliteservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void nonBusinessMethods() {
            super.nonBusinessMethods();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void invokeRemovedStateful() {
            super.invokeRemovedStateful();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void passAsParam() {
            super.passAsParam();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void passAsReturn() {
            super.passAsReturn();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void passEnumAsParams() {
            super.passEnumAsParams();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void passEnumAsReturn() {
            super.passEnumAsReturn();
        }


}