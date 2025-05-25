package com.sun.ts.tests.ejb30.bb.session.stateful.timeout.descriptor;

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


@Tag("platform")
@Tag("ejb_3x_remote_optional")
@Tag("web_optional")
@Tag("tck-javatest")
@TestMethodOrder(MethodOrderer.MethodName.class)
@ExtendWith(ArquillianExtension.class)
public class ClientEjblitejspTest extends com.sun.ts.tests.ejb30.bb.session.stateful.timeout.descriptor.Client {
    static final String VEHICLE_ARCHIVE = "bb_stateful_timeout_descriptor_ejblitejsp_vehicle";

    /**
     * EE10 Deployment Descriptors: bb_stateful_timeout_descriptor_ejblitejsp_vehicle_web:
     * WEB-INF/ejb-jar.xml,war.sun-ejb-jar.xml
     * 
     * Found Descriptors: War:
     * 
     */
    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = VEHICLE_ARCHIVE, order = 2)
    public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
        // the war with the correct archive name
        WebArchive bb_stateful_timeout_descriptor_ejblitejsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "bb_stateful_timeout_descriptor_ejblitejsp_vehicle_web.war");
        // The class files
        bb_stateful_timeout_descriptor_ejblitejsp_vehicle_web.addClasses(
                com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.class,
                com.sun.ts.tests.ejb30.lite.stateful.timeout.common.ClientBase.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.ejb30.lite.stateful.timeout.common.StatefulTimeoutBeanBase.class,
                Fault.class,
                com.sun.ts.tests.common.vehicle.ejbliteshare.ReasonableStatus.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.timeout.descriptor.EJBLiteJSPTag.class,
                com.sun.ts.tests.ejb30.common.lite.NumberEnum.class,
                com.sun.ts.tests.ejb30.lite.stateful.timeout.common.StatefulTimeoutRemoteIF.class,
                com.sun.ts.tests.ejb30.common.helper.Helper.class, 
                com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase.class,
                com.sun.ts.tests.ejb30.lite.stateful.timeout.common.StatefulTimeoutIF.class,
                com.sun.ts.tests.ejb30.common.lite.EJBLiteJsfClientBase.class,
                com.sun.ts.tests.ejb30.bb.session.stateful.timeout.descriptor.Client.class, 
                EETest.class,
                ServiceEETest.class,
                com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.ejb30.common.lite.NumberIF.class);
        
        // The web.xml descriptor
        URL warResURL = Client.class.getResource("/vehicle/ejblitejsp/ejblitejsp_vehicle_web.xml");
        if (warResURL != null) {
            bb_stateful_timeout_descriptor_ejblitejsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
        
        }
        // The sun-web.xml descriptor
        warResURL = Client.class.getResource("//vehicle/ejblitejsp/ejblitejsp_vehicle_web.war.sun-web.xml");
        if (warResURL != null) {
            bb_stateful_timeout_descriptor_ejblitejsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
        }

        // Any libraries added to the war

        // Web content
        warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/timeout/descriptor/ejb-jar.xml");
        if (warResURL != null) {
            bb_stateful_timeout_descriptor_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/ejb-jar.xml");
        }
        
        warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/timeout/descriptor/ejblitejsp.tld");
        if (warResURL != null) {
            bb_stateful_timeout_descriptor_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/tlds/ejblitejsp.tld");
        }
        
        warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsp/ejblitejsp.tld");
        if (warResURL != null) {
            bb_stateful_timeout_descriptor_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/ejblitejsp.tld");
        }
        
        warResURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejblitejsp/ejblitejsp_vehicle.jsp");
        if (warResURL != null) {
            bb_stateful_timeout_descriptor_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/ejblitejsp_vehicle.jsp");
        }

        warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/session/stateful/timeout/descriptor/bb_stateful_timeout_descriptor_ejblitejsp_vehicle_web.war.sun-ejb-jar.xml");
        if (warResURL != null) {
            bb_stateful_timeout_descriptor_ejblitejsp_vehicle_web.addAsWebResource(warResURL, "/WEB-INF/sun-ejb-jar.xml");
        }

        // Call the archive processor
        archiveProcessor.processWebArchive(bb_stateful_timeout_descriptor_ejblitejsp_vehicle_web, Client.class, warResURL);

        return bb_stateful_timeout_descriptor_ejblitejsp_vehicle_web;
    }

    @Test
    @Override
    @TargetVehicle("ejblitejsp")
    public void defaultUnitLocal() {
        super.defaultUnitLocal();
    }

    @Test
    @Override
    @TargetVehicle("ejblitejsp")
    public void defaultUnitRemote() {
        super.defaultUnitRemote();
    }

    @Test
    @Override
    @TargetVehicle("ejblitejsp")
    public void defaultUnitNoInterface() {
        super.defaultUnitNoInterface();
    }

    @Test
    @Override
    @TargetVehicle("ejblitejsp")
    public void secondUnitLocal() {
        super.secondUnitLocal();
    }

    @Test
    @Override
    @TargetVehicle("ejblitejsp")
    public void secondUnitRemote() {
        super.secondUnitRemote();
    }

    @Test
    @Override
    @TargetVehicle("ejblitejsp")
    public void secondUnitNoInterface() {
        super.secondUnitNoInterface();
    }

}