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

package com.sun.ts.tests.websocket.spec.application.closing;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;

/*
 * The tests here are not guaranteed to pass in standalone TCK, hence put 
 * into full CTS bundle 
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSCClient extends WebSocketCommonClient {

  private static final long serialVersionUID = 3037319902828702665L;

  public WSCClient() {
    setContextRoot("wsc_spec_application_closing_web");
  }

  public static void main(String[] args) {
    new WSCClient().run(args);
  }

  /*
   * @testName: connectionHasBeenClosedWithStatus1006AfterTimeoutTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-2.1.5-1;
   * 
   * @test_Strategy: If the close was initiated by the local container, for
   * example if the local container determines the session has timed out, the
   * local implementation must use the websocket protocol close code 1006
   */
  public void connectionHasBeenClosedWithStatus1006AfterTimeoutTest()
      throws Fault {
    boolean closed = false;
    String[] messages = WSCCloseSessionServer.MESSAGES;
    logExceptionOnInvocation(false);
    // set properties
    // do not cleanup
    try {
      invoke("closesession", messages[0], messages[0], false);
    } catch (Exception e) {
      // Could have been closed before response returned or it
      // could have taken some time, then the validation fails
    }
    // setMaxIdleTimeout is checked only twice a minute
    try {
      logExceptionOnInvocation(false);
      TestUtil.sleepSec(5); // give time to close connection
      invokeAgain("ok", "ok", true); // check for closed
    } catch (Exception e) {
      closed = true;
      logTrace("Connection has been closed as expected:", getCauseMessage(e));
    }
    assertTrue(closed, "The connection has not been closed");
    // ask for close code
    invoke("closesession", messages[1], "1006");
    logMsg(
        "After invalidation of HTTP session, connection has been closed with expected status 1006");
  }

}
