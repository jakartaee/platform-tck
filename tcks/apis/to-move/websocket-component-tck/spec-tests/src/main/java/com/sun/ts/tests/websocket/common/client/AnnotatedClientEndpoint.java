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
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

/**
 * For tests where the annotated client endpoint is to be tested, this
 * predefined endpoint can be used. Though merely a bridge to a ClientEndpoint
 * which can be used to connect to server using it in
 * {@link WebSocketContainer#connectToServer(jakarta.websocket.Endpoint, jakarta.websocket.ClientEndpointConfig, java.net.URI)}
 * , this annotated endpoint is used to test the {@link OnMessage},
 * {@link OnError}, {@link OnOpen}, and {@link OnClose} are being called.
 * 
 * </p>
 * Note that {@link ClientEndpoint} annotation is to be add to subclasses, and
 * {@link OnMessage}, {@link OnError}, {@link OnOpen}, and {@link OnClose} are
 * to be overridden when convenient.
 */
public abstract class AnnotatedClientEndpoint<DATATYPE> {

	protected ClientEndpoint<DATATYPE> clientEndpoint;

	public AnnotatedClientEndpoint(ClientEndpoint<DATATYPE> endpoint) {
		this.clientEndpoint = endpoint;
	}

	// @OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		clientEndpoint.onOpen(session, config, false);
	}

	// @OnMessage
	public void onMessage(DATATYPE msg) {
		clientEndpoint.onMessage(msg);
	}

	// @OnClose
	public void onClose(Session session, CloseReason closeReason) {
		clientEndpoint.onClose(session, closeReason);
	}

	// @OnError
	public void onError(Session session, Throwable t) {
		clientEndpoint.onError(session, t);
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
