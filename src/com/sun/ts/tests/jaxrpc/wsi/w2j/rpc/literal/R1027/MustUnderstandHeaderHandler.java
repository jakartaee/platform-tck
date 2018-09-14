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
 * @(#)MustUnderstandHeaderHandler.java	1.2 03/05/16
 */

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R1027;

import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.namespace.QName;
import javax.xml.soap.*;

public class MustUnderstandHeaderHandler extends GenericHandler {

  public QName[] getHeaders() {
    return new QName[] {};
  }

  public boolean handleResponse(MessageContext context) {
    try {
      addMustUnderstandHeader((SOAPMessageContext) context);
    } catch (SOAPException se) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(se);
    }
    return true;
  }

  private void addMustUnderstandHeader(SOAPMessageContext context)
      throws SOAPException {
    SOAPHeader header = getHeader(
        context.getMessage().getSOAPPart().getEnvelope());
    SOAPHeaderElement mustUnderstand = header.addHeaderElement(getHeaderName());
    mustUnderstand.addTextNode("baz");
    mustUnderstand.setMustUnderstand(true);
    context.getMessage().saveChanges();
  }

  private SOAPHeader getHeader(SOAPEnvelope env) throws SOAPException {
    SOAPHeader header = env.getHeader();
    if (header == null) {
      return env.addHeader();
    } else {
      return header;
    }
  }

  private Name getHeaderName() throws SOAPException {
    SOAPFactory factory = SOAPFactory.newInstance();
    return factory.createName("foo", "bar", "http://baz.org");
  }
}
