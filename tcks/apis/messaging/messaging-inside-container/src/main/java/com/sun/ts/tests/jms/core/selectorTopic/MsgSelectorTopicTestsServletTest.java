package com.sun.ts.tests.jms.core.selectorTopic;

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
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
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
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class MsgSelectorTopicTestsServletTest extends com.sun.ts.tests.jms.core.selectorTopic.MsgSelectorTopicTests {
    static final String VEHICLE_ARCHIVE = "selectorTopic_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        selectorTopic_appclient_vehicle: 
        selectorTopic_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        selectorTopic_ejb_vehicle: 
        selectorTopic_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        selectorTopic_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        selectorTopic_jsp_vehicle: 
        selectorTopic_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        selectorTopic_servlet_vehicle: 
        selectorTopic_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/core/selectorTopic/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive selectorTopic_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "selectorTopic_servlet_vehicle_web.war");
            // The class files
            selectorTopic_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.core.selectorTopic.MsgSelectorTopicTests.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = MsgSelectorTopicTests.class.getResource("/com/sun/ts/tests/jms/core/selectorTopic/servlet_vehicle_web.xml");
            if(warResURL != null) {
              selectorTopic_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = MsgSelectorTopicTests.class.getResource("/com/sun/ts/tests/jms/core/selectorTopic/selectorTopic_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              selectorTopic_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(selectorTopic_servlet_vehicle_web, MsgSelectorTopicTests.class, warResURL);

        // Ear
            EnterpriseArchive selectorTopic_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "selectorTopic_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            selectorTopic_servlet_vehicle_ear.addAsModule(selectorTopic_servlet_vehicle_web);



        return selectorTopic_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void selectorTest01() throws java.lang.Exception {
            super.selectorTest01();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void identifierTest01() throws java.lang.Exception {
            super.identifierTest01();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void whitespaceTest1() throws java.lang.Exception {
            super.whitespaceTest1();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void expressionTest1() throws java.lang.Exception {
            super.expressionTest1();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void bracketingTest1() throws java.lang.Exception {
            super.bracketingTest1();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void comparisonTest01() throws java.lang.Exception {
            super.comparisonTest01();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void operatorTest1() throws java.lang.Exception {
            super.operatorTest1();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void betweenTest1() throws java.lang.Exception {
            super.betweenTest1();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void inTest1() throws java.lang.Exception {
            super.inTest1();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void likeTest01() throws java.lang.Exception {
            super.likeTest01();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void isNullTest1() throws java.lang.Exception {
            super.isNullTest1();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void caseTest1() throws java.lang.Exception {
            super.caseTest1();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void precedenceTest1() throws java.lang.Exception {
            super.precedenceTest1();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void nullTest01() throws java.lang.Exception {
            super.nullTest01();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void nullTest11() throws java.lang.Exception {
            super.nullTest11();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void emptyTest() throws java.lang.Exception {
            super.emptyTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void durableTopicEmptyStringSelTest() throws java.lang.Exception {
            super.durableTopicEmptyStringSelTest();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void durableTopicNullSelTest() throws java.lang.Exception {
            super.durableTopicNullSelTest();
        }


}