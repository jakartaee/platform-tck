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
package com.sun.ts.tests.websocket.spec.servercontainer.addendpoint;

import java.io.IOException;
import java.lang.System.Logger;
import java.util.Map;
import java.util.Set;

import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/TCKTestServerPathParam/{param1}/{param2}")
public class WSTestServerPathParam {

	private static final Logger logger = System.getLogger(WSTestServerPathParam.class.getName());

	@OnOpen
	public void init(Session session) throws IOException {
		String message = "========WSTestServerPathParam opened";
		session.getBasicRemote().sendText(message);
	}

	@OnMessage
	public void respond(String message, Session session) {
		logger.log(Logger.Level.INFO,"========WSTestServerPathParam received String:" + message);
		StringBuffer sb = new StringBuffer();

		try {
			session.getBasicRemote().sendText("========WSTestServerPathParam received String: " + message);
			Map<String, String> pathparams = session.getPathParameters();
			Set<String> keys = pathparams.keySet();
			for (Object key : keys) {
				logger.log(Logger.Level.INFO,";" + key.toString() + "=" + pathparams.get(key.toString()));
				sb.append(";" + key.toString() + "=" + pathparams.get(key.toString()));
			}
			session.getBasicRemote().sendText("========WSTestServerPathParam: pathparams returned" + sb.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@OnError
	public void onError(Session session, Throwable t) {
		logger.log(Logger.Level.INFO,"WSTestServerPathParam onError");
		try {
			session.getBasicRemote().sendText("========WSTestServerPathParam onError");
		} catch (Exception e) {
			e.printStackTrace();
		}
		t.printStackTrace();
	}
}
