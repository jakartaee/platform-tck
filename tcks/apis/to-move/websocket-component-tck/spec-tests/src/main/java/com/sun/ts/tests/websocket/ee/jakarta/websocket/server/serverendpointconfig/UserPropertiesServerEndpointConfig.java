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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.websocket.Decoder;
import jakarta.websocket.Encoder;
import jakarta.websocket.Extension;
import jakarta.websocket.server.ServerEndpointConfig;

public class UserPropertiesServerEndpointConfig implements ServerEndpointConfig {

	public static final String KEY_1 = "SEC-1";
	public static final String KEY_2 = "SEC-2";

	private static final Map<String, Object> SEC_USER_PROPERTIES = new HashMap<>();

	static {
		SEC_USER_PROPERTIES.put(KEY_1, new Object());
		SEC_USER_PROPERTIES.put(KEY_2, new Object());
	}

	@Override
	public Map<String, Object> getUserProperties() {
		// Need to always return the same object to test that the user properties
		// exposed to modifyHandshake() are a shallow copy
		return SEC_USER_PROPERTIES;
	}

	@Override
	public Class<?> getEndpointClass() {
		return WSProgramaticUserPropertiesServer.class;
	}

	@Override
	public String getPath() {
		return "/programatic/userproperties";
	}

	@Override
	public List<String> getSubprotocols() {
		return Collections.emptyList();
	}

	@Override
	public List<Extension> getExtensions() {
		return Collections.emptyList();
	}

	@Override
	public Configurator getConfigurator() {
		return new UserPropertiesConfigurator();
	}

	@Override
	public List<Class<? extends Encoder>> getEncoders() {
		return Collections.emptyList();
	}

	@Override
	public List<Class<? extends Decoder>> getDecoders() {
		return Collections.emptyList();
	}

}
