package com.sun.ts.tests.ejb30.bb.async.stateful.descriptor;

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
public class ClientEjbliteservletTest extends com.sun.ts.tests.ejb30.bb.async.stateful.descriptor.Client {
    static final String VEHICLE_ARCHIVE = "ejbbb_async_stateful_descriptor_ejbliteservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejbbb_async_stateful_descriptor_ejblitejsf_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejbbb_async_stateful_descriptor_ejblitejsp_vehicle_web: WEB-INF/ejb-jar.xml
        ejbbb_async_stateful_descriptor_ejbliteservlet_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml
        ejbbb_async_stateful_descriptor_ejbliteservlet2_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml

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
            WebArchive ejbbb_async_stateful_descriptor_ejbliteservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejbbb_async_stateful_descriptor_ejbliteservlet_vehicle_web.war");
            // The class files
            ejbbb_async_stateful_descriptor_ejbliteservlet_vehicle_web.addClasses(
            Fault.class,
            com.sun.ts.tests.ejb30.bb.async.common.descriptor.Descriptor2IF.class,
            com.sun.ts.tests.ejb30.bb.async.common.descriptor.DescriptorRemoteIF.class,
            com.sun.ts.tests.ejb30.bb.async.common.descriptor.DescriptorJsfClientBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.bb.async.common.descriptor.TimeoutDescriptorBeanBase.class,
            com.sun.ts.tests.ejb30.bb.async.stateful.descriptor.EJBLiteServletVehicle.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.bb.async.common.descriptor.DescriptorIF.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.bb.async.stateful.descriptor.TimeoutDescriptorBean.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.bb.async.common.descriptor.DescriptorClientBase.class,
            com.sun.ts.tests.ejb30.bb.async.common.descriptor.Descriptor2RemoteIF.class,
            com.sun.ts.tests.ejb30.bb.async.stateful.descriptor.JsfClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.bb.async.common.descriptor.DescriptorBean.class,
            com.sun.ts.tests.ejb30.bb.async.stateful.descriptor.Client.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.bb.async.stateful.descriptor.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            SetupException.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejbbb_async_stateful_descriptor_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/ejbliteservlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejbbb_async_stateful_descriptor_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/async/stateful/descriptor/ejb-jar.xml");
            if(warResURL != null) {
              ejbbb_async_stateful_descriptor_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejb-jar.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet/ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejbbb_async_stateful_descriptor_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet_vehicle_web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejbbb_async_stateful_descriptor_ejbliteservlet_vehicle_web, Client.class, warResURL);

        return ejbbb_async_stateful_descriptor_ejbliteservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void allViews() {
            super.allViews();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void localViews() {
            super.localViews();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void remoteViews() {
            super.remoteViews();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void allParams() {
            super.allParams();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void noParams() {
            super.noParams();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void intParams() {
            super.intParams();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void intParamsLocalViews() {
            super.intParamsLocalViews();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void timeoutDescriptorBean() {
            super.timeoutDescriptorBean();
        }


}