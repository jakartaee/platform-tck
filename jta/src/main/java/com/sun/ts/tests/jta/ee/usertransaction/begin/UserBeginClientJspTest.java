package com.sun.ts.tests.jta.ee.usertransaction.begin;

import com.sun.ts.tests.jta.ee.usertransaction.begin.UserBeginClient;
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

public class UserBeginClientJspTest extends com.sun.ts.tests.jta.ee.usertransaction.begin.UserBeginClient {
    static final String VEHICLE_ARCHIVE = "begin_jsp_vehicle";

        /**
        EE10 Deployment Descriptors:
        begin_ejb_vehicle: 
        begin_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        begin_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        begin_jsp_vehicle: 
        begin_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        begin_servlet_vehicle: 
        begin_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jta/ee/usertransaction/begin/jsp_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive begin_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "begin_jsp_vehicle_web.war");
            // The class files
            begin_jsp_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.jta.ee.usertransaction.begin.UserBeginClient.class,
            com.sun.ts.tests.jta.ee.common.Transact.class,
                                com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
            com.sun.ts.tests.jta.ee.common.InitFailedException.class,
                                com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
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
            URL warResURL = UserBeginClient.class.getResource("jsp_vehicle_web.xml");
            if(warResURL != null) {
              begin_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = UserBeginClient.class.getResource("/jsp_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              begin_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }
            // Web content
            warResURL = UserBeginClient.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
            begin_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
            warResURL = UserBeginClient.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
            begin_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");

           archiveProcessor.processWebArchive(begin_jsp_vehicle_web, UserBeginClient.class, warResURL);

        // Ear
            EnterpriseArchive begin_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "begin_jsp_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            begin_jsp_vehicle_ear.addAsModule(begin_jsp_vehicle_web);



            // The application.xml descriptor
            URL earResURL = UserBeginClient.class.getResource("/com/sun/ts/tests/jta/ee/usertransaction/begin/");
            if(earResURL != null) {
              begin_jsp_vehicle_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = UserBeginClient.class.getResource("/com/sun/ts/tests/jta/ee/usertransaction/begin/.ear.sun-application.xml");
            if(earResURL != null) {
              begin_jsp_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            archiveProcessor.processEarArchive(begin_jsp_vehicle_ear, UserBeginClient.class, earResURL);
        return begin_jsp_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testUserBegin001() throws java.lang.Exception {
            super.testUserBegin001();
        }

        @Test
        @Override
        @TargetVehicle("jsp")
        public void testUserBegin002() throws java.lang.Exception {
            super.testUserBegin002();
        }


}