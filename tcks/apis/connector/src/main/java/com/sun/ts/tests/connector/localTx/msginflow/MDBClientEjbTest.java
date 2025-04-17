package com.sun.ts.tests.connector.localTx.msginflow;

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
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class MDBClientEjbTest extends com.sun.ts.tests.connector.localTx.msginflow.MDBClient {
        @Deployment(name = "msginflow_mdb", order = 1, testable = false)
        public static EnterpriseArchive createCommonDeployment() {
            JavaArchive msginflow_mdb_msginflow_ejb = ShrinkWrap.create(JavaArchive.class, "msginflow_mdb_msginflow.jar");
            // The class files
            msginflow_mdb_msginflow_ejb.addClasses(
                    com.sun.ts.tests.connector.mdb.MessageBean.class,
                    com.sun.ts.tests.connector.util.DBSupport.class
            );
            URL ejbResURL = MDBClient.class.getResource("/com/sun/ts/tests/connector/mdb/msginflow_mdb_msginflow_ejb.xml");
            if (ejbResURL != null) {
                msginflow_mdb_msginflow_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            ejbResURL = MDBClient.class.getResource("/com/sun/ts/tests/connector/mdb/msginflow_mdb_msginflow_ejb.jar.sun-ejb-jar.xml");
            if (ejbResURL != null) {
                msginflow_mdb_msginflow_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }

            JavaArchive msginflow1_mdb_msginflow_ejb = ShrinkWrap.create(JavaArchive.class, "msginflow1_mdb_msginflow.jar");
            // The class files
            msginflow1_mdb_msginflow_ejb.addClasses(
                    com.sun.ts.tests.connector.mdb.MessageBeanOne.class,
                    com.sun.ts.tests.connector.util.DBSupport.class
            );
            ejbResURL = MDBClient.class.getResource("/com/sun/ts/tests/connector/mdb/msginflow1_mdb_msginflow_ejb.xml");
            if (ejbResURL != null) {
                msginflow1_mdb_msginflow_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            ejbResURL = MDBClient.class.getResource("/com/sun/ts/tests/connector/mdb/msginflow1_mdb_msginflow_ejb.jar.sun-ejb-jar.xml");
            if (ejbResURL != null) {
                msginflow1_mdb_msginflow_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }

            EnterpriseArchive msginflow_mdb_ear = ShrinkWrap.create(EnterpriseArchive.class, "msginflow_mdb.ear");
            msginflow_mdb_ear.addAsModule(msginflow_mdb_msginflow_ejb);
            msginflow_mdb_ear.addAsModule(msginflow1_mdb_msginflow_ejb);


            return msginflow_mdb_ear;
        }

        @Deployment(name = "msginflow_mdb_jca", order = 1, testable = false)
        public static EnterpriseArchive createCommonDeployment1() {
            JavaArchive msginflow_mdb_jca_msginflow_ejb = ShrinkWrap.create(JavaArchive.class, "msginflow_mdb_jca_msginflow.jar");
            // The class files
            msginflow_mdb_jca_msginflow_ejb.addClasses(
                    com.sun.ts.tests.connector.mdb.JCAMessageBean.class,
                    com.sun.ts.tests.connector.util.DBSupport.class
            );
            URL ejbResURL = MDBClient.class.getResource("/com/sun/ts/tests/connector/mdb/msginflow_mdb_jca_msginflow_ejb.xml");
            if (ejbResURL != null) {
                msginflow_mdb_jca_msginflow_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            ejbResURL = MDBClient.class.getResource("/com/sun/ts/tests/connector/mdb/msginflow_mdb_jca_msginflow_ejb.jar.sun-ejb-jar.xml");
            if (ejbResURL != null) {
                msginflow_mdb_jca_msginflow_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }

            EnterpriseArchive msginflow_mdb_jca_ear = ShrinkWrap.create(EnterpriseArchive.class, "msginflow_mdb_jca.ear");
            msginflow_mdb_jca_ear.addAsModule(msginflow_mdb_jca_msginflow_ejb);

            return msginflow_mdb_jca_ear;
        }

    static final String VEHICLE_ARCHIVE = "msginflow_mdb_ejb_vehicle";

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
        Client:

        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/connector/localTx/msginflow/ejb_vehicle_ejb.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.jar.sun-ejb-jar.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_ejb.xml
        Rar:

        /com/sun/ts/tests/common/connector/whitebox/mdcomplete/ra-md-complete.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive msginflow_mdb_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "msginflow_mdb_ejb_vehicle_client.jar");
            // The class files
            msginflow_mdb_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
            com.sun.ts.tests.connector.util.DBSupport.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.ServiceEETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = MDBClient.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
            if(resURL != null) {
              msginflow_mdb_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = MDBClient.class.getResource("//com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              msginflow_mdb_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            msginflow_mdb_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(msginflow_mdb_ejb_vehicle_client, MDBClient.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive msginflow_mdb_msginflow_ejb = ShrinkWrap.create(JavaArchive.class, "msginflow_mdb_msginflow_ejb.jar");
            // The class files
            msginflow_mdb_msginflow_ejb.addClasses(
                com.sun.ts.tests.connector.mdb.MessageBean.class,
                com.sun.ts.tests.connector.util.DBSupport.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = MDBClient.class.getResource("/com/sun/ts/tests/connector/mdb/msginflow_mdb_msginflow_ejb.xml");
            if(ejbResURL1 != null) {
              msginflow_mdb_msginflow_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = MDBClient.class.getResource("/com/sun/ts/tests/connector/mdb/msginflow_mdb_msginflow_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              msginflow_mdb_msginflow_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(msginflow_mdb_msginflow_ejb, MDBClient.class, ejbResURL1);
        // Ejb 2
            // the jar with the correct archive name
            JavaArchive msginflow1_mdb_msginflow_ejb = ShrinkWrap.create(JavaArchive.class, "msginflow1_mdb_msginflow_ejb.jar");
            // The class files
            msginflow1_mdb_msginflow_ejb.addClasses(
                com.sun.ts.tests.connector.mdb.MessageBeanOne.class,
                com.sun.ts.tests.connector.util.DBSupport.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL2 = MDBClient.class.getResource("/com/sun/ts/tests/connector/mdb/msginflow1_mdb_msginflow_ejb.xml");
            if(ejbResURL2 != null) {
              msginflow1_mdb_msginflow_ejb.addAsManifestResource(ejbResURL2, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL2 = MDBClient.class.getResource("/com/sun/ts/tests/connector/mdb/msginflow1_mdb_msginflow_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL2 != null) {
              msginflow1_mdb_msginflow_ejb.addAsManifestResource(ejbResURL2, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(msginflow1_mdb_msginflow_ejb, MDBClient.class, ejbResURL2);
        // Ejb 3
            // the jar with the correct archive name
            JavaArchive msginflow_mdb_jca_msginflow_ejb = ShrinkWrap.create(JavaArchive.class, "msginflow_mdb_jca_msginflow_ejb.jar");
            // The class files
            msginflow_mdb_jca_msginflow_ejb.addClasses(
                com.sun.ts.tests.connector.util.DBSupport.class,
                com.sun.ts.tests.connector.mdb.JCAMessageBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL3 = MDBClient.class.getResource("/com/sun/ts/tests/connector/mdb/msginflow_mdb_jca_msginflow_ejb.xml");
            if(ejbResURL3 != null) {
              msginflow_mdb_jca_msginflow_ejb.addAsManifestResource(ejbResURL3, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL3 = MDBClient.class.getResource("/com/sun/ts/tests/connector/mdb/msginflow_mdb_jca_msginflow_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL3 != null) {
              msginflow_mdb_jca_msginflow_ejb.addAsManifestResource(ejbResURL3, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(msginflow_mdb_jca_msginflow_ejb, MDBClient.class, ejbResURL3);
        // Ejb 4
            // the jar with the correct archive name
            JavaArchive msginflow_mdb_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "msginflow_mdb_ejb_vehicle_ejb.jar");
            // The class files
            msginflow_mdb_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.connector.util.DBSupport.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.tests.connector.localTx.msginflow.MDBClient.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL4 = MDBClient.class.getResource("/ejb_vehicle_ejb.xml");
            if(ejbResURL4 != null) {
              msginflow_mdb_ejb_vehicle_ejb.addAsManifestResource(ejbResURL4, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL4 = MDBClient.class.getResource("/ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL4 != null) {
              msginflow_mdb_ejb_vehicle_ejb.addAsManifestResource(ejbResURL4, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(msginflow_mdb_ejb_vehicle_ejb, MDBClient.class, ejbResURL4);


        // Ear
            EnterpriseArchive msginflow_mdb_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "msginflow_mdb_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            msginflow_mdb_ejb_vehicle_ear.addAsModule(msginflow_mdb_msginflow_ejb);
            msginflow_mdb_ejb_vehicle_ear.addAsModule(msginflow1_mdb_msginflow_ejb);
            msginflow_mdb_ejb_vehicle_ear.addAsModule(msginflow_mdb_jca_msginflow_ejb);
            msginflow_mdb_ejb_vehicle_ear.addAsModule(msginflow_mdb_ejb_vehicle_ejb);
            msginflow_mdb_ejb_vehicle_ear.addAsModule(msginflow_mdb_ejb_vehicle_client);


            // The application.xml descriptor
            URL earResURL = null;
            // The sun-application.xml descriptor
            earResURL = MDBClient.class.getResource("/.ear.sun-application.xml");
            if(earResURL != null) {
              msginflow_mdb_ejb_vehicle_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(msginflow_mdb_ejb_vehicle_ear, MDBClient.class, earResURL);
        return msginflow_mdb_ejb_vehicle_ear;
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testReceiveMessage() throws java.lang.Exception {
            super.testReceiveMessage();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testProxyInterfaceImp() throws java.lang.Exception {
            super.testProxyInterfaceImp();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testUniqueMessageEndpoint() throws java.lang.Exception {
            super.testUniqueMessageEndpoint();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testMessageEndpointFactoryForEquals() throws java.lang.Exception {
            super.testMessageEndpointFactoryForEquals();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testUniqueMessageEndpointFactory() throws java.lang.Exception {
            super.testUniqueMessageEndpointFactory();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testEndpointActivationName() throws java.lang.Exception {
            super.testEndpointActivationName();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testGetEndpoinClass() throws java.lang.Exception {
            super.testGetEndpoinClass();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testMessageDeliveryTransacted() throws java.lang.Exception {
            super.testMessageDeliveryTransacted();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testMessageDeliveryNonTransacted() throws java.lang.Exception {
            super.testMessageDeliveryNonTransacted();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testMessageDeliveryTransactedUsingXid() throws java.lang.Exception {
            super.testMessageDeliveryTransactedUsingXid();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testActivationSpeccalledOnce() throws java.lang.Exception {
            super.testActivationSpeccalledOnce();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testEJBExceptionNotSupported() throws java.lang.Exception {
            super.testEJBExceptionNotSupported();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testEJBExceptionRequired() throws java.lang.Exception {
            super.testEJBExceptionRequired();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testAppExceptionNotSupported() throws java.lang.Exception {
            super.testAppExceptionNotSupported();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testAppExceptionRequired() throws java.lang.Exception {
            super.testAppExceptionRequired();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testSICMsgPrincipal() throws java.lang.Exception {
            super.testSICMsgPrincipal();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testIBAnnoMsgTransactedUsingXid() throws java.lang.Exception {
            super.testIBAnnoMsgTransactedUsingXid();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testActivationSpecImplRAA() throws java.lang.Exception {
            super.testActivationSpecImplRAA();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testIBAnnoASConfigProp() throws java.lang.Exception {
            super.testIBAnnoASConfigProp();
        }

        @Test
        @Override
        @OperateOnDeployment(VEHICLE_ARCHIVE)
        @TargetVehicle("ejb")
        public void testContextSetupCompleted() throws java.lang.Exception {
            super.testContextSetupCompleted();
        }


}