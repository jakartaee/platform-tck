package com.sun.ts.tests.ejb30.lite.appexception.singleton.annotated;

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
public class ClientEjblitejspTest extends com.sun.ts.tests.ejb30.lite.appexception.singleton.annotated.Client {
    static final String VEHICLE_ARCHIVE = "ejblite_appexception_singleton_annotated_ejblitejsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_appexception_singleton_annotated_ejblitejsf_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_appexception_singleton_annotated_ejblitejsp_vehicle_web: WEB-INF/ejb-jar.xml
        ejblite_appexception_singleton_annotated_ejbliteservlet_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml
        ejblite_appexception_singleton_annotated_ejbliteservlet2_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml

        Found Descriptors:
        War:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
            // the war with the correct archive name
            WebArchive ejblite_appexception_singleton_annotated_ejblitejsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_appexception_singleton_annotated_ejblitejsp_vehicle_web.war");
            // The class files
            ejblite_appexception_singleton_annotated_ejblitejsp_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.common.appexception.AtCheckedRollbackAppException.class,
            com.sun.ts.tests.ejb30.lite.appexception.singleton.annotated.EJBLiteJSPTag.class,
            com.sun.ts.tests.ejb30.common.appexception.UncheckedAppException.class,
            com.sun.ts.tests.ejb30.common.appexception.CheckedAppException.class,
            com.sun.ts.tests.ejb30.lite.appexception.common.JsfClientBase.class,
            com.sun.ts.tests.ejb30.common.appexception.CommonIF.class,
            com.sun.ts.tests.ejb30.lite.appexception.singleton.annotated.AppExceptionBean.class,
            com.sun.ts.tests.ejb30.common.appexception.AtUncheckedAppException.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.lite.appexception.singleton.annotated.RollbackBean.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.common.appexception.AtUncheckedRollbackAppException.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.lite.appexception.singleton.annotated.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.ejb30.common.appexception.AppExceptionBeanBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.common.appexception.AppExceptionLocalIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.common.appexception.RollbackIF.class,
            com.sun.ts.tests.ejb30.common.appexception.CheckedRollbackAppException.class,
            com.sun.ts.tests.ejb30.lite.appexception.common.ClientBase.class,
            com.sun.ts.tests.ejb30.common.appexception.AppExceptionIF.class,
            com.sun.ts.tests.ejb30.lite.appexception.singleton.annotated.Client.class,
            com.sun.ts.tests.ejb30.common.appexception.UncheckedRollbackAppException.class,
            com.sun.ts.tests.ejb30.common.appexception.AtCheckedAppException.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.lite.appexception.singleton.annotated.NoInterfaceAppExceptionBean.class,
            com.sun.ts.tests.ejb30.lite.appexception.singleton.annotated.JsfClient.class,
            com.sun.ts.tests.ejb30.common.appexception.RollbackBeanBase.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/vehicle/ejblitejsp/ejblitejsp_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_appexception_singleton_annotated_ejblitejsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("//vehicle/ejblitejsp/ejblitejsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_appexception_singleton_annotated_ejblitejsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/lite/appexception/singleton/annotated/ejb-jar.xml");
            if(warResURL != null) {
              ejblite_appexception_singleton_annotated_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejb-jar.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/lite/appexception/singleton/annotated/ejblitejsp.tld");
            if(warResURL != null) {
              ejblite_appexception_singleton_annotated_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/tlds/ejblitejsp.tld");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsp/ejblitejsp.tld");
            if(warResURL != null) {
              ejblite_appexception_singleton_annotated_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/ejblitejsp.tld");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsp/ejblitejsp_vehicle.jsp");
            if(warResURL != null) {
              ejblite_appexception_singleton_annotated_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/ejblitejsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_appexception_singleton_annotated_ejblitejsp_vehicle_web, Client.class, warResURL);

        return ejblite_appexception_singleton_annotated_ejblitejsp_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void checkedAppExceptionTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.checkedAppExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void checkedAppExceptionTest2() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.checkedAppExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void checkedAppExceptionTestLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.checkedAppExceptionTestLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void checkedRollbackAppExceptionTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.checkedRollbackAppExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void checkedRollbackAppExceptionTestLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.checkedRollbackAppExceptionTestLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void atCheckedAppExceptionTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atCheckedAppExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void atCheckedAppExceptionTest2() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atCheckedAppExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void atCheckedAppExceptionTestLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atCheckedAppExceptionTestLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void atUncheckedAppExceptionTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atUncheckedAppExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void atUncheckedAppExceptionTest2() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atUncheckedAppExceptionTest2();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void atUncheckedAppExceptionTestLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atUncheckedAppExceptionTestLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void atCheckedRollbackAppExceptionTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atCheckedRollbackAppExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void atCheckedRollbackAppExceptionTestLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atCheckedRollbackAppExceptionTestLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void atUncheckedRollbackAppExceptionTest() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atUncheckedRollbackAppExceptionTest();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void atUncheckedRollbackAppExceptionTestLocal() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.atUncheckedRollbackAppExceptionTestLocal();
        }


}