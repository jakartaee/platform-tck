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

package com.sun.ts.tests.webservices12.ejb.annotations.HandlerTest.server;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import javax.xml.soap.*;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.*;
import javax.xml.ws.handler.*;
import javax.xml.ws.handler.soap.*;
import javax.xml.namespace.QName;

import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

import com.sun.ts.tests.jaxws.common.Handler_Util;
import com.sun.ts.tests.jaxws.common.JAXWS_Util;
import com.sun.ts.tests.jaxws.common.Constants;

public class SOAPHandler
    implements javax.xml.ws.handler.soap.SOAPHandler<SOAPMessageContext> {

  private final String HANDLER_NAME = "ServerSOAPHandler";

  public Set<QName> getHeaders() {
    return new HashSet<QName>();
  }

  public boolean handleMessage(SOAPMessageContext context) {
    System.out.println("in " + HANDLER_NAME + ":handleMessage");

    String direction = Handler_Util.getDirection(context);
    if (Handler_Util.checkForMsg(this, context, "transformBodyTest")) {
      transformBodyTest(context, direction);
    } else if (Handler_Util.checkForMsg(this, context, "transformHeaderTest")) {
      transformHeaderTest(context, direction);
    } else {
      System.out.println(
          "didn't find transformBodyTest message, handler will ignore");
    }
    System.out.println("exiting " + HANDLER_NAME + ":handleMessage");
    return true;
  }

  public void transformBodyTest(MessageContext context, String direction) {
    System.out.println("in " + HANDLER_NAME + ":transformBodyTest");
    try {
      System.out.println("direction=" + direction);
      SOAPMessage msg = ((SOAPMessageContext) context).getMessage();
      SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
      SOAPBody body = env.getBody();
      Iterator it = body.getChildElements();
      while (it.hasNext()) {
        SOAPElement elem = (SOAPElement) it.next();
        Name elemName = elem.getElementName();
        Iterator it2 = ((SOAPElement) elem).getChildElements();
        while (it2.hasNext()) {
          SOAPElement elem2 = (SOAPElement) it2.next();
          String value = elem2.getValue();
          if (value.indexOf("transformBodyTest") >= 0) {
            value = value + direction + HANDLER_NAME;
            elem2.setValue(value);
          }
        }
      }
      msg.saveChanges();
      Handler_Util.dumpMsg(context);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      e.printStackTrace();
    }
    System.out.println("exiting " + HANDLER_NAME + ":transformBodyTest");
  }

  public void transformHeaderTest(MessageContext context, String direction) {
    System.out.println("in " + HANDLER_NAME + ":transformHeaderTest");
    try {
      System.out.println("direction=" + direction);
      System.out.println("transformHeaderTest:BEFORE");
      Handler_Util.dumpMsg(context);
      SOAPMessage msg = ((SOAPMessageContext) context).getMessage();
      SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
      if (env.getHeader() == null) {
        System.out.println("ERROR: NO HEADER EXISTS");
      } else {
        System.out.println("Changing the existing soap header");
        SOAPHeader sh = env.getHeader();
        Iterator it = sh.examineAllHeaderElements();
        while (it.hasNext()) {
          SOAPElement elem = (SOAPElement) it.next();
          Iterator it2 = ((SOAPElement) elem).getChildElements();
          while (it2.hasNext()) {
            SOAPElement elem2 = (SOAPElement) it2.next();
            String value = elem2.getValue();
            if (value.indexOf("theTransformHeader") >= 0) {
              value = value + direction + HANDLER_NAME;
              elem2.setValue(value);
            }
          }
        }
      }
      msg.saveChanges();
      Handler_Util.dumpMsg(context);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      e.printStackTrace();
    }
    System.out.println("exiting " + HANDLER_NAME + ":doHandlerTest3");
  }

  public void close(MessageContext context) {
    System.out.println("in " + HANDLER_NAME + ":close");
  }

  public boolean handleFault(SOAPMessageContext context) {
    System.out.println("in " + HANDLER_NAME + ":handleFault");
    return true;
  }

}
