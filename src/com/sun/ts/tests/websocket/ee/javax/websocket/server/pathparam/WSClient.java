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

package com.sun.ts.tests.websocket.ee.javax.websocket.server.pathparam;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSClient extends WebSocketCommonClient {
  private static final long serialVersionUID = -6963654147324631018L;

  public WSClient() {
    setContextRoot("ws_ee_javax_websocket_server_pathparam_web");
  }

  public static void main(String[] args) {
    new WSClient().run(args);
  }

  /* Run test */

  // -----------------------------On Message

  /*
   * @testName: multipleStringParamsOnMessageTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-4;
   * 
   * @test_Strategy: test sending more path params
   */
  public void multipleStringParamsOnMessageTest() throws Fault {
    multipleStringParams(OPS.MESSAGE);
  }

  /*
   * @testName: noStringParamsOnMessageTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * 
   * @test_Strategy: test sending zero path params If the name does not match a
   * path variable in the URI-template, the value of the method parameter this
   * annotation annotates is null.
   */
  public void noStringParamsOnMessageTest() throws Fault {
    noStringParams(OPS.MESSAGE);
  }

  /*
   * @testName: directStringParamOnMessageTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-4;
   * 
   * @test_Strategy: test sending zero path params
   */
  public void directStringParamOnMessageTest() throws Fault {
    directStringParam(OPS.MESSAGE);
  }

  /*
   * @testName: primitiveBooleanAndCharParamsOnMessageTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-5;
   * 
   * @test_Strategy: test sending boolean and char to primitives
   */
  public void primitiveBooleanAndCharParamsOnMessageTest() throws Fault {
    primitiveBooleanAndCharParams(OPS.MESSAGE);
  }

  /*
   * @testName: fullDoubleAndFloatParamsOnMessageTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-5;
   * 
   * @test_Strategy: test sending double and float to Full classes
   */
  public void fullDoubleAndFloatParamsOnMessageTest() throws Fault {
    fullDoubleAndFloatParams(OPS.MESSAGE);
  }

  // -----------------------------On Open

  /*
   * @testName: multipleStringParamsOnOpenTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-4;
   * 
   * @test_Strategy: test sending more path params
   */
  public void multipleStringParamsOnOpenTest() throws Fault {
    multipleStringParams(OPS.OPEN);
  }

  /*
   * @testName: noStringParamsOnOpenTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * 
   * @test_Strategy: test sending zero path params If the name does not match a
   * path variable in the URI-template, the value of the method parameter this
   * annotation annotates is null.
   */
  public void noStringParamsOnOpenTest() throws Fault {
    noStringParams(OPS.OPEN);
  }

  /*
   * @testName: directStringParamOnOpenTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-4;
   * 
   * @test_Strategy: test sending zero path params
   */
  public void directStringParamOnOpenTest() throws Fault {
    directStringParam(OPS.OPEN);
  }

  /*
   * @testName: primitiveBooleanAndCharParamsOnOpenTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-5;
   * 
   * @test_Strategy: test sending boolean and char to primitives
   */
  public void primitiveBooleanAndCharParamsOnOpenTest() throws Fault {
    primitiveBooleanAndCharParams(OPS.OPEN);
  }

  /*
   * @testName: fullDoubleAndFloatParamsOnOpenTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-5;
   * 
   * @test_Strategy: test sending double and float to Full classes
   */
  public void fullDoubleAndFloatParamsOnOpenTest() throws Fault {
    fullDoubleAndFloatParams(OPS.OPEN);
  }

  // -----------------------------On IOException

  /*
   * @testName: multipleStringParamsOnIOETest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-4;
   * 
   * @test_Strategy: test sending more path params
   */
  public void multipleStringParamsOnIOETest() throws Fault {
    multipleStringParams(OPS.IOEXCEPTION);
  }

  /*
   * @testName: noStringParamsOnIOETest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * 
   * @test_Strategy: test sending zero path params If the name does not match a
   * path variable in the URI-template, the value of the method parameter this
   * annotation annotates is null.
   */
  public void noStringParamsOnIOETest() throws Fault {
    noStringParams(OPS.IOEXCEPTION);
  }

  /*
   * @testName: directStringParamOnIOETest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-4;
   * 
   * @test_Strategy: test sending zero path params
   */
  public void directStringParamOnIOETest() throws Fault {
    directStringParam(OPS.IOEXCEPTION);
  }

  /*
   * @testName: primitiveBooleanAndCharParamsOnIOETest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-5;
   * 
   * @test_Strategy: test sending boolean and char to primitives
   */
  public void primitiveBooleanAndCharParamsOnIOETest() throws Fault {
    primitiveBooleanAndCharParams(OPS.IOEXCEPTION);
  }

  /*
   * @testName: fullDoubleAndFloatParamsOnIOTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-5;
   * 
   * @test_Strategy: test sending double and float to Full classes
   */
  public void fullDoubleAndFloatParamsOnIOTest() throws Fault {
    fullDoubleAndFloatParams(OPS.IOEXCEPTION);
  }

  // -----------------------------On RuntimeException

  /*
   * @testName: multipleStringParamsOnRETest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-4;
   * 
   * @test_Strategy: test sending more path params
   */
  public void multipleStringParamsOnRETest() throws Fault {
    multipleStringParams(OPS.RUNTIMEEXCEPTION);
  }

  /*
   * @testName: noStringParamsOnRETest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * 
   * @test_Strategy: test sending zero path params If the name does not match a
   * path variable in the URI-template, the value of the method parameter this
   * annotation annotates is null.
   */
  public void noStringParamsOnRETest() throws Fault {
    noStringParams(OPS.RUNTIMEEXCEPTION);
  }

  /*
   * @testName: directStringParamOnRETest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-4;
   * 
   * @test_Strategy: test sending zero path params
   */
  public void directStringParamOnRETest() throws Fault {
    directStringParam(OPS.RUNTIMEEXCEPTION);
  }

  /*
   * @testName: primitiveBooleanAndCharParamsOnRETest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-5;
   * 
   * @test_Strategy: test sending boolean and char to primitives
   */
  public void primitiveBooleanAndCharParamsOnRETest() throws Fault {
    primitiveBooleanAndCharParams(OPS.RUNTIMEEXCEPTION);
  }

  /*
   * @testName: fullDoubleAndFloatParamsOnRETest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-5;
   * 
   * @test_Strategy: test sending double and float to Full classes
   */
  public void fullDoubleAndFloatParamsOnRETest() throws Fault {
    fullDoubleAndFloatParams(OPS.RUNTIMEEXCEPTION);
  }

  // -----------------------------On Close

  /*
   * @testName: multipleStringParamsOnCloseTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-4;
   * 
   * @test_Strategy: test sending more path params
   */
  public void multipleStringParamsOnCloseTest() throws Fault {
    String param = "param";
    StringBuilder sb = new StringBuilder();
    for (int i = 1; i != 12; i++) {
      invoke("onclose", "-1", WSOnClosePathParamServer.RESET);
      sb.append("/").append(param).append(i);
      setProperty(Property.REQUEST, buildRequest(param, sb.toString()));
      setProperty(Property.SEARCH_STRING, sb.toString().replace("/", ""));
      setProperty(Property.CONTENT, OPS.MESSAGE.name());
      invoke();
      TestUtil.sleepMsec(200);
      for (int j = 0; j != i; j++)
        invoke("onclose", String.valueOf(j), param + String.valueOf(j + 1));
    }
  }

  /*
   * @testName: noStringParamsOnCloseTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * 
   * @test_Strategy: test sending zero path params If the name does not match a
   * path variable in the URI-template, the value of the method parameter this
   * annotation annotates is null.
   */
  public void noStringParamsOnCloseTest() throws Fault {
    invoke("onclose", "-1", WSOnClosePathParamServer.RESET);
    String search = noStringParams(OPS.MESSAGE);
    TestUtil.sleepMsec(200);
    invoke("onclose", "0", search);
  }

  /*
   * @testName: directStringParamOnCloseTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-4;
   * 
   * @test_Strategy: test sending zero path params
   */
  public void directStringParamOnCloseTest() throws Fault {
    invoke("onclose", "-1", WSOnClosePathParamServer.RESET);
    String search = directStringParam(OPS.MESSAGE);
    TestUtil.sleepMsec(200);
    invoke("onclose", "0", search);
  }

  /*
   * @testName: primitiveBooleanAndCharParamsOnCloseTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-5;
   * 
   * @test_Strategy: test sending boolean and char to primitives
   */
  public void primitiveBooleanAndCharParamsOnCloseTest() throws Fault {
    invoke("onclose", "-1", WSOnClosePathParamServer.RESET);
    String[] search = primitiveBooleanAndCharParams(OPS.MESSAGE);
    TestUtil.sleepMsec(200);
    invoke("onclose", "0", search[0]);
    invoke("onclose", "1", search[1]);
  }

  /*
   * @testName: fullDoubleAndFloatParamsOnCloseTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
   * WebSocket:SPEC:WSC-4.3-5;
   * 
   * @test_Strategy: test sending double and float to Full classes
   */
  public void fullDoubleAndFloatParamsOnCloseTest() throws Fault {
    invoke("onclose", "-1", WSOnClosePathParamServer.RESET);
    String[] search = fullDoubleAndFloatParams(OPS.RUNTIMEEXCEPTION);
    TestUtil.sleepMsec(200);
    invoke("onclose", "0", search[0]);
    invoke("onclose", "1", search[1]);
  }

  // -------------------------------------------------------------------
  private void multipleStringParams(OPS op) throws Fault {
    String param = "param";
    StringBuilder sb = new StringBuilder();
    for (int i = 1; i != 12; i++) {
      sb.append("/").append(param).append(i);
      setProperty(Property.REQUEST, buildRequest(param, sb.toString()));
      setProperty(Property.SEARCH_STRING, sb.toString().replace("/", ""));
      setProperty(Property.CONTENT, op.name());
      invoke();
    }
  }

  private String noStringParams(OPS op) throws Fault {
    String param = "nonused/nonused";
    String search = "null";
    setProperty(Property.REQUEST, buildRequest(param));
    setProperty(Property.SEARCH_STRING, search);
    setProperty(Property.CONTENT, op.name());
    invoke();
    return search;
  }

  private String directStringParam(OPS op) throws Fault {
    String param = "1234567890";
    setProperty(Property.REQUEST, buildRequest(param));
    setProperty(Property.SEARCH_STRING, param);
    setProperty(Property.CONTENT, op.name());
    invoke();
    return param;
  }

  private String[] primitiveBooleanAndCharParams(OPS op) throws Fault {
    String[] param = { "true", "0" };
    setProperty(Property.REQUEST,
        buildRequest("different/", param[0], "/", param[1]));
    setProperty(Property.SEARCH_STRING, param[0] + param[1]);
    setProperty(Property.CONTENT, op.name());
    invoke();
    return param;
  }

  private String[] fullDoubleAndFloatParams(OPS op) throws Fault {
    String[] param = { String.valueOf(12.34), String.valueOf(56.78f) };
    setProperty(Property.REQUEST,
        buildRequest("full/", param[0], "/", param[1]));
    setProperty(Property.SEARCH_STRING, param[0] + param[1]);
    setProperty(Property.CONTENT, op.name());
    invoke();
    return param;
  }

}
