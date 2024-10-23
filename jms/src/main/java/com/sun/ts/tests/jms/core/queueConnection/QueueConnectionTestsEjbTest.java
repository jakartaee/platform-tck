package com.sun.ts.tests.jms.core.queueConnection;

import com.sun.ts.tests.jms.core.queueConnection.QueueConnectionTests;
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
@Tag("jms")
@Tag("platform")
@Tag("jms_web")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class QueueConnectionTestsEjbTest extends com.sun.ts.tests.jms.core.queueConnection.QueueConnectionTests {
    static final String VEHICLE_ARCHIVE = "queueConnection_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        queueConnection_appclient_vehicle: 
        queueConnection_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        queueConnection_ejb_vehicle: 
        queueConnection_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        queueConnection_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        queueConnection_jsp_vehicle: 
        queueConnection_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        queueConnection_servlet_vehicle: 
        queueConnection_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/core/queueConnection/ejb_vehicle_ejb.xml
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
            JavaArchive queueConnection_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "queueConnection_ejb_vehicle_client.jar");
            // The class files
            queueConnection_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jms.core.queueConnection.QueueConnectionTests.class
            );
            // The application-client.xml descriptor
            URL resURL = QueueConnectionTests.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              queueConnection_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = QueueConnectionTests.class.getResource("queueConnection_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              queueConnection_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            //queueConnection_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + QueueConnectionTests.class.getName() + "\n"), "MANIFEST.MF");
            queueConnection_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(queueConnection_ejb_vehicle_client, QueueConnectionTests.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive queueConnection_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "queueConnection_ejb_vehicle_ejb.jar");
            // The class files
            queueConnection_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.jms.core.queueConnection.QueueConnectionTests.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = QueueConnectionTests.class.getResource("ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              queueConnection_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = QueueConnectionTests.class.getResource("queueConnection_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              queueConnection_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(queueConnection_ejb_vehicle_ejb, QueueConnectionTests.class, ejbResURL);

        // Ear
            EnterpriseArchive queueConnection_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "queueConnection_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            queueConnection_ejb_vehicle_ear.addAsModule(queueConnection_ejb_vehicle_ejb);
            queueConnection_ejb_vehicle_ear.addAsModule(queueConnection_ejb_vehicle_client);



        return queueConnection_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void connNotStartedQueueTest() throws java.lang.Exception {
            super.connNotStartedQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void metaDataTests() throws java.lang.Exception {
            super.metaDataTests();
        }


}