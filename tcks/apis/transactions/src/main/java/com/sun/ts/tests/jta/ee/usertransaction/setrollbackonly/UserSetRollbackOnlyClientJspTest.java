package com.sun.ts.tests.jta.ee.usertransaction.setrollbackonly;

import java.lang.System.Logger;
import java.net.URL;

import com.sun.ts.lib.harness.Fault;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@ExtendWith(ArquillianExtension.class)
@Tag("jta")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")

public class UserSetRollbackOnlyClientJspTest extends com.sun.ts.tests.jta.ee.usertransaction.setrollbackonly.UserSetRollbackOnlyClient {
    static final String VEHICLE_ARCHIVE = "setrollbackonly_jsp_vehicle";

    private static String packagePath = UserSetRollbackOnlyClientJspTest.class.getPackageName().replace(".", "/");

    private static final Logger logger = System.getLogger(UserSetRollbackOnlyClientJspTest.class.getName());

    @BeforeEach
    void logStartTest(TestInfo testInfo) {
        logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
    }

    @AfterEach
    void logFinishTest(TestInfo testInfo) {
        logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
    }

    @Override
    @AfterEach
    public void cleanup() {
        logger.log(Logger.Level.INFO, "cleanup ok");
    }

    /**
     * EE10 Deployment Descriptors: setrollbackonly_ejb_vehicle: setrollbackonly_ejb_vehicle_client:
     * META-INF/application-client.xml,jar.sun-application-client.xml setrollbackonly_ejb_vehicle_ejb:
     * META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml setrollbackonly_jsp_vehicle: setrollbackonly_jsp_vehicle_web:
     * WEB-INF/web.xml,war.sun-web.xml setrollbackonly_servlet_vehicle: setrollbackonly_servlet_vehicle_web:
     * WEB-INF/web.xml,war.sun-web.xml
     *
     * Found Descriptors: War:
     *
     * /com/sun/ts/tests/jta/ee/usertransaction/setrollbackonly/jsp_vehicle_web.xml
     * /com/sun/ts/tests/common/vehicle/jsp/jsp_vehicle_web.xml Ear:
     *
     */
    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = VEHICLE_ARCHIVE, order = 2)
    public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
        // the war with the correct archive name
        WebArchive setrollbackonly_jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "setrollbackonly_jsp_vehicle_web.war");
        // The class files
        setrollbackonly_jsp_vehicle_web.addClasses(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                Fault.class, com.sun.ts.tests.jta.ee.common.Transact.class,
                com.sun.ts.tests.jta.ee.common.InvalidStatusException.class, com.sun.ts.tests.jta.ee.common.InitFailedException.class,
                com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
                com.sun.ts.tests.jta.ee.usertransaction.setrollbackonly.UserSetRollbackOnlyClient.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class, com.sun.ts.tests.jta.ee.common.InvalidStatusException.class,
                com.sun.ts.tests.jta.ee.common.InitFailedException.class, EETest.class,
                ServiceEETest.class, com.sun.ts.tests.jta.ee.common.TransactionStatus.class,
                SetupException.class, com.sun.ts.tests.common.vehicle.VehicleClient.class,
                UserSetRollbackOnlyClientJspTest.class);
        // The web.xml descriptor
        URL warResURL = UserSetRollbackOnlyClientJspTest.class.getClassLoader().getResource(packagePath + "/jsp_vehicle_web.xml");
        if (warResURL != null) {
            setrollbackonly_jsp_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
        }
        // Web content
        warResURL = UserSetRollbackOnlyClientJspTest.class.getResource("/vehicle/jsp/contentRoot/client.html");
        setrollbackonly_jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
        warResURL = UserSetRollbackOnlyClientJspTest.class.getResource("/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
        setrollbackonly_jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");

        // The sun-web.xml descriptor
        warResURL = UserSetRollbackOnlyClientJspTest.class.getClassLoader()
                .getResource(packagePath + "/setrollbackonly_jsp_vehicle_web.war.sun-web.xml");
        if (warResURL != null) {
            setrollbackonly_jsp_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
        }
        archiveProcessor.processWebArchive(setrollbackonly_jsp_vehicle_web, UserSetRollbackOnlyClientJspTest.class, warResURL);

        return setrollbackonly_jsp_vehicle_web;
        // Ear
        // EnterpriseArchive setrollbackonly_jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class,
        // "setrollbackonly_jsp_vehicle.ear");
        // setrollbackonly_jsp_vehicle_ear.addAsModule(setrollbackonly_jsp_vehicle_web);
        // return setrollbackonly_jsp_vehicle_ear;
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void testUserSetRollbackOnly001() throws java.lang.Exception {
        super.testUserSetRollbackOnly001();
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void testUserSetRollbackOnly002() throws java.lang.Exception {
        super.testUserSetRollbackOnly002();
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void testUserSetRollbackOnly003() throws java.lang.Exception {
        super.testUserSetRollbackOnly003();
    }

    @Test
    @Override
    @TargetVehicle("jsp")
    public void testUserSetRollbackOnly004() throws java.lang.Exception {
        super.testUserSetRollbackOnly004();
    }

}