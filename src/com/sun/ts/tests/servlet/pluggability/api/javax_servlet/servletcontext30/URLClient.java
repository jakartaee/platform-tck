/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id:$
 */
package com.sun.ts.tests.servlet.pluggability.api.javax_servlet.servletcontext30;

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

    setContextRoot("/servlet_plu_servletcontext30_web");
    setServletName("TestServlet");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /* Run test */
  /*
   * Test setup; In Deployment Descriptor, a Servlet TestServlet and Listener
   * TestListener; TestListener extends ServletContextListener;
   *
   * In TestListener, three Servlets are added using the following methods: -
   * ServletContext.addServlet(String, String); -
   * ServletContext.addServlet(String, Class); -
   * ServletContext.createServlet(Class);
   *
   * In TestListener, three Filters are added using the following methods: -
   * ServletContext.addFilter(String, String); -
   * ServletContext.addFilter(String, Class); -
   * ServletContext.createServlet(Class);
   *
   * In TestListener, three Listeners are added using the following methods: -
   * ServletContext.addListener(Listener Class) -
   * ServletContext.addListener(Listener name) - EventListener listener =
   * ServletContext.createListener(class); ServletContext.addListener(listener);
   * for all three following Listeners: - ServletContextAttributeListener -
   * ServletRequestListener - ServletRequestAttributesListener
   */
  /*
   * @testName: getContextPathTest
   *
   * @assertion_ids: Servlet:JAVADOC:124; Servlet:JAVADOC:258;
   * Servlet:JAVADOC:637; Servlet:JAVADOC:671.1; Servlet:JAVADOC:671.2;
   * Servlet:JAVADOC:671.3; Servlet:JAVADOC:672.1; Servlet:JAVADOC:672.2;
   * Servlet:JAVADOC:672.3; Servlet:JAVADOC:673.1; Servlet:JAVADOC:673.2;
   * Servlet:JAVADOC:673.3; Servlet:JAVADOC:679;
   *
   * @test_Strategy: In TestServlet verify that the result from the
   * ServletContext.getServletContextPath call returns the context path.
   *
   * In client verify that all Listeners are added correctly and invoked in the
   * order added.
   */
  public void getContextPathTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getContextPathTest");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "AddSRListenerClass_INVOKED" + "|AddSRListenerString_INVOKED"
            + "|CreateSRListener_INVOKED" + "|AttributeAddedClass"
            + "|AttributeAddedString");
    invoke();
  }

  /*
   * @testName: testAddServletString
   *
   * @assertion_ids: Servlet:JAVADOC:655; Servlet:JAVADOC:668;
   * Servlet:JAVADOC:671.1; Servlet:JAVADOC:671.2; Servlet:JAVADOC:671.3;
   * Servlet:JAVADOC:672.1; Servlet:JAVADOC:672.2; Servlet:JAVADOC:672.3;
   * Servlet:JAVADOC:673.1; Servlet:JAVADOC:673.2; Servlet:JAVADOC:673.3;
   * Servlet:JAVADOC:674; Servlet:JAVADOC:679; Servlet:JAVADOC:696;
   *
   * @test_Strategy: Create a ServletContextListener, in which, 1. create a
   * Servlet by calling ServletContext.addServlet(String, String), 2. mapping
   * the new Servlet programmatically. 3. create a Filter by
   * ServletContext.addFilter(String, String) 4. map the filter to the new
   * Servlet programmatically for FORWARD only 5. client send a request to the
   * new servlet, Verify in client that request goes through and Filter is NOT
   * invoked. Verify in client that all Listeners are added correctly and
   * invoked in the order added.
   */
  public void testAddServletString() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/addServletString HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "ADD_FILTER_STRING_INVOKED");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "AddSRListenerClass_INVOKED" + "|AddSRListenerString_INVOKED"
            + "|CreateSRListener_INVOKED" + "|SCAttributeAddedClass:"
            + "|SCAttributeAddedString:" + "|SCAttributeAdded:");
    invoke();
  }

  /*
   * @testName: testAddFilterString
   *
   * @assertion_ids: Servlet:JAVADOC:655; Servlet:JAVADOC:668;
   * Servlet:JAVADOC:671.1; Servlet:JAVADOC:671.2; Servlet:JAVADOC:671.3;
   * Servlet:JAVADOC:672.1; Servlet:JAVADOC:672.2; Servlet:JAVADOC:672.3;
   * Servlet:JAVADOC:673.1; Servlet:JAVADOC:673.2; Servlet:JAVADOC:673.3;
   * Servlet:JAVADOC:674; Servlet:JAVADOC:679; Servlet:JAVADOC:696;
   *
   * @test_Strategy: Create a ServletContextListener, in which, 1. create a
   * Servlet by calling ServletContext.addServlet(String, String), 2. mapping
   * the new Servlet programmatically. 3. create a Filter by
   * ServletContext.addFilter(String, String) 4. map the filter to the new
   * Servlet programmatically for FORWARD only 5. client send a request to
   * another servlet, 6. which then forward to the newly created Servlet Verify
   * in client that request goes through and Filter IS invoked. Verify in client
   * that all Listeners are added correctly and invoked in the order added.
   */
  public void testAddFilterString() throws Fault {
    TEST_PROPS.setProperty(APITEST, "testAddFilterString");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "AddServletString" + "|AddSRListenerClass_INVOKED"
            + "|AddSRListenerString_INVOKED" + "|CreateSRListener_INVOKED"
            + "|ADD_FILTER_STRING_INVOKED" + "|SCAttributeAddedClass:"
            + "|SCAttributeAddedString:" + "|SCAttributeAdded:");
    invoke();
  }

  /*
   * @testName: testAddServletClass
   *
   * @assertion_ids: Servlet:JAVADOC:655; Servlet:JAVADOC:670;
   * Servlet:JAVADOC:671.1; Servlet:JAVADOC:671.2; Servlet:JAVADOC:671.3;
   * Servlet:JAVADOC:672.1; Servlet:JAVADOC:672.2; Servlet:JAVADOC:672.3;
   * Servlet:JAVADOC:673.1; Servlet:JAVADOC:673.2; Servlet:JAVADOC:673.3;
   * Servlet:JAVADOC:676; Servlet:JAVADOC:679; Servlet:JAVADOC:696;
   *
   * @test_Strategy: Create a ServletContextListener, in which, 1. create a
   * Servlet by calling ServletContext.addServlet(String, Class), 2. mapping the
   * new Servlet programmatically. 3. create a Filter by
   * ServletContext.addFilter(String, Class) 4. map the filter to the new
   * Servlet programmatically for REQUEST only 5. client send a request to the
   * new servlet, Verify in client that request goes through and Filter is
   * invoked. Verify in client that all Listeners are added correctly and
   * invoked in the order added.
   */
  public void testAddServletClass() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/addServletClass HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING,
        "AddServletClass" + "|AddSRListenerClass_INVOKED"
            + "|AddSRListenerString_INVOKED" + "|CreateSRListener_INVOKED"
            + "|ADD_FILTER_CLASS_INVOKED" + "|SCAttributeAddedClass:"
            + "|SCAttributeAddedString:" + "|SCAttributeAdded:");
    invoke();
  }

  /*
   * @testName: testAddFilterClass
   *
   * @assertion_ids: Servlet:JAVADOC:655; Servlet:JAVADOC:670;
   * Servlet:JAVADOC:671.1; Servlet:JAVADOC:671.2; Servlet:JAVADOC:671.3;
   * Servlet:JAVADOC:672.1; Servlet:JAVADOC:672.2; Servlet:JAVADOC:672.3;
   * Servlet:JAVADOC:673.1; Servlet:JAVADOC:673.2; Servlet:JAVADOC:673.3;
   * Servlet:JAVADOC:676; Servlet:JAVADOC:679; Servlet:JAVADOC:696;
   *
   * @test_Strategy: Create a ServletContextListener, in which, 1. create a
   * Servlet by calling ServletContext.addServlet(String, Class), 2. mapping the
   * new Servlet programmatically. 3. create a Filter by
   * ServletContext.addFilter(String, Class) 4. map the filter to the new
   * Servlet programmatically for REQUEST only 5. client send a request to
   * another servlet, 6. which then dispatch by include to the newly created
   * Servlet Verify in client that request goes through and Filter IS NOT
   * invoked. Verify in client that all Listeners are added correctly and
   * invoked in the order added.
   */
  public void testAddFilterClass() throws Fault {
    TEST_PROPS.setProperty(APITEST, "testAddFilterClass");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "ADD_FILTER_CLASS_INVOKED");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "AddServletClass" + "|AddSRListenerClass_INVOKED"
            + "|AddSRListenerString_INVOKED" + "|CreateSRListener_INVOKED"
            + "|SCAttributeAddedClass" + "|SCAttributeAddedString");
    invoke();
  }

  /*
   * @testName: testAddServlet
   *
   * @assertion_ids: Servlet:JAVADOC:655; Servlet:JAVADOC:669;
   * Servlet:JAVADOC:670; Servlet:JAVADOC:671.1; Servlet:JAVADOC:671.2;
   * Servlet:JAVADOC:671.3; Servlet:JAVADOC:672.1; Servlet:JAVADOC:672.2;
   * Servlet:JAVADOC:672.3; Servlet:JAVADOC:673.1; Servlet:JAVADOC:673.2;
   * Servlet:JAVADOC:673.3; Servlet:JAVADOC:675; Servlet:JAVADOC:677;
   * Servlet:JAVADOC:679; Servlet:JAVADOC:681; Servlet:JAVADOC:696;
   *
   * @test_Strategy: Create a ServletContextListener, in which, 1. Create a
   * Servlet instance using ServletContext.createServlet; 2. Add the Servlet
   * instance: ServletContext.addServlet(String, Servlet), 3. mapping the new
   * Servlet programmatically. 4. create a Filter instance by
   * ServletContext.createFilter; 5 Add the Filter instance:
   * ServletContext.addFilter(String, Filter) 6. map the filter to the new
   * Servlet programmatically for REQUEST only 7. client send a request to the
   * new servlet, Verify in client that request goes through and Filter is
   * invoked. Verify in client that all Listeners are added correctly and
   * invoked in the order added.
   */
  public void testAddServlet() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/createServlet HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "CreateServlet" + "|AddSRListenerClass_INVOKED"
            + "|AddSRListenerString_INVOKED" + "|CreateSRListener_INVOKED"
            + "|SCAttributeAddedClass:" + "|SCAttributeAddedString:"
            + "|SCAttributeAdded:" + "|CREATE_FILTER_INVOKED");

    invoke();
  }

  /*
   * @testName: testAddFilterForward
   *
   * @assertion_ids: Servlet:JAVADOC:655; Servlet:JAVADOC:669;
   * Servlet:JAVADOC:670; Servlet:JAVADOC:671.1; Servlet:JAVADOC:671.2;
   * Servlet:JAVADOC:671.3; Servlet:JAVADOC:672.1; Servlet:JAVADOC:672.2;
   * Servlet:JAVADOC:672.3; Servlet:JAVADOC:673.1; Servlet:JAVADOC:673.2;
   * Servlet:JAVADOC:673.3; Servlet:JAVADOC:675; Servlet:JAVADOC:677;
   * Servlet:JAVADOC:679; Servlet:JAVADOC:681; Servlet:JAVADOC:696;
   *
   * @test_Strategy: Create a ServletContextListener, in which, 1. Create a
   * Servlet instance using ServletContext.createServlet; 2. Add the Servlet
   * instance: ServletContext.addServlet(String, Servlet), 3. mapping the new
   * Servlet programmatically. 4. create a Filter instance by
   * ServletContext.createFilter; 5 Add the Filter instance:
   * ServletContext.addFilter(String, Filter) 6. map the filter to the new
   * Servlet programmatically for REQUEST only 7. client send a request to the
   * new servlet via FORWARD, Verify in client that request does go through and
   * Filter is NOT invoked. Verify in client that all Listeners are added
   * correctly and invoked in the order added.
   */
  public void testAddFilterForward() throws Fault {
    TEST_PROPS.setProperty(APITEST, "testCreateFilterForward");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "CREATE_FILTER_INVOKED");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "CreateServlet" + "|AddSRListenerClass_INVOKED"
            + "|AddSRListenerString_INVOKED" + "|CreateSRListener_INVOKED"
            + "|SCAttributeAddedClass:" + "|SCAttributeAddedString:"
            + "|SCAttributeAdded:");
    invoke();
  }

  /*
   * @testName: testAddFilterInclude
   *
   * @assertion_ids: Servlet:JAVADOC:655; Servlet:JAVADOC:669;
   * Servlet:JAVADOC:670; Servlet:JAVADOC:671.1; Servlet:JAVADOC:671.2;
   * Servlet:JAVADOC:671.3; Servlet:JAVADOC:672.1; Servlet:JAVADOC:672.2;
   * Servlet:JAVADOC:672.3; Servlet:JAVADOC:673.1; Servlet:JAVADOC:673.2;
   * Servlet:JAVADOC:673.3; Servlet:JAVADOC:675; Servlet:JAVADOC:677;
   * Servlet:JAVADOC:679; Servlet:JAVADOC:681; Servlet:JAVADOC:696;
   *
   * @test_Strategy: Create a ServletContextListener, in which, 1. Create a
   * Servlet instance using ServletContext.createServlet; 2. Add the Servlet
   * instance: ServletContext.addServlet(String, Servlet), 3. mapping the new
   * Servlet programmatically. 4. create a Filter instance by
   * ServletContext.createFilter; 5 Add the Filter instance:
   * ServletContext.addFilter(String, Filter) 6. map the filter to the new
   * Servlet programmatically for REQUEST only 7. client send a request to the
   * new servlet via INCLUDE, Verify in client that request does go through and
   * Filter is NOT invoked. Verify in client that all Listeners are added
   * correctly and invoked in the order added.
   */
  public void testAddFilterInclude() throws Fault {
    TEST_PROPS.setProperty(APITEST, "testCreateFilterInclude");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "CREATE_FILTER_INVOKED");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "CreateServlet" + "|AddSRListenerClass_INVOKED"
            + "|AddSRListenerString_INVOKED" + "|CreateSRListener_INVOKED"
            + "|SCAttributeAddedClass:" + "|SCAttributeAddedString:"
            + "|SCAttributeAdded:");
    invoke();
  }

  /*
   * @testName: testAddServletNotFound
   *
   * @assertion_ids: Servlet:JAVADOC:655; Servlet:JAVADOC:670;
   * Servlet:JAVADOC:671.1; Servlet:JAVADOC:671.2; Servlet:JAVADOC:671.3;
   * Servlet:JAVADOC:672.1; Servlet:JAVADOC:672.2; Servlet:JAVADOC:672.3;
   * Servlet:JAVADOC:673.1; Servlet:JAVADOC:673.2; Servlet:JAVADOC:673.3;
   * Servlet:JAVADOC:676; Servlet:JAVADOC:679; Servlet:JAVADOC:696;
   *
   * @test_Strategy: Create a ServletContextListener, in which, 1. create a
   * Servlet by calling ServletContext.addServlet(String, Class), 2. mapping the
   * new Servlet programmatically to multiple URLs, one of them is used by
   * another Servlet already. 3. create a Filter by
   * ServletContext.addFilter(String, Class) 4. map the filter to the new
   * Servlet programmatically for all DispatcherType 5. client send a request to
   * the new servlet, Verify in client that request does NOT go through and
   * Filter is NOT invoked.
   */
  public void testAddServletNotFound() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/addServletNotFound HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, NOT_FOUND);
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "AddServletNotFound|ADD_FILTER_NOTFOUND");
    invoke();
  }

  /*
   * @testName: testCreateSRAListener
   *
   * @assertion_ids: Servlet:JAVADOC:655; Servlet:JAVADOC:669;
   * Servlet:JAVADOC:670; Servlet:JAVADOC:671.1; Servlet:JAVADOC:671.2;
   * Servlet:JAVADOC:671.3; Servlet:JAVADOC:672.1; Servlet:JAVADOC:672.2;
   * Servlet:JAVADOC:672.3; Servlet:JAVADOC:673.1; Servlet:JAVADOC:673.2;
   * Servlet:JAVADOC:673.3; Servlet:JAVADOC:675; Servlet:JAVADOC:677;
   * Servlet:JAVADOC:679; Servlet:JAVADOC:681; Servlet:JAVADOC:696;
   *
   * @test_Strategy: Create a ServletContextListener, in which, one
   * ServletAttributeListener, one ServletRequestListener one
   * ServletRequestAttributeListener are added: 1. Create a Servlet instance
   * using ServletContext.createServlet; 2. Add the Servlet instance:
   * ServletContext.addServlet(String, Servlet), 3. mapping the new Servlet
   * programmatically. 4. create a Filter instance by
   * ServletContext.createFilter; 5 Add the Filter instance:
   * ServletContext.addFilter(String, Filter) 6. map the filter to the new
   * Servlet programmatically for REQUEST only 7. client send a request to
   * another servlet, in which, ServletRequestAttributes are added, then
   * dispatch to the new servlet via FORWARD Verify in client that - create
   * Listener works - request does NOT through and Filter is NOT invoked. - all
   * Listeners are added correctly and invoked in the order added.
   */
  public void testCreateSRAListener() throws Fault {
    TEST_PROPS.setProperty(APITEST, "testCreateSRAListener");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "CREATE_FILTER_INVOKED");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "AddServletClass" + "|AddSRListenerClass_INVOKED"
            + "|AddSRListenerString_INVOKED" + "|CreateSRListener_INVOKED"
            + "|SCAttributeAddedClass:" + "|SCAttributeAddedString:"
            + "|SCAttributeAdded:" + "|SRAttributeAddedClass:"
            + "|SRAttributeAddedString:" + "|SRAttributeAdded:");
    invoke();
  }

  /*
   * @testName: negativeCreateTests
   *
   * @assertion_ids: Servlet:JAVADOC:243; Servlet:JAVADOC:678;
   * Servlet:JAVADOC:680; Servlet:JAVADOC:682; Servlet:JAVADOC:694;
   *
   * @test_Strategy: 1. Create a Servlet which throws ServletException at init;
   * 2. Create a Filter which throws ServletException at init 3. Create a
   * EventListener which throws NullPointerException at init
   *
   * Create a ServletContextListener, in which: 4. Call
   * ServletContext.createFilter(Filter) which should fail; 5. Call
   * ServletContext.createServlet(Servlet) which fails 6. Call
   * ServletContext.createListener(EventListener) which fails 7. Call
   * ServletContext.setInitParameter to pass information about status on 4-6 8.
   * In another Servlet, get all information stored in ServletContext
   * InitParameter 9. Send a request to the new Servlet Verify that -
   * createServlet failed accordingly; - createFilter failed accordingly; -
   * createListener failed accordingly; - setInitParameter works properly
   */
  public void negativeCreateTests() throws Fault {
    TEST_PROPS.setProperty(APITEST, "negativeCreateTests");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "SERVLET_TEST=TRUE" + "|FILTER_TEST=TRUE" + "|LISTENER_TEST=TRUE");

    invoke();
  }

  /*
   * @testName: getEffectiveMajorVersionTest
   *
   * @assertion_ids: Servlet:JAVADOC:685;
   *
   * @test_Strategy: Create a Servlet, in which call
   * ServletContext.getEffectiveMajorVersion() Verify that 3 is returned.
   */
  public void getEffectiveMajorVersionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getEffectiveMajorVersionTest");
    TEST_PROPS.setProperty(SEARCH_STRING, "EFFECTIVEMAJORVERSION=3;");
    invoke();
  }

  /*
   * @testName: getEffectiveMinorVersionTest
   *
   * @assertion_ids: Servlet:JAVADOC:686;
   *
   * @test_Strategy: Create a Servlet, in which call
   * ServletContext.getEffectiveMinorVersion() Verify that 0 is returned.
   */
  public void getEffectiveMinorVersionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getEffectiveMinorVersionTest");
    TEST_PROPS.setProperty(SEARCH_STRING, "EFFECTIVEMINORVERSION=0;");
    invoke();
  }

  /*
   * @testName: getDefaultSessionTrackingModes
   *
   * @assertion_ids: Servlet:JAVADOC:684;
   *
   * @test_Strategy: Create a Servlet, in which call
   * ServletContext.getDefaultSessionTrackingModes() Verify it works.
   */
  public void getDefaultSessionTrackingModes() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getDefaultSessionTrackingModes");
    invoke();
  }

  /*
   * @testName: sessionTrackingModesValueOfTest
   *
   * @assertion_ids: Servlet:JAVADOC:747;
   *
   * @test_Strategy: Create a Servlet, verify SessionTrackingModes.valueOf()
   * works
   */
  public void sessionTrackingModesValueOfTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "sessionTrackingModesValueOfTest");
    invoke();
  }

  /*
   * @testName: sessionTrackingModesValuesTest
   *
   * @assertion_ids: Servlet:JAVADOC:748;
   *
   * @test_Strategy: Create a Servlet, verify SessionTrackingModes.values()
   * works
   */
  public void sessionTrackingModesValuesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "sessionTrackingModesValuesTest");
    invoke();
  }
}
