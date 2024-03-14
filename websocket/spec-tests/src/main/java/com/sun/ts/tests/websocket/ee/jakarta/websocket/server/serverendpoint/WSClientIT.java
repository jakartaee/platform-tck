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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.server.serverendpoint;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
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

import jakarta.websocket.server.ServerEndpointConfig.Configurator;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 *                     ws_wait;
 */
@ExtendWith(ArquillianExtension.class)
public class WSClientIT extends WebSocketCommonClient {

	private static final long serialVersionUID = 3021625862493775185L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class, "wsc_ee_server_serverendpoint_web.war");
		archive.addPackages(false, Filters.exclude(WSClientIT.class),
				"com.sun.ts.tests.websocket.ee.jakarta.websocket.server.serverendpoint");
		archive.addPackages(false, "com.sun.ts.tests.websocket.common.stringbean");
		archive.addClasses(IOUtil.class, StringUtil.class);
		return archive;
	};

	public WSClientIT() throws Exception {
		setContextRoot("wsc_ee_server_serverendpoint_web");
	}

	/* Run test */

	/*
	 * @testName: getDefaultConfiguratorTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:188;
	 * 
	 * @test_Strategy: Default: ServerEndpointConfig.Configurator.class
	 */
	@Test
	public void getDefaultConfiguratorTest() throws Exception {
		String[] sequence = { "default", "encoded", "decoded", "subprotocoled" };
		invokeDefaults(sequence, "configurator", Configurator.class.getName());
	}

	/*
	 * @testName: getDefaultEncodersTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:190;
	 * 
	 * @test_Strategy: Default: {}
	 */
	@Test
	public void getDefaultEncodersTest() throws Exception {
		String[] sequence = { "default", "configured", "decoded", "subprotocoled" };
		invokeDefaults(sequence, "encoders", "{}");
	}

	/*
	 * @testName: getDefaultDecodersTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:189;
	 * 
	 * @test_Strategy: Default: {}
	 */
	@Test
	public void getDefaultDecodersTest() throws Exception {
		String[] sequence = { "default", "encoded", "configured", "subprotocoled" };
		invokeDefaults(sequence, "decoders", "{}");
	}

	/*
	 * @testName: getDefaultSubprotocolsTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:191;
	 * 
	 * @test_Strategy: Default: {}
	 */
	@Test
	public void getDefaultSubprotocolsTest() throws Exception {
		String[] sequence = { "default", "encoded", "configured", "decoded" };
		invokeDefaults(sequence, "subprotocols", "{}");
	}

	/*
	 * @testName: getDefaultValueTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:192;
	 * 
	 * @test_Strategy: Default: {}
	 */
	@Test
	public void getDefaultValueTest() throws Exception {
		String[] sequence = { "default", "encoded", "configured", "decoded", "subprotocoled" };
		invokeValues(sequence, "value");
	}

	/*
	 * @testName: getEncodersTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:190;
	 * 
	 * @test_Strategy: The ordered array of encoder classes this endpoint will use.
	 */
	@Test
	public void getEncodersTest() throws Exception {
		setProperty(Property.REQUEST, buildRequest("encoded"));
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
	 * @assertion_ids: WebSocket:JAVADOC:189;
	 * 
	 * @test_Strategy: The ordered array of decoder classes this endpoint will use.
	 */
	@Test
	public void getDecodersTest() throws Exception {
		setProperty(Property.REQUEST, buildRequest("decoded"));
		setProperty(Property.CONTENT, "decoders");
		setProperty(Property.UNORDERED_SEARCH_STRING, StringBeanBinaryDecoder.class.getName());
		setProperty(Property.UNORDERED_SEARCH_STRING, StringBeanBinaryStreamDecoder.class.getName());
		setProperty(Property.UNORDERED_SEARCH_STRING, StringBeanTextDecoder.class.getName());
		setProperty(Property.UNORDERED_SEARCH_STRING, StringBeanTextStreamDecoder.class.getName());
		invoke();
	}

	/*
	 * @testName: getSubprotocolsTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:191;
	 * 
	 * @test_Strategy: The ordered array of web socket protocols this endpoint
	 * supports.
	 */
	@Test
	public void getSubprotocolsTest() throws Exception {
		setProperty(Property.REQUEST, buildRequest("subprotocoled"));
		setProperty(Property.CONTENT, "subprotocols");
		setProperty(Property.UNORDERED_SEARCH_STRING, "abc");
		setProperty(Property.UNORDERED_SEARCH_STRING, "def");
		setProperty(Property.UNORDERED_SEARCH_STRING, "ghi");
		invoke();
	}

	/*
	 * @testName: getConfiguratorTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:188;
	 * 
	 * @test_Strategy: The optional custom configurator class that the developer
	 * would like to use to further configure new instances of this endpoint.
	 */
	@Test
	public void getConfiguratorTest() throws Exception {
		setProperty(Property.REQUEST, buildRequest("configured"));
		setProperty(Property.CONTENT, "configurator");
		setProperty(Property.SEARCH_STRING, SimpleConfigurator.class.getName());
		invoke();
	}

	/*
	 * @testName: countConfigurationInstancesTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:188;
	 * 
	 * @test_Strategy: The implementation creates a new instance of the configurator
	 * per logical endpoint.
	 */
	@Test
	public void countConfigurationInstancesTest() throws Exception {
		setProperty(Property.REQUEST, buildRequest("countone"));
		setProperty(Property.CONTENT, "any");
		invoke();
		setProperty(Property.REQUEST, buildRequest("counttwo"));
		setProperty(Property.CONTENT, "any");
		setProperty(Property.SEARCH_STRING, "2");
		invoke();
	}

	// ///////////////////////////////////////////////////////////////////////

	private void invokeDefaults(String[] endpointSequence, String content, String search) throws Exception {
		for (String endpoint : endpointSequence) {
			setProperty(Property.REQUEST, buildRequest(endpoint));
			setProperty(Property.CONTENT, content);
			setProperty(Property.SEARCH_STRING, search);
			invoke();
		}
	}

	private void invokeValues(String[] endpointSequence, String content) throws Exception {
		for (String endpoint : endpointSequence) {
			setProperty(Property.REQUEST, buildRequest(endpoint));
			setProperty(Property.CONTENT, content);
			setProperty(Property.SEARCH_STRING, endpoint);
			invoke();
		}
	}
}
