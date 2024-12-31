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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.clientendpointonmessage;

import java.io.IOException;

import com.sun.ts.tests.websocket.common.client.AnnotatedStringClientEndpoint;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;

@ClientEndpoint
public class WSByteArrayPartialClientEndpoint extends AnnotatedStringClientEndpoint {

	protected Session session;

	private StringBuffer sb = new StringBuffer();

	@OnMessage
	public void bytesToString(byte[] array, boolean finito) throws IOException {
		sb.append(new String(array)).append("(").append(finito).append(")");
		if (finito) {
			super.onMessage(sb.toString());
			session.getBasicRemote().sendText(sb.toString().replaceAll("[(][a-z]*[)]", ""));
			sb = new StringBuffer();
		}
	}

	@OnError
	@Override
	public void onError(Session session, Throwable t) {
		super.onError(session, t);
	}

	@OnClose
	@Override
	public void onClose(Session session, CloseReason closeReason) {
		super.onClose(session, closeReason);
	}

	@OnOpen
	@Override
	public void onOpen(Session session, EndpointConfig config) {
		this.session = session;
		super.onOpen(session, config);
	}
}
