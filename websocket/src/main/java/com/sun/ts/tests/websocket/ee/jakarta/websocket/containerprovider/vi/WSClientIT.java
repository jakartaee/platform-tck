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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.containerprovider.vi;

import java.io.IOException;
import java.lang.System.Logger;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.util.IOUtil;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.WebSocketContainer;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ws_wait;
 */
@ExtendWith(ArquillianExtension.class)
public class WSClientIT extends WebSocketCommonClient {

	private static final Logger logger = System.getLogger(WSClientIT.class.getName());

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class, "wsc_ee_containerprovider_vi_web.war");
		archive.addClasses(WSCServer.class);
		archive.addClasses(IOUtil.class);
		return archive;
	};

	private static final long serialVersionUID = 4245245442874605867L;

	public WSClientIT() throws Exception {
		setContextRoot("wsc_ee_containerprovider_vi_web");
	}

	/* Run test */
	/*
	 * @testName: getWebSocketContainerOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28;
	 * 
	 * @test_Strategy: ContainerProvider.getWebSocketContainer
	 */
	@Test
	public void getWebSocketContainerOnClientTest() throws Exception {
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		assertNotNull(container, "ContainerProvider#getWebSocketContainer is null");
		logMsg("ContainerProvider#getWebSocketContainer obtained WebSocketContainer as expected");
	}

	/*
	 * @testName: getWebSocketContainerOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28;
	 * 
	 * @test_Strategy: ContainerProvider.getWebSocketContainer
	 */
	@Test
	public void getWebSocketContainerOnServerTest() throws Exception {
		invoke("srv", "anything", "true");
		logMsg("ContainerProvider#getWebSocketContainer obtained WebSocketContainer as expected");
	}

}
