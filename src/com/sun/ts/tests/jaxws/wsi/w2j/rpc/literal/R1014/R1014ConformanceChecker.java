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
 * @(#)R1014ConformanceChecker.java	1.4 03/11/24
 */

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R1014;

import com.sun.ts.tests.jaxws.common.RequestConformanceChecker;

import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.soap.*;
import java.util.Iterator;

public class R1014ConformanceChecker extends RequestConformanceChecker {

  public void test(SOAPMessageContext context) throws SOAPException {
    SOAPBody body = getBody(context);
    Iterator children = body.getChildElements();
    SOAPElement element;
    String uri;
    while (children.hasNext() && response == null) {
      element = (SOAPElement) children.next();
      uri = element.getElementName().getURI();
      if (uri == null || uri.equals("")) {
        response = "failed. Children of soap:Body must be qualified";
      }
    }
  }

  private SOAPBody getBody(SOAPMessageContext context) throws SOAPException {
    return context.getMessage().getSOAPPart().getEnvelope().getBody();
  }
}
