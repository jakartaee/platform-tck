package com.sun.ts.tests.ejb30.lite.async.singleton.annotated;

import com.sun.ts.tests.ejb30.lite.async.singleton.annotated.Client;
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
public class ClientEjbliteservletTest extends com.sun.ts.tests.ejb30.lite.async.singleton.annotated.Client {
    static final String VEHICLE_ARCHIVE = "ejblite_async_singleton_annotated_ejbliteservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_async_singleton_annotated_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml,war.sun-ejb-jar.xml
        ejblite_async_singleton_annotated_ejblitejsp_vehicle_web: war.sun-ejb-jar.xml
        ejblite_async_singleton_annotated_ejbliteservlet_vehicle_web: WEB-INF/web.xml,war.sun-ejb-jar.xml
        ejblite_async_singleton_annotated_ejbliteservlet2_vehicle_web: WEB-INF/web.xml,war.sun-ejb-jar.xml

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
            WebArchive ejblite_async_singleton_annotated_ejbliteservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_async_singleton_annotated_ejbliteservlet_vehicle_web.war");
            // The class files
            ejblite_async_singleton_annotated_ejbliteservlet_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.lite.async.common.annotated.AsyncIF.class,
            com.sun.ts.tests.ejb30.lite.async.singleton.annotated.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.lite.async.common.AsyncJsfClientBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.lite.async.singleton.annotated.AsyncAnnotatedMethodsBean.class,
            com.sun.ts.tests.ejb30.lite.async.common.annotated.AsyncAnnotatedMethodsIF.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.lite.async.common.annotated.AnnotatedJsfClientBase.class,
            com.sun.ts.tests.ejb30.lite.async.common.annotated.AsyncAnnotatedMethodsBeanBase.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.lite.async.common.annotated.Async2IF.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.ejb30.common.calc.CalculatorException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.lite.async.singleton.annotated.AsyncBean.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.async.common.annotated.AnnotatedClientBase.class,
            com.sun.ts.tests.ejb30.lite.async.common.AsyncClientBase.class,
            com.sun.ts.tests.ejb30.lite.async.singleton.annotated.JsfClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.lite.async.singleton.annotated.EJBLiteServletVehicle.class,
            com.sun.ts.tests.ejb30.lite.async.common.annotated.AsyncBeanBase.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.common.statussingleton.StatusSingletonBean.class,
            com.sun.ts.tests.ejb30.lite.async.singleton.annotated.Client.class,
            com.sun.ts.tests.ejb30.lite.async.singleton.annotated.Async2Bean.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb30.lite.async.common.annotated.AsyncAnnotatedMethodsCommonIF.class
            );

            // The web.xml descriptor
            URL warResURL = Client.class.getResource("ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_async_singleton_annotated_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/ejbliteservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejblite_async_singleton_annotated_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet/ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_async_singleton_annotated_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_async_singleton_annotated_ejbliteservlet_vehicle_web, Client.class, warResURL);

        return ejblite_async_singleton_annotated_ejbliteservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void addAway() {
            super.addAway();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void voidRuntimeException() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.voidRuntimeException();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void futureRuntimeException() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.futureRuntimeException();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void futureError() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.futureError();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void futureException() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.futureException();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void futureValueList() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.futureValueList();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void addReturn() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.addReturn();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void addSyncThrowException() {
            super.addSyncThrowException();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void addSyncReturn() {
            super.addSyncReturn();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void addReturnWaitMillis() throws java.util.concurrent.ExecutionException, java.lang.InterruptedException, java.util.concurrent.TimeoutException {
            super.addReturnWaitMillis();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void cancelMayInterruptIfRunningFalse() throws java.util.concurrent.ExecutionException, java.lang.InterruptedException, java.util.concurrent.TimeoutException {
            super.cancelMayInterruptIfRunningFalse();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void cancelMayInterruptIfRunningTrue() throws java.util.concurrent.ExecutionException, java.lang.InterruptedException, java.util.concurrent.TimeoutException {
            super.cancelMayInterruptIfRunningTrue();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void passByValueOrReference() {
            super.passByValueOrReference();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void passByValueOrReferenceAsync() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.passByValueOrReferenceAsync();
        }


}