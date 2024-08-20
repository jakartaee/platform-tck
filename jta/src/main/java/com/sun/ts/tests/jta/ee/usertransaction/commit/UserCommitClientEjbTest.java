package com.sun.ts.tests.jta.ee.usertransaction.commit;

import com.sun.ts.tests.jta.ee.usertransaction.commit.UserCommitClient;
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

public class UserCommitClientEjbTest extends com.sun.ts.tests.jta.ee.usertransaction.commit.UserCommitClient {
    static final String VEHICLE_ARCHIVE = "commit_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        commit_ejb_vehicle: 
        commit_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        commit_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        commit_jsp_vehicle: 
        commit_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        commit_servlet_vehicle: 
        commit_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/jta/ee/usertransaction/commit/ejb_vehicle_ejb.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive commit_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "commit_ejb_vehicle_client.jar");
            // The class files
            commit_ejb_vehicle_client.addClasses(
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
            URL resURL = UserCommitClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              commit_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = UserCommitClient.class.getResource("//com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              commit_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            commit_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + UserCommitClient.class.getName() + "\n"), "MANIFEST.MF");
            archiveProcessor.processClientArchive(commit_ejb_vehicle_client, UserCommitClient.class, resURL);


        // Ejb
            // the jar with the correct archive name
            JavaArchive commit_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "commit_ejb_vehicle_ejb.jar");
            // The class files
            commit_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.jta.ee.common.Transact.class,
                com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
                com.sun.ts.tests.jta.ee.common.InitFailedException.class,
                com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
                com.sun.ts.tests.jta.ee.usertransaction.commit.UserCommitClient.class,
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
            URL ejbResURL = UserCommitClient.class.getResource("/ejb_vehicle_ejb.xml");
            if(ejbResURL != null) {
              commit_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = UserCommitClient.class.getResource("/ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              commit_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            archiveProcessor.processEjbArchive(commit_ejb_vehicle_ejb, UserCommitClient.class, ejbResURL);

        // Ear
            EnterpriseArchive commit_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "commit_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            commit_ejb_vehicle_ear.addAsModule(commit_ejb_vehicle_ejb);
            commit_ejb_vehicle_ear.addAsModule(commit_ejb_vehicle_client);



            // // The application.xml descriptor
            // URL earResURL = UserCommitClient.class.getResource("/com/sun/ts/tests/jta/ee/usertransaction/commit/");
            // if(earResURL != null) {
            //   commit_ejb_vehicle_ear.addAsManifestResource(earResURL, "application.xml");
            // }
            // The sun-application.xml descriptor
            URL earResURL = UserCommitClient.class.getResource("/com/sun/ts/tests/jta/ee/usertransaction/commit/commit_ejb_vehicle_client.jar.sun-application-client.xml");
            if(earResURL != null) {
              commit_ejb_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            archiveProcessor.processEarArchive(commit_ejb_vehicle_ear, UserCommitClient.class, earResURL);
        return commit_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testUserCommit001() throws java.lang.Exception {
            super.testUserCommit001();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testUserCommit002() throws java.lang.Exception {
            super.testUserCommit002();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testUserCommit003() throws java.lang.Exception {
            super.testUserCommit003();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testUserCommit004() throws java.lang.Exception {
            super.testUserCommit004();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testUserCommit005() throws java.lang.Exception {
            super.testUserCommit005();
        }


}