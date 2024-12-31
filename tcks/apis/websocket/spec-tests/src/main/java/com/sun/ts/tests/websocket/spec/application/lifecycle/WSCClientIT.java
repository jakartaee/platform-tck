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

package com.sun.ts.tests.websocket.spec.application.lifecycle;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.util.IOUtil;

import jakarta.websocket.ClientEndpointConfig;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

/*
 * The tests here are not guaranteed to pass in standalone TCK, hence put 
 * into full CTS bundle 
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ws_wait;
 */
@ExtendWith(ArquillianExtension.class)
public class WSCClientIT extends WebSocketCommonClient {

	private static final long serialVersionUID = 7108001387006240382L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class, "wsc_spec_application_lifecycle_web.war");
		archive.addPackages(true, Filters.exclude(WSCClientIT.class),
				"com.sun.ts.tests.websocket.spec.application.lifecycle");
		archive.addClasses(IOUtil.class);
		return archive;
	};

	public WSCClientIT() throws Exception {
		setContextRoot("wsc_spec_application_lifecycle_web");
	}

	/*
	 * @testName: serverLifeCycleTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-2.1.1-1;
	 * 
	 * @test_Strategy: the websocket implementation must use one instance per
	 * application per VM of the Endpoint class to represent the logical endpoint
	 * per connected peer.
	 */
	@Test
	public void serverLifeCycleTest() throws Exception {
		CountDownLatch innerLatch = new CountDownLatch(1);
		CountDownLatch outerLatch = new CountDownLatch(1);
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		InnerEndpoint inner = new InnerEndpoint(innerLatch);
		OuterEndpoint outer = new OuterEndpoint(inner, outerLatch);
		ClientEndpointConfig cec = ClientEndpointConfig.Builder.create().build();
		try {
			String path = getAbsoluteUrl() + "/lifecycle";
			URI uri = new URI(path);
			logMsg("connecting to", path);

			Session innerSession = container.connectToServer(inner, cec, uri);
			Session outerSession = container.connectToServer(outer, cec, uri);
			outerSession.getBasicRemote().sendText("anything");
			outerLatch.await(_ws_wait, TimeUnit.SECONDS);

			innerSession.close();
			outerSession.close();
		} catch (Exception e) {
			new Exception(e);
		}
		assertEquals("0", outer.getReceivedMessage(), "Server Endpoint has not been created per connection, got",
				outer.getReceivedMessage());
		assertEquals("0", inner.getReceivedMessage(), "Server Endpoint has not been created per connection; got",
				inner.getReceivedMessage());
		logMsg("The server endpoint has been created one per application as expected");
	}
}
