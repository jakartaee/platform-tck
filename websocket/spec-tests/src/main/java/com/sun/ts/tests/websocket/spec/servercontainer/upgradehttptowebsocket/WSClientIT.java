/*
 * Copyright (c) 2021, 2023 Contributors to the Eclipse Foundation.
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
package com.sun.ts.tests.websocket.spec.servercontainer.upgradehttptowebsocket;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;

import jakarta.websocket.ClientEndpointConfig;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

@ExtendWith(ArquillianExtension.class)
public class WSClientIT extends WebSocketCommonClient {

	private static final long serialVersionUID = 1L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class, "ws_spec_upgradehttp_web.war");
		archive.addPackages(false, Filters.exclude(WSClientIT.class),
				"com.sun.ts.tests.websocket.spec.servercontainer.upgradehttptowebsocket");
		return archive;
	};

	private static final String MESSAGE_TEXT = "TCK upgrade test message";

	public WSClientIT() throws Exception {
		setContextRoot("ws_spec_upgradehttp_web");
	}

	/*
	 * @class.setup_props: webServerHost; webServerPort; ws_wait;
	 */
	/* Run test */

	/*
	 * @testName: upgradeHttpToWebSocketTest
	 * 
	 * @assertion_ids:
	 *
	 * @test_Strategy: Make a WebSocket client connection request to the path mapped
	 * to WSTestServlet. Confirm that a WebSocket connection is established. Send a
	 * message. Confirm the message is echoed back.
	 */
	@Test
	public void upgradeHttpToWebSocketTest() throws Exception {
		boolean passed = false;

		try {
			WebSocketContainer clientContainer = ContainerProvider.getWebSocketContainer();
			ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

			CountDownLatch messageLatch = new CountDownLatch(1);
			StringBuffer receivedMessageString = new StringBuffer();

			config.getUserProperties().put("messageLatch", messageLatch);
			config.getUserProperties().put("receivedMessageString", receivedMessageString);

			Session session = clientContainer.connectToServer(TCKUpgradeEndpoint.class, config,
					new URI("ws://" + _hostname + ":" + _port + "/" + getContextRoot() + "/TCKTestServlet"));

			session.getBasicRemote().sendText(MESSAGE_TEXT);
			messageLatch.await(_ws_wait, TimeUnit.SECONDS);

			session.close();

			if (!receivedMessageString.toString().equals(MESSAGE_TEXT)) {
				throw new Exception("Received: [" + receivedMessageString.toString() + "] but should have been ["
						+ MESSAGE_TEXT + "]");
			}

			passed = true;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
		if (!passed) {
			throw new Exception("Test failed with incorrect response");
		}
	}

	public final static class TCKUpgradeEndpoint extends Endpoint {

		private CountDownLatch messageLatch;
		private StringBuffer receivedMessageString;

		@Override
		public void onOpen(Session session, EndpointConfig config) {

			messageLatch = (CountDownLatch) config.getUserProperties().get("messageLatch");
			receivedMessageString = (StringBuffer) config.getUserProperties().get("receivedMessageString");

			session.addMessageHandler(new MessageHandler.Whole<String>() {

				@Override
				public void onMessage(String message) {
					receivedMessageString.append(message);
					messageLatch.countDown();
				}
			});
		}
	}
}
