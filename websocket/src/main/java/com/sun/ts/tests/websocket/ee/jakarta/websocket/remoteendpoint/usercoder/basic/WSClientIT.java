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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.remoteendpoint.usercoder.basic;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.client.BinaryAndTextClientEndpoint;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.impl.WaitingSendHandler;
import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.ee.jakarta.websocket.remoteendpoint.usercoder.CoderSuperClass;
import com.sun.ts.tests.websocket.ee.jakarta.websocket.remoteendpoint.usercoder.OPS;
import com.sun.ts.tests.websocket.ee.jakarta.websocket.remoteendpoint.usercoder.WSCBinaryClientEndpoint;
import com.sun.ts.tests.websocket.ee.jakarta.websocket.remoteendpoint.usercoder.WSCBinaryStreamClientEndpoint;
import com.sun.ts.tests.websocket.ee.jakarta.websocket.remoteendpoint.usercoder.WSCSuperEndpoint;
import com.sun.ts.tests.websocket.ee.jakarta.websocket.remoteendpoint.usercoder.WSCTextClientEndpoint;
import com.sun.ts.tests.websocket.ee.jakarta.websocket.remoteendpoint.usercoder.WSCTextStreamClientEndpoint;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ws_wait;
 */
@ExtendWith(ArquillianExtension.class)
public class WSClientIT extends WebSocketCommonClient {
	private static final long serialVersionUID = 7637718042723179933L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class,
				"wsc_ee_jakarta_websocket_remoteendpoint_usercoder_basic_web.war");
		archive.addPackages(false, Filters.exclude(WSClientIT.class),
				"com.sun.ts.tests.websocket.ee.jakarta.websocket.remoteendpoint.usercoder.basic");
		archive.addPackages(false, "com.sun.ts.tests.websocket.ee.jakarta.websocket.remoteendpoint.usercoder");
		archive.addPackages(true, "com.sun.ts.tests.websocket.common.stringbean");
		archive.addClasses(WaitingSendHandler.class);
		archive.addClasses(IOUtil.class);
		return archive;
	};

	public WSClientIT() throws Exception {
		setContextRoot("wsc_ee_jakarta_websocket_remoteendpoint_usercoder_basic_web");
	}

	/* Run test */

	/*
	 * @testName: sendObjectBooleanTextEncoderOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:61;
	 * 
	 * @test_Strategy: A developer-provided encoder for a Java primitive type
	 * overrides the container default encoder. Encoder.Text.encode
	 */
	@Test
	public void sendObjectBooleanTextEncoderOnServerTest() throws Exception {
		invoke("text", OPS.BOOL, CoderSuperClass.COMMON_CODED_STRING);
	}

	/*
	 * @testName: sendObjectCharTextEncoderOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:61;
	 * 
	 * @test_Strategy: A developer-provided encoder for a Java primitive type
	 * overrides the container default encoder. Encoder.Text.encode
	 */
	@Test
	public void sendObjectCharTextEncoderOnServerTest() throws Exception {
		invoke("text", OPS.CHAR, CoderSuperClass.COMMON_CODED_STRING);
	}

	/*
	 * @testName: sendObjectNumberTextEncoderOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:61;
	 * 
	 * @test_Strategy: A developer-provided encoder for a Java primitive type
	 * overrides the container default encoder. Encoder.Text.encode
	 */
	@Test
	public void sendObjectNumberTextEncoderOnServerTest() throws Exception {
		invoke("text", OPS.BYTE, CoderSuperClass.COMMON_CODED_STRING);
		invoke("text", OPS.SHORT, CoderSuperClass.COMMON_CODED_STRING);
		invoke("text", OPS.INT, CoderSuperClass.COMMON_CODED_STRING);
		invoke("text", OPS.LONG, CoderSuperClass.COMMON_CODED_STRING);
		invoke("text", OPS.FLOAT, CoderSuperClass.COMMON_CODED_STRING);
		invoke("text", OPS.DOUBLE, CoderSuperClass.COMMON_CODED_STRING);
	}

	/*
	 * @testName: sendObjectBooleanTextEncoderOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:61;
	 * 
	 * @test_Strategy: A developer-provided encoder for a Java primitive type
	 * overrides the container default encoder. Encoder.Text.encode
	 */
	@Test
	public void sendObjectBooleanTextEncoderOnClientTest() throws Exception {
		WSCTextClientEndpoint client = new WSCTextClientEndpoint();
		setAnnotatedClientEndpoint(client);
		invoke("echo", Boolean.valueOf(WSCSuperEndpoint.BOOL), CoderSuperClass.COMMON_CODED_STRING);
	}

	/*
	 * @testName: sendObjectCharTextEncoderOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:61;
	 * 
	 * @test_Strategy: A developer-provided encoder for a Java primitive type
	 * overrides the container default encoder. Encoder.Text.encode
	 */
	@Test
	public void sendObjectCharTextEncoderOnClientTest() throws Exception {
		WSCTextClientEndpoint client = new WSCTextClientEndpoint();
		setAnnotatedClientEndpoint(client);
		invoke("echo", Character.valueOf(WSCSuperEndpoint.CHAR), CoderSuperClass.COMMON_CODED_STRING);
	}

	/*
	 * @testName: sendObjectNumberTextEncoderOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:61;
	 * 
	 * @test_Strategy: A developer-provided encoder for a Java primitive type
	 * overrides the container default encoder. Encoder.Text.encode
	 */
	@Test
	public void sendObjectNumberTextEncoderOnClientTest() throws Exception {
		WSCTextClientEndpoint client = new WSCTextClientEndpoint();
		setAnnotatedClientEndpoint(client);
		invoke("echo", WSCSuperEndpoint.NUMERIC, CoderSuperClass.COMMON_CODED_STRING);

		setAnnotatedClientEndpoint(client);
		invoke("echo", Short.valueOf(WSCSuperEndpoint.NUMERIC.shortValue()), CoderSuperClass.COMMON_CODED_STRING);

		setAnnotatedClientEndpoint(client);
		invoke("echo", WSCSuperEndpoint.NUMERIC.intValue(), CoderSuperClass.COMMON_CODED_STRING);

		setAnnotatedClientEndpoint(client);
		invoke("echo", WSCSuperEndpoint.NUMERIC.longValue(), CoderSuperClass.COMMON_CODED_STRING);

		setAnnotatedClientEndpoint(client);
		invoke("echo", WSCSuperEndpoint.NUMERIC.doubleValue(), CoderSuperClass.COMMON_CODED_STRING);

		setAnnotatedClientEndpoint(client);
		invoke("echo", WSCSuperEndpoint.NUMERIC.floatValue(), CoderSuperClass.COMMON_CODED_STRING);
	}

	// ----------------------------------------------------------------------
	/*
	 * @testName: sendObjectPrimitivesTextStreamEncoderOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:63;
	 * 
	 * @test_Strategy: A developer-provided encoder for a Java primitive type
	 * overrides the container default encoder. Encoder.TextStream.encode
	 */
	@Test
	public void sendObjectPrimitivesTextStreamEncoderOnServerTest() throws Exception {
		for (OPS op : OPS.values())
			invoke("textstream", op, CoderSuperClass.COMMON_CODED_STRING);
	}

	/*
	 * @testName: sendObjectPrimitivesTextStreamEncoderOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:63;
	 * 
	 * @test_Strategy: A developer-provided encoder for a Java primitive type
	 * overrides the container default encoder. Encoder.TextStream.encode
	 */
	@Test
	public void sendObjectPrimitivesTextStreamEncoderOnClientTest() throws Exception {
		WSCTextStreamClientEndpoint client = new WSCTextStreamClientEndpoint();

		for (Object entity : OPS.getClientEntities()) {
			setAnnotatedClientEndpoint(client);
			invoke("echo", entity, CoderSuperClass.COMMON_CODED_STRING);
		}
	}

	/*
	 * @testName: sendObjectPrimitivesBinaryEncoderOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:56;
	 * 
	 * @test_Strategy: A developer-provided encoder for a Java primitive type
	 * overrides the container default encoder. Encoder.Binary.encode
	 */
	@Test
	public void sendObjectPrimitivesBinaryEncoderOnServerTest() throws Exception {
		for (OPS op : OPS.values()) {
			setClientEndpoint(BinaryAndTextClientEndpoint.class);
			invoke("binary", op, CoderSuperClass.COMMON_CODED_STRING);
		}
	}

	/*
	 * @testName: sendObjectPrimitivesBinaryEncoderOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:56;
	 * 
	 * @test_Strategy: A developer-provided encoder for a Java primitive type
	 * overrides the container default encoder. Encoder.Binary.encode
	 */
	@Test
	public void sendObjectPrimitivesBinaryEncoderOnClientTest() throws Exception {
		WSCBinaryClientEndpoint client = new WSCBinaryClientEndpoint();

		for (Object entity : OPS.getClientEntities()) {
			setAnnotatedClientEndpoint(client);
			invoke("echo", entity, CoderSuperClass.COMMON_CODED_STRING);
		}
	}

	/*
	 * @testName: sendObjectPrimitivesBinaryStreamEncoderOnServerTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:58;
	 * 
	 * @test_Strategy: A developer-provided encoder for a Java primitive type
	 * overrides the container default encoder. Encoder.BinaryStream.encode
	 */
	@Test
	public void sendObjectPrimitivesBinaryStreamEncoderOnServerTest() throws Exception {
		for (OPS op : OPS.values()) {
			setClientEndpoint(BinaryAndTextClientEndpoint.class);
			invoke("binarystream", op, CoderSuperClass.COMMON_CODED_STRING);
		}
	}

	/*
	 * @testName: sendObjectPrimitivesBinaryStreamEncoderOnClientTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:109; WebSocket:JAVADOC:58;
	 * 
	 * @test_Strategy: A developer-provided encoder for a Java primitive type
	 * overrides the container default encoder. Encoder.BinaryStream.encode
	 */
	@Test
	public void sendObjectPrimitivesBinaryStreamEncoderOnClientTest() throws Exception {
		WSCBinaryStreamClientEndpoint client = new WSCBinaryStreamClientEndpoint();

		for (Object entity : OPS.getClientEntities()) {
			setAnnotatedClientEndpoint(client);
			invoke("echo", entity, CoderSuperClass.COMMON_CODED_STRING);
		}
	}
}
