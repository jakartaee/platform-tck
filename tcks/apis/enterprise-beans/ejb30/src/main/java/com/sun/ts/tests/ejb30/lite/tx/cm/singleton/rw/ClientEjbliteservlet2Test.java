package com.sun.ts.tests.ejb30.lite.tx.cm.singleton.rw;

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
public class ClientEjbliteservlet2Test extends com.sun.ts.tests.ejb30.lite.tx.cm.singleton.rw.Client {
    static final String VEHICLE_ARCHIVE = "ejblite_tx_cm_singleton_rw_ejbliteservlet2_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_tx_cm_singleton_rw_ejblitejsf_vehicle_web: WEB-INF/classes/META-INF/persistence.xml,WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_tx_cm_singleton_rw_ejblitejsp_vehicle_web: WEB-INF/classes/META-INF/persistence.xml
        ejblite_tx_cm_singleton_rw_ejbliteservlet_vehicle_web: WEB-INF/classes/META-INF/persistence.xml,WEB-INF/web.xml
        ejblite_tx_cm_singleton_rw_ejbliteservlet2_vehicle_web: WEB-INF/classes/META-INF/persistence.xml,WEB-INF/web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/common/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle_web.xml
        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
            // the war with the correct archive name
            WebArchive ejblite_tx_cm_singleton_rw_ejbliteservlet2_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_tx_cm_singleton_rw_ejbliteservlet2_vehicle_web.war");
            // The class files
            ejblite_tx_cm_singleton_rw_ejbliteservlet2_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.lite.tx.cm.common.RWJsfClientBase.class,
            Fault.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.singleton.rw.JsfClient.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.singleton.rw.TestBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.common.CoffeeUtil.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.common.RWTestBeanBase0.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.singleton.rw.HttpServletDelegate.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.common.CoffeeEJBLite.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.common.RWTxBeanBase.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.singleton.rw.EJBLiteServlet2Filter.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.common.RWTestBeanBase.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.singleton.rw.TxBean.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.common.RWClientBase.class,
            SetupException.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.singleton.rw.Client.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("ejbliteservlet2_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_tx_cm_singleton_rw_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/ejbliteservlet2_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_tx_cm_singleton_rw_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_tx_cm_singleton_rw_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet2_vehicle_web.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle.jsp");
            if(warResURL != null) {
              ejblite_tx_cm_singleton_rw_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/ejbliteservlet2_vehicle.jsp");
            }
            // ejblite-pu persistence unit
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/lite/tx/cm/common/persistence.xml");
            ejblite_tx_cm_singleton_rw_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "classes/META-INF/persistence.xml");

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_tx_cm_singleton_rw_ejbliteservlet2_vehicle_web, Client.class, warResURL);

        return ejblite_tx_cm_singleton_rw_ejbliteservlet2_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void mandatory() {
            super.mandatory();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void required() {
            super.required();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void requiredNoExistingTransaction() {
            super.requiredNoExistingTransaction();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void supports() {
            super.supports();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void requiresNew() {
            super.requiresNew();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void postConstructTransaction() {
            super.postConstructTransaction();
        }


}