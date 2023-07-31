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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.endpoint.client;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.util.IOUtil;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ws_wait;
 */
@ExtendWith(ArquillianExtension.class)
public class WSClientIT extends WebSocketCommonClient {
	private static final long serialVersionUID = 5511697770152558944L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class, "wsc_ee_endpoint_client_web.war");
		archive.addClasses(OPS.class, WSCEchoServer.class);
		archive.addClasses(IOUtil.class);
		return archive;
	};

	public WSClientIT() throws Exception {
		setContextRoot("wsc_ee_endpoint_client_web");
	}

	/* Run test */

	/*
	 * @testName: onErrorWorksTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:68;WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:66;
	 * 
	 * @test_Strategy: check @OnError works on Endpoint on Client Side
	 * jakarta.websocket.Endpoint.onOpen Endpoint.Endpoint
	 */
	@Test
	public void onErrorWorksTest() throws Exception {
		WSCErrorClientEndpoint endpoint = new WSCErrorClientEndpoint();
		setClientEndpointInstance(endpoint);
		invoke("echo", OPS.ECHO_MSG, OPS.ECHO_MSG);
		assertFalse(endpoint.onErrorCalled, "@OnError has been unexpectedly called");

		setCountDownLatchCount(2);
		setClientEndpointInstance(endpoint);
		invoke("echo", OPS.THROW, OPS.THROW);
		assertTrue(endpoint.onErrorCalled,
				"@OnError has NOT been called after RuntimeException is thrown on @OnMessage");
		logMsg("@OnError has been called after RuntimeException is thrown on @OnMessage as expected");
	}

	/*
	 * @testName: onCloseWorksTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:67;WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:66;
	 * 
	 * @test_Strategy: check @OnClose works on Endpoint on Client side
	 * jakarta.websocket.Endpoint.onOpen Endpoint.Endpoint
	 */
	@Test
	public void onCloseWorksTest() throws Exception {
		WSCCloseClientEndpoint endpoint = new WSCCloseClientEndpoint();
		setClientEndpointInstance(endpoint);
		invoke("echo", OPS.ECHO_MSG.name(), OPS.ECHO_MSG.name(), false);
		assertFalse(endpoint.onCloseCalled, "@OnClose has been unexpectedly called");
		cleanup();
		endpoint.waitForClose(_ws_wait);
		assertTrue(endpoint.onCloseCalled, "@OnClose has NOT been called after session.close()");
		logMsg("@OnClose has been called after session.close() as expected");
	}

}
