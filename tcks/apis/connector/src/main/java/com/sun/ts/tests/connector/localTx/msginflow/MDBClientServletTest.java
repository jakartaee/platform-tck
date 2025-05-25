package com.sun.ts.tests.connector.localTx.msginflow;

import java.net.URL;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("connector_mdb_optional")
@Tag("platform")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class MDBClientServletTest extends com.sun.ts.tests.connector.localTx.msginflow.MDBClient {
        @Deployment(name = "msginflow_mdb", order = 1, testable = false)
        public static EnterpriseArchive createCommonDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
            JavaArchive msginflow_mdb_msginflow_ejb = ShrinkWrap.create(JavaArchive.class, "msginflow_mdb_msginflow_ejb.jar");
            // The class files
            msginflow_mdb_msginflow_ejb.addClasses(
                    com.sun.ts.tests.connector.mdb.MessageBean.class,
                    com.sun.ts.tests.connector.util.DBSupport.class
            );
            URL ejbResURL = MDBClient.class.getResource("/com/sun/ts/tests/connector/mdb/msginflow_mdb_msginflow_ejb.xml");
            msginflow_mdb_msginflow_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            ejbResURL = MDBClient.class.getResource("/com/sun/ts/tests/connector/mdb/msginflow_mdb_msginflow_ejb.jar.sun-ejb-jar.xml");
            msginflow_mdb_msginflow_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            archiveProcessor.processEjbArchive(msginflow_mdb_msginflow_ejb, com.sun.ts.tests.connector.mdb.MessageBean.class, ejbResURL);

            JavaArchive msginflow1_mdb_msginflow_ejb = ShrinkWrap.create(JavaArchive.class, "msginflow1_mdb_msginflow_ejb.jar");
            // The class files
            msginflow1_mdb_msginflow_ejb.addClasses(
                    com.sun.ts.tests.connector.mdb.MessageBeanOne.class,
                    com.sun.ts.tests.connector.util.DBSupport.class
            );
            ejbResURL = MDBClient.class.getResource("/com/sun/ts/tests/connector/mdb/msginflow1_mdb_msginflow_ejb.xml");
            msginflow1_mdb_msginflow_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            ejbResURL = MDBClient.class.getResource("/com/sun/ts/tests/connector/mdb/msginflow1_mdb_msginflow_ejb.jar.sun-ejb-jar.xml");
            msginflow1_mdb_msginflow_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            archiveProcessor.processEjbArchive(msginflow1_mdb_msginflow_ejb, com.sun.ts.tests.connector.mdb.MessageBean.class, ejbResURL);

            EnterpriseArchive msginflow_mdb_ear = ShrinkWrap.create(EnterpriseArchive.class, "msginflow_mdb.ear");
            msginflow_mdb_ear.addAsModule(msginflow_mdb_msginflow_ejb);
            msginflow_mdb_ear.addAsModule(msginflow1_mdb_msginflow_ejb);


            return msginflow_mdb_ear;
        }

        @Deployment(name = "msginflow_mdb_jca", order = 1, testable = false)
        public static EnterpriseArchive createCommonDeployment1(@ArquillianResource TestArchiveProcessor archiveProcessor)  {
            JavaArchive msginflow_mdb_jca_msginflow_ejb = ShrinkWrap.create(JavaArchive.class, "msginflow_mdb_jca_msginflow_ejb.jar");
            // The class files
            msginflow_mdb_jca_msginflow_ejb.addClasses(
                    com.sun.ts.tests.connector.mdb.JCAMessageBean.class,
                    com.sun.ts.tests.connector.util.DBSupport.class
            );
            URL ejbResURL = MDBClient.class.getResource("/com/sun/ts/tests/connector/mdb/msginflow_mdb_jca_msginflow_ejb.xml");
                msginflow_mdb_jca_msginflow_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            ejbResURL = MDBClient.class.getResource("/com/sun/ts/tests/connector/mdb/msginflow_mdb_jca_msginflow_ejb.jar.sun-ejb-jar.xml");
            msginflow_mdb_jca_msginflow_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            archiveProcessor.processEjbArchive(msginflow_mdb_jca_msginflow_ejb, com.sun.ts.tests.connector.mdb.JCAMessageBean.class, ejbResURL);

            EnterpriseArchive msginflow_mdb_jca_ear = ShrinkWrap.create(EnterpriseArchive.class, "msginflow_mdb_jca.ear");
            msginflow_mdb_jca_ear.addAsModule(msginflow_mdb_jca_msginflow_ejb);

            return msginflow_mdb_jca_ear;
        }

    static final String VEHICLE_ARCHIVE = "msginflow_mdb_servlet_vehicle";

        /**
        EE10 Deployment Descriptors:
        msginflow_mdb: 
        msginflow_mdb_ejb_vehicle: 
        msginflow_mdb_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        msginflow_mdb_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        msginflow_mdb_jca: 
        msginflow_mdb_jca_msginflow_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        msginflow_mdb_jsp_vehicle: 
        msginflow_mdb_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        msginflow_mdb_msginflow_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        msginflow_mdb_servlet_vehicle: 
        msginflow_mdb_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Ejb:

        /com/sun/ts/tests/connector/mdb/msginflow_mdb_jca_msginflow_ejb.xml
        /com/sun/ts/tests/connector/mdb/msginflow_mdb_jca_msginflow_ejb.jar.sun-ejb-jar.xml
        War:

        /com/sun/ts/tests/connector/localTx/msginflow/servlet_vehicle_web.xml
        /com/sun/ts/tests/common/vehicle/servlet/servlet_vehicle_web.xml
        Rar:

        /com/sun/ts/tests/common/connector/whitebox/mdcomplete/ra-md-complete.xml
        Ear:

        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive msginflow_mdb_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "msginflow_mdb_servlet_vehicle_web.war");
            // The class files
            msginflow_mdb_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.connector.util.DBSupport.class,
            EETest.class,
            ServiceEETest.class,
            com.sun.ts.tests.connector.localTx.msginflow.MDBClient.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = MDBClient.class.getResource("servlet_vehicle_web.xml");
            if(warResURL != null) {
              msginflow_mdb_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = MDBClient.class.getResource("msginflow_mdb_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              msginflow_mdb_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

           // Call the archive processor
           archiveProcessor.processWebArchive(msginflow_mdb_servlet_vehicle_web, MDBClient.class, warResURL);


        // Ear
            EnterpriseArchive msginflow_mdb_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "msginflow_mdb_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            msginflow_mdb_servlet_vehicle_ear.addAsModule(msginflow_mdb_servlet_vehicle_web);


            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(msginflow_mdb_servlet_vehicle_ear, MDBClient.class, earResURL);
        return msginflow_mdb_servlet_vehicle_ear;
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testReceiveMessage() throws java.lang.Exception {
            super.testReceiveMessage();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testProxyInterfaceImp() throws java.lang.Exception {
            super.testProxyInterfaceImp();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testUniqueMessageEndpoint() throws java.lang.Exception {
            super.testUniqueMessageEndpoint();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testMessageEndpointFactoryForEquals() throws java.lang.Exception {
            super.testMessageEndpointFactoryForEquals();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testUniqueMessageEndpointFactory() throws java.lang.Exception {
            super.testUniqueMessageEndpointFactory();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testEndpointActivationName() throws java.lang.Exception {
            super.testEndpointActivationName();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testGetEndpoinClass() throws java.lang.Exception {
            super.testGetEndpoinClass();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testMessageDeliveryTransacted() throws java.lang.Exception {
            super.testMessageDeliveryTransacted();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testMessageDeliveryNonTransacted() throws java.lang.Exception {
            super.testMessageDeliveryNonTransacted();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testMessageDeliveryTransactedUsingXid() throws java.lang.Exception {
            super.testMessageDeliveryTransactedUsingXid();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testActivationSpeccalledOnce() throws java.lang.Exception {
            super.testActivationSpeccalledOnce();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testEJBExceptionNotSupported() throws java.lang.Exception {
            super.testEJBExceptionNotSupported();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testEJBExceptionRequired() throws java.lang.Exception {
            super.testEJBExceptionRequired();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testAppExceptionNotSupported() throws java.lang.Exception {
            super.testAppExceptionNotSupported();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testAppExceptionRequired() throws java.lang.Exception {
            super.testAppExceptionRequired();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testSICMsgPrincipal() throws java.lang.Exception {
            super.testSICMsgPrincipal();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testIBAnnoMsgTransactedUsingXid() throws java.lang.Exception {
            super.testIBAnnoMsgTransactedUsingXid();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testActivationSpecImplRAA() throws java.lang.Exception {
            super.testActivationSpecImplRAA();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testIBAnnoASConfigProp() throws java.lang.Exception {
            super.testIBAnnoASConfigProp();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("servlet")
        public void testContextSetupCompleted() throws java.lang.Exception {
            super.testContextSetupCompleted();
        }


}