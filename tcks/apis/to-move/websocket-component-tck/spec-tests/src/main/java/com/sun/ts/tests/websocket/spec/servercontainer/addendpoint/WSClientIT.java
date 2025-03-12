/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates and others.
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

/*
 * $Id$
 */
package com.sun.ts.tests.websocket.spec.servercontainer.addendpoint;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.System.Logger;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.common.util.MessageValidator;
import com.sun.ts.tests.websocket.common.util.SessionUtil;

import jakarta.websocket.ClientEndpointConfig;
import jakarta.websocket.CloseReason;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

@ExtendWith(ArquillianExtension.class)
public class WSClientIT extends WebSocketCommonClient {

	private static final Logger logger = System.getLogger(WSClientIT.class.getName());

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "websocket_servercontainer_addendpoint_web.war");
		final JavaArchive initializerJar = ShrinkWrap.create(JavaArchive.class, "initializer.jar")
				.addClass(TCKWebSocketContainerInitializer.class);
		InputStream inStream = WSClientIT.class.getClassLoader().getResourceAsStream(
				"com/sun/ts/tests/websocket/spec/servercontainer/addendpoint/jakarta.servlet.ServletContainerInitializer");
		ByteArrayAsset servletContainerInitializer = new ByteArrayAsset(inStream);
		initializerJar.addAsManifestResource(servletContainerInitializer,
				"services/jakarta.servlet.ServletContainerInitializer");
		archive.addAsLibrary(initializerJar);
		archive.addPackages(true, "com.sun.ts.tests.websocket.spec.servercontainer.addendpoint");
		archive.addClasses(IOUtil.class);
		return archive;
	};

	private static final String CONTEXT_ROOT = "/websocket_servercontainer_addendpoint_web";

	private static final String SENT_STRING_MESSAGE = "Hello World in String";

	private static ByteBuffer SENT_BYTE_MESSAGE = ByteBuffer
			.allocate("Hello World in ByteBuffer".getBytes().length + 1);

	private static StringBuffer receivedMessageString = new StringBuffer();

	static volatile CountDownLatch messageLatch;

	public WSClientIT() throws Exception {
		setContextRoot("websocket_servercontainer_addendpoint_web");
	}

	/*
	 * @class.setup_props: webServerHost; webServerPort; ws_wait; ts_home;
	 */
	/* Run test */
	/*
	 * @testName: addMessageHandlerBasicStringTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:121; WebSocket:JAVADOC:122; WebSocket:JAVADOC:184;
	 * WebSocket:SPEC:WSC-2.2.2-1; WebSocket:SPEC:WSC-2.2.3-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void addMessageHandlerBasicStringTest() throws Exception {
		boolean passed = true;
		String search = "Expected IllegalStateException thrown by Second TextMessageHandler|"
				+ "First TextMessageHander received|" + "TCKTestServerString opened|"
				+ "First TextMessageHander received|" + "TCKTestServerString received String: Hello World in String|"
				+ "First TextMessageHander received|" + "TCKTestServerString responds";

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(3);
			final Session session = clientContainer.connectToServer(TCKBasicStringEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServerString"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			passed = MessageValidator.checkSearchStrings(search, receivedMessageString.toString());
			session.close();

		} catch (Exception e) {
			passed = false;
			e.printStackTrace();
		}

		logger.log(Logger.Level.INFO,receivedMessageString.toString());
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: addMessageHandlerBasicByteBufferTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:105;
	 * WebSocket:JAVADOC:121; WebSocket:JAVADOC:122; WebSocket:JAVADOC:184;
	 * WebSocket:SPEC:WSC-2.1.3-1; WebSocket:SPEC:WSC-2.1.3-2;
	 * WebSocket:SPEC:WSC-2.2.2-1; WebSocket:SPEC:WSC-2.2.3-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void addMessageHandlerBasicByteBufferTest() throws Exception {
		boolean passed = true;
		String search = "Expected IllegalStateException thrown by Second ByteBuffer MessageHandler|"
				+ "First Basic ByteBuffer MessageHander received|" + "TCKTestServerByte opened|"
				+ "First Basic ByteBuffer MessageHander received|"
				+ "TCKTestServerByte received ByteBuffer: Hello World in ByteBuffer|"
				+ "TCKTestServerByte responds: Message in bytes";

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(1);
			final Session session = clientContainer.connectToServer(TCKBasicByteEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServerByte"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			messageLatch = new CountDownLatch(1);
			SENT_BYTE_MESSAGE.put("Hello World in ByteBuffer".getBytes());
			SENT_BYTE_MESSAGE.flip();
			try {
				session.getBasicRemote().sendBinary(SENT_BYTE_MESSAGE);
			} catch (IOException e) {
				e.printStackTrace();
			}
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			passed = MessageValidator.checkSearchStrings(search, receivedMessageString.toString());
			session.close();

		} catch (Exception e) {
			passed = false;
			e.printStackTrace();
		}

		logger.log(Logger.Level.INFO,receivedMessageString.toString());
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: addMessageHandlersTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:105;
	 * WebSocket:JAVADOC:112; WebSocket:JAVADOC:121; WebSocket:JAVADOC:122;
	 * WebSocket:JAVADOC:134; WebSocket:JAVADOC:147; WebSocket:JAVADOC:184;
	 * WebSocket:SPEC:WSC-2.1.3-1; WebSocket:SPEC:WSC-2.1.3-2;
	 * WebSocket:SPEC:WSC-2.2.2-1; WebSocket:SPEC:WSC-2.2.3-1;
	 * WebSocket:SPEC:WSC-4.4-1; WebSocket:SPEC:WSC-4.4-2;
	 * 
	 * @test_Strategy:
	 */
	@Test
	public void addMessageHandlersTest() throws Exception {
		boolean passed = true;
		String message_sent_bytebuffer = "BasicByteBufferMessageHandler added";
		String message_sent_string = "BasicStringMessageHandler added";

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			final Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);
			Set<MessageHandler> msgHanders = session.getMessageHandlers();
			receivedMessageString.append("Start with MessageHandler=" + msgHanders.size());
			messageLatch = new CountDownLatch(2);
			session.getBasicRemote().sendText(message_sent_string);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			ByteBuffer data = ByteBuffer.allocate(message_sent_bytebuffer.getBytes().length);
			data.put(message_sent_bytebuffer.getBytes());
			data.flip();
			messageLatch = new CountDownLatch(3);
			session.getBasicRemote().sendBinary(data);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			for (MessageHandler msgHander : msgHanders) {
				session.removeMessageHandler(msgHander);
				receivedMessageString.append("MessageHandler=" + session.getMessageHandlers().size());
			}

			passed = MessageValidator.checkSearchStrings("TCKBasicEndpoint OnOpen|" + "TCKTestServer opened|"
					+ "Start with MessageHandler=2|" + "TCKTestServer received String:|" + message_sent_string + "|"
					+ "TCKTestServer responds, please close your session|" + "Basic ByteBuffer MessageHander received|"
					+ "TCKTestServer received ByteBuffer:|" + "Basic ByteBuffer MessageHander received|"
					+ message_sent_bytebuffer + "|" + "Basic ByteBuffer MessageHander received|"
					+ "TCKTestServer responds: Message in bytes|" + "MessageHandler=1|" + "MessageHandler=0",
					receivedMessageString.toString());
			session.close();

		} catch (Exception e) {
			passed = false;
			e.printStackTrace();
		}

		logger.log(Logger.Level.INFO,receivedMessageString.toString());
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: addMessageHandlersTest1
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:103;
	 * WebSocket:JAVADOC:105; WebSocket:JAVADOC:112; WebSocket:JAVADOC:121;
	 * WebSocket:JAVADOC:122; WebSocket:JAVADOC:134; WebSocket:JAVADOC:147;
	 * WebSocket:JAVADOC:184; WebSocket:SPEC:WSC-2.1.3-1;
	 * WebSocket:SPEC:WSC-2.1.3-2; WebSocket:SPEC:WSC-2.2.2-1;
	 * WebSocket:SPEC:WSC-2.2.3-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void addMessageHandlersTest1() throws Exception {
		boolean passed = true;
		final String message_sent_bytebuffer = "BasicByteBufferMessageHandler added";
		final String message_sent_reader = "BasicTextReaderMessageHandler added";
		final String message_reader_msghandler = "BasicReaderMessageHander received=";

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			// No message handlers to receive messages server sends in @OnOpen
			final Session session = clientContainer.connectToServer(TCKBasicEndpoint1.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));

			Set<MessageHandler> msgHanders_1 = session.getMessageHandlers();
			receivedMessageString.append("Start with MessageHandler=" + msgHanders_1.size());
			session.addMessageHandler(new MessageHandler.Whole<Reader>() {

				@Override
				public void onMessage(Reader r) {
					char[] buffer = new char[128];
					try {
						int i = r.read(buffer);
						receivedMessageString.append("========" + message_reader_msghandler + new String(buffer, 0, i));
						messageLatch.countDown();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});

			messageLatch = new CountDownLatch(4);
			Writer writer = session.getBasicRemote().getSendWriter();
			writer.append(message_sent_reader);
			writer.close();
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			Set<MessageHandler> msgHanders_2 = session.getMessageHandlers();
			receivedMessageString.append("Now we Have MessageHandler=" + msgHanders_2.size());

			session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {

				@Override
				public void onMessage(ByteBuffer data) {
					byte[] data1 = new byte[data.remaining()];
					data.get(data1);
					receivedMessageString.append(new String(data1));
					messageLatch.countDown();
				}
			});

			ByteBuffer data = ByteBuffer.allocate((message_sent_bytebuffer).getBytes().length);
			data.put((message_sent_bytebuffer).getBytes());
			data.flip();
			messageLatch = new CountDownLatch(3);
			session.getBasicRemote().sendBinary(data);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);
			Set<MessageHandler> msgHanders_3 = session.getMessageHandlers();
			receivedMessageString.append("Now it is MessageHandler=" + msgHanders_3.size());

			for (MessageHandler msgHander : msgHanders_3) {
				session.removeMessageHandler(msgHander);
				receivedMessageString.append("MessageHandler=" + session.getMessageHandlers().size());
			}

			passed = MessageValidator.checkSearchStrings(
					"Start with MessageHandler=0|" + message_reader_msghandler + "|" + "TCKTestServer received String:|"
							+ message_sent_reader + "|" + "TCKTestServer responds, please close your session|"
							+ "Now we Have MessageHandler=1|" + "TCKTestServer received ByteBuffer:|"
							+ message_sent_bytebuffer + "|" + "TCKTestServer responds: Message in bytes|"
							+ "Now it is MessageHandler=2|" + "MessageHandler=1|" + "MessageHandler=0",
					receivedMessageString.toString());

			session.close();

		} catch (Exception e) {
			passed = false;
			e.printStackTrace();
		}

		logger.log(Logger.Level.INFO,receivedMessageString.toString());

		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: closeTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:123; WebSocket:JAVADOC:145; WebSocket:JAVADOC:184;
	 * WebSocket:SPEC:WSC-4.5-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void closeTest() throws Exception {
		boolean passed = true;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			session.close();
			SessionUtil.waitUntilClosed(session, _ws_wait, TimeUnit.SECONDS);

			passed = MessageValidator
					.checkSearchStrings(
							"TCKBasicEndpoint OnOpen|" + "TCKTestServer opened|" + "session from Server is open=TRUE|"
									+ "TCKBasicEndpoint OnClose CloseCode=NORMAL_CLOSURE",
							receivedMessageString.toString());

			if (session.isOpen()) {
				passed = false;
				receivedMessageString.append("=================Session stays open after calling close()");
			}

		} catch (Exception e) {
			e.printStackTrace();
			passed = false;
		}

		logger.log(Logger.Level.INFO,receivedMessageString.toString());
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: close1Test
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:123; WebSocket:JAVADOC:145; WebSocket:JAVADOC:184;
	 * WebSocket:SPEC:WSC-2.2.2-1; WebSocket:SPEC:WSC-2.2.3-1;
	 * WebSocket:SPEC:WSC-2.1.5-1; WebSocket:SPEC:WSC-4.5-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void close1Test() throws Exception {
		String testName = "close1Test";
		boolean passed = true;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);
			session.getBasicRemote().sendText("testName=" + testName);
			SessionUtil.waitUntilClosed(session, _ws_wait, TimeUnit.SECONDS);

			passed = MessageValidator.checkSearchStrings("TCKTestServer opened|" + "session from Server is open=TRUE|"
					+ "TCKBasicEndpoint OnClose CloseCode=NORMAL_CLOSURE", receivedMessageString.toString());

			if (session.isOpen()) {
				passed = false;
				receivedMessageString
						.append("=================Session stays open after calling close() from server side");
				session.close();
			}
		} catch (Exception e) {
			passed = false;
			receivedMessageString.append(e.getMessage());
		}
		logger.log(Logger.Level.INFO,receivedMessageString.toString());
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: close2Test
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:125; WebSocket:JAVADOC:145; WebSocket:JAVADOC:19;
	 * WebSocket:JAVADOC:20; WebSocket:JAVADOC:184; WebSocket:SPEC:WSC-2.2.2-1;
	 * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-2.1.5-1;
	 * WebSocket:SPEC:WSC-4.5-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void close2Test() throws Exception {
		String testName = "close2Test";
		boolean passed = true;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);
			session.getBasicRemote().sendText("testName=" + testName);
			SessionUtil.waitUntilClosed(session, _ws_wait, TimeUnit.SECONDS);

			passed = MessageValidator.checkSearchStrings(
					"TCKBasicEndpoint OnOpen|" + "TCKTestServer opened|" + "session from Server is open=TRUE|"
							+ "TCKBasicEndpoint OnClose CloseCode=TOO_BIG|"
							+ "TCKBasicEndpoint OnClose ReasonPhrase=TCKCloseNowWithReason",
					receivedMessageString.toString());

			if (session.isOpen()) {
				passed = false;
				receivedMessageString
						.append("=================Session stays open after calling close() from server side");
				session.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		logger.log(Logger.Level.INFO,receivedMessageString.toString());
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: close3Test
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:123; WebSocket:JAVADOC:145; WebSocket:JAVADOC:184;
	 * WebSocket:SPEC:WSC-4.5-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void close3Test() throws Exception {
		boolean passed = true;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/WSCloseTestServer"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			session.close();
			SessionUtil.waitUntilClosed(session, _ws_wait, TimeUnit.SECONDS);

			passed = MessageValidator.checkSearchStrings("TCKBasicEndpoint OnOpen|" + "WSCloseTestServer opened|"
					+ "session from WSCloseTestServer is open=TRUE|"
					+ "TCKBasicEndpoint OnClose CloseCode=NORMAL_CLOSURE", receivedMessageString.toString());

			if (session.isOpen()) {
				passed = false;
				receivedMessageString.append("=================Session stays open after calling close()");
			}

		} catch (Exception e) {
			e.printStackTrace();
			passed = false;
		}

		logger.log(Logger.Level.INFO,receivedMessageString.toString());
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: close4Test
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:123; WebSocket:JAVADOC:145; WebSocket:JAVADOC:184;
	 * WebSocket:SPEC:WSC-2.2.2-1; WebSocket:SPEC:WSC-2.2.3-1;
	 * WebSocket:SPEC:WSC-2.1.5-1; WebSocket:SPEC:WSC-4.5-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void close4Test() throws Exception {
		String testName = "close1Test";
		boolean passed = true;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/WSCloseTestServer"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			messageLatch = new CountDownLatch(1);
			session.getBasicRemote().sendText("testName=" + testName);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			SessionUtil.waitUntilClosed(session, _ws_wait, TimeUnit.SECONDS);

			passed = MessageValidator.checkSearchStrings("TCKBasicEndpoint OnOpen|" + "WSCloseTestServer opened|"
					+ "session from WSCloseTestServer is open=TRUE|"
					+ "TCKBasicEndpoint OnClose CloseCode=NORMAL_CLOSURE", receivedMessageString.toString());

			if (session.isOpen()) {
				passed = false;
				receivedMessageString
						.append("=================Session stays open after calling close() from server side");
				session.close();
			}
		} catch (Exception e) {
			passed = false;
			receivedMessageString.append(e.getMessage());
		}
		logger.log(Logger.Level.INFO,receivedMessageString.toString());
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: close5Test
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:125; WebSocket:JAVADOC:145; WebSocket:JAVADOC:19;
	 * WebSocket:JAVADOC:20; WebSocket:JAVADOC:184; WebSocket:SPEC:WSC-2.2.2-1;
	 * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-2.1.5-1;
	 * WebSocket:SPEC:WSC-4.5-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void close5Test() throws Exception {
		String testName = "close2Test";
		boolean passed = true;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/WSCloseTestServer"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			messageLatch = new CountDownLatch(1);
			session.getBasicRemote().sendText("testName=" + testName);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			SessionUtil.waitUntilClosed(session, _ws_wait, TimeUnit.SECONDS);

			passed = MessageValidator.checkSearchStrings("TCKBasicEndpoint OnOpen|" + "WSCloseTestServer opened|"
					+ "session from WSCloseTestServer is open=TRUE|" + "TCKBasicEndpoint OnClose CloseCode=TOO_BIG|"
					+ "TCKBasicEndpoint OnClose ReasonPhrase=TCKCloseNowWithReason", receivedMessageString.toString());

			if (session.isOpen()) {
				passed = false;
				receivedMessageString
						.append("=================Session stays open after calling close() from server side");
				session.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		logger.log(Logger.Level.INFO,receivedMessageString.toString());
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: close6Test
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:123; WebSocket:JAVADOC:145; WebSocket:JAVADOC:184;
	 * WebSocket:SPEC:WSC-4.5-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void close6Test() throws Exception {
		boolean passed = true;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/WSCloseTestServer1"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			session.close();
			SessionUtil.waitUntilClosed(session, _ws_wait, TimeUnit.SECONDS);

			passed = MessageValidator.checkSearchStrings("TCKBasicEndpoint OnOpen|" + "WSCloseTestServer1 opened|"
					+ "session from WSCloseTestServer1 is open=TRUE|"
					+ "TCKBasicEndpoint OnClose CloseCode=NORMAL_CLOSURE", receivedMessageString.toString());

			if (session.isOpen()) {
				passed = false;
				receivedMessageString.append("=================Session stays open after calling close()");
			}

		} catch (Exception e) {
			e.printStackTrace();
			passed = false;
		}

		logger.log(Logger.Level.INFO,receivedMessageString.toString());
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: close7Test
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:123; WebSocket:JAVADOC:145; WebSocket:JAVADOC:184;
	 * WebSocket:SPEC:WSC-2.2.2-1; WebSocket:SPEC:WSC-2.2.3-1;
	 * WebSocket:SPEC:WSC-2.1.5-1; WebSocket:SPEC:WSC-4.5-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void close7Test() throws Exception {
		String testName = "close1Test";
		boolean passed = true;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/WSCloseTestServer1"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			messageLatch = new CountDownLatch(1);
			session.getBasicRemote().sendText("testName=" + testName);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			SessionUtil.waitUntilClosed(session, _ws_wait, TimeUnit.SECONDS);

			passed = MessageValidator.checkSearchStrings("TCKBasicEndpoint OnOpen|" + "WSCloseTestServer1 opened|"
					+ "session from WSCloseTestServer1 is open=TRUE|"
					+ "TCKBasicEndpoint OnClose CloseCode=NORMAL_CLOSURE", receivedMessageString.toString());

			if (session.isOpen()) {
				passed = false;
				receivedMessageString
						.append("=================Session stays open after calling close() from server side");
				session.close();
			}
		} catch (Exception e) {
			passed = false;
			receivedMessageString.append(e.getMessage());
		}
		logger.log(Logger.Level.INFO,receivedMessageString.toString());
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: close8Test
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:125; WebSocket:JAVADOC:145; WebSocket:JAVADOC:19;
	 * WebSocket:JAVADOC:20; WebSocket:JAVADOC:184; WebSocket:SPEC:WSC-2.2.2-1;
	 * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-2.1.5-1;
	 * WebSocket:SPEC:WSC-4.5-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void close8Test() throws Exception {
		String testName = "close2Test";
		boolean passed = true;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/WSCloseTestServer1"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			messageLatch = new CountDownLatch(1);
			session.getBasicRemote().sendText("testName=" + testName);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			SessionUtil.waitUntilClosed(session, _ws_wait, TimeUnit.SECONDS);

			passed = MessageValidator.checkSearchStrings("TCKBasicEndpoint OnOpen|" + "WSCloseTestServer1 opened|"
					+ "session from WSCloseTestServer1 is open=TRUE|" + "TCKBasicEndpoint OnClose CloseCode=TOO_BIG|"
					+ "TCKBasicEndpoint OnClose ReasonPhrase=TCKCloseNowWithReason", receivedMessageString.toString());

			if (session.isOpen()) {
				passed = false;
				receivedMessageString
						.append("=================Session stays open after calling close() from server side");
				session.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		logger.log(Logger.Level.INFO,receivedMessageString.toString());
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: close9Test
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:123; WebSocket:JAVADOC:145; WebSocket:JAVADOC:184;
	 * WebSocket:SPEC:WSC-4.5-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void close9Test() throws Exception {
		boolean passed = true;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/WSCloseTestServer2"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			session.close();
			SessionUtil.waitUntilClosed(session, _ws_wait, TimeUnit.SECONDS);

			passed = MessageValidator.checkSearchStrings("TCKBasicEndpoint OnOpen|" + "WSCloseTestServer2 opened|"
					+ "session from WSCloseTestServer2 is open=TRUE|"
					+ "TCKBasicEndpoint OnClose CloseCode=NORMAL_CLOSURE", receivedMessageString.toString());

			if (session.isOpen()) {
				passed = false;
				receivedMessageString.append("=================Session stays open after calling close()");
			}

		} catch (Exception e) {
			e.printStackTrace();
			passed = false;
		}

		logger.log(Logger.Level.INFO,receivedMessageString.toString());
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: close10Test
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:123; WebSocket:JAVADOC:145; WebSocket:JAVADOC:184;
	 * WebSocket:SPEC:WSC-2.2.2-1; WebSocket:SPEC:WSC-2.2.3-1;
	 * WebSocket:SPEC:WSC-2.1.5-1; WebSocket:SPEC:WSC-4.5-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void close10Test() throws Exception {
		String testName = "close1Test";
		boolean passed = true;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/WSCloseTestServer2"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);
			messageLatch = new CountDownLatch(1);
			session.getBasicRemote().sendText("testName=" + testName);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			SessionUtil.waitUntilClosed(session, _ws_wait, TimeUnit.SECONDS);

			passed = MessageValidator.checkSearchStrings("TCKBasicEndpoint OnOpen|" + "WSCloseTestServer2 opened|"
					+ "session from WSCloseTestServer2 is open=TRUE|"
					+ "TCKBasicEndpoint OnClose CloseCode=NORMAL_CLOSURE", receivedMessageString.toString());

			if (session.isOpen()) {
				passed = false;
				receivedMessageString
						.append("=================Session stays open after calling close() from server side");
				session.close();
			}
		} catch (Exception e) {
			passed = false;
			receivedMessageString.append(e.getMessage());
		}
		logger.log(Logger.Level.INFO,receivedMessageString.toString());
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: close11Test
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:125; WebSocket:JAVADOC:145; WebSocket:JAVADOC:19;
	 * WebSocket:JAVADOC:20; WebSocket:JAVADOC:184; WebSocket:SPEC:WSC-2.2.2-1;
	 * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-2.1.5-1;
	 * WebSocket:SPEC:WSC-4.5-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void close11Test() throws Exception {
		String testName = "close2Test";
		boolean passed = true;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/WSCloseTestServer2"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			messageLatch = new CountDownLatch(1);
			session.getBasicRemote().sendText("testName=" + testName);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			SessionUtil.waitUntilClosed(session, _ws_wait, TimeUnit.SECONDS);

			passed = MessageValidator.checkSearchStrings("TCKBasicEndpoint OnOpen|" + "WSCloseTestServer2 opened|"
					+ "session from WSCloseTestServer2 is open=TRUE|" + "TCKBasicEndpoint OnClose CloseCode=TOO_BIG|"
					+ "TCKBasicEndpoint OnClose ReasonPhrase=TCKCloseNowWithReason", receivedMessageString.toString());

			if (session.isOpen()) {
				passed = false;
				receivedMessageString
						.append("=================Session stays open after calling close() from server side");
				session.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		logger.log(Logger.Level.INFO,receivedMessageString.toString());
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: close12Test
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:125; WebSocket:JAVADOC:145; WebSocket:JAVADOC:19;
	 * WebSocket:JAVADOC:20; WebSocket:JAVADOC:184; WebSocket:SPEC:WSC-2.1.5-1;
	 * WebSocket:SPEC:WSC-2.2.2-1; WebSocket:SPEC:WSC-2.2.3-1;
	 * WebSocket:SPEC:WSC-4.5-2;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void close12Test() throws Exception {
		boolean passed = true;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKCloseEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			session.close(new CloseReason(CloseReason.CloseCodes.TOO_BIG, "TCKCloseNowWithReason"));
			SessionUtil.waitUntilClosed(session, _ws_wait, TimeUnit.SECONDS);

			passed = MessageValidator.checkSearchStrings("TCKCloseEndpoint OnOpen|" + "TCKTestServer opened|"
					+ "CKCloseEndpoint OnClose CloseCode|" + "TCKCloseEndpoint OnError",
					receivedMessageString.toString());

			if (session.isOpen()) {
				passed = false;
				receivedMessageString
						.append("=================Session stays open after calling close() from server side");
				session.close();
			}
		} catch (Exception e) {
			passed = false;
			e.printStackTrace();
		}
		logger.log(Logger.Level.INFO,receivedMessageString.toString());
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: getContainerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:129;
	 * WebSocket:JAVADOC:184;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void getContainerTest() throws Exception {
		boolean passed = true;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			WebSocketContainer tmp = session.getContainer();

			if (clientContainer != tmp) {
				passed = false;
				logger.log(Logger.Level.ERROR,"Incorrect return from getContainer" + tmp);
				logger.log(Logger.Level.ERROR,"Expecting " + clientContainer);
			}

			session.close();

			if (!passed) {
				throw new Exception("Test failed with incorrect response");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	/*
	 * @testName: getIdTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:130; WebSocket:JAVADOC:184;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void getIdTest() throws Exception {
		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKGetIdEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
			// No message handlers configured

			String tmp = session.getId();

			session.getBasicRemote().sendText("testName=getId1Test");
			// No message handlers configured

			session.close();
			logger.log(Logger.Level.ERROR,"getId=" + tmp);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

	}

	/*
	 * @testName: getId1Test
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:130; WebSocket:JAVADOC:184; WebSocket:SPEC:WSC-2.2.2-1;
	 * WebSocket:SPEC:WSC-2.2.3-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void getId1Test() throws Exception {
		boolean passed = true;
		String testName = "getId1Test";

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			String tmp = session.getId();

			receivedMessageString.append("getId returned from client side" + tmp);

			messageLatch = new CountDownLatch(2);
			session.getBasicRemote().sendText("testName=" + testName);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			logger.log(Logger.Level.INFO,receivedMessageString.toString());

			passed = MessageValidator
					.checkSearchStrings(
							"TCKBasicEndpoint OnOpen|" + "TCKTestServer opened|" + "session from Server is open=TRUE|"
									+ "TCKTestServer received String: testName=" + testName,
							receivedMessageString.toString());

			session.close();

			if (!passed) {
				throw new Exception("Test failed with incorrect response");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	/*
	 * @testName: setMaxBinaryMessageBufferSizeTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:131; WebSocket:JAVADOC:148; WebSocket:JAVADOC:184;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void setMaxBinaryMessageBufferSizeTest() throws Exception {
		int size = 98765432;
		boolean passed = true;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			receivedMessageString.append(
					"getMaxBinaryMessageBufferSize returned default value =" + session.getMaxBinaryMessageBufferSize());
			session.setMaxBinaryMessageBufferSize(size);

			int tmp = session.getMaxBinaryMessageBufferSize();
			if (tmp == size) {
				receivedMessageString.append("getMaxBinaryMessageBufferSize returned expected value =" + tmp);
			} else {
				passed = false;
				receivedMessageString.append("getMaxBinaryMessageBufferSize returned unexpected value =" + tmp);
				receivedMessageString.append("expected value =" + size);
			}

			session.close();

		} catch (Exception e) {
			passed = false;
			receivedMessageString.append(e.getMessage());
		}

		logger.log(Logger.Level.INFO,receivedMessageString.toString());
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: setMaxTextMessageBufferSizeTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:133; WebSocket:JAVADOC:150; WebSocket:JAVADOC:184;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void setMaxTextMessageBufferSizeTest() throws Exception {
		int size = 987654321;
		messageLatch = new CountDownLatch(1); // not to throw NPE in
												// TCKBasicEndpoint

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			logger.log(Logger.Level.INFO,"getMaxTextMessageBufferSize returned default value =" + session.getMaxTextMessageBufferSize());
			session.setMaxTextMessageBufferSize(size);
			int tmp = session.getMaxTextMessageBufferSize();
			if (tmp == size) {
				logger.log(Logger.Level.INFO,"getMaxTextMessageBufferSize returned expected value =" + tmp);
			} else {
				logger.log(Logger.Level.INFO,"getMaxTextMessageBufferSize returned unexpected value =" + tmp);
				logger.log(Logger.Level.INFO,"expected value =" + size);
			}

			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	/*
	 * @testName: setTimeoutTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:132; WebSocket:JAVADOC:149; WebSocket:JAVADOC:184;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void setTimeoutTest() throws Exception {
		long tt = 9876543210L;
		messageLatch = new CountDownLatch(1); // not to throw NPE in
												// TCKBasicEndpoint

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));

			System.out
					.println("getMaxIdleTimeout returned default value on client side =" + session.getMaxIdleTimeout());
			session.setMaxIdleTimeout(tt);
			long tmp = session.getMaxIdleTimeout();
			if (tmp == tt) {
				logger.log(Logger.Level.INFO,"getMaxIdleTimeout returned expected value =" + tmp);
			} else {
				logger.log(Logger.Level.INFO,"getMaxIdleTimeout returned unexpected value =" + tmp);
				logger.log(Logger.Level.INFO,"expected value =" + tt);
			}
			session.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	/*
	 * @testName: setTimeout1Test
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:132; WebSocket:JAVADOC:149; WebSocket:JAVADOC:140;
	 * WebSocket:SPEC:WSC-2.2.2-1; WebSocket:SPEC:WSC-2.2.3-1;
	 * WebSocket:SPEC:WSC-4.4-1; WebSocket:SPEC:WSC-4.4-2; WebSocket:JAVADOC:184;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void setTimeout1Test() throws Exception {
		String testName = "setTimeout1Test";
		boolean passed = true;
		long tt = _ws_wait * 4 * 1000L;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer?timeout=" + _ws_wait));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			System.out
					.println("getMaxIdleTimeout returned default value on client side =" + session.getMaxIdleTimeout());
			session.setMaxIdleTimeout(tt);

			// Wait for 2 messages but not third (which should be sent after timeout)
			messageLatch = new CountDownLatch(2);
			session.getBasicRemote().sendText("testName=" + testName);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			SessionUtil.waitUntilClosed(session, _ws_wait * 8, TimeUnit.SECONDS);
			if (session.isOpen()) {
				passed = false;
				receivedMessageString.append("Session is still open after timeout");
			}

			logger.log(Logger.Level.INFO,receivedMessageString.toString());

			boolean tmp = MessageValidator.checkSearchStrings(
					"TCKBasicEndpoint OnOpen|" + "TCKTestServer opened|" + "session from Server is open=TRUE|"
							+ "TCKTestServer received String: testName=setTimeout1Test",
					receivedMessageString.toString());
			if (!tmp) {
				passed = false;
			}

			if (receivedMessageString.indexOf("AnyString=") > -1
					|| receivedMessageString.indexOf("TCKTestServer second message after sleep") > -1) {
				passed = false;
				logger.log(Logger.Level.ERROR,"Test failed due to message sent and/or received after timeout from client side");

			}
			if (!passed) {
				throw new Exception("Test failed with incorrect response");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	/*
	 * @testName: setTimeout2Test
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:132; WebSocket:JAVADOC:149; WebSocket:JAVADOC:140;
	 * WebSocket:JAVADOC:184; WebSocket:SPEC:WSC-2.2.2-1;
	 * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-4.4-1;
	 * WebSocket:SPEC:WSC-4.4-2;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void setTimeout2Test() throws Exception {
		String testName = "setTimeout2Test";
		boolean passed = true;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer?timeout=" + _ws_wait));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			System.out
					.println("getMaxIdleTimeout returned default value on client side =" + session.getMaxIdleTimeout());

			// Session timeout is set on server
			// Wait for 2 messages but not third (which should be sent after timeout)
			messageLatch = new CountDownLatch(2);
			session.getBasicRemote().sendText("testName=" + testName);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			SessionUtil.waitUntilClosed(session, _ws_wait * 8, TimeUnit.SECONDS);
			if (session.isOpen()) {
				passed = false;
				receivedMessageString.append("Session is still open after timeout");
			}

			logger.log(Logger.Level.INFO,receivedMessageString.toString());
			boolean tmp = MessageValidator.checkSearchStrings(
					"TCKBasicEndpoint OnOpen|" + "TCKTestServer opened|" + "session from Server is open=TRUE|"
							+ "TCKTestServer received String: testName=setTimeout2Test",
					receivedMessageString.toString());

			if (!tmp) {
				passed = false;
			}

			if (receivedMessageString.indexOf("AnyString=") > -1
					|| receivedMessageString.indexOf("TCKTestServer second message after sleep") > -1) {
				passed = false;
				logger.log(Logger.Level.ERROR,"Test failed due to message sent and/or received after timeout from server side");

			}

			if (!passed) {
				throw new Exception("Test Failed");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	/*
	 * @testName: getQueryStringTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:140; WebSocket:JAVADOC:184; WebSocket:SPEC:WSC-2.2.2-1;
	 * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-4.4-1;
	 * WebSocket:SPEC:WSC-4.4-2;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void getQueryStringTest() throws Exception {
		String testName = "getQueryStringTest";
		boolean passed = true;
		String querystring = "test1=value1&test2=value2&test3=value3";

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer?" + querystring));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			messageLatch = new CountDownLatch(2);
			session.getBasicRemote().sendText("testName=" + testName);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			logger.log(Logger.Level.INFO,receivedMessageString.toString());
			passed = MessageValidator.checkSearchStrings(
					"TCKBasicEndpoint OnOpen|" + "TCKTestServer opened|" + "session from Server is open=TRUE|"
							+ "TCKTestServer received String: testName=" + testName + "|"
							+ "TCKTestServer: expected Query String returned|" + querystring,
					receivedMessageString.toString());

			if (!passed) {
				throw new Exception("Test Failed");
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	/*
	 * @testName: getPathParametersTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:184; WebSocket:SPEC:WSC-2.2.2-1; WebSocket:JAVADOC:138;
	 * WebSocket:SPEC:WSC-4.4-1; WebSocket:SPEC:WSC-4.4-2;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void getPathParametersTest() throws Exception {
		String message = "invoke test";
		boolean passed = true;
		String param1 = "test1";
		String param2 = "test2=xyz";

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(1);
			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config, new URI("ws://"
					+ _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServerPathParam/" + param1 + "/" + param2));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			messageLatch = new CountDownLatch(2);
			session.getBasicRemote().sendText(message);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			passed = MessageValidator.checkSearchStrings(
					"TCKBasicEndpoint OnOpen|" + "WSTestServerPathParam opened|"
							+ "WSTestServerPathParam received String: " + message + "|"
							+ "WSTestServerPathParam: pathparams returned;param1=" + param1 + ";param2=" + param2,
					receivedMessageString.toString());

			session.close();

		} catch (Exception e) {
			passed = false;
			e.printStackTrace();
		}

		logger.log(Logger.Level.INFO,receivedMessageString.toString());

		if (!passed) {
			throw new Exception("Test Failed");
		}
	}

	/*
	 * @testName: getRequestURITest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:142; WebSocket:JAVADOC:184; WebSocket:SPEC:WSC-2.2.2-1;
	 * WebSocket:SPEC:WSC-2.2.3-1; WebSocket:SPEC:WSC-4.4-1;
	 * WebSocket:SPEC:WSC-4.4-2;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void getRequestURITest() throws Exception {
		String testName = "getRequestURITest";
		boolean passed = true;
		String querystring = "test1=value1&test2=value2&test3=value3";

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);

			StringBuilder uriBuilder = new StringBuilder();
			uriBuilder.append("ws://");
			uriBuilder.append(_hostname);
			if (_port != 80) {
				uriBuilder.append(':');
				uriBuilder.append(_port);
			}
			uriBuilder.append(CONTEXT_ROOT);
			uriBuilder.append("/TCKTestServer?");
			uriBuilder.append(querystring);
			String uri = uriBuilder.toString();

			Session session = clientContainer.connectToServer(TCKBasicEndpoint.class, config, new URI(uri));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			messageLatch = new CountDownLatch(4);
			session.getBasicRemote().sendText("testName=" + testName);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			logger.log(Logger.Level.INFO,receivedMessageString.toString());
			passed = MessageValidator.checkSearchStrings("TCKBasicEndpoint OnOpen|" + "TCKTestServer opened|"
					+ "session from Server is open=TRUE|" + "TCKTestServer received String: testName=" + testName + "|"
					+ "TCKTestServer: getRequestURI returned=" + uri, receivedMessageString.toString());
		} catch (Exception e) {
			e.printStackTrace();
			passed = false;
		}
		if (!passed) {
			throw new Exception("Test Failed");
		}
	}

	/*
	 * @testName: getOpenSessionsTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:159; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79; WebSocket:JAVADOC:128; WebSocket:JAVADOC:112;
	 * WebSocket:JAVADOC:137; WebSocket:JAVADOC:184; WebSocket:SPEC:WSC-2.2.2-1;
	 * WebSocket:SPEC:WSC-2.2.3-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void getOpenSessionsTest() throws Exception {
		boolean passed = true;
		String testName = "getOpenSessionsTest";

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(2);
			Session session = clientContainer.connectToServer(TCKOpenSessionEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			messageLatch = new CountDownLatch(4);
			session.getBasicRemote().sendText("testName=" + testName);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			int os = getOpenSessions(receivedMessageString.toString());

			messageLatch = new CountDownLatch(2);
			Session session1 = clientContainer.connectToServer(TCKOpenSessionEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			messageLatch = new CountDownLatch(4);
			session1.getBasicRemote().sendText("testName=" + testName);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			int os1 = getOpenSessions(receivedMessageString.toString());

			messageLatch = new CountDownLatch(2);
			Session session2 = clientContainer.connectToServer(TCKOpenSessionEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			messageLatch = new CountDownLatch(4);
			session2.getBasicRemote().sendText("testName=" + testName);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			int os2 = getOpenSessions(receivedMessageString.toString());

			session.close();
			SessionUtil.waitUntilClosed(session, _ws_wait, TimeUnit.SECONDS);

			messageLatch = new CountDownLatch(4);
			session1.getBasicRemote().sendText("testName=" + testName);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			int os3 = getOpenSessions(receivedMessageString.toString());

			session1.close();
			SessionUtil.waitUntilClosed(session1, _ws_wait, TimeUnit.SECONDS);

			messageLatch = new CountDownLatch(4);
			session2.getBasicRemote().sendText("testName=" + testName);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			int os4 = getOpenSessions(receivedMessageString.toString());

			session2.close();

			if (os == 1) {
				if (os1 != 2 || os2 != 3) {
					passed = false;
					System.out.print("Not perfect 12321 pattern for open session");
				}
			}

			if (os4 > os3 || os3 > os2) {
				passed = false;
				System.out.print("Incorrect XXX++ pattern for open session");
			}

			if (os < 1 || os1 < 2 || os2 < 3 || os3 < 2 || os4 < 1) {
				passed = false;
				System.out.print("Cannot have less open session than 12321");
			}

			if (os1 - os > 1 || os2 - os1 > 1) {
				passed = false;
				System.out.print("Too many session are open");
			}

		} catch (Exception ioe) {
			passed = false;
			ioe.printStackTrace();
		}
		logger.log(Logger.Level.INFO,receivedMessageString.toString());

		if (!passed) {
			throw new Exception("Incorrect response received");
		}
	}

	@Override
	public void cleanup() throws Exception {
		super.cleanup();
	}

	public final static class TCKBasicEndpoint extends Endpoint {

		@Override
		public void onOpen(Session session, EndpointConfig config) {
			receivedMessageString.append("TCKBasicEndpoint OnOpen");
			session.addMessageHandler(new MessageHandler.Whole<String>() {

				@Override
				public void onMessage(String message) {
					receivedMessageString.append(message);
					messageLatch.countDown();
				}
			});

			session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {

				@Override
				public void onMessage(ByteBuffer data) {
					byte[] data1 = new byte[data.remaining()];
					data.get(data1);
					receivedMessageString
							.append("========Basic ByteBuffer MessageHander received=" + new String(data1));
					messageLatch.countDown();
				}
			});

		}

		@Override
		public void onClose(Session session, CloseReason closeReason) {
			receivedMessageString.append("TCKBasicEndpoint OnClose CloseCode=" + closeReason.getCloseCode());
			receivedMessageString.append("TCKBasicEndpoint OnClose ReasonPhrase=" + closeReason.getReasonPhrase());
		}
	}

	public final static class TCKBasicEndpoint1 extends Endpoint {

		@Override
		public void onOpen(Session session, EndpointConfig config) {
		}
	}

	public final static class TCKGetIdEndpoint extends Endpoint {

		@Override
		public void onOpen(Session session, EndpointConfig config) {
			receivedMessageString.append("======Another SessionID=" + session.getId());
			logger.log(Logger.Level.INFO,session.getId());
		}
	}

	public final static class TCKBasicStringEndpoint extends Endpoint {

		public void onMessage(String message) {
			receivedMessageString.append("========First TextMessageHander received=").append(message);
			messageLatch.countDown();
		}

		@Override
		public void onOpen(Session session, EndpointConfig config) {
			session.addMessageHandler(new MessageHandler.Whole<String>() {

				@Override
				public void onMessage(String message) {
					receivedMessageString.append("========First TextMessageHander received=").append(message);
					messageLatch.countDown();
				}
			});
			try {
				session.addMessageHandler(new MessageHandler.Whole<String>() {

					@Override
					public void onMessage(String message) {
						receivedMessageString.append("========Second TextMessageHander received=").append(message);
						messageLatch.countDown();
					}
				});
			} catch (IllegalStateException ile) {
				receivedMessageString
						.append("========Expected IllegalStateException thrown by Second TextMessageHandler");
				// ile.printStackTrace();
			}
			try {
				session.getBasicRemote().sendText(SENT_STRING_MESSAGE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public final static class TCKBasicByteEndpoint extends Endpoint {

		@Override
		public void onOpen(Session session, EndpointConfig config) {

			session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {

				@Override
				public void onMessage(ByteBuffer message) {
					String message_string = IOUtil.byteBufferToString(message);
					receivedMessageString.append("========First Basic ByteBuffer MessageHander received=")
							.append(message_string);
					messageLatch.countDown();
				}
			});
			try {
				session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {

					@Override
					public void onMessage(ByteBuffer data) {
						receivedMessageString.append("========Second Basic ByteBuffer MessageHander received=")
								.append(data.toString());
						messageLatch.countDown();
					}
				});
			} catch (IllegalStateException ile) {
				receivedMessageString
						.append("========Expected IllegalStateException thrown by Second ByteBuffer MessageHandler");
				// ile.printStackTrace();
			}
		}
	}

	public final static class TCKOpenSessionEndpoint extends Endpoint {

		@Override
		public void onOpen(Session session, EndpointConfig config) {

			session.addMessageHandler(new MessageHandler.Whole<String>() {

				@Override
				public void onMessage(String message) {
					receivedMessageString.append(message);
					messageLatch.countDown();
				}
			});
		}

		@Override
		public void onClose(Session session, CloseReason closeReason) {
			receivedMessageString.append("onClose");
		}
	}

	public final static class TCKCloseEndpoint extends Endpoint {

		@Override
		public void onOpen(Session session, EndpointConfig config) {
			receivedMessageString.append("TCKCloseEndpoint OnOpen");
			session.addMessageHandler(new MessageHandler.Whole<String>() {

				@Override
				public void onMessage(String message) {
					receivedMessageString.append(message);
					messageLatch.countDown();
				}
			});

			session.addMessageHandler(new MessageHandler.Whole<ByteBuffer>() {

				@Override
				public void onMessage(ByteBuffer data) {
					byte[] data1 = new byte[data.remaining()];
					data.get(data1);
					receivedMessageString
							.append("========Basic ByteBuffer MessageHander received=" + new String(data1));
					messageLatch.countDown();
				}
			});
		}

		@Override
		public void onClose(Session session, CloseReason closeReason) {
			receivedMessageString.append("TCKCloseEndpoint OnClose CloseCode=" + closeReason.getCloseCode());
			receivedMessageString.append("Pass_On_To_Error=" + closeReason.getReasonPhrase());

			@SuppressWarnings("unused")
			int i = 1 / 0;
		}

		@Override
		public void onError(Session session, Throwable t) {
			receivedMessageString.append("TCKCloseEndpoint OnError");
			receivedMessageString.append(t.getMessage());
		}
	}

	@SuppressWarnings("unused")
	private int getOpenSessions(String message) {
		int start = receivedMessageString.lastIndexOf("getOpenSessions=");
		int stop = receivedMessageString.lastIndexOf("========TCKTestServer responded");
		int os = Integer.parseInt(receivedMessageString.substring(start + 16, stop));
		logger.log(Logger.Level.INFO,"open session=" + os);
		return os;
	}
}
