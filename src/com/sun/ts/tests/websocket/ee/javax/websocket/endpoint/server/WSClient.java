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

package com.sun.ts.tests.websocket.ee.javax.websocket.endpoint.server;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSClient extends WebSocketCommonClient {
  private static final long serialVersionUID = -712294674123256741L;

  public WSClient() {
    setContextRoot("wsc_ee_endpoint_server_web");
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
   * @test_Strategy: check @OnError works on Endpoint on Server Side
   * javax.websocket.Endpoint.onOpen Endpoint.Endpoint
   */
  public void onErrorWorksTest() throws Fault {
    invoke("msg", WSCMsgServer.MESSAGES[0], WSCMsgServer.EMPTY);
    invoke("error", "anything", "anything");
    invokeUntilFound("msg", WSCMsgServer.MESSAGES[1],
        WSCErrorServerEndpoint.EXCEPTION);
    logMsg(
        "@OnError has been called after RuntimeException is thrown on @OnMessage as expected");
  }

  /*
   * @testName: onCloseWorksTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:67;WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:66;
   * 
   * @test_Strategy: check @OnClose works on Endpoint on Server side
   * javax.websocket.Endpoint.onOpen Endpoint.Endpoint
   */
  public void onCloseWorksTest() throws Fault {
    invoke("msg", WSCMsgServer.MESSAGES[0], WSCMsgServer.EMPTY);
    invoke("close", "anything", "anything");
    invokeUntilFound("msg", WSCMsgServer.MESSAGES[1],
        WSCCloseServerEndpoint.CLOSE);
    logMsg("@OnClose has been called after session.close() as expected");
  }

  // //////////////////////////////////////////////////////////////////////
  private void invokeUntilFound(String endpoint, String content, String search)
      throws Fault {
    int sleep = 100;
    long maxWait = _ws_wait * (1000 / sleep);
    long count = 0;
    boolean found = false;
    String response = null;
    while (!found && count < maxWait) {
      invoke(endpoint, content, "", false);
      response = getLastResponse(String.class);
      if (response.equals(search))
        found = true;
      cleanup();
      if (!found)
        TestUtil.sleepMsec(sleep);
      count++;
    }
    if (!found) {
      fault(search, "has not been found in response, last response was",
          response);
    }
  }

  @Override
  protected com.sun.javatest.Status run(String[] args) {
    if (args.length == 0) {
      args = new String[] { "-p", "install/websocket/bin/ts.jte", "-t",
          "onCloseWorksTest", "-vehicle", "servlet" };
    }
    return super.run(args);
  }

}
