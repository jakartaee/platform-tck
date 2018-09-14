/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxws.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.namespace.QName;
import java.util.Set;

public abstract class HTTPSOAPHandler
    implements SOAPHandler<SOAPMessageContext> {
  public boolean handleMessage(SOAPMessageContext context) {
    System.out.println("entering HTTPSOAPHandler:handleMessage");
    boolean outbound = (Boolean) context
        .get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

    if (outbound) {
      System.out.println("Direction=outbound");
      processOutboundMessage(context);
    } else {
      System.out.println("Direction=inbound");
      processInboundMessage(context);
    }

    System.out.println("exiting HTTPSOAPHandler:handleMessage");
    return true;
  }

  public void close(MessageContext context) {
  }

  public boolean handleFault(SOAPMessageContext context) {
    return true;
  }

  public Set<QName> getHeaders() {
    return null;
  }

  protected void processOutboundMessage(SOAPMessageContext context) {
  }

  protected void processInboundMessage(SOAPMessageContext context) {
  }
}
