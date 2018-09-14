/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc_handler.GenericHandler;

import com.sun.ts.tests.jaxrpc.common.HandlerTracker;

import javax.xml.rpc.handler.*;
import javax.xml.namespace.QName;

public class ServerHandler1 extends GenericHandler {
  private QName[] headers = null;

  public boolean handleRequest(MessageContext context) {
    HandlerTracker.reportHandleRequest(this);
    return true;
  }

  public QName[] getHeaders() {
    HandlerTracker.reportGetHeaders(this);
    return headers;
  }
}
