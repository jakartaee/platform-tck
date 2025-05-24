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
public class MsgSelectorTopicTestsAppclientTest extends com.sun.ts.tests.jms.core.selectorTopic.MsgSelectorTopicTests {
    static final String VEHICLE_ARCHIVE = "selectorTopic_appclient_vehicle";

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
        Client:

        /com/sun/ts/tests/jms/core/selectorTopic/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive selectorTopic_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "selectorTopic_appclient_vehicle_client.jar");
            // The class files
            selectorTopic_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.jms.core.selectorTopic.MsgSelectorTopicTests.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = MsgSelectorTopicTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              selectorTopic_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MsgSelectorTopicTests.class.getResource("selectorTopic_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              selectorTopic_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            selectorTopic_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + MsgSelectorTopicTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(selectorTopic_appclient_vehicle_client, MsgSelectorTopicTests.class, resURL);

        // Ear
            EnterpriseArchive selectorTopic_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "selectorTopic_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            selectorTopic_appclient_vehicle_ear.addAsModule(selectorTopic_appclient_vehicle_client);



        return selectorTopic_appclient_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void selectorTest01() throws java.lang.Exception {
            super.selectorTest01();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void identifierTest01() throws java.lang.Exception {
            super.identifierTest01();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void whitespaceTest1() throws java.lang.Exception {
            super.whitespaceTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void expressionTest1() throws java.lang.Exception {
            super.expressionTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void bracketingTest1() throws java.lang.Exception {
            super.bracketingTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void comparisonTest01() throws java.lang.Exception {
            super.comparisonTest01();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void operatorTest1() throws java.lang.Exception {
            super.operatorTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void betweenTest1() throws java.lang.Exception {
            super.betweenTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void inTest1() throws java.lang.Exception {
            super.inTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest01() throws java.lang.Exception {
            super.likeTest01();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void isNullTest1() throws java.lang.Exception {
            super.isNullTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void caseTest1() throws java.lang.Exception {
            super.caseTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void precedenceTest1() throws java.lang.Exception {
            super.precedenceTest1();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void nullTest01() throws java.lang.Exception {
            super.nullTest01();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void nullTest11() throws java.lang.Exception {
            super.nullTest11();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void emptyTest() throws java.lang.Exception {
            super.emptyTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void durableTopicEmptyStringSelTest() throws java.lang.Exception {
            super.durableTopicEmptyStringSelTest();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void durableTopicNullSelTest() throws java.lang.Exception {
            super.durableTopicNullSelTest();
        }


}