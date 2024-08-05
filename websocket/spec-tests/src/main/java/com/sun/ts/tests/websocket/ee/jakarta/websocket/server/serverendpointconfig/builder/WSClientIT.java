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

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.impl.ExtensionImpl;
import com.sun.ts.tests.websocket.common.impl.ExtensionParameterImpl;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryDecoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryEncoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryStreamDecoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryStreamEncoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextDecoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextEncoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextStreamDecoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextStreamEncoder;
import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.common.util.StringUtil;
import com.sun.ts.tests.websocket.ee.jakarta.websocket.server.serverendpointconfig.ServerEndpointConfigConfigurator;
import com.sun.ts.tests.websocket.ee.jakarta.websocket.server.serverendpointconfig.configurator.WSCClientIT;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ws_wait;
 */
@ExtendWith(ArquillianExtension.class)
public class WSClientIT extends WebSocketCommonClient {

	private static final long serialVersionUID = 1217261514689339373L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class,
				"wsc_ee_jakarta_websocket_server_serverendpointconfig_builder_web.war");
		archive.addPackages(true, Filters.exclude(WSCClientIT.class),
				"com.sun.ts.tests.websocket.ee.jakarta.websocket.server.serverendpointconfig.builder");
		archive.addPackages(true, "com.sun.ts.tests.websocket.ee.jakarta.websocket.server.serverendpointconfig");
		archive.addPackages(true, "com.sun.ts.tests.websocket.common.stringbean");
		archive.addClasses(ExtensionImpl.class, ExtensionParameterImpl.class);
		archive.addClasses(IOUtil.class, StringUtil.class);
		return archive;
	};

	public WSClientIT() throws Exception {
		setContextRoot("wsc_ee_jakarta_websocket_server_serverendpointconfig_builder_web");

	}

	static String[] endpoints = new String[] { "builder/configurator", "builder/decoders", "builder/encoders",
			"builder/extensions", "builder/subprotocols" };

	/* Run test */

	/*
	 * @testName: getConfiguratorTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:199; WebSocket:JAVADOC:198;
	 * WebSocket:JAVADOC:200; WebSocket:JAVADOC:193; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79;
	 * 
	 * @test_Strategy:
	 * jakarta.websocket.server.ServerEndpointConfig.Builder.configurator
	 * jakarta.websocket.server.ServerEndpointConfig.Builder.build
	 * jakarta.websocket.server.ServerEndpointConfig.Builder.create
	 * jakarta.websocket.server.ServerEndpointConfig.getConfigurator
	 * jakarta.websocket.Endpoint.onOpen;
	 * jakarta.websocket.MessageHandler.Whole.onMessage
	 */
	@Test
	public void getConfiguratorTest() throws Exception {
		String name = AppConfig.CONFIG.getClass().getName();
		String[] responses = new String[] { ServerEndpointConfigConfigurator.class.getName(), name, name, name, name };
		for (int i = 0; i != endpoints.length; i++) {
			setProperty(Property.REQUEST, buildRequest(endpoints[i]));
			setProperty(Property.CONTENT, "configurator");
			setProperty(Property.SEARCH_STRING, responses[i]);
			invoke();
		}
	}

	/*
	 * @testName: getExtensionsTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:203; WebSocket:JAVADOC:198;
	 * WebSocket:JAVADOC:200; WebSocket:JAVADOC:195; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79;
	 * 
	 * @test_Strategy:
	 * jakarta.websocket.server.ServerEndpointConfig.Builder.extensions
	 * jakarta.websocket.server.ServerEndpointConfig.Builder.build
	 * jakarta.websocket.server.ServerEndpointConfig.Builder.create
	 * jakarta.websocket.server.ServerEndpointConfig.getExtensions
	 * jakarta.websocket.Endpoint.onOpen;
	 * jakarta.websocket.MessageHandler.Whole.onMessage
	 */
	@Test
	public void getExtensionsTest() throws Exception {
		String[] responses = new String[] { "[]", "[]", "[]", AppConfig.EXT_NAMES[0] + "|" + AppConfig.EXT_NAMES[1],
				"[]" };
		for (int i = 0; i != endpoints.length; i++) {
			setProperty(Property.REQUEST, buildRequest(endpoints[i]));
			setProperty(Property.CONTENT, "extensions");
			setProperty(Property.UNORDERED_SEARCH_STRING, responses[i]);
			invoke();
		}
	}

	/*
	 * @testName: getPathTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:196; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79;
	 * 
	 * @test_Strategy: jakarta.websocket.server.ServerEndpointConfig.getPath
	 * jakarta.websocket.Endpoint.onOpen;
	 * jakarta.websocket.MessageHandler.Whole.onMessage
	 */
	@Test
	public void getPathTest() throws Exception {
		for (String endpoint : endpoints) {
			setProperty(Property.REQUEST, buildRequest(endpoint));
			setProperty(Property.CONTENT, "path");
			setProperty(Property.SEARCH_STRING, endpoint);
			invoke();
		}
	}

	/*
	 * @testName: getSubprotocolsTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:204; WebSocket:JAVADOC:198;
	 * WebSocket:JAVADOC:200; WebSocket:JAVADOC:197; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79;
	 * 
	 * @test_Strategy:
	 * jakarta.websocket.server.ServerEndpointConfig.Builder.subprotocols
	 * jakarta.websocket.server.ServerEndpointConfig.Builder.build
	 * jakarta.websocket.server.ServerEndpointConfig.Builder.create
	 * jakarta.websocket.server.ServerEndpointConfig.getSubprotocols Return the
	 * websocket subprotocols configured. jakarta.websocket.Endpoint.onOpen;
	 * jakarta.websocket.MessageHandler.Whole.onMessage
	 */
	@Test
	public void getSubprotocolsTest() throws Exception {
		setProperty(Property.REQUEST, buildRequest(endpoints[endpoints.length - 1]));
		setProperty(Property.CONTENT, "subprotocols");
		setProperty(Property.UNORDERED_SEARCH_STRING, "abc");
		setProperty(Property.UNORDERED_SEARCH_STRING, "def");
		invoke();
	}

	/*
	 * @testName: getEmptySubprotocolsTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:197; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79;
	 * 
	 * @test_Strategy: jakarta.websocket.server.ServerEndpointConfig.getSubprotocols
	 * Return the websocket subprotocols configured.
	 * jakarta.websocket.Endpoint.onOpen;
	 * jakarta.websocket.MessageHandler.Whole.onMessage
	 */
	@Test
	public void getEmptySubprotocolsTest() throws Exception {
		for (int i = 0; i != endpoints.length - 1; i++) {
			setProperty(Property.REQUEST, buildRequest(endpoints[i]));
			setProperty(Property.CONTENT, "subprotocols");
			invoke(false);
			String response = getResponseAsString();
			assertEqualsInt(0, response.replace("[", "").replace("]", "").trim().length(),
					"Unexpected subprotocol list received", response);
			cleanup();
		}
	}

	/*
	 * @testName: getEncodersTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:202; WebSocket:JAVADOC:200;
	 * WebSocket:JAVADOC:198;
	 * 
	 * @test_Strategy:
	 * jakarta.websocket.server.ServerEndpointConfig.Builder.encoders
	 * jakarta.websocket.server.ServerEndpointConfig.Builder.create
	 * jakarta.websocket.server.ServerEndpointConfig.Builder.build
	 */
	@Test
	public void getEncodersTest() throws Exception {
		setProperty(Property.REQUEST, buildRequest("builder/encoders"));
		setProperty(Property.CONTENT, "encoders");
		setProperty(Property.UNORDERED_SEARCH_STRING, StringBeanBinaryEncoder.class.getName());
		setProperty(Property.UNORDERED_SEARCH_STRING, StringBeanBinaryStreamEncoder.class.getName());
		setProperty(Property.UNORDERED_SEARCH_STRING, StringBeanTextEncoder.class.getName());
		setProperty(Property.UNORDERED_SEARCH_STRING, StringBeanTextStreamEncoder.class.getName());
		invoke();
	}

	/*
	 * @testName: getDecodersTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:201; WebSocket:JAVADOC:200;
	 * WebSocket:JAVADOC:198;
	 * 
	 * @test_Strategy:
	 * jakarta.websocket.server.ServerEndpointConfig.Builder.decoders
	 * jakarta.websocket.server.ServerEndpointConfig.Builder.create
	 * jakarta.websocket.server.ServerEndpointConfig.Builder.build
	 */
	@Test
	public void getDecodersTest() throws Exception {
		setProperty(Property.REQUEST, buildRequest("builder/decoders"));
		setProperty(Property.CONTENT, "decoders");
		setProperty(Property.UNORDERED_SEARCH_STRING, StringBeanBinaryDecoder.class.getName());
		setProperty(Property.UNORDERED_SEARCH_STRING, StringBeanBinaryStreamDecoder.class.getName());
		setProperty(Property.UNORDERED_SEARCH_STRING, StringBeanTextDecoder.class.getName());
		setProperty(Property.UNORDERED_SEARCH_STRING, StringBeanTextStreamDecoder.class.getName());
		invoke();
	}

	/*
	 * @testName: getDefaultEncodersTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:202;
	 * 
	 * @test_Strategy: Default: {}
	 * jakarta.websocket.server.ServerEndpointConfig.Builder.encoders
	 */
	@Test
	public void getDefaultEncodersTest() throws Exception {
		for (int i = 0; i != endpoints.length; i++)
			if (i != 2) {
				setProperty(Property.REQUEST, buildRequest(endpoints[i]));
				setProperty(Property.CONTENT, "encoders");
				setProperty(Property.SEARCH_STRING, "{}");
				invoke();
			}
	}

	/*
	 * @testName: getDefaultDecodersTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:201;
	 * 
	 * @test_Strategy: Default: {}
	 * jakarta.websocket.server.ServerEndpointConfig.Builder.decoders
	 */
	@Test
	public void getDefaultDecodersTest() throws Exception {
		for (int i = 0; i != endpoints.length; i++)
			if (i != 1) {
				setProperty(Property.REQUEST, buildRequest(endpoints[i]));
				setProperty(Property.CONTENT, "decoders");
				setProperty(Property.SEARCH_STRING, "{}");
				invoke();
			}
	}
}
