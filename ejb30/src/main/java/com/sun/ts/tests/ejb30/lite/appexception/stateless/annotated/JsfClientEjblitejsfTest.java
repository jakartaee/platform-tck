package com.sun.ts.tests.ejb30.lite.appexception.stateless.annotated;

import com.sun.ts.tests.ejb30.lite.appexception.stateless.annotated.JsfClient;
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
@Tag("ejb_web")
@Tag("web")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class JsfClientEjblitejsfTest extends com.sun.ts.tests.ejb30.lite.appexception.stateless.annotated.JsfClient {
    static final String VEHICLE_ARCHIVE = "ejblite_appexception_stateless_annotated_ejblitejsf_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_appexception_stateless_annotated_ejblitejsf_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_appexception_stateless_annotated_ejblitejsp_vehicle_web: WEB-INF/ejb-jar.xml
        ejblite_appexception_stateless_annotated_ejbliteservlet_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml
        ejblite_appexception_stateless_annotated_ejbliteservlet2_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml

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
            WebArchive ejblite_appexception_stateless_annotated_ejblitejsf_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_appexception_stateless_annotated_ejblitejsf_vehicle_web.war");
            // The class files
            ejblite_appexception_stateless_annotated_ejblitejsf_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.common.appexception.AtCheckedRollbackAppException.class,
            com.sun.ts.tests.ejb30.lite.appexception.stateless.annotated.Client.class,
            com.sun.ts.tests.ejb30.lite.appexception.stateless.annotated.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.common.appexception.UncheckedAppException.class,
            com.sun.ts.tests.ejb30.common.appexception.CheckedAppException.class,
            com.sun.ts.tests.ejb30.lite.appexception.common.JsfClientBase.class,
            com.sun.ts.tests.ejb30.lite.appexception.stateless.annotated.NoInterfaceAppExceptionBean.class,
            com.sun.ts.tests.ejb30.common.appexception.CommonIF.class,
            com.sun.ts.tests.ejb30.common.appexception.AtUncheckedAppException.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.lite.appexception.stateless.annotated.AppExceptionBean.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.appexception.AtUncheckedRollbackAppException.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.ejb30.lite.appexception.stateless.annotated.RollbackBean.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.ejb30.common.appexception.AppExceptionBeanBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.common.appexception.AppExceptionLocalIF.class,
            com.sun.ts.tests.ejb30.lite.appexception.stateless.annotated.JsfClient.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.common.appexception.RollbackIF.class,
            com.sun.ts.tests.ejb30.common.appexception.CheckedRollbackAppException.class,
            com.sun.ts.tests.ejb30.lite.appexception.common.ClientBase.class,
            com.sun.ts.tests.ejb30.common.appexception.AppExceptionIF.class,
            com.sun.ts.tests.ejb30.common.appexception.UncheckedRollbackAppException.class,
            com.sun.ts.tests.ejb30.common.appexception.AtCheckedAppException.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.common.appexception.RollbackBeanBase.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The web.xml descriptor
            URL warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_appexception_stateless_annotated_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = JsfClient.class.getResource("//com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_appexception_stateless_annotated_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb30/lite/appexception/stateless/annotated/beans.xml");
            if(warResURL != null) {
              ejblite_appexception_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/beans.xml");
            if(warResURL != null) {
              ejblite_appexception_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb30/lite/appexception/stateless/annotated/ejb-jar.xml");
            if(warResURL != null) {
              ejblite_appexception_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejb-jar.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_appexception_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejblitejsf_vehicle_web.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb30/lite/appexception/stateless/annotated/faces-config.xml");
            if(warResURL != null) {
              ejblite_appexception_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/faces-config.xml");
            if(warResURL != null) {
              ejblite_appexception_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle.xhtml");
            if(warResURL != null) {
              ejblite_appexception_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/ejblitejsf_vehicle.xhtml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_appexception_stateless_annotated_ejblitejsf_vehicle_web, JsfClient.class, warResURL);

        return ejblite_appexception_stateless_annotated_ejblitejsf_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void checkedAppExceptionTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.checkedAppExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void checkedAppExceptionTest2() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.checkedAppExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void checkedAppExceptionTestLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.checkedAppExceptionTestLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void checkedRollbackAppExceptionTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.checkedRollbackAppExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void checkedRollbackAppExceptionTestLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.checkedRollbackAppExceptionTestLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void atCheckedAppExceptionTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atCheckedAppExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void atCheckedAppExceptionTest2() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atCheckedAppExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void atCheckedAppExceptionTestLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atCheckedAppExceptionTestLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void atUncheckedAppExceptionTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atUncheckedAppExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void atUncheckedAppExceptionTest2() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atUncheckedAppExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void atUncheckedAppExceptionTestLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atUncheckedAppExceptionTestLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void atCheckedRollbackAppExceptionTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atCheckedRollbackAppExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void atCheckedRollbackAppExceptionTestLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atCheckedRollbackAppExceptionTestLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void atUncheckedRollbackAppExceptionTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atUncheckedRollbackAppExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void atUncheckedRollbackAppExceptionTestLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atUncheckedRollbackAppExceptionTestLocal();
        }


}