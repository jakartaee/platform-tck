package com.sun.ts.tests.ejb30.bb.async.singleton.descriptor;

import com.sun.ts.tests.ejb30.bb.async.singleton.descriptor.Client;
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
@Tag("ejb_remote_async_optional")
@Tag("web_optional")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientEjblitejsfTest extends com.sun.ts.tests.ejb30.bb.async.singleton.descriptor.Client {
    static final String VEHICLE_ARCHIVE = "ejbbb_async_singleton_descriptor_ejblitejsf_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejbbb_async_singleton_descriptor_ejblitejsf_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml
        ejbbb_async_singleton_descriptor_ejblitejsp_vehicle_web: WEB-INF/ejb-jar.xml
        ejbbb_async_singleton_descriptor_ejbliteservlet_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml
        ejbbb_async_singleton_descriptor_ejbliteservlet2_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml

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
            WebArchive ejbbb_async_singleton_descriptor_ejblitejsf_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejbbb_async_singleton_descriptor_ejblitejsf_vehicle_web.war");
            // The class files
            ejbbb_async_singleton_descriptor_ejblitejsf_vehicle_web.addClasses(
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.bb.async.singleton.descriptor.JsfClient.class,
            com.sun.ts.tests.ejb30.bb.async.common.descriptor.Descriptor2IF.class,
            com.sun.ts.tests.ejb30.bb.async.common.descriptor.DescriptorRemoteIF.class,
            com.sun.ts.tests.ejb30.bb.async.common.descriptor.DescriptorJsfClientBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.bb.async.common.descriptor.TimeoutDescriptorBeanBase.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.bb.async.common.descriptor.DescriptorIF.class,
            com.sun.ts.tests.ejb30.bb.async.singleton.descriptor.HttpServletDelegate.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.bb.async.singleton.descriptor.TimeoutDescriptorBean.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.bb.async.common.descriptor.DescriptorClientBase.class,
            com.sun.ts.tests.ejb30.bb.async.common.descriptor.Descriptor2RemoteIF.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.bb.async.common.descriptor.DescriptorBean.class,
            com.sun.ts.tests.ejb30.bb.async.singleton.descriptor.Client.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejbbb_async_singleton_descriptor_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("//com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejbbb_async_singleton_descriptor_ejblitejsf_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/async/singleton/descriptor/beans.xml");
            if(warResURL != null) {
              ejbbb_async_singleton_descriptor_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/beans.xml");
            if(warResURL != null) {
              ejbbb_async_singleton_descriptor_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/beans.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/async/singleton/descriptor/ejb-jar.xml");
            if(warResURL != null) {
              ejbbb_async_singleton_descriptor_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejb-jar.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle_web.xml");
            if(warResURL != null) {
              ejbbb_async_singleton_descriptor_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejblitejsf_vehicle_web.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/async/singleton/descriptor/faces-config.xml");
            if(warResURL != null) {
              ejbbb_async_singleton_descriptor_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/faces-config.xml");
            if(warResURL != null) {
              ejbbb_async_singleton_descriptor_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/faces-config.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsf/ejblitejsf_vehicle.xhtml");
            if(warResURL != null) {
              ejbbb_async_singleton_descriptor_ejblitejsf_vehicle_web.addAsWebResource(warResURL, "/ejblitejsf_vehicle.xhtml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejbbb_async_singleton_descriptor_ejblitejsf_vehicle_web, Client.class, warResURL);

        return ejbbb_async_singleton_descriptor_ejblitejsf_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void allViews() {
            super.allViews();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void localViews() {
            super.localViews();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void remoteViews() {
            super.remoteViews();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void allParams() {
            super.allParams();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void noParams() {
            super.noParams();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void intParams() {
            super.intParams();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void intParamsLocalViews() {
            super.intParamsLocalViews();
        }

        @Test
        @Override
        @TargetVehicle("ejblitejsf")
        public void timeoutDescriptorBean() {
            super.timeoutDescriptorBean();
        }


}