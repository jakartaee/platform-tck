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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.containerprovider.metainf;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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
	private static final long serialVersionUID = 8387217970724424176L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class, "wsc_ee_containerprovider_metainf_web.war");
		archive.addClasses(WSCServer.class);
		archive.addClasses(IOUtil.class);

		final JavaArchive containerProviderJar = ShrinkWrap
				.create(JavaArchive.class, "wsc_ee_containerprovider_metainf_lib.jar")
				.addClasses(LibrariedQuestionaire.class, TCKClassLoader.class, TCKWebSocketContainer.class,
						TCKContainerProvider.class);
		InputStream inStream = WSClientIT.class.getClassLoader().getResourceAsStream(
				"com/sun/ts/tests/websocket/ee/jakarta/websocket/containerprovider/metainf/jakarta.websocket.ContainerProvider");
		ByteArrayAsset containerProvider = new ByteArrayAsset(inStream);
		containerProviderJar.addAsManifestResource(containerProvider, "services/jakarta.websocket.ContainerProvider");
		archive.addAsLibrary(containerProviderJar);
		return archive;
	};

	public WSClientIT() throws Exception {
		setContextRoot("wsc_ee_containerprovider_metainf_web");
	}

	/* Run test */
	/*
	 * @testName: getWebSocketContainerOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:27;
	 * 
	 * @test_Strategy: Check the TCKContainerProvider is used, as order by
	 * META-INF/services/jakarta.websocket.ContainerProvider file
	 * 
	 * ContainerProvider.ContainerProvider()
	 */
	@Test
	public void getWebSocketContainerOnServerTest() throws Exception {
		invoke("srv", "anything", TCKWebSocketContainer.class.getName());
	}

}
