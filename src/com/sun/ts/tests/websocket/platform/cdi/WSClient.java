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

package com.sun.ts.tests.websocket.platform.cdi;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSClient extends WebSocketCommonClient {

  private static final long serialVersionUID = 5295841512141568599L;

  public WSClient() {
    setContextRoot("wsc_platform_cdi_web");
  }

  public static void main(String[] args) {
    new WSClient().run(args);
  }

  /* Run test */

  /*
   * @testName: cdiFieldTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-7.1.1-1;
   * 
   * @test_Strategy: Websocket endpoints running in the Java EE platform must
   * have full dependency injection support as described in the CDI
   * specification. Websocket implementations part of the Java EE platform are
   * required to support field, injection using the javax.inject.Inject
   * annotation into all endpoint classes
   */
  public void cdiFieldTest() throws Fault {
    String msg = "field";
    setProperty(Property.REQUEST, buildRequest(msg));
    setProperty(Property.CONTENT, msg);
    setProperty(Property.SEARCH_STRING, msg);
    setProperty(Property.SEARCH_STRING, WSInjectableServer.class.getName());
    invoke();
  }

  /*
   * @testName: cdiMethodTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-7.1.1-1;
   * 
   * @test_Strategy: Websocket endpoints running in the Java EE platform must
   * have full dependency injection support as described in the CDI
   * specification. Websocket implementations part of the Java EE platform are
   * required to support method injection using the javax.inject.Inject
   * annotation into all endpoint classes
   */
  public void cdiMethodTest() throws Fault {
    String msg = "method";
    setProperty(Property.REQUEST, buildRequest(msg));
    setProperty(Property.CONTENT, msg);
    setProperty(Property.SEARCH_STRING, msg);
    setProperty(Property.SEARCH_STRING, WSInjectableServer.class.getName());
    invoke();
  }

  /*
   * @testName: cdiConstructorTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-7.1.1-1;
   * 
   * @test_Strategy: Websocket endpoints running in the Java EE platform must
   * have full dependency injection support as described in the CDI
   * specification. Websocket implementations part of the Java EE platform are
   * required to support constructor injection using the javax.inject.Inject
   * annotation into all endpoint classes
   */
  public void cdiConstructorTest() throws Fault {
    String msg = "constructor";
    setProperty(Property.REQUEST, buildRequest(msg));
    setProperty(Property.CONTENT, msg);
    setProperty(Property.SEARCH_STRING, msg);
    setProperty(Property.SEARCH_STRING, WSInjectableServer.class.getName());
    invoke();
  }

}
