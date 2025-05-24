package com.sun.ts.tests.ejb30.lite.async.stateful.metadata;

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
public class ClientEjbliteservletTest extends com.sun.ts.tests.ejb30.lite.async.stateful.metadata.Client {
    static final String VEHICLE_ARCHIVE = "ejblite_async_stateful_metadata_ejbliteservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_async_stateful_metadata_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml,war.sun-ejb-jar.xml
        ejblite_async_stateful_metadata_ejblitejsp_vehicle_web: war.sun-ejb-jar.xml
        ejblite_async_stateful_metadata_ejbliteservlet_vehicle_web: WEB-INF/web.xml,war.sun-ejb-jar.xml
        ejblite_async_stateful_metadata_ejbliteservlet2_vehicle_web: WEB-INF/web.xml,war.sun-ejb-jar.xml

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
            WebArchive ejblite_async_stateful_metadata_ejbliteservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_async_stateful_metadata_ejbliteservlet_vehicle_web.war");
            // The class files
            ejblite_async_stateful_metadata_ejbliteservlet_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.lite.async.common.metadata.MetadataClientBase.class,
            com.sun.ts.tests.ejb30.lite.async.common.metadata.InterfaceTypeLevelBeanBase.class,
            Fault.class,
            com.sun.ts.tests.ejb30.lite.async.common.metadata.BeanClassLevel1BeanBase.class,
            com.sun.ts.tests.ejb30.lite.async.stateful.metadata.Client.class,
            com.sun.ts.tests.ejb30.lite.async.common.metadata.MetadataJsfClientBase.class,
            com.sun.ts.tests.ejb30.lite.async.stateful.metadata.EJBLiteServletVehicle.class,
            com.sun.ts.tests.ejb30.lite.async.stateful.metadata.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.lite.async.common.AsyncJsfClientBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.lite.async.stateful.metadata.BeanClassLevelBean.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            EETest.class,
            com.sun.ts.tests.ejb30.lite.async.common.metadata.PlainInterfaceTypeLevelIF.class,
            ServiceEETest.class,
            com.sun.ts.tests.ejb30.common.calc.CalculatorException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.lite.async.common.metadata.BeanClassLevel3BeanBase.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.lite.async.common.metadata.BeanClassLevel2BeanBase.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.async.common.metadata.BeanClassLevel0BeanBase.class,
            com.sun.ts.tests.ejb30.lite.async.common.AsyncClientBase.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.common.statussingleton.StatusSingletonBean.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            SetupException.class,
            com.sun.ts.tests.ejb30.lite.async.stateful.metadata.JsfClient.class
            );

            // The web.xml descriptor
            URL warResURL = Client.class.getResource("ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_async_stateful_metadata_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/ejbliteservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_async_stateful_metadata_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet/ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_async_stateful_metadata_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_async_stateful_metadata_ejbliteservlet_vehicle_web, Client.class, warResURL);

        return ejblite_async_stateful_metadata_ejbliteservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void beanClassLevelReturnType() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.beanClassLevelReturnType();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void beanClassLevelRuntimeException() throws java.lang.InterruptedException, java.util.concurrent.TimeoutException {
            super.beanClassLevelRuntimeException();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void customFutureImpl() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.customFutureImpl();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void beanClassLevelSyncMethod() {
            super.beanClassLevelSyncMethod();
        }


}