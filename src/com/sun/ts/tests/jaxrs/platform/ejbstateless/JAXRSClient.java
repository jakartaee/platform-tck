/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.platform.ejbstateless;

import javax.ws.rs.core.Response.Status;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = -96529594720799580L;

  public JAXRSClient() {
    setContextRoot("/jaxrs_platform_ejbstateless_web/ssb");
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
   * @testName: test1
   * 
   * @assertion_ids: JAXRS:SPEC:51; JAXRS:SPEC:57;
   * 
   * @test_Strategy: Client sends a request on a no-interface stateless EJB root
   * resource located at /ssb; Verify that correct resource method invoked
   */
  public void test1() throws Fault {
    setProperty(REQUEST, buildRequest(Request.GET, ""));
    setProperty(SEARCH_STRING, "jaxrs_platform_ejbstateless_web|ssb");
    setProperty(SEARCH_STRING, "Hello|From|Stateless|EJB|Root");
    invoke();
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: JAXRS:SPEC:51; JAXRS:SPEC:57;
   * 
   * @test_Strategy: Client sends a request on a no-interface stateless EJB root
   * resource located at /ssb/sub; Verify that correct resource method invoked
   */
  public void test2() throws Fault {
    setProperty(REQUEST, buildRequest(Request.GET, "sub"));
    setProperty(SEARCH_STRING, "jaxrs_platform_ejbstateless_web|ssb");
    setProperty(SEARCH_STRING, "Hello|From|Stateless|EJB|Sub");
    invoke();
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: JAXRS:SPEC:51; JAXRS:SPEC:57;
   * 
   * @test_Strategy: Client sends a request on a stateless EJB's local interface
   * root resource located at /ssb/localsub; Verify that correct resource method
   * invoked
   */
  public void test3() throws Fault {
    setProperty(REQUEST, buildRequest(Request.GET, "localsub"));
    setProperty(SEARCH_STRING, "jaxrs_platform_ejbstateless_web|ssb");
    setProperty(SEARCH_STRING, "localsub|Hello|From|Stateless|Local|EJB|Sub");
    invoke();
  }

  /*
   * @testName: ejbExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:52;
   * 
   * @test_Strategy: If an Exception-Mapper for a EJBException or subclass is
   * not included with an application then exceptions thrown by an EJB resource
   * class or provider method MUST be unwrapped and processed as described in
   * Section 3.3.4.
   */
  public void ejbExceptionTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "exception"));
    setProperty(Property.STATUS_CODE, getStatusCode(Status.CREATED));
    invoke();
  }

  /*
   * @testName: jaxrsInjectPriorPostConstructOnRootResourceTest
   * 
   * @assertion_ids: JAXRS:SPEC:53; JAXRS:SPEC:53.1; JAXRS:SPEC:53.3;
   * 
   * @test_Strategy: The following additional requirements apply when using EJBs
   * as resource classes:
   * 
   * Field and property injection of JAX-RS resources MUST be performed prior to
   * the container invoking any
   * 
   * @PostConstruct annotated method
   * 
   * Implementations MUST NOT require use of @Inject or
   * 
   * @Resource to trigger injection of JAX-RS annotated fields or properties.
   * Implementations MAY support such usage but SHOULD warn users about
   * non-portability.
   */
  public void jaxrsInjectPriorPostConstructOnRootResourceTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "priorroot"));
    setProperty(Property.SEARCH_STRING, String.valueOf(true));
    invoke();
  }
}
