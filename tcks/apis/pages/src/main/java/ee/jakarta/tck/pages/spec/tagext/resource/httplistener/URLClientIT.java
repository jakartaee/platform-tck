/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package ee.jakarta.tck.pages.spec.tagext.resource.httplistener;

import java.io.PrintWriter;

import java.io.IOException;
import ee.jakarta.tck.pages.common.client.ServletAbstractUrlClient;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

import ee.jakarta.tck.pages.common.util.JspTestUtil;
import ee.jakarta.tck.pages.common.util.ServletTestUtil;
import ee.jakarta.tck.pages.common.util.Data;
import ee.jakarta.tck.pages.common.servlet.HttpTCKServlet;

import java.lang.System.Logger;

@Tag("jsp")
@Tag("platform")
@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends ServletAbstractUrlClient {

  private static final Logger logger = System.getLogger(URLClientIT.class.getName());

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "STARTING TEST : "+testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "FINISHED TEST : "+testInfo.getDisplayName());
  }


  public URLClientIT() throws Exception {
    setServletName("TestServlet");
    setContextRoot("/jsp_tagext_resource_httplistener_web");
  }

  @Deployment(testable = true)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tagext_resource_httplistener_web.war");
    archive.addClasses(JspTestUtil.class,
          ServletTestUtil.class,
          Data.class,
          HttpTCKServlet.class
    );
    archive.addPackages(true, Filters.exclude(URLClientIT.class),
            URLClientIT.class.getPackageName());
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tagext_resource_httplistener_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tagext_resource_httplistener.tld", "tagext_resource_httplistener.tld");    

    //  This TCK test needs additional information about roles and principals (DIRECTOR:j2ee).
    //  In GlassFish, the following sun-web.xml descriptor can be added:
    //  archive.addAsWebInfResource("jsp_tagext_resource_httplistener_web.war.sun-web.xml", "sun-web.xml");

    //  Vendor implementations are encouraged to utilize Arqullian SPI (LoadableExtension, ApplicationArchiveProcessor)
    //  to extend the archive with vendor deployment descriptors as needed.
    //  For GlassFish, this is demonstrated in the glassfish-runner/jsp-platform-tck module of the Jakarta Platform GitHub repository.

    return archive;

  }

  
  /* Run test */

  /*
   * @testName: testResourceSL
   *
   * @assertion_ids: JSP:SPEC:304;JSP:SPEC:305
   *
   * @test_Strategy: [EventListenerResourceInjection]
   * [EventListenerResourceInjectionTiming] Create a SessionListener, Package
   * the servlet in a WAR file without declaring serveral resources references
   * in deployment descriptor - javax.sql.DataSource -
   * jakarta.jms.QueueConnectionFactory - jakarta.jms.TopicConnectionFactory -
   * jakarta.jms.ConnectionFactory - jakarta.jms.Queue - jakarta.jms.Topic -
   * jakarta.mail.Session - java.net.URL
   *
   * Check that: - We can deploy the application. - We can lookup the all the
   * above resource using annotation @Resource in the SessionListener
   */

  @Test
  @RunAsClient
  public void testResourceSL() throws Exception {
    String testName = "testResourceSL";
    TEST_PROPS.setProperty(APITEST, testName);

    invoke();
  }

  /*
   * @testName: testResourceSAL
   *
   * @assertion_ids: JSP:SPEC:304;JSP:SPEC:305
   *
   * @test_Strategy: [EventListenerResourceInjection]
   * [EventListenerResourceInjectionTiming] Create a
   * HttpSessionAttributeListener, Package all above in a WAR file without
   * declaring serveral resources references in deployment descriptor -
   * javax.sql.DataSource - jakarta.jms.QueueConnectionFactory -
   * jakarta.jms.TopicConnectionFactory - jakarta.jms.ConnectionFactory -
   * jakarta.jms.Queue - jakarta.jms.Topic - jakarta.mail.Session - java.net.URL
   *
   * Check that: - We can deploy the application. - We can lookup the all the
   * above resource using annotation inside the HttpSessionAttributeListener
   */

  @Test
  @RunAsClient
  public void testResourceSAL() throws Exception {
    String testName = "testResourceSAL";
    TEST_PROPS.setProperty(APITEST, testName);

    invoke();
  }
}
