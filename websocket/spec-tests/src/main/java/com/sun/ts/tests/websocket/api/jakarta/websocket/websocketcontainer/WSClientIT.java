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

/*
 * $Id:$
 */
package com.sun.ts.tests.websocket.api.jakarta.websocket.websocketcontainer;

import java.lang.System.Logger;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Extension;
import jakarta.websocket.Extension.Parameter;
import jakarta.websocket.WebSocketContainer;

@Tag("websocket")
@Tag("platform")
@Tag("web")

public class WSClientIT {

	private static final Logger logger = System.getLogger(WSClientIT.class.getName());

	/* Run test */
	/*
	 * @testName: getMaxSessionIdleTimeoutTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:167;
	 *
	 * @test_Strategy: Test method getMaxSessionIdleTimeout
	 */
	@Test
	public void getMaxSessionIdleTimeoutTest() throws Exception {
		WebSocketContainer client = ContainerProvider.getWebSocketContainer();

		if (client.getDefaultMaxSessionIdleTimeout() != 0L) {
			logger.log(Logger.Level.TRACE,"Default timeout is: " + client.getDefaultMaxSessionIdleTimeout());
		}
	}

	/*
	 * @testName: setMaxSessionIdleTimeoutTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:167;
	 * WebSocket:JAVADOC:172;
	 *
	 * @test_Strategy: Test method setMaxSessionIdleTimeout
	 */
	@Test
	public void setMaxSessionIdleTimeoutTest() throws Exception {
		long timeout = 987654321L;
		WebSocketContainer client = ContainerProvider.getWebSocketContainer();
		client.setDefaultMaxSessionIdleTimeout(timeout);

		if (client.getDefaultMaxSessionIdleTimeout() != timeout) {
			throw new Exception("Test failed. getMaxSessionIdleTimeout didn't return set value." + "Expecting "
					+ timeout + "; got " + client.getDefaultMaxSessionIdleTimeout());
		}
	}

	/*
	 * @testName: getMaxTextMessageBufferSizeTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:168;
	 *
	 * @test_Strategy: Test method getMaxTextMessageBufferSize
	 */
	@Test
	public void getMaxTextMessageBufferSizeTest() throws Exception {
		WebSocketContainer client = ContainerProvider.getWebSocketContainer();
		long default_msgsize = client.getDefaultMaxTextMessageBufferSize();
		logger.log(Logger.Level.INFO,"Default MaxTextMessageBufferSize is " + default_msgsize);
	}

	/*
	 * @testName: setMaxTextMessageBufferSizeTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:168;
	 * WebSocket:JAVADOC:173;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void setMaxTextMessageBufferSizeTest() throws Exception {
		int expected_msgsize = 987654321;

		WebSocketContainer client = ContainerProvider.getWebSocketContainer();
		long default_msgsize = client.getDefaultMaxTextMessageBufferSize();
		logger.log(Logger.Level.INFO,"Default MaxTextMessageBufferSize is " + default_msgsize);

		client.setDefaultMaxTextMessageBufferSize(expected_msgsize);
		long actual_msgsize = client.getDefaultMaxTextMessageBufferSize();
		if (expected_msgsize != actual_msgsize) {
			throw new Exception("DefaultMaxTextMessageBufferSize does not match. " + "Expecting " + expected_msgsize
					+ ", got " + actual_msgsize);
		}
	}

	/*
	 * @testName: getMaxBinaryMessageBufferSizeTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:166;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void getMaxBinaryMessageBufferSizeTest() throws Exception {
		WebSocketContainer client = ContainerProvider.getWebSocketContainer();
		long default_msgsize = client.getDefaultMaxBinaryMessageBufferSize();
		logger.log(Logger.Level.INFO,"Default MaxTextMessageBufferSize is " + default_msgsize);
	}

	/*
	 * @testName: setMaxBinaryMessageBufferSizeTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:166;
	 * WebSocket:JAVADOC:171;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void setMaxBinaryMessageBufferSizeTest() throws Exception {
		int expected_msgsize = 987654321;

		WebSocketContainer client = ContainerProvider.getWebSocketContainer();
		long default_msgsize = client.getDefaultMaxBinaryMessageBufferSize();
		logger.log(Logger.Level.INFO,"Default MaxBinaryMessageBufferSize is " + default_msgsize);

		client.setDefaultMaxBinaryMessageBufferSize(expected_msgsize);
		long actual_msgsize = client.getDefaultMaxBinaryMessageBufferSize();
		if (expected_msgsize != actual_msgsize) {
			throw new Exception("DefaultMaxBinaryMessageBufferSize does not match. " + "Expecting " + expected_msgsize
					+ ", got " + actual_msgsize);
		}
	}

	/*
	 * @testName: getDefaultAsyncSendTimeoutTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:165;
	 * 
	 * @test_Strategy: Test method getMaxSessionIdleTimeout
	 */
	@Test
	public void getDefaultAsyncSendTimeoutTest() throws Exception {
		WebSocketContainer client = ContainerProvider.getWebSocketContainer();

		if (client.getDefaultAsyncSendTimeout() != 0L) {
			logger.log(Logger.Level.TRACE,"Default timeout is: " + client.getDefaultMaxSessionIdleTimeout());
		}
	}

	/*
	 * @testName: setAsyncSendTimeoutTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:165;
	 * WebSocket:JAVADOC:170;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void setAsyncSendTimeoutTest() throws Exception {
		long expected_timeout = 987654321L;
		WebSocketContainer client = ContainerProvider.getWebSocketContainer();
		client.setAsyncSendTimeout(expected_timeout);

		long actual_timeout = client.getDefaultAsyncSendTimeout();
		if (actual_timeout != expected_timeout) {
			throw new Exception("Test failed. getDefaultAsyncSendTimeout didn't return set value." + "Expecting "
					+ expected_timeout + "; got " + actual_timeout);
		}
	}

	/*
	 * @testName: getInstalledExtensionsTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:28; WebSocket:JAVADOC:169;
	 *
	 * @test_Strategy:
	 */
	@Test
	public void getInstalledExtensionsTest() throws Exception {
		WebSocketContainer client = ContainerProvider.getWebSocketContainer();

		Set<Extension> extensions = client.getInstalledExtensions();
		if (extensions != null) {
			if (!extensions.isEmpty()) {
				for (Extension tmp : extensions) {
					logger.log(Logger.Level.INFO,"Installed Extension: " + tmp.getName());
					List<Parameter> params = tmp.getParameters();
					for (Parameter tmp1 : params) {
						logger.log(Logger.Level.INFO,"Parameter's name= " + tmp1.getName() + "Parameter's value= " + tmp1.getValue());
					}
				}
			} else {
				logger.log(Logger.Level.INFO,"Installed Extension returned empty set");
			}
		} else {
			logger.log(Logger.Level.INFO,"getInstalledExtensions() returned null");
		}
	}

	public void cleanup() {
	}
}
