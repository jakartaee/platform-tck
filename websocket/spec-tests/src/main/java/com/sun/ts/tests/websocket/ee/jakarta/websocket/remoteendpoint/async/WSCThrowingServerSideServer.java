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

package com.sun.ts.tests.websocket.ee.jakarta.websocket.remoteendpoint.async;

import java.io.IOException;
import java.lang.System.Logger;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.sun.ts.tests.websocket.common.impl.WaitingSendHandler;
import com.sun.ts.tests.websocket.common.stringbean.StringBean;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextEncoder;
import com.sun.ts.tests.websocket.common.util.IOUtil;

import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.RemoteEndpoint.Async;
import jakarta.websocket.SendResult;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/throwing", encoders = { ThrowingBinaryCoder.class, ThrowingTextCoder.class,
		StringBeanTextEncoder.class, ThrowingStringBeanEncoder.class })
public class WSCThrowingServerSideServer {

	private static final Logger logger = System.getLogger(WSCThrowingServerSideServer.class.getName());

	static final String[] RESPONSE = { "OK", "FAIL" };

	@OnMessage
	public StringBean onMessage(String msg, Session session) {
		Async asyncRemote = session.getAsyncRemote();
		OPS op = OPS.valueOf(msg.toUpperCase());
		switch (op) {
		case SENDBINARYEXECUTIONEXCEPTION:
			msg = sendBinaryHasExecutionException(asyncRemote);
			break;
		case SENDOBJECTEXECUTIONEXCEPTION:
			msg = sendObjectHasExecutionException(asyncRemote);
			break;
		case SENDOBJECTHANDLEREXECUTIONEXCEPTION:
			msg = sendObjectWithSendHandlerHasExecutionException(asyncRemote);
			break;
		default:
			throw new IllegalArgumentException("Method " + msg + " not implemented");
		}
		return new StringBean(msg);
	}

	@OnError
	public void onError(Session session, Throwable t) throws IOException {
		logger.log(Logger.Level.INFO,"@OnError in " + getClass().getName());
		t.printStackTrace(); // Write to error log, too
		String message = "Exception: " + IOUtil.printStackTrace(t);
		session.getBasicRemote().sendText(message);
	}

	public static String sendBinaryHasExecutionException(Async asyncRemote) {
		Future<Void> future = asyncRemote
				.sendBinary(ByteBuffer.wrap(OPS.SENDBINARYEXECUTIONEXCEPTION.name().getBytes()));
		try {
			future.get();
			return RESPONSE[1];
		} catch (ExecutionException e) {
			return RESPONSE[0];
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static String sendObjectHasExecutionException(Async asyncRemote) {
		Future<Void> future = asyncRemote.sendObject(new ThrowingStringBean());
		try {
			future.get();
			return RESPONSE[1];
		} catch (ExecutionException e) {
			return RESPONSE[0];
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static String sendObjectWithSendHandlerHasExecutionException(Async asyncRemote) {
		WaitingSendHandler handler = new WaitingSendHandler();
		asyncRemote.sendObject(new ThrowingStringBean(), handler);
		SendResult result = handler.waitForResult(WSCServerSideServer.SECONDS);
		return !result.isOK() && result.getException() != null ? RESPONSE[0] : RESPONSE[1];
	}
}
