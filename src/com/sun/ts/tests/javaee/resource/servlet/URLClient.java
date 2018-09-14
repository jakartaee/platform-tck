/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {

  public URLClient() {
    setServletName("TestServlet");
  }

  /**
   * Entry point for different-VM execution.
   */
  public static void main(String[] args) {
    new URLClient().run(args);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

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
  public void resourceCompTest() throws Fault {
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
  public void resourceModuleTest() throws Fault {
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
  public void resourceAppTest() throws Fault {
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
  public void resourceGlobalTest() throws Fault {
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
  public void resRefTest() throws Fault {
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
  public void compTest() throws Fault {
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
  public void moduleTest() throws Fault {
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
  public void appTest() throws Fault {
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
  public void globalTest() throws Fault {
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
  public void compEqualsModuleTest() throws Fault {
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
  public void moduleEqualsCompTest() throws Fault {
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
  public void beanTest() throws Fault {
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
  public void beanCompTest() throws Fault {
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
  public void beanModuleTest() throws Fault {
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
  public void beanAppTest() throws Fault {
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
  public void beanGlobalTest() throws Fault {
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
  public void beanResourceCompTest() throws Fault {
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
  public void beanResourceModuleTest() throws Fault {
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
  public void beanResourceAppTest() throws Fault {
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
  public void beanResourceGlobalTest() throws Fault {
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
  public void beanRefCompTest() throws Fault {
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
  public void beanRefModuleTest() throws Fault {
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
  public void beanRefAppTest() throws Fault {
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
  public void beanRefGlobalTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "beanRefGlobalTest");
    invoke();
  }
}
