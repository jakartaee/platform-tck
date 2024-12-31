package com.sun.ts.tests.ejb30.misc.metadataComplete.warejb;

import com.sun.ts.tests.ejb30.misc.metadataComplete.warejb.Client;
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
@Tag("tck-javatest")

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest extends com.sun.ts.tests.ejb30.misc.metadataComplete.warejb.Client {
    /**
        EE10 Deployment Descriptors:
        misc_metadataComplete_warejb: META-INF/application.xml
        misc_metadataComplete_warejb_ejb: META-INF/ejb-jar.xml,jar.sun-ejb-jar.xml
        misc_metadataComplete_warejb_web: WEB-INF/web.xml

        Found Descriptors:
        Ejb:

        /com/sun/ts/tests/ejb30/misc/metadataComplete/warejb/misc_metadataComplete_warejb_ejb.xml
        /com/sun/ts/tests/ejb30/misc/metadataComplete/warejb/misc_metadataComplete_warejb_ejb.jar.sun-ejb-jar.xml
        War:

        /com/sun/ts/tests/ejb30/misc/metadataComplete/warejb/misc_metadataComplete_warejb_web.xml
        Ear:

        /com/sun/ts/tests/ejb30/misc/metadataComplete/warejb/application.xml
        */
        @TargetsContainer("tck-javatest")
        @OverProtocol("javatest")
        @Deployment(name = "misc_metadataComplete_warejb", order = 2)
        public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) {
        // War
            // the war with the correct archive name
            WebArchive misc_metadataComplete_warejb_web = ShrinkWrap.create(WebArchive.class, "misc_metadataComplete_warejb_web.war");
            // The class files
            misc_metadataComplete_warejb_web.addClasses(
            com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet.class,
            com.sun.ts.tests.ejb30.common.calc.RemoteCalculator.class,
            com.sun.ts.tests.ejb30.common.calc.CalculatorException.class,
            com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
            com.sun.ts.tests.ejb30.misc.metadataComplete.warejb.TestServlet.class,
            com.sun.ts.tests.servlet.common.util.Data.class
            );
            // The web.xml descriptor
            URL warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/metadataComplete/warejb/misc_metadataComplete_warejb_web.xml");
            if(warResURL != null) {
              misc_metadataComplete_warejb_web.addAsWebInfResource(warResURL, "web.xml");
            }
            // The sun-web.xml descriptor
            warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/metadataComplete/warejb/misc_metadataComplete_warejb_web.war.sun-web.xml");
            if(warResURL != null) {
              misc_metadataComplete_warejb_web.addAsWebInfResource(warResURL, "sun-web.xml");
            }

           // Call the archive processor
           archiveProcessor.processWebArchive(misc_metadataComplete_warejb_web, Client.class, warResURL);

        // Ejb
            // the jar with the correct archive name
            JavaArchive misc_metadataComplete_warejb_ejb = ShrinkWrap.create(JavaArchive.class, "misc_metadataComplete_warejb_ejb.jar");
            // The class files
            misc_metadataComplete_warejb_ejb.addClasses(
                com.sun.ts.tests.ejb30.misc.metadataComplete.appclient2ejbjars.BusinessInterceptorNotUsed.class,
                com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars.InterceptorNotUsed.class,
                com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars.InterceptorUsed.class,
                com.sun.ts.tests.ejb30.common.calc.NoInterfaceRemoteCalculator.class,
                com.sun.ts.tests.ejb30.common.calc.BaseRemoteCalculator.class,
                com.sun.ts.tests.ejb30.common.calc.RemoteCalculator.class,
                com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars.RemoteCalculatorBean0.class,
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars.StatefulRemoteCalculatorBean.class,
                com.sun.ts.tests.ejb30.misc.metadataComplete.appclientejbjars.StatelessRemoteCalculatorBean.class,
                com.sun.ts.tests.ejb30.common.calc.CalculatorException.class,
                com.sun.ts.tests.ejb30.misc.metadataComplete.appclient2ejbjars.StatelessAnnotationUsedRemoteCalculatorBean.class
            );
            // The ejb-jar.xml descriptor
            URL ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/metadataComplete/warejb/misc_metadataComplete_warejb_ejb.xml");
            if(ejbResURL != null) {
              misc_metadataComplete_warejb_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
            }
            // The sun-ejb-jar.xml file
            ejbResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/metadataComplete/warejb/misc_metadataComplete_warejb_ejb.jar.sun-ejb-jar.xml");
            if(ejbResURL != null) {
              misc_metadataComplete_warejb_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
            }
            // Call the archive processor
            archiveProcessor.processEjbArchive(misc_metadataComplete_warejb_ejb, Client.class, ejbResURL);

        // Ear
            EnterpriseArchive misc_metadataComplete_warejb_ear = ShrinkWrap.create(EnterpriseArchive.class, "misc_metadataComplete_warejb.ear");

            // Any libraries added to the ear

            // The component jars built by the package target
            misc_metadataComplete_warejb_ear.addAsModule(misc_metadataComplete_warejb_ejb);
            misc_metadataComplete_warejb_ear.addAsModule(misc_metadataComplete_warejb_web);



            // The application.xml descriptor
            URL earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/metadataComplete/warejb/application.xml");
            if(earResURL != null) {
              misc_metadataComplete_warejb_ear.addAsManifestResource(earResURL, "application.xml");
            }
            // The sun-application.xml descriptor
            earResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/misc/metadataComplete/warejb/application.ear.sun-application.xml");
            if(earResURL != null) {
              misc_metadataComplete_warejb_ear.addAsManifestResource(earResURL, "sun-application.xml");
            }
            // Call the archive processor
            archiveProcessor.processEarArchive(misc_metadataComplete_warejb_ear, Client.class, earResURL);
        return misc_metadataComplete_warejb_ear;
        }

        @Test
        @Override
        public void annotationNotProcessedForStateless() throws java.lang.Exception {
            super.annotationNotProcessedForStateless();
        }

        @Test
        @Override
        public void annotationNotProcessedForStateful() throws java.lang.Exception {
            super.annotationNotProcessedForStateful();
        }

        @Test
        @Override
        public void annotationNotProcessedForWar() throws java.lang.Exception {
            super.annotationNotProcessedForWar();
        }

        @Test
        @Override
        public void typeLevelAnnotationNotProcessedForWar() throws java.lang.Exception {
            super.typeLevelAnnotationNotProcessedForWar();
        }

        @Test
        @Override
        public void excludeDefaultIncludeClassInterceptor() throws java.lang.Exception {
            super.excludeDefaultIncludeClassInterceptor();
        }

        @Test
        @Override
        public void excludeDefaulAndClassReinstateDefault() throws java.lang.Exception {
            super.excludeDefaulAndClassReinstateDefault();
        }

        @Test
        @Override
        public void defaultAndMethodInterceptor() throws java.lang.Exception {
            super.defaultAndMethodInterceptor();
        }

        @Test
        @Override
        public void sameInterceptor3Times() throws java.lang.Exception {
            super.sameInterceptor3Times();
        }

        @Test
        @Override
        public void sameInterceptorTwice() throws java.lang.Exception {
            super.sameInterceptorTwice();
        }


}