package com.sun.ts.tests.jms.core.queueMsgProperties;

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
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("jms")
@Tag("platform")
@Tag("jms_web")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class QueuePropertyTestsEjbTest extends com.sun.ts.tests.jms.core.queueMsgProperties.QueuePropertyTests {
    static final String VEHICLE_ARCHIVE = "queueMsgProperties_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        queueMsgProperties_appclient_vehicle: 
        queueMsgProperties_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        queueMsgProperties_ejb_vehicle: 
        queueMsgProperties_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        queueMsgProperties_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        queueMsgProperties_jsp_vehicle: 
        queueMsgProperties_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        queueMsgProperties_servlet_vehicle: 
        queueMsgProperties_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core/queueMsgProperties/ejb_vehicle_ejb.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.jar.sun-ejb-jar.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive queueMsgProperties_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "queueMsgProperties_ejb_vehicle_client.jar");
            // The class files
            queueMsgProperties_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.core.queueMsgProperties.QueuePropertyTests.class
            );
            // The application-client.xml descriptor
            URL resURL = QueuePropertyTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              queueMsgProperties_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = QueuePropertyTests.class.getResource("queueMsgProperties_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              queueMsgProperties_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            //queueMsgProperties_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + QueuePropertyTests.class.getName() + "\n"), "MANIFEST.MF");
            queueMsgProperties_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(queueMsgProperties_ejb_vehicle_client, QueuePropertyTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive queueMsgProperties_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "queueMsgProperties_ejb_vehicle_ejb.jar");
            // The class files
            queueMsgProperties_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                Fault.class,
                com.sun.ts.tests.jms.core.queueMsgProperties.QueuePropertyTests.class,
                EETest.class,
                ServiceEETest.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = QueuePropertyTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              queueMsgProperties_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = QueuePropertyTests.class.getResource("queueMsgProperties_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              queueMsgProperties_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(queueMsgProperties_ejb_vehicle_ejb, QueuePropertyTests.class, ejbResURL);

        // Ear
            EnterpriseArchive queueMsgProperties_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "queueMsgProperties_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            queueMsgProperties_ejb_vehicle_ear.addAsModule(queueMsgProperties_ejb_vehicle_ejb);
            queueMsgProperties_ejb_vehicle_ear.addAsModule(queueMsgProperties_ejb_vehicle_client);



        return queueMsgProperties_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgPropertiesQTest() throws java.lang.Exception {
            super.msgPropertiesQTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgPropertiesConversionQTest() throws java.lang.Exception {
            super.msgPropertiesConversionQTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void msgPropertyExistTest() throws java.lang.Exception {
            super.msgPropertyExistTest();
        }


}