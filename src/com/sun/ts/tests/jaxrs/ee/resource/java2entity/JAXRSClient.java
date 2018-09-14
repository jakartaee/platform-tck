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

package com.sun.ts.tests.jaxrs.ee.resource.java2entity;

import javax.ws.rs.core.Response;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = 1L;

  public JAXRSClient() {
    setContextRoot("/jaxrs_resource_java2entity_web/resource");
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
   * @testName: directClassTypeTest
   * 
   * @assertion_ids: JAXRS:SPEC:15; JAXRS:SPEC:15.1; JAXRS:SPEC:15.2;
   * JAXRS:SPEC:15.3; JAXRS:SPEC:15.4;
   * 
   * @test_Strategy: Other | Return type or subclass | Class of instance |
   * Generic type of return type
   */
  public void directClassTypeTest() throws Fault {
    setProperty(REQUEST, buildRequest(GET, "linkedlist"));
    setProperty(SEARCH_STRING, Response.Status.OK.name());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH,
        IncorrectCollectionWriter.ERROR);
    invoke();
  }

  /*
   * @testName: responseDirectClassTypeTest
   * 
   * @assertion_ids: JAXRS:SPEC:15; JAXRS:SPEC:15.1; JAXRS:SPEC:15.2;
   * JAXRS:SPEC:15.3; JAXRS:SPEC:15.4;
   * 
   * @test_Strategy: Response | Object or subclass | Class of instance | Class
   * of instance
   */
  public void responseDirectClassTypeTest() throws Fault {
    setProperty(REQUEST, buildRequest(GET, "response/linkedlist"));
    setProperty(SEARCH_STRING, Response.Status.OK.name());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH,
        IncorrectCollectionWriter.ERROR);
    invoke();
  }

  /*
   * @testName: responseGenericEntityTest
   * 
   * @assertion_ids: JAXRS:SPEC:15; JAXRS:SPEC:15.1; JAXRS:SPEC:15.2;
   * JAXRS:SPEC:15.3; JAXRS:SPEC:15.4;
   * 
   * @test_Strategy: Response | GenericEntity or subclass | RawType property |
   * Type property
   */
  public void responseGenericEntityTest() throws Fault {
    setProperty(REQUEST,
        buildRequest(GET, "response/genericentity/linkedlist"));
    setProperty(SEARCH_STRING, Response.Status.OK.name());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH,
        IncorrectCollectionWriter.ERROR);
    invoke();
  }

  /*
   * @testName: genericEntityTest
   * 
   * @assertion_ids: JAXRS:SPEC:15; JAXRS:SPEC:15.1; JAXRS:SPEC:15.2;
   * JAXRS:SPEC:15.3; JAXRS:SPEC:15.4;
   * 
   * @test_Strategy: GenericEntity | GenericEntity or subclass | RawType
   * property | Type property
   */
  public void genericEntityTest() throws Fault {
    setProperty(REQUEST, buildRequest(GET, "genericentity/linkedlist"));
    setProperty(SEARCH_STRING, Response.Status.OK.name());
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH,
        IncorrectCollectionWriter.ERROR);
    invoke();
  }
}
