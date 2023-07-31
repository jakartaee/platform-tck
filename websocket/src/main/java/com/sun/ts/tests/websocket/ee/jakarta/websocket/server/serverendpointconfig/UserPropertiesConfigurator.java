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

import java.util.Map;

import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import jakarta.websocket.server.ServerEndpointConfig.Configurator;

public class UserPropertiesConfigurator extends Configurator {

	public static final String KEY_3 = "UPC-1";
	public static final String KEY_4 = "UPC-2";

	@Override
	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
		Map<String, Object> userProperties = sec.getUserProperties();

		// First check that the expected properties are present
		if (userProperties.size() != 2) {
			throw new IllegalStateException(
					"User properties map has [" + userProperties.size() + "] entries when 2 are expected");
		}

		// Then check that both expected keys are present
		checkKey(userProperties, UserPropertiesServerEndpointConfig.KEY_1);
		checkKey(userProperties, UserPropertiesServerEndpointConfig.KEY_2);

		// Now remove key 2 and and two keys of our own
		userProperties.remove(UserPropertiesServerEndpointConfig.KEY_2);
		userProperties.put(KEY_3, new Object());
		userProperties.put(KEY_4, new Object());
	}

	private void checkKey(Map<String, Object> map, String key) {
		if (!map.containsKey(key)) {
			throw new IllegalStateException("User properties map is missing entry with key [" + key + "]");
		}
	}
}
