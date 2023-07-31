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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.handshakeresponse;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.impl.ClientConfigurator;
import com.sun.ts.tests.websocket.common.util.IOUtil;

import jakarta.websocket.ClientEndpointConfig;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ws_wait;
 */
@ExtendWith(ArquillianExtension.class)
public class WSCClientIT extends WebSocketCommonClient {

	private static final long serialVersionUID = 2315071335485201973L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class, "wsc_ee_jakarta_websocket_handshakeresponse_web.war");
		archive.addPackages(true, Filters.exclude(WSCClientIT.class),
				"com.sun.ts.tests.websocket.ee.jakarta.websocket.handshakeresponse");
		archive.addClasses(IOUtil.class);
		return archive;
	};

	public WSCClientIT() throws Exception {
		setContextRoot("wsc_ee_jakarta_websocket_handshakeresponse_web");
	}

	static final String KEY = "aFirstKey";

	static final String[] HEADERS = { "header1", "header2", "header3", "header4", "header5", "header6", "header7",
			"header8" };

	/* Run test */

	/*
	 * @testName: headerToHeaderTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:77; WebSocket:JAVADOC:174;
	 * WebSocket:JAVADOC:15; WebSocket:JAVADOC:16; WebSocket:JAVADOC:210;
	 * 
	 * @test_Strategy: HandshakeResponse.getHeaders HandshakeRequest.getHeaders
	 * ClientEndpointConfig.Configurator.afterResponse
	 * ClientEndpointConfig.Configurator.beforeRequest
	 * ServerEndpointConfig.Configurator.modifyHandshake
	 * 
	 * This test sets headers to request on client, on server it reads them, and put
	 * them to response, and headers are checked on client
	 */
	@Test
	public void headerToHeaderTest() throws Exception {
		ClientConfigurator configurator = new ClientConfigurator();
		configurator.addToRequestAndResponse(KEY, HEADERS);
		ClientEndpointConfig config = ClientEndpointConfig.Builder.create().configurator(configurator).build();
		setClientEndpointConfig(config);
		invoke("echo", "anything", "anything");
		configurator.assertBeforeRequestHasBeenCalled();
		configurator.assertAfterResponseHasBeenCalled();
	}

	/*
	 * @testName: addHeadersOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:77; WebSocket:JAVADOC:174;
	 * WebSocket:JAVADOC:15; WebSocket:JAVADOC:16; WebSocket:JAVADOC:210;
	 * 
	 * @test_Strategy: HandshakeResponse.getHeaders HandshakeRequest.getHeaders
	 * ClientEndpointConfig.Configurator.afterResponse
	 * ClientEndpointConfig.Configurator.beforeRequest
	 * ServerEndpointConfig.Configurator.modifyHandshake
	 * 
	 * This test puts new values to header map on server and it is checked on a
	 * client
	 */
	@Test
	public void addHeadersOnServerTest() throws Exception {
		ClientConfigurator configurator = new ClientConfigurator();
		configurator.addToResponse(SetHeadersConfigurator.KEY, SetHeadersConfigurator.HEADERS);
		ClientEndpointConfig config = ClientEndpointConfig.Builder.create().configurator(configurator).build();
		setClientEndpointConfig(config);
		invoke("setheaders", "anything", "anything");
		configurator.assertBeforeRequestHasBeenCalled();
		configurator.assertAfterResponseHasBeenCalled();
	}
}
