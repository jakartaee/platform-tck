/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.webservices13.servlet.WSAddressingFeaturesTestUsingAnnotations;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaxws.common.*;

import com.sun.ts.tests.jaxws.wsa.common.WsaBaseSOAPHandler;
import com.sun.ts.tests.jaxws.wsa.common.ActionNotSupportedException;
import com.sun.ts.tests.jaxws.wsa.common.AddressingPropertyException;
import com.sun.ts.tests.jaxws.wsa.common.W3CAddressingConstants;
import com.sun.ts.tests.jaxws.wsa.common.MapException;
import com.sun.ts.tests.jaxws.wsa.common.MapRequiredException;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.wsaddressing.W3CEndpointReference;
import jakarta.xml.soap.SOAPException;

public class ServerSOAPHandler extends WsaBaseSOAPHandler {
  protected void checkInboundAction(SOAPMessageContext context, String oper,
      String action) {
    TestUtil.logMsg("ServerSOAPHandler.checkInboundAction: [operation=" + oper
        + ", input action=" + action + "]");
    System.out.println("ServerSOAPHandler.checkInboundAction: [operation="
        + oper + ", input action=" + action + "]");
    if (Handler_Util.checkForMsg(context,
        "VerifyAddrHeadersExistForRequiredEchoPort")) {
      checkAddressingHeadersExist(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "VerifyAddrHeadersDoNotExistForDisabledEchoPort")) {
      checkAddressingHeadersDoNotExist(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "VerifyAddrHeadersDoNotExistForDisabledEcho2Port")) {
      checkAddressingHeadersDoNotExist(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "VerifyAddrHeadersMayExistForEnabledEchoPort")) {
      checkAddressingHeadersMayExist(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "VerifyAddrHeadersMayExistForEnabledEcho2Port")) {
      checkAddressingHeadersMayExist(context, action);
    }
  }

  private void verifyAction(String action) {
    if (!action.equals(TestConstants.ECHO_INPUT_ACTION)) {
      throw new ActionNotSupportedException(
          "Expected:" + TestConstants.ECHO_INPUT_ACTION + ", Actual:" + action);
    }
  }

  private void checkAddressingHeadersExist(SOAPMessageContext context,
      String action) {
    TestUtil.logMsg("ServerSOAPHandler.checkAddressingHeadersExist");
    System.out.println("ServerSOAPHandler.checkAddressingHeadersExist");
    verifyAction(action);
    checkInboundToExist(context);
    checkInboundMessageIdExist(context);
    checkInboundReplyToExist(context);
  }

  private void checkAddressingHeadersDoNotExist(SOAPMessageContext context,
      String action) {
    TestUtil.logMsg("ServerSOAPHandler.checkAddressingHeadersDoNotExist");
    System.out.println("ServerSOAPHandler.checkAddressingHeadersDoNotExist");
    checkActionDoesNotExist(action);
    checkInboundToDoesNotExist(context);
    checkInboundMessageIdDoesNotExist(context);
    checkInboundReplyToDoesNotExist(context);
  }

  private void checkAddressingHeadersMayExist(SOAPMessageContext context,
      String action) {
    TestUtil.logMsg("ServerSOAPHandler.checkAddressingHeadersMayExist");
    System.out.println("ServerSOAPHandler.checkAddressingHeadersMayExist");
    // If Addressing headers exist then check them otherwise don't
    if (action != null) {
      verifyAction(action);
      checkInboundToExist(context);
      checkInboundMessageIdExist(context);
      checkInboundReplyToExist(context);
    }
  }

  protected String getAction(SOAPMessageContext context) throws SOAPException {
    String testName = (String) context.get("test.name");
    TestUtil.logMsg("ServerSOAPHandler.getAction(): testName=" + testName);
    System.out.println("ServerSOAPHandler.getAction(): testName=" + testName);
    if (testName == null) {
      return super.getAction(context);
    } else if (testName.equals("VerifyAddrHeadersExistForRequiredEchoPort")) {
      return super.getAction(context);
    } else if (testName.equals("VerifyAddrHeadersDoNotExistForDisabledEchoPort")
        || testName.equals("VerifyAddrHeadersDoNotExistForDisabledEcho2Port")) {
      return super.getActionDoesNotExist(context);
    } else if (testName.equals("VerifyAddrHeadersMayExistForEnabledEchoPort")
        || testName.equals("VerifyAddrHeadersMayExistForEnabledEcho2Port")) {
      try {
        return super.getAction(context);
      } catch (Exception e) {
        return null;
      }
    } else
      return null;
  }

  protected String whichHandler() {
    return "ServerSOAPHandler";
  }
}
