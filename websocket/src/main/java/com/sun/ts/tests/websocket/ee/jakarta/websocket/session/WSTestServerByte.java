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
package com.sun.ts.tests.websocket.ee.jakarta.websocket.session;

import java.io.IOException;
import java.lang.System.Logger;
import java.nio.ByteBuffer;

import com.sun.ts.tests.websocket.common.util.IOUtil;

import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/TCKTestServerByte")
public class WSTestServerByte {

	private static final Logger logger = System.getLogger(WSTestServerByte.class.getName());

	@OnOpen
	public void init(Session session) throws IOException {
		String message = "========TCKTestServerByte opened";
		ByteBuffer data = ByteBuffer.allocate(message.getBytes().length);
		data.put(message.getBytes());
		data.flip();
		session.getBasicRemote().sendBinary(data);
	}

	@OnMessage
	public void respond(ByteBuffer message, Session session) {
		String message_string = IOUtil.byteBufferToString(message);

		logger.log(Logger.Level.INFO,"TCKTestServerByte got ByteBuffer message: " + message_string + " from session " + session);
		try {
			ByteBuffer data = ByteBuffer.allocate(("========TCKTestServerByte received ByteBuffer: "
					+ "========TCKTestServerByte responds: Message in bytes").getBytes().length + message.capacity());
			data.put(("========TCKTestServerByte received ByteBuffer: ").getBytes());
			data.put(message_string.getBytes());
			data.put(("========TCKTestServerByte responds: Message in bytes").getBytes());
			data.flip();
			session.getBasicRemote().sendBinary(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@OnError
	public void onError(Session session, Throwable t) {
		logger.log(Logger.Level.INFO,"TCKTestServerByte onError");
		try {
			session.getBasicRemote().sendText("========TCKTestServerByte onError");
		} catch (Exception e) {
			e.printStackTrace();
		}
		t.printStackTrace();
	}
}
