/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.platform.servletapp2;

import javax.ws.rs.core.MediaType;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = -7156949059075869559L;

  public JAXRSClient() {
    setContextRoot("/jaxrs_platform_servletapp2_web");
  }

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /* Run test */
  /*
   * @testName: test1
   * 
   * @assertion_ids: JAXRS:SPEC:23; JAXRS:SPEC:48; JAXRS:SPEC:55; JAXRS:SPEC:58;
   * 
   * @test_Strategy: Create Application subclass annotated
   * with @ApplicationPath; Create servlet-name with fully qualified Application
   * subclass name in web.xml with servlet-mapping; Package all resource in
   * web.war file; Client sends a request on a resource at /InheritanceTest,
   * Verify that inheritance works; Verify deploy JAX-RS resource as Servlet
   * application with Application works; Verify servlet-mapping
   * overrides @ApplicationPath;
   */
  public void test1() throws Fault {
    setProperty(Property.REQUEST_HEADERS,
        buildAccept(MediaType.TEXT_PLAIN_TYPE));
    setProperty(Property.REQUEST,
        buildRequest(Request.GET, "ServletApp2/InheritanceTest"));
    setProperty(Property.SEARCH_STRING, "First");
    invoke();
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: JAXRS:SPEC:24; JAXRS:SPEC:48; JAXRS:SPEC:55; JAXRS:SPEC:58;
   * 
   * @test_Strategy: Create Application subclass annotated
   * with @ApplicationPath; Create servlet-name with fully qualified Application
   * subclass name in web.xml with servlet-mapping; Package all resource in
   * web.war file; Client sends a request on a resource at /InheritanceTest1,
   * Verify that inheritance works. Verify deploy JAX-RS resource as Servlet
   * application with Application works; Verify servlet-mapping
   * overrides @ApplicationPath;
   */
  public void test2() throws Fault {
    setProperty(Property.REQUEST_HEADERS,
        buildAccept(MediaType.TEXT_HTML_TYPE));
    setProperty(Property.REQUEST,
        buildRequest(Request.GET, "ServletApp2/InheritanceTest1"));
    setProperty(Property.SEARCH_STRING, "Second");
    invoke();
  }
}
