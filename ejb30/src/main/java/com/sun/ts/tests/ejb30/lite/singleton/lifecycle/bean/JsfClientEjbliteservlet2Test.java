package com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean;

import com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean.JsfClient;
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
public class JsfClientEjbliteservlet2Test extends com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean.JsfClient {
    static final String VEHICLE_ARCHIVE = "ejblite_singleton_lifecycle_bean_ejbliteservlet2_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_singleton_lifecycle_bean_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejblite_singleton_lifecycle_bean_ejblitejsp_vehicle_web: 
        ejblite_singleton_lifecycle_bean_ejbliteservlet_vehicle_web: WEB-INF/web.xml
        ejblite_singleton_lifecycle_bean_ejbliteservlet2_vehicle_web: WEB-INF/web.xml

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
            WebArchive ejblite_singleton_lifecycle_bean_ejbliteservlet2_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_singleton_lifecycle_bean_ejbliteservlet2_vehicle_web.war");
            // The class files
            ejblite_singleton_lifecycle_bean_ejbliteservlet2_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean.JsfClient.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean.Client.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean.BSingletonIF.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean.BeanBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean.CSingletonBean.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean.ASingletonBean.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean.BSingletonBean.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean.StatefulBean.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean.EJBLiteServlet2Filter.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean.CSingletonIF.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean.CommonSingletonIF.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            com.sun.ts.tests.ejb30.lite.singleton.lifecycle.bean.C2SingletonIF.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );

            // The web.xml descriptor
            URL warResURL = JsfClient.class.getResource("ejbliteservlet2_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_singleton_lifecycle_bean_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = JsfClient.class.getResource("/ejbliteservlet2_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_singleton_lifecycle_bean_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_singleton_lifecycle_bean_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet2_vehicle_web.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle.jsp");
            if(warResURL != null) {
              ejblite_singleton_lifecycle_bean_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/ejbliteservlet2_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_singleton_lifecycle_bean_ejbliteservlet2_vehicle_web, JsfClient.class, warResURL);

        return ejblite_singleton_lifecycle_bean_ejbliteservlet2_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void beanReferenceEqualsAndHashCodeA() {
            super.beanReferenceEqualsAndHashCodeA();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void beanReferenceEqualsAndHashCodeB() {
            super.beanReferenceEqualsAndHashCodeB();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void beanReferenceEqualsAndHashCodeC2() {
            super.beanReferenceEqualsAndHashCodeC2();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void beanReferenceEqualsAndHashCodeA_2() {
            super.beanReferenceEqualsAndHashCodeA_2();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void beanReferenceEqualsAndHashCodeB_2() {
            super.beanReferenceEqualsAndHashCodeB_2();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void beanReferenceEqualsAndHashCodeC2_2() {
            super.beanReferenceEqualsAndHashCodeC2_2();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void beanReferencesNotEqual() {
            super.beanReferencesNotEqual();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void beanClassHashCodeA() {
            super.beanClassHashCodeA();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void beanClassHashCodeB() {
            super.beanClassHashCodeB();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void beanClassHashCodeC2() {
            super.beanClassHashCodeC2();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void beanClassHashCodeCC2() {
            super.beanClassHashCodeCC2();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void noDestructionAfterSystemException() {
            super.noDestructionAfterSystemException();
        }


}