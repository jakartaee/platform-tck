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

package com.sun.ts.tests.websocket.ee.javax.websocket.websocketmessage;

import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import javax.websocket.CloseReason;
import javax.websocket.Session;

import com.sun.ts.tests.websocket.common.client.EndpointCallback;
import com.sun.ts.tests.websocket.common.client.SendMessageCallback;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.impl.StringPongMessage;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSClient extends WebSocketCommonClient {

  private static final long serialVersionUID = 932557106496525508L;

  static final String ECHO = "Echo message to be sent to server endpoint";

  static final String[] TEXT_PRIMITIVE_SEQUENCE = { "byte", "short", "int",
      "long", "double", "float" };

  static final String[] TEXT_TYPE_SEQUENCE = { "string", "reader",
      "textdecoder", "textstreamdecoder" };

  static final String[] BINARY_SEQUENCE = { "bytearray", "bytebuffer",
      "inputstream", "binarydecoder", "binarystreamdecoder" };

  public static int getIndex(String item) {
    for (int i = 0; i != TEXT_PRIMITIVE_SEQUENCE.length; i++)
      if (TEXT_PRIMITIVE_SEQUENCE[i].equals(item))
        return i;
    return -1;
  }

  public WSClient() {
    setContextRoot("ws_ee_websocketmessage_web");
  }

  public static void main(String[] args) {
    new WSClient().run(args);
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
    setProperty(Property.REQUEST, buildRequest("string"));
    setProperty(Property.CONTENT, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO);
    invoke();
  }

  /*
   * @testName: echoIntTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo int
   */
  public void echoIntTest() throws Fault {
    int entity = Integer.MIN_VALUE;
    setEntity(entity);
    setProperty(Property.REQUEST, buildRequest("primitiveint"));
    setProperty(Property.SEARCH_STRING, String.valueOf(entity));
    invoke();
  }

  /*
   * @testName: echoByteTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo byte
   */
  public void echoByteTest() throws Fault {
    byte entity = 123;
    setEntity(entity);
    setProperty(Property.REQUEST, buildRequest("primitivebyte"));
    setProperty(Property.SEARCH_STRING, String.valueOf(entity));
    invoke();
  }

  /*
   * @testName: echoCharTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo char
   */
  public void echoCharTest() throws Fault {
    char entity = 'E';
    setEntity(entity);
    setProperty(Property.REQUEST, buildRequest("primitivechar"));
    setProperty(Property.SEARCH_STRING, String.valueOf(entity) + "char");
    invoke();
  }

  /*
   * @testName: echoBooleanTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo boolean
   */
  public void echoBooleanTest() throws Fault {
    boolean entity = true;
    setEntity(entity);
    setProperty(Property.REQUEST, buildRequest("primitiveboolean"));
    setProperty(Property.SEARCH_STRING, String.valueOf(entity));
    invoke();
  }

  /*
   * @testName: echoShortTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo short
   */
  public void echoShortTest() throws Fault {
    short entity = -32100;
    setEntity(entity);
    setProperty(Property.REQUEST, buildRequest("primitiveshort"));
    setProperty(Property.SEARCH_STRING, String.valueOf(entity));
    invoke();
  }

  /*
   * @testName: echoDoubleTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo double
   */
  public void echoDoubleTest() throws Fault {
    double entity = -12345678.88;
    setEntity(entity);
    setProperty(Property.REQUEST, buildRequest("primitivedouble"));
    setProperty(Property.SEARCH_STRING, String.valueOf(entity));
    invoke();
  }

  /*
   * @testName: echoFloatTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo float
   */
  public void echoFloatTest() throws Fault {
    float entity = -12345678.88f;
    setEntity(entity);
    setProperty(Property.REQUEST, buildRequest("primitivefloat"));
    setProperty(Property.SEARCH_STRING, String.valueOf(entity));
    invoke();
  }

  /*
   * @testName: echoLongTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo long
   */
  public void echoLongTest() throws Fault {
    long entity = Long.MIN_VALUE;
    setEntity(entity);
    setProperty(Property.REQUEST, buildRequest("primitivelong"));
    setProperty(Property.SEARCH_STRING, String.valueOf(entity));
    invoke();
  }

  /*
   * @testName: echoFullIntTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo int
   */
  public void echoFullIntTest() throws Fault {
    Integer entity = Integer.MIN_VALUE;
    setEntity(entity);
    setProperty(Property.REQUEST, buildRequest("fullint"));
    setProperty(Property.SEARCH_STRING, String.valueOf(entity));
    invoke();
  }

  /*
   * @testName: echoFullByteTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo byte
   */
  public void echoFullByteTest() throws Fault {
    Byte entity = Byte.MIN_VALUE;
    setEntity(entity);
    setProperty(Property.REQUEST, buildRequest("fullbyte"));
    setProperty(Property.SEARCH_STRING, String.valueOf(entity));
    invoke();
  }

  /*
   * @testName: echoFullCharTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo char
   */
  public void echoFullCharTest() throws Fault {
    Character entity = 'E';
    setEntity(entity);
    setProperty(Property.REQUEST, buildRequest("fullchar"));
    setProperty(Property.SEARCH_STRING, String.valueOf(entity));
    invoke();
  }

  /*
   * @testName: echoFullBooleanTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo boolean
   */
  public void echoFullBooleanTest() throws Fault {
    Boolean entity = true;
    setEntity(entity);
    setProperty(Property.REQUEST, buildRequest("fullboolean"));
    setProperty(Property.SEARCH_STRING, String.valueOf(entity));
    invoke();
  }

  /*
   * @testName: echoFullShortTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo short
   */
  public void echoFullShortTest() throws Fault {
    Short entity = Short.MAX_VALUE;
    setEntity(entity);
    setProperty(Property.REQUEST, buildRequest("fullshort"));
    setProperty(Property.SEARCH_STRING, String.valueOf(entity));
    invoke();
  }

  /*
   * @testName: echoFullDoubleTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo double
   */
  public void echoFullDoubleTest() throws Fault {
    Double entity = Double.MIN_VALUE;
    setEntity(entity);
    setProperty(Property.REQUEST, buildRequest("fulldouble"));
    setProperty(Property.SEARCH_STRING, String.valueOf(entity));
    invoke();
  }

  /*
   * @testName: echoFullFloatTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo float
   */
  public void echoFullFloatTest() throws Fault {
    Float entity = Float.MAX_VALUE;
    setEntity(entity);
    setProperty(Property.REQUEST, buildRequest("fullfloat"));
    setProperty(Property.SEARCH_STRING, String.valueOf(entity));
    invoke();
  }

  /*
   * @testName: echoFullLongTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo long
   */
  public void echoFullLongTest() throws Fault {
    Long entity = Long.MAX_VALUE;
    setEntity(entity);
    setProperty(Property.REQUEST, buildRequest("fulllong"));
    setProperty(Property.SEARCH_STRING, String.valueOf(entity));
    invoke();
  }

  // ------------------------------------------------------------------

  /*
   * @testName: byteToTextsTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
   * WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send byte receive other texts
   */
  public void byteToTextsTest() throws Fault {
    byte entity = 123;
    setEntity(entity);
    int index = getIndex("byte");
    invokeTextSequences(index + 1, String.valueOf(entity));
    invokeTextSequencesWithPathParam(index, String.valueOf(entity), "101");
  }

  /*
   * @testName: shortToTextsTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
   * WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send short receive other
   */
  public void shortToTextsTest() throws Fault {
    short entity = Short.MAX_VALUE;
    setEntity(entity);
    int index = getIndex("short");
    invokeTextSequences(index + 1, String.valueOf(entity));
    invokeTextSequencesWithPathParam(index, String.valueOf(entity), "1001");
  }

  /*
   * @testName: intToTextsTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
   * WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send int receive other
   */
  public void intToTextsTest() throws Fault {
    int entity = Short.MAX_VALUE; // Integer.MAX_VALUE is
    // 2.147483647E9 at Double
    setEntity(entity);
    int index = getIndex("int");
    invokeTextSequences(index + 1, String.valueOf(entity));
    invokeTextSequencesWithPathParam(index, String.valueOf(entity), "100001");
  }

  /*
   * @testName: longToTextsTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
   * WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send long receive other
   */
  public void longToTextsTest() throws Fault {
    long entity = Short.MIN_VALUE; // Long.MIN_VALUE is
    // -9.223372036854776E18 at Double
    setEntity(entity);
    int index = getIndex("long");
    invokeTextSequences(index + 1, String.valueOf(entity));
    invokeTextSequencesWithPathParam(index, String.valueOf(entity), "100001");
  }

  /*
   * @testName: floatToTextsTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
   * WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send float receive other
   */
  public void floatToTextsTest() throws Fault {
    float entity = Float.MIN_VALUE;
    setEntity(entity);
    int index = getIndex("float");
    invokeTextSequences(index + 1, String.valueOf(entity));
    invokeTextSequencesWithPathParam(index, String.valueOf(entity),
        String.valueOf(101.101f));
  }

  /*
   * @testName: doubleToTextsTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
   * WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send double receive other
   */
  public void doubleToTextsTest() throws Fault {
    double entity = Double.MAX_VALUE;
    setEntity(entity);
    invokeSequence(0, String.valueOf(entity), "", "", TEXT_TYPE_SEQUENCE);
    invokeSequence(0, String.valueOf(entity), "", "session",
        TEXT_TYPE_SEQUENCE);
    String pathParam = String.valueOf(101.101);
    invokeSequence(0, String.valueOf(entity) + pathParam, "",
        "pathparam/" + pathParam, TEXT_TYPE_SEQUENCE);
    invokeSequence(0, String.valueOf(entity) + pathParam, "",
        "sessionpathparam/" + pathParam, TEXT_TYPE_SEQUENCE);
  }

  /*
   * @testName: stringToTextsTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
   * WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send string receive other
   */
  public void stringToTextsTest() throws Fault {
    String param = "StringParam";
    setEntity(ECHO);
    invokeSequence(0, ECHO, "", "", TEXT_TYPE_SEQUENCE);
    invokeSequence(0, ECHO, "", "session", TEXT_TYPE_SEQUENCE);
    invokeSequence(0, ECHO + param, "", "pathparam/" + param,
        TEXT_TYPE_SEQUENCE);
    invokeSequence(0, ECHO + param, "", "sessionpathparam/" + param,
        TEXT_TYPE_SEQUENCE);
  }

  /*
   * @testName: stringToAllNumbersTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
   * WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send string receive other
   */
  public void stringToAllNumbersTest() throws Fault {
    String entity = String.valueOf(Byte.MAX_VALUE);
    String param = "111";
    setEntity(entity);
    invokeSequence(0, String.valueOf(entity), "primitive", "",
        TEXT_PRIMITIVE_SEQUENCE);
    invokeSequence(0, String.valueOf(entity), "full", "",
        TEXT_PRIMITIVE_SEQUENCE);
    invokeSequence(0, String.valueOf(entity), "primitive", "session",
        TEXT_PRIMITIVE_SEQUENCE);
    invokeSequence(0, String.valueOf(entity), "full", "session",
        TEXT_PRIMITIVE_SEQUENCE);

    invokeSequence(0, String.valueOf(entity) + param, "primitive",
        "pathparam/" + param, TEXT_PRIMITIVE_SEQUENCE);
    invokeSequence(0, String.valueOf(entity) + param, "full",
        "pathparam/" + param, TEXT_PRIMITIVE_SEQUENCE);
    invokeSequence(0, String.valueOf(entity) + param, "primitive",
        "sessionpathparam/" + param, TEXT_PRIMITIVE_SEQUENCE);
    invokeSequence(0, String.valueOf(entity) + param, "full",
        "sessionpathparam/" + param, TEXT_PRIMITIVE_SEQUENCE);
  }

  /*
   * @testName: booleanToBooleanWithPathParamTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
   * WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send boolean receive boolean
   */
  public void booleanToBooleanWithPathParamTest() throws Fault {
    boolean entity = true;
    String[] SEQUENCE = { "primitiveboolean", "fullboolean" };
    setEntity(entity);
    invokeSequence(0, "true", "", "", SEQUENCE);
    invokeSequence(0, "true", "", "session", SEQUENCE);
    invokeSequence(0, "truefalse", "", "pathparam/false", SEQUENCE);
    invokeSequence(0, "truefalse", "", "sessionpathparam/false", SEQUENCE);
  }

  /*
   * @testName: stringToBooleanTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
   * WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send string receive boolean
   */
  public void stringToBooleanTest() throws Fault {
    String entity = "true";
    String[] SEQUENCE = { "primitiveboolean", "fullboolean" };
    setEntity(entity);
    invokeSequence(0, entity, "", "", SEQUENCE);
    invokeSequence(0, entity, "", "session", SEQUENCE);
    invokeSequence(0, "truefalse", "", "pathparam/false", SEQUENCE);
    invokeSequence(0, "truefalse", "", "sessionpathparam/false", SEQUENCE);
  }

  /*
   * @testName: stringToCharTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
   * WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send string receive char
   */
  public void stringToCharTest() throws Fault {
    String entity = "&";
    String[] SEQUENCE = { "primitivechar", "fullchar" };
    setEntity(entity);
    invokeSequence(0, entity, "", "", SEQUENCE);
    invokeSequence(0, entity, "", "session", SEQUENCE);
    invokeSequence(0, entity + "X", "", "pathparam/X", SEQUENCE);
    invokeSequence(0, entity + "y", "", "sessionpathparam/y", SEQUENCE);
  }

  /*
   * @testName: charToTextsTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
   * WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send char receive other
   */
  public void charToTextsTest() throws Fault {
    char entity = '1';
    setEntity(entity);
    invokeTextSequences(0, String.valueOf(entity));
    invokeTextSequencesWithPathParam(0, String.valueOf(entity), "99");
  }

  /*
   * @testName: partialStringTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
   * WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send string and string receive the strings, too
   */
  public void partialStringTest() throws Fault {
    setCountDownLatchCount(2);
    String partial2 = "partialStringTest";
    String response = ECHO + "(false)" + partial2 + "(true)";
    setEntity(ECHO, partial2);
    setProperty(Property.REQUEST, buildRequest("partialstring"));
    setProperty(Property.SEARCH_STRING, response);
    invoke();
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
    String partial2 = "partialStringAndSessionTest";
    String response = ECHO + "(false)" + partial2 + "(true)";
    setEntity(ECHO, partial2);
    setProperty(Property.REQUEST, buildRequest("partialstringsession"));
    setProperty(Property.SEARCH_STRING, response);
    invoke();
  }

  /*
   * @testName: partialStringWithPathParamTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
   * WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send string and string receive the strings, too
   */
  public void partialStringWithPathParamTest() throws Fault {
    setCountDownLatchCount(2);
    String partial2 = "partialStringTest";
    String response = ECHO + "(false)" + partial2 + partial2 + "(true)"
        + partial2;
    setEntity(ECHO, partial2);
    setProperty(Property.REQUEST,
        buildRequest("partialstringpathparam/" + partial2));
    setProperty(Property.SEARCH_STRING, response);
    invoke();
  }

  /*
   * @testName: partialStringAndSessionWithPathParamTest
   * 
   * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
   * WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send string and string receive the combined string
   */
  public void partialStringAndSessionWithPathParamTest() throws Fault {
    String partial2 = "partialStringAndSessionTest";
    String response = ECHO + "(false)" + partial2 + partial2 + "(true)"
        + partial2;
    setEntity(ECHO, partial2);
    setProperty(Property.REQUEST,
        buildRequest("partialstringsessionpathparam/" + partial2));
    setProperty(Property.SEARCH_STRING, response);
    invoke();
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
    setEntity(ByteBuffer.wrap(ECHO.getBytes()));
    setProperty(Property.REQUEST, buildRequest("bytebuffer"));
    setProperty(Property.SEARCH_STRING, ECHO);
    invoke();
  }

  /*
   * @testName: byteBufferToBytesTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: Send ByteBuffer receive bytes
   */
  public void byteBufferToBytesTest() throws Fault {
    setEntity(ByteBuffer.wrap(ECHO.getBytes()));
    setProperty(Property.REQUEST, buildRequest("bytearray"));
    setProperty(Property.SEARCH_STRING, ECHO);
    invoke();
  }

  /*
   * @testName: byteBufferToBinaryTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo byte array
   */
  public void byteBufferToBinaryTest() throws Fault {
    String param = "byteBufferToBinaryTest";
    setEntity(ByteBuffer.wrap(ECHO.getBytes()));
    invokeSequence(0, ECHO, "", "", BINARY_SEQUENCE);
    invokeSequence(0, ECHO, "", "session", BINARY_SEQUENCE);
    invokeSequence(0, ECHO + param, "", "pathparam/" + param, BINARY_SEQUENCE);
    invokeSequence(0, ECHO + param, "", "sessionpathparam/" + param,
        BINARY_SEQUENCE);
  }

  /*
   * @testName: pongToPongTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: echo byte array
   */
  public void pongToPongTest() throws Fault {
    setEntity(new StringPongMessage(ECHO));
    String[] sequence = { "pongmessage", "pongmessagesession" };
    invokeSequence(0, ECHO, "", "", sequence);
    String[] paramsequence = { "pongmessagepathparam/param",
        "pongmessagesessionpathparam/param" };
    invokeSequence(0, ECHO + "param", "", "", paramsequence);
  }

  /*
   * @testName: partialByteArrayTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send bytearray and bytearray receive the combined message
   */
  public void partialByteArrayTest() throws Fault {
    setCountDownLatchCount(2);
    String partial2 = "partialByteArrayTest";
    String response = ECHO + "(false)" + partial2 + "(true)";
    setEntity(ByteBuffer.wrap(ECHO.getBytes()),
        ByteBuffer.wrap(partial2.getBytes()));
    setProperty(Property.REQUEST, buildRequest("partialbytearray"));
    setProperty(Property.SEARCH_STRING, response);
    invoke();
  }

  /*
   * @testName: partialByteArrayAndSessionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send bytearray and bytearray receive the combined message
   */
  public void partialByteArrayAndSessionTest() throws Fault {
    String partial2 = "partialByteArrayAndSessionTest";
    String response = ECHO + "(false)" + partial2 + "(true)";
    setEntity(ByteBuffer.wrap(ECHO.getBytes()),
        ByteBuffer.wrap(partial2.getBytes()));
    setProperty(Property.REQUEST, buildRequest("partialbytearraysession"));
    setProperty(Property.SEARCH_STRING, response);
    invoke();
  }

  /*
   * @testName: partialByteArrayWithPathParamTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send bytearray and bytearray receive the combined message
   */
  public void partialByteArrayWithPathParamTest() throws Fault {
    setCountDownLatchCount(2);
    String partial2 = "partialByteArrayWithPathParamTest";
    String response = ECHO + "(false)[" + partial2 + "]" + partial2 + "(true)["
        + partial2 + "]";
    setEntity(ByteBuffer.wrap(ECHO.getBytes()),
        ByteBuffer.wrap(partial2.getBytes()));
    setProperty(Property.REQUEST,
        buildRequest("partialbytearraypathparam/" + partial2));
    setProperty(Property.SEARCH_STRING, response);
    invoke();
  }

  /*
   * @testName: partialByteArrayAndSessionWithPathParamTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send bytearray and bytearray receive the combined message
   */
  public void partialByteArrayAndSessionWithPathParamTest() throws Fault {
    String partial2 = "partialByteArrayAndSessionWithPathParamTest";
    String response = ECHO + "(false)[" + partial2 + "]" + partial2 + "(true)["
        + partial2 + "]";
    setEntity(ByteBuffer.wrap(ECHO.getBytes()),
        ByteBuffer.wrap(partial2.getBytes()));
    setProperty(Property.REQUEST,
        buildRequest("partialbytearraysessionpathparam/" + partial2));
    setProperty(Property.SEARCH_STRING, response);
    invoke();
  }

  /*
   * @testName: partialByteBufferTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send ByteBuffer and ByteBuffer receive the combined message
   */
  public void partialByteBufferTest() throws Fault {
    setCountDownLatchCount(2);
    String partial2 = "partialByteBufferTest";
    String response = ECHO + "(false)" + partial2 + "(true)";
    setEntity(ByteBuffer.wrap(ECHO.getBytes()),
        ByteBuffer.wrap(partial2.getBytes()));
    setProperty(Property.REQUEST, buildRequest("partialbytebuffer"));
    setProperty(Property.SEARCH_STRING, response);
    invoke();
  }

  /*
   * @testName: partialByteBufferAndSessionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send ByteBuffer and ByteBuffer receive the combined message
   */
  public void partialByteBufferAndSessionTest() throws Fault {
    String partial2 = "partialByteBufferAndSessionTest";
    String response = ECHO + "(false)" + partial2 + "(true)";
    setEntity(ByteBuffer.wrap(ECHO.getBytes()),
        ByteBuffer.wrap(partial2.getBytes()));
    setProperty(Property.REQUEST, buildRequest("partialbytebuffersession"));
    setProperty(Property.SEARCH_STRING, response);
    invoke();
  }

  /*
   * @testName: partialByteBufferWithPathParamTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send ByteBuffer and ByteBuffer receive the combined message
   */
  public void partialByteBufferWithPathParamTest() throws Fault {
    setCountDownLatchCount(2);
    String partial2 = "partialByteBufferWithPathParamTest";
    String response = ECHO + "(false)[" + partial2 + "]" + partial2 + "(true)["
        + partial2 + "]";
    setEntity(ByteBuffer.wrap(ECHO.getBytes()),
        ByteBuffer.wrap(partial2.getBytes()));
    setProperty(Property.REQUEST,
        buildRequest("partialbytebufferpathparam/" + partial2));
    setProperty(Property.SEARCH_STRING, response);
    invoke();
  }

  /*
   * @testName: partialByteBufferAndSessionWithPathParamTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80;
   * 
   * @test_Strategy: send ByteBuffer and ByteBuffer receive the combined message
   */
  public void partialByteBufferAndSessionWithPathParamTest() throws Fault {
    String partial2 = "partialByteBufferAndSessionWithPathParamTest";
    String response = ECHO + "(false)[" + partial2 + "]" + partial2 + "(true)["
        + partial2 + "]";
    setEntity(ByteBuffer.wrap(ECHO.getBytes()),
        ByteBuffer.wrap(partial2.getBytes()));
    setProperty(Property.REQUEST,
        buildRequest("partialbytebuffersessionpathparam/" + partial2));
    setProperty(Property.SEARCH_STRING, response);
    invoke();
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
    String entity = "12345";
    setEntity(entity);
    setProperty(Property.REQUEST, buildRequest("maxlen"));
    setProperty(Property.SEARCH_STRING, entity);
    invoke();
  }

  /*
   * @testName: maxLengthFailsTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:80; WebSocket:SPEC:WSC-4.7.1-1;
   * 
   * @test_Strategy: the message is longer than maxMessageLength
   */
  public void maxLengthFailsTest() throws Fault {
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
    setProperty(Property.REQUEST, buildRequest("maxlen"));
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
    setEntity("123456789");
    setProperty(Property.REQUEST, buildRequest("defaultmaxlen"));
    setProperty(Property.SEARCH_STRING, "-1");
    invoke();
  }

  // ////////////////////////////////////////////////////////////////////
  private void invokeSequence(int startIndex, String search, String prefix,
      String suffix, String[] sequence) throws Fault {
    for (int i = startIndex; i != sequence.length; i++) {
      setProperty(Property.REQUEST, buildRequest(prefix, sequence[i], suffix));
      setProperty(Property.SEARCH_STRING, search);
      invoke();
    }
  }

  private void invokeTextSequences(int startIndex, String search) throws Fault {
    invokeSequence(startIndex, search, "primitive", "",
        TEXT_PRIMITIVE_SEQUENCE);
    invokeSequence(startIndex, search, "full", "", TEXT_PRIMITIVE_SEQUENCE);
    invokeSequence(0, search, "", "", TEXT_TYPE_SEQUENCE);
    invokeSequence(startIndex, search, "primitive", "session",
        TEXT_PRIMITIVE_SEQUENCE);
    invokeSequence(startIndex, search, "full", "session",
        TEXT_PRIMITIVE_SEQUENCE);
    invokeSequence(0, search, "", "session", TEXT_TYPE_SEQUENCE);
  }

  private void invokeTextSequencesWithPathParam(int startIndex, String content,
      String pathParam) throws Fault {
    String suffix = "pathparam/" + pathParam;
    String search = content + pathParam;
    invokeSequence(startIndex, search, "primitive", suffix,
        TEXT_PRIMITIVE_SEQUENCE);
    invokeSequence(startIndex, search, "full", suffix, TEXT_PRIMITIVE_SEQUENCE);
    invokeSequence(0, search, "", suffix, TEXT_TYPE_SEQUENCE);
    invokeSequence(startIndex, search, "primitive", "session" + suffix,
        TEXT_PRIMITIVE_SEQUENCE);
    invokeSequence(startIndex, search, "full", "session" + suffix,
        TEXT_PRIMITIVE_SEQUENCE);
    invokeSequence(0, search, "", "session" + suffix, TEXT_TYPE_SEQUENCE);

  }
}
