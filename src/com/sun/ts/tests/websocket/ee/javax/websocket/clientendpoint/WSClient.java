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

package com.sun.ts.tests.websocket.ee.javax.websocket.clientendpoint;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.impl.ClientConfigurator;
import com.sun.ts.tests.websocket.common.util.StringUtil;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSClient extends WebSocketCommonClient {
  private static final long serialVersionUID = 5512614247407170347L;

  public WSClient() {
    setContextRoot("wsc_ee_clientendpoint_web");
  }

  public static void main(String[] args) {
    new WSClient().run(args);
  }

  /* Run test */
  /*
   * @testName: subprotocolsMatchTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:4;
   * 
   * @test_Strategy: ClientEndpoint.subprotocols
   *
   * when there is a subprotocol match, the server responds
   */
  public void subprotocolsMatchTest() throws Fault {
    WSCMatchedSubprotocolClientEndpoint endpoint = new WSCMatchedSubprotocolClientEndpoint();
    setAnnotatedClientEndpoint(endpoint);
    invoke("subprotocol", OPS.NEGOTIATED, StringUtil.WEBSOCKET_SUBPROTOCOLS_1);
  }

  /*
   * @testName: subprotocolsNotMatchTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:4;
   * 
   * @test_Strategy: ClientEndpoint.subprotocols when there is NOT a subprotocol
   * match, the server responds as well
   */
  public void subprotocolsNotMatchTest() throws Fault {
    WSCUnmatchedSubprotocolClientEndpoint endpoint = new WSCUnmatchedSubprotocolClientEndpoint();
    setAnnotatedClientEndpoint(endpoint);
    invoke("subprotocol", OPS.NEGOTIATED, "{}");
  }

  /*
   * @testName: configuratorTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:1; WebSocket:JAVADOC:5;
   * WebSocket:JAVADOC:15; WebSocket:JAVADOC:16; WebSocket:JAVADOC:17;
   * 
   * @test_Strategy: ClientEndpoint.configurator
   * ClientEndpointConfig.getConfigurator
   * ClientEndpointConfig.Configurator.afterResponse
   * ClientEndpointConfig.Configurator.beforeRequest
   * ClientEndpointConfig.Configurator.ClientEndpointConfig.Configurator
   */
  public void configuratorTest() throws Fault {
    WSCConfiguratedClientEndpoint endpoint = new WSCConfiguratedClientEndpoint();
    setAnnotatedClientEndpoint(endpoint);
    invoke("echo", OPS.ECHO_MSG, OPS.ECHO_MSG);
    ClientConfigurator configurator = ClientConfiguratorHolderClientConfigurator
        .getConfigurator();
    configurator.assertBeforeRequestHasBeenCalled();
    configurator.assertAfterResponseHasBeenCalled();
  }

  /*
   * @testName: onErrorWorksTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:1; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-2.2.4-1;
   * 
   * @test_Strategy: check @OnError works on Client Endpoint
   * 
   * The method level @OnOpen and @OnClose annotations allow the developers to
   * decorate methods on their
   * 
   * @ServerEndpoint annotated Java class to specify that they must be called by
   * the implementation when the resulting endpoint receives a new connection
   * from a peer or when a connection from a peer is closed, respectively.
   * 
   * @OnMessage annotation allows the developer to indicate which methods the
   * implementation must call when a message is received.
   * 
   * @OnError annotation to mark one of its methods must be called by the
   * implementation with information about the error whenever such an error
   * occurs
   */
  public void onErrorWorksTest() throws Fault {
    WSCErrorClientEndpoint endpoint = new WSCErrorClientEndpoint();
    setAnnotatedClientEndpoint(endpoint);
    invoke("echo", OPS.ECHO_MSG, OPS.ECHO_MSG);
    assertFalse(endpoint.onErrorCalled,
        "@OnError has been unexpectedly called");

    setCountDownLatchCount(2);
    setAnnotatedClientEndpoint(endpoint);
    invoke("echo", OPS.THROW, OPS.THROW);
    assertTrue(endpoint.onErrorCalled,
        "@OnError has NOT been called after RuntimeException is thrown on @OnMessage");
    logMsg(
        "@OnError has been called after RuntimeException is thrown on @OnMessage as expected");
  }

  /*
   * @testName: onCloseWorksTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:1; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1;
   * 
   * @test_Strategy: check @OnClose works on Client Endpoint
   * 
   * The method level @OnOpen and @OnClose annotations allow the developers to
   * decorate methods on their
   * 
   * @ServerEndpoint annotated Java class to specify that they must be called by
   * the implementation when the resulting endpoint receives a new connection
   * from a peer or when a connection from a peer is closed, respectively.
   * 
   * @OnMessage annotation allows the developer to indicate which methods the
   * implementation must call when a message is received.
   */
  public void onCloseWorksTest() throws Fault {
    WSCCloseClientEndpoint endpoint = new WSCCloseClientEndpoint();
    setAnnotatedClientEndpoint(endpoint);
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
