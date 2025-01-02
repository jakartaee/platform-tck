/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * $Id$
 */
package com.sun.ts.tests.jms.core20.appclient.jmscontexttopictests;

import java.lang.System.Logger;

import jakarta.jms.CompletionListener;
import jakarta.jms.Message;

public class MyCompletionListener2 implements CompletionListener {

	private String name = null;

	private Message message = null;

	private Exception exception = null;

	private int numMessages = 1;

	boolean complete = false;

	private static final Logger logger = (Logger) System.getLogger(MyCompletionListener2.class.getName());

	public MyCompletionListener2() {
		this("MyCompletionListener2");
	}

	public MyCompletionListener2(String name) {
		this.name = name;
	}

	// getters/setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public void onCompletion(Message message) {
		logger.log(Logger.Level.INFO, "onCompletion(): Got Message: " + message);
		this.message = message;
		complete = true;
	}

	public void onException(Message message, Exception exception) {
		logger.log(Logger.Level.INFO, "onException(): Got Exception: " + exception);
		logger.log(Logger.Level.INFO, "With Message: " + message);
		this.exception = exception;
		this.message = message;
		complete = true;
	}

}
