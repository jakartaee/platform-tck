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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.server.pathparam;

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
	private static final long serialVersionUID = -6963654147324631018L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "ws_ee_jakarta_websocket_server_pathparam_web.war");
		archive.addPackages(true, Filters.exclude(WSClientIT.class),
				"com.sun.ts.tests.websocket.ee.jakarta.websocket.server.pathparam");
		archive.addClasses(IOUtil.class);
		return archive;
	};

	public WSClientIT() throws Exception {
		setContextRoot("ws_ee_jakarta_websocket_server_pathparam_web");
	}

	/* Run test */

	// -----------------------------On Message

	/*
	 * @testName: multipleStringParamsOnMessageTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-4;
	 * 
	 * @test_Strategy: test sending more path params
	 */
	@Test
	public void multipleStringParamsOnMessageTest() throws Exception {
		multipleStringParams(OPS.MESSAGE);
	}

	/*
	 * @testName: noStringParamsOnMessageTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * 
	 * @test_Strategy: test sending zero path params If the name does not match a
	 * path variable in the URI-template, the value of the method parameter this
	 * annotation annotates is null.
	 */
	@Test
	public void noStringParamsOnMessageTest() throws Exception {
		noStringParams(OPS.MESSAGE);
	}

	/*
	 * @testName: directStringParamOnMessageTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-4;
	 * 
	 * @test_Strategy: test sending zero path params
	 */
	@Test
	public void directStringParamOnMessageTest() throws Exception {
		directStringParam(OPS.MESSAGE);
	}

	/*
	 * @testName: primitiveBooleanAndCharParamsOnMessageTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-5;
	 * 
	 * @test_Strategy: test sending boolean and char to primitives
	 */
	@Test
	public void primitiveBooleanAndCharParamsOnMessageTest() throws Exception {
		primitiveBooleanAndCharParams(OPS.MESSAGE);
	}

	/*
	 * @testName: fullDoubleAndFloatParamsOnMessageTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-5;
	 * 
	 * @test_Strategy: test sending double and float to Full classes
	 */
	@Test
	public void fullDoubleAndFloatParamsOnMessageTest() throws Exception {
		fullDoubleAndFloatParams(OPS.MESSAGE);
	}

	// -----------------------------On Open

	/*
	 * @testName: multipleStringParamsOnOpenTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-4;
	 * 
	 * @test_Strategy: test sending more path params
	 */
	@Test
	public void multipleStringParamsOnOpenTest() throws Exception {
		multipleStringParams(OPS.OPEN);
	}

	/*
	 * @testName: noStringParamsOnOpenTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * 
	 * @test_Strategy: test sending zero path params If the name does not match a
	 * path variable in the URI-template, the value of the method parameter this
	 * annotation annotates is null.
	 */
	@Test
	public void noStringParamsOnOpenTest() throws Exception {
		noStringParams(OPS.OPEN);
	}

	/*
	 * @testName: directStringParamOnOpenTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-4;
	 * 
	 * @test_Strategy: test sending zero path params
	 */
	@Test
	public void directStringParamOnOpenTest() throws Exception {
		directStringParam(OPS.OPEN);
	}

	/*
	 * @testName: primitiveBooleanAndCharParamsOnOpenTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-5;
	 * 
	 * @test_Strategy: test sending boolean and char to primitives
	 */
	@Test
	public void primitiveBooleanAndCharParamsOnOpenTest() throws Exception {
		primitiveBooleanAndCharParams(OPS.OPEN);
	}

	/*
	 * @testName: fullDoubleAndFloatParamsOnOpenTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-5;
	 * 
	 * @test_Strategy: test sending double and float to Full classes
	 */
	@Test
	public void fullDoubleAndFloatParamsOnOpenTest() throws Exception {
		fullDoubleAndFloatParams(OPS.OPEN);
	}

	// -----------------------------On IOException

	/*
	 * @testName: multipleStringParamsOnIOETest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-4;
	 * 
	 * @test_Strategy: test sending more path params
	 */
	@Test
	public void multipleStringParamsOnIOETest() throws Exception {
		multipleStringParams(OPS.IOEXCEPTION);
	}

	/*
	 * @testName: noStringParamsOnIOETest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * 
	 * @test_Strategy: test sending zero path params If the name does not match a
	 * path variable in the URI-template, the value of the method parameter this
	 * annotation annotates is null.
	 */
	@Test
	public void noStringParamsOnIOETest() throws Exception {
		noStringParams(OPS.IOEXCEPTION);
	}

	/*
	 * @testName: directStringParamOnIOETest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-4;
	 * 
	 * @test_Strategy: test sending zero path params
	 */
	@Test
	public void directStringParamOnIOETest() throws Exception {
		directStringParam(OPS.IOEXCEPTION);
	}

	/*
	 * @testName: primitiveBooleanAndCharParamsOnIOETest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-5;
	 * 
	 * @test_Strategy: test sending boolean and char to primitives
	 */
	@Test
	public void primitiveBooleanAndCharParamsOnIOETest() throws Exception {
		primitiveBooleanAndCharParams(OPS.IOEXCEPTION);
	}

	/*
	 * @testName: fullDoubleAndFloatParamsOnIOTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-5;
	 * 
	 * @test_Strategy: test sending double and float to Full classes
	 */
	@Test
	public void fullDoubleAndFloatParamsOnIOTest() throws Exception {
		fullDoubleAndFloatParams(OPS.IOEXCEPTION);
	}

	// -----------------------------On RuntimeException

	/*
	 * @testName: multipleStringParamsOnRETest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-4;
	 * 
	 * @test_Strategy: test sending more path params
	 */
	@Test
	public void multipleStringParamsOnRETest() throws Exception {
		multipleStringParams(OPS.RUNTIMEEXCEPTION);
	}

	/*
	 * @testName: noStringParamsOnRETest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * 
	 * @test_Strategy: test sending zero path params If the name does not match a
	 * path variable in the URI-template, the value of the method parameter this
	 * annotation annotates is null.
	 */
	@Test
	public void noStringParamsOnRETest() throws Exception {
		noStringParams(OPS.RUNTIMEEXCEPTION);
	}

	/*
	 * @testName: directStringParamOnRETest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-4;
	 * 
	 * @test_Strategy: test sending zero path params
	 */
	@Test
	public void directStringParamOnRETest() throws Exception {
		directStringParam(OPS.RUNTIMEEXCEPTION);
	}

	/*
	 * @testName: primitiveBooleanAndCharParamsOnRETest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-5;
	 * 
	 * @test_Strategy: test sending boolean and char to primitives
	 */
	@Test
	public void primitiveBooleanAndCharParamsOnRETest() throws Exception {
		primitiveBooleanAndCharParams(OPS.RUNTIMEEXCEPTION);
	}

	/*
	 * @testName: fullDoubleAndFloatParamsOnRETest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-5;
	 * 
	 * @test_Strategy: test sending double and float to Full classes
	 */
	@Test
	public void fullDoubleAndFloatParamsOnRETest() throws Exception {
		fullDoubleAndFloatParams(OPS.RUNTIMEEXCEPTION);
	}

	// -----------------------------On Close

	/*
	 * @testName: multipleStringParamsOnCloseTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-4;
	 * 
	 * @test_Strategy: test sending more path params
	 */
	@Test
	public void multipleStringParamsOnCloseTest() throws Exception {
		String param = "param";
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i != 12; i++) {
			invoke("onclose", "-1", WSOnClosePathParamServer.RESET);
			sb.append("/").append(param).append(i);
			setProperty(Property.REQUEST, buildRequest(param, sb.toString()));
			setProperty(Property.SEARCH_STRING, sb.toString().replace("/", ""));
			setProperty(Property.CONTENT, OPS.MESSAGE.name());
			invoke();
			TestUtil.sleepMsec(200);
			for (int j = 0; j != i; j++)
				invoke("onclose", String.valueOf(j), param + String.valueOf(j + 1));
		}
	}

	/*
	 * @testName: noStringParamsOnCloseTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * 
	 * @test_Strategy: test sending zero path params If the name does not match a
	 * path variable in the URI-template, the value of the method parameter this
	 * annotation annotates is null.
	 */
	@Test
	public void noStringParamsOnCloseTest() throws Exception {
		invoke("onclose", "-1", WSOnClosePathParamServer.RESET);
		String search = noStringParams(OPS.MESSAGE);
		TestUtil.sleepMsec(200);
		invoke("onclose", "0", search);
	}

	/*
	 * @testName: directStringParamOnCloseTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-4;
	 * 
	 * @test_Strategy: test sending zero path params
	 */
	@Test
	public void directStringParamOnCloseTest() throws Exception {
		invoke("onclose", "-1", WSOnClosePathParamServer.RESET);
		String search = directStringParam(OPS.MESSAGE);
		TestUtil.sleepMsec(200);
		invoke("onclose", "0", search);
	}

	/*
	 * @testName: primitiveBooleanAndCharParamsOnCloseTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-5;
	 * 
	 * @test_Strategy: test sending boolean and char to primitives
	 */
	@Test
	public void primitiveBooleanAndCharParamsOnCloseTest() throws Exception {
		invoke("onclose", "-1", WSOnClosePathParamServer.RESET);
		String[] search = primitiveBooleanAndCharParams(OPS.MESSAGE);
		TestUtil.sleepMsec(200);
		invoke("onclose", "0", search[0]);
		invoke("onclose", "1", search[1]);
	}

	/*
	 * @testName: fullDoubleAndFloatParamsOnCloseTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:181; WebSocket:SPEC:WSC-4.3-3;
	 * WebSocket:SPEC:WSC-4.3-5;
	 * 
	 * @test_Strategy: test sending double and float to Full classes
	 */
	@Test
	public void fullDoubleAndFloatParamsOnCloseTest() throws Exception {
		invoke("onclose", "-1", WSOnClosePathParamServer.RESET);
		String[] search = fullDoubleAndFloatParams(OPS.RUNTIMEEXCEPTION);
		TestUtil.sleepMsec(200);
		invoke("onclose", "0", search[0]);
		invoke("onclose", "1", search[1]);
	}

	// -------------------------------------------------------------------
	private void multipleStringParams(OPS op) throws Exception {
		String param = "param";
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i != 12; i++) {
			sb.append("/").append(param).append(i);
			setProperty(Property.REQUEST, buildRequest(param, sb.toString()));
			setProperty(Property.SEARCH_STRING, sb.toString().replace("/", ""));
			setProperty(Property.CONTENT, op.name());
			invoke();
		}
	}

	private String noStringParams(OPS op) throws Exception {
		String param = "nonused/nonused";
		String search = "null";
		setProperty(Property.REQUEST, buildRequest(param));
		setProperty(Property.SEARCH_STRING, search);
		setProperty(Property.CONTENT, op.name());
		invoke();
		return search;
	}

	private String directStringParam(OPS op) throws Exception {
		String param = "1234567890";
		setProperty(Property.REQUEST, buildRequest(param));
		setProperty(Property.SEARCH_STRING, param);
		setProperty(Property.CONTENT, op.name());
		invoke();
		return param;
	}

	private String[] primitiveBooleanAndCharParams(OPS op) throws Exception {
		String[] param = { "true", "0" };
		setProperty(Property.REQUEST, buildRequest("different/", param[0], "/", param[1]));
		setProperty(Property.SEARCH_STRING, param[0] + param[1]);
		setProperty(Property.CONTENT, op.name());
		invoke();
		return param;
	}

	private String[] fullDoubleAndFloatParams(OPS op) throws Exception {
		String[] param = { String.valueOf(12.34), String.valueOf(56.78f) };
		setProperty(Property.REQUEST, buildRequest("full/", param[0], "/", param[1]));
		setProperty(Property.SEARCH_STRING, param[0] + param[1]);
		setProperty(Property.CONTENT, op.name());
		invoke();
		return param;
	}

}
