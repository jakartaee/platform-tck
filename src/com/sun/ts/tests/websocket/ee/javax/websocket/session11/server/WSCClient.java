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

package com.sun.ts.tests.websocket.ee.javax.websocket.session11.server;

import java.nio.ByteBuffer;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.impl.StringPongMessage;
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
    setContextRoot("wsc_ee_javax_websocket_session11_server_web");
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
    invoke(TypeEnum.LINKEDLIST_HASHSET_TEXT.name().toLowerCase(), ECHO,
        LinkedListHashSetMessageHandler.HANDLER_SAYS, ECHO);
    logMsg(
        "addMessageHandler(LinkedList<HashSet<String>>.class, Whole<LinkedList<HashSet<String>>>) works as expected.");
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
    invoke(TypeEnum.LIST_TEXT.name().toLowerCase(), ECHO,
        StringListWholeMessageHandler.HANDLER_SAYS, ECHO);
    logMsg(
        "addMessageHandler(StringList.class, Whole<StringList>) works as expected.");
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
    invoke(TypeEnum.STRINGBEAN.name().toLowerCase(), ECHO,
        StringBeanMessageHandler.HANDLER_SAYS, ECHO);
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
    invoke(TypeEnum.STRINGBEANSTREAM.name().toLowerCase(), ECHO,
        StringBeanMessageHandler.HANDLER_SAYS, ECHO);
    logMsg(
        "addMessageHandler(StringBean.class, Whole<StringBean>) works as expected.");
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
    invoke(TypeEnum.STRING_WHOLE.name().toLowerCase(), ECHO,
        StringWholeMessageHandler.HANDLER_SAYS, ECHO);
    logMsg("addMessageHandler(String.class, Whole<String>) works as expected.");
  }

  /*
   * @testName: readerMessageHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void readerMessageHandlerTest() throws Fault {
    invoke(TypeEnum.READER.name().toLowerCase(), ECHO,
        ReaderMessageHandler.HANDLER_SAYS, ECHO);
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
    invoke(TypeEnum.PONG.name().toLowerCase(), new StringPongMessage(ECHO),
        PongMessageHandler.HANDLER_SAYS, ECHO);
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
    invoke(TypeEnum.BYTEBUFFER_WHOLE.name().toLowerCase(),
        ByteBuffer.wrap(ECHO.getBytes()), ByteBufferMessageHandler.HANDLER_SAYS,
        ECHO);
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
    invoke(TypeEnum.BYTEARRAY_WHOLE.name().toLowerCase(),
        ByteBuffer.wrap(ECHO.getBytes()), ByteArrayMessageHandler.HANDLER_SAYS,
        ECHO);
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
    invoke(TypeEnum.INPUTSTREAM.name().toLowerCase(),
        ByteBuffer.wrap(ECHO.getBytes()),
        InputStreamMessageHandler.HANDLER_SAYS, ECHO);
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
    invoke(TypeEnum.STRINGBEANBINARY.name().toLowerCase(),
        ByteBuffer.wrap(ECHO.getBytes()), StringBeanMessageHandler.HANDLER_SAYS,
        ECHO);
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
    invoke(TypeEnum.STRINGBEANBINARYSTREAM.name().toLowerCase(),
        ByteBuffer.wrap(ECHO.getBytes()), StringBeanMessageHandler.HANDLER_SAYS,
        ECHO);
    logMsg(
        "addMessageHandler(StringBean.class, Whole<StringBean>) works as expected.");
  }

  // -----------------------------------------------------------------------
  // Partial
  // -----------------------------------------------------------------------

  /*
   * @testName: stringPartialHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:213
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void stringPartialHandlerTest() throws Fault {
    setEntity(ECHO, ECHO_PARTIAL);

    setProperty(Property.SEARCH_STRING,
        StringPartialMessageHandler.HANDLER_SAYS);
    setProperty(Property.SEARCH_STRING, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO_PARTIAL);
    setProperty(Property.REQUEST,
        buildRequest(TypeEnum.STRING_PARTIAL.name().toLowerCase()));

    invoke();
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
    setEntity(ByteBuffer.wrap(ECHO.getBytes()),
        ByteBuffer.wrap(ECHO_PARTIAL.getBytes()));

    setProperty(Property.SEARCH_STRING,
        ByteBufferPartialMessageHandler.HANDLER_SAYS);
    setProperty(Property.SEARCH_STRING, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO_PARTIAL);
    setProperty(Property.REQUEST,
        buildRequest(TypeEnum.BYTEBUFFER_PARTIAL.name().toLowerCase()));

    invoke();
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
    setEntity(ByteBuffer.wrap(ECHO.getBytes()),
        ByteBuffer.wrap(ECHO_PARTIAL.getBytes()));

    setProperty(Property.SEARCH_STRING,
        ByteArrayPartialMessageHandler.HANDLER_SAYS);
    setProperty(Property.SEARCH_STRING, ECHO);
    setProperty(Property.SEARCH_STRING, ECHO_PARTIAL);
    setProperty(Property.REQUEST,
        buildRequest(TypeEnum.BYTEARRAY_PARTIAL.name().toLowerCase()));

    invoke();
    logMsg(
        "addMessageHandler(byte[].class, Partial<byte[]>) works as expected.");
  }

  // -----------------------------------------------------------------------
  // Annotated
  // -----------------------------------------------------------------------

  /*
   * @testName: binaryAnnotatedStringBeanMessageHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void binaryAnnotatedStringBeanMessageHandlerTest() throws Fault {
    invoke("annotatedbinary", ByteBuffer.wrap(ECHO.getBytes()),
        StringBeanMessageHandler.HANDLER_SAYS, ECHO);
    logMsg(
        "addMessageHandler(StringBean.class, Whole<StringBean>) works as expected.");
  }

  /*
   * @testName: textAnnotatedStringBeanMessageHandlerTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Register to handle to incoming messages in this
   * conversation.
   */
  public void textAnnotatedStringBeanMessageHandlerTest() throws Fault {
    invoke("annotatedtext", ECHO, StringBeanMessageHandler.HANDLER_SAYS, ECHO);
    logMsg(
        "addMessageHandler(StringBean.class, Whole<StringBean>) works as expected.");
  }

  // -----------------------------------------------------------------------
  // Exception
  // -----------------------------------------------------------------------

  /*
   * @testName: addMessageHandlerPongMessageThrowsExceptionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Adding more than one of any one type will result in a
   * runtime exception.
   */
  public void addMessageHandlerPongMessageThrowsExceptionTest() throws Fault {
    // do not throw
    setEntity(new StringPongMessage(ECHO));
    setProperty(Property.REQUEST, buildRequest("exception"));
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH,
        WSCAnnotatedMixedServerEndpoint.EXCEPTION);
    setProperty(Property.SEARCH_STRING, PongMessageHandler.HANDLER_SAYS);
    setProperty(Property.SEARCH_STRING, ECHO);
    invoke();
    // throw
    setEntity(new StringPongMessage(TypeEnum.PONG.name()));
    setProperty(Property.REQUEST, buildRequest("exception"));
    setProperty(Property.SEARCH_STRING,
        WSCAnnotatedMixedServerEndpoint.EXCEPTION);
    invoke(false);
    logMsg(
        "addMessageHandler(PongMessage.class, Partial<PongMessage>) throws RuntimeException as expected when called twice:",
        getResponseAsString().split(": ")[1]);
  }

  /*
   * @testName: addMessageHandlerStringPartialMessageThrowsExceptionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Adding more than one of any one type will result in a
   * runtime exception.
   */
  public void addMessageHandlerStringPartialMessageThrowsExceptionTest()
      throws Fault {
    // do not throw
    setEntity(ECHO, ECHO_PARTIAL);
    setProperty(Property.REQUEST, buildRequest("exception"));
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH,
        WSCAnnotatedMixedServerEndpoint.EXCEPTION);
    setProperty(Property.SEARCH_STRING,
        StringPartialMessageHandler.HANDLER_SAYS);
    setProperty(Property.SEARCH_STRING, ECHO);
    invoke();
    // throw
    setEntity(TypeEnum.STRING_PARTIAL.name(), TypeEnum.STRING_PARTIAL.name());
    setProperty(Property.REQUEST, buildRequest("exception"));
    setProperty(Property.SEARCH_STRING,
        WSCAnnotatedMixedServerEndpoint.EXCEPTION);
    invoke(false);
    logMsg(
        "addMessageHandler(String.class, Partial<String>) throws RuntimeException as expected when called twice:",
        getResponseAsString().split(": ")[1]);
  }

  /*
   * @testName: addMessageHandlerInputStreamThrowsExceptionTest
   * 
   * @assertion_ids: WebSocket:JAVADOC:212;
   * 
   * @test_Strategy: Adding more than one of any one type will result in a
   * runtime exception.
   */
  public void addMessageHandlerInputStreamThrowsExceptionTest() throws Fault {
    // do not throw
    setEntity(ByteBuffer.wrap(ECHO.getBytes()));
    setProperty(Property.REQUEST, buildRequest("exception"));
    setProperty(Property.UNEXPECTED_RESPONSE_MATCH,
        WSCAnnotatedMixedServerEndpoint.EXCEPTION);
    setProperty(Property.SEARCH_STRING, InputStreamMessageHandler.HANDLER_SAYS);
    setProperty(Property.SEARCH_STRING, ECHO);
    invoke();
    // throw
    setEntity(ByteBuffer.wrap(TypeEnum.INPUTSTREAM.name().getBytes()));
    setProperty(Property.REQUEST, buildRequest("exception"));
    setProperty(Property.SEARCH_STRING,
        WSCAnnotatedMixedServerEndpoint.EXCEPTION);
    invoke(false);
    logMsg(
        "addMessageHandler(InputStream.class, Whole<InputStream>) throws RuntimeException as expected when called twice:",
        getResponseAsString().split(": ")[1]);
  }

  // ///////////////////////////////////////////////////////////////////////
}
