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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.server.serverendpointconfig.configurator;

import java.io.IOException;

import com.sun.ts.tests.websocket.common.util.IOUtil;

import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;

@ServerEndpoint(value = "/modifyhandshake", configurator = ModifyHandshakeConfigurator.class)
public class WSCModifyHandshakeServer {

	ServerEndpointConfig config;

	@OnMessage
	public String onMessage(String msg) {
		boolean ret = false;
		if (msg.equals("origin"))
			ret = ModifyHandshakeConfigurator.isCheckedOriginBeforeModifyHandshake();
		else if (msg.equals("config"))
			ret = ModifyHandshakeConfigurator.getConfig().getClass().getName().equals(config.getClass().getName());
		else if (msg.equals("request"))
			ret = ModifyHandshakeConfigurator.getRequest() != null;
		else if (msg.equals("response"))
			ret = ModifyHandshakeConfigurator.getResponse() != null;
		return String.valueOf(ret);
	}

	@OnOpen
	public void onOpen(EndpointConfig config) {
		this.config = (ServerEndpointConfig) config;
	}

	@OnError
	public void onError(Session session, Throwable thr) throws IOException {
		thr.printStackTrace(); // Write to error log, too
		String message = IOUtil.printStackTrace(thr);
		session.getBasicRemote().sendText(message);
	}

}
