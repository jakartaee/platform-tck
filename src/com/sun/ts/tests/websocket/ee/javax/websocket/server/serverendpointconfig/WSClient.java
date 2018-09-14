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

package com.sun.ts.tests.websocket.ee.javax.websocket.server.serverendpointconfig;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSClient extends WebSocketCommonClient {

  private static final long serialVersionUID = 6621336154397058231L;

  public WSClient() {
    setContextRoot("wsc_ee_javax_websocket_server_serverendpointconfig_web");

  }

  public static void main(String[] args) {
    new WSClient().run(args);
  }

  /* Run test */

  /*
   * @testName: getConfiguratorTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:193; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79;
   * 
   * @test_Strategy: javax.websocket.server.ServerEndpointConfig.getConfigurator
   * javax.websocket.Endpoint.onOpen;
   * javax.websocket.MessageHandler.Whole.onMessage
   */
  public void getConfiguratorTest() throws Fault {
    String[] endpoints = new String[] { "programatic/subprotocols",
        "annotated/configurator", "programatic/configurator" };
    String[] responses = new String[] {
        SubprotocolsServerEndpointConfig.class.getName(),
        ServerEndpointConfigConfigurator.class.getName(),
        ServerEndpointConfigConfigurator.class.getName() };
    for (int i = 0; i != endpoints.length; i++) {
      setProperty(Property.REQUEST, buildRequest(endpoints[i]));
      setProperty(Property.CONTENT, "configurator");
      setProperty(Property.SEARCH_STRING, responses[i]);
      invoke();
    }
  }

  /*
   * @testName: getEndpointClassTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:194; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79;
   * 
   * @test_Strategy:
   * javax.websocket.server.ServerEndpointConfig.getEndpointClass
   * javax.websocket.Endpoint.onOpen;
   * javax.websocket.MessageHandler.Whole.onMessage
   */
  public void getEndpointClassTest() throws Fault {
    String[] endpoints = new String[] { "programatic/subprotocols",
        "annotated/subprotocols", "annotated/configurator",
        "programatic/configurator", "programatic/extensions" };
    String[] responses = new String[] {
        WSProgramaticSubprotocolsServer.class.getName(),
        WSAnnotatedSubprotocolsServer.class.getName(),
        WSAnnotatedConfiguratorServer.class.getName(),
        WSProgramaticConfiguratorServer.class.getName(),
        WSProgramaticExtensionsServer.class.getName() };
    for (int i = 0; i != endpoints.length; i++) {
      setProperty(Property.REQUEST, buildRequest(endpoints[i]));
      setProperty(Property.CONTENT, "endpoint");
      setProperty(Property.SEARCH_STRING, responses[i]);
      invoke();
    }
  }

  /*
   * @testName: getExtensionsTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:195; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79;
   * 
   * @test_Strategy: javax.websocket.server.ServerEndpointConfig.getExtensions
   * javax.websocket.Endpoint.onOpen;
   * javax.websocket.MessageHandler.Whole.onMessage
   */
  public void getExtensionsTest() throws Fault {
    String[] endpoints = new String[] { "programatic/subprotocols",
        "annotated/subprotocols", "annotated/configurator",
        "programatic/configurator", "programatic/extensions" };
    String[] responses = new String[] { "[]", "[]", "[]", "[]",
        ExtensionsServerEndpointConfig.EXT_NAMES[0] + "|"
            + ExtensionsServerEndpointConfig.EXT_NAMES[1] };
    for (int i = 0; i != endpoints.length; i++) {
      setProperty(Property.REQUEST, buildRequest(endpoints[i]));
      setProperty(Property.CONTENT, "extensions");
      setProperty(Property.UNORDERED_SEARCH_STRING, responses[i]);
      invoke();
    }
  }

  /*
   * @testName: getPathTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:196; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79;
   * 
   * @test_Strategy: javax.websocket.server.ServerEndpointConfig.getPath
   * javax.websocket.Endpoint.onOpen;
   * javax.websocket.MessageHandler.Whole.onMessage
   */
  public void getPathTest() throws Fault {
    String[] sequence = new String[] { "programatic/subprotocols",
        "annotated/subprotocols", "annotated/configurator",
        "programatic/configurator", "programatic/extensions" };
    for (String endpoint : sequence) {
      setProperty(Property.REQUEST, buildRequest(endpoint));
      setProperty(Property.CONTENT, "path");
      setProperty(Property.SEARCH_STRING, endpoint);
      invoke();
    }
  }

  /*
   * @testName: getSubprotocolsTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:197; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79;
   * 
   * @test_Strategy: javax.websocket.server.ServerEndpointConfig.getSubprotocols
   * Return the websocket subprotocols configured.
   * javax.websocket.Endpoint.onOpen;
   * javax.websocket.MessageHandler.Whole.onMessage
   */
  public void getSubprotocolsTest() throws Fault {
    String[] endpoints = new String[] { "programatic/subprotocols",
        "annotated/subprotocols" };
    for (String endpoint : endpoints) {
      setProperty(Property.REQUEST, buildRequest(endpoint));
      setProperty(Property.CONTENT, "subprotocols");
      setProperty(Property.UNORDERED_SEARCH_STRING, "abc");
      setProperty(Property.UNORDERED_SEARCH_STRING, "def");
      invoke();
    }
  }

  /*
   * @testName: getEmptySubprotocolsTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:197; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79;
   * 
   * @test_Strategy: javax.websocket.server.ServerEndpointConfig.getSubprotocols
   * Return the websocket subprotocols configured.
   * javax.websocket.Endpoint.onOpen;
   * javax.websocket.MessageHandler.Whole.onMessage
   */
  public void getEmptySubprotocolsTest() throws Fault {
    String[] endpoints = new String[] { "annotated/configurator",
        "programatic/configurator", "programatic/extensions" };
    for (String endpoint : endpoints) {
      setProperty(Property.REQUEST, buildRequest(endpoint));
      setProperty(Property.CONTENT, "subprotocols");
      invoke(false);
      String response = getResponseAsString();
      assertEqualsInt(0,
          response.replace("[", "").replace("]", "").trim().length(),
          "Unexpected subprotocol list received", response);
      cleanup();
    }
  }
}
