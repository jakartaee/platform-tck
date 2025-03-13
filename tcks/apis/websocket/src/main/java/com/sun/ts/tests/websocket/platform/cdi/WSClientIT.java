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

package com.sun.ts.tests.websocket.platform.cdi;

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
import com.sun.ts.tests.websocket.common.util.IOUtil;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ws_wait;
 */
@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("web")
public class WSClientIT extends WebSocketCommonClient {

	private static final long serialVersionUID = 5295841512141568599L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class, "wsc_platform_cdi_web.war");
		archive.addPackages(true, Filters.exclude(WSClientIT.class), "com.sun.ts.tests.websocket.platform.cdi");
		archive.addClasses(IOUtil.class);
		archive.addAsWebInfResource(WSClientIT.class.getPackage(), "beans.xml", "beans.xml");
		return archive;
	};

	public WSClientIT() throws Exception {
		setContextRoot("wsc_platform_cdi_web");
	}

	/* Run test */

	/*
	 * @testName: cdiFieldTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-7.1.1-1;
	 * 
	 * @test_Strategy: Websocket endpoints running in the Java EE platform must have
	 * full dependency injection support as described in the CDI specification.
	 * Websocket implementations part of the Java EE platform are required to
	 * support field, injection using the jakarta.inject.Inject annotation into all
	 * endpoint classes
	 */
	@Test
	public void cdiFieldTest() throws Exception {
		String msg = "field";
		setProperty(Property.REQUEST, buildRequest(msg));
		setProperty(Property.CONTENT, msg);
		setProperty(Property.SEARCH_STRING, msg);
		setProperty(Property.SEARCH_STRING, WSInjectableServer.class.getName());
		invoke();
	}

	/*
	 * @testName: cdiMethodTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-7.1.1-1;
	 * 
	 * @test_Strategy: Websocket endpoints running in the Java EE platform must have
	 * full dependency injection support as described in the CDI specification.
	 * Websocket implementations part of the Java EE platform are required to
	 * support method injection using the jakarta.inject.Inject annotation into all
	 * endpoint classes
	 */
	@Test
	public void cdiMethodTest() throws Exception {
		String msg = "method";
		setProperty(Property.REQUEST, buildRequest(msg));
		setProperty(Property.CONTENT, msg);
		setProperty(Property.SEARCH_STRING, msg);
		setProperty(Property.SEARCH_STRING, WSInjectableServer.class.getName());
		invoke();
	}

	/*
	 * @testName: cdiConstructorTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-7.1.1-1;
	 * 
	 * @test_Strategy: Websocket endpoints running in the Java EE platform must have
	 * full dependency injection support as described in the CDI specification.
	 * Websocket implementations part of the Java EE platform are required to
	 * support constructor injection using the jakarta.inject.Inject annotation into
	 * all endpoint classes
	 */
	@Test
	public void cdiConstructorTest() throws Exception {
		String msg = "constructor";
		setProperty(Property.REQUEST, buildRequest(msg));
		setProperty(Property.CONTENT, msg);
		setProperty(Property.SEARCH_STRING, msg);
		setProperty(Property.SEARCH_STRING, WSInjectableServer.class.getName());
		invoke();
	}

}
