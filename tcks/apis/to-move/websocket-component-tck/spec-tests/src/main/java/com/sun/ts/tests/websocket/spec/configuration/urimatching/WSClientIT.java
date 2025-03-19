/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates and others.
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

package com.sun.ts.tests.websocket.spec.configuration.urimatching;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.util.IOUtil;

import jakarta.websocket.DeploymentException;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ws_wait;
 */
@ExtendWith(ArquillianExtension.class)
public class WSClientIT extends WebSocketCommonClient {

	private static final long serialVersionUID = -6954038749538806576L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class, "wsc_spec_configuration_urimatching_web.war");
		archive.addPackages(false, Filters.exclude(WSClientIT.class),
				"com.sun.ts.tests.websocket.spec.configuration.urimatching");
		archive.addClasses(IOUtil.class);
		return archive;
	};

	static final String ECHO = "Echo message to be sent to server endpoint";

	public WSClientIT() throws Exception {
		setContextRoot("wsc_spec_configuration_urimatching_web");
	}

	/* Run test */

	// TEXT ------------------------------------------

	/*
	 * @testName: match1ExactTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
	 * WebSocket:SPEC:WSC-4.3-3;
	 * 
	 * @test_Strategy: Match /a
	 */
	@Test
	public void match1ExactTest() throws Exception {
		setProperty(Property.REQUEST, buildRequest("a"));
		setProperty(Property.CONTENT, ECHO);
		setProperty(Property.SEARCH_STRING, ECHO);
		setProperty(Property.SEARCH_STRING, WSL1ExactServer.class.getName());
		invoke();
	}

	/*
	 * @testName: match1ParamTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
	 * WebSocket:SPEC:WSC-4.3-3;
	 * 
	 * @test_Strategy: Match /{a}
	 */
	@Test
	public void match1ParamTest() throws Exception {
		String param = "c";
		setProperty(Property.REQUEST, buildRequest(param));
		setProperty(Property.CONTENT, ECHO);
		setProperty(Property.SEARCH_STRING, ECHO);
		setProperty(Property.SEARCH_STRING, param);
		setProperty(Property.SEARCH_STRING, WSL1ParamServer.class.getName());
		invoke();
	}

	/*
	 * @testName: match2CParamDTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
	 * WebSocket:SPEC:WSC-4.3-3;
	 * 
	 * @test_Strategy: Match /c/{d}
	 */
	@Test
	public void match2CParamDTest() throws Exception {
		String param = "one";
		setProperty(Property.REQUEST, buildRequest("c/", param));
		setProperty(Property.CONTENT, ECHO);
		setProperty(Property.SEARCH_STRING, ECHO);
		setProperty(Property.SEARCH_STRING, param);
		setProperty(Property.SEARCH_STRING, WSL2CParamDServer.class.getName());
		invoke();
	}

	/*
	 * @testName: match2CDExactTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
	 * 
	 * @test_Strategy: Match /c/d
	 */
	@Test
	public void match2CDExactTest() throws Exception {
		setProperty(Property.REQUEST, buildRequest("c/d"));
		setProperty(Property.CONTENT, ECHO);
		setProperty(Property.SEARCH_STRING, ECHO);
		setProperty(Property.SEARCH_STRING, WSL2ExactCDServer.class.getName());
		invoke();
	}

	/*
	 * @testName: match2ParamCExactDTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
	 * WebSocket:SPEC:WSC-4.3-3;
	 * 
	 * @test_Strategy: Match /{c}/d
	 */
	@Test
	public void match2ParamCExactDTest() throws Exception {
		String c = "one";
		setProperty(Property.REQUEST, buildRequest(c, "/d"));
		setProperty(Property.CONTENT, ECHO);
		setProperty(Property.SEARCH_STRING, ECHO);
		setProperty(Property.SEARCH_STRING, c);
		setProperty(Property.SEARCH_STRING, WSL2DParamCServer.class.getName());
		invoke();
	}

	/*
	 * @testName: match2ParamCDTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
	 * WebSocket:SPEC:WSC-4.3-3;
	 * 
	 * @test_Strategy: Match /{c}/{d}
	 */
	@Test
	public void match2ParamCDTest() throws Exception {
		String c = "one";
		String d = "two";
		setProperty(Property.REQUEST, buildRequest(c, "/", d));
		setProperty(Property.CONTENT, ECHO);
		setProperty(Property.SEARCH_STRING, ECHO);
		setProperty(Property.SEARCH_STRING, c);
		setProperty(Property.SEARCH_STRING, d);
		setProperty(Property.SEARCH_STRING, WSL2ParamCDServer.class.getName());
		invoke();
	}

	/*
	 * @testName: match3ACDExactTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
	 * WebSocket:SPEC:WSC-4.3-3;
	 * 
	 * @test_Strategy: Match /a/c/d
	 */
	@Test
	public void match3ACDExactTest() throws Exception {
		setProperty(Property.REQUEST, buildRequest("a/c/d"));
		setProperty(Property.CONTENT, ECHO);
		setProperty(Property.SEARCH_STRING, ECHO);
		setProperty(Property.SEARCH_STRING, WSL3ACDExactServer.class.getName());
		invoke();
	}

	/*
	 * @testName: match3AParamCDTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
	 * WebSocket:SPEC:WSC-4.3-3;
	 * 
	 * @test_Strategy: Match /a/{c}/{d}
	 */
	@Test
	public void match3AParamCDTest() throws Exception {
		String c = "one";
		String d = "two";
		setProperty(Property.REQUEST, buildRequest("a/", c, "/", d));
		setProperty(Property.CONTENT, ECHO);
		setProperty(Property.SEARCH_STRING, ECHO);
		setProperty(Property.SEARCH_STRING, c);
		setProperty(Property.SEARCH_STRING, d);
		setProperty(Property.SEARCH_STRING, WSL3AParamCDServer.class.getName());
		invoke();
	}

	/*
	 * @testName: match3CParamADTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
	 * WebSocket:SPEC:WSC-4.3-3;
	 * 
	 * @test_Strategy: Match /{a}/c/{d}
	 */
	@Test
	public void match3CParamADTest() throws Exception {
		String a = "one";
		String d = "two";
		setProperty(Property.REQUEST, buildRequest(a, "/c/", d));
		setProperty(Property.CONTENT, ECHO);
		setProperty(Property.SEARCH_STRING, ECHO);
		setProperty(Property.SEARCH_STRING, a);
		setProperty(Property.SEARCH_STRING, d);
		setProperty(Property.SEARCH_STRING, WSL3CParamADServer.class.getName());
		invoke();
	}

	/*
	 * @testName: match3DParamACTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
	 * WebSocket:SPEC:WSC-4.3-3;
	 * 
	 * @test_Strategy: Match /{a}/{c}/d
	 */
	@Test
	public void match3DParamACTest() throws Exception {
		String a = "one";
		String c = "two";
		setProperty(Property.REQUEST, buildRequest(a, "/", c, "/d"));
		setProperty(Property.CONTENT, ECHO);
		setProperty(Property.SEARCH_STRING, ECHO);
		setProperty(Property.SEARCH_STRING, a);
		setProperty(Property.SEARCH_STRING, c);
		setProperty(Property.SEARCH_STRING, WSL3DParamACServer.class.getName());
		invoke();
	}

	/*
	 * @testName: match3ParamACDTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
	 * WebSocket:SPEC:WSC-4.3-3;
	 * 
	 * @test_Strategy: Match /{a}/c/{d}
	 */
	@Test
	public void match3ParamACDTest() throws Exception {
		String a = "one";
		String c = "two";
		String d = "three";
		setProperty(Property.REQUEST, buildRequest(a, "/", c, "/", d));
		setProperty(Property.CONTENT, ECHO);
		setProperty(Property.SEARCH_STRING, ECHO);
		setProperty(Property.SEARCH_STRING, a);
		setProperty(Property.SEARCH_STRING, c);
		setProperty(Property.SEARCH_STRING, d);
		setProperty(Property.SEARCH_STRING, WSL3ParamACDServer.class.getName());
		invoke();
	}

	/*
	 * @testName: match3ACParamDTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
	 * WebSocket:SPEC:WSC-4.3-3;
	 * 
	 * @test_Strategy: Match /a/c/{d}
	 */
	@Test
	public void match3ACParamDTest() throws Exception {
		String d = "three";
		setProperty(Property.REQUEST, buildRequest("a/c/", d));
		setProperty(Property.CONTENT, ECHO);
		setProperty(Property.SEARCH_STRING, ECHO);
		setProperty(Property.SEARCH_STRING, d);
		setProperty(Property.SEARCH_STRING, WSL3ACParamDServer.class.getName());
		invoke();
	}

	/*
	 * @testName: match3ADParamCTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
	 * WebSocket:SPEC:WSC-4.3-3;
	 * 
	 * @test_Strategy: Match /a/{c}/d
	 */
	@Test
	public void match3ADParamCTest() throws Exception {
		String c = "two";
		setProperty(Property.REQUEST, buildRequest("a/", c, "/d"));
		setProperty(Property.CONTENT, ECHO);
		setProperty(Property.SEARCH_STRING, ECHO);
		setProperty(Property.SEARCH_STRING, c);
		setProperty(Property.SEARCH_STRING, WSL3ADParamCServer.class.getName());
		invoke();
	}

	/*
	 * @testName: match3CDParamATest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-1; WebSocket:SPEC:WSC-3.1.1-3;
	 * WebSocket:SPEC:WSC-4.3-3;
	 * 
	 * @test_Strategy: Match /a/{c}/d
	 */
	@Test
	public void match3CDParamATest() throws Exception {
		String a = "two";
		setProperty(Property.REQUEST, buildRequest(a, "/c/d"));
		setProperty(Property.CONTENT, ECHO);
		setProperty(Property.SEARCH_STRING, ECHO);
		setProperty(Property.SEARCH_STRING, a);
		setProperty(Property.SEARCH_STRING, WSL3CDParamAServer.class.getName());
		invoke();
	}

	/*
	 * @testName: noMatch4ACDETest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-3.1.1-4;
	 * 
	 * @test_Strategy: no Match for /a/c/d/e The implementation must not establish
	 * the connection unless there is a match. Throws: DeploymentException - if the
	 * configuration is not valid
	 */
	@Test
	public void noMatch4ACDETest() throws Exception {
		setProperty(Property.REQUEST, buildRequest("a/c/d/e"));
		setProperty(Property.CONTENT, ECHO);
		try {
			invoke();
		} catch (Exception e) {
			assertCause(e, DeploymentException.class, "DeploymentException is not the cause", e);
			return;
		}
		new Exception("No Deployment exception thrown");
	}

}
