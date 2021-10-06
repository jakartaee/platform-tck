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
 * $Id: ServerSOAPHandler.java 52501 2007-01-24 02:29:49Z lschwenk $
 */
package com.sun.ts.tests.jaxws.wsa.w2j.document.literal.respectbindingfeature;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaxws.common.*;

import com.sun.ts.tests.jaxws.common.Handler_Util;
import com.sun.ts.tests.jaxws.wsa.common.WsaBaseSOAPHandler;
import com.sun.ts.tests.jaxws.wsa.common.ActionNotSupportedException;
import com.sun.ts.tests.jaxws.wsa.common.AddressingPropertyException;
import com.sun.ts.tests.jaxws.wsa.common.W3CAddressingConstants;
import com.sun.ts.tests.jaxws.wsa.common.MapException;
import com.sun.ts.tests.jaxws.wsa.common.MapRequiredException;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.wsaddressing.W3CEndpointReference;

import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPHeader;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.Text;
import javax.xml.namespace.QName;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Set;

public class ServerSOAPHandler extends WsaBaseSOAPHandler {
  protected void checkInboundAction(SOAPMessageContext context, String oper,
      String action) {
    TestUtil.logMsg("ServerSOAPHandler.checkInboundAction: [operation=" + oper
        + ", input action=" + action + "]");
    System.out.println("ServerSOAPHandler.checkInboundAction: [operation="
        + oper + ", input action=" + action + "]");
    // figure out which testname i'm checking for and call appropropriate
    // verify*(...)
    if (Handler_Util.checkForMsg(context,
        "afCltEnabledNotREQSvrEnabledREQrbfSvrEnabledCltEnabledTest")) {
      verifyCltEnabledNotREQSvrEnabledREQrbfSvrEnabledCltEnabled(context,
          action);
    } else if (Handler_Util.checkForMsg(context,
        "afCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabledTest")) {
      verifyCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabled(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "afCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabledTest")) {
      verifyCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabled(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "afCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabledTest")) {
      verifyCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabled(context, action);
    } else {
      if (TestConstants.ADD_NUMBERS_IN_ACTION_URI.equals(action)) {
        throw new ActionNotSupportedException("Expected:"
            + TestConstants.ADD_NUMBERS_IN_ACTION_URI + ", Actual:" + action);

      }
    }
  }

  private void verifyCltEnabledNotREQSvrEnabledREQrbfSvrEnabledCltEnabled(
      SOAPMessageContext context, String action) {
    // verify specific headers for
    // CltEnabledNotREQSvrEnabledREQrbfSvrEnabledCltEnabled here...
    checkAddressingHeadersExist(context, action);
  }

  private void verifyCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabled(
      SOAPMessageContext context, String action) {
    // verify specific headers for
    // CltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabled here...
    checkAddressingHeadersExist(context, action);
  }

  private void verifyCltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabled(
      SOAPMessageContext context, String action) {
    // verify specific headers for
    // CltNotEnabledSvrEnabledREQrbfSvrEnabledCltEnabled here...
    checkAddressingHeadersDoNotExist(context, action);
  }

  private void verifyCltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabled(
      SOAPMessageContext context, String action) {
    // verify specific headers for
    // CltEnabledREQSvrNotEnabledrbfSvrEnabledCltEnabled here...
    checkAddressingHeadersExist(context, action);
  }

  private void verifyAction(String action) {
    TestUtil.logMsg("ServerSOAPHandler.verifyAction: [action=" + action + "]");
    System.out
        .println("ServerSOAPHandler.verifyAction: [action=" + action + "]");
    if (!TestConstants.ADD_NUMBERS_IN_ACTION.equals(action)) {
      throw new ActionNotSupportedException("Expected:"
          + TestConstants.ADD_NUMBERS_IN_ACTION + ", Actual:" + action);
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

  protected String getAction(SOAPMessageContext context) throws SOAPException {
    try {
      return super.getAction(context);
    } catch (Exception e) {
      return null;
    }
  }

  protected String whichHandler() {
    return "ServerSOAPHandler";
  }
}
