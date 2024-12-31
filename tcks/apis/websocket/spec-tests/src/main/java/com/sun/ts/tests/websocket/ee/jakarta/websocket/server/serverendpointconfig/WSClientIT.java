/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates and others.
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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.server.serverendpointconfig;

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
import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.common.util.StringUtil;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ws_wait;
 */
@ExtendWith(ArquillianExtension.class)
public class WSClientIT extends WebSocketCommonClient {

	private static final long serialVersionUID = 6621336154397058231L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class,
				"wsc_ee_jakarta_websocket_server_serverendpointconfig_web.war");
		archive.addPackages(true, Filters.exclude(WSClientIT.class,
				com.sun.ts.tests.websocket.ee.jakarta.websocket.server.serverendpointconfig.builder.WSClientIT.class,
				com.sun.ts.tests.websocket.ee.jakarta.websocket.server.serverendpointconfig.configurator.WSCClientIT.class),
				"com.sun.ts.tests.websocket.ee.jakarta.websocket.server.serverendpointconfig");
		archive.addPackages(false, "com.sun.ts.tests.websocket.common.stringbean");
		archive.addClasses(ExtensionImpl.class, ExtensionParameterImpl.class);
		archive.addClasses(IOUtil.class, StringUtil.class);
		return archive;
	};

	public WSClientIT() throws Exception {
		setContextRoot("wsc_ee_jakarta_websocket_server_serverendpointconfig_web");

	}

	/* Run test */

	/*
	 * @testName: getConfiguratorTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:193; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79;
	 * 
	 * @test_Strategy: jakarta.websocket.server.ServerEndpointConfig.getConfigurator
	 * jakarta.websocket.Endpoint.onOpen;
	 * jakarta.websocket.MessageHandler.Whole.onMessage
	 */
	@Test
	public void getConfiguratorTest() throws Exception {
		String[] endpoints = new String[] { "programatic/subprotocols", "annotated/configurator",
				"programatic/configurator" };
		String[] responses = new String[] { SubprotocolsServerEndpointConfig.class.getName(),
				ServerEndpointConfigConfigurator.class.getName(), ServerEndpointConfigConfigurator.class.getName() };
		for (int i = 0; i != endpoints.length; i++) {
			setProperty(Property.REQUEST, buildRequest(endpoints[i]));
			setProperty(Property.CONTENT, "configurator");
			setProperty(Property.SEARCH_STRING, responses[i]);
			invoke();
		}
	}

	/*
	 * @testName: getEndpointClassTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:194; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79;
	 * 
	 * @test_Strategy:
	 * jakarta.websocket.server.ServerEndpointConfig.getEndpointClass
	 * jakarta.websocket.Endpoint.onOpen;
	 * jakarta.websocket.MessageHandler.Whole.onMessage
	 */
	@Test
	public void getEndpointClassTest() throws Exception {
		String[] endpoints = new String[] { "programatic/subprotocols", "annotated/subprotocols",
				"annotated/configurator", "programatic/configurator", "programatic/extensions" };
		String[] responses = new String[] { WSProgramaticSubprotocolsServer.class.getName(),
				WSAnnotatedSubprotocolsServer.class.getName(), WSAnnotatedConfiguratorServer.class.getName(),
				WSProgramaticConfiguratorServer.class.getName(), WSProgramaticExtensionsServer.class.getName() };
		for (int i = 0; i != endpoints.length; i++) {
			setProperty(Property.REQUEST, buildRequest(endpoints[i]));
			setProperty(Property.CONTENT, "endpoint");
			setProperty(Property.SEARCH_STRING, responses[i]);
			invoke();
		}
	}

	/*
	 * @testName: getExtensionsTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:195; WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:79;
	 * 
	 * @test_Strategy: jakarta.websocket.server.ServerEndpointConfig.getExtensions
	 * jakarta.websocket.Endpoint.onOpen;
	 * jakarta.websocket.MessageHandler.Whole.onMessage
	 */
	@Test
	public void getExtensionsTest() throws Exception {
		String[] endpoints = new String[] { "programatic/subprotocols", "annotated/subprotocols",
				"annotated/configurator", "programatic/configurator", "programatic/extensions" };
		String[] responses = new String[] { "[]", "[]", "[]", "[]",
				ExtensionsServerEndpointConfig.EXT_NAMES[0] + "|" + ExtensionsServerEndpointConfig.EXT_NAMES[1] };
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
		String[] sequence = new String[] { "programatic/subprotocols", "annotated/subprotocols",
				"annotated/configurator", "programatic/configurator", "programatic/extensions" };
		for (String endpoint : sequence) {
			setProperty(Property.REQUEST, buildRequest(endpoint));
			setProperty(Property.CONTENT, "path");
			setProperty(Property.SEARCH_STRING, endpoint);
			invoke();
		}
	}

	/*
	 * @testName: getSubprotocolsTest
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
	public void getSubprotocolsTest() throws Exception {
		String[] endpoints = new String[] { "programatic/subprotocols", "annotated/subprotocols" };
		for (String endpoint : endpoints) {
			setProperty(Property.REQUEST, buildRequest(endpoint));
			setProperty(Property.CONTENT, "subprotocols");
			setProperty(Property.UNORDERED_SEARCH_STRING, "abc");
			setProperty(Property.UNORDERED_SEARCH_STRING, "def");
			invoke();
		}
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
		String[] endpoints = new String[] { "annotated/configurator", "programatic/configurator",
				"programatic/extensions" };
		for (String endpoint : endpoints) {
			setProperty(Property.REQUEST, buildRequest(endpoint));
			setProperty(Property.CONTENT, "subprotocols");
			invoke(false);
			String response = getResponseAsString();
			assertEqualsInt(0, response.replace("[", "").replace("]", "").trim().length(),
					"Unexpected subprotocol list received", response);
			cleanup();
		}
	}

	/*
	 * @testName: getUserPropertiesTest
	 * 
	 * @assertion_ids:
	 * 
	 * @test_Strategy: Run test twice. Modifications to user properties in first run
	 * should not be visible to second run as shallow copies should be used.
	 */
	@Test
	public void getUserPropertiesTest() throws Exception {
		String[] endpoints = new String[] { "programatic/userproperties", "programatic/userproperties" };
		for (String endpoint : endpoints) {
			setProperty(Property.REQUEST, buildRequest(endpoint));
			setProperty(Property.CONTENT, "userproperties");
			invoke(false);
			String response = getResponseAsString();
			assertEquals("PASS", response, "Unexpected result received", response);
			cleanup();
		}
	}
}
