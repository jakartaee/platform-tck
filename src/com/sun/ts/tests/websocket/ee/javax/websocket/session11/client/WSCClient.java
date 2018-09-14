/*
 * Copyright (c) 2014, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.ee.javax.websocket.session11.client;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.Decoder;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.impl.StringPingMessage;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryDecoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryStreamDecoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextDecoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextStreamDecoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.session11.common.AlternativeInputStreamDecoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.session11.common.AlternativeReaderDecoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.session11.common.LinkedListHashSetTextDecoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.session11.common.StringListTextDecoder;
import com.sun.ts.tests.websocket.ee.javax.websocket.session11.common.TypeEnum;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
public class WSCClient extends WebSocketCommonClient {
  private static final long serialVersionUID = 0;

  protected static final String ECHO = "Echo message";

  protected static final String ECHO_PARTIAL = "second part send partially";

  protected static final String ANYTHING = "Anything";

  public WSCClient() {
    setContextRoot("wsc_ee_javax_websocket_session11_client_web");
    logExceptionOnInvocation(false);
  }

  public static void main(String[] args) {
    new WSCClient().run(args);
  }

  /* Run test */
  /*
   * @testName: linkedListHashSetTextHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void linkedListHashSetTextHandlerTest() throws Fault {
    setDecoder(TypeEnum.LINKEDLIST_HASHSET_TEXT,
        LinkedListHashSetTextDecoder.class);
    invoke("echo", ANYTHING, LinkedListHashSetMessageHandler.HANDLER_SAYS,
        WSCEchoServerEndpoint.SAYS, ECHO);
    logMsg(
        "addMessageHandler(LinkedList<HashSet<String>>.class, Whole<LinkedList<HashSet<String>>>) works as expected.");
  }

  /*
   * @testName: textStringBeanHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void textStringBeanHandlerTest() throws Fault {
    setDecoder(TypeEnum.STRINGBEAN, StringBeanTextDecoder.class);
    invoke("echo", ANYTHING, StringBeanMessageHandler.HANDLER_SAYS,
        WSCEchoServerEndpoint.SAYS, ECHO);
    logMsg(
        "addMessageHandler(StringBean.class, Whole<StringBean>) works as expected.");
  }

  /*
   * @testName: textStreamStringBeanHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void textStreamStringBeanHandlerTest() throws Fault {
    setDecoder(TypeEnum.STRINGBEAN, StringBeanTextStreamDecoder.class);
    invoke("echo", ANYTHING, StringBeanMessageHandler.HANDLER_SAYS,
        WSCEchoServerEndpoint.SAYS, ECHO);
    logMsg(
        "addMessageHandler(StringBean.class, Whole<StringBean>) works as expected.");
  }

  /*
   * @testName: linkedListTextHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void linkedListTextHandlerTest() throws Fault {
    setDecoder(TypeEnum.LIST_TEXT, StringListTextDecoder.class);
    invoke("echo", ANYTHING, StringListWholeMessageHandler.HANDLER_SAYS,
        WSCEchoServerEndpoint.SAYS, ECHO);
    logMsg(
        "addMessageHandler(StringList.class, Whole<StringList>) works as expected.");
  }

  /*
   * @testName: stringTextHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void stringTextHandlerTest() throws Fault {
    setDecoder(TypeEnum.STRING_WHOLE, null);
    invoke("echo", ANYTHING, StringTextMessageHandler.HANDLER_SAYS,
        WSCEchoServerEndpoint.SAYS, ECHO);
    logMsg("addMessageHandler(String.class, Whole<String>) works as expected.");
  }

  /*
   * @testName: readerMessageHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:38;
   * 
   * @test_Strategy: test the binary decoder throws decoderException and it is
   * caught in @OnError
   */
  public void readerMessageHandlerTest() throws Fault {
    setDecoderAndEntity(TypeEnum.READER, null, ECHO);
    invoke("echo", ANYTHING, ReaderMessageHandler.HANDLER_SAYS,
        WSCEchoServerEndpoint.SAYS, ECHO);
    logMsg("addMessageHandler(Reader.class, Whole<Reader>) works as expected.");
  }

  // -----------------------------------------------------------------------
  // Control
  // -----------------------------------------------------------------------

  /*
   * @testName: pongMessageHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void pongMessageHandlerTest() throws Fault {
    setDecoderAndEntity(TypeEnum.PONG, null, new StringPingMessage(ECHO));
    invoke("echo", ANYTHING, PongMessageHandler.HANDLER_SAYS, ECHO);
    logMsg(
        "addMessageHandler(PongMessage.class, Whole<PongMessage>) works as expected.");
  }

  // -----------------------------------------------------------------------
  // Binary
  // -----------------------------------------------------------------------

  /*
   * @testName: byteBufferMessageHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void byteBufferMessageHandlerTest() throws Fault {
    setDecoderAndEntity(TypeEnum.BYTEBUFFER_WHOLE, null,
        ByteBuffer.wrap(ECHO.getBytes()));
    invoke("binecho", ANYTHING, ByteBufferMessageHandler.HANDLER_SAYS,
        WSCBinaryEchoServerEndpoint.SAYS, ECHO);
    logMsg(
        "addMessageHandler(ByteBuffer.class, Whole<ByteBuffer>) works as expected.");
  }

  /*
   * @testName: byteArrayMessageHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void byteArrayMessageHandlerTest() throws Fault {
    setDecoderAndEntity(TypeEnum.BYTEARRAY_WHOLE, null, ECHO.getBytes());
    invoke("binecho", ANYTHING, ByteArrayMessageHandler.HANDLER_SAYS,
        WSCBinaryEchoServerEndpoint.SAYS, ECHO);
    logMsg("addMessageHandler(byte[].class, Whole<byte[]>) works as expected.");
  }

  /*
   * @testName: inputStreamMessageHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void inputStreamMessageHandlerTest() throws Fault {
    setDecoderAndEntity(TypeEnum.INPUTSTREAM, null,
        ByteBuffer.wrap(ECHO.getBytes()));
    invoke("binecho", ANYTHING, InputStreamMessageHandler.HANDLER_SAYS,
        WSCBinaryEchoServerEndpoint.SAYS, ECHO);
    logMsg(
        "addMessageHandler(InputStream.class, Whole<InputStream>) works as expected.");
  }

  /*
   * @testName: binaryStringBeanMessageHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void binaryStringBeanMessageHandlerTest() throws Fault {
    setDecoderAndEntity(TypeEnum.STRINGBEAN, StringBeanBinaryDecoder.class,
        ByteBuffer.wrap(ECHO.getBytes()));
    invoke("binecho", ANYTHING, StringBeanMessageHandler.HANDLER_SAYS,
        WSCBinaryEchoServerEndpoint.SAYS, ECHO);
    logMsg(
        "addMessageHandler(StringBean.class, Whole<StringBean>) works as expected.");
  }

  /*
   * @testName: binaryStreamStringBeanMessageHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void binaryStreamStringBeanMessageHandlerTest() throws Fault {
    setDecoderAndEntity(TypeEnum.STRINGBEAN,
        StringBeanBinaryStreamDecoder.class, ByteBuffer.wrap(ECHO.getBytes()));
    invoke("binecho", ANYTHING, StringBeanMessageHandler.HANDLER_SAYS,
        WSCBinaryEchoServerEndpoint.SAYS, ECHO);
    logMsg(
        "addMessageHandler(StringBean.class, Whole<StringBean>) works as expected.");
  }

  // -----------------------------------------------------------------------
  // Partial
  // -----------------------------------------------------------------------
  /*
   * @testName: stringPartialHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:213;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void stringPartialHandlerTest() throws Fault {
    setDecoderAndEntity(TypeEnum.STRING_PARTIAL, null, ECHO, ECHO_PARTIAL);
    invoke("echo", ANYTHING, StringPartialMessageHandler.HANDLER_SAYS,
        WSCEchoServerEndpoint.SAYS, ECHO, ECHO_PARTIAL);
    logMsg(
        "addMessageHandler(String.class, Partial<String>) works as expected.");
  }

  /*
   * @testName: byteBufferPartialMessageHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:213;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void byteBufferPartialMessageHandlerTest() throws Fault {
    setDecoderAndEntity(TypeEnum.BYTEBUFFER_PARTIAL, null,
        ByteBuffer.wrap(ECHO.getBytes()),
        ByteBuffer.wrap(ECHO_PARTIAL.getBytes()));
    invoke("binecho", ANYTHING, ByteBufferPartialMessageHandler.HANDLER_SAYS,
        WSCBinaryEchoServerEndpoint.SAYS, ECHO, ECHO_PARTIAL);
    logMsg(
        "addMessageHandler(ByteBuffer.class, Partial<ByteBuffer>) works as expected.");
  }

  /*
   * @testName: byteArrayPartialMessageHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:213;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void byteArrayPartialMessageHandlerTest() throws Fault {
    setDecoderAndEntity(TypeEnum.BYTEARRAY_PARTIAL, null,
        ByteBuffer.wrap(ECHO.getBytes()),
        ByteBuffer.wrap(ECHO_PARTIAL.getBytes()));
    invoke("binecho", ANYTHING, ByteArrayPartialMessageHandler.HANDLER_SAYS,
        WSCBinaryEchoServerEndpoint.SAYS, ECHO, ECHO_PARTIAL);
    logMsg(
        "addMessageHandler(byte[].class, Partial<byte[]>) works as expected.");
  }

  // -----------------------------------------------------------------------
  // Annotated
  // -----------------------------------------------------------------------

  /*
   * @testName: annotatedTextReaderClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void annotatedTextReaderClientTest() throws Fault {
    setAnnotatedClientEndpoint(new AnnotatedTextClient());
    invoke("echo", ECHO, ReaderMessageHandler.HANDLER_SAYS,
        AlternativeReaderDecoder.DECODER_SAYS, WSCEchoServerEndpoint.SAYS,
        ECHO);
    logMsg("addMessageHandler(Reader.class, Whole<Reader>) works as expected.");
  }

  /*
   * @testName: annotatedBinaryInputStreamClientTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void annotatedBinaryInputStreamClientTest() throws Fault {
    setAnnotatedClientEndpoint(new AnnotatedBinaryClient());
    invoke("binecho", ByteBuffer.wrap(ECHO.getBytes()),
        InputStreamMessageHandler.HANDLER_SAYS,
        AlternativeInputStreamDecoder.DECODER_SAYS,
        WSCBinaryEchoServerEndpoint.SAYS, ECHO);
    logMsg(
        "addMessageHandler(InputStream.class, Whole<InputStream>) works as expected.");
  }

  // -----------------------------------------------------------------------
  // Exceptions
  // -----------------------------------------------------------------------

  /*
   * @testName: twiceAddMessageHandlerReaderThrowsExceptionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Adding more than one of any one type will result in a
   * runtime exception.
   */
  public void twiceAddMessageHandlerReaderThrowsExceptionTest() throws Fault {
    AnnotatedThrowingClient client = new AnnotatedThrowingClient(
        TypeEnum.READER);
    setAnnotatedClientEndpoint(client);
    invoke("echo", ECHO, ReaderMessageHandler.HANDLER_SAYS,
        WSCEchoServerEndpoint.SAYS, ECHO);
    assertNotNull(client.getThrown(),
        "there is no exception thrown when adding MessageHandler twice");
    logMsg(
        "addMessageHandler(Reader.class, Whole<Reader>) throws RuntimeException as expected when called twice:",
        client.getThrown().getMessage());
  }

  /*
   * @testName: twiceAddMessageHandlerInputStreamThrowsExceptionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Adding more than one of any one type will result in a
   * runtime exception.
   */
  public void twiceAddMessageHandlerInputStreamThrowsExceptionTest()
      throws Fault {
    AnnotatedThrowingClient client = new AnnotatedThrowingClient(
        TypeEnum.INPUTSTREAM);
    setAnnotatedClientEndpoint(client);
    invoke("binecho", ByteBuffer.wrap(ECHO.getBytes()),
        InputStreamMessageHandler.HANDLER_SAYS,
        WSCBinaryEchoServerEndpoint.SAYS, ECHO);
    assertNotNull(client.getThrown(),
        "there is no exception thrown when adding MessageHandler twice");
    logMsg(
        "addMessageHandler(InputStream.class, Whole<InpuStream>) throws RuntimeException as expected when called twice:",
        client.getThrown().getMessage());
  }

  /*
   * @testName: twiceAddMessageHandlerPongMessageThrowsExceptionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Adding more than one of any one type will result in a
   * runtime exception.
   */
  public void twiceAddMessageHandlerPongMessageThrowsExceptionTest()
      throws Fault {
    AnnotatedThrowingClient client = new AnnotatedThrowingClient(TypeEnum.PONG);
    setAnnotatedClientEndpoint(client);
    invoke("echo", new StringPingMessage(ECHO), PongMessageHandler.HANDLER_SAYS,
        ECHO);
    assertNotNull(client.getThrown(),
        "there is no exception thrown when adding MessageHandler twice");
    logMsg(
        "addMessageHandler(PongMessage.class, Whole<PongMessage>) throws RuntimeException as expected when called twice:",
        client.getThrown().getMessage());
  }

  /*
   * @testName: twiceAddMessageHandlerPartialStringThrowsExceptionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:213;
   * 
   * @test_Strategy: Adding more than one of any one type will result in a
   * runtime exception.
   */
  public void twiceAddMessageHandlerPartialStringThrowsExceptionTest()
      throws Fault {
    AnnotatedThrowingClient client = new AnnotatedThrowingClient(
        TypeEnum.STRING_PARTIAL);
    setAnnotatedClientEndpoint(client);
    setEntity(ECHO, ECHO_PARTIAL);
    setProperty(Property.SEARCH_STRING,
        StringPartialMessageHandler.HANDLER_SAYS);
    setProperty(Property.SEARCH_STRING, WSCEchoServerEndpoint.SAYS);
    setProperty(Property.SEARCH_STRING, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO_PARTIAL);
    setProperty(Property.REQUEST, buildRequest("echo"));
    invoke();
    assertNotNull(client.getThrown(),
        "there is no exception thrown when adding MessageHandler twice");
    logMsg(
        "addMessageHandler(String.class, Partial<String>) throws RuntimeException as expected when called twice:",
        client.getThrown().getMessage());
  }

  // ///////////////////////////////////////////////////////////////////////
  protected void setDecoder(TypeEnum type,
      Class<? extends Decoder> decoderClass) {
    setDecoderAndEntity(type, decoderClass, ECHO);
  }

  protected void setDecoderAndEntity(TypeEnum type,
      Class<? extends Decoder> decoderClass, Object... entity) {
    setEntity(entity);
    MixedProgramaticEndpoint endpoint = new MixedProgramaticEndpoint(type,
        this.entity);
    setClientEndpointInstance(endpoint);
    List<Class<? extends Decoder>> list = new LinkedList<Class<? extends Decoder>>();
    if (decoderClass != null)
      list.add(decoderClass);
    ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
        .decoders(list).build();
    setClientEndpointConfig(config);
  }
}
