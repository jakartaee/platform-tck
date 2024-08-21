package com.sun.ts.tests.ejb30.bb.async.singleton.metadata;

import com.sun.ts.tests.ejb30.bb.async.singleton.metadata.JsfClient;
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
public class JsfClientEjbliteservlet2Test extends com.sun.ts.tests.ejb30.bb.async.singleton.metadata.JsfClient {
    static final String VEHICLE_ARCHIVE = "ejbbb_async_singleton_metadata_ejbliteservlet2_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejbbb_async_singleton_metadata_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml,war.sun-ejb-jar.xml
        ejbbb_async_singleton_metadata_ejblitejsp_vehicle_web: war.sun-ejb-jar.xml
        ejbbb_async_singleton_metadata_ejbliteservlet_vehicle_web: WEB-INF/web.xml,war.sun-ejb-jar.xml
        ejbbb_async_singleton_metadata_ejbliteservlet2_vehicle_web: WEB-INF/web.xml,war.sun-ejb-jar.xml

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
            WebArchive ejbbb_async_singleton_metadata_ejbliteservlet2_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejbbb_async_singleton_metadata_ejbliteservlet2_vehicle_web.war");
            // The class files
            ejbbb_async_singleton_metadata_ejbliteservlet2_vehicle_web.addClasses(
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.ejb30.bb.async.common.metadata.BeanClassLevel3BeanBase.class,
            com.sun.ts.tests.ejb30.bb.async.common.metadata.BeanClassLevel1BeanBase.class,
            com.sun.ts.tests.ejb30.bb.async.singleton.metadata.HttpServletDelegate.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.bb.async.singleton.metadata.JsfClient.class,
            com.sun.ts.tests.ejb30.bb.async.common.AsyncClientBase.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.bb.async.common.metadata.PlainInterfaceTypeLevelRemoteIF.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.tests.ejb30.bb.async.singleton.metadata.BeanClassLevelBean.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.ejb30.common.calc.CalculatorException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.bb.async.common.AsyncJsfClientBase.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.bb.async.singleton.metadata.Client.class,
            com.sun.ts.tests.ejb30.bb.async.common.metadata.BeanClassLevel2BeanBase.class,
            com.sun.ts.tests.ejb30.bb.async.common.metadata.MetadataClientBase.class,
            com.sun.ts.tests.ejb30.bb.async.common.metadata.PlainInterfaceTypeLevelIF.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.bb.async.common.metadata.BeanClassLevel0BeanBase.class,
            com.sun.ts.tests.ejb30.bb.async.singleton.metadata.EJBLiteServlet2Filter.class,
            com.sun.ts.tests.ejb30.bb.async.common.metadata.MetadataJsfClientBase.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.common.statussingleton.StatusSingletonBean.class,
            com.sun.ts.tests.ejb30.bb.async.common.metadata.InterfaceTypeLevelBeanBase.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            com.sun.ts.lib.harness.EETest.SetupException.class
            );

            // The web.xml descriptor
            URL warResURL = JsfClient.class.getResource("ejbliteservlet2_vehicle_web.xml");
            if(warResURL != null) {
              ejbbb_async_singleton_metadata_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = JsfClient.class.getResource("/ejbliteservlet2_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              ejbbb_async_singleton_metadata_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle_web.xml");
            if(warResURL != null) {
              ejbbb_async_singleton_metadata_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet2_vehicle_web.xml");
            }
            warResURL = JsfClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle.jsp");
            if(warResURL != null) {
              ejbbb_async_singleton_metadata_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/ejbliteservlet2_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejbbb_async_singleton_metadata_ejbliteservlet2_vehicle_web, JsfClient.class, warResURL);

        return ejbbb_async_singleton_metadata_ejbliteservlet2_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void beanClassLevelReturnType() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.beanClassLevelReturnType();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void beanClassLevelRuntimeException() throws java.lang.InterruptedException, java.util.concurrent.TimeoutException {
            super.beanClassLevelRuntimeException();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void customFutureImpl() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
            super.customFutureImpl();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void beanClassLevelSyncMethod() {
            super.beanClassLevelSyncMethod();
        }


}