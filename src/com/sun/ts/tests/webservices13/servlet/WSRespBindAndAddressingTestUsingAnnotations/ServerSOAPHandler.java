/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: ServerSOAPHandler.java 52501 2009-04-15 02:29:49Z adf $
 */
package com.sun.ts.tests.webservices13.servlet.WSRespBindAndAddressingTestUsingAnnotations;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaxws.common.*;

import com.sun.ts.tests.jaxws.wsa.common.WsaBaseSOAPHandler;
import com.sun.ts.tests.jaxws.wsa.common.ActionNotSupportedException;
import com.sun.ts.tests.jaxws.wsa.common.AddressingPropertyException;
import com.sun.ts.tests.jaxws.wsa.common.W3CAddressingConstants;
import com.sun.ts.tests.jaxws.wsa.common.MapException;
import com.sun.ts.tests.jaxws.wsa.common.MapRequiredException;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.wsaddressing.W3CEndpointReference;
import javax.xml.soap.SOAPException;

public class ServerSOAPHandler extends WsaBaseSOAPHandler {
  protected void checkInboundAction(SOAPMessageContext context, String oper,
      String action) {
    TestUtil.logMsg("ServerSOAPHandler.checkInboundAction: [operation=" + oper
        + ", input action=" + action + "]");
    System.out.println("ServerSOAPHandler.checkInboundAction: [operation="
        + oper + ", input action=" + action + "]");
    if (Handler_Util.checkForMsg(context,
        "afCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabledTest")) {
      checkAddressingHeadersExist(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "afCltEnabledNotREQSvrNotEnabledrbfSvrEnabledCltEnabledTest")) {
      // checkAddressingHeadersExist(context, action); Don't check wsa headers
      // for addressing optional
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
    verifyAction(action);
    checkInboundToExist(context);
    checkInboundMessageIdExist(context);
    checkInboundReplyToExist(context);
  }

  private void checkAddressingHeadersDoNotExist(SOAPMessageContext context,
      String action) {
    checkActionDoesNotExist(action);
    checkInboundToDoesNotExist(context);
    checkInboundMessageIdDoesNotExist(context);
    checkInboundReplyToDoesNotExist(context);
  }

  protected String getAction(SOAPMessageContext context) throws SOAPException {
    String testName = (String) context.get("test.name");
    TestUtil.logMsg("ServerSOAPHandler.getAction(): testName=" + testName);
    System.out.println("ServerSOAPHandler.getAction(): testName=" + testName);
    if (testName == null)
      return super.getAction(context);
    else if (testName
        .equals("afCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabledTest")
        || testName.equals(
            "afCltEnabledNotREQSvrNotEnabledrbfSvrEnabledCltEnabledTest"))
      return super.getAction(context);
    else
      return null;
  }

  protected String whichHandler() {
    return "ServerSOAPHandler";
  }
}
