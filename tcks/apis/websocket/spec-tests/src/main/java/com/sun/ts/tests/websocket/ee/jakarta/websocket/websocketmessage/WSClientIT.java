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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.websocketmessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.client.EndpointCallback;
import com.sun.ts.tests.websocket.common.client.SendMessageCallback;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.impl.StringPongMessage;
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

	private static final long serialVersionUID = 932557106496525508L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "ws_ee_websocketmessage_web.war");
		archive.addPackages(false, Filters.exclude(WSClientIT.class),
				"com.sun.ts.tests.websocket.ee.jakarta.websocket.websocketmessage");
		archive.addPackages(true, "com.sun.ts.tests.websocket.common.stringbean");
		archive.addClasses(IOUtil.class);
		return archive;
	};

	static final String ECHO = "Echo message to be sent to server endpoint";

	static final String[] TEXT_PRIMITIVE_SEQUENCE = { "byte", "short", "int", "long", "double", "float" };

	static final String[] TEXT_TYPE_SEQUENCE = { "string", "reader", "textdecoder", "textstreamdecoder" };

	static final String[] BINARY_SEQUENCE = { "bytearray", "bytebuffer", "inputstream", "binarydecoder",
			"binarystreamdecoder" };

	public static int getIndex(String item) {
		for (int i = 0; i != TEXT_PRIMITIVE_SEQUENCE.length; i++)
			if (TEXT_PRIMITIVE_SEQUENCE[i].equals(item))
				return i;
		return -1;
	}

	public WSClientIT() throws Exception {
		setContextRoot("ws_ee_websocketmessage_web");
	}

	/* Run test */

	// TEXT ------------------------------------------

	/*
	 * @testName: echoStringTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: Echo String
	 */
	@Test
	public void echoStringTest() throws Exception {
		setProperty(Property.REQUEST, buildRequest("string"));
		setProperty(Property.CONTENT, ECHO);
		setProperty(Property.SEARCH_STRING, ECHO);
		invoke();
	}

	/*
	 * @testName: echoIntTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: echo int
	 */
	@Test
	public void echoIntTest() throws Exception {
		int entity = Integer.MIN_VALUE;
		setEntity(entity);
		setProperty(Property.REQUEST, buildRequest("primitiveint"));
		setProperty(Property.SEARCH_STRING, String.valueOf(entity));
		invoke();
	}

	/*
	 * @testName: echoByteTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: echo byte
	 */
	@Test
	public void echoByteTest() throws Exception {
		byte entity = 123;
		setEntity(entity);
		setProperty(Property.REQUEST, buildRequest("primitivebyte"));
		setProperty(Property.SEARCH_STRING, String.valueOf(entity));
		invoke();
	}

	/*
	 * @testName: echoCharTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: echo char
	 */
	@Test
	public void echoCharTest() throws Exception {
		char entity = 'E';
		setEntity(entity);
		setProperty(Property.REQUEST, buildRequest("primitivechar"));
		setProperty(Property.SEARCH_STRING, String.valueOf(entity) + "char");
		invoke();
	}

	/*
	 * @testName: echoBooleanTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: echo boolean
	 */
	@Test
	public void echoBooleanTest() throws Exception {
		boolean entity = true;
		setEntity(entity);
		setProperty(Property.REQUEST, buildRequest("primitiveboolean"));
		setProperty(Property.SEARCH_STRING, String.valueOf(entity));
		invoke();
	}

	/*
	 * @testName: echoShortTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: echo short
	 */
	@Test
	public void echoShortTest() throws Exception {
		short entity = -32100;
		setEntity(entity);
		setProperty(Property.REQUEST, buildRequest("primitiveshort"));
		setProperty(Property.SEARCH_STRING, String.valueOf(entity));
		invoke();
	}

	/*
	 * @testName: echoDoubleTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: echo double
	 */
	@Test
	public void echoDoubleTest() throws Exception {
		double entity = -12345678.88;
		setEntity(entity);
		setProperty(Property.REQUEST, buildRequest("primitivedouble"));
		setProperty(Property.SEARCH_STRING, String.valueOf(entity));
		invoke();
	}

	/*
	 * @testName: echoFloatTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: echo float
	 */
	@Test
	public void echoFloatTest() throws Exception {
		float entity = -12345678.88f;
		setEntity(entity);
		setProperty(Property.REQUEST, buildRequest("primitivefloat"));
		setProperty(Property.SEARCH_STRING, String.valueOf(entity));
		invoke();
	}

	/*
	 * @testName: echoLongTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: echo long
	 */
	@Test
	public void echoLongTest() throws Exception {
		long entity = Long.MIN_VALUE;
		setEntity(entity);
		setProperty(Property.REQUEST, buildRequest("primitivelong"));
		setProperty(Property.SEARCH_STRING, String.valueOf(entity));
		invoke();
	}

	/*
	 * @testName: echoFullIntTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: echo int
	 */
	@Test
	public void echoFullIntTest() throws Exception {
		Integer entity = Integer.MIN_VALUE;
		setEntity(entity);
		setProperty(Property.REQUEST, buildRequest("fullint"));
		setProperty(Property.SEARCH_STRING, String.valueOf(entity));
		invoke();
	}

	/*
	 * @testName: echoFullByteTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: echo byte
	 */
	@Test
	public void echoFullByteTest() throws Exception {
		Byte entity = Byte.MIN_VALUE;
		setEntity(entity);
		setProperty(Property.REQUEST, buildRequest("fullbyte"));
		setProperty(Property.SEARCH_STRING, String.valueOf(entity));
		invoke();
	}

	/*
	 * @testName: echoFullCharTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: echo char
	 */
	@Test
	public void echoFullCharTest() throws Exception {
		Character entity = 'E';
		setEntity(entity);
		setProperty(Property.REQUEST, buildRequest("fullchar"));
		setProperty(Property.SEARCH_STRING, String.valueOf(entity));
		invoke();
	}

	/*
	 * @testName: echoFullBooleanTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: echo boolean
	 */
	@Test
	public void echoFullBooleanTest() throws Exception {
		Boolean entity = true;
		setEntity(entity);
		setProperty(Property.REQUEST, buildRequest("fullboolean"));
		setProperty(Property.SEARCH_STRING, String.valueOf(entity));
		invoke();
	}

	/*
	 * @testName: echoFullShortTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: echo short
	 */
	@Test
	public void echoFullShortTest() throws Exception {
		Short entity = Short.MAX_VALUE;
		setEntity(entity);
		setProperty(Property.REQUEST, buildRequest("fullshort"));
		setProperty(Property.SEARCH_STRING, String.valueOf(entity));
		invoke();
	}

	/*
	 * @testName: echoFullDoubleTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: echo double
	 */
	@Test
	public void echoFullDoubleTest() throws Exception {
		Double entity = Double.MIN_VALUE;
		setEntity(entity);
		setProperty(Property.REQUEST, buildRequest("fulldouble"));
		setProperty(Property.SEARCH_STRING, String.valueOf(entity));
		invoke();
	}

	/*
	 * @testName: echoFullFloatTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: echo float
	 */
	@Test
	public void echoFullFloatTest() throws Exception {
		Float entity = Float.MAX_VALUE;
		setEntity(entity);
		setProperty(Property.REQUEST, buildRequest("fullfloat"));
		setProperty(Property.SEARCH_STRING, String.valueOf(entity));
		invoke();
	}

	/*
	 * @testName: echoFullLongTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-3; WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: echo long
	 */
	@Test
	public void echoFullLongTest() throws Exception {
		Long entity = Long.MAX_VALUE;
		setEntity(entity);
		setProperty(Property.REQUEST, buildRequest("fulllong"));
		setProperty(Property.SEARCH_STRING, String.valueOf(entity));
		invoke();
	}

	// ------------------------------------------------------------------

	/*
	 * @testName: byteToTextsTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
	 * WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send byte receive other texts
	 */
	@Test
	public void byteToTextsTest() throws Exception {
		byte entity = 123;
		setEntity(entity);
		int index = getIndex("byte");
		invokeTextSequences(index + 1, String.valueOf(entity));
		invokeTextSequencesWithPathParam(index, String.valueOf(entity), "101");
	}

	/*
	 * @testName: shortToTextsTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
	 * WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send short receive other
	 */
	@Test
	public void shortToTextsTest() throws Exception {
		short entity = Short.MAX_VALUE;
		setEntity(entity);
		int index = getIndex("short");
		invokeTextSequences(index + 1, String.valueOf(entity));
		invokeTextSequencesWithPathParam(index, String.valueOf(entity), "1001");
	}

	/*
	 * @testName: intToTextsTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
	 * WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send int receive other
	 */
	@Test
	public void intToTextsTest() throws Exception {
		int entity = Short.MAX_VALUE; // Integer.MAX_VALUE is
		// 2.147483647E9 at Double
		setEntity(entity);
		int index = getIndex("int");
		invokeTextSequences(index + 1, String.valueOf(entity));
		invokeTextSequencesWithPathParam(index, String.valueOf(entity), "100001");
	}

	/*
	 * @testName: longToTextsTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
	 * WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send long receive other
	 */
	@Test
	public void longToTextsTest() throws Exception {
		long entity = Short.MIN_VALUE; // Long.MIN_VALUE is
		// -9.223372036854776E18 at Double
		setEntity(entity);
		int index = getIndex("long");
		invokeTextSequences(index + 1, String.valueOf(entity));
		invokeTextSequencesWithPathParam(index, String.valueOf(entity), "100001");
	}

	/*
	 * @testName: floatToTextsTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
	 * WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send float receive other
	 */
	@Test
	public void floatToTextsTest() throws Exception {
		float entity = Float.MIN_VALUE;
		setEntity(entity);
		int index = getIndex("float");
		invokeTextSequences(index + 1, String.valueOf(entity));
		invokeTextSequencesWithPathParam(index, String.valueOf(entity), String.valueOf(101.101f));
	}

	/*
	 * @testName: doubleToTextsTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
	 * WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send double receive other
	 */
	@Test
	public void doubleToTextsTest() throws Exception {
		double entity = Double.MAX_VALUE;
		setEntity(entity);
		invokeSequence(0, String.valueOf(entity), "", "", TEXT_TYPE_SEQUENCE);
		invokeSequence(0, String.valueOf(entity), "", "session", TEXT_TYPE_SEQUENCE);
		String pathParam = String.valueOf(101.101);
		invokeSequence(0, String.valueOf(entity) + pathParam, "", "pathparam/" + pathParam, TEXT_TYPE_SEQUENCE);
		invokeSequence(0, String.valueOf(entity) + pathParam, "", "sessionpathparam/" + pathParam, TEXT_TYPE_SEQUENCE);
	}

	/*
	 * @testName: stringToTextsTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
	 * WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send string receive other
	 */
	@Test
	public void stringToTextsTest() throws Exception {
		String param = "StringParam";
		setEntity(ECHO);
		invokeSequence(0, ECHO, "", "", TEXT_TYPE_SEQUENCE);
		invokeSequence(0, ECHO, "", "session", TEXT_TYPE_SEQUENCE);
		invokeSequence(0, ECHO + param, "", "pathparam/" + param, TEXT_TYPE_SEQUENCE);
		invokeSequence(0, ECHO + param, "", "sessionpathparam/" + param, TEXT_TYPE_SEQUENCE);
	}

	/*
	 * @testName: stringToAllNumbersTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
	 * WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send string receive other
	 */
	@Test
	public void stringToAllNumbersTest() throws Exception {
		String entity = String.valueOf(Byte.MAX_VALUE);
		String param = "111";
		setEntity(entity);
		invokeSequence(0, String.valueOf(entity), "primitive", "", TEXT_PRIMITIVE_SEQUENCE);
		invokeSequence(0, String.valueOf(entity), "full", "", TEXT_PRIMITIVE_SEQUENCE);
		invokeSequence(0, String.valueOf(entity), "primitive", "session", TEXT_PRIMITIVE_SEQUENCE);
		invokeSequence(0, String.valueOf(entity), "full", "session", TEXT_PRIMITIVE_SEQUENCE);

		invokeSequence(0, String.valueOf(entity) + param, "primitive", "pathparam/" + param, TEXT_PRIMITIVE_SEQUENCE);
		invokeSequence(0, String.valueOf(entity) + param, "full", "pathparam/" + param, TEXT_PRIMITIVE_SEQUENCE);
		invokeSequence(0, String.valueOf(entity) + param, "primitive", "sessionpathparam/" + param,
				TEXT_PRIMITIVE_SEQUENCE);
		invokeSequence(0, String.valueOf(entity) + param, "full", "sessionpathparam/" + param, TEXT_PRIMITIVE_SEQUENCE);
	}

	/*
	 * @testName: booleanToBooleanWithPathParamTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
	 * WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send boolean receive boolean
	 */
	@Test
	public void booleanToBooleanWithPathParamTest() throws Exception {
		boolean entity = true;
		String[] SEQUENCE = { "primitiveboolean", "fullboolean" };
		setEntity(entity);
		invokeSequence(0, "true", "", "", SEQUENCE);
		invokeSequence(0, "true", "", "session", SEQUENCE);
		invokeSequence(0, "truefalse", "", "pathparam/false", SEQUENCE);
		invokeSequence(0, "truefalse", "", "sessionpathparam/false", SEQUENCE);
	}

	/*
	 * @testName: stringToBooleanTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
	 * WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send string receive boolean
	 */
	@Test
	public void stringToBooleanTest() throws Exception {
		String entity = "true";
		String[] SEQUENCE = { "primitiveboolean", "fullboolean" };
		setEntity(entity);
		invokeSequence(0, entity, "", "", SEQUENCE);
		invokeSequence(0, entity, "", "session", SEQUENCE);
		invokeSequence(0, "truefalse", "", "pathparam/false", SEQUENCE);
		invokeSequence(0, "truefalse", "", "sessionpathparam/false", SEQUENCE);
	}

	/*
	 * @testName: stringToCharTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
	 * WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send string receive char
	 */
	@Test
	public void stringToCharTest() throws Exception {
		String entity = "&";
		String[] SEQUENCE = { "primitivechar", "fullchar" };
		setEntity(entity);
		invokeSequence(0, entity, "", "", SEQUENCE);
		invokeSequence(0, entity, "", "session", SEQUENCE);
		invokeSequence(0, entity + "X", "", "pathparam/X", SEQUENCE);
		invokeSequence(0, entity + "y", "", "sessionpathparam/y", SEQUENCE);
	}

	/*
	 * @testName: charToTextsTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
	 * WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send char receive other
	 */
	@Test
	public void charToTextsTest() throws Exception {
		char entity = '1';
		setEntity(entity);
		invokeTextSequences(0, String.valueOf(entity));
		invokeTextSequencesWithPathParam(0, String.valueOf(entity), "99");
	}

	/*
	 * @testName: partialStringTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
	 * WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send string and string receive the strings, too
	 */
	@Test
	public void partialStringTest() throws Exception {
		setCountDownLatchCount(2);
		String partial2 = "partialStringTest";
		String response = ECHO + "(false)" + partial2 + "(true)";
		setEntity(ECHO, partial2);
		setProperty(Property.REQUEST, buildRequest("partialstring"));
		setProperty(Property.SEARCH_STRING, response);
		invoke();
	}

	/*
	 * @testName: partialStringAndSessionTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
	 * WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send string and string receive the combined string
	 */
	@Test
	public void partialStringAndSessionTest() throws Exception {
		String partial2 = "partialStringAndSessionTest";
		String response = ECHO + "(false)" + partial2 + "(true)";
		setEntity(ECHO, partial2);
		setProperty(Property.REQUEST, buildRequest("partialstringsession"));
		setProperty(Property.SEARCH_STRING, response);
		invoke();
	}

	/*
	 * @testName: partialStringWithPathParamTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
	 * WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send string and string receive the strings, too
	 */
	@Test
	public void partialStringWithPathParamTest() throws Exception {
		setCountDownLatchCount(2);
		String partial2 = "partialStringTest";
		String response = ECHO + "(false)" + partial2 + partial2 + "(true)" + partial2;
		setEntity(ECHO, partial2);
		setProperty(Property.REQUEST, buildRequest("partialstringpathparam/" + partial2));
		setProperty(Property.SEARCH_STRING, response);
		invoke();
	}

	/*
	 * @testName: partialStringAndSessionWithPathParamTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-4.7-2; WebSocket:SPEC:WSC-4.7-3;
	 * WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send string and string receive the combined string
	 */
	@Test
	public void partialStringAndSessionWithPathParamTest() throws Exception {
		String partial2 = "partialStringAndSessionTest";
		String response = ECHO + "(false)" + partial2 + partial2 + "(true)" + partial2;
		setEntity(ECHO, partial2);
		setProperty(Property.REQUEST, buildRequest("partialstringsessionpathparam/" + partial2));
		setProperty(Property.SEARCH_STRING, response);
		invoke();
	}

	// ----------------- BINARY -----------------------------------

	/*
	 * @testName: echoByteBufferTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: Echo ByteBuffer
	 */
	@Test
	public void echoByteBufferTest() throws Exception {
		setEntity(ByteBuffer.wrap(ECHO.getBytes()));
		setProperty(Property.REQUEST, buildRequest("bytebuffer"));
		setProperty(Property.SEARCH_STRING, ECHO);
		invoke();
	}

	/*
	 * @testName: byteBufferToBytesTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: Send ByteBuffer receive bytes
	 */
	@Test
	public void byteBufferToBytesTest() throws Exception {
		setEntity(ByteBuffer.wrap(ECHO.getBytes()));
		setProperty(Property.REQUEST, buildRequest("bytearray"));
		setProperty(Property.SEARCH_STRING, ECHO);
		invoke();
	}

	/*
	 * @testName: byteBufferToBinaryTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: echo byte array
	 */
	@Test
	public void byteBufferToBinaryTest() throws Exception {
		String param = "byteBufferToBinaryTest";
		setEntity(ByteBuffer.wrap(ECHO.getBytes()));
		invokeSequence(0, ECHO, "", "", BINARY_SEQUENCE);
		invokeSequence(0, ECHO, "", "session", BINARY_SEQUENCE);
		invokeSequence(0, ECHO + param, "", "pathparam/" + param, BINARY_SEQUENCE);
		invokeSequence(0, ECHO + param, "", "sessionpathparam/" + param, BINARY_SEQUENCE);
	}

	/*
	 * @testName: pongToPongTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: echo byte array
	 */
	@Test
	public void pongToPongTest() throws Exception {
		setEntity(new StringPongMessage(ECHO));
		String[] sequence = { "pongmessage", "pongmessagesession" };
		invokeSequence(0, ECHO, "", "", sequence);
		String[] paramsequence = { "pongmessagepathparam/param", "pongmessagesessionpathparam/param" };
		invokeSequence(0, ECHO + "param", "", "", paramsequence);
	}

	/*
	 * @testName: partialByteArrayTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send bytearray and bytearray receive the combined message
	 */
	@Test
	public void partialByteArrayTest() throws Exception {
		setCountDownLatchCount(2);
		String partial2 = "partialByteArrayTest";
		String response = ECHO + "(false)" + partial2 + "(true)";
		setEntity(ByteBuffer.wrap(ECHO.getBytes()), ByteBuffer.wrap(partial2.getBytes()));
		setProperty(Property.REQUEST, buildRequest("partialbytearray"));
		setProperty(Property.SEARCH_STRING, response);
		invoke();
	}

	/*
	 * @testName: partialByteArrayAndSessionTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send bytearray and bytearray receive the combined message
	 */
	@Test
	public void partialByteArrayAndSessionTest() throws Exception {
		String partial2 = "partialByteArrayAndSessionTest";
		String response = ECHO + "(false)" + partial2 + "(true)";
		setEntity(ByteBuffer.wrap(ECHO.getBytes()), ByteBuffer.wrap(partial2.getBytes()));
		setProperty(Property.REQUEST, buildRequest("partialbytearraysession"));
		setProperty(Property.SEARCH_STRING, response);
		invoke();
	}

	/*
	 * @testName: partialByteArrayWithPathParamTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send bytearray and bytearray receive the combined message
	 */
	@Test
	public void partialByteArrayWithPathParamTest() throws Exception {
		setCountDownLatchCount(2);
		String partial2 = "partialByteArrayWithPathParamTest";
		String response = ECHO + "(false)[" + partial2 + "]" + partial2 + "(true)[" + partial2 + "]";
		setEntity(ByteBuffer.wrap(ECHO.getBytes()), ByteBuffer.wrap(partial2.getBytes()));
		setProperty(Property.REQUEST, buildRequest("partialbytearraypathparam/" + partial2));
		setProperty(Property.SEARCH_STRING, response);
		invoke();
	}

	/*
	 * @testName: partialByteArrayAndSessionWithPathParamTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send bytearray and bytearray receive the combined message
	 */
	@Test
	public void partialByteArrayAndSessionWithPathParamTest() throws Exception {
		String partial2 = "partialByteArrayAndSessionWithPathParamTest";
		String response = ECHO + "(false)[" + partial2 + "]" + partial2 + "(true)[" + partial2 + "]";
		setEntity(ByteBuffer.wrap(ECHO.getBytes()), ByteBuffer.wrap(partial2.getBytes()));
		setProperty(Property.REQUEST, buildRequest("partialbytearraysessionpathparam/" + partial2));
		setProperty(Property.SEARCH_STRING, response);
		invoke();
	}

	/*
	 * @testName: partialByteBufferTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send ByteBuffer and ByteBuffer receive the combined message
	 */
	@Test
	public void partialByteBufferTest() throws Exception {
		setCountDownLatchCount(2);
		String partial2 = "partialByteBufferTest";
		String response = ECHO + "(false)" + partial2 + "(true)";
		setEntity(ByteBuffer.wrap(ECHO.getBytes()), ByteBuffer.wrap(partial2.getBytes()));
		setProperty(Property.REQUEST, buildRequest("partialbytebuffer"));
		setProperty(Property.SEARCH_STRING, response);
		invoke();
	}

	/*
	 * @testName: partialByteBufferAndSessionTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send ByteBuffer and ByteBuffer receive the combined message
	 */
	@Test
	public void partialByteBufferAndSessionTest() throws Exception {
		String partial2 = "partialByteBufferAndSessionTest";
		String response = ECHO + "(false)" + partial2 + "(true)";
		setEntity(ByteBuffer.wrap(ECHO.getBytes()), ByteBuffer.wrap(partial2.getBytes()));
		setProperty(Property.REQUEST, buildRequest("partialbytebuffersession"));
		setProperty(Property.SEARCH_STRING, response);
		invoke();
	}

	/*
	 * @testName: partialByteBufferWithPathParamTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send ByteBuffer and ByteBuffer receive the combined message
	 */
	@Test
	public void partialByteBufferWithPathParamTest() throws Exception {
		setCountDownLatchCount(2);
		String partial2 = "partialByteBufferWithPathParamTest";
		String response = ECHO + "(false)[" + partial2 + "]" + partial2 + "(true)[" + partial2 + "]";
		setEntity(ByteBuffer.wrap(ECHO.getBytes()), ByteBuffer.wrap(partial2.getBytes()));
		setProperty(Property.REQUEST, buildRequest("partialbytebufferpathparam/" + partial2));
		setProperty(Property.SEARCH_STRING, response);
		invoke();
	}

	/*
	 * @testName: partialByteBufferAndSessionWithPathParamTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: send ByteBuffer and ByteBuffer receive the combined message
	 */
	@Test
	public void partialByteBufferAndSessionWithPathParamTest() throws Exception {
		String partial2 = "partialByteBufferAndSessionWithPathParamTest";
		String response = ECHO + "(false)[" + partial2 + "]" + partial2 + "(true)[" + partial2 + "]";
		setEntity(ByteBuffer.wrap(ECHO.getBytes()), ByteBuffer.wrap(partial2.getBytes()));
		setProperty(Property.REQUEST, buildRequest("partialbytebuffersessionpathparam/" + partial2));
		setProperty(Property.SEARCH_STRING, response);
		invoke();
	}

	// --------------------------------- MAX LEN
	/*
	 * @testName: maxLengthOKTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: the message is shorter than maxMessageLength
	 */
	@Test
	public void maxLengthOKTest() throws Exception {
		String entity = "12345";
		setEntity(entity);
		setProperty(Property.REQUEST, buildRequest("maxlen"));
		setProperty(Property.SEARCH_STRING, entity);
		invoke();
	}

	/*
	 * @testName: maxLengthFailsTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80; WebSocket:SPEC:WSC-4.7.1-1;
	 * 
	 * @test_Strategy: the message is longer than maxMessageLength
	 */
	@Test
	public void maxLengthFailsTest() throws Exception {
		final AtomicInteger ai = new AtomicInteger(0);
		setEntity("123456");
		EndpointCallback callback = new SendMessageCallback(entity) {
			@Override
			public void onClose(Session session, CloseReason closeReason) {
				ai.set(closeReason.getCloseCode().getCode());
				getCountDownLatch().countDown();
			}
		};
		setClientCallback(callback);
		setProperty(Property.REQUEST, buildRequest("maxlen"));
		invoke(false);
		assertEqualsInt(1009, ai.get(), "Unexpected close reason found", ai.get());
		logMsg("Found expected close reason code", 1009);
	}

	/*
	 * @testName: defaultMaxLengthTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:80;
	 * 
	 * @test_Strategy: Default -1;
	 */
	@Test
	public void defaultMaxLengthTest() throws Exception {
		setEntity("123456789");
		setProperty(Property.REQUEST, buildRequest("defaultmaxlen"));
		setProperty(Property.SEARCH_STRING, "-1");
		invoke();
	}

	// ////////////////////////////////////////////////////////////////////
	private void invokeSequence(int startIndex, String search, String prefix, String suffix, String[] sequence)
			throws Exception {
		for (int i = startIndex; i != sequence.length; i++) {
			setProperty(Property.REQUEST, buildRequest(prefix, sequence[i], suffix));
			setProperty(Property.SEARCH_STRING, search);
			invoke();
		}
	}

	private void invokeTextSequences(int startIndex, String search) throws Exception {
		invokeSequence(startIndex, search, "primitive", "", TEXT_PRIMITIVE_SEQUENCE);
		invokeSequence(startIndex, search, "full", "", TEXT_PRIMITIVE_SEQUENCE);
		invokeSequence(0, search, "", "", TEXT_TYPE_SEQUENCE);
		invokeSequence(startIndex, search, "primitive", "session", TEXT_PRIMITIVE_SEQUENCE);
		invokeSequence(startIndex, search, "full", "session", TEXT_PRIMITIVE_SEQUENCE);
		invokeSequence(0, search, "", "session", TEXT_TYPE_SEQUENCE);
	}

	private void invokeTextSequencesWithPathParam(int startIndex, String content, String pathParam) throws Exception {
		String suffix = "pathparam/" + pathParam;
		String search = content + pathParam;
		invokeSequence(startIndex, search, "primitive", suffix, TEXT_PRIMITIVE_SEQUENCE);
		invokeSequence(startIndex, search, "full", suffix, TEXT_PRIMITIVE_SEQUENCE);
		invokeSequence(0, search, "", suffix, TEXT_TYPE_SEQUENCE);
		invokeSequence(startIndex, search, "primitive", "session" + suffix, TEXT_PRIMITIVE_SEQUENCE);
		invokeSequence(startIndex, search, "full", "session" + suffix, TEXT_PRIMITIVE_SEQUENCE);
		invokeSequence(0, search, "", "session" + suffix, TEXT_TYPE_SEQUENCE);

	}
}
