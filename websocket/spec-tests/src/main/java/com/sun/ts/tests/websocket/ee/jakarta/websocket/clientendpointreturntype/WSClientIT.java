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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.clientendpointreturntype;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.client.AnnotatedClientEndpoint;
import com.sun.ts.tests.websocket.common.client.ByteBufferClientEndpoint;
import com.sun.ts.tests.websocket.common.client.EndpointCallback;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.stringbean.StringBean;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryEncoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryStreamEncoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextEncoder;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextStreamEncoder;
import com.sun.ts.tests.websocket.common.util.IOUtil;

import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ws_wait;
 */
@ExtendWith(ArquillianExtension.class)
public class WSClientIT extends WebSocketCommonClient {

	private static final long serialVersionUID = 3375865828117749296L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class, "wsc_ee_clientendpointreturntype_web.war");
		archive.addClasses(WSCServer.class);
		archive.addClasses(StringBean.class, StringBeanBinaryEncoder.class, StringBeanBinaryStreamEncoder.class,
				StringBeanTextEncoder.class, StringBeanTextStreamEncoder.class);
		archive.addClasses(IOUtil.class);
		return archive;
	};

	public WSClientIT() throws Exception {
		setContextRoot("wsc_ee_clientendpointreturntype_web");
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
		invokeSequence("true", new WSCPrimitiveBooleanClientEndpoint(), new WSCFullBooleanClientEndpoint());
		invokeSequence("123", new WSCPrimitiveByteClientEndpoint(), new WSCFullByteClientEndpoint());
		invokeSequence(String.valueOf(Short.MAX_VALUE), new WSCPrimitiveShortClientEndpoint(),
				new WSCFullShortClientEndpoint());
		invokeSequence(String.valueOf(Short.MIN_VALUE), new WSCPrimitiveIntClientEndpoint(),
				new WSCFullIntClientEndpoint());
		invokeSequence(String.valueOf(Short.MIN_VALUE), new WSCPrimitiveLongClientEndpoint(),
				new WSCFullLongClientEndpoint());
		invokeSequence(String.valueOf(123.456f), new WSCPrimitiveFloatClientEndpoint(),
				new WSCFullFloatClientEndpoint());
		invokeSequence(String.valueOf(789.012), new WSCPrimitiveDoubleClientEndpoint(),
				new WSCFullDoubleClientEndpoint());
		invokeSequence(String.valueOf('A'), new WSCPrimitiveCharClientEndpoint(), new WSCFullCharClientEndpoint());
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
		invokeSequence("textEncoderTest", new WSCTextEncoderClientEndpoint());
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
		invokeSequence("textStreamEncoderTest", new WSCTextStreamEncoderClientEndpoint());
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
		invokeSequence("binaryEncoderTest", new WSCBinaryEncoderClientEndpoint());
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
		invokeSequence("binaryStreamEncoderTest", new WSCBinaryStreamEncoderClientEndpoint());
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
		invokeSequence("byteArrayTest", new WSCByteArrayClientEndpoint());
	}

	/*
	 * @testName: byteBufferTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: test byte array
	 */
	@Test
	public void byteBufferTest() throws Exception {
		setClientEndpoint(ByteBufferClientEndpoint.class);
		invokeSequence("byteBufferTest", new WSCByteBufferClientEndpoint());
	}

	// Private -----------------------------------------
	private void invokeSequence(String search, AnnotatedClientEndpoint<?>... endpoints) throws Exception {
		for (AnnotatedClientEndpoint<?> endpoint : endpoints) {
			setCountDownLatchCount(3);
			setAnnotatedClientEndpoint(endpoint);
			addClientCallback(new EndpointCallback() {
				@Override
				public void onClose(Session session, CloseReason closeReason) {
					getCountDownLatch().countDown();
					super.onClose(session, closeReason);
				}
			});
			invoke("srv", search, search);
		}
	}

}
