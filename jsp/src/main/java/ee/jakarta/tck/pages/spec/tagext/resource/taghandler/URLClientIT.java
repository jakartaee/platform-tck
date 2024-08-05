/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package ee.jakarta.tck.pages.spec.tagext.resource.taghandler;


import java.io.IOException;
import ee.jakarta.tck.pages.common.client.AbstractUrlClient;

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
import java.lang.System.Logger;

@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {

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

  }

  @Deployment(testable = true)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tagext_resource_taghandler_web.war");
    archive.addClasses(JspTestUtil.class);
    archive.addPackages(true, Filters.exclude(URLClientIT.class),
            URLClientIT.class.getPackageName());
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tagext_resource_taghandler_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tagext_resource_taghandler.tld", "tagext_resource_taghandler.tld");    

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ResourceTagHandlerTimingTest.jsp")), "ResourceTagHandlerTimingTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ResourceTagHandlerTest.jsp")), "ResourceTagHandlerTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ResourceSimpleTagHandlerTimingTest.jsp")), "ResourceSimpleTagHandlerTimingTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ResourceSimpleTagHandlerTest.jsp")), "ResourceSimpleTagHandlerTest.jsp");
    
    //  This TCK test needs additional information about roles and principals (DIRECTOR:j2ee).
    //  In GlassFish, the following sun-web.xml descriptor can be added:
    //  archive.addAsWebInfResource("jsp_tagext_resource_httplistener_web.war.sun-web.xml", "sun-web.xml");

    //  Vendor implementations are encouraged to utilize Arqullian SPI (LoadableExtension, ApplicationArchiveProcessor)
    //  to extend the archive with vendor deployment descriptors as needed.
    //  For GlassFish, this is demonstrated in the glassfish-runner/jsp-tck module of the Jakarta Platform GitHub repository.

    return archive;

  }

  
  /* Run test */

  /*
   * @testName: ResourceTagHandlerTest
   *
   * @assertion_ids: JSP:SPEC:302
   *
   * @test_Strategy: [TagHandlerResourceInjection] Create a tag handler that
   * implements the Tag interface. Package the tag handler in a WAR file without
   * declaring several resource references in the deployment descriptor -
   * javax.sql.DataSource - jakarta.jms.QueueConnectionFactory -
   * jakarta.jms.TopicConnectionFactory - jakarta.jms.ConnectionFactory -
   * jakarta.jms.Queue - jakarta.jms.Topic - jakarta.mail.Session - java.net.URL
   *
   * Check that: - We can deploy the application. - We can lookup the all the
   * above resources using annotations in a tag handler.
   */

  @Test
  @Tag("resource")
  @RunAsClient
  public void ResourceTagHandlerTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagext_resource_taghandler_web/ResourceTagHandlerTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: ResourceSimpleTagHandlerTest
   *
   * @assertion_ids: JSP:SPEC:302
   *
   * @test_Strategy: [TagHandlerResourceInjection] Create a tag handler that
   * implements the SimpleTag interface. Package the tag handler in a WAR file
   * without declaring several resource references in the deployment descriptor
   * - javax.sql.DataSource - jakarta.jms.QueueConnectionFactory -
   * jakarta.jms.TopicConnectionFactory - jakarta.jms.ConnectionFactory -
   * jakarta.jms.Queue - jakarta.jms.Topic - jakarta.mail.Session - java.net.URL
   *
   * Check that: - We can deploy the application. - We can lookup the all the
   * above resources using annotations in a tag handler.
   */

  @Test
  @Tag("resource")
  @RunAsClient
  public void ResourceSimpleTagHandlerTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagext_resource_taghandler_web/ResourceSimpleTagHandlerTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: ResourceTagHandlerTimingTest
   *
   * @assertion_ids: JSP:SPEC:303
   *
   * @test_Strategy: [TagHandlerResourceInjectionTiming] Create a tag handler
   * that implements the Tag interface. Package the tag handler in a WAR file
   * without declaring a reference for a resource in the deployment descriptor.
   * Show that injection occurs immediately after an instance of the tag handler
   * is constructed, and before any of the tag properties are initialized, by
   * using a value derived from an injected resource in a setter method.
   */

  @Test
  @Tag("resource")
  @RunAsClient
  public void ResourceTagHandlerTimingTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagext_resource_taghandler_web/ResourceTagHandlerTimingTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: ResourceSimpleTagHandlerTimingTest
   *
   * @assertion_ids: JSP:SPEC:303
   *
   * @test_Strategy: [TagHandlerResourceInjectionTiming] Create a tag handler
   * that implements the SimpleTag interface. Package the tag handler in a WAR
   * file without declaring a reference for a resource in the deployment
   * descriptor. Show that injection occurs immediately after an instance of the
   * tag handler is constructed, and before any of the tag properties are
   * initialized, by using a value derived from an injected resource in a setter
   * method.
   */

  @Test
  @Tag("resource")
  @RunAsClient
  public void ResourceSimpleTagHandlerTimingTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagext_resource_taghandler_web/ResourceSimpleTagHandlerTimingTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
