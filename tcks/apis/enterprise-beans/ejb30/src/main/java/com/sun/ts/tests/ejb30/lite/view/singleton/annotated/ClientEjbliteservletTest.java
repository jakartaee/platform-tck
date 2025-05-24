package com.sun.ts.tests.ejb30.lite.view.singleton.annotated;

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
public class ClientEjbliteservletTest extends com.sun.ts.tests.ejb30.lite.view.singleton.annotated.Client {
    static final String VEHICLE_ARCHIVE = "ejblite_view_singleton_annotated_ejbliteservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_view_singleton_annotated_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_view_singleton_annotated_ejblitejsp_vehicle_web: 
        ejblite_view_singleton_annotated_ejbliteservlet_vehicle_web: WEB-INF/web.xml
        ejblite_view_singleton_annotated_ejbliteservlet2_vehicle_web: WEB-INF/web.xml

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
            WebArchive ejblite_view_singleton_annotated_ejbliteservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_view_singleton_annotated_ejbliteservlet_vehicle_web.war");
            // The class files
            ejblite_view_singleton_annotated_ejbliteservlet_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.lite.view.singleton.annotated.AnnotatedInterfaceBean.class,
            com.sun.ts.tests.ejb30.lite.view.singleton.annotated.JsfClient.class,
            com.sun.ts.tests.ejb30.lite.view.common.SuperclassBean.class,
            com.sun.ts.tests.ejb30.lite.view.singleton.annotated.SessionBeanLocalBean.class,
            Fault.class,
            com.sun.ts.tests.ejb30.common.busiface.BusinessLocalIF2.class,
            com.sun.ts.tests.ejb30.common.busiface.SessionBeanLocalBeanBase.class,
            com.sun.ts.tests.ejb30.lite.view.singleton.annotated.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.common.busiface.AnnotatedLocalBusinessInterface1.class,
            com.sun.ts.tests.ejb30.lite.view.common.ClientBase.class,
            com.sun.ts.tests.ejb30.lite.view.common.JsfClientBase.class,
            com.sun.ts.tests.ejb30.common.busiface.BusinessBeanBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.common.busiface.BusinessLocal1Base.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.lite.view.common.SuperclassBeanBase.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.lite.view.singleton.annotated.Client.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.lite.view.singleton.annotated.EJBLiteServletVehicle.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.ejb30.lite.view.singleton.annotated.SerializableLocalBean.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.ejb30.lite.view.singleton.annotated.SubclassExtendsPOJOBean.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.common.busiface.AnnotatedLocalBusinessInterface2.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.view.singleton.annotated.BusinessBean.class,
            com.sun.ts.tests.ejb30.lite.view.singleton.annotated.SubclassExtendsBeanBean.class,
            com.sun.ts.tests.ejb30.common.busiface.BusinessLocalIF1.class,
            com.sun.ts.tests.ejb30.common.busiface.Constants.class,
            com.sun.ts.tests.ejb30.lite.view.singleton.annotated.LocalAndNoInterfaceBean.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.lite.view.singleton.annotated.ExternalizableLocalBean.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            SetupException.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_view_singleton_annotated_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/ejbliteservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_view_singleton_annotated_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet/ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_view_singleton_annotated_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_view_singleton_annotated_ejbliteservlet_vehicle_web, Client.class, warResURL);

        return ejblite_view_singleton_annotated_ejbliteservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void multipleInterfacesLocal() {
            super.multipleInterfacesLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void singleInterfaceLocal() {
            super.singleInterfaceLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void multipleAnnotatedInterfacesLocal() {
            super.multipleAnnotatedInterfacesLocal();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void localAndNoInterfaceView() {
            super.localAndNoInterfaceView();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void getBusinessObjectInSuperclassBean() {
            super.getBusinessObjectInSuperclassBean();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void getBusinessObjectInSubclassBean() {
            super.getBusinessObjectInSubclassBean();
        }


}