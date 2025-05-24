package com.sun.ts.tests.ejb30.lite.stateful.concurrency.metadata.descriptor;

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
public class ClientEjbliteservletTest extends com.sun.ts.tests.ejb30.lite.stateful.concurrency.metadata.descriptor.Client {
    static final String VEHICLE_ARCHIVE = "ejblite_stateful_concurrency_metadata_descriptor_ejbliteservlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        ejblite_stateful_concurrency_metadata_descriptor_ejblitejsp_vehicle_web: WEB-INF/ejb-jar.xml
        ejblite_stateful_concurrency_metadata_descriptor_ejbliteservlet_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml
        ejblite_stateful_concurrency_metadata_descriptor_ejbliteservlet2_vehicle_web: WEB-INF/ejb-jar.xml,WEB-INF/web.xml

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
            WebArchive ejblite_stateful_concurrency_metadata_descriptor_ejbliteservlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "ejblite_stateful_concurrency_metadata_descriptor_ejbliteservlet_vehicle_web.war");
            // The class files
            ejblite_stateful_concurrency_metadata_descriptor_ejbliteservlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.Pinger.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.metadata.descriptor.Client.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.metadata.descriptor.HttpServletDelegate.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.metadata.common.ClientBase.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.metadata.descriptor.EJBLiteServletVehicle.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyClientBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.metadata.common.StatefulConcurrencyBeanBase.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("ejbliteservlet_vehicle_web.xml");
            if(warResURL != null) {
              ejblite_stateful_concurrency_metadata_descriptor_ejbliteservlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }


            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/lite/stateful/concurrency/metadata/descriptor/ejb-jar.xml");
            if(warResURL != null) {
              ejblite_stateful_concurrency_metadata_descriptor_ejbliteservlet_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejb-jar.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(ejblite_stateful_concurrency_metadata_descriptor_ejbliteservlet_vehicle_web, Client.class, warResURL);

        return ejblite_stateful_concurrency_metadata_descriptor_ejbliteservlet_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void notAllowed() throws java.lang.InterruptedException {
            super.notAllowed();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void containerConcurrent() throws java.lang.InterruptedException {
            super.containerConcurrent();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet")
        public void defaultConcurrent() throws java.lang.InterruptedException {
            super.defaultConcurrent();
        }


}