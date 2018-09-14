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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/TCKTestServer")
public class WSTestServer {

  private static final Class<?>[] TEST_ARGS = { String.class, Session.class };

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

  @OnError
  public void error(Session session, Throwable t) {
    try {
      session.getBasicRemote().sendText("========TCKTestServer onError");
      if (session.isOpen()) {
        session.getBasicRemote()
            .sendText("========onError: session from Server is open=TRUE");
        session.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    t.printStackTrace();
  }
}
