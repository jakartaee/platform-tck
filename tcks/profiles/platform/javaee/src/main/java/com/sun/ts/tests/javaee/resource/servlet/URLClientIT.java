/*
 * Copyright (c) 2024 Oracle and/or its affiliates. All rights reserved.
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
 * Test resource annotations and lookups using Servlets.
 */

package com.sun.ts.tests.javaee.resource.servlet;

import com.sun.ts.tests.servlet2.common.client.AbstractUrlClient;

import java.io.IOException;

import com.sun.ts.tests.servlet2.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.servlet2.common.util.ServletTestUtil;
import com.sun.ts.tests.servlet2.common.util.Data;

import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import java.lang.System.Logger;
import java.util.Properties;

@Tag("platform")
@Tag("javaee-module")
@Tag("tck-javatest")
@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {

  private static final Logger logger = System.getLogger(URLClientIT.class.getName());

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
  }

  public URLClientIT() {
    setServletName("TestServlet");
  }

  /* Run test */

  @Deployment(testable = false)
  public static EnterpriseArchive createDeployment() throws IOException {

    WebArchive webarchive = ShrinkWrap.create(WebArchive.class, "javaee_resource_servlet.war");
    webarchive.addPackages(true, Filters.exclude(URLClientIT.class, Pojo.class), URLClientIT.class.getPackageName())
            .addClasses(HttpTCKServlet.class, ServletTestUtil.class, Data.class)
            .addAsLibrary(prepackage());

    EnterpriseArchive earArchive = ShrinkWrap.create(EnterpriseArchive.class, "javaee_resource_servlet.ear");
    earArchive.addAsModule(webarchive);

    return earArchive;

  }

  public static JavaArchive prepackage() throws IOException {
    JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "lib.jar");
    archive.addClasses(Pojo.class);
    return archive;
  }

  /*
   * @testName: resourceCompTest
   * 
   * @assertion: A resource declared in java:comp/env can be referenced using
   * "lookup" in a resource reference.
   * 
   * @assertion_ids: JavaEE:SPEC:8013, JavaEE:SPEC:8014
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, reference it using @Resource on a field
   * of the Servlet and check that the resource is injected and has the expected
   * property configured.
   */
  @Test
  public void resourceCompTest() throws Exception {
    String testName = "resourceCompTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + testName + " HTTP/1.1");
    invoke();
  }

  /*
   * @testName: resourceModuleTest
   * 
   * @assertion: A resource declared in java:module/env can be referenced using
   * "lookup" in a resource reference.
   * 
   * @assertion_ids: JavaEE:SPEC:8013, JavaEE:SPEC:8014
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, reference it using @Resource on a field
   * of the Servlet and check that the resource is injected and has the expected
   * property configured.
   */
  @Test
  public void resourceModuleTest() throws Exception {
    String testName = "resourceModuleTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + testName + " HTTP/1.1");
    invoke();
  }

  /*
   * @testName: resourceAppTest
   * 
   * @assertion: A resource declared in java:app/env can be referenced using
   * "lookup" in a resource reference.
   * 
   * @assertion_ids: JavaEE:SPEC:8013, JavaEE:SPEC:8014
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, reference it using @Resource on a field
   * of the Servlet and check that the resource is injected and has the expected
   * property configured.
   */
  @Test
  public void resourceAppTest() throws Exception {
    String testName = "resourceAppTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + testName + " HTTP/1.1");
    invoke();
  }

  /*
   * @testName: resourceGlobalTest
   * 
   * @assertion: A resource declared in java:comp/env can be referenced using
   * "lookup" in a resource reference.
   * 
   * @assertion_ids: JavaEE:SPEC:8013, JavaEE:SPEC:8014
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, reference it using @Resource on a field
   * of the Servlet and check that the resource is injected and has the expected
   * property configured.
   */
  @Test
  public void resourceGlobalTest() throws Exception {
    String testName = "resourceGlobalTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + testName + " HTTP/1.1");
    invoke();
  }

  /*
   * @testName: resRefTest
   * 
   * @assertion: A resource declared in java:comp/env can be accessed using a
   * JNDI lookup.
   * 
   * @assertion_ids: JavaEE:SPEC:8000, JavaEE:SPEC:8006, JavaEE:SPEC:8010
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, look it up using JNDI from a different
   * Servlet in the same web module and check that the resource is found and has
   * the expected property configured.
   */
  @Test
  public void resRefTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "resRefTest");
    invoke();
  }

  /*
   * @testName: compTest
   * 
   * @assertion: A resource declared in java:comp/env can be accessed using a
   * JNDI lookup.
   * 
   * @assertion_ids: JavaEE:SPEC:8000, JavaEE:SPEC:8006, JavaEE:SPEC:8002
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, look it up using JNDI from a different
   * Servlet in the same web module and check that the resource is found and has
   * the expected property configured.
   */
  @Test
  public void compTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "compTest");
    invoke();
  }

  /*
   * @testName: moduleTest
   * 
   * @assertion: A resource declared in java:module/env can be accessed using a
   * JNDI lookup.
   * 
   * @assertion_ids: JavaEE:SPEC:8000, JavaEE:SPEC:8006, JavaEE:SPEC:8003
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, look it up using JNDI from a different
   * Servlet in the same web module and check that the resource is found and has
   * the expected property configured.
   */
  @Test
  public void moduleTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "moduleTest");
    invoke();
  }

  /*
   * @testName: appTest
   * 
   * @assertion: A resource declared in java:app/env can be accessed using a
   * JNDI lookup.
   * 
   * @assertion_ids: JavaEE:SPEC:8000, JavaEE:SPEC:8006, JavaEE:SPEC:8004
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, look it up using JNDI from a different
   * Servlet in the same application and check that the resource is found and
   * has the expected property configured.
   */
  @Test
  public void appTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "appTest");
    invoke();
  }

  /*
   * @testName: globalTest
   * 
   * @assertion: A resource declared in java:global/env can be accessed using a
   * JNDI lookup.
   * 
   * @assertion_ids: JavaEE:SPEC:8000, JavaEE:SPEC:8006, JavaEE:SPEC:8005
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, look it up using JNDI from a different
   * Servlet and check that the resource is found and has the expected property
   * configured.
   */
  @Test
  public void globalTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "globalTest");
    invoke();
  }

  /*
   * @testName: compEqualsModuleTest
   * 
   * @assertion: A resource declared in java:comp/env can be accessed using a
   * JNDI lookup in java:module/env.
   * 
   * @assertion_ids: JavaEE:SPEC:8000, JavaEE:SPEC:8006
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, look it up using JNDI from a different
   * Servlet in the same web module and check that the resource is found and has
   * the expected property configured.
   */
  @Test
  public void compEqualsModuleTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "compEqualsModuleTest");
    invoke();
  }

  /*
   * @testName: moduleEqualsCompTest
   * 
   * @assertion: A resource declared in java:module/env can be accessed using a
   * JNDI lookup in java:comp/env.
   * 
   * @assertion_ids: JavaEE:SPEC:8000, JavaEE:SPEC:8006
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, look it up using JNDI from a different
   * Servlet in the same web module and check that the resource is found and has
   * the expected property configured.
   */
  @Test
  public void moduleEqualsCompTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "moduleEqualsCompTest");
    invoke();
  }

  /*
   * @testName: beanTest
   * 
   * @assertion: A resource declared in java:app/env by a CDI managed bean can
   * be accessed using a JNDI lookup.
   * 
   * @assertion_ids: JavaEE:SPEC:8000, JavaEE:SPEC:8004
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a CDI bean, look it up using JNDI from a Servlet
   * and check that the resource is found and has the expected property
   * configured.
   */
  @Test
  public void beanTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "beanTest");
    invoke();
  }

  /*
   * @testName: beanCompTest
   * 
   * @assertion: A resource declared in java:comp/env can be accessed using a
   * JNDI lookup from a CDI bean.
   * 
   * @assertion_ids: JavaEE:SPEC:8000, JavaEE:SPEC:8002
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, look it up using JNDI from a CDI bean in
   * the same web module and check that the resource is found and has the
   * expected property configured.
   */
  @Test
  public void beanCompTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "beanCompTest");
    invoke();
  }

  /*
   * @testName: beanModuleTest
   * 
   * @assertion: A resource declared in java:module/env can be accessed using a
   * JNDI lookup from a CDI bean.
   * 
   * @assertion_ids: JavaEE:SPEC:8000, JavaEE:SPEC:8003
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, look it up using JNDI from a CDI bean in
   * the same web module and check that the resource is found and has the
   * expected property configured.
   */
  @Test
  public void beanModuleTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "beanModuleTest");
    invoke();
  }

  /*
   * @testName: beanAppTest
   * 
   * @assertion: A resource declared in java:app/env can be accessed using a
   * JNDI lookup from a CDI bean.
   * 
   * @assertion_ids: JavaEE:SPEC:8000, JavaEE:SPEC:8004
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, look it up using JNDI from a CDI bean in
   * the same application and check that the resource is found and has the
   * expected property configured.
   */
  @Test
  public void beanAppTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "beanAppTest");
    invoke();
  }

  /*
   * @testName: beanGlobalTest
   * 
   * @assertion: A resource declared in java:global/env can be accessed using a
   * JNDI lookup from a CDI bean.
   * 
   * @assertion_ids: JavaEE:SPEC:8000, JavaEE:SPEC:8005
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, look it up using JNDI from a CDI bean
   * and check that the resource is found and has the expected property
   * configured.
   */
  @Test
  public void beanGlobalTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "beanGlobalTest");
    invoke();
  }

  /*
   * @testName: beanResourceCompTest
   * 
   * @assertion: A resource declared in java:comp/env can be injected into a CDI
   * bean in the same web module using @Resource
   * 
   * @assertion_ids: JavaEE:SPEC:8002, JavaEE:SPEC:8006, JavaEE:SPEC:8010,
   * JavaEE:SPEC:8012, JavaEE:SPEC:8014
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, inject it into a CDI bean and check that
   * the resource is found and has the expected property configured.
   */
  @Test
  public void beanResourceCompTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "beanResourceCompTest");
    invoke();
  }

  /*
   * @testName: beanResourceModuleTest
   * 
   * @assertion: A resource declared in java:module/env can be injected into a
   * CDI bean in the same web module using @Resource
   * 
   * @assertion_ids: JavaEE:SPEC:8003, JavaEE:SPEC:8006, JavaEE:SPEC:8010,
   * JavaEE:SPEC:8012, JavaEE:SPEC:8014
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, inject it into a CDI bean and check that
   * the resource is found and has the expected property configured.
   */
  @Test
  public void beanResourceModuleTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "beanResourceModuleTest");
    invoke();
  }

  /*
   * @testName: beanResourceAppTest
   * 
   * @assertion: A resource declared in java:app/env can be injected into a CDI
   * bean using @Resource
   * 
   * @assertion_ids: JavaEE:SPEC:8004, JavaEE:SPEC:8006, JavaEE:SPEC:8010,
   * JavaEE:SPEC:8012, JavaEE:SPEC:8014
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, inject it into a CDI bean and check that
   * the resource is found and has the expected property configured.
   */
  @Test
  public void beanResourceAppTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "beanResourceAppTest");
    invoke();
  }

  /*
   * @testName: beanResourceGlobalTest
   * 
   * @assertion: A resource declared in java:global/env can be accessed using a
   * JNDI lookup from a CDI bean. injected into a CDI bean using @Resource
   * 
   * @assertion_ids: JavaEE:SPEC:8005, JavaEE:SPEC:8006, JavaEE:SPEC:8010,
   * JavaEE:SPEC:8012, JavaEE:SPEC:8014
   * 
   * @test_Strategy: Declare a JavaMail Session resource using
   * 
   * @MailSessionResource on a Servlet, inject it into a CDI bean and check that
   * the resource is found and has the expected property configured.
   */
  @Test
  public void beanResourceGlobalTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "beanResourceGlobalTest");
    invoke();
  }

  /*
   * @testName: beanRefCompTest
   * 
   * @assertion: A @Resource annotation on a CDI bean in a web module can create
   * a java:app/env resource reference accessible to other CDI beans in the same
   * web module, and can refer to java:comp/env resources in that web module
   * 
   * @assertion_ids: JavaEE:SPEC:8000, JavaEE:SPEC:8002, JavaEE:SPEC:8011,
   * JavaEE:SPEC:8014
   * 
   * @test_Strategy: Declare a resource reference on a CDI bean field
   * using @Resource referring to a java:comp resource declared elsewhere in
   * that web module, look up the resource reference by name from another CDI
   * bean using JNDI, and check that the resource is found and has the expected
   * property configured.
   */
  @Test
  public void beanRefCompTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "beanRefCompTest");
    invoke();
  }

  /*
   * @testName: beanRefModuleTest
   * 
   * @assertion: A @Resource annotation on a CDI bean in a web module can create
   * a java:app/env resource reference accessible to other CDI beans in the same
   * web module, and can refer to java:module/env resources in that web module
   * 
   * @assertion_ids: JavaEE:SPEC:8000, JavaEE:SPEC:8003, JavaEE:SPEC:8011,
   * JavaEE:SPEC:8014
   * 
   * @test_Strategy: Declare a resource reference on a CDI bean field
   * using @Resource referring to a java:comp resource declared elsewhere in
   * that web module, look up the resource reference by name from another CDI
   * bean using JNDI, and check that the resource is found and has the expected
   * property configured.
   */
  @Test
  public void beanRefModuleTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "beanRefModuleTest");
    invoke();
  }

  /*
   * @testName: beanRefAppTest
   * 
   * @assertion: A @Resource annotation on a CDI bean in a web module can create
   * a java:app/env resource reference accessible to other CDI beans in the same
   * web module, and can refer to java:app/env resources
   * 
   * @assertion_ids: JavaEE:SPEC:8000, JavaEE:SPEC:8004, JavaEE:SPEC:8011,
   * JavaEE:SPEC:8014
   * 
   * @test_Strategy: Declare a resource reference on a CDI bean field
   * using @Resource referring to a java:comp resource declared elsewhere in
   * that web module, look up the resource reference by name from another CDI
   * bean using JNDI, and check that the resource is found and has the expected
   * property configured.
   */
  @Test
  public void beanRefAppTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "beanRefAppTest");
    invoke();
  }

  /*
   * @testName: beanRefGlobalTest
   * 
   * @assertion: A @Resource annotation on a CDI bean in a web module can create
   * a java:app/env resource reference accessible to other CDI beans in the same
   * web module, and can refer to java:global/env resources
   * 
   * @assertion_ids: JavaEE:SPEC:8000, JavaEE:SPEC:8005, JavaEE:SPEC:8011,
   * JavaEE:SPEC:8014
   * 
   * @test_Strategy: Declare a resource reference on a CDI bean field
   * using @Resource referring to a java:comp resource declared elsewhere in
   * that web module, look up the resource reference by name from another CDI
   * bean using JNDI, and check that the resource is found and has the expected
   * property configured.
   */
  @Test
  public void beanRefGlobalTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "beanRefGlobalTest");
    invoke();
  }
}
