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

package com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxws.common.*;

import jakarta.xml.ws.handler.LogicalMessageContext;
import jakarta.xml.ws.soap.SOAPFaultException;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.ProtocolException;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.namespace.QName;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.soap.Name;

import java.io.StringReader;
import javax.xml.transform.stream.StreamSource;

public class ClientLogicalHandler4 extends LogicalHandlerBase {
  private static final String WHICHHANDLERTYPE = "Client";

  private static final String HANDLERNAME = "ClientLogicalHandler4";

  private static final String NAMESPACEURI = "http://dlhandlerservice.org/wsdl";

  private final QName FAULTCODE = new QName(NAMESPACEURI, "ItsASoapFault",
      "tns");

  private static final String FAULTACTOR = "faultActor";

  private Name name = null;

  private jakarta.xml.soap.SOAPFault sf;

  public ClientLogicalHandler4() {
    super();
    super.setWhichHandlerType(WHICHHANDLERTYPE);
    super.setHandlerName(HANDLERNAME);
  }

  public boolean handleMessage(LogicalMessageContext context) {
    Handler_Util.setTraceFlag(
        Handler_Util.getValueFromMsg(this, context, "harnesslogtraceflag"));

    Handler_Util.initTestUtil(this,
        Handler_Util.getValueFromMsg(this, context, "harnessloghost"),
        Handler_Util.getValueFromMsg(this, context, "harnesslogport"),
        Handler_Util.getValueFromMsg(this, context, "harnesslogtraceflag"));

    System.out.println("in " + this + ":handleMessage");
    TestUtil.logTrace("in " + this + ":handleMessage");
    String direction = Handler_Util.getDirection(context);
    if (!Handler_Util.checkForMsg(this, context, "GetTrackerData")) {
      HandlerTracker.reportHandleMessage(this, direction);
      if (direction.equals(Constants.OUTBOUND)) {
        if (Handler_Util.checkForMsg(this, context,
            "ClientLogicalOutboundHandleMessageThrowsRuntimeExceptionTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an outbound RuntimeException");
          throw new RuntimeException(HANDLERNAME
              + ".handleMessage throwing an outbound RuntimeException");
        } else if (Handler_Util.checkForMsg(this, context,
            "ClientLogicalOutboundHandleMessageFalseTest")) {
          String tmp = Handler_Util.getMessageAsString(context);
          String response = tmp.replaceAll("MyAction", "MyResult");
          context.getMessage()
              .setPayload(new StreamSource(new StringReader(response)));
          return false;
        } else if (Handler_Util.checkForMsg(this, context,
            "ClientLogicalOutboundHandleMessageThrowsSOAPFaultTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an outbound SOAPFaultException");
          String faultString = "ClientLogicalHandler4.handleMessage throwing an outbound SOAPFaultException";
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
        } else if (Handler_Util.checkForMsg(this, context,
            "ClientLogicalOutboundHandleMessageThrowsWebServiceExceptionTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an outbound WebServiceException");
          throw new WebServiceException(HANDLERNAME
              + ".handleMessage throwing an outbound WebServiceException");
        } else if (Handler_Util.checkForMsg(this, context,
            "ClientLogicalOutboundHandleMessageThrowsProtocolExceptionTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an outbound ProtocolException");
          throw new ProtocolException(HANDLERNAME
              + ".handleMessage throwing an outbound ProtocolException");
        }
      } else {
        // inbound
        if (Handler_Util.checkForMsg(this, context,
            "ClientLogicalInboundHandleMessageThrowsRuntimeExceptionTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an inbound RuntimeException");
          throw new RuntimeException(HANDLERNAME
              + ".handleMessage throwing an inbound RuntimeException");

        } else if (Handler_Util.checkForMsg(this, context,
            "ClientLogicalInboundHandleMessageFalseTest")) {
          return false;
        } else if (Handler_Util.checkForMsg(this, context,
            "ClientLogicalInboundHandleMessageThrowsSOAPFaultTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an inbound SOAPFaultException");
          String faultString = "ClientLogicalHandler4.handleMessage throwing an inbound SOAPFaultException";
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
        } else if (Handler_Util.checkForMsg(this, context,
            "ClientLogicalInboundHandleMessageThrowsWebServiceExceptionTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an inbound WebServiceException");
          throw new WebServiceException(HANDLERNAME
              + ".handleMessage throwing an inbound WebServiceException");
        } else if (Handler_Util.checkForMsg(this, context,
            "ClientLogicalInboundHandleMessageThrowsProtocolExceptionTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an inbound ProtocolException");
          throw new ProtocolException(HANDLERNAME
              + ".handleMessage throwing an inbound ProtocolException");
        }
      }
    }
    return true;
  }

  public boolean handleFault(LogicalMessageContext context) {
    System.out.println("in " + this + ":handleFault");
    TestUtil.logTrace("in " + this + ":handleFault");
    HandlerTracker.reportHandleFault(this);
    String direction = Handler_Util.getDirection(context);
    if (direction.equals(Constants.INBOUND)) {
      if (Handler_Util.checkForMsg(this, context,
          "ClientLogicalHandler6.handleMessage throws SOAPFaultException for ClientLogicalOutboundHandleFaultFalseTest")) {
        return false;
      } else if (Handler_Util.checkForMsg(this, context,
          "ClientLogicalHandler6.handleMessage throws SOAPFaultException for ClientLogicalOutboundHandleFaultThrowsSOAPFaultExceptionTest")) {
        HandlerTracker.reportComment(this,
            "Throwing an inbound SOAPFaultException");
        String faultString = "ClientLogicalHandler4.handleFault throwing an inbound SOAPFaultException";
        try {
          name = SOAPFactory.newInstance().createName("somefaultentry");
          sf = JAXWS_Util.createSOAPFault("soap11", FAULTCODE, FAULTACTOR,
              faultString, name);
        } catch (Exception e) {
          HandlerTracker.reportThrowable(this, new Exception(
              "Unexpected error occurred in handleFault for an inbound message"
                  + e));
        }
        throw new SOAPFaultException(sf);
      } else if (Handler_Util.checkForMsg(this, context,
          "ClientLogicalOutboundHandleFaultThrowsRuntimeExceptionTest")) {
        HandlerTracker.reportComment(this,
            "Throwing an inbound RuntimeException");
        throw new RuntimeException(
            HANDLERNAME + ".handleFault throwing an inbound RuntimeException");
      }
    }
    return true;
  }

}
