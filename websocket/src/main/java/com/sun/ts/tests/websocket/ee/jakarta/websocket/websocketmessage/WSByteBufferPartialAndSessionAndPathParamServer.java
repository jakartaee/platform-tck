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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.websocketmessage;

import java.io.IOException;
import java.lang.System.Logger;
import java.nio.ByteBuffer;



import com.sun.ts.tests.websocket.common.util.IOUtil;

import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/partialbytebuffersessionpathparam/{param}")
public class WSByteBufferPartialAndSessionAndPathParamServer {

	private static final Logger logger = System.getLogger(WSByteBufferPartialAndSessionAndPathParamServer.class.getName());

	StringBuilder sb = new StringBuilder();

	@OnMessage
	public void echo(ByteBuffer buf, Session s, boolean finito, @PathParam("param") String param) throws IOException {
		sb.append(IOUtil.byteBufferToString(buf)).append("(").append(finito);
		sb.append(")").append('[').append(param).append(']');
		if (finito) {
			s.getBasicRemote().sendText(sb.toString());
			sb = new StringBuilder();
		}
	}

	@OnError
	public void onError(Session session, Throwable t) throws IOException {
		logger.log(Logger.Level.INFO,"@OnError in" + getClass().getName());
		t.printStackTrace(); // Write to error log, too
		String message = "Exception: " + IOUtil.printStackTrace(t);
		session.getBasicRemote().sendText(message);
	}

}
