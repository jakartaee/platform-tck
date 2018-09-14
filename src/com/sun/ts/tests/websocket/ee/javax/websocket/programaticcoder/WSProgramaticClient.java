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

package com.sun.ts.tests.websocket.ee.javax.websocket.programaticcoder;

import java.util.LinkedList;
import java.util.List;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.Decoder;
import javax.websocket.Encoder;

import com.sun.ts.tests.websocket.common.stringbean.StringBean;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanClientEndpoint;
import com.sun.ts.tests.websocket.ee.javax.websocket.coder.InitDestroyBinaryDecoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.coder.InitDestroyBinaryEncoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.coder.InitDestroyBinaryStreamDecoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.coder.InitDestroyBinaryStreamEncoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.coder.InitDestroyTextDecoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.coder.InitDestroyTextEncoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.coder.InitDestroyTextStreamDecoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.coder.InitDestroyTextStreamEncoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.coder.WSClient;
import com.sun.ts.tests.websocket.ee.javax.websocket.coder.WillDecodeFirstBinaryDecoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.coder.WillDecodeFirstTextDecoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.coder.WillDecodeSecondBinaryDecoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.coder.WillDecodeSecondTextDecoder;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSProgramaticClient extends WSClient {

  private static final long serialVersionUID = 5189314464250884426L;

  public WSProgramaticClient() {
    setContextRoot("wsc_ee_programaticcoder_web");
  }

  public static void main(String[] args) {
    new WSProgramaticClient().run(args);
  }

  /* Run test */
  /*
   * @testName: textEncoderInitDestroyTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:54; WebSocket:JAVADOC:55;
   * WebSocket:JAVADOC:61; WebSocket:SPEC:WSC-4.1.2-1;
   * 
   * @test_Strategy: test text encoder init and destroy were called The
   * websocket implementation creates a new instance of the encoder per endpoint
   * instance per connection.
   *
   * The implementation must attempt to encode application objects of matching
   * parametrized type as the encoder when they are attempted to be sent
   *
   * encode
   */
  public void textEncoderInitDestroyTest() throws Fault {
    super.textEncoderInitDestroyTest();
  }

  /*
   * @testName: textStreamEncoderInitDestroyTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:54; WebSocket:JAVADOC:55;
   * WebSocket:JAVADOC:63; WebSocket:SPEC:WSC-4.1.2-1;
   * 
   * @test_Strategy: test text stream encoder init and destroy were called The
   * websocket implementation creates a new instance of the encoder per endpoint
   * instance per connection.
   *
   * The implementation must attempt to encode application objects of matching
   * parametrized type as the encoder when they are attempted to be sent
   *
   * encode
   */
  public void textStreamEncoderInitDestroyTest() throws Fault {
    super.textStreamEncoderInitDestroyTest();
  }

  /*
   * @testName: binaryEncoderInitDestroyTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:54; WebSocket:JAVADOC:55;
   * WebSocket:JAVADOC:56; WebSocket:SPEC:WSC-4.1.2-1;
   * 
   * @test_Strategy: test binary encoder init and destroy were called The
   * websocket implementation creates a new instance of the encoder per endpoint
   * instance per connection.
   *
   * The implementation must attempt to encode application objects of matching
   * parametrized type as the encoder when they are attempted to be sent
   *
   * encode
   */
  public void binaryEncoderInitDestroyTest() throws Fault {
    super.binaryEncoderInitDestroyTest();
  }

  /*
   * @testName: binaryStreamEncoderInitDestroyTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:54; WebSocket:JAVADOC:55;
   * WebSocket:JAVADOC:58; WebSocket:SPEC:WSC-4.1.2-1;
   * 
   * @test_Strategy: test binary stream encoder init and destroy were called The
   * websocket implementation creates a new instance of the encoder per endpoint
   * instance per connection.
   *
   * The implementation must attempt to encode application objects of matching
   * parametrized type as the encoder when they are attempted to be sent
   *
   * encode
   */
  public void binaryStreamEncoderInitDestroyTest() throws Fault {
    super.binaryStreamEncoderInitDestroyTest();
  }

  // ----------------------------------------------------------------------

  /*
   * @testName: textDecoderInitDestroyTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:35; WebSocket:JAVADOC:36;
   * WebSocket:JAVADOC:43; WebSocket:JAVADOC:45; WebSocket:SPEC:WSC-4.1.3-1;
   * 
   * @test_Strategy: test text decoder init and destroy were called The
   * websocket implementation creates a new instance of the encoder per endpoint
   * instance per connection.
   *
   * The implementation must attempt to decode websocket messages using the
   * decoder in the list appropriate to the native websocket message type and
   * pass the message in decoded object form to the websocket endpoint
   *
   * decode, willdecode
   */
  public void textDecoderInitDestroyTest() throws Fault {
    super.textDecoderInitDestroyTest();
  }

  /*
   * @testName: textStreamDecoderInitDestroyTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:35; WebSocket:JAVADOC:36;
   * WebSocket:JAVADOC:46; WebSocket:SPEC:WSC-4.1.3-1;
   * 
   * @test_Strategy: test text stream decoder init and destroy were called The
   * websocket implementation creates a new instance of the encoder per endpoint
   * instance per connection.
   *
   * The implementation must attempt to decode websocket messages using the
   * decoder in the list appropriate to the native websocket message type and
   * pass the message in decoded object form to the websocket endpoint
   *
   * decode
   */
  public void textStreamDecoderInitDestroyTest() throws Fault {
    super.textStreamDecoderInitDestroyTest();
  }

  /*
   * @testName: binaryDecoderInitDestroyTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:35; WebSocket:JAVADOC:36;
   * WebSocket:JAVADOC:37; WebSocket:JAVADOC:39; WebSocket:SPEC:WSC-4.1.3-1;
   * 
   * @test_Strategy: test binary decoder init and destroy were called The
   * websocket implementation creates a new instance of the encoder per endpoint
   * instance per connection.
   *
   * The implementation must attempt to decode websocket messages using the
   * decoder in the list appropriate to the native websocket message type and
   * pass the message in decoded object form to the websocket endpoint
   *
   * decode, willDecode
   */
  public void binaryDecoderInitDestroyTest() throws Fault {
    super.binaryDecoderInitDestroyTest();
  }

  /*
   * @testName: binaryStreamDecoderInitDestroyTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:35; WebSocket:JAVADOC:36;
   * WebSocket:JAVADOC:40; WebSocket:SPEC:WSC-4.1.3-1;
   * 
   * @test_Strategy: test binary stream decoder init and destroy were called The
   * websocket implementation creates a new instance of the encoder per endpoint
   * instance per connection.
   *
   * The implementation must attempt to decode websocket messages using the
   * decoder in the list appropriate to the native websocket message type and
   * pass the message in decoded object form to the websocket endpoint
   *
   * decode
   */
  public void binaryStreamDecoderInitDestroyTest() throws Fault {
    super.binaryStreamDecoderInitDestroyTest();
  }

  // ----------------------------------------------------------------------

  /*
   * @testName: textEncoderEncodeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:61; WebSocket:SPEC:WSC-4.1.2-1;
   * 
   * @test_Strategy: test text encoder encode was called
   *
   * The implementation must attempt to encode application objects of matching
   * parametrized type as the encoder when they are attempted to be sent
   */
  public void textEncoderEncodeTest() throws Fault {
    super.textEncoderEncodeTest();
  }

  /*
   * @testName: textStreamEncoderEncodeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:63; WebSocket:SPEC:WSC-4.1.2-1;
   * 
   * @test_Strategy: test text stream encode was called
   *
   * The implementation must attempt to encode application objects of matching
   * parametrized type as the encoder when they are attempted to be sent
   */
  public void textStreamEncoderEncodeTest() throws Fault {
    super.textStreamEncoderEncodeTest();
  }

  /*
   * @testName: binaryEncoderEncodeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:56; WebSocket:SPEC:WSC-4.1.2-1;
   * 
   * @test_Strategy: test binary encoder encode was called
   *
   * The implementation must attempt to encode application objects of matching
   * parametrized type as the encoder when they are attempted to be sent
   */
  public void binaryEncoderEncodeTest() throws Fault {
    super.binaryEncoderEncodeTest();
  }

  /*
   * @testName: binaryStreamEncoderEncodeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:58; WebSocket:SPEC:WSC-4.1.2-1;
   * 
   * @test_Strategy: test binary stream encoder encode was called
   *
   * The implementation must attempt to encode application objects of matching
   * parametrized type as the encoder when they are attempted to be sent
   */
  public void binaryStreamEncoderEncodeTest() throws Fault {
    super.binaryStreamEncoderEncodeTest();
  }

  // -----------------------------------------------------------------------

  /*
   * @testName: textDecoderDecodeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:43; WebSocket:JAVADOC:45;
   * WebSocket:SPEC:WSC-4.1.3-1; WebSocket:SPEC:WSC-4.1.3-2;
   * 
   * @test_Strategy: test text decoder decode and willDecode were called
   *
   * The implementation must attempt to decode websocket messages using the
   * decoder in the list appropriate to the native websocket message type and
   * pass the message in decoded object form to the websocket endpoint
   */
  public void textDecoderDecodeTest() throws Fault {
    super.textDecoderDecodeTest();
  }

  /*
   * @testName: textStreamDecoderDecodeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:46; WebSocket:SPEC:WSC-4.1.3-1;
   * 
   * @test_Strategy: test text stream decoder decode was called
   *
   * The implementation must attempt to decode websocket messages using the
   * decoder in the list appropriate to the native websocket message type and
   * pass the message in decoded object form to the websocket endpoint
   */
  public void textStreamDecoderDecodeTest() throws Fault {
    super.textStreamDecoderDecodeTest();
  }

  /*
   * @testName: binaryDecoderDecodeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:37; WebSocket:JAVADOC:39;
   * WebSocket:SPEC:WSC-4.1.3-1; WebSocket:SPEC:WSC-4.1.3-2;
   * 
   * @test_Strategy: test binary decoder decode and willDecode were called
   *
   * The implementation must attempt to decode websocket messages using the
   * decoder in the list appropriate to the native websocket message type and
   * pass the message in decoded object form to the websocket endpoint
   */
  public void binaryDecoderDecodeTest() throws Fault {
    super.binaryDecoderDecodeTest();
  }

  /*
   * @testName: binaryStreamDecoderDecodeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:40; WebSocket:SPEC:WSC-4.1.3-1;
   * 
   * @test_Strategy: test binary stream decoder decode was called
   *
   * The implementation must attempt to decode websocket messages using the
   * decoder in the list appropriate to the native websocket message type and
   * pass the message in decoded object form to the websocket endpoint
   */
  public void binaryStreamDecoderDecodeTest() throws Fault {
    super.binaryStreamDecoderDecodeTest();
  }

  /*
   * @testName: binaryDecoderWillDecodeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:37; WebSocket:JAVADOC:39;
   * WebSocket:SPEC:WSC-4.1.3-1; WebSocket:SPEC:WSC-4.1.3-2;
   * 
   * @test_Strategy: test binary decoder with willDecode returning false will
   * not be used
   */
  public void binaryDecoderWillDecodeTest() throws Fault {
    super.binaryDecoderWillDecodeTest();
  }

  /*
   * @testName: textDecoderWillDecodeTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:43; WebSocket:JAVADOC:45;
   * WebSocket:SPEC:WSC-4.1.3-1; WebSocket:SPEC:WSC-4.1.3-2;
   * 
   * @test_Strategy: test text decoder with willDecode returning false will not
   * be used
   */
  public void textDecoderWillDecodeTest() throws Fault {
    super.textDecoderWillDecodeTest();
  }

  // =====================================================================

  /*
   * @testName: textEncoderInitDestroyOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:54; WebSocket:JAVADOC:55;
   * WebSocket:JAVADOC:61; WebSocket:SPEC:WSC-4.1.2-1;
   * 
   * @test_Strategy: test text encoder init and destroy were called The
   * websocket implementation creates a new instance of the encoder per endpoint
   * instance per connection.
   *
   * The implementation must attempt to encode application objects of matching
   * parametrized type as the encoder when they are attempted to be sent
   *
   * encode
   */
  public void textEncoderInitDestroyOnClientTest() throws Fault {
    clientClear();
    setEncoder(InitDestroyTextEncoder.class);
    invoke("simpleecho", new StringBean(ECHO), ECHO);
    assertInit(InitDestroyTextEncoder.class);
    assertDestroy(InitDestroyTextEncoder.class);
  }

  /*
   * @testName: textStreamEncoderInitDestroyOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:54; WebSocket:JAVADOC:55;
   * WebSocket:JAVADOC:63; WebSocket:SPEC:WSC-4.1.2-1;
   * 
   * @test_Strategy: test text stream encoder init and destroy were called The
   * websocket implementation creates a new instance of the encoder per endpoint
   * instance per connection.
   *
   * The implementation must attempt to encode application objects of matching
   * parametrized type as the encoder when they are attempted to be sent
   *
   * encode
   */
  public void textStreamEncoderInitDestroyOnClientTest() throws Fault {
    clientClear();
    setEncoder(InitDestroyTextStreamEncoder.class);
    invoke("simpleecho", new StringBean(ECHO), ECHO);
    assertInit(InitDestroyTextStreamEncoder.class);
    assertDestroy(InitDestroyTextStreamEncoder.class);
  }

  /*
   * @testName: binaryEncoderInitDestroyOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:54; WebSocket:JAVADOC:55;
   * WebSocket:JAVADOC:56; WebSocket:SPEC:WSC-4.1.2-1;
   * 
   * @test_Strategy: test binary encoder init and destroy were called The
   * websocket implementation creates a new instance of the encoder per endpoint
   * instance per connection.
   *
   * The implementation must attempt to encode application objects of matching
   * parametrized type as the encoder when they are attempted to be sent
   *
   * encode
   */
  public void binaryEncoderInitDestroyOnClientTest() throws Fault {
    clientClear();
    setEncoder(InitDestroyBinaryEncoder.class);
    invoke("simpleecho", new StringBean(ECHO), ECHO);
    assertInit(InitDestroyBinaryEncoder.class);
    assertDestroy(InitDestroyBinaryEncoder.class);
  }

  /*
   * @testName: binaryStreamEncoderInitDestroyOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:54; WebSocket:JAVADOC:55;
   * WebSocket:JAVADOC:58; WebSocket:SPEC:WSC-4.1.2-1;
   * 
   * @test_Strategy: test binary stream encoder init and destroy were called The
   * websocket implementation creates a new instance of the encoder per endpoint
   * instance per connection.
   *
   * The implementation must attempt to encode application objects of matching
   * parametrized type as the encoder when they are attempted to be sent
   *
   * encode
   */
  public void binaryStreamEncoderInitDestroyOnClientTest() throws Fault {
    clientClear();
    setEncoder(InitDestroyBinaryStreamEncoder.class);
    invoke("simpleecho", new StringBean(ECHO), ECHO);
    assertInit(InitDestroyBinaryStreamEncoder.class);
    assertDestroy(InitDestroyBinaryStreamEncoder.class);
  }

  // ---------------------------------------------------------------------

  /*
   * @testName: textDecoderInitDestroyOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:35; WebSocket:JAVADOC:36;
   * WebSocket:JAVADOC:43; WebSocket:JAVADOC:45; WebSocket:SPEC:WSC-4.1.3-1;
   * 
   * @test_Strategy: test text decoder init and destroy were called The
   * websocket implementation creates a new instance of the encoder per endpoint
   * instance per connection.
   *
   * The implementation must attempt to decode websocket messages using the
   * decoder in the list appropriate to the native websocket message type and
   * pass the message in decoded object form to the websocket endpoint
   *
   * decode, willdecode
   */
  public void textDecoderInitDestroyOnClientTest() throws Fault {
    clientClear();
    setClientEndpointInstance(new StringBeanClientEndpoint());
    setDecoder(InitDestroyTextDecoder.class);
    invoke("simpleecho", ECHO, ECHO);
    assertInit(InitDestroyTextDecoder.class);
    assertDestroy(InitDestroyTextDecoder.class);
  }

  /*
   * @testName: textStreamDecoderInitDestroyOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:35; WebSocket:JAVADOC:36;
   * WebSocket:JAVADOC:46; WebSocket:SPEC:WSC-4.1.3-1;
   * 
   * @test_Strategy: test text stream decoder init and destroy were called The
   * websocket implementation creates a new instance of the encoder per endpoint
   * instance per connection.
   *
   * The implementation must attempt to decode websocket messages using the
   * decoder in the list appropriate to the native websocket message type and
   * pass the message in decoded object form to the websocket endpoint
   *
   * decode
   */
  public void textStreamDecoderInitDestroyOnClientTest() throws Fault {
    clientClear();
    setClientEndpointInstance(new StringBeanClientEndpoint());
    setDecoder(InitDestroyTextStreamDecoder.class);
    invoke("simpleecho", ECHO, ECHO);
    assertInit(InitDestroyTextStreamDecoder.class);
    assertDestroy(InitDestroyTextStreamDecoder.class);
  }

  /*
   * @testName: binaryDecoderInitDestroyOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:35; WebSocket:JAVADOC:36;
   * WebSocket:JAVADOC:37; WebSocket:JAVADOC:39; WebSocket:SPEC:WSC-4.1.3-1;
   * 
   * @test_Strategy: test binary decoder init and destroy were called The
   * websocket implementation creates a new instance of the encoder per endpoint
   * instance per connection.
   *
   * The implementation must attempt to decode websocket messages using the
   * decoder in the list appropriate to the native websocket message type and
   * pass the message in decoded object form to the websocket endpoint
   *
   * decode, willDecode
   */
  public void binaryDecoderInitDestroyOnClientTest() throws Fault {
    clientClear();
    setClientEndpointInstance(new StringBeanClientEndpoint());
    setDecoder(InitDestroyBinaryDecoder.class);
    invoke("simplebin", ECHO, ECHO);
    assertInit(InitDestroyBinaryDecoder.class);
    assertDestroy(InitDestroyBinaryDecoder.class);
  }

  /*
   * @testName: binaryStreamDecoderInitDestroyOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:35; WebSocket:JAVADOC:36;
   * WebSocket:JAVADOC:40; WebSocket:SPEC:WSC-4.1.3-1;
   * 
   * @test_Strategy: test binary stream decoder init and destroy were called The
   * websocket implementation creates a new instance of the encoder per endpoint
   * instance per connection.
   *
   * The implementation must attempt to decode websocket messages using the
   * decoder in the list appropriate to the native websocket message type and
   * pass the message in decoded object form to the websocket endpoint
   *
   * decode
   */
  public void binaryStreamDecoderInitDestroyOnClientTest() throws Fault {
    clientClear();
    setClientEndpointInstance(new StringBeanClientEndpoint());
    setDecoder(InitDestroyBinaryStreamDecoder.class);
    invoke("simplebin", ECHO, ECHO);
    assertInit(InitDestroyBinaryStreamDecoder.class);
    assertDestroy(InitDestroyBinaryStreamDecoder.class);
  }

  // ----------------------------------------------------------------------

  /*
   * @testName: textEncoderEncodeOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:61; WebSocket:SPEC:WSC-4.1.2-1;
   * 
   * @test_Strategy: test text encoder encode was called
   *
   * The implementation must attempt to encode application objects of matching
   * parametrized type as the encoder when they are attempted to be sent
   */
  public void textEncoderEncodeOnClientTest() throws Fault {
    clientClear();
    setEncoder(InitDestroyTextEncoder.class);
    invoke("simpleecho", new StringBean(ECHO), ECHO);
    assertCode(InitDestroyTextEncoder.class);
  }

  /*
   * @testName: textStreamEncoderEncodeOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:63; WebSocket:SPEC:WSC-4.1.2-1;
   * 
   * @test_Strategy: test text stream encode was called
   *
   * The implementation must attempt to encode application objects of matching
   * parametrized type as the encoder when they are attempted to be sent
   */
  public void textStreamEncoderEncodeOnClientTest() throws Fault {
    clientClear();
    setEncoder(InitDestroyTextStreamEncoder.class);
    invoke("simpleecho", new StringBean(ECHO), ECHO);
    assertCode(InitDestroyTextStreamEncoder.class);
  }

  /*
   * @testName: binaryEncoderEncodeOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:56; WebSocket:SPEC:WSC-4.1.2-1;
   * 
   * @test_Strategy: test binary encoder encode was called
   *
   * The implementation must attempt to encode application objects of matching
   * parametrized type as the encoder when they are attempted to be sent
   */
  public void binaryEncoderEncodeOnClientTest() throws Fault {
    clientClear();
    setEncoder(InitDestroyBinaryEncoder.class);
    invoke("simpleecho", new StringBean(ECHO), ECHO);
    assertCode(InitDestroyBinaryEncoder.class);
  }

  /*
   * @testName: binaryStreamEncoderEncodeOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:58; WebSocket:SPEC:WSC-4.1.2-1;
   * 
   * @test_Strategy: test binary stream encoder encode was called
   *
   * The implementation must attempt to encode application objects of matching
   * parametrized type as the encoder when they are attempted to be sent
   */
  public void binaryStreamEncoderEncodeOnClientTest() throws Fault {
    clientClear();
    setEncoder(InitDestroyBinaryStreamEncoder.class);
    invoke("simpleecho", new StringBean(ECHO), ECHO);
    assertCode(InitDestroyBinaryStreamEncoder.class);
  }

  // ----------------------------------------------------------------------

  /*
   * @testName: textDecoderDecodeOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:43; WebSocket:JAVADOC:45;
   * WebSocket:SPEC:WSC-4.1.3-1; WebSocket:SPEC:WSC-4.1.3-2;
   * 
   * @test_Strategy: test text decoder decode and willDecode were called
   *
   * The implementation must attempt to decode websocket messages using the
   * decoder in the list appropriate to the native websocket message type and
   * pass the message in decoded object form to the websocket endpoint
   */
  public void textDecoderDecodeOnClientTest() throws Fault {
    clientClear();
    setDecoder(InitDestroyTextDecoder.class);
    setClientEndpointInstance(new StringBeanClientEndpoint());
    invoke("simpleecho", ECHO, ECHO);
    assertCode(InitDestroyTextDecoder.class);
    assertWillCode(InitDestroyTextDecoder.class);
  }

  /*
   * @testName: textStreamDecoderDecodeOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:46; WebSocket:SPEC:WSC-4.1.3-1;
   * 
   * @test_Strategy: test text stream decoder decode was called
   *
   * The implementation must attempt to decode websocket messages using the
   * decoder in the list appropriate to the native websocket message type and
   * pass the message in decoded object form to the websocket endpoint
   */
  public void textStreamDecoderDecodeOnClientTest() throws Fault {
    clientClear();
    setDecoder(InitDestroyTextStreamDecoder.class);
    setClientEndpointInstance(new StringBeanClientEndpoint());
    invoke("simpleecho", ECHO, ECHO);
    assertCode(InitDestroyTextStreamDecoder.class);
  }

  /*
   * @testName: binaryDecoderDecodeOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:37; WebSocket:JAVADOC:39;
   * WebSocket:SPEC:WSC-4.1.3-1; WebSocket:SPEC:WSC-4.1.3-2;
   * 
   * @test_Strategy: test binary decoder decode and willDecode were called
   *
   * The implementation must attempt to decode websocket messages using the
   * decoder in the list appropriate to the native websocket message type and
   * pass the message in decoded object form to the websocket endpoint
   */
  public void binaryDecoderDecodeOnClientTest() throws Fault {
    clientClear();
    setDecoder(InitDestroyBinaryDecoder.class);
    setClientEndpointInstance(new StringBeanClientEndpoint());
    invoke("simplebin", ECHO, ECHO);
    assertCode(InitDestroyBinaryDecoder.class);
    assertWillCode(InitDestroyBinaryDecoder.class);
  }

  /*
   * @testName: binaryStreamDecoderDecodeOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:40; WebSocket:SPEC:WSC-4.1.3-1;
   * 
   * @test_Strategy: test binary stream decoder decode was called
   *
   * The implementation must attempt to decode websocket messages using the
   * decoder in the list appropriate to the native websocket message type and
   * pass the message in decoded object form to the websocket endpoint
   */
  public void binaryStreamDecoderDecodeOnClientTest() throws Fault {
    clientClear();
    setDecoder(InitDestroyBinaryStreamDecoder.class);
    setClientEndpointInstance(new StringBeanClientEndpoint());
    invoke("simplebin", ECHO, ECHO);
    assertCode(InitDestroyBinaryStreamDecoder.class);
  }

  /*
   * @testName: binaryDecoderWillDecodeOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:37; WebSocket:JAVADOC:39;
   * WebSocket:SPEC:WSC-4.1.3-1; WebSocket:SPEC:WSC-4.1.3-2;
   * 
   * @test_Strategy: test binary decoder with willDecode returning false will
   * not be used
   */
  public void binaryDecoderWillDecodeOnClientTest() throws Fault {
    clientClear();
    setClientEndpointInstance(new StringBeanClientEndpoint());
    setDecoder(WillDecodeFirstBinaryDecoder.class,
        WillDecodeSecondBinaryDecoder.class);
    invoke("simplebin", ECHO, ECHO);
    assertWillCode(WillDecodeFirstBinaryDecoder.class);
    assertWillCode(WillDecodeSecondBinaryDecoder.class);
    assertNotCode(WillDecodeFirstBinaryDecoder.class);
    assertCode(WillDecodeSecondBinaryDecoder.class);
  }

  /*
   * @testName: textDecoderWillDecodeOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:43; WebSocket:JAVADOC:45;
   * WebSocket:SPEC:WSC-4.1.3-1; WebSocket:SPEC:WSC-4.1.3-2;
   * 
   * @test_Strategy: test text decoder with willDecode returning false will not
   * be used
   */
  public void textDecoderWillDecodeOnClientTest() throws Fault {
    clientClear();
    setClientEndpointInstance(new StringBeanClientEndpoint());
    setDecoder(WillDecodeFirstTextDecoder.class,
        WillDecodeSecondTextDecoder.class);
    invoke("simpleecho", ECHO, ECHO);
    assertWillCode(WillDecodeFirstTextDecoder.class);
    assertWillCode(WillDecodeSecondTextDecoder.class);
    assertNotCode(WillDecodeFirstTextDecoder.class);
    assertCode(WillDecodeSecondTextDecoder.class);
  }

  /////////////////////////////////////////////////////////////////////////

  @SafeVarargs
  private final void setEncoder(Class<? extends Encoder>... encoders) {
    List<Class<? extends Encoder>> list = new LinkedList<Class<? extends Encoder>>();
    for (Class<? extends Encoder> encoder : encoders)
      list.add(encoder);
    ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
        .encoders(list).build();
    setClientEndpointConfig(config);
  }

  @SafeVarargs
  private final void setDecoder(Class<? extends Decoder>... decoders) {
    List<Class<? extends Decoder>> list = new LinkedList<Class<? extends Decoder>>();
    for (Class<? extends Decoder> decoder : decoders)
      list.add(decoder);
    ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
        .decoders(list).build();
    setClientEndpointConfig(config);
  }
}
