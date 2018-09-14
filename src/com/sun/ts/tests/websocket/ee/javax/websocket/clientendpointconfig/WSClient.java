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
 * $Id:$
 */
package com.sun.ts.tests.websocket.ee.javax.websocket.clientendpointconfig;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.websocket.common.TCKExtension;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.util.MessageValidator;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.websocket.*;

public class WSClient extends WebSocketCommonClient {

  private static final String CONTEXT_ROOT = "/ws_ee_clientendpointconfig_web";

  private static StringBuffer receivedMessageString = new StringBuffer();

  static CountDownLatch messageLatch;

  public static void main(String[] args) {
    WSClient theTests = new WSClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ws_wait; ts_home;
   */

  /* Run test */
  /*
   * @testName: constructortest
   * 
   * @assertion_ids: WebSocket:JAVADOC:8; WebSocket:JAVADOC:10;
   * WebSocket:JAVADOC:70; WebSocket:JAVADOC:71; WebSocket:JAVADOC:28;
   * WebSocket:JAVADOC:159; WebSocket:JAVADOC:128; WebSocket:JAVADOC:123;
   * WebSocket:JAVADOC:112; WebSocket:JAVADOC:121; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:135; WebSocket:JAVADOC:136;
   * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-3.2.1-1;
   * WebSocket:SPEC:WSC-3.2.2-1;
   *
   * @test_Strategy: Test constructor
   */
  public void constructortest() throws Fault {
    boolean passed = true;

    try {
      messageLatch = new CountDownLatch(10);

      ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          cec, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("Dummy String Test");
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      session.close();
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "Extension size=0|"
              + "TCKTestServer received String:Dummy String Test|"
              + "TCKTestServer: Extension size=0",
          receivedMessageString.toString());
      if (receivedMessageString.indexOf("subProtocol=|") > -1) {
        receivedMessageString
            .append("Correct subProtocol received from client side");
      } else {
        passed = false;
        receivedMessageString
            .append("Incorrect subProtocol received from client side");
      }

      if (receivedMessageString.indexOf("TCKTestServer: subProtocol==|") > -1) {
        receivedMessageString
            .append("========Correct subProtocol received from server side");
      } else {
        passed = false;
        receivedMessageString
            .append("========Incorrect subProtocol received from server side");
      }
    } catch (Exception e) {
      passed = false;
      e.printStackTrace();
    }
    System.out.println(receivedMessageString);

    if (passed == false) {
      throw new Fault(
          "Test failed with incorrect values, " + receivedMessageString);
    }
  }

  /*
   * @testName: preferredSubprotocolsTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:8; WebSocket:JAVADOC:10;
   * WebSocket:JAVADOC:14; WebSocket:JAVADOC:70; WebSocket:JAVADOC:71;
   * WebSocket:JAVADOC:28; WebSocket:JAVADOC:159; WebSocket:JAVADOC:128;
   * WebSocket:JAVADOC:123; WebSocket:JAVADOC:112; WebSocket:JAVADOC:121;
   * WebSocket:JAVADOC:69; WebSocket:JAVADOC:79; WebSocket:JAVADOC:135;
   * WebSocket:JAVADOC:136; WebSocket:SPEC:WSC-2.2.3-1;
   *
   * @test_Strategy:
   */
  public void preferredSubprotocolsTest() throws Fault {
    boolean passed = true;
    List<String> expected_subprotocols = Arrays.asList("MBWS", "MBLWS", "soap",
        "WAMP", "v10.stomp", "v11.stomp", "v12.stomp");
    try {
      messageLatch = new CountDownLatch(10);

      ClientEndpointConfig cec = ClientEndpointConfig.Builder.create()
          .preferredSubprotocols(expected_subprotocols).build();
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          cec, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("Dummy String Test");
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      session.close();
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "Extension size=0|"
              + "TCKTestServer received String:Dummy String Test|"
              + "TCKTestServer: Extension size=0",
          receivedMessageString.toString());
      if (receivedMessageString.indexOf("subProtocol=|") > -1) {
        receivedMessageString
            .append("Correct subProtocol received from client side");
      } else {
        passed = false;
        receivedMessageString
            .append("Incorrect subProtocol received from client side");
      }

      if (receivedMessageString.indexOf("TCKTestServer: subProtocol==|") > -1) {
        receivedMessageString
            .append("========Correct subProtocol received from server side");
      } else {
        passed = false;
        receivedMessageString
            .append("========Incorrect subProtocol received from server side");
      }
    } catch (IOException ioex) {
      passed = false;
      ioex.printStackTrace();
    } catch (Exception e) {
      passed = false;
      e.printStackTrace();
    }
    System.out.println(receivedMessageString);

