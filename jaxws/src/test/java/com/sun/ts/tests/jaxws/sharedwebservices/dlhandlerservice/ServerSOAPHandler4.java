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
 * $Id$
 */

package com.sun.ts.tests.jaxws.sharedwebservices.dlhandlerservice;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxws.common.*;

import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.ws.soap.SOAPFaultException;
import javax.xml.namespace.QName;
import jakarta.xml.soap.*;
import java.util.Iterator;

public class ServerSOAPHandler4 extends SOAPHandlerBase {
  private static final String WHICHHANDLERTYPE = "Server";

  private static final String HANDLERNAME = "ServerSOAPHandler4";

  private static final String NAMESPACEURI = "http://dlhandlerservice.org/wsdl";

  private final QName FAULTCODE = new QName(NAMESPACEURI, "ItsASoapFault",
      "tns");

  private static final String FAULTACTOR = "faultActor";

  private Name name = null;

  private jakarta.xml.soap.SOAPFault sf;

  public ServerSOAPHandler4() {
    super();
    super.setWhichHandlerType(WHICHHANDLERTYPE);
    super.setHandlerName(HANDLERNAME);
  }

  public boolean handleMessage(SOAPMessageContext context) {
    System.out.println("in " + this + ":handleMessage");
    TestUtil.logTrace("in " + this + ":handleMessage");
    Handler_Util.setTraceFlag(
        Handler_Util.getValueFromMsg(this, context, "harnesslogtraceflag"));

    Handler_Util.initTestUtil(this,
        Handler_Util.getValueFromMsg(this, context, "harnessloghost"),
        Handler_Util.getValueFromMsg(this, context, "harnesslogport"),
        Handler_Util.getValueFromMsg(this, context, "harnesslogtraceflag"));

    String direction = Handler_Util.getDirection(context);
    if (!Handler_Util.checkForMsg(this, context, "GetTrackerData")) {
      HandlerTracker.reportHandleMessage(this,
          Handler_Util.getDirection(context));
      if (direction.equals(Constants.INBOUND)) {
        if (Handler_Util.checkForMsg(this, context,
            "ServerSOAPInboundHandleMessageThrowsRuntimeExceptionTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an inbound RuntimeException");
          throw new RuntimeException(HANDLERNAME
              + ".handleMessage throwing an inbound RuntimeException");
        } else if (Handler_Util.checkForMsg(this, context,
            "ServerSOAPInboundHandleMessageFalseTest")) {
          SOAPMessage message = context.getMessage();
          try {
            SOAPBody body = message.getSOAPBody();
            SOAPElement origBodyParam = (SOAPElement) body.getChildElements()
                .next();
            System.out.println("-----------------------------");
            System.out.println("Original SOAP Message Request");
            System.out.println("-----------------------------");
            message.writeTo(System.out);
            System.out.println("\n");

            Iterator iterator = origBodyParam.getChildElements();
            origBodyParam.detachNode();

            QName origName = origBodyParam.getElementQName();

            SOAPElement newElement = body.addBodyElement(new QName(
                origName.getNamespaceURI(), "MyResult", origName.getPrefix()));
            while (iterator.hasNext()) {
              Object o = iterator.next();
              SOAPElement s = (SOAPElement) o;
              newElement.addChildElement(s);
            }

            System.out.println("-----------------------------");
            System.out.println("Modified SOAP Message Request");
            System.out.println("-----------------------------");
            message.writeTo(System.out);
            System.out.println("\n");
            message.saveChanges();

          } catch (Exception e) {
            HandlerTracker.reportThrowable(this, new Exception(
                "Unexpected error occurred in handleMessage for inbound ServerSOAPInboundHandleMessageFalseTest:"
                    + e));
          }
          return false;
        } else if (Handler_Util.checkForMsg(this, context,
            "ServerSOAPInboundHandleMessageThrowsSOAPFaultExceptionTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an inbound SOAPFaultException");
          String faultString = "ServerSOAPHandler4.handleMessage throwing an inbound SOAPFaultException";
          try {
            name = SOAPFactory.newInstance().createName("somefaultentry");
            sf = JAXWS_Util.createSOAPFault("soap11", FAULTCODE, FAULTACTOR,
                faultString, name);
          } catch (Exception e) {
            HandlerTracker.reportThrowable(this, new Exception(
                "Unexpected error occurred in handleMessage for an inbound message"
                    + e));
          }
          throw new SOAPFaultException(sf);
        }
      } else if (direction.equals(Constants.OUTBOUND)) {

        if (Handler_Util.checkForMsg(this, context,
            "ServerSOAPOutboundHandleMessageThrowsRuntimeExceptionTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an outbound RuntimeException");
          throw new RuntimeException(HANDLERNAME
              + ".handleMessage throwing an outbound RuntimeException");
        } else if (Handler_Util.checkForMsg(this, context,
            "ServerSOAPOutboundHandleMessageFalseTest")) {
          return false;
        } else if (Handler_Util.checkForMsg(this, context,
            "ServerSOAPOutboundHandleMessageThrowsSOAPFaultExceptionTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an outbound SOAPFaultException");
          String faultString = "ServerSOAPHandler4.handleMessage throwing an outbound SOAPFaultException";
          try {
            name = SOAPFactory.newInstance().createName("somefaultentry");
            sf = JAXWS_Util.createSOAPFault("soap11", FAULTCODE, FAULTACTOR,
                faultString, name);
          } catch (Exception e) {
            HandlerTracker.reportThrowable(this, new Exception(
                "Unexpected error occurred in handleMessage for an outbound message"
                    + e));
          }
          throw new SOAPFaultException(sf);
        }
      }
    }

    System.out.println("exiting " + this + ":handleMessage");
    TestUtil.logTrace("exiting " + this + ":handleMessage");
    return true;
  }

