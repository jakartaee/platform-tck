package com.sun.ts.tests.jms.core.selectorQueue;

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
public class MsgSelectorQueueTestsAppclientTest extends com.sun.ts.tests.jms.core.selectorQueue.MsgSelectorQueueTests {
    static final String VEHICLE_ARCHIVE = "selectorQueue_appclient_vehicle";

        /**
        EE10 Deployment Descriptors:
        selectorQueue_appclient_vehicle: 
        selectorQueue_appclient_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        selectorQueue_ejb_vehicle: 
        selectorQueue_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        selectorQueue_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        selectorQueue_jsp_vehicle: 
        selectorQueue_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        selectorQueue_servlet_vehicle: 
        selectorQueue_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/jms/core/selectorQueue/appclient_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive selectorQueue_appclient_vehicle_client = ShrinkWrap.create(JavaArchive.class, "selectorQueue_appclient_vehicle_client.jar");
            // The class files
            selectorQueue_appclient_vehicle_client.addClasses(
            com.sun.ts.tests.jms.common.JmsTool.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.jms.core.selectorQueue.MsgSelectorQueueTests.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = MsgSelectorQueueTests.class.getResource("appclient_vehicle_client.xml");
            if(resURL != null) {
              selectorQueue_appclient_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            } 
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MsgSelectorQueueTests.class.getResource("selectorQueue_appclient_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              selectorQueue_appclient_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            selectorQueue_appclient_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + MsgSelectorQueueTests.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(selectorQueue_appclient_vehicle_client, MsgSelectorQueueTests.class, resURL);

        // Ear
            EnterpriseArchive selectorQueue_appclient_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "selectorQueue_appclient_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            selectorQueue_appclient_vehicle_ear.addAsModule(selectorQueue_appclient_vehicle_client);



        return selectorQueue_appclient_vehicle_ear;
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
        public void selectorTest02() throws java.lang.Exception {
            super.selectorTest02();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void selectorTest03() throws java.lang.Exception {
            super.selectorTest03();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void selectorTest04() throws java.lang.Exception {
            super.selectorTest04();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void selectorTest05() throws java.lang.Exception {
            super.selectorTest05();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void selectorTest06() throws java.lang.Exception {
            super.selectorTest06();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void selectorTest07() throws java.lang.Exception {
            super.selectorTest07();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void selectorTest08() throws java.lang.Exception {
            super.selectorTest08();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void selectorTest09() throws java.lang.Exception {
            super.selectorTest09();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void selectorTest11() throws java.lang.Exception {
            super.selectorTest11();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void selectorTest12() throws java.lang.Exception {
            super.selectorTest12();
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
        public void identifierTest02() throws java.lang.Exception {
            super.identifierTest02();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void identifierTest03() throws java.lang.Exception {
            super.identifierTest03();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void identifierTest04() throws java.lang.Exception {
            super.identifierTest04();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void identifierTest05() throws java.lang.Exception {
            super.identifierTest05();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void identifierTest06() throws java.lang.Exception {
            super.identifierTest06();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void identifierTest07() throws java.lang.Exception {
            super.identifierTest07();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void identifierTest08() throws java.lang.Exception {
            super.identifierTest08();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void identifierTest09() throws java.lang.Exception {
            super.identifierTest09();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void identifierTest10() throws java.lang.Exception {
            super.identifierTest10();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void identifierTest11() throws java.lang.Exception {
            super.identifierTest11();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void identifierTest12() throws java.lang.Exception {
            super.identifierTest12();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void identifierTest13() throws java.lang.Exception {
            super.identifierTest13();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void identifierTest14() throws java.lang.Exception {
            super.identifierTest14();
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
        public void whitespaceTest2() throws java.lang.Exception {
            super.whitespaceTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void whitespaceTest3() throws java.lang.Exception {
            super.whitespaceTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void whitespaceTest4() throws java.lang.Exception {
            super.whitespaceTest4();
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
        public void expressionTest2() throws java.lang.Exception {
            super.expressionTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void expressionTest3() throws java.lang.Exception {
            super.expressionTest3();
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
        public void bracketingTest2() throws java.lang.Exception {
            super.bracketingTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void bracketingTest3() throws java.lang.Exception {
            super.bracketingTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void bracketingTest4() throws java.lang.Exception {
            super.bracketingTest4();
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
        public void comparisonTest02() throws java.lang.Exception {
            super.comparisonTest02();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void comparisonTest03() throws java.lang.Exception {
            super.comparisonTest03();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void comparisonTest04() throws java.lang.Exception {
            super.comparisonTest04();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void comparisonTest05() throws java.lang.Exception {
            super.comparisonTest05();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void comparisonTest06() throws java.lang.Exception {
            super.comparisonTest06();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void comparisonTest07() throws java.lang.Exception {
            super.comparisonTest07();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void comparisonTest08() throws java.lang.Exception {
            super.comparisonTest08();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void comparisonTest09() throws java.lang.Exception {
            super.comparisonTest09();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void comparisonTest10() throws java.lang.Exception {
            super.comparisonTest10();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void comparisonTest11() throws java.lang.Exception {
            super.comparisonTest11();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void comparisonTest12() throws java.lang.Exception {
            super.comparisonTest12();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void comparisonTest13() throws java.lang.Exception {
            super.comparisonTest13();
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
        public void operatorTest2() throws java.lang.Exception {
            super.operatorTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void operatorTest3() throws java.lang.Exception {
            super.operatorTest3();
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
        public void betweenTest2() throws java.lang.Exception {
            super.betweenTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void betweenTest3() throws java.lang.Exception {
            super.betweenTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void betweenTest4() throws java.lang.Exception {
            super.betweenTest4();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void betweenTest5() throws java.lang.Exception {
            super.betweenTest5();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void betweenTest6() throws java.lang.Exception {
            super.betweenTest6();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void betweenTest7() throws java.lang.Exception {
            super.betweenTest7();
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
        public void inTest2() throws java.lang.Exception {
            super.inTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void inTest3() throws java.lang.Exception {
            super.inTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void inTest4() throws java.lang.Exception {
            super.inTest4();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void inTest5() throws java.lang.Exception {
            super.inTest5();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void inTest6() throws java.lang.Exception {
            super.inTest6();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void inTest7() throws java.lang.Exception {
            super.inTest7();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void inTest8() throws java.lang.Exception {
            super.inTest8();
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
        public void likeTest02() throws java.lang.Exception {
            super.likeTest02();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest03() throws java.lang.Exception {
            super.likeTest03();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest04() throws java.lang.Exception {
            super.likeTest04();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest05() throws java.lang.Exception {
            super.likeTest05();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest06() throws java.lang.Exception {
            super.likeTest06();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest07() throws java.lang.Exception {
            super.likeTest07();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest08() throws java.lang.Exception {
            super.likeTest08();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest09() throws java.lang.Exception {
            super.likeTest09();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest10() throws java.lang.Exception {
            super.likeTest10();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest11() throws java.lang.Exception {
            super.likeTest11();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest12() throws java.lang.Exception {
            super.likeTest12();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest13() throws java.lang.Exception {
            super.likeTest13();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest14() throws java.lang.Exception {
            super.likeTest14();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest15() throws java.lang.Exception {
            super.likeTest15();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest16() throws java.lang.Exception {
            super.likeTest16();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest17() throws java.lang.Exception {
            super.likeTest17();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest18() throws java.lang.Exception {
            super.likeTest18();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest19() throws java.lang.Exception {
            super.likeTest19();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest20() throws java.lang.Exception {
            super.likeTest20();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void likeTest21() throws java.lang.Exception {
            super.likeTest21();
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
        public void isNullTest2() throws java.lang.Exception {
            super.isNullTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void isNullTest3() throws java.lang.Exception {
            super.isNullTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void isNullTest4() throws java.lang.Exception {
            super.isNullTest4();
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
        public void precedenceTest2() throws java.lang.Exception {
            super.precedenceTest2();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void precedenceTest3() throws java.lang.Exception {
            super.precedenceTest3();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void precedenceTest4() throws java.lang.Exception {
            super.precedenceTest4();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void precedenceTest5() throws java.lang.Exception {
            super.precedenceTest5();
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
        public void nullTest02() throws java.lang.Exception {
            super.nullTest02();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void nullTest03() throws java.lang.Exception {
            super.nullTest03();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void nullTest04() throws java.lang.Exception {
            super.nullTest04();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void nullTest05() throws java.lang.Exception {
            super.nullTest05();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void nullTest06() throws java.lang.Exception {
            super.nullTest06();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void nullTest07() throws java.lang.Exception {
            super.nullTest07();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void nullTest08() throws java.lang.Exception {
            super.nullTest08();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void nullTest09() throws java.lang.Exception {
            super.nullTest09();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void nullTest10() throws java.lang.Exception {
            super.nullTest10();
        }

        @Test
        @Override
        @TargetVehicle("appclient")
        public void nullTest11() throws java.lang.Exception {
            super.nullTest11();
        }


}