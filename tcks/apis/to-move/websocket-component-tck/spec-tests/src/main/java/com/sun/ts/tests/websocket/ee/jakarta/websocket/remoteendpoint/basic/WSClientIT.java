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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.remoteendpoint.basic;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.websocket.common.client.BinaryAndTextClientEndpoint;
import com.sun.ts.tests.websocket.common.client.ClientEndpoint;
import com.sun.ts.tests.websocket.common.client.EndpointCallback;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.impl.StringPingMessage;
import com.sun.ts.tests.websocket.common.impl.StringPongMessage;
import com.sun.ts.tests.websocket.common.impl.WaitingSendHandler;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextEncoder;
import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.common.util.StringUtil;
import com.sun.ts.tests.websocket.ee.jakarta.websocket.remoteendpoint.PongMessageClientEndpoint;

import jakarta.websocket.ClientEndpointConfig;
import jakarta.websocket.Encoder;
import jakarta.websocket.RemoteEndpoint.Basic;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ws_wait;
 */
@ExtendWith(ArquillianExtension.class)
public class WSClientIT extends WebSocketCommonClient {
	private static final long serialVersionUID = -8530759310254161854L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class,
				"wsc_ee_jakarta_websocket_remoteendpoint_basic_web.war");
		archive.addPackages(true, Filters.exclude(WSClientIT.class),
				"com.sun.ts.tests.websocket.ee.jakarta.websocket.remoteendpoint.basic");
		archive.addPackages(true, "com.sun.ts.tests.websocket.common.stringbean");
		archive.addClasses(WaitingSendHandler.class);
		archive.addClasses(IOUtil.class);
		return archive;
	};

	static final String[] RESPONSE = WSCServerSideServer.RESPONSE;

	static final String ECHO = "echo";

	public WSClientIT() throws Exception {
		setContextRoot("wsc_ee_jakarta_websocket_remoteendpoint_basic_web");
	}

	/* Run test */

	/*
	 * @testName: sendBinaryOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:105;
	 * 
	 * @test_Strategy: Send a binary message, returning when all of the message has
	 * been transmitted.
	 */
	@Test
	public void sendBinaryOnServerTest() throws Exception {
		sendOnServer(OPS.SENDBINARY);
	}

	/*
	 * @testName: sendBinaryOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:107;
	 * 
	 * @test_Strategy: Send a binary message, returning when all of the message has
	 * been transmitted.
	 */
	@Test
	public void sendBinaryOnClientTest() throws Exception {
		sendOnClient(OPS.SENDBINARY);
	}

	/*
	 * @testName: sendBinaryPartialOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:107;
	 * 
	 * @test_Strategy: Send a binary message, returning when all of the message has
	 * been transmitted.
	 */
	@Test
	public void sendBinaryPartialOnServerTest() throws Exception {
		sendOnServer(OPS.SENDBINARYPART1,
				OPS.SENDBINARYPART1.name() + OPS.SENDBINARYPART2.name() + OPS.SENDBINARYPART3.name());
	}

	/*
	 * @testName: sendBinaryPartialOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:105;
	 * 
	 * @test_Strategy: Send a binary message, returning when all of the message has
	 * been transmitted.
	 */
	@Test
	public void sendBinaryPartialOnClientTest() throws Exception {
		sendOnClient(OPS.SENDBINARYPART1,
				OPS.SENDBINARYPART1.name() + OPS.SENDBINARYPART2.name() + OPS.SENDBINARYPART3.name());
	}

	/*
	 * @testName: sendBinaryThrowsIAEOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:105;
	 * 
	 * @test_Strategy: Throws: IllegalArgumentException - if the data is null.
	 */
	@Test
	public void sendBinaryThrowsIAEOnServerTest() throws Exception {
		invoke("server", OPS.SENDBINARYTHROWS.name(), RESPONSE[0]);
	}

	/*
	 * @testName: sendBinaryThrowsIAEOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:105;
	 * 
	 * @test_Strategy: Throws: IllegalArgumentException - if the data is null.
	 */
	@Test
	public void sendBinaryThrowsIAEOnClientTest() throws Exception {
		sendOnClientThrows(OPS.SENDBINARYTHROWS);
	}

	/*
	 * @testName: sendObjectOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Sends a custom developer object, blocking until it has been
	 * transmitted.
	 */
	@Test
	public void sendObjectOnServerTest() throws Exception {
		sendOnServer(OPS.SENDOBJECT);
	}

	/*
	 * @testName: sendObjectOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Sends a custom developer object, blocking until it has been
	 * transmitted.
	 */
	@Test
	public void sendObjectOnClientTest() throws Exception {
		sendOnClient(OPS.SENDOBJECT);
	}

	/*
	 * @testName: sendObjectBooleanOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Sends a custom developer object, blocking until it has been
	 * transmitted.
	 */
	@Test
	public void sendObjectBooleanOnServerTest() throws Exception {
		sendOnServer(OPS.SENDOBJECT_BOOL, false);
	}

	/*
	 * @testName: sendObjectBooleanOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Sends a custom developer object, blocking until it has been
	 * transmitted.
	 */
	@Test
	public void sendObjectBooleanOnClientTest() throws Exception {
		sendOnClient(OPS.SENDOBJECT_BOOL, String.valueOf(false));
	}

	/*
	 * @testName: sendObjectByteOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Sends a custom developer object, blocking until it has been
	 * transmitted.
	 */
	@Test
	public void sendObjectByteOnServerTest() throws Exception {
		sendOnServer(OPS.SENDOBJECT_BYTE, -100);
	}

	/*
	 * @testName: sendObjectByteOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Sends a custom developer object, blocking until it has been
	 * transmitted.
	 */
	@Test
	public void sendObjectByteOnClientTest() throws Exception {
		sendOnClient(OPS.SENDOBJECT_BYTE, "-100");
	}

	/*
	 * @testName: sendObjectCharOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Sends a custom developer object, blocking until it has been
	 * transmitted.
	 */
	@Test
	public void sendObjectCharOnServerTest() throws Exception {
		sendOnServer(OPS.SENDOBJECT_CHAR, String.valueOf((char) 106));
	}

	/*
	 * @testName: sendObjectCharOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Sends a custom developer object, blocking until it has been
	 * transmitted.
	 */
	@Test
	public void sendObjectCharOnClientTest() throws Exception {
		sendOnClient(OPS.SENDOBJECT_CHAR, String.valueOf((char) 106));
	}

	/*
	 * @testName: sendObjectDoubleOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Sends a custom developer object, blocking until it has been
	 * transmitted.
	 */
	@Test
	public void sendObjectDoubleOnServerTest() throws Exception {
		sendOnServer(OPS.SENDOBJECT_DOUBLE, -105d);
	}

	/*
	 * @testName: sendObjectDoubleOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Sends a custom developer object, blocking until it has been
	 * transmitted.
	 */
	@Test
	public void sendObjectDoubleOnClientTest() throws Exception {
		sendOnClient(OPS.SENDOBJECT_DOUBLE, "-105");
	}

	/*
	 * @testName: sendObjectFloatOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Sends a custom developer object, blocking until it has been
	 * transmitted.
	 */
	@Test
	public void sendObjectFloatOnServerTest() throws Exception {
		sendOnServer(OPS.SENDOBJECT_FLOAT, -104f);
	}

	/*
	 * @testName: sendObjectFloatOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Sends a custom developer object, blocking until it has been
	 * transmitted.
	 */
	@Test
	public void sendObjectFloatOnClientTest() throws Exception {
		sendOnClient(OPS.SENDOBJECT_FLOAT, "-104");
	}

	/*
	 * @testName: sendObjectIntOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Sends a custom developer object, blocking until it has been
	 * transmitted.
	 */
	@Test
	public void sendObjectIntOnServerTest() throws Exception {
		sendOnServer(OPS.SENDOBJECT_INT, -102);
	}

	/*
	 * @testName: sendObjectIntOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Sends a custom developer object, blocking until it has been
	 * transmitted.
	 */
	@Test
	public void sendObjectIntOnClientTest() throws Exception {
		sendOnClient(OPS.SENDOBJECT_INT, "-102");
	}

	/*
	 * @testName: sendObjectLongOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Sends a custom developer object, blocking until it has been
	 * transmitted.
	 */
	@Test
	public void sendObjectLongOnServerTest() throws Exception {
		sendOnServer(OPS.SENDOBJECT_LONG, -103L);
	}

	/*
	 * @testName: sendObjectLongOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Sends a custom developer object, blocking until it has been
	 * transmitted.
	 */
	@Test
	public void sendObjectLongOnClientTest() throws Exception {
		sendOnClient(OPS.SENDOBJECT_LONG, "-103");
	}

	/*
	 * @testName: sendObjectShortOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Sends a custom developer object, blocking until it has been
	 * transmitted.
	 */
	@Test
	public void sendObjectShortOnServerTest() throws Exception {
		sendOnServer(OPS.SENDOBJECT_SHORT, -101);
	}

	/*
	 * @testName: sendObjectShortOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Sends a custom developer object, blocking until it has been
	 * transmitted.
	 */
	@Test
	public void sendObjectShortOnClientTest() throws Exception {
		sendOnClient(OPS.SENDOBJECT_SHORT, "-101");
	}

	/*
	 * @testName: sendObjectThrowsIAEOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Throws: IllegalArgumentException - if the data is null.
	 */
	@Test
	public void sendObjectThrowsIAEOnServerTest() throws Exception {
		invoke("server", OPS.SENDOBJECTTHROWS.name(), RESPONSE[0]);
	}

	/*
	 * @testName: sendObjectThrowsIAEOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109;
	 * 
	 * @test_Strategy: Throws: IllegalArgumentException - if the data is null.
	 */
	@Test
	public void sendObjectThrowsIAEOnClientTest() throws Exception {
		sendOnClientThrows(OPS.SENDOBJECTTHROWS);
	}

	/*
	 * @testName: sendObjectThrowsEncodeExceptionOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:111;
	 * 
	 * @test_Strategy: Throws: EncodeException - if there was a problem encoding the
	 * message object into the form of a native websocket message
	 */
	@Test
	public void sendObjectThrowsEncodeExceptionOnServerTest() throws Exception {
		invoke("server", OPS.SENDOBJECTTHROWSENCODEEEXCEPTION.name(), RESPONSE[0]);
	}

	/*
	 * @testName: sendObjectThrowsEncodeExceptionOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:111;
	 * 
	 * @test_Strategy: Throws: EncodeException - if there was a problem encoding the
	 * message object into the form of a native websocket message
	 */
	@Test
	public void sendObjectThrowsEncodeExceptionOnClientTest() throws Exception {
		sendOnClientThrowsException(OPS.SENDOBJECTTHROWSENCODEEEXCEPTION, "EncodeException");
	}

	/*
	 * @testName: sendTextOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:112;
	 * 
	 * @test_Strategy: Send a text message, blocking until all of the message has
	 * been transmitted.
	 */
	@Test
	public void sendTextOnServerTest() throws Exception {
		sendOnServer(OPS.SENDTEXT);
	}

	/*
	 * @testName: sendTextOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:114;
	 * 
	 * @test_Strategy: Send a text message, blocking until all of the message has
	 * been transmitted.
	 */
	@Test
	public void sendTextOnClientTest() throws Exception {
		sendOnClient(OPS.SENDTEXT);
	}

	/*
	 * @testName: sendTextPartialOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:114;
	 * 
	 * @test_Strategy: Send a text message, blocking until all of the message has
	 * been transmitted.
	 */
	@Test
	public void sendTextPartialOnServerTest() throws Exception {
		sendOnServer(OPS.SENDTEXTPART1, OPS.SENDTEXTPART1.name() + OPS.SENDTEXTPART2.name() + OPS.SENDTEXTPART3.name());
	}

	/*
	 * @testName: sendTextPartialOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:112;
	 * 
	 * @test_Strategy: Send a text message, blocking until all of the message has
	 * been transmitted.
	 */
	@Test
	public void sendTextPartialOnClientTest() throws Exception {
		sendOnClient(OPS.SENDTEXTPART1, OPS.SENDTEXTPART1.name() + OPS.SENDTEXTPART2.name() + OPS.SENDTEXTPART3.name());
	}

	/*
	 * @testName: sendTextThrowsIAEOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:112;
	 * 
	 * @test_Strategy: Throws: IllegalArgumentException - if the text is null.
	 */
	@Test
	public void sendTextThrowsIAEOnServerTest() throws Exception {
		invoke("server", OPS.SENDTEXTTHROWS.name(), RESPONSE[0]);
	}

	/*
	 * @testName: sendTextThrowsIAEOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:112;
	 * 
	 * @test_Strategy: Throws: IllegalArgumentException - if the text is null.
	 */
	@Test
	public void sendTextThrowsIAEOnClientTest() throws Exception {
		sendOnClientThrows(OPS.SENDTEXTTHROWS);
	}

	/*
	 * @testName: getSendStreamOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:101;
	 * 
	 * @test_Strategy: Opens an output stream on which a binary message may be sent.
	 * The developer must close the output stream in order to indicate that the
	 * complete message has been placed into the output stream.
	 */
	@Test
	public void getSendStreamOnServerTest() throws Exception {
		sendOnServer(OPS.SENDSTREAM);
	}

	/*
	 * @testName: getSendStreamOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:101;
	 * 
	 * @test_Strategy: Opens an output stream on which a binary message may be sent.
	 * The developer must close the output stream in order to indicate that the
	 * complete message has been placed into the output stream.
	 */
	@Test
	public void getSendStreamOnClientTest() throws Exception {
		sendOnClient(OPS.SENDSTREAM);
	}

	/*
	 * @testName: getSendWriterOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:103;
	 * 
	 * @test_Strategy: Opens an output stream on which a binary message may be sent.
	 * The developer must close the writer in order to indicate that the complete
	 * message has been placed into the character stream.
	 */
	@Test
	public void getSendWriterOnServerTest() throws Exception {
		sendOnServer(OPS.SENDWRITER);
	}

	/*
	 * @testName: getSendWriterOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:103;
	 * 
	 * @test_Strategy: Opens an output stream on which a binary message may be sent.
	 * The developer must close the writer in order to indicate that the complete
	 * message has been placed into the character stream.
	 */
	@Test
	public void getSendWriterOnClientTest() throws Exception {
		sendOnClient(OPS.SENDWRITER);
	}

	/*
	 * @testName: batchingAllowedOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:84;WebSocket:JAVADOC:91;
	 * 
	 * @test_Strategy: calls setBatchingAllowed(!getBatchingAllowed()) and checks no
	 * exception is thrown
	 */
	@Test
	public void batchingAllowedOnServerTest() throws Exception {
		sendOnServer(OPS.BATCHING_ALLOWED);
	}

	/*
	 * @testName: batchingAllowedOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:84;WebSocket:JAVADOC:91;
	 * 
	 * @test_Strategy: calls setBatchingAllowed(!getBatchingAllowed()) and checks no
	 * exception is thrown
	 */
	@Test
	public void batchingAllowedOnClientTest() throws Exception {
		sendOnClient(OPS.BATCHING_ALLOWED);
	}

	/*
	 * @testName: sendPingOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:85;
	 * 
	 * @test_Strategy: Send a Ping message containing the given application data to
	 * the remote endpoint. The corresponding Pong message may be picked up using
	 * the MessageHandler.Pong handler
	 */
	@Test
	public void sendPingOnServerTest() throws Exception {
		setCountDownLatchCount(2);
		setProperty(Property.CONTENT, OPS.SEND_PING.name());
		setProperty(Property.REQUEST, buildRequest("server"));
		// first server sends ping, websocket impl responses with pong
		// then server sends ok and the pong is caught back on server
		// and pong data are send back to client
		setProperty(Property.UNORDERED_SEARCH_STRING, search(RESPONSE[0], OPS.SEND_PING));
		invoke();
	}

	/*
	 * @testName: sendPingOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:85;
	 * 
	 * @test_Strategy: Send a Ping message containing the given application data to
	 * the remote endpoint. The corresponding Pong message may be picked up using
	 * the MessageHandler.Pong handler
	 */
	@Test
	public void sendPingOnClientTest() throws Exception {
		setClientEndpoint(PongMessageClientEndpoint.class);
		sendOnClient(OPS.SEND_PING);
	}

	/*
	 * @testName: sendPingDelaysTimoutOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:85; WebSocket:SPEC:WSC-2.2.5-1;
	 * WebSocket:SPEC:WSC-2.2.5-2;
	 * 
	 * @test_Strategy: Allows the developer to send an unsolicited Pong message
	 * containing the given application data in order to serve as a unidirectional
	 * heartbeat for the session.
	 * 
	 * if a websocket implementation receives a ping message from a peer, it must
	 * respond as soon as possible to that peer with a pong message containing the
	 * same application data.
	 * 
	 * if the implementation receives a pong message addressed to this endpoint, it
	 * must call that MessageHandler or that annotated message
	 */
	@Test
	public void sendPingDelaysTimoutOnServerTest() throws Exception {
		StringPingMessage ping = new StringPingMessage(OPS.POKE.name());
		setClientEndpoint(PongMessageClientEndpoint.class);
		invoke("server", OPS.IDLE.name(), OPS.IDLE.name(), false);

		TestUtil.sleepMsec(500);
		invokeAgain(ping, OPS.POKE.name(), false);
		TestUtil.sleepMsec(500);
		invokeAgain(ping, OPS.POKE.name(), false);
		TestUtil.sleepMsec(500);
		invokeAgain(ping, OPS.POKE.name(), false);
		TestUtil.sleepMsec(500);

		invokeAgain(OPS.POKE.name(), OPS.POKE.name(), true);
	}

	/*
	 * @testName: sendPingDelaysTimoutOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:85; WebSocket:SPEC:WSC-2.2.5-1;
	 * WebSocket:SPEC:WSC-2.2.5-2;
	 * 
	 * @test_Strategy: Allows the developer to send an unsolicited Pong message
	 * containing the given application data in order to serve as a unidirectional
	 * heartbeat for the session.
	 * 
	 * if a websocket implementation receives a ping message from a peer, it must
	 * respond as soon as possible to that peer with a pong message containing the
	 * same application data.
	 * 
	 * if the implementation receives a pong message addressed to this endpoint, it
	 * must call that MessageHandler or that annotated message
	 */
	@Test
	public void sendPingDelaysTimoutOnClientTest() throws Exception {
		setClientEndpoint(PongMessageClientEndpoint.class);
		invoke("server", OPS.POKE.name(), OPS.POKE.name(), false);
		getSession().setMaxIdleTimeout(1500L);

		setCountDownLatchCount(5);
		setProperty(Property.CONTENT, OPS.PING_4_TIMES.name());
		setProperty(Property.SEARCH_STRING, OPS.POKE.name(), OPS.POKE.name(), OPS.POKE.name(), OPS.POKE.name());
		setProperty(Property.SEARCH_STRING_IGNORE_CASE);
		setProperty(Property.SEARCH_STRING_IGNORE_CASE, RESPONSE[0]);
		invokeAgain(true);
	}

	/*
	 * @testName: sendPingThrowsOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:87;
	 * 
	 * @test_Strategy: throws IllegalArgumentException - if the applicationData
	 * exceeds the maximum allowed payload of 125 bytes
	 */
	@Test
	public void sendPingThrowsOnServerTest() throws Exception {
		invoke("server", OPS.SEND_PING_THROWS, RESPONSE[0]);
	}

	/*
	 * @testName: sendPingThrowsOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:87;
	 * 
	 * @test_Strategy: throws IllegalArgumentException - if the applicationData
	 * exceeds the maximum allowed payload of 125 bytes
	 */
	@Test
	public void sendPingThrowsOnClientTest() throws Exception {
		sendOnClientThrows(OPS.SEND_PING_THROWS);
	}

	/*
	 * @testName: sendPongOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:88;
	 * 
	 * @test_Strategy: Allows the developer to send an unsolicited Pong message
	 * containing the given application data in order to serve as a unidirectional
	 * heartbeat for the session.
	 */
	@Test
	public void sendPongOnServerTest() throws Exception {
		sendOnServer(OPS.SEND_PONG, OPS.SEND_PONG.name(), PongMessageClientEndpoint.class);
	}

	/*
	 * @testName: sendPongOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:88;
	 * 
	 * @test_Strategy: Allows the developer to send an unsolicited Pong message
	 * containing the given application data in order to serve as a unidirectional
	 * heartbeat for the session.
	 */
	@Test
	public void sendPongOnClientTest() throws Exception {
		sendOnClient(OPS.SEND_PONG);
	}

	/*
	 * @testName: sendPongDelaysTimoutOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:88; WebSocket:SPEC:WSC-2.2.5-2;
	 * 
	 * @test_Strategy: Allows the developer to send an unsolicited Pong message
	 * containing the given application data in order to serve as a unidirectional
	 * heartbeat for the session.
	 * 
	 * if the implementation receives a pong message addressed to this endpoint, it
	 * must call that MessageHandler or that annotated message
	 */
	@Test
	public void sendPongDelaysTimoutOnServerTest() throws Exception {
		StringPongMessage ping = new StringPongMessage(OPS.POKE.name());
		setClientEndpoint(PongMessageClientEndpoint.class);
		invoke("server", OPS.IDLE.name(), OPS.IDLE.name(), false);

		TestUtil.sleepMsec(500);
		invokeAgain(ping, OPS.POKE.name(), false);
		TestUtil.sleepMsec(500);
		invokeAgain(ping, OPS.POKE.name(), false);
		TestUtil.sleepMsec(500);
		invokeAgain(ping, OPS.POKE.name(), false);
		TestUtil.sleepMsec(500);

		invokeAgain(OPS.POKE.name(), OPS.POKE.name(), true);
	}

	/*
	 * @testName: sendPongDelaysTimoutOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:88; WebSocket:SPEC:WSC-2.2.5-2;
	 * 
	 * @test_Strategy: Allows the developer to send an unsolicited Pong message
	 * containing the given application data in order to serve as a unidirectional
	 * heartbeat for the session.
	 * 
	 * if the implementation receives a pong message addressed to this endpoint, it
	 * must call that MessageHandler or that annotated message
	 */
	@Test
	public void sendPongDelaysTimoutOnClientTest() throws Exception {
		setClientEndpoint(PongMessageClientEndpoint.class);
		invoke("server", OPS.POKE.name(), OPS.POKE.name(), false);
		getSession().setMaxIdleTimeout(1500L);

		setCountDownLatchCount(5);
		setProperty(Property.CONTENT, OPS.PONG_4_TIMES.name());
		setProperty(Property.SEARCH_STRING, OPS.POKE.name(), OPS.POKE.name(), OPS.POKE.name(), OPS.POKE.name());
		setProperty(Property.SEARCH_STRING_IGNORE_CASE);
		setProperty(Property.SEARCH_STRING_IGNORE_CASE, RESPONSE[0]);
		invokeAgain(true);
	}

	/*
	 * @testName: sendPongThrowsOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:90;
	 * 
	 * @test_Strategy: throws IllegalArgumentException - if the applicationData
	 * exceeds the maximum allowed payload of 125 bytes
	 */
	@Test
	public void sendPongThrowsOnServerTest() throws Exception {
		invoke("server", OPS.SEND_PONG_THROWS, RESPONSE[0]);
	}

	/*
	 * @testName: sendPongThrowsOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:90;
	 * 
	 * @test_Strategy: throws IllegalArgumentException - if the applicationData
	 * exceeds the maximum allowed payload of 125 bytes
	 */
	@Test
	public void sendPongThrowsOnClientTest() throws Exception {
		sendOnClientThrows(OPS.SEND_PONG_THROWS);
	}

	// /////////////////////////////////////////////////////////////////////////
	private String search(Object... ops) {
		return StringUtil.objectsToStringWithDelimiter("|", (Object[]) ops);
	}

	private void sendOnServer(OPS ops) throws Exception {
		sendOnServer(ops, ops);
	}

	private void sendOnServer(OPS ops, Object search) throws Exception {
		sendOnServer(ops, search, BinaryAndTextClientEndpoint.class);
	}

	private void sendOnServer(OPS ops, Object search, Class<? extends ClientEndpoint<?>> endpoint) throws Exception {
		setCountDownLatchCount(3);
		setProperty(Property.CONTENT, ops.name());
		setProperty(Property.REQUEST, buildRequest("server"));
		setProperty(Property.UNORDERED_SEARCH_STRING, search(search, OPS.POKE, RESPONSE[0]));
		PokingEndpointCallback callback = new PokingEndpointCallback(entity);
		setClientCallback(callback);
		setClientEndpoint(endpoint);
		invoke();
	}

	private void sendOnClient(final OPS op) throws Exception {
		sendOnClient(op, op.name());
	}

	private void sendOnClient(final OPS op, String search) throws Exception {
		EndpointCallback callback = new BasicEndpointCallback() {
			@Override
			void doBasic(Basic basicRemote) throws Exception {
				String ret = null;
				String method = null;
				switch (op) {
				case SENDBINARY:
					ret = WSCServerSideServer.sendBinary(basicRemote);
					method = "sendBinary(ByteBuffer)";
					break;
				case SENDBINARYPART1:
					ret = WSCServerSideServer.sendBinaryPartial(basicRemote);
					method = "sendBinary(ByteBuffer, boolean)";
					break;
				case SENDOBJECT:
					ret = WSCServerSideServer.sendObject(basicRemote);
					method = "sendObject(Object)";
					break;
				case SENDOBJECT_BOOL:
					ret = WSCServerSideServer.sendObject(basicRemote, boolean.class);
					method = "sendObject(boolean)";
					break;
				case SENDOBJECT_BYTE:
					ret = WSCServerSideServer.sendObject(basicRemote, byte.class);
					method = "sendObject(byte)";
					break;
				case SENDOBJECT_CHAR:
					ret = WSCServerSideServer.sendObject(basicRemote, char.class);
					method = "sendObject(char)";
					break;
				case SENDOBJECT_DOUBLE:
					ret = WSCServerSideServer.sendObject(basicRemote, double.class);
					method = "sendObject(double)";
					break;
				case SENDOBJECT_FLOAT:
					ret = WSCServerSideServer.sendObject(basicRemote, float.class);
					method = "sendObject(float)";
					break;
				case SENDOBJECT_INT:
					ret = WSCServerSideServer.sendObject(basicRemote, int.class);
					method = "sendObject(int)";
					break;
				case SENDOBJECT_LONG:
					ret = WSCServerSideServer.sendObject(basicRemote, long.class);
					method = "sendObject(long)";
					break;
				case SENDOBJECT_SHORT:
					ret = WSCServerSideServer.sendObject(basicRemote, short.class);
					method = "sendObject(short)";
					break;
				case SENDTEXT:
					ret = WSCServerSideServer.sendText(basicRemote);
					method = "sendText(String)";
					break;
				case SENDTEXTPART1:
					ret = WSCServerSideServer.sendTextPartial(basicRemote);
					method = "sendText(String, boolean)";
					break;
				case SENDSTREAM:
					ret = WSCServerSideServer.getSendStream(basicRemote);
					method = "getSendStream()";
					break;
				case SENDWRITER:
					ret = WSCServerSideServer.getSendWriter(basicRemote);
					method = "getSendWriter()";
					break;
				case BATCHING_ALLOWED:
					ret = WSCServerSideServer.batchingAllowed(basicRemote);
					method = "setBatchingAllowed(!getBatchingAllowed)";
					break;
				case SEND_PING:
					ret = WSCServerSideServer.sendPing(basicRemote);
					method = "sendPing(ByteBuffer)";
					break;
				case SEND_PONG:
					ret = WSCServerSideServer.sendPong(basicRemote);
					method = "sendPong(ByteBuffer)";
					break;
				default:
					new Exception("Method " + op + " not implemented");
				}
				assertEquals(RESPONSE[0], ret, method, "did not endup as expected");
				logMsg(method, "works as expected");
			}
		};
		setClientCallback(callback);

		// Add StringBean encoder just for sendObject methods
		List<Class<? extends Encoder>> list = new LinkedList<>();
		list.add(StringBeanTextEncoder.class);
		ClientEndpointConfig config = ClientEndpointConfig.Builder.create().encoders(list).build();
		setClientEndpointConfig(config);

		invoke("client", "anything", search);
	}

	private void sendOnClientThrows(final OPS op) throws Exception {
		EndpointCallback callback = new BasicEndpointCallback() {
			@Override
			void doBasic(Basic basicRemote) throws Exception {
				String ret = null;
				String method = null;
				switch (op) {
				case SENDBINARYTHROWS:
					ret = WSCServerSideServer.sendBinaryThrows(basicRemote);
					method = "sendBinary(ByteBuffer)";
					break;
				case SENDOBJECTTHROWS:
					ret = WSCServerSideServer.sendObjectThrows(basicRemote);
					method = "sendObject(Object)";
					break;
				case SENDTEXTTHROWS:
					ret = WSCServerSideServer.sendTextThrows(basicRemote);
					method = "sendText(String)";
					break;
				case SEND_PING_THROWS:
					ret = WSCServerSideServer.sendPingThrows(basicRemote);
					method = "sendPing(<too_long_message>)";
					break;
				case SEND_PONG_THROWS:
					ret = WSCServerSideServer.sendPongThrows(basicRemote);
					method = "sendPong(<too_long_message>)";
					break;
				default:
					new Exception("Method " + op + " not implemented");
				}
				assertEquals(RESPONSE[0], ret, method, "does not throw IllegalArgumentException as expected");
				logMsg(method, "throws IllegalArgumentException as expected");
				try {
					basicRemote.sendText(entity.getEntityAt(String.class, 0));
				} catch (IOException e) {
					new Exception(e);
				}
			}
		};
		setClientCallback(callback);
		invoke("client", ECHO, ECHO);
	}

	private void sendOnClientThrowsException(final OPS op, final String exception) throws Exception {
		EndpointCallback callback = new BasicEndpointCallback() {
			@Override
			void doBasic(Basic basicRemote) throws Exception {
				String ret = null;
				String method = null;
				switch (op) {
				case SENDOBJECTTHROWSENCODEEEXCEPTION:
					ret = WSCServerSideServer.sendObjectThrowsEncodeException(basicRemote);
					method = "sendObject(Object)";
					break;
				default:
					new Exception("Method " + op + " not implemented");
				}
				assertEquals(RESPONSE[0], ret, method, "does not throw", exception, "as expected");
				logMsg(method, "throws", exception, "as expected");
				try {
					basicRemote.sendText(entity.getEntityAt(String.class, 0));
				} catch (IOException e) {
					new Exception(e);
				}
			}
		};
		// Add ThrowingEncoder encoder just for sendObject methods
		List<Class<? extends Encoder>> list = new LinkedList<>();
		list.add(ThrowingEncoder.class);
		ClientEndpointConfig config = ClientEndpointConfig.Builder.create().encoders(list).build();
		setClientEndpointConfig(config);
		setClientCallback(callback);
		invoke("client", ECHO, ECHO);
	}
}
