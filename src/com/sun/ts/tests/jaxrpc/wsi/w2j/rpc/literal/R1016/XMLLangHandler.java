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

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R1016;

import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.*;
import javax.xml.namespace.QName;
import java.util.Iterator;

public class XMLLangHandler extends GenericHandler {

  public QName[] getHeaders() {
    return new QName[] {};
  }

  public boolean handleResponse(MessageContext context) {
    try {
      addXMLLangAttribute((SOAPMessageContext) context);
    } catch (SOAPException se) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(se);
    }
    return true;
  }

  private void addXMLLangAttribute(SOAPMessageContext context)
      throws SOAPException {

    Iterator children = context.getMessage().getSOAPPart().getEnvelope()
        .getBody().getFault().getChildElements();
    SOAPElement child;
    while (children.hasNext()) {
      child = (SOAPElement) children.next();
      if (child.getElementName().getLocalName().equals("faultstring")) {
        child.addAttribute(getXMLLangName(SOAPFactory.newInstance()), "en");
      }
    }
    context.getMessage().saveChanges();
  }

  private Name getXMLLangName(SOAPFactory factory) throws SOAPException {
    return factory.createName("lang", "xml", "");
  }
}
