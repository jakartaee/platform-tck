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

package com.sun.ts.tests.websocket.common.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

public abstract class ClientEndpoint<T extends Object> extends Endpoint
    implements MessageHandler.Whole<T> {

  /**
   * This structure is static, because the original API was unable to set an
   * instance of endpoint when connecting to server, the only available option
   * as an argument of
   * {@link WebSocketContainer#connectToServer(Class, ClientEndpointConfig, java.net.URI)}
   * was Class. The instance of endpoint was created by websocket API, and no
   * information could be injected into the instance.
   */
  public static class ClientEndpointData {
    protected static volatile StringBuffer sb = new StringBuffer();

    protected static volatile Throwable websocketError = null;

    protected static EndpointCallback callback;

    protected static volatile CountDownLatch messageLatch;

    protected static volatile Object lastMessage = null;

    protected static final Object LOCK = new Object();

    protected static volatile CountDownLatch onCloseLatch = null;

    public static Throwable getError() {
      return websocketError;
    }

    private static void setError(Throwable error) {
      websocketError = error;
    }

    public static String getResponseAsString() {
      WebSocketTestCase.logMsg("Response:", sb.toString());
      return sb.toString();
    }

    public static void resetData() {
      synchronized (LOCK) {
        WebSocketCommonClient.logTrace("Reseting callback and message", "");
        sb = new StringBuffer();
        websocketError = null;
        callback = null;
        lastMessage = null;
        messageLatch = null;
        onCloseLatch = null;
      }
    }

    public static void newCountDown(int count) {
      messageLatch = new CountDownLatch(count);
    };

    public static void awaitCountDown(int seconds) {
      try {
        messageLatch.await(seconds, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    public static void newOnCloseCountDown() {
      if (onCloseLatch == null)
        onCloseLatch = new CountDownLatch(1);
    }

    public static long getCount() {
      return messageLatch.getCount();
    }

    public static void awaitOnClose() {
      try {
        onCloseLatch.await(2000L, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        e.printStackTrace(); // If it does not wait,
        // the test should still pass
      }
    }

    public static Object getOriginalMessage() {
      return lastMessage;
    }

    private static void setOriginalMessage(Object message) {
      lastMessage = message;
    }

  }

  /**
   * Hopefully this approach might be changed when api allows for passing the
   * instance instead of class.
   */

  @Override
  public void onOpen(Session session, EndpointConfig config) {
    onOpen(session, config, true);
  }

  public void onOpen(Session session, EndpointConfig config,
      boolean addMessageHandler) {
    WebSocketCommonClient.logTrace("On open on session id", session.getId());
    String uri = session.getRequestURI() == null ? "NULL"
        : session.getRequestURI().toASCIIString();
    WebSocketCommonClient.logTrace("RequestUri:", uri);
    if (session.isOpen()) {
      if (addMessageHandler)
        session.addMessageHandler(this);
    } else
      WebSocketCommonClient.logTrace("Session is closed!!!!", "");
    synchronized (ClientEndpointData.LOCK) {
      if (ClientEndpointData.callback != null && session.isOpen())
        ClientEndpointData.callback.onOpen(session, config);
    }
  }

  @Override
  public void onError(Session session, Throwable t) {
    ClientEndpointData.setError(t);
    t.printStackTrace();
    synchronized (ClientEndpointData.LOCK) {
      if (ClientEndpointData.callback != null)
        ClientEndpointData.callback.onError(session, t);
    }
  }

  protected void appendMessage(T message) {
    ClientEndpointData.sb.append(message.toString());
  }

  @Override
  public void onMessage(T message) {
    ClientEndpointData.setOriginalMessage(message);
    appendMessage(message);
    WebSocketCommonClient.logTrace("Received message so far",
        ClientEndpointData.sb.toString());
    synchronized (ClientEndpointData.LOCK) {
      if (ClientEndpointData.callback != null)
        ClientEndpointData.callback.onMessage(message);
    }
    WebSocketTestCase.logTrace("CountDownLatch hit");
    if (ClientEndpointData.messageLatch.getCount() == 0)
      throw new IllegalStateException("CountDownLatch.getCount == 0 already");
    ClientEndpointData.messageLatch.countDown();
  }

  @Override
  public void onClose(Session session, CloseReason closeReason) {
    synchronized (ClientEndpointData.LOCK) {
      WebSocketTestCase.logTrace("On close on session id", session.getId(),
          "reason", closeReason);
      if (ClientEndpointData.lastMessage == null)
        WebSocketTestCase
            .logTrace("onClose has been called before a message was received");
      else
        WebSocketTestCase.logTrace("onClose has been called");
      if (ClientEndpointData.callback != null)
        ClientEndpointData.callback.onClose(session, closeReason);
      // onCloseLatch == null when close has not been called by client
      if (ClientEndpointData.onCloseLatch == null)
        ClientEndpointData.newOnCloseCountDown();
      ClientEndpointData.onCloseLatch.countDown();
    }
  }

  public static CountDownLatch getCountDownLatch() {
    return ClientEndpointData.messageLatch;
  }

  public static StringBuffer getMessageBuilder() {
    return ClientEndpointData.sb;
  }

  @SuppressWarnings("unchecked")
  public T getLastMessage(Class<T> messageType) {
    return (T) ClientEndpointData.lastMessage;
  }

  public static Throwable getLastError() {
    return ClientEndpointData.websocketError;
  }

}
