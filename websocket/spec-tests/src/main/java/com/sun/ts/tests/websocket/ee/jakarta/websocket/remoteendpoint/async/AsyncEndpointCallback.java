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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.remoteendpoint.async;

import com.sun.ts.tests.websocket.common.client.EndpointCallback;

import jakarta.websocket.EndpointConfig;
import jakarta.websocket.RemoteEndpoint.Async;
import jakarta.websocket.Session;

public abstract class AsyncEndpointCallback extends EndpointCallback {
	@Override
	public void onOpen(Session session, EndpointConfig config) {
		super.onOpen(session, config);
		Async asyncRemote = session.getAsyncRemote();
		try {
			doAsync(asyncRemote);
		} catch (Exception f) {
			throw new RuntimeException(f);
		}
	}

	abstract void doAsync(Async async) throws Exception;
}
