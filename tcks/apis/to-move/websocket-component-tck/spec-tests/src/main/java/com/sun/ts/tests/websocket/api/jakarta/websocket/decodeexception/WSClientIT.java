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

/*
 * $Id:$
 */
package com.sun.ts.tests.websocket.api.jakarta.websocket.decodeexception;

import java.lang.System.Logger;
import java.nio.ByteBuffer;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import jakarta.websocket.DecodeException;

@Tag("websocket")
@Tag("platform")
@Tag("web")

public class WSClientIT {

	private static final Logger logger = System.getLogger(WSClientIT.class.getName());

	/* Run test */
	/*
	 * @testName: constructorTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:32; WebSocket:JAVADOC:33;
	 * WebSocket:JAVADOC:34;
	 *
	 * @test_Strategy: Test constructor DecodeException(String, String)
	 */
	@Test
	public void constructorTest() throws Exception {
		boolean passed = true;
		String reason = "TCK: Cannot decode the message";
		String encoded_message = "xyz for now";
		StringBuilder tmp = new StringBuilder();

		DecodeException dex = new DecodeException(encoded_message, reason);

		if (!encoded_message.equals(dex.getText())) {
			passed = false;
			tmp.append("Expected message ").append(encoded_message).append(", returned").append(dex.getText());
		}

		if (dex.getBytes() != null) {
			passed = false;
			tmp.append("Expected ByteBuffer  to be null, returned").append(dex.getBytes());
		}

		if (passed == false) {
			throw new Exception("Test failed: " + tmp.toString());
		}
	}

	/*
	 * @testName: constructorTest1
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:31; WebSocket:JAVADOC:33;
	 * WebSocket:JAVADOC:34;
	 *
	 * @test_Strategy: Test constructor DecodeException(ByteBuffer, String)
	 */
	@Test
	public void constructorTest1() throws Exception {
		boolean passed = true;
		String reason = "TCK: Cannot decode the message";
		ByteBuffer encoded_message = ByteBuffer.allocate(20);
		encoded_message.put("xyz for now".getBytes());

		DecodeException dex = new DecodeException(encoded_message, reason);

		if (dex.getText() != null) {
			passed = false;
			logger.log(Logger.Level.ERROR,"Expected encoded_message null, returned" + dex.getText());
		}

		if (!encoded_message.equals(dex.getBytes())) {
			passed = false;
			logger.log(Logger.Level.ERROR,"Expected ByteBuffer " + encoded_message + ", returned" + dex.getBytes());
		}

		if (passed == false) {
			throw new Exception("Test failed");
		}
	}

	/*
	 * @testName: constructorTest2
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:30; WebSocket:JAVADOC:33;
	 * WebSocket:JAVADOC:34;
	 *
	 * @test_Strategy: Test constructor DecodeException(String, String, Throwable)
	 */
	@Test
	public void constructorTest2() throws Exception {
		boolean passed = true;
		String reason = "TCK: Cannot decode the message";
		String encoded_message = "xyz for now";

		DecodeException dex = new DecodeException(encoded_message, reason, new Throwable("CocntructorTest2"));

		if (!encoded_message.equals(dex.getText())) {
			passed = false;
			logger.log(Logger.Level.ERROR,"Expected encoded_message " + encoded_message + ", returned" + dex.getText());
		}

		if (dex.getBytes() != null) {
			passed = false;
			logger.log(Logger.Level.ERROR,"Expected ByteBuffer null, returned" + dex.getBytes());
		}

		if (passed == false) {
			throw new Exception("Test failed");
		}
	}

	/*
	 * @testName: constructorTest3
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:29; WebSocket:JAVADOC:33;
	 * WebSocket:JAVADOC:34;
	 *
	 * @test_Strategy: Test constructor DecodeException(ByteBuffer, String,
	 * Throwable)
	 */
	@Test
	public void constructorTest3() throws Exception {
		boolean passed = true;
		String reason = "TCK: Cannot decode the message";
		ByteBuffer encoded_message = ByteBuffer.allocate(20);
		encoded_message.put("xyz for now".getBytes());

		DecodeException dex = new DecodeException(encoded_message, reason, new Throwable("constructorTest3"));

		if (dex.getText() != null) {
			passed = false;
			logger.log(Logger.Level.ERROR,"Expected encoded_message null, returned" + dex.getText());
		}

		if (!encoded_message.equals(dex.getBytes())) {
			passed = false;
			logger.log(Logger.Level.ERROR,"Expected ByteBuffer " + encoded_message + ", returned" + dex.getBytes());
		}

		if (passed == false) {
			throw new Exception("Test failed");
		}
	}

	public void cleanup() {
	}
}
