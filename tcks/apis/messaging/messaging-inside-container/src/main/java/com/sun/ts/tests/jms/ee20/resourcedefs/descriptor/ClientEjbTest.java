package com.sun.ts.tests.jms.ee20.resourcedefs.descriptor;

import com.sun.ts.tests.jms.ee20.resourcedefs.descriptor.Client;
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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@Disabled("https://github.com/jakartaee/platform-tck/issues/2231")
@ExtendWith(ArquillianExtension.class)
@Tag("jms")
@Tag("jms_optional")
@Tag("platform_optional")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientEjbTest extends com.sun.ts.tests.jms.ee20.resourcedefs.descriptor.Client {
    static final String VEHICLE_ARCHIVE = "resourcedefs_descriptor_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        resourcedefs_descriptor_appclient_vehicle: 
        resourcedefs_descriptor_appclient_vehicle_client: META-INF/application-client.xml
        resourcedefs_descriptor_ejb_vehicle: 
        resourcedefs_descriptor_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        resourcedefs_descriptor_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        resourcedefs_descriptor_jsp_vehicle: 
        resourcedefs_descriptor_jsp_vehicle_web: WEB-INF/web.xml
        resourcedefs_descriptor_servlet_vehicle: 
        resourcedefs_descriptor_servlet_vehicle_web: WEB-INF/web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jms/ee20/resourcedefs/descriptor/ejb_vehicle_ejb.xml
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
            JavaArchive resourcedefs_descriptor_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "resourcedefs_descriptor_ejb_vehicle_client.jar");
            // The class files
            resourcedefs_descriptor_ejb_vehicle_client.addClasses(
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
            com.sun.ts.tests.jms.ee20.resourcedefs.descriptor.Client.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
                resourcedefs_descriptor_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            // resURL = Client.class.getResource("ejb_vehicle_client.jar.sun-application-client.xml");
            // if(resURL != null) {
            //   resourcedefs_descriptor_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            // }
            // resourcedefs_descriptor_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            resourcedefs_descriptor_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(resourcedefs_descriptor_ejb_vehicle_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive resourcedefs_descriptor_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "resourcedefs_descriptor_ejb_vehicle_ejb.jar");
            // The class files
            resourcedefs_descriptor_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.jms.common.JmsTool.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.jms.ee20.resourcedefs.descriptor.Client.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("xml/ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              resourcedefs_descriptor_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // // The sun-ejb-jar.xml file
            // ejbResURL = Client.class.getResource("ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            // if(ejbResURL != null) {
            //   resourcedefs_descriptor_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            // }
            // Call the archive processor
            archiveProcessor.processEjbArchive(resourcedefs_descriptor_ejb_vehicle_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive resourcedefs_descriptor_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "resourcedefs_descriptor_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            resourcedefs_descriptor_ejb_vehicle_ear.addAsModule(resourcedefs_descriptor_ejb_vehicle_ejb);
            resourcedefs_descriptor_ejb_vehicle_ear.addAsModule(resourcedefs_descriptor_ejb_vehicle_client);



            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(resourcedefs_descriptor_ejb_vehicle_ear, Client.class, earResURL);
        return resourcedefs_descriptor_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void sendAndRecvQueueTest() throws java.lang.Exception {
            super.sendAndRecvQueueTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void sendAndRecvTopicTest() throws java.lang.Exception {
            super.sendAndRecvTopicTest();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void checkClientIDOnDurableConnFactoryTest() throws java.lang.Exception {
            super.checkClientIDOnDurableConnFactoryTest();
        }


}