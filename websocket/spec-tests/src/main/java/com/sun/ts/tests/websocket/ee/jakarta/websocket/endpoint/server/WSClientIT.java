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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.endpoint.server;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.util.IOUtil;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ws_wait;
 */
@ExtendWith(ArquillianExtension.class)
public class WSClientIT extends WebSocketCommonClient {
	private static final long serialVersionUID = -712294674123256741L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class, "wsc_ee_endpoint_server_web.war");
		archive.addPackages(true, Filters.exclude(WSClientIT.class),
				"com.sun.ts.tests.websocket.ee.jakarta.websocket.endpoint.server");
		archive.addClasses(IOUtil.class);
		return archive;
	};

	public WSClientIT() throws Exception {
		setContextRoot("wsc_ee_endpoint_server_web");
	}

	/* Run test */

	/*
	 * @testName: onErrorWorksTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:68;WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:66;
	 * 
	 * @test_Strategy: check @OnError works on Endpoint on Server Side
	 * jakarta.websocket.Endpoint.onOpen Endpoint.Endpoint
	 */
	@Test
	public void onErrorWorksTest() throws Exception {
		invoke("msg", WSCMsgServer.MESSAGES[0], WSCMsgServer.EMPTY);
		invoke("error", "anything", "anything");
		invokeUntilFound("msg", WSCMsgServer.MESSAGES[1], WSCErrorServerEndpoint.EXCEPTION);
		logMsg("@OnError has been called after RuntimeException is thrown on @OnMessage as expected");
	}

	/*
	 * @testName: onCloseWorksTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:67;WebSocket:JAVADOC:69;
	 * WebSocket:JAVADOC:66;
	 * 
	 * @test_Strategy: check @OnClose works on Endpoint on Server side
	 * jakarta.websocket.Endpoint.onOpen Endpoint.Endpoint
	 */
	@Test
	public void onCloseWorksTest() throws Exception {
		invoke("msg", WSCMsgServer.MESSAGES[0], WSCMsgServer.EMPTY);
		invoke("close", "anything", "anything");
		invokeUntilFound("msg", WSCMsgServer.MESSAGES[1], WSCCloseServerEndpoint.CLOSE);
		logMsg("@OnClose has been called after session.close() as expected");
	}

	// //////////////////////////////////////////////////////////////////////
	private void invokeUntilFound(String endpoint, String content, String search) throws Exception {
		int sleep = 100;
		long maxWait = _ws_wait * (1000 / sleep);
		long count = 0;
		boolean found = false;
		String response = null;
		while (!found && count < maxWait) {
			invoke(endpoint, content, "", false);
			response = getLastResponse(String.class);
			if (response.equals(search))
				found = true;
			cleanup();
			if (!found)
				TestUtil.sleepMsec(sleep);
			count++;
		}
		if (!found) {
			new Exception(search + " has not been found in response, last response was " + response);
		}
	}

}
