package com.sun.ts.tests.jta.ee.usertransaction.setrollbackonly;

import com.sun.ts.tests.jta.ee.usertransaction.setrollbackonly.UserSetRollbackOnlyClient;
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

public class UserSetRollbackOnlyClientEjbTest extends com.sun.ts.tests.jta.ee.usertransaction.setrollbackonly.UserSetRollbackOnlyClient {
    static final String VEHICLE_ARCHIVE = "setrollbackonly_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        setrollbackonly_ejb_vehicle: 
        setrollbackonly_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        setrollbackonly_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        setrollbackonly_jsp_vehicle: 
        setrollbackonly_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        setrollbackonly_servlet_vehicle: 
        setrollbackonly_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jta/ee/usertransaction/setrollbackonly/ejb_vehicle_ejb.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive setrollbackonly_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "setrollbackonly_ejb_vehicle_client.jar");
            // The class files
            setrollbackonly_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = UserSetRollbackOnlyClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              setrollbackonly_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            // resURL = UserSetRollbackOnlyClient.class.getResource("//com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.jar.sun-application-client.xml");
            // if(resURL != null) {
            //   setrollbackonly_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            // }
            setrollbackonly_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + UserSetRollbackOnlyClient.class.getName() + "\n"), "MANIFEST.MF");
            archiveProcessor.processClientArchive(setrollbackonly_ejb_vehicle_client, UserSetRollbackOnlyClient.class, resURL);


        // Ejb
            // the jar with the correct archive name
            JavaArchive setrollbackonly_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "setrollbackonly_ejb_vehicle_ejb.jar");
            // The class files
            setrollbackonly_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.jta.ee.common.Transact.class,
                com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
                com.sun.ts.tests.jta.ee.common.InitFailedException.class,
                com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
                com.sun.ts.tests.jta.ee.usertransaction.setrollbackonly.UserSetRollbackOnlyClient.class,
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
            URL ejbResURL = UserSetRollbackOnlyClient.class.getResource("/ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              setrollbackonly_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = UserSetRollbackOnlyClient.class.getResource("/setrollbackonly_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              setrollbackonly_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            archiveProcessor.processEjbArchive(setrollbackonly_ejb_vehicle_ejb, UserSetRollbackOnlyClient.class, ejbResURL);

        // Ear
            EnterpriseArchive setrollbackonly_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "setrollbackonly_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            setrollbackonly_ejb_vehicle_ear.addAsModule(setrollbackonly_ejb_vehicle_ejb);
            setrollbackonly_ejb_vehicle_ear.addAsModule(setrollbackonly_ejb_vehicle_client);



            // // The application.xml descriptor
            // URL earResURL = UserSetRollbackOnlyClient.class.getResource("/com/sun/ts/tests/jta/ee/usertransaction/setrollbackonly/");
            // if(earResURL != null) {
            //   setrollbackonly_ejb_vehicle_ear.addAsManifestResource(earResURL, "application.xml");
            // }
            // The sun-application.xml descriptor
            URL earResURL = UserSetRollbackOnlyClient.class.getResource("/com/sun/ts/tests/jta/ee/usertransaction/setrollbackonly/setrollbackonly_ejb_vehicle_client.jar.sun-application-client.xml");
            if(earResURL != null) {
              setrollbackonly_ejb_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            archiveProcessor.processEarArchive(setrollbackonly_ejb_vehicle_ear, UserSetRollbackOnlyClient.class, earResURL);
        return setrollbackonly_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testUserSetRollbackOnly001() throws java.lang.Exception {
            super.testUserSetRollbackOnly001();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testUserSetRollbackOnly002() throws java.lang.Exception {
            super.testUserSetRollbackOnly002();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testUserSetRollbackOnly003() throws java.lang.Exception {
            super.testUserSetRollbackOnly003();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testUserSetRollbackOnly004() throws java.lang.Exception {
            super.testUserSetRollbackOnly004();
        }


}