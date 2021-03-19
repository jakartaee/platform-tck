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

import com.sun.ts.tests.jaxws.common.LogicalHandlerBase;
import jakarta.xml.ws.handler.LogicalMessageContext;
import jakarta.xml.ws.soap.SOAPFaultException;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.namespace.QName;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.soap.Name;

public class ClientLogicalHandler6 extends LogicalHandlerBase {
  private static final String WHICHHANDLERTYPE = "Client";

  private static final String HANDLERNAME = "ClientLogicalHandler6";

  private static final String NAMESPACEURI = "http://dlhandlerservice.org/wsdl";

  private final QName FAULTCODE = new QName(NAMESPACEURI, "ItsASoapFault",
      "tns");

  private static final String FAULTACTOR = "faultActor";

  private Name name = null;

  private jakarta.xml.soap.SOAPFault sf;

  public ClientLogicalHandler6() {
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
            "ClientLogicalOutboundHandleFaultFalseTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an outbound SOAPFaultException");
          String faultString = "ClientLogicalHandler6.handleMessage throws SOAPFaultException for ClientLogicalOutboundHandleFaultFalseTest";
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
            "ClientLogicalOutboundHandleFaultThrowsSOAPFaultExceptionTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an outbound SOAPFaultException");
          String faultString = "ClientLogicalHandler6.handleMessage throws SOAPFaultException for ClientLogicalOutboundHandleFaultThrowsSOAPFaultExceptionTest";
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
            "ClientLogicalOutboundHandleFaultThrowsRuntimeExceptionTest")) {
          HandlerTracker.reportComment(this,
              "Throwing an outbound SOAPFaultException");
          String faultString = "ClientLogicalHandler6.handleMessage throws SOAPFaultException for ClientLogicalOutboundHandleFaultThrowsRuntimeExceptionTest";
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

    return true;
  }

  public boolean handleFault(LogicalMessageContext context) {
    System.out.println("in " + this + ":handleFault");
    TestUtil.logTrace("in " + this + ":handleFault");
    HandlerTracker.reportHandleFault(this);
    String direction = Handler_Util.getDirection(context);
    if (direction.equals(Constants.INBOUND)) {
      if (Handler_Util.checkForMsg(this, context,
          "ServerLogicalHandler6.handleMessage throws SOAPFaultException for ServerLogicalInboundHandlerThrowsSOAPFaultToClientHandlersTest")) {
        HandlerTracker.reportComment(this,
            "received SOAPFault from Inbound ServerLogicalHandler6");
        return false;
      } else if (Handler_Util.checkForMsg(this, context,
          "ServerLogicalHandler6.handleMessage throws SOAPFaultException for ServerLogicalOutboundHandlerThrowsSOAPFaultToClientHandlersTest")) {
        HandlerTracker.reportComment(this,
            "received SOAPFault from Outbound ServerLogicalHandler6");
        return false;
      }
    }

    System.out.println("exiting " + this + ":handleFault");
    TestUtil.logTrace("exiting " + this + ":handleFault");
    return true;
  }
}
