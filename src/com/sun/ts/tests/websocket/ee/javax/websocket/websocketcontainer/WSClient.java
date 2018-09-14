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

/*
 * $Id$
 */
package com.sun.ts.tests.websocket.ee.javax.websocket.websocketcontainer;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ws_wait;
 *                     ts_home;
 */
public class WSClient extends WebSocketCommonClient {

  private static final long serialVersionUID = 5168766356852793713L;

  private static final String CONTEXT_ROOT = "ws_websocketcontainer_web";

  private static final String SENT_STRING_MESSAGE = "Hello World";

  public static void main(String[] args) {
    new WSClient().run(args);
  }

  public WSClient() {
    setContextRoot(CONTEXT_ROOT);
  }

  /* Run test */
  /*
   * @testName: simpleTest
   * 
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void simpleTest() throws Fault {
    String expected = "TCKTestServer opened|" + "TCKTestServer received: "
        + SENT_STRING_MESSAGE + "|TCKTestServer responds";
    setCountDownLatchCount(3);
    setProperty(Property.REQUEST, buildRequest("TCKTestServer"));
    setProperty(Property.SEARCH_STRING, expected);
    setProperty(Property.CONTENT, SENT_STRING_MESSAGE);
    invoke();

  }
}
