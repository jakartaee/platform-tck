/*
 * Copyright (c) 2021 Contributors to the Eclipse Foundation.
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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.server.serverendpointconfig;

import java.io.IOException;
import java.util.Map;

import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;

public class WSProgramaticUserPropertiesServer extends Endpoint implements MessageHandler.Whole<String> {

	Session session;

	private static final String KEY_5 = "UPS-5";

	@Override
	public void onMessage(String msg) {
		Map<String, Object> userProperties = session.getUserProperties();

		// First check that the expected properties are present
		if (userProperties.size() != 3) {
			throw new IllegalStateException(
					"User properties map has [" + userProperties.size() + "] entries when 3 are expected");
		}

		// Then check expected keys are present
		checkKey(userProperties, UserPropertiesServerEndpointConfig.KEY_1);
		checkKey(userProperties, UserPropertiesConfigurator.KEY_3);
		checkKey(userProperties, UserPropertiesConfigurator.KEY_4);

		// Now remove key 4 and add one of our own
		userProperties.remove(UserPropertiesConfigurator.KEY_4);
		userProperties.put(KEY_5, new Object());

		try {
			session.getBasicRemote().sendText("PASS");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void checkKey(Map<String, Object> map, String key) {
		if (!map.containsKey(key)) {
			throw new IllegalStateException("User properties map is missing entry with key [" + key + "]");
		}
	}

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		this.session = session;
		session.addMessageHandler(this);
	}

	@Override
	public void onError(Session session, Throwable thr) {
		thr.printStackTrace();
	}
}
