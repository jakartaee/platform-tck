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

package com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.onewayhandlertest.client;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import com.sun.ts.tests.jaxws.common.Constants;
import com.sun.ts.tests.jaxws.common.JAXWS_Util;
import com.sun.ts.tests.jaxws.common.LogicalHandlerBase2;
import com.sun.ts.tests.jaxws.common.Handler_Util;
import com.sun.ts.tests.jaxws.common.HandlerTracker;
import javax.xml.namespace.QName;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.ws.soap.SOAPFaultException;
import jakarta.xml.soap.SOAPFault;
import jakarta.xml.soap.Name;
import jakarta.xml.ws.handler.LogicalMessageContext;

public class ClientLogicalHandler2 extends LogicalHandlerBase2 {
  private static final String WHICHHANDLERTYPE = "Client";

  private static final String HANDLERNAME = "ClientLogicalHandler2";

  private static final String NAMESPACEURI = "http://rlowhandlertestservice.org/wsdl";

  private static final QName FAULTCODE = new QName(NAMESPACEURI,
      "ItsASoapFault", "tns");

  private static final String FAULTACTOR = "faultActor";

  private Name name = null;

  private SOAPFault sf;

  public ClientLogicalHandler2() {
    super();
    super.setWhichHandlerType(WHICHHANDLERTYPE);
    super.setHandlerName(HANDLERNAME);
  }

  public boolean handleMessage(LogicalMessageContext context) {
    System.out.println("in " + this + ":handleMessage");
    TestUtil.logTrace("in " + this + ":handleMessage");
    try {
      preinvoke();
      Handler_Util.setTraceFlag(
          Handler_Util.getValueFromMsg(this, context, "harnesslogtraceflag"));

      Handler_Util.initTestUtil(this,
          Handler_Util.getValueFromMsg(this, context, "harnessloghost"),
          Handler_Util.getValueFromMsg(this, context, "harnesslogport"),
          Handler_Util.getValueFromMsg(this, context, "harnesslogtraceflag"));

      if (!Handler_Util.checkForMsg(this, context, "GetTrackerData")) {
        String direction = Handler_Util.getDirection(context);
        HandlerTracker.reportHandleMessage(this, direction);
        if (direction.equals(Constants.OUTBOUND)) {
          if (Handler_Util.checkForMsg(this, context,
              "ClientLogicalOutboundHandleMessageThrowsSOAPFaultTest")) {
            HandlerTracker.reportComment(this,
                "Throwing an outbound SOAPFaultException");
            String faultString = "ClientLogicalHandler2.handleMessage throwing an outbound SOAPFaultException";
            try {
              name = SOAPFactory.newInstance().createName("somefaultentry");
              sf = JAXWS_Util.createSOAPFault("soap11", FAULTCODE, FAULTACTOR,
                  faultString, name);
            } catch (Exception e) {
              HandlerTracker.reportThrowable(this, new Exception(
                  "Unexpected error in handleMessage for an outbound message"
                      + e));
            }
            throw new SOAPFaultException(sf);
          } else if (Handler_Util.checkForMsg(this, context,
              "ClientLogicalOutboundHandleMessageReturnsFalseTest")) {
            HandlerTracker.reportComment(this, "HandleMessage returns false");
            return false;
          }
        }
      } else {
        TestUtil.logTrace("found GetTrackerData message, handler will ignore");
      }
    } finally {
      postinvoke();
    }
    System.out.println("exiting " + this + ":handleMessage");
    TestUtil.logTrace("exiting " + this + ":handleMessage");
    return true;
  }

}
