package com.sun.ts.tests.ejb30.lite.tx.cm.singleton.inheritance;

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
public class JsfClientEjblitejsfTest extends com.sun.ts.tests.ejb30.lite.tx.cm.singleton.inheritance.JsfClient {
    static final String VEHICLE_ARCHIVE = "ejblite_tx_cm_singleton_inheritance_ejblitejsf_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_tx_cm_singleton_inheritance_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_tx_cm_singleton_inheritance_ejblitejsp_vehicle_web: 
        ejblite_tx_cm_singleton_inheritance_ejbliteservlet_vehicle_web: WEB-INF/web.xml
        ejblite_tx_cm_singleton_inheritance_ejbliteservlet2_vehicle_web: WEB-INF/web.xml

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
            WebArchive ejblite_tx_cm_singleton_inheritance_ejblitejsf_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_tx_cm_singleton_inheritance_ejblitejsf_vehicle_web.war");
            // The class files
            ejblite_tx_cm_singleton_inheritance_ejblitejsf_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.tests.ejb30.tx.common.session.inheritance.TxBeanBase.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.singleton.inheritance.EBean.class,
            Fault.class,
            com.sun.ts.tests.ejb30.tx.common.session.inheritance.CBeanBase.class,
            com.sun.ts.tests.ejb30.tx.common.session.inheritance.TestLogic.class,
            com.sun.ts.tests.ejb30.tx.common.session.inheritance.EBeanBase.class,
            com.sun.ts.tests.ejb30.tx.common.session.inheritance.TxLocalIF.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.singleton.inheritance.CBean.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.common.InheritanceClientBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.singleton.inheritance.ABean.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.tx.common.session.inheritance.BBeanBase.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.common.InheritanceJsfClientBase.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.singleton.inheritance.FBean.class,
            com.sun.ts.tests.ejb30.tx.common.session.inheritance.DBeanBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.singleton.inheritance.DBean.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.singleton.inheritance.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.tx.common.session.inheritance.TxCommonIF.class,
            com.sun.ts.tests.ejb30.tx.common.session.inheritance.ABeanBase.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.singleton.inheritance.BBean.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.singleton.inheritance.JsfClient.class,
            com.sun.ts.tests.ejb30.tx.common.session.inheritance.FBeanBase.class,
            com.sun.ts.tests.ejb30.tx.common.session.inheritance.TxRemoteIF.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            SetupException.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.singleton.inheritance.Client.class
            );
            // The web.xml descriptor
            URL warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_tx_cm_singleton_inheritance_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = JsfClient.class.getResource("//com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_tx_cm_singleton_inheritance_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb30/lite/tx/cm/singleton/inheritance/beans.xml");
            if(warResURL != null) {
              ejblite_tx_cm_singleton_inheritance_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/beans.xml");
            if(warResURL != null) {
              ejblite_tx_cm_singleton_inheritance_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_tx_cm_singleton_inheritance_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejblitejsf_vehicle_web.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb30/lite/tx/cm/singleton/inheritance/faces-config.xml");
            if(warResURL != null) {
              ejblite_tx_cm_singleton_inheritance_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/faces-config.xml");
            if(warResURL != null) {
              ejblite_tx_cm_singleton_inheritance_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle.xhtml");
            if(warResURL != null) {
              ejblite_tx_cm_singleton_inheritance_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/ejblitejsf_vehicle.xhtml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_tx_cm_singleton_inheritance_ejblitejsf_vehicle_web, JsfClient.class, warResURL);

        return ejblite_tx_cm_singleton_inheritance_ejblitejsf_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void aBeanLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.aBeanLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void bBeanLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.bBeanLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void cBeanLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.cBeanLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void dBeanLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.dBeanLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void eBeanLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.eBeanLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void overloadedMethodsTxLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.overloadedMethodsTxLocal();
        }


}