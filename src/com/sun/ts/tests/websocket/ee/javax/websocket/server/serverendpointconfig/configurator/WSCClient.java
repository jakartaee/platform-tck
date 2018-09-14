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

package com.sun.ts.tests.websocket.ee.javax.websocket.server.serverendpointconfig.configurator;

import java.util.Arrays;
import java.util.List;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.Extension;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.impl.ExtensionImpl;
import com.sun.ts.tests.websocket.common.util.StringUtil;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSCClient extends WebSocketCommonClient {

  private static final long serialVersionUID = 3657966075180214792L;

  public WSCClient() {
    setContextRoot(
        "wsc_ee_javax_websocket_server_serverendpointconfig_configurator_web");
  }

  public static void main(String[] args) {
    new WSCClient().run(args);
  }

  /* Run test */

  /*
   * @testName: checkOriginTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:205;
   * 
   * @test_Strategy: Check the value of the Origin header (See Origin Header)
   * the client passed during the opening handshake.
   */
  public void checkOriginTest() throws Fault {
    invoke("origin", "reset", "");

    // invoke("origin", "get", _hostname);
    // RFC-6455 section 1.3 [1] text that states:
    // "This header field is sent by browser clients; for non-browser
    // clients, this header field may be sent if it makes sense in the
    // context of those clients."

    invoke("origin", "get", "", false);
    String origin = getResponseAsString();
    assertTrue(
        OriginConfigurator.NULL.equals(origin) || origin.contains(_hostname),
        "Unexpected origin received", origin);
  }

  /*
   * @testName: checkOriginReturnsFalseTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:205; WebSocket:SPEC:WSC-3.1.4-1;
   * 
   * @test_Strategy: Check the value of the Origin header (See Origin Header)
   * the client passed during the opening handshake.
   */
  public void checkOriginReturnsFalseTest() throws Fault {
    // set false
    invoke("originreturnsfalseconfig", "false", "false");
    // check false
    setProperty(Property.REQUEST, buildRequest("originreturnsfalse"));
    setProperty(Property.CONTENT, "anything");
    try {
      logExceptionOnInvoke = false;
      invoke();
      fault("First attempt for handshake should return false due to origin");
    } catch (Exception e) {
      // that's all right
      logMsg("thrown exception as expected when checkOrigin == false");
    }
    // set true
    invoke("originreturnsfalseconfig", "true", "true");
    // check true
    invoke("originreturnsfalse", "anything", "anything");
  }

  /*
   * @testName: getEndpointInstanceTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:206; WebSocket:SPEC:WSC-3.1.7-1;
   * WebSocket:SPEC:WSC-3.1.7-2;
   * 
   * @test_Strategy: This method is called by the container each time a new
   * client connects to the logical endpoint this configurator configures.
   * Developers may override this method to control instantiation of endpoint
   * instances in order to customize the initialization of the endpoint
   * instance, or manage them in some other way. The platform default
   * implementation of this method returns a new endpoint instance per call,
   * thereby ensuring that there is one endpoint instance per client.
   * 
   * The implementation must call this method each time a new client connects to
   * the logical endpoint.
   * 
   * The platform default implementation of this method is to return a new
   * instance of the endpoint class each time it is called
   */
  public void getEndpointInstanceTest() throws Fault {
    String search = WSCGetEndpointInstanceServer.class.getName()
        + GetEndpointInstanceConfigurator.ADDITIONAL_INFORMATION;
    // first request sets instance to null
    invoke("endpointinstance", "anything", search);
    // make sure second request does not have instance null
    invoke("endpointinstance", "anything", search);
  }

  /*
   * @testName: getNegotiatedExtensionsRequestedTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:208; WebSocket:JAVADOC:195;
   * WebSocket:JAVADOC:13; WebSocket:JAVADOC:211; WebSocket:JAVADOC:73;
   * WebSocket:JAVADOC:74; WebSocket:JAVADOC:75; WebSocket:JAVADOC:76;
   * 
   * @test_Strategy: what we send is what we receive
   * ServerEndpointConfig.Configurator.getNegotiatedExtensions
   * ServerEndpointConfig.getExtensions ClientEndpointConfig.Builder.extensions
   * ServerEndpointConfig.Configurator.Configurator()
   * Extension.{getName,getParameters} Extension.Parameter.{getName,getValue}
   */
  @SuppressWarnings("unchecked")
  public void getNegotiatedExtensionsRequestedTest() throws Fault {
    List<? extends Extension> extensions = WSCExtensionsServer
        .getRequestedExtension();
    ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
        .extensions((List<Extension>) extensions).build();
    setClientEndpointConfig(config);
    invoke("extensions", "requested", ExtensionImpl.toString(extensions));
  }

  /*
   * @testName: getNegotiatedExtensionsInstalledTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:208; WebSocket:JAVADOC:195;
   * WebSocket:JAVADOC:13; WebSocket:JAVADOC:211; WebSocket:JAVADOC:73;
   * WebSocket:JAVADOC:74; WebSocket:JAVADOC:75; WebSocket:JAVADOC:76;
   * 
   * @test_Strategy: check installed is what ServerEndpoint gets us
   * ServerEndpointConfig.Configurator.getNegotiatedExtensions
   * ServerEndpointConfig.getExtensions ClientEndpointConfig.Builder.extensions
   * ServerEndpointConfig.Configurator.Configurator()
   * Extension.{getName,getParameters} Extension.Parameter.{getName,getValue}
   */
  @SuppressWarnings("unchecked")
  public void getNegotiatedExtensionsInstalledTest() throws Fault {
    List<? extends Extension> extensions = WSCExtensionsServer
        .getRequestedExtension();
    ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
        .extensions((List<Extension>) extensions).build();
    setClientEndpointConfig(config);

    List<Extension> installed = new ExtensionsServerEndpointConfig()
        .getExtensions(); // order must be kept, no need to sort
    invoke("extensions", "installed", ExtensionImpl.toString(installed));
  }

  /*
   * @testName: getNegotiatedExtensionsResultedTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:208; WebSocket:JAVADOC:195;
   * WebSocket:JAVADOC:13; WebSocket:JAVADOC:211; WebSocket:JAVADOC:73;
   * WebSocket:JAVADOC:74; WebSocket:JAVADOC:75; WebSocket:JAVADOC:76;
   * WebSocket:SPEC:WSC-3.1.3-1; WebSocket:SPEC:WSC-3.2.2-1;
   * 
   * @test_Strategy: check the resulted extensions
   * ServerEndpointConfig.Configurator.getNegotiatedExtensions
   * ServerEndpointConfig.getExtensions ClientEndpointConfig.Builder.extensions
   * ServerEndpointConfig.Configurator.Configurator()
   * Extension.{getName,getParameters} Extension.Parameter.{getName,getValue}
   * 
   * The default client configuration must use the developer provided list of
   * extensions to send, in order of preference, the extensions, including
   * parameters, that it would like to use in the opening handshake it
   * formulates.
   */
  @SuppressWarnings("unchecked")
  public void getNegotiatedExtensionsResultedTest() throws Fault {
    List<? extends Extension> extensions = WSCExtensionsServer
        .getRequestedExtension();
    ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
        .extensions((List<Extension>) extensions).build();
    setClientEndpointConfig(config);
    invoke("extensions", "resulted", ExtensionImpl.toString(extensions));
  }

  /*
   * @testName: getNegotiatedExtensionsOrderedByRequestTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:208; WebSocket:JAVADOC:195;
   * WebSocket:JAVADOC:13; WebSocket:JAVADOC:211; WebSocket:JAVADOC:73;
   * WebSocket:JAVADOC:74; WebSocket:JAVADOC:75; WebSocket:JAVADOC:76;
   * WebSocket:SPEC:WSC-3.1.3-1; WebSocket:SPEC:WSC-3.2.2-1;
   * 
   * @test_Strategy: check the resulted extensions
   * ServerEndpointConfig.Configurator.getNegotiatedExtensions The default
   * platform implementation of this method returns a list containing all of the
   * requested extensions passed to this method that it supports, using the
   * order in the requested extensions ServerEndpointConfig.getExtensions
   * ClientEndpointConfig.Builder.extensions
   * ServerEndpointConfig.Configurator.Configurator()
   * Extension.{getName,getParameters} Extension.Parameter.{getName,getValue}
   * 
   * The default client configuration must use the developer provided list of
   * extensions to send, in order of preference, the extensions, including
   * parameters, that it would like to use in the opening handshake it
   * formulates.
   */
  @SuppressWarnings("unchecked")
  public void getNegotiatedExtensionsOrderedByRequestTest() throws Fault {
    List<? extends Extension> extensions = WSCExtensionsServer
        .getOrderedExtensions();
    ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
        .extensions((List<Extension>) extensions).build();
    setClientEndpointConfig(config);
    invoke("extensions", "resultedinorder", ExtensionImpl.toString(extensions));
  }

  /*
   * @testName: getNegotiatedSubprotocolRequestedTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:209; WebSocket:JAVADOC:197;
   * WebSocket:JAVADOC:14; WebSocket:JAVADOC:211;
   * 
   * @test_Strategy: ServerEndpointConfig.Configurator.getNegotiatedSubprotocol
   * ServerEndpointConfig.getSubprotocols
   * ClientEndpointConfig.Builder.preferredSubprotocols
   * ServerEndpointConfig.Configurator.Configurator()
   */
  public void getNegotiatedSubprotocolRequestedTest() throws Fault {
    List<String> subprotocols = WSCSubprotocolServer.getRequestedSubprotocols();
    ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
        .preferredSubprotocols(subprotocols).build();
    setClientEndpointConfig(config);
    invoke("subprotocols", "requested",
        StringUtil.collectionToString(subprotocols));
  }

  /*
   * @testName: getNegotiatedSubprotocolSupportedTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:209; WebSocket:JAVADOC:197;
   * WebSocket:JAVADOC:14; WebSocket:JAVADOC:211;
   * 
   * @test_Strategy: ServerEndpointConfig.Configurator.getNegotiatedSubprotocol
   * ServerEndpointConfig.getSubprotocols
   * ClientEndpointConfig.Builder.preferredSubprotocols
   * ServerEndpointConfig.Configurator.Configurator()
   */
  public void getNegotiatedSubprotocolSupportedTest() throws Fault {
    List<String> subprotocols = WSCSubprotocolServer.getRequestedSubprotocols();
    ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
        .preferredSubprotocols(subprotocols).build();
    setClientEndpointConfig(config);
    invoke("subprotocols", "supported", StringUtil
        .collectionToString(WSCSubprotocolServer.getSupportedSubprotocols()));
  }

  /*
   * @testName: getNegotiatedSubprotocolResultedTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:209; WebSocket:JAVADOC:197;
   * WebSocket:JAVADOC:14; WebSocket:JAVADOC:211; WebSocket:SPEC:WSC-3.1.2-1;
   * WebSocket:SPEC:WSC-3.2.1-1;
   * 
   * @test_Strategy: ServerEndpointConfig.Configurator.getNegotiatedSubprotocol
   * ServerEndpointConfig.getSubprotocols
   * ClientEndpointConfig.Builder.preferredSubprotocols
   * ServerEndpointConfig.Configurator.Configurator()
   * 
   * The default platform implementation of this method returns the FIRST
   * subprotocol in the list sent by the CLIENT that the server supports,
   * 
   * The default client configuration uses the developer provided list of
   * subprotocols, to send in order of preference, the names of the subprotocols
   * it would like to use in the opening handshake it formulates
   */
  public void getNegotiatedSubprotocolResultedTest() throws Fault {
    List<String> subprotocols = WSCSubprotocolServer.getRequestedSubprotocols();
    ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
        .preferredSubprotocols(subprotocols).build();
    setClientEndpointConfig(config);
    invoke("subprotocols", "resulted", StringUtil.WEBSOCKET_SUBPROTOCOLS[2]);
  }

  /*
   * @testName: getNegotiatedSubprotocolResultedIsEmptyTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:209; WebSocket:JAVADOC:197;
   * WebSocket:JAVADOC:14; WebSocket:JAVADOC:211; WebSocket:SPEC:WSC-3.1.2-1;
   * WebSocket:SPEC:WSC-3.2.1-1;
   * 
   * @test_Strategy: ServerEndpointConfig.Configurator.getNegotiatedSubprotocol
   * ServerEndpointConfig.getSubprotocols
   * ClientEndpointConfig.Builder.preferredSubprotocols
   * ServerEndpointConfig.Configurator.Configurator()
   * 
   * Returns: the empty string if there isn't one.
   * 
   * The default client configuration uses the developer provided list of
   * subprotocols, to send in order of preference, the names of the subprotocols
   * it would like to use in the opening handshake it formulates
   */
  public void getNegotiatedSubprotocolResultedIsEmptyTest() throws Fault {
    ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
        .preferredSubprotocols(
            Arrays.asList(StringUtil.WEBSOCKET_SUBPROTOCOLS[6]))
        .build();
    setClientEndpointConfig(config);
    invoke("subprotocols", "resulted", "{}");
  }

  /*
   * @testName: modifyHandshakeAfterCheckOriginTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:210; WebSocket:SPEC:WSC-3.1.5-1;
   * 
   * @test_Strategy: ServerEndpointConfig.Configurator.modifyHandhsake
   * 
   * The container has already determined the validity of the origin using the
   * checkOrigin method.
   * 
   * The default server configuration makes no modification of the opening
   * handshake process other than that described above
   */
  public void modifyHandshakeAfterCheckOriginTest() throws Fault {
    invoke("modifyhandshake", "origin", "true");
  }

  /*
   * @testName: modifyHandshakeConfigTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:210;
   * 
   * @test_Strategy: ServerEndpointConfig.Configurator.modifyHandhsake
   */
  public void modifyHandshakeConfigTest() throws Fault {
    invoke("modifyhandshake", "config", "true");
  }

  /*
   * @testName: modifyHandshakeRequestTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:210;
   * 
   * @test_Strategy: ServerEndpointConfig.Configurator.modifyHandhsake
   */
  public void modifyHandshakeRequestTest() throws Fault {
    invoke("modifyhandshake", "request", "true");
  }

  /*
   * @testName: modifyHandshakeResponseTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:210;
   * 
   * @test_Strategy: ServerEndpointConfig.Configurator.modifyHandhsake
   */
  public void modifyHandshakeResponseTest() throws Fault {
    invoke("modifyhandshake", "response", "true");
  }
}
