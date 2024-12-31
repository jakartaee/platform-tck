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

package com.sun.ts.tests.websocket.common.client;

import java.util.concurrent.CountDownLatch;

import jakarta.websocket.CloseReason;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.MessageHandler.Partial;
import jakarta.websocket.MessageHandler.Whole;
import jakarta.websocket.Session;

/**
 * Default empty implementation of merged functionality of {@link Endpoint}
 * interface with {@link MessageHandler} interface.
 * </p>
 * Depending on what {@link WebSocketCommonClient.Entity} is defined (partial,
 * whole), {@link Whole} or {@link Partial} functionality is used with onMessage
 */
public class EndpointCallback {

	@SuppressWarnings("unused")
	public void onError(Session session, Throwable t) {
	}

	@SuppressWarnings("unused")
	public void onMessage(Object o) {
	}

	@SuppressWarnings("unused")
	public void onOpen(Session session, EndpointConfig config) {
	}

	@SuppressWarnings("unused")
	public void onClose(Session session, CloseReason closeReason) {
	}

	public CountDownLatch getCountDownLatch() {
		return ClientEndpoint.getCountDownLatch();
	}

	public StringBuffer getMessageBuilder() {
		return ClientEndpoint.getMessageBuilder();
	}

	public Throwable getLastError() {
		return ClientEndpoint.getLastError();
	}
}