  public boolean handleFault(SOAPMessageContext context) {
    System.out.println("in " + this + ":handleFault");
    TestUtil.logTrace("in " + this + ":handleFault");
    HandlerTracker.reportHandleFault(this);
    String direction = Handler_Util.getDirection(context);
    if (direction.equals(Constants.OUTBOUND)) {
      if (Handler_Util.checkForMsg(this, context,
          "ServerSOAPHandler6.handleMessage throws SOAPFaultException for ServerSOAPInboundHandleFaultThrowsSOAPFaultExceptionTest")) {
        HandlerTracker.reportComment(this,
            "Throwing an outbound SOAPFaultException");
        String faultString = "ServerSOAPHandler4.handleFault throwing an outbound SOAPFaultException";
        try {
          name = SOAPFactory.newInstance().createName("somefaultentry");
          sf = JAXWS_Util.createSOAPFault("soap11", FAULTCODE, FAULTACTOR,
              faultString, name);
        } catch (Exception e) {
          HandlerTracker.reportThrowable(this, new Exception(
              "Unexpected error occurred in ServerSOAPHandler4.handleFault for an outbound message"
                  + e));
        }
        throw new SOAPFaultException(sf);
      } else if (Handler_Util.checkForMsg(this, context,
          "ServerSOAPHandler6.handleMessage throws SOAPFaultException for ServerSOAPInboundHandleFaultThrowsRuntimeExceptionTest")) {
        HandlerTracker.reportComment(this,
            "Throwing an outbound RuntimeException");
        throw new RuntimeException(
            HANDLERNAME + ".handleFault throwing an outbound RuntimeException");
      } else if (Handler_Util.checkForMsg(this, context,
          "ServerSOAPHandler5.handleMessage throws SOAPFaultException for ServerSOAPInboundHandleFaultFalseTest")) {
        return false;
      }
    }

    System.out.println("exiting " + this + ":handleFault");
    TestUtil.logTrace("exiting " + this + ":handleFault");
    return true;
  }

}
