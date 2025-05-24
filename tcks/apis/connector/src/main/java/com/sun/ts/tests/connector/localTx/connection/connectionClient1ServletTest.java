package com.sun.ts.tests.connector.localTx.connection;

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
@Tag("connector")
@Tag("platform")
@Tag("connector_standalone")
@Tag("connector_web")
@Tag("web_optional")
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class connectionClient1ServletTest extends com.sun.ts.tests.connector.localTx.connection.connectionClient1 {
    static final String VEHICLE_ARCHIVE = "localTx_conn_servlet_vehicle";

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
        War:

        /com/sun/ts/tests/connector/localTx/connection/servlet_vehicle_web.xml
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
            WebArchive localTx_conn_servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "localTx_conn_servlet_vehicle_web.war");
            // The class files
            localTx_conn_servlet_vehicle_web.addClasses(
            com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
            com.sun.ts.tests.connector.localTx.connection.connectionClient1.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            Fault.class,
            com.sun.ts.tests.connector.util.DBSupport.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The web.xml descriptor
            URL warResURL = connectionClient1.class.getResource("localTx_conn_servlet_vehicle_servlet.xml");
            if(warResURL != null) {
              localTx_conn_servlet_vehicle_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = connectionClient1.class.getResource("localTx_conn_servlet_vehicle_web.war.sun-web.xml");
            if(warResURL != null) {
              localTx_conn_servlet_vehicle_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

            // Any libraries added to the war

           // Call the archive processor
           archiveProcessor.processWebArchive(localTx_conn_servlet_vehicle_web, connectionClient1.class, warResURL);


        // Ear
            EnterpriseArchive localTx_conn_servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "localTx_conn_servlet_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            localTx_conn_servlet_vehicle_ear.addAsModule(localTx_conn_servlet_vehicle_web);

            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(localTx_conn_servlet_vehicle_ear, connectionClient1.class, earResURL);
        return localTx_conn_servlet_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testGetConnection1() throws java.lang.Exception {
            super.testGetConnection1();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testgetConnectionWithParameter1() throws java.lang.Exception {
            super.testgetConnectionWithParameter1();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPIResourceException() throws java.lang.Exception {
            super.testAPIResourceException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPINotSupportedException() throws java.lang.Exception {
            super.testAPINotSupportedException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPILocalTransactionException() throws java.lang.Exception {
            super.testAPILocalTransactionException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPIResourceAllocationException() throws java.lang.Exception {
            super.testAPIResourceAllocationException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPIResourceAdapterInternalException() throws java.lang.Exception {
            super.testAPIResourceAdapterInternalException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPISecurityException() throws java.lang.Exception {
            super.testAPISecurityException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPISharingViolationException() throws java.lang.Exception {
            super.testAPISharingViolationException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPIUnavailableException() throws java.lang.Exception {
            super.testAPIUnavailableException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPIWorkException() throws java.lang.Exception {
            super.testAPIWorkException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPIWorkCompletedException() throws java.lang.Exception {
            super.testAPIWorkCompletedException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPIWorkRejectedException() throws java.lang.Exception {
            super.testAPIWorkRejectedException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPIEISSystemException() throws java.lang.Exception {
            super.testAPIEISSystemException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPIApplicationServerInternalException() throws java.lang.Exception {
            super.testAPIApplicationServerInternalException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPICommException() throws java.lang.Exception {
            super.testAPICommException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPIRetryableWorkRejectedException() throws java.lang.Exception {
            super.testAPIRetryableWorkRejectedException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPIIllegalStateException() throws java.lang.Exception {
            super.testAPIIllegalStateException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPIRetryableUnavailableException() throws java.lang.Exception {
            super.testAPIRetryableUnavailableException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPIManagedConnectionMetaData() throws java.lang.Exception {
            super.testAPIManagedConnectionMetaData();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPIManagedConnection() throws java.lang.Exception {
            super.testAPIManagedConnection();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPIInvalidPropertyException() throws java.lang.Exception {
            super.testAPIInvalidPropertyException();
        }

        @Test
        @Override
        @TargetVehicle("servlet")
        public void testAPIHintsContext() throws java.lang.Exception {
            super.testAPIHintsContext();
        }


}