    if (passed == false) {
      throw new Fault(
          "Test failed with incorrect values, " + receivedMessageString);
    }
  }

  /*
   * @testName: extensionsTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:8; WebSocket:JAVADOC:10;
   * WebSocket:JAVADOC:13; WebSocket:JAVADOC:70; WebSocket:JAVADOC:71;
   * WebSocket:JAVADOC:28; WebSocket:JAVADOC:159; WebSocket:JAVADOC:128;
   * WebSocket:JAVADOC:123; WebSocket:JAVADOC:112; WebSocket:JAVADOC:121;
   * WebSocket:JAVADOC:69; WebSocket:JAVADOC:79; WebSocket:JAVADOC:135;
   * WebSocket:JAVADOC:136; WebSocket:SPEC:WSC-2.2.3-1;
   *
   * @test_Strategy:
   */
  public void extensionsTest() throws Fault {
    boolean passed = true;
    final List<Extension.Parameter> extension1 = new ArrayList<Extension.Parameter>() {

      {
        add(new TCKExtension.TCKParameter("permessage-compress", "deflate"));
        add(new TCKExtension.TCKParameter("permessage-compress", "foo"));
        add(new TCKExtension.TCKParameter("prop", "val"));
      }
    };

    final List<Extension.Parameter> extension2 = new ArrayList<Extension.Parameter>() {

      {
        add(new TCKExtension.TCKParameter("permessage-compress", "deflate"));
        add(new TCKExtension.TCKParameter("prop1", "val1"));
        add(new TCKExtension.TCKParameter("prop2", "val2"));
      }
    };

    ArrayList<Extension> extensions = new ArrayList<Extension>();
    extensions.add(new TCKExtension("ext1", extension1));
    extensions.add(new TCKExtension("ext2", extension2));

    try {
      messageLatch = new CountDownLatch(10);

      ClientEndpointConfig cec = ClientEndpointConfig.Builder.create()
          .extensions(extensions).build();
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          cec, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("Dummy String Test");
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      session.close();
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "TCKTestServer received String:Dummy String Test",
          receivedMessageString.toString());

      if (receivedMessageString.indexOf("Extension size=0") > -1
          || receivedMessageString.indexOf("Extension size=1") > -1
          || receivedMessageString.indexOf("Extension size=2") > -1) {
        receivedMessageString
            .append("Correct Extensions received from client side");
      } else {
        passed = false;
        receivedMessageString
            .append("Incorrect Extensions received from client side");
      }

      if (receivedMessageString.indexOf("TCKTestServer: Extension size=0") > -1
          || receivedMessageString
              .indexOf("TCKTestServer: Extension size=1") > -1
          || receivedMessageString
              .indexOf("TCKTestServer: Extension size=2") > -1) {
        receivedMessageString
            .append("========Correct Extensions received from server side");
      } else {
        passed = false;
        receivedMessageString
            .append("========Incorrect Extensions received from server side");
      }

    } catch (IOException ioex) {
      passed = false;
      ioex.printStackTrace();
    } catch (Exception e) {
      passed = false;
      e.printStackTrace();
    }
    System.out.println(receivedMessageString);

    if (passed == false) {
      throw new Fault(
          "Test failed with incorrect values, " + receivedMessageString);
    }
  }

  /*
   * @testName: constructorTest1
   * 
   * @assertion_ids: WebSocket:JAVADOC:8; WebSocket:JAVADOC:10;
   * WebSocket:JAVADOC:13; WebSocket:JAVADOC:13; WebSocket:JAVADOC:70;
   * WebSocket:JAVADOC:71; WebSocket:JAVADOC:28; WebSocket:JAVADOC:159;
   * WebSocket:JAVADOC:128; WebSocket:JAVADOC:123; WebSocket:JAVADOC:112;
   * WebSocket:JAVADOC:121; WebSocket:JAVADOC:69; WebSocket:JAVADOC:79;
   * WebSocket:JAVADOC:135; WebSocket:JAVADOC:136; WebSocket:SPEC:WSC-2.2.3-1;
   *
   * @test_Strategy:
   */
  public void constructorTest1() throws Fault {
    boolean passed = true;
    List<String> expected_subprotocols = Arrays.asList("MBWS", "MBLWS", "soap",
        "WAMP", "v10.stomp", "v11.stomp", "v12.stomp");

    final List<Extension.Parameter> extension1 = new ArrayList<Extension.Parameter>() {

      {
        add(new TCKExtension.TCKParameter("permessage-compress", "deflate"));
        add(new TCKExtension.TCKParameter("permessage-compress", "foo"));
        add(new TCKExtension.TCKParameter("prop", "val"));
      }
    };

    final List<Extension.Parameter> extension2 = new ArrayList<Extension.Parameter>() {

      {
        add(new TCKExtension.TCKParameter("permessage-compress", "deflate"));
        add(new TCKExtension.TCKParameter("prop1", "val1"));
        add(new TCKExtension.TCKParameter("prop2", "val2"));
      }
    };

    ArrayList<Extension> extensions = new ArrayList<Extension>();
    extensions.add(new TCKExtension("ext1", extension1));
    extensions.add(new TCKExtension("ext2", extension2));

    try {
      messageLatch = new CountDownLatch(10);
      ClientEndpointConfig cec = ClientEndpointConfig.Builder.create()
          .preferredSubprotocols(expected_subprotocols).extensions(extensions)
          .build();
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          cec, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("Dummy String Test");
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      session.close();
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "TCKTestServer received String:Dummy String Test",
          receivedMessageString.toString());

      if (receivedMessageString.indexOf("subProtocol=|") > -1) {
        receivedMessageString
            .append("Correct subProtocol received from client side");
      } else {
        passed = false;
        receivedMessageString
            .append("Incorrect subProtocol received from client side");
      }

      if (receivedMessageString.indexOf("TCKTestServer: subProtocol==|") > -1) {
        receivedMessageString
            .append("========Correct subProtocol received from server side");
      } else {
        passed = false;
        receivedMessageString
            .append("========Incorrect subProtocol received from server side");
      }

      if (receivedMessageString.indexOf("Extension size=0") > -1
          || receivedMessageString.indexOf("Extension size=1") > -1
          || receivedMessageString.indexOf("Extension size=2") > -1) {
        receivedMessageString
            .append("Correct Extensions received from client side");
      } else {
        passed = false;
        receivedMessageString
            .append("Incorrect Extensions received from client side");
      }
      if (receivedMessageString.indexOf("TCKTestServer: Extension size=0") > -1
          || receivedMessageString
              .indexOf("TCKTestServer: Extension size=1") > -1
          || receivedMessageString
              .indexOf("TCKTestServer: Extension size=2") > -1) {
        receivedMessageString
            .append("========Correct Extensions received from server side");
      } else {
        passed = false;
        receivedMessageString
            .append("========Incorrect Extensions received from server side");
      }
    } catch (IOException ioex) {
      passed = false;
      ioex.printStackTrace();
    } catch (Exception e) {
      passed = false;
      e.printStackTrace();
    }
    System.out.println(receivedMessageString);

    if (passed == false) {
      throw new Fault(
          "Test failed with incorrect values, " + receivedMessageString);
    }
  }

  /*
   * @testName: encodersTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:8; WebSocket:JAVADOC:10;
   * WebSocket:JAVADOC:12; WebSocket:JAVADOC:70; WebSocket:JAVADOC:71;
   * WebSocket:JAVADOC:28; WebSocket:JAVADOC:159; WebSocket:JAVADOC:128;
   * WebSocket:JAVADOC:123; WebSocket:JAVADOC:112; WebSocket:JAVADOC:121;
   * WebSocket:JAVADOC:69; WebSocket:JAVADOC:79; WebSocket:JAVADOC:135;
   * WebSocket:JAVADOC:136; WebSocket:SPEC:WSC-2.2.3-1;
   *
   * @test_Strategy:
   */
  public void encodersTest() throws Fault {
    boolean passed = true;

    List<Class<? extends Encoder>> expected_encoders = new ArrayList<Class<? extends Encoder>>();
    expected_encoders
        .add(com.sun.ts.tests.websocket.common.util.ErrorEncoder.class);
    expected_encoders
        .add(com.sun.ts.tests.websocket.common.util.BooleanEncoder.class);

    try {
      messageLatch = new CountDownLatch(15);
      ClientEndpointConfig cec = ClientEndpointConfig.Builder.create()
          .encoders(expected_encoders).build();

      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          cec, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("Dummy String Test");
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      session.close();
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "EndpointConfig.getEncoders() returned encoders size=2|"
              + "com.sun.ts.tests.websocket.common.util.ErrorEncoder|"
              + "com.sun.ts.tests.websocket.common.util.BooleanEncoder|"
              + "TCKTestServer received String:Dummy String Test",
          receivedMessageString.toString());
    } catch (IOException ioex) {
      passed = false;
      ioex.printStackTrace();
    } catch (Exception e) {
      passed = false;
      e.printStackTrace();
    }
    System.out.println(receivedMessageString);

    if (passed == false) {
      throw new Fault(
          "Test failed with incorrect values, " + receivedMessageString);
    }
  }

  /*
   * @testName: decodersTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:8; WebSocket:JAVADOC:10;
   * WebSocket:JAVADOC:11; WebSocket:JAVADOC:70; WebSocket:JAVADOC:71;
   * WebSocket:JAVADOC:28; WebSocket:JAVADOC:159; WebSocket:JAVADOC:128;
   * WebSocket:JAVADOC:123; WebSocket:JAVADOC:112; WebSocket:JAVADOC:121;
   * WebSocket:JAVADOC:69; WebSocket:JAVADOC:79; WebSocket:JAVADOC:135;
   * WebSocket:JAVADOC:136; WebSocket:SPEC:WSC-2.2.3-1;
   *
   * @test_Strategy:
   */
  public void decodersTest() throws Fault {
    boolean passed = true;

    List<Class<? extends Decoder>> expected_decoders = new ArrayList<Class<? extends Decoder>>();
    expected_decoders
        .add(com.sun.ts.tests.websocket.common.util.ByteDecoder.class);
    expected_decoders
        .add(com.sun.ts.tests.websocket.common.util.BooleanDecoder.class);

    try {
      messageLatch = new CountDownLatch(15);

      ClientEndpointConfig cec = ClientEndpointConfig.Builder.create()
          .decoders(expected_decoders).build();
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          cec, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("Dummy String Test");
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      session.close();
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "EndpointConfig.getDecoders() returned decoders size=|"
              + "com.sun.ts.tests.websocket.common.util.ByteDecoder|"
              + "com.sun.ts.tests.websocket.common.util.BooleanDecoder|"
              + "TCKTestServer received String:Dummy String Test",
          receivedMessageString.toString());
    } catch (IOException ioex) {
      passed = false;
      ioex.printStackTrace();
    } catch (Exception e) {
      passed = false;
      e.printStackTrace();
    }
    System.out.println(receivedMessageString);

    if (passed == false) {
      throw new Fault(
          "Test failed with incorrect values, " + receivedMessageString);
    }
  }

  /*
   * @testName: constructorTest2
   * 
   * @assertion_ids: WebSocket:JAVADOC:8; WebSocket:JAVADOC:10;
   * WebSocket:JAVADOC:11; WebSocket:JAVADOC:13; WebSocket:JAVADOC:14;
   * WebSocket:JAVADOC:70; WebSocket:JAVADOC:71; WebSocket:JAVADOC:28;
   * WebSocket:JAVADOC:159; WebSocket:JAVADOC:128; WebSocket:JAVADOC:123;
   * WebSocket:JAVADOC:112; WebSocket:JAVADOC:121; WebSocket:JAVADOC:69;
   * WebSocket:JAVADOC:79; WebSocket:JAVADOC:135; WebSocket:JAVADOC:136;
   * WebSocket:SPEC:WSC-2.2.3-1;
   *
   * @test_Strategy:
   */
  public void constructorTest2() throws Fault {
    boolean passed = true;

    List<String> expected_subprotocols = Arrays.asList("MBWS", "MBLWS", "soap",
        "WAMP", "v10.stomp", "v11.stomp", "v12.stomp");

    List<Class<? extends Decoder>> expected_decoders = new ArrayList<Class<? extends Decoder>>();
    expected_decoders
        .add(com.sun.ts.tests.websocket.common.util.ByteDecoder.class);
    expected_decoders
        .add(com.sun.ts.tests.websocket.common.util.BooleanDecoder.class);

    final List<Extension.Parameter> extension1 = new ArrayList<Extension.Parameter>() {

      {
        add(new TCKExtension.TCKParameter("permessage-compress", "deflate"));
        add(new TCKExtension.TCKParameter("permessage-compress", "foo"));
        add(new TCKExtension.TCKParameter("prop", "val"));
      }
    };

    final List<Extension.Parameter> extension2 = new ArrayList<Extension.Parameter>() {

      {
        add(new TCKExtension.TCKParameter("permessage-compress", "deflate"));
        add(new TCKExtension.TCKParameter("prop1", "val1"));
        add(new TCKExtension.TCKParameter("prop2", "val2"));
      }
    };

    ArrayList<Extension> extensions = new ArrayList<Extension>();
    extensions.add(new TCKExtension("ext1", extension1));
    extensions.add(new TCKExtension("ext2", extension2));

    try {
      messageLatch = new CountDownLatch(20);

      ClientEndpointConfig cec = ClientEndpointConfig.Builder.create()
          .preferredSubprotocols(expected_subprotocols).extensions(extensions)
          .decoders(expected_decoders).build();
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          cec, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("Dummy String Test");
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      session.close();
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "EndpointConfig.getDecoders() returned decoders size=|"
              + "com.sun.ts.tests.websocket.common.util.ByteDecoder|"
              + "com.sun.ts.tests.websocket.common.util.BooleanDecoder|"
              + "TCKTestServer received String:Dummy String Test",
          receivedMessageString.toString());

      if (receivedMessageString.indexOf("Extension size=0") > -1
          || receivedMessageString.indexOf("Extension size=1") > -1
          || receivedMessageString.indexOf("Extension size=2") > -1) {
        receivedMessageString
            .append("Correct Extensions received from client side");
      } else {
        passed = false;
        receivedMessageString
            .append("Incorrect Extensions received from client side");
      }
      if (receivedMessageString.indexOf("TCKTestServer: Extension size=0") > -1
          || receivedMessageString
              .indexOf("TCKTestServer: Extension size=1") > -1
          || receivedMessageString
              .indexOf("TCKTestServer: Extension size=2") > -1) {
        receivedMessageString
            .append("========Correct Extensions received from server side");
      } else {
        passed = false;
        receivedMessageString
            .append("========Incorrect Extensions received from server side");
      }

      if (receivedMessageString.indexOf("subProtocol=|") > -1) {
        receivedMessageString
            .append("Correct subProtocol received from client side");
      } else {
        passed = false;
        receivedMessageString
            .append("Incorrect subProtocol received from client side");
      }

      if (receivedMessageString.indexOf("TCKTestServer: subProtocol==|") > -1) {
        receivedMessageString
            .append("========Correct subProtocol received from server side");
      } else {
        passed = false;
        receivedMessageString
            .append("========Incorrect subProtocol received from server side");
      }
    } catch (IOException ioex) {
      passed = false;
      ioex.printStackTrace();
    } catch (Exception e) {
      passed = false;
      e.printStackTrace();
    }
    System.out.println(receivedMessageString);

    if (passed == false) {
      throw new Fault(
          "Test failed with incorrect values, " + receivedMessageString);
    }
  }

  /*
   * @testName: constructorTest3
   * 
   * @assertion_ids: WebSocket:JAVADOC:8; WebSocket:JAVADOC:10;
   * WebSocket:JAVADOC:11; WebSocket:JAVADOC:12; WebSocket:JAVADOC:13;
   * WebSocket:JAVADOC:14; WebSocket:JAVADOC:70; WebSocket:JAVADOC:71;
   * WebSocket:JAVADOC:28; WebSocket:JAVADOC:159; WebSocket:JAVADOC:128;
   * WebSocket:JAVADOC:123; WebSocket:JAVADOC:112; WebSocket:JAVADOC:121;
   * WebSocket:JAVADOC:69; WebSocket:JAVADOC:79; WebSocket:JAVADOC:135;
   * WebSocket:JAVADOC:136; WebSocket:SPEC:WSC-2.2.3-1;
   *
   * @test_Strategy:
   */
  public void constructorTest3() throws Fault {
    boolean passed = true;

    List<String> expected_subprotocols = Arrays.asList("MBWS", "MBLWS", "soap",
        "WAMP", "v10.stomp", "v11.stomp", "v12.stomp");

    List<Class<? extends Decoder>> expected_decoders = new ArrayList<Class<? extends Decoder>>();
    expected_decoders
        .add(com.sun.ts.tests.websocket.common.util.ByteDecoder.class);
    expected_decoders
        .add(com.sun.ts.tests.websocket.common.util.BooleanDecoder.class);

    List<Class<? extends Encoder>> expected_encoders = new ArrayList<Class<? extends Encoder>>();
    expected_encoders
        .add(com.sun.ts.tests.websocket.common.util.ErrorEncoder.class);
    expected_encoders
        .add(com.sun.ts.tests.websocket.common.util.BooleanEncoder.class);

    final List<Extension.Parameter> extension1 = new ArrayList<Extension.Parameter>() {

      {
        add(new TCKExtension.TCKParameter("permessage-compress", "deflate"));
        add(new TCKExtension.TCKParameter("permessage-compress", "foo"));
        add(new TCKExtension.TCKParameter("prop", "val"));
      }
    };

    final List<Extension.Parameter> extension2 = new ArrayList<Extension.Parameter>() {

      {
        add(new TCKExtension.TCKParameter("permessage-compress", "deflate"));
        add(new TCKExtension.TCKParameter("prop1", "val1"));
        add(new TCKExtension.TCKParameter("prop2", "val2"));
      }
    };

    ArrayList<Extension> extensions = new ArrayList<Extension>();
    extensions.add(new TCKExtension("ext1", extension1));
    extensions.add(new TCKExtension("ext2", extension2));

    try {
      messageLatch = new CountDownLatch(20);

      ClientEndpointConfig cec = ClientEndpointConfig.Builder.create()
          .preferredSubprotocols(expected_subprotocols).extensions(extensions)
          .decoders(expected_decoders).encoders(expected_encoders).build();
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();

      Session session = clientContainer.connectToServer(TCKBasicEndpoint.class,
          cec, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      session.getBasicRemote().sendText("Dummy String Test");
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      session.close();
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      passed = MessageValidator.checkSearchStrings(
          "EndpointConfig.getEncoders() returned encoders size=2|"
              + "com.sun.ts.tests.websocket.common.util.ErrorEncoder|"
              + "com.sun.ts.tests.websocket.common.util.BooleanEncoder|"
              + "EndpointConfig.getDecoders() returned decoders size=|"
              + "com.sun.ts.tests.websocket.common.util.ByteDecoder|"
              + "com.sun.ts.tests.websocket.common.util.BooleanDecoder|"
              + "TCKTestServer received String:Dummy String Test",
          receivedMessageString.toString());

      if (receivedMessageString.indexOf("Extension size=0") > -1
          || receivedMessageString.indexOf("Extension size=1") > -1
          || receivedMessageString.indexOf("Extension size=2") > -1) {
        receivedMessageString
            .append("Correct Extensions received from client side");
      } else {
        passed = false;
        receivedMessageString
            .append("Incorrect Extensions received from client side");
      }
      if (receivedMessageString.indexOf("TCKTestServer: Extension size=0") > -1
          || receivedMessageString
              .indexOf("TCKTestServer: Extension size=1") > -1
          || receivedMessageString
              .indexOf("TCKTestServer: Extension size=2") > -1) {
        receivedMessageString
            .append("========Correct Extensions received from server side");
      } else {
        passed = false;
        receivedMessageString
            .append("========Incorrect Extensions received from server side");
      }

      if (receivedMessageString.indexOf("subProtocol=|") > -1) {
        receivedMessageString
            .append("Correct subProtocol received from client side");
      } else {
        passed = false;
        receivedMessageString
            .append("Incorrect subProtocol received from client side");
      }

      if (receivedMessageString.indexOf("TCKTestServer: subProtocol==|") > -1) {
        receivedMessageString
            .append("========Correct subProtocol received from server side");
      } else {
        passed = false;
        receivedMessageString
            .append("========Incorrect subProtocol received from server side");
      }
    } catch (IOException ioex) {
      passed = false;
      ioex.printStackTrace();
    } catch (Exception e) {
      passed = false;
      e.printStackTrace();
    }
    System.out.println(receivedMessageString);

    if (passed == false) {
      throw new Fault(
          "Test failed with incorrect values, " + receivedMessageString);
    }
  }

  public void cleanup() throws Fault {
    super.cleanup();
  }

  public final static class TCKBasicEndpoint extends Endpoint {

    @Override
    public void onOpen(Session session, EndpointConfig config) {
      String subprotocol = session.getNegotiatedSubprotocol();
      receivedMessageString.append("subProtocol=" + subprotocol + "|");

      List<Extension> extensions = session.getNegotiatedExtensions();

      receivedMessageString.append("Extension size=" + extensions.size() + "|");
      for (Extension extension : extensions) {
        receivedMessageString.append(extension.getName() + "|");
      }

      List<Class<? extends Encoder>> encoders = config.getEncoders();
      int size = encoders.size();
      receivedMessageString.append(
          "EndpointConfig.getEncoders() returned encoders size=" + size + "|");
      for (Class<? extends Encoder> encoder : encoders) {
        receivedMessageString.append("Encoder " + encoder + " found ");
      }

      List<Class<? extends Decoder>> decoders = config.getDecoders();
      size = decoders.size();
      receivedMessageString.append(
          "EndpointConfig.getDecoders() returned decoders size=" + size + "|");
      for (Class<? extends Decoder> decoder : decoders) {
        receivedMessageString.append("Decoder " + decoder + " found ");
      }
      session.addMessageHandler(new MessageHandler.Whole<String>() {

        @Override
        public void onMessage(String message) {
          messageLatch.countDown();
          receivedMessageString
              .append("========Basic String MessageHander received=" + message);
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

    @Override
    public void onClose(Session session, CloseReason closeReason) {
      receivedMessageString.append("CloseCode=" + closeReason.getCloseCode());
      receivedMessageString
          .append("ReasonPhrase=" + closeReason.getReasonPhrase());
    }
  }

  class TCKConfigurator extends ClientEndpointConfig.Configurator {

    void TCKConfigurator() {
    }

    public void beforeRequest(Map<String, List<String>> headers) {
    }

    public void afterResponse(HandshakeResponse hr) {
    }
  }
}
