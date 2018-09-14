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
package com.sun.ts.tests.servlet.pluggability.api.javax_servlet.registration;

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

    setContextRoot("/servlet_plu_registration_web");
    setServletName("TestServlet");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /* Run test */
  /*
   * @testName: servletURLMappingTest
   *
   * @assertion_ids: Servlet:JAVADOC:676; Servlet:JAVADOC:694;
   * Servlet:JAVADOC:696; Servlet:JAVADOC:697;
   *
   * @test_Strategy: Create a ServletContextListener, in which, 1. Add the
   * Servlet ServletContext.addServlet(String, Class), 2. mapping the new
   * Servlet programmatically. 3. Store information returned in
   * ServletContextAttribute by javax.servlet.ServletRegistration.getMappings 4.
   * client send a request to another servlet to get the information Verify in
   * client that getMapping works
   */
  public void servletURLMappingTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "servletURLMappingTest");
    TEST_PROPS.setProperty(UNORDERED_SEARCH_STRING,
        "URL_MAPPING_TEST" + "|/ADDSERVLETCLASS" + "|/SECONDADDSERVLETCLASS"
            + "|/THIRDADDSERVLETCLASS" + "|/ADDSERVLETCLASS/*");
    invoke();
  }

  /*
   * @testName: filterServletMappingTest
   *
   * @assertion_ids: Servlet:JAVADOC:243; Servlet:JAVADOC:655;
   * Servlet:JAVADOC:657; Servlet:JAVADOC:668; Servlet:JAVADOC:669;
   * Servlet:JAVADOC:670; Servlet:JAVADOC:677; Servlet:JAVADOC:694;
   *
   * @test_Strategy: Create a ServletContextListener, in which, 1. Create a
   * FilterRegistration ServletContext.addFilter(String, Class) 2. Create
   * another FilterRegistration ServletContext.addFilter(String, String), 3.
   * Create the third FilterRegistration ServletContext.addServlet(String,
   * Filter), 4. Map all FilterRegistration calling addMappingForServletNames 5.
   * Store information in ServletContextAttribute:
   * javax.servlet.FilterRegistration.getServletNameMappings 6. client send a
   * request to another servlet to get the information Verify in client that
   * getServletNameMappings works
   */
  public void filterServletMappingTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "filterServletMappingTest");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "FILTER_SERVLET_MAPPING" + "|AddServletString" + "|AddServletClass"
            + "|AddServletNotFound" + "|CreateServlet");
    invoke();
  }

  /*
   * @testName: servletRegistrationsTest
   *
   * @assertion_ids: Servlet:JAVADOC:674; Servlet:JAVADOC:675;
   * Servlet:JAVADOC:676; Servlet:JAVADOC:681; Servlet:JAVADOC:692;
   * Servlet:JAVADOC:694; Servlet:JAVADOC:696;
   *
   *
   * @test_Strategy: Create a ServletContextListener, in which, 1. Add the
   * Servlet ServletContext.addServlet(String, Class), 2. Add another Servlet
   * ServletContext.addServlet(String, String), 3. Add the third Servlet
   * ServletContext.addServlet(String, Servlet), 4. Define a servlet in web.xml;
   * 5. Store information returned in ServletContextAttribute by
   * javax.servlet.ServletRegistration.getServletRegistrations 6. client send a
   * request to another servlet to get the information Verify in client that
   * getServletRegistrations works
   */
  public void servletRegistrationsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getServletRegistrationsTest");
    TEST_PROPS.setProperty(UNORDERED_SEARCH_STRING,
        "SERVLET_REGISTRATIONS:" + "|ADDSERVLETCLASS" + "|ADDSERVLETNOTFOUND"
            + "|ADDSERVLETSTRING" + "|CREATESERVLET" + "|TESTSERVLET");
    invoke();
  }

  /*
   * @testName: getServletRegistrationTest
   *
   * @assertion_ids: Servlet:JAVADOC:674; Servlet:JAVADOC:675;
   * Servlet:JAVADOC:676; Servlet:JAVADOC:681; Servlet:JAVADOC:691;
   * Servlet:JAVADOC:694; Servlet:JAVADOC:696;
   *
   * @test_Strategy: Create a ServletContextListener, in which, 1. Add the
   * Servlet ServletContext.addServlet(String, Class), 2. Add another Servlet
   * ServletContext.addServlet(String, String), 3. Add the third Servlet
   * ServletContext.addServlet(String, Servlet), 4. Define a servlet in web.xml;
   * 5. Store information returned in ServletContextAttribute by
   * javax.servlet.ServletRegistration.getServletRegistration(String) 6. client
   * send a request to another servlet to get the information Verify in client
   * that getServletRegistration(String) works
   */
  public void getServletRegistrationTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getServletRegistrationTest");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "SERVLET_REGISTRATION:" + "|ADDSERVLETSTRING" + "|ADDSERVLETCLASS"
            + "|ADDSERVLETNOTFOUND" + "|CREATESERVLET" + "|TESTSERVLET");
    invoke();
  }

  /*
   * @testName: getFilterRegistrationsTest
   *
   * @assertion_ids: Servlet:JAVADOC:674; Servlet:JAVADOC:675;
   * Servlet:JAVADOC:676; Servlet:JAVADOC:681; Servlet:JAVADOC:689;
   * Servlet:JAVADOC:694; Servlet:JAVADOC:696;
   *
   *
   * @test_Strategy: Create a ServletContextListener, in which, 1. Add the
   * Servlet ServletContext.addServlet(String, Class), 2. Add another Servlet
   * ServletContext.addServlet(String, String), 3. Add the third Servlet
   * ServletContext.addServlet(String, Servlet), 4. Define a servlet in web.xml;
   * 5. Store information returned in ServletContextAttribute by
   * javax.servlet.ServletRegistration.getFilterRegistrations 6. client send a
   * request to another servlet to get the information Verify in client that
   * getFilterRegistrations works
   */
  public void getFilterRegistrationsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getFilterRegistrationsTest");
    TEST_PROPS.setProperty(UNORDERED_SEARCH_STRING,
        "FILTER_REGISTRATIONS:" + "|AddFilterClass" + "|AddFilterNotFound"
            + "|AddFilterString" + "|CreateFilter");
    invoke();
  }

  /*
   * @testName: getFilterRegistrationTest
   *
   * @assertion_ids: Servlet:JAVADOC:674; Servlet:JAVADOC:675;
   * Servlet:JAVADOC:676; Servlet:JAVADOC:681; Servlet:JAVADOC:688;
   * Servlet:JAVADOC:694; Servlet:JAVADOC:696;
   *
   * @test_Strategy: Create a ServletContextListener, in which, 1. Add the
   * Servlet ServletContext.addServlet(String, Class), 2. Add another Servlet
   * ServletContext.addServlet(String, String), 3. Add the third Servlet
   * ServletContext.addServlet(String, Servlet), 4. Define a servlet in web.xml;
   * 5. Store information returned in ServletContextAttribute by
   * javax.servlet.ServletRegistration.getFilterRegistration(String) 6. client
   * send a request to another servlet to get the information Verify in client
   * that getFilterRegistration(String) works
   */
  public void getFilterRegistrationTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getFilterRegistrationTest");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "FILTER_REGISTRATION:" + "|AddFilterString" + "|AddFilterClass"
            + "|AddFilterNotFound" + "|CreateFilter");
    invoke();
  }

  /*
   * @testName: getRegistrationNameTest
   *
   * @assertion_ids: Servlet:JAVADOC:243; Servlet:JAVADOC:662;
   * Servlet:JAVADOC:668; Servlet:JAVADOC:669; Servlet:JAVADOC:670;
   * Servlet:JAVADOC:674; Servlet:JAVADOC:675; Servlet:JAVADOC:676;
   * Servlet:JAVADOC:677; Servlet:JAVADOC:681; Servlet:JAVADOC:694;
   *
   * @test_Strategy: Create a ServletContextListener, in which, 1. Create a
   * ServletRegistration ServletContext.addServlet(String, Class) 2. Create
   * another ServletRegistration ServletContext.addServlet(String, String), 3.
   * Create the third ServletRegistration ServletContext.addServlet(String,
   * Servlet), 4. Create a FilterRegistration ServletContext.addFilter(String,
   * Class) 5. Create another FilterRegistration
   * ServletContext.addFilter(String, String), 6. Create the third
   * FilterRegistration ServletContext.addFilter(String, Filter), 7. Store
   * information returned in ServletContextAttribute by
   * javax.servlet.Registration.getName() 8. client send a request to another
   * servlet to get the information Verify in client that
   * javax.servlet.Registration.getName() works
   */
  public void getRegistrationNameTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRegistrationNameTest");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "REGISTRION_NAME:" + "|ADDSERVLETSTRING" + "|ADDFILTERSTRING"
            + "|ADDSERVLETCLASS" + "|ADDFILTERCLASS" + "|CREATESERVLET"
            + "|CREATEFILTER" + "|ADDSERVLETNOTFOUND" + "|ADDFILTERNOTFOUND");
    invoke();
  }

  /*
   * @testName: getRegistrationClassNameTest
   *
   * @assertion_ids: Servlet:JAVADOC:243; Servlet:JAVADOC:659;
   * Servlet:JAVADOC:668; Servlet:JAVADOC:669; Servlet:JAVADOC:670;
   * Servlet:JAVADOC:674; Servlet:JAVADOC:675; Servlet:JAVADOC:676;
   * Servlet:JAVADOC:677; Servlet:JAVADOC:681; Servlet:JAVADOC:694;
   *
   * @test_Strategy: Create a ServletContextListener, in which, 1. Create a
   * ServletRegistration ServletContext.addServlet(String, Class) 2. Create
   * another ServletRegistration ServletContext.addServlet(String, String), 3.
   * Create the third ServletRegistration ServletContext.addServlet(String,
   * Servlet), 4. Create a FilterRegistration ServletContext.addFilter(String,
   * Class) 5. Create another FilterRegistration
   * ServletContext.addFilter(String, String), 6. Create the third
   * FilterRegistration ServletContext.addFilter(String, Filter), 7. Store
   * information returned in ServletContextAttribute by
   * javax.servlet.Registration.getClassName() 8. client send a request to
   * another servlet to get the information Verify in client that
   * javax.servlet.Registration.getClassName() works
   */
  public void getRegistrationClassNameTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRegistrationClassNameTest");
    TEST_PROPS.setProperty(SEARCH_STRING, "REGISTRION_CLASS_NAME:"
        + "|COM.SUN.TS.TESTS.SERVLET.API.JAVAX_SERVLET.SERVLETCONTEXT30.ADDSERVLETSTRING"
        + "|COM.SUN.TS.TESTS.SERVLET.API.JAVAX_SERVLET.SERVLETCONTEXT30.ADDFILTERSTRING"
        + "|COM.SUN.TS.TESTS.SERVLET.API.JAVAX_SERVLET.SERVLETCONTEXT30.ADDSERVLETCLASS"
        + "|COM.SUN.TS.TESTS.SERVLET.API.JAVAX_SERVLET.SERVLETCONTEXT30.ADDFILTERCLASS"
        + "|COM.SUN.TS.TESTS.SERVLET.API.JAVAX_SERVLET.SERVLETCONTEXT30.CREATESERVLET"
        + "|COM.SUN.TS.TESTS.SERVLET.API.JAVAX_SERVLET.SERVLETCONTEXT30.CREATEFILTER"
        + "|COM.SUN.TS.TESTS.SERVLET.API.JAVAX_SERVLET.SERVLETCONTEXT30.ADDSERVLETNOTFOUND"
        + "|COM.SUN.TS.TESTS.SERVLET.API.JAVAX_SERVLET.SERVLETCONTEXT30.ADDFILTERNOTFOUND");
    invoke();
  }

  /*
   * @testName: getRegistrationInitParameterTest
   *
   * @assertion_ids: Servlet:JAVADOC:243; Servlet:JAVADOC:660;
   * Servlet:JAVADOC:663; Servlet:JAVADOC:668; Servlet:JAVADOC:669;
   * Servlet:JAVADOC:670; Servlet:JAVADOC:674; Servlet:JAVADOC:675;
   * Servlet:JAVADOC:676; Servlet:JAVADOC:677; Servlet:JAVADOC:681;
   * Servlet:JAVADOC:694;
   *
   * @test_Strategy: Create a ServletContextListener, in which, 1. Create a
   * ServletRegistration ServletContext.addServlet(String, Class) 2. Create
   * another ServletRegistration ServletContext.addServlet(String, String), 3.
   * Create the third ServletRegistration ServletContext.addServlet(String,
   * Servlet), 4. Create a FilterRegistration ServletContext.addFilter(String,
   * Class) 5. Create another FilterRegistration
   * ServletContext.addFilter(String, String), 6. Create the third
   * FilterRegistration ServletContext.addFilter(String, Filter), 7. Call
   * setInitParameter(String, String) on all above Registratyion Object; 8.
   * Store information returned in ServletContextAttribute by
   * javax.servlet.Registration.getInitParameter(String) 9. client send a
   * request to another servlet to get the information Verify in client that
   * javax.servlet.Registration.getInitParameter(String) works
   */
  public void getRegistrationInitParameterTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRegistrationInitParameterTest");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "REGISTRATION_INIT_PARAMETER:" + "|AddFilterString"
            + "|AddServletString" + "|AddFilterClass" + "|AddServletClass"
            + "|CreateFilter" + "|CreateServlet" + "|AddFilterNotFound"
            + "|AddServletNotFound");
    invoke();
  }

  /*
   * @testName: getRegistrationInitParametersTest
   *
   * @assertion_ids: Servlet:JAVADOC:243; Servlet:JAVADOC:661;
   * Servlet:JAVADOC:664; Servlet:JAVADOC:668; Servlet:JAVADOC:669;
   * Servlet:JAVADOC:670; Servlet:JAVADOC:674; Servlet:JAVADOC:675;
   * Servlet:JAVADOC:676; Servlet:JAVADOC:677; Servlet:JAVADOC:681;
   * Servlet:JAVADOC:694;
   *
   * @test_Strategy: Create a ServletContextListener, in which, 1. Create a
   * ServletRegistration ServletContext.addServlet(String, Class) 2. Create
   * another ServletRegistration ServletContext.addServlet(String, String), 3.
   * Create the third ServletRegistration ServletContext.addServlet(String,
   * Servlet), 4. Create a FilterRegistration ServletContext.addFilter(String,
   * Class) 5. Create another FilterRegistration
   * ServletContext.addFilter(String, String), 6. Create the third
   * FilterRegistration ServletContext.addFilter(String, Filter), 7. Call
   * setInitParameters(Map) on all above Registratyion Object; 8. Store
   * information returned in ServletContextAttribute by
   * javax.servlet.Registration.getInitParameters() 9. client send a request to
   * another servlet to get the information Verify in client that
   * javax.servlet.Registration.getInitParameters() works
   */
  public void getRegistrationInitParametersTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRegistrationInitParametersTest");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "REGISTRATION_INIT_PARAMETERS:"
            + "|Filter=No|FilterName=AddFilterString|Servlet=Yes"
            + "|DISPATCH=FORWARD|ServletName=AddServletString"
            + "|Filter=Yes|FilterName=AddFilterString|Servlet=No"
            + "|DISPATCH=FORWARD|ServletName=AddServletString"
            + "|Filter=No|FilterName=AddFilterClass|Servlet=Yes"
            + "|DISPATCH=REQUEST|ServletName=AddServletClass"
            + "|Filter=Yes|FilterName=AddFilterClass|Servlet=No"
            + "|DISPATCH=REQUEST|ServletName=AddServletClass"
            + "|Filter=No|FilterName=CreateFilter|Servlet=Yes"
            + "|DISPATCH=REQUEST|ServletName=CreateServlet"
            + "|Filter=Yes|FilterName=CreateFilter|Servlet=No"
            + "|DISPATCH=REQUEST|ServletName=CreateServlet"
            + "|Filter=No|FilterName=AddFilterNotFound|Servlet=Yes"
            + "|DISPATCH=ALL|ServletName=AddServletNotFound"
            + "|Filter=Yes|FilterName=AddFilterNotFound|Servlet=No"
            + "|DISPATCH=ALL|ServletName=AddServletNotFound");
    invoke();
  }
}
