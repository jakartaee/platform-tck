package com.sun.ts.tests.ejb30.misc.datasource.twojars;

import com.sun.ts.tests.ejb30.misc.datasource.twojars.Client;
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
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;



@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("ejb_3x_remote_optional")
@Tag("web_optional")
@Tag("tck-appclient")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.misc.datasource.twojars.Client {

    @Deployment(name = "ejb3_2standalone_component_ejb", order = 1, testable = false)
    public static JavaArchive createCommonDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        JavaArchive ejb3_2standalone_component_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_2standalone_component_ejb.jar");
        // The class files
        ejb3_2standalone_component_ejb.addClasses(
                com.sun.ts.tests.ejb30.assembly.appres.common.AppResBeanBase.class,
                com.sun.ts.tests.ejb30.assembly.appres.common.AppResCommonIF.class,
                com.sun.ts.tests.ejb30.assembly.appres.common.AppResLocalIF.class,
                com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF.class,
                com.sun.ts.tests.ejb30.common.helper.Helper.class,
                com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceTest.class,
                com.sun.ts.tests.ejb30.misc.datasource.twojars.DataSource2Bean.class

        );

        URL ejbResURL = Client.class.getResource("two_standalone_component_ejb.xml");
        ejb3_2standalone_component_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
        ejbResURL = Client.class.getResource("two_standalone_component_ejb.jar.sun-ejb-jar.xml");
        ejb3_2standalone_component_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
        archiveProcessor.processEjbArchive(ejb3_2standalone_component_ejb, Client.class, ejbResURL);

        return ejb3_2standalone_component_ejb;
    }
    /**
        EE10 Deployment Descriptors:
        ejb3_misc_datasource_twojars:
        ejb3_misc_datasource_twojars_client: META-INF/application-client.xml
        ejb3_misc_datasource_twojars_ejb: jar.sun-ejb-jar.xml

        Found Descriptors:
        Client:

        /com/sun/ts/tests/ejb30/misc/datasource/twojars/ejb3_misc_datasource_twojars_client.xml
        Ejb:

        /com/sun/ts/tests/ejb30/misc/datasource/twojars/ejb3_misc_datasource_twojars_ejb.jar.sun-ejb-jar.xml
        Ear:

        */
        @TargetsContainer("tck-appclient")
        @OverProtocol("appclient")
        @Deployment(name = "ejb3_misc_datasource_twojars", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // Client
            // the jar with the correct archive name
            JavaArchive ejb3_misc_datasource_twojars_client = ShrinkWrap.create(JavaArchive.class, "ejb3_misc_datasource_twojars_client.jar");
            // The class files
            ejb3_misc_datasource_twojars_client.addClasses(
            com.sun.ts.lib.harness.EETest.Fault.class,
            com.sun.ts.lib.harness.EETest.class,
            com.sun.ts.lib.harness.EETest.SetupException.class,
            com.sun.ts.tests.ejb30.misc.datasource.twojars.Client.class
            );
            // The application-client.xml descriptor
            URL resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/datasource/twojars/ejb3_misc_datasource_twojars_client.xml");
            ejb3_misc_datasource_twojars_client.addAsManifestResource(resURL, "application-client.xml");
            // The sun-application-client.xml file need to be added or should this be in in the vendor Arquillian extension?
            //resURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/datasource/twojars/ejb3_misc_datasource_twojars_client.jar.sun-application-client.xml");
            //ejb3_misc_datasource_twojars_client.addAsManifestResource(resURL, "sun-application-client.xml");
            ejb3_misc_datasource_twojars_client.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
            // Call the archive processor
            archiveProcessor.processClientArchive(ejb3_misc_datasource_twojars_client, Client.class, resURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive ejb3_misc_datasource_twojars_ejb = ShrinkWrap.create(JavaArchive.class, "ejb3_misc_datasource_twojars_ejb.jar");
            // The class files
            ejb3_misc_datasource_twojars_ejb.addClasses(
                com.sun.ts.tests.ejb30.misc.datasource.twojars.DataSourceBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/datasource/twojars/ejb3_misc_datasource_twojars_ejb.xml");
            if(ejbResURL != null) {
              ejb3_misc_datasource_twojars_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/datasource/twojars/ejb3_misc_datasource_twojars_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              ejb3_misc_datasource_twojars_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(ejb3_misc_datasource_twojars_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive ejb3_misc_datasource_twojars_ear = ShrinkWrap.create(EnterpriseArchive.class, "ejb3_misc_datasource_twojars.ear");

            // Any libraries added to the ear
                URL libURL;
                JavaArchive shared_lib = ShrinkWrap.create(JavaArchive.class, "shared.jar");
                    // The class files
                    shared_lib.addClasses(
                        com.sun.ts.tests.ejb30.assembly.appres.common.AppResLocalIF.class,
                        com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common.DataSourceTest.class,
                        com.sun.ts.tests.ejb30.assembly.appres.common.AppResBeanBase.class,
                        com.sun.ts.tests.ejb30.common.helper.Helper.class,
                        com.sun.ts.tests.ejb30.assembly.appres.common.AppResCommonIF.class,
                        com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF.class,
                        com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class
                    );


                ejb3_misc_datasource_twojars_ear.addAsLibrary(shared_lib);


            // The component jars built by the package target
            ejb3_misc_datasource_twojars_ear.addAsModule(ejb3_misc_datasource_twojars_ejb);
            ejb3_misc_datasource_twojars_ear.addAsModule(ejb3_misc_datasource_twojars_client);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/datasource/twojars/application.xml");
            if(earResURL != null) {
              ejb3_misc_datasource_twojars_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/datasource/twojars/application.ear.sun-application.xml");
            if(earResURL != null) {
              ejb3_misc_datasource_twojars_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(ejb3_misc_datasource_twojars_ear, Client.class, earResURL);
        return ejb3_misc_datasource_twojars_ear;
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_misc_datasource_twojars")
        public void clientPostConstruct() {
            super.clientPostConstruct();
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_misc_datasource_twojars")
        public void ejbPostConstruct() {
            super.ejbPostConstruct();
        }

        @Test
        @Override
        @OperateOnDeployment("ejb3_misc_datasource_twojars")
        public void ejb2PostConstruct() {
            super.ejb2PostConstruct();
        }


}