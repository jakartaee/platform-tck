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

package com.sun.ts.tests.webservices12.ejb.annotations.WSEjbSOAPHandlersTest2;

import com.sun.ts.tests.jaxws.common.HandlerTracker;
import com.sun.ts.tests.jaxws.common.Handler_Util;

import javax.xml.ws.handler.*;
import javax.xml.ws.handler.soap.*;
import javax.xml.namespace.QName;

import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Iterator;

public class ServerHandler2 implements SOAPHandler<SOAPMessageContext> {
  public void init(java.util.Map<String, Object> config) {
  }

  public boolean handleFault(SOAPMessageContext context) {
    HandlerTracker.reportHandleFault(this);
    return true;
  }

  public void close(MessageContext context) {
    HandlerTracker.reportClose(this);
  }

  public boolean handleMessage(SOAPMessageContext context) {
    HandlerTracker.reportHandleMessage(this,
        Handler_Util.getDirection(context));
    return true;
  }

  public Set<QName> getHeaders() {
    HandlerTracker.reportGetHeaders(this);
    return new HashSet<QName>();
  }
}
