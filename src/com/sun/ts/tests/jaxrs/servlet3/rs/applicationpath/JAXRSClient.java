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

package com.sun.ts.tests.jaxrs.servlet3.rs.applicationpath;

import javax.ws.rs.core.Response.Status;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {
  public JAXRSClient() {
    setContextRoot("/jaxrs_ee_applicationpath_web");
  }

  private static final long serialVersionUID = 1L;

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    JAXRSClient theTests = new JAXRSClient();
    theTests.run(args);
  }

  /*
   * @testName: applicationPathAnnotationEncodedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:297
   * 
   * @test_Strategy: Check the ApplicationPath annotation on Application works
   * 
   * Note that percent encoded values are allowed in the value, an
   * implementation will recognize such values and will not double encode the
   * '%' character.
   */
  public void applicationPathAnnotationEncodedTest() throws Fault {
    setProperty(Property.REQUEST,
        buildRequest(Request.GET, "ApplicationPath!/Resource"));
    invoke();
  }

  /*
   * @testName: applicationPathAnnotationNotUsedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:297
   * 
   * @test_Strategy: Check the ApplicationPath is used properly
   */
  public void applicationPathAnnotationNotUsedTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "Resource"));
    setProperty(Property.STATUS_CODE, "-1");
    invoke();
    Status status = getResponseStatusCode();
    assertTrue(status != Status.OK && status != Status.NO_CONTENT,
        "unexpected status code received", status);
    logMsg("Received expected status code", status);
  }

}
