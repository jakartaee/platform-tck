package com.sun.ts.tests.connector.annotations.mdcomplete;

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
public class ClientEjbTest extends com.sun.ts.tests.connector.annotations.mdcomplete.Client {
    static final String VEHICLE_ARCHIVE = "mdcomplete_ejb_vehicle";

        /**
        EE10 Deployment Descriptors:
        mdcomplete_ejb_vehicle: META-INF/jboss-deployment-structure.xml
        mdcomplete_ejb_vehicle_client: META-INF/application-client.xml,jar.sun-application-client.xml
        mdcomplete_ejb_vehicle_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        mdcomplete_jsp_vehicle: META-INF/jboss-deployment-structure.xml
        mdcomplete_jsp_vehicle_web: WEB-INF/web.xml,war.sun-web.xml
        mdcomplete_servlet_vehicle: META-INF/jboss-deployment-structure.xml
        mdcomplete_servlet_vehicle_web: WEB-INF/web.xml,war.sun-web.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/connector/annotations/mdcomplete/mdcomplete_ejb_vehicle_client.xml
        /com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml
        Ejb:

        /com/sun/ts/tests/connector/annotations/mdcomplete/mdcomplete_ejb_vehicle_ejb.xml
        /com/sun/ts/tests/connector/annotations/mdcomplete/ejb_vehicle_ejb.xml
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
            JavaArchive mdcomplete_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "mdcomplete_ejb_vehicle_client.jar");
            // The class files
            mdcomplete_ejb_vehicle_client.addClasses(
            com.sun.ts.tests.connector.annotations.mdcomplete.Client.class,
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
            URL resURL = Client.class.getResource("mdcomplete_ejb_vehicle_client.xml");
            if(resURL != null) {
              mdcomplete_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
            }
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            resURL = Client.class.getResource("mdcomplete_ejb_vehicle_client.jar.sun-application-client.xml");
            if(resURL != null) {
              mdcomplete_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
            }
            mdcomplete_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(mdcomplete_ejb_vehicle_client, Client.class, resURL);

        // Ejb 1
            // the jar with the correct archive name
            JavaArchive mdcomplete_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "mdcomplete_ejb_vehicle_ejb.jar");
            // The class files
            mdcomplete_ejb_vehicle_ejb.addClasses(
                com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
                com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
                Fault.class,
                com.sun.ts.tests.connector.annotations.mdcomplete.Client.class,
                com.sun.ts.tests.connector.util.DBSupport.class,
                EETest.class,
                ServiceEETest.class,
                SetupException.class,
                com.sun.ts.tests.common.vehicle.VehicleClient.class,
                com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL1 = Client.class.getResource("mdcomplete_ejb_vehicle_ejb.xml");
            if(ejbResURL1 != null) {
              mdcomplete_ejb_vehicle_ejb.addAsManifestResource(ejbResURL1, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL1 = Client.class.getResource("mdcomplete_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL1 != null) {
              mdcomplete_ejb_vehicle_ejb.addAsManifestResource(ejbResURL1, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(mdcomplete_ejb_vehicle_ejb, Client.class, ejbResURL1);


        // Ear
            EnterpriseArchive mdcomplete_ejb_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "mdcomplete_ejb_vehicle.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            mdcomplete_ejb_vehicle_ear.addAsModule(mdcomplete_ejb_vehicle_ejb);
            mdcomplete_ejb_vehicle_ear.addAsModule(mdcomplete_ejb_vehicle_client);


            // The application.xml descriptor
            URL earResURL = null;
            // Call the archive processor
            archiveProcessor.processEarArchive(mdcomplete_ejb_vehicle_ear, Client.class, earResURL);
        return mdcomplete_ejb_vehicle_ear;
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testMDCompleteConfigProp() throws java.lang.Exception {
            super.testMDCompleteConfigProp();
        }

        @Test
        @Override
        @TargetVehicle("ejb")
        public void testMDCompleteMCFAnno() throws java.lang.Exception {
            super.testMDCompleteMCFAnno();
        }
}