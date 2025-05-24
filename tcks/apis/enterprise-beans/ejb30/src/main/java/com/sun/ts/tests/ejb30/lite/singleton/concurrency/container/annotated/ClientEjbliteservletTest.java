package com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.annotated;

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
public class ClientEjbliteservletTest extends com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.annotated.Client {
    static final String VEHICLE_ARCHIVE = "ejblite_singleton_concurrency_container_annotated_ejbliteservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_singleton_concurrency_container_annotated_ejblitejsf_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_singleton_concurrency_container_annotated_ejblitejsp_vehicle_web: WEB-INF/ejb-jar.xml
        ejblite_singleton_concurrency_container_annotated_ejbliteservlet_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml
        ejblite_singleton_concurrency_container_annotated_ejbliteservlet2_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml

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
            WebArchive ejblite_singleton_concurrency_container_annotated_ejbliteservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_singleton_concurrency_container_annotated_ejbliteservlet_vehicle_web.war");
            // The class files
            ejblite_singleton_concurrency_container_annotated_ejbliteservlet_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.annotated.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.annotated.SingletonBean.class,
            Fault.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.common.ConcurrencyIF.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.annotated.EJBLiteServletVehicle.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.annotated.Interceptor3.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.common.ClientBase.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.common.InterceptorBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.lite.singleton.common.SingletonInterceptorBase.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.common.JsfClientBase.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.annotated.ReadSingletonBean.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.annotated.Client.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.annotated.CMInterceptorBase.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.annotated.JsfClient.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.common.BeanBase.class,
            com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.annotated.Interceptor0.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            SetupException.class
            );

            // The web.xml descriptor
            URL warResURL = Client.class.getResource("ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_singleton_concurrency_container_annotated_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/ejbliteservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_singleton_concurrency_container_annotated_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/lite/singleton/concurrency/container/annotated/ejb-jar.xml");
            if(warResURL != null) {
              ejblite_singleton_concurrency_container_annotated_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejb-jar.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet/ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_singleton_concurrency_container_annotated_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_singleton_concurrency_container_annotated_ejbliteservlet_vehicle_web, Client.class, warResURL);

        return ejblite_singleton_concurrency_container_annotated_ejbliteservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void lockedSum1() {
            super.lockedSum1();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void lockedSumFromInterceptors1() {
            super.lockedSumFromInterceptors1();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void lockedLinkedList1() {
            super.lockedLinkedList1();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void lockedSum2() {
            super.lockedSum2();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void lockedSumFromInterceptors2() {
            super.lockedSumFromInterceptors2();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void lockedLinkedList2() {
            super.lockedLinkedList2();
        }


}