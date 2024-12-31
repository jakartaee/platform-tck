/*
 * Copyright (c) 2014, 2020 Oracle and/or its affiliates and others.
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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.session11.server;

import java.io.IOException;
import java.lang.System.Logger;
import java.nio.ByteBuffer;

import com.sun.ts.tests.websocket.common.stringbean.StringBean;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanBinaryDecoder;
import com.sun.ts.tests.websocket.common.util.IOUtil;

import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/annotatedbinary", decoders = StringBeanBinaryDecoder.class)
public class WSCAnnotatedBinaryServerEndpoint {

	private static final Logger logger = System.getLogger(WSCAnnotatedBinaryServerEndpoint.class.getName());

	@OnOpen
	public void onOpen(Session session) {
		session.addMessageHandler(StringBean.class, new StringBeanMessageHandler(session));
	}

	@OnError
	public void onError(Session session, Throwable t) throws IOException {
		logger.log(Logger.Level.INFO,"@OnError in " + getClass().getName());
		t.printStackTrace(); // Write to error log, too
		String message = "Exception: " + IOUtil.printStackTrace(t);
		session.getBasicRemote().sendBinary(ByteBuffer.wrap(message.getBytes()));
	}
}
