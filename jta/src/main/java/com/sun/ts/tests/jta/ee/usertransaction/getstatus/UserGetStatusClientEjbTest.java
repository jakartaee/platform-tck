package com.sun.ts.tests.jta.ee.usertransaction.getstatus;

import com.sun.ts.tests.jta.ee.usertransaction.getstatus.UserGetStatusClient;
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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("jta")
@Tag("platform")
@Tag("web")
@Tag("tck-appclient")

public class UserGetStatusClientEjbTest extends com.sun.ts.tests.jta.ee.usertransaction.getstatus.UserGetStatusClient {
    static final String VEHICLE_ARCHIVE = "getstatus_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        getstatus_ejb_vehicle: 
        getstatus_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        getstatus_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        getstatus_jsp_vehicle: 
        getstatus_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        getstatus_servlet_vehicle: 
        getstatus_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jta/ee/usertransaction/getstatus/ejb_vehicle_ejb.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive getstatus_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "getstatus_ejb_vehicle_client.jar");
            // The class files
            getstatus_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleHome.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class,
            com.sun.ts.tests.jta.ee.usertransaction.getstatus.UserGetStatusClient.class
            );
            // The application-client.xml descriptor
            URL resURL = UserGetStatusClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              getstatus_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            // resURL = UserGetStatusClient.class.getResource("/getstatus_jsp_vehicle_web.war.sun-web.xml");
            // if(resURL != null) {
            //   getstatus_ejb_vehicle_client.addAsManifestResource(resURL, "sun-web.xml");
            // }
            getstatus_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + UserGetStatusClient.class.getName() + "\n"), "MANIFEST.MF");
            archiveProcessor.processClientArchive(getstatus_ejb_vehicle_client, UserGetStatusClient.class, resURL);


        // Ejb
            // the jar with the correct archive name
            JavaArchive getstatus_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "getstatus_ejb_vehicle_ejb.jar");
            // The class files
            getstatus_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.jta.ee.usertransaction.getstatus.UserGetStatusClient.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.jta.ee.common.Transact.class,
                com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
                com.sun.ts.tests.jta.ee.common.InitFailedException.class,
                com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
                com.sun.ts.tests.jta.ee.common.InitFailedException.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = UserGetStatusClient.class.getResource("/ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              getstatus_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = UserGetStatusClient.class.getResource("/getstatus_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              getstatus_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            archiveProcessor.processEjbArchive(getstatus_ejb_vehicle_ejb, UserGetStatusClient.class, ejbResURL);

        // Ear
            EnterpriseArchive getstatus_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "getstatus_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            getstatus_ejb_vehicle_ear.addAsModule(getstatus_ejb_vehicle_ejb);
            getstatus_ejb_vehicle_ear.addAsModule(getstatus_ejb_vehicle_client);



            // // The application.xml descriptor
            // URL earResURL = UserGetStatusClient.class.getResource("/com/sun/ts/tests/jta/ee/usertransaction/getstatus/");
            // if(earResURL != null) {
            //   getstatus_ejb_vehicle_ear.addAsManifestResource(earResURL, "application.xml");
            // }
            // // The sun-application.xml descriptor
            // earResURL = UserGetStatusClient.class.getResource("/com/sun/ts/tests/jta/ee/usertransaction/getstatus/.ear.sun-application.xml");
            // if(earResURL != null) {
            //   getstatus_ejb_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
            // }
            // archiveProcessor.processEarArchive(getstatus_ejb_vehicle_ear, UserGetStatusClient.class, earResURL);
        return getstatus_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testUserGetStatus001() throws java.lang.Exception {
            super.testUserGetStatus001();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testUserGetStatus002() throws java.lang.Exception {
            super.testUserGetStatus002();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testUserGetStatus003() throws java.lang.Exception {
            super.testUserGetStatus003();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testUserGetStatus004() throws java.lang.Exception {
            super.testUserGetStatus004();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testUserGetStatus005() throws java.lang.Exception {
            super.testUserGetStatus005();
        }


}