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

package com.sun.ts.tests.jaxrs.api.rs.bindingpriority;

import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = 1501029701397272718L;

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
   * @testName: checkBindingPriorityHigherRegisteredFirstTest
   * 
   * @assertion_ids: JAXRS:SPEC:92;
   * 
   * @test_Strategy: Priority defined for a filter or interceptor.
   */
  public void checkBindingPriorityHigherRegisteredFirstTest() throws Fault {
    AtomicInteger ai = new AtomicInteger(0);
    ContextProvider lowerProiority = new LowerPriorityProvider(ai);
    ContextProvider higherPriority = new HigherPriorityProvider(ai);
    Response response = invokeWithClientRequestFilters(higherPriority,
        lowerProiority);
    assertFault(response.getStatus() == Status.OK.getStatusCode(),
        "returned status", response.getStatus());
  }

  /*
   * @testName: checkBindingPriorityLowerRegisteredFirstTest
   * 
   * @assertion_ids: JAXRS:SPEC:92;
   * 
   * @test_Strategy: Priority defined for a filter or interceptor.
   */
  public void checkBindingPriorityLowerRegisteredFirstTest() throws Fault {
    AtomicInteger ai = new AtomicInteger(0);
    ContextProvider lowerProiority = new LowerPriorityProvider(ai);
    ContextProvider higherPriority = new HigherPriorityProvider(ai);
    Response response = invokeWithClientRequestFilters(lowerProiority,
        higherPriority);
    assertFault(response.getStatus() == Status.OK.getStatusCode(),
        "returned status", response.getStatus());
  }

  //////////////////////////////////////////////////////////////////////

  protected Response invokeWithClientRequestFilters(
      ClientRequestFilter... filters) {
    Client client = ClientBuilder.newClient();
    for (ClientRequestFilter filter : filters)
      client.register(filter);
    WebTarget target = client.target("http://nourl/");
    Response response = target.request().buildGet().invoke();
    return response;
  }

}
