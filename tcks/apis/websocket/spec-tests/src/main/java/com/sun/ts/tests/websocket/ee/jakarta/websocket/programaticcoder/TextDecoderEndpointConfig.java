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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.programaticcoder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.sun.ts.tests.websocket.ee.jakarta.websocket.coder.InitDestroyTextDecoder;

import jakarta.websocket.Decoder;
import jakarta.websocket.Encoder;
import jakarta.websocket.Extension;
import jakarta.websocket.server.ServerEndpointConfig;

public class TextDecoderEndpointConfig implements ServerEndpointConfig {

	@Override
	public Map<String, Object> getUserProperties() {
		return Collections.emptyMap();
	}

	@Override
	public Class<?> getEndpointClass() {
		return WSCTextDecoderServer.class;
	}

	@Override
	public String getPath() {
		return "/textdecoder";
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
		return new ServerEndpointConfig.Configurator() {
		};
	}

	@Override
	public List<Class<? extends Encoder>> getEncoders() {
		return Collections.emptyList();
	}

	@Override
	public List<Class<? extends Decoder>> getDecoders() {
		Class<? extends Decoder> clz = InitDestroyTextDecoder.class;
		List<Class<? extends Decoder>> list = new LinkedList<>();
		list.add(clz);
		return list;
	}

}
