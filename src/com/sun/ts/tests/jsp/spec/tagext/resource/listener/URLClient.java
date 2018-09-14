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

package com.sun.ts.tests.jsp.spec.tagext.resource.listener;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;

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

    setServletName("TestServlet");
    setContextRoot("/jsp_tagext_resource_listener_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run test */

  /*
   * @testName: ContextListenerTest
   *
   * @assertion_ids: JSP:SPEC:304; JSP:SPEC:305
   *
   * @test_Strategy: [EventListenerResourceInjection]
   * [EventListenerResourceInjectionTiming] Create a ServletContextListener,
   * Package all above in a WAR file without declaring several resource
   * references in the deployment descriptor - javax.sql.DataSource -
   * javax.jms.QueueConnectionFactory - javax.jms.TopicConnectionFactory -
   * javax.jms.ConnectionFactory - javax.jms.Queue - javax.jms.Topic -
   * javax.mail.Session - java.net.URL
   *
   * Check that: - We can deploy the application. - We can lookup the all the
   * above resource using annotations inside the ServletContextListener
   */

  public void ContextListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "testResourceCL");
    invoke();
  }

  /*
   * @testName: ContextAttributeListenerTest
   *
   * @assertion_ids: JSP:SPEC:304; JSP:SPEC:305
   *
   * @test_Strategy: [EventListenerResourceInjection]
   * [EventListenerResourceInjectionTiming] Create a
   * ServletContextAttributeListener, Package all above in a WAR file without
   * declaring several resource reference in the deployment descriptor -
   * javax.sql.DataSource - javax.jms.QueueConnectionFactory -
   * javax.jms.TopicConnectionFactory - javax.jms.ConnectionFactory -
   * javax.jms.Queue - javax.jms.Topic - javax.mail.Session - java.net.URL
   *
   * Check that: - We can deploy the application. - We can lookup the all the
   * above resource using annotations inside the ServletContextAttributeListener
   */

  public void ContextAttributeListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "testResourceCAL");
    invoke();
  }

  /*
   * @testName: RequestListenerTest
   *
   * @assertion_ids: JSP:SPEC:304; JSP:SPEC:305
   *
   * @test_Strategy: [EventListenerResourceInjection]
   * [EventListenerResourceInjectionTiming] Create a ServletRequestListener,
   * Package all above in a WAR file without declaring several resource
   * reference in the deployment descriptor - javax.sql.DataSource -
   * javax.jms.QueueConnectionFactory - javax.jms.TopicConnectionFactory -
   * javax.jms.ConnectionFactory - javax.jms.Queue - javax.jms.Topic -
   * javax.mail.Session - java.net.URL
   *
   * Check that: - We can deploy the application. - We can lookup the all the
   * above resource using annotations inside the ServletRequestListener
   */

  public void RequestListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "testResourceRL");
    invoke();
  }

  /*
   * @testName: RequestAttributeListenerTest
   *
   * @assertion_ids: JSP:SPEC:304; JSP:SPEC:305
   *
   * @test_Strategy: [EventListenerResourceInjection]
   * [EventListenerResourceInjectionTiming] Create a
   * ServletRequestAttributeListener, Package all above in a WAR file without
   * declaring several resource reference in the deployment descriptor -
   * javax.sql.DataSource - javax.jms.QueueConnectionFactory -
   * javax.jms.TopicConnectionFactory - javax.jms.ConnectionFactory -
   * javax.jms.Queue - javax.jms.Topic - javax.mail.Session - java.net.URL
   *
   * Check that: - We can deploy the application. - We can lookup the all the
   * above resource using annotations inside the ServletRequestAttributeListener
   */

  public void RequestAttributeListenerTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "testResourceRAL");
    invoke();
  }
}
