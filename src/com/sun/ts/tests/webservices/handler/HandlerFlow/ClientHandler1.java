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

package com.sun.ts.tests.webservices.handler.HandlerFlow;

import com.sun.ts.tests.jaxrpc.common.HandlerBase;
import com.sun.ts.tests.jaxrpc.common.HandlerTracker;

import javax.xml.soap.*;
import javax.xml.rpc.soap.*;
import javax.xml.rpc.handler.*;
import javax.xml.rpc.handler.soap.*;
import java.util.*;
import javax.naming.InitialContext;

public class ClientHandler1 extends HandlerBase {

  private static final String on_property = "handler.context.property";

  private static final String on_value = "SharedMessageContext";

  public boolean handleRequest(MessageContext context) {

    Name opName = null;
    boolean called_hello = false;
    boolean called_howdy = false;
    boolean called_enventry = false;

    SOAPMessage msg = ((SOAPMessageContext) context).getMessage();
    SOAPEnvelope env = null;
    SOAPBody body = null;
    SOAPElement elem = null;
    SOAPElement elem2 = null;
    Name elemName = null;
    Name elemName2 = null;
    try {
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
          if (elemName.getLocalName().equals("hello"))
            called_hello = true;
          if (elemName.getLocalName().equals("howdy"))
            called_howdy = true;
          if (elemName.getLocalName().equals("hi"))
            context.setProperty(on_property, on_value);
          if (elemName.getLocalName().equals("enventry"))
            called_enventry = true;
        }
      }
    } catch (Exception e) {
      System.out.println("*** Exception " + e.getMessage() + " absorbed");
      e.printStackTrace();
    }
    if ((called_hello) || (called_howdy)) {
      try {
        env = msg.getSOAPPart().getEnvelope();
        if (env.getHeader() == null) {
          Name name = env.createName("GetDogsName", "SHADOW",
              "http://www.shadow.org/puppyname");
          SOAPHeader sh = env.addHeader();
          SOAPHeaderElement shElement = sh.addHeaderElement(name);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    if (called_enventry) {

      try {
        env = msg.getSOAPPart().getEnvelope();
        body = env.getBody();
        Iterator it = body.getChildElements();
        elem = (SOAPElement) it.next();
        elemName = elem.getElementName();
        if (elemName.getLocalName().equals("enventry")) {
          try {
            InitialContext ic = new InitialContext();
            String st = (String) ic.lookup("java:comp/env/jsr109/entry1");
            Integer i = (Integer) ic.lookup("java:comp/env/jsr109/entry2");
            Iterator it2 = elem.getChildElements();
            SOAPElement arg = (SOAPElement) it2.next();

            // Get the value of the text node
            Iterator it3 = arg.getChildElements();
            Text text = (Text) it3.next();
            String value = text.getValue() + ":" + st + ":" + String.valueOf(i);

            // Replace the text node
            text.detachNode();
            arg.addTextNode(value);

            System.out.println("*** ClientHandler1.handleRequest, old text="
                + text.getValue());
            System.out.println(
                "*** ClientHandler1.handleRequest, new text=" + arg.getValue());
            System.out
                .println("*** ClientHandler1.handleRequest, found ENV entries "
                    + i.intValue() + " and " + st);

          } catch (Exception t) {
            t.printStackTrace();
            System.out.println("*** Exception " + t.getMessage() + " absorbed");
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("*** Exception " + e.getMessage() + " absorbed");
      }
    }
    try {
      msg.saveChanges();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return super.handleRequest(context);
  }
}
