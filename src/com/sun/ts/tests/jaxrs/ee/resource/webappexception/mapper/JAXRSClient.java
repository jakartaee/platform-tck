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

package com.sun.ts.tests.jaxrs.ee.resource.webappexception.mapper;

import javax.ws.rs.core.Response.Status;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {
  private static final long serialVersionUID = 1L;

  public JAXRSClient() {
    setContextRoot("/jaxrs_resource_webappexception_mapper_web/resource");
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
   * @testName: noResponseTest
   * 
   * @assertion_ids: JAXRS:SPEC:16; JAXRS:SPEC:16.1; JAXRS:SPEC:16.2;
   * 
   * @test_Strategy: An implementation MUST catch all exceptions and process
   * them as follows: Instances of WebApplicationException MUST be mapped to a
   * response as follows. If the response property of the exception does not
   * contain an entity and an exception mapping provider (see section 4.4) is
   * available for WebApplicationException an implementation MUST use the
   * provider to create a new Response instance, otherwise the response property
   * is used directly.
   */
  public void noResponseTest() throws Fault {
    setProperty(REQUEST, buildRequest(GET, "noresponse"));
    setProperty(STATUS_CODE, getStatusCode(Status.ACCEPTED));
    invoke();
  }

  /*
   * @testName: okResponseTest
   * 
   * @assertion_ids: JAXRS:SPEC:16; JAXRS:SPEC:16.1; JAXRS:SPEC:16.2;
   * 
   * @test_Strategy: An implementation MUST catch all exceptions and process
   * them as follows: Instances of WebApplicationException MUST be mapped to a
   * response as follows. If the response property of the exception does not
   * contain an entity and an exception mapping provider (see section 4.4) is
   * available for WebApplicationException an implementation MUST use the
   * provider to create a new Response instance, otherwise the response property
   * is used directly.
   */
  public void okResponseTest() throws Fault {
    setProperty(REQUEST, buildRequest(GET, "responseok"));
    setProperty(STATUS_CODE, getStatusCode(Status.ACCEPTED));
    invoke();
  }

  /*
   * @testName: responseEntityTest
   * 
   * @assertion_ids: JAXRS:SPEC:16; JAXRS:SPEC:16.1; JAXRS:SPEC:16.2;
   * 
   * @test_Strategy: An implementation MUST catch all exceptions and process
   * them as follows: Instances of WebApplicationException MUST be mapped to a
   * response as follows. If the response property of the exception does not
   * contain an entity and an exception mapping provider (see section 4.4) is
   * available for WebApplicationException an implementation MUST use the
   * provider to create a new Response instance, otherwise the response property
   * is used directly.
   * 
   * The ExceptionMapper is omitted
   */
  public void responseEntityTest() throws Fault {
    setProperty(REQUEST, buildRequest(GET, "responseentity"));
    setProperty(Property.SEARCH_STRING, Resource.class.getSimpleName());
    invoke();
  }

  /*
   * @testName: statusOkResponseTest
   * 
   * @assertion_ids: JAXRS:SPEC:16; JAXRS:SPEC:16.1; JAXRS:SPEC:16.2;
   * 
   * @test_Strategy: An implementation MUST catch all exceptions and process
   * them as follows: Instances of WebApplicationException MUST be mapped to a
   * response as follows. If the response property of the exception does not
   * contain an entity and an exception mapping provider (see section 4.4) is
   * available for WebApplicationException an implementation MUST use the
   * provider to create a new Response instance, otherwise the response property
   * is used directly.
   */
  public void statusOkResponseTest() throws Fault {
    setProperty(REQUEST, buildRequest(GET, "responsestatusok"));
    setProperty(STATUS_CODE, getStatusCode(Status.ACCEPTED));
    invoke();
  }

  /*
   * @testName: statusIntOkResponseTest
   * 
   * @assertion_ids: JAXRS:SPEC:16; JAXRS:SPEC:16.1; JAXRS:SPEC:16.2;
   * 
   * @test_Strategy: An implementation MUST catch all exceptions and process
   * them as follows: Instances of WebApplicationException MUST be mapped to a
   * response as follows. If the response property of the exception does not
   * contain an entity and an exception mapping provider (see section 4.4) is
   * available for WebApplicationException an implementation MUST use the
   * provider to create a new Response instance, otherwise the response property
   * is used directly.
   */
  public void statusIntOkResponseTest() throws Fault {
    setProperty(REQUEST, buildRequest(GET, "responsestatusintok"));
    setProperty(STATUS_CODE, getStatusCode(Status.ACCEPTED));
    invoke();
  }

  /*
   * @testName: throwableResponseTest
   * 
   * @assertion_ids: JAXRS:SPEC:16; JAXRS:SPEC:16.1; JAXRS:SPEC:16.2;
   * 
   * @test_Strategy: An implementation MUST catch all exceptions and process
   * them as follows: Instances of WebApplicationException MUST be mapped to a
   * response as follows. If the response property of the exception does not
   * contain an entity and an exception mapping provider (see section 4.4) is
   * available for WebApplicationException an implementation MUST use the
   * provider to create a new Response instance, otherwise the response property
   * is used directly.
   */
  public void throwableResponseTest() throws Fault {
    setProperty(REQUEST, buildRequest(GET, "responsethrowable"));
    setProperty(STATUS_CODE, getStatusCode(Status.ACCEPTED));
    invoke();
  }

  /*
   * @testName: throwableOkResponseTest
   * 
   * @assertion_ids: JAXRS:SPEC:16; JAXRS:SPEC:16.1; JAXRS:SPEC:16.2;
   * 
   * @test_Strategy: An implementation MUST catch all exceptions and process
   * them as follows: Instances of WebApplicationException MUST be mapped to a
   * response as follows. If the response property of the exception does not
   * contain an entity and an exception mapping provider (see section 4.4) is
   * available for WebApplicationException an implementation MUST use the
   * provider to create a new Response instance, otherwise the response property
   * is used directly.
   */
  public void throwableOkResponseTest() throws Fault {
    setProperty(REQUEST, buildRequest(GET, "responsestatusthrowableok"));
    setProperty(STATUS_CODE, getStatusCode(Status.ACCEPTED));
    invoke();
  }

  /*
   * @testName: throwableIntOkResponseTest
   * 
   * @assertion_ids: JAXRS:SPEC:16; JAXRS:SPEC:16.1; JAXRS:SPEC:16.2;
   * 
   * @test_Strategy: An implementation MUST catch all exceptions and process
   * them as follows: Instances of WebApplicationException MUST be mapped to a
   * response as follows. If the response property of the exception does not
   * contain an entity and an exception mapping provider (see section 4.4) is
   * available for WebApplicationException an implementation MUST use the
   * provider to create a new Response instance, otherwise the response property
   * is used directly.
   */
  public void throwableIntOkResponseTest() throws Fault {
    setProperty(REQUEST, buildRequest(GET, "responsestatusthrowableintok"));
    setProperty(STATUS_CODE, getStatusCode(Status.ACCEPTED));
    invoke();
  }

  /*
   * @testName: throwUncheckedExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:16; JAXRS:SPEC:16.3;
   * 
   * @test_Strategy: Unchecked exceptions and errors MUST be re-thrown and
   * allowed to propagate to the underlying container..
   */
  public void throwUncheckedExceptionTest() throws Fault {
    setProperty(REQUEST, buildRequest(GET, "uncheckedexception"));
    setProperty(STATUS_CODE, getStatusCode(Status.NOT_ACCEPTABLE));
    invoke();
  }

  /*
   * @testName: webApplicationExceptionHasResponseWithEntityDoesNotUseMapperTest
   * 
   * @assertion_ids: JAXRS:SPEC:16; JAXRS:SPEC:16.1;
   * 
   * @test_Strategy: An implementation MUST catch all exceptions and process
   * them as follows: Instances of WebApplicationException MUST be mapped to a
   * response as follows. If the response property of the exception does not
   * contain an entity and an exception mapping provider (see section 4.4) is
   * available for WebApplicationException an implementation MUST use the
   * provider to create a new Response instance, otherwise the response property
   * is used directly.
   */
  public void webApplicationExceptionHasResponseWithEntityDoesNotUseMapperTest()
      throws Fault {
    int[] codes = { 2000, 4000, 400, 401, 403, 404, 405, 406, 415, 3000, 5000,
        500, 503 };
    for (int i = 0; i != codes.length; i++) {
      setProperty(Property.REQUEST,
          buildRequest(Request.GET, "direct/" + codes[i]));
      setProperty(Property.STATUS_CODE,
          String.valueOf(codes[i] > 1000 ? codes[i] / 10 : codes[i]));
      setProperty(Property.SEARCH_STRING,
          codes[i] > 1000 ? DirectResponseUsageResource.ENTITY
              : DirectResponseUsageResource.getReasonPhrase(codes[i]));
      invoke();
    }
  }

  /*
   * @testName: webApplicationExceptionHasResponseWithoutEntityDoesUseMapperTest
   * 
   * @assertion_ids: JAXRS:SPEC:16; JAXRS:SPEC:16.1;
   * 
   * @test_Strategy: An implementation MUST catch all exceptions and process
   * them as follows: Instances of WebApplicationException MUST be mapped to a
   * response as follows. If the response property of the exception does not
   * contain an entity and an exception mapping provider (see section 4.4) is
   * available for WebApplicationException an implementation MUST use the
   * provider to create a new Response instance, otherwise the response property
   * is used directly.
   */
  public void webApplicationExceptionHasResponseWithoutEntityDoesUseMapperTest()
      throws Fault {
    int[] codes = { 4000, 400, 401, 403, 404, 405, 406, 415, 3000, 5000, 500,
        503 };
    for (int i = 0; i != codes.length; i++) {
      setProperty(Property.REQUEST,
          buildRequest(Request.GET, "noentity/" + codes[i]));
      setProperty(Property.STATUS_CODE, getStatusCode(Status.FOUND));
      invoke();
    }
  }
}
