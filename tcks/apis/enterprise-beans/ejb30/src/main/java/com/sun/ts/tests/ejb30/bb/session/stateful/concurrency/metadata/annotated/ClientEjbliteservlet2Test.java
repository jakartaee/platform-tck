package com.sun.ts.tests.ejb30.bb.session.stateful.concurrency.metadata.annotated;

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
@Tag("ejb_3x_remote_optional")
@Tag("web_optional")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientEjbliteservlet2Test extends com.sun.ts.tests.ejb30.bb.session.stateful.concurrency.metadata.annotated.Client {
    static final String VEHICLE_ARCHIVE = "bb_stateful_concurrency_metadata_annotated_ejbliteservlet2_vehicle";

        /**
        EE10 Deployment Descriptors:
        bb_stateful_concurrency_metadata_annotated_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml,war.sun-ejb-jar.xml
        bb_stateful_concurrency_metadata_annotated_ejblitejsp_vehicle_web: war.sun-ejb-jar.xml
        bb_stateful_concurrency_metadata_annotated_ejbliteservlet_vehicle_web: WEB-INF/web.xml,war.sun-ejb-jar.xml
        bb_stateful_concurrency_metadata_annotated_ejbliteservlet2_vehicle_web: WEB-INF/web.xml,war.sun-ejb-jar.xml

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
            WebArchive bb_stateful_concurrency_metadata_annotated_ejbliteservlet2_vehicle_web = ShrinkWrap.create(WebArchive.class, "bb_stateful_concurrency_metadata_annotated_ejbliteservlet2_vehicle_web.war");
            // The class files
            bb_stateful_concurrency_metadata_annotated_ejbliteservlet2_vehicle_web.addClasses(
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.metadata.common.JsfClientBase.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.concurrency.metadata.annotated.JsfClient.class,
            Fault.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.Pinger.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.concurrency.metadata.annotated.ContainerConcurrencyBean.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.concurrency.metadata.annotated.NotAllowedConcurrencyBean.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyClientBase.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.class,
            com.sun.ts.tests.ejb30.common.helper.Helper.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
            EETest.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.concurrency.metadata.annotated.HttpServletDelegate.class,
            ServiceEETest.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.metadata.common.StatefulConcurrencyBeanBase.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.ejb30.common.lite.NumberIF.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyJsfClientBase.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.concurrency.metadata.annotated.StatefulConcurrencyRemoteIF.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.concurrency.metadata.annotated.EJBLiteServlet2Filter.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.concurrency.metadata.annotated.StatefulConcurrencyBeanBase2.class,
            com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.concurrency.metadata.annotated.Client.class,
            com.sun.ts.tests.ejb30.lite.stateful.concurrency.metadata.common.ClientBase.class,
            com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
            com.sun.ts.tests.ejb30.bb.session.stateful.concurrency.metadata.annotated.DefaultConcurrencyBean.class,
            com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            SetupException.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("ejbliteservlet2_vehicle_web.xml");
            if(warResURL != null) {
              bb_stateful_concurrency_metadata_annotated_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/ejbliteservlet2_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              bb_stateful_concurrency_metadata_annotated_ejbliteservlet2_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

            // Web content
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle_web.xml");
            if(warResURL != null) {
              bb_stateful_concurrency_metadata_annotated_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejbliteservlet2_vehicle_web.xml");
            }
            warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejbliteservlet2/ejbliteservlet2_vehicle.jsp");
            if(warResURL != null) {
              bb_stateful_concurrency_metadata_annotated_ejbliteservlet2_vehicle_web.addAsWebResource(warResURL, "/ejbliteservlet2_vehicle.jsp");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(bb_stateful_concurrency_metadata_annotated_ejbliteservlet2_vehicle_web, Client.class, warResURL);

        return bb_stateful_concurrency_metadata_annotated_ejbliteservlet2_vehicle_web;
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void notAllowed() throws java.lang.InterruptedException {
            super.notAllowed();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void containerConcurrent() throws java.lang.InterruptedException {
            super.containerConcurrent();
        }

        @Test
        @Override
        @TargetVehicle("ejbliteservlet2")
        public void defaultConcurrent() throws java.lang.InterruptedException {
            super.defaultConcurrent();
        }


}