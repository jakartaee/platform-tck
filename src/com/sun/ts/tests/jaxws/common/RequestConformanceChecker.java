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

import com.sun.ts.tests.jaxws.wsi.constants.WSIConstants;

import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;

import com.sun.ts.tests.jaxws.common.Handler_Util;

public abstract class RequestConformanceChecker
    implements SOAPHandler<SOAPMessageContext>, WSIConstants {

  // this is not threadsafe
  protected String response;

  public Set<QName> getHeaders() {
    return new HashSet<QName>();
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

  public abstract void test(SOAPMessageContext context) throws SOAPException;

  public boolean handleMessage(SOAPMessageContext context) {
    System.out
        .println("HANDLER: RequestConformanceChecker.handleMessage() BEGIN");
    try {
      if (Handler_Util.getDirection(context).equals(Constants.INBOUND)) {
        if (isRequestTest((context))) {
          System.out.println(
              "HANDLER: RequestConformanceChecker.handleMessage() HANDLING REQUEST");
          test(context);
        }
      } else {
        if (response != null) {
          System.out.println(
              "HANDLER: RequestConformanceChecker.handleMessage() HANDLING RESPONSE");
          setResponse(context);
          response = null;
        }
      }
    } catch (SOAPException se) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(se);
    }
    System.out
        .println("HANDLER: RequestConformanceChecker.handleMessage() END");
    return true;
  }

  private void setResponse(SOAPMessageContext context) throws SOAPException {
    SOAPBody body = context.getMessage().getSOAPPart().getEnvelope().getBody();
    SOAPElement responseElement = getResponseElement(body);
    Iterator children = responseElement.getChildElements();
    ((Text) children.next()).detachNode();
    responseElement.addTextNode(response);
    context.getMessage().saveChanges();
  }

  private SOAPElement getResponseElement(SOAPBody body) {
    return getResponseElement((SOAPElement) body.getChildElements().next());
  }

  private SOAPElement getResponseElement(SOAPElement elem) {
    if (elem.getChildElements().next() instanceof Text) {
      return elem;
    } else {
      return getResponseElement((SOAPElement) elem.getChildElements().next());
    }
  }

  private boolean isRequestTest(SOAPMessageContext context)
      throws SOAPException {
    SOAPHeader header = context.getMessage().getSOAPPart().getEnvelope()
        .getHeader();
    if (header != null) {
      Iterator headers = header
          .examineHeaderElements("http://conformance-checker.org");
      SOAPHeaderElement headerElement;
      while (headers.hasNext()) {
        headerElement = (SOAPHeaderElement) headers.next();
        if (headerElement.getElementName().getLocalName().equals("test")) {
          if (headerElement.getValue().equals("request")) {
            return true;
          } else if (headerElement.getValue().equals("response")) {
            return false;
          }
        }
      }
    }
    return true;
  }
}
