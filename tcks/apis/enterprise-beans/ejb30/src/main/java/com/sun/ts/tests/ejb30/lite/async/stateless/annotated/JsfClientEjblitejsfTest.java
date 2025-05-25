package com.sun.ts.tests.ejb30.lite.async.stateless.annotated;

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
public class JsfClientEjblitejsfTest extends com.sun.ts.tests.ejb30.lite.async.stateless.annotated.JsfClient {
    static final String VEHICLE_ARCHIVE = "ejblite_async_stateless_annotated_ejblitejsf_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_async_stateless_annotated_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml,war.sun-ejb-jar.xml
        ejblite_async_stateless_annotated_ejblitejsp_vehicle_web: war.sun-ejb-jar.xml
        ejblite_async_stateless_annotated_ejbliteservlet_vehicle_web: WEB-INF/web.xml,war.sun-ejb-jar.xml
        ejblite_async_stateless_annotated_ejbliteservlet2_vehicle_web: WEB-INF/web.xml,war.sun-ejb-jar.xml

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
            WebArchive ejblite_async_stateless_annotated_ejblitejsf_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_async_stateless_annotated_ejblitejsf_vehicle_web.war");
            // The class files
            ejblite_async_stateless_annotated_ejblitejsf_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.tests.ejb30.lite.async.stateless.annotated.AsyncAnnotatedMethodsBean.class,
            Fault.class,
            com.sun.ts.tests.ejb30.lite.async.common.annotated.AsyncIF.class,
            com.sun.ts.tests.ejb30.lite.async.stateless.annotated.Async2Bean.class,
            com.sun.ts.tests.ejb30.lite.async.common.AsyncJsfClientBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.lite.async.common.annotated.AsyncAnnotatedMethodsIF.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.lite.async.common.annotated.AnnotatedJsfClientBase.class,
            com.sun.ts.tests.ejb30.lite.async.common.annotated.AsyncAnnotatedMethodsBeanBase.class,
            EETest.class,
            com.sun.ts.tests.ejb30.lite.async.common.annotated.Async2IF.class,
            com.sun.ts.tests.ejb30.lite.async.stateless.annotated.Client.class,
            ServiceEETest.class,
            com.sun.ts.tests.ejb30.common.calc.CalculatorException.class,
            com.sun.ts.tests.ejb30.lite.async.stateless.annotated.HttpServletDelegate.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.async.stateless.annotated.AsyncBean.class,
            com.sun.ts.tests.ejb30.lite.async.common.annotated.AnnotatedClientBase.class,
            com.sun.ts.tests.ejb30.lite.async.common.AsyncClientBase.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.lite.async.common.annotated.AsyncBeanBase.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.common.statussingleton.StatusSingletonBean.class,
            com.sun.ts.tests.ejb30.lite.async.stateless.annotated.JsfClient.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            SetupException.class,
            com.sun.ts.tests.ejb30.lite.async.common.annotated.AsyncAnnotatedMethodsCommonIF.class
            );

            // The web.xml descriptor
            URL warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_async_stateless_annotated_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = JsfClient.class.getResource("//com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_async_stateless_annotated_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb30/lite/async/stateless/annotated/beans.xml");
            if(warResURL != null) {
              ejblite_async_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/beans.xml");
            if(warResURL != null) {
              ejblite_async_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_async_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejblitejsf_vehicle_web.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/ejb30/lite/async/stateless/annotated/faces-config.xml");
            if(warResURL != null) {
              ejblite_async_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/faces-config.xml");
            if(warResURL != null) {
              ejblite_async_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle.xhtml");
            if(warResURL != null) {
              ejblite_async_stateless_annotated_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/ejblitejsf_vehicle.xhtml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_async_stateless_annotated_ejblitejsf_vehicle_web, JsfClient.class, warResURL);

        return ejblite_async_stateless_annotated_ejblitejsf_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void addAway() {
            super.addAway();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void voidRuntimeException() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.voidRuntimeException();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void futureRuntimeException() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.futureRuntimeException();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void futureError() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.futureError();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void futureException() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.futureException();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void futureValueList() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.futureValueList();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void addReturn() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.addReturn();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void addSyncThrowException() {
            super.addSyncThrowException();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void addSyncReturn() {
            super.addSyncReturn();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void addReturnWaitMillis() throws java.util.concurrent.ExecutionException, java.lang.InterruptedException, java.util.concurrent.TimeoutException {
            super.addReturnWaitMillis();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void cancelMayInterruptIfRunningFalse() throws java.util.concurrent.ExecutionException, java.lang.InterruptedException, java.util.concurrent.TimeoutException {
            super.cancelMayInterruptIfRunningFalse();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void cancelMayInterruptIfRunningTrue() throws java.util.concurrent.ExecutionException, java.lang.InterruptedException, java.util.concurrent.TimeoutException {
            super.cancelMayInterruptIfRunningTrue();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void passByValueOrReference() {
            super.passByValueOrReference();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void passByValueOrReferenceAsync() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.passByValueOrReferenceAsync();
        }


}