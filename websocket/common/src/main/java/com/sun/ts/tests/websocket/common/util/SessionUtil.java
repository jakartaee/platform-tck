/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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
package com.sun.ts.tests.websocket.common.util;

import java.util.concurrent.TimeUnit;

import jakarta.websocket.Session;

public class SessionUtil {

	private SessionUtil() {
	}

	public static void waitUntilClosed(Session session, long timeout, TimeUnit unit) {
		long timeoutMillis = unit.toMillis(timeout);
		while (session.isOpen() && timeoutMillis > 0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// Clear the interrupt flag
				Thread.interrupted();
				// Exit the loop
				break;
			}
			timeoutMillis -= 100;
		}
	}
}
