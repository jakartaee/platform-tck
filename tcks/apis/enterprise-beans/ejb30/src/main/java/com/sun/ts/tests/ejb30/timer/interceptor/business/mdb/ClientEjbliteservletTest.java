package com.sun.ts.tests.ejb30.timer.interceptor.business.mdb;

import com.sun.ts.lib.harness.Fault;

import java.net.URL;

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

@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("ejb_mdb_optional")
@Tag("web_optional")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientEjbliteservletTest extends com.sun.ts.tests.ejb30.timer.interceptor.business.mdb.Client {
    static final String VEHICLE_ARCHIVE = "ejb30_timer_interceptor_business_mdb_ejbliteservlet_vehicle";

    /**
     * EE10 Deployment Descriptors: ejb30_timer_interceptor_business_mdb_ejbliteservlet_vehicle_web:
     * WEB-INF/ejb-jar.xml,
     * WEB-INF/web.xml,
     * war.sun-ejb-jar.xml,
     * war.sun-web.xml
     *
     * Found Descriptors: War:
     *
     * /com/sun/ts/tests/ejb30/timer/interceptor/business/mdb/ejbliteservlet_vehicle_web.xml
     * /com/sun/ts/tests/common/vehicle/ejbliteservlet/ejbliteservlet_vehicle_web.xml
     */
    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = VEHICLE_ARCHIVE, order = 2)
    public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
        // the war with the correct archive name
        WebArchive ejb30_timer_interceptor_business_mdb_ejbliteservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejb30_timer_interceptor_business_mdb_ejbliteservlet_vehicle_web.war");

        // The class files
        ejb30_timer_interceptor_business_mdb_ejbliteservlet_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.timer.interceptor.business.common.Interceptor3.class,
            Fault.class,
            com.sun.ts.tests.ejb30.timer.interceptor.business.mdb.Client.class,
            com.sun.ts.tests.ejb30.timer.interceptor.business.common.Interceptor1.class,
            com.sun.ts.tests.ejb30.timer.common.TimerBeanBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.timer.interceptor.business.common.ClientBase.class,
            com.sun.ts.tests.ejb30.timer.interceptor.business.mdb.BusinessTimerBean.class,
            com.sun.ts.tests.ejb30.timer.common.MessageSenderBean.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.timer.interceptor.business.mdb.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.timer.common.ClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.timer.common.TimerInfo.class,
            com.sun.ts.tests.ejb30.timer.interceptor.business.common.Interceptor2.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.common.messaging.Constants.class,
            com.sun.ts.tests.ejb30.timer.common.TimeoutStatusBean.class,
            com.sun.ts.tests.ejb30.timer.interceptor.business.common.InterceptorBase.class,
            com.sun.ts.tests.ejb30.timer.interceptor.business.common.BusinessTimerBeanBase.class,
            com.sun.ts.tests.ejb30.timer.interceptor.business.mdb.TestBean.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.timer.interceptor.business.mdb.EJBLiteServletVehicle.class,
            com.sun.ts.tests.ejb30.timer.common.TimerUtil.class,
            SetupException.class);

        // The web.xml descriptor
        URL warResURL = Client.class.getResource("ejbliteservlet_vehicle_web.xml");
        if (warResURL != null) {
            ejb30_timer_interceptor_business_mdb_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
        }

        // The sun-web.xml descriptor
        warResURL = Client.class.getResource("ejb30_timer_interceptor_business_mdb_ejbliteservlet_vehicle_web.war.sun-web.xml");
        if (warResURL != null) {
            ejb30_timer_interceptor_business_mdb_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
        }

        // The ejb-jar.xml descriptor
        warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/timer/interceptor/business/mdb/ejb-jar.xml");
        if (warResURL != null) {
            ejb30_timer_interceptor_business_mdb_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejb-jar.xml");
        }

        // The ejb-jar.xml descriptor
        warResURL = Client.class.getResource("ejb30_timer_interceptor_business_mdb_ejbliteservlet_vehicle_web.war.sun-ejb-jar.xml");
        if (warResURL != null) {
            ejb30_timer_interceptor_business_mdb_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/sun-ejb-jar.xml");
        }

        // Call the archive processor
        archiveProcessor.processWebArchive(ejb30_timer_interceptor_business_mdb_ejbliteservlet_vehicle_web, Client.class, warResURL);

        return ejb30_timer_interceptor_business_mdb_ejbliteservlet_vehicle_web;
    }

    @Test
    @Override
    @TargetVehicle("ejbliteservlet")
    public void aroundInvokeMethods() {
        super.aroundInvokeMethods();
    }

    @Test
    @Override
    @TargetVehicle("ejbliteservlet")
    public void messageFromSingletonBeanToMDB() throws java.lang.InterruptedException {
        super.messageFromSingletonBeanToMDB();
    }

}