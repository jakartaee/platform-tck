/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates and others.
 * All rights reserved.
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
package com.sun.ts.tests.websocket.ee.jakarta.websocket.session;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/WSCloseTestServer2")
public class WSCloseTestServer2 {

  private static final Class<?>[] TEST_ARGS = { String.class, Session.class };

  static String testName;

  @OnOpen
  public void init(Session session) throws IOException {
    session.getBasicRemote().sendText("========WSCloseTestServer2 opened");
    if (session.isOpen()) {
      session.getBasicRemote()
          .sendText("========session from WSCloseTestServer2 is open=TRUE");
    } else {
      session.getBasicRemote()
          .sendText("========session from WSCloseTestServer2 is open=FALSE");
    }
  }

  @OnMessage
  public void respondString(String message, Session session) {
    System.out.println("WSCloseTestServer2 got String message: " + message);
    try {
      if (message.startsWith("testName=") && message.endsWith("Test")) {
        testName = message.substring(9);
        Method method = WSCloseTestServer2.class.getMethod(testName, TEST_ARGS);
        method.invoke(this, new Object[] { message, session });
      } else {
        session.getBasicRemote()
            .sendText("========WSCloseTestServer2 received String:" + message);
        session.getBasicRemote().sendText(
            "========WSCloseTestServer2 responds, please close your session");
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
      session.getBasicRemote().sendText("========WSCloseTestServer2 onError");
      if (session.isOpen()) {
        session.getBasicRemote().sendText(
            "========onError: session from WSCloseTestServer2 is open=TRUE");
      } else {
        session.getBasicRemote().sendText(
            "========onError: session from WSCloseTestServer2 is open=FALSE");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    t.printStackTrace();
  }

  @OnClose
  public void onClose3(Session session, CloseReason closeReason) {
    System.out
        .println("==From WSCloseTestServer2 onClose(Session, CloseReason)==");
    try {
      session.getBasicRemote()
          .sendText("========WSCloseTestServer2 OnClose(Session, CloseReason)");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void close1Test(String message, Session session) {
    try {
      session.getBasicRemote()
          .sendText("========WSCloseTestServer2 received String: " + message);
      session.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void close2Test(String message, Session session) {
    try {
      session.getBasicRemote()
          .sendText("========WSCloseTestServer2 received String: " + message);
      session.close(new CloseReason(CloseReason.CloseCodes.TOO_BIG,
          "TCKCloseNowWithReason"));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void close3Test(String message, Session session) {
    try {
      session.getBasicRemote()
          .sendText("========WSCloseTestServer2 received String: " + message);
      session.close(new CloseReason(CloseReason.CloseCodes.TOO_BIG,
          "TCKCloseNowWithReason"));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
