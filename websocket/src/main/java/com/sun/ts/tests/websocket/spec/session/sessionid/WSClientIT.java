/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates and others.
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
package com.sun.ts.tests.websocket.spec.session.sessionid;

import java.io.IOException;
import java.lang.System.Logger;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.util.IOUtil;

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

	private static final long serialVersionUID = 10L;

	private static final Logger logger = System.getLogger(WSClientIT.class.getName());

	private static final String CONTEXT_ROOT = "/ws_spec_sessionid_web";

	private static StringBuffer receivedMessageString = new StringBuffer();

	static CountDownLatch messageLatch, onCloseLatch;

	static volatile String session_id, session_id_endpoint_onOpen, session_id_endpoint_onClose;

	static volatile Session session, session_endpoint_onOpen, session_endpoint_onClose;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class, "ws_spec_sessionid_web.war");
		archive.addClasses(WSTestServer.class);
		archive.addClasses(IOUtil.class);
		archive.addAsWebInfResource(WSClientIT.class.getPackage(), "web.xml", "web.xml");

		return archive;
	};

	public WSClientIT() throws Exception {
		setContextRoot("ws_spec_sessionid_web");
	}

	/*
	 * @class.setup_props: webServerHost; webServerPort; ws_wait; ts_home;
	 */
	/* Run test */

	/*
	 * @testName: getIdTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:162; WebSocket:JAVADOC:67;
	 * WebSocket:JAVADOC:69; WebSocket:JAVADOC:130; WebSocket:SPEC:WSC-2.1.2-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void getIdTest() throws Exception {
		boolean passed = true;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(1);
			session = clientContainer.connectToServer(
					com.sun.ts.tests.websocket.spec.session.sessionid.WSClientIT.TCKGetIdEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			session_id = session.getId();

			onCloseLatch = new CountDownLatch(1);
			session.close();
			onCloseLatch.await(_ws_wait, TimeUnit.SECONDS);

			if (session_id != session_id_endpoint_onOpen || session_id != session_id_endpoint_onClose) {
				passed = false;
				System.out.print("Session IDs are not the same.");
			}
			logger.log(Logger.Level.INFO,"session_id                 =" + session_id);
			logger.log(Logger.Level.INFO,"session_id_endpoint_onClose=" + session_id_endpoint_onClose);
			logger.log(Logger.Level.INFO,"session_id_endpoint_onOpen =" + session_id_endpoint_onOpen);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: instanceTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:162; WebSocket:JAVADOC:67;
	 * WebSocket:JAVADOC:69; WebSocket:JAVADOC:130; WebSocket:SPEC:WSC-2.1.2-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void instanceTest() throws Exception {
		boolean passed = true;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			messageLatch = new CountDownLatch(1);
			session = clientContainer.connectToServer(
					com.sun.ts.tests.websocket.spec.session.sessionid.WSClientIT.TCKGetIdEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			onCloseLatch = new CountDownLatch(1);
			session.close();
			onCloseLatch.await(_ws_wait, TimeUnit.SECONDS);

			if (session != session_endpoint_onOpen || session != session_endpoint_onClose) {
				passed = false;
				System.out.print("Sessions are not the same.");
			}
			logger.log(Logger.Level.INFO,"session                 =" + session);
			logger.log(Logger.Level.INFO,"session_endpoint_onClose=" + session_endpoint_onClose);
			logger.log(Logger.Level.INFO,"session_endpoint_onOpen =" + session_endpoint_onOpen);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	/*
	 * @testName: unique
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:8;
	 * WebSocket:JAVADOC:10; WebSocket:JAVADOC:162; WebSocket:JAVADOC:67;
	 * WebSocket:JAVADOC:69; WebSocket:JAVADOC:130; WebSocket:SPEC:WSC-2.1.2-1;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void unique() throws Exception {
		int size = 5;
		boolean passed = true;
		Session[] sessions = new Session[size];

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			for (int i = 0; i < size; i++) {
				messageLatch = new CountDownLatch(1);
				session = clientContainer.connectToServer(
						com.sun.ts.tests.websocket.spec.session.sessionid.WSClientIT.TCKGetIdEndpoint.class, config,
						new URI("ws://" + _hostname + ":" + _port + CONTEXT_ROOT + "/TCKTestServer"));
				sessions[i] = session;
				messageLatch.await(_ws_wait, TimeUnit.SECONDS);

				onCloseLatch = new CountDownLatch(1);
				session.close();
				onCloseLatch.await(_ws_wait, TimeUnit.SECONDS);

				logger.log(Logger.Level.INFO,"Session " + i);

				if (session != session_endpoint_onOpen || session != session_endpoint_onClose) {
					passed = false;
					System.out.print("Sessions are not the same.");
				}

				logger.log(Logger.Level.INFO,"session                 =" + session);
				logger.log(Logger.Level.INFO,"session_endpoint_onClose=" + session_endpoint_onClose);
				logger.log(Logger.Level.INFO,"session_endpoint_onOpen =" + session_endpoint_onOpen);
			}

			for (int i = 0; i < size; i++) {
				for (int j = i + 1; j < size; j++) {
					if (sessions[i] == sessions[j]) {
						passed = false;
						logger.log(Logger.Level.INFO,"two sessions are the same: ");
						logger.log(Logger.Level.INFO,"session " + i + " " + sessions[i]);
						logger.log(Logger.Level.INFO,"session " + j + " " + sessions[j]);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	@Override
	public void cleanup() throws Exception {
		super.cleanup();
	}

	public final static class TCKGetIdEndpoint extends Endpoint {

		@Override
		public void onOpen(Session session, EndpointConfig config) {
			session_id_endpoint_onOpen = session.getId();
			session_endpoint_onOpen = session;

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
					String message_string = IOUtil.byteBufferToString(data);

					receivedMessageString.append("========Basic ByteBuffer MessageHander received=" + message_string);
					messageLatch.countDown();
				}
			});
		}

		@Override
		public void onClose(Session session, CloseReason closeReason) {
			session_id_endpoint_onClose = session.getId();
			session_endpoint_onClose = session;

			receivedMessageString.append("CloseCode=" + closeReason.getCloseCode());
			receivedMessageString.append("ReasonPhrase=" + closeReason.getReasonPhrase());
			onCloseLatch.countDown();
		}
	}
}
