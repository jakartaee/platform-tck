package com.sun.ts.tests.ejb30.lite.tx.cm.stateless.annotated;

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
public class JsfClientEjblitejsfTest extends com.sun.ts.tests.ejb30.lite.tx.cm.stateless.annotated.JsfClient {
    static final String VEHICLE_ARCHIVE = "ejblite_tx_cm_stateless_annotated_ejblitejsf_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_tx_cm_stateless_annotated_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_tx_cm_stateless_annotated_ejblitejsp_vehicle_web: 
        ejblite_tx_cm_stateless_annotated_ejbliteservlet_vehicle_web: WEB-INF/web.xml
        ejblite_tx_cm_stateless_annotated_ejbliteservlet2_vehicle_web: WEB-INF/web.xml

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
            WebArchive ejblite_tx_cm_stateless_annotated_ejblitejsf_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_tx_cm_stateless_annotated_ejblitejsf_vehicle_web.war");
            // The class files
            ejblite_tx_cm_stateless_annotated_ejblitejsf_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.tests.ejb30.tx.common.session.cm.LocalTxBean.class,
            com.sun.ts.tests.ejb30.tx.common.session.cm.LocalTxBeanBase.class,
            Fault.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.stateless.annotated.Client.class,
            com.sun.ts.tests.ejb30.tx.common.session.cm.TxBeanBase0.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.common.ClientBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.stateless.annotated.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.tx.common.session.cm.LocalTestBeanBase.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.tx.common.session.cm.LocalTxIF.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.stateless.annotated.JsfClient.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.stateless.annotated.TestBean.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.lite.tx.cm.common.JsfClientBase.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            SetupException.class
            );
            // The web.xml descriptor
            URL warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_tx_cm_stateless_annotated_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = JsfClient.class.getResource("//com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_tx_cm_stateless_annotated_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb30/lite/tx/cm/stateless/annotated/beans.xml");
            if(warResURL != null) {
              ejblite_tx_cm_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/beans.xml");
            if(warResURL != null) {
              ejblite_tx_cm_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_tx_cm_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejblitejsf_vehicle_web.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb30/lite/tx/cm/stateless/annotated/faces-config.xml");
            if(warResURL != null) {
              ejblite_tx_cm_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/faces-config.xml");
            if(warResURL != null) {
              ejblite_tx_cm_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle.xhtml");
            if(warResURL != null) {
              ejblite_tx_cm_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/ejblitejsf_vehicle.xhtml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_tx_cm_stateless_annotated_ejblitejsf_vehicle_web, JsfClient.class, warResURL);

        return ejblite_tx_cm_stateless_annotated_ejblitejsf_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void localMandatoryTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.localMandatoryTest();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void localNeverTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.localNeverTest();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void localSupportsTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.localSupportsTest();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void localIllegalGetSetRollbackOnlyNeverTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.localIllegalGetSetRollbackOnlyNeverTest();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void localIllegalGetSetRollbackOnlyNotSupportedTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.localIllegalGetSetRollbackOnlyNotSupportedTest();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void localSystemExceptionTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.localSystemExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void localRequiresNewTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.localRequiresNewTest();
        }


}