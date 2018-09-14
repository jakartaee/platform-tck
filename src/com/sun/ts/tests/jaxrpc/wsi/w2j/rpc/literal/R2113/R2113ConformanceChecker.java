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

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R2113;

import com.sun.ts.tests.jaxrpc.common.RequestConformanceChecker;

import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.*;
import java.util.Iterator;

public class R2113ConformanceChecker extends RequestConformanceChecker {

  public void test(SOAPMessageContext context) throws SOAPException {
    test(context.getMessage().getSOAPPart().getEnvelope());
  }

  private void test(SOAPElement elem) {
    boolean fails = false;
    fails = hasSoapEncArrayTypeAttr(elem);
    if (!fails) {
      Iterator children = elem.getChildElements();
      while (children.hasNext()) {
        Node n = (Node) children.next();
        if (n instanceof SOAPElement) {
          test((SOAPElement) n);
        }
      }
    } else {
      response = "failed. Cannot use soapenc:arrayType attribute.";
    }
  }

  private boolean hasSoapEncArrayTypeAttr(SOAPElement elem) {
    Iterator attrs = elem.getAllAttributes();
    Name name;
    String uri;
    while (attrs.hasNext()) {
      name = (Name) attrs.next();
      uri = name.getURI();
      if (uri == null) {
        uri = "";
      }
      if (name.getLocalName().equals(SOAP_ENC_ARRAY_TYPE)
          && uri.equals(SOAP_ENV_NS)) {
        return true;
      }
    }
    return false;
  }
}
