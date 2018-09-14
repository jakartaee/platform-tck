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

package com.sun.ts.tests.webservices.handlerEjb.HandlerFlow;

import com.sun.ts.tests.jaxrpc.common.HandlerBase;
import com.sun.ts.tests.jaxrpc.common.HandlerTracker;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import javax.xml.soap.*;
import javax.xml.rpc.soap.*;
import javax.xml.rpc.handler.*;
import javax.xml.rpc.handler.soap.*;

public class ServerHandler2 extends HandlerBase {

  public boolean handleRequest(MessageContext context) {
    SOAPMessage msg = ((SOAPMessageContext) context).getMessage();
    SOAPEnvelope env = null;
    SOAPBody body = null;
    SOAPElement elem = null;
    Name elemName = null;
    try {
      env = msg.getSOAPPart().getEnvelope();
      body = env.getBody();
      Iterator it = body.getChildElements();
      elem = (SOAPElement) it.next();
      elemName = elem.getElementName();
      if ((elemName.getLocalName().equals("howdy"))
          && (env.getHeader() == null)) {
        Iterator it2 = elem.getChildElements();
        SOAPElement arg = (SOAPElement) it2.next();

        // Get the value of the text node
        Iterator it3 = arg.getChildElements();
        Text text = (Text) it3.next();
        String value = text.getValue() + " - Indeed";

        // Replace the text node
        text.detachNode();
        arg.addTextNode(value);
        msg.saveChanges();
      }
    } catch (Exception e) {
      System.out.println("*** Exception " + e.getMessage() + " absorbed");
      e.printStackTrace();
    }

    return super.handleRequest(context);
  }
}
