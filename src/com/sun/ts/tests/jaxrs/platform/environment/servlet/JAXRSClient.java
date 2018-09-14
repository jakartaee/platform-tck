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

package com.sun.ts.tests.jaxrs.platform.environment.servlet;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */

public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = 1L;

  public JAXRSClient() {
    setContextRoot("/jaxrs_platform_environment_servlet_web/resource");
  }

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

  /* Run test */
  /*
   * @testName: checkServletExtensionTest
   * 
   * @assertion_ids: JAXRS:SPEC:41; JAXRS:SPEC:42;
   * 
   * @test_Strategy: The @Context annotation can be used to indicate a
   * dependency on a Servlet-defined resource.
   * 
   * A Servlet-based implementation MUST support injection of the following
   * Servlet-defined types: ServletConfig, ServletContext, HttpServletRequest
   * and HttpServletResponse.
   */
  public void checkServletExtensionTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "context"));
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH, "is null");
    invoke();
  }

  /*
   * @testName: streamReaderRequestEntityTest
   * 
   * @assertion_ids: JAXRS:SPEC:43;
   * 
   * @test_Strategy: An injected HttpServletRequest allows a resource method to
   * stream the contents of a request entity.
   */
  public void streamReaderRequestEntityTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.POST, "streamreader"));
    setProperty(Property.CONTENT, Resource.class.getName());
    setProperty(Property.SEARCH_STRING, Resource.class.getName());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH, "empty");
    invoke();
  }

  /*
   * @testName: prematureHttpServletResponseTest
   * 
   * @assertion_ids: JAXRS:SPEC:44;
   * 
   * @test_Strategy: An injected HttpServletResponse allows a resource method to
   * commit the HTTP response prior to returning. An implementation MUST check
   * the committed status and only process the return value if the response is
   * not yet committed.
   */
  public void prematureHttpServletResponseTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "premature"));
    invoke();
  }

  /*
   * @testName: servletFilterRequestConsumptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:45; JAXRS:SPEC:46;
   * 
   * @test_Strategy: Servlet filters may trigger consumption of a request body
   * by accessing request parameters.
   * 
   * In a servlet container the @FormParam annotation and the standard entity
   * provider for application/x-www-form--urlencoded MUST obtain their values
   * from the servlet request parameters if the request body has already been
   * consumed.
   */
  public void servletFilterRequestConsumptionTest() throws Fault {
    String content = "ENTITY";
    setProperty(Property.REQUEST_HEADERS,
        "Content-type:" + ConsumingFilter.CONTENTTYPE);
    setProperty(Property.CONTENT, "entity=" + content);
    setProperty(Property.REQUEST, buildRequest(Request.POST, "consume"));
    setProperty(Property.SEARCH_STRING, content);
    invoke();
  }
}
