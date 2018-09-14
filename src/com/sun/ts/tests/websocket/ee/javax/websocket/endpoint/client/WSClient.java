/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.ee.javax.websocket.endpoint.client;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSClient extends WebSocketCommonClient {
  private static final long serialVersionUID = 5511697770152558944L;

  public WSClient() {
    setContextRoot("wsc_ee_endpoint_client_web");
  }

  public static void main(String[] args) {
    new WSClient().run(args);
  }

  /* Run test */

  /*
   * @testName: onErrorWorksTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:68;WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:66;
   * 
   * @test_Strategy: check @OnError works on Endpoint on Client Side
   * javax.websocket.Endpoint.onOpen Endpoint.Endpoint
   */
  public void onErrorWorksTest() throws Fault {
    WSCErrorClientEndpoint endpoint = new WSCErrorClientEndpoint();
    setClientEndpointInstance(endpoint);
    invoke("echo", OPS.ECHO_MSG, OPS.ECHO_MSG);
    assertFalse(endpoint.onErrorCalled,
        "@OnError has been unexpectedly called");

    setCountDownLatchCount(2);
    setClientEndpointInstance(endpoint);
    invoke("echo", OPS.THROW, OPS.THROW);
    assertTrue(endpoint.onErrorCalled,
        "@OnError has NOT been called after RuntimeException is thrown on @OnMessage");
    logMsg(
        "@OnError has been called after RuntimeException is thrown on @OnMessage as expected");
  }

  /*
   * @testName: onCloseWorksTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:67;WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:66;
   * 
   * @test_Strategy: check @OnClose works on Endpoint on Client side
   * javax.websocket.Endpoint.onOpen Endpoint.Endpoint
   */
  public void onCloseWorksTest() throws Fault {
    WSCCloseClientEndpoint endpoint = new WSCCloseClientEndpoint();
    setClientEndpointInstance(endpoint);
    invoke("echo", OPS.ECHO_MSG.name(), OPS.ECHO_MSG.name(), false);
    assertFalse(endpoint.onCloseCalled,
        "@OnClose has been unexpectedly called");
    cleanup();
    endpoint.waitForClose(_ws_wait);
    assertTrue(endpoint.onCloseCalled,
        "@OnClose has NOT been called after session.close()");
    logMsg("@OnClose has been called after session.close() as expected");
  }

}
