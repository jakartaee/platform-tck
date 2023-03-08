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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.endpoint.server;

import java.io.IOException;

import com.sun.ts.tests.websocket.common.util.IOUtil;

import jakarta.websocket.CloseReason;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.OnClose;
import jakarta.websocket.Session;

public class WSCErrorServerEndpoint extends Endpoint implements MessageHandler.Whole<String> {
	static final String EXCEPTION = "TCK test throwable";

	private Session session;

	@Override
	public void onMessage(String msg) {
		session.getAsyncRemote().sendText(msg);
		throw new RuntimeException(EXCEPTION);
	}

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		session.addMessageHandler(this);
		this.session = session;
	}

	@Override
	@OnClose
	public void onClose(Session session, CloseReason closeReason) {
		super.onClose(session, closeReason);
	}

	@Override
	public void onError(Session session, Throwable t) {
		String msg = getCauseMessage(t);
		if (EXCEPTION.equals(msg)) {
			WSCMsgServer.setLastMessage(EXCEPTION);
		} else {
			t.printStackTrace(); // Write to error log, too
			String message = IOUtil.printStackTrace(t);
			try {
				session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static String getCauseMessage(Throwable t) {
		String msg = null;
		while (t != null) {
			msg = t.getMessage();
			t = t.getCause();
		}
		return msg;
	}

}
