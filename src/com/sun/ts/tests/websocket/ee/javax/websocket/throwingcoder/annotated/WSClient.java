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

package com.sun.ts.tests.websocket.ee.javax.websocket.throwingcoder.annotated;

import java.nio.ByteBuffer;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.stringbean.StringBean;
import com.sun.ts.tests.websocket.ee.javax.websocket.throwingcoder.ThrowingTextDecoder;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSClient extends WebSocketCommonClient {
  private static final long serialVersionUID = -8094860820359975543L;

  protected static final String ECHO = "Echo message";

  public WSClient() {
    setContextRoot("ee_java_websocket_throwingcoder_annotated_web");
    logExceptionOnInvocation(false);
  }

  public static void main(String[] args) {
    new WSClient().run(args);
  }

  /* Run test */
  /*
   * @testName: binaryDecoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:38;
   * 
   * @test_Strategy: test the binary decoder throws decoderException and it is
   * caught in @OnError
   */
  public void binaryDecoderThrowAndCatchOnServerTest() throws Fault {
    invoke("binarydecoder", ByteBuffer.wrap(ECHO.getBytes()),
        ThrowingTextDecoder.ERR_MSG);
    logMsg("The DecoderException has been propagated to @OnError");
  }

  /*
   * @testName: binaryDecoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:38;
   * 
   * @test_Strategy: test the binary decoder throws decoderException and it is
   * caught in @OnError
   */
  public void binaryDecoderThrowAndCatchOnClientTest() throws Fault {
    WSCClientEndpointWithBinaryDecoder endpoint = new WSCClientEndpointWithBinaryDecoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simplebin", ECHO);
    logMsg("The DecoderException has been propagated to @OnError");
  }

  /*
   * @testName: binaryStreamDecoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:41;
   * 
   * @test_Strategy: test the binary stream decoder throws decoderException and
   * it is caught in @OnError
   */
  public void binaryStreamDecoderThrowAndCatchOnServerTest() throws Fault {
    invoke("binarystreamdecoder", ByteBuffer.wrap(ECHO.getBytes()),
        ThrowingTextDecoder.ERR_MSG);
    logMsg("The DecoderException has been propagated to @OnError");
  }

  /*
   * @testName: binaryStreamDecoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:41;
   * 
   * @test_Strategy: test the binary stream decoder throws decoderException and
   * it is caught in @OnError
   */
  public void binaryStreamDecoderThrowAndCatchOnClientTest() throws Fault {
    WSCClientEndpointWithBinaryStreamDecoder endpoint = new WSCClientEndpointWithBinaryStreamDecoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simplebin", ECHO);
    logMsg("The DecoderException has been propagated to @OnError");
  }

  /*
   * @testName: textDecoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:44;
   * 
   * @test_Strategy: test the text decoder throws decoderException and it is
   * caught in @OnError
   */
  public void textDecoderThrowAndCatchOnServerTest() throws Fault {
    invoke("textdecoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The DecoderException has been propagated to @OnError");
  }

  /*
   * @testName: textDecoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:44;
   * 
   * @test_Strategy: test the text decoder throws decoderException and it is
   * caught in @OnError
   */
  public void textDecoderThrowAndCatchOnClientTest() throws Fault {
    WSCClientEndpointWithTextDecoder endpoint = new WSCClientEndpointWithTextDecoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", ECHO);
    logMsg("The DecoderException has been propagated to @OnError");
  }

  /*
   * @testName: textStreamDecoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:47;
   * 
   * @test_Strategy: test the text stream decoder throws decoderException and it
   * is caught in @OnError
   */
  public void textStreamDecoderThrowAndCatchOnServerTest() throws Fault {
    invoke("textstreamdecoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The DecoderException has been propagated to @OnError");
  }

  /*
   * @testName: textStreamDecoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:47;
   * 
   * @test_Strategy: test the text stream decoder throws decoderException and it
   * is caught in @OnError
   */
  public void textStreamDecoderThrowAndCatchOnClientTest() throws Fault {
    WSCClientEndpointWithTextStreamDecoder endpoint = new WSCClientEndpointWithTextStreamDecoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", ECHO);
    logMsg("The DecoderException has been propagated to @OnError");
  }

  // ---------------------------------------------------------------------

  /*
   * @testName: binaryEncoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:57;
   * 
   * @test_Strategy: test the binary encoder throws EncoderException and it is
   * caught in @OnError
   */
  public void binaryEncoderThrowAndCatchOnServerTest() throws Fault {
    invoke("binaryencoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: binaryEncoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:57;
   * 
   * @test_Strategy: test the binary encoder throws EncoderException and it is
   * caught in @OnError
   */
  public void binaryEncoderThrowAndCatchOnClientTest() throws Fault {
    WSCClientEndpointWithBinaryEncoder endpoint = new WSCClientEndpointWithBinaryEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", new StringBean(ECHO));
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: binaryStreamEncoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:59;
   * 
   * @test_Strategy: test the binary stream encoder throws EncoderException and
   * it is caught in @OnError
   */
  public void binaryStreamEncoderThrowAndCatchOnServerTest() throws Fault {
    invoke("binarystreamencoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: binaryStreamEncoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:59;
   * 
   * @test_Strategy: test the binary stream encoder throws EncoderException and
   * it is caught in @OnError
   */
  public void binaryStreamEncoderThrowAndCatchOnClientTest() throws Fault {
    WSCClientEndpointWithBinaryStreamEncoder endpoint = new WSCClientEndpointWithBinaryStreamEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", new StringBean(ECHO));
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: textEncoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:62;
   * 
   * @test_Strategy: test the text encoder throws EncoderException and it is
   * caught in @OnError
   */
  public void textEncoderThrowAndCatchOnServerTest() throws Fault {
    invoke("textencoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: textEncoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:62;
   * 
   * @test_Strategy: test the text encoder throws EncoderException and it is
   * caught in @OnError
   */
  public void textEncoderThrowAndCatchOnClientTest() throws Fault {
    WSCClientEndpointWithTextEncoder endpoint = new WSCClientEndpointWithTextEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", new StringBean(ECHO));
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: textStreamEncoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:64;
   * 
   * @test_Strategy: test the text stream encoder throws EncoderException and it
   * is caught in @OnError
   */
  public void textStreamEncoderThrowAndCatchOnServerTest() throws Fault {
    invoke("textstreamencoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: textStreamEncoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:64;
   * 
   * @test_Strategy: test the text stream encoder throws EncoderException and it
   * is caught in @OnError
   */
  public void textStreamEncoderThrowAndCatchOnClientTest() throws Fault {
    WSCClientEndpointWithTextStreamEncoder endpoint = new WSCClientEndpointWithTextStreamEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", new StringBean(ECHO));
    logMsg("The EncoderException has been propagated to @OnError");
  }

  // -----------------------------------------------------------------------
  // returning encoders

  /*
   * @testName: returningBinaryEncoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:57;
   * 
   * @test_Strategy: test the binary encoder throws EncoderException and it is
   * caught in @OnError
   */
  public void returningBinaryEncoderThrowAndCatchOnServerTest() throws Fault {
    invoke("returningbinaryencoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: returningBinaryEncoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:57;
   * 
   * @test_Strategy: test the binary encoder throws EncoderException and it is
   * caught in @OnError
   */
  public void returningBinaryEncoderThrowAndCatchOnClientTest() throws Fault {
    WSCReturningClientEndpointWithBinaryEncoder endpoint = new WSCReturningClientEndpointWithBinaryEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", ECHO);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: returningBinaryStreamEncoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:59;
   * 
   * @test_Strategy: test the binary stream encoder throws EncoderException and
   * it is caught in @OnError
   */
  public void returningBinaryStreamEncoderThrowAndCatchOnServerTest()
      throws Fault {
    invoke("returningbinarystreamencoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: returningBinaryStreamEncoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:59;
   * 
   * @test_Strategy: test the binary stream encoder throws EncoderException and
   * it is caught in @OnError
   */
  public void returningBinaryStreamEncoderThrowAndCatchOnClientTest()
      throws Fault {
    WSCReturningClientEndpointWithBinaryStreamEncoder endpoint = new WSCReturningClientEndpointWithBinaryStreamEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", ECHO);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: returningTextEncoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:62;
   * 
   * @test_Strategy: test the text encoder throws EncoderException and it is
   * caught in @OnError
   */
  public void returningTextEncoderThrowAndCatchOnServerTest() throws Fault {
    invoke("returningtextencoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: returningTextEncoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:62;
   * 
   * @test_Strategy: test the text encoder throws EncoderException and it is
   * caught in @OnError
   */
  public void returningTextEncoderThrowAndCatchOnClientTest() throws Fault {
    WSCReturningClientEndpointWithTextEncoder endpoint = new WSCReturningClientEndpointWithTextEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", ECHO);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: returningTextStreamEncoderThrowAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:64;
   * 
   * @test_Strategy: test the text stream encoder throws EncoderException and it
   * is caught in @OnError
   */
  public void returningTextStreamEncoderThrowAndCatchOnServerTest()
      throws Fault {
    invoke("returningtextstreamencoder", ECHO, ThrowingTextDecoder.ERR_MSG);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  /*
   * @testName: returningTextStreamEncoderThrowAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:64;
   * 
   * @test_Strategy: test the text stream encoder throws EncoderException and it
   * is caught in @OnError
   */
  public void returningTextStreamEncoderThrowAndCatchOnClientTest()
      throws Fault {
    WSCReturningClientEndpointWithTextStreamEncoder endpoint = new WSCReturningClientEndpointWithTextStreamEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeOnClient("simpleecho", ECHO);
    logMsg("The EncoderException has been propagated to @OnError");
  }

  // ------------------------------------------------------------------------
  // IO Exceptions

  /*
   * @testName: binaryStreamDecoderThrowIOAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:42;
   * 
   * @test_Strategy: test the binary stream decoder throws IOException and it is
   * caught in @OnError
   */
  public void binaryStreamDecoderThrowIOAndCatchOnServerTest() throws Fault {
    invoke("iobinarystreamdecoder", ByteBuffer.wrap(ECHO.getBytes()),
        ThrowingTextDecoder.IO_ERR_MSG);
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: binaryStreamDecoderThrowIOAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:42;
   * 
   * @test_Strategy: test the binary stream decoder throws IOException and it is
   * caught in @OnError
   */
  public void binaryStreamDecoderThrowIOAndCatchOnClientTest() throws Fault {
    WSCClientEndpointWithIOBinaryStreamDecoder endpoint = new WSCClientEndpointWithIOBinaryStreamDecoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeIOOnClient("simplebin", ECHO);
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: textStreamDecoderThrowIOAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:48;
   * 
   * @test_Strategy: test the text stream decoder throws IOException and it is
   * caught in @OnError
   */
  public void textStreamDecoderThrowIOAndCatchOnServerTest() throws Fault {
    invoke("iotextstreamdecoder", ECHO, ThrowingTextDecoder.IO_ERR_MSG);
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: textStreamDecoderThrowIOAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:48;
   * 
   * @test_Strategy: test the text stream decoder throws IOException and it is
   * caught in @OnError
   */
  public void textStreamDecoderThrowIOAndCatchOnClientTest() throws Fault {
    WSCClientEndpointWithIOTextStreamDecoder endpoint = new WSCClientEndpointWithIOTextStreamDecoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeIOOnClient("simpleecho", ECHO);
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: binaryStreamEncoderThrowIOAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:60;
   * 
   * @test_Strategy: test the binary stream encoder throws IOException and it is
   * caught in @OnError
   */
  public void binaryStreamEncoderThrowIOAndCatchOnServerTest() throws Fault {
    invoke("iobinarystreamencoder", ECHO, ThrowingTextDecoder.IO_ERR_MSG);
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: binaryStreamEncoderThrowIOAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:60;
   * 
   * @test_Strategy: test the binary stream encoder throws IOException and it is
   * caught in @OnError
   */
  public void binaryStreamEncoderThrowIOAndCatchOnClientTest() throws Fault {
    WSCClientEndpointWithIOBinaryStreamEncoder endpoint = new WSCClientEndpointWithIOBinaryStreamEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeIOOnClient("simpleecho", new StringBean(ECHO));
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: textStreamEncoderThrowIOAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:65;
   * 
   * @test_Strategy: test the text stream encoder throws IOException and it is
   * caught in @OnError
   */
  public void textStreamEncoderThrowIOAndCatchOnServerTest() throws Fault {
    invoke("iotextstreamencoder", ECHO, ThrowingTextDecoder.IO_ERR_MSG);
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: textStreamEncoderThrowIOAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:65;
   * 
   * @test_Strategy: test the text stream encoder throws IOException and it is
   * caught in @OnError
   */
  public void textStreamEncoderThrowIOAndCatchOnClientTest() throws Fault {
    WSCClientEndpointWithIOTextStreamEncoder endpoint = new WSCClientEndpointWithIOTextStreamEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeIOOnClient("simpleecho", new StringBean(ECHO));
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: returningBinaryStreamEncoderThrowIOAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:60;
   * 
   * @test_Strategy: test the binary stream encoder throws IOException and it is
   * caught in @OnError
   */
  public void returningBinaryStreamEncoderThrowIOAndCatchOnServerTest()
      throws Fault {
    invoke("ioreturningbinarystreamencoder", ECHO,
        ThrowingTextDecoder.IO_ERR_MSG);
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: returningBinaryStreamEncoderThrowIOAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:60;
   * 
   * @test_Strategy: test the binary stream encoder throws IOException and it is
   * caught in @OnError
   */
  public void returningBinaryStreamEncoderThrowIOAndCatchOnClientTest()
      throws Fault {
    WSCReturningClientEndpointWithIOBinaryStreamEncoder endpoint = new WSCReturningClientEndpointWithIOBinaryStreamEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeIOOnClient("simpleecho", ECHO);
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: returningTextStreamEncoderThrowIOAndCatchOnServerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:65;
   * 
   * @test_Strategy: test the text stream encoder throws IOException and it is
   * caught in @OnError
   */
  public void returningTextStreamEncoderThrowIOAndCatchOnServerTest()
      throws Fault {
    invoke("ioreturningtextstreamencoder", ECHO,
        ThrowingTextDecoder.IO_ERR_MSG);
    logMsg("The IOException has been propagated to @OnError");
  }

  /*
   * @testName: returningTextStreamEncoderThrowIOAndCatchOnClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:65;
   * 
   * @test_Strategy: test the text stream encoder throws IOException and it is
   * caught in @OnError
   */
  public void returningTextStreamEncoderThrowIOAndCatchOnClientTest()
      throws Fault {
    WSCReturningClientEndpointWithIOTextStreamEncoder endpoint = new WSCReturningClientEndpointWithIOTextStreamEncoder();
    setAnnotatedClientEndpoint(endpoint);
    invokeIOOnClient("simpleecho", ECHO);
    logMsg("The IOException has been propagated to @OnError");
  }

  // ///////////////////////////////////////////////////////////////////////
  private void invokeOnClient(String endpoint, Object entity) throws Fault {
    setCountDownLatchCount(2);
    invoke(endpoint, entity, ThrowingTextDecoder.ERR_MSG,
        ThrowingTextDecoder.ERR_MSG);
  }

  private void invokeIOOnClient(String endpoint, Object entity) throws Fault {
    setCountDownLatchCount(2);
    invoke(endpoint, entity, ThrowingTextDecoder.IO_ERR_MSG,
        ThrowingTextDecoder.IO_ERR_MSG);
  }
}
