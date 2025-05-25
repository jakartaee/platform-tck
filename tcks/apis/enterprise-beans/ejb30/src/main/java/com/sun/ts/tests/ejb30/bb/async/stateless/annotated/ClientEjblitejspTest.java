package com.sun.ts.tests.ejb30.bb.async.stateless.annotated;

import com.sun.ts.lib.harness.Fault;

import java.net.URL;

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



@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("ejb_remote_async_optional")
@Tag("web_optional")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientEjblitejspTest extends com.sun.ts.tests.ejb30.bb.async.stateless.annotated.Client {
    static final String VEHICLE_ARCHIVE = "ejbbb_async_stateless_annotated_ejblitejsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejbbb_async_stateless_annotated_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml,war.sun-ejb-jar.xml
        ejbbb_async_stateless_annotated_ejblitejsp_vehicle_web: war.sun-ejb-jar.xml
        ejbbb_async_stateless_annotated_ejbliteservlet_vehicle_web: WEB-INF/web.xml,war.sun-ejb-jar.xml
        ejbbb_async_stateless_annotated_ejbliteservlet2_vehicle_web: WEB-INF/web.xml,war.sun-ejb-jar.xml

        Found Descriptors:
        War:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
            // the war with the correct archive name
            WebArchive ejbbb_async_stateless_annotated_ejblitejsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejbbb_async_stateless_annotated_ejblitejsp_vehicle_web.war");
            // The class files
            ejbbb_async_stateless_annotated_ejblitejsp_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            Fault.class,
            com.sun.ts.tests.ejb30.bb.async.common.annotated.AsyncIF.class,
            com.sun.ts.tests.ejb30.bb.async.common.annotated.AnnotatedJsfClientBase.class,
            com.sun.ts.tests.ejb30.bb.async.stateless.annotated.Client.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.bb.async.common.AsyncClientBase.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.bb.async.stateless.annotated.JsfClient.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.bb.async.common.annotated.Async2RemoteIF.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.ejb30.bb.async.common.annotated.AsyncAnnotatedMethodsCommonIF.class,
            com.sun.ts.tests.ejb30.common.calc.CalculatorException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.bb.async.common.annotated.AsyncBeanBase.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.bb.async.common.AsyncJsfClientBase.class,
            com.sun.ts.tests.ejb30.bb.async.stateless.annotated.AsyncBean.class,
            com.sun.ts.tests.ejb30.bb.async.stateless.annotated.AsyncAnnotatedMethodsBean.class,
            com.sun.ts.tests.ejb30.bb.async.stateless.annotated.HttpServletDelegate.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.bb.async.stateless.annotated.EJBLiteJSPTag.class,
            com.sun.ts.tests.ejb30.bb.async.common.annotated.AsyncAnnotatedMethodsIF.class,
            com.sun.ts.tests.ejb30.bb.async.common.annotated.AsyncAnnotatedMethodsRemoteIF.class,
            com.sun.ts.tests.ejb30.bb.async.common.annotated.AsyncRemoteIF.class,
            com.sun.ts.tests.ejb30.bb.async.stateless.annotated.Async2Bean.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.bb.async.common.annotated.AnnotatedClientBase.class,
            com.sun.ts.tests.ejb30.bb.async.common.annotated.Async2IF.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.common.statussingleton.StatusSingletonBean.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            SetupException.class,
            com.sun.ts.tests.ejb30.bb.async.common.annotated.AsyncAnnotatedMethodsBeanBase.class
            );

            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/vehicle/ejblitejsp/ejblitejsp_vehicle_web.xml");
            if(warResURL != null) {
              ejbbb_async_stateless_annotated_ejblitejsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("//vehicle/ejblitejsp/ejblitejsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejbbb_async_stateless_annotated_ejblitejsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/async/stateless/annotated/ejblitejsp.tld");
            if(warResURL != null) {
              ejbbb_async_stateless_annotated_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/tlds/ejblitejsp.tld");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsp/ejblitejsp.tld");
            if(warResURL != null) {
              ejbbb_async_stateless_annotated_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/ejblitejsp.tld");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsp/ejblitejsp_vehicle.jsp");
            if(warResURL != null) {
              ejbbb_async_stateless_annotated_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/ejblitejsp_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejbbb_async_stateless_annotated_ejblitejsp_vehicle_web, Client.class, warResURL);

        return ejbbb_async_stateless_annotated_ejblitejsp_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void addAway() {
            super.addAway();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void voidRuntimeException() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.voidRuntimeException();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void futureRuntimeException() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.futureRuntimeException();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void futureError() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.futureError();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void futureException() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.futureException();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void futureValueList() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.futureValueList();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void addReturn() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.addReturn();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void addSyncThrowException() {
            super.addSyncThrowException();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void addSyncReturn() {
            super.addSyncReturn();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void addReturnWaitMillis() throws java.util.concurrent.ExecutionException, java.lang.InterruptedException, java.util.concurrent.TimeoutException {
            super.addReturnWaitMillis();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void cancelMayInterruptIfRunningFalse() throws java.util.concurrent.ExecutionException, java.lang.InterruptedException, java.util.concurrent.TimeoutException {
            super.cancelMayInterruptIfRunningFalse();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void cancelMayInterruptIfRunningTrue() throws java.util.concurrent.ExecutionException, java.lang.InterruptedException, java.util.concurrent.TimeoutException {
            super.cancelMayInterruptIfRunningTrue();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void passByValueOrReference() {
            super.passByValueOrReference();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsp")
        public void passByValueOrReferenceAsync() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.passByValueOrReferenceAsync();
        }


}