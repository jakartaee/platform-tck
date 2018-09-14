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

package com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.basic;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.Encoder;
import javax.websocket.RemoteEndpoint.Basic;

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
  private static final long serialVersionUID = -8530759310254161854L;

  static final String[] RESPONSE = WSCServerSideServer.RESPONSE;

  static final String ECHO = "echo";

  public WSClient() {
    setContextRoot("wsc_ee_javax_websocket_remoteendpoint_basic_web");
  }

  public static void main(String[] args) {
    new WSClient().run(args);
  }

  /* Run test */

  /*
   * @testName: sendBinaryOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:105;
   * 
   * @test_Strategy: Send a binary message, returning when all of the message
   * has been transmitted.
   */
  public void sendBinaryOnServerTest() throws Fault {
    sendOnServer(OPS.SENDBINARY);
  }

  /*
   * @testName: sendBinaryOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:107;
   * 
   * @test_Strategy: Send a binary message, returning when all of the message
   * has been transmitted.
   */
  public void sendBinaryOnClientTest() throws Fault {
    sendOnClient(OPS.SENDBINARY);
  }

  /*
   * @testName: sendBinaryPartialOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:107;
   * 
   * @test_Strategy: Send a binary message, returning when all of the message
   * has been transmitted.
   */
  public void sendBinaryPartialOnServerTest() throws Fault {
    sendOnServer(OPS.SENDBINARYPART1, OPS.SENDBINARYPART1.name()
        + OPS.SENDBINARYPART2.name() + OPS.SENDBINARYPART3.name());
  }

  /*
   * @testName: sendBinaryPartialOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:105;
   * 
   * @test_Strategy: Send a binary message, returning when all of the message
   * has been transmitted.
   */
  public void sendBinaryPartialOnClientTest() throws Fault {
    sendOnClient(OPS.SENDBINARYPART1, OPS.SENDBINARYPART1.name()
        + OPS.SENDBINARYPART2.name() + OPS.SENDBINARYPART3.name());
  }

  /*
   * @testName: sendBinaryThrowsIAEOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:105;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if the data is null.
   */
  public void sendBinaryThrowsIAEOnServerTest() throws Fault {
    invoke("server", OPS.SENDBINARYTHROWS.name(), RESPONSE[0]);
  }

  /*
   * @testName: sendBinaryThrowsIAEOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:105;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if the data is null.
   */
  public void sendBinaryThrowsIAEOnClientTest() throws Fault {
    sendOnClientThrows(OPS.SENDBINARYTHROWS);
  }

  /*
   * @testName: sendObjectOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Sends a custom developer object, blocking until it has been
   * transmitted.
   */
  public void sendObjectOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECT);
  }

  /*
   * @testName: sendObjectOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Sends a custom developer object, blocking until it has been
   * transmitted.
   */
  public void sendObjectOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECT);
  }

  /*
   * @testName: sendObjectBooleanOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Sends a custom developer object, blocking until it has been
   * transmitted.
   */
  public void sendObjectBooleanOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECT_BOOL, false);
  }

  /*
   * @testName: sendObjectBooleanOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Sends a custom developer object, blocking until it has been
   * transmitted.
   */
  public void sendObjectBooleanOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECT_BOOL, String.valueOf(false));
  }

  /*
   * @testName: sendObjectByteOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Sends a custom developer object, blocking until it has been
   * transmitted.
   */
  public void sendObjectByteOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECT_BYTE, -100);
  }

  /*
   * @testName: sendObjectByteOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Sends a custom developer object, blocking until it has been
   * transmitted.
   */
  public void sendObjectByteOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECT_BYTE, "-100");
  }

  /*
   * @testName: sendObjectCharOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Sends a custom developer object, blocking until it has been
   * transmitted.
   */
  public void sendObjectCharOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECT_CHAR, String.valueOf((char) 106));
  }

  /*
   * @testName: sendObjectCharOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Sends a custom developer object, blocking until it has been
   * transmitted.
   */
  public void sendObjectCharOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECT_CHAR, String.valueOf((char) 106));
  }

  /*
   * @testName: sendObjectDoubleOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Sends a custom developer object, blocking until it has been
   * transmitted.
   */
  public void sendObjectDoubleOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECT_DOUBLE, -105d);
  }

  /*
   * @testName: sendObjectDoubleOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Sends a custom developer object, blocking until it has been
   * transmitted.
   */
  public void sendObjectDoubleOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECT_DOUBLE, "-105");
  }

  /*
   * @testName: sendObjectFloatOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Sends a custom developer object, blocking until it has been
   * transmitted.
   */
  public void sendObjectFloatOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECT_FLOAT, -104f);
  }

  /*
   * @testName: sendObjectFloatOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Sends a custom developer object, blocking until it has been
   * transmitted.
   */
  public void sendObjectFloatOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECT_FLOAT, "-104");
  }

  /*
   * @testName: sendObjectIntOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Sends a custom developer object, blocking until it has been
   * transmitted.
   */
  public void sendObjectIntOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECT_INT, -102);
  }

  /*
   * @testName: sendObjectIntOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Sends a custom developer object, blocking until it has been
   * transmitted.
   */
  public void sendObjectIntOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECT_INT, "-102");
  }

  /*
   * @testName: sendObjectLongOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Sends a custom developer object, blocking until it has been
   * transmitted.
   */
  public void sendObjectLongOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECT_LONG, -103L);
  }

  /*
   * @testName: sendObjectLongOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Sends a custom developer object, blocking until it has been
   * transmitted.
   */
  public void sendObjectLongOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECT_LONG, "-103");
  }

  /*
   * @testName: sendObjectShortOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Sends a custom developer object, blocking until it has been
   * transmitted.
   */
  public void sendObjectShortOnServerTest() throws Fault {
    sendOnServer(OPS.SENDOBJECT_SHORT, -101);
  }

  /*
   * @testName: sendObjectShortOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Sends a custom developer object, blocking until it has been
   * transmitted.
   */
  public void sendObjectShortOnClientTest() throws Fault {
    sendOnClient(OPS.SENDOBJECT_SHORT, "-101");
  }

  /*
   * @testName: sendObjectThrowsIAEOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if the data is null.
   */
  public void sendObjectThrowsIAEOnServerTest() throws Fault {
    invoke("server", OPS.SENDOBJECTTHROWS.name(), RESPONSE[0]);
  }

  /*
   * @testName: sendObjectThrowsIAEOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:109;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if the data is null.
   */
  public void sendObjectThrowsIAEOnClientTest() throws Fault {
    sendOnClientThrows(OPS.SENDOBJECTTHROWS);
  }

  /*
   * @testName: sendObjectThrowsEncodeExceptionOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:111;
   * 
   * @test_Strategy: Throws: EncodeException - if there was a problem encoding
   * the message object into the form of a native websocket message
   */
  public void sendObjectThrowsEncodeExceptionOnServerTest() throws Fault {
    invoke("server", OPS.SENDOBJECTTHROWSENCODEEEXCEPTION.name(), RESPONSE[0]);
  }

  /*
   * @testName: sendObjectThrowsEncodeExceptionOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:111;
   * 
   * @test_Strategy: Throws: EncodeException - if there was a problem encoding
   * the message object into the form of a native websocket message
   */
  public void sendObjectThrowsEncodeExceptionOnClientTest() throws Fault {
    sendOnClientThrowsException(OPS.SENDOBJECTTHROWSENCODEEEXCEPTION,
        "EncodeException");
  }

  /*
   * @testName: sendTextOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:112;
   * 
   * @test_Strategy: Send a text message, blocking until all of the message has
   * been transmitted.
   */
  public void sendTextOnServerTest() throws Fault {
    sendOnServer(OPS.SENDTEXT);
  }

  /*
   * @testName: sendTextOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:114;
   * 
   * @test_Strategy: Send a text message, blocking until all of the message has
   * been transmitted.
   */
  public void sendTextOnClientTest() throws Fault {
    sendOnClient(OPS.SENDTEXT);
  }

  /*
   * @testName: sendTextPartialOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:114;
   * 
   * @test_Strategy: Send a text message, blocking until all of the message has
   * been transmitted.
   */
  public void sendTextPartialOnServerTest() throws Fault {
    sendOnServer(OPS.SENDTEXTPART1, OPS.SENDTEXTPART1.name()
        + OPS.SENDTEXTPART2.name() + OPS.SENDTEXTPART3.name());
  }

  /*
   * @testName: sendTextPartialOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:112;
   * 
   * @test_Strategy: Send a text message, blocking until all of the message has
   * been transmitted.
   */
  public void sendTextPartialOnClientTest() throws Fault {
    sendOnClient(OPS.SENDTEXTPART1, OPS.SENDTEXTPART1.name()
        + OPS.SENDTEXTPART2.name() + OPS.SENDTEXTPART3.name());
  }

  /*
   * @testName: sendTextThrowsIAEOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:112;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if the text is null.
   */
  public void sendTextThrowsIAEOnServerTest() throws Fault {
    invoke("server", OPS.SENDTEXTTHROWS.name(), RESPONSE[0]);
  }

  /*
   * @testName: sendTextThrowsIAEOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:112;
   * 
   * @test_Strategy: Throws: IllegalArgumentException - if the text is null.
   */
  public void sendTextThrowsIAEOnClientTest() throws Fault {
    sendOnClientThrows(OPS.SENDTEXTTHROWS);
  }

  /*
   * @testName: getSendStreamOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:101;
   * 
   * @test_Strategy: Opens an output stream on which a binary message may be
   * sent. The developer must close the output stream in order to indicate that
   * the complete message has been placed into the output stream.
   */
  public void getSendStreamOnServerTest() throws Fault {
    sendOnServer(OPS.SENDSTREAM);
  }

  /*
   * @testName: getSendStreamOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:101;
   * 
   * @test_Strategy: Opens an output stream on which a binary message may be
   * sent. The developer must close the output stream in order to indicate that
   * the complete message has been placed into the output stream.
   */
  public void getSendStreamOnClientTest() throws Fault {
    sendOnClient(OPS.SENDSTREAM);
  }

  /*
   * @testName: getSendWriterOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:103;
   * 
   * @test_Strategy: Opens an output stream on which a binary message may be
   * sent. The developer must close the writer in order to indicate that the
   * complete message has been placed into the character stream.
   */
  public void getSendWriterOnServerTest() throws Fault {
    sendOnServer(OPS.SENDWRITER);
  }

  /*
   * @testName: getSendWriterOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:103;
   * 
   * @test_Strategy: Opens an output stream on which a binary message may be
   * sent. The developer must close the writer in order to indicate that the
   * complete message has been placed into the character stream.
   */
  public void getSendWriterOnClientTest() throws Fault {
    sendOnClient(OPS.SENDWRITER);
  }

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
   * @assertion_ids: WebSocket:JAVADOC:85;
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
    EndpointCallback callback = new BasicEndpointCallback() {
      @Override
      void doBasic(Basic basicRemote) throws Fault {
        String ret = null;
        String method = null;
        switch (op) {
        case SENDBINARY:
          ret = WSCServerSideServer.sendBinary(basicRemote);
          method = "sendBinary(ByteBuffer)";
          break;
        case SENDBINARYPART1:
          ret = WSCServerSideServer.sendBinaryPartial(basicRemote);
          method = "sendBinary(ByteBuffer, boolean)";
          break;
        case SENDOBJECT:
          ret = WSCServerSideServer.sendObject(basicRemote);
          method = "sendObject(Object)";
          break;
        case SENDOBJECT_BOOL:
          ret = WSCServerSideServer.sendObject(basicRemote, boolean.class);
          method = "sendObject(boolean)";
          break;
        case SENDOBJECT_BYTE:
          ret = WSCServerSideServer.sendObject(basicRemote, byte.class);
          method = "sendObject(byte)";
          break;
        case SENDOBJECT_CHAR:
          ret = WSCServerSideServer.sendObject(basicRemote, char.class);
          method = "sendObject(char)";
          break;
        case SENDOBJECT_DOUBLE:
          ret = WSCServerSideServer.sendObject(basicRemote, double.class);
          method = "sendObject(double)";
          break;
        case SENDOBJECT_FLOAT:
          ret = WSCServerSideServer.sendObject(basicRemote, float.class);
          method = "sendObject(float)";
          break;
        case SENDOBJECT_INT:
          ret = WSCServerSideServer.sendObject(basicRemote, int.class);
          method = "sendObject(int)";
          break;
        case SENDOBJECT_LONG:
          ret = WSCServerSideServer.sendObject(basicRemote, long.class);
          method = "sendObject(long)";
          break;
        case SENDOBJECT_SHORT:
          ret = WSCServerSideServer.sendObject(basicRemote, short.class);
          method = "sendObject(short)";
          break;
        case SENDTEXT:
          ret = WSCServerSideServer.sendText(basicRemote);
          method = "sendText(String)";
          break;
        case SENDTEXTPART1:
          ret = WSCServerSideServer.sendTextPartial(basicRemote);
          method = "sendText(String, boolean)";
          break;
        case SENDSTREAM:
          ret = WSCServerSideServer.getSendStream(basicRemote);
          method = "getSendStream()";
          break;
        case SENDWRITER:
          ret = WSCServerSideServer.getSendWriter(basicRemote);
          method = "getSendWriter()";
          break;
        case BATCHING_ALLOWED:
          ret = WSCServerSideServer.batchingAllowed(basicRemote);
          method = "setBatchingAllowed(!getBatchingAllowed)";
          break;
        case SEND_PING:
          ret = WSCServerSideServer.sendPing(basicRemote);
          method = "sendPing(ByteBuffer)";
          break;
        case SEND_PONG:
          ret = WSCServerSideServer.sendPong(basicRemote);
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

  private void sendOnClientThrows(final OPS op) throws Fault {
    EndpointCallback callback = new BasicEndpointCallback() {
      @Override
      void doBasic(Basic basicRemote) throws Fault {
        String ret = null;
        String method = null;
        switch (op) {
        case SENDBINARYTHROWS:
          ret = WSCServerSideServer.sendBinaryThrows(basicRemote);
          method = "sendBinary(ByteBuffer)";
          break;
        case SENDOBJECTTHROWS:
          ret = WSCServerSideServer.sendObjectThrows(basicRemote);
          method = "sendObject(Object)";
          break;
        case SENDTEXTTHROWS:
          ret = WSCServerSideServer.sendTextThrows(basicRemote);
          method = "sendText(String)";
          break;
        case SEND_PING_THROWS:
          ret = WSCServerSideServer.sendPingThrows(basicRemote);
          method = "sendPing(<too_long_message>)";
          break;
        case SEND_PONG_THROWS:
          ret = WSCServerSideServer.sendPongThrows(basicRemote);
          method = "sendPong(<too_long_message>)";
          break;
        default:
          fault("Method", op, "not implemented");
        }
        assertEquals(RESPONSE[0], ret, method,
            "does not throw IllegalArgumentException as expected");
        logMsg(method, "throws IllegalArgumentException as expected");
        try {
          basicRemote.sendText(entity.getEntityAt(String.class, 0));
        } catch (IOException e) {
          fault(e);
        }
      }
    };
    setClientCallback(callback);
    invoke("client", ECHO, ECHO);
  }

  private void sendOnClientThrowsException(final OPS op, final String exception)
      throws Fault {
    EndpointCallback callback = new BasicEndpointCallback() {
      @Override
      void doBasic(Basic basicRemote) throws Fault {
        String ret = null;
        String method = null;
        switch (op) {
        case SENDOBJECTTHROWSENCODEEEXCEPTION:
          ret = WSCServerSideServer
              .sendObjectThrowsEncodeException(basicRemote);
          method = "sendObject(Object)";
          break;
        default:
          fault("Method", op, "not implemented");
        }
        assertEquals(RESPONSE[0], ret, method, "does not throw", exception,
            "as expected");
        logMsg(method, "throws", exception, "as expected");
        try {
          basicRemote.sendText(entity.getEntityAt(String.class, 0));
        } catch (IOException e) {
          fault(e);
        }
      }
    };
    // Add ThrowingEncoder encoder just for sendObject methods
    List<Class<? extends Encoder>> list = new LinkedList<Class<? extends Encoder>>();
    list.add(ThrowingEncoder.class);
    ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
        .encoders(list).build();
    setClientEndpointConfig(config);
    setClientCallback(callback);
    invoke("client", ECHO, ECHO);
  }
}
