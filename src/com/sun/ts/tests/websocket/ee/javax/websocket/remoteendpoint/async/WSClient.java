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

package com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.async;

import java.util.LinkedList;
import java.util.List;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.Encoder;
import javax.websocket.RemoteEndpoint.Async;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.websocket.common.client.BinaryAndTextClientEndpoint;
import com.sun.ts.tests.websocket.common.client.ClientEndpoint;
import com.sun.ts.tests.websocket.common.client.EndpointCallback;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.impl.StringPingMessage;
import com.sun.ts.tests.websocket.common.impl.StringPongMessage;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextEncoder;
import com.sun.ts.tests.websocket.common.util.StringUtil;
import com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.PongMessageClientEndpoint;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSClient extends WebSocketCommonClient {

  private static final long serialVersionUID = 7620798773325933328L;

  static final String[] RESPONSE = WSCServerSideServer.RESPONSE;

  static final String ECHO = "echo";

  public WSClient() {
    setContextRoot("wsc_ee_javax_websocket_remoteendpoint_async_web");
  }

  public static void main(String[] args) {
    new WSClient().run(args);
  }

  /* Run test */

  /*
   * @testName: sendTimeoutOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:100; WebSocket:JAVADOC:93;
   * WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: setSendTimeOut and getSendTimeout
   * javax.websocket.Session.getAsyncRemote
   */
  public void sendTimeoutOnServerTest() throws Fault {
    invoke("server", OPS.TIMEOUT.name(), RESPONSE[0]);
  }

  /*
   * @testName: sendTimeoutOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:100; WebSocket:JAVADOC:93;
   * WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: setSendTimeOut and getSendTimeout
   * javax.websocket.Session.getAsyncRemote
   */
  public void sendTimeoutOnClientTest() throws Fault {
    EndpointCallback callback = new AsyncEndpointCallback() {
      @Override
      void doAsync(Async asyncRemote) throws Fault {
        String ret = WSCServerSideServer.timeout(asyncRemote);
        assertEquals(RESPONSE[0], ret,
            "getSendTimeout did not return what was set by setSendTimeout");
        logMsg("getSendTimeout and setSendTimeout work as expected");
        asyncRemote.sendText(WSClient.this.entity.getEntityAt(String.class, 0));
      }
    };
    setClientCallback(callback);
    invoke("client", ECHO, ECHO);
  }

  /*
   * @testName: sendBinaryOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:94; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Initiates the asynchronous transmission of a binary
   * message. The Future's get() method returns null upon successful completion.
   * javax.websocket.Session.getAsyncRemote
   */
  public void sendBinaryOnServerTest() throws Fault {
    sendOnServer(OPS.SENDBINARY);
  }

  /*
   * @testName: sendBinaryOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:94; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Initiates the asynchronous transmission of a binary
   * message. The Future's get() method returns null upon successful completion.
   * javax.websocket.Session.getAsyncRemote
   */
  public void sendBinaryOnClientTest() throws Fault {
    sendOnClient(OPS.SENDBINARY);
  }

  /*
   * TODO use when ensured priority of user defined encoder/decoder
   * 
   * @assertion_ids: WebSocket:JAVADOC:94;
   * 
   * @test_Strategy: Initiates the asynchronous transmission of a binary
   * message. The Future's get() method returns null upon successful completion.
   *
   * public void sendBinaryHasExecutionExceptionOnServerTest() throws Fault {
   * setClientEndpoint(BinaryAndTextClientEndpoint.class); invoke("throwing",
   * OPS.SENDBINARYEXECUTIONEXCEPTION.name(), RESPONSE[0]); }
   */

  /*
   * @testName: sendBinaryThrowsIAEOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:94;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if the data is null.
   */
  public void sendBinaryThrowsIAEOnServerTest() throws Fault {
    invoke("server", OPS.SENDBINARYTHROWS.name(), RESPONSE[0]);
  }

  /*
   * @testName: sendBinaryThrowsIAEOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:94;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if the data is null.
   */
  public void sendBinaryThrowsIAEOnClientTest() throws Fault {
    sendOnClientThrows(OPS.SENDBINARYTHROWS);
  }

  /*
   * @testName: sendBinaryWithHandlerOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:95; WebSocket:JAVADOC:116;
   * WebSocket:JAVADOC:120; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Initiates the asynchronous transmission of a binary
   * message. The Future's get() method returns null upon successful completion.
   * javax.websocket.SendHandler.onResult( SendResult )
   * javax.websocket.SendResult.SendResult()
   * javax.websocket.Session.getAsyncRemote
   */
  public void sendBinaryWithHandlerOnServerTest() throws Fault {
    sendOnServer(OPS.SENDBINARYHANDLER);
  }

  /*
   * @testName: sendBinaryWithHandlerOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:95; WebSocket:JAVADOC:116;
   * WebSocket:JAVADOC:120; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Initiates the asynchronous transmission of a binary
   * message. The Future's get() method returns null upon successful completion.
   * javax.websocket.SendHandler.onResult( SendResult )
   * javax.websocket.SendResult.SendResult()
   * javax.websocket.Session.getAsyncRemote
   */
  public void sendBinaryWithHandlerOnClientTest() throws Fault {
    sendOnClient(OPS.SENDBINARYHANDLER);
  }

  /*
   * @testName: sendBinaryWithHandlerThrowsIAEOnServerWhenNullDataTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:95;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if either the data or
   * the handler are null.
   */
  public void sendBinaryWithHandlerThrowsIAEOnServerWhenNullDataTest()
      throws Fault {
    invoke("server", OPS.SENDBINARYHANDLERTHROWSONDATA.name(), RESPONSE[0]);
  }

  /*
   * @testName: sendBinaryWithHandlerThrowsIAEOnServerWhenNullHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:95;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if either the data or
   * the handler are null.
   */
  public void sendBinaryWithHandlerThrowsIAEOnServerWhenNullHandlerTest()
      throws Fault {
    invoke("server", OPS.SENDBINARYHANDLERTHROWSONHANDLER.name(), RESPONSE[0]);
  }

  /*
   * @testName: sendBinaryWithHandlerThrowsIAEOnClientWhenNullDataTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:95;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if either the data or
   * the handler are null.
   */
  public void sendBinaryWithHandlerThrowsIAEOnClientWhenNullDataTest()
      throws Fault {
    sendOnClientThrows(OPS.SENDBINARYHANDLERTHROWSONDATA);
  }

  /*
   * @testName: sendBinaryWithHandlerThrowsIAEOnClientWhenNullHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:95;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if either the data or
   * the handler are null.
   */
  public void sendBinaryWithHandlerThrowsIAEOnClientWhenNullHandlerTest()
      throws Fault {
    sendOnClientThrows(OPS.SENDBINARYHANDLERTHROWSONHANDLER);
  }

  /*
   * @testName: sendObjectOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Initiates the asynchronous transmission of a binary
   * message. The Future's get() method returns null upon successful completion.
   * javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECT);
  }

  /*
   * @testName: sendObjectOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Initiates the asynchronous transmission of a binary
   * message. The Future's get() method returns null upon successful completion.
   * javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECT);
  }

  /*
   * @testName: sendObjectBooleanOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Containers will by default be able to encode java primitive
   * types and their object equivalents javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectBooleanOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECT_BOOL, false);
  }

  /*
   * @testName: sendObjectBooleanOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Containers will by default be able to encode java primitive
   * types and their object equivalents javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectBooleanOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECT_BOOL, String.valueOf(false));
  }

  /*
   * @testName: sendObjectByteOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Containers will by default be able to encode java primitive
   * types and their object equivalents javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectByteOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECT_BYTE, -100);
  }

  /*
   * @testName: sendObjectByteOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Containers will by default be able to encode java primitive
   * types and their object equivalents javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectByteOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECT_BYTE, "-100");
  }

  /*
   * @testName: sendObjectCharOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Containers will by default be able to encode java primitive
   * types and their object equivalents
   */
  public void sendObjectCharOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECT_CHAR, String.valueOf((char) 106));
  }

  /*
   * @testName: sendObjectCharOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Containers will by default be able to encode java primitive
   * types and their object equivalents javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectCharOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECT_CHAR, String.valueOf((char) 106));
  }

  /*
   * @testName: sendObjectDoubleOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Containers will by default be able to encode java primitive
   * types and their object equivalents javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectDoubleOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECT_DOUBLE, -105d);
  }

  /*
   * @testName: sendObjectDoubleOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Containers will by default be able to encode java primitive
   * types and their object equivalents javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectDoubleOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECT_DOUBLE, "-105");
  }

  /*
   * @testName: sendObjectFloatOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Containers will by default be able to encode java primitive
   * types and their object equivalents javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectFloatOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECT_FLOAT, -104f);
  }

  /*
   * @testName: sendObjectFloatOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Containers will by default be able to encode java primitive
   * types and their object equivalents javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectFloatOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECT_FLOAT, "-104");
  }

  /*
   * @testName: sendObjectIntOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Containers will by default be able to encode java primitive
   * types and their object equivalents javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectIntOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECT_INT, -102);
  }

  /*
   * @testName: sendObjectIntOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Containers will by default be able to encode java primitive
   * types and their object equivalents javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectIntOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECT_INT, "-102");
  }

  /*
   * @testName: sendObjectLongOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Containers will by default be able to encode java primitive
   * types and their object equivalents javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectLongOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECT_LONG, -103L);
  }

  /*
   * @testName: sendObjectLongOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Containers will by default be able to encode java primitive
   * types and their object equivalents javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectLongOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECT_LONG, "-103");
  }

  /*
   * @testName: sendObjectShortOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Containers will by default be able to encode java primitive
   * types and their object equivalents javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectShortOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECT_SHORT, -101);
  }

  /*
   * @testName: sendObjectShortOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Containers will by default be able to encode java primitive
   * types and their object equivalents javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectShortOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECT_SHORT, "-101");
  }

  /*
   * @testName: sendObjectThrowsIAEOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if the data is null.
   */
  public void sendObjectThrowsIAEOnServerTest() throws Fault {
    invoke("server", OPS.SENDOBJECTTHROWS.name(), RESPONSE[0]);
  }

  /*
   * @testName: sendObjectThrowsIAEOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if the data is null.
   */
  public void sendObjectThrowsIAEOnClientTest() throws Fault {
    sendOnClientThrows(OPS.SENDOBJECTTHROWS);
  }

  /*
   * @testName: sendObjectHasExecutionExceptionOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96;
   * 
   * @test_Strategy: Errors in transmission are wrapped in the
   * ExecutionException thrown when querying the Future object.
   */
  public void sendObjectHasExecutionExceptionOnServerTest() throws Fault {
    setClientEndpoint(BinaryAndTextClientEndpoint.class);
    invoke("throwing", OPS.SENDOBJECTEXECUTIONEXCEPTION.name(), RESPONSE[0]);
  }

  /*
   * @testName: sendObjectHasExecutionExceptionOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96;
   * 
   * @test_Strategy: Errors in transmission are wrapped in the
   * ExecutionException thrown when querying the Future object.
   */
  public void sendObjectHasExecutionExceptionOnClientTest() throws Fault {
    sendOnClientHasExecutionException(OPS.SENDOBJECTEXECUTIONEXCEPTION);
  }

  /*
   * @testName: sendObjectWithHandlerOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:97; WebSocket:JAVADOC:116;
   * WebSocket:JAVADOC:120; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Initiates the asynchronous transmission of a binary
   * message. The Future's get() method returns null upon successful completion.
   * javax.websocket.SendHandler.onResult( SendResult )
   * javax.websocket.SendHandler.SendHandler()
   * javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectWithHandlerOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECTHANDLER);
  }

  /*
   * @testName: sendObjectWithHandlerOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:97; WebSocket:JAVADOC:116;
   * WebSocket:JAVADOC:120; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Initiates the asynchronous transmission of a binary
   * message. The Future's get() method returns null upon successful completion.
   * javax.websocket.SendHandler.onResult( SendResult )
   * javax.websocket.SendHandler.SendHandler()
   * javax.websocket.Session.getAsyncRemote
   */
  public void sendObjectWithHandlerOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECTHANDLER);
  }

  /*
   * @testName: sendObjectWithHandlerThrowsIAEOnServerWhenDataIsNullTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:97;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if either the data or
   * the handler are null.
   */
  public void sendObjectWithHandlerThrowsIAEOnServerWhenDataIsNullTest()
      throws Fault {
    invoke("server", OPS.SENDOBJECTHANDLERTHROWSONDATA.name(), RESPONSE[0]);
  }

  /*
   * @testName: sendObjectWithHandlerThrowsIAEOnServerWhenHandlerIsNullTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:97;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if either the data or
   * the handler are null.
   */
  public void sendObjectWithHandlerThrowsIAEOnServerWhenHandlerIsNullTest()
      throws Fault {
    invoke("server", OPS.SENDOBJECTHANDLERTHROWSONHANDLER.name(), RESPONSE[0]);
  }

  /*
   * @testName: sendObjectWithHandlerThrowsIAEOnClientWhenDataIsNullTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:97;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if either the data or
   * the handler are null.
   */
  public void sendObjectWithHandlerThrowsIAEOnClientWhenDataIsNullTest()
      throws Fault {
    sendOnClientThrows(OPS.SENDOBJECTHANDLERTHROWSONDATA);
  }

  /*
   * @testName: sendObjectWithHandlerThrowsIAEOnClientWhenHandlerIsNullTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:97;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if either the data or
   * the handler are null.
   */
  public void sendObjectWithHandlerThrowsIAEOnClientWhenHandlerIsNullTest()
      throws Fault {
    sendOnClientThrows(OPS.SENDOBJECTHANDLERTHROWSONHANDLER);
  }

  /*
   * @testName: sendObjectWithHandlerHasExceptionOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:116;
   * WebSocket:JAVADOC:117; WebSocket:JAVADOC:118; WebSocket:JAVADOC:119;
   * 
   * @test_Strategy: Check SendHandler#getException && SendHandler#isOk
   * SendResult.SendResult( Throwable )
   */
  public void sendObjectWithHandlerHasExceptionOnServerTest() throws Fault {
    setClientEndpoint(BinaryAndTextClientEndpoint.class);
    invoke("throwing", OPS.SENDOBJECTHANDLEREXECUTIONEXCEPTION.name(),
        RESPONSE[0]);
  }

  /*
   * @testName: sendObjectWithHandlerHasExecutionExceptionOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:96; WebSocket:JAVADOC:116;
   * WebSocket:JAVADOC:117; WebSocket:JAVADOC:118; WebSocket:JAVADOC:119;
   * 
   * @test_Strategy: Check SendHandler#getException && SendHandler#isOk
   * SendResult.SendResult( Throwable )
   */
  public void sendObjectWithHandlerHasExecutionExceptionOnClientTest()
      throws Fault {
    sendOnClientHasExecutionException(OPS.SENDOBJECTHANDLEREXECUTIONEXCEPTION);
  }

  /*
   * @testName: sendTextOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:99; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Initiates the asynchronous transmission of a binary
   * message. The Future's get() method returns null upon successful completion.
   * javax.websocket.Session.getAsyncRemote
   */
  public void sendTextOnServerTest() throws Fault {
    sendOnServer(OPS.SENDTEXT);
  }

  /*
   * @testName: sendTextOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:99; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Initiates the asynchronous transmission of a binary
   * message. The Future's get() method returns null upon successful completion.
   * javax.websocket.Session.getAsyncRemote
   */
  public void sendTextOnClientTest() throws Fault {
    sendOnClient(OPS.SENDTEXT);
  }

  /*
   * @testName: sendTextThrowsIAEOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:99;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if the data is null.
   */
  public void sendTextThrowsIAEOnServerTest() throws Fault {
    invoke("server", OPS.SENDTEXTTHROWS.name(), RESPONSE[0]);
  }

  /*
   * @testName: sendTextThrowsIAEOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:99;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if the data is null.
   */
  public void sendTextThrowsIAEOnClientTest() throws Fault {
    sendOnClientThrows(OPS.SENDTEXTTHROWS);
  }

  /*
   * @testName: sendTextWithHandlerOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:98; WebSocket:JAVADOC:116;
   * WebSocket:JAVADOC:120; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Initiates the asynchronous transmission of a binary
   * message. The Future's get() method returns null upon successful completion.
   * javax.websocket.SendHandler.onResult( SendResult )
   * javax.websocket.SendHandler.SendHandler()
   * javax.websocket.Session.getAsyncRemote
   */
  public void sendTextWithHandlerOnServerTest() throws Fault {
    sendOnServer(OPS.SENDTEXTHANDLER);
  }

  /*
   * @testName: sendTextWithHandlerOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:98; WebSocket:JAVADOC:116;
   * WebSocket:JAVADOC:120; WebSocket:JAVADOC:127;
   * 
   * @test_Strategy: Initiates the asynchronous transmission of a binary
   * message. The Future's get() method returns null upon successful completion.
   * javax.websocket.SendHandler.onResult( SendResult )
   * javax.websocket.SendHandler.SendHandler()
   * javax.websocket.Session.getAsyncRemote
   */
  public void sendTextWithHandlerOnClientTest() throws Fault {
    sendOnClient(OPS.SENDTEXTHANDLER);
  }

  /*
   * @testName: sendTextWithHandlerThrowsIAEOnServerWhenDataIsNullTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:98;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if either the data or
   * the handler are null.
   */
  public void sendTextWithHandlerThrowsIAEOnServerWhenDataIsNullTest()
      throws Fault {
    invoke("server", OPS.SENDTEXTHANDLERTHROWSONDATA.name(), RESPONSE[0]);
  }

  /*
   * @testName: sendTextWithHandlerThrowsIAEOnServerWhenHandlerIsNullTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:98;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if either the data or
   * the handler are null.
   */
  public void sendTextWithHandlerThrowsIAEOnServerWhenHandlerIsNullTest()
      throws Fault {
    invoke("server", OPS.SENDTEXTHANDLERTHROWSONHANDLER.name(), RESPONSE[0]);
  }

  /*
   * @testName: sendTextWithHandlerThrowsIAEOnClientWhenDataIsNullTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:98;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if either the data or
   * the handler are null.
   */
  public void sendTextWithHandlerThrowsIAEOnClientWhenDataIsNullTest()
      throws Fault {
    sendOnClientThrows(OPS.SENDTEXTHANDLERTHROWSONDATA);
  }

  /*
   * @testName: sendTextWithHandlerThrowsIAEOnClientWhenHandlerIsNullTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:98;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if either the data or
   * the handler are null.
   */
  public void sendTextWithHandlerThrowsIAEOnClientWhenHandlerIsNullTest()
      throws Fault {
    sendOnClientThrows(OPS.SENDTEXTHANDLERTHROWSONHANDLER);
  }

  // ------------------------------------------------------------------------
  /*
   * @testName: batchingAllowedOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:84;WebSocket:JAVADOC:91;
   * 
   * @test_Strategy: calls setBatchingAllowed(!getBatchingAllowed()) and checks
   * no exception is thrown
   */
  public void batchingAllowedOnServerTest() throws Fault {
    sendOnServer(OPS.BATCHING_ALLOWED);
  }

  /*
   * @testName: batchingAllowedOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:84;WebSocket:JAVADOC:91;
   * 
   * @test_Strategy: calls setBatchingAllowed(!getBatchingAllowed()) and checks
   * no exception is thrown
   */
  public void batchingAllowedOnClientTest() throws Fault {
    sendOnClient(OPS.BATCHING_ALLOWED);
  }

  /*
   * @testName: sendPingOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:85; WebSocket:SPEC:WSC-2.2.5-1;
   * WebSocket:SPEC:WSC-2.2.5-2;
   * 
   * @test_Strategy: Send a Ping message containing the given application data
   * to the remote endpoint. The corresponding Pong message may be picked up
   * using the MessageHandler.Pong handler
   */
  public void sendPingOnServerTest() throws Fault {
    setCountDownLatchCount(2);
    setProperty(Property.CONTENT, OPS.SEND_PING.name());
    setProperty(Property.REQUEST, buildRequest("server"));
    // first server sends ping, websocket impl responses with pong
    // then server sends ok and the pong is caught back on server
    // and pong data are send back to client
    setProperty(Property.UNORDERED_SEARCH_STRING,
        search(RESPONSE[0], OPS.SEND_PING));
    invoke();
  }

  /*
   * @testName: sendPingOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:85;
   * 
   * @test_Strategy: Send a Ping message containing the given application data
   * to the remote endpoint. The corresponding Pong message may be picked up
   * using the MessageHandler.Pong handler
   */
  public void sendPingOnClientTest() throws Fault {
    setClientEndpoint(PongMessageClientEndpoint.class);
    sendOnClient(OPS.SEND_PING);
  }

  /*
   * @testName: sendPingDelaysTimoutOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:85; WebSocket:SPEC:WSC-2.2.5-1;
   * WebSocket:SPEC:WSC-2.2.5-2;
   * 
   * @test_Strategy: Allows the developer to send an unsolicited Pong message
   * containing the given application data in order to serve as a unidirectional
   * heartbeat for the session.
   * 
   * if a websocket implementation receives a ping message from a peer, it must
   * respond as soon as possible to that peer with a pong message containing the
   * same application data.
   * 
   * if the implementation receives a pong message addressed to this endpoint,
   * it must call that MessageHandler or that annotated message
   */
  public void sendPingDelaysTimoutOnServerTest() throws Fault {
    StringPingMessage ping = new StringPingMessage(OPS.POKE.name());
    setClientEndpoint(PongMessageClientEndpoint.class);
    invoke("server", OPS.IDLE.name(), OPS.IDLE.name(), false);

    TestUtil.sleepMsec(500);
    invokeAgain(ping, OPS.POKE.name(), false);
    TestUtil.sleepMsec(500);
    invokeAgain(ping, OPS.POKE.name(), false);
    TestUtil.sleepMsec(500);
    invokeAgain(ping, OPS.POKE.name(), false);
    TestUtil.sleepMsec(500);

    invokeAgain(OPS.POKE.name(), OPS.POKE.name(), true);
  }

  /*
   * @testName: sendPingDelaysTimoutOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:85; WebSocket:SPEC:WSC-2.2.5-1;
   * WebSocket:SPEC:WSC-2.2.5-2;
   * 
   * @test_Strategy: Allows the developer to send an unsolicited Pong message
   * containing the given application data in order to serve as a unidirectional
   * heartbeat for the session.
   * 
   * if a websocket implementation receives a ping message from a peer, it must
   * respond as soon as possible to that peer with a pong message containing the
   * same application data.
   * 
   * if the implementation receives a pong message addressed to this endpoint,
   * it must call that MessageHandler or that annotated message
   */
  public void sendPingDelaysTimoutOnClientTest() throws Fault {
    setClientEndpoint(PongMessageClientEndpoint.class);
    invoke("server", OPS.POKE.name(), OPS.POKE.name(), false);
    getSession().setMaxIdleTimeout(1500L);

    setCountDownLatchCount(5);
    setProperty(Property.CONTENT, OPS.PING_4_TIMES.name());
    setProperty(Property.SEARCH_STRING, OPS.POKE.name(), OPS.POKE.name(),
        OPS.POKE.name(), OPS.POKE.name());
    setProperty(Property.SEARCH_STRING_IGNORE_CASE);
    setProperty(Property.SEARCH_STRING_IGNORE_CASE, RESPONSE[0]);
    invokeAgain(true);
  }

  /*
   * @testName: sendPingThrowsOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:87;
   * 
   * @test_Strategy: throws IllegalArgumentException - if the applicationData
   * exceeds the maximum allowed payload of 125 bytes
   */
  public void sendPingThrowsOnServerTest() throws Fault {
    invoke("server", OPS.SEND_PING_THROWS, RESPONSE[0]);
  }

  /*
   * @testName: sendPingThrowsOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:87;
   * 
   * @test_Strategy: throws IllegalArgumentException - if the applicationData
   * exceeds the maximum allowed payload of 125 bytes
   */
  public void sendPingThrowsOnClientTest() throws Fault {
    sendOnClientThrows(OPS.SEND_PING_THROWS);
  }

  /*
   * @testName: sendPongOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:88;
   * 
   * @test_Strategy: Allows the developer to send an unsolicited Pong message
   * containing the given application data in order to serve as a unidirectional
   * heartbeat for the session.
   */
  public void sendPongOnServerTest() throws Fault {
    sendOnServer(OPS.SEND_PONG, OPS.SEND_PONG.name(),
        PongMessageClientEndpoint.class);
  }

  /*
   * @testName: sendPongOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:88;
   * 
   * @test_Strategy: Allows the developer to send an unsolicited Pong message
   * containing the given application data in order to serve as a unidirectional
   * heartbeat for the session.
   */
  public void sendPongOnClientTest() throws Fault {
    sendOnClient(OPS.SEND_PONG);
  }

  /*
   * @testName: sendPongDelaysTimoutOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:88; WebSocket:SPEC:WSC-2.2.5-2;
   * 
   * @test_Strategy: Allows the developer to send an unsolicited Pong message
   * containing the given application data in order to serve as a unidirectional
   * heartbeat for the session.
   * 
   * if the implementation receives a pong message addressed to this endpoint,
   * it must call that MessageHandler or that annotated message
   */
  public void sendPongDelaysTimoutOnServerTest() throws Fault {
    StringPongMessage ping = new StringPongMessage(OPS.POKE.name());
    setClientEndpoint(PongMessageClientEndpoint.class);
    invoke("server", OPS.IDLE.name(), OPS.IDLE.name(), false);

    TestUtil.sleepMsec(500);
    invokeAgain(ping, OPS.POKE.name(), false);
    TestUtil.sleepMsec(500);
    invokeAgain(ping, OPS.POKE.name(), false);
    TestUtil.sleepMsec(500);
    invokeAgain(ping, OPS.POKE.name(), false);
    TestUtil.sleepMsec(500);

    invokeAgain(OPS.POKE.name(), OPS.POKE.name(), true);
  }

  /*
   * @testName: sendPongDelaysTimoutOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:88; WebSocket:SPEC:WSC-2.2.5-2;
   * 
   * @test_Strategy: Allows the developer to send an unsolicited Pong message
   * containing the given application data in order to serve as a unidirectional
   * heartbeat for the session.
   * 
   * if the implementation receives a pong message addressed to this endpoint,
   * it must call that MessageHandler or that annotated message
   */
  public void sendPongDelaysTimoutOnClientTest() throws Fault {
    setClientEndpoint(PongMessageClientEndpoint.class);
    invoke("server", OPS.POKE.name(), OPS.POKE.name(), false);
    getSession().setMaxIdleTimeout(1500L);

    setCountDownLatchCount(5);
    setProperty(Property.CONTENT, OPS.PONG_4_TIMES.name());
    setProperty(Property.SEARCH_STRING, OPS.POKE.name(), OPS.POKE.name(),
        OPS.POKE.name(), OPS.POKE.name());
    setProperty(Property.SEARCH_STRING_IGNORE_CASE);
    setProperty(Property.SEARCH_STRING_IGNORE_CASE, RESPONSE[0]);
    invokeAgain(true);
  }

  /*
   * @testName: sendPongThrowsOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:90;
   * 
   * @test_Strategy: throws IllegalArgumentException - if the applicationData
   * exceeds the maximum allowed payload of 125 bytes
   */
  public void sendPongThrowsOnServerTest() throws Fault {
    invoke("server", OPS.SEND_PONG_THROWS, RESPONSE[0]);
  }

  /*
   * @testName: sendPongThrowsOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:90;
   * 
   * @test_Strategy: throws IllegalArgumentException - if the applicationData
   * exceeds the maximum allowed payload of 125 bytes
   */
  public void sendPongThrowsOnClientTest() throws Fault {
    sendOnClientThrows(OPS.SEND_PONG_THROWS);
  }

  // /////////////////////////////////////////////////////////////////////////
  private String search(Object... ops) {
    return StringUtil.objectsToStringWithDelimiter("|", (Object[]) ops);
  }

  private void sendOnServer(OPS ops) throws Fault {
    sendOnServer(ops, ops);
  }

  private void sendOnServer(OPS ops, Object search) throws Fault {
    sendOnServer(ops, search, BinaryAndTextClientEndpoint.class);
  }

  private void sendOnServer(OPS ops, Object search,
      Class<? extends ClientEndpoint<?>> endpoint) throws Fault {
    setCountDownLatchCount(3);
    setProperty(Property.CONTENT, ops.name());
    setProperty(Property.REQUEST, buildRequest("server"));
    setProperty(Property.UNORDERED_SEARCH_STRING,
        search(search, OPS.POKE, RESPONSE[0]));
    PokingEndpointCallback callback = new PokingEndpointCallback(entity);
    setClientCallback(callback);
    setClientEndpoint(endpoint);
    invoke();
  }

  private void sendOnClient(final OPS op) throws Fault {
    sendOnClient(op, op.name());
  }

  private void sendOnClient(final OPS op, String search) throws Fault {
    EndpointCallback callback = new AsyncEndpointCallback() {
      @Override
      void doAsync(Async asyncRemote) throws Fault {
        String ret = null;
        String method = null;
        switch (op) {
        case SENDBINARY:
          ret = WSCServerSideServer.sendBinary(asyncRemote);
          method = "sendBinary(ByteBuffer)";
          break;
        case SENDBINARYHANDLER:
          ret = WSCServerSideServer.sendBinaryWithHandler(asyncRemote);
          method = "sendBinary(ByteBuffer, SendHandler)";
          break;
        case SENDOBJECT:
          ret = WSCServerSideServer.sendObject(asyncRemote);
          method = "sendObject(Object)";
          break;
        case SENDOBJECTHANDLER:
          ret = WSCServerSideServer.sendObjectWithHandler(asyncRemote);
          method = "sendObject(Object, SendHandler)";
          break;
        case SENDOBJECT_BOOL:
          ret = WSCServerSideServer.sendObject(asyncRemote, boolean.class);
          method = "sendObject(boolean)";
          break;
        case SENDOBJECT_BYTE:
          ret = WSCServerSideServer.sendObject(asyncRemote, byte.class);
          method = "sendObject(byte)";
          break;
        case SENDOBJECT_CHAR:
          ret = WSCServerSideServer.sendObject(asyncRemote, char.class);
          method = "sendObject(char)";
          break;
        case SENDOBJECT_DOUBLE:
          ret = WSCServerSideServer.sendObject(asyncRemote, double.class);
          method = "sendObject(double)";
          break;
        case SENDOBJECT_FLOAT:
          ret = WSCServerSideServer.sendObject(asyncRemote, float.class);
          method = "sendObject(float)";
          break;
        case SENDOBJECT_INT:
          ret = WSCServerSideServer.sendObject(asyncRemote, int.class);
          method = "sendObject(int)";
          break;
        case SENDOBJECT_LONG:
          ret = WSCServerSideServer.sendObject(asyncRemote, long.class);
          method = "sendObject(long)";
          break;
        case SENDOBJECT_SHORT:
          ret = WSCServerSideServer.sendObject(asyncRemote, short.class);
          method = "sendObject(short)";
          break;
        case SENDTEXT:
          ret = WSCServerSideServer.sendText(asyncRemote);
          method = "sendText(String)";
          break;
        case SENDTEXTHANDLER:
          ret = WSCServerSideServer.sendTextWithHandler(asyncRemote);
          method = "sendText(String, SendHandler)";
          break;
        case BATCHING_ALLOWED:
          ret = WSCServerSideServer.batchingAllowed(asyncRemote);
          method = "setBatchingAllowed(!getBatchingAllowed)";
          break;
        case SEND_PING:
          ret = WSCServerSideServer.sendPing(asyncRemote);
          method = "sendPing(ByteBuffer)";
          break;
        case SEND_PONG:
          ret = WSCServerSideServer.sendPong(asyncRemote);
          method = "sendPong(ByteBuffer)";
          break;
        default:
          fault("Method", op, "not implemented");
        }
        assertEquals(RESPONSE[0], ret, method, "did not endup as expected");
        logMsg(method, "works as expected");
      }
    };
    setClientCallback(callback);

    // Add StringBean encoder just for sendObject methods
    List<Class<? extends Encoder>> list = new LinkedList<Class<? extends Encoder>>();
    list.add(StringBeanTextEncoder.class);
    ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
        .encoders(list).build();
    setClientEndpointConfig(config);

    invoke("client", "anything", search);
  }

  private void sendOnClientHasExecutionException(final OPS op) throws Fault {
    EndpointCallback callback = new AsyncEndpointCallback() {
      @Override
      void doAsync(Async asyncRemote) throws Fault {
        String ret = null;
        String method = null;
        switch (op) {
        case SENDOBJECTEXECUTIONEXCEPTION:
          ret = WSCThrowingServerSideServer
              .sendObjectHasExecutionException(asyncRemote);
          method = "sendObject(Object)";
          break;
        case SENDOBJECTHANDLEREXECUTIONEXCEPTION:
          ret = WSCThrowingServerSideServer
              .sendObjectWithSendHandlerHasExecutionException(asyncRemote);
          method = "sendObject(Object, SendHandler)";
          break;
        default:
          fault("Method", op, "not implemented");
        }
        assertEquals(RESPONSE[0], ret, method,
            "did not endup with ExecutionException");
        logMsg(method, "end up with  ExecutionException as expected");
        asyncRemote.sendText(entity.getEntityAt(String.class, 0));
      }
    };
    setClientCallback(callback);

    // Add StringBean encoder just for sendObject methods
    List<Class<? extends Encoder>> list = new LinkedList<Class<? extends Encoder>>();
    list.add(ThrowingStringBeanEncoder.class);
    ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
        .encoders(list).build();
    setClientEndpointConfig(config);

    invoke("client", ECHO, ECHO);
  }

  private void sendOnClientThrows(final OPS op) throws Fault {
    EndpointCallback callback = new AsyncEndpointCallback() {
      @Override
      void doAsync(Async asyncRemote) throws Fault {
        String ret = null;
        String method = null;
        switch (op) {
        case SENDBINARYTHROWS:
          ret = WSCServerSideServer.sendBinaryThrows(asyncRemote);
          method = "sendBinary(ByteBuffer)";
          break;
        case SENDBINARYHANDLERTHROWSONDATA:
          ret = WSCServerSideServer
              .sendBinaryWithHandlerThrowsOnData(asyncRemote);
          method = "sendBinary(ByteBuffer, SendHandler)";
          break;
        case SENDBINARYHANDLERTHROWSONHANDLER:
          ret = WSCServerSideServer
              .sendBinaryWithHandlerThrowsOnHandler(asyncRemote);
          method = "sendBinary(ByteBuffer, SendHandler)";
          break;
        case SENDOBJECTTHROWS:
          ret = WSCServerSideServer.sendObjectThrows(asyncRemote);
          method = "sendObject(Object)";
          break;
        case SENDOBJECTHANDLERTHROWSONDATA:
          ret = WSCServerSideServer
              .sendObjectWithHandlerThrowsOnData(asyncRemote);
          method = "sendObject(Object, SendHandler)";
          break;
        case SENDOBJECTHANDLERTHROWSONHANDLER:
          ret = WSCServerSideServer
              .sendObjectWithHandlerThrowsOnHandler(asyncRemote);
          method = "sendObject(object, SendHandler)";
          break;
        case SENDTEXTTHROWS:
          ret = WSCServerSideServer.sendTextThrows(asyncRemote);
          method = "sendText(String)";
          break;
        case SENDTEXTHANDLERTHROWSONDATA:
          ret = WSCServerSideServer
              .sendTextWithHandlerThrowsOnData(asyncRemote);
          method = "sendText(String, SendHandler)";
          break;
        case SENDTEXTHANDLERTHROWSONHANDLER:
          ret = WSCServerSideServer
              .sendTextWithHandlerThrowsOnHandler(asyncRemote);
          method = "sendText(String, SendHandler)";
          break;
        case SEND_PING_THROWS:
          ret = WSCServerSideServer.sendPingThrows(asyncRemote);
          method = "sendPing(<too_long_message>)";
          break;
        case SEND_PONG_THROWS:
          ret = WSCServerSideServer.sendPongThrows(asyncRemote);
          method = "sendPong(<too_long_message>)";
          break;
        default:
          fault("Method", op, "not implemented");
        }
        assertEquals(RESPONSE[0], ret, method,
            "does not throw IllegalArgumentException as expected");
        logMsg(method, "throws IllegalArgumentException as expected");
        asyncRemote.sendText(entity.getEntityAt(String.class, 0));
      }
    };
    setClientCallback(callback);
    invoke("client", ECHO, ECHO);
  }
}
