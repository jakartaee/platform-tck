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

package com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.basic;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;

import javax.websocket.EncodeException;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.PongMessage;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.sun.ts.tests.websocket.common.stringbean.StringBean;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextEncoder;
import com.sun.ts.tests.websocket.common.util.IOUtil;

@ServerEndpoint(value = "/server", encoders = { StringBeanTextEncoder.class,
    ThrowingEncoder.class })
public class WSCServerSideServer {
  static final String[] RESPONSE = { "OK", "FAIL" };

  @OnMessage
  public String onMessage(PongMessage pong) {
    return IOUtil.byteBufferToString(pong.getApplicationData());
  }

  @OnMessage
  public String onMessage(String msg, Session session) {
    Basic basicRemote = session.getBasicRemote();
    OPS op = OPS.valueOf(msg.toUpperCase());
    switch (op) {
    case POKE:
      // returns msg;
      break;
    case SENDBINARY:
      msg = sendBinary(basicRemote);
      break;
    case SENDBINARYPART1:
      msg = sendBinaryPartial(basicRemote);
      break;
    case SENDBINARYTHROWS:
      msg = sendBinaryThrows(basicRemote);
      break;
    case SENDOBJECT:
      msg = sendObject(basicRemote);
      break;
    case SENDOBJECTTHROWS:
      msg = sendObjectThrows(basicRemote);
      break;
    case SENDOBJECTTHROWSENCODEEEXCEPTION:
      msg = sendObjectThrowsEncodeException(basicRemote);
      break;
    case SENDOBJECT_BOOL:
      msg = sendObject(basicRemote, boolean.class);
      break;
    case SENDOBJECT_BYTE:
      msg = sendObject(basicRemote, byte.class);
      break;
    case SENDOBJECT_CHAR:
      msg = sendObject(basicRemote, char.class);
      break;
    case SENDOBJECT_DOUBLE:
      msg = sendObject(basicRemote, double.class);
      break;
    case SENDOBJECT_FLOAT:
      msg = sendObject(basicRemote, float.class);
      break;
    case SENDOBJECT_INT:
      msg = sendObject(basicRemote, int.class);
      break;
    case SENDOBJECT_LONG:
      msg = sendObject(basicRemote, long.class);
      break;
    case SENDOBJECT_SHORT:
      msg = sendObject(basicRemote, short.class);
      break;
    case SENDTEXT:
      msg = sendText(basicRemote);
      break;
    case SENDTEXTPART1:
      msg = sendTextPartial(basicRemote);
      break;
    case SENDTEXTTHROWS:
      msg = sendTextThrows(basicRemote);
      break;
    case SENDSTREAM:
      msg = getSendStream(basicRemote);
      break;
    case SENDWRITER:
      msg = getSendWriter(basicRemote);
      break;
    case BATCHING_ALLOWED:
      msg = batchingAllowed(basicRemote);
      break;
    case SEND_PING:
      msg = sendPing(basicRemote);
      break;
    case SEND_PING_THROWS:
      msg = sendPingThrows(basicRemote);
      break;
    case SEND_PONG:
      msg = sendPong(basicRemote);
      break;
    case SEND_PONG_THROWS:
      msg = sendBinaryThrows(basicRemote);
      break;
    case IDLE:
      session.setMaxIdleTimeout(1500L);
      break;
    case PING_4_TIMES:
      msg = sendPing4times(basicRemote);
      break;
    case PONG_4_TIMES:
      msg = sendPong4times(basicRemote);
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
   * Again, since the asynchronous thread sends message in virtually no time,
   * one does not have any force to hold that send operation in its thread, to
   * check the send operation is really unblocking and asynchronous
   * 
   * @param asyncRemote
   * @return
   */
  protected static String sendBinary(Basic basicRemote) {
    try {
      basicRemote.sendBinary(ByteBuffer.wrap(OPS.SENDBINARY.name().getBytes()));
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendBinaryPartial(Basic basicRemote) {
    try {
      basicRemote.sendBinary(
          ByteBuffer.wrap(OPS.SENDBINARYPART1.name().getBytes()), false);
      basicRemote.sendBinary(
          ByteBuffer.wrap(OPS.SENDBINARYPART2.name().getBytes()), false);
      basicRemote.sendBinary(
          ByteBuffer.wrap(OPS.SENDBINARYPART3.name().getBytes()), true);
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendBinaryThrows(Basic basicRemote) {
    try {
      basicRemote.sendBinary((ByteBuffer) null);
      return RESPONSE[1];
    } catch (IllegalArgumentException e) {
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendObject(Basic basicRemote) {
    try {
      basicRemote.sendObject(new StringBean(OPS.SENDOBJECT.name()));
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendObject(Basic basicRemote, Class<?> type) {
    try {
      if (type == byte.class) {
        byte b = -100;
        basicRemote.sendObject(b);
      } else if (type == short.class) {
        short s = -101;
        basicRemote.sendObject(s);
      } else if (type == int.class) {
        int i = -102;
        basicRemote.sendObject(i);
      } else if (type == long.class) {
        long l = -103L;
        basicRemote.sendObject(l);
      } else if (type == float.class) {
        float f = -104f;
        basicRemote.sendObject(f);
      } else if (type == double.class) {
        double d = -105d;
        basicRemote.sendObject(d);
      } else if (type == boolean.class) {
        boolean b = false;
        basicRemote.sendObject(b);
      } else if (type == char.class) {
        char c = 106;
        basicRemote.sendObject(c);
      }
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }

  }

  protected static String sendObjectThrows(Basic basicRemote) {
    try {
      basicRemote.sendObject((StringBean) null);
      return RESPONSE[1];
    } catch (IllegalArgumentException e) {
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendObjectThrowsEncodeException(Basic basicRemote) {
    try {
      basicRemote.sendObject(new HolderForThrowingEncoder(
          OPS.SENDOBJECTTHROWSENCODEEEXCEPTION.name()));
      return RESPONSE[1];
    } catch (EncodeException e) {
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendText(Basic basicRemote) {
    try {
      basicRemote.sendText(OPS.SENDTEXT.name());
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendTextPartial(Basic basicRemote) {
    try {
      basicRemote.sendText(OPS.SENDTEXTPART1.name(), false);
      basicRemote.sendText(OPS.SENDTEXTPART2.name(), false);
      basicRemote.sendText(OPS.SENDTEXTPART3.name(), true);
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendTextThrows(Basic basicRemote) {
    try {
      basicRemote.sendText((String) null);
      return RESPONSE[1];
    } catch (IllegalArgumentException e) {
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String getSendStream(Basic basicRemote) {
    try {
      OutputStream stream = basicRemote.getSendStream();
      stream.write(OPS.SENDSTREAM.name().getBytes());
      stream.close();
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String getSendWriter(Basic basicRemote) {
    try {
      Writer writer = basicRemote.getSendWriter();
      writer.append(OPS.SENDWRITER.name());
      writer.close();
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String batchingAllowed(Basic basicRemote) {
    try {
      boolean allowed = basicRemote.getBatchingAllowed();
      basicRemote.setBatchingAllowed(!allowed);
      // now getBatching can be true, or false if not supported
      // really there is nothing to test, except that the exception is
      // not thrown
      basicRemote.sendText(OPS.BATCHING_ALLOWED.name());
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendPing(Basic basicRemote) {
    try {
      basicRemote.sendPing(ByteBuffer.wrap(OPS.SEND_PING.name().getBytes()));
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendPingThrows(Basic basicRemote) {
    try {
      basicRemote.sendPing(ByteBuffer.wrap(generateMessage(126).getBytes()));
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

  protected static String sendPong(Basic basicRemote) {
    try {
      basicRemote.sendPong(ByteBuffer.wrap(OPS.SEND_PONG.name().getBytes()));
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendPongThrows(Basic basicRemote) {
    try {
      basicRemote.sendPong(ByteBuffer.wrap(generateMessage(126).getBytes()));
      return RESPONSE[1];
    } catch (IllegalArgumentException iae) {
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendPing4times(Basic basicRemote) {
    try {
      byte[] bytes = OPS.POKE.name().getBytes();
      basicRemote.sendPing(ByteBuffer.wrap(bytes));
      Thread.sleep(500L);
      basicRemote.sendPing(ByteBuffer.wrap(bytes));
      Thread.sleep(500L);
      basicRemote.sendPing(ByteBuffer.wrap(bytes));
      Thread.sleep(500L);
      basicRemote.sendPing(ByteBuffer.wrap(bytes));
      Thread.sleep(500L);
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

  protected static String sendPong4times(Basic basicRemote) {
    try {
      byte[] bytes = OPS.POKE.name().getBytes();
      basicRemote.sendPong(ByteBuffer.wrap(bytes));
      Thread.sleep(500L);
      basicRemote.sendPong(ByteBuffer.wrap(bytes));
      Thread.sleep(500L);
      basicRemote.sendPong(ByteBuffer.wrap(bytes));
      Thread.sleep(500L);
      basicRemote.sendPong(ByteBuffer.wrap(bytes));
      Thread.sleep(500L);
      return RESPONSE[0];
    } catch (Exception e) {
      e.printStackTrace();
      return IOUtil.printStackTrace(e);
    }
  }

}
