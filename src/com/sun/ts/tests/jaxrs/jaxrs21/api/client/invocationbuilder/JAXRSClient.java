/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.jaxrs21.api.client.invocationbuilder;

import javax.ws.rs.client.ClientBuilder;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
/**
 * @since 2.1
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = 21L;

  public JAXRSClient() {
    setContextRoot("/jaxrs21_api_client_invocationbuilder_web/resource");
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
   * @testName: testRxClassGetsClassInstance
   * 
   * @assertion_ids: JAXRS:JAVADOC:1163; JAXRS:JAVADOC:1189;
   * 
   * @test_Strategy: Access a reactive invoker based on provider RxInvoker
   * subclass. Note that corresponding RxInvokerProvider must be registered to
   * client runtime.
   */
  public void testRxClassGetsClassInstance() throws Fault {
    TCKRxInvoker invoker = ClientBuilder.newClient()
        .register(TCKRxInvokerProvider.class).target("somewhere").request()
        .rx(TCKRxInvoker.class);
    assertNotNull(invoker, "rx did not instantiated the invoker");
    assertEquals(invoker.getClass(), TCKRxInvoker.class,
        "Custom rxInvoker has not been created");

    invoker = ClientBuilder.newClient().target("somewhere")
        .register(TCKRxInvokerProvider.class).request().rx(TCKRxInvoker.class);
    assertNotNull(invoker, "rx did not instantiated the invoker");
    assertEquals(invoker.getClass(), TCKRxInvoker.class,
        "Custom rxInvoker has not been created");

    System.out.println("Custom rxInvoker has been created as expected");
  }

  /*
   * @testName: testRxClassThrowsWhenNotRegistered
   * 
   * @assertion_ids: JAXRS:JAVADOC:1163; JAXRS:JAVADOC:1189;
   * 
   * @test_Strategy: Access a reactive invoker based on provider RxInvoker
   * subclass. Note that corresponding RxInvokerProvider must be registered to
   * client runtime.
   */
  public void testRxClassThrowsWhenNotRegistered() throws Fault {
    try {
      ClientBuilder.newClient().target("somewhere").request()
          .rx(TCKRxInvoker.class);
      System.out.println(
          "Illegal state exception has not been thrown when no provider is registered");
    } catch (IllegalStateException e) {
      System.out.println(
          "Illegal state exception has been thrown when no provider is registered as expected");
    }
  }

  /*
   * @testName: testRxClassExceutorServiceGetsClassInstance
   * 
   * @assertion_ids: JAXRS:JAVADOC:1163; JAXRS:JAVADOC:1189;
   * 
   * @test_Strategy: Access a reactive invoker based on provider RxInvoker
   * subclass. Note that corresponding RxInvokerProvider must be registered to
   * client runtime.
   */
  public void testRxClassExceutorServiceGetsClassInstance() throws Fault {
    TCKRxInvoker invoker = ClientBuilder.newClient()
        .register(TCKRxInvokerProvider.class).target("somewhere").request()
        .rx(TCKRxInvoker.class);
    assertNotNull(invoker, "rx did not instantiated the invoker");
    assertEquals(invoker.getClass(), TCKRxInvoker.class,
        "Custom rxInvoker has not been created");

    invoker = ClientBuilder.newClient().target("somewhere")
        .register(TCKRxInvokerProvider.class).request().rx(TCKRxInvoker.class);
    assertNotNull(invoker, "rx did not instantiated the invoker");
    assertEquals(invoker.getClass(), TCKRxInvoker.class,
        "Custom rxInvoker has not been created");

    System.out.println("Custom rxInvoker has been created as expected");
  }

  /*
   * @testName: testRxClassExecutorServiceThrowsWhenNotRegistered
   * 
   * @assertion_ids: JAXRS:JAVADOC:1163; JAXRS:JAVADOC:1189;
   * 
   * @test_Strategy: Access a reactive invoker based on provider RxInvoker
   * subclass. Note that corresponding RxInvokerProvider must be registered to
   * client runtime.
   */
  public void testRxClassExecutorServiceThrowsWhenNotRegistered() throws Fault {
    try {
      ClientBuilder.newClient().target("somewhere").request()
          .rx(TCKRxInvoker.class);
      System.out.println(
          "Illegal state exception has not been thrown when no provider is registered");
    } catch (IllegalStateException e) {
      System.out.println(
          "Illegal state exception has been thrown when no provider is registered as expected");
    }
  }
}
