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

package com.sun.ts.tests.websocket.ee.javax.websocket.remoteendpoint.usercoder;

import jakarta.websocket.EndpointConfig;

public abstract class CoderSuperClass {
  public static final String COMMON_CODED_STRING = "Anything that should have been coded has been coded into this";

  public static final Byte NUMERIC = 100;

  public static final char CHAR = '0';

  public static final boolean BOOL = false;

  public void init(EndpointConfig config) {
  }

  public void destroy() {
  }

}
