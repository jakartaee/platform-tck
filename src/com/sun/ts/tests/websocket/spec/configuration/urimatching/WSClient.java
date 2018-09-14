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

package com.sun.ts.tests.websocket.spec.configuration.urimatching;

import javax.websocket.DeploymentException;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSClient extends WebSocketCommonClient {

  private static final long serialVersionUID = -6954038749538806576L;

  static final String ECHO = "Echo message to be sent to server endpoint";

  public WSClient() {
    setContextRoot("wsc_spec_configuration_urimatching_web");
  }

  public static void main(String[] args) {
    new WSClient().run(args);
  }

  /* Run test */

  // TEXT ------------------------------------------

  /*
   * @testName: match1ExactTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
   * WebSocket:SPEC:WSC-4.3-3;
   * 
   * @test_Strategy: Match /a
   */
  public void match1ExactTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest("a"));
    setProperty(Property.CONTENT, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO);
    setProperty(Property.SEARCH_STRING, WSL1ExactServer.class.getName());
    invoke();
  }

  /*
   * @testName: match1ParamTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
   * WebSocket:SPEC:WSC-4.3-3;
   * 
   * @test_Strategy: Match /{a}
   */
  public void match1ParamTest() throws Fault {
    String param = "c";
    setProperty(Property.REQUEST, buildRequest(param));
    setProperty(Property.CONTENT, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO);
    setProperty(Property.SEARCH_STRING, param);
    setProperty(Property.SEARCH_STRING, WSL1ParamServer.class.getName());
    invoke();
  }

  /*
   * @testName: match2CParamDTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
   * WebSocket:SPEC:WSC-4.3-3;
   * 
   * @test_Strategy: Match /c/{d}
   */
  public void match2CParamDTest() throws Fault {
    String param = "one";
    setProperty(Property.REQUEST, buildRequest("c/", param));
    setProperty(Property.CONTENT, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO);
    setProperty(Property.SEARCH_STRING, param);
    setProperty(Property.SEARCH_STRING, WSL2CParamDServer.class.getName());
    invoke();
  }

  /*
   * @testName: match2CDExactTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
   * 
   * @test_Strategy: Match /c/d
   */
  public void match2CDExactTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest("c/d"));
    setProperty(Property.CONTENT, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO);
    setProperty(Property.SEARCH_STRING, WSL2ExactCDServer.class.getName());
    invoke();
  }

  /*
   * @testName: match2ParamCExactDTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
   * WebSocket:SPEC:WSC-4.3-3;
   * 
   * @test_Strategy: Match /{c}/d
   */
  public void match2ParamCExactDTest() throws Fault {
    String c = "one";
    setProperty(Property.REQUEST, buildRequest(c, "/d"));
    setProperty(Property.CONTENT, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO);
    setProperty(Property.SEARCH_STRING, c);
    setProperty(Property.SEARCH_STRING, WSL2DParamCServer.class.getName());
    invoke();
  }

  /*
   * @testName: match2ParamCDTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
   * WebSocket:SPEC:WSC-4.3-3;
   * 
   * @test_Strategy: Match /{c}/{d}
   */
  public void match2ParamCDTest() throws Fault {
    String c = "one";
    String d = "two";
    setProperty(Property.REQUEST, buildRequest(c, "/", d));
    setProperty(Property.CONTENT, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO);
    setProperty(Property.SEARCH_STRING, c);
    setProperty(Property.SEARCH_STRING, d);
    setProperty(Property.SEARCH_STRING, WSL2ParamCDServer.class.getName());
    invoke();
  }

  /*
   * @testName: match3ACDExactTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
   * WebSocket:SPEC:WSC-4.3-3;
   * 
   * @test_Strategy: Match /a/c/d
   */
  public void match3ACDExactTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest("a/c/d"));
    setProperty(Property.CONTENT, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO);
    setProperty(Property.SEARCH_STRING, WSL3ACDExactServer.class.getName());
    invoke();
  }

  /*
   * @testName: match3AParamCDTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
   * WebSocket:SPEC:WSC-4.3-3;
   * 
   * @test_Strategy: Match /a/{c}/{d}
   */
  public void match3AParamCDTest() throws Fault {
    String c = "one";
    String d = "two";
    setProperty(Property.REQUEST, buildRequest("a/", c, "/", d));
    setProperty(Property.CONTENT, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO);
    setProperty(Property.SEARCH_STRING, c);
    setProperty(Property.SEARCH_STRING, d);
    setProperty(Property.SEARCH_STRING, WSL3AParamCDServer.class.getName());
    invoke();
  }

  /*
   * @testName: match3CParamADTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
   * WebSocket:SPEC:WSC-4.3-3;
   * 
   * @test_Strategy: Match /{a}/c/{d}
   */
  public void match3CParamADTest() throws Fault {
    String a = "one";
    String d = "two";
    setProperty(Property.REQUEST, buildRequest(a, "/c/", d));
    setProperty(Property.CONTENT, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO);
    setProperty(Property.SEARCH_STRING, a);
    setProperty(Property.SEARCH_STRING, d);
    setProperty(Property.SEARCH_STRING, WSL3CParamADServer.class.getName());
    invoke();
  }

  /*
   * @testName: match3DParamACTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
   * WebSocket:SPEC:WSC-4.3-3;
   * 
   * @test_Strategy: Match /{a}/{c}/d
   */
  public void match3DParamACTest() throws Fault {
    String a = "one";
    String c = "two";
    setProperty(Property.REQUEST, buildRequest(a, "/", c, "/d"));
    setProperty(Property.CONTENT, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO);
    setProperty(Property.SEARCH_STRING, a);
    setProperty(Property.SEARCH_STRING, c);
    setProperty(Property.SEARCH_STRING, WSL3DParamACServer.class.getName());
    invoke();
  }

  /*
   * @testName: match3ParamACDTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
   * WebSocket:SPEC:WSC-4.3-3;
   * 
   * @test_Strategy: Match /{a}/c/{d}
   */
  public void match3ParamACDTest() throws Fault {
    String a = "one";
    String c = "two";
    String d = "three";
    setProperty(Property.REQUEST, buildRequest(a, "/", c, "/", d));
    setProperty(Property.CONTENT, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO);
    setProperty(Property.SEARCH_STRING, a);
    setProperty(Property.SEARCH_STRING, c);
    setProperty(Property.SEARCH_STRING, d);
    setProperty(Property.SEARCH_STRING, WSL3ParamACDServer.class.getName());
    invoke();
  }

  /*
   * @testName: match3ACParamDTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
   * WebSocket:SPEC:WSC-4.3-3;
   * 
   * @test_Strategy: Match /a/c/{d}
   */
  public void match3ACParamDTest() throws Fault {
    String d = "three";
    setProperty(Property.REQUEST, buildRequest("a/c/", d));
    setProperty(Property.CONTENT, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO);
    setProperty(Property.SEARCH_STRING, d);
    setProperty(Property.SEARCH_STRING, WSL3ACParamDServer.class.getName());
    invoke();
  }

  /*
   * @testName: match3ADParamCTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
   * WebSocket:SPEC:WSC-4.3-3;
   * 
   * @test_Strategy: Match /a/{c}/d
   */
  public void match3ADParamCTest() throws Fault {
    String c = "two";
    setProperty(Property.REQUEST, buildRequest("a/", c, "/d"));
    setProperty(Property.CONTENT, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO);
    setProperty(Property.SEARCH_STRING, c);
    setProperty(Property.SEARCH_STRING, WSL3ADParamCServer.class.getName());
    invoke();
  }

  /*
   * @testName: match3CDParamATest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
   * WebSocket:SPEC:WSC-4.3-3;
   * 
   * @test_Strategy: Match /a/{c}/d
   */
  public void match3CDParamATest() throws Fault {
    String a = "two";
    setProperty(Property.REQUEST, buildRequest(a, "/c/d"));
    setProperty(Property.CONTENT, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO);
    setProperty(Property.SEARCH_STRING, a);
    setProperty(Property.SEARCH_STRING, WSL3CDParamAServer.class.getName());
    invoke();
  }

  /*
   * @testName: noMatch4ACDETest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-4;
   * 
   * @test_Strategy: no Match for /a/c/d/e The implementation must not establish
   * the connection unless there is a match. Throws: DeploymentException - if
   * the configuration is not valid
   */
  public void noMatch4ACDETest() throws Fault {
    setProperty(Property.REQUEST, buildRequest("a/c/d/e"));
    setProperty(Property.CONTENT, ECHO);
    try {
      invoke();
    } catch (Fault e) {
      assertCause(e, DeploymentException.class,
          "DeploymentException is not the cause", e);
      return;
    }
    fault("No Deployment exception thrown");
  }

}
