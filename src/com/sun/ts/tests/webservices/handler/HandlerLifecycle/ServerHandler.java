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

package com.sun.ts.tests.webservices.handler.HandlerLifecycle;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxrpc.common.*;
import com.sun.javatest.Status;

import com.sun.ts.tests.jaxrpc.common.HandlerBase;
import com.sun.ts.tests.jaxrpc.common.HandlerTracker;
import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import javax.xml.soap.*;
import javax.xml.rpc.soap.*;
import javax.xml.rpc.handler.*;
import javax.xml.rpc.*;
import javax.xml.rpc.handler.soap.*;
import java.util.*;
import javax.xml.soap.*;
import javax.xml.rpc.handler.*;
import javax.xml.rpc.handler.soap.*;

public class ServerHandler extends HandlerBase {

  public boolean handleRequest(MessageContext context) {
    SOAPMessage msg = ((SOAPMessageContext) context).getMessage();
    SOAPEnvelope env = null;
    SOAPBody body = null;
    SOAPElement elem = null;
    SOAPElement elem2 = null;
    SOAPElement elem3 = null;
    Name elemName = null;
    Name elemName2 = null;

    try {
      preinvoke();
      env = msg.getSOAPPart().getEnvelope();
      body = env.getBody();
      Iterator it = body.getChildElements();
      while (it.hasNext()) {
        elem = (SOAPElement) it.next();
        elemName = elem.getElementName();
        Iterator it2 = ((SOAPElement) elem).getChildElements();
        while (it2.hasNext()) {
          elem2 = (SOAPElement) it2.next();
          elemName2 = elem2.getElementName();
          if (elemName.getLocalName().equals("enventry")) {
            boolean result = AnotherHandlerTracker.getAHT()
                .checkForFailedInstance(this);
            // Check to ensure this handler has not failed before.
            if (result) {
              elem2.detachNode();
              SOAPElement myelement = elem.addChildElement(elemName2);
              myelement.addTextNode("reusedHandler");
            } else if ((elem2.getValue() != null)
                && (elem2.getValue().equals("serverRequestFail"))) {
              AnotherHandlerTracker.getAHT().addFailedInstance(this);
              throw new JAXRPCException(
                  "Causing Handler Instance to be destroyed here...");
            }
          }
        }
      }
      msg.saveChanges();
    } catch (javax.xml.soap.SOAPException se) {
      se.printStackTrace();
    } finally {
      postinvoke();
    }

    return true;
  }

  public boolean handleResponse(MessageContext context) {

    String soapAction = null;
    Name opName = null;
    SOAPMessage msg = ((SOAPMessageContext) context).getMessage();
    SOAPEnvelope env = null;
    SOAPBody body = null;
    SOAPElement elem = null;
    SOAPElement elem2 = null;
    SOAPElement elem3 = null;
    Name elemName = null;
    Name elemName2 = null;

    try {
      preinvoke();
      env = msg.getSOAPPart().getEnvelope();
      body = env.getBody();
      Iterator it = body.getChildElements();
      while (it.hasNext()) {
        elem = (SOAPElement) it.next();
        // step over response element
        elemName = elem.getElementName();
        Iterator it2 = ((SOAPElement) elem).getChildElements();
        while (it2.hasNext()) {
          elem2 = (SOAPElement) it2.next();
          // find result part
          elemName2 = elem2.getElementName();
          if (elemName2.getLocalName().equals("result")) {
            boolean result = AnotherHandlerTracker.getAHT()
                .checkForFailedInstance(this);
            // Check to ensure this handler has not failed before.
            if (result) {
              elem2.detachNode();
              SOAPElement myelement = elem.addChildElement(elemName2);
              myelement.addTextNode("reusedHandler");
            } else if ((elem2.getValue() != null)
                && (elem2.getValue().equals("serverResponseFail"))) {
              AnotherHandlerTracker.getAHT().addFailedInstance(this);
              throw new JAXRPCException(
                  "Causing Handler Instance to be destroyed here...");
            }
          }
        }
      }
      msg.saveChanges();
    } catch (javax.xml.soap.SOAPException se) {
      se.printStackTrace();
    } finally {
      postinvoke();
    }

    return true;

  }
}
