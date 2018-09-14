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

package com.sun.ts.tests.websocket.ee.javax.websocket.session;

import com.sun.ts.tests.websocket.common.client.ClientEndpoint;
import com.sun.ts.tests.websocket.common.client.EndpointCallback;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.common.util.MessageValidator;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.websocket.*;

public class WSClient extends WebSocketCommonClient {

  private static final String CONTEXT_ROOT = "/ws_session_web";

  private static final String SENT_STRING_MESSAGE = "Hello World in String";

  private static ByteBuffer SENT_BYTE_MESSAGE = ByteBuffer
      .allocate("Hello World in ByteBuffer".getBytes().length + 1);

  private static StringBuffer receivedMessageString = new StringBuffer();

  static CountDownLatch messageLatch;

  public static void main(String[] args) {
    new WSClient().run(args);
  }

  public WSClient() {
    setContextRoot("ws_session_web");
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ws_wait; ts_home;
   */
  /* Run test */
  /*
   * @testName: isOpenTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:130; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-4.4-1;
   * WebSocket:SPEC:WSC-4.4-2;
   *
   * @test_Strategy:
   */
  public void isOpenTest() throws Fault {
    String testName = "isOpenTest";
    String search = "TCKTestServer opened|"
        + "session from Server is open=TRUE";

    setCountDownLatchCount(2);
    setClientCallback(new TCKGetIdEndpoint());
    setProperty(Property.REQUEST, buildRequest("TCKTestServer"));
    setProperty(Property.UNORDERED_SEARCH_STRING, search);
    invoke(false);

    search = "TCKTestServer received String: testName=" + testName + "|"
        + "session from Server is still open=TRUE";
    setProperty(Property.UNORDERED_SEARCH_STRING, search);
    setProperty(Property.CONTENT, "testName=isOpenTest");
    invokeAgain(true);
  }

  /*
   * @testName: isOpenTest1
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:130; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-4.4-1;
   * WebSocket:SPEC:WSC-4.4-2;
   *
   * @test_Strategy:
   */
  public void isOpenTest1() throws Fault {
    String testName = "isOpenTest";
    String search = "TCKTestServer opened|"
        + "session from Server is open=TRUE";

    setCountDownLatchCount(2);
    setClientCallback(new TCKGetIdEndpoint());
    setProperty(Property.REQUEST, buildRequest("TCKTestServer"));
    setProperty(Property.UNORDERED_SEARCH_STRING, search);
    invoke(false);

    try {
      ClientEndpoint.getCountDownLatch().await(_ws_wait, TimeUnit.SECONDS);
      if (!getSession().isOpen()) {
        throw new Fault("Client side Session is not open");
      }
    } catch (Exception ioe) {
      fault(ioe);
    }
    invoke(true);
  }

  /*
   * @testName: addMessageHandlerBasicStringTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:121; WebSocket:JAVADOC:122; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1;
   *
   * @test_Strategy:
   */
  public void addMessageHandlerBasicStringTest() throws Fault {
    String search = "Expected IllegalStateException thrown by Second TextMessageHandler|"
        + "First TextMessageHander received|" + "TCKTestServerString opened|"
        + "First TextMessageHander received|"
        + "TCKTestServerString received String: Hello World in String|"
        + "First TextMessageHander received|" + "TCKTestServerString responds|"
        + "TCKTestServerString received String: |"
        + "First TextMessageHander received|" + "TCKTestServerString responds";
    setCountDownLatchCount(3);
    setClientEndpoint(TCKBasicStringEndpoint.class);
    setProperty(Property.REQUEST, buildRequest("TCKTestServerString"));
    setProperty(Property.UNORDERED_SEARCH_STRING, search);
    invoke(true);
  }

  /*
   * @testName: addMessageHandlerBasicByteBufferTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:105;
   * WebSocket:JAVADOC:121; WebSocket:JAVADOC:122; WebSocket:SPEC:WSC-2.1.3-1;
   * WebSocket:SPEC:WSC-2.1.3-2; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1;
   *
   * @test_Strategy:
   */
  public void addMessageHandlerBasicByteBufferTest() throws Fault {
    String search = "Expected IllegalStateException thrown by Second ByteBuffer MessageHandler|"
        + "First Basic ByteBuffer MessageHander received|"
        + "TCKTestServerByte opened|"
        + "First Basic ByteBuffer MessageHander received|"
        + "TCKTestServerByte received ByteBuffer: Hello World in ByteBuffer|"
        + "TCKTestServerByte responds: Message in bytes";
    setCountDownLatchCount(2);
    setClientEndpoint(TCKBasicByteEndpoint.class);
    setProperty(Property.REQUEST, buildRequest("TCKTestServerByte"));
    setProperty(Property.UNORDERED_SEARCH_STRING, search);
    invoke(true);
  }

  /*
   * @testName: addMessageHandlersTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:105;
   * WebSocket:JAVADOC:112; WebSocket:JAVADOC:121; WebSocket:JAVADOC:122;
   * WebSocket:JAVADOC:134; WebSocket:JAVADOC:147; WebSocket:SPEC:WSC-2.1.3-1;
   * WebSocket:SPEC:WSC-2.1.3-2; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-4.4-1;
   * WebSocket:SPEC:WSC-4.4-2;
   * 
   * @test_Strategy:
   */
  public void addMessageHandlersTest() throws Fault {
    boolean passed = true;
    String message_sent_bytebuffer = "BasicByteBufferMessageHandler added";
    String message_sent_string = "BasicStringMessageHandler added";

    try {
      messageLatch = new CountDownLatch(30);
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      final Session session = clientContainer
          .connectToServer(TCKBasicEndpoint.class, config, new URI("ws://"
              + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      Set<MessageHandler> msgHanders = session.getMessageHandlers();
      receivedMessageString
          .append("Start with MessageHandler=" + msgHanders.size());
      session.getBasicRemote().sendText(message_sent_string);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      ByteBuffer data = ByteBuffer
          .allocate(message_sent_bytebuffer.getBytes().length);
      data.put(message_sent_bytebuffer.getBytes());
      data.flip();
      session.getBasicRemote().sendBinary(data);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      for (MessageHandler msgHander : msgHanders) {
        session.removeMessageHandler(msgHander);
        receivedMessageString
            .append("MessageHandler=" + session.getMessageHandlers().size());
      }

      passed = MessageValidator.checkSearchStrings("TCKBasicEndpoint OnOpen|"
          + "TCKTestServer opened|" + "Start with MessageHandler=2|"
          + "TCKTestServer received String:|" + message_sent_string + "|"
          + "TCKTestServer responds, please close your session|"
          + "Basic ByteBuffer MessageHander received|"
          + "TCKTestServer received ByteBuffer:|"
          + "Basic ByteBuffer MessageHander received|" + message_sent_bytebuffer
          + "|" + "Basic ByteBuffer MessageHander received|"
          + "TCKTestServer responds: Message in bytes|" + "MessageHandler=1|"
          + "MessageHandler=0", receivedMessageString.toString());
      session.close();

    } catch (Exception e) {
      passed = false;
      e.printStackTrace();
    }

    System.out.println(receivedMessageString.toString());
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  /*
   * @testName: addMessageHandlersTest1
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:103;
   * WebSocket:JAVADOC:105; WebSocket:JAVADOC:112; WebSocket:JAVADOC:121;
   * WebSocket:JAVADOC:122; WebSocket:JAVADOC:134; WebSocket:JAVADOC:147;
   * WebSocket:SPEC:WSC-2.1.3-1; WebSocket:SPEC:WSC-2.1.3-2;
   * WebSocket:SPEC:WSC-2.2.2-1; WebSocket:SPEC:WSC-2.2.3-1;
   *
   * @test_Strategy:
   */
  public void addMessageHandlersTest1() throws Fault {
    boolean passed = true;
    final String message_sent_bytebuffer = "BasicByteBufferMessageHandler added";
    final String message_sent_reader = "BasicTextReaderMessageHandler added";
    final String message_reader_msghandler = "BasicReaderMessageHander received=";

    try {
      messageLatch = new CountDownLatch(50);
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      final Session session = clientContainer
          .connectToServer(TCKBasicEndpoint1.class, config, new URI("ws://"
              + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));

      Set<MessageHandler> msgHanders_1 = session.getMessageHandlers();
      receivedMessageString
          .append("Start with MessageHandler=" + msgHanders_1.size());
      session.addMessageHandler(new MessageHandler.Whole<Reader>() {

        @Override
        public void onMessage(Reader r) {
          char[] buffer = new char[128];
          try {
            int i = r.read(buffer);
            receivedMessageString.append("========" + message_reader_msghandler
                + new String(buffer, 0, i));
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      });

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      Writer writer = session.getBasicRemote().getSendWriter();
      writer.append(message_sent_reader);
      writer.close();
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      Set<MessageHandler> msgHanders_2 = session.getMessageHandlers();
      receivedMessageString
          .append("Now we Have MessageHandler=" + msgHanders_2.size());

      session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {

        @Override
        public void onMessage(ByteBuffer data) {
          byte[] data1 = new byte[data.remaining()];
          data.get(data1);
          receivedMessageString.append(new String(data1));
          messageLatch.countDown();
        }
      });

      ByteBuffer data = ByteBuffer
          .allocate((message_sent_bytebuffer).getBytes().length);
      data.put((message_sent_bytebuffer).getBytes());
      data.flip();
      session.getBasicRemote().sendBinary(data);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      Set<MessageHandler> msgHanders_3 = session.getMessageHandlers();
      receivedMessageString
          .append("Now it is MessageHandler=" + msgHanders_3.size());

      for (MessageHandler msgHander : msgHanders_3) {
        session.removeMessageHandler(msgHander);
        receivedMessageString
            .append("MessageHandler=" + session.getMessageHandlers().size());
      }

      passed = MessageValidator.checkSearchStrings(
          "Start with MessageHandler=0|" + message_reader_msghandler + "|"
              + "TCKTestServer received String:|" + message_sent_reader + "|"
              + "TCKTestServer responds, please close your session|"
              + "Now we Have MessageHandler=1|"
              + "TCKTestServer received ByteBuffer:|" + message_sent_bytebuffer
              + "|" + "TCKTestServer responds: Message in bytes|"
              + "Now it is MessageHandler=2|" + "MessageHandler=1|"
              + "MessageHandler=0",
          receivedMessageString.toString());

      session.close();

    } catch (Exception e) {
      passed = false;
      e.printStackTrace();
    }

    System.out.println(receivedMessageString.toString());

    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  /*
   * @testName: closeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:123; WebSocket:JAVADOC:145; WebSocket:SPEC:WSC-4.5-1;
   *
   * @test_Strategy:
   */
  public void closeTest() throws Fault {
    boolean passed = true;

    try {
      messageLatch = new CountDownLatch(20);
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      session.close();

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "TCKBasicEndpoint OnOpen|" + "TCKTestServer opened|"
              + "session from Server is open=TRUE|"
              + "TCKBasicEndpoint OnClose CloseCode=NORMAL_CLOSURE",
          receivedMessageString.toString());

      if (session.isOpen()) {
        passed = false;
        receivedMessageString.append(
            "=================Session stays open after calling close()");
      }

    } catch (Exception e) {
      e.printStackTrace();
      passed = false;
    }

    System.out.println(receivedMessageString.toString());
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  /*
   * @testName: close1Test
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:123; WebSocket:JAVADOC:145; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-2.1.5-1;
   * WebSocket:SPEC:WSC-4.5-1;
   *
   * @test_Strategy:
   */
  public void close1Test() throws Fault {
    String testName = "close1Test";
    boolean passed = true;

    try {
      messageLatch = new CountDownLatch(15);
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("testName=" + testName);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "TCKTestServer opened|" + "session from Server is open=TRUE|"
              + "TCKBasicEndpoint OnClose CloseCode=NORMAL_CLOSURE",
          receivedMessageString.toString());

      if (session.isOpen()) {
        passed = false;
        receivedMessageString.append(
            "=================Session stays open after calling close() from server side");
        session.close();
      }
    } catch (Exception e) {
      passed = false;
      receivedMessageString.append(e.getMessage());
    }
    System.out.println(receivedMessageString.toString());
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  /*
   * @testName: close2Test
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:125; WebSocket:JAVADOC:145; WebSocket:JAVADOC:19;
   * WebSocket:JAVADOC:20; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-2.1.5-1;
   * WebSocket:SPEC:WSC-4.5-1;
   *
   * @test_Strategy:
   */
  public void close2Test() throws Fault {
    String testName = "close2Test";
    boolean passed = true;

    try {
      messageLatch = new CountDownLatch(15);

      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("testName=" + testName);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "TCKBasicEndpoint OnOpen|" + "TCKTestServer opened|"
              + "session from Server is open=TRUE|"
              + "TCKBasicEndpoint OnClose CloseCode=TOO_BIG|"
              + "TCKBasicEndpoint OnClose ReasonPhrase=TCKCloseNowWithReason",
          receivedMessageString.toString());

      if (session.isOpen()) {
        passed = false;
        receivedMessageString.append(
            "=================Session stays open after calling close() from server side");
        session.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault(e);
    }
    System.out.println(receivedMessageString.toString());
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  /*
   * @testName: close3Test
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:123; WebSocket:JAVADOC:145; WebSocket:SPEC:WSC-4.5-1;
   *
   * @test_Strategy:
   */
  public void close3Test() throws Fault {
    boolean passed = true;

    try {
      messageLatch = new CountDownLatch(20);
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/WSCloseTestServer"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.close();
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "TCKBasicEndpoint OnOpen|" + "WSCloseTestServer opened|"
              + "session from WSCloseTestServer is open=TRUE|"
              + "TCKBasicEndpoint OnClose CloseCode=NORMAL_CLOSURE",
          receivedMessageString.toString());

      if (session.isOpen()) {
        passed = false;
        receivedMessageString.append(
            "=================Session stays open after calling close()");
      }

    } catch (Exception e) {
      e.printStackTrace();
      passed = false;
    }

    System.out.println(receivedMessageString.toString());
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  /*
   * @testName: close4Test
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:123; WebSocket:JAVADOC:145; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-2.1.5-1;
   * WebSocket:SPEC:WSC-4.5-1;
   *
   * @test_Strategy:
   */
  public void close4Test() throws Fault {
    String testName = "close1Test";
    boolean passed = true;

    try {
      messageLatch = new CountDownLatch(15);
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/WSCloseTestServer"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("testName=" + testName);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "TCKBasicEndpoint OnOpen|" + "WSCloseTestServer opened|"
              + "session from WSCloseTestServer is open=TRUE|"
              + "TCKBasicEndpoint OnClose CloseCode=NORMAL_CLOSURE",
          receivedMessageString.toString());

      if (session.isOpen()) {
        passed = false;
        receivedMessageString.append(
            "=================Session stays open after calling close() from server side");
        session.close();
      }
    } catch (Exception e) {
      passed = false;
      receivedMessageString.append(e.getMessage());
    }
    System.out.println(receivedMessageString.toString());
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  /*
   * @testName: close5Test
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:125; WebSocket:JAVADOC:145; WebSocket:JAVADOC:19;
   * WebSocket:JAVADOC:20; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-2.1.5-1;
   * WebSocket:SPEC:WSC-4.5-1;
   *
   * @test_Strategy:
   */
  public void close5Test() throws Fault {
    String testName = "close2Test";
    boolean passed = true;

    try {
      messageLatch = new CountDownLatch(15);

      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/WSCloseTestServer"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("testName=" + testName);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "TCKBasicEndpoint OnOpen|" + "WSCloseTestServer opened|"
              + "session from WSCloseTestServer is open=TRUE|"
              + "TCKBasicEndpoint OnClose CloseCode=TOO_BIG|"
              + "TCKBasicEndpoint OnClose ReasonPhrase=TCKCloseNowWithReason",
          receivedMessageString.toString());

      if (session.isOpen()) {
        passed = false;
        receivedMessageString.append(
            "=================Session stays open after calling close() from server side");
        session.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault(e);
    }
    System.out.println(receivedMessageString.toString());
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  /*
   * @testName: close6Test
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:123; WebSocket:JAVADOC:145; WebSocket:SPEC:WSC-4.5-1;
   *
   * @test_Strategy:
   */
  public void close6Test() throws Fault {
    boolean passed = true;

    try {
      messageLatch = new CountDownLatch(20);
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/WSCloseTestServer1"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      session.close();

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "TCKBasicEndpoint OnOpen|" + "WSCloseTestServer1 opened|"
              + "session from WSCloseTestServer1 is open=TRUE|"
              + "TCKBasicEndpoint OnClose CloseCode=NORMAL_CLOSURE",
          receivedMessageString.toString());

      if (session.isOpen()) {
        passed = false;
        receivedMessageString.append(
            "=================Session stays open after calling close()");
      }

    } catch (Exception e) {
      e.printStackTrace();
      passed = false;
    }

    System.out.println(receivedMessageString.toString());
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  /*
   * @testName: close7Test
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:123; WebSocket:JAVADOC:145; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-2.1.5-1;
   * WebSocket:SPEC:WSC-4.5-1;
   *
   * @test_Strategy:
   */
  public void close7Test() throws Fault {
    String testName = "close1Test";
    boolean passed = true;

    try {
      messageLatch = new CountDownLatch(15);
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/WSCloseTestServer1"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("testName=" + testName);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "TCKBasicEndpoint OnOpen|" + "WSCloseTestServer1 opened|"
              + "session from WSCloseTestServer1 is open=TRUE|"
              + "TCKBasicEndpoint OnClose CloseCode=NORMAL_CLOSURE",
          receivedMessageString.toString());

      if (session.isOpen()) {
        passed = false;
        receivedMessageString.append(
            "=================Session stays open after calling close() from server side");
        session.close();
      }
    } catch (Exception e) {
      passed = false;
      receivedMessageString.append(e.getMessage());
    }
    System.out.println(receivedMessageString.toString());
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  /*
   * @testName: close8Test
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:125; WebSocket:JAVADOC:145; WebSocket:JAVADOC:19;
   * WebSocket:JAVADOC:20; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-2.1.5-1;
   * WebSocket:SPEC:WSC-4.5-1;
   *
   * @test_Strategy:
   */
  public void close8Test() throws Fault {
    String testName = "close2Test";
    boolean passed = true;

    try {
      messageLatch = new CountDownLatch(15);

      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/WSCloseTestServer1"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("testName=" + testName);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "TCKBasicEndpoint OnOpen|" + "WSCloseTestServer1 opened|"
              + "session from WSCloseTestServer1 is open=TRUE|"
              + "TCKBasicEndpoint OnClose CloseCode=TOO_BIG|"
              + "TCKBasicEndpoint OnClose ReasonPhrase=TCKCloseNowWithReason",
          receivedMessageString.toString());

      if (session.isOpen()) {
        passed = false;
        receivedMessageString.append(
            "=================Session stays open after calling close() from server side");
        session.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault(e);
    }
    System.out.println(receivedMessageString.toString());
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  /*
   * @testName: close9Test
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:123; WebSocket:JAVADOC:145; WebSocket:SPEC:WSC-4.5-1;
   *
   * @test_Strategy:
   */
  public void close9Test() throws Fault {
    boolean passed = true;

    try {
      messageLatch = new CountDownLatch(20);
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/WSCloseTestServer2"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      session.close();

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "TCKBasicEndpoint OnOpen|" + "WSCloseTestServer2 opened|"
              + "session from WSCloseTestServer2 is open=TRUE|"
              + "TCKBasicEndpoint OnClose CloseCode=NORMAL_CLOSURE",
          receivedMessageString.toString());

      if (session.isOpen()) {
        passed = false;
        receivedMessageString.append(
            "=================Session stays open after calling close()");
      }

    } catch (Exception e) {
      e.printStackTrace();
      passed = false;
    }

    System.out.println(receivedMessageString.toString());
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  /*
   * @testName: close10Test
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:123; WebSocket:JAVADOC:145; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-2.1.5-1;
   * WebSocket:SPEC:WSC-4.5-1;
   *
   * @test_Strategy:
   */
  public void close10Test() throws Fault {
    String testName = "close1Test";
    boolean passed = true;

    try {
      messageLatch = new CountDownLatch(15);
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/WSCloseTestServer2"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("testName=" + testName);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "TCKBasicEndpoint OnOpen|" + "WSCloseTestServer2 opened|"
              + "session from WSCloseTestServer2 is open=TRUE|"
              + "TCKBasicEndpoint OnClose CloseCode=NORMAL_CLOSURE",
          receivedMessageString.toString());

      if (session.isOpen()) {
        passed = false;
        receivedMessageString.append(
            "=================Session stays open after calling close() from server side");
        session.close();
      }
    } catch (Exception e) {
      passed = false;
      receivedMessageString.append(e.getMessage());
    }
    System.out.println(receivedMessageString.toString());
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  /*
   * @testName: close11Test
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:125; WebSocket:JAVADOC:145; WebSocket:JAVADOC:19;
   * WebSocket:JAVADOC:20; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-2.1.5-1;
   * WebSocket:SPEC:WSC-4.5-1;
   *
   * @test_Strategy:
   */
  public void close11Test() throws Fault {
    String testName = "close2Test";
    boolean passed = true;

    try {
      messageLatch = new CountDownLatch(15);

      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/WSCloseTestServer2"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("testName=" + testName);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "TCKBasicEndpoint OnOpen|" + "WSCloseTestServer2 opened|"
              + "session from WSCloseTestServer2 is open=TRUE|"
              + "TCKBasicEndpoint OnClose CloseCode=TOO_BIG|"
              + "TCKBasicEndpoint OnClose ReasonPhrase=TCKCloseNowWithReason",
          receivedMessageString.toString());

      if (session.isOpen()) {
        passed = false;
        receivedMessageString.append(
            "=================Session stays open after calling close() from server side");
        session.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault(e);
    }
    System.out.println(receivedMessageString.toString());
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  /*
   * @testName: close12Test
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:125; WebSocket:JAVADOC:145; WebSocket:JAVADOC:19;
   * WebSocket:JAVADOC:20; WebSocket:SPEC:WSC-2.1.5-1;
   * WebSocket:SPEC:WSC-2.2.2-1; WebSocket:SPEC:WSC-2.2.3-1;
   * WebSocket:SPEC:WSC-4.5-2;
   *
   * @test_Strategy:
   */
  public void close12Test() throws Fault {
    boolean passed = true;

    try {
      messageLatch = new CountDownLatch(15);

      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKCloseEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.close();
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "TCKCloseEndpoint OnOpen|" + "TCKTestServer opened|"
              + "TCKCloseEndpoint OnClose|" + "TCKCloseEndpoint OnError",
          receivedMessageString.toString());
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      if (session.isOpen()) {
        passed = false;
        receivedMessageString.append(
            "=================Session stays open after calling close() from server side");
        session.close();
      }
    } catch (Exception e) {
      passed = false;
      e.printStackTrace();
    }
    System.out.println(receivedMessageString.toString());
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  /*
   * @testName: getContainerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:129;
   *
   * @test_Strategy:
   */
  public void getContainerTest() throws Fault {
    boolean passed = true;
    messageLatch = new CountDownLatch(15);

    try {
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      WebSocketContainer tmp = session.getContainer();

      if (clientContainer != tmp) {
        passed = false;
        System.err.println("Incorrect return from getContainer" + tmp);
        System.err.println("Expecting " + clientContainer);
      }

      session.close();

      if (!passed) {
        throw new Fault("Test failed with incorrect response");
      }

    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault(e);
    }
  }

  /*
   * @testName: getId1Test
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:130; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1;
   *
   * @test_Strategy:
   */
  public void getId1Test() throws Fault {
    boolean passed = true;
    String testName = "getId1Test";
    messageLatch = new CountDownLatch(5);

    try {
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));

      String tmp = session.getId();

      receivedMessageString.append("getId returned from client side" + tmp);

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("testName=" + testName);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      System.out.println(receivedMessageString.toString());

      passed = MessageValidator.checkSearchStrings(
          "TCKBasicEndpoint OnOpen|" + "TCKTestServer opened|"
              + "session from Server is open=TRUE|"
              + "TCKTestServer received String: testName=" + testName,
          receivedMessageString.toString());

      session.close();

      if (!passed) {
        throw new Fault("Test failed with incorrect response");
      }

    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault(e);
    }
  }

  /*
   * @testName: setMaxBinaryMessageBufferSizeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:131; WebSocket:JAVADOC:148;
   *
   * @test_Strategy:
   */
  public void setMaxBinaryMessageBufferSizeTest() throws Fault {
    int size = 98765432;
    boolean passed = true;

    try {
      messageLatch = new CountDownLatch(15);
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));

      receivedMessageString
          .append("getMaxBinaryMessageBufferSize returned default value ="
              + session.getMaxBinaryMessageBufferSize());
      session.setMaxBinaryMessageBufferSize(size);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      int tmp = session.getMaxBinaryMessageBufferSize();
      if (tmp == size) {
        receivedMessageString.append(
            "getMaxBinaryMessageBufferSize returned expected value =" + tmp);
      } else {
        passed = false;
        receivedMessageString.append(
            "getMaxBinaryMessageBufferSize returned unexpected value =" + tmp);
        receivedMessageString.append("expected value =" + size);
      }

      session.close();

    } catch (Exception e) {
      passed = false;
      receivedMessageString.append(e.getMessage());
    }

    System.out.println(receivedMessageString.toString());
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  /*
   * @testName: setMaxTextMessageBufferSizeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:133; WebSocket:JAVADOC:150;
   *
   * @test_Strategy:
   */
  public void setMaxTextMessageBufferSizeTest() throws Fault {
    int size = 987654321;
    messageLatch = new CountDownLatch(1); // not to throw NPE in
                                          // TCKBasicEndpoint

    try {
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));

      System.out.println("getMaxTextMessageBufferSize returned default value ="
          + session.getMaxTextMessageBufferSize());
      session.setMaxTextMessageBufferSize(size);
      int tmp = session.getMaxTextMessageBufferSize();
      if (tmp == size) {
        System.out.println(
            "getMaxTextMessageBufferSize returned expected value =" + tmp);
      } else {
        System.out.println(
            "getMaxTextMessageBufferSize returned unexpected value =" + tmp);
        System.out.println("expected value =" + size);
      }

      session.close();
    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault(e);
    }
  }

  /*
   * @testName: setTimeoutTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:132; WebSocket:JAVADOC:149;
   *
   * @test_Strategy:
   */
  public void setTimeoutTest() throws Fault {
    long tt = 9876543210L;
    messageLatch = new CountDownLatch(1); // not to throw NPE in
                                          // TCKBasicEndpoint

    try {
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));

      System.out
          .println("getMaxIdleTimeout returned default value on client side ="
              + session.getMaxIdleTimeout());
      session.setMaxIdleTimeout(tt);
      long tmp = session.getMaxIdleTimeout();
      if (tmp == tt) {
        System.out.println("getMaxIdleTimeout returned expected value =" + tmp);
      } else {
        System.out
            .println("getMaxIdleTimeout returned unexpected value =" + tmp);
        System.out.println("expected value =" + tt);
      }
      session.close();

    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault(e);
    }
  }

  /*
   * @testName: setTimeout1Test
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:132; WebSocket:JAVADOC:149; WebSocket:JAVADOC:140;
   * WebSocket:SPEC:WSC-2.2.2-1; WebSocket:SPEC:WSC-2.2.3-1;
   * WebSocket:SPEC:WSC-4.4-1; WebSocket:SPEC:WSC-4.4-2;
   *
   * @test_Strategy:
   */
  public void setTimeout1Test() throws Fault {
    String testName = "setTimeout1Test";
    boolean passed = true;
    long tt = _ws_wait * 4 * 1000L;
    messageLatch = new CountDownLatch(10);

    try {
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer?timeout=" + _ws_wait));

      System.out
          .println("getMaxIdleTimeout returned default value on client side ="
              + session.getMaxIdleTimeout());
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.setMaxIdleTimeout(tt);
      session.getBasicRemote().sendText("testName=" + testName);
      Thread.sleep(_ws_wait * 8000);
      if (session.isOpen()) {
        passed = false;
        receivedMessageString.append("Session is still open after timeout");
      }

      System.out.println(receivedMessageString.toString());

      boolean tmp = MessageValidator.checkSearchStrings(
          "TCKBasicEndpoint OnOpen|" + "TCKTestServer opened|"
              + "session from Server is open=TRUE|"
              + "TCKTestServer received String: testName=setTimeout1Test",
          receivedMessageString.toString());
      if (!tmp) {
        passed = false;
      }

      if (receivedMessageString.indexOf("AnyString=") > -1
          || receivedMessageString
              .indexOf("TCKTestServer second message after sleep") > -1) {
        passed = false;
        System.err.println(
            "Test failed due to message sent and/or received after timeout from client side");

      }
      if (!passed) {
        throw new Fault("Test failed with incorrect response");
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault(e);
    }
  }

  /*
   * @testName: setTimeout2Test
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:132; WebSocket:JAVADOC:149; WebSocket:JAVADOC:140;
   * WebSocket:SPEC:WSC-2.2.2-1; WebSocket:SPEC:WSC-2.2.3-1;
   * WebSocket:SPEC:WSC-4.4-1; WebSocket:SPEC:WSC-4.4-2;
   *
   * @test_Strategy:
   */
  public void setTimeout2Test() throws Fault {
    String testName = "setTimeout2Test";
    boolean passed = true;
    messageLatch = new CountDownLatch(5);

    try {
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer?timeout=" + _ws_wait));

      System.out
          .println("getMaxIdleTimeout returned default value on client side ="
              + session.getMaxIdleTimeout());
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("testName=" + testName);
      Thread.sleep(_ws_wait * 8000);
      if (session.isOpen()) {
        passed = false;
        receivedMessageString.append("Session is still open after timeout");
      }

      System.out.println(receivedMessageString.toString());
      boolean tmp = MessageValidator.checkSearchStrings(
          "TCKBasicEndpoint OnOpen|" + "TCKTestServer opened|"
              + "session from Server is open=TRUE|"
              + "TCKTestServer received String: testName=setTimeout2Test",
          receivedMessageString.toString());

      if (!tmp) {
        passed = false;
      }

      if (receivedMessageString.indexOf("AnyString=") > -1
          || receivedMessageString
              .indexOf("TCKTestServer second message after sleep") > -1) {
        passed = false;
        System.err.println(
            "Test failed due to message sent and/or received after timeout from server side");

      }

      if (!passed) {
        throw new Fault("Test Failed");
      }

    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault(e);
    }
  }

  /*
   * @testName: getQueryStringTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:140; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-4.4-1;
   * WebSocket:SPEC:WSC-4.4-2;
   *
   * @test_Strategy:
   */
  public void getQueryStringTest() throws Fault {
    String testName = "getQueryStringTest";
    boolean passed = true;
    messageLatch = new CountDownLatch(5);
    String querystring = "test1=value1&test2=value2&test3=value3";

    try {
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer?" + querystring));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("testName=" + testName);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      System.out.println(receivedMessageString.toString());
      passed = MessageValidator.checkSearchStrings(
          "TCKBasicEndpoint OnOpen|" + "TCKTestServer opened|"
              + "session from Server is open=TRUE|"
              + "TCKTestServer received String: testName=" + testName + "|"
              + "TCKTestServer: expected Query String returned|" + querystring,
          receivedMessageString.toString());

      if (!passed) {
        throw new Fault("Test Failed");
      }

    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault(e);
    }
  }

  /*
   * @testName: getPathParametersTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:SPEC:WSC-2.2.2-1; WebSocket:JAVADOC:138;
   * WebSocket:SPEC:WSC-4.4-1; WebSocket:SPEC:WSC-4.4-2;
   *
   * @test_Strategy:
   */
  public void getPathParametersTest() throws Fault {
    String testName = "getPathParametersTest";
    String message = "invoke test";
    boolean passed = true;
    messageLatch = new CountDownLatch(5);
    String param1 = "test1";
    String param2 = "test2=xyz";

    try {
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServerPathParam/" + param1 + "/" + param2));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText(message);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator
          .checkSearchStrings(
              "TCKBasicEndpoint OnOpen|" + "WSTestServerPathParam opened|"
                  + "WSTestServerPathParam received String: " + message + "|"
                  + "WSTestServerPathParam: pathparams returned;param1="
                  + param1 + ";param2=" + param2,
              receivedMessageString.toString());

      session.close();

    } catch (Exception e) {
      passed = false;
      e.printStackTrace();
    }

    System.out.println(receivedMessageString.toString());

    if (!passed) {
      throw new Fault("Test Failed");
    }
  }

  /*
   * @testName: getRequestURITest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:142; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-4.4-1;
   * WebSocket:SPEC:WSC-4.4-2;
   *
   * @test_Strategy:
   */
  public void getRequestURITest() throws Fault {
    String testName = "getRequestURITest";
    boolean passed = true;
    messageLatch = new CountDownLatch(6);
    String querystring = "test1=value1&test2=value2&test3=value3";

    try {
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer?" + querystring));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("testName=" + testName);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      System.out.println(receivedMessageString.toString());
      passed = MessageValidator.checkSearchStrings("TCKBasicEndpoint OnOpen|"
          + "TCKTestServer opened|" + "session from Server is open=TRUE|"
          + "TCKTestServer received String: testName=" + testName + "|"
          + "TCKTestServer: getRequestURI returned=/ws_session_web/TCKTestServer",
          receivedMessageString.toString());

      if (!passed) {
        throw new Fault("Test Failed");
      }

    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault(e);
    }
  }

  /*
   * @testName: getProtocolVersionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:139; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-4.4-1;
   *
   * @test_Strategy:
   */
  public void getProtocolVersionTest() throws Fault {
    String expectedProtocolVersion = "13";
    String testName = "getProtocolVersionTest";
    String search = "TCKTestServer opened|"
        + "session from Server is open=TRUE";
    setCountDownLatchCount(2);
    setClientCallback(new TCKGetIdEndpoint());
    setProperty(Property.REQUEST, buildRequest("TCKTestServer"));
    setProperty(Property.SEARCH_STRING, search);
    invoke(false);

    assertEquals(expectedProtocolVersion, getSession().getProtocolVersion(),
        "getProtocolVersion() is not", expectedProtocolVersion, "as expected");
    logMsg("getProtocolVersion() is", expectedProtocolVersion, "as expected");

    setProperty(Property.UNORDERED_SEARCH_STRING,
        "TCKTestServer received String:");
    setProperty(Property.UNORDERED_SEARCH_STRING,
        "testName=getProtocolVersionTest");
    setProperty(Property.UNORDERED_SEARCH_STRING,
        "TCKTestServer: getProtocolVersion returned=13");
    setProperty(Property.CONTENT, "testName=" + testName);
    invokeAgain(true);
  }

  /*
   * @testName: getOpenSessionsTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:137; WebSocket:SPEC:WSC-2.2.2-1;
   * WebSocket:SPEC:WSC-2.2.3-1;
   *
   * @test_Strategy:
   */
  public void getOpenSessionsTest() throws Fault {
    boolean passed = true;
    String testName = "getOpenSessionsTest";
    messageLatch = new CountDownLatch(30);

    try {
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      Session session = clientContainer
          .connectToServer(TCKOpenSessionEndpoint.class, config, new URI("ws://"
              + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("testName=" + testName);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      int os = getOpenSessions(receivedMessageString.toString());

      Session session1 = clientContainer
          .connectToServer(TCKOpenSessionEndpoint.class, config, new URI("ws://"
              + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session1.getBasicRemote().sendText("testName=" + testName);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      int os1 = getOpenSessions(receivedMessageString.toString());

      Session session2 = clientContainer
          .connectToServer(TCKOpenSessionEndpoint.class, config, new URI("ws://"
              + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session2.getBasicRemote().sendText("testName=" + testName);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      int os2 = getOpenSessions(receivedMessageString.toString());

      session.close();

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session1.getBasicRemote().sendText("testName=" + testName);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      int os3 = getOpenSessions(receivedMessageString.toString());

      session1.close();

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session2.getBasicRemote().sendText("testName=" + testName);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      int os4 = getOpenSessions(receivedMessageString.toString());

      session2.close();

      if (os == 1) {
        if (os1 != 2 || os2 != 3) {
          passed = false;
          System.out.print("Not perfect 12321 pattern for open session");
        }
      }

      if (os4 > os3 || os3 > os2) {
        passed = false;
        System.out.print("Incorrect XXX++ pattern for open session");
      }

      if (os < 1 || os1 < 2 || os2 < 3 || os3 < 2 || os4 < 1) {
        passed = false;
        System.out.print("Cannot have less open session than 12321");
      }

      if (os1 - os > 1 || os2 - os1 > 1) {
        passed = false;
        System.out.print("Too many session are open");
      }

    } catch (Exception ioe) {
      passed = false;
      ioe.printStackTrace();
    }
    System.out.println(receivedMessageString.toString());

    if (!passed) {
      throw new Fault("Incorrect response received");
    }
  }

  @Override
  public void cleanup() throws Fault {
    super.cleanup();
  }

  public final static class TCKBasicEndpoint extends Endpoint {

    @Override
    public void onOpen(Session session, EndpointConfig config) {
      receivedMessageString.append("TCKBasicEndpoint OnOpen");
      session.addMessageHandler(new MessageHandler.Whole<String>() {

        public void onMessage(String message) {
          receivedMessageString.append(message);
          messageLatch.countDown();
        }
      });

      session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {

        public void onMessage(ByteBuffer data) {
          byte[] data1 = new byte[data.remaining()];
          data.get(data1);
          receivedMessageString
              .append("========Basic ByteBuffer MessageHander received="
                  + new String(data1));
          messageLatch.countDown();
        }
      });

    }

    public void onClose(Session session, CloseReason closeReason) {
      receivedMessageString.append(
          "TCKBasicEndpoint OnClose CloseCode=" + closeReason.getCloseCode());
      receivedMessageString.append("TCKBasicEndpoint OnClose ReasonPhrase="
          + closeReason.getReasonPhrase());
    }
  }

  public final static class TCKBasicEndpoint1 extends Endpoint {

    @Override
    public void onOpen(Session session, EndpointConfig config) {
    }
  }

  public final static class TCKGetIdEndpoint extends EndpointCallback {

    @Override
    public void onOpen(Session session, EndpointConfig config) {
      ClientEndpoint.getMessageBuilder().append("======Another SessionID=")
          .append(session.getId());
      System.out.println(session.getId());
    }
  }

  public final static class TCKBasicStringEndpoint
      extends ClientEndpoint<String> {

    @Override
    public void onMessage(String message) {
      getMessageBuilder().append("========First TextMessageHander received=")
          .append(message);
      getCountDownLatch().countDown();
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
      super.onOpen(session, config);
      try {
        session.addMessageHandler(new MessageHandler.Whole<String>() {

          public void onMessage(String message) {
            getMessageBuilder()
                .append("========Second TextMessageHander received=")
                .append(message);
            getCountDownLatch().countDown();
          }
        });
      } catch (IllegalStateException ile) {
        getMessageBuilder().append(
            "========Expected IllegalStateException thrown by Second TextMessageHandler");
        // ile.printStackTrace();
      }
      try {
        session.getBasicRemote().sendText(SENT_STRING_MESSAGE);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public final static class TCKBasicByteEndpoint
      extends ClientEndpoint<ByteBuffer> {

    @Override
    public void onMessage(ByteBuffer message) {
      String message_string = IOUtil.byteBufferToString(message);

      getMessageBuilder()
          .append("========First Basic ByteBuffer MessageHander received=")
          .append(message_string);
      getCountDownLatch().countDown();
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
      super.onOpen(session, config);
      SENT_BYTE_MESSAGE.put("Hello World in ByteBuffer".getBytes());
      SENT_BYTE_MESSAGE.flip();
      try {
        session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {

          @Override
          public void onMessage(ByteBuffer data) {
            String message_string = IOUtil.byteBufferToString(data);
            getMessageBuilder()
                .append(
                    "========Second Basic ByteBuffer MessageHander received=")
                .append(message_string);
            getCountDownLatch().countDown();
          }
        });
      } catch (IllegalStateException ile) {
        getMessageBuilder().append(
            "========Expected IllegalStateException thrown by Second ByteBuffer MessageHandler");
        // ile.printStackTrace();
      }
      try {
        session.getBasicRemote().sendBinary(SENT_BYTE_MESSAGE);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public final static class TCKOpenSessionEndpoint extends Endpoint {

    @Override
    public void onOpen(Session session, EndpointConfig config) {

      session.addMessageHandler(new MessageHandler.Whole<String>() {

        public void onMessage(String message) {
          receivedMessageString.append(message);
          messageLatch.countDown();
        }
      });
    }

    public void onClose(Session session, CloseReason closeReason) {
      receivedMessageString.append("onClose");
    }
  }

  public final static class TCKCloseEndpoint extends Endpoint {

    @Override
    public void onOpen(Session session, EndpointConfig config) {
      receivedMessageString.append("TCKCloseEndpoint OnOpen");
      session.addMessageHandler(new MessageHandler.Whole<String>() {

        public void onMessage(String message) {
          receivedMessageString.append(message);
          messageLatch.countDown();
        }
      });

      session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {

        public void onMessage(ByteBuffer data) {
          String message_string = IOUtil.byteBufferToString(data);

          receivedMessageString
              .append("========Basic ByteBuffer MessageHander received="
                  + message_string);
          messageLatch.countDown();
        }
      });
    }

    public void onClose(Session session, CloseReason closeReason) {
      receivedMessageString.append("TCKCloseEndpoint OnClose");
      receivedMessageString.append("Pass_On_To_Error=");
      int i = 1 / 0;
    }

    public void onError(Session session, Throwable t) {
      receivedMessageString.append("TCKCloseEndpoint OnError");
      receivedMessageString.append(t.getMessage());
    }
  }

  private int getOpenSessions(String message) {
    int start = receivedMessageString.lastIndexOf("getOpenSessions=");
    int stop = receivedMessageString
        .lastIndexOf("========TCKTestServer responded");
    int os = Integer
        .parseInt(receivedMessageString.substring(start + 16, stop));
    System.out.println("open session=" + os);
    return os;
  }
}
