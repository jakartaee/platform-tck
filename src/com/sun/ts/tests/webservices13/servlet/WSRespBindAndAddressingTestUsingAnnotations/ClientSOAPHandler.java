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
 * $Id: ClientSOAPHandler.java 52501 2009-04-15 02:29:49Z adf $
 */
package com.sun.ts.tests.webservices13.servlet.WSRespBindAndAddressingTestUsingAnnotations;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.wsi.constants.SOAPConstants;
import com.sun.ts.tests.jaxws.wsa.common.MapRequiredException;
import com.sun.ts.tests.jaxws.wsa.common.MapException;
import com.sun.ts.tests.jaxws.wsa.common.WsaBaseSOAPHandler;
import com.sun.ts.tests.jaxws.wsa.common.W3CAddressingConstants;
import com.sun.ts.tests.jaxws.wsa.common.ActionNotSupportedException;
import com.sun.ts.tests.jaxws.wsa.common.AddressingPropertyException;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import java.util.Iterator;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.Text;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

public class ClientSOAPHandler extends WsaBaseSOAPHandler {

  protected void checkInboundAction(SOAPMessageContext context, String oper,
      String action) {
    TestUtil.logMsg("ClientSOAPHandler.checkInboundAction: [operation=" + oper
        + ", input action=" + action + "]");
    if (Handler_Util.checkForMsg(context,
        "afCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabledTest")) {
      checkAddressingHeadersExist(context, action);
    } else if (Handler_Util.checkForMsg(context,
        "afCltEnabledNotREQSvrNotEnabledrbfSvrEnabledCltEnabledTest")) {
      checkAddressingHeadersDoNotExist(context, action);
    }
  }

  private void verifyAction(String action) {
    if (!action.equals(TestConstants.ECHO_OUTPUT_ACTION)) {
      throw new ActionNotSupportedException("Expected:"
          + TestConstants.ECHO_OUTPUT_ACTION + ", Actual:" + action);
    }
  }

  private void checkAddressingHeadersExist(SOAPMessageContext context,
      String action) {
    verifyAction(action);
    checkInboundToExist(context);
    checkInboundRelatesToExist(context);
  }

  private void checkAddressingHeadersDoNotExist(SOAPMessageContext context,
      String action) {
    checkActionDoesNotExist(action);
    checkInboundToDoesNotExist(context);
    checkInboundRelatesToDoesNotExist(context);
  }

  private void handleMessageInboundCheckAddressingHeadersDoNotExist(
      SOAPMessageContext context) {
    String headerValue = null;
    String whichHeaders = null;
    try {
      headerValue = getTo(context);
      whichHeaders = whichHeaders + "wsa:To, ";
    } catch (Exception e) {
    }
    try {
      headerValue = getReplyTo(context);
      whichHeaders = whichHeaders + "wsa:ReplyTo, ";
    } catch (Exception e) {
    }
    try {
      headerValue = getMessageId(context);
      whichHeaders = whichHeaders + "wsa:MessageId, ";
    } catch (Exception e) {
    }
    try {
      headerValue = getRelationship(context);
      whichHeaders = whichHeaders + "wsa:Relationship, ";
    } catch (Exception e) {
    }
    try {
      headerValue = getAction(context);
      whichHeaders = whichHeaders + "wsa:Action";
    } catch (Exception e) {
    }
    if (whichHeaders != null) {
      throw new AddressingPropertyException(
          "ERROR: The following addressing headers exist in soap message: ["
              + whichHeaders + "]");
    }
  }

  protected String getAction(SOAPMessageContext context) throws SOAPException {
    String testName = (String) context.get("test.name");
    TestUtil.logMsg("ClientSOAPHandler.getAction(): testName=" + testName);
    if (testName == null)
      return super.getAction(context);
    else if (testName
        .equals("afCltEnabledREQSvrEnabledREQrbfSvrEnabledCltEnabledTest"))
      return super.getAction(context);
    else if (testName
        .equals("afCltEnabledNotREQSvrNotEnabledrbfSvrEnabledCltEnabledTest"))
      return super.getActionDoesNotExist(context);
    else
      return null;
  }

  protected String whichHandler() {
    return "ClientSOAPHandler";
  }
}
