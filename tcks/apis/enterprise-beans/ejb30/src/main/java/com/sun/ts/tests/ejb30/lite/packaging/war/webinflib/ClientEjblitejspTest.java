package com.sun.ts.tests.ejb30.lite.packaging.war.webinflib;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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
public class ClientEjblitejspTest extends com.sun.ts.tests.ejb30.lite.packaging.war.webinflib.Client {
    static final String VEHICLE_ARCHIVE = "ejblite_packaging_war_webinflib_ejblitejsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_packaging_war_webinflib_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_packaging_war_webinflib_ejblitejsp_vehicle_web: 
        ejblite_packaging_war_webinflib_ejbliteservlet_vehicle_web: WEB-INF/web.xml
        ejblite_packaging_war_webinflib_ejbliteservlet2_vehicle_web: WEB-INF/web.xml
        ejblite_packaging_war_webinflibonly_ejblitejsp_vehicle_web: 
        ejblite_packaging_war_webinflibonly_ejbliteservlet_vehicle_web: WEB-INF/web.xml
        ejblite_packaging_war_webinflibonly_ejbliteservlet2_vehicle_web: WEB-INF/web.xml

        Found Descriptors:
        War:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
            // the war with the correct archive name
            WebArchive ejblite_packaging_war_webinflib_ejblitejsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_packaging_war_webinflib_ejblitejsp_vehicle_web.war");
            // The class files
            ejblite_packaging_war_webinflib_ejblitejsp_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.webinflib.BeanBase.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.webinflib.ThreeBean.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.webinflib.Client.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.webinflib.EJBLiteJSPTag.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.webinflib.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.lite.packaging.war.webinflib.JsfClient.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/vehicle/ejblitejsp/ejblitejsp_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_packaging_war_webinflib_ejblitejsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("//vehicle/ejblitejsp/ejblitejsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_packaging_war_webinflib_ejblitejsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war
                URL libURL;
                JavaArchive x1_lib = ShrinkWrap.create(JavaArchive.class, "1.jar");
                    // The class files
                    x1_lib.addClasses(
                        com.sun.ts.tests.ejb30.lite.packaging.war.webinflib.OneBean.class,
                        com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
                        com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class
                    );


                ejblite_packaging_war_webinflib_ejblitejsp_vehicle_web.addAsLibrary(x1_lib);
                JavaArchive x2_lib = ShrinkWrap.create(JavaArchive.class, "2.jar");
                x2_lib.addClasses(com.sun.ts.tests.ejb30.lite.packaging.war.webinflib.TwoBean.class);
                ejblite_packaging_war_webinflib_ejblitejsp_vehicle_web.addAsLibrary(x2_lib);


            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/lite/packaging/war/webinflib/ejblitejsp.tld");
            if(warResURL != null) {
              ejblite_packaging_war_webinflib_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/tlds/ejblitejsp.tld");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsp/ejblitejsp.tld");
            if(warResURL != null) {
              ejblite_packaging_war_webinflib_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/ejblitejsp.tld");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsp/ejblitejsp_vehicle.jsp");
            if(warResURL != null) {
              ejblite_packaging_war_webinflib_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/ejblitejsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_packaging_war_webinflib_ejblitejsp_vehicle_web, Client.class, warResURL);

        return ejblite_packaging_war_webinflib_ejblitejsp_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void circularInjection() {
            super.circularInjection();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void sameClassLoader() {
            super.sameClassLoader();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void clientToBeanClassLookup() {
            super.clientToBeanClassLookup();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void beanClassToClientLookup() {
            super.beanClassToClientLookup();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void crossEJBLookup() {
            super.crossEJBLookup();
        }


}