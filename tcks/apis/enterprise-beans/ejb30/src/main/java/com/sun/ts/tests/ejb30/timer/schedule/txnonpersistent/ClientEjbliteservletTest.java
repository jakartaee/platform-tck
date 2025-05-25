package com.sun.ts.tests.ejb30.timer.schedule.txnonpersistent;

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
@Tag("ejb")
@Tag("ejb30")
@Tag("platform")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientEjbliteservletTest extends com.sun.ts.tests.ejb30.timer.schedule.txnonpersistent.Client {
    static final String VEHICLE_ARCHIVE = "ejb30_timer_schedule_txnonpersistent_ejbliteservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejb30_timer_schedule_txnonpersistent_ejbliteservlet_vehicle_web: WEB-INF/web.xml

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
            WebArchive ejb30_timer_schedule_txnonpersistent_ejbliteservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejb30_timer_schedule_txnonpersistent_ejbliteservlet_vehicle_web.war");
            // The class files
            ejb30_timer_schedule_txnonpersistent_ejbliteservlet_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.timer.schedule.tx.ScheduleTxBeanBase.class,
            Fault.class,
            com.sun.ts.tests.ejb30.timer.common.JsfClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.TimerBeanBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.ClientBase.class,
            com.sun.ts.tests.ejb30.timer.schedule.txnonpersistent.ScheduleBMTBean.class,
            com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.ejb30.timer.schedule.txnonpersistent.EJBLiteServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.timer.common.TimerInfo.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.timer.schedule.tx.ScheduleBMTBean.class,
            com.sun.ts.tests.ejb30.timer.schedule.txnonpersistent.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.timer.common.TimeoutStatusBean.class,
            com.sun.ts.tests.ejb30.timer.common.ScheduleValues.class,
            com.sun.ts.tests.ejb30.timer.schedule.tx.ClientBase.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.timer.schedule.tx.ScheduleBean.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.class,
            com.sun.ts.tests.ejb30.timer.common.TimerUtil.class,
            com.sun.ts.tests.ejb30.timer.schedule.txnonpersistent.Client.class,
            com.sun.ts.tests.ejb30.timer.schedule.txnonpersistent.ScheduleBean.class,
            SetupException.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejb30_timer_schedule_txnonpersistent_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/ejbliteservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejb30_timer_schedule_txnonpersistent_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet/ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejb30_timer_schedule_txnonpersistent_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejb30_timer_schedule_txnonpersistent_ejbliteservlet_vehicle_web, Client.class, warResURL);

        return ejb30_timer_schedule_txnonpersistent_ejbliteservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void createRollback() {
            super.createRollback();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void createRollbackBMT() {
            super.createRollbackBMT();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void createRollbackTxPropagation() throws java.lang.Exception {
            super.createRollbackTxPropagation();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void createRollbackTxPropagationBMT() throws java.lang.Exception {
            super.createRollbackTxPropagationBMT();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void cancelRollback() {
            super.cancelRollback();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void cancelRollbackBMT() {
            super.cancelRollbackBMT();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void cancelRollbackPropagation() throws java.lang.Exception {
            super.cancelRollbackPropagation();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void cancelRollbackPropagationBMT() throws java.lang.Exception {
            super.cancelRollbackPropagationBMT();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void timeoutRollback() {
            super.timeoutRollback();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void timeoutSystemException() {
            super.timeoutSystemException();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void timeoutSystemExceptionBMT() {
            super.timeoutSystemExceptionBMT();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void createTimerWithoutTx() {
            super.createTimerWithoutTx();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void createTimerWithoutTxHavingClientTx() throws java.lang.Exception {
            super.createTimerWithoutTxHavingClientTx();
        }


}