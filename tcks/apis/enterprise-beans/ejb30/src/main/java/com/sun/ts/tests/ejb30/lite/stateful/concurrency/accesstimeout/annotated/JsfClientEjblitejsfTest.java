package com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.annotated;

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
public class JsfClientEjblitejsfTest extends com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.annotated.JsfClient {
    static final String VEHICLE_ARCHIVE = "ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsf_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsp_vehicle_web: 
        ejblite_stateful_concurrency_accesstimeout_annotated_ejbliteservlet_vehicle_web: WEB-INF/web.xml
        ejblite_stateful_concurrency_accesstimeout_annotated_ejbliteservlet2_vehicle_web: WEB-INF/web.xml

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
            WebArchive ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsf_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsf_vehicle_web.war");
            // The class files
            ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsf_vehicle_web.addClasses(
            com.sun.ts.lib.harness.EETest.Fault.class,
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
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
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
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.annotated.BeanClassMethodLevelOverrideAccessTimeoutBean.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.annotated.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.ClassLevelAnnotatedAccessTimeoutBeanBase.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.annotated.Client.class
            );

            // The web.xml descriptor
            URL warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = JsfClient.class.getResource("//com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb30/lite/stateful/concurrency/accesstimeout/annotated/beans.xml");
            if(warResURL != null) {
              ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/beans.xml");
            if(warResURL != null) {
              ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejblitejsf_vehicle_web.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb30/lite/stateful/concurrency/accesstimeout/annotated/faces-config.xml");
            if(warResURL != null) {
              ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/faces-config.xml");
            if(warResURL != null) {
              ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle.xhtml");
            if(warResURL != null) {
              ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/ejblitejsf_vehicle.xhtml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsf_vehicle_web, JsfClient.class, warResURL);

        return ejblite_stateful_concurrency_accesstimeout_annotated_ejblitejsf_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void beanClassLevel() throws java.lang.InterruptedException {
            super.beanClassLevel();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void beanClassLevel2() throws java.lang.InterruptedException {
            super.beanClassLevel2();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void beanSuperClassLevel() throws java.lang.InterruptedException {
            super.beanSuperClassLevel();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void beanSuperClassMethodLevel() throws java.lang.InterruptedException {
            super.beanSuperClassMethodLevel();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void beanSuperClassMethodLevelOverride() throws java.lang.InterruptedException {
            super.beanSuperClassMethodLevelOverride();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void beanClassMethodLevel() throws java.lang.InterruptedException {
            super.beanClassMethodLevel();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void beanClassMethodLevelOverride() throws java.lang.InterruptedException {
            super.beanClassMethodLevelOverride();
        }


}