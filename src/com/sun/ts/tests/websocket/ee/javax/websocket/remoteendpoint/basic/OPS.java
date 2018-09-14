/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.basic;

public enum OPS {
  POKE, SENDSTREAM, //
  SENDWRITER, //
  SENDBINARY, SENDBINARYPART1, SENDBINARYPART2, SENDBINARYPART3, SENDBINARYTHROWS, //
  SENDOBJECT, SENDOBJECTTHROWS, SENDOBJECTTHROWSENCODEEEXCEPTION, //
  SENDOBJECT_BYTE, SENDOBJECT_SHORT, SENDOBJECT_INT, SENDOBJECT_LONG, //
  SENDOBJECT_FLOAT, SENDOBJECT_DOUBLE, SENDOBJECT_BOOL, SENDOBJECT_CHAR, //
  SENDTEXT, SENDTEXTPART1, SENDTEXTPART2, SENDTEXTPART3, SENDTEXTTHROWS, //
  BATCHING_ALLOWED, SEND_PING, SEND_PONG, SEND_PING_THROWS, SEND_PONG_THROWS, //
  IDLE /* let ping make no idle timeout */, PING_4_TIMES, PONG_4_TIMES
}
