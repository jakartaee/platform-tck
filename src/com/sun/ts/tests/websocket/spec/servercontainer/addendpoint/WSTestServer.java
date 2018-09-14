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
package com.sun.ts.tests.websocket.spec.servercontainer.addendpoint;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import com.sun.ts.tests.websocket.common.util.IOUtil;

@ServerEndpoint(value = "/TCKTestServer")
public class WSTestServer {

  private static final Class<?>[] TEST_ARGS = { String.class, Session.class };

  private static final Class<?>[] TEST_ARGS_BYTEBUFFER = { ByteBuffer.class,
      Session.class };

  static String testName;

  @OnOpen
  public void init(Session session) throws IOException {
    session.getBasicRemote().sendText("========TCKTestServer opened");
    if (session.isOpen()) {
      session.getBasicRemote()
          .sendText("========session from Server is open=TRUE");
    } else {
      session.getBasicRemote()
          .sendText("========session from Server is open=FALSE");
    }
  }

  @OnMessage
  public void respondString(String message, Session session) {
    System.out.println("TCKTestServer got String message: " + message);
    try {
      if (message.startsWith("testName=") && message.endsWith("Test")) {
        testName = message.substring(9);
        Method method = WSTestServer.class.getMethod(testName, TEST_ARGS);
        method.invoke(this, new Object[] { message, session });
      } else {
        session.getBasicRemote()
            .sendText("========TCKTestServer received String:" + message);
        session.getBasicRemote().sendText(
            "========TCKTestServer responds, please close your session");
      }
    } catch (InvocationTargetException ite) {
      System.err.println("Cannot run method " + testName);
      ite.printStackTrace();
    } catch (NoSuchMethodException nsme) {
      System.err.println("Test: " + testName + " does not exist");
      nsme.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @OnMessage
  public void respondByte(ByteBuffer message, Session session) {
    String message_string = IOUtil.byteBufferToString(message);

    System.out
        .println("TCKTestServer got ByteBuffer message: " + message_string);

    try {
      if (message_string.startsWith("testName=")) {
        testName = message_string.substring(9);
        Method method = WSTestServer.class.getMethod(testName,
            TEST_ARGS_BYTEBUFFER);
        method.invoke(this, new Object[] { message, session });
      } else {
        ByteBuffer data = ByteBuffer
            .wrap(("========TCKTestServer received ByteBuffer: ").getBytes());
        ByteBuffer data1 = ByteBuffer.wrap(
            ("========TCKTestServer responds: Message in bytes").getBytes());
        ByteBuffer dataOrig = ByteBuffer.wrap(message_string.getBytes());
        try {
          session.getBasicRemote().sendBinary(data);
          session.getBasicRemote().sendBinary(dataOrig);
          session.getBasicRemote().sendBinary(data1);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (InvocationTargetException ite) {
      System.err.println("Cannot run method " + testName);
      ite.printStackTrace();
    } catch (NoSuchMethodException nsme) {
      System.err.println("Test: " + testName + " does not exist");
      nsme.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @OnError
  public void onError(Session session, Throwable t) {
    try {
      session.getBasicRemote().sendText("========TCKTestServer onError");
      if (session.isOpen()) {
        session.getBasicRemote()
            .sendText("========onError: session from Server is open=TRUE");
      } else {
        session.getBasicRemote()
            .sendText("========onError: session from Server is open=FALSE");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    t.printStackTrace();
  }

  @OnClose
  public void onClose(Session session) {
    System.out.println("==From WSTestServer onClose(Session)==");
    try {
      session.getBasicRemote()
          .sendText("========WSTestServer OnClose(Session)");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void getId1Test(String message, Session session) {
    try {
      session.getBasicRemote()
          .sendText("========TCKTestServer received String: " + message);
      session.getBasicRemote().sendText(
          "========TCKTestServer responds: default getId=" + session.getId());
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void close1Test(String message, Session session) {
    try {
      session.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void close2Test(String message, Session session) {
    try {
      session.close(new CloseReason(CloseReason.CloseCodes.TOO_BIG,
          "TCKCloseNowWithReason"));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void setTimeout1Test(String message, Session session) {
    System.out.println(
        "========In setMaxIdleTimeout1Test: TCKTestServer received String:"
            + message);
    try {
      session.getBasicRemote()
          .sendText("========TCKTestServer received String: " + message);
      session.getBasicRemote()
          .sendText("========TCKTestServer default timeout: "
              + session.getMaxIdleTimeout());
      String query_string = session.getQueryString();
      int timeout = 0;
      if (query_string.startsWith("timeout")) {
        timeout = Integer.parseInt(query_string.substring(8));
        Thread.sleep(timeout * 8000);
        session.getBasicRemote()
            .sendText("========TCKTestServer second message after sleep "
                + timeout * 8 + " second");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setTimeout2Test(String message, Session session) {
    System.out.println("========TCKTestServer received String:" + message);
    CountDownLatch messageLatch = new CountDownLatch(1);

    try {
      session.getBasicRemote()
          .sendText("========TCKTestServer received String: " + message);
      session.getBasicRemote()
          .sendText("========TCKTestServer default timeout: "
              + session.getMaxIdleTimeout());

      String query_string = session.getQueryString();
      int timeout = 0;
      if (query_string.startsWith("timeout")) {
        timeout = Integer.parseInt(query_string.substring(8));
        session.setMaxIdleTimeout(timeout * 4000);
        session.getBasicRemote().sendText(
            "========TCKTestServer set timeout to " + timeout * 4 + " second");
        session.getBasicRemote()
            .sendText("========TCKTestServer getTimeout return "
                + session.getMaxIdleTimeout() + " milliseconds");
        messageLatch.await(timeout * 6, TimeUnit.SECONDS);
        session.getBasicRemote()
            .sendText("========TCKTestServer second message after sleep "
                + timeout * 6 + " second");
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getQueryStringTest(String message, Session session) {
    System.out.println("========TCKTestServer received String:" + message);
    String expected_querystring = "test1=value1&test2=value2&test3=value3";

    try {
      session.getBasicRemote()
          .sendText("========TCKTestServer received String: " + message);
      String query_string = session.getQueryString();
      if (expected_querystring.equals(query_string)) {
        session.getBasicRemote()
            .sendText("========TCKTestServer: expected Query String returned"
                + query_string);
      } else {
        session.getBasicRemote()
            .sendText("========TCKTestServer: unexpected Query String returned"
                + query_string);
        session.getBasicRemote().sendText(
            "========TCKTestServer: expecting " + expected_querystring);
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void getRequestURITest(String message, Session session) {
    System.out.println("========TCKTestServer received String:" + message);

    try {
      session.getBasicRemote()
          .sendText("========TCKTestServer received String: " + message);
      URI requestURI = session.getRequestURI();
      session.getBasicRemote()
          .sendText("========TCKTestServer: getRequestURI returned="
              + requestURI.toString());
      session.getBasicRemote()
          .sendText("========TCKTestServer: getRequestURI returned queryString="
              + requestURI.getQuery());
      session.getBasicRemote()
          .sendText("========TCKTestServer: getRequestURI returned scheme="
              + requestURI.getScheme());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void setMaxBinaryMessageBufferSizeTest2(ByteBuffer message_b,
      Session session) {
    System.out.println("In setMaxBinaryMessageBufferSizeTest2");
    int size = 64;
    String message = "Binary Message over size 64=0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String message1 = message.substring(0, size - 1);
    String message2 = message.substring(size, 90 - 1);

    ByteBuffer data = ByteBuffer
        .wrap(("========TCKTestServer received ByteBuffer: ").getBytes());
    ByteBuffer data1 = ByteBuffer
        .wrap(("========TCKTestServer responds:").getBytes());
    ByteBuffer data2 = ByteBuffer.wrap(message.getBytes());

    try {
      session.getBasicRemote().sendBinary(data);
      session.getBasicRemote().sendBinary(message_b);
      session.getBasicRemote().sendBinary(data1);
      session.getBasicRemote().sendBinary(data2);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void setMaxTextMessageBufferSize2Test(String message_r,
      Session session) {
    System.out.println("In setMaxTextMessageBufferSize2Test");
    int size = 64;
    String message = "String Message over size 64=0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String message1 = message.substring(0, size - 1);
    String message2 = message.substring(size, 90 - 1);

    try {
      session.getBasicRemote()
          .sendText("========TCKTestServer received String Message: ");
      session.getBasicRemote().sendText(message);
      session.getBasicRemote().sendText("========TCKTestServer responded");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void getOpenSessionsTest(String message, Session session) {
    System.out.println("In getOpenSessionsTest");

    try {
      session.getBasicRemote()
          .sendText("========TCKTestServer received String Message: ");
      session.getBasicRemote().sendText(message);
      Set<Session> tmp = session.getOpenSessions();
      session.getBasicRemote()
          .sendText("========getOpenSessions=" + tmp.size());
      session.getBasicRemote().sendText("========TCKTestServer responded");
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
