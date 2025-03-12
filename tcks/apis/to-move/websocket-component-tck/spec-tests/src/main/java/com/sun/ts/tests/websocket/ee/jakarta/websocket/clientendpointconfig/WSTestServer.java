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
 * $Id:$
 */
package com.sun.ts.tests.websocket.ee.jakarta.websocket.clientendpointconfig;

import java.io.IOException;
import java.lang.System.Logger;
import java.nio.ByteBuffer;
import java.util.List;

import com.sun.ts.tests.websocket.common.util.IOUtil;

import jakarta.websocket.Decoder;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Extension;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/TCKTestServer")
public class WSTestServer {

	private static final Logger logger = System.getLogger(WSTestServer.class.getName());

	static EndpointConfig config;

	@OnOpen
	public void init(Session session, EndpointConfig configs) throws IOException {
		logger.log(Logger.Level.INFO,"========TCKTestServer opened");
		config = configs;
		session.getBasicRemote().sendText("========TCKTestServer opened");
		if (session.isOpen()) {
			session.getBasicRemote().sendText("========session from Server is open=TRUE");
		} else {
			session.getBasicRemote().sendText("========session from Server is open=FALSE");
		}
	}

	@OnMessage
	public void respondString(String message, Session session) {
		logger.log(Logger.Level.INFO,"TCKTestServer got String message: " + message);
		try {
			session.getBasicRemote().sendText("========TCKTestServer received String:" + message + "|");

			String subprotocol = session.getNegotiatedSubprotocol();
			session.getBasicRemote().sendText("========TCKTestServer: subProtocol==" + subprotocol + "|");

			List<Extension> extensions = session.getNegotiatedExtensions();

			session.getBasicRemote().sendText("========TCKTestServer: Extension size=" + extensions.size() + "|");
			for (Extension extension : extensions) {
				session.getBasicRemote().sendText("========TCKTestServer: extensions " + extension.getName() + "|");
			}

			List<Class<? extends Encoder>> encoders = config.getEncoders();
			int size = encoders.size();
			session.getBasicRemote()
					.sendText("========TCKTestServer: getEncoders() returned encoders size=" + size + "|");
			for (Class<? extends Encoder> encoder : encoders) {
				session.getBasicRemote().sendText(encoder + "|");
			}

			List<Class<? extends Decoder>> decoders = config.getDecoders();
			size = decoders.size();
			session.getBasicRemote().sendText(
					"========TCKTestServer: EndpointConfig.getDecoders() returned decoders size=" + size + "|");
			for (Class<? extends Decoder> decoder : decoders) {
				session.getBasicRemote().sendText(decoder + "|");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnMessage
	public void respondByte(ByteBuffer message, Session session) {
		String message_string = IOUtil.byteBufferToString(message);

		logger.log(Logger.Level.INFO,"TCKTestServer got ByteBuffer message: " + message_string);

		ByteBuffer data = ByteBuffer.wrap(("========TCKTestServer received ByteBuffer: ").getBytes());
		ByteBuffer data1 = ByteBuffer.wrap(("========TCKTestServer responds: Message in bytes").getBytes());

		try {
			session.getBasicRemote().sendBinary(data);
			session.getBasicRemote().sendBinary(message);
			session.getBasicRemote().sendBinary(data1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnError
	public void onError(Session session, Throwable t) {
		logger.log(Logger.Level.INFO,"TCKTestServer onError");
		try {
			session.getBasicRemote().sendText("========TCKTestServer onError");
			if (session.isOpen()) {
				session.getBasicRemote().sendText("========onError: session from Server is open=TRUE");
			} else {
				session.getBasicRemote().sendText("========onError: session from Server is open=FALSE");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		t.printStackTrace();
	}

	@OnClose
	public void onClose() {
		logger.log(Logger.Level.INFO,"==From onClose==");
	}
}
