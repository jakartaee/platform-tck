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

package com.sun.ts.tests.websocket.platform.jakarta.websocket.server.handshakerequest.session;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.impl.ClientConfigurator;
import com.sun.ts.tests.websocket.common.util.IOUtil;

import jakarta.websocket.ClientEndpointConfig;

/*
 * The tests here are not guaranteed to pass in standalone TCK, hence put 
 * into full CTS bundle 
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ws_wait;
 */
@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("web")
public class WSCClientIT extends WebSocketCommonClient {

	private static final long serialVersionUID = 3741198886806200627L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class,
				"wsc_platform_jakarta_websocket_handshakerequest_session_web.war");
		archive.addPackages(false, Filters.exclude(WSCClientIT.class),
				"com.sun.ts.tests.websocket.platform.jakarta.websocket.server.handshakerequest.session");
		archive.addClasses(IOUtil.class);
		return archive;
	};

	public WSCClientIT() throws Exception {
		setContextRoot("wsc_platform_jakarta_websocket_handshakerequest_session_web");
	}

	/*
	 * @testName: getHttpSessionTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:175; WebSocket:JAVADOC:15;
	 * WebSocket:JAVADOC:210; WebSocket:SPEC:WSC-7.2-1;
	 * 
	 * @test_Strategy: HandshakeRequest.getHttpSession
	 * ClientEndpointConfig.Configurator.afterResponse
	 * ServerEndpointConfig.Configurator.modifyHandshake
	 * 
	 * the http session or null if either the websocket implementation is not part
	 * of a Java EE web container, or there is no HttpSession associated with the
	 * opening handshake request.
	 * 
	 * The API allows access in the opening handshake to the unique HttpSession
	 * corresponding to that same client.
	 * 
	 * This test is only run as part of CTS, i.e. in full java EE environment, i.e.
	 * in servlet container
	 */
	@Test
	public void getHttpSessionTest() throws Exception {
		ClientConfigurator configurator = new ClientConfigurator();
		configurator.addToResponse(GetHttpSessionConfigurator.RESPONSE_KEY, String.valueOf(true));
		ClientEndpointConfig config = ClientEndpointConfig.Builder.create().configurator(configurator).build();
		setClientEndpointConfig(config);
		invoke("gethttpsession", "anything", "anything");
		configurator.assertBeforeRequestHasBeenCalled();
		configurator.assertAfterResponseHasBeenCalled();
	}
}
