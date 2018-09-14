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
package com.sun.ts.tests.websocket.ee.javax.websocket.sessionexception;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.util.MessageValidator;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.websocket.*;

public class WSClient extends WebSocketCommonClient {

  private static final String CONTEXT_ROOT = "/ws_sessionexception_web";

  private static final String SENT_STRING_MESSAGE = "Hello World in String";

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
   * @testName: test1
   * 
   * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
   * WebSocket:JAVADOC:10; WebSocket:JAVADOC:162; WebSocket:JAVADOC:67;
   * WebSocket:JAVADOC:69; WebSocket:JAVADOC:79; WebSocket:JAVADOC:128;
   * WebSocket:JAVADOC:112; WebSocket:JAVADOC:151; WebSocket:JAVADOC:152;
   *
   * @test_Strategy:
   */
  public void test1() throws Fault {
    boolean passed = true;
    String message_expected = "TCKTest";
    String message_sent_string = "SessionException test";

    try {
      messageLatch = new CountDownLatch(5);
      WebSocketContainer clientContainer = ContainerProvider
          .getWebSocketContainer();
      ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
          .build();

      final Session session = clientContainer.connectToServer(
          com.sun.ts.tests.websocket.ee.javax.websocket.sessionexception.WSClient.TCKBasicEndpoint.class,
          config, new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT
              + "/TCKTestServer"));

      messageLatch.await(_ws_wait, TimeUnit.SECONDS);
      receivedMessageString
          .append("========Just setup to get a  real working session");
      session.getBasicRemote().sendText(message_sent_string);
      messageLatch.await(_ws_wait, TimeUnit.SECONDS);

      Throwable cause_expected = new Throwable(message_expected + 1);
      SessionException ex = new SessionException(message_expected,
          new Throwable(cause_expected), session);

      Session tmp = ex.getSession();
      if (tmp != session) {
        passed = false;
        receivedMessageString
            .append("========Incorrect session returned,  expecting " + session
                + ", got " + tmp);
      }

      String msg = ex.getMessage();
      if (!message_expected.equals(msg)) {
        passed = false;
        receivedMessageString
            .append("========Incorrect message returned,  expecting "
                + message_expected + ", got " + msg);
      }

      Throwable cause = ex.getCause();
      if (cause.getCause() != null) {
        cause = cause.getCause();
      } else {
        passed = false;
        receivedMessageString
            .append("Lost one wrap of Throwable during the process");
      }
      if (cause_expected != cause) {
        passed = false;
        receivedMessageString
            .append("========Incorrect cause returned,  expecting "
                + cause_expected + ", got " + cause);
      }

      session.close();

      Boolean passed_tmp = MessageValidator.checkSearchStrings(
          "TCKTestServer opened|" + "TCKTestServer received String:|"
              + message_sent_string + "|"
              + "TCKTestServer responds, please close your session",
          receivedMessageString.toString());

      if (!passed_tmp) {
        passed = false;
      }

    } catch (Exception e) {
      e.printStackTrace();
      passed = false;
      receivedMessageString.append("========unexpected Exception caught");
    }

    System.out.println(receivedMessageString.toString());
    if (!passed) {
      throw new Fault("Test failed with incorrect response");
    }
  }

  @Override
  public void cleanup() throws Fault {
    super.cleanup();
  }

  public final static class TCKBasicEndpoint extends Endpoint {

    @Override
    public void onOpen(Session session, EndpointConfig config) {
      session.addMessageHandler(new MessageHandler.Whole<String>() {

        public void onMessage(String message) {
          messageLatch.countDown();
          receivedMessageString.append(message);
        }
      });
    }

    public void onClose(Session session, CloseReason closeReason) {
      receivedMessageString.append("CloseCode=" + closeReason.getCloseCode());
      receivedMessageString
          .append("ReasonPhrase=" + closeReason.getReasonPhrase());
    }
  }
}
