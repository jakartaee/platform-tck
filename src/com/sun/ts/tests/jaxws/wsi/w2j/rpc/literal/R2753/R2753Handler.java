/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2753;

import com.sun.ts.tests.jaxws.common.Handler_Util;
import com.sun.ts.tests.jaxws.common.Constants;

import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.ws.handler.MessageContext;
import javax.xml.namespace.QName;
import jakarta.xml.soap.*;

import java.util.Set;
import java.util.HashSet;

public class R2753Handler implements SOAPHandler<SOAPMessageContext> {

  public Set<QName> getHeaders() {
    Set<QName> s = new HashSet<QName>();
    s.add(new QName("http://extra-header.org", "extra-header"));
    return s;
  }

  public void init(java.util.Map<String, Object> config) {
  };

  public boolean handleFault(SOAPMessageContext context) {
    return true;
  };

  public void destroy() {
  };

  public void close(MessageContext context) {
  };

  public boolean handleMessage(SOAPMessageContext context) {
    System.out.println("HANDLER: R2739Handler.handleMessage() BEGIN");
    handle(context);
    System.out.println("HANDLER: R2739Handler.handleMessage() END");
    return true;
  }

  private void handle(MessageContext context) {
    try {
      addExtraHeader((SOAPMessageContext) context);
    } catch (SOAPException se) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(se);
    }
  }

  private void addExtraHeader(SOAPMessageContext context) throws SOAPException {
    SOAPEnvelope env = context.getMessage().getSOAPPart().getEnvelope();
    if (env.getHeader() == null) {
      SOAPHeader header = env.addHeader();
      SOAPHeaderElement extraHeader = header
          .addHeaderElement(getExtraHeaderName(env));
      extraHeader.setActor("extra-header-actor");
      extraHeader.setMustUnderstand(true);
      context.getMessage().saveChanges();
    }
  }

  private Name getExtraHeaderName(SOAPEnvelope env) throws SOAPException {
    return env.createName("extra-header", "ns1", "http://extra-header.org");
  }
}
