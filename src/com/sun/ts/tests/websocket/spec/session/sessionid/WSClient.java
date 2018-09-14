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
package com.sun.ts.tests.websocket.spec.session.sessionid;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.util.IOUtil;

public class WSClient extends WebSocketCommonClient {

  private static final long serialVersionUID = 10L;

  private static final String CONTEXT_ROOT = "/ws_spec_sessionid_web";

  private static StringBuffer receivedMessageString = new StringBuffer();

  static CountDownLatch messageLatch, onCloseLatch;

  static volatile String session_id, session_id_endpoint_onOpen,
      session_id_endpoint_onClose;

  static volatile Session session, session_endpoint_onOpen,
      session_endpoint_onClose;

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
   * @testName: getIdTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:162; WebSocket:JAVADOC:67;
   * WebSocket:JAVADOC:69; WebSocket:JAVADOC:130; WebSocket:SPEC:WSC-2.1.2-1;
   *
   * @test_Strategy:
   */
  public void getIdTest() throws Fault {
    boolean passed = true;

    try {
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      messageLatch = new CountDownLatch(1);
      session = clientContainer.connectToServer(
          com.sun.ts.tests.websocket.spec.session.sessionid.WSClient.TCKGetIdEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      session_id = session.getId();

      onCloseLatch = new CountDownLatch(1);
      session.close();
      onCloseLatch.await(_ws_wait, TimeUnit.SECONDS);

      if (session_id != session_id_endpoint_onOpen
          || session_id != session_id_endpoint_onClose) {
        passed = false;
        System.out.print("Session IDs are not the same.");
      }
      System.out.println("session_id                 =" + session_id);
      System.out.println(
          "session_id_endpoint_onClose=" + session_id_endpoint_onClose);
      System.out
          .println("session_id_endpoint_onOpen =" + session_id_endpoint_onOpen);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault(e);
    }
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  /*
   * @testName: instanceTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:162; WebSocket:JAVADOC:67;
   * WebSocket:JAVADOC:69; WebSocket:JAVADOC:130; WebSocket:SPEC:WSC-2.1.2-1;
   *
   * @test_Strategy:
   */
  public void instanceTest() throws Fault {
    boolean passed = true;

    try {
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      messageLatch = new CountDownLatch(1);
      session = clientContainer.connectToServer(
          com.sun.ts.tests.websocket.spec.session.sessionid.WSClient.TCKGetIdEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      onCloseLatch = new CountDownLatch(1);
      session.close();
      onCloseLatch.await(_ws_wait, TimeUnit.SECONDS);

      if (session != session_endpoint_onOpen
          || session != session_endpoint_onClose) {
        passed = false;
        System.out.print("Sessions are not the same.");
      }
      System.out.println("session                 =" + session);
      System.out
          .println("session_endpoint_onClose=" + session_endpoint_onClose);
      System.out.println("session_endpoint_onOpen =" + session_endpoint_onOpen);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault(e);
    }
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  /*
   * @testName: unique
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:162; WebSocket:JAVADOC:67;
   * WebSocket:JAVADOC:69; WebSocket:JAVADOC:130; WebSocket:SPEC:WSC-2.1.2-1;
   *
   * @test_Strategy:
   */
  public void unique() throws Fault {
    int size = 5;
    boolean passed = true;
    Session[] sessions = new Session[size];

    try {
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      for (int i = 0; i < size; i++) {
        messageLatch = new CountDownLatch(1);
        session = clientContainer.connectToServer(
            com.sun.ts.tests.websocket.spec.session.sessionid.WSClient.TCKGetIdEndpoint.class,
            config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
                + "/TCKTestServer"));
        sessions[i] = session;
        messageLatch.await(_ws_wait, TimeUnit.SECONDS);

        onCloseLatch = new CountDownLatch(1);
        session.close();
        onCloseLatch.await(_ws_wait, TimeUnit.SECONDS);

        System.out.println("Session " + i);

        if (session != session_endpoint_onOpen
            || session != session_endpoint_onClose) {
          passed = false;
          System.out.print("Sessions are not the same.");
        }

        System.out.println("session                 =" + session);
        System.out
            .println("session_endpoint_onClose=" + session_endpoint_onClose);
        System.out
            .println("session_endpoint_onOpen =" + session_endpoint_onOpen);
      }

      for (int i = 0; i < size; i++) {
        for (int j = i + 1; j < size; j++) {
          if (sessions[i] == sessions[j]) {
            passed = false;
            System.out.println("two sessions are the same: ");
            System.out.println("session " + i + " " + sessions[i]);
            System.out.println("session " + j + " " + sessions[j]);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault(e);
    }
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  @Override
  public void cleanup() throws Fault {
    super.cleanup();
  }

  public final static class TCKGetIdEndpoint extends Endpoint {

    @Override
    public void onOpen(Session session, EndpointConfig config) {
      session_id_endpoint_onOpen = session.getId();
      session_endpoint_onOpen = session;

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
      session_id_endpoint_onClose = session.getId();
      session_endpoint_onClose = session;

      receivedMessageString.append("CloseCode=" + closeReason.getCloseCode());
      receivedMessageString
          .append("ReasonPhrase=" + closeReason.getReasonPhrase());
      onCloseLatch.countDown();
    }
  }
}
