package com.sun.ts.tests.connector.localTx.workcontext;

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
@Tag("connector")
@Tag("platform")
@Tag("connector_standalone")
@Tag("connector_web")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class WorkContextClientEjbTest extends com.sun.ts.tests.connector.localTx.workcontext.WorkContextClient {
    static final String VEHICLE_ARCHIVE = "workcontext_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        workcontext_ejb_vehicle: 
        workcontext_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        workcontext_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        workcontext_jsp_vehicle: 
        workcontext_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        workcontext_servlet_vehicle: 
        workcontext_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/connector/localTx/workcontext/workcontext_ejb_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/connector/localTx/workcontext/workcontext_ejb_vehicle_ejb.xml
        /com/sun/ts/tests/connector/localTx/workcontext/ejb_vehicle_ejb.xml
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
            JavaArchive workcontext_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "workcontext_ejb_vehicle_client.jar");
            // The class files
            workcontext_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.connector.localTx.workcontext.WorkContextClient.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
            com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
            Fault.class,
            com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
            com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
            com.sun.ts.tests.connector.util.DBSupport.class,
            EETest.class,
            ServiceEETest.class,
            SetupException.class,
            com.sun.ts.tests.common.vehicle.VehicleClient.class
            );
            // The application-client.xml descriptor
            URL resURL = WorkContextClient.class.getResource("workcontext_ejb_vehicle_client.xml");
            workcontext_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = WorkContextClient.class.getResource("workcontext_ejb_vehicle_client.jar.sun-application-client.xml");
            workcontext_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            workcontext_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(workcontext_ejb_vehicle_client, WorkContextClient.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive workcontext_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "workcontext_ejb_vehicle_ejb.jar");
            // The class files
            workcontext_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.connector.localTx.workcontext.WorkContextClient.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                Fault.class,
                com.sun.ts.tests.connector.util.DBSupport.class,
                EETest.class,
                ServiceEETest.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = WorkContextClient.class.getResource("workcontext_ejb_vehicle_ejb.xml");
            workcontext_ejb_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            // The sun-ejb-jar.xml file
            ejbResURL1 = WorkContextClient.class.getResource("workcontext_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            workcontext_ejb_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            // Call the archive processor
            archiveProcessor.processEjbArchive(workcontext_ejb_vehicle_ejb, WorkContextClient.class, ejbResURL1);


        // Ear
            EnterpriseArchive workcontext_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "workcontext_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            workcontext_ejb_vehicle_ear.addAsModule(workcontext_ejb_vehicle_ejb);
            workcontext_ejb_vehicle_ear.addAsModule(workcontext_ejb_vehicle_client);


            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(workcontext_ejb_vehicle_ear, WorkContextClient.class, earResURL);
        return workcontext_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testIsContextSupported() throws java.lang.Exception {
            super.testIsContextSupported();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testSecurityContextExecSubject() throws java.lang.Exception {
            super.testSecurityContextExecSubject();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testSecurityContextServiceSubject() throws java.lang.Exception {
            super.testSecurityContextServiceSubject();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testSecurityContextCBH() throws java.lang.Exception {
            super.testSecurityContextCBH();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testWorkContextErrorCode() throws java.lang.Exception {
            super.testWorkContextErrorCode();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testWorkContextErrorCode2() throws java.lang.Exception {
            super.testWorkContextErrorCode2();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testNestedWorkContexts() throws java.lang.Exception {
            super.testNestedWorkContexts();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testNestedWorkContexts2() throws java.lang.Exception {
            super.testNestedWorkContexts2();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testNestedWorkContexts3() throws java.lang.Exception {
            super.testNestedWorkContexts3();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testWorkContextProvider() throws java.lang.Exception {
            super.testWorkContextProvider();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testWorkContextNotifications() throws java.lang.Exception {
            super.testWorkContextNotifications();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testHICNotifications() throws java.lang.Exception {
            super.testHICNotifications();
        }


}