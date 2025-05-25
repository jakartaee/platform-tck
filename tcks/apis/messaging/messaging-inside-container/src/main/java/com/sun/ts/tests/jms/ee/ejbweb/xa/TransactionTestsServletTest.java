package com.sun.ts.tests.jms.ee.ejbweb.xa;

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
public class TransactionTestsServletTest extends com.sun.ts.tests.jms.ee.ejbweb.xa.TransactionTests {
    static final String VEHICLE_ARCHIVE = "transaction_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        transaction_ejb_vehicle: 
        transaction_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        transaction_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        transaction_jsp_vehicle: 
        transaction_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        transaction_servlet_vehicle: 
        transaction_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        transactional_ejblitejsf_vehicle_web: WEB-INF/beans.xml,WEB-INF/faces-config.xml,WEB-INF/web.xml

        Found Descriptors:
        War:

        /com/sun/ts/tests/jms/ee/ejbweb/xa/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive transaction_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "transaction_servlet_vehicle_web.war");
            // The class files
            transaction_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.jms.ee.ejbweb.xa.TransactionTests.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = TransactionTests.class.getResource("/com/sun/ts/tests/jms/ee/ejbweb/xa/servlet_vehicle_web.xml");
            if(warResURL != null) {
              transaction_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = TransactionTests.class.getResource("/com/sun/ts/tests/jms/ee/ejbweb/xa/transaction_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              transaction_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }


           // Call the archive processor
           archiveProcessor.processWebArchive(transaction_servlet_vehicle_web, TransactionTests.class, warResURL);

        // Ear
            EnterpriseArchive transaction_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "transaction_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            transaction_servlet_vehicle_ear.addAsModule(transaction_servlet_vehicle_web);


        return transaction_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void Test01() throws java.lang.Exception {
            super.Test01();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void Test02() throws java.lang.Exception {
            super.Test02();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void Test03() throws java.lang.Exception {
            super.Test03();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void Test04() throws java.lang.Exception {
            super.Test04();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void Test05() throws java.lang.Exception {
            super.Test05();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void Test06() throws java.lang.Exception {
            super.Test06();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void Test07() throws java.lang.Exception {
            super.Test07();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void Test08() throws java.lang.Exception {
            super.Test08();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void Test09() throws java.lang.Exception {
            super.Test09();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void Test10() throws java.lang.Exception {
            super.Test10();
        }


}