package com.sun.ts.tests.connector.localTx.connection;

import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
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
@Tag("connector")
@Tag("platform")
@Tag("connector_standalone")
@Tag("connector_web")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class connectionClient1EjbTest extends com.sun.ts.tests.connector.localTx.connection.connectionClient1 {
    static final String VEHICLE_ARCHIVE = "localTx_conn_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        localTx_conn_ejb_vehicle: 
        localTx_conn_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        localTx_conn_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        localTx_conn_jsp_vehicle: 
        localTx_conn_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        localTx_conn_servlet_vehicle: 
        localTx_conn_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/connector/localTx/connection/localTx_conn_ejb_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/connector/localTx/connection/localTx_conn_ejb_vehicle_ejb.xml
        /com/sun/ts/tests/connector/localTx/connection/ejb_vehicle_ejb.xml
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
            JavaArchive localTx_conn_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "localTx_conn_ejb_vehicle_client.jar");
            // The class files
            localTx_conn_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.connector.localTx.connection.connectionClient1.class,
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
            URL resURL = connectionClient1.class.getResource("localTx_conn_ejb_vehicle_client.xml");
            localTx_conn_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = connectionClient1.class.getResource("localTx_conn_ejb_vehicle_client.jar.sun-application-client.xml");
            localTx_conn_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            localTx_conn_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(localTx_conn_ejb_vehicle_client, connectionClient1.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive localTx_conn_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "localTx_conn_ejb_vehicle_ejb.jar");
            // The class files
            localTx_conn_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.connector.localTx.connection.connectionClient1.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                com.sun.ts.lib.harness.EETest.Fault.class,
                com.sun.ts.tests.connector.util.DBSupport.class,
                com.sun.ts.lib.harness.EETest.class,
                com.sun.ts.lib.harness.ServiceEETest.class,
                com.sun.ts.lib.harness.EETest.SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = connectionClient1.class.getResource("localTx_conn_ejb_vehicle_ejb.xml");
            localTx_conn_ejb_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            // The sun-ejb-jar.xml file
            ejbResURL1 = connectionClient1.class.getResource("localTx_conn_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            localTx_conn_ejb_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            // Call the archive processor
            archiveProcessor.processEjbArchive(localTx_conn_ejb_vehicle_ejb, connectionClient1.class, ejbResURL1);


        // Ear
            EnterpriseArchive localTx_conn_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "localTx_conn_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            localTx_conn_ejb_vehicle_ear.addAsModule(localTx_conn_ejb_vehicle_ejb);
            localTx_conn_ejb_vehicle_ear.addAsModule(localTx_conn_ejb_vehicle_client);


            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(localTx_conn_ejb_vehicle_ear, connectionClient1.class, earResURL);
        return localTx_conn_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testGetConnection1() throws java.lang.Exception {
            super.testGetConnection1();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testgetConnectionWithParameter1() throws java.lang.Exception {
            super.testgetConnectionWithParameter1();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPIResourceException() throws java.lang.Exception {
            super.testAPIResourceException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPINotSupportedException() throws java.lang.Exception {
            super.testAPINotSupportedException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPILocalTransactionException() throws java.lang.Exception {
            super.testAPILocalTransactionException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPIResourceAllocationException() throws java.lang.Exception {
            super.testAPIResourceAllocationException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPIResourceAdapterInternalException() throws java.lang.Exception {
            super.testAPIResourceAdapterInternalException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPISecurityException() throws java.lang.Exception {
            super.testAPISecurityException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPISharingViolationException() throws java.lang.Exception {
            super.testAPISharingViolationException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPIUnavailableException() throws java.lang.Exception {
            super.testAPIUnavailableException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPIWorkException() throws java.lang.Exception {
            super.testAPIWorkException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPIWorkCompletedException() throws java.lang.Exception {
            super.testAPIWorkCompletedException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPIWorkRejectedException() throws java.lang.Exception {
            super.testAPIWorkRejectedException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPIEISSystemException() throws java.lang.Exception {
            super.testAPIEISSystemException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPIApplicationServerInternalException() throws java.lang.Exception {
            super.testAPIApplicationServerInternalException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPICommException() throws java.lang.Exception {
            super.testAPICommException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPIRetryableWorkRejectedException() throws java.lang.Exception {
            super.testAPIRetryableWorkRejectedException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPIIllegalStateException() throws java.lang.Exception {
            super.testAPIIllegalStateException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPIRetryableUnavailableException() throws java.lang.Exception {
            super.testAPIRetryableUnavailableException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPIManagedConnectionMetaData() throws java.lang.Exception {
            super.testAPIManagedConnectionMetaData();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPIManagedConnection() throws java.lang.Exception {
            super.testAPIManagedConnection();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPIInvalidPropertyException() throws java.lang.Exception {
            super.testAPIInvalidPropertyException();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testAPIHintsContext() throws java.lang.Exception {
            super.testAPIHintsContext();
        }


}