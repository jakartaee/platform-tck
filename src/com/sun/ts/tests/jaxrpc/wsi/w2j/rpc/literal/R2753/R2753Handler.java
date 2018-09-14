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

/*
 * @(#)R2753Handler.java	1.3 03/06/06
 */

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R2753;

import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.namespace.QName;
import javax.xml.soap.*;

public class R2753Handler extends GenericHandler {
  public QName[] getHeaders() {
    return new QName[] { new QName("http://extra-header.org", "extra-header") };
  }

  public boolean handleRequest(MessageContext context) {
    handle(context);
    return true;
  }

  public boolean handleResponse(MessageContext context) {
    handle(context);
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
