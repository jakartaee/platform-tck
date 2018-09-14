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

package com.sun.ts.tests.jaxrs.servlet3.rs.core.streamingoutput;

import javax.ws.rs.core.Response.Status;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {
  private static final long serialVersionUID = 1L;

  public static final String _root = "/jaxrs_ee_core_streamoutput_web/StreamOutputTest";

  public JAXRSClient() {
    setContextRoot(_root);
  }

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    JAXRSClient theTests = new JAXRSClient();
    theTests.run(args);
  }

  /* Run test */

  /*
   * @testName: writeTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:173;
   * 
   * @test_Strategy: Client send a request. Verify that
   * StreamingOutput.write(OutputStream) works.
   */
  public void writeTest() throws Fault {
    setProperty(REQUEST, buildRequest(GET, "Test1"));
    setProperty(SEARCH_STRING, "StreamingOutputTest1");
    invoke();
  }

  /*
   * @testName: writeIOExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:174; JAXRS:JAVADOC:132;
   * 
   * @test_Strategy: Client send a request. Verify that
   * StreamingOutput.write(OutputStream) throws IOException (Servlet container
   * shall return 500 - ResponseBuilder responsibility).
   */
  public void writeIOExceptionTest() throws Fault {
    setProperty(REQUEST, buildRequest(GET, "IOExceptionTest"));
    setProperty(STATUS_CODE, getStatusCode(Status.INTERNAL_SERVER_ERROR));
    invoke();
  }

  /*
   * @testName: writeWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:175;
   * 
   * @test_Strategy: Client send a request. Verify that
   * StreamingOutput.write(OutputStream) throws WebApplicationException works.
   */
  public void writeWebApplicationExceptionTest() throws Fault {
    setProperty(REQUEST, buildRequest(GET, "Test2"));
    setProperty(STATUS_CODE, getStatusCode(Status.NOT_FOUND));
    invoke();
  }
}
