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
 * $Id: ClientSOAPHandler.java 52769 2007-02-21 19:46:02Z af70133 $
 */

package com.sun.ts.tests.jaxws.wsa.j2w.document.literal.requestresponse;

import com.sun.ts.lib.util.*;

import com.sun.ts.tests.jaxws.wsa.common.WsaBaseSOAPHandler;
import com.sun.ts.tests.jaxws.wsa.common.W3CAddressingConstants;
import com.sun.ts.tests.jaxws.wsa.common.AddressingHeaderException;
import com.sun.ts.tests.jaxws.wsa.common.AddressingPropertyException;
import com.sun.ts.tests.jaxws.wsa.common.ActionNotSupportedException;
import com.sun.ts.tests.jaxws.common.Handler_Util;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.MessageContext.Scope;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.soap.*;
import java.util.*;

public class ClientSOAPHandler extends WsaBaseSOAPHandler {
  String testName = null;

  protected void checkInboundAction(SOAPMessageContext context, String oper,
      String action) {
    TestUtil.logMsg("ClientSOAPHandler.checkInboundAction: [operation=" + oper
        + ", input action=" + action + "]");
    if (Handler_Util.checkForMsg(context, "testDefaultRequestResponseAction")) {
      checkAddressingHeadersExist(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "testExplicitRequestResponseAction")) {
      checkAddressingHeadersExist(context, action);
    }
  }

  private void verifyAction(String action) {
    TestUtil.logMsg("ClientSOAPHandler.verifyAction: [action=" + action + "]");
    if (testName.equals("testDefaultRequestResponseAction")) {
      if (!action.equals(TestConstants.ADD_NUMBERS_OUT_ACTION)) {
        throw new ActionNotSupportedException("Expected:"
            + TestConstants.ADD_NUMBERS_OUT_ACTION + ", Actual:" + action);
      }
    } else if (testName.equals("testExplicitRequestResponseAction")) {
      if (!action.equals(TestConstants.ADD_NUMBERS2_OUT_ACTION)) {
        throw new ActionNotSupportedException("Expected:"
            + TestConstants.ADD_NUMBERS2_OUT_ACTION + ", Actual:" + action);
      }
    }
  }

  private void checkAddressingHeadersExist(SOAPMessageContext context,
      String action) {
    TestUtil.logMsg("ClientSOAPHandler.checkAddressingHeadersExist");
    verifyAction(action);
    checkInboundToExist(context);
    checkInboundRelatesToExist(context);
  }

  protected void processOutboundMessage(SOAPMessageContext context, String oper,
      String testName) {
    if (testName != null && testName.equals("actionMismatch")) {
      TestUtil.logMsg("ClientSOAPHandler.processOutboundMessage: operation="
          + oper + ", testName=" + testName);
      Map<String, List<String>> headers = (Map<String, List<String>>) context
          .get(MessageContext.HTTP_REQUEST_HEADERS);
      if (headers == null)
        headers = new Hashtable<String, List<String>>();
      List<String> values = new Vector<String>();
      values.add("ActionNotSupported");
      headers.put("Soapaction", values);
      context.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
      context.setScope(MessageContext.HTTP_REQUEST_HEADERS, Scope.APPLICATION);
    }
  }

  protected String getAction(SOAPMessageContext context) throws SOAPException {
    testName = (String) context.get("test.name");
    TestUtil.logMsg("ClientSOAPHandler.getAction(): testName=" + testName);
    if (testName == null)
      return super.getAction(context);
    else if (!testName.equals("missingActionHeader"))
      return super.getAction(context);
    else
      return null;
  }

  protected String whichHandler() {
    return "ClientSOAPHandler";
  }
}
