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

package com.sun.ts.tests.websocket.ee.javax.websocket.clientendpointonmessage;

import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.CloseReason;
import javax.websocket.Session;

import com.sun.ts.tests.websocket.common.client.EndpointCallback;
import com.sun.ts.tests.websocket.common.client.SendMessageCallback;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSClient extends WebSocketCommonClient {
  private static final long serialVersionUID = 7948609603037530363L;

  static final String ECHO = "Echo message to be sent to server endpoint";

  public static void main(String[] args) {
    new WSClient().run(args);
  }

  @Override
  public void setup(String[] args, Properties p) throws Fault {
    setCountDownLatchCount(3);
    setContextRoot("ws_ee_clientendpointonmessage_web");
    addClientCallback(new EndpointCallback() {
      @Override
      public void onClose(Session session, CloseReason closeReason) {
        getCountDownLatch().countDown();
        super.onClose(session, closeReason);
      }
    });
    super.setup(args, p);
  }

  /* Run test */

  // TEXT ------------------------------------------

  /*
   * @testName: echoStringTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: Echo String
   */
  public void echoStringTest() throws Fault {
    setAnnotatedClientEndpoint(new WSStringClientEndpoint());
    setProperty(Property.CONTENT, ECHO);
    invoke(OPS.TEXT, ECHO);
  }

  /*
   * @testName: echoIntTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo int
   */
  public void echoIntTest() throws Fault {
    setAnnotatedClientEndpoint(new WSPrimitiveIntClientEndpoint());
    int entity = Integer.MIN_VALUE;
    setEntity(entity);
    invoke(OPS.TEXT, String.valueOf(entity));
  }

  /*
   * @testName: echoByteTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo byte
   */
  public void echoByteTest() throws Fault {
    setAnnotatedClientEndpoint(new WSPrimitiveByteClientEndpoint());
    byte entity = 123;
    setEntity(entity);
    invoke(OPS.TEXT, String.valueOf(entity));
  }

  /*
   * @testName: echoCharTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo char
   */
  public void echoCharTest() throws Fault {
    setAnnotatedClientEndpoint(new WSPrimitiveCharClientEndpoint());
    char entity = 'E';
    setEntity(entity);
    invoke(OPS.TEXT, String.valueOf(entity));
  }

  /*
   * @testName: echoBooleanTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo boolean
   */
  public void echoBooleanTest() throws Fault {
    setAnnotatedClientEndpoint(new WSPrimitiveBooleanClientEndpoint());
    boolean entity = true;
    setEntity(entity);
    invoke(OPS.TEXT, String.valueOf(entity));
  }

  /*
   * @testName: echoShortTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo short
   */
  public void echoShortTest() throws Fault {
    setAnnotatedClientEndpoint(new WSPrimitiveShortClientEndpoint());
    short entity = -32100;
    setEntity(entity);
    invoke(OPS.TEXT, String.valueOf(entity));
  }

  /*
   * @testName: echoDoubleTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo double
   */
  public void echoDoubleTest() throws Fault {
    setAnnotatedClientEndpoint(new WSPrimitiveDoubleClientEndpoint());
    double entity = -12345678.88;
    setEntity(entity);
    invoke(OPS.TEXT, String.valueOf(entity));
  }

  /*
   * @testName: echoFloatTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo float
   */
  public void echoFloatTest() throws Fault {
    setAnnotatedClientEndpoint(new WSPrimitiveFloatClientEndpoint());
    float entity = -12345678.88f;
    setEntity(entity);
    invoke(OPS.TEXT, String.valueOf(entity));
  }

  /*
   * @testName: echoLongTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo long
   */
  public void echoLongTest() throws Fault {
    setAnnotatedClientEndpoint(new WSPrimitiveLongClientEndpoint());
    long entity = Long.MIN_VALUE;
    setEntity(entity);
    invoke(OPS.TEXT, String.valueOf(entity));
  }

  /*
   * @testName: echoFullIntTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo int
   */
  public void echoFullIntTest() throws Fault {
    setAnnotatedClientEndpoint(new WSFullLongClientEndpoint());
    Integer entity = Integer.MIN_VALUE;
    setEntity(entity);
    invoke(OPS.TEXT, entity.toString());
  }

  /*
   * @testName: echoFullByteTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo byte
   */
  public void echoFullByteTest() throws Fault {
    setAnnotatedClientEndpoint(new WSFullByteClientEndpoint());
    Byte entity = Byte.MIN_VALUE;
    setEntity(entity);
    invoke(OPS.TEXT, entity.toString());
  }

  /*
   * @testName: echoFullCharTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo char
   */
  public void echoFullCharTest() throws Fault {
    setAnnotatedClientEndpoint(new WSFullCharClientEndpoint());
    Character entity = 'E';
    setEntity(entity);
    invoke(OPS.TEXT, entity.toString());
  }

  /*
   * @testName: echoFullBooleanTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo boolean
   */
  public void echoFullBooleanTest() throws Fault {
    setAnnotatedClientEndpoint(new WSFullBooleanClientEndpoint());
    Boolean entity = true;
    setEntity(entity);
    invoke(OPS.TEXT, entity.toString());
  }

  /*
   * @testName: echoFullShortTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo short
   */
  public void echoFullShortTest() throws Fault {
    setAnnotatedClientEndpoint(new WSFullShortClientEndpoint());
    Short entity = Short.MAX_VALUE;
    setEntity(entity);
    invoke(OPS.TEXT, entity.toString());
  }

  /*
   * @testName: echoFullDoubleTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo double
   */
  public void echoFullDoubleTest() throws Fault {
    setAnnotatedClientEndpoint(new WSFullDoubleClientEndpoint());
    Double entity = Double.MIN_VALUE;
    setEntity(entity);
    invoke(OPS.TEXT, entity.toString());
  }

  /*
   * @testName: echoFullFloatTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo float
   */
  public void echoFullFloatTest() throws Fault {
    setAnnotatedClientEndpoint(new WSFullFloatClientEndpoint());
    Float entity = Float.MAX_VALUE;
    setEntity(entity);
    invoke(OPS.TEXT, entity.toString());
  }

  /*
   * @testName: echoFullLongTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo long
   */
  public void echoFullLongTest() throws Fault {
    setAnnotatedClientEndpoint(new WSFullLongClientEndpoint());
    Long entity = Long.MAX_VALUE;
    setEntity(entity);
    invoke(OPS.TEXT, entity.toString());
  }

  /*
   * @testName: echoReaderTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: OnMessage(Reader)
   */
  public void echoReaderTest() throws Fault {
    setAnnotatedClientEndpoint(new WSReaderClientEndpoint());
    setProperty(Property.CONTENT, ECHO);
    invoke(OPS.TEXT, ECHO);
  }

  /*
   * @testName: echoTextDecoderTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: OnMessage using TextDecoder
   */
  public void echoTextDecoderTest() throws Fault {
    setAnnotatedClientEndpoint(new WSTextDecoderClientEndpoint());
    setProperty(Property.CONTENT, ECHO);
    invoke(OPS.TEXT, ECHO);
  }

  /*
   * @testName: echoTextStreamDecoderTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: OnMessage using TextStreamDecoder
   */
  public void echoTextStreamDecoderTest() throws Fault {
    setAnnotatedClientEndpoint(new WSTextStreamDecoderClientEndpoint());
    setProperty(Property.CONTENT, ECHO);
    invoke(OPS.TEXT, ECHO);
  }

  // -------------------------TEXT AND SESSION ------------------------------
  /*
   * @testName: echoStringAndSessionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: Echo String
   */
  public void echoStringAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSStringAndSessionClientEndpoint());
    setProperty(Property.CONTENT, ECHO);
    invoke(OPS.TEXT, ECHO);
  }

  /*
   * @testName: echoIntAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo int
   */
  public void echoIntAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSPrimitiveIntAndSessionClientEndpoint());
    int entity = Integer.MIN_VALUE;
    setEntity(entity);
    invoke(OPS.TEXT, String.valueOf(entity));
  }

  /*
   * @testName: echoByteAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo byte
   */
  public void echoByteAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSPrimitiveByteAndSessionClientEndpoint());
    byte entity = 123;
    setEntity(entity);
    invoke(OPS.TEXT, String.valueOf(entity));
  }

  /*
   * @testName: echoCharAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo char
   */
  public void echoCharAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSPrimitiveCharAndSessionClientEndpoint());
    char entity = 'E';
    setEntity(entity);
    invoke(OPS.TEXT, String.valueOf(entity) + "char");
  }

  /*
   * @testName: echoBooleanAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo boolean
   */
  public void echoBooleanAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(
        new WSPrimitiveBooleanAndSessionClientEndpoint());
    boolean entity = true;
    setEntity(entity);
    invoke(OPS.TEXT, String.valueOf(entity));
  }

  /*
   * @testName: echoShortAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo short
   */
  public void echoShortAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSPrimitiveShortAndSessionClientEndpoint());
    short entity = -32100;
    setEntity(entity);
    invoke(OPS.TEXT, String.valueOf(entity));
  }

  /*
   * @testName: echoDoubleAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo double
   */
  public void echoDoubleAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSPrimitiveDoubleAndSessionClientEndpoint());
    double entity = -12345678.88;
    setEntity(entity);
    invoke(OPS.TEXT, String.valueOf(entity));
  }

  /*
   * @testName: echoFloatAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo float
   */
  public void echoFloatAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSPrimitiveFloatAndSessionClientEndpoint());
    float entity = -12345678.88f;
    setEntity(entity);
    invoke(OPS.TEXT, String.valueOf(entity));
  }

  /*
   * @testName: echoLongAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo long
   */
  public void echoLongAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSPrimitiveLongAndSessionClientEndpoint());
    long entity = Long.MIN_VALUE;
    setEntity(entity);
    invoke(OPS.TEXT, String.valueOf(entity));
  }

  /*
   * @testName: echoFullIntAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo int
   */
  public void echoFullIntAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSFullLongAndSessionClientEndpoint());
    Integer entity = Integer.MIN_VALUE;
    setEntity(entity);
    invoke(OPS.TEXT, entity.toString());
  }

  /*
   * @testName: echoFullByteAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo byte
   */
  public void echoFullByteAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSFullByteAndSessionClientEndpoint());
    Byte entity = Byte.MIN_VALUE;
    setEntity(entity);
    invoke(OPS.TEXT, entity.toString());
  }

  /*
   * @testName: echoFullCharAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo char
   */
  public void echoFullCharAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSFullCharAndSessionClientEndpoint());
    Character entity = 'E';
    setEntity(entity);
    invoke(OPS.TEXT, entity.toString());
  }

  /*
   * @testName: echoFullBooleanAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo boolean
   */
  public void echoFullBooleanAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSFullBooleanAndSessionClientEndpoint());
    Boolean entity = true;
    setEntity(entity);
    invoke(OPS.TEXT, entity.toString());
  }

  /*
   * @testName: echoFullShortAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo short
   */
  public void echoFullShortAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSFullShortAndSessionClientEndpoint());
    Short entity = Short.MAX_VALUE;
    setEntity(entity);
    invoke(OPS.TEXT, entity.toString());
  }

  /*
   * @testName: echoFullDoubleAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo double
   */
  public void echoFullDoubleAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSFullDoubleAndSessionClientEndpoint());
    Double entity = Double.MIN_VALUE;
    setEntity(entity);
    invoke(OPS.TEXT, entity.toString());
  }

  /*
   * @testName: echoFullFloatAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo float
   */
  public void echoFullFloatAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSFullFloatAndSessionClientEndpoint());
    Float entity = Float.MAX_VALUE;
    setEntity(entity);
    invoke(OPS.TEXT, entity.toString());
  }

  /*
   * @testName: echoFullLongAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo long
   */
  public void echoFullLongAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSFullLongAndSessionClientEndpoint());
    Long entity = Long.MAX_VALUE;
    setEntity(entity);
    invoke(OPS.TEXT, entity.toString());
  }

  /*
   * @testName: echoReaderAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: OnMessage(Reader)
   */
  public void echoReaderAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSReaderAndSessionClientEndpoint());
    setProperty(Property.CONTENT, ECHO);
    invoke(OPS.TEXT, ECHO);
  }

  /*
   * @testName: echoTextDecoderAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: OnMessage using TextDecoder
   */
  public void echoTextDecoderAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSTextDecoderAndSessionClientEndpoint());
    setProperty(Property.CONTENT, ECHO);
    invoke(OPS.TEXT, ECHO);
  }

  /*
   * @testName: echoTextStreamDecoderAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: OnMessage using TextStreamDecoder
   */
  public void echoTextStreamDecoderAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(
        new WSTextStreamDecoderAndSessionClientEndpoint());
    setProperty(Property.CONTENT, ECHO);
    invoke(OPS.TEXT, ECHO);
  }

  // -----------------------PARTIAL---------------------------------

  /*
   * @testName: partialStringTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
   * WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send partial message
   */
  public void partialStringTest() throws Fault {
    setAnnotatedClientEndpoint(new WSStringPartialClientEndpoint());
    String partial2 = "partialStringTest";
    String response = ECHO + "_(false)" + partial2 + "(true)";
    setEntity(ECHO + "_" + partial2);
    invoke(OPS.TEXTPARTIAL, response);
  }

  /*
   * @testName: partialStringAndSessionTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
   * WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send string and string receive the combined string
   */
  public void partialStringAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSStringPartialAndSessionClientEndpoint());
    String partial2 = "partialStringAndSessionTest";
    String response = ECHO + "_(false)" + partial2 + "(true)";
    setEntity(ECHO + "_" + partial2);
    invoke(OPS.TEXTPARTIAL, response);
  }

  // ----------------- BINARY -----------------------------------

  /*
   * @testName: echoByteBufferTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: Echo ByteBuffer
   */
  public void echoByteBufferTest() throws Fault {
    setAnnotatedClientEndpoint(new WSByteBufferClientEndpoint());
    setEntity(ByteBuffer.wrap(ECHO.getBytes()));
    invoke(OPS.BINARY, ECHO);
  }

  /*
   * @testName: byteBufferToBytesTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: Send ByteBuffer receive bytes
   */
  public void byteBufferToBytesTest() throws Fault {
    setAnnotatedClientEndpoint(new WSByteArrayClientEndpoint());
    setEntity(ByteBuffer.wrap(ECHO.getBytes()));
    invoke(OPS.BINARY, ECHO);
  }

  /*
   * @testName: byteBufferToInputStreamTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: Send ByteBuffer receive InputStream
   */
  public void byteBufferToInputStreamTest() throws Fault {
    setAnnotatedClientEndpoint(new WSInputStreamClientEndpoint());
    setEntity(ByteBuffer.wrap(ECHO.getBytes()));
    invoke(OPS.BINARY, ECHO);
  }

  /*
   * @testName: byteBufferToBinaryDecoderTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: Send ByteBuffer receive Object
   */
  public void byteBufferToBinaryDecoderTest() throws Fault {
    setAnnotatedClientEndpoint(new WSBinaryDecoderClientEndpoint());
    setEntity(ByteBuffer.wrap(ECHO.getBytes()));
    invoke(OPS.BINARY, ECHO);
  }

  /*
   * @testName: byteBufferToBinaryStreamDecoderTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: Send ByteBuffer receive Object
   */
  public void byteBufferToBinaryStreamDecoderTest() throws Fault {
    setAnnotatedClientEndpoint(new WSBinaryStreamDecoderClientEndpoint());
    setEntity(ByteBuffer.wrap(ECHO.getBytes()));
    invoke(OPS.BINARY, ECHO);
  }

  // ----------------- CONTROL -----------------------------------
  /*
   * @testName: pongToPongTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: pong message
   */
  public void pongToPongTest() throws Fault {
    setAnnotatedClientEndpoint(new WSPongMessageClientEndpoint());
    setEntity(ECHO);
    invoke(OPS.PONG, ECHO);
  }

  /*
   * @testName: pongToPongAndSessionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: pong message
   */
  public void pongToPongAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSPongMessageAndSessionClientEndpoint());
    setEntity(ECHO);
    invoke(OPS.PONG, ECHO);
  }

  // ----------------- BINARY AND SESSION -----------------------------------
  /*
   * @testName: echoByteBufferAndSessionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: Echo ByteBuffer
   */
  public void echoByteBufferAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSByteBufferAndSessionClientEndpoint());
    setEntity(ByteBuffer.wrap(ECHO.getBytes()));
    invoke(OPS.BINARY, ECHO);
  }

  /*
   * @testName: byteBufferToBytesAndSessionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: Send ByteBuffer receive bytes
   */
  public void byteBufferToBytesAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSByteArrayAndSessionClientEndpoint());
    setEntity(ByteBuffer.wrap(ECHO.getBytes()));
    invoke(OPS.BINARY, ECHO);
  }

  /*
   * @testName: byteBufferToInputStreamAndSessionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: Send ByteBuffer receive InputStream
   */
  public void byteBufferToInputStreamAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSInputStreamAndSessionClientEndpoint());
    setEntity(ByteBuffer.wrap(ECHO.getBytes()));
    invoke(OPS.BINARY, ECHO);
  }

  /*
   * @testName: byteBufferToBinaryDecoderAndSessionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: Send ByteBuffer receive Object
   */
  public void byteBufferToBinaryDecoderAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(new WSBinaryDecoderAndSessionClientEndpoint());
    setEntity(ByteBuffer.wrap(ECHO.getBytes()));
    invoke(OPS.BINARY, ECHO);
  }

  /*
   * @testName: byteBufferToBinaryStreamDecoderAndSessionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: Send ByteBuffer receive Object
   */
  public void byteBufferToBinaryStreamDecoderAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(
        new WSBinaryStreamDecoderAndSessionClientEndpoint());
    setEntity(ByteBuffer.wrap(ECHO.getBytes()));
    invoke(OPS.BINARY, ECHO);
  }

  // ----------------- PARTIAL BINARY -----------------------------------
  /*
   * @testName: partialByteArrayTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send bytearray and bytearray receive the combined message
   */
  public void partialByteArrayTest() throws Fault {
    setAnnotatedClientEndpoint(new WSByteArrayPartialClientEndpoint());
    String partial2 = "partialByteArrayTest";
    String response = ECHO + "_(false)" + partial2 + "(true)";
    setEntity(ByteBuffer.wrap((ECHO + "_" + partial2).getBytes()));
    invoke(OPS.BINARYPARTIAL, response);
  }

  /*
   * @testName: partialByteArrayAndSessionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send bytearray and bytearray receive the combined message
   */
  public void partialByteArrayAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(
        new WSByteArrayPartialAndSessionClientEndpoint());
    String partial2 = "partialByteArrayAndSessionTest";
    String response = ECHO + "_(false)" + partial2 + "(true)";
    setEntity(ByteBuffer.wrap((ECHO + "_" + partial2).getBytes()));
    invoke(OPS.BINARYPARTIAL, response);
  }

  /*
   * @testName: partialByteBufferTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send ByteBuffer and ByteBuffer receive the combined message
   */
  public void partialByteBufferTest() throws Fault {
    setAnnotatedClientEndpoint(new WSByteBufferPartialClientEndpoint());
    String partial2 = "partialByteBufferTest";
    String response = ECHO + "_(false)" + partial2 + "(true)";
    setEntity(ByteBuffer.wrap((ECHO + "_" + partial2).getBytes()));
    invoke(OPS.BINARYPARTIAL, response);
  }

  /*
   * @testName: partialByteBufferAndSessionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send ByteBuffer and ByteBuffer receive the combined message
   */
  public void partialByteBufferAndSessionTest() throws Fault {
    setAnnotatedClientEndpoint(
        new WSByteBufferPartialAndSessionClientEndpoint());
    String partial2 = "partialByteBufferAndSessionTest";
    String response = ECHO + "_(false)" + partial2 + "(true)";
    setEntity(ByteBuffer.wrap((ECHO + "_" + partial2).getBytes()));
    invoke(OPS.BINARYPARTIAL, response);
  }

  // --------------------------------- MAX LEN
  /*
   * @testName: maxLengthOKTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: the message is shorter than maxMessageLength
   */
  public void maxLengthOKTest() throws Fault {
    setAnnotatedClientEndpoint(new WSMaxLengthClientEndpoint());
    String entity = "12345";
    setEntity(entity);
    invoke(OPS.TEXT, entity);
  }

  /*
   * @testName: maxLengthFailsTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80; WebSocket:SPEC:WSC-4.7.1-1;
   * 
   * @test_Strategy: the message is longer than maxMessageLength
   */
  public void maxLengthFailsTest() throws Fault {
    setCountDownLatchCount(1);
    final AtomicInteger ai = new AtomicInteger(0);
    setEntity("123456");
    EndpointCallback callback = new SendMessageCallback(entity) {
      @Override
      public void onClose(Session session, CloseReason closeReason) {
        ai.set(closeReason.getCloseCode().getCode());
        getCountDownLatch().countDown();
      }
    };
    setClientCallback(callback);
    setAnnotatedClientEndpoint(new WSMaxLengthClientEndpoint());
    setProperty(Property.REQUEST, buildRequest(OPS.TEXT));
    invoke(false);
    assertEqualsInt(1009, ai.get(), "Unexpected close reason found", ai.get());
    logMsg("Found expected close reason code", 1009);
  }

  /*
   * @testName: defaultMaxLengthTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: Default -1;
   */
  public void defaultMaxLengthTest() throws Fault {
    setAnnotatedClientEndpoint(new WSDefaultMaxLengthClientEndpoint());
    setEntity("123456789");
    invoke(OPS.TEXT, "-1");
  }

  // ////////////////////////////////////////////////////////////////////
  private String buildRequest(OPS op) {
    return buildRequest("srv", "/", op.name());
  }

  private void invoke(OPS op, String search) throws Fault {
    setProperty(Property.REQUEST, buildRequest(op));
    setProperty(Property.SEARCH_STRING, search, search);
    invoke();
  }
}
