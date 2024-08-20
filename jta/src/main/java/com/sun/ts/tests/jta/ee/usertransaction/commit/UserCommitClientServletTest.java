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
@Tag("tck-javatest")

public class UserCommitClientServletTest extends com.sun.ts.tests.jta.ee.usertransaction.commit.UserCommitClient {
    static final String VEHICLE_ARCHIVE = "commit_servlet_vehicle";

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
        War:

        /com/sun/ts/tests/jta/ee/usertransaction/commit/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive commit_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "commit_servlet_vehicle_web.war");
            // The class files
            commit_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.jta.ee.common.Transact.class,
            com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
            com.sun.ts.tests.jta.ee.common.InitFailedException.class,
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
            com.sun.ts.tests.jta.ee.usertransaction.commit.UserCommitClient.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
            com.sun.ts.tests.jta.ee.common.InitFailedException.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = UserCommitClient.class.getResource("servlet_vehicle_web.xml");
            if(warResURL != null) {
              commit_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = UserCommitClient.class.getResource("/commit_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              commit_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
           archiveProcessor.processWebArchive(commit_servlet_vehicle_web, UserCommitClient.class, warResURL);

        // Ear
            EnterpriseArchive commit_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "commit_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            commit_servlet_vehicle_ear.addAsModule(commit_servlet_vehicle_web);



            // // The application.xml descriptor
            // URL earResURL = UserCommitClient.class.getResource("/com/sun/ts/tests/jta/ee/usertransaction/commit/");
            // if(earResURL != null) {
            //   commit_servlet_vehicle_ear.addAsManifestResource(earResURL, "application.xml");
            // }
            // // The sun-application.xml descriptor
            // earResURL = UserCommitClient.class.getResource("/com/sun/ts/tests/jta/ee/usertransaction/commit/.ear.sun-application.xml");
            // if(earResURL != null) {
            //   commit_servlet_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
            // }
            // archiveProcessor.processEarArchive(commit_servlet_vehicle_ear, UserCommitClient.class, earResURL);
        return commit_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testUserCommit001() throws java.lang.Exception {
            super.testUserCommit001();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testUserCommit002() throws java.lang.Exception {
            super.testUserCommit002();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testUserCommit003() throws java.lang.Exception {
            super.testUserCommit003();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testUserCommit004() throws java.lang.Exception {
            super.testUserCommit004();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testUserCommit005() throws java.lang.Exception {
            super.testUserCommit005();
        }


}