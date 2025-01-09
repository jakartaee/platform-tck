package com.sun.ts.tests.ejb30.bb.localaccess.webclient;

import com.sun.ts.tests.ejb30.bb.localaccess.webclient.Client;
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

@Tag("platform")
@Tag("ejb_3x_remote_optional")
@Tag("web_optional")
@Tag("tck-javatest")
@TestMethodOrder(MethodOrderer.MethodName.class)
@ExtendWith(ArquillianExtension.class)
public class ClientTest extends com.sun.ts.tests.ejb30.bb.localaccess.webclient.Client {
    
    /**
     * EE10 Deployment Descriptors: localaccess_webclient_web: WEB-INF/web.xml
     * 
     * Found Descriptors: War:
     * 
     * /com/sun/ts/tests/ejb30/bb/localaccess/webclient/localaccess_webclient_web.xml
     */
    @TargetsContainer("tck-javatest")
    @OverProtocol("javatest")
    @Deployment(name = "localaccess_webclient", order = 2)
    public static WebArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

        // War
        
        // The war with the correct archive name
        WebArchive localaccess_webclient_web = ShrinkWrap.create(WebArchive.class, "localaccess_webclient_web.war");
        
        // The class files
        localaccess_webclient_web.addClasses(com.sun.ts.tests.ejb30.common.helper.TestFailedException.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.TestBeanBase.class,
                com.sun.ts.tests.ejb30.bb.localaccess.webclient.StatelessTestBean.class,
                com.sun.ts.tests.ejb30.bb.localaccess.webclient.TestServletSuper.class, 
                com.sun.ts.tests.servlet.common.util.Data.class,
                com.sun.ts.tests.ejb30.bb.localaccess.webclient.TestServlet.class,
                com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet.class, 
                com.sun.ts.tests.ejb30.bb.localaccess.common.CommonIF.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.TestBeanIF.class, 
                com.sun.ts.tests.ejb30.common.helper.TLogger.class,
                com.sun.ts.tests.ejb30.common.calc.CalculatorException.class, 
                com.sun.ts.tests.ejb30.common.helper.ServiceLocator.class,
                com.sun.ts.tests.ejb30.bb.localaccess.common.Constants.class);
        
        // The web.xml descriptor
        URL warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/localaccess/webclient/localaccess_webclient_web.xml");
        if (warResURL != null) {
            localaccess_webclient_web.addAsWebInfResource(warResURL, "web.xml");
        }
        
        // The sun-web.xml descriptor
        warResURL = Client.class.getResource("/com/sun/ts/tests/ejb30/bb/localaccess/webclient/localaccess_webclient_web.war.sun-web.xml");
        if (warResURL != null) {
            localaccess_webclient_web.addAsWebInfResource(warResURL, "sun-web.xml");
        }

        // Call the archive processor
        archiveProcessor.processWebArchive(localaccess_webclient_web, Client.class, warResURL);

        return localaccess_webclient_web;
    }

    @Test
    @Override
    public void injectedIntoTestServletSuperEvenNoAnnotationInTestServlet() throws java.lang.Exception {
        super.injectedIntoTestServletSuperEvenNoAnnotationInTestServlet();
    }

    @Test
    @Override
    public void postConstructCalledInTestServletSuperEvenNoAnnotationInTestServlet() throws java.lang.Exception {
        super.postConstructCalledInTestServletSuperEvenNoAnnotationInTestServlet();
    }

    @Test
    @Override
    public void passByValueTest() throws java.lang.Exception {
        super.passByValueTest();
    }

}