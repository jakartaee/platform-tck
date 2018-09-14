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

package com.sun.ts.tests.websocket.platform.javax.websocket.server.handshakerequest.authenticated;

import java.util.Properties;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.impl.ClientConfigurator;

/*
 * The tests here are not guaranteed to pass in standalone TCK, hence it is put 
 * into full CTS bundle 
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 *                     user;
 *                     password;
 */
public class WSCClient extends WebSocketCommonClient {

  private static final long serialVersionUID = -5851958128006729743L;

  String user;

  String password;

  public WSCClient() {
    setContextRoot(
        "wsc_platform_javax_websocket_handshakeresponse_authenticated_web");
  }

  public static void main(String[] args) {
    new WSCClient().run(args);
  }

  public void setup(String[] args, Properties p) throws Fault {
    user = assertProperty(p, "user");
    password = assertProperty(p, "password");
    super.setup(args, p);
  }

  /*
   * @testName: getUserPrincipalTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:179; WebSocket:JAVADOC:77;
   * WebSocket:JAVADOC:15; WebSocket:JAVADOC:210; WebSocket:SPEC:WSC-8.3-1;
   * WebSocket:SPEC:WSC-7.2-2;
   * 
   * @test_Strategy: HandshakeRequest.getUserPrincipal
   * HandshakeResponse.getHeaders
   * ClientEndpointConfig.Configurator.afterResponse
   * ServerEndpointConfig.Configurator.modifyHandshake A transport guarantee of
   * NONE
   * 
   * Similarly, if the opening handshake request is already authenticated with
   * the server, the opening handshake API allows the developer to query the
   * user Principal of the request. If the connection is established with the
   * requesting client, the websocket implementation considers the user
   * Principal for the associated websocket Session to be the user Principal
   * that was present on the opening handshake.
   * 
   * Return the authenticated user
   */
  public void getUserPrincipalTest() throws Fault {
    ClientConfigurator configurator = new ClientConfigurator();
    configurator.addToResponse(GetUserPrincipalConfigurator.KEY,
        String.valueOf(user));
    addClientConfigurator(configurator);
    addAuthorisation();
    invoke("auth/getuserprincipal", "anything", "anything");
    configurator.assertAfterResponseHasBeenCalled();
  }

  /*
   * @testName: isUserInRoleTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:180; WebSocket:JAVADOC:77;
   * WebSocket:JAVADOC:15; WebSocket:JAVADOC:210; WebSocket:SPEC:WSC-8.3-1;
   * 
   * @test_Strategy: HandshakeRequest.isUserInRole HandshakeResponse.getHeaders
   * ClientEndpointConfig.Configurator.afterResponse
   * ServerEndpointConfig.Configurator.modifyHandshake A transport guarantee of
   * NONE
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
    invoke("auth/isuserinrole", "anything", "anything");
    configurator.assertBeforeRequestHasBeenCalled();
    configurator.assertAfterResponseHasBeenCalled();

    // check the user is in staff role
    configurator = new ClientConfigurator();
    configurator.addToResponse(IsUserInRoleConfigurator.KEY,
        String.valueOf(true));
    configurator.addToRequest(IsUserInRoleConfigurator.KEY, "staff");
    addAuthorisation();
    addClientConfigurator(configurator);
    invoke("auth/isuserinrole", "anything", "anything");
    configurator.assertAfterResponseHasBeenCalled();
    configurator.assertBeforeRequestHasBeenCalled();
  }

  /*
   * @testName: isNotAuthenticatedTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-8.1-1; WebSocket:SPEC:WSC-8.3-1;
   * 
   * @test_Strategy: the websocket implementation must return a 401
   * (Unauthorized) response to the opening handshake request and may not
   * initiate a websocket connection A transport guarantee of NONE
   * 
   * 401 is not possible to check, but the websocket connection we can check
   */
  public void isNotAuthenticatedTest() throws Fault {
    boolean connection = false;
    try {
      logExceptionOnInvocation(false);
      invoke("auth/isuserinrole", "staff", "staff");
      connection = true;
    } catch (Fault f) {
      logMsg("The connection was not initiated as expected:",
          getCauseMessage(f));
    }
    assertFalse(connection,
        "The connection is initiated and 401 response was not returned");
  }

  /*
   * @testName: authorizationIsNotUsedForUrlTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-8.3-1; WebSocket:SPEC:WSC-8.2-1;
   * 
   * @test_Strategy: The <url-pattern> used in the security constraint must be
   * used by the container to match the request URI of the opening handshake of
   * the websocket A transport guarantee of NONE
   */
  public void authorizationIsNotUsedForUrlTest() throws Fault {
    invoke("unauth/echo", "echo", "echo");
  }

  /*
   * @testName: authorizationIsNotAppliedForPOSTTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-8.2-2; WebSocket:SPEC:WSC-8.3-1;
   * 
   * @test_Strategy: The implementation must interpret any http-method other
   * than GET (or the default, missing) as not applying to the websocket. A
   * transport guarantee of NONE
   */
  public void authorizationIsNotAppliedForPOSTTest() throws Fault {
    invoke("post/echo", "echo", "echo");
  }

  // /////////////////////////////////////////////////////////////////////
  void addAuthorisation() {
    setProperty(Property.BASIC_AUTH_USER, user);
    setProperty(Property.BASIC_AUTH_PASSWD, password);
  }
}
