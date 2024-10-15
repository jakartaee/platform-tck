package com.sun.ts.tests.ejb30.timer.schedule.auto.attr.mdb;

import com.sun.ts.tests.ejb30.timer.schedule.auto.attr.mdb.Client;
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
@Tag("ejb_mdb_optional")
@Tag("web_optional")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientEjbliteservletTest extends com.sun.ts.tests.ejb30.timer.schedule.auto.attr.mdb.Client {
    static final String VEHICLE_ARCHIVE = "ejb30_timer_schedule_auto_attr_mdb_ejbliteservlet_vehicle";

    /**
     * EE10 Deployment Descriptors: ejb30_timer_schedule_auto_attr_mdb_ejbliteservlet_vehicle_web:
     * WEB-INF/ejb-jar.xml,WEB-INF/web.xml,war.sun-ejb-jar.xml
     * 
     * Found Descriptors: War:
     * 
     * /com/sun/ts/tests/common/vehicle/ejbliteservlet/ejbliteservlet_vehicle_web.xml
     */
    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = VEHICLE_ARCHIVE, order = 2)
    public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
        // the war with the correct archive name
        WebArchive ejb30_timer_schedule_auto_attr_mdb_ejbliteservlet_vehicle_web = ShrinkWrap.create(WebArchive.class,
                "ejb30_timer_schedule_auto_attr_mdb_ejbliteservlet_vehicle_web.war");
        // The class files
        ejb30_timer_schedule_auto_attr_mdb_ejbliteservlet_vehicle_web.addClasses(
                com.sun.ts.tests.ejb30.timer.schedule.auto.attr.mdb.HttpServletDelegate.class, 
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.ejb30.timer.schedule.auto.attr.mdb.ScheduleBean.class,
                com.sun.ts.tests.ejb30.timer.schedule.auto.attr.stateless.ScheduleBeanBase2.class,
                com.sun.ts.tests.ejb30.timer.common.JsfClientBase.class, 
                com.sun.ts.tests.ejb30.timer.common.TimerBeanBase.class,
                com.sun.ts.tests.ejb30.timer.schedule.auto.attr.mdb.EJBLiteServletVehicle.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.ejb30.timer.schedule.auto.attr.stateless.ScheduleBeanBase3.class,
                com.sun.ts.tests.ejb30.common.helper.Helper.class, 
                com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
                com.sun.ts.tests.ejb30.timer.common.ClientBase.class,
                com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod.class, 
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class, com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.ejb30.common.lite.NumberIF.class, 
                com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class, 
                com.sun.ts.tests.ejb30.timer.common.TimerInfo.class,
                com.sun.ts.tests.ejb30.timer.schedule.auto.attr.stateless.Client.class,
                com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
                com.sun.ts.tests.ejb30.timer.common.TimeoutStatusBean.class, 
                com.sun.ts.tests.ejb30.timer.common.ScheduleValues.class,
                com.sun.ts.tests.ejb30.timer.schedule.auto.attr.mdb.Client.class, 
                com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
                com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
                com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.class, 
                com.sun.ts.tests.ejb30.timer.common.TimerUtil.class,
                com.sun.ts.lib.harness.EETest.SetupException.class);
        
        // The web.xml descriptor
        URL warResURL = Client.class.getResource("ejbliteservlet_vehicle_web.xml");
        if (warResURL != null) {
            ejb30_timer_schedule_auto_attr_mdb_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
        }
        // The sun-web.xml descriptor
        warResURL = Client.class.getResource("/ejbliteservlet_vehicle_web.war.sun-web.xml");
        if (warResURL != null) {
            ejb30_timer_schedule_auto_attr_mdb_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
        }

        // Any libraries added to the war

        // Web content
        warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/timer/schedule/auto/attr/mdb/ejb-jar.xml");
        if (warResURL != null) {
            ejb30_timer_schedule_auto_attr_mdb_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejb-jar.xml");
        }
        
        warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/timer/schedule/auto/attr/mdb/ejb30_timer_schedule_auto_attr_mdb_ejbliteservlet_vehicle_web.war.sun-ejb-jar.xml");
        if (warResURL != null) {
            ejb30_timer_schedule_auto_attr_mdb_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/sun-ejb-jar.xml");
        }
        
        warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/timer/schedule/auto/attr/mdb/ejb30_timer_schedule_auto_attr_mdb_ejbliteservlet_vehicle_web.war.sun-ejb-jar.xml");
        if (warResURL != null) {
            ejb30_timer_schedule_auto_attr_mdb_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/sun-ejb-jar.xml");
        }
        
        warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet/ejbliteservlet_vehicle_web.xml");
        if (warResURL != null) {
            ejb30_timer_schedule_auto_attr_mdb_ejbliteservlet_vehicle_web.addAsWebResource(warResURL,
                    "/WEB-INF/ejbliteservlet_vehicle_web.xml");
        }

        // Call the archive processor
        archiveProcessor.processWebArchive(ejb30_timer_schedule_auto_attr_mdb_ejbliteservlet_vehicle_web, Client.class, warResURL);

        return ejb30_timer_schedule_auto_attr_mdb_ejbliteservlet_vehicle_web;
    }

    @Test
    @Override
    @TargetVehicle("ejbliteservlet")
    public void autoTimerInSuperClassNoParam() {
        super.autoTimerInSuperClassNoParam();
    }

    @Test
    @Override
    @TargetVehicle("ejbliteservlet")
    public void autoTimerNonPersistent() {
        super.autoTimerNonPersistent();
    }

    @Test
    @Override
    @TargetVehicle("ejbliteservlet")
    public void autoTimerPersistent() {
        super.autoTimerPersistent();
    }

    @Test
    @Override
    @TargetVehicle("ejbliteservlet")
    public void autoTimerWithInfo() {
        super.autoTimerWithInfo();
    }

    @Test
    @Override
    @TargetVehicle("ejbliteservlet")
    public void autoTimerWithoutInfo() {
        super.autoTimerWithoutInfo();
    }

}