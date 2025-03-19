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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.websocketmessagereturntype;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.client.ByteBufferClientEndpoint;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.util.IOUtil;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ws_wait;
 */
@ExtendWith(ArquillianExtension.class)
public class WSClientIT extends WebSocketCommonClient {

	private static final long serialVersionUID = -5623230069104999740L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "ws_ee_websocketmessagereturntype_web.war");
		archive.addPackages(true, Filters.exclude(WSClientIT.class),
				"com.sun.ts.tests.websocket.ee.jakarta.websocket.websocketmessagereturntype");
		archive.addPackages(true, "com.sun.ts.tests.websocket.common.stringbean");
		archive.addClasses(IOUtil.class);
		return archive;
	};

	public WSClientIT() throws Exception {
		setContextRoot("ws_ee_websocketmessagereturntype_web");
	}

	/* Run test */

	// TEXT ------------------------------------------

	/*
	 * @testName: dataTypesTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
	 * WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: test primitive and boxed datatypes
	 */
	@Test
	public void dataTypesTest() throws Exception {
		invokeDataTypeSequence("true", "boolean");
		invokeDataTypeSequence("123", "byte");
		invokeDataTypeSequence(String.valueOf(Short.MAX_VALUE), "short");
		invokeDataTypeSequence(String.valueOf(Short.MIN_VALUE), "int");
		invokeDataTypeSequence(String.valueOf(Short.MIN_VALUE), "long");
		invokeDataTypeSequence(String.valueOf(123.456f), "float");
		invokeDataTypeSequence(String.valueOf(789.012), "double");
		invokeDataTypeSequence(String.valueOf('A'), "char");
	}

	/*
	 * @testName: textEncoderTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: test text encoder
	 */
	@Test
	public void textEncoderTest() throws Exception {
		invokeSequence("textEncoderTest", "textencoder");
	}

	/*
	 * @testName: textStreamEncoderTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: test text stream encoder
	 */
	@Test
	public void textStreamEncoderTest() throws Exception {
		invokeSequence("textStreamEncoderTest", "textstreamencoder");
	}

	// -----------------Binary --------------------------------

	/*
	 * @testName: binaryEncoderTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: test binary encoder
	 */
	@Test
	public void binaryEncoderTest() throws Exception {
		setClientEndpoint(ByteBufferClientEndpoint.class);
		invokeSequence("binaryEncoderTest", "binaryencoder");
	}

	/*
	 * @testName: binaryStreamEncoderTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: test binary stream encoder
	 */
	@Test
	public void binaryStreamEncoderTest() throws Exception {
		setClientEndpoint(ByteBufferClientEndpoint.class);
		invokeSequence("binaryStreamEncoderTest", "binarystreamencoder");
	}

	/*
	 * @testName: byteArrayTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: test byte array
	 */
	@Test
	public void byteArrayTest() throws Exception {
		setClientEndpoint(ByteBufferClientEndpoint.class);
		invokeSequence("byteArrayTest", "bytearray");
	}

	/*
	 * @testName: byteBufferTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: test byte buffer
	 */
	@Test
	public void byteBufferTest() throws Exception {
		setClientEndpoint(ByteBufferClientEndpoint.class);
		invokeSequence("byteBufferTest", "bytebuffer");
	}

	/*
	 * @testName: directByteBufferTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: test direct byte buffer
	 */
	@Test
	public void directByteBufferTest() throws Exception {
		setClientEndpoint(ByteBufferClientEndpoint.class);
		invokeSequence("byteBufferTest", "directbytebuffer");
	}

	// Private -----------------------------------------
	private void invokeSequence(String search, String... sequence) throws Exception {
		for (int i = 0; i != sequence.length; i++) {
			setProperty(Property.REQUEST, buildRequest(sequence[i]));
			setProperty(Property.SEARCH_STRING, search);
			setProperty(Property.CONTENT, search);
			invoke();
		}
	}

	private void invokeDataTypeSequence(String search, String type) throws Exception {
		String[] sequence = { "primitive" + type, "full" + type };
		invokeSequence(search, sequence);
	}

}
