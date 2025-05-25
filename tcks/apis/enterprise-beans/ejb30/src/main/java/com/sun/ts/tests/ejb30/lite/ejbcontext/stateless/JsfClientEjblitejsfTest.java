package com.sun.ts.tests.ejb30.lite.ejbcontext.stateless;

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
public class JsfClientEjblitejsfTest extends com.sun.ts.tests.ejb30.lite.ejbcontext.stateless.JsfClient {
    static final String VEHICLE_ARCHIVE = "ejblite_ejbcontext_ejblitejsf_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_ejbcontext_ejblitejsf_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_ejbcontext_ejblitejsp_vehicle_web: WEB-INF/ejb-jar.xml
        ejblite_ejbcontext_ejbliteservlet_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml
        ejblite_ejbcontext_ejbliteservlet2_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml

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
            WebArchive ejblite_ejbcontext_ejblitejsf_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_ejbcontext_ejblitejsf_vehicle_web.war");
            // The class files
            ejblite_ejbcontext_ejblitejsf_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.tests.ejb30.lite.ejbcontext.common.EJBContext2IF.class,
            Fault.class,
            com.sun.ts.tests.ejb30.lite.ejbcontext.stateless.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.lite.ejbcontext.common.EJBContextBeanBase.class,
            com.sun.ts.tests.ejb30.lite.ejbcontext.stateless.EJBContext2Bean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.lite.ejbcontext.common.EJBContextIF.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.lite.ejbcontext.common.JsfClientBase.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.ejbcontext.common.ClientBase.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.lite.ejbcontext.common.Interceptor1.class,
            com.sun.ts.tests.ejb30.lite.ejbcontext.stateless.Client.class,
            com.sun.ts.tests.ejb30.lite.ejbcontext.stateless.JsfClient.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.lite.ejbcontext.common.Util.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            SetupException.class,
            com.sun.ts.tests.ejb30.lite.ejbcontext.stateless.EJBContextBean.class
            );
            // The web.xml descriptor
            URL warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_ejbcontext_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = JsfClient.class.getResource("//com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_ejbcontext_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb30/lite/ejbcontext/stateless/beans.xml");
            if(warResURL != null) {
              ejblite_ejbcontext_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/beans.xml");
            if(warResURL != null) {
              ejblite_ejbcontext_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb30/lite/ejbcontext/stateless/ejb-jar.xml");
            if(warResURL != null) {
              ejblite_ejbcontext_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejb-jar.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_ejbcontext_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejblitejsf_vehicle_web.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb30/lite/ejbcontext/stateless/faces-config.xml");
            if(warResURL != null) {
              ejblite_ejbcontext_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/faces-config.xml");
            if(warResURL != null) {
              ejblite_ejbcontext_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle.xhtml");
            if(warResURL != null) {
              ejblite_ejbcontext_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/ejblitejsf_vehicle.xhtml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_ejbcontext_ejblitejsf_vehicle_web, JsfClient.class, warResURL);

        return ejblite_ejbcontext_ejblitejsf_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void lookupIllegalArgumentException() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.lookupIllegalArgumentException();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void getMessageContextIllegalStateException() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.getMessageContextIllegalStateException();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void getInvokedBusinessInterface() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.getInvokedBusinessInterface();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void getBusinessObjectIllegalStateException() throws com.sun.ts.tests.ejb30.common.helper.TestFailedException {
            super.getBusinessObjectIllegalStateException();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void getBusinessObject() {
            super.getBusinessObject();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void ejbContextInjections() {
            super.ejbContextInjections();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void ejbContextInjectionsInInterceptor() {
            super.ejbContextInjectionsInInterceptor();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void lookupEJBContext() {
            super.lookupEJBContext();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void lookupEJBContextInInterceptor() {
            super.lookupEJBContextInInterceptor();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void lookupPortableJNDINames() {
            super.lookupPortableJNDINames();
        }


}