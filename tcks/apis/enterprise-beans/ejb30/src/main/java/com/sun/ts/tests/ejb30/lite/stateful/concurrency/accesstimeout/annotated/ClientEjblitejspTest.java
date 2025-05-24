package com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.annotated;

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
public class ClientEjblitejspTest extends com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.annotated.Client {
    static final String VEHICLE_ARCHIVE = "ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsp_vehicle_web: 
        ejblite_stateful_concurrency_accesstimeout_annotated_ejbliteservlet_vehicle_web: WEB-INF/web.xml
        ejblite_stateful_concurrency_accesstimeout_annotated_ejbliteservlet2_vehicle_web: WEB-INF/web.xml

        Found Descriptors:
        War:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
            // the war with the correct archive name
            WebArchive ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsp_vehicle_web.war");
            // The class files
            ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsp_vehicle_web.addClasses(
            Fault.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.annotated.JsfClient.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.Pinger.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.annotated.BeanClassMethodLevelAccessTimeoutBean.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.JsfClientBase.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyClientBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.MethodLevelAnnotatedAccessTimeoutBeanBase.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyJsfClientBase.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.annotated.BeanClassLevelAccessTimeoutBean.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.ClientBase.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.PlainAccessTimeoutBeanBase.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.annotated.AnnotatedSuperClassAccessTimeoutBean.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.annotated.EJBLiteJSPTag.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.annotated.BeanClassMethodLevelOverrideAccessTimeoutBean.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.annotated.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.ClassLevelAnnotatedAccessTimeoutBeanBase.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            SetupException.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.annotated.Client.class
            );

            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/vehicle/ejblitejsp/ejblitejsp_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("//vehicle/ejblitejsp/ejblitejsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/lite/stateful/concurrency/accesstimeout/annotated/ejblitejsp.tld");
            if(warResURL != null) {
              ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/tlds/ejblitejsp.tld");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsp/ejblitejsp.tld");
            if(warResURL != null) {
              ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/ejblitejsp.tld");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsp/ejblitejsp_vehicle.jsp");
            if(warResURL != null) {
              ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/ejblitejsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsp_vehicle_web, Client.class, warResURL);

        return ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsp_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void beanClassLevel() throws java.lang.InterruptedException {
            super.beanClassLevel();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void beanClassLevel2() throws java.lang.InterruptedException {
            super.beanClassLevel2();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void beanSuperClassLevel() throws java.lang.InterruptedException {
            super.beanSuperClassLevel();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void beanSuperClassMethodLevel() throws java.lang.InterruptedException {
            super.beanSuperClassMethodLevel();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void beanSuperClassMethodLevelOverride() throws java.lang.InterruptedException {
            super.beanSuperClassMethodLevelOverride();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void beanClassMethodLevel() throws java.lang.InterruptedException {
            super.beanClassMethodLevel();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void beanClassMethodLevelOverride() throws java.lang.InterruptedException {
            super.beanClassMethodLevelOverride();
        }


}