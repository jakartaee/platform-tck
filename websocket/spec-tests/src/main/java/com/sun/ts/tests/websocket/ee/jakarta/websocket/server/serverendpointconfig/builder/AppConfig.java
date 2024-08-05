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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.server.serverendpointconfig.builder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryDecoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryEncoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryStreamDecoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryStreamEncoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextDecoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextEncoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextStreamDecoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextStreamEncoder;
import com.sun.ts.tests.websocket.ee.jakarta.websocket.server.serverendpointconfig.ServerEndpointConfigConfigurator;

import jakarta.websocket.Decoder;
import jakarta.websocket.Encoder;
import jakarta.websocket.Endpoint;
import jakarta.websocket.Extension;
import jakarta.websocket.server.ServerApplicationConfig;
import jakarta.websocket.server.ServerEndpointConfig;

public class AppConfig implements ServerApplicationConfig {
	static final String[] EXT_NAMES = new String[] { "empty", "secondEmpty" };

	public static final ServerEndpointConfig.Configurator CONFIG = new ServerEndpointConfig.Configurator() {
	};

	ServerEndpointConfig getSubprotocolsConfig() {
		ServerEndpointConfig config = ServerEndpointConfig.Builder.create(WSCommonServer.class, "/builder/subprotocols")
				.subprotocols(Arrays.asList("abc", "def")).configurator(CONFIG).build();
		return config;
	}

	ServerEndpointConfig getConfiguratorConfig() {
		ServerEndpointConfig config = ServerEndpointConfig.Builder.create(WSCommonServer.class, "/builder/configurator")
				.configurator(new ServerEndpointConfigConfigurator()).build();
		return config;
	}

	ServerEndpointConfig getExtensionsConfig() {
		Extension firstExt = new Extension() {
			@Override
			public String getName() {
				return EXT_NAMES[0];
			}

			@Override
			public List<Parameter> getParameters() {
				return Collections.emptyList();
			}
		};

		Extension secondExt = new Extension() {
			@Override
			public String getName() {
				return EXT_NAMES[1];
			}

			@Override
			public List<Parameter> getParameters() {
				return Collections.emptyList();
			}
		};
		ServerEndpointConfig config = ServerEndpointConfig.Builder.create(WSCommonServer.class, "/builder/extensions")
				.configurator(CONFIG).extensions(Arrays.asList(firstExt, secondExt)).build();
		return config;
	}

	ServerEndpointConfig getDecodersConfig() {
		List<Class<? extends Decoder>> decoders = Arrays.asList(StringBeanBinaryStreamDecoder.class,
				StringBeanTextDecoder.class, StringBeanBinaryDecoder.class, StringBeanTextStreamDecoder.class);
		ServerEndpointConfig config = ServerEndpointConfig.Builder.create(WSCommonServer.class, "/builder/decoders")
				.decoders(decoders).configurator(CONFIG).build();
		return config;
	}

	ServerEndpointConfig getEncodersConfig() {
		List<Class<? extends Encoder>> encoders = Arrays.asList(StringBeanTextStreamEncoder.class,
				StringBeanBinaryEncoder.class, StringBeanTextEncoder.class, StringBeanBinaryStreamEncoder.class);
		ServerEndpointConfig config = ServerEndpointConfig.Builder.create(WSCommonServer.class, "/builder/encoders")
				.encoders(encoders).configurator(CONFIG).build();
		return config;
	}

	@Override
	public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses) {
		Set<ServerEndpointConfig> set = new HashSet<>();
		set.add(getSubprotocolsConfig());
		set.add(getConfiguratorConfig());
		set.add(getExtensionsConfig());
		set.add(getDecodersConfig());
		set.add(getEncodersConfig());
		return set;
	}

	@Override
	public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
		Set<Class<?>> set = new HashSet<>();
		return set;
	}
}
