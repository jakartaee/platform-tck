package com.sun.ts.tests.connector.annotations.mdcomplete;

import java.net.URL;
import java.util.Properties;

import com.sun.ts.lib.harness.RemoteStatus;
import com.sun.ts.tests.connector.annotations.mdcomplete.proxy.ClientServletTarget;
import com.sun.ts.tests.connector.annotations.mdcomplete.proxy.IClient;
import com.sun.ts.tests.connector.annotations.mdcomplete.proxy.IClientProxy;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.javatest.JavaTestProtocolConfiguration;


@ExtendWith(ArquillianExtension.class)
@Tag("connector")
@Tag("platform")
@Tag("connector_standalone")
@Tag("connector_web")
@Tag("web_optional")
@Tag("tck-javatest")

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
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = VEHICLE_ARCHIVE, order = 2)
        public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

            // non-vehicle appclientproxy invoker war
            WebArchive appclientproxy = ShrinkWrap.create(WebArchive.class, "appclientproxy.war");
            appclientproxy.addClasses(Client.class,
                    com.sun.ts.lib.harness.EETest.Fault.class,
                    com.sun.ts.tests.connector.util.DBSupport.class,
                    com.sun.ts.lib.harness.EETest.class,
                    com.sun.ts.lib.harness.ServiceEETest.class,
                    com.sun.ts.lib.harness.EETest.SetupException.class,
                    com.sun.ts.tests.connector.annotations.mdcomplete.proxy.ClientServletTarget.class,
                    com.sun.ts.tests.common.vehicle.none.proxy.ServletNoVehicle.class
            );
            appclientproxy.addAsWebInfResource(new StringAsset(""), "beans.xml");
            URL webResURL = Client.class.getResource("proxy_servlet_web.xml");
            appclientproxy.addAsWebInfResource(webResURL, "web.xml");
            archiveProcessor.processWebArchive(appclientproxy, Client.class, null);

        return appclientproxy;
        }

        static IClient client;

        @BeforeAll
        public static void setUp() throws Exception {
            IClientProxy clientProxy = new IClientProxy();
            Properties testProps = JavaTestProtocolConfiguration.getTsJteProps();
            String webServerHost = testProps.getProperty("webServerHost", "localhost");
            String webServerPort = testProps.getProperty("webServerPort", "8080");
            client = clientProxy.newProxy(webServerHost, webServerPort);
            RemoteStatus status = client.setup(new String[]{VEHICLE_ARCHIVE}, testProps);
            validateStatus(status, "setup");
        }
        @Test
        @Override
        @RunAsClient
        public void testMDCompleteConfigProp() throws java.lang.Exception {
            RemoteStatus status = client.testMDCompleteConfigProp();
            System.out.println(status);
            validateStatus(status, "testMDCompleteConfigProp");
        }

        @Test
        @Override
        @RunAsClient
        public void testMDCompleteMCFAnno() throws java.lang.Exception {
            RemoteStatus status = client.testMDCompleteMCFAnno();
            System.out.println(status);
            validateStatus(status, "testMDCompleteMCFAnno");
        }

        static void validateStatus(RemoteStatus status, String test) throws Exception{
            if (status.toStatus().isFailed()) {
                if(status.hasError()) {
                    Exception ex = new Exception(test+" failed: " + status.getErrorMessage());
                    if (status.getErrorTrace() != null) {
                        ex.setStackTrace(status.getErrorTrace());
                    }
                    throw ex;
                }
                Assertions.fail(test+" failed: " + status.toString());
            }

        }
}