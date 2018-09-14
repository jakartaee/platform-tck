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
 * $Id$
 */

package com.sun.ts.tests.jsp.spec.tagext.resource.taghandler;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run test */

  /*
   * @testName: ResourceTagHandlerTest
   *
   * @assertion_ids: JSP:SPEC:302
   *
   * @test_Strategy: [TagHandlerResourceInjection] Create a tag handler that
   * implements the Tag interface. Package the tag handler in a WAR file without
   * declaring several resource references in the deployment descriptor -
   * javax.sql.DataSource - javax.jms.QueueConnectionFactory -
   * javax.jms.TopicConnectionFactory - javax.jms.ConnectionFactory -
   * javax.jms.Queue - javax.jms.Topic - javax.mail.Session - java.net.URL
   *
   * Check that: - We can deploy the application. - We can lookup the all the
   * above resources using annotations in a tag handler.
   */

  public void ResourceTagHandlerTest() throws Fault {
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
   * - javax.sql.DataSource - javax.jms.QueueConnectionFactory -
   * javax.jms.TopicConnectionFactory - javax.jms.ConnectionFactory -
   * javax.jms.Queue - javax.jms.Topic - javax.mail.Session - java.net.URL
   *
   * Check that: - We can deploy the application. - We can lookup the all the
   * above resources using annotations in a tag handler.
   */

  public void ResourceSimpleTagHandlerTest() throws Fault {
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

  public void ResourceTagHandlerTimingTest() throws Fault {
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

  public void ResourceSimpleTagHandlerTimingTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagext_resource_taghandler_web/ResourceSimpleTagHandlerTimingTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
