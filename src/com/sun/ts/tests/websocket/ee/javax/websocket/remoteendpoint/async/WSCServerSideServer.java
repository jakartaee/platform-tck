/*
 * Copyright (c) 2013, 2019 Oracle and/or its affiliates and others.
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

package com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.async;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.PongMessage;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.SendResult;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.sun.ts.tests.websocket.common.impl.WaitingSendHandler;
import com.sun.ts.tests.websocket.common.stringbean.StringBean;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextEncoder;
import com.sun.ts.tests.websocket.common.util.IOUtil;

@ServerEndpoint(value = "/server", encoders = { StringBeanTextEncoder.class })
public class WSCServerSideServer {
  static final String[] RESPONSE = { "OK", "FAIL" };

  static final long SECONDS = 10;

  @OnMessage
  public String onMessage(PongMessage pong) {
    return IOUtil.byteBufferToString(pong.getApplicationData());
  }

  @OnMessage
  public String onMessage(String msg, Session session) {
    Async asyncRemote = session.getAsyncRemote();
    OPS op = OPS.valueOf(msg.toUpperCase());
    switch (op) {
    case POKE:
      // returns msg;
      break;
    case TIMEOUT:
      msg = timeout(asyncRemote);
      break;
    case SENDBINARY:
      msg = sendBinary(asyncRemote);
      break;
    case SENDBINARYTHROWS:
      msg = sendBinaryThrows(asyncRemote);
      break;
    case SENDBINARYHANDLER:
      msg = sendBinaryWithHandler(asyncRemote);
      break;
    case SENDBINARYHANDLERTHROWSONDATA:
      msg = sendBinaryWithHandlerThrowsOnData(asyncRemote);
      break;
    case SENDBINARYHANDLERTHROWSONHANDLER:
      msg = sendBinaryWithHandlerThrowsOnHandler(asyncRemote);
      break;
    case SENDOBJECT:
      msg = sendObject(asyncRemote);
      break;
    case SENDOBJECT_BOOL:
      msg = sendObject(asyncRemote, boolean.class);
      break;
    case SENDOBJECT_BYTE:
      msg = sendObject(asyncRemote, byte.class);
      break;
    case SENDOBJECT_CHAR:
      msg = sendObject(asyncRemote, char.class);
      break;
    case SENDOBJECT_DOUBLE:
      msg = sendObject(asyncRemote, double.class);
      break;
    case SENDOBJECT_FLOAT:
      msg = sendObject(asyncRemote, float.class);
      break;
    case SENDOBJECT_INT:
      msg = sendObject(asyncRemote, int.class);
      break;
    case SENDOBJECT_LONG:
      msg = sendObject(asyncRemote, long.class);
      break;
    case SENDOBJECT_SHORT:
      msg = sendObject(asyncRemote, short.class);
      break;
    case SENDOBJECTTHROWS:
      msg = sendObjectThrows(asyncRemote);
      break;
    case SENDOBJECTHANDLER:
      msg = sendObjectWithHandler(asyncRemote);
      break;
    case SENDOBJECTHANDLERTHROWSONDATA:
      msg = sendObjectWithHandlerThrowsOnData(asyncRemote);
      break;
    case SENDOBJECTHANDLERTHROWSONHANDLER:
      msg = sendObjectWithHandlerThrowsOnHandler(asyncRemote);
      break;
    case SENDTEXT:
      msg = sendText(asyncRemote);
      break;
    case SENDTEXTTHROWS:
      msg = sendTextThrows(asyncRemote);
      break;
    case SENDTEXTHANDLER:
      msg = sendTextWithHandler(asyncRemote);
      break;
    case SENDTEXTHANDLERTHROWSONDATA:
      msg = sendTextWithHandlerThrowsOnData(asyncRemote);
      break;
    case SENDTEXTHANDLERTHROWSONHANDLER:
      msg = sendTextWithHandlerThrowsOnHandler(asyncRemote);
      break;
    case BATCHING_ALLOWED:
      msg = batchingAllowed(asyncRemote);
      break;
    case SEND_PING:
      msg = sendPing(asyncRemote);
      break;
    case SEND_PING_THROWS:
      msg = sendPingThrows(asyncRemote);
      break;
    case SEND_PONG:
      msg = sendPong(asyncRemote);
      break;
    case SEND_PONG_THROWS:
      msg = sendBinaryThrows(asyncRemote);
      break;
    case IDLE:
      session.setMaxIdleTimeout(1500L);
      break;
    case PING_4_TIMES:
      msg = sendPing4times(asyncRemote);
      break;
    case PONG_4_TIMES:
      msg = sendPong4times(asyncRemote);
      break;
    default:
      throw new IllegalArgumentException("Method " + msg + " not implemented");
    }
    return msg;
  }

  @OnError
  public void onError(Session session, Throwable t) throws IOException {
    System.out.println("@OnError in " + getClass().getName());
    t.printStackTrace(); // Write to error log, too
    String message = "Exception: " + IOUtil.printStackTrace(t);
    session.getBasicRemote().sendText(message);
  }

  /**
   * The setSendTimeout() method is not possible to be tested in terms of
   * functionality, as there is unfortunately no way guarantee that the timeout
   * would be reached regardless the websocket implementation
   */
  protected static String timeout(Async asyncRemote) {
    boolean set = true;
    int timeout = 1;
    asyncRemote.setSendTimeout(timeout);
    set &= (timeout == asyncRemote.getSendTimeout());
    timeout = 5;
    asyncRemote.setSendTimeout(timeout);
    set &= (timeout == asyncRemote.getSendTimeout());
    return set ? RESPONSE[0] : RESPONSE[1];
  }

  /**
   * Again, since the asynchronous thread sends message in virtually no time,
   * one does not have any force to hold that send operation in its thread, to
   * check the send operation is really unblocking and asynchronous
   * 
   * @param asyncRemote
   * @return
   */
  protected static String sendBinary(Async asyncRemote) {
    Future<Void> future = asyncRemote
        .sendBinary(ByteBuffer.wrap(OPS.SENDBINARY.name().getBytes()));
    try {
      Void v = future.get();
      return v == null ? RESPONSE[0] : RESPONSE[1];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendBinaryThrows(Async asyncRemote) {
    try {
      asyncRemote.sendBinary((ByteBuffer) null);
      return RESPONSE[1];
    } catch (IllegalArgumentException e) {
      return RESPONSE[0];
    }
  }

  protected static String sendBinaryWithHandler(Async asyncRemote) {
    WaitingSendHandler handler = new WaitingSendHandler();
    asyncRemote.sendBinary(
        ByteBuffer.wrap(OPS.SENDBINARYHANDLER.name().getBytes()), handler);
    SendResult result = handler.waitForResult(SECONDS);
    return result.isOK() ? RESPONSE[0] : RESPONSE[1];
  }

  protected static String sendBinaryWithHandlerThrowsOnData(Async asyncRemote) {
    try {
      asyncRemote.sendBinary((ByteBuffer) null, new WaitingSendHandler());
      return RESPONSE[1];
    } catch (IllegalArgumentException e) {
      return RESPONSE[0];
    }
  }

  protected static String sendBinaryWithHandlerThrowsOnHandler(
      Async asyncRemote) {
    try {
      asyncRemote.sendBinary(
          ByteBuffer.wrap(OPS.SENDBINARYHANDLERTHROWSONDATA.name().getBytes()),
          (WaitingSendHandler) null);
      return RESPONSE[1];
    } catch (IllegalArgumentException e) {
      return RESPONSE[0];
    }
  }

  protected static String sendObject(Async asyncRemote) {
    Future<Void> future = asyncRemote
        .sendObject(new StringBean(OPS.SENDOBJECT.name()));
    try {
      Void v = future.get();
      return v == null ? RESPONSE[0] : RESPONSE[1];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendObject(Async asyncRemote, Class<?> type) {
    try {
      if (type == byte.class) {
        byte b = -100;
        asyncRemote.sendObject(b);
      } else if (type == short.class) {
        short s = -101;
        asyncRemote.sendObject(s);
      } else if (type == int.class) {
        int i = -102;
        asyncRemote.sendObject(i);
      } else if (type == long.class) {
        long l = -103L;
        asyncRemote.sendObject(l);
      } else if (type == float.class) {
        float f = -104f;
        asyncRemote.sendObject(f);
      } else if (type == double.class) {
        double d = -105d;
        asyncRemote.sendObject(d);
      } else if (type == boolean.class) {
        boolean b = false;
        asyncRemote.sendObject(b);
      } else if (type == char.class) {
        char c = 106;
        asyncRemote.sendObject(c);
      }
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendObjectThrows(Async asyncRemote) {
    try {
      asyncRemote.sendObject((StringBean) null);
      return RESPONSE[1];
    } catch (IllegalArgumentException e) {
      return RESPONSE[0];
    }
  }

  protected static String sendObjectWithHandler(Async asyncRemote) {
    WaitingSendHandler handler = new WaitingSendHandler();
    asyncRemote.sendObject(new StringBean(OPS.SENDOBJECTHANDLER.name()),
        handler);
    SendResult result = handler.waitForResult(SECONDS);
    return result.isOK() ? RESPONSE[0] : RESPONSE[1];
  }

  protected static String sendObjectWithHandlerThrowsOnData(Async asyncRemote) {
    try {
      asyncRemote.sendObject((Object) null, new WaitingSendHandler());
      return RESPONSE[1];
    } catch (IllegalArgumentException e) {
      return RESPONSE[0];
    }
  }

  protected static String sendObjectWithHandlerThrowsOnHandler(
      Async asyncRemote) {
    try {
      OPS op = OPS.SENDOBJECTHANDLERTHROWSONHANDLER;
      asyncRemote.sendObject(op, (WaitingSendHandler) null);
      return RESPONSE[1];
    } catch (IllegalArgumentException e) {
      return RESPONSE[0];
    }
  }

  protected static String sendText(Async asyncRemote) {
    Future<Void> future = asyncRemote.sendText(OPS.SENDTEXT.name());
    try {
      Void v = future.get();
      return v == null ? RESPONSE[0] : RESPONSE[1];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendTextThrows(Async asyncRemote) {
    try {
      asyncRemote.sendText((String) null);
      return RESPONSE[1];
    } catch (IllegalArgumentException e) {
      return RESPONSE[0];
    }
  }

  protected static String sendTextWithHandler(Async asyncRemote) {
    WaitingSendHandler handler = new WaitingSendHandler();
    asyncRemote.sendText(OPS.SENDTEXTHANDLER.name(), handler);
    SendResult result = handler.waitForResult(SECONDS);
    return result.isOK() ? RESPONSE[0] : RESPONSE[1];
  }

  protected static String sendTextWithHandlerThrowsOnData(Async asyncRemote) {
    try {
      asyncRemote.sendText((String) null, new WaitingSendHandler());
      return RESPONSE[1];
    } catch (IllegalArgumentException e) {
      return RESPONSE[0];
    }
  }

  protected static String sendTextWithHandlerThrowsOnHandler(
      Async asyncRemote) {
    try {
      asyncRemote.sendText(OPS.SENDOBJECTHANDLERTHROWSONHANDLER.name(),
          (WaitingSendHandler) null);
      return RESPONSE[1];
    } catch (IllegalArgumentException e) {
      return RESPONSE[0];
    }
  }

  protected static String batchingAllowed(Async asyncRemote) {
    try {
      boolean allowed = asyncRemote.getBatchingAllowed();
      asyncRemote.setBatchingAllowed(!allowed);
      // now getBatching can be true, or false if not supported
      // really there is nothing to test, except that the exception is
      // not thrown
      asyncRemote.sendText(OPS.BATCHING_ALLOWED.name());
      asyncRemote.flushBatch();
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendPing(Async asyncRemote) {
    try {
      asyncRemote.sendPing(ByteBuffer.wrap(OPS.SEND_PING.name().getBytes()));
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendPingThrows(Async asyncRemote) {
    try {
      asyncRemote.sendPing(ByteBuffer.wrap(generateMessage(126).getBytes()));
      return RESPONSE[1];
    } catch (IllegalArgumentException iae) {
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String generateMessage(int length) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i != length; i++)
      sb.append(i % 10);
    return sb.toString();
  }

  protected static String sendPong(Async asyncRemote) {
    try {
      asyncRemote.sendPong(ByteBuffer.wrap(OPS.SEND_PONG.name().getBytes()));
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendPongThrows(Async asyncRemote) {
    try {
      asyncRemote.sendPong(ByteBuffer.wrap(generateMessage(126).getBytes()));
      return RESPONSE[1];
    } catch (IllegalArgumentException iae) {
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendPing4times(Async asyncRemote) {
    try {
      byte[] bytes = OPS.POKE.name().getBytes();
      asyncRemote.sendPing(ByteBuffer.wrap(bytes));
      Thread.sleep(500L);
      asyncRemote.sendPing(ByteBuffer.wrap(bytes));
      Thread.sleep(500L);
      asyncRemote.sendPing(ByteBuffer.wrap(bytes));
      Thread.sleep(500L);
      asyncRemote.sendPing(ByteBuffer.wrap(bytes));
      Thread.sleep(500L);
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendPong4times(Async asyncRemote) {
    try {
      byte[] bytes = OPS.POKE.name().getBytes();
      asyncRemote.sendPong(ByteBuffer.wrap(bytes));
      Thread.sleep(500L);
      asyncRemote.sendPong(ByteBuffer.wrap(bytes));
      Thread.sleep(500L);
      asyncRemote.sendPong(ByteBuffer.wrap(bytes));
      Thread.sleep(500L);
      asyncRemote.sendPong(ByteBuffer.wrap(bytes));
      Thread.sleep(500L);
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }
}
