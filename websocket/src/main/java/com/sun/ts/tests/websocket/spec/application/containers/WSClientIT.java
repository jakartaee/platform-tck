/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.spec.application.containers;

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
	private static final long serialVersionUID = -2942255764587010746L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class, "wsc_spec_application_containers_web.war");
		archive.addClasses(WSCServer.class);
		archive.addClasses(IOUtil.class);
		return archive;
	};

	public WSClientIT() throws Exception {
		setContextRoot("wsc_spec_application_containers_web");
	}

	/*
	 * @testName: webSocketContainerIsOnePerVMTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-2.1.7-1;
	 * 
	 * @test_Strategy: In server deployments of websocket implementations, there is
	 * one unique WebSocketContainer instance per application per Java VM
	 */
	@Test
	public void webSocketContainerIsOnePerVMTest() throws Exception {
		invoke("srv", "anything", "", false);
		String address = getResponseAsString();
		invokeAgain("anything", "", false);
		String address2 = getResponseAsString().substring(address.length());
		cleanup();
		assertEquals(address, address2,
				"There are is not just one WebSocketContainer instance per application per VM); hashCode of first",
				address, "hasCode of the second", address2);
		logMsg("session#getContainer returns only one instance of WebSocketContainer as expected");
	}

}
