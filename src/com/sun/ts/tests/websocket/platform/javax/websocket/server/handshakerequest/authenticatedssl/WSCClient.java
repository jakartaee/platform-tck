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

package com.sun.ts.tests.websocket.platform.javax.websocket.server.handshakerequest.authenticatedssl;

import java.util.Properties;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.impl.ClientConfigurator;

/*
 * The tests here are not guaranteed to pass in standalone TCK, hence it is put 
 * into full CTS bundle 
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     securedWebServicePort;
 *                     ts_home;
 *                     ws_wait;
 *                     user;
 *                     password;
 */
public class WSCClient extends WebSocketCommonClient {

  private static final long serialVersionUID = -5851958128006729743L;

  String user;

  String password;

  String port;

  public WSCClient() {
    setContextRoot("wsc_platform_javax_websocket_handshakeresponse_ssl_web");
    setRequestProtocol("wss");
  }

  public static void main(String[] args) {
    new WSCClient().run(args);
  }

  public void setup(String[] args, Properties p) throws Fault {
    user = assertProperty(p, "user");
    password = assertProperty(p, "password");
    port = assertProperty(p, "securedWebServicePort");
    super.setup(args, p);
    _port = Integer.parseInt(port);
  }

  /*
   * @testName: getUserPrincipalTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:179; WebSocket:JAVADOC:77;
   * WebSocket:JAVADOC:15; WebSocket:JAVADOC:210; WebSocket:SPEC:WSC-8.3-2;
   * 
   * @test_Strategy: HandshakeRequest.getUserPrincipal
   * HandshakeResponse.getHeaders
   * ClientEndpointConfig.Configurator.afterResponse
   * ServerEndpointConfig.Configurator.modifyHandshake A transport guarantee of
   * CONFIDENTIAL
   * 
   * Return the authenticated user
   */
  public void getUserPrincipalTest() throws Fault {
    ClientConfigurator configurator = new ClientConfigurator();
    configurator.addToResponse(GetUserPrincipalConfigurator.KEY,
        String.valueOf(user));
    addClientConfigurator(configurator);
    addAuthorisation();
    invoke("ssl/getuserprincipal", "anything", "anything");
    configurator.assertAfterResponseHasBeenCalled();
  }

  /*
   * @testName: isUserInRoleTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:180; WebSocket:JAVADOC:77;
   * WebSocket:JAVADOC:15; WebSocket:JAVADOC:210; WebSocket:SPEC:WSC-8.3-2;
   * 
   * @test_Strategy: HandshakeRequest.isUserInRole HandshakeResponse.getHeaders
   * ClientEndpointConfig.Configurator.afterResponse
   * ServerEndpointConfig.Configurator.modifyHandshake A transport guarantee of
   * CONFIDENTIAL
   */
  public void isUserInRoleTest() throws Fault {
    // check DIRECTOR role is not known
    // and set staff role to check
    ClientConfigurator configurator = new ClientConfigurator();
    configurator.addToResponse(IsUserInRoleConfigurator.KEY,
        String.valueOf(false));
    configurator.addToRequest(IsUserInRoleConfigurator.KEY, "DIRECTOR");
    addClientConfigurator(configurator);
    addAuthorisation();
    invoke("ssl/isuserinrole", "anything", "anything");
    configurator.assertBeforeRequestHasBeenCalled();
    configurator.assertAfterResponseHasBeenCalled();

    // check the user is in staff role
    configurator = new ClientConfigurator();
    configurator.addToResponse(IsUserInRoleConfigurator.KEY,
        String.valueOf(true));
    configurator.addToRequest(IsUserInRoleConfigurator.KEY, "staff");
    addAuthorisation();
    addClientConfigurator(configurator);
    invoke("ssl/isuserinrole", "anything", "anything");
    configurator.assertAfterResponseHasBeenCalled();
    configurator.assertBeforeRequestHasBeenCalled();
  }

  // /////////////////////////////////////////////////////////////////////
  void addAuthorisation() {
    setProperty(Property.BASIC_AUTH_USER, user);
    setProperty(Property.BASIC_AUTH_PASSWD, password);
  }
}